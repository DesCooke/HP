package com.example.des.hp.Tip;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.R;
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
            layoutName="activity_tip_list";
            setContentView(R.layout.activity_tip_list);

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
            inflater.inflate(R.menu.tipgroupdetailsformmenu, menu);
        }
        catch(Exception e)
        {
            ShowError("onCreateOptionsMenu", e.getMessage());
        }

        return true;
    }
    //endregion

    public void showForm()
    {
        super.showForm();
        try
        {
            allowCellMove=true;

            tipGroupItem=new TipGroupItem();
            tipList=new ArrayList<>();
            try(DatabaseAccess da = databaseAccess())
            {
                if(!da.getTipGroupItem(holidayId, tipGroupId, tipGroupItem))
                    return;
                if(!da.getTipList(holidayId, tipGroupId, tipList))
                    return;
            }

            tipAdapter=new TipAdapter(this, tipList);

            CreateRecyclerView(R.id.tipListView, tipAdapter);

            tipAdapter.setOnItemClickListener((view, obj) -> {
                Intent intent=new Intent(getApplicationContext(), TipDetailsView.class);
                intent.putExtra("ACTION", "view");
                intent.putExtra("HOLIDAYID", obj.holidayId);
                intent.putExtra("TIPGROUPID", obj.tipGroupId);
                intent.putExtra("TIPID", obj.tipId);
                intent.putExtra("TITLE", title + "/" + subTitle);
                intent.putExtra("SUBTITLE", obj.tipDescription);
                startActivity(intent);
            });

            SetImage(tipGroupItem.tipGroupPicture);

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
            Collections.swap(tipAdapter.data, from, to);
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
            tipAdapter.onItemMove();
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
            tipAdapter.notifyItemMoved(from, to);
        }
        catch(Exception e)
        {
            ShowError("NotifyItemMoved", e.getMessage());
        }

    }
    //endregion

}

