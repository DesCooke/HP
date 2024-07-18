package com.example.des.hp.TipGroup;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.R;
import com.example.des.hp.Tip.TipDetailsList;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

public class TipGroupDetailsList extends BaseActivity
{

    //region Member Variables
    public ArrayList<TipGroupItem> tipGroupList;
    public TipGroupAdapter tipGroupAdapter;
    //endregion

    //region Constructors/Destructors
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            layoutName="activity_tipgroup_list";
            setContentView(R.layout.activity_tipgroup_list);

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
            inflater.inflate(R.menu.tipgroup_list_add, menu);
        }
        catch(Exception e)
        {
            ShowError("onCreateOptionsMenu", e.getMessage());
        }

        return true;
    }
    //endregion

    //region Form Functions
    public void showTipGroupAdd(View view)
    {
        try
        {
            Intent intent=new Intent(getApplicationContext(), TipGroupDetailsEdit.class);
            intent.putExtra("ACTION", "add");
            intent.putExtra("HOLIDAYID", holidayId);
            startActivity(intent);
        }
        catch(Exception e)
        {
            ShowError("showTipGroupAdd", e.getMessage());
        }

    }

    public void showForm()
    {
        super.showForm();
        try
        {
            allowCellMove=true;

            tipGroupList=new ArrayList<>();
            try(DatabaseAccess da = databaseAccess();)
            {
                if(!da.getTipGroupList(holidayId, tipGroupList))
                    return;
            }
            tipGroupAdapter=new TipGroupAdapter(this, tipGroupList);

            CreateRecyclerView(R.id.tipGroupListView, tipGroupAdapter);

            tipGroupAdapter.setOnItemClickListener(new TipGroupAdapter.OnItemClickListener()
            {
                @Override
                public void onItemClick(View view, TipGroupItem obj)
                {
                    Intent intent=new Intent(getApplicationContext(), TipDetailsList.class);
                    intent.putExtra("ACTION", "view");
                    intent.putExtra("HOLIDAYID", obj.holidayId);
                    intent.putExtra("TIPGROUPID", obj.tipGroupId);
                    intent.putExtra("TITLE", title + "/" + subTitle);
                    intent.putExtra("SUBTITLE", obj.tipGroupDescription);
                    startActivity(intent);
                }
            });

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
            Collections.swap(tipGroupAdapter.data, from, to);
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
            tipGroupAdapter.onItemMove(from, to);
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
            tipGroupAdapter.notifyItemMoved(from, to);
        }
        catch(Exception e)
        {
            ShowError("NotifyItemMoved", e.getMessage());
        }

    }
    //endregion

    //region OnClick Events
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        try
        {
            switch(item.getItemId())
            {
                case R.id.action_add_tipgroup:
                    showTipGroupAdd(null);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
        catch(Exception e)
        {
            ShowError("onOptionsItemSelected", e.getMessage());
        }
        return true;
    }
    //endregion

}

