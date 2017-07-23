package com.example.des.hp.Tip;

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

public class TipDetailsEdit extends AppCompatActivity {

    public DatabaseAccess databaseAccess;
    private final int SELECT_PHOTO = 1;
    private ImageView imageViewSmall;
    private String action;
    public int holidayId;
    public int tipGroupId;
    public int tipId;
    public TextView tipDescription;
    public ActionBar actionBar;
    public TipItem tipItem;
    public CheckBox cbPicturePicked;
    private ImageUtils imageUtils;
    public View.OnClickListener dwetOnOkClick;
    public DialogWithEditTextFragment dialogWithEditTextFragment;
    public DialogWithMultiEditTextFragment dialogWithMultiEditTextFragment;
    public TextView txtTipNotes;
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

                            tipItem.pictureChanged = true;


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
        tipItem.pictureChanged = true;
        tipItem.pictureAssigned = false;
    }

    public void saveTip(View view)
    {
        myMessages.ShowMessageShort("Saving " + tipDescription.getText().toString());

        tipItem.pictureAssigned = cbPicturePicked.isChecked();
        tipItem.tipDescription = tipDescription.getText().toString();
        tipItem.fileBitmap = null;
        if (tipItem.pictureAssigned)
            tipItem.fileBitmap=((BitmapDrawable)imageViewSmall.getDrawable()).getBitmap() ;

        tipItem.tipNotes = txtTipNotes.getText().toString();

        if(action.equals("add"))
        {
            MyInt myInt = new MyInt();

            tipItem.holidayId = holidayId;
            tipItem.tipGroupId= tipGroupId;

            if(!databaseAccess.getNextTipId(holidayId, tipGroupId, myInt))
                return;
            tipItem.tipId = myInt.Value;

            if(!databaseAccess.getNextTipSequenceNo(holidayId, tipGroupId, myInt))
                return;
            tipItem.sequenceNo = myInt.Value;

            if(!databaseAccess.addTipItem(tipItem))
                return;
        }

        if(action.equals("modify"))
        {
            tipItem.holidayId = holidayId;
            tipItem.tipGroupId= tipGroupId;
            tipItem.tipId = tipId;
            if(!databaseAccess.updateTipItem(tipItem))
                return;
        }

        finish();
    }


    public void TipDescriptionPicked(View view)
    {
        tipDescription.setText(dialogWithEditTextFragment.getFinalText());

        dialogWithEditTextFragment.dismiss();
    }

    // Create a YES onclick procedure
    public void pickTipDescription(View view)
    {
        dwetOnOkClick = new View.OnClickListener()
        {
            public void onClick(View view)
            {
                TipDescriptionPicked(view);
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
                                tipDescription.getText().toString(),                // initial text
                                dwetOnOkClick,
                                this,
                                false
                        );

        dialogWithEditTextFragment.showIt();
    }

    public void TipNotesPicked(View view)
    {
        txtTipNotes.setText(dialogWithMultiEditTextFragment.getFinalText());

        dialogWithMultiEditTextFragment.dismiss();
    }

    // Create a YES onclick procedure
    public void pickTipNotes(View view)
    {
        dwetOnOkClick = new View.OnClickListener()
        {
            public void onClick(View view)
            {
                TipNotesPicked(view);
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
                                txtTipNotes.getText().toString(),                // initial text
                                dwetOnOkClick,
                                this
                        );


        dialogWithMultiEditTextFragment.showIt();
    }

    private void ShowError(String argFunction, String argMessage)
    {
        myMessages.ShowError
                (
                        "Error in TipDetailsEdit::" + argFunction,
                        argMessage
                );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tip_details_edit);

        databaseAccess = new DatabaseAccess(this);
        actionBar = getSupportActionBar();
        imageUtils = new ImageUtils(this);
        myMessages = new MyMessages(this);

        cbPicturePicked=(CheckBox)findViewById(R.id.picturePicked);
        imageViewSmall = (ImageView)findViewById(R.id.imageViewSmall);
        tipDescription =(TextView)findViewById(R.id.txtTipDescription);
        txtTipNotes = (TextView) findViewById(R.id.txtTipNotes);

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
                tipItem = new TipItem();
                holidayId = extras.getInt("HOLIDAYID");
                tipGroupId = extras.getInt("TIPGROUPID");
                tipDescription.setText("");
                cbPicturePicked.setChecked(false);
                actionBar.setSubtitle("Add a Tip");
                txtTipNotes.setText("");
            }
            if(action!=null && action.equals("modify"))
            {
                holidayId = extras.getInt("HOLIDAYID");
                tipGroupId = extras.getInt("TIPGROUPID");
                tipId = extras.getInt("TIPID");
                tipItem = new TipItem();
                if(!databaseAccess.getTipItem(holidayId, tipGroupId, tipId, tipItem))
                    return;

                tipDescription.setText(tipItem.tipDescription);

                if(imageUtils.getPageHeaderImage(this, tipItem.tipPicture, imageViewSmall)==false)
                    return;

                cbPicturePicked.setChecked(tipItem.pictureAssigned);

                actionBar.setSubtitle(subtitle);

                txtTipNotes.setText(String.valueOf(tipItem.tipNotes));
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
