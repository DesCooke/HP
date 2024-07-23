package com.example.des.hp.Day;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.Holiday.HolidayItem;
import com.example.des.hp.myutils.*;
import com.example.des.hp.R;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
import static com.example.des.hp.myutils.MyMessages.myMessages;

public class DayDetailsEdit extends BaseActivity implements View.OnClickListener
{

    public TextView dayName;
    public String holidayName;
    public DayItem dayItem;
    private Switch radUnknown;
    private Switch radEasy;
    private Switch radModerate;
    private Switch radBusy;
    public DialogWithEditTextFragment dialogWithEditTextFragment;
    public View.OnClickListener dwetOnOkClick;
    public LinearLayout grpDayName;
    public ImageButton btnClear;
    public Button btnSave;
    public ImageView deleteDay;
    //endregion

    //region Constructors/Destructors
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            layoutName="activity_day_details_edit";
            setContentView(R.layout.activity_day_details_edit);

            dayItem=new DayItem();

            imageView= findViewById(R.id.imageViewSmall);
            dayName= findViewById(R.id.txtDayName);
            radUnknown= findViewById(R.id.radUnknown);
            radEasy= findViewById(R.id.radEasy);
            radModerate= findViewById(R.id.radModerate);
            radBusy= findViewById(R.id.radBusy);
            grpDayName= findViewById(R.id.grpDayName);
            btnClear= findViewById(R.id.btnClear);
            btnSave= findViewById(R.id.btnSave);

