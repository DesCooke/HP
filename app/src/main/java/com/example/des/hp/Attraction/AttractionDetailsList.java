/*
Shows a list of attraction items (seaword, magic kingdom etc)
for the current holiday to pick from
 */
package com.example.des.hp.Attraction;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.example.des.hp.AttractionArea.AttractionAreaDetailsList;
import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.ExtraFiles.ExtraFilesDetailsList;
import com.example.des.hp.Notes.NoteView;
import com.example.des.hp.R;
import com.example.des.hp.myutils.*;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

/**
 * * Created by Des on 02/11/2016.
 */

public class AttractionDetailsList extends BaseActivity
{
    
    public ArrayList<AttractionItem> attractionList;
    public int holidayId;
    public AttractionAdapter attractionAdapter;
    public String title;
    public String subtitle;
    public ActionBar actionBar;
    
    public void showAttractionAdd(View view)
    {
        try
        {
            Intent intent = new Intent(getApplicationContext(), AttractionDetailsEdit.class);
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
        try
        {
            if (actionBar != null)
            {
                if (title.length() > 0)
                {
                    actionBar.setTitle(title);
                    actionBar.setSubtitle(subtitle);
                } else
                {
                    actionBar.setTitle("TIPS");
                    actionBar.setSubtitle("");
                }
            }
            
            attractionList = new ArrayList<>();
            if (!databaseAccess().getAttractionList(holidayId, attractionList))
                return;
            
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.attractionListView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setHasFixedSize(true);
            //listView1.setDivider(null);
            attractionAdapter = new AttractionAdapter(this, attractionList);
            recyclerView.setAdapter(attractionAdapter);
            
            itemTouchHelper.attachToRecyclerView(recyclerView);
            
            attractionAdapter.setOnItemClickListener
                (
                    new AttractionAdapter.OnItemClickListener()
                    {
                        @Override
                        public void onItemClick(View view, AttractionItem obj, int position)
                        {
                            Intent intent = new Intent(getApplicationContext(), AttractionAreaDetailsList.class);
                            intent.putExtra("ACTION", "view");
                            intent.putExtra("HOLIDAYID", attractionList.get(position).holidayId);
                            intent.putExtra("ATTRACTIONID", attractionList.get(position).attractionId);
                            if (actionBar != null)
                            {
                                intent.putExtra("TITLE", actionBar.getTitle() + "/" +
                                    actionBar.getSubtitle());
                                intent.putExtra("SUBTITLE", attractionList.get(position).attractionDescription);
                            }
                            startActivity(intent);
                        }
                    }
                );
        }
        catch (Exception e)
        {
            ShowError("showForm", e.getMessage());
        }
        
    }
    
    // handle swipe to delete, and draggable
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
                        
                        
                        if (dragFrom == -1)
                        {
                            dragFrom = fromPosition;
                        }
                        dragTo = toPosition;
                        
                        if (fromPosition < toPosition)
                        {
                            for (int i = fromPosition; i < toPosition; i++)
                            {
                                Collections.swap(attractionAdapter.data, i, i + 1);
                            }
                        } else
                        {
                            for (int i = fromPosition; i > toPosition; i--)
                            {
                                Collections.swap(attractionAdapter.data, i, i - 1);
                            }
                        }
                        attractionAdapter.notifyItemMoved(fromPosition, toPosition);
                        
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
                        
                        if (dragFrom != -1 && dragTo != -1 && dragFrom != dragTo)
                        {
                            attractionAdapter.onItemMove();
                        }
                        
                        dragFrom = dragTo = -1;
                    }
                    
                }
            );
    
    @Override
    protected void onResume()
    {
        super.onResume();
        
        try
        {
            showForm();
        }
        catch (Exception e)
        {
            ShowError("onResume", e.getMessage());
        }
        
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attraction_details_list);
        
        try
        {
            actionBar = getSupportActionBar();
            
            title = "";
            subtitle = "";
            Bundle extras = getIntent().getExtras();
            if (extras != null)
            {
                holidayId = extras.getInt("HOLIDAYID");
                title = extras.getString("TITLE");
                subtitle = extras.getString("SUBTITLE");
            }
            
            showForm();
        }
        catch (Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
        
    }
    
}

