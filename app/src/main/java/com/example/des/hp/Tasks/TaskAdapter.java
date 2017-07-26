package com.example.des.hp.Tasks;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
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

/**
 ** Created by Des on 06/10/2016.
 */

class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder>
{
    private Context context;
    private int layoutResourceId;
    public ArrayList<TaskItem> data = null;
    private DateUtils dateUtils;
    private OnItemClickListener mOnItemClickListener;
    private ImageUtils imageUtils;


    interface OnItemClickListener {
        void onItemClick(View view, TaskItem obj, int position);
    }

    void setOnItemClickListener(final OnItemClickListener mItemClickListener)
    {
        this.mOnItemClickListener = mItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder
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

            taskImage = (ImageView) v.findViewById(R.id.imgIcon);
            txtTaskDescription = (TextView) v.findViewById(R.id.taskDescription);
            txtTaskDate = (TextView) v.findViewById(R.id.taskDate);
            taskItemCell = (LinearLayout) v.findViewById(R.id.taskItemCell);
            chkTaskComplete = (CheckBox) v.findViewById(R.id.chkTaskComplete);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    TaskAdapter(Activity activity, ArrayList<TaskItem> items) {
        this.context = activity;
        imageUtils = new ImageUtils(activity);
        data = items;
    }

    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tasklistitemrow, parent, false);

        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final TaskItem c = data.get(position);
        holder.txtTaskDescription.setText(c.taskDescription);
        holder.txtTaskDate.setText(c.taskDateString);
        if(c.taskDateKnown)
        {
            holder.txtTaskDate.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.txtTaskDate.setVisibility(View.INVISIBLE);
        }

        holder.chkTaskComplete.setChecked(c.taskComplete);

        if(c.pictureAssigned)
        {
            holder.taskImage.setVisibility(View.VISIBLE);
            if(imageUtils.getListIcon(context, c.taskPicture, holder.taskImage)==false)
                return;
        }
        else
        {
            holder.taskImage.setVisibility(View.INVISIBLE);
        }


        holder.taskItemCell.setOnClickListener(new View.OnClickListener()
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


    public TaskItem getItem(int position){
        return data.get(position);
    }

    public void add(int position, TaskItem mail){
        data.add(position, mail);
        notifyDataSetChanged();
    }

    boolean onItemMove(int fromPosition, int toPosition) {
        updateGlobalData(data);
        return true;
    }

    private void updateGlobalData(ArrayList<TaskItem> items)
    {
        for (int i=0;i<items.size();i++)
        {
            items.get(i).sequenceNo=i+1;
        }
        if(!databaseAccess().updateTaskItems(items))
            return;
        notifyDataSetChanged();
    }

    private int lastPosition = -1;

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return data.size();
    }

}
