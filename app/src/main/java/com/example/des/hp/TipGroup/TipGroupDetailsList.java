package com.example.des.hp.TipGroup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.R;
import com.example.des.hp.Tip.TipDetailsList;
import com.example.des.hp.myutils.*;

import java.util.ArrayList;
import java.util.Collections;

/**
 * * Created by Des on 02/11/2016.
 */

public class TipGroupDetailsList extends BaseActivity
{

    public DatabaseAccess databaseAccess;
    public ArrayList<TipGroupItem> tipGroupList;
    public int holidayId;
    public TipGroupAdapter tipGroupAdapter;
    public String title;
    public String subtitle;
    public ActionBar actionBar;
    public MyMessages myMessages;

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
        try
        {
            if(actionBar != null)
            {
                if(title.length() > 0)
                {
                    actionBar.setTitle(title);
                    actionBar.setSubtitle(subtitle);
                } else
                {
                    actionBar.setTitle("TIPS");
                    actionBar.setSubtitle("");
                }
            }

            tipGroupList=new ArrayList<>();
            if(!databaseAccess.getTipGroupList(holidayId, tipGroupList))
                return;

            RecyclerView recyclerView=(RecyclerView) findViewById(R.id.tipGroupListView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setHasFixedSize(true);
            //listView1.setDivider(null);
            tipGroupAdapter=new TipGroupAdapter(this, tipGroupList);
            recyclerView.setAdapter(tipGroupAdapter);

            itemTouchHelper.attachToRecyclerView(recyclerView);

            tipGroupAdapter.setOnItemClickListener(new TipGroupAdapter.OnItemClickListener()
            {
                @Override
                public void onItemClick(View view, TipGroupItem obj, int position)
                {
                    Intent intent=new Intent(getApplicationContext(), TipDetailsList.class);
                    intent.putExtra("ACTION", "view");
                    intent.putExtra("HOLIDAYID", tipGroupList.get(position).holidayId);
                    intent.putExtra("TIPGROUPID", tipGroupList.get(position).tipGroupId);
                    if(actionBar != null)
                    {
                        intent.putExtra("TITLE", actionBar.getTitle() + "/" + actionBar.getSubtitle());
                        intent.putExtra("SUBTITLE", tipGroupList.get(position).tipGroupDescription);
                    }
                    startActivity(intent);
                }
            });
        }
        catch(Exception e)
        {
            ShowError("showForm", e.getMessage());
        }
    }

    // handle swipe to delete, and dragable
    ItemTouchHelper itemTouchHelper=new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT)
    {
        int dragFrom=-1;
        int dragTo=-1;

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target)
        {
            int fromPosition=viewHolder.getAdapterPosition();
            int toPosition=target.getAdapterPosition();


            if(dragFrom == -1)
            {
                dragFrom=fromPosition;
            }
            dragTo=toPosition;

            if(fromPosition < toPosition)
            {
                for(int i=fromPosition; i < toPosition; i++)
                {
                    Collections.swap(tipGroupAdapter.data, i, i + 1);
                }
            } else
            {
                for(int i=fromPosition; i > toPosition; i--)
                {
                    Collections.swap(tipGroupAdapter.data, i, i - 1);
                }
            }
            tipGroupAdapter.notifyItemMoved(fromPosition, toPosition);

            return true;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder)
        {
            int dragFlags=ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlags=ItemTouchHelper.START | ItemTouchHelper.END;
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
                tipGroupAdapter.onItemMove(dragFrom, dragTo);
            }

            dragFrom=dragTo=-1;
        }

    });

    @Override
    protected void onResume()
    {
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
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            setContentView(R.layout.activity_tipgroup_list);

            databaseAccess=new DatabaseAccess(this);
            actionBar=getSupportActionBar();
            myMessages=new MyMessages(this);

            title="";
            subtitle="";
            Bundle extras=getIntent().getExtras();
            if(extras != null)
            {
                holidayId=extras.getInt("HOLIDAYID");
                title=extras.getString("TITLE");
                subtitle=extras.getString("SUBTITLE");
            }
            showForm();
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }

    }

}

