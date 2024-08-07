package com.example.des.hp.Event;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.R;
import com.example.des.hp.myutils.*;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
import static com.example.des.hp.myutils.MyMessages.myMessages;

public class EventDetailsEdit extends EventDetailsView implements View.OnClickListener
{
    //region Member variables
    public DialogWithEditTextFragment dialogWithEditTextFragment;
    public View.OnClickListener dwetOnOkClick;

    // Buttons
    public ImageButton btnClear;
    public ImageView btnDelete;
    public Button btnSave;
    //endregion

    //region Constructors/Destructors
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            imageChanged=false;

            btnDelete = findViewById(R.id.my_toolbar_delete);
            btnClear = findViewById(R.id.btnClear);
            btnSave = findViewById(R.id.btnSave);

            btnClear.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.VISIBLE);

            btnDelete.setOnClickListener(view -> deleteSchedule());
            btnUrl1.setOnClickListener(view -> editUrl1());
            btnUrl2.setOnClickListener(view -> editUrl2());
            btnUrl3.setOnClickListener(view -> editUrl3());

            ShowToolbarDelete();

            heartRating.setIsIndicator(false);
            scenicRating.setIsIndicator(false);
            thrillRating.setIsIndicator(false);

            if(action != null && action.equals("add"))
            {
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

    public void editUrl1(){
        EnterStringToMyString("Map Url", "Enter MapUrl", Url1);
    }

    public void editUrl2(){
        EnterStringToMyString("Info Url 1", "Enter Info Url 1", Url2);
    }

    public void editUrl3(){
        EnterStringToMyString("Info Url 2", "Enter Info Url 2", Url3);
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

    public void EnterStringToMyString(String formCaption, String formMessage, MyString theString)
    {
        dialogWithEditTextFragment=
                DialogWithEditTextFragment.newInstance(getSupportFragmentManager(),     // for the transaction bit
                        "hihi2",            // unique name for this dialog type
                        formCaption,    // form caption
                        formMessage,             // form message
                        R.drawable.attachment,
                        theString.Value, // initial text
                        view -> {
                            theString.Value = dialogWithEditTextFragment.getFinalText();
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

            eventScheduleItem.schedName=txtSchedName.getText().toString();

            if(imageChanged) {
                eventScheduleItem.schedPicture = "";
                if (!internalImageFilename.isEmpty())
                    eventScheduleItem.schedPicture = internalImageFilename;
            }
            eventScheduleItem.pictureAssigned=imageSet;
            eventScheduleItem.pictureChanged=imageChanged;
            eventScheduleItem.scheduleBitmap=null;
            if(imageSet)
                eventScheduleItem.scheduleBitmap=((BitmapDrawable) imageView.getDrawable()).getBitmap();

            eventScheduleItem.url1 = Url1.Value;
            eventScheduleItem.url2 = Url2.Value;
            eventScheduleItem.url3 = Url3.Value;

            eventScheduleItem.eventScheduleDetailItem.ReservationType=0;
            if(radWalkIn.isChecked())
                eventScheduleItem.eventScheduleDetailItem.ReservationType=1;
            if(radOnTheDay.isChecked())
                eventScheduleItem.eventScheduleDetailItem.ReservationType=2;
            if(rad180Days.isChecked())
                eventScheduleItem.eventScheduleDetailItem.ReservationType=3;
            if(radBooked.isChecked())
                eventScheduleItem.eventScheduleDetailItem.ReservationType=4;

            eventScheduleItem.startTimeKnown=false;
            eventScheduleItem.startHour=0;
            eventScheduleItem.startMin=0;
            eventScheduleItem.endTimeKnown=false;
            eventScheduleItem.endHour=0;
            eventScheduleItem.endMin=0;
            eventScheduleItem.eventScheduleDetailItem.heartRating=heartRating.getRating();
            eventScheduleItem.eventScheduleDetailItem.scenicRating=scenicRating.getRating();
            eventScheduleItem.eventScheduleDetailItem.thrillRating=thrillRating.getRating();
            eventScheduleItem.eventScheduleDetailItem.AttractionType=txtAttractionType.getText().toString();
            eventScheduleItem.eventScheduleDetailItem.BookingReference=txtBookingReference.getText().toString();
            eventScheduleItem.eventScheduleDetailItem.FlightNo=txtFlightNo.getText().toString();
            eventScheduleItem.eventScheduleDetailItem.Terminal=txtTerminal.getText().toString();
            eventScheduleItem.startTimeKnown = chkStartKnown.isChecked();
            eventScheduleItem.startHour = getHour(txtStart);
            eventScheduleItem.startMin = getMinute(txtStart);
            eventScheduleItem.endTimeKnown = chkEndKnown.isChecked();
            eventScheduleItem.endHour = getHour(txtEnd);
            eventScheduleItem.endMin = getMinute(txtEnd);

            if(action.equals("add"))
            {
                eventScheduleItem.holidayId=holidayId;
                eventScheduleItem.dayId=dayId;
                eventScheduleItem.attractionId=attractionId;
                eventScheduleItem.attractionAreaId=attractionAreaId;

                if(!da.getNextScheduleId(holidayId, dayId, attractionId, attractionAreaId, myInt))
                    return;
                eventScheduleItem.scheduleId=myInt.Value;

                if(!da.getNextScheduleSequenceNo(holidayId, dayId, attractionId, attractionAreaId, myInt))
                    return;
                eventScheduleItem.sequenceNo=myInt.Value;

                eventScheduleItem.schedType=getResources().getInteger(R.integer.schedule_type_generalattraction);

                eventScheduleItem.eventScheduleDetailItem.holidayId=holidayId;
                eventScheduleItem.eventScheduleDetailItem.dayId=dayId;
                eventScheduleItem.eventScheduleDetailItem.attractionId=attractionId;
                eventScheduleItem.eventScheduleDetailItem.attractionAreaId=attractionAreaId;
                eventScheduleItem.eventScheduleDetailItem.scheduleId= eventScheduleItem.scheduleId;

                if(!da.addScheduleItem(eventScheduleItem))
                    return;
            }

            if(action.equals("modify"))
            {

                if(!da.updateScheduleItem(eventScheduleItem))
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
