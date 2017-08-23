package com.example.des.hp.Tip;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.R;
import com.example.des.hp.TipGroup.TipGroupDetailsEdit;
import com.example.des.hp.TipGroup.TipGroupDetailsView;
import com.example.des.hp.TipGroup.TipGroupItem;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

public class TipDetailsList extends BaseActivity
{
    
    //region Member Variables
    public ArrayList<TipItem> tipList;
    public TipAdapter tipAdapter;
    public TipGroupItem tipGroupItem;
    //endregion
    
    //region Constructors/Destructors
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            hideImageIfEmpty=true;
            layoutName = "activity_tip_list";
            setContentView(R.layout.activity_tip_list);
            
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
            inflater.inflate(R.menu.tipgroupdetailsformmenu, menu);
        }
        catch (Exception e)
        {
            ShowError("onCreateOptionsMenu", e.getMessage());
        }
        
        return true;
    }
    //endregion
    
    //region Form Functions
    public void showTipAdd(View view)
    {
        try
        {
            Intent intent = new Intent(getApplicationContext(), TipDetailsEdit.class);
            intent.putExtra("ACTION", "add");
            intent.putExtra("HOLIDAYID", holidayId);
            intent.putExtra("TIPGROUPID", tipGroupId);
            startActivity(intent);
        }
        catch (Exception e)
        {
            ShowError("showTipAdd", e.getMessage());
        }
        
    }
    
    public void showForm()
    {
        super.showForm();
        try
        {
            allowCellMove = true;
            
            tipGroupItem = new TipGroupItem();
            if (!databaseAccess().getTipGroupItem(holidayId, tipGroupId, tipGroupItem))
                return;
            
            tipList = new ArrayList<>();
            if (!databaseAccess().getTipList(holidayId, tipGroupId, tipList))
                return;
            tipAdapter = new TipAdapter(this, tipList);
            
            CreateRecyclerView(R.id.tipListView, tipAdapter);
            
            tipAdapter.setOnItemClickListener
                (
                    new TipAdapter.OnItemClickListener()
                    {
                        @Override
                        public void onItemClick(View view, TipItem obj, int position)
                        {
                            Intent intent = new Intent(getApplicationContext(), TipDetailsView.class);
                            intent.putExtra("ACTION", "view");
                            intent.putExtra("HOLIDAYID", tipList.get(position).holidayId);
                            intent.putExtra("TIPGROUPID", tipList.get(position).tipGroupId);
                            intent.putExtra("TIPID", tipList.get(position).tipId);
                            intent.putExtra("TITLE", title + "/" +
                                subTitle);
                            intent.putExtra("SUBTITLE", tipList.get(position).tipDescription);
                            startActivity(intent);
                        }
                    }
                );
            
            SetImage(tipGroupItem.tipGroupPicture);
            
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
        Collections.swap(tipAdapter.data, from, to);
    }

    @Override
    public void OnItemMove(int from, int to)
    {
        tipAdapter.onItemMove(from, to);
    }

    @Override
    public void NotifyItemMoved(int from, int to)
    {
        tipAdapter.notifyItemMoved(from, to);
    }
    //endregion
    
    //region OnClick Events
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        try
        {
            switch (item.getItemId())
            {
                case R.id.action_delete_tipgroup:
                    deleteTipGroup();
                    return true;
                case R.id.action_view_tipgroup:
                    viewTipGroup();
                    return true;
                case R.id.action_edit_tipgroup:
                    editTipGroup();
                    return true;
                case R.id.action_add_tip:
                    showTipAdd(null);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
        catch (Exception e)
        {
            ShowError("onOptionsItemSelected", e.getMessage());
        }
        return true;
        
    }
    
    public void editTipGroup()
    {
        try
        {
            Intent intent = new Intent(getApplicationContext(), TipGroupDetailsEdit.class);
            intent.putExtra("ACTION", "modify");
            intent.putExtra("HOLIDAYID", holidayId);
            intent.putExtra("TIPGROUPID", tipGroupId);
            intent.putExtra("TITLE", title);
            intent.putExtra("SUBTITLE", subTitle);
            startActivity(intent);
        }
        catch (Exception e)
        {
            ShowError("editTipGroup", e.getMessage());
        }
        
    }
    
    public void viewTipGroup()
    {
        try
        {
            Intent intent = new Intent(getApplicationContext(), TipGroupDetailsView.class);
            intent.putExtra("ACTION", "view");
            intent.putExtra("HOLIDAYID", holidayId);
            intent.putExtra("TIPGROUPID", tipGroupId);
            intent.putExtra("TITLE", title);
            intent.putExtra("SUBTITLE", subTitle);
            startActivity(intent);
        }
        catch (Exception e)
        {
            ShowError("viewTipGroup", e.getMessage());
        }
        
    }
    
    public void deleteTipGroup()
    {
        try
        {
            
            if (!databaseAccess().deleteTipGroupItem(tipGroupItem))
                return;
            finish();
        }
        catch (Exception e)
        {
            ShowError("deleteTipGroup", e.getMessage());
        }
        
    }
    //endregion
    
}

