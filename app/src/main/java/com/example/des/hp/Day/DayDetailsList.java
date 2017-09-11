package com.example.des.hp.Day;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.R;
import com.example.des.hp.Holiday.*;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;


public class DayDetailsList extends BaseActivity
{
    //region Member Variables
    public ArrayList<DayItem> dayList;
    public HolidayItem holidayItem;
    public DayAdapter dayAdapter;
    //endregion

    //region Constructors/Destructors
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            layoutName="activity_day_list";
            setContentView(R.layout.activity_day_list);

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
            inflater.inflate(R.menu.day_list_add, menu);
        }
        catch(Exception e)
        {
            ShowError("onCreateOptionsMenu", e.getMessage());
        }

        return true;
    }
    //endregion

    //region Form Functions
    public void showDayAdd(View view)
    {
        try
        {
            Intent intent=new Intent(getApplicationContext(), DayDetailsEdit.class);
            intent.putExtra("ACTION", "add");
            intent.putExtra("HOLIDAYID", holidayId);
            startActivity(intent);
        }
        catch(Exception e)
        {
            ShowError("showDayAdd", e.getMessage());
        }
    }

    public void showForm()
    {
        super.showForm();
        try
        {
            allowCellMove=true;

            holidayItem=new HolidayItem();
            if(!databaseAccess().getHolidayItem(holidayId, holidayItem))
                return;

            SetTitles(holidayItem.holidayName, "Itinerary");

            DatabaseAccess.currentStartDate=holidayItem.startDateDate;

            dayList=new ArrayList<>();
            if(!databaseAccess().getDayList(holidayId, dayList))
                return;
            dayAdapter=new DayAdapter(this, dayList);

            CreateRecyclerView(R.id.dayListView, dayAdapter);

            dayAdapter.setOnItemClickListener(new DayAdapter.OnItemClickListener()
            {
                @Override
                public void onItemClick(View view, DayItem obj)
                {
                    Intent intent=new Intent(getApplicationContext(), DayDetailsView.class);
                    intent.putExtra("ACTION", "view");
                    intent.putExtra("HOLIDAYID", obj.holidayId);
                    intent.putExtra("DAYID", obj.dayId);
                    startActivity(intent);
                }
            });

            afterShow();
        }
        catch(Exception e)
        {
            ShowError("showForm", e.getMessage());
        }

    }

    @Override
    public void SwapItems(int from, int to)
    {
        Collections.swap(dayAdapter.data, from, to);
    }

    @Override
    public void OnItemMove(int from, int to)
    {
        dayAdapter.onItemMove(from, to);
    }

    @Override
    public void NotifyItemMoved(int from, int to)
    {
        dayAdapter.notifyItemMoved(from, to);
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
                case R.id.action_add_day:
                    showDayAdd(null);
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


}

