package com.example.des.hp.Day;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.R;
import com.example.des.hp.Holiday.*;
import com.example.des.hp.myutils.*;

import java.util.ArrayList;
import java.util.Collections;


/**
 * * Created by Des on 02/11/2016.
 */

public class DayDetailsList extends BaseActivity
{
    public DatabaseAccess databaseAccess;
    public ArrayList<DayItem> dayList;
    public int holidayId;
    public HolidayItem holidayItem;
    public DayAdapter dayAdapter;
    public MyMessages myMessages;
    
    public void showDayAdd(View view)
    {
        try
        {
            Intent intent = new Intent(getApplicationContext(), DayDetailsEdit.class);
            intent.putExtra("ACTION", "add");
            intent.putExtra("HOLIDAYID", holidayId);
            startActivity(intent);
        }
        catch (Exception e)
        {
            ShowError("showDayAdd", e.getMessage());
        }
    }
    
    public void showForm()
    {
        try
        {
            databaseAccess = new DatabaseAccess(this);
            holidayItem = new HolidayItem();
            if (!databaseAccess.getHolidayItem(holidayId, holidayItem))
                return;
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null)
            {
                actionBar.setTitle(holidayItem.holidayName);
                actionBar.setSubtitle("Itinerary");
            }
            
            DatabaseAccess.currentStartDate = holidayItem.startDateDate;
            dayList = new ArrayList<>();
            if (!databaseAccess.getDayList(holidayId, dayList))
                return;
            
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.dayListView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this/*getActivity()*/));
            recyclerView.setHasFixedSize(true);
            //listView1.setDivider(null);
            dayAdapter = new DayAdapter(this, dayList);
            recyclerView.setAdapter(dayAdapter);
            
            itemTouchHelper.attachToRecyclerView(recyclerView);
            
            dayAdapter.setOnItemClickListener
                (
                    new DayAdapter.OnItemClickListener()
                    {
                        @Override
                        public void onItemClick(View view, DayItem obj, int position)
                        {
                            Intent intent = new Intent(getApplicationContext(), DayDetailsView.class);
                            intent.putExtra("ACTION", "view");
                            intent.putExtra("HOLIDAYID", dayList.get(position).holidayId);
                            intent.putExtra("DAYID", dayList.get(position).dayId);
                            startActivity(intent);
                        }
                    }
                );
            
        }
        catch (Exception e)
        {
            ShowError("showForm", e.getMessage());
        }
        
    }
    
    // handle swipe to delete, and draggable
    ItemTouchHelper itemTouchHelper =
        new ItemTouchHelper
            (
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT)
                {
                    int dragFrom = -1;
                    int dragTo = -1;
                    
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target)
                    {
                        int fromPosition = viewHolder.getAdapterPosition();
                        int toPosition = target.getAdapterPosition();
                        
                        
                        if (dragFrom == -1)
                        {
                            dragFrom = fromPosition;
                        }
                        dragTo = toPosition;
                        
                        if (fromPosition < toPosition)
                        {
                            for (int i = fromPosition; i < toPosition; i++)
                            {
                                Collections.swap(dayAdapter.data, i, i + 1);
                            }
                        } else
                        {
                            for (int i = fromPosition; i > toPosition; i--)
                            {
                                Collections.swap(dayAdapter.data, i, i - 1);
                            }
                        }
                        dayAdapter.notifyItemMoved(fromPosition, toPosition);
                        
                        return true;
                    }
                    
                    @Override
                    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder)
                    {
                        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                        return makeMovementFlags(dragFlags, swipeFlags);
                    }
                    
                    @Override
                    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction)
                    {
                    }
                    
                    @Override
                    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder)
                    {
                        super.clearView(recyclerView, viewHolder);
                        
                        if (dragFrom != -1 && dragTo != -1 && dragFrom != dragTo)
                        {
                            dayAdapter.onItemMove(dragFrom, dragTo);
                        }
                        
                        dragFrom = dragTo = -1;
                    }
                    
                }
            );
    
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
            setContentView(R.layout.activity_day_list);
            
            myMessages = new MyMessages(this);
            
            Bundle extras = getIntent().getExtras();
            if (extras != null)
            {
                holidayId = extras.getInt("HOLIDAYID");
            }
            showForm();
        }
        catch (Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
        
    }
    
}

