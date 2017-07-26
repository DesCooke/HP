package com.example.des.hp.AttractionArea;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.graphics.BitmapFactory;
import android.widget.TextView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.myutils.*;
import com.example.des.hp.R;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
import static com.example.des.hp.myutils.MyMessages.myMessages;

public class AttractionAreaDetailsEdit extends BaseActivity
{
    
    private final int SELECT_PHOTO = 1;
    private ImageView imageViewSmall;
    private String action;
    public int holidayId;
    public int attractionId;
    public int attractionAreaId;
    public TextView attractionAreaDescription;
    public ActionBar actionBar;
    public AttractionAreaItem attractionAreaItem;
    public CheckBox cbPicturePicked;
    private ImageUtils imageUtils;
    public View.OnClickListener dwetOnOkClick;
    public DialogWithEditTextFragment dialogWithEditTextFragment;
    public DialogWithMultiEditTextFragment dialogWithMultiEditTextFragment;
    public TextView txtAttractionAreaNotes;
    
    public void pickImage(View view)
    {
        try
        {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, SELECT_PHOTO);
        }
        catch (Exception e)
        {
            ShowError("pickImage", e.getMessage());
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent)
    {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        try
        {
            switch (requestCode)
            {
                case SELECT_PHOTO:
                    if (resultCode == RESULT_OK)
                    {
                        try
                        {
                            MyBitmap myBitmap = new MyBitmap();
                            Boolean lRetCode =
                                imageUtils.ScaleBitmapFromUrl
                                    (
                                        imageReturnedIntent.getData(),
                                        getContentResolver(),
                                        myBitmap
                                    );
                            if (!lRetCode)
                                return;
                            
                            // assign new bitmap and set scale type
                            imageViewSmall.setImageBitmap(myBitmap.Value);
                            
                            cbPicturePicked.setChecked(true);
                            
                            attractionAreaItem.pictureChanged = true;
                            
                            
                        }
                        catch (Exception e)
                        {
                            ShowError("onActivityResult-selectPhoto", e.getMessage());
                        }
                    }
                    break;
                
            }
        }
        catch (Exception e)
        {
            ShowError("onActivityResult", e.getMessage());
        }
    }
    
