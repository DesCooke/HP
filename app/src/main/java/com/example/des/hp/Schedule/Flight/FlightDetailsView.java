package com.example.des.hp.Schedule.Flight;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.des.hp.R;
import com.example.des.hp.Schedule.*;

public class FlightDetailsView extends BaseScheduleView
{
    public CheckBox chkCheckinKnown;
    public TextView checkIn;
    public CheckBox chkDepartureKnown;
    public TextView departs;
    public TextView txtBookingRef;
    public CheckBox chkArriveKnown;
    public TextView arrives;
    public TextView txtFlightNo;
    public TextView txtTerminal;
    public ImageButton btnClear;
    public Button btnSave;

    public LinearLayout grpCheckin;
    public LinearLayout grpDeparture;
    public LinearLayout grpArrival;
    public LinearLayout grpFlightNo;
    public LinearLayout grpTerminal;
    public LinearLayout grpBookingRef;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            scheduleTypeDescription = getString(R.string.schedule_desc_flight);

            setContentView(R.layout.activity_flight_details_view);

            checkIn=(TextView) findViewById(R.id.txtCheckin);
            departs=(TextView) findViewById(R.id.txtDeparture);
            arrives=(TextView) findViewById(R.id.txtArrival);
            txtFlightNo=(TextView) findViewById(R.id.txtFlightNo);
            txtTerminal=(TextView) findViewById(R.id.txtTerminal);
            txtBookingRef=(TextView) findViewById(R.id.txtBookingRef);
            chkCheckinKnown=(CheckBox) findViewById(R.id.chkCheckinKnown);
            chkDepartureKnown=(CheckBox) findViewById(R.id.chkDepartureKnown);
            chkArriveKnown=(CheckBox) findViewById(R.id.chkArrivalKnown);
            btnClear=(ImageButton) findViewById(R.id.btnClear);
            btnSave=(Button) findViewById(R.id.btnSave);

            grpCheckin=(LinearLayout) findViewById(R.id.grpCheckin);
            grpDeparture=(LinearLayout) findViewById(R.id.grpDeparture);
            grpArrival=(LinearLayout) findViewById(R.id.grpArrival);
            grpFlightNo=(LinearLayout) findViewById(R.id.grpFlightNo);
            grpTerminal=(LinearLayout) findViewById(R.id.grpTerminal);
            grpBookingRef=(LinearLayout) findViewById(R.id.grpBookingRef);

            afterCreate();

            showForm();
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }

    }

    public void showForm()
    {
        super.showForm();

        try
        {
            if(action != null)
                if(action.equals("add"))
                    if(scheduleItem.flightItem == null)
                        scheduleItem.flightItem=new FlightItem();

            chkCheckinKnown.setChecked(scheduleItem.startTimeKnown);
            setTimeText(checkIn, scheduleItem.startHour, scheduleItem.startMin);

            chkDepartureKnown.setChecked(scheduleItem.flightItem.departsKnown);
            setTimeText(departs, scheduleItem.flightItem.departsHour, scheduleItem.flightItem.departsMin);

            chkArriveKnown.setChecked(scheduleItem.endTimeKnown);
            setTimeText(arrives, scheduleItem.endHour, scheduleItem.endMin);

            txtSchedName.setText(scheduleItem.schedName);
            txtFlightNo.setText(scheduleItem.flightItem.flightNo);
            txtTerminal.setText(scheduleItem.flightItem.terminal);
            txtBookingRef.setText(scheduleItem.flightItem.bookingReference);

            afterShow();

        }
        catch(Exception e)

        {
            ShowError("showForm", e.getMessage());
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        try
        {
            switch(item.getItemId())
            {
                case R.id.action_delete_flight:
                    deleteSchedule();
                    return true;
                case R.id.action_edit_flight:
                    editSchedule(FlightDetailsEdit.class);
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

    public boolean onCreateOptionsMenu(Menu menu)
    {
        try
        {
            MenuInflater inflater=getMenuInflater();
            inflater.inflate(R.menu.flightdetailsformmenu, menu);
            return true;
        }
        catch(Exception e)
        {
            ShowError("onCreateOptionsMenu", e.getMessage());
        }
        return true;
    }

}
