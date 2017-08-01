package com.example.des.hp.Schedule.Flight;

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

public class FlightDetailsView extends BaseScheduleView
{

    public LinearLayout grpStartDate;
    public FlightItem flightItem;
    public CheckBox chkCheckinKnown;
    public TextView checkIn;
    public CheckBox chkDepartureKnown;
    public TextView departs;
    public CheckBox chkArriveKnown;
    public TextView arrives;
    public TextView txtFlightNo;
    public TextView txtTerminal;
    public TextView txtBookingRef;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
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
