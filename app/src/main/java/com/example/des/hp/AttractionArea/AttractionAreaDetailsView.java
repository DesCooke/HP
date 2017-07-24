/*
Shows details of the current attractionarea (futureworld) for the current holiday/attraction
and lists the list of scheduleditems for that area
 */
package com.example.des.hp.AttractionArea;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.graphics.BitmapFactory;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.ExtraFiles.ExtraFilesDetailsList;
import com.example.des.hp.Highlight.PageHighlightsFragment;
import com.example.des.hp.Notes.NoteItem;
import com.example.des.hp.Notes.NoteView;
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

public class AttractionAreaDetailsView extends BaseActivity
{

    public DatabaseAccess databaseAccess;
    private ImageView imageView;
    public int holidayId;
    public int attractionId;
    public int attractionAreaId;
    public DateUtils dateUtils;
    public LinearLayout grpStartDate;
    public String holidayName;
    public AttractionAreaItem attractionAreaItem;
    private ImageUtils imageUtils;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private PageScheduleFragment f_schedule;
    private PageHighlightsFragment f_highlights;
    private ActionBar actionBar;
    public MyMessages myMessages;
    public ImageButton btnShowInfo;
    public BadgeView btnShowInfoBadge;
    public MyColor myColor;
    public ImageButton btnShowNotes;

