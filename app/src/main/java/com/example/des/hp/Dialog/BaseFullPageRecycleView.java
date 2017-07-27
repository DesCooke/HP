package com.example.des.hp.Dialog;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.des.hp.R;

import java.util.Collections;

/**
 * Created by cooked on 26/07/2017.
 */

public abstract class BaseFullPageRecycleView  extends BaseActivity
{
    public int holidayId=0;
    public boolean allowCellMove=false;
    public boolean allowCellSwipe=false;

    public void CreateRecyclerView(int pView, RecyclerView.Adapter adapter)
    {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.dayListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this/*getActivity()*/));
        recyclerView.setHasFixedSize(true);
        //listView1.setDivider(null);
        recyclerView.setAdapter(adapter);


        itemTouchHelper.attachToRecyclerView(recyclerView);

    }



    public abstract void OnItemMove(int from, int to);
    public abstract void SwapItems(int from, int to);
    public abstract void NotifyItemMoved(int from, int to);

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
                                SwapItems(i, i+1); //Collections.swap(dayAdapter.data, i, i + 1);
                            }
                        } else
                        {
                            for (int i = fromPosition; i > toPosition; i--)
                            {
                                SwapItems(i, i-1); //Collections.swap(dayAdapter.data, i, i - 1);
                            }
                        }
                        NotifyItemMoved(fromPosition, toPosition);//dayAdapter.notifyItemMoved(fromPosition, toPosition);

                        return true;
                    }

                    @Override
                    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder)
                    {
                        int dragFlags=0;
                        int swipeFlags=0;

                        if(allowCellMove)
                            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;

                        if(allowCellSwipe)
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

                        if (dragFrom != -1 && dragTo != -1 && dragFrom != dragTo)
                        {
                            OnItemMove(dragFrom, dragTo);
                        }

                        dragFrom = dragTo = -1;
                    }

                }
            );

}
