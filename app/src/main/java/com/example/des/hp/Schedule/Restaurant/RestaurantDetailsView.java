package com.example.des.hp.Schedule.Restaurant;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import com.example.des.hp.R;
import com.example.des.hp.Schedule.*;

public class RestaurantDetailsView extends BaseScheduleView
{
    
    public LinearLayout grpStartDate;
    public CheckBox chkCheckinKnown;
    public TextView checkIn;
    public CheckBox chkDepartureKnown;
    public TextView departs;
    public TextView txtBookingRef;
    public RadioButton radUnknown;
    public RadioButton radWalkIn;
    public RadioButton radOnTheDay;
    public RadioButton rad180Days;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        try
        {
            setContentView(R.layout.activity_restaurant_details_view);
            
            txtSchedName = (TextView) findViewById(R.id.txtSchedName);
            checkIn = (TextView) findViewById(R.id.txtCheckin);
            departs = (TextView) findViewById(R.id.txtDeparture);
            txtBookingRef = (TextView) findViewById(R.id.txtBookingRef);
            chkCheckinKnown = (CheckBox) findViewById(R.id.chkCheckinKnown);
            chkDepartureKnown = (CheckBox) findViewById(R.id.chkDepartureKnown);
            radUnknown = (RadioButton) findViewById(R.id.radUnknown);
            radWalkIn = (RadioButton) findViewById(R.id.radWalkIn);
            radOnTheDay = (RadioButton) findViewById(R.id.radOnTheDay);
            rad180Days = (RadioButton) findViewById(R.id.rad180Days);
            
            afterCreate();
            
            showForm();
        }
        catch (Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
        
    }
    
    public void showForm()
    {
        super.showForm();
        
        try
        {
            radUnknown.setChecked(false);
            radWalkIn.setChecked(false);
            radOnTheDay.setChecked(false);
            rad180Days.setChecked(false);
            if (scheduleItem.restaurantItem.reservationType == 0)
                radUnknown.setChecked(true);
            if (scheduleItem.restaurantItem.reservationType == 1)
                radWalkIn.setChecked(true);
            if (scheduleItem.restaurantItem.reservationType == 2)
                radOnTheDay.setChecked(true);
            if (scheduleItem.restaurantItem.reservationType == 3)
                rad180Days.setChecked(true);
            
            chkCheckinKnown.setChecked(scheduleItem.startTimeKnown);
            setTimeText(checkIn, scheduleItem.startHour, scheduleItem.startMin);
            
            chkDepartureKnown.setChecked(scheduleItem.endTimeKnown);
            setTimeText(departs, scheduleItem.endHour, scheduleItem.endMin);
            
            txtSchedName.setText(scheduleItem.schedName);
            txtBookingRef.setText(scheduleItem.restaurantItem.bookingReference);


            afterShow();

        }
        catch (Exception e)
        {
            ShowError("showForm", e.getMessage());
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        try
        {
            switch (item.getItemId())
            {
                case R.id.action_delete_restaurant:
                    deleteSchedule();
                    return true;
                case R.id.action_edit_restaurant:
                    editSchedule(RestaurantDetailsEdit.class);
                    return true;
                case R.id.action_move:
                    move();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
        catch (Exception e)
        {
            ShowError("onOptionsItemSelected", e.getMessage());
        }
        
        return true;
    }
    
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try
        {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.restaurantdetailsformmenu, menu);
        }
        catch (Exception e)
        {
            ShowError("onCreateOptionsMenu", e.getMessage());
        }
        return true;
    }
    
}
