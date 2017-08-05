package com.example.des.hp.Schedule.Hotel;

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

public class HotelDetailsView extends BaseScheduleView
{
    //region Member variables
    public LinearLayout grpStartDate;
    public CheckBox chkCheckinKnown;
    public TextView checkIn;
    public CheckBox chkDepartureKnown;
    public TextView departs;
    public TextView txtBookingRef;
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
            scheduleTypeDescription = getString(R.string.schedule_desc_hotel);

            setContentView(R.layout.activity_hotel_details_view);
            
            checkIn = (TextView) findViewById(R.id.txtCheckin);
            departs = (TextView) findViewById(R.id.txtDeparture);
            txtBookingRef = (TextView) findViewById(R.id.txtBookingRef);
            chkCheckinKnown = (CheckBox) findViewById(R.id.chkCheckinKnown);
            chkDepartureKnown = (CheckBox) findViewById(R.id.chkDepartureKnown);
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
            inflater.inflate(R.menu.hoteldetailsformmenu, menu);
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
                    if(scheduleItem.hotelItem == null)
                        scheduleItem.hotelItem=new HotelItem();

            chkCheckinKnown.setChecked(scheduleItem.startTimeKnown);
            setTimeText(checkIn, scheduleItem.startHour, scheduleItem.startMin);
            
            chkDepartureKnown.setChecked(scheduleItem.endTimeKnown);
            setTimeText(departs, scheduleItem.endHour, scheduleItem.endMin);
            
            txtSchedName.setText(scheduleItem.schedName);
            txtBookingRef.setText(scheduleItem.hotelItem.bookingReference);

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
                case R.id.action_edit_hotel:
                    editSchedule(HotelDetailsEdit.class);
                    return true;
                case R.id.action_delete_hotel:
                    deleteSchedule();
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
