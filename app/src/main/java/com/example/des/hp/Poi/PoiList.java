package com.example.des.hp.Poi;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

import android.content.Intent;
import android.os.Bundle;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.Event.EventAdapter;
import com.example.des.hp.Event.EventDetailsEdit;
import com.example.des.hp.Event.EventDetailsView;
import com.example.des.hp.Event.EventScheduleItem;
import com.example.des.hp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;

public class PoiList extends BaseActivity {

    public ArrayList<EventScheduleItem> scheduleList;
    public EventAdapter eventAdapter;

    public FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            setContentView(R.layout.activity_poi_list);

            fab = findViewById(R.id.fab);
            fab.setOnClickListener(view -> StartNewAddIntent());

            afterCreate();

            showForm();
        }
        catch (Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }

    }

    public void StartNewAddIntent()
    {
        try
        {
            Intent intent = new Intent(getApplicationContext(), EventDetailsEdit.class);
            intent.putExtra("ACTION", "add");
            intent.putExtra("HOLIDAYID", holidayId);
            intent.putExtra("DAYID", 0);
            intent.putExtra("ATTRACTIONID", 0);
            intent.putExtra("ATTRACTIONAREAID", 0);
            intent.putExtra("TITLE", title);
            intent.putExtra("SUBTITLE", subTitle);
            startActivity(intent);
        }
        catch (Exception e)
        {
            ShowError("StartNewAddIntent", e.getMessage());
        }
    }

    public void showForm()
    {
        super.showForm();
        try(DatabaseAccess da = databaseAccess())
        {
            allowCellMove = true;

            scheduleList = new ArrayList<>();
            if (!da.getScheduleList(holidayId, 0, 0, 0, scheduleList))
                return;

            SetToolbarTitles(title, subTitle);

            eventAdapter = new EventAdapter(this, scheduleList);

            CreateRecyclerView(R.id.poiListView, eventAdapter);

            eventAdapter.setOnItemClickListener((view, obj) -> {
                if (obj.schedType == getResources().getInteger(R.integer.schedule_type_generalattraction))
                {
                    StartNewEditIntent(obj);
                }
            });

            afterShow();
        }
        catch (Exception e)
        {
            ShowError("showForm", e.getMessage());
        }
    }

    public void StartNewEditIntent(EventScheduleItem obj)
    {
        try
        {
            Intent intent = new Intent(getApplicationContext(), EventDetailsView.class);
            intent.putExtra("ACTION", "view");
            intent.putExtra("HOLIDAYID", obj.holidayId);
            intent.putExtra("DAYID", 0);
            intent.putExtra("ATTRACTIONID", 0);
            intent.putExtra("ATTRACTIONAREAID", 0);
            intent.putExtra("SCHEDULEID", obj.scheduleId);
            intent.putExtra("TITLE", title);
            intent.putExtra("SUBTITLE", subTitle);

            startActivity(intent);
        }
        catch (Exception e)
        {
            ShowError("StartNewEditIntent", e.getMessage());
        }
    }

    @Override
    public void SwapItems(int from, int to)
    {
        try
        {
            Collections.swap(eventAdapter.data, from, to);
        }
        catch (Exception e)
        {
            ShowError("SwapItems", e.getMessage());
        }

    }

    @Override
    public void OnItemMove(int from, int to)
    {
        try
        {
            eventAdapter.onItemMove();
        }
        catch (Exception e)
        {
            ShowError("OnItemMove", e.getMessage());
        }
    }

    @Override
    public void NotifyItemMoved(int from, int to)
    {
        try
        {
            eventAdapter.notifyItemMoved(from, to);
        }
        catch (Exception e)
        {
            ShowError("NotifyItemMove", e.getMessage());
        }
    }



}
