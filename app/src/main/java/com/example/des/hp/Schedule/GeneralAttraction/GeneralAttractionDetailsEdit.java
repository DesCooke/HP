package com.example.des.hp.Schedule.GeneralAttraction;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.R;
import com.example.des.hp.myutils.*;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
import static com.example.des.hp.myutils.MyMessages.myMessages;

public class GeneralAttractionDetailsEdit extends GeneralAttractionDetailsView implements View.OnClickListener
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
            imageChanged=false;
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

            grpSchedName.setOnClickListener(this);
            grpAttractionType.setOnClickListener(this);
            grpBookingReference.setOnClickListener(this);
            grpFlightNo.setOnClickListener(this);
            grpTerminal.setOnClickListener(this);
            grpDeparts.setOnClickListener(this);
            grpShow.setOnClickListener(this);
            grpStartTime.setOnClickListener(this);
            grpEndTime.setOnClickListener(this);
            grpPickUp.setOnClickListener(this);
            grpDropOff.setOnClickListener(this);
            grpCheckIn.setOnClickListener(this);
            grpArrival.setOnClickListener(this);
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

                case R.id.grpSchedName:
                    pickSchedName(view);
                    break;
                case R.id.grpAttractionType:
                    EnterString("Attraction Type", "Enter Attraction Type", txtAttractionType);
                    break;
                case R.id.grpBookingReference:
                    EnterString("Booking Reference", "Enter Booking Reference", txtBookingReference);
                    break;
                case R.id.grpFlightNo:
                    EnterString("Flight NO", "Enter Flight No", txtFlightNo);
                    break;
                case R.id.grpTerminal:
                    EnterString("Terminal", "Enter Terminal", txtTerminal);
                    break;
                case R.id.grpDeparts:
                    handleTime(txtDeparts, chkDepartsKnown, "Select Departure Time");
                    break;
                case R.id.grpShow:
                    handleTime(txtShow, chkShowKnown, "Select Show Time");
                    break;
                case R.id.grpStartTime:
                    handleTime(txtStart, chkStartKnown, "Select Start Time");
                    break;
                case R.id.grpEndTime:
                    handleTime(txtEnd, chkEndKnown, "Select End Time");
                    break;
                case R.id.grpPickUp:
                    handleTime(txtPickUp, chkPickUpKnown, "Select PickUp Time");
                    break;
                case R.id.grpDropOff:
                    handleTime(txtDropOff, chkDropOffKnown, "Select DropOff Time");
                    break;
                case R.id.grpCheckIn:
                    handleTime(txtCheckIn, chkCheckInKnown, "Select CheckIn Time");
                    break;
                case R.id.grpArrival:
                    handleTime(txtArrival, chkArrivalKnown, "Select Arrival Time");
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
                case R.id.radBooked:
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

    public void EnterString(String formCaption, String formMessage, TextView textView)
    {
        dialogWithEditTextFragment=
                DialogWithEditTextFragment.newInstance(getSupportFragmentManager(),     // for the transaction bit
                        "hihi1",            // unique name for this dialog type
                        formCaption,    // form caption
                        formMessage,             // form message
                        R.drawable.attachment,
                        textView.getText().toString(), // initial text
                        new View.OnClickListener()
                        {
                            public void onClick(View view)
                            {
                                textView.setText(dialogWithEditTextFragment.getFinalText());
                                dialogWithEditTextFragment.dismiss();
                            }
                        },
                        this,
                        false
                );

        dialogWithEditTextFragment.showIt();
    }
    //endregion

    //region Saving
    public void saveSchedule(View view)
    {
        try(DatabaseAccess da = databaseAccess();)
        {
            myMessages().ShowMessageShort("Saving Schedule");

            MyInt myInt=new MyInt();

            scheduleItem.schedName=txtSchedName.getText().toString();

            if(imageChanged) {
                scheduleItem.schedPicture = "";
                if (internalImageFilename.length() > 0)
                    scheduleItem.schedPicture = internalImageFilename;
            }
            scheduleItem.pictureAssigned=imageSet;
            scheduleItem.pictureChanged=imageChanged;
            scheduleItem.scheduleBitmap=null;
            if(imageSet)
                scheduleItem.scheduleBitmap=((BitmapDrawable) imageView.getDrawable()).getBitmap();

            scheduleItem.generalAttractionItem.ReservationType=0;
            if(radWalkIn.isChecked())
                scheduleItem.generalAttractionItem.ReservationType=1;
            if(radOnTheDay.isChecked())
                scheduleItem.generalAttractionItem.ReservationType=2;
            if(rad180Days.isChecked())
                scheduleItem.generalAttractionItem.ReservationType=3;
            if(radBooked.isChecked())
                scheduleItem.generalAttractionItem.ReservationType=4;

            scheduleItem.startTimeKnown=false;
            scheduleItem.startHour=0;
            scheduleItem.startMin=0;
            scheduleItem.endTimeKnown=false;
            scheduleItem.endHour=0;
            scheduleItem.endMin=0;
            scheduleItem.generalAttractionItem.heartRating=heartRating.getRating();
            scheduleItem.generalAttractionItem.scenicRating=scenicRating.getRating();
            scheduleItem.generalAttractionItem.thrillRating=thrillRating.getRating();
            scheduleItem.generalAttractionItem.AttractionType=txtAttractionType.getText().toString();
            scheduleItem.generalAttractionItem.BookingReference=txtBookingReference.getText().toString();
            scheduleItem.generalAttractionItem.FlightNo=txtFlightNo.getText().toString();
            scheduleItem.generalAttractionItem.Terminal=txtTerminal.getText().toString();
            scheduleItem.generalAttractionItem.DepartsKnown = chkDepartsKnown.isChecked();
            scheduleItem.generalAttractionItem.DepartsHour = getHour(txtDeparts);
            scheduleItem.generalAttractionItem.DepartsMin = getMinute(txtDeparts);
            scheduleItem.generalAttractionItem.ShowKnown = chkShowKnown.isChecked();
            scheduleItem.generalAttractionItem.ShowHour = getHour(txtShow);
            scheduleItem.generalAttractionItem.ShowMin = getMinute(txtShow);
            scheduleItem.generalAttractionItem.PickUpKnown = chkPickUpKnown.isChecked();
            scheduleItem.generalAttractionItem.PickUpHour = getHour(txtPickUp);
            scheduleItem.generalAttractionItem.PickUpMin = getMinute(txtPickUp);
            scheduleItem.generalAttractionItem.DropOffKnown = chkDropOffKnown.isChecked();
            scheduleItem.generalAttractionItem.DropOffHour = getHour(txtDropOff);
            scheduleItem.generalAttractionItem.DropOffMin = getMinute(txtDropOff);
            scheduleItem.generalAttractionItem.CheckInKnown = chkCheckInKnown.isChecked();
            scheduleItem.generalAttractionItem.CheckInHour = getHour(txtCheckIn);
            scheduleItem.generalAttractionItem.CheckInMin = getMinute(txtCheckIn);
            scheduleItem.generalAttractionItem.ArrivalKnown = chkArrivalKnown.isChecked();
            scheduleItem.generalAttractionItem.ArrivalHour = getHour(txtArrival);
            scheduleItem.generalAttractionItem.ArrivalMin = getMinute(txtArrival);
            scheduleItem.startTimeKnown = chkStartKnown.isChecked();
            scheduleItem.startHour = getHour(txtStart);
            scheduleItem.startMin = getMinute(txtStart);
            scheduleItem.endTimeKnown = chkEndKnown.isChecked();
            scheduleItem.endHour = getHour(txtEnd);
            scheduleItem.endMin = getMinute(txtEnd);

            if(action.equals("add"))
            {
                scheduleItem.holidayId=holidayId;
                scheduleItem.dayId=dayId;
                scheduleItem.attractionId=attractionId;
                scheduleItem.attractionAreaId=attractionAreaId;

                if(!da.getNextScheduleId(holidayId, dayId, attractionId, attractionAreaId, myInt))
                    return;
                scheduleItem.scheduleId=myInt.Value;

                if(!da.getNextScheduleSequenceNo(holidayId, dayId, attractionId, attractionAreaId, myInt))
                    return;
                scheduleItem.sequenceNo=myInt.Value;

                scheduleItem.schedType=getResources().getInteger(R.integer.schedule_type_generalattraction);

                scheduleItem.generalAttractionItem.holidayId=holidayId;
                scheduleItem.generalAttractionItem.dayId=dayId;
                scheduleItem.generalAttractionItem.attractionId=attractionId;
                scheduleItem.generalAttractionItem.attractionAreaId=attractionAreaId;
                scheduleItem.generalAttractionItem.scheduleId=scheduleItem.scheduleId;

                if(!da.addScheduleItem(scheduleItem))
                    return;
            }

            if(action.equals("edit"))
            {

                if(!da.updateScheduleItem(scheduleItem))
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
