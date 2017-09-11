package com.example.des.hp.Day;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.R;
import com.example.des.hp.Holiday.*;
import com.example.des.hp.Schedule.Flight.*;
import com.example.des.hp.Schedule.GeneralAttraction.GeneralAttractionDetailsEdit;
import com.example.des.hp.Schedule.GeneralAttraction.GeneralAttractionDetailsView;
import com.example.des.hp.Schedule.Hotel.*;
import com.example.des.hp.Schedule.Bus.*;
import com.example.des.hp.Schedule.Park.*;
import com.example.des.hp.Schedule.Parade.*;
import com.example.des.hp.Schedule.Cinema.*;
import com.example.des.hp.Schedule.Other.*;
import com.example.des.hp.Schedule.Restaurant.RestaurantDetailsView;
import com.example.des.hp.Schedule.Ride.RideDetailsEdit;
import com.example.des.hp.Schedule.Ride.RideDetailsView;
import com.example.des.hp.Schedule.ScheduleAdapter;
import com.example.des.hp.Schedule.ScheduleItem;
import com.example.des.hp.Schedule.Show.*;
import com.example.des.hp.Schedule.Restaurant.RestaurantDetailsEdit;
import com.example.des.hp.myutils.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
import static com.example.des.hp.myutils.DateUtils.dateUtils;

public class DayDetailsView extends BaseActivity
{

    //region Member variables
    public LinearLayout grpStartDate;
    public String holidayName;
    public DayItem dayItem;
    private TextView txtDayCat;
    public LinearLayout grpMenuFile;
    public ArrayList<ScheduleItem> scheduleList;
    public ScheduleAdapter scheduleAdapter;
    //endregion

    //region Constructors/Destructors
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            layoutName="activity_day_details_view";
            setContentView(R.layout.activity_day_details_view);

            txtDayCat=(TextView) findViewById(R.id.txtDayCat);
            grpMenuFile=(LinearLayout) findViewById(R.id.grpMenuFile);

            afterCreate();

