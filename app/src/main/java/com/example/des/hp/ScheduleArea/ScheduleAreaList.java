package com.example.des.hp.ScheduleArea;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.R;

import java.util.ArrayList;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

public class ScheduleAreaList extends BaseActivity
{

    public ArrayList<ScheduleAreaItem> scheduleAreaList;
    public int holidayId;
    public int dayId;
    public int attractionId;
    public int attractionAreaId;
    public int scheduleId;
    public String title;
    public String subtitle;
    public ActionBar actionBar;

    public void showForm()
    {
        try
        {
            if(actionBar != null)
            {
                actionBar.setTitle("MOVE TO ANOTHER LOCATION");
                actionBar.setSubtitle("");
            }

            scheduleAreaList=new ArrayList<>();
            try(DatabaseAccess da = databaseAccess())
            {
                if(!da.getScheduleAreaList(holidayId, scheduleAreaList))
                    return;
            }

            RecyclerView recyclerView= findViewById(R.id.scheduleAreaListView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setHasFixedSize(true);
            //listView1.setDivider(null);
            ScheduleAreaAdapter scheduleAreaAdapter = new ScheduleAreaAdapter(scheduleAreaList);
            recyclerView.setAdapter(scheduleAreaAdapter);

            scheduleAreaAdapter.setOnItemClickListener((view, obj) -> {
                Intent intent=new Intent();
                intent.putExtra("DAYID", obj.dayId);
                intent.putExtra("ATTRACTIONID", obj.attractionId);
                intent.putExtra("ATTRACTIONAREAID", obj.attractionAreaId);
                setResult(RESULT_OK, intent);
                finish();
            });
        }
        catch(Exception e)
        {
            ShowError("showForm", e.getMessage());
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            setContentView(R.layout.activity_schedulearea_list);

            actionBar=getSupportActionBar();

            title="";
            subtitle="";
            Bundle extras=getIntent().getExtras();
            if(extras != null)
            {
                holidayId=extras.getInt("HOLIDAYID");
                dayId=extras.getInt("DAYID");
                attractionId=extras.getInt("ATTRACTIONID");
                attractionAreaId=extras.getInt("ATTRACTIONAREAID");
                scheduleId=extras.getInt("SCHEDULEID");
            }
            showForm();
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }

    }

}

