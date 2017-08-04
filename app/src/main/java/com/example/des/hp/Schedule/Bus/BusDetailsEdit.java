package com.example.des.hp.Schedule.Bus;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.des.hp.R;
import com.example.des.hp.myutils.*;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
import static com.example.des.hp.myutils.MyMessages.myMessages;

public class BusDetailsEdit extends BusDetailsView implements View.OnClickListener
{

    public DialogWithEditTextFragment dialogWithEditTextFragment;
    public View.OnClickListener dwetOnOkClick;

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
            myMessages().ShowMessageShort("Saving Schedule");

            MyInt myInt=new MyInt();

            scheduleItem.schedName=txtSchedName.getText().toString();

            scheduleItem.pictureAssigned=imageSet;
            scheduleItem.pictureChanged=imageChanged;
            scheduleItem.scheduleBitmap=null;
            if(imageSet)
                scheduleItem.scheduleBitmap=((BitmapDrawable) imageView.getDrawable()).getBitmap();
            scheduleItem.startTimeKnown=chkCheckinKnown.isChecked();
            scheduleItem.startHour=getHour(checkIn);
            scheduleItem.startMin=getMinute(checkIn);
            scheduleItem.endTimeKnown=chkArriveKnown.isChecked();
            scheduleItem.endHour=getHour(arrives);
            scheduleItem.endMin=getMinute(arrives);
            scheduleItem.busItem.bookingReference=txtBookingRef.getText().toString();


            if(action.equals("add"))
            {
                scheduleItem.holidayId=holidayId;
                scheduleItem.dayId=dayId;
                scheduleItem.attractionId=attractionId;
                scheduleItem.attractionAreaId=attractionAreaId;
                if(!databaseAccess().getNextScheduleId(holidayId, dayId, attractionId, attractionAreaId, myInt))
                    return;
                scheduleItem.scheduleId=myInt.Value;

                if(!databaseAccess().getNextScheduleSequenceNo(holidayId, dayId, attractionId, attractionAreaId, myInt))
                    return;
                scheduleItem.sequenceNo=myInt.Value;

                scheduleItem.schedType=getResources().getInteger(R.integer.schedule_type_bus);

                scheduleItem.busItem.holidayId=holidayId;
                scheduleItem.busItem.dayId=dayId;
                scheduleItem.busItem.attractionId=attractionId;
                scheduleItem.busItem.attractionAreaId=attractionAreaId;
                scheduleItem.busItem.scheduleId=scheduleItem.scheduleId;

                if(!databaseAccess().addScheduleItem(scheduleItem))
                    return;
            }

            if(action.equals("edit"))
            {
                if(!databaseAccess().updateScheduleItem(scheduleItem))
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
            btnClear.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.VISIBLE);

            if(action != null && action.equals("add"))
                txtSchedName.setText("");

            grpStartTime.setOnClickListener(this);
            grpBookingRef.setOnClickListener(this);
            grpSchedName.setOnClickListener(this);
            grpEndTime.setOnClickListener(this);
            imageView.setOnClickListener(this);
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
    }

    public void onClick(View view)
    {
        switch(view.getId())
        {

            case R.id.grpStartTime:
                checkInClick(view);
                break;

            case R.id.grpBookingRef:
                pickBookingRef(view);
                break;

            case R.id.grpSchedName:
                pickSchedName(view);
                break;

            case R.id.grpEndTime:
                arrivesClick(view);
                break;

            case R.id.imageViewSmall:
                pickImage(view);
                break;
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
                    deleteSchedule();
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

    public void checkInClick(View view)
    {
        handleTime(checkIn, chkCheckinKnown, "Bus Arrives");
    }

    public void arrivesClick(View view)
    {
        handleTime(arrives, chkArriveKnown, "Journey Ends");
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        /* disable the menu entirely */
        return false;
    }
}
