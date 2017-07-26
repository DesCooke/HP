package com.example.des.hp.Tip;

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

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.R;
import com.example.des.hp.TipGroup.TipGroupDetailsEdit;
import com.example.des.hp.TipGroup.TipGroupDetailsView;
import com.example.des.hp.TipGroup.TipGroupItem;
import com.example.des.hp.myutils.*;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

/**
 ** Created by Des on 02/11/2016.
 */

public class TipDetailsList extends BaseActivity
{

    public ArrayList<TipItem> tipList;
    public int holidayId;
    public int tipGroupId;
    public TipAdapter tipAdapter;
    public String title;
    public String subtitle;
    public ActionBar actionBar;
    public TipGroupItem tipGroupItem;

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
        catch(Exception e)
        {
            ShowError("showTipAdd", e.getMessage());
        }

    }

    public void showForm()
    {
        try {
            if (actionBar != null) {
                if (title.length() > 0) {
                    actionBar.setTitle(title);
                    actionBar.setSubtitle(subtitle);
                } else {
                    actionBar.setTitle("TIPS");
                    actionBar.setSubtitle("");
                }
            }

            tipGroupItem = new TipGroupItem();
            if (!databaseAccess().getTipGroupItem(holidayId, tipGroupId, tipGroupItem))
                return;

            tipList = new ArrayList<>();
            if (!databaseAccess().getTipList(holidayId, tipGroupId, tipList))
                return;

            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.tipListView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setHasFixedSize(true);
            //listView1.setDivider(null);
            tipAdapter = new TipAdapter(this, tipList);
            recyclerView.setAdapter(tipAdapter);

            itemTouchHelper.attachToRecyclerView(recyclerView);

            tipAdapter.setOnItemClickListener
                    (
                            new TipAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, TipItem obj, int position) {
                                    Intent intent = new Intent(getApplicationContext(), TipDetailsView.class);
                                    intent.putExtra("ACTION", "view");
                                    intent.putExtra("HOLIDAYID", tipList.get(position).holidayId);
                                    intent.putExtra("TIPGROUPID", tipList.get(position).tipGroupId);
                                    intent.putExtra("TIPID", tipList.get(position).tipId);
                                    if (actionBar != null) {
                                        intent.putExtra("TITLE", actionBar.getTitle() + "/" +
                                                actionBar.getSubtitle());
                                        intent.putExtra("SUBTITLE", tipList.get(position).tipDescription);
                                    }
                                    startActivity(intent);
                                }
                            }
                    );
        }
        catch(Exception e)
        {
            ShowError("showForm", e.getMessage());
        }
    }

    // handle swipe to delete, and dragable
    ItemTouchHelper itemTouchHelper =
            new ItemTouchHelper
                    (
                            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT)
                            {
                                int dragFrom = -1;
                                int dragTo = -1;

                                @Override
                                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target)
                                {
                                    int fromPosition = viewHolder.getAdapterPosition();
                                    int toPosition = target.getAdapterPosition();


                                    if(dragFrom == -1)
                                    {
                                        dragFrom =  fromPosition;
                                    }
                                    dragTo = toPosition;

                                    if (fromPosition < toPosition)
                                    {
                                        for (int i = fromPosition; i < toPosition; i++)
                                        {
                                            Collections.swap(tipAdapter.data, i, i + 1);
                                        }
                                    } else
                                    {
                                        for (int i = fromPosition; i > toPosition; i--)
                                        {
                                            Collections.swap(tipAdapter.data, i, i - 1);
                                        }
                                    }
                                    tipAdapter.notifyItemMoved(fromPosition, toPosition);

                                    return true;
                                }

                                @Override
                                public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder)
                                {
                                    int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                                    int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                                    return makeMovementFlags(dragFlags, swipeFlags);
                                }

                                @Override
                                public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction)
                                {
                                }

                                @Override
                                public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder)
                                {
                                    super.clearView(recyclerView, viewHolder);

                                    if(dragFrom != -1 && dragTo != -1 && dragFrom != dragTo)
                                    {
                                        tipAdapter.onItemMove(dragFrom, dragTo);
                                    }

                                    dragFrom = dragTo = -1;
                                }

                            }
                    );

    @Override
    protected void onResume(){
        super.onResume();

        try
        {
            showForm();
        }
        catch(Exception e)
        {
            ShowError("onResume", e.getMessage());
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
        setContentView(R.layout.activity_tip_list);

        actionBar = getSupportActionBar();

        title = "";
        subtitle = "";
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            holidayId = extras.getInt("HOLIDAYID");
            tipGroupId = extras.getInt("TIPGROUPID");
            title = extras.getString("TITLE");
            subtitle = extras.getString("SUBTITLE");
        }
            showForm();
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }

    }

    public void editTipGroup()
    {
        try
        {
        Intent intent = new Intent(getApplicationContext(), TipGroupDetailsEdit.class);
        intent.putExtra("ACTION", "modify");
        intent.putExtra("HOLIDAYID", holidayId);
        intent.putExtra("TIPGROUPID", tipGroupId);
        intent.putExtra("TITLE", actionBar.getTitle());
        intent.putExtra("SUBTITLE", actionBar.getSubtitle());
        startActivity(intent);
        }
        catch(Exception e)
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
        intent.putExtra("TITLE", actionBar.getTitle());
        intent.putExtra("SUBTITLE", actionBar.getSubtitle());
        startActivity(intent);
        }
        catch(Exception e)
        {
            ShowError("viewTipGroup", e.getMessage());
        }

    }

    public void deleteTipGroup()
    {
        try
        {

        if(!databaseAccess().deleteTipGroupItem(tipGroupItem))
            return;
        finish();
        }
        catch(Exception e)
        {
            ShowError("deleteTipGroup", e.getMessage());
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        try
        {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tipgroupdetailsformmenu, menu);
        }
        catch(Exception e)
        {
            ShowError("onCreateOptionsMenu", e.getMessage());
        }

        return true;
    }

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

}

