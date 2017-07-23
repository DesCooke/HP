package com.example.des.hp.TipGroup;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.TextView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.myutils.*;
import com.example.des.hp.R;

import java.io.InputStream;

public class TipGroupDetailsEdit extends AppCompatActivity {

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
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
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
        cbPicturePicked.setChecked(false);
        imageViewSmall.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.imagemissing));
    }

    public void btnClearImage(View view)
    {
        clearImage(view);
        tipGroupItem.pictureChanged = true;
        tipGroupItem.pictureAssigned = false;
    }

    public void saveTipGroup(View view)
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


    public void TipGroupDescriptionPicked(View view)
    {
        tipGroupDescription.setText(dialogWithEditTextFragment.getFinalText());

        dialogWithEditTextFragment.dismiss();
    }

    // Create a YES onclick procedure
    public void pickTipGroupDescription(View view)
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

    public void TipGroupNotesPicked(View view)
    {
        txtTipGroupNotes.setText(dialogWithMultiEditTextFragment.getFinalText());

        dialogWithMultiEditTextFragment.dismiss();
    }

    // Create a YES onclick procedure
    public void pickTipGroupNotes(View view)
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

    private void ShowError(String argFunction, String argMessage)
    {
        myMessages.ShowError
                (
                        "Error in TipGroupDetailsEdit::" + argFunction,
                        argMessage
                );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

/*
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.daydetailsformmenu, menu);
        return true;
    }
*/
}
