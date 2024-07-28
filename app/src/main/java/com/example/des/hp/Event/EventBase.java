package com.example.des.hp.Event;

import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.R;
import com.example.des.hp.ScheduleArea.ScheduleAreaList;
import com.example.des.hp.myutils.DateUtils;
import com.example.des.hp.myutils.DialogTimePicker;
import com.example.des.hp.myutils.DialogWithEditTextFragment;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

/** @noinspection ALL*/
public class EventBase extends BaseActivity
{
    public DateUtils dateUtils;
    public EventScheduleItem eventScheduleItem;
    public TextView txtSchedName;
    public LinearLayout grpSchedName;
    public String holidayName;
    public DialogWithEditTextFragment dialogWithEditTextFragment;
    public View.OnClickListener dwetOnOkClick;
    public ImageButton btnShowInfo;
    public ImageButton btnShowNotes;
    public String scheduleTypeDescription="";
    public LinearLayout grpMenuFile;

    public void SchedNamePicked(View view)
    {
        try
        {
            txtSchedName.setText(dialogWithEditTextFragment.getFinalText());

            dialogWithEditTextFragment.dismiss();
        }
        catch(Exception e)
        {
            ShowError("SchedNamePicked", e.getMessage());
        }
    }

    public void pickSchedName(View view)
    {
        try
        {
            dwetOnOkClick= this::SchedNamePicked;

            dialogWithEditTextFragment=DialogWithEditTextFragment.newInstance(getSupportFragmentManager(),     // for the transaction bit
                "hihi",            // unique name for this dialog type
                scheduleTypeDescription,    // form caption
                scheduleTypeDescription,             // form message
                R.drawable.attachment, txtSchedName.getText().toString(), // initial text
                dwetOnOkClick, this, false
            );

            dialogWithEditTextFragment.showIt();
        }
        catch(Exception e)
        {
            ShowError("pickSchedName", e.getMessage());
        }
    }

    @Override
    public int getNoteId()
    {
        try
        {
            return (eventScheduleItem.noteId);
        }
        catch(Exception e)
        {
            ShowError("getNoteId", e.getMessage());
        }
        return (0);
    }

    @Override
    public void setNoteId(int noteId)
    {
        try
        {
            eventScheduleItem.noteId=noteId;
            try(DatabaseAccess da = databaseAccess())
            {
                da.updateScheduleItem(eventScheduleItem);
            }
        }
        catch(Exception e)
        {
            ShowError("setNoteId", e.getMessage());
        }
    }

    @Override
    public int getInfoId()
    {
        try
        {
            return (eventScheduleItem.infoId);
        }
        catch(Exception e)
        {
            ShowError("getInfoId", e.getMessage());
        }
        return (0);
    }

