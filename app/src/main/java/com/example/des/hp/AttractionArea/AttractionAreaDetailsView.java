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
import com.example.des.hp.Schedule.GeneralAttraction.GeneralAttractionDetailsEdit;
import com.example.des.hp.Schedule.GeneralAttraction.GeneralAttractionDetailsView;
import com.example.des.hp.Schedule.ScheduleAdapter;
import com.example.des.hp.Schedule.ScheduleItem;

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
            switch (item.getItemId())
            {
                case R.id.action_edit_attractionarea:
                    showAttractionAreaEdit();
                    return (true);
                case R.id.action_delete_attractionarea:
                    deleteAttractionArea();
                    return (true);
                case R.id.action_view_attractionarea:
                    showAttractionAreaView();
                    return (true);
                case R.id.action_add_generalattraction:
                    StartNewAddIntent(GeneralAttractionDetailsEdit.class);
                    return (true);
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
            Intent intent = new Intent(getApplicationContext(), neededClass);
            intent.putExtra("ACTION", "add");
            intent.putExtra("HOLIDAYID", holidayId);
            intent.putExtra("DAYID", 0);
            intent.putExtra("ATTRACTIONID", attractionId);
            intent.putExtra("ATTRACTIONAREAID", attractionAreaId);
            intent.putExtra("TITLE", title);
            intent.putExtra("SUBTITLE", subTitle);
            startActivity(intent);
        }
        catch (Exception e)
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
            
            scheduleAdapter.setOnItemClickListener(new ScheduleAdapter.OnItemClickListener()
            {
                @Override
                public void onItemClick(View view, ScheduleItem obj)
                {
                    if (obj.schedType == getResources().getInteger(R.integer.schedule_type_generalattraction))
                    {
                        StartNewEditIntent(GeneralAttractionDetailsView.class, obj);
                    }
                }
            });
            afterShow();
        }
        catch (Exception e)
        {
            ShowError("showForm", e.getMessage());
        }
    }
    
    public void StartNewEditIntent(Class neededClass, ScheduleItem obj)
    {
        try
        {
            Intent intent = new Intent(getApplicationContext(), neededClass);
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
        catch (Exception e)
        {
            ShowError("StartNewEditIntent", e.getMessage());
        }
    }
    //endregion
    
    //region form Functions
    @Override
    public int getInfoId()
    {
        try
        {
            return (attractionAreaItem.infoId);
        }
        catch (Exception e)
        {
            ShowError("getInfoId", e.getMessage());
        }
        return (0);
    }
    
    public void setNoteId(int pNoteId)
    {
        try
        {
            attractionAreaItem.noteId = pNoteId;
            databaseAccess().updateAttractionAreaItem(attractionAreaItem);
        }
        catch (Exception e)
        {
            ShowError("setNoteId", e.getMessage());
        }
    }
    
    @Override
    public int getNoteId()
    {
        try
        {
            return (attractionAreaItem.noteId);
        }
        catch (Exception e)
        {
            ShowError("getNoteId", e.getMessage());
        }
        return (0);
    }
    
    @Override
    public void setInfoId(int pInfoId)
    {
        try
        {
            attractionAreaItem.infoId = pInfoId;
            databaseAccess().updateAttractionAreaItem(attractionAreaItem);
        }
        catch (Exception e)
        {
            ShowError("setInfoId", e.getMessage());
        }
        
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
    public void SwapItems(int from, int to)
    {
        try
        {
            Collections.swap(scheduleAdapter.data, from, to);
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
            scheduleAdapter.onItemMove(from, to);
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
            scheduleAdapter.notifyItemMoved(from, to);
        }
        catch (Exception e)
        {
            ShowError("NotifyItemMove", e.getMessage());
        }
    }
    
    //endregion
    
}

