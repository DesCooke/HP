package com.example.des.hp.ScheduleArea;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import com.example.des.hp.R;

/**
 * * Created by Des on 06/10/2016.
 */

class ScheduleAreaAdapter extends RecyclerView.Adapter<ScheduleAreaAdapter.ViewHolder>
{
    public ArrayList<ScheduleAreaItem> data;
    private OnItemClickListener mOnItemClickListener;


    interface OnItemClickListener
    {
        void onItemClick(View view, ScheduleAreaItem obj);
    }

    void setOnItemClickListener(final OnItemClickListener mItemClickListener)
    {
        this.mOnItemClickListener=mItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        TextView txtSchedName;
        TextView txtSchedDesc;
        LinearLayout fullCell;


        ViewHolder(View v)
        {
            super(v);

            txtSchedName= v.findViewById(R.id.txtSchedName);
            txtSchedDesc= v.findViewById(R.id.txtSchedDesc);
            fullCell= v.findViewById(R.id.fullCell);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    ScheduleAreaAdapter(ArrayList<ScheduleAreaItem> items)
    {
        data=items;
    }

    @NonNull
    @Override
    public ScheduleAreaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // create a new view
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.schedulearealistitemrow, parent, false);

        // set the view's size, margins, padding and layout parameters
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        final ScheduleAreaItem c=data.get(position);
        holder.txtSchedName.setText(c.schedName);
        holder.txtSchedDesc.setText(c.schedDesc);

        holder.fullCell.setOnClickListener(view -> {
            if(mOnItemClickListener != null)
            {
                mOnItemClickListener.onItemClick(view, c);
            }
        });
    }


    public ScheduleAreaItem getItem(int position)
    {
        return data.get(position);
    }

    public void add(int position, ScheduleAreaItem mail)
    {
        data.add(position, mail);
        notifyItemInserted(position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return data.size();
    }

}
