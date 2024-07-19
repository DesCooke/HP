package com.example.des.hp.Tasks;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.myutils.*;
import com.example.des.hp.R;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder>
{
    private final Context context;
    public ArrayList<TaskItem> data;
    private OnItemClickListener mOnItemClickListener;
    private final ImageUtils imageUtils;


    interface OnItemClickListener
    {
        void onItemClick(View view, TaskItem obj);
    }

    void setOnItemClickListener(final OnItemClickListener mItemClickListener)
    {
        this.mOnItemClickListener=mItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        ImageView taskImage;
        TextView txtTaskDescription;
        LinearLayout taskItemCell;
        TextView txtTaskDate;
        CheckBox chkTaskComplete;

        ViewHolder(View v)
        {
            super(v);

            taskImage= v.findViewById(R.id.imgIcon);
            txtTaskDescription= v.findViewById(R.id.taskDescription);
            txtTaskDate= v.findViewById(R.id.taskDate);
            taskItemCell= v.findViewById(R.id.taskItemCell);
            chkTaskComplete= v.findViewById(R.id.chkTaskComplete);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    TaskAdapter(Activity activity, ArrayList<TaskItem> items)
    {
        this.context=activity;
        imageUtils=new ImageUtils(activity);
        data=items;
    }

    @NonNull
    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // create a new view
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.tasklistitemrow, parent, false);

        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        final TaskItem c=data.get(position);
        holder.txtTaskDescription.setText(c.taskDescription);
        holder.txtTaskDate.setText(c.taskDateString);
        if(c.taskDateKnown)
        {
            holder.txtTaskDate.setVisibility(View.VISIBLE);
        } else
        {
            holder.txtTaskDate.setVisibility(View.INVISIBLE);
        }

        holder.chkTaskComplete.setChecked(c.taskComplete);

        if(c.pictureAssigned)
        {
            holder.taskImage.setVisibility(View.VISIBLE);
            if(!imageUtils.getListIcon(c.holidayId, context, c.taskPicture, holder.taskImage))
                return;
        } else
        {
            holder.taskImage.setVisibility(View.INVISIBLE);
        }


        holder.taskItemCell.setOnClickListener(view -> {
            if(mOnItemClickListener != null)
            {
                mOnItemClickListener.onItemClick(view, c);
            }
        });
    }


    public TaskItem getItem(int position)
    {
        return data.get(position);
    }

    public void add(int position, TaskItem mail)
    {
        data.add(position, mail);
        notifyItemInserted(position);
    }

    void onItemMove()
    {
        updateGlobalData(data);
    }

    private void updateGlobalData(ArrayList<TaskItem> items)
    {
        for(int i=0; i < items.size(); i++)
        {
            items.get(i).sequenceNo=i + 1;
        }
        try(DatabaseAccess da = databaseAccess())
        {
            if(!da.updateTaskItems(items))
                return;
        }
        notifyItemRangeChanged(0, items.size()-1);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return data.size();
    }

}
