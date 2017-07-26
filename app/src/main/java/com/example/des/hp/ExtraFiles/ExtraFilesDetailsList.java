package com.example.des.hp.ExtraFiles;

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
import com.example.des.hp.myutils.*;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

/**
 * * Created by Des on 02/11/2016.
 */

public class ExtraFilesDetailsList extends BaseActivity
{
    
    public ArrayList<ExtraFilesItem> extraFilesList;
    public int fileGroupId;
    public ExtraFilesAdapter extraFilesAdapter;
    public String title;
    public String subtitle;
    public ActionBar actionBar;
    
    public void showMapAdd(View view)
    {
        try
        {
            Intent intent = new Intent(getApplicationContext(), ExtraFilesDetailsEdit.class);
            intent.putExtra("ACTION", "add");
            intent.putExtra("FILEGROUPID", fileGroupId);
            startActivity(intent);
        }
        catch (Exception e)
        {
            ShowError("showMapAdd", e.getMessage());
        }
    }
    
    public void showForm()
    {
        try
        {
            actionBar = getSupportActionBar();
            if (actionBar != null)
            {
                if (title.length() > 0)
                {
                    actionBar.setTitle(title);
                    actionBar.setSubtitle(subtitle);
                } else
                {
                    actionBar.setTitle("File Group");
                    actionBar.setSubtitle("Extra Files");
                }
            }
            
            extraFilesList = new ArrayList<>();
            if (!databaseAccess().getExtraFilesList(fileGroupId, extraFilesList))
                return;
            
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.extraFilesListView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setHasFixedSize(true);
            //listView1.setDivider(null);
            extraFilesAdapter = new ExtraFilesAdapter(this, extraFilesList);
            recyclerView.setAdapter(extraFilesAdapter);
            
            itemTouchHelper.attachToRecyclerView(recyclerView);
            
            extraFilesAdapter.setOnItemClickListener
                (
                    new ExtraFilesAdapter.OnItemClickListener()
                    {
                        @Override
                        public void onItemClick(View view, ExtraFilesItem obj, int position)
                        {
                            Intent intent = new Intent(getApplicationContext(), ExtraFilesDetailsView.class);
                            intent.putExtra("ACTION", "view");
                            intent.putExtra("FILEGROUPID", extraFilesList.get(position).fileGroupId);
                            intent.putExtra("FILEID", extraFilesList.get(position).fileId);
                            if (actionBar != null)
                            {
                                intent.putExtra("TITLE", actionBar.getTitle() + "/" +
                                    actionBar.getSubtitle());
                                intent.putExtra("SUBTITLE", extraFilesList.get(position).fileDescription);
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
                        
                        
                        if (dragFrom == -1)
                        {
                            dragFrom = fromPosition;
                        }
                        dragTo = toPosition;
                        
                        if (fromPosition < toPosition)
                        {
                            for (int i = fromPosition; i < toPosition; i++)
                            {
                                Collections.swap(extraFilesAdapter.data, i, i + 1);
                            }
                        } else
                        {
                            for (int i = fromPosition; i > toPosition; i--)
                            {
                                Collections.swap(extraFilesAdapter.data, i, i - 1);
                            }
                        }
                        extraFilesAdapter.notifyItemMoved(fromPosition, toPosition);
                        
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
                            extraFilesAdapter.onItemMove(dragFrom, dragTo);
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
        try
        {
            setContentView(R.layout.activity_extra_files_list);
            
            title = "";
            subtitle = "";
            Bundle extras = getIntent().getExtras();
            if (extras != null)
            {
                fileGroupId = extras.getInt("FILEGROUPID");
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

