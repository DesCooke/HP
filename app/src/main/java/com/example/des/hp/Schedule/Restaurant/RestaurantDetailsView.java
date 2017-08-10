package com.example.des.hp.Schedule.Restaurant;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import com.example.des.hp.R;
import com.example.des.hp.Schedule.*;

public class RestaurantDetailsView extends BaseScheduleView
{
    //region Member variables
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
    public LinearLayout grpBookingRef;
    public ImageButton btnClear;
    public Button btnSave;
    public LinearLayout grpStartTime;
    public LinearLayout grpEndTime;
    //endregion

    //region Constructors/Destructors
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        try
        {
            scheduleTypeDescription = getString(R.string.schedule_desc_restaurant);

            layoutName="activity_restaurant_details_view";
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
            btnClear=(ImageButton) findViewById(R.id.btnClear);
            btnSave=(Button) findViewById(R.id.btnSave);
            grpStartTime=(LinearLayout) findViewById(R.id.grpStartTime);
            grpEndTime=(LinearLayout) findViewById(R.id.grpEndTime);
            grpBookingRef=(LinearLayout) findViewById(R.id.grpBookingRef);

            afterCreate();
            
            showForm();
        }
        catch (Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
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
    //endregion

    //region Regular Form Activities
    public void showForm()
    {
        super.showForm();
        
        try
        {
            if(action != null)
                if(action.equals("add"))
                    if(scheduleItem.restaurantItem == null)
                        scheduleItem.restaurantItem=new RestaurantItem();

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

            SetImage(scheduleItem.schedPicture);

            afterShow();

        }
        catch (Exception e)
        {
            ShowError("showForm", e.getMessage());
        }
    }
    //endregion

    //region OnClick Events
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
    //endregion

}
