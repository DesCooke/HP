package com.example.des.hp.Holiday;

import android.annotation.SuppressLint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.R;
import com.example.des.hp.myutils.*;

import java.io.File;
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
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    public Switch sw;
    public TextView lblKnownDates;
    public HolidayItem holidayItem;
    public DialogWithEditTextFragment dialogWithEditTextFragment;
    public View.OnClickListener dwetOnOkClick;
    public ImageButton btnClear;
    public Button btnSave;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    public Switch swDays;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    public Switch swMaps;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    public Switch swTasks;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    public Switch swTips;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    public Switch swBudget;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    public Switch swAttractions;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    public Switch swContacts;
    public ImageView deleteHoliday;



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

            holidayName= findViewById(R.id.txtHolidayName);
            txtStartDate= findViewById(R.id.txtStartDate);
            sw= findViewById(R.id.swKnownDates);
            grpStartDate= findViewById(R.id.grpStartDate);
            lblKnownDates= findViewById(R.id.lblKnownDates);
            grpHolidayName= findViewById(R.id.grpHolidayName);
            btnClear= findViewById(R.id.btnClear);
            btnSave= findViewById(R.id.btnSave);
            swDays= findViewById(R.id.swDays);
            swMaps= findViewById(R.id.swMaps);
            swTasks= findViewById(R.id.swTasks);
            swTips= findViewById(R.id.swTips);
            swBudget= findViewById(R.id.swBudget);
            swAttractions= findViewById(R.id.swAttractions);
            swContacts= findViewById(R.id.swContacts);

            deleteHoliday=findViewById(R.id.my_toolbar_delete);
            deleteHoliday.setOnClickListener(view -> deleteHoliday());

            holidayItem=new HolidayItem();

            btnClear.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.VISIBLE);

            if(action != null && action.equals("add"))
            {
                SetToolbarTitles(getString(R.string.title_planner), "Add a Holiday");
                holidayName.setText("");
                datesAreUnknown();
            } else
            {
                try(DatabaseAccess da = databaseAccess())
                {
                    if(!da.getHolidayItem(holidayId, holidayItem))
                        return;
                }

                holidayName.setText(holidayItem.holidayName);

                txtStartDate.setText(holidayItem.startDateStr);

                ShowToolbarDelete();

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
            int id=view.getId();
            if(id==R.id.imageViewSmall)
                pickImage(view);
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
            ddp.txtStartDate= findViewById(R.id.txtStartDate);
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
            dwetOnOkClick= this::HolidayNamePicked;

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

            holidayItem.holidayName=holidayName.getText().toString();
            holidayItem.pictureChanged = imageChanged;
            if(imageChanged) {
                holidayItem.holidayPicture = "";
                if (!internalImageFilename.isEmpty())
                    holidayItem.holidayPicture = internalImageFilename;
                holidayItem.pictureAssigned = imageSet;
            }

            // if holiday name has changed - then rename directory asap
            if(holidayItem.origHolidayName.compareTo(holidayItem.holidayName)!=0){
                String oldname=holidayItem.origHolidayName;
                String newname=holidayItem.holidayName;
                holidayItem.holidayName=oldname;
                // holiday name changes - so rename directory
                String oldDirname=ImageUtils.imageUtils().GetHolidayDirFromHolidayItem(holidayItem);
                holidayItem.holidayName=newname;
                String newDirname=ImageUtils.imageUtils().GetHolidayDirFromHolidayItem(holidayItem);
                File file=new File(oldDirname);
                boolean res = file.renameTo(new File(newDirname));
                if(!res)
                    throw new Exception("Hmmm - error renaming holiday directory");
            }

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

            try(DatabaseAccess da = databaseAccess())
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

    public void deleteHoliday()
    {
        try
        {
            try(DatabaseAccess da = databaseAccess())
            {
                if(!da.deleteHolidayItem(holidayItem))
                    return;
            }
            finish();
        }
        catch(Exception e)
        {
            ShowError("deleteHoliday", e.getMessage());
        }
    }


}
