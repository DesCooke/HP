package com.example.des.hp.Schedule.Cinema;

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

public class CinemaDetailsView extends BaseScheduleView
{

    public LinearLayout grpStartDate;
    public CinemaItem cinemaItem;
    public CheckBox chkCheckinKnown;
    public TextView checkIn;
    public CheckBox chkDepartureKnown;
    public TextView departs;
    public TextView txtBookingRef;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            setContentView(R.layout.activity_cinema_details_view);

            checkIn=(TextView) findViewById(R.id.txtCheckin);
            departs=(TextView) findViewById(R.id.txtDeparture);
            txtBookingRef=(TextView) findViewById(R.id.txtBookingRef);
            chkCheckinKnown=(CheckBox) findViewById(R.id.chkCheckinKnown);
            chkDepartureKnown=(CheckBox) findViewById(R.id.chkDepartureKnown);

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
            setTimeText(checkIn, scheduleItem.startHour, scheduleItem.startMin);

            setTimeText(departs, scheduleItem.endHour, scheduleItem.endMin);

            txtSchedName.setText(scheduleItem.schedName);
            txtBookingRef.setText(scheduleItem.cinemaItem.bookingReference);
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
                case R.id.action_delete_cinema:
                    deleteSchedule();
                    return true;
                case R.id.action_edit_cinema:
                    editSchedule(CinemaDetailsEdit.class);
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
        return (true);
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        try
        {
            MenuInflater inflater=getMenuInflater();
            inflater.inflate(R.menu.cinemadetailsformmenu, menu);
            return true;
        }
        catch(Exception e)
        {
            ShowError("onCreateOptionsMenu", e.getMessage());
        }
        return true;
    }

}
