package com.example.des.hp.Attraction;

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

public class AttractionDetailsEdit extends BaseActivity
{
    
    public DatabaseAccess databaseAccess;
    private final int SELECT_PHOTO = 1;
    private ImageView imageViewSmall;
    private String action;
    public int holidayId;
    public int attractionId;
    public TextView attractionDescription;
    public ActionBar actionBar;
    public AttractionItem attractionItem;
    public CheckBox cbPicturePicked;
    private ImageUtils imageUtils;
    public View.OnClickListener dwetOnOkClick;
    public DialogWithEditTextFragment dialogWithEditTextFragment;
    public DialogWithMultiEditTextFragment dialogWithMultiEditTextFragment;
    public TextView txtAttractionNotes;
    public MyMessages myMessages;
    
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
                            
                            attractionItem.pictureChanged = true;
                            
                        }
                        catch (Exception e)
                        {
                            ShowError("onActivityResult-SelectPhoto", e.getMessage());
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
            attractionItem.pictureChanged = true;
            attractionItem.pictureAssigned = false;
        }
        catch (Exception e)
        {
            ShowError("btnClearImage", e.getMessage());
        }
    }
    
    public void saveAttraction(View view)
    {
        try
        {
            MyInt retInt = new MyInt();
            
            myMessages.ShowMessageShort("Saving " + attractionDescription.getText().toString());
            
            attractionItem.pictureAssigned = cbPicturePicked.isChecked();
            attractionItem.attractionDescription = attractionDescription.getText().toString();
            attractionItem.fileBitmap = null;
            if (attractionItem.pictureAssigned)
                attractionItem.fileBitmap = ((BitmapDrawable) imageViewSmall.getDrawable()).getBitmap();
            
            attractionItem.attractionNotes = txtAttractionNotes.getText().toString();
            
            if (action.equals("add"))
            {
                attractionItem.holidayId = holidayId;
                if (!databaseAccess.getNextAttractionId(holidayId, retInt))
                    return;
                attractionItem.attractionId = retInt.Value;
                if (!databaseAccess.getNextAttractionSequenceNo(holidayId, retInt))
                    return;
                attractionItem.sequenceNo = retInt.Value;
                if (!databaseAccess.addAttractionItem(attractionItem))
                    return;
            }
            
            if (action.equals("modify"))
            {
                attractionItem.holidayId = holidayId;
                attractionItem.attractionId = attractionId;
                if (!databaseAccess.updateAttractionItem(attractionItem))
                    return;
            }
            
            finish();
        }
        catch (Exception e)
        {
            ShowError("saveAttraction", e.getMessage());
        }
    }
    
    
    public void AttractionDescriptionPicked(View view)
    {
        try
        {
            attractionDescription.setText(dialogWithEditTextFragment.getFinalText());
            
            dialogWithEditTextFragment.dismiss();
        }
        catch (Exception e)
        {
            ShowError("AttractionDescriptionPicked", e.getMessage());
        }
    }
    
    // Create a YES onclick procedure
    public void pickAttractionDescription(View view)
    {
        try
        {
            dwetOnOkClick = new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    AttractionDescriptionPicked(view);
                }
            };
            
            
            dialogWithEditTextFragment =
                DialogWithEditTextFragment.newInstance
                    (
                        getFragmentManager(),     // for the transaction bit
                        "hihi",            // unique name for this dialog type
                        "Attraction",    // form caption
                        "Description",             // form message
                        R.drawable.attachment,
                        attractionDescription.getText().toString(),                // initial text
                        dwetOnOkClick,
                        this,
                        false
                    );
            
            dialogWithEditTextFragment.showIt();
        }
        catch (Exception e)
        {
            ShowError("pickAttractionDescription", e.getMessage());
        }
        
    }
    
    public void AttractionNotesPicked(View view)
    {
        try
        {
            txtAttractionNotes.setText(dialogWithMultiEditTextFragment.getFinalText());
            
            dialogWithMultiEditTextFragment.dismiss();
        }
        catch (Exception e)
        {
            ShowError("AttractionNotesPicked", e.getMessage());
        }
    }
    
    // Create a YES onclick procedure
    public void pickAttractionNotes(View view)
    {
        try
        {
            dwetOnOkClick = new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    AttractionNotesPicked(view);
                }
            };
            
            
            dialogWithMultiEditTextFragment =
                DialogWithMultiEditTextFragment.newInstance
                    (
                        getFragmentManager(),     // for the transaction bit
                        "hihi",            // unique name for this dialog type
                        "Attraction Notes",    // form caption
                        "Notes",             // form message
                        R.drawable.attachment,
                        txtAttractionNotes.getText().toString(),                // initial text
                        dwetOnOkClick,
                        this
                    );
            
            
            dialogWithMultiEditTextFragment.showIt();
        }
        catch (Exception e)
        {
            ShowError("pickAttractionNotes", e.getMessage());
        }
        
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        try
        {
            setContentView(R.layout.activity_attraction_details_edit);
            
            databaseAccess = new DatabaseAccess(this);
            actionBar = getSupportActionBar();
            imageUtils = new ImageUtils(this);
            myMessages = new MyMessages(this);
            
            cbPicturePicked = (CheckBox) findViewById(R.id.picturePicked);
            imageViewSmall = (ImageView) findViewById(R.id.imageViewSmall);
            attractionDescription = (TextView) findViewById(R.id.txtAttractionDescription);
            txtAttractionNotes = (TextView) findViewById(R.id.txtAttractionNotes);
            
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
                    attractionItem = new AttractionItem();
                    holidayId = extras.getInt("HOLIDAYID");
                    attractionDescription.setText("");
                    cbPicturePicked.setChecked(false);
                    actionBar.setSubtitle("Add an Attraction");
                    txtAttractionNotes.setText("");
                }
                if (action != null && action.equals("modify"))
                {
                    holidayId = extras.getInt("HOLIDAYID");
                    attractionId = extras.getInt("ATTRACTIONID");
                    attractionItem = new AttractionItem();
                    if (!databaseAccess.getAttractionItem(holidayId, attractionId, attractionItem))
                        return;
                    
                    attractionDescription.setText(attractionItem.attractionDescription);
                    
                    if (attractionItem.attractionPicture.length() > 0)
                        if (!imageUtils.getPageHeaderImage(this, attractionItem.attractionPicture, imageViewSmall))
                            return;
                    
                    cbPicturePicked.setChecked(attractionItem.pictureAssigned);
                    
                    actionBar.setSubtitle(subtitle);
                    
                    txtAttractionNotes.setText(String.valueOf(attractionItem.attractionNotes));
                }
            }
        }
        catch (Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
        
    }
}
