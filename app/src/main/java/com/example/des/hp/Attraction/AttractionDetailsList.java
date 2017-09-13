/*
Shows a list of attraction items (seaworld, magic kingdom etc)
for the current holiday to pick from
 */
package com.example.des.hp.Attraction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.des.hp.AttractionArea.AttractionAreaDetailsList;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.R;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

public class AttractionDetailsList extends BaseActivity
{

    //region Member Variables
    public ArrayList<AttractionItem> attractionList;
    public AttractionAdapter attractionAdapter;
    //endregion

    //region Constructors/Destructors
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            layoutName="activity_attraction_details_list";
            setContentView(R.layout.activity_attraction_details_list);

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
            inflater.inflate(R.menu.attraction_list_add, menu);
        }
        catch(Exception e)
        {
            ShowError("onCreateOptionsMenu", e.getMessage());
        }

        return true;
    }
    //endregion

    //region Form Functions
    public void showAttractionAdd(View view)
    {
        try
        {
            Intent intent=new Intent(getApplicationContext(), AttractionDetailsEdit.class);
            intent.putExtra("ACTION", "add");
            intent.putExtra("HOLIDAYID", holidayId);
            startActivity(intent);
        }
        catch(Exception e)
        {
            ShowError("showAttractionAdd", e.getMessage());
        }
    }

    public void showForm()
    {
        super.showForm();
        try
        {
            allowCellMove=true;

            attractionList=new ArrayList<>();
            if(!databaseAccess().getAttractionList(holidayId, attractionList))
                return;
            attractionAdapter=new AttractionAdapter(this, attractionList);

            CreateRecyclerView(R.id.attractionListView, attractionAdapter);

            attractionAdapter.setOnItemClickListener(new AttractionAdapter.OnItemClickListener()
            {
                @Override
                public void onItemClick(View view, AttractionItem obj)
                {
                    Intent intent=new Intent(getApplicationContext(), AttractionAreaDetailsList.class);
                    intent.putExtra("ACTION", "view");
                    intent.putExtra("HOLIDAYID", obj.holidayId);
                    intent.putExtra("ATTRACTIONID", obj.attractionId);
                    intent.putExtra("TITLE", title + "/" + subTitle);
                    intent.putExtra("SUBTITLE", obj.attractionDescription);
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
            Collections.swap(attractionAdapter.data, from, to);
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
            attractionAdapter.onItemMove();
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
            attractionAdapter.notifyItemMoved(from, to);
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
                case R.id.action_add_attraction:
                    showAttractionAdd(null);
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

