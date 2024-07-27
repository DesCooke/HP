/*
Shows a list of  attractionarea items (futureword etc) for the current attraction (magic kingdom)
 */
package com.example.des.hp.ThemeParks;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.R;
import com.example.des.hp.Event.EventDetailsEdit;
import com.example.des.hp.Event.EventDetailsView;
import com.example.des.hp.Event.EventAdapter;
import com.example.des.hp.Event.EventScheduleItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

public class ThemeParkAreaView extends BaseActivity
{
    
    //region Member variables
    public ArrayList<EventScheduleItem> scheduleList;
    public EventAdapter eventAdapter;
    public ThemeParkAreaItem themeParkAreaItem;
    public TextView txtThemeParkAreaName;
    public ImageButton btnClear;
    public Button btnSave;
    public ImageView btnEdit;
    public ImageView btnDelete;
    public FloatingActionButton fab;
    //endregion
    
    //region Constructors/Destructors
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        try
        {
            setContentView(R.layout.activity_themepark_area_details_view);

            btnClear = findViewById(R.id.btnClear);
            btnSave = findViewById(R.id.btnSave);
            btnDelete=findViewById(R.id.my_toolbar_delete);
            btnDelete.setOnClickListener(view -> deleteThemeParkArea());
            btnEdit=findViewById(R.id.my_toolbar_edit);
            btnEdit.setOnClickListener(view -> editThemeParkArea());
            fab = findViewById(R.id.fab);
            fab.setOnClickListener(view -> StartNewAddIntent());

            txtThemeParkAreaName = findViewById(R.id.txtThemeParkAreaName);

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
    public void StartNewAddIntent()
    {
        try
        {
            Intent intent = new Intent(getApplicationContext(), EventDetailsEdit.class);
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
        try(DatabaseAccess da = databaseAccess())
        {
            allowCellMove = true;

            ThemeParkItem themeParkItem = new ThemeParkItem();
            if (!da.getAttractionItem(holidayId, attractionId, themeParkItem))
                return;

            themeParkAreaItem = new ThemeParkAreaItem();
            if (!da.getAttractionAreaItem(holidayId, attractionId, attractionAreaId, themeParkAreaItem))
                return;
            
            scheduleList = new ArrayList<>();
            if (!da.getScheduleList(holidayId, 0, attractionId, attractionAreaId, scheduleList))
                return;
            
            SetImage(themeParkAreaItem.attractionAreaPicture);

            txtThemeParkAreaName.setText(
                    String.format("%s",
                            themeParkAreaItem.attractionAreaDescription));
            
            if (!title.isEmpty())
            {
                SetToolbarTitles(title, subTitle);
            } else
            {
                SetToolbarTitles("ATTRACTIONS", "");
            }
            
            eventAdapter = new EventAdapter(this, scheduleList);
            
            CreateRecyclerView(R.id.themeParkAreaListView, eventAdapter);

            eventAdapter.setOnItemClickListener((view, obj) -> {
                if (obj.schedType == getResources().getInteger(R.integer.schedule_type_generalattraction))
                {
                    StartNewEditIntent(obj);
                }
            });

            if(action.compareTo("view")==0){
                ShowToolbarEdit();
            }
            if(action.compareTo("modify")==0){
                ShowToolbarDelete();
            }

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
            return (themeParkAreaItem.infoId);
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
            themeParkAreaItem.noteId = pNoteId;
            try(DatabaseAccess da = databaseAccess())
            {
                da.updateAttractionAreaItem(themeParkAreaItem);
            }
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
            return (themeParkAreaItem.noteId);
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
            themeParkAreaItem.infoId = pInfoId;
            try(DatabaseAccess da = databaseAccess())
            {
                da.updateAttractionAreaItem(themeParkAreaItem);
            }
        }
        catch (Exception e)
        {
            ShowError("setInfoId", e.getMessage());
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
    
    //endregion

    public void editThemeParkArea(){
        Intent intent = new Intent(getApplicationContext(), ThemeParkAreaEdit.class);
        intent.putExtra("ACTION", "modify");
        intent.putExtra("HOLIDAYID", themeParkAreaItem.holidayId);
        intent.putExtra("ATTRACTIONID", themeParkAreaItem.attractionId);
        intent.putExtra("ATTRACTIONAREAID", themeParkAreaItem.attractionAreaId);
        intent.putExtra("TITLE", title);
        intent.putExtra("SUBTITLE", subTitle);
        startActivity(intent);
    }

    public void deleteThemeParkArea()
    {
        try
        {
            try(DatabaseAccess da = databaseAccess())
            {
                if(!da.deleteAttractionAreaItem(themeParkAreaItem))
                    return;
            }
            finish();
        }
        catch(Exception e)
        {
            ShowError("deleteThemeParkArea", e.getMessage());
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        try
        {
            if(action.compareTo("add")!=0) {
                try (DatabaseAccess da = databaseAccess()) {
                    if (!da.getAttractionAreaItem(holidayId, attractionId, attractionAreaId, themeParkAreaItem)) {
                        finish();
                    }
                    if (themeParkAreaItem != null) {
                        if (themeParkAreaItem.holidayId == 0) {
                            finish();
                        }
                    }
                }
            }
        }
        catch(Exception e)
        {
            ShowError("onResume", e.getMessage());
        }

    }

}