    public void clearImage(View view)
    {
        try
        {
            cbPicturePicked.setChecked(false);
            imageViewSmall.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.imagemissing));
        }
        catch (Exception e)
        {
            ShowError("clearImage", e.getMessage());
        }
    }
    
    public void btnClearImage(View view)
    {
        try
        {
            clearImage(view);
            attractionAreaItem.pictureChanged = true;
            attractionAreaItem.pictureAssigned = false;
        }
        catch (Exception e)
        {
            ShowError("btnClearImage", e.getMessage());
        }
    }
    
    public void saveAttractionArea(View view)
    {
        MyInt retInt = new MyInt();
        try
        {
            myMessages().ShowMessageShort("Saving " + attractionAreaDescription.getText().toString());
            
            attractionAreaItem.pictureAssigned = cbPicturePicked.isChecked();
            attractionAreaItem.attractionAreaDescription = attractionAreaDescription.getText().toString();
            attractionAreaItem.fileBitmap = null;
            if (attractionAreaItem.pictureAssigned)
                attractionAreaItem.fileBitmap = ((BitmapDrawable) imageViewSmall.getDrawable()).getBitmap();
            
            attractionAreaItem.attractionAreaNotes = txtAttractionAreaNotes.getText().toString();
            
            if (action.equals("add"))
            {
                attractionAreaItem.holidayId = holidayId;
                attractionAreaItem.attractionId = attractionId;
                if (!databaseAccess().getNextAttractionAreaId(holidayId, attractionId, retInt))
                    return;
                attractionAreaItem.attractionAreaId = retInt.Value;
                if (!databaseAccess().getNextAttractionAreaSequenceNo(holidayId, attractionId, retInt))
                    return;
                attractionAreaItem.sequenceNo = retInt.Value;
                if (!databaseAccess().addAttractionAreaItem(attractionAreaItem))
                    return;
            }
            
            if (action.equals("modify"))
            {
                attractionAreaItem.holidayId = holidayId;
                attractionAreaItem.attractionId = attractionId;
                attractionAreaItem.attractionAreaId = attractionAreaId;
                if (!databaseAccess().updateAttractionAreaItem(attractionAreaItem))
                    return;
            }
            
            finish();
        }
        catch (Exception e)
        {
            ShowError("saveAttractionArea", e.getMessage());
        }
    }
    
    
    public void AttractionAreaDescriptionPicked(View view)
    {
        try
        {
            attractionAreaDescription.setText(dialogWithEditTextFragment.getFinalText());
            
            dialogWithEditTextFragment.dismiss();
        }
        catch (Exception e)
        {
            ShowError("AttractionAreaDescriptionPicked", e.getMessage());
        }
        
    }
    
    // Create a YES onclick procedure
    public void pickAttractionAreaDescription(View view)
    {
        try
        {
            dwetOnOkClick = new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    AttractionAreaDescriptionPicked(view);
                }
            };
            
            
            dialogWithEditTextFragment =
                DialogWithEditTextFragment.newInstance
                    (
                        getFragmentManager(),     // for the transaction bit
                        "hihi",            // unique name for this dialog type
                        "Attraction Area",    // form caption
                        "Description",             // form message
                        R.drawable.attachment,
                        attractionAreaDescription.getText().toString(),                // initial text
                        dwetOnOkClick,
                        this,
                        false
                    );
            
            dialogWithEditTextFragment.showIt();
        }
        catch (Exception e)
        {
            ShowError("pickAttractionAreaDescription", e.getMessage());
        }
        
    }
    
    public void AttractionAreaNotesPicked(View view)
    {
        try
        {
            txtAttractionAreaNotes.setText(dialogWithMultiEditTextFragment.getFinalText());
            
            dialogWithMultiEditTextFragment.dismiss();
        }
        catch (Exception e)
        {
            ShowError("AttractionAreaNotesPicked", e.getMessage());
        }
        
    }
    
    // Create a YES onclick procedure
    public void pickAttractionAreaNotes(View view)
    {
        try
        {
            dwetOnOkClick = new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    AttractionAreaNotesPicked(view);
                }
            };
            
            
            dialogWithMultiEditTextFragment =
                DialogWithMultiEditTextFragment.newInstance
                    (
                        getFragmentManager(),     // for the transaction bit
                        "hihi",            // unique name for this dialog type
                        "TIP Group Notes",    // form caption
                        "Notes",             // form message
                        R.drawable.attachment,
                        txtAttractionAreaNotes.getText().toString(),                // initial text
                        dwetOnOkClick,
                        this
                    );
            
            
            dialogWithMultiEditTextFragment.showIt();
        }
        catch (Exception e)
        {
            ShowError("pickAttractionAreaNotes", e.getMessage());
        }
    }
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_attractionarea_details_edit);
        
        try
        {
            actionBar = getSupportActionBar();
            imageUtils = new ImageUtils(this);
            
            cbPicturePicked = (CheckBox) findViewById(R.id.picturePicked);
            imageViewSmall = (ImageView) findViewById(R.id.imageViewSmall);
            attractionAreaDescription = (TextView) findViewById(R.id.txtAttractionAreaDescription);
            txtAttractionAreaNotes = (TextView) findViewById(R.id.txtAttractionAreaNotes);
            
            clearImage(null);
            
            Bundle extras = getIntent().getExtras();
            if (extras != null)
            {
                String title = extras.getString("TITLE");
                String subtitle = extras.getString("SUBTITLE");
                actionBar.setTitle(title);
                action = extras.getString("ACTION");
                if (action != null && action.equals("add"))
                {
                    attractionAreaItem = new AttractionAreaItem();
                    holidayId = extras.getInt("HOLIDAYID");
                    attractionId = extras.getInt("ATTRACTIONID");
                    attractionAreaDescription.setText("");
                    cbPicturePicked.setChecked(false);
                    actionBar.setSubtitle("Add a Attraction Area");
                    txtAttractionAreaNotes.setText("");
                }
                if (action != null && action.equals("modify"))
                {
                    holidayId = extras.getInt("HOLIDAYID");
                    attractionId = extras.getInt("ATTRACTIONID");
                    attractionAreaId = extras.getInt("ATTRACTIONAREAID");
                    attractionAreaItem = new AttractionAreaItem();
                    if (!databaseAccess().getAttractionAreaItem(holidayId, attractionId, attractionAreaId, attractionAreaItem))
                        return;
                    
                    attractionAreaDescription.setText(attractionAreaItem.attractionAreaDescription);
                    
                    if (!imageUtils.getPageHeaderImage(this, attractionAreaItem.attractionAreaPicture, imageViewSmall))
                        return;
                    
                    cbPicturePicked.setChecked(attractionAreaItem.pictureAssigned);
                    
                    actionBar.setSubtitle(subtitle);
                    
                    txtAttractionAreaNotes.setText(String.valueOf(attractionAreaItem.attractionAreaNotes));
                }
            }
        }
        catch (Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
        
    }
}
