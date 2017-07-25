package com.example.des.hp.TipGroup;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBar;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.TextView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.myutils.*;
import com.example.des.hp.R;

import java.io.InputStream;

public class TipGroupDetailsEdit extends BaseActivity
{

    public DatabaseAccess databaseAccess;
    private final int SELECT_PHOTO = 1;
    private ImageView imageViewSmall;
    private String action;
    public int holidayId;
    public int tipGroupId;
    public TextView tipGroupDescription;
    public ActionBar actionBar;
    public TipGroupItem tipGroupItem;
    public CheckBox cbPicturePicked;
    private ImageUtils imageUtils;
    public View.OnClickListener dwetOnOkClick;
    public DialogWithEditTextFragment dialogWithEditTextFragment;
    public DialogWithMultiEditTextFragment dialogWithMultiEditTextFragment;
    public TextView txtTipGroupNotes;
    public MyMessages myMessages;

    public void pickImage(View view)
    {
        try
        {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
        }
        catch(Exception e)
        {
            ShowError("pickImage", e.getMessage());
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
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
                            if(lRetCode==false)
                                return;

                            // assign new bitmap and set scale type
                            imageViewSmall.setImageBitmap(myBitmap.Value);

                            cbPicturePicked.setChecked(true);

                            tipGroupItem.pictureChanged = true;


                        } catch (Exception e)
                        {
                            ShowError("onActivityResult-selectphoto", e.getMessage());
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
        catch(Exception e)
        {
            ShowError("clearImage", e.getMessage());
        }

    }

    public void btnClearImage(View view)
    {
        try
        {
        clearImage(view);
        tipGroupItem.pictureChanged = true;
        tipGroupItem.pictureAssigned = false;
        }
        catch(Exception e)
        {
            ShowError("btnClearImage", e.getMessage());
        }

    }

    public void saveTipGroup(View view)
    {
        try
        {
        myMessages.ShowMessageShort("Saving " + tipGroupDescription.getText().toString());

        tipGroupItem.pictureAssigned = cbPicturePicked.isChecked();
        tipGroupItem.tipGroupDescription = tipGroupDescription.getText().toString();
        tipGroupItem.fileBitmap = null;
        if (tipGroupItem.pictureAssigned)
            tipGroupItem.fileBitmap=((BitmapDrawable)imageViewSmall.getDrawable()).getBitmap() ;

        tipGroupItem.tipGroupNotes = txtTipGroupNotes.getText().toString();

        if(action.equals("add"))
        {
            MyInt myInt = new MyInt();

            tipGroupItem.holidayId = holidayId;

            if(!databaseAccess.getNextTipGroupId(holidayId, myInt))
                return;
            tipGroupItem.tipGroupId = myInt.Value;

            if(!databaseAccess.getNextTipGroupSequenceNo(holidayId, myInt))
                return;

            tipGroupItem.sequenceNo = myInt.Value;

            if(!databaseAccess.addTipGroupItem(tipGroupItem))
                return;
        }

        if(action.equals("modify"))
        {
            tipGroupItem.holidayId = holidayId;
            tipGroupItem.tipGroupId = tipGroupId;
            if(!databaseAccess.updateTipGroupItem(tipGroupItem))
                return;
        }

        finish();
        }
        catch(Exception e)
        {
            ShowError("saveTipGroup", e.getMessage());
        }

    }


    public void TipGroupDescriptionPicked(View view)
    {
        try
        {
        tipGroupDescription.setText(dialogWithEditTextFragment.getFinalText());

        dialogWithEditTextFragment.dismiss();
        }
        catch(Exception e)
        {
            ShowError("TipGroupDescriptionPicked", e.getMessage());
        }

    }

    // Create a YES onclick procedure
    public void pickTipGroupDescription(View view)
    {
        try
        {
        dwetOnOkClick = new View.OnClickListener()
        {
            public void onClick(View view)
            {
                TipGroupDescriptionPicked(view);
            }
        };


        dialogWithEditTextFragment =
                DialogWithEditTextFragment.newInstance
                        (
                                getFragmentManager(),     // for the transaction bit
                                "hihi",            // unique name for this dialog type
                                "Tip Group" ,    // form caption
                                "Description",             // form message
                                R.drawable.attachment,
                                tipGroupDescription.getText().toString(),                // initial text
                                dwetOnOkClick,
                                this,
                                false
                        );

        dialogWithEditTextFragment.showIt();
        }
        catch(Exception e)
        {
            ShowError("pickTipGroupDescription", e.getMessage());
        }

    }

    public void TipGroupNotesPicked(View view)
    {
        try
        {
        txtTipGroupNotes.setText(dialogWithMultiEditTextFragment.getFinalText());

        dialogWithMultiEditTextFragment.dismiss();
        }
        catch(Exception e)
        {
            ShowError("TipGroupNotesPicked", e.getMessage());
        }

    }

    // Create a YES onclick procedure
    public void pickTipGroupNotes(View view)

    {
        try
        {
        dwetOnOkClick = new View.OnClickListener()
        {
            public void onClick(View view)
            {
                TipGroupNotesPicked(view);
            }
        };


        dialogWithMultiEditTextFragment =
                DialogWithMultiEditTextFragment.newInstance
                        (
                                getFragmentManager(),     // for the transaction bit
                                "hjhj",            // unique name for this dialog type
                                "TIP Group Notes" ,    // form caption
                                "Notes",             // form message
                                R.drawable.attachment,
                                txtTipGroupNotes.getText().toString(),                // initial text
                                dwetOnOkClick,
                                this
                        );


        dialogWithMultiEditTextFragment.showIt();
        }
        catch(Exception e)
        {
            ShowError("pickTipGroupNotes", e.getMessage());
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try
        {
        setContentView(R.layout.activity_tipgroup_details_edit);

        databaseAccess = new DatabaseAccess(this);
        actionBar = getSupportActionBar();
        imageUtils = new ImageUtils(this);
        myMessages = new MyMessages(this);

        cbPicturePicked=(CheckBox)findViewById(R.id.picturePicked);
        imageViewSmall = (ImageView)findViewById(R.id.imageViewSmall);
        tipGroupDescription =(TextView)findViewById(R.id.txtTipGroupDescription);
        txtTipGroupNotes = (TextView) findViewById(R.id.txtTipGroupNotes);

        clearImage(null);

        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            String title = extras.getString("TITLE");
            String subtitle = extras.getString("SUBTITLE");
            actionBar.setTitle(title);
            action = extras.getString("ACTION");
            if(action!=null && action.equals("add"))
            {
                tipGroupItem = new TipGroupItem();
                holidayId = extras.getInt("HOLIDAYID");
                tipGroupDescription.setText("");
                cbPicturePicked.setChecked(false);
                actionBar.setSubtitle("Add a Tip");
                txtTipGroupNotes.setText("");
            }
            if(action!=null && action.equals("modify"))
            {
                holidayId = extras.getInt("HOLIDAYID");
                tipGroupId = extras.getInt("TIPGROUPID");
                tipGroupItem = new TipGroupItem();
                if(!databaseAccess.getTipGroupItem(holidayId, tipGroupId, tipGroupItem))
                    return;

                tipGroupDescription.setText(tipGroupItem.tipGroupDescription);

                if(imageUtils.getPageHeaderImage(this, tipGroupItem.tipGroupPicture, imageViewSmall)==false)
                    return;

                cbPicturePicked.setChecked(tipGroupItem.pictureAssigned);

                actionBar.setSubtitle(subtitle);

                txtTipGroupNotes.setText(String.valueOf(tipGroupItem.tipGroupNotes));
            }
        }
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }

    }

}
