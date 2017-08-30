package com.example.des.hp.Schedule.Flight;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.example.des.hp.R;
import com.example.des.hp.myutils.*;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
import static com.example.des.hp.myutils.MyMessages.myMessages;

public class FlightDetailsEdit extends FlightDetailsView implements View.OnClickListener
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

            grpCheckin.setOnClickListener(this);
            grpBookingRef.setOnClickListener(this);
            grpSchedName.setOnClickListener(this);
            grpDeparture.setOnClickListener(this);
            grpArrival.setOnClickListener(this);
            grpTerminal.setOnClickListener(this);
            imageView.setOnClickListener(this);
            grpFlightNo.setOnClickListener(this);
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
        switch(view.getId())
        {

            case R.id.grpCheckin:
                checkInClick(view);
                break;

            case R.id.grpBookingRef:
                pickBookingRef(view);
                break;

            case R.id.grpSchedName:
                pickSchedName(view);
                break;

            case R.id.grpDeparture:
                departureClick(view);
                break;

            case R.id.imageViewSmall:
                pickImage(view);
                break;

            case R.id.grpArrival:
                arrivalClick(view);
                break;

            case R.id.grpTerminal:
                pickTerminal(view);
                break;

            case R.id.grpFlightNo:
                pickFlightNo(view);
                break;
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

    public void FlightNoPicked(View view)
    {
        try
        {
            txtFlightNo.setText(dialogWithEditTextFragment.getFinalText());

            dialogWithEditTextFragment.dismiss();
        }
        catch(Exception e)
        {
            ShowError("FlightNoPicked", e.getMessage());
        }

    }

    public void pickFlightNo(View view)
    {
        try
        {
            dwetOnOkClick=new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    FlightNoPicked(view);
                }
            };

            dialogWithEditTextFragment=DialogWithEditTextFragment.newInstance(getFragmentManager(),     // for the transaction bit
                "hihi",            // unique name for this dialog type
                "Flight Number",    // form caption
                "Flight Number",             // form message
                R.drawable.attachment, txtFlightNo.getText().toString(), // initial text
                dwetOnOkClick, this, false
            );

            dialogWithEditTextFragment.showIt();
        }
        catch(Exception e)
        {
            ShowError("pickFlightNo", e.getMessage());
        }

    }

    public void TerminalPicked(View view)
    {
        try
        {
            txtTerminal.setText(dialogWithEditTextFragment.getFinalText());

            dialogWithEditTextFragment.dismiss();
        }
        catch(Exception e)
        {
            ShowError("TerminalPicked", e.getMessage());
        }

    }

    public void pickTerminal(View view)
    {
        try
        {
            dwetOnOkClick=new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    TerminalPicked(view);
                }
            };

            dialogWithEditTextFragment=DialogWithEditTextFragment.newInstance(getFragmentManager(),     // for the transaction bit
                "hihi",            // unique name for this dialog type
                "Terminal",    // form caption
                "Terminal",             // form message
                R.drawable.attachment, txtTerminal.getText().toString(), // initial text
                dwetOnOkClick, this, false
            );

            dialogWithEditTextFragment.showIt();
        }
        catch(Exception e)
        {
            ShowError("pickTerminal", e.getMessage());
        }
    }

    public void checkInClick(View view)
    {
        handleTime(checkIn, chkCheckinKnown, "Select Check-in Time");
    }

    public void departureClick(View view)
    {
        handleTime(departs, chkDepartureKnown, "Select Departure Time");
    }

    public void arrivalClick(View view)
    {
        handleTime(arrives, chkArriveKnown, "Select Arrival Time");
    }


    //endregion

    //region Saving
    public void saveSchedule(View view)
    {
        try
        {
            MyInt myInt=new MyInt();

            myMessages().ShowMessageShort("Saving Schedule");

            scheduleItem.schedPicture="";
            if(internalImageFilename.length()>0)
                scheduleItem.schedPicture=internalImageFilename;
            scheduleItem.pictureAssigned=imageSet;
            scheduleItem.pictureChanged=imageChanged;
            scheduleItem.schedName=txtSchedName.getText().toString();
            scheduleItem.scheduleBitmap=null;
            if(imageSet)
                scheduleItem.scheduleBitmap=((BitmapDrawable) imageView.getDrawable()).getBitmap();

            scheduleItem.startTimeKnown=chkCheckinKnown.isChecked();
            scheduleItem.startHour=getHour(checkIn);
            scheduleItem.startMin=getMinute(checkIn);
            scheduleItem.endTimeKnown=chkArriveKnown.isChecked();
            scheduleItem.endHour=getHour(arrives);
            scheduleItem.endMin=getMinute(arrives);
            scheduleItem.flightItem.departsKnown=chkDepartureKnown.isChecked();
            scheduleItem.flightItem.departsHour=getHour(departs);
            scheduleItem.flightItem.departsMin=getMinute(departs);
            scheduleItem.flightItem.flightNo=txtFlightNo.getText().toString();
            scheduleItem.flightItem.terminal=txtTerminal.getText().toString();
            scheduleItem.flightItem.bookingReference=txtBookingRef.getText().toString();

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

                scheduleItem.schedType=getResources().getInteger(R.integer.schedule_type_flight);


                scheduleItem.flightItem.holidayId=holidayId;
                scheduleItem.flightItem.dayId=dayId;
                scheduleItem.flightItem.attractionId=attractionId;
                scheduleItem.flightItem.attractionAreaId=attractionAreaId;
                scheduleItem.flightItem.scheduleId=scheduleItem.scheduleId;

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
