package com.example.des.hp.Day;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.graphics.BitmapFactory;
import android.widget.TextView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.Dialog.BaseFullPageRecycleView;
import com.example.des.hp.ExtraFiles.ExtraFilesDetailsList;
import com.example.des.hp.Highlight.PageHighlightsFragment;
import com.example.des.hp.PageFragmentAdapter;
import com.example.des.hp.R;
import com.example.des.hp.Schedule.PageScheduleFragment;
import com.example.des.hp.Holiday.*;
import com.example.des.hp.Schedule.Flight.*;
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
import com.example.des.hp.thirdpartyutils.BadgeView;
import com.example.des.hp.Notes.NoteItem;
import com.example.des.hp.Notes.NoteView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
import static com.example.des.hp.myutils.MyMessages.myMessages;
import static java.security.AccessController.getContext;

public class DayDetailsView extends BaseFullPageRecycleView
{
    
    private final int SELECT_PHOTO = 1;
    private ImageView imageView;
    public DateUtils dateUtils;
    public LinearLayout grpStartDate;
    public String holidayName;
    public CheckBox cb;
    public DayItem dayItem;
    private ImageUtils imageUtils;
    private TextView txtDayCat;
    public MyColor myColor;
    public LinearLayout grpMenuFile;
    public ArrayList<ScheduleItem> scheduleList;
    public ScheduleAdapter scheduleAdapter;
    
