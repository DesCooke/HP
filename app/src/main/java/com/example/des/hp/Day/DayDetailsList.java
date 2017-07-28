package com.example.des.hp.Day;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.des.hp.Dialog.BaseFullPageRecycleView;
import com.example.des.hp.Dialog.BaseItem;
import com.example.des.hp.R;
import com.example.des.hp.Holiday.*;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;


public class DayDetailsList extends BaseFullPageRecycleView
{
    public ArrayList<DayItem> dayList;
    public HolidayItem holidayItem;
    public DayAdapter dayAdapter;

    @Override
    public void setInfoId(int pInfoId){}
    @Override
    public int getInfoId(){return 0;}
    
    @Override
    public void setNoteId(int pNoteId){}
    @Override
    public int getNoteId(){return 0;}

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

            databaseAccess().currentStartDate=holidayItem.startDateDate;

            dayList=new ArrayList<>();
            if(!databaseAccess().getDayList(holidayId, dayList))
                return;
            dayAdapter=new DayAdapter(this, dayList);

            CreateRecyclerView(R.id.dayListView, dayAdapter);

            dayAdapter.setOnItemClickListener(new DayAdapter.OnItemClickListener()
            {
                @Override
                public void onItemClick(View view, DayItem obj, int position)
                {
                    Intent intent=new Intent(getApplicationContext(), DayDetailsView.class);
                    intent.putExtra("ACTION", "view");
                    intent.putExtra("HOLIDAYID", dayList.get(position).holidayId);
                    intent.putExtra("DAYID", dayList.get(position).dayId);
                    startActivity(intent);
                }
            });

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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            setContentView(R.layout.activity_day_list);

            showForm();
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }

    }

}

