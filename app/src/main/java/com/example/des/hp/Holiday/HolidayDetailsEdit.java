package com.example.des.hp.Holiday;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.R;
import com.example.des.hp.myutils.*;

import java.util.Date;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
import static com.example.des.hp.myutils.DateUtils.dateUtils;
import static com.example.des.hp.myutils.MyMessages.myMessages;

public class HolidayDetailsEdit extends BaseActivity implements View.OnClickListener
{

    //region Member variables
    public LinearLayout grpStartDate;
    public LinearLayout grpHolidayName;
    public TextView holidayName;
    public TextView txtStartDate;
    public Switch sw;
    public TextView lblKnownDates;
    public HolidayItem holidayItem;
    public DialogWithEditTextFragment dialogWithEditTextFragment;
    public View.OnClickListener dwetOnOkClick;
    public ImageButton btnClear;
    public Button btnSave;
    public Switch swDays;
    public Switch swMaps;
    public Switch swTasks;
    public Switch swTips;
    public Switch swBudget;
    public Switch swAttractions;
    public Switch swContacts;

    //endregion

    //region Constructors/Destructors
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            layoutName="activity_holiday_details_edit";
            setContentView(R.layout.activity_holiday_details_edit);

            holidayName=(TextView) findViewById(R.id.txtHolidayName);
            txtStartDate=(TextView) findViewById(R.id.txtStartDate);
            sw=(Switch) findViewById(R.id.swKnownDates);
            grpStartDate=(LinearLayout) findViewById(R.id.grpStartDate);
            lblKnownDates=(TextView) findViewById(R.id.lblKnownDates);
            grpHolidayName=(LinearLayout) findViewById(R.id.grpHolidayName);
            btnClear=(ImageButton) findViewById(R.id.btnClear);
            btnSave=(Button) findViewById(R.id.btnSave);
            swDays=(Switch) findViewById(R.id.swDays);
            swMaps=(Switch) findViewById(R.id.swMaps);
            swTasks=(Switch) findViewById(R.id.swTasks);
            swTips=(Switch) findViewById(R.id.swTips);
            swBudget=(Switch) findViewById(R.id.swBudget);
            swAttractions=(Switch) findViewById(R.id.swAttractions);
            swContacts=(Switch) findViewById(R.id.swContacts);

            holidayItem=new HolidayItem();