    public void clearImage(View view)
    {
        imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.imagemissing));
    }
    public void showNotes(View view)
    {
        Intent intent2 = new Intent(getApplicationContext(), NoteView.class);
        if(attractionAreaItem.noteId==0)
        {
            MyInt myInt = new MyInt();
            if(!databaseAccess.getNextNoteId(holidayId, myInt))
                return;
            attractionAreaItem.noteId = myInt.Value;
            if(!databaseAccess.updateAttractionAreaItem(attractionAreaItem))
                return;
        }
        intent2.putExtra("ACTION", "view");
        intent2.putExtra("HOLIDAYID", attractionAreaItem.holidayId);
        intent2.putExtra("NOTEID", attractionAreaItem.noteId);
        intent2.putExtra("TITLE", attractionAreaItem.attractionAreaDescription);
        intent2.putExtra("SUBTITLE", "Notes");
        startActivity(intent2);
    }


    @Override
    protected void onResume(){
        super.onResume();

        try
        {
            showForm();
        }
        catch(Exception e)
        {
            ShowError("onResume", e.getMessage());
        }

    }


    private void ShowError(String argFunction, String argMessage)
    {
        myMessages.ShowError
                (
                        "Error in AttractionAreaDetailsView::" + argFunction,
                        argMessage
                );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_attractionarea_details_view);

        databaseAccess = new DatabaseAccess(this);
        dateUtils = new DateUtils(this);
        imageUtils = new ImageUtils(this);
        myMessages = new MyMessages(this);
        myColor = new MyColor(this);

        viewPager = (ViewPager)findViewById(R.id.viewpager);
        imageView = (ImageView)findViewById(R.id.imageView);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        actionBar = getSupportActionBar();

        btnShowInfo=(ImageButton) findViewById(R.id.btnShowInfo);
        btnShowNotes=(ImageButton) findViewById(R.id.btnShowNotes);

        btnShowInfoBadge = new BadgeView(this, btnShowInfo);
        btnShowInfoBadge.setText(Integer.toString(0));
        btnShowInfoBadge.show();

        showForm();

    }

    private void setupViewPager(ViewPager viewPager)
    {
        PageFragmentAdapter adapter = new PageFragmentAdapter(getSupportFragmentManager());
        if (f_schedule == null)
        {
            f_schedule = new PageScheduleFragment();
            f_schedule.title = "";
            f_schedule.subTitle = "";
            if(actionBar!=null)
            {
                if(actionBar.getTitle()!=null)
                    f_schedule.title = actionBar.getTitle().toString();
                if(actionBar.getSubtitle()!=null)
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

    public void showForm()
    {
        try {
            clearImage(null);

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String action = extras.getString("ACTION");
                if (action != null && action.equals("view")) {
                    holidayId = extras.getInt("HOLIDAYID");
                    attractionId = extras.getInt("ATTRACTIONID");
                    attractionAreaId = extras.getInt("ATTRACTIONAREAID");
                    attractionAreaItem = new AttractionAreaItem();
                    if (!databaseAccess.getAttractionAreaItem(holidayId, attractionId, attractionAreaId, attractionAreaItem))
                        return;

                    HolidayItem holidayItem = new HolidayItem();
                    if (!databaseAccess.getHolidayItem(holidayId, holidayItem))
                        return;

                    if (attractionAreaItem.attractionAreaPicture.length() > 0)
                        if (!imageUtils.getPageHeaderImage(this, attractionAreaItem.attractionAreaPicture, imageView))
                            return;

                    String lSubTitle;
                    lSubTitle = attractionAreaItem.attractionAreaDescription;

                    if (actionBar != null) {
                        actionBar.setTitle(holidayItem.holidayName);
                        actionBar.setSubtitle(lSubTitle);
                    }

                    MyInt lFileCount = new MyInt();
                    lFileCount.Value = 0;
                    if (attractionAreaItem.infoId > 0) {
                        if (!databaseAccess.getExtraFilesCount(attractionAreaItem.infoId, lFileCount))
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
                    if(!databaseAccess.getNoteItem(attractionAreaItem.holidayId, attractionAreaItem.noteId, noteItem))
                        return;
                    if (noteItem.notes.length() == 0)
                    {
                        if (myColor.SetImageButtonTint(btnShowNotes, R.color.colorDisabled) == false)
                            return;
                    } else {
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
        catch(Exception e)
        {
            ShowError("showForm", e.getMessage());
        }

    }

    private void setupTabClick()
    {
    }

    private void setupTabIcons()
    {
        TabLayout.Tab lScheduleTab = tabLayout.getTabAt(0);
        TabLayout.Tab lHighlightsTab = tabLayout.getTabAt(1);
        if(lScheduleTab!=null)
            lScheduleTab.setText(getResources().getString(R.string.tab_schedule));
        if(lHighlightsTab!=null)
            lHighlightsTab.setText(getResources().getString(R.string.tab_highlights));
    }


    public void editAttractionArea()
    {
        Intent intent = new Intent(getApplicationContext(), AttractionAreaDetailsEdit.class);
        intent.putExtra("ACTION", "modify");
        intent.putExtra("HOLIDAYID", holidayId);
        intent.putExtra("DAYID", 0);
        intent.putExtra("ATTRACTIONID", attractionId);
        intent.putExtra("ATTRACTIONAREAID", attractionAreaId);
        startActivity(intent);
    }

    public void showInfo(View view)
    {
        Intent intent2 = new Intent(getApplicationContext(), ExtraFilesDetailsList.class);
        if(attractionAreaItem.infoId==0)
        {
            MyInt myInt = new MyInt();
            if(!databaseAccess.getNextFileGroupId(myInt))
                return;
            String lstr="infoid is 0 - next one is " + myInt.Value;
            myMessages.ShowMessageLong(lstr);
            attractionAreaItem.infoId = myInt.Value;
            if(!databaseAccess.updateAttractionAreaItem(attractionAreaItem))
                return;
        }
        intent2.putExtra("FILEGROUPID", attractionAreaItem.infoId);
        intent2.putExtra("TITLE", attractionAreaItem.attractionAreaDescription);
        intent2.putExtra("SUBTITLE", "Info");
        startActivity(intent2);
    }


    public void viewAttractionArea()
    {
        Intent intent = new Intent(getApplicationContext(), AttractionAreaView.class);
        intent.putExtra("ACTION", "view");
        intent.putExtra("HOLIDAYID", holidayId);
        intent.putExtra("DAYID", 0);
        intent.putExtra("ATTRACTIONID", attractionId);
        intent.putExtra("ATTRACTIONAREAID", attractionAreaId);
        startActivity(intent);
    }

    public void addFlight()
    {
        Intent intent = new Intent(getApplicationContext(), FlightDetailsEdit.class);
        intent.putExtra("ACTION", "add");
        intent.putExtra("HOLIDAYID", holidayId);
        intent.putExtra("DAYID", 0);
        intent.putExtra("ATTRACTIONID", attractionId);
        intent.putExtra("ATTRACTIONAREAID", attractionAreaId);
        if(actionBar!=null)
        {
            CharSequence lTitle = actionBar.getTitle();
            if(lTitle!=null)
                intent.putExtra("TITLE", lTitle.toString());
            CharSequence lSubTitle = actionBar.getSubtitle();
            if(lSubTitle!=null)
                intent.putExtra("SUBTITLE", lSubTitle.toString());
        }
        startActivity(intent);
    }

    public void addHotel()
    {
        Intent intent = new Intent(getApplicationContext(), HotelDetailsEdit.class);
        intent.putExtra("ACTION", "add");
        intent.putExtra("HOLIDAYID", holidayId);
        intent.putExtra("DAYID", 0);
        intent.putExtra("ATTRACTIONID", attractionId);
        intent.putExtra("ATTRACTIONAREAID", attractionAreaId);
        if(actionBar!=null)
        {
            CharSequence lTitle = actionBar.getTitle();
            if(lTitle!=null)
                intent.putExtra("TITLE", lTitle.toString());
            CharSequence lSubTitle = actionBar.getSubtitle();
            if(lSubTitle!=null)
                intent.putExtra("SUBTITLE", lSubTitle.toString());
        }
        startActivity(intent);
    }

    public void addCinema()
    {
        Intent intent = new Intent(getApplicationContext(), CinemaDetailsEdit.class);
        intent.putExtra("ACTION", "add");
        intent.putExtra("HOLIDAYID", holidayId);
        intent.putExtra("DAYID", 0);
        intent.putExtra("ATTRACTIONID", attractionId);
        intent.putExtra("ATTRACTIONAREAID", attractionAreaId);
        if(actionBar!=null)
        {
            CharSequence lTitle = actionBar.getTitle();
            if(lTitle!=null)
                intent.putExtra("TITLE", lTitle.toString());
            CharSequence lSubTitle = actionBar.getSubtitle();
            if(lSubTitle!=null)
                intent.putExtra("SUBTITLE", lSubTitle.toString());
        }
        startActivity(intent);
    }

    public void addPark()
    {
        Intent intent = new Intent(getApplicationContext(), ParkDetailsEdit.class);
        intent.putExtra("ACTION", "add");
        intent.putExtra("HOLIDAYID", holidayId);
        intent.putExtra("DAYID", 0);
        intent.putExtra("ATTRACTIONID", attractionId);
        intent.putExtra("ATTRACTIONAREAID", attractionAreaId);
        if(actionBar!=null)
        {
            CharSequence lTitle = actionBar.getTitle();
            if(lTitle!=null)
                intent.putExtra("TITLE", lTitle.toString());
            CharSequence lSubTitle = actionBar.getSubtitle();
            if(lSubTitle!=null)
                intent.putExtra("SUBTITLE", lSubTitle.toString());
        }
        startActivity(intent);
    }

    public void addParade()
    {
        Intent intent = new Intent(getApplicationContext(), ParadeDetailsEdit.class);
        intent.putExtra("ACTION", "add");
        intent.putExtra("HOLIDAYID", holidayId);
        intent.putExtra("DAYID", 0);
        intent.putExtra("ATTRACTIONID", attractionId);
        intent.putExtra("ATTRACTIONAREAID", attractionAreaId);
        if(actionBar!=null)
        {
            CharSequence lTitle = actionBar.getTitle();
            if(lTitle!=null)
                intent.putExtra("TITLE", lTitle.toString());
            CharSequence lSubTitle = actionBar.getSubtitle();
            if(lSubTitle!=null)
                intent.putExtra("SUBTITLE", lSubTitle.toString());
        }
        startActivity(intent);
    }

    public void addOther()
    {
        Intent intent = new Intent(getApplicationContext(), OtherDetailsEdit.class);
        intent.putExtra("ACTION", "add");
        intent.putExtra("HOLIDAYID", holidayId);
        intent.putExtra("DAYID", 0);
        intent.putExtra("ATTRACTIONID", attractionId);
        intent.putExtra("ATTRACTIONAREAID", attractionAreaId);
        if(actionBar!=null)
        {
            CharSequence lTitle = actionBar.getTitle();
            if(lTitle!=null)
                intent.putExtra("TITLE", lTitle.toString());
            CharSequence lSubTitle = actionBar.getSubtitle();
            if(lSubTitle!=null)
                intent.putExtra("SUBTITLE", lSubTitle.toString());
        }
        startActivity(intent);
    }

    public void addBus()
    {
        Intent intent = new Intent(getApplicationContext(), BusDetailsEdit.class);
        intent.putExtra("ACTION", "add");
        intent.putExtra("HOLIDAYID", holidayId);
        intent.putExtra("DAYID", 0);
        intent.putExtra("ATTRACTIONID", attractionId);
        intent.putExtra("ATTRACTIONAREAID", attractionAreaId);
        if(actionBar!=null)
        {
            CharSequence lTitle = actionBar.getTitle();
            if(lTitle!=null)
                intent.putExtra("TITLE", lTitle.toString());
            CharSequence lSubTitle = actionBar.getSubtitle();
            if(lSubTitle!=null)
                intent.putExtra("SUBTITLE", lSubTitle.toString());
        }
        startActivity(intent);
    }

    public void addShow()
    {
        Intent intent = new Intent(getApplicationContext(), ShowDetailsEdit.class);
        intent.putExtra("ACTION", "add");
        intent.putExtra("HOLIDAYID", holidayId);
        intent.putExtra("DAYID", 0);
        intent.putExtra("ATTRACTIONID", attractionId);
        intent.putExtra("ATTRACTIONAREAID", attractionAreaId);
        if(actionBar!=null)
        {
            CharSequence lTitle = actionBar.getTitle();
            if(lTitle!=null)
                intent.putExtra("TITLE", lTitle.toString());
            CharSequence lSubTitle = actionBar.getSubtitle();
            if(lSubTitle!=null)
                intent.putExtra("SUBTITLE", lSubTitle.toString());
        }
        startActivity(intent);
    }

    public void addRestaurant()
    {
        Intent intent = new Intent(getApplicationContext(), RestaurantDetailsEdit.class);
        intent.putExtra("ACTION", "add");
        intent.putExtra("HOLIDAYID", holidayId);
        intent.putExtra("DAYID", 0);
        intent.putExtra("ATTRACTIONID", attractionId);
        intent.putExtra("ATTRACTIONAREAID", attractionAreaId);
        if(actionBar!=null)
        {
            CharSequence lTitle = actionBar.getTitle();
            if(lTitle!=null)
                intent.putExtra("TITLE", lTitle.toString());
            CharSequence lSubTitle = actionBar.getSubtitle();
            if(lSubTitle!=null)
                intent.putExtra("SUBTITLE", lSubTitle.toString());
        }
        startActivity(intent);
    }

    public void addRide()
    {
        Intent intent = new Intent(getApplicationContext(), RideDetailsEdit.class);
        intent.putExtra("ACTION", "add");
        intent.putExtra("HOLIDAYID", holidayId);
        intent.putExtra("DAYID", 0);
        intent.putExtra("ATTRACTIONID", attractionId);
        intent.putExtra("ATTRACTIONAREAID", attractionAreaId);
        if(actionBar!=null)
        {
            CharSequence lTitle = actionBar.getTitle();
            if(lTitle!=null)
                intent.putExtra("TITLE", lTitle.toString());
            CharSequence lSubTitle = actionBar.getSubtitle();
            if(lSubTitle!=null)
                intent.putExtra("SUBTITLE", lSubTitle.toString());
        }
        startActivity(intent);
    }

    public void deleteAttractionArea()
    {
        if(!databaseAccess.deleteAttractionAreaItem(attractionAreaItem))
            return;
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_view_attractionarea:
                viewAttractionArea();
                return true;
            case R.id.action_edit_attractionarea:
                editAttractionArea();
                return true;
            case R.id.action_delete_attractionarea:
                deleteAttractionArea();
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

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.attractionareadetailsformmenu, menu);
        return true;
    }
}
