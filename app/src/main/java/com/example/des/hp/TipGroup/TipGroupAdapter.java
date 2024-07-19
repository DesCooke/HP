package com.example.des.hp.TipGroup;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

/**
 * * Created by Des on 06/10/2016.
 */

class TipGroupAdapter extends RecyclerView.Adapter<TipGroupAdapter.ViewHolder>
{
    private final Context context;
    public ArrayList<TipGroupItem> data;
    private OnItemClickListener mOnItemClickListener;
    private final ImageUtils imageUtils;


    interface OnItemClickListener
    {
        void onItemClick(View view, TipGroupItem obj);
    }

    void setOnItemClickListener(final OnItemClickListener mItemClickListener)
    {
        this.mOnItemClickListener=mItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        ImageView tipGroupImage;
        TextView txtTipGroupDescription;
        LinearLayout tipGroupItemCell;


        ViewHolder(View v)
        {
            super(v);

            tipGroupImage= v.findViewById(R.id.imgIcon);
            txtTipGroupDescription= v.findViewById(R.id.tipGroupDescription);
            tipGroupItemCell= v.findViewById(R.id.tipGroupItemCell);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    TipGroupAdapter(Activity activity, ArrayList<TipGroupItem> items)
    {
        this.context=activity;
        imageUtils=new ImageUtils(activity);
        data=items;
    }

    @NonNull
    @Override
    public TipGroupAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // create a new view
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.tipgrouplistitemrow, parent, false);

        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        final TipGroupItem c=data.get(position);
        holder.txtTipGroupDescription.setText(c.tipGroupDescription);

        if(c.pictureAssigned)
        {
            holder.tipGroupImage.setVisibility(View.VISIBLE);
            if(!imageUtils.getListIcon(c.holidayId, context, c.tipGroupPicture, holder.tipGroupImage))
                return;
        } else
        {
            holder.tipGroupImage.setVisibility(View.INVISIBLE);
        }


        holder.tipGroupItemCell.setOnClickListener(view -> {
            if(mOnItemClickListener != null)
            {
                mOnItemClickListener.onItemClick(view, c);
            }
        });
    }


    public TipGroupItem getItem(int position)
    {
        return data.get(position);
    }

    public void add(int position, TipGroupItem mail)
    {
        data.add(position, mail);
        notifyItemInserted(position);
    }

    void onItemMove()
    {
        updateGlobalData(data);
    }

    private void updateGlobalData(ArrayList<TipGroupItem> items)
    {
        for(int i=0; i < items.size(); i++)
        {
            items.get(i).sequenceNo=i + 1;
        }
        try(DatabaseAccess da = databaseAccess())
        {
            if(!da.updateTipGroupItems(items))
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
