package com.example.des.hp.Dialog;

/*
** BASEACTIVITY->BASEFULLPAGERECYCLEVIEW
**
** In the onCreate, the first thing you do is setup the View: -
**
** In the onCreate, the first thing you do is setup the View: -
**   setContentView(R.layout.activity_day_list);
**
** Then you call the showForm
**   In here you create and fill your adapter
**      dayList=new ArrayList<>();
        if(!databaseAccess().getDayList(holidayId, dayList))
          return;
        dayAdapter=new DayAdapter(this, dayList);
**
**   Now you call the base CreateRecyclerView with the View and Adapter
**
**            CreateRecyclerView(R.id.dayListView, dayAdapter);
**
**   Now continue to setup your Adapter onClick events etc
**     dayAdapter.setOnItemClickListener(new DayAdapter.OnItemClickListener()
       {
         @Override
         public void onItemClick(View view, DayItem obj, int position)
         {
           Intent intent=new Intent(getApplicationContext(), DayDetailsView.class);
           intent.putExtra("ACTION", "view");
           intent.putExtra("HOLIDAYID", dayList.get(position).holidayId);
           intent.putExtra("DAYID", dayList.get(position).dayId);
           startActivity(intent);
         }
       }
       );
**
**  Ability to Move Cells Around
**
**  In showForm, set allowCellMove to true
**
**  Create three new functions
**    SwapItems(from, to)
**      allows you to swap items in the collection
**    OnItemMove(from, to)
**      allows you to inform the adapter
**    NotifyItemMoved(from, to)
**      Sends a message to the Recycler to redraw area
**
*/
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
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
import static com.example.des.hp.myutils.MyColor.myColor;
import static com.example.des.hp.myutils.MyMessages.myMessages;

public class BaseFullPageRecycleView extends BaseActivity
{

    public boolean allowCellMove = false;
    public boolean allowCellSwipe = false;
    public boolean gridLayout = false; // ie. vertical list
    public RecyclerView recyclerView;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private Bundle mBundleRecyclerViewState;
    
    public void CreateRecyclerView(int pView, RecyclerView.Adapter adapter)
    {
        try
        {
            recyclerView = (RecyclerView) findViewById(pView);
            if(gridLayout==false)
            {
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
            }
            else
            {
                recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
            }
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
    


    public void OnItemMove(int from, int to)
    {
    }
    
    public void SwapItems(int from, int to)
    {

    }
    
    public void NotifyItemMoved(int from, int to)
    {

    }

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
    }
}
