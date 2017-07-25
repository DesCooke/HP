package com.example.des.hp.Schedule.Park;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.TextView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.R;
import com.example.des.hp.myutils.*;
import com.example.des.hp.Schedule.*;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ParkDetailsEdit extends BaseActivity
{

    public DatabaseAccess databaseAccess;
    private final int SELECT_PHOTO=1;
    private ImageView imageViewSmall;
    private String originalFileName;
    private String action;
    public int holidayId;
    public int dayId;
    public int attractionId;
    public int attractionAreaId;
    public int scheduleId;
    public DateUtils dateUtils;
    public LinearLayout grpStartDate;
    public TextView txtSchedName;
    public String holidayName;
    public ActionBar actionBar;
    public ScheduleItem scheduleItem;
    public ParkItem parkItem;
    public CheckBox cbPicturePicked;
    private ImageUtils imageUtils;
    public CheckBox chkCheckinKnown;
    public TextView checkIn;
    public CheckBox chkDepartureKnown;
    public TextView departs;
    public TextView txtBookingRef;
    public MyMessages myMessages;
    public DialogWithEditTextFragment dialogWithEditTextFragment;
    public View.OnClickListener dwetOnOkClick;

    public void pickImage(View view)
    {
        try
        {
            Intent photoPickerIntent=new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, SELECT_PHOTO);
        }
        catch(Exception e)
        {
            ShowError("pickImage", e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent)
    {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        try
        {
            switch(requestCode)
            {
                case SELECT_PHOTO:
                    if(resultCode == RESULT_OK)
                    {
                        try
                        {
                            MyBitmap myBitmap=new MyBitmap();
                            Boolean lRetCode=imageUtils.ScaleBitmapFromUrl(imageReturnedIntent.getData(), getContentResolver(), myBitmap);
                            if(lRetCode == false)
                                return;

                            // assign new bitmap and set scale type
                            imageViewSmall.setImageBitmap(myBitmap.Value);

                            cbPicturePicked.setChecked(true);

                            scheduleItem.pictureChanged=true;


                        }
                        catch(Exception e)
                        {
                            ShowError("onActivityResult-selectphoto", e.getMessage());
                        }
                    }
            }
        }
        catch(Exception e)
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
        catch(Exception e)
        {
            ShowError("clearImage", e.getMessage());
        }
    }

    public void btnClearImage(View view)
    {
        try
        {
            clearImage(view);
            scheduleItem.pictureChanged=true;
        }
        catch(Exception e)
        {
            ShowError("btnClearImage", e.getMessage());
        }
    }

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
            dwetOnOkClick=new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    SchedNamePicked(view);
                }
            };

            dialogWithEditTextFragment=DialogWithEditTextFragment.newInstance(getFragmentManager(),     // for the transaction bit
                "hihi",            // unique name for this dialog type
                "Park",    // form caption
                "Park",             // form message
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


    public void BookingRefPicked(View view)
    {
        try
        {
            txtBookingRef.setText(dialogWithEditTextFragment.getFinalText());

            dialogWithEditTextFragment.dismiss();
        }
        catch(Exception e)
        {
            ShowError("BookingRefPicked", e.getMessage());
        }
    }

    public void pickBookingRef(View view)
    {
        try
        {
            dwetOnOkClick=new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    BookingRefPicked(view);
                }
            };

            dialogWithEditTextFragment=DialogWithEditTextFragment.newInstance(getFragmentManager(),     // for the transaction bit
                "hihi",            // unique name for this dialog type
                "Booking Ref",    // form caption
                "Booking Ref",             // form message
                R.drawable.attachment, txtBookingRef.getText().toString(), // initial text
                dwetOnOkClick, this, false
            );

            dialogWithEditTextFragment.showIt();
        }
        catch(Exception e)
        {
            ShowError("pickBookingRef", e.getMessage());
        }
    }

    public void saveSchedule(View view)
    {
        try
        {
            MyInt myInt=new MyInt();

            myMessages.ShowMessageShort("Saving Schedule");

            scheduleItem.pictureAssigned=cbPicturePicked.isChecked();
            scheduleItem.schedName=txtSchedName.getText().toString();
            scheduleItem.scheduleBitmap=null;
            if(scheduleItem.pictureAssigned)
                scheduleItem.scheduleBitmap=((BitmapDrawable) imageViewSmall.getDrawable()).getBitmap();


            if(action.equals("add"))
            {
                scheduleItem.holidayId=holidayId;
                scheduleItem.dayId=dayId;
                scheduleItem.attractionId=attractionId;
                scheduleItem.attractionAreaId=attractionAreaId;

                if(!databaseAccess.getNextScheduleId(holidayId, dayId, attractionId, attractionAreaId, myInt))
                    return;
                scheduleItem.scheduleId=myInt.Value;

                if(!databaseAccess.getNextScheduleSequenceNo(holidayId, dayId, attractionId, attractionAreaId, myInt))
                    return;
                scheduleItem.sequenceNo=myInt.Value;

                scheduleItem.startTimeKnown=chkCheckinKnown.isChecked();
                scheduleItem.startHour=getHour(checkIn);
                scheduleItem.startMin=getMinute(checkIn);
                scheduleItem.endTimeKnown=chkDepartureKnown.isChecked();
                scheduleItem.endHour=getHour(departs);
                scheduleItem.endMin=getMinute(departs);
                scheduleItem.schedType=getResources().getInteger(R.integer.schedule_type_park);


                parkItem.holidayId=holidayId;
                parkItem.dayId=dayId;
                parkItem.attractionId=attractionId;
                parkItem.attractionAreaId=attractionAreaId;
                parkItem.scheduleId=scheduleItem.scheduleId;
                parkItem.bookingReference=txtBookingRef.getText().toString();

                scheduleItem.parkItem=parkItem;

                if(!databaseAccess.addScheduleItem(scheduleItem))
                    return;
            }

            if(action.equals("edit"))
            {
                scheduleItem.startTimeKnown=chkCheckinKnown.isChecked();
                scheduleItem.startHour=getHour(checkIn);
                scheduleItem.startMin=getMinute(checkIn);
                scheduleItem.endTimeKnown=chkDepartureKnown.isChecked();
                scheduleItem.endHour=getHour(departs);
                scheduleItem.endMin=getMinute(departs);
                if(scheduleItem.parkItem != null)
                {
                    scheduleItem.parkItem.bookingReference=txtBookingRef.getText().toString();
                }

                if(!databaseAccess.updateScheduleItem(scheduleItem))
                    return;
            }

            finish();
        }
        catch(Exception e)
        {
            ShowError("SaveSchedule", e.getMessage());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            setContentView(R.layout.activity_park_details_edit);

            actionBar=getSupportActionBar();
            databaseAccess=new DatabaseAccess(this);
            dateUtils=new DateUtils(this);
            imageUtils=new ImageUtils(this);
            myMessages=new MyMessages(this);

            cbPicturePicked=(CheckBox) findViewById(R.id.picturePicked);
            imageViewSmall=(ImageView) findViewById(R.id.imageViewSmall);
            txtSchedName=(TextView) findViewById(R.id.txtSchedName);
            checkIn=(TextView) findViewById(R.id.txtCheckin);
            departs=(TextView) findViewById(R.id.txtDeparture);
            txtBookingRef=(TextView) findViewById(R.id.txtBookingRef);
            chkCheckinKnown=(CheckBox) findViewById(R.id.chkCheckinKnown);
            chkDepartureKnown=(CheckBox) findViewById(R.id.chkDepartureKnown);

            clearImage(null);
            Bundle extras=getIntent().getExtras();
            if(extras != null)
            {
                String title=extras.getString("TITLE");
                String subtitle=extras.getString("SUBTITLE");
                actionBar.setTitle(title);
                actionBar.setSubtitle(subtitle);

                holidayId=extras.getInt("HOLIDAYID");
                dayId=extras.getInt("DAYID");
                attractionId=extras.getInt("ATTRACTIONID");
                attractionAreaId=extras.getInt("ATTRACTIONAREAID");
                holidayName=extras.getString("HOLIDAYNAME");

                action=extras.getString("ACTION");
                if(action != null && action.equals("add"))
                {
                    scheduleItem=new ScheduleItem();
                    parkItem=new ParkItem();

                    txtSchedName.setText("");
                    cbPicturePicked.setChecked(false);
                }
                if(action != null && action.equals("edit"))
                {
                    scheduleId=extras.getInt("SCHEDULEID");
                    scheduleItem=new ScheduleItem();
                    if(!databaseAccess.getScheduleItem(holidayId, dayId, attractionId, attractionAreaId, scheduleId, scheduleItem))
                        return;

                    chkCheckinKnown.setChecked(scheduleItem.startTimeKnown);
                    setTimeText(checkIn, scheduleItem.startHour, scheduleItem.startMin);

                    chkDepartureKnown.setChecked(scheduleItem.endTimeKnown);
                    setTimeText(departs, scheduleItem.endHour, scheduleItem.endMin);

                    txtSchedName.setText(scheduleItem.schedName);
                    txtBookingRef.setText(scheduleItem.parkItem.bookingReference);

                    originalFileName=scheduleItem.schedPicture;

                    if(imageUtils.getPageHeaderImage(this, scheduleItem.schedPicture, imageViewSmall) == false)
                        return;

                    cbPicturePicked.setChecked(scheduleItem.pictureAssigned);

                }
            }
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }

    }

    private int getHour(TextView textview)
    {
        try
        {
            String[] sarray=textview.getText().toString().split(":");
            int lHour=Integer.parseInt(sarray[0]);
            if(lHour < 0)
                lHour=0;
            if(lHour > 23)
                lHour=23;
            return (lHour);
        }
        catch(Exception e)
        {
            ShowError("getHour", e.getMessage());
        }
        return (0);
    }

    private int getMinute(TextView textview)
    {
        try
        {
            String[] sarray=textview.getText().toString().split(":");
            int lMinute=Integer.parseInt(sarray[1]);
            if(lMinute < 0)
                lMinute=0;
            if(lMinute > 59)
                lMinute=59;
            return (lMinute);
        }
        catch(Exception e)
        {
            ShowError("getMinute", e.getMessage());
        }
        return (0);
    }

    public void checkInClick(View view)
    {
        handleTime(checkIn, chkCheckinKnown, "Select Check-in Time");
    }

    public void departureClick(View view)
    {
        handleTime(departs, chkDepartureKnown, "Select Departure Time");
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

}