    @Override
    public void setInfoId(int infoId)
    {
        try
        {
            eventScheduleItem.infoId=infoId;
            try(DatabaseAccess da = databaseAccess())
            {
                da.updateScheduleItem(eventScheduleItem);
            }
        }
        catch(Exception e)
        {
            ShowError("setInfoId", e.getMessage());
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        try
        {
            if(requestCode==MOVEITEM){
                if(resultCode == RESULT_OK)
                {
                    try
                    {
                        eventScheduleItem.dayId=data.getIntExtra("DAYID", 0);
                        eventScheduleItem.attractionId=data.getIntExtra("ATTRACTIONID", 0);
                        eventScheduleItem.attractionAreaId=data.getIntExtra("ATTRACTIONAREAID", 0);
                        try(DatabaseAccess da = databaseAccess())
                        {
                            da.updateScheduleItem(eventScheduleItem);
                        }
                        finish();
                    }
                    catch(Exception e)
                    {
                        ShowError("onActivityResult-MOVEITEM", e.getMessage());
                    }
                }
            }
        }
        catch(Exception e)
        {
            ShowError("onActivityResult", e.getMessage());
        }
    }

    @Override
    public void afterCreate()
    {
        super.afterCreate();

        try
        {
            dateUtils=new DateUtils();
            txtSchedName= findViewById(R.id.txtSchedName);
            grpSchedName= findViewById(R.id.grpSchedName);
            grpMenuFile= findViewById(R.id.grpMenuFile);
        }
        catch(Exception e)
        {
            ShowError("afterCreate", e.getMessage());
        }
    }

    @Override
    public void showForm()
    {
        super.showForm();

        try
        {
            eventScheduleItem =new EventScheduleItem();
            if(action != null && action.equals("add"))
            {
                txtSchedName.setText("");
                SetImage("");
            } else
            {
                try(DatabaseAccess da = databaseAccess())
                {
                    if(!da.getScheduleItem(holidayId, dayId, attractionId, attractionAreaId, scheduleId, eventScheduleItem))
                        return;
                }

                txtSchedName.setText(eventScheduleItem.schedName);

                SetImage(eventScheduleItem.schedPicture);
                imageChanged=false;

            }
        }
        catch(Exception e)
        {
            ShowError("showForm", e.getMessage());
        }
    }

    public void move()
    {
        try
        {
            Intent intent=new Intent(getApplicationContext(), ScheduleAreaList.class);
            intent.putExtra("ACTION", "move");
            intent.putExtra("HOLIDAYID", holidayId);
            intent.putExtra("DAYID", dayId);
            intent.putExtra("ATTRACTIONID", attractionId);
            intent.putExtra("ATTRACTIONAREAID", attractionAreaId);
            intent.putExtra("SCHEDULEID", scheduleId);
            startActivityForResult(intent, MOVEITEM);
        }
        catch(Exception e)
        {
            ShowError("move", e.getMessage());
        }
    }

    public void deleteSchedule()
    {
        try
        {
            try(DatabaseAccess da = databaseAccess())
            {
                if(!da.deleteScheduleItem(eventScheduleItem))
                    return;
            }

            finish();
        }
        catch(Exception e)
        {
            ShowError("deleteSchedule", e.getMessage());
        }
    }

    public void editSchedule(Class classNeeded)
    {
        try
        {
            Intent intent=new Intent(getApplicationContext(), classNeeded);
            intent.putExtra("ACTION", "edit");
            intent.putExtra("HOLIDAYID", holidayId);
            intent.putExtra("DAYID", dayId);
            intent.putExtra("ATTRACTIONID", attractionId);
            intent.putExtra("ATTRACTIONAREAID", attractionAreaId);
            intent.putExtra("SCHEDULEID", scheduleId);
            intent.putExtra("HOLIDAYNAME", holidayName);
            intent.putExtra("TITLE", title);
            intent.putExtra("SUBTITLE", subTitle);

            startActivity(intent);
        }
        catch(Exception e)
        {
            ShowError("editSchedule", e.getMessage());
        }
    }

    public int getHour(TextView textview)
    {
        int lHour=0;
        try
        {
            String[] sarray=textview.getText().toString().split(":");
            lHour=Integer.parseInt(sarray[0]);
            if(lHour < 0)
                lHour=0;
            if(lHour > 23)
                lHour=23;
        }
        catch(Exception e)
        {
            ShowError("getHour", e.getMessage());
        }
        return (lHour);
    }

    public int getMinute(TextView textview)
    {
        int lMinute=0;
        try
        {
            String[] sarray=textview.getText().toString().split(":");
            lMinute=Integer.parseInt(sarray[1]);
            if(lMinute < 0)
                lMinute=0;
            if(lMinute > 59)
                lMinute=59;
        }
        catch(Exception e)
        {
            ShowError("getMinute", e.getMessage());
        }
        return (lMinute);
    }

    public void setTimeText(TextView textView, int hour, int minute)
    {
        try
        {
            String lTime;
            lTime="";
            if(hour < 10)
                lTime="0";
            lTime=lTime + hour;
            lTime=lTime + ":";
            if(minute < 10)
                lTime=lTime + "0";
            lTime=lTime + minute;
            textView.setText(lTime);
        }
        catch(Exception e)
        {
            ShowError("setTimeText", e.getMessage());
        }
    }

    public void handleTime(TextView txtTime, CheckBox chkTime, String title)
    {
        try
        {
            DialogTimePicker mTimePicker;
            int hour;
            int minute;

            hour=getHour(txtTime);
            minute=getMinute(txtTime);

            mTimePicker=new DialogTimePicker(this);
            mTimePicker.title=title;
            mTimePicker.chkTimeKnown=chkTime;
            mTimePicker.txtStartTime=txtTime;
            mTimePicker.hour=hour;
            mTimePicker.minute=minute;
            mTimePicker.timeKnown=chkTime.isChecked();
            mTimePicker.show();
        }
        catch(Exception e)
        {
            ShowError("handleTime", e.getMessage());
        }
    }

    @Override
    public void afterShow()
    {
        super.afterShow();

        try
        {
            if(action != null && action.equals("add"))
            {
                txtSchedName.setText("");
                SetImage("");
            }
            else
            {
                try(DatabaseAccess da = databaseAccess();)
                {
                    if(!da.getScheduleItem(holidayId, dayId, attractionId, attractionAreaId, scheduleId, eventScheduleItem))
                        return;
                }

                txtSchedName.setText(eventScheduleItem.schedName);

                SetImage(eventScheduleItem.schedPicture);

            }
        }
        catch(Exception e)
        {
            ShowError("afterShow", e.getMessage());
        }
    }

}
