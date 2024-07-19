package com.example.des.hp.Contact;

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

class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder>
{
    private final Context context;
    public ArrayList<ContactItem> data;
    private OnItemClickListener mOnItemClickListener;
    private final ImageUtils imageUtils;
    
    
    interface OnItemClickListener
    {
        void onItemClick(View view, ContactItem obj);
    }
    
    void setOnItemClickListener(final OnItemClickListener mItemClickListener)
    {
        this.mOnItemClickListener = mItemClickListener;
    }
    
    static class ViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        ImageView contactImage;
        TextView txtContactDescription;
        LinearLayout contactItemCell;
        
        
        ViewHolder(View v)
        {
            super(v);
            
            contactImage = v.findViewById(R.id.imgIcon);
            txtContactDescription = v.findViewById(R.id.contactDescription);
            contactItemCell = v.findViewById(R.id.contactItemCell);
        }
    }
    
    // Provide a suitable constructor (depends on the kind of dataset)
    ContactAdapter(Activity activity, ArrayList<ContactItem> items)
    {
        this.context = activity;
        imageUtils = new ImageUtils(activity);
        data = items;
    }
    
    @NonNull
    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contactlistitemrow, parent, false);
        
        // set the view's size, margins, padding and layout parameters
        return new ViewHolder(v);
    }
    
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        final ContactItem c = data.get(position);
        holder.txtContactDescription.setText(c.contactDescription);
        
        if (c.pictureAssigned)
        {
            holder.contactImage.setVisibility(View.VISIBLE);
            if (!imageUtils.getListIcon(c.holidayId, context, c.contactPicture, holder.contactImage))
                return;
        } else
        {
            holder.contactImage.setVisibility(View.INVISIBLE);
        }
        
        
        holder.contactItemCell.setOnClickListener(view -> {
            if (mOnItemClickListener != null)
            {
                mOnItemClickListener.onItemClick(view, c);
            }
        });
    }
    
    
    public ContactItem getItem(int position)
    {
        return data.get(position);
    }
    
    public void add(int position, ContactItem mail)
    {
        data.add(position, mail);
        notifyItemInserted(position);
    }
    
    void onItemMove()
    {
        updateGlobalData(data);
    }
    
    private void updateGlobalData(ArrayList<ContactItem> items)
    {
        for (int i = 0; i < items.size(); i++)
        {
            items.get(i).sequenceNo = i + 1;
        }
        try(DatabaseAccess da = databaseAccess())
        {
            if (!da.updateContactItems(items))
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
