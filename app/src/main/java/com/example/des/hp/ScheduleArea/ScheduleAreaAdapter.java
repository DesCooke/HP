package com.example.des.hp.ScheduleArea;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.myutils.*;
import com.example.des.hp.R;

/**
 ** Created by Des on 06/10/2016.
 */

class ScheduleAreaAdapter extends RecyclerView.Adapter<ScheduleAreaAdapter.ViewHolder>
{
    private Context context;
    public ArrayList<ScheduleAreaItem> data = null;
    private OnItemClickListener mOnItemClickListener;
    private ImageUtils imageUtils;


    interface OnItemClickListener {
        void onItemClick(View view, ScheduleAreaItem obj, int position);
    }

    void setOnItemClickListener(final OnItemClickListener mItemClickListener)
    {
        this.mOnItemClickListener = mItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        TextView txtSchedName;
        TextView txtSchedDesc;
        LinearLayout fullCell;


        ViewHolder(View v)
        {
            super(v);

            txtSchedName = (TextView) v.findViewById(R.id.txtSchedName);
            txtSchedDesc = (TextView) v.findViewById(R.id.txtSchedDesc);
            fullCell = (LinearLayout) v.findViewById(R.id.fullCell);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    ScheduleAreaAdapter(Activity activity, ArrayList<ScheduleAreaItem> items) {
        this.context = activity;
        imageUtils = new ImageUtils(activity);
        data = items;
    }

    @Override
    public ScheduleAreaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedulearealistitemrow, parent, false);

        // set the view's size, margins, padding and layout parameters
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final ScheduleAreaItem c = data.get(position);
        holder.txtSchedName.setText(c.schedName);
        holder.txtSchedDesc.setText(c.schedDesc);

        holder.fullCell.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (mOnItemClickListener != null)
                {
                    mOnItemClickListener.onItemClick(view, c, position);
                }
            }
        });
    }


    public ScheduleAreaItem getItem(int position){
        return data.get(position);
    }

    public void add(int position, ScheduleAreaItem mail){
        data.add(position, mail);
        notifyDataSetChanged();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return data.size();
    }

}
