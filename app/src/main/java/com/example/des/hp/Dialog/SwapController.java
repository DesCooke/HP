package com.example.des.hp.Dialog;

import static androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SwapController extends ItemTouchHelper.SimpleCallback
{
    public BaseActivity baseActivity;
    int dragFrom=-1;
    int dragTo=-1;

    public SwapController(BaseActivity argBaseActivity, int dragDirs, int swipeDirs)
    {
        super(dragDirs, swipeDirs);
        baseActivity = argBaseActivity;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target)
    {
        int fromPosition=viewHolder.getAdapterPosition();
        int toPosition=target.getAdapterPosition();

        try
        {
            if(dragFrom == -1)
            {
                dragFrom=fromPosition;
            }
            dragTo=toPosition;

            if(fromPosition < toPosition)
            {
                for(int i=fromPosition; i < toPosition; i++)
                {
                    baseActivity.SwapItems(i, i + 1);
                }
            } else
            {
                for(int i=fromPosition; i > toPosition; i--)
                {
                    baseActivity.SwapItems(i, i - 1);
                }
            }
            baseActivity.NotifyItemMoved(fromPosition, toPosition);
        }
        catch(Exception e)
        {
            baseActivity.ShowError("onMove", e.getMessage());
        }

        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder)
    {
        int dragFlags=0;
        int swipeFlags=0;

        dragFlags=ItemTouchHelper.UP | ItemTouchHelper.DOWN;

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
            if(dragFrom != -1 && dragTo != -1 && dragFrom != dragTo)
            {
                baseActivity.OnItemMove(dragFrom, dragTo);
            }

            dragFrom=dragTo=-1;
        }
        catch(Exception e)
        {
            baseActivity.ShowError("clearView", e.getMessage());
        }

    }

}
