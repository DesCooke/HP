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
import com.example.des.hp.Schedule.GeneralAttraction.GeneralAttractionDetailsEdit;
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

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
import static com.example.des.hp.myutils.MyMessages.myMessages;

public class AttractionAreaDetailsView extends BaseActivity
{
    
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
    public ImageButton btnShowInfo;
    public BadgeView btnShowInfoBadge;
    public MyColor myColor;
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
            if (attractionAreaItem.noteId == 0)
            {
                MyInt myInt = new MyInt();
                if (!databaseAccess().getNextNoteId(holidayId, myInt))
                    return;
                attractionAreaItem.noteId = myInt.Value;
                if (!databaseAccess().updateAttractionAreaItem(attractionAreaItem))
                    return;
            }
            intent2.putExtra("ACTION", "view");
            intent2.putExtra("HOLIDAYID", attractionAreaItem.holidayId);
            intent2.putExtra("NOTEID", attractionAreaItem.noteId);
            intent2.putExtra("TITLE", attractionAreaItem.attractionAreaDescription);
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
        
        setContentView(R.layout.activity_attractionarea_details_view);
        
        try
        {
            dateUtils = new DateUtils(this);
            imageUtils = new ImageUtils(this);
            myColor = new MyColor(this);
            
            viewPager = (ViewPager) findViewById(R.id.viewpager);
            imageView = (ImageView) findViewById(R.id.imageView);
            tabLayout = (TabLayout) findViewById(R.id.tabs);
            actionBar = getSupportActionBar();
            
            btnShowInfo = (ImageButton) findViewById(R.id.btnShowInfo);
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
    
    private void setupViewPager(ViewPager viewPager)
    {
        try
        {
            PageFragmentAdapter adapter = new PageFragmentAdapter(getSupportFragmentManager());
            if (f_schedule == null)
            {
                f_schedule = new PageScheduleFragment();
                f_schedule.title = "";
                f_schedule.subTitle = "";
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
                    attractionId = extras.getInt("ATTRACTIONID");
                    attractionAreaId = extras.getInt("ATTRACTIONAREAID");
                    attractionAreaItem = new AttractionAreaItem();
                    if (!databaseAccess().getAttractionAreaItem(holidayId, attractionId, attractionAreaId, attractionAreaItem))
                        return;
                    
                    HolidayItem holidayItem = new HolidayItem();
                    if (!databaseAccess().getHolidayItem(holidayId, holidayItem))
                        return;
                    
                    if (attractionAreaItem.attractionAreaPicture.length() > 0)
                        if (!imageUtils.getPageHeaderImage(this, attractionAreaItem.attractionAreaPicture, imageView))
                            return;
                    
                    String lSubTitle;
                    lSubTitle = attractionAreaItem.attractionAreaDescription;
                    
                    if (actionBar != null)
                    {
                        actionBar.setTitle(holidayItem.holidayName);
                        actionBar.setSubtitle(lSubTitle);
                    }
                    
                    MyInt lFileCount = new MyInt();
                    lFileCount.Value = 0;
                    if (attractionAreaItem.infoId > 0)
                    {
                        if (!databaseAccess().getExtraFilesCount(attractionAreaItem.infoId, lFileCount))
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
                    if (!databaseAccess().getNoteItem(attractionAreaItem.holidayId, attractionAreaItem.noteId, noteItem))
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
    
    
    public void editAttractionArea()
    {
        try
        {
            Intent intent = new Intent(getApplicationContext(), AttractionAreaDetailsEdit.class);
            intent.putExtra("ACTION", "modify");
            intent.putExtra("HOLIDAYID", holidayId);
            intent.putExtra("DAYID", 0);
            intent.putExtra("ATTRACTIONID", attractionId);
            intent.putExtra("ATTRACTIONAREAID", attractionAreaId);
            startActivity(intent);
        }
        catch (Exception e)
        {
            ShowError("editAttractionArea", e.getMessage());
        }
    }
    
    public void showInfo(View view)
    {
        try
        {
            Intent intent2 = new Intent(getApplicationContext(), ExtraFilesDetailsList.class);
            if (attractionAreaItem.infoId == 0)
            {
                MyInt myInt = new MyInt();
                if (!databaseAccess().getNextFileGroupId(myInt))
                    return;
                String lstr = "infoid is 0 - next one is " + myInt.Value;
                myMessages().ShowMessageLong(lstr);
                attractionAreaItem.infoId = myInt.Value;
                if (!databaseAccess().updateAttractionAreaItem(attractionAreaItem))
                    return;
            }
            intent2.putExtra("FILEGROUPID", attractionAreaItem.infoId);
            intent2.putExtra("TITLE", attractionAreaItem.attractionAreaDescription);
            intent2.putExtra("SUBTITLE", "Info");
            startActivity(intent2);
        }
        catch (Exception e)
        {
            ShowError("showInfo", e.getMessage());
        }
        
    }
    
    
    public void viewAttractionArea()
    {
        try
        {
            Intent intent = new Intent(getApplicationContext(), AttractionAreaView.class);
            intent.putExtra("ACTION", "view");
            intent.putExtra("HOLIDAYID", holidayId);
            intent.putExtra("DAYID", 0);
            intent.putExtra("ATTRACTIONID", attractionId);
            intent.putExtra("ATTRACTIONAREAID", attractionAreaId);
            startActivity(intent);
        }
        catch (Exception e)
        {
            ShowError("viewAttractionArea", e.getMessage());
        }
        
    }
    
    public void addFlight()
    {
        try
        {
            Intent intent = new Intent(getApplicationContext(), FlightDetailsEdit.class);
            intent.putExtra("ACTION", "add");
            intent.putExtra("HOLIDAYID", holidayId);
            intent.putExtra("DAYID", 0);
            intent.putExtra("ATTRACTIONID", attractionId);
            intent.putExtra("ATTRACTIONAREAID", attractionAreaId);
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
            intent.putExtra("DAYID", 0);
            intent.putExtra("ATTRACTIONID", attractionId);
            intent.putExtra("ATTRACTIONAREAID", attractionAreaId);
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
            intent.putExtra("DAYID", 0);
            intent.putExtra("ATTRACTIONID", attractionId);
            intent.putExtra("ATTRACTIONAREAID", attractionAreaId);
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
            intent.putExtra("DAYID", 0);
            intent.putExtra("ATTRACTIONID", attractionId);
            intent.putExtra("ATTRACTIONAREAID", attractionAreaId);
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
    
    public void addParade()
    {
        try
        {
            Intent intent = new Intent(getApplicationContext(), ParadeDetailsEdit.class);
            intent.putExtra("ACTION", "add");
            intent.putExtra("HOLIDAYID", holidayId);
            intent.putExtra("DAYID", 0);
            intent.putExtra("ATTRACTIONID", attractionId);
            intent.putExtra("ATTRACTIONAREAID", attractionAreaId);
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
            intent.putExtra("DAYID", 0);
            intent.putExtra("ATTRACTIONID", attractionId);
            intent.putExtra("ATTRACTIONAREAID", attractionAreaId);
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
            intent.putExtra("DAYID", 0);
            intent.putExtra("ATTRACTIONID", attractionId);
            intent.putExtra("ATTRACTIONAREAID", attractionAreaId);
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
            intent.putExtra("DAYID", 0);
            intent.putExtra("ATTRACTIONID", attractionId);
            intent.putExtra("ATTRACTIONAREAID", attractionAreaId);
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
            intent.putExtra("DAYID", 0);
            intent.putExtra("ATTRACTIONID", attractionId);
            intent.putExtra("ATTRACTIONAREAID", attractionAreaId);
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
    
    public void addRide()
    {
        try
        {
            Intent intent = new Intent(getApplicationContext(), RideDetailsEdit.class);
            intent.putExtra("ACTION", "add");
            intent.putExtra("HOLIDAYID", holidayId);
            intent.putExtra("DAYID", 0);
            intent.putExtra("ATTRACTIONID", attractionId);
            intent.putExtra("ATTRACTIONAREAID", attractionAreaId);
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

    public void addGeneralAttraction()
    {
        try
        {
            Intent intent = new Intent(getApplicationContext(), GeneralAttractionDetailsEdit.class);
            intent.putExtra("ACTION", "add");
            intent.putExtra("HOLIDAYID", holidayId);
            intent.putExtra("DAYID", 0);
            intent.putExtra("ATTRACTIONID", attractionId);
            intent.putExtra("ATTRACTIONAREAID", attractionAreaId);
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
            ShowError("addGeneralAttraction", e.getMessage());
        }
    }

    public void deleteAttractionArea()
    {
        try
        {
            if (!databaseAccess().deleteAttractionAreaItem(attractionAreaItem))
                return;
            finish();
        }
        catch (Exception e)
        {
            ShowError("deleteAttractionArea", e.getMessage());
        }
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        try
        {
            switch (item.getItemId())
            {
                case R.id.action_view_attractionarea:
                    viewAttractionArea();
                    break;
                case R.id.action_edit_attractionarea:
                    editAttractionArea();
                    break;
                case R.id.action_delete_attractionarea:
                    deleteAttractionArea();
                    break;
                case R.id.action_add_flight:
                    addFlight();
                    break;
                case R.id.action_add_hotel:
                    addHotel();
                    break;
                case R.id.action_add_show:
                    addShow();
                    break;
                case R.id.action_add_bus:
                    addBus();
                    break;
                case R.id.action_add_restaurant:
                    addRestaurant();
                    break;
                case R.id.action_add_cinema:
                    addCinema();
                    break;
                case R.id.action_add_park:
                    addPark();
                    break;
                case R.id.action_add_parade:
                    addParade();
                    break;
                case R.id.action_add_ride:
                    addRide();
                    break;
                case R.id.action_add_other:
                    addOther();
                    break;
                case R.id.action_add_generalattraction:
                    addGeneralAttraction();
                    break;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
        catch (Exception e)
        {
            ShowError("onOptionsItemSelected", e.getMessage());
        }
        return (true);
    }
    
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try
        {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.attractionareadetailsformmenu, menu);
        }
        catch (Exception e)
        {
            ShowError("onCreateOptionsMenu", e.getMessage());
        }
        return true;
    }
}
