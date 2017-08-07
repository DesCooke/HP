package com.example.des.hp.InternalImages;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.myutils.*;
import com.example.des.hp.R;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

/**
 ** Created by Des on 06/10/2016.
 */

class InternalImageAdapter extends RecyclerView.Adapter<InternalImageAdapter.ViewHolder>
{
    private Context context;
    private Resources res;
    private int layoutResourceId;
    public ArrayList<InternalImageItem> data = null;
    private OnItemClickListener mOnItemClickListener;
    private ImageUtils imageUtils;

    interface OnItemClickListener {
        void onItemClick(View view, InternalImageItem obj, int position);
    }

    void setOnItemClickListener(final OnItemClickListener mItemClickListener)
    {
        this.mOnItemClickListener = mItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        ImageView internalImage;

        ViewHolder(View v)
        {
            super(v);

            internalImage = (ImageView) v.findViewById(R.id.internalImage);
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    InternalImageAdapter(Activity activity, ArrayList<InternalImageItem> items) {
        this.context = activity;
        res = this.context.getResources();
        imageUtils = new ImageUtils(activity);
        data = items;
    }

    @Override
    public InternalImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.internalimageitemrow, parent, false);

        // set the view's size, margins, padding and layout parameters
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final InternalImageItem c = data.get(position);

        if(c.internalImageFilename.length()>0)
            if(imageUtils.getListIcon(context, c.internalImageFilename, holder.internalImage)==false)
                return;
/*
        holder.dayItemCell.setOnClickListener(new View.OnClickListener()
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
*/
    }


    public InternalImageItem getItem(int position){
        return data.get(position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return data.size();
    }

}