            btnClear.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.VISIBLE);

            if(action != null && action.equals("add"))
            {
                SetTitles(getString(R.string.title_planner), "Add a Holiday");
                holidayName.setText("");
                datesAreUnknown();
            } else
            {
                try(DatabaseAccess da = databaseAccess();)
                {
                    if(!da.getHolidayItem(holidayId, holidayItem))
                        return;
                }

                holidayName.setText(holidayItem.holidayName);

                txtStartDate.setText(holidayItem.startDateStr);

                if(holidayItem.startDateInt == DateUtils.unknownDate)
                {
                    sw.setChecked(false);
                    datesAreUnknown();
                } else
                {
                    sw.setChecked(true);
                    datesAreKnown();
                }
                swDays.setChecked(holidayItem.buttonDays);
                swMaps.setChecked(holidayItem.buttonMaps);
                swTasks.setChecked(holidayItem.buttonTasks);
                swTips.setChecked(holidayItem.buttonTips);
                swBudget.setChecked(holidayItem.buttonBudget);
                swAttractions.setChecked(holidayItem.buttonAttractions);
                swContacts.setChecked(holidayItem.buttonContacts);

                setTitle(holidayItem.holidayName);
                SetImage(holidayItem.holidayPicture);
            }
            afterCreate();
            imageView.setOnClickListener(this);
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }

    }
    //endregion

    //region OnClick Events
    public void onClick(View view)
    {
        try
        {
            switch(view.getId())
            {
                case R.id.imageViewSmall:
                    pickImage(view);
                    break;
            }
        }
        catch(Exception e)
        {
            ShowError("onClick", e.getMessage());
        }

    }

    public void pickDateTime(View view)
    {
        try
        {
            DialogDatePicker ddp=new DialogDatePicker(this);
            ddp.txtStartDate=(TextView) findViewById(R.id.txtStartDate);
            Date date=new Date();
            if(!dateUtils().StrToDate(ddp.txtStartDate.getText().toString(), date))
                return;
            ddp.setInitialDate(date);
            ddp.show();
        }
        catch(Exception e)
        {
            ShowError("pickDateTime", e.getMessage());
        }
    }

    public void HolidayNamePicked(View view)
    {
        try
        {
            holidayName.setText(dialogWithEditTextFragment.getFinalText());

            dialogWithEditTextFragment.dismiss();
        }
        catch(Exception e)
        {
            ShowError("HolidayNamePicked", e.getMessage());
        }
    }

    public void pickHolidayName(View view)
    {
        try
        {
            dwetOnOkClick=new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    HolidayNamePicked(view);
                }
            };

            dialogWithEditTextFragment=DialogWithEditTextFragment.newInstance(getSupportFragmentManager(),     // for the transaction bit
                "hihi",            // unique name for this dialog type
                "Holiday",    // form caption
                "Description",             // form message
                R.drawable.attachment, holidayName.getText().toString(), // initial text
                dwetOnOkClick, this, false
            );

            dialogWithEditTextFragment.showIt();
        }
        catch(Exception e)
        {
            ShowError("pickHolidayName", e.getMessage());
        }
    }

    public void datesAreUnknown()
    {
        try
        {
            grpStartDate.setVisibility(View.INVISIBLE);
            lblKnownDates.setText(getString(R.string.dates_not_known));
        }
        catch(Exception e)
        {
            ShowError("datesAreUnknown", e.getMessage());
        }
    }

    public void datesAreKnown()
    {
        try
        {
            grpStartDate.setVisibility(View.VISIBLE);
            lblKnownDates.setText(getString(R.string.dates_known));
        }
        catch(Exception e)
        {
            ShowError("datesAreKnown", e.getMessage());
        }
    }

    public void toggleVisibility(View view)
    {
        try
        {
            if(sw.isChecked())
            {
                MyString myString=new MyString();
                if(!dateUtils().DateToStr(new Date(), myString))
                    return;
                txtStartDate.setText(myString.Value);
                datesAreKnown();
            } else
            {
                datesAreUnknown();
            }
        }
        catch(Exception e)
        {
            ShowError("toggleVisibility", e.getMessage());
        }
    }
    //endregion

    //region form Functions
    @Override
    public void showForm()
    {
        try
        {
            super.showForm();

            SetImage(holidayItem.holidayPicture);

            afterShow();
        }
        catch(Exception e)
        {
            ShowError("showForm", e.getMessage());
        }

    }

    public void saveHoliday(View view)
    {
        try
        {
            myMessages().ShowMessageShort("Saving Holiday");

            holidayItem.holidayPicture="";
            if(internalImageFilename.length() > 0)
                holidayItem.holidayPicture=internalImageFilename;
            holidayItem.pictureAssigned=imageSet;
            holidayItem.pictureChanged=imageChanged;
            holidayItem.holidayName=holidayName.getText().toString();
            if(sw.isChecked())
            {
                holidayItem.startDateDate=new Date();
                if(!dateUtils().StrToDate(txtStartDate.getText().toString(), holidayItem.startDateDate))
                    return;
            } else
            {
                holidayItem.startDateDate=new Date(DateUtils.unknownDate);
            }

            MyLong myLong=new MyLong();
            if(!dateUtils().DateToInt(holidayItem.startDateDate, myLong))
                return;

            holidayItem.startDateInt=myLong.Value;

            holidayItem.holidayBitmap=null;
            if(holidayItem.pictureAssigned)
                holidayItem.holidayBitmap=((BitmapDrawable) imageView.getDrawable()).getBitmap();

            holidayItem.buttonDays = swDays.isChecked();
            holidayItem.buttonMaps = swMaps.isChecked();
            holidayItem.buttonTasks = swTasks.isChecked();
            holidayItem.buttonTips = swTips.isChecked();
            holidayItem.buttonBudget = swBudget.isChecked();
            holidayItem.buttonAttractions = swAttractions.isChecked();
            holidayItem.buttonContacts = swContacts.isChecked();

            try(DatabaseAccess da = databaseAccess();)
            {
                if(action.equals("add"))
                {
                    MyInt myInt=new MyInt();
                    if(!da.getNextHolidayId(myInt))
                        return;
                    holidayItem.holidayId=myInt.Value;

                    if(!da.addHolidayItem(holidayItem))
                        return;
                }

                if(action.equals("modify"))
                {
                    holidayItem.holidayId=holidayId;
                    if(!da.updateHolidayItem(holidayItem))
                        return;
                }

            }
            finish();
        }
        catch(Exception e)
        {
            ShowError("saveHoliday", e.getMessage());
        }
    }
    //endregion

}
