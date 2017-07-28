package com.example.des.hp.Dialog;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageButton;

import com.example.des.hp.ExtraFiles.ExtraFilesDetailsList;
import com.example.des.hp.Notes.NoteItem;
import com.example.des.hp.Notes.NoteView;
import com.example.des.hp.R;
import com.example.des.hp.myutils.MyColor;
import com.example.des.hp.myutils.MyInt;
import com.example.des.hp.thirdpartyutils.BadgeView;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

/**
 * Created by cooked on 26/07/2017.
 */

public abstract class BaseFullPageRecycleView extends BaseActivity
{
    // Inter Intent variables
    public int holidayId = 0;
    public int dayId = 0;
    public int attractionId = 0;
    public int attractionAreaId = 0;
    public String action;
    
    public boolean allowCellMove = false;
    public boolean allowCellSwipe = false;
    public RecyclerView recyclerView;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private Bundle mBundleRecyclerViewState;
    
    public boolean showInfoEnabled;
    public ImageButton btnShowInfo;
    public BadgeView btnShowInfoBadge;
    
    public boolean showNotesEnabled;
    public ImageButton btnShowNotes;
    public MyColor myColor;
    
    public void CreateRecyclerView(int pView, RecyclerView.Adapter adapter)
    {
        try
        {
            recyclerView = (RecyclerView) findViewById(pView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setHasFixedSize(true);
            //listView1.setDivider(null);
            recyclerView.setAdapter(adapter);
            
            itemTouchHelper.attachToRecyclerView(recyclerView);
        }
        catch (Exception e)
        {
            ShowError("CreateRecyclerView", e.getMessage());
        }
        
    }
    
    public void showNotes(View view)
    {
        try
        {
            Intent intent2 = new Intent(getApplicationContext(), NoteView.class);
            int lNoteId = getNoteId();
            if (lNoteId == 0)
            {
                MyInt myInt = new MyInt();
                if (!databaseAccess().getNextNoteId(holidayId, myInt))
                    return;
                lNoteId = myInt.Value;
                setNoteId(lNoteId);
            }
            intent2.putExtra("ACTION", "view");
            intent2.putExtra("HOLIDAYID", holidayId);
            intent2.putExtra("NOTEID", lNoteId);
            intent2.putExtra("TITLE", subTitle);
            intent2.putExtra("SUBTITLE", "Notes");
            startActivity(intent2);
        }
        catch (Exception e)
        {
            ShowError("showNotes", e.getMessage());
        }
    }
    
    
    public void afterCreate()
    {
        showInfoEnabled = false;
        btnShowInfo = (ImageButton) findViewById(R.id.btnShowInfo);
        if (btnShowInfo != null)
            showInfoEnabled = true;
        
        showNotesEnabled = false;
        btnShowNotes = (ImageButton) findViewById(R.id.btnShowNotes);
        if (btnShowNotes != null)
            showNotesEnabled = true;
        
        if (showInfoEnabled)
        {
            btnShowInfoBadge = new BadgeView(this, btnShowInfo);
            btnShowInfoBadge.setText(Integer.toString(0));
            btnShowInfoBadge.show();
        }
    }
    
    public void showInfo(View view)
    {
        try
        {
            int lInfoId;
            lInfoId = getInfoId();
            Intent intent2 = new Intent(getApplicationContext(), ExtraFilesDetailsList.class);
            if (lInfoId == 0)
            {
                MyInt myInt = new MyInt();
                if (!databaseAccess().getNextFileGroupId(myInt))
                    return;
                lInfoId = myInt.Value;
                setInfoId(lInfoId);
            }
            intent2.putExtra("FILEGROUPID", lInfoId);
            intent2.putExtra("TITLE", subTitle);
            intent2.putExtra("SUBTITLE", "Info");
            startActivity(intent2);
        }
        catch (Exception e)
        {
            ShowError("showInfo", e.getMessage());
        }
    }
    
    public void afterShow()
    {
        if(showInfoEnabled)
        {
            MyInt lFileCount = new MyInt();
            lFileCount.Value = 0;
            int lInfoId = getInfoId();
            if (lInfoId > 0)
            {
                if (!databaseAccess().getExtraFilesCount(lInfoId, lFileCount))
                    return;
            }
            btnShowInfoBadge.setText(Integer.toString(lFileCount.Value));
    
            if (lFileCount.Value == 0)
            {
                btnShowInfoBadge.setVisibility(View.INVISIBLE);
                if (myColor.SetImageButtonTint(btnShowInfo, R.color.colorDisabled) == false)
                    return;
            } else
            {
                btnShowInfoBadge.setVisibility(View.VISIBLE);
                if (myColor.SetImageButtonTint(btnShowInfo, R.color.colorEnabled) == false)
                    return;
            }
        }
        
        if(showNotesEnabled)
        {
            int lNoteId = getNoteId();
            NoteItem noteItem = new NoteItem();
            if (!databaseAccess().getNoteItem(holidayId, lNoteId, noteItem))
                return;
            if (noteItem.notes.length() == 0)
            {
                if (myColor.SetImageButtonTint(btnShowNotes, R.color.colorDisabled) == false)
                    return;
            } else
            {
                if (myColor.SetImageButtonTint(btnShowNotes, R.color.colorEnabled) == false)
                    return;
            }
        }
    }
    
    public abstract void OnItemMove(int from, int to);
    
    public abstract void SwapItems(int from, int to);
    
    public abstract void NotifyItemMoved(int from, int to);
    
    public abstract int getInfoId();
    
    public abstract int getNoteId();
    
    public abstract void setInfoId(int pInfoId);
    
    public abstract void setNoteId(int pNoteId);

    // handle swipe to delete, and draggable
    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT)
    {
        int dragFrom = -1;
        int dragTo = -1;
        
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target)
        {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            
            try
            {
                if (dragFrom == -1)
                {
                    dragFrom = fromPosition;
                }
                dragTo = toPosition;
                
                if (fromPosition < toPosition)
                {
                    for (int i = fromPosition; i < toPosition; i++)
                    {
                        SwapItems(i, i + 1);
                    }
                } else
                {
                    for (int i = fromPosition; i > toPosition; i--)
                    {
                        SwapItems(i, i - 1);
                    }
                }
                NotifyItemMoved(fromPosition, toPosition);
            }
            catch (Exception e)
            {
                ShowError("onMove", e.getMessage());
            }
            
            return true;
        }
        
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder)
        {
            int dragFlags = 0;
            int swipeFlags = 0;
            
            if (allowCellMove)
                dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            
            if (allowCellSwipe)
                swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            
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
            
            try
            {
                if (dragFrom != -1 && dragTo != -1 && dragFrom != dragTo)
                {
                    OnItemMove(dragFrom, dragTo);
                }
                
                dragFrom = dragTo = -1;
            }
            catch (Exception e)
            {
                ShowError("clearView", e.getMessage());
            }
            
        }
        
    });
    
    @Override
    protected void onPause()
    {
        super.onPause();
        
        try
        {
            // save RecyclerView state
            mBundleRecyclerViewState = new Bundle();
            Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
            mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
        }
        catch (Exception e)
        {
            ShowError("onPause", e.getMessage());
        }
        
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        
        try
        {
            // restore RecyclerView state
            if (mBundleRecyclerViewState != null)
            {
                Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
                recyclerView.getLayoutManager().onRestoreInstanceState(listState);
            }
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
            myColor = new MyColor(this);
            
            Bundle extras = getIntent().getExtras();
            if (extras != null)
            {
                holidayId = extras.getInt("HOLIDAYID", 0);
                dayId = extras.getInt("DAYID", 0);
                attractionAreaId = extras.getInt("ATTRACTIONAREAID", 0);
                attractionId = extras.getInt("ATTRACTIONID", 0);
                action = extras.getString("ACTION", "");
            }
        }
        catch (Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
        
    }
}
