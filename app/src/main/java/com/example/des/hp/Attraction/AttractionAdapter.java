package com.example.des.hp.Attraction;

import android.app.Activity;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import com.example.des.hp.myutils.*;
import com.example.des.hp.R;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

class AttractionAdapter extends RecyclerView.Adapter<AttractionAdapter.ViewHolder>
{
    private Context context;
    public ArrayList<AttractionItem> data = null;
    private OnItemClickListener mOnItemClickListener;
    private ImageUtils imageUtils;
    
    
    interface OnItemClickListener
    {
        void onItemClick(View view, AttractionItem obj);
    }
    
    void setOnItemClickListener(final OnItemClickListener mItemClickListener)
    {
        this.mOnItemClickListener = mItemClickListener;
    }
    
    class ViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        ImageView attractionImage;
        TextView txtAttractionDescription;
        LinearLayout attractionItemCell;
        
        
        ViewHolder(View v)
        {
            super(v);
            
            attractionImage = v.findViewById(R.id.imgIcon);
            txtAttractionDescription = v.findViewById(R.id.attractionDescription);
            attractionItemCell = v.findViewById(R.id.attractionItemCell);
        }
    }
    
    // Provide a suitable constructor (depends on the kind of dataset)
    AttractionAdapter(Activity activity, ArrayList<AttractionItem> items)
    {
        this.context = activity;
        imageUtils = new ImageUtils(activity);
        data = items;
    }
    
    @Override
    public AttractionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.attractionlistitemrow, parent, false);
        
        // set the view's size, margins, padding and layout parameters
        return new ViewHolder(v);
    }
    
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        
        final AttractionItem c = data.get(position);
        holder.txtAttractionDescription.setText(c.attractionDescription);
        
        if (c.pictureAssigned)
        {
            holder.attractionImage.setVisibility(View.VISIBLE);
            if (!imageUtils.getListIcon(c.holidayId, context, c.attractionPicture, holder.attractionImage))
                return;
        } else
        {
            holder.attractionImage.setVisibility(View.INVISIBLE);
        }
        
        
        holder.attractionItemCell.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (mOnItemClickListener != null)
                {
                    mOnItemClickListener.onItemClick(view, c);
                }
            }
        });
    }
    
    
    public AttractionItem getItem(int position)
    {
        return data.get(position);
    }
    
    public void add(int position, AttractionItem mail)
    {
        data.add(position, mail);
        notifyDataSetChanged();
    }
    
    boolean onItemMove()
    {
        updateGlobalData(data);
        return true;
    }
    
    private void updateGlobalData(ArrayList<AttractionItem> items)
    {
        for (int i = 0; i < items.size(); i++)
        {
            items.get(i).sequenceNo = i + 1;
        }
        if (!databaseAccess().updateAttractionItems(items))
            return;
        notifyDataSetChanged();
    }
    
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return data.size();
    }
    
}
