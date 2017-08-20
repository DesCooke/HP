package com.example.des.hp.Day;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.graphics.BitmapFactory;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.Schedule.Bus.BusItem;
import com.example.des.hp.myutils.*;
import com.example.des.hp.R;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
import static com.example.des.hp.myutils.ImageUtils.imageUtils;
import static com.example.des.hp.myutils.MyMessages.myMessages;

public class DayDetailsEdit extends BaseActivity implements View.OnClickListener
{

    //region Member variables
    public LinearLayout grpStartDate;
    public TextView dayName;
    public String holidayName;
    public DayItem dayItem;
    private RadioButton radUnknown;
    private RadioButton radEasy;
    private RadioButton radModerate;
    private RadioButton radBusy;
    public DialogWithEditTextFragment dialogWithEditTextFragment;
    public View.OnClickListener dwetOnOkClick;
    public LinearLayout grpDayName;
    public ImageButton btnClear;
    public Button btnSave;
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

            dayItem = new DayItem();

            imageView = (ImageView) findViewById(R.id.imageViewSmall);
            dayName = (TextView) findViewById(R.id.txtDayName);
            radUnknown = (RadioButton) findViewById(R.id.radUnknown);
            radEasy = (RadioButton) findViewById(R.id.radEasy);
            radModerate = (RadioButton) findViewById(R.id.radModerate);
            radBusy = (RadioButton) findViewById(R.id.radBusy);
            grpDayName = (LinearLayout) findViewById(R.id.grpDayName);
            btnClear=(ImageButton) findViewById(R.id.btnClear);
            btnSave=(Button) findViewById(R.id.btnSave);

            btnClear.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.VISIBLE);

            btnClearImage(null);

            Bundle extras = getIntent().getExtras();
            if (extras != null)
            {
                action = extras.getString("ACTION");
                if (action != null && action.equals("add"))
                {
                    holidayId = extras.getInt("HOLIDAYID");
                    holidayName = extras.getString("HOLIDAYNAME");
                    dayName.setText("");
                    SetTitles(holidayName, "Add a Day");
                }
                if (action != null && action.equals("modify"))
                {
                    holidayId = extras.getInt("HOLIDAYID");
                    dayId = extras.getInt("DAYID");
                    holidayName = extras.getString("HOLIDAYNAME");
                    if (!databaseAccess().getDayItem(holidayId, dayId, dayItem))
                        return;

                    String originalFileName = dayItem.dayPicture;

                    SetTitles(holidayName,dayItem.dayName);

                }
            }

            grpDayName.setOnClickListener(this);
            imageView.setOnClickListener(this);

            afterCreate();

            showForm();

        }
        catch (Exception e)
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
            if (dayItem.dayCat == getResources().getInteger(R.integer.day_cat_unknown))
                radUnknown.setChecked(true);
            if (dayItem.dayCat == getResources().getInteger(R.integer.day_cat_easy))
                radEasy.setChecked(true);
            if (dayItem.dayCat == getResources().getInteger(R.integer.day_cat_moderate))
                radModerate.setChecked(true);
            if (dayItem.dayCat == getResources().getInteger(R.integer.day_cat_busy))
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
        switch(view.getId())
        {

            case R.id.grpDayName:
                pickDayName(view);
                break;

            case R.id.imageViewSmall:
                pickImage(view);
                break;
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

    //endregion


    //region Regular Form Activities
    public void saveSchedule(View view)
    {
        try
        {
            myMessages().ShowMessageShort("Saving Day");

            MyInt myInt = new MyInt();

            dayItem.dayName = dayName.getText().toString();

            dayItem.pictureAssigned=imageSet;
            dayItem.pictureChanged=imageChanged;
            dayItem.dayBitmap = null;
            if (imageSet)
                dayItem.dayBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

            if (action.equals("add"))
            {
                dayItem.holidayId = holidayId;
                
                if (!databaseAccess().getNextDayId(holidayId, myInt))
                    return;
                dayItem.dayId = myInt.Value;
                
                if (!databaseAccess().getNextSequenceNo(holidayId, myInt))
                    return;
                dayItem.sequenceNo = myInt.Value;
                if (!databaseAccess().addDayItem(dayItem))
                    return;
            }
            
            if (action.equals("modify"))
            {
                if (!databaseAccess().updateDayItem(dayItem))
                    return;
            }
            
            finish();
        }
        catch (Exception e)
        {
            ShowError("saveDay", e.getMessage());
        }
    }
    
    //endregion
}
