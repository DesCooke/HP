package com.example.des.hp.ScheduleArea;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.des.hp.Attraction.AttractionDetailsEdit;
import com.example.des.hp.Attraction.AttractionDetailsView;
import com.example.des.hp.Attraction.AttractionItem;
import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.R;
import com.example.des.hp.myutils.*;

import java.util.ArrayList;
import java.util.Collections;

/**
 ** Created by Des on 02/11/2016.
 */

public class ScheduleAreaList extends BaseActivity
{

    public DatabaseAccess databaseAccess;
    public ArrayList<ScheduleAreaItem> scheduleAreaList;
    public int holidayId;
    public int dayId;
    public int attractionId;
    public int attractionAreaId;
    public int scheduleId;
    public ScheduleAreaAdapter scheduleAreaAdapter;
    public String title;
    public String subtitle;
    public ActionBar actionBar;
    public AttractionItem attractionItem;
    public MyMessages myMessages;

    public void showForm()
    {
        try {
            if (actionBar != null)
            {
                actionBar.setTitle("MOVE TO ANOTHER LOCATION");
                actionBar.setSubtitle("");
            }

            scheduleAreaList = new ArrayList<>();
            if (!databaseAccess.getScheduleAreaList(holidayId, scheduleAreaList))
                return;

            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.scheduleAreaListView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setHasFixedSize(true);
            //listView1.setDivider(null);
            scheduleAreaAdapter = new ScheduleAreaAdapter(this, scheduleAreaList);
            recyclerView.setAdapter(scheduleAreaAdapter);

            scheduleAreaAdapter.setOnItemClickListener
                    (
                            new ScheduleAreaAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, ScheduleAreaItem obj, int position)
                                {
                                    Intent intent = new Intent();
                                    intent.putExtra("DAYID",obj.dayId);
                                    intent.putExtra("ATTRACTIONID", obj.attractionId);
                                    intent.putExtra("ATTRACTIONAREAID", obj.attractionAreaId);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }
                            }
                    );
        }
        catch(Exception e)
        {
            ShowError("showForm", e.getMessage());
        }
    }

    private void ShowError(String argFunction, String argMessage)
    {
        myMessages.ShowError
                (
                        "Error in ScheduleAreaDetailsList::" + argFunction,
                        argMessage
                );
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedulearea_list);

        databaseAccess = new DatabaseAccess(this);
        actionBar = getSupportActionBar();
        myMessages = new MyMessages(this);

        title = "";
        subtitle = "";
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            holidayId = extras.getInt("HOLIDAYID");
            dayId = extras.getInt("DAYID");
            attractionId = extras.getInt("ATTRACTIONID");
            attractionAreaId = extras.getInt("ATTRACTIONAREAID");
            scheduleId = extras.getInt("SCHEDULEID");
        }
        try
        {
            showForm();
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }

    }

}

