/*
Shows a list of attraction items (seaworld, magic kingdom etc)
for the current holiday to pick from
 */
package com.example.des.hp.ThemeParks;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

//import com.example.des.hp.AttractionArea.AttractionAreaDetailsList;
import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

import androidx.annotation.NonNull;

public class ThemeParkList extends BaseActivity
{
    
    //region Member Variables
    public ArrayList<ThemeParkItem> attractionList;
    private ThemeParkAdapter themeParkAdapter;
    //endregion
    
    //region Constructors/Destructors
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        try
        {
            layoutName = "activity_attraction_details_list";
            setContentView(R.layout.activity_themepark_details_list);

            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(this::showThemeParkAdd);
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
            inflater.inflate(R.menu.attraction_list_add, menu);
        }
        catch (Exception e)
        {
            ShowError("onCreateOptionsMenu", e.getMessage());
        }
        
        return true;
    }
    //endregion
    
    //region Form Functions
    public void showThemeParkAdd(View view)
    {
        try
        {
            Intent intent = new Intent(getApplicationContext(), ThemeParkEdit.class);
            intent.putExtra("ACTION", "add");
            intent.putExtra("HOLIDAYID", holidayId);
            startActivity(intent);
        }
        catch (Exception e)
        {
            ShowError("showAttractionAdd", e.getMessage());
        }
    }
    
    public void showForm()
    {
        super.showForm();
        try(DatabaseAccess da = databaseAccess())
        {
            allowCellMove = true;
            
            attractionList = new ArrayList<>();
            if (!da.getAttractionList(holidayId, attractionList))
                return;
            themeParkAdapter = new ThemeParkAdapter(this, attractionList);
            
            CreateRecyclerView(R.id.attractionListView, themeParkAdapter);
            
            themeParkAdapter.setOnItemClickListener((view, obj) -> {
                Intent intent = new Intent(getApplicationContext(), ThemeParkView.class);
                intent.putExtra("ACTION", "view");
                intent.putExtra("HOLIDAYID", obj.holidayId);
                intent.putExtra("ATTRACTIONID", obj.attractionId);
                intent.putExtra("TITLE", obj.attractionDescription);
                intent.putExtra("SUBTITLE", subTitle);
                startActivity(intent);
            });
            
            afterShow();
        }
        catch (Exception e)
        {
            ShowError("showForm", e.getMessage());
        }
    }
    
    @Override
    public void SwapItems(int from, int to)
    {
        try
        {
            Collections.swap(themeParkAdapter.data, from, to);
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
            themeParkAdapter.onItemMove();
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
            themeParkAdapter.notifyItemMoved(from, to);
        }
        catch (Exception e)
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
            if (item.getItemId() == R.id.action_add_attraction) {
                showThemeParkAdd(null);
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
        catch (Exception e)
        {
            ShowError("onOptionsItemSelected", e.getMessage());
        }
        return true;
    }
    //endregion
    
}

