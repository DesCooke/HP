package com.example.des.hp.ThemeParks;

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

class ThemeParkAreaAdapter extends RecyclerView.Adapter<ThemeParkAreaAdapter.ViewHolder>
{
    private final Context context;
    public ArrayList<ThemeParkAreaItem> data;
    private OnItemClickListener mOnItemClickListener;
    private final ImageUtils imageUtils;
    
    
    interface OnItemClickListener
    {
        void onItemClick(View view, ThemeParkAreaItem obj);
    }
    
    void setOnItemClickListener(final OnItemClickListener mItemClickListener)
    {
        this.mOnItemClickListener = mItemClickListener;
    }
    
    static class ViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        ImageView attractionAreaImage;
        TextView txtAttractionAreaDescription;
        LinearLayout attractionAreaItemCell;
        
        
        ViewHolder(View v)
        {
            super(v);
            
            attractionAreaImage = v.findViewById(R.id.imgIcon);
            txtAttractionAreaDescription = v.findViewById(R.id.attractionAreaDescription);
            attractionAreaItemCell = v.findViewById(R.id.attractionAreaItemCell);
        }
    }
    
    // Provide a suitable constructor (depends on the kind of dataset)
    ThemeParkAreaAdapter(Activity activity, ArrayList<ThemeParkAreaItem> items)
    {
        this.context = activity;
        imageUtils = new ImageUtils(activity);
        data = items;
    }
    
    @NonNull
    @Override
    public ThemeParkAreaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.attractionarealistitemrow, parent, false);
        
        // set the view's size, margins, padding and layout parameters
        return new ViewHolder(v);
    }
    
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        final ThemeParkAreaItem c = data.get(position);
        holder.txtAttractionAreaDescription.setText(c.attractionAreaDescription);
        
        if (c.pictureAssigned)
        {
            holder.attractionAreaImage.setVisibility(View.VISIBLE);
            if (!imageUtils.getListIcon(c.holidayId, context, c.attractionAreaPicture, holder.attractionAreaImage))
                return;
        } else
        {
            holder.attractionAreaImage.setVisibility(View.INVISIBLE);
        }
        
        
        holder.attractionAreaItemCell.setOnClickListener(view -> {
            if (mOnItemClickListener != null)
            {
                mOnItemClickListener.onItemClick(view, c);
            }
        });
    }
    
    
    public ThemeParkAreaItem getItem(int position)
    {
        return data.get(position);
    }
    
    public void add(int position, ThemeParkAreaItem mail)
    {
        data.add(position, mail);
        notifyItemInserted(position);
    }
    
    void onItemMove()
    {
        updateGlobalData(data);
    }
    
    private void updateGlobalData(ArrayList<ThemeParkAreaItem> items)
    {
        for (int i = 0; i < items.size(); i++)
        {
            items.get(i).sequenceNo = i + 1;
        }
        try(DatabaseAccess da = databaseAccess())
        {
            if (!da.updateAttractionAreaItems(items))
                return;
        }
        notifyItemRangeChanged(0,items.size()-1);
    }
    
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return data.size();
    }
    
}
