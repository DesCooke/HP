package com.example.des.hp.Schedule.Restaurant;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.RadioButton;

import com.example.des.hp.R;
import com.example.des.hp.myutils.*;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
import static com.example.des.hp.myutils.MyMessages.myMessages;

public class RestaurantDetailsEdit extends RestaurantDetailsView implements View.OnClickListener
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

            if(action != null && action.equals("add"))
            {
                grpMenuFile.setVisibility(View.GONE);
                txtSchedName.setText("");
            }

            grpStartTime.setOnClickListener(this);
            grpBookingRef.setOnClickListener(this);
            grpSchedName.setOnClickListener(this);
            grpEndTime.setOnClickListener(this);
            imageView.setOnClickListener(this);
            radUnknown.setOnClickListener(this);
            radWalkIn.setOnClickListener(this);
            radOnTheDay.setOnClickListener(this);
            rad180Days.setOnClickListener(this);
            radBooked.setOnClickListener(this);
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


                case R.id.radUnknown:
                    selectReservationType(view);
                    break;


                case R.id.radWalkIn:
                    selectReservationType(view);
                    break;

                case R.id.radOnTheDay:
                    selectReservationType(view);
                    break;

                case R.id.rad180Days:
                    selectReservationType(view);
                    break;
            }
        }
        catch(Exception e)
        {
            ShowError("onClick", e.getMessage());
        }

    }

    public void selectReservationType(View view)
    {
        try
        {
            radUnknown.setChecked(false);
            radWalkIn.setChecked(false);
            radOnTheDay.setChecked(false);
            rad180Days.setChecked(false);
            radBooked.setChecked(false);
            ((RadioButton) view).setChecked(true);
        }
        catch(Exception e)
        {
            ShowError("selectReservationType", e.getMessage());
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
            handleTime(checkIn, chkCheckinKnown, "Select Reservation Time");
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
            handleTime(departs, chkDepartureKnown, "Select Finish Time");
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

            scheduleItem.restaurantItem.reservationType=0;
            if(radWalkIn.isChecked())
                scheduleItem.restaurantItem.reservationType=1;
            if(radOnTheDay.isChecked())
                scheduleItem.restaurantItem.reservationType=2;
            if(rad180Days.isChecked())
                scheduleItem.restaurantItem.reservationType=3;
            if(radBooked.isChecked())
                scheduleItem.restaurantItem.reservationType=4;

            scheduleItem.startTimeKnown=chkCheckinKnown.isChecked();
            scheduleItem.startHour=getHour(checkIn);
            scheduleItem.startMin=getMinute(checkIn);
            scheduleItem.endTimeKnown=chkDepartureKnown.isChecked();
            scheduleItem.endHour=getHour(departs);
            scheduleItem.endMin=getMinute(departs);
            scheduleItem.restaurantItem.bookingReference=txtBookingRef.getText().toString();

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

                scheduleItem.schedType=getResources().getInteger(R.integer.schedule_type_restaurant);

                scheduleItem.restaurantItem.holidayId=holidayId;
                scheduleItem.restaurantItem.dayId=dayId;
                scheduleItem.restaurantItem.attractionId=attractionId;
                scheduleItem.restaurantItem.attractionAreaId=attractionAreaId;
                scheduleItem.restaurantItem.scheduleId=scheduleItem.scheduleId;
                scheduleItem.restaurantItem.bookingReference=txtBookingRef.getText().toString();

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
    //endregion

}