            btnClear.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.VISIBLE);

            deleteDay=findViewById(R.id.my_toolbar_delete);
            deleteDay.setOnClickListener(view -> deleteDay());

            alwaysShowBtnShowNotes=true;
            alwaysShowBtnShowInfo=true;
            alwaysShowBtnClearImage=true;

            btnClearImage(null);

            Bundle extras=getIntent().getExtras();
            if(extras != null)
            {
                action=extras.getString("ACTION");
                if(action != null && action.equals("add"))
                {
                    holidayId=extras.getInt("HOLIDAYID");
                    holidayName=extras.getString("HOLIDAYNAME");
                    dayName.setText("");
                    SetToolbarTitles("Add a Day", holidayName);
                }
                if(action != null && action.equals("modify"))
                {
                    holidayId=extras.getInt("HOLIDAYID");
                    dayId=extras.getInt("DAYID");

                    try(DatabaseAccess da = databaseAccess())
                    {
                        if(!da.getDayItem(holidayId, dayId, dayItem))
                            return;
                        HolidayItem holidayItem=new HolidayItem();
                        if(!da.getHolidayItem(holidayId, holidayItem))
                            return;
                        SetToolbarTitles(dayItem.dayName, holidayItem.holidayName);
                    }

                    ShowToolbarDelete();

                }
            }

            grpDayName.setOnClickListener(this);
            imageView.setOnClickListener(this);

            afterCreate();

            showForm();

        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }

    }
    //endregion

    //region Regular Form Activities
    public void showForm()
    {
        super.showForm();
        try
        {

            dayName.setText(dayItem.dayName);
            radUnknown.setChecked(false);
            radEasy.setChecked(false);
            radModerate.setChecked(false);
            radBusy.setChecked(false);
            if(dayItem.dayCat == getResources().getInteger(R.integer.day_cat_unknown))
                radUnknown.setChecked(true);
            if(dayItem.dayCat == getResources().getInteger(R.integer.day_cat_easy))
                radEasy.setChecked(true);
            if(dayItem.dayCat == getResources().getInteger(R.integer.day_cat_moderate))
                radModerate.setChecked(true);
            if(dayItem.dayCat == getResources().getInteger(R.integer.day_cat_busy))
                radBusy.setChecked(true);

            SetImage(dayItem.dayPicture);

            afterShow();
        }
        catch(Exception e)
        {
            ShowError("showForm", e.getMessage());
        }
    }
    //endregion

    //region OnClick events
    public void onClick(View view)
    {
        try
        {
            int id=view.getId();
            if(id==R.id.grpDayName)
                pickDayName(view);
            if(id==R.id.imageViewSmall)
                pickImage(view);
        }
        catch(Exception e)
        {
            ShowError("onClick", e.getMessage());
        }
    }

    public void handleDayCatOnClick(View view)
    {
        try
        {
            if(view != radUnknown)
                if(radUnknown.isChecked())
                    radUnknown.setChecked(false);
            if(view != radEasy)
                if(radEasy.isChecked())
                    radEasy.setChecked(false);
            if(view != radModerate)
                if(radModerate.isChecked())
                    radModerate.setChecked(false);
            if(view != radBusy)
                if(radBusy.isChecked())
                    radBusy.setChecked(false);

            if(radUnknown.isChecked())
                dayItem.dayCat=getResources().getInteger(R.integer.day_cat_unknown);
            if(radEasy.isChecked())
                dayItem.dayCat=getResources().getInteger(R.integer.day_cat_easy);
            if(radModerate.isChecked())
                dayItem.dayCat=getResources().getInteger(R.integer.day_cat_moderate);
            if(radBusy.isChecked())
                dayItem.dayCat=getResources().getInteger(R.integer.day_cat_busy);
        }
        catch(Exception e)
        {
            ShowError("handleDayCatOnClick", e.getMessage());
        }
    }

    public void DayNamePicked(View view)
    {
        try
        {
            dayName.setText(dialogWithEditTextFragment.getFinalText());

            dialogWithEditTextFragment.dismiss();
        }
        catch(Exception e)
        {
            ShowError("DayNamePicked", e.getMessage());
        }
    }

    public void pickDayName(View view)
    {
        try
        {
            dwetOnOkClick= this::DayNamePicked;

            dialogWithEditTextFragment=DialogWithEditTextFragment.newInstance(getSupportFragmentManager(),     // for the transaction bit
                "hihi",            // unique name for this dialog type
                "Day",    // form caption
                "Description",             // form message
                R.drawable.attachment, dayName.getText().toString(), // initial text
                dwetOnOkClick, this, false
            );

            dialogWithEditTextFragment.showIt();
        }
        catch(Exception e)
        {
            ShowError("pickDayName", e.getMessage());
        }
    }

    //endregion


    //region Regular Form Activities
    public void saveSchedule(View view)
    {
        try(DatabaseAccess da = databaseAccess())
        {
            myMessages().ShowMessageShort("Saving Day");

            MyInt myInt=new MyInt();

            dayItem.dayName=dayName.getText().toString();

            dayItem.dayPicture="";
            if(!internalImageFilename.isEmpty())
                dayItem.dayPicture=internalImageFilename;
            dayItem.pictureAssigned=imageSet;
            dayItem.pictureChanged=imageChanged;
            dayItem.dayBitmap=null;
            if(imageSet)
                dayItem.dayBitmap=((BitmapDrawable) imageView.getDrawable()).getBitmap();

            if(action.equals("add")) {
                dayItem.holidayId = holidayId;

                if (!da.getNextDayId(holidayId, myInt))
                    return;
                dayItem.dayId = myInt.Value;

                if (!da.getNextSequenceNo(holidayId, myInt))
                    return;
                dayItem.sequenceNo = myInt.Value;
                if (!da.addDayItem(dayItem))
                    return;
            }

            if(action.equals("modify"))
            {
                if(!da.updateDayItem(dayItem))
                    return;
            }

            finish();
        }
        catch(Exception e)
        {
            ShowError("saveDay", e.getMessage());
        }
    }

    //endregion

    public void deleteDay()
    {
        try
        {
            try(DatabaseAccess da = databaseAccess())
            {
                if(!da.deleteDayItem(dayItem))
                    return;
            }
            finish();
        }
        catch(Exception e)
        {
            ShowError("deleteDay", e.getMessage());
        }
    }

    @Override
    public void setNoteId(int pNoteId)
    {
        try
        {
            dayItem.noteId=pNoteId;
            try(DatabaseAccess da = databaseAccess())
            {
                da.updateDayItem(dayItem);
            }
        }
        catch(Exception e)
        {
            ShowError("setNoteId", e.getMessage());
        }

    }

    @Override
    public int getNoteId()
    {
        try
        {
            return (dayItem.noteId);
        }
        catch(Exception e)
        {
            ShowError("getNoteId", e.getMessage());
        }
        return (0);
    }

    @Override
    public void setInfoId(int pInfoId)
    {
        try
        {
            dayItem.infoId=pInfoId;
            try(DatabaseAccess da = databaseAccess())
            {
                da.updateDayItem(dayItem);
            }
        }
        catch(Exception e)
        {
            ShowError("setInfoId", e.getMessage());
        }

    }

    @Override
    public int getInfoId()
    {
        try
        {
            return (dayItem.infoId);
        }
        catch(Exception e)
        {
            ShowError("getInfoId", e.getMessage());
        }
        return (0);

    }



}
