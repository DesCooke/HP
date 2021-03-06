package com.example.des.hp.Schedule.Show;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.example.des.hp.R;
import com.example.des.hp.myutils.*;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
import static com.example.des.hp.myutils.MyMessages.myMessages;

public class ShowDetailsEdit extends ShowDetailsView implements View.OnClickListener
{

    //region Member variables
    public DialogWithEditTextFragment dialogWithEditTextFragment;
    public View.OnClickListener dwetOnOkClick;
    //endregion

    //region Constructors/Destructors
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            btnClear.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.VISIBLE);

            heartRating.setIsIndicator(false);
            scenicRating.setIsIndicator(false);
            thrillRating.setIsIndicator(false);

            if(action != null && action.equals("add"))
            {
                grpMenuFile.setVisibility(View.GONE);
                txtSchedName.setText("");
                heartRating.setRating(Float.valueOf(getString(R.string.default_rating)));
                scenicRating.setRating(Float.valueOf(getString(R.string.default_rating)));
                thrillRating.setRating(Float.valueOf(getString(R.string.default_rating)));
            }

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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        /* disable the menu entirely */
        return false;
    }
    //endregion

    //region OnClick Events
    public void onClick(View view)
    {
        try
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
                    departureClick(view);
                    break;

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

    public void checkInClick(View view)
    {
        try
        {
            handleTime(checkIn, chkCheckinKnown, "Select Check-in Time");
        }
        catch(Exception e)
        {
            ShowError("checkInClick", e.getMessage());
        }

    }

    public void departureClick(View view)
    {
        try
        {
            handleTime(departs, chkDepartureKnown, "Select Departure Time");
        }
        catch(Exception e)
        {
            ShowError("departureClick", e.getMessage());
        }

    }

    //endregion

    //region Saving
    public void saveSchedule(View view)
    {
        try
        {
            myMessages().ShowMessageShort("Saving Schedule");

            MyInt myInt=new MyInt();

            scheduleItem.schedName=txtSchedName.getText().toString();

            scheduleItem.schedPicture="";
            if(internalImageFilename.length() > 0)
                scheduleItem.schedPicture=internalImageFilename;
            scheduleItem.pictureAssigned=imageSet;
            scheduleItem.pictureChanged=imageChanged;
            scheduleItem.scheduleBitmap=null;
            if(imageSet)
                scheduleItem.scheduleBitmap=((BitmapDrawable) imageView.getDrawable()).getBitmap();

            scheduleItem.startTimeKnown=chkCheckinKnown.isChecked();
            scheduleItem.startHour=getHour(checkIn);
            scheduleItem.startMin=getMinute(checkIn);
            scheduleItem.endTimeKnown=chkDepartureKnown.isChecked();
            scheduleItem.endHour=getHour(departs);
            scheduleItem.endMin=getMinute(departs);
            if(scheduleItem.showItem != null)
            {
                scheduleItem.showItem.bookingReference=txtBookingRef.getText().toString();
                scheduleItem.showItem.heartRating=heartRating.getRating();
                scheduleItem.showItem.scenicRating=scenicRating.getRating();
                scheduleItem.showItem.thrillRating=thrillRating.getRating();
            }

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

                scheduleItem.schedType=getResources().getInteger(R.integer.schedule_type_show);

                scheduleItem.showItem.holidayId=holidayId;
                scheduleItem.showItem.dayId=dayId;
                scheduleItem.showItem.attractionId=attractionId;
                scheduleItem.showItem.attractionAreaId=attractionAreaId;
                scheduleItem.showItem.scheduleId=scheduleItem.scheduleId;
                scheduleItem.showItem.bookingReference=txtBookingRef.getText().toString();
                scheduleItem.showItem.heartRating=heartRating.getRating();
                scheduleItem.showItem.scenicRating=scenicRating.getRating();
                scheduleItem.showItem.thrillRating=thrillRating.getRating();

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

}
