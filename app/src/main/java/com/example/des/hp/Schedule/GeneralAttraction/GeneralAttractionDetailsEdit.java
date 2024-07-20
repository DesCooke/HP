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
            alwaysShowToolBar=true;
            btnClear.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.VISIBLE);

            heartRating.setIsIndicator(false);
            scenicRating.setIsIndicator(false);
            thrillRating.setIsIndicator(false);

            if(action != null && action.equals("add"))
            {
                alwaysShowToolBar=false;
                txtSchedName.setText("");
                heartRating.setRating(Float.parseFloat(getString(R.string.default_rating)));
                scenicRating.setRating(Float.parseFloat(getString(R.string.default_rating)));
                thrillRating.setRating(Float.parseFloat(getString(R.string.default_rating)));
            }

            grpSchedName.setOnClickListener(this);
            grpAttractionType.setOnClickListener(this);
            grpBookingReference.setOnClickListener(this);
            grpFlightNo.setOnClickListener(this);
            grpTerminal.setOnClickListener(this);
            grpStartTime.setOnClickListener(this);
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
        try {
            int id = view.getId();
            if (id == R.id.grpSchedName)
                pickSchedName(view);
            if (id == R.id.grpAttractionType)
                EnterString("Attraction Type", "Enter Attraction Type", txtAttractionType);
            if (id == R.id.grpBookingReference)
                EnterString("Booking Reference", "Enter Booking Reference", txtBookingReference);
            if (id == R.id.grpFlightNo)
                EnterString("Flight NO", "Enter Flight No", txtFlightNo);
            if (id == R.id.grpTerminal)
                EnterString("Terminal", "Enter Terminal", txtTerminal);
            if (id == R.id.grpStartTime)
                handleTime(txtStart, chkStartKnown, "Select Start Time");
            if (id == R.id.grpEndTime)
                handleTime(txtEnd, chkEndKnown, "Select End Time");
            if (id == R.id.imageViewSmall)
                pickImage(view);
            if (id == R.id.radUnknown)
                selectReservationType(view);
            if (id == R.id.radWalkIn)
                selectReservationType(view);
            if (id == R.id.radOnTheDay)
                selectReservationType(view);
            if (id == R.id.rad180Days)
                selectReservationType(view);
            if (id == R.id.radBooked)
                selectReservationType(view);
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
                        view -> {
                            textView.setText(dialogWithEditTextFragment.getFinalText());
                            dialogWithEditTextFragment.dismiss();
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
        try(DatabaseAccess da = databaseAccess())
        {
            myMessages().ShowMessageShort("Saving Schedule");

            MyInt myInt=new MyInt();

            scheduleItem.schedName=txtSchedName.getText().toString();

            if(imageChanged) {
                scheduleItem.schedPicture = "";
                if (!internalImageFilename.isEmpty())
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
