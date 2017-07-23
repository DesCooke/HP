package com.example.des.hp.AttractionArea;

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

class AttractionAreaAdapter extends RecyclerView.Adapter<AttractionAreaAdapter.ViewHolder>
{
    private Context context;
    public ArrayList<AttractionAreaItem> data = null;
    private DatabaseAccess databaseAccess;
    private OnItemClickListener mOnItemClickListener;
    private ImageUtils imageUtils;


    interface OnItemClickListener {
        void onItemClick(View view, AttractionAreaItem obj, int position);
    }

    void setOnItemClickListener(final OnItemClickListener mItemClickListener)
    {
        this.mOnItemClickListener = mItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        ImageView attractionAreaImage;
        TextView txtAttractionAreaDescription;
        LinearLayout attractionAreaItemCell;
        

        ViewHolder(View v)
        {
            super(v);

            attractionAreaImage = (ImageView) v.findViewById(R.id.imgIcon);
            txtAttractionAreaDescription = (TextView) v.findViewById(R.id.attractionAreaDescription);
            attractionAreaItemCell = (LinearLayout) v.findViewById(R.id.attractionAreaItemCell);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    AttractionAreaAdapter(Activity activity, ArrayList<AttractionAreaItem> items) {
        this.context = activity;
        imageUtils = new ImageUtils(activity);
        databaseAccess = new DatabaseAccess(activity);
        data = items;
    }

    @Override
    public AttractionAreaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.attractionarealistitemrow, parent, false);

        // set the view's size, margins, padding and layout parameters
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final AttractionAreaItem c = data.get(position);
        holder.txtAttractionAreaDescription.setText(c.attractionAreaDescription);

        if(c.pictureAssigned)
        {
            holder.attractionAreaImage.setVisibility(View.VISIBLE);
            if(!imageUtils.getListIcon(context, c.attractionAreaPicture, holder.attractionAreaImage))
                return;
        }
        else
        {
            holder.attractionAreaImage.setVisibility(View.INVISIBLE);
        }


        holder.attractionAreaItemCell.setOnClickListener(new View.OnClickListener()
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


    public AttractionAreaItem getItem(int position){
        return data.get(position);
    }

    public void add(int position, AttractionAreaItem mail){
        data.add(position, mail);
        notifyDataSetChanged();
    }

    boolean onItemMove() {
        updateGlobalData(data);
        return true;
    }

    private void updateGlobalData(ArrayList<AttractionAreaItem> items)
    {
        for (int i=0;i<items.size();i++)
        {
            items.get(i).sequenceNo=i+1;
        }
        if(!databaseAccess.updateAttractionAreaItems(items))
            return;
        notifyDataSetChanged();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return data.size();
    }

}
