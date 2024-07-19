package com.example.des.hp.InternalImages;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import com.example.des.hp.myutils.*;
import com.example.des.hp.R;


/**
 * * Created by Des on 06/10/2016.
 */

class InternalImageAdapter extends RecyclerView.Adapter<InternalImageAdapter.ViewHolder>
{
    private final Context context;
    public ArrayList<InternalImageItem> data;
    private OnItemClickListener mOnItemClickListener;
    private final ImageUtils imageUtils;

    interface OnItemClickListener
    {
        void onItemClick(View view, InternalImageItem obj);
    }

    void setOnItemClickListener(final OnItemClickListener mItemClickListener)
    {
        this.mOnItemClickListener=mItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        ImageView internalImage;
        TextView txtFilename;

        ViewHolder(View v)
        {
            super(v);

            internalImage= v.findViewById(R.id.internalImage);
            txtFilename= v.findViewById(R.id.txtFilename);
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    InternalImageAdapter(Activity activity, ArrayList<InternalImageItem> items)
    {
        this.context=activity;
        imageUtils=new ImageUtils(activity);
        data=items;
    }

    @NonNull
    @Override
    public InternalImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // create a new view
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.internalimageitemrow, parent, false);

        // set the view's size, margins, padding and layout parameters
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        final InternalImageItem c=data.get(position);

        if(!c.internalImageFilename.isEmpty())
        {
            if(!imageUtils.getGridIcon(c.holidayId, context, c.internalImageFilename, holder.internalImage))
                return;
            holder.txtFilename.setText(c.internalImageFilename);
        }

        holder.internalImage.setOnClickListener(view -> {
            if(mOnItemClickListener != null)
            {
                mOnItemClickListener.onItemClick(view, c);
            }
        });

    }


    public InternalImageItem getItem(int position)
    {
        return data.get(position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return data.size();
    }

}
