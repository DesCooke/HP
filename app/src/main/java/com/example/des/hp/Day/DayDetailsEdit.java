package com.example.des.hp.Day;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.graphics.BitmapFactory;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.myutils.*;
import com.example.des.hp.R;

public class DayDetailsEdit extends BaseActivity
{
    
    public DatabaseAccess databaseAccess;
    private final int SELECT_PHOTO = 1;
    private ImageView imageViewSmall;
    private String action;
    public int holidayId;
    public int dayId;
    public DateUtils dateUtils;
    public LinearLayout grpStartDate;
    public TextView dayName;
    public String holidayName;
    public ActionBar actionBar;
    public DayItem dayItem;
    public CheckBox cbPicturePicked;
    private ImageUtils imageUtils;
    private RadioButton radUnknown;
    private RadioButton radEasy;
    private RadioButton radModerate;
    private RadioButton radBusy;
    public MyMessages myMessages;
    public DialogWithEditTextFragment dialogWithEditTextFragment;
    public View.OnClickListener dwetOnOkClick;
    
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
    
    public void handleDayCatOnClick(View view)
    {
        try
        {
            if (view != radUnknown)
                if (radUnknown.isChecked())
                    radUnknown.setChecked(false);
            if (view != radEasy)
                if (radEasy.isChecked())
                    radEasy.setChecked(false);
            if (view != radModerate)
                if (radModerate.isChecked())
                    radModerate.setChecked(false);
            if (view != radBusy)
                if (radBusy.isChecked())
                    radBusy.setChecked(false);
            
            if (radUnknown.isChecked())
                dayItem.dayCat = getResources().getInteger(R.integer.day_cat_unknown);
            if (radEasy.isChecked())
                dayItem.dayCat = getResources().getInteger(R.integer.day_cat_easy);
            if (radModerate.isChecked())
                dayItem.dayCat = getResources().getInteger(R.integer.day_cat_moderate);
            if (radBusy.isChecked())
                dayItem.dayCat = getResources().getInteger(R.integer.day_cat_busy);
        }
        catch (Exception e)
        {
            ShowError("handleDayCatOnClick", e.getMessage());
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
                            if (lRetCode == false)
                                return;
                            
                            // assign new bitmap and set scale type
                            imageViewSmall.setImageBitmap(myBitmap.Value);
                            
                            cbPicturePicked.setChecked(true);
                            
                            dayItem.pictureChanged = true;
                            
                            
                        }
                        catch (Exception e)
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
            dayItem.pictureChanged = true;
        }
        catch (Exception e)
        {
            ShowError("btnClearImage", e.getMessage());
        }
    }
    
    public void saveDay(View view)
    {
        try
        {
            myMessages.ShowMessageShort("Saving Day");
            
            dayItem.pictureAssigned = cbPicturePicked.isChecked();
            dayItem.dayName = dayName.getText().toString();
            dayItem.dayBitmap = null;
            if (dayItem.pictureAssigned)
                dayItem.dayBitmap = ((BitmapDrawable) imageViewSmall.getDrawable()).getBitmap();
            
            
            if (action.equals("add"))
            {
                MyInt myInt = new MyInt();
                
                dayItem.holidayId = holidayId;
                
                if (!databaseAccess.getNextDayId(holidayId, myInt))
                    return;
                dayItem.dayId = myInt.Value;
                
                if (!databaseAccess.getNextSequenceNo(holidayId, myInt))
                    return;
                dayItem.sequenceNo = myInt.Value;
                if (!databaseAccess.addDayItem(dayItem))
                    return;
            }
            
            if (action.equals("modify"))
            {
                dayItem.holidayId = holidayId;
                dayItem.dayId = dayId;
                if (!databaseAccess.updateDayItem(dayItem))
                    return;
            }
            
            finish();
        }
        catch (Exception e)
        {
            ShowError("saveDay", e.getMessage());
        }
    }
    
    public void DayNamePicked(View view)
    {
        try
        {
            dayName.setText(dialogWithEditTextFragment.getFinalText());
            
            dialogWithEditTextFragment.dismiss();
        }
        catch (Exception e)
        {
            ShowError("DayNamePicked", e.getMessage());
        }
    }
    
    public void pickDayName(View view)
    {
        try
        {
            dwetOnOkClick = new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    DayNamePicked(view);
                }
            };
            
            dialogWithEditTextFragment =
                DialogWithEditTextFragment.newInstance
                    (
                        getFragmentManager(),     // for the transaction bit
                        "hihi",            // unique name for this dialog type
                        "Day",    // form caption
                        "Description",             // form message
                        R.drawable.attachment,
                        dayName.getText().toString(), // initial text
                        dwetOnOkClick,
                        this,
                        false
                    );
            
            dialogWithEditTextFragment.showIt();
        }
        catch (Exception e)
        {
            ShowError("pickDayName", e.getMessage());
        }
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        try
        {
            setContentView(R.layout.activity_day_details_edit);
            
            databaseAccess = new DatabaseAccess(this);
            dateUtils = new DateUtils(this);
            actionBar = getSupportActionBar();
            imageUtils = new ImageUtils(this);
            myMessages = new MyMessages(this);
            
            cbPicturePicked = (CheckBox) findViewById(R.id.picturePicked);
            imageViewSmall = (ImageView) findViewById(R.id.imageViewSmall);
            dayName = (TextView) findViewById(R.id.txtDayName);
            radUnknown = (RadioButton) findViewById(R.id.radUnknown);
            radEasy = (RadioButton) findViewById(R.id.radEasy);
            radModerate = (RadioButton) findViewById(R.id.radModerate);
            radBusy = (RadioButton) findViewById(R.id.radBusy);
            
            clearImage(null);
            
            Bundle extras = getIntent().getExtras();
            if (extras != null)
            {
                action = extras.getString("ACTION");
                if (action != null && action.equals("add"))
                {
                    dayItem = new DayItem();
                    holidayId = extras.getInt("HOLIDAYID");
                    holidayName = extras.getString("HOLIDAYNAME");
                    dayName.setText("");
                    cbPicturePicked.setChecked(false);
                    actionBar.setTitle(holidayName);
                    actionBar.setSubtitle("Add a Day");
                    radUnknown.setChecked(true);
                    radEasy.setChecked(false);
                    radModerate.setChecked(false);
                    radBusy.setChecked(false);
                }
                if (action != null && action.equals("modify"))
                {
                    holidayId = extras.getInt("HOLIDAYID");
                    dayId = extras.getInt("DAYID");
                    holidayName = extras.getString("HOLIDAYNAME");
                    dayItem = new DayItem();
                    if (!databaseAccess.getDayItem(holidayId, dayId, dayItem))
                        return;
                    
                    dayName.setText(dayItem.dayName);
                    
                    String originalFileName = dayItem.dayPicture;
                    
                    if (imageUtils.getPageHeaderImage(this, dayItem.dayPicture, imageViewSmall) == false)
                        return;
                    
                    cbPicturePicked.setChecked(dayItem.pictureAssigned);
                    
                    actionBar.setTitle(holidayName);
                    actionBar.setSubtitle(dayItem.dayName);
                    
                    radUnknown.setChecked(false);
                    radEasy.setChecked(false);
                    radModerate.setChecked(false);
                    radBusy.setChecked(false);
                    if (dayItem.dayCat == getResources().getInteger(R.integer.day_cat_unknown))
                        radUnknown.setChecked(true);
                    if (dayItem.dayCat == getResources().getInteger(R.integer.day_cat_easy))
                        radEasy.setChecked(true);
                    if (dayItem.dayCat == getResources().getInteger(R.integer.day_cat_moderate))
                        radModerate.setChecked(true);
                    if (dayItem.dayCat == getResources().getInteger(R.integer.day_cat_busy))
                        radBusy.setChecked(true);
                    
                }
            }
        }
        catch (Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
        
    }
}
