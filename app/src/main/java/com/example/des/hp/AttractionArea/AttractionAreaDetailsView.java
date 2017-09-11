/*
Shows a list of  attractionarea items (futureword etc) for the current attraction (magic kingdom)
 */
package com.example.des.hp.AttractionArea;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.R;
import com.example.des.hp.Schedule.Bus.BusDetailsEdit;
import com.example.des.hp.Schedule.Bus.BusDetailsView;
import com.example.des.hp.Schedule.Cinema.CinemaDetailsEdit;
import com.example.des.hp.Schedule.Cinema.CinemaDetailsView;
import com.example.des.hp.Schedule.Flight.FlightDetailsEdit;
import com.example.des.hp.Schedule.Flight.FlightDetailsView;
import com.example.des.hp.Schedule.GeneralAttraction.GeneralAttractionDetailsEdit;
import com.example.des.hp.Schedule.GeneralAttraction.GeneralAttractionDetailsView;
import com.example.des.hp.Schedule.Hotel.HotelDetailsEdit;
import com.example.des.hp.Schedule.Hotel.HotelDetailsView;
import com.example.des.hp.Schedule.Other.OtherDetailsEdit;
import com.example.des.hp.Schedule.Other.OtherDetailsView;
import com.example.des.hp.Schedule.Parade.ParadeDetailsEdit;
import com.example.des.hp.Schedule.Parade.ParadeDetailsView;
import com.example.des.hp.Schedule.Park.ParkDetailsEdit;
import com.example.des.hp.Schedule.Park.ParkDetailsView;
import com.example.des.hp.Schedule.Restaurant.RestaurantDetailsEdit;
import com.example.des.hp.Schedule.Restaurant.RestaurantDetailsView;
import com.example.des.hp.Schedule.Ride.RideDetailsEdit;
import com.example.des.hp.Schedule.Ride.RideDetailsView;
import com.example.des.hp.Schedule.ScheduleAdapter;
import com.example.des.hp.Schedule.ScheduleItem;
import com.example.des.hp.Schedule.Show.ShowDetailsEdit;
import com.example.des.hp.Schedule.Show.ShowDetailsView;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

public class AttractionAreaDetailsView extends BaseActivity
{

    //region Member variables
    public ArrayList<ScheduleItem> scheduleList;
    public ScheduleAdapter scheduleAdapter;
    public AttractionAreaItem attractionAreaItem;
    public LinearLayout grpMenuFile;
    public TextView txtAttractionAreaDescription;
    //endregion

    //region Constructors/Destructors
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            layoutName = "activity_attractionarea_details_view";
            setContentView(R.layout.activity_attractionarea_details_view);

