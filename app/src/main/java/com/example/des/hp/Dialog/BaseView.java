package com.example.des.hp.Dialog;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBar;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.TextView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.ExtraFiles.ExtraFilesDetailsList;
import com.example.des.hp.R;
import com.example.des.hp.Schedule.Ride.RideDetailsEdit;
import com.example.des.hp.ScheduleArea.ScheduleAreaList;
import com.example.des.hp.myutils.*;
import com.example.des.hp.Schedule.*;
import com.example.des.hp.thirdpartyutils.BadgeView;
import com.example.des.hp.Notes.NoteItem;
import com.example.des.hp.Notes.NoteView;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

public class BaseView extends BaseActivity
{

    private final int SELECT_PHOTO=1;
    private final int MOVEITEM=2;
    private ImageView imageViewSmall;
    private String originalFileName;
    public DateUtils dateUtils;
    public LinearLayout grpStartDate;
    public TextView txtSchedName;
    public String holidayName;
    public ScheduleItem scheduleItem;
    public CheckBox cbPicturePicked;
    private ImageUtils imageUtils;
    public CheckBox chkCheckinKnown;
    public TextView checkIn;
    public CheckBox chkArriveKnown;
    public TextView arrives;
    public TextView txtBookingRef;
    public DialogWithEditTextFragment dialogWithEditTextFragment;
    public View.OnClickListener dwetOnOkClick;

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        try
        {
            switch(requestCode)
            {
                case MOVEITEM:
                    if(resultCode == RESULT_OK)
                    {
                        try
                        {
                            scheduleItem.dayId=data.getIntExtra("DAYID", 0);
                            scheduleItem.attractionId=data.getIntExtra("ATTRACTIONID", 0);
                            scheduleItem.attractionAreaId=data.getIntExtra("ATTRACTIONAREAID", 0);
                            databaseAccess().updateScheduleItem(scheduleItem);
                            finish();
                        }
                        catch(Exception e)
                        {
                            ShowError("onActivityResult-MOVEITEM", e.getMessage());
                        }
                    }
                    break;

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
            dateUtils=new DateUtils(this);
            imageUtils=new ImageUtils(this);

            cbPicturePicked=(CheckBox) findViewById(R.id.picturePicked);
            imageViewSmall=(ImageView) findViewById(R.id.imageViewSmall);
            txtSchedName=(TextView) findViewById(R.id.txtSchedName);
            checkIn=(TextView) findViewById(R.id.txtCheckin);
            arrives=(TextView) findViewById(R.id.txtArrival);
            txtBookingRef=(TextView) findViewById(R.id.txtBookingRef);
            chkCheckinKnown=(CheckBox) findViewById(R.id.chkCheckinKnown);
            chkArriveKnown=(CheckBox) findViewById(R.id.chkArrivalKnown);
        }
        catch(Exception e)
        {
            ShowError("afterCreate", e.getMessage());
        }
    }

    @Override
    public void afterShow()
    {
        super.afterShow();
        try
        {
            clearImage(null);

            originalFileName=scheduleItem.schedPicture;
            if(originalFileName.length()>0)
            {
                if(imageUtils.getPageHeaderImage(this, scheduleItem.schedPicture, imageViewSmall) == false)
                    return;

                cbPicturePicked.setChecked(scheduleItem.pictureAssigned);
            }
        }
        catch(Exception e)
        {
            ShowError("showForm", e.getMessage());
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        try
        {
            showForm();
        }
        catch(Exception e)
        {
            ShowError("onResume", e.getMessage());
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        try
        {
            switch(item.getItemId())
            {
                case R.id.action_delete_bus:
                    deleteBus();
                    return true;
                case R.id.action_edit_bus:
                    editBus();
                    return true;
                case R.id.action_move:
                    move();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
        catch(Exception e)
        {
            ShowError("onOptionsItemSelected", e.getMessage());
        }
        return true;
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

    public boolean onCreateOptionsMenu(Menu menu)
    {
        try
        {
            MenuInflater inflater=getMenuInflater();
            inflater.inflate(R.menu.busdetailsformmenu, menu);
        }
        catch(Exception e)
        {
            ShowError("onCreateOptionsMenu", e.getMessage());
        }

        return true;
    }

    public void deleteBus()
    {
        try
        {
            if(!databaseAccess().deleteScheduleItem(scheduleItem))
                return;

            finish();
        }
        catch(Exception e)
        {
            ShowError("deleteBus", e.getMessage());
        }
    }

    public void editBus()
    {
        try
        {
            Intent intent=new Intent(getApplicationContext(), BusDetailsEdit.class);
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
            ShowError("editBus", e.getMessage());
        }
    }


    private int getHour(TextView textview)
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
            ShowError("onCreate", e.getMessage());
        }
        return (lHour);
    }

    private int getMinute(TextView textview)
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

    private void handleTime(TextView txtTime, CheckBox chkTime, String title)
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

    private void setTimeText(TextView textView, int hour, int minute)
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

    @Override
    public int getInfoId()
    {
        return (scheduleItem.infoId);
    }

    public void setNoteId(int pNoteId)
    {
        scheduleItem.noteId=pNoteId;
        if(!databaseAccess().updateScheduleItem(scheduleItem))
            return;
    }

    @Override
    public int getNoteId()
    {
        return (scheduleItem.noteId);
    }

    @Override
    public void setInfoId(int pInfoId)
    {
        scheduleItem.infoId=pInfoId;
        if(!databaseAccess().updateScheduleItem(scheduleItem))
            return;
    }


}
