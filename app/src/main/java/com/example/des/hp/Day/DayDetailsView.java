package com.example.des.hp.Day;

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
import com.example.des.hp.Schedule.Ride.RideDetailsEdit;
import com.example.des.hp.Schedule.Show.*;
import com.example.des.hp.Schedule.Restaurant.RestaurantDetailsEdit;
import com.example.des.hp.myutils.*;
import com.example.des.hp.thirdpartyutils.BadgeView;
import com.example.des.hp.Notes.NoteItem;
import com.example.des.hp.Notes.NoteView;

import java.util.Date;
import java.util.Locale;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

public class DayDetailsView extends BaseActivity
{
    
    private final int SELECT_PHOTO = 1;
    private ImageView imageView;
    public int holidayId;
    public int dayId;
    public DateUtils dateUtils;
    public LinearLayout grpStartDate;
    public String holidayName;
    public CheckBox cb;
    public DayItem dayItem;
    private ImageUtils imageUtils;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private PageScheduleFragment f_schedule;
    private PageHighlightsFragment f_highlights;
    private ActionBar actionBar;
    private TextView txtDayCat;
    public ImageButton btnShowInfo;
    public BadgeView btnShowInfoBadge;
    public MyColor myColor;
    public LinearLayout grpMenuFile;
    public ImageButton btnShowNotes;
    
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
    
    public void showNotes(View view)
    {
        try
        {
            Intent intent2 = new Intent(getApplicationContext(), NoteView.class);
            if (dayItem.noteId == 0)
            {
                MyInt myInt = new MyInt();
                if (!databaseAccess().getNextNoteId(holidayId, myInt))
                    return;
                dayItem.noteId = myInt.Value;
                if (!databaseAccess().updateDayItem(dayItem))
                    return;
            }
            intent2.putExtra("ACTION", "view");
            intent2.putExtra("HOLIDAYID", dayItem.holidayId);
            intent2.putExtra("NOTEID", dayItem.noteId);
            intent2.putExtra("TITLE", dayItem.dayName);
            intent2.putExtra("SUBTITLE", "Notes");
            startActivity(intent2);
        }
        catch (Exception e)
        {
            ShowError("showNotes", e.getMessage());
        }
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        
        try
        {
            showForm();
        }
        catch (Exception e)
        {
            ShowError("onResume", e.getMessage());
        }
        
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            setContentView(R.layout.activity_day_details_view);
            
            dateUtils = new DateUtils(this);
            imageUtils = new ImageUtils(this);
            myColor = new MyColor(this);
            
            viewPager = (ViewPager) findViewById(R.id.viewpager);
            imageView = (ImageView) findViewById(R.id.imageView);
            tabLayout = (TabLayout) findViewById(R.id.tabs);
            actionBar = getSupportActionBar();
            txtDayCat = (TextView) findViewById(R.id.txtDayCat);
            btnShowInfo = (ImageButton) findViewById(R.id.btnShowInfo);
            grpMenuFile = (LinearLayout) findViewById(R.id.grpMenuFile);
            btnShowNotes = (ImageButton) findViewById(R.id.btnShowNotes);
            
            btnShowInfoBadge = new BadgeView(this, btnShowInfo);
            btnShowInfoBadge.setText(Integer.toString(0));
            btnShowInfoBadge.show();
            
            showForm();
        }
        catch (Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
        
    }
    
    public void showInfo(View view)
    {
        try
        {
            Intent intent2 = new Intent(getApplicationContext(), ExtraFilesDetailsList.class);
            if (dayItem.infoId == 0)
            {
                MyInt myInt = new MyInt();
                if (!databaseAccess().getNextFileGroupId(myInt))
                    return;
                dayItem.infoId = myInt.Value;
                if (!databaseAccess().updateDayItem(dayItem))
                    return;
            }
            intent2.putExtra("FILEGROUPID", dayItem.infoId);
            intent2.putExtra("TITLE", dayItem.dayName);
            intent2.putExtra("SUBTITLE", "Info");
            startActivity(intent2);
        }
        catch (Exception e)
        {
            ShowError("showInfo", e.getMessage());
        }
    }
    
    
    private void setupViewPager(ViewPager viewPager)
    {
        try
        {
            PageFragmentAdapter adapter = new PageFragmentAdapter(getSupportFragmentManager());
            if (f_schedule == null)
            {
                f_schedule = new PageScheduleFragment();
                if (actionBar != null)
                {
                    if (actionBar.getTitle() != null)
                        f_schedule.title = actionBar.getTitle().toString();
                    if (actionBar.getSubtitle() != null)
                        f_schedule.subTitle = actionBar.getSubtitle().toString();
                }
            }
            
            if (f_highlights == null)
            {
                f_highlights = new PageHighlightsFragment();
            }
            
            adapter.addFragment(f_schedule, getString(R.string.tab_schedule));
            adapter.addFragment(f_highlights, getString(R.string.tab_highlights));
            
            viewPager.setAdapter(adapter);
        }
        catch (Exception e)
        {
            ShowError("setupViewPager", e.getMessage());
        }
    }
    