            showForm();
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }

    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        try
        {
            MenuInflater inflater=getMenuInflater();
            inflater.inflate(R.menu.daydetailsformmenu, menu);
        }
        catch(Exception e)
        {
            ShowError("onCreateOptionsMenu", e.getMessage());
        }
        return true;
    }
    //endregion

    //region OnClick Events
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        try
        {
            switch(item.getItemId())
            {
                case R.id.action_edit_day:
                    editDay();
                    return true;
                case R.id.action_delete_day:
                    deleteDay();
                    return true;
                case R.id.action_add_flight:
                    StartNewAddIntent(FlightDetailsEdit.class);
                    return true;
                case R.id.action_add_hotel:
                    StartNewAddIntent(HotelDetailsEdit.class);
                    return true;
                case R.id.action_add_show:
                    StartNewAddIntent(ShowDetailsEdit.class);
                    return true;
                case R.id.action_add_bus:
                    StartNewAddIntent(BusDetailsEdit.class);
                    return true;
                case R.id.action_add_restaurant:
                    StartNewAddIntent(RestaurantDetailsEdit.class);
                    return true;
                case R.id.action_add_cinema:
                    StartNewAddIntent(CinemaDetailsEdit.class);
                    return true;
                case R.id.action_add_park:
                    StartNewAddIntent(ParkDetailsEdit.class);
                    return true;
                case R.id.action_add_parade:
                    StartNewAddIntent(ParadeDetailsEdit.class);
                    return true;
                case R.id.action_add_ride:
                    StartNewAddIntent(RideDetailsEdit.class);
                    return true;
                case R.id.action_add_other:
                    StartNewAddIntent(OtherDetailsEdit.class);
                    return true;
                case R.id.action_add_generalattraction:
                    StartNewAddIntent(GeneralAttractionDetailsEdit.class);
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
    //endregion

    //region showForm
    public void showForm()
    {
        super.showForm();
        try
        {
            allowCellMove=true;

            dayItem=new DayItem();
            if(!databaseAccess().getDayItem(holidayId, dayId, dayItem))
                return;

            HolidayItem holidayItem=new HolidayItem();
            if(!databaseAccess().getHolidayItem(holidayId, holidayItem))
                return;

            SetImage(dayItem.dayPicture);

            String lSubTitle;
            MyBoolean isUnknown=new MyBoolean();
            if(!dateUtils().IsUnknown(DatabaseAccess.currentStartDate, isUnknown))
                return;
            if(isUnknown.Value)
            {
                lSubTitle=String.format(Locale.ENGLISH, getResources().getString(R.string.fmt_day_line), dayItem.sequenceNo);
            } else
            {
                Date lcurrdate=new Date();

                // we subtract 1 because sequenceno starts at 1 - but we want to add 0 days for the
                // first element
                if(!dateUtils().AddDays(DatabaseAccess.currentStartDate, (dayItem.sequenceNo - 1), lcurrdate))
                    return;

                MyString myString=new MyString();
                if(!dateUtils().DateToStr(lcurrdate, myString))
                    return;
                lSubTitle=String.format(Locale.ENGLISH, getResources().getString(R.string.fmt_date_line), myString.Value);
            }


            SetTitles(holidayItem.holidayName, dayItem.dayName + " / " + lSubTitle);


            int lColor=-1;
            String lDayCat="Day Category: <unknown>";
            if(dayItem.dayCat == getResources().getInteger(R.integer.day_cat_easy))
            {
                lColor=getColor(R.color.colorEasy);
                lDayCat="Day Category: Easy";
            }
            if(dayItem.dayCat == getResources().getInteger(R.integer.day_cat_moderate))
            {
                lColor=getColor(R.color.colorModerate);
                lDayCat="Day Category: Moderate";
            }
            if(dayItem.dayCat == getResources().getInteger(R.integer.day_cat_busy))
            {
                lColor=getColor(R.color.colorBusy);
                lDayCat="Day Category: VBusy";
            }

            txtDayCat.setText(lDayCat);
            if(lColor != -1)
            {
                txtDayCat.setBackgroundColor(lColor);
                grpMenuFile.setBackgroundColor(lColor);
            }

            scheduleList=new ArrayList<>();
            if(!databaseAccess().getScheduleList(holidayId, dayId, attractionId, attractionAreaId, scheduleList))
                return;
            scheduleAdapter=new ScheduleAdapter(this, scheduleList);

            CreateRecyclerView(R.id.dayListView, scheduleAdapter);
            if(lColor != -1)
                recyclerView.setBackgroundColor(lColor);

            scheduleAdapter.setOnItemClickListener(new ScheduleAdapter.OnItemClickListener()
            {
                @Override
                public void onItemClick(View view, ScheduleItem obj)
                {
                    if(obj.schedType == getResources().getInteger(R.integer.schedule_type_flight))
                    {
                        StartNewEditIntent(FlightDetailsView.class, obj);
                    }
                    if(obj.schedType == getResources().getInteger(R.integer.schedule_type_hotel))
                    {
                        StartNewEditIntent(HotelDetailsView.class, obj);
                    }
                    if(obj.schedType == getResources().getInteger(R.integer.schedule_type_bus))
                    {
                        StartNewEditIntent(BusDetailsView.class, obj);
                    }
                    if(obj.schedType == getResources().getInteger(R.integer.schedule_type_show))
                    {
                        StartNewEditIntent(ShowDetailsView.class, obj);
                    }
                    if(obj.schedType == getResources().getInteger(R.integer.schedule_type_restaurant))
                    {
                        StartNewEditIntent(RestaurantDetailsView.class, obj);
                    }
                    if(obj.schedType == getResources().getInteger(R.integer.schedule_type_ride))
                    {
                        StartNewEditIntent(RideDetailsView.class, obj);
                    }
                    if(obj.schedType == getResources().getInteger(R.integer.schedule_type_cinema))
                    {
                        StartNewEditIntent(CinemaDetailsView.class, obj);
                    }
                    if(obj.schedType == getResources().getInteger(R.integer.schedule_type_park))
                    {
                        StartNewEditIntent(ParkDetailsView.class, obj);
                    }
                    if(obj.schedType == getResources().getInteger(R.integer.schedule_type_parade))
                    {
                        StartNewEditIntent(ParadeDetailsView.class, obj);
                    }
                    if(obj.schedType == getResources().getInteger(R.integer.schedule_type_other))
                    {
                        StartNewEditIntent(OtherDetailsView.class, obj);
                    }
                    if(obj.schedType == getResources().getInteger(R.integer.schedule_type_generalattraction))
                    {
                        StartNewEditIntent(GeneralAttractionDetailsView.class, obj);
                    }
                }
            });
            afterShow();
        }
        catch(Exception e)
        {
            ShowError("showForm", e.getMessage());
        }

    }
    //endregion

    //region form Functions
    @Override
    public int getInfoId()
    {
        return (dayItem.infoId);
    }

    public void setNoteId(int pNoteId)
    {
        dayItem.noteId=pNoteId;
        databaseAccess().updateDayItem(dayItem);
    }

    @Override
    public int getNoteId()
    {
        return (dayItem.noteId);
    }

    @Override
    public void setInfoId(int pInfoId)
    {
        dayItem.infoId=pInfoId;
        databaseAccess().updateDayItem(dayItem);
    }

    public void editDay()
    {
        try
        {
            Intent intent=new Intent(getApplicationContext(), DayDetailsEdit.class);
            intent.putExtra("ACTION", "modify");
            intent.putExtra("HOLIDAYID", holidayId);
            intent.putExtra("DAYID", dayId);
            startActivity(intent);
        }
        catch(Exception e)
        {
            ShowError("editDay", e.getMessage());
        }
    }

    public void StartNewEditIntent(Class neededClass, ScheduleItem obj)
    {
        Intent intent=new Intent(getApplicationContext(), neededClass);
        intent.putExtra("ACTION", "view");
        intent.putExtra("HOLIDAYID", obj.holidayId);
        intent.putExtra("DAYID", obj.dayId);
        intent.putExtra("ATTRACTIONID", obj.attractionId);
        intent.putExtra("ATTRACTIONAREAID", obj.attractionAreaId);
        intent.putExtra("SCHEDULEID", obj.scheduleId);
        intent.putExtra("TITLE", title);
        intent.putExtra("SUBTITLE", subTitle);

        startActivity(intent);
    }

    public void StartNewAddIntent(Class neededClass)
    {
        try
        {
            Intent intent=new Intent(getApplicationContext(), neededClass);
            intent.putExtra("ACTION", "add");
            intent.putExtra("HOLIDAYID", holidayId);
            intent.putExtra("DAYID", dayId);
            intent.putExtra("ATTRACTIONID", 0);
            intent.putExtra("ATTRACTIONAREAID", 0);
            intent.putExtra("TITLE", title);
            intent.putExtra("SUBTITLE", subTitle);
            startActivity(intent);
        }
        catch(Exception e)
        {
            ShowError("StartNewAddIntent", e.getMessage());
        }
    }


    public void deleteDay()
    {
        try
        {
            if(!databaseAccess().deleteDayItem(dayItem))
                return;
            finish();
        }
        catch(Exception e)
        {
            ShowError("deleteDay", e.getMessage());
        }
    }


    @Override
    public void SwapItems(int from, int to)
    {
        Collections.swap(scheduleAdapter.data, from, to);
    }

    @Override
    public void OnItemMove(int from, int to)
    {
        scheduleAdapter.onItemMove(from, to);
    }

    @Override
    public void NotifyItemMoved(int from, int to)
    {
        scheduleAdapter.notifyItemMoved(from, to);
    }

    //endregion
}
