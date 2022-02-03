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

public class SwipeController extends ItemTouchHelper.Callback
{
    public BaseActivity baseActivity;

    private RecyclerView.ViewHolder currentItemViewHolder = null;

    private RectF buttonInstance;
    private float lastDx=0.00f;

    private boolean swipeBack = false;
    private ButtonsState buttonShowedState = ButtonsState.GONE;
    private static final float buttonWidth = 300;

    enum ButtonsState {
        GONE,
        LEFT_VISIBLE,
        RIGHT_VISIBLE
    }
    public SwipeController(BaseActivity argBaseActivity)
    {
        baseActivity = argBaseActivity;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target)
    {
        return true;
    }

    @Override
    public void onChildDraw(Canvas c,
                            RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder,
                            float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        lastDx = dX;
        setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        drawButtons(c, viewHolder);
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    private void drawButtons(Canvas c, RecyclerView.ViewHolder viewHolder)
    {
        float buttonWidthWithoutPadding = buttonWidth - 20;
        float corners = 16;

        View itemView = viewHolder.itemView;
        Paint p = new Paint();

        buttonInstance = null;
        if (buttonShowedState == ButtonsState.LEFT_VISIBLE)
        {
            RectF leftButton = null;
            if(baseActivity.allowEdit) {
                leftButton = new RectF(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + buttonWidthWithoutPadding, itemView.getBottom());
                p.setColor(Color.BLUE);
                c.drawRoundRect(leftButton, corners, corners, p);
                drawText("EDIT", c, leftButton, p);
            }

            buttonInstance = leftButton;
        }
        if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {
            RectF rightButton = null;
            if(baseActivity.allowDelete) {
                rightButton = new RectF(itemView.getRight() - buttonWidthWithoutPadding, itemView.getTop(), itemView.getRight(), itemView.getBottom());

                p.setColor(Color.RED);
                c.drawRoundRect(rightButton, corners, corners, p);
                drawText("DELETE", c, rightButton, p);
            }

            buttonInstance = rightButton;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setTouchListener(final Canvas c, @NonNull final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener()
        {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                swipeBack = event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP;
                buttonShowedState = ButtonsState.GONE;
                if (!swipeBack) {
                    if (dX < -buttonWidth)
                        buttonShowedState = ButtonsState.RIGHT_VISIBLE;
                    else if (dX > buttonWidth)
                        buttonShowedState = ButtonsState.LEFT_VISIBLE;
                } else {
                    baseActivity.NotifyDataSetChanged();
                }
                return false;
            }
        });
    }

    private void drawText(String text, @NonNull Canvas c, @NonNull RectF button, @NonNull Paint p) {
        float textSize = 60;
        p.setColor(Color.WHITE);
        p.setAntiAlias(true);
        p.setTextSize(textSize);

        float textWidth = p.measureText(text);
        c.drawText(text, button.centerX()-(textWidth/2), button.centerY()+(textSize/2), p);
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder)
    {
        int dragFlags=0;
        int swipeFlags=0;

        swipeFlags=ItemTouchHelper.START | ItemTouchHelper.END;

        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
        if (lastDx < -500.0f) {
            int pos = viewHolder.getAdapterPosition();

            if (baseActivity.allowDelete && direction == 16) {
                baseActivity.DeleteItemAtPos(pos);
            }
            if (baseActivity.allowEdit && direction == 32) {
                baseActivity.EditItemAtPos(pos);
            }
        }
    }

}