    public void clearImage(View view)
    {
        try
        {
            imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.imagemissing));
        }
        catch (Exception e)
        {
            ShowError("clearImage", e.getMessage());
        }
        
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            setContentView(R.layout.activity_day_details_view);
            
            afterCreate();
            
            dateUtils = new DateUtils(this);
            imageUtils = new ImageUtils(this);
            myColor = new MyColor(this);
            
            imageView = (ImageView) findViewById(R.id.imageView);
            txtDayCat = (TextView) findViewById(R.id.txtDayCat);
            grpMenuFile = (LinearLayout) findViewById(R.id.grpMenuFile);
            
            showForm();
        }
        catch (Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
        
    }
    
    @Override
    public int getInfoId()
    {
        return (dayItem.infoId);
    }
    
    public void setNoteId(int pNoteId)
    {
        dayItem.noteId = pNoteId;
        if (!databaseAccess().updateDayItem(dayItem))
            return;
    }
    
    @Override
    public int getNoteId()
    {
        return (dayItem.noteId);
    }
    
    @Override
    public void setInfoId(int pInfoId)
    {
        dayItem.infoId = pInfoId;
        if (!databaseAccess().updateDayItem(dayItem))
            return;
    }
    
    public void showForm()
    {
        super.showForm();
        try
        {
            allowCellMove = true;
            
            clearImage(null);
            
            if (action != null && action.equals("view"))
            {
                dayItem = new DayItem();
                if (!databaseAccess().getDayItem(holidayId, dayId, dayItem))
                    return;
                
                HolidayItem holidayItem = new HolidayItem();
                if (!databaseAccess().getHolidayItem(holidayId, holidayItem))
                    return;
                
                String originalFileName = dayItem.dayPicture;
                if (imageUtils.getPageHeaderImage(this, dayItem.dayPicture, imageView) == false)
                    return;
                
                String lSubTitle;
                MyBoolean isUnknown = new MyBoolean();
                if (!dateUtils.IsUnknown(databaseAccess().currentStartDate, isUnknown))
                    return;
                if (isUnknown.Value)
                {
                    lSubTitle = String.format(Locale.ENGLISH, getResources().getString(R.string.fmt_day_line), dayItem.sequenceNo);
                } else
                {
                    Date lcurrdate = new Date();
                    
                    // we subtract 1 because sequenceno starts at 1 - but we want to add 0 days for the
                    // first element
                    if (dateUtils.AddDays(databaseAccess().currentStartDate, (dayItem.sequenceNo - 1), lcurrdate) == false)
                        return;
                    
                    MyString myString = new MyString();
                    if (dateUtils.DateToStr(lcurrdate, myString) == false)
                        return;
                    lSubTitle = String.format(Locale.ENGLISH, getResources().getString(R.string.fmt_date_line), myString.Value);
                }
                
                
                SetTitles(holidayItem.holidayName, dayItem.dayName + " / " + lSubTitle);
                
                
                int lColor = -1;
                String lDayCat = "Day Category: <unknown>";
                if (dayItem.dayCat == getResources().getInteger(R.integer.day_cat_easy))
                {
                    lColor = getColor(R.color.colorEasy);
                    lDayCat = "Day Category: Easy";
                }
                if (dayItem.dayCat == getResources().getInteger(R.integer.day_cat_moderate))
                {
                    lColor = getColor(R.color.colorModerate);
                    lDayCat = "Day Category: Moderate";
                }
                if (dayItem.dayCat == getResources().getInteger(R.integer.day_cat_busy))
                {
                    lColor = getColor(R.color.colorBusy);
                    lDayCat = "Day Category: VBusy";
                }
                
                txtDayCat.setText(lDayCat);
                if (lColor != -1)
                {
                    txtDayCat.setBackgroundColor(lColor);
                    grpMenuFile.setBackgroundColor(lColor);
                }
                
                scheduleList = new ArrayList<>();
                if (!databaseAccess().getScheduleList(holidayId, dayId, attractionId, attractionAreaId, scheduleList))
                    return;
                scheduleAdapter = new ScheduleAdapter(this, scheduleList);
                
                CreateRecyclerView(R.id.dayListView, scheduleAdapter);
                if (lColor != -1)
                    recyclerView.setBackgroundColor(lColor);
                
                scheduleAdapter.setOnItemClickListener(new ScheduleAdapter.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(View view, ScheduleItem obj, int position)
                    {
                        if (scheduleList.get(position).schedType == getResources().getInteger(R.integer.schedule_type_flight))
                        {
                            Intent intent = new Intent(getApplicationContext(), FlightDetailsView.class);
                            intent.putExtra("ACTION", "view");
                            intent.putExtra("HOLIDAYID", scheduleList.get(position).holidayId);
                            intent.putExtra("DAYID", scheduleList.get(position).dayId);
                            intent.putExtra("ATTRACTIONID", scheduleList.get(position).attractionId);
                            intent.putExtra("ATTRACTIONAREAID", scheduleList.get(position).attractionAreaId);
                            intent.putExtra("SCHEDULEID", scheduleList.get(position).scheduleId);
                            intent.putExtra("TITLE", title);
                            intent.putExtra("SUBTITLE", subTitle);
                            
                            startActivity(intent);
                        }
                        if (scheduleList.get(position).schedType == getResources().getInteger(R.integer.schedule_type_hotel))
                        {
                            Intent intent = new Intent(getApplicationContext(), HotelDetailsView.class);
                            intent.putExtra("ACTION", "view");
                            intent.putExtra("HOLIDAYID", scheduleList.get(position).holidayId);
                            intent.putExtra("DAYID", scheduleList.get(position).dayId);
                            intent.putExtra("ATTRACTIONID", scheduleList.get(position).attractionId);
                            intent.putExtra("ATTRACTIONAREAID", scheduleList.get(position).attractionAreaId);
                            intent.putExtra("SCHEDULEID", scheduleList.get(position).scheduleId);
                            intent.putExtra("TITLE", title);
                            intent.putExtra("SUBTITLE", subTitle);
                            
                            startActivity(intent);
                        }
                        if (scheduleList.get(position).schedType == getResources().getInteger(R.integer.schedule_type_bus))
                        {
                            Intent intent = new Intent(getApplicationContext(), BusDetailsView.class);
                            intent.putExtra("ACTION", "edit");
                            intent.putExtra("HOLIDAYID", scheduleList.get(position).holidayId);
                            intent.putExtra("DAYID", scheduleList.get(position).dayId);
                            intent.putExtra("ATTRACTIONID", scheduleList.get(position).attractionId);
                            intent.putExtra("ATTRACTIONAREAID", scheduleList.get(position).attractionAreaId);
                            intent.putExtra("SCHEDULEID", scheduleList.get(position).scheduleId);
                            intent.putExtra("TITLE", title);
                            intent.putExtra("SUBTITLE", subTitle);
                            
                            startActivity(intent);
                        }
                        if (scheduleList.get(position).schedType == getResources().getInteger(R.integer.schedule_type_show))
                        {
                            Intent intent = new Intent(getApplicationContext(), ShowDetailsView.class);
                            intent.putExtra("ACTION", "view");
                            intent.putExtra("HOLIDAYID", scheduleList.get(position).holidayId);
                            intent.putExtra("DAYID", scheduleList.get(position).dayId);
                            intent.putExtra("ATTRACTIONID", scheduleList.get(position).attractionId);
                            intent.putExtra("ATTRACTIONAREAID", scheduleList.get(position).attractionAreaId);
                            intent.putExtra("SCHEDULEID", scheduleList.get(position).scheduleId);
                            intent.putExtra("TITLE", title);
                            intent.putExtra("SUBTITLE", subTitle);
                            
                            startActivity(intent);
                        }
                        if (scheduleList.get(position).schedType == getResources().getInteger(R.integer.schedule_type_restaurant))
                        {
                            Intent intent = new Intent(getApplicationContext(), RestaurantDetailsView.class);
                            intent.putExtra("ACTION", "view");
                            intent.putExtra("HOLIDAYID", scheduleList.get(position).holidayId);
                            intent.putExtra("DAYID", scheduleList.get(position).dayId);
                            intent.putExtra("ATTRACTIONID", scheduleList.get(position).attractionId);
                            intent.putExtra("ATTRACTIONAREAID", scheduleList.get(position).attractionAreaId);
                            intent.putExtra("SCHEDULEID", scheduleList.get(position).scheduleId);
                            intent.putExtra("TITLE", title);
                            intent.putExtra("SUBTITLE", subTitle);
                            
                            startActivity(intent);
                        }
                        if (scheduleList.get(position).schedType == getResources().getInteger(R.integer.schedule_type_ride))
                        {
                            Intent intent = new Intent(getApplicationContext(), RideDetailsView.class);
                            intent.putExtra("ACTION", "view");
                            intent.putExtra("HOLIDAYID", scheduleList.get(position).holidayId);
                            intent.putExtra("DAYID", scheduleList.get(position).dayId);
                            intent.putExtra("ATTRACTIONID", scheduleList.get(position).attractionId);
                            intent.putExtra("ATTRACTIONAREAID", scheduleList.get(position).attractionAreaId);
                            intent.putExtra("SCHEDULEID", scheduleList.get(position).scheduleId);
                            intent.putExtra("TITLE", title);
                            intent.putExtra("SUBTITLE", subTitle);
                            
                            startActivity(intent);
                        }
                        if (scheduleList.get(position).schedType == getResources().getInteger(R.integer.schedule_type_cinema))
                        {
                            Intent intent = new Intent(getApplicationContext(), CinemaDetailsView.class);
                            intent.putExtra("ACTION", "edit");
                            intent.putExtra("HOLIDAYID", scheduleList.get(position).holidayId);
                            intent.putExtra("DAYID", scheduleList.get(position).dayId);
                            intent.putExtra("ATTRACTIONID", scheduleList.get(position).attractionId);
                            intent.putExtra("ATTRACTIONAREAID", scheduleList.get(position).attractionAreaId);
                            intent.putExtra("SCHEDULEID", scheduleList.get(position).scheduleId);
                            intent.putExtra("TITLE", title);
                            intent.putExtra("SUBTITLE", subTitle);
                            
                            startActivity(intent);
                        }
                        if (scheduleList.get(position).schedType == getResources().getInteger(R.integer.schedule_type_park))
                        {
                            Intent intent = new Intent(getApplicationContext(), ParkDetailsView.class);
                            intent.putExtra("ACTION", "view");
                            intent.putExtra("HOLIDAYID", scheduleList.get(position).holidayId);
                            intent.putExtra("DAYID", scheduleList.get(position).dayId);
                            intent.putExtra("ATTRACTIONID", scheduleList.get(position).attractionId);
                            intent.putExtra("ATTRACTIONAREAID", scheduleList.get(position).attractionAreaId);
                            intent.putExtra("SCHEDULEID", scheduleList.get(position).scheduleId);
                            intent.putExtra("TITLE", title);
                            intent.putExtra("SUBTITLE", subTitle);
                            
                            startActivity(intent);
                        }
                        if (scheduleList.get(position).schedType == getResources().getInteger(R.integer.schedule_type_parade))
                        {
                            Intent intent = new Intent(getApplicationContext(), ParadeDetailsView.class);
                            intent.putExtra("ACTION", "view");
                            intent.putExtra("HOLIDAYID", scheduleList.get(position).holidayId);
                            intent.putExtra("DAYID", scheduleList.get(position).dayId);
                            intent.putExtra("ATTRACTIONID", scheduleList.get(position).attractionId);
                            intent.putExtra("ATTRACTIONAREAID", scheduleList.get(position).attractionAreaId);
                            intent.putExtra("SCHEDULEID", scheduleList.get(position).scheduleId);
                            intent.putExtra("TITLE", title);
                            intent.putExtra("SUBTITLE", subTitle);
                            
                            startActivity(intent);
                        }
                        if (scheduleList.get(position).schedType == getResources().getInteger(R.integer.schedule_type_other))
                        {
                            Intent intent = new Intent(getApplicationContext(), OtherDetailsView.class);
                            intent.putExtra("ACTION", "view");
                            intent.putExtra("HOLIDAYID", scheduleList.get(position).holidayId);
                            intent.putExtra("DAYID", scheduleList.get(position).dayId);
                            intent.putExtra("ATTRACTIONID", scheduleList.get(position).attractionId);
                            intent.putExtra("ATTRACTIONAREAID", scheduleList.get(position).attractionAreaId);
                            intent.putExtra("SCHEDULEID", scheduleList.get(position).scheduleId);
                            intent.putExtra("TITLE", title);
                            intent.putExtra("SUBTITLE", subTitle);
                            
                            startActivity(intent);
                        }
                    }
                });
                afterShow();
            }
            
            
        }
        catch (Exception e)
        {
            ShowError("showForm", e.getMessage());
        }
        
    }
    
    public void editDay()
    {
        try
        {
            Intent intent = new Intent(getApplicationContext(), DayDetailsEdit.class);
            intent.putExtra("ACTION", "modify");
            intent.putExtra("HOLIDAYID", holidayId);
            intent.putExtra("DAYID", dayId);
            startActivity(intent);
        }
        catch (Exception e)
        {
            ShowError("editDay", e.getMessage());
        }
    }
    
    public void StartNewIntent(Class neededClass)
    {
        try
        {
            Intent intent = new Intent(getApplicationContext(), neededClass);
            intent.putExtra("ACTION", "add");
            intent.putExtra("HOLIDAYID", holidayId);
            intent.putExtra("DAYID", dayId);
            intent.putExtra("ATTRACTIONID", 0);
            intent.putExtra("ATTRACTIONAREAID", 0);
            intent.putExtra("TITLE", title);
            intent.putExtra("SUBTITLE", subTitle);
            startActivity(intent);
        }
        catch (Exception e)
        {
            ShowError("StartNewIntent", e.getMessage());
        }
    }


    public void deleteDay()
    {
        try
        {
            if (!databaseAccess().deleteDayItem(dayItem))
                return;
            finish();
        }
        catch (Exception e)
        {
            ShowError("deleteDay", e.getMessage());
        }
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        try
        {
            switch (item.getItemId())
            {
                case R.id.action_edit_day:
                    editDay();
                    return true;
                case R.id.action_delete_day:
                    deleteDay();
                    return true;
                case R.id.action_add_flight:
                    StartNewIntent(FlightDetailsEdit.class);
                    return true;
                case R.id.action_add_hotel:
                    StartNewIntent(HotelDetailsEdit.class);
                    return true;
                case R.id.action_add_show:
                    StartNewIntent(ShowDetailsEdit.class);
                    return true;
                case R.id.action_add_bus:
                    StartNewIntent(BusDetailsEdit.class);
                    return true;
                case R.id.action_add_restaurant:
                    StartNewIntent(RestaurantDetailsEdit.class);
                    return true;
                case R.id.action_add_cinema:
                    StartNewIntent(CinemaDetailsEdit.class);
                    return true;
                case R.id.action_add_park:
                    StartNewIntent(ParkDetailsEdit.class);
                    return true;
                case R.id.action_add_parade:
                    StartNewIntent(ParadeDetailsEdit.class);
                    return true;
                case R.id.action_add_ride:
                    StartNewIntent(RideDetailsEdit.class);
                    return true;
                case R.id.action_add_other:
                    StartNewIntent(OtherDetailsEdit.class);
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
            inflater.inflate(R.menu.daydetailsformmenu, menu);
        }
        catch (Exception e)
        {
            ShowError("onCreateOptionsMenu", e.getMessage());
        }
        return true;
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
    
}