    public void showForm()
    {
        try
        {
            clearImage(null);
            
            Bundle extras = getIntent().getExtras();
            if (extras != null)
            {
                String action = extras.getString("ACTION");
                if (action != null && action.equals("view"))
                {
                    holidayId = extras.getInt("HOLIDAYID");
                    dayId = extras.getInt("DAYID");
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
                    
                    
                    if (actionBar != null)
                    {
                        actionBar.setTitle(holidayItem.holidayName);
                        actionBar.setSubtitle(dayItem.dayName + " / " + lSubTitle);
                    }
                    
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
                        viewPager.setBackgroundColor(lColor);
                        txtDayCat.setBackgroundColor(lColor);
                        grpMenuFile.setBackgroundColor(lColor);
                    }
                    
                    MyInt lFileCount = new MyInt();
                    lFileCount.Value = 0;
                    if (dayItem.infoId > 0)
                    {
                        if (!databaseAccess().getExtraFilesCount(dayItem.infoId, lFileCount))
                            return;
                    }
                    btnShowInfoBadge.setText(Integer.toString(lFileCount.Value));
                    
                    if (lFileCount.Value == 0)
                    {
                        btnShowInfoBadge.setVisibility(View.INVISIBLE);
                        if (myColor.SetImageButtonTint(btnShowInfo, R.color.colorDisabled) == false)
                            return;
                    } else
                    {
                        btnShowInfoBadge.setVisibility(View.VISIBLE);
                        if (myColor.SetImageButtonTint(btnShowInfo, R.color.colorEnabled) == false)
                            return;
                    }
                    
                    NoteItem noteItem = new NoteItem();
                    if (!databaseAccess().getNoteItem(dayItem.holidayId, dayItem.noteId, noteItem))
                        return;
                    if (noteItem.notes.length() == 0)
                    {
                        if (myColor.SetImageButtonTint(btnShowNotes, R.color.colorDisabled) == false)
                            return;
                    } else
                    {
                        if (myColor.SetImageButtonTint(btnShowNotes, R.color.colorEnabled) == false)
                            return;
                    }
                }
                
            }
            setupViewPager(viewPager);
            
            tabLayout.setupWithViewPager(viewPager);
            setupTabIcons();
            setupTabClick();
        }
        catch (Exception e)
        {
            ShowError("showForm", e.getMessage());
        }
        
    }
    
    private void setupTabClick()
    {
        /*
        try
        {
            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
            {
                @Override
                public void onTabSelected(TabLayout.Tab tab)
                {
                    int position = tab.getPosition();
                    viewPager.setCurrentItem(position);
                }
                
                @Override
                public void onTabUnselected(TabLayout.Tab tab)
                {
                }
                
                @Override
                public void onTabReselected(TabLayout.Tab tab)
                {
                }
            });
        }
        catch (Exception e)
        {
            ShowError("setupTabClick", e.getMessage());
        }
        */
        
    }
    
    private void setupTabIcons()
    {
        try
        {
            TabLayout.Tab lScheduleTab = tabLayout.getTabAt(0);
            TabLayout.Tab lHighlightsTab = tabLayout.getTabAt(1);
            if (lScheduleTab != null)
                lScheduleTab.setText(getResources().getString(R.string.tab_schedule));
            if (lHighlightsTab != null)
                lHighlightsTab.setText(getResources().getString(R.string.tab_highlights));
        }
        catch (Exception e)
        {
            ShowError("setupTabIcons", e.getMessage());
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
    
    public void addFlight()
    {
        try
        {
            Intent intent = new Intent(getApplicationContext(), FlightDetailsEdit.class);
            intent.putExtra("ACTION", "add");
            intent.putExtra("HOLIDAYID", holidayId);
            intent.putExtra("DAYID", dayId);
            if (actionBar != null)
            {
                CharSequence lTitle = actionBar.getTitle();
                if (lTitle != null)
                    intent.putExtra("TITLE", lTitle.toString());
                CharSequence lSubTitle = actionBar.getSubtitle();
                if (lSubTitle != null)
                    intent.putExtra("SUBTITLE", lSubTitle.toString());
            }
            startActivity(intent);
        }
        catch (Exception e)
        {
            ShowError("addFlight", e.getMessage());
        }
    }
    
    public void addHotel()
    {
        try
        {
            Intent intent = new Intent(getApplicationContext(), HotelDetailsEdit.class);
            intent.putExtra("ACTION", "add");
            intent.putExtra("HOLIDAYID", holidayId);
            intent.putExtra("DAYID", dayId);
            intent.putExtra("ATTRACTIONID", 0);
            intent.putExtra("ATTRACTIONAREAID", 0);
            if (actionBar != null)
            {
                CharSequence lTitle = actionBar.getTitle();
                if (lTitle != null)
                    intent.putExtra("TITLE", lTitle.toString());
                CharSequence lSubTitle = actionBar.getSubtitle();
                if (lSubTitle != null)
                    intent.putExtra("SUBTITLE", lSubTitle.toString());
            }
            startActivity(intent);
        }
        catch (Exception e)
        {
            ShowError("addHotel", e.getMessage());
        }
    }
    
    public void addCinema()
    {
        try
        {
            Intent intent = new Intent(getApplicationContext(), CinemaDetailsEdit.class);
            intent.putExtra("ACTION", "add");
            intent.putExtra("HOLIDAYID", holidayId);
            intent.putExtra("DAYID", dayId);
            intent.putExtra("ATTRACTIONID", 0);
            intent.putExtra("ATTRACTIONAREAID", 0);
            if (actionBar != null)
            {
                CharSequence lTitle = actionBar.getTitle();
                if (lTitle != null)
                    intent.putExtra("TITLE", lTitle.toString());
                CharSequence lSubTitle = actionBar.getSubtitle();
                if (lSubTitle != null)
                    intent.putExtra("SUBTITLE", lSubTitle.toString());
            }
            startActivity(intent);
        }
        catch (Exception e)
        {
            ShowError("addCinema", e.getMessage());
        }
        
    }
    
    public void addPark()
    {
        try
        {
            Intent intent = new Intent(getApplicationContext(), ParkDetailsEdit.class);
            intent.putExtra("ACTION", "add");
            intent.putExtra("HOLIDAYID", holidayId);
            intent.putExtra("DAYID", dayId);
            intent.putExtra("ATTRACTIONID", 0);
            intent.putExtra("ATTRACTIONAREAID", 0);
            if (actionBar != null)
            {
                CharSequence lTitle = actionBar.getTitle();
                if (lTitle != null)
                    intent.putExtra("TITLE", lTitle.toString());
                CharSequence lSubTitle = actionBar.getSubtitle();
                if (lSubTitle != null)
                    intent.putExtra("SUBTITLE", lSubTitle.toString());
            }
            startActivity(intent);
        }
        catch (Exception e)
        {
            ShowError("addPark", e.getMessage());
        }
    }
    
    public void addRide()
    {
        try
        {
            Intent intent = new Intent(getApplicationContext(), RideDetailsEdit.class);
            intent.putExtra("ACTION", "add");
            intent.putExtra("HOLIDAYID", holidayId);
            intent.putExtra("DAYID", dayId);
            intent.putExtra("ATTRACTIONID", 0);
            intent.putExtra("ATTRACTIONAREAID", 0);
            if (actionBar != null)
            {
                CharSequence lTitle = actionBar.getTitle();
                if (lTitle != null)
                    intent.putExtra("TITLE", lTitle.toString());
                CharSequence lSubTitle = actionBar.getSubtitle();
                if (lSubTitle != null)
                    intent.putExtra("SUBTITLE", lSubTitle.toString());
            }
            startActivity(intent);
        }
        catch (Exception e)
        {
            ShowError("addRide", e.getMessage());
        }
    }
    
    public void addParade()
    {
        try
        {
            Intent intent = new Intent(getApplicationContext(), ParadeDetailsEdit.class);
            intent.putExtra("ACTION", "add");
            intent.putExtra("HOLIDAYID", holidayId);
            intent.putExtra("DAYID", dayId);
            intent.putExtra("ATTRACTIONID", 0);
            intent.putExtra("ATTRACTIONAREAID", 0);
            if (actionBar != null)
            {
                CharSequence lTitle = actionBar.getTitle();
                if (lTitle != null)
                    intent.putExtra("TITLE", lTitle.toString());
                CharSequence lSubTitle = actionBar.getSubtitle();
                if (lSubTitle != null)
                    intent.putExtra("SUBTITLE", lSubTitle.toString());
            }
            startActivity(intent);
        }
        catch (Exception e)
        {
            ShowError("addParade", e.getMessage());
        }
    }
    
    public void addOther()
    {
        try
        {
            Intent intent = new Intent(getApplicationContext(), OtherDetailsEdit.class);
            intent.putExtra("ACTION", "add");
            intent.putExtra("HOLIDAYID", holidayId);
            intent.putExtra("DAYID", dayId);
            intent.putExtra("ATTRACTIONID", 0);
            intent.putExtra("ATTRACTIONAREAID", 0);
            if (actionBar != null)
            {
                CharSequence lTitle = actionBar.getTitle();
                if (lTitle != null)
                    intent.putExtra("TITLE", lTitle.toString());
                CharSequence lSubTitle = actionBar.getSubtitle();
                if (lSubTitle != null)
                    intent.putExtra("SUBTITLE", lSubTitle.toString());
            }
            startActivity(intent);
        }
        catch (Exception e)
        {
            ShowError("addOther", e.getMessage());
        }
        
    }
    
    public void addBus()
    {
        try
        {
            Intent intent = new Intent(getApplicationContext(), BusDetailsEdit.class);
            intent.putExtra("ACTION", "add");
            intent.putExtra("HOLIDAYID", holidayId);
            intent.putExtra("DAYID", dayId);
            intent.putExtra("ATTRACTIONID", 0);
            intent.putExtra("ATTRACTIONAREAID", 0);
            if (actionBar != null)
            {
                CharSequence lTitle = actionBar.getTitle();
                if (lTitle != null)
                    intent.putExtra("TITLE", lTitle.toString());
                CharSequence lSubTitle = actionBar.getSubtitle();
                if (lSubTitle != null)
                    intent.putExtra("SUBTITLE", lSubTitle.toString());
            }
            startActivity(intent);
        }
        catch (Exception e)
        {
            ShowError("addBus", e.getMessage());
        }
    }
    
    public void addShow()
    {
        try
        {
            Intent intent = new Intent(getApplicationContext(), ShowDetailsEdit.class);
            intent.putExtra("ACTION", "add");
            intent.putExtra("HOLIDAYID", holidayId);
            intent.putExtra("DAYID", dayId);
            intent.putExtra("ATTRACTIONID", 0);
            intent.putExtra("ATTRACTIONAREAID", 0);
            if (actionBar != null)
            {
                CharSequence lTitle = actionBar.getTitle();
                if (lTitle != null)
                    intent.putExtra("TITLE", lTitle.toString());
                CharSequence lSubTitle = actionBar.getSubtitle();
                if (lSubTitle != null)
                    intent.putExtra("SUBTITLE", lSubTitle.toString());
            }
            startActivity(intent);
        }
        catch (Exception e)
        {
            ShowError("addShow", e.getMessage());
        }
    }
    
    public void addRestaurant()
    {
        try
        {
            Intent intent = new Intent(getApplicationContext(), RestaurantDetailsEdit.class);
            intent.putExtra("ACTION", "add");
            intent.putExtra("HOLIDAYID", holidayId);
            intent.putExtra("DAYID", dayId);
            intent.putExtra("ATTRACTIONID", 0);
            intent.putExtra("ATTRACTIONAREAID", 0);
            if (actionBar != null)
            {
                CharSequence lTitle = actionBar.getTitle();
                if (lTitle != null)
                    intent.putExtra("TITLE", lTitle.toString());
                CharSequence lSubTitle = actionBar.getSubtitle();
                if (lSubTitle != null)
                    intent.putExtra("SUBTITLE", lSubTitle.toString());
            }
            startActivity(intent);
        }
        catch (Exception e)
        {
            ShowError("addRestaurant", e.getMessage());
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
                    addFlight();
                    return true;
                case R.id.action_add_hotel:
                    addHotel();
                    return true;
                case R.id.action_add_show:
                    addShow();
                    return true;
                case R.id.action_add_bus:
                    addBus();
                    return true;
                case R.id.action_add_restaurant:
                    addRestaurant();
                    return true;
                case R.id.action_add_cinema:
                    addCinema();
                    return true;
                case R.id.action_add_park:
                    addPark();
                    return true;
                case R.id.action_add_parade:
                    addParade();
                    return true;
                case R.id.action_add_ride:
                    addRide();
                    return true;
                case R.id.action_add_other:
                    addOther();
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
}
