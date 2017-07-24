package com.example.des.hp.Holiday;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBar;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.R;
import com.example.des.hp.myutils.*;

import java.io.InputStream;
import java.util.Date;

public class HolidayDetailsEdit extends BaseActivity
{

    public DatabaseAccess databaseAccess;
    private final int SELECT_PHOTO = 1;
    private ImageView imageViewSmall;
    private String action;
    public int holidayId;
    public DateUtils dateUtils;
    public LinearLayout grpStartDate;
    public TextView holidayName;
    public CheckBox cb;
    public TextView txtStartDate;
    public Switch sw;
    public TextView lblKnownDates;
    public ActionBar actionBar;
    public HolidayItem holidayItem;
    public CheckBox cbPicturePicked;
    private ImageUtils imageUtils;
    public MyMessages myMessages;
    public DialogWithEditTextFragment dialogWithEditTextFragment;
    public View.OnClickListener dwetOnOkClick;

    public void pickImage(View view)
    {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    public void pickDateTime(View view)
    {
        DialogDatePicker ddp = new DialogDatePicker(this);
        ddp.txtStartDate = (TextView)findViewById(R.id.txtStartDate);
        Date date = new Date();
        if(dateUtils.StrToDate(ddp.txtStartDate.getText().toString(), date)==false)
            return;
        ddp.setInitialDate(date);
        ddp.show();
    }

    public void HolidayNamePicked(View view)
    {
        holidayName.setText(dialogWithEditTextFragment.getFinalText());

        dialogWithEditTextFragment.dismiss();
    }

    public void pickHolidayName(View view)
    {
        dwetOnOkClick = new View.OnClickListener()
        {
            public void onClick(View view)
            {
                HolidayNamePicked(view);
            }
        };

        dialogWithEditTextFragment =
                DialogWithEditTextFragment.newInstance
                        (
                                getFragmentManager(),     // for the transaction bit
                                "hihi",            // unique name for this dialog type
                                "Holiday" ,    // form caption
                                "Description",             // form message
                                R.drawable.attachment,
                                holidayName.getText().toString(), // initial text
                                dwetOnOkClick,
                                this,
                                false
                        );

        dialogWithEditTextFragment.showIt();
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

                            holidayItem.pictureChanged = true;

                        } catch (Exception e)
                        {
                            ShowError("onActivityResult-selectphoto", e.getMessage());
                        }
                    }
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

    public void btnClearClick(View view)
    {
        clearImage(view);
        holidayItem.pictureChanged=true;
    }

    public void saveHoliday(View view)
    {
        myMessages.ShowMessageShort("Saving Holiday");

        CheckBox cb=(CheckBox)findViewById(R.id.picturePicked);
        holidayItem.pictureAssigned = cb.isChecked();

        holidayItem.holidayName = holidayName.getText().toString();
        if (sw.isChecked())
        {
            holidayItem.startDateDate = new Date();
            if(dateUtils.StrToDate(txtStartDate.getText().toString(), holidayItem.startDateDate)==false)
                return;
        }
        else
        {
            holidayItem.startDateDate = new Date(DateUtils.unknownDate);
        }

        MyLong myLong = new MyLong();
        if(dateUtils.DateToInt(holidayItem.startDateDate, myLong)==false)
            return;

        holidayItem.startDateInt = myLong.Value;

        holidayItem.holidayBitmap = null;
        if (holidayItem.pictureAssigned)
            holidayItem.holidayBitmap=((BitmapDrawable)imageViewSmall.getDrawable()).getBitmap() ;
        

        if(action.equals("add"))
        {
            MyInt myInt = new MyInt();
            if(!databaseAccess.getNextHolidayId(myInt))
                return;
            holidayItem.holidayId = myInt.Value;

            if(!databaseAccess.addHolidayItem(holidayItem))
                return;
        }

        if(action.equals("modify"))
        {
            holidayItem.holidayId = holidayId;
            if(!databaseAccess.updateHolidayItem(holidayItem))
                return;
        }

        finish();
    }

    private void ShowError(String argFunction, String argMessage)
    {
        myMessages.ShowError
                (
                        "Error in HolidayDetailsEdit::" + argFunction,
                        argMessage
                );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_holiday_details_edit);

        databaseAccess = new DatabaseAccess(this);
        dateUtils = new DateUtils(this);
        imageUtils = new ImageUtils(this);
        myMessages = new MyMessages(this);

        cbPicturePicked=(CheckBox)findViewById(R.id.picturePicked);
        imageViewSmall = (ImageView)findViewById(R.id.imageViewSmall);
        holidayName=(TextView)findViewById(R.id.txtHolidayName);
        txtStartDate = (TextView)findViewById(R.id.txtStartDate);
        sw = (Switch)findViewById(R.id.swKnownDates);
        grpStartDate = (LinearLayout)findViewById(R.id.grpStartDate);
        lblKnownDates = (TextView)findViewById(R.id.lblKnownDates);

        clearImage(null);

        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            action = extras.getString("ACTION");
            if(action!=null && action.equals("add"))
            {
                holidayItem = new HolidayItem();

                actionBar = getSupportActionBar();
                if(actionBar!=null)
                {
                    actionBar.setTitle(R.string.title_planner);
                    actionBar.setSubtitle("Add a Holiday");
                }

                holidayName.setText("");
                cbPicturePicked.setChecked(false);
                datesAreUnknown();
            }
            if(action!=null && action.equals("modify"))
            {
                holidayId = extras.getInt("HOLIDAYID");
                holidayItem = new HolidayItem();
                if(!databaseAccess.getHolidayItem(holidayId, holidayItem))
                    return;

                holidayName.setText(holidayItem.holidayName);

                String originalFileName = holidayItem.holidayPicture;

                if(holidayItem.holidayPicture.length()>0)
                    if(imageUtils.getPageHeaderImage(this, holidayItem.holidayPicture, imageViewSmall)==false)
                        return;

                cbPicturePicked.setChecked(holidayItem.pictureAssigned);
                txtStartDate.setText(holidayItem.startDateStr);

                if(holidayItem.startDateInt == DateUtils.unknownDate)
                {
                    sw.setChecked(false);
                    datesAreUnknown();
                }
                else
                {
                    sw.setChecked(true);
                    datesAreKnown();
                }
                setTitle(holidayItem.holidayName);
            }
        }

    }

    public void datesAreUnknown()
    {
        grpStartDate.setVisibility(View.INVISIBLE);
        lblKnownDates.setText("Dates not known");
    }

    public void datesAreKnown()
    {
        grpStartDate.setVisibility(View.VISIBLE);
        lblKnownDates.setText("Dates are known");
    }

    public void toggleVisibility(View view)
    {
        if(sw.isChecked())
        {
            MyString myString = new MyString();
            if(dateUtils.DateToStr(new Date(), myString)==false)
                return;
            txtStartDate.setText(myString.Value);
            datesAreKnown();
        }
        else
        {
            datesAreUnknown();
        }
    }
/*
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.holidaydetailsformmenu, menu);
        return true;
    }
*/
}
