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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

import androidx.annotation.NonNull;


public class DayDetailsList extends BaseActivity
{
    //region Member Variables
    public ArrayList<DayItem> dayList;
    public HolidayItem holidayItem;
    private DayAdapter dayAdapter;
    public FloatingActionButton fab;
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
            intent.putExtra("HOLIDAYNAME", holidayItem.holidayName);
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
            dayList=new ArrayList<>();
            try(DatabaseAccess da = databaseAccess())
            {
                if(!da.getHolidayItem(holidayId, holidayItem))
                    return;

                SetToolbarTitles(holidayItem.holidayName, "Itinerary");

                DatabaseAccess.currentStartDate=holidayItem.startDateDate;

                if(!da.getDayList(holidayId, dayList))
                    return;
            }

            dayAdapter=new DayAdapter(this, dayList);

            CreateRecyclerView(R.id.dayListView, dayAdapter);

            dayAdapter.setOnItemClickListener((view, obj) -> {
                Intent intent=new Intent(getApplicationContext(), DayDetailsView.class);
                intent.putExtra("ACTION", "view");
                intent.putExtra("HOLIDAYID", obj.holidayId);
                intent.putExtra("DAYID", obj.dayId);
                startActivity(intent);
            });

            fab=findViewById(R.id.fab);
            if(fab!=null)
                fab.setOnClickListener(this::showDayAdd);

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
        try
        {
            Collections.swap(dayAdapter.data, from, to);
        }
        catch(Exception e)
        {
            ShowError("SwapItems", e.getMessage());
        }

    }

    @Override
    public void OnItemMove(int from, int to)
    {
        try
        {
            dayAdapter.onItemMove(from, to);
        }
        catch(Exception e)
        {
            ShowError("OnItemMove", e.getMessage());
        }

    }

    @Override
    public void NotifyItemMoved(int from, int to)
    {
        try
        {
            dayAdapter.notifyItemMoved(from, to);

        }
        catch(Exception e)
        {
            ShowError("NotifyItemMoved", e.getMessage());
        }

    }

    //endregion

    //region OnClick Events
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        try
        {
            int id=item.getItemId();
            if(id==R.id.action_add_day)
                showDayAdd(null);
        }
        catch(Exception e)
        {
            ShowError("onOptionsItemSelected", e.getMessage());
        }
        return true;
    }
    //endregion


}