            txtAttractionAreaDescription = (TextView) findViewById(R.id.txtAttractionAreaDescription);
            grpMenuFile = (LinearLayout) findViewById(R.id.grpMenuFile);

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
            inflater.inflate(R.menu.attractionareadetailsformmenu, menu);
        }
        catch (Exception e)
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
        boolean lv_return = false;
        try
        {
            switch(item.getItemId())
            {
                case R.id.action_edit_attractionarea:
                    showAttractionAreaEdit();
                    return(true);
                case R.id.action_delete_attractionarea:
                    deleteAttractionArea();
                    return(true);
                case R.id.action_view_attractionarea:
                    showAttractionAreaView();
                    return(true);
                case R.id.action_add_flight:
                    StartNewAddIntent(FlightDetailsEdit.class);
                    return(true);
                case R.id.action_add_hotel:
                    StartNewAddIntent(HotelDetailsEdit.class);
                    return(true);
                case R.id.action_add_show:
                    StartNewAddIntent(ShowDetailsEdit.class);
                    return(true);
                case R.id.action_add_bus:
                    StartNewAddIntent(BusDetailsEdit.class);
                    return(true);
                case R.id.action_add_restaurant:
                    StartNewAddIntent(RestaurantDetailsEdit.class);
                    return(true);
                case R.id.action_add_cinema:
                    StartNewAddIntent(CinemaDetailsEdit.class);
                    return(true);
                case R.id.action_add_park:
                    StartNewAddIntent(ParkDetailsEdit.class);
                    return(true);
                case R.id.action_add_parade:
                    StartNewAddIntent(ParadeDetailsEdit.class);
                    return(true);
                case R.id.action_add_ride:
                    StartNewAddIntent(RideDetailsEdit.class);
                    return(true);
                case R.id.action_add_other:
                    StartNewAddIntent(OtherDetailsEdit.class);
                    return(true);
                case R.id.action_add_generalattraction:
                    StartNewAddIntent(GeneralAttractionDetailsEdit.class);
                    return(true);
                default:
                    lv_return = super.onOptionsItemSelected(item);
            }
        }
        catch (Exception e)
        {
            ShowError("onOptionsItemSelected", e.getMessage());
        }
        return (lv_return);
    }

    public void StartNewAddIntent(Class neededClass)
    {
        try
        {
            Intent intent=new Intent(getApplicationContext(), neededClass);
            intent.putExtra("ACTION", "add");
            intent.putExtra("HOLIDAYID", holidayId);
            intent.putExtra("DAYID", 0);
            intent.putExtra("ATTRACTIONID", attractionId);
            intent.putExtra("ATTRACTIONAREAID", attractionAreaId);
            intent.putExtra("TITLE", title);
            intent.putExtra("SUBTITLE", subTitle);
            startActivity(intent);
        }
        catch(Exception e)
        {
            ShowError("StartNewAddIntent", e.getMessage());
        }
    }


    //endregion

    //region showForm
    public void showForm()
    {
        super.showForm();
        try
        {
            allowCellMove = true;

            attractionAreaItem = new AttractionAreaItem();
            if (!databaseAccess().getAttractionAreaItem(holidayId, attractionId, attractionAreaId, attractionAreaItem))
                return;

            scheduleList = new ArrayList<>();
            if (!databaseAccess().getScheduleList(holidayId, 0, attractionId, attractionAreaId, scheduleList))
                return;

            SetImage(attractionAreaItem.attractionAreaPicture);

            txtAttractionAreaDescription.setText(attractionAreaItem.attractionAreaDescription);

            subTitle = attractionAreaItem.attractionAreaDescription;
            if (title.length() > 0)
            {
                SetTitles(title, subTitle);
            } else
            {
                SetTitles("ATTRACTIONS", "");
            }

            scheduleAdapter = new ScheduleAdapter(this, scheduleList);

            CreateRecyclerView(R.id.attractionAreaListView, scheduleAdapter);

            scheduleAdapter.setOnItemClickListener
                (
                    new ScheduleAdapter.OnItemClickListener()
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
                    }
                );
            afterShow();
        }
        catch (Exception e)
        {
            ShowError("showForm", e.getMessage());
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
    //endregion

    //region form Functions
    @Override
    public int getInfoId()
    {
        return (attractionAreaItem.infoId);
    }

    public void setNoteId(int pNoteId)
    {
        attractionAreaItem.noteId=pNoteId;
        databaseAccess().updateAttractionAreaItem(attractionAreaItem);
    }

    @Override
    public int getNoteId()
    {
        return (attractionAreaItem.noteId);
    }

    @Override
    public void setInfoId(int pInfoId)
    {
        attractionAreaItem.infoId=pInfoId;
        databaseAccess().updateAttractionAreaItem(attractionAreaItem);
    }

    public void showAttractionAreaView()
    {
        try
        {
            Intent intent = new Intent(getApplicationContext(), AttractionAreaView.class);
            intent.putExtra("ACTION", "view");
            intent.putExtra("HOLIDAYID", holidayId);
            intent.putExtra("ATTRACTIONID", attractionId);
            intent.putExtra("ATTRACTIONAREAID", attractionAreaId);
            startActivity(intent);
        }
        catch (Exception e)
        {
            ShowError("showAttractionAreaAdd", e.getMessage());
        }
    }

    public void showAttractionAreaEdit()
    {
        try
        {
            Intent intent = new Intent(getApplicationContext(), AttractionAreaDetailsEdit.class);
            intent.putExtra("ACTION", "modify");
            intent.putExtra("HOLIDAYID", holidayId);
            intent.putExtra("ATTRACTIONID", attractionId);
            intent.putExtra("ATTRACTIONAREAID", attractionAreaId);
            startActivity(intent);
        }
        catch (Exception e)
        {
            ShowError("showAttractionAreaAdd", e.getMessage());
        }
    }

    public void deleteAttractionArea()
    {
        try
        {
            if(!databaseAccess().deleteAttractionAreaItem(attractionAreaItem))
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

