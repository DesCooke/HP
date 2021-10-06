package com.example.des.hp.InternalFiles;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import com.example.des.hp.R;

class InternalFileAdapter extends RecyclerView.Adapter<InternalFileAdapter.ViewHolder>
{
    private int layoutResourceId;
    public ArrayList<InternalFileItem> data=null;
    private OnItemClickListener mOnItemClickListener;

    interface OnItemClickListener
    {
        void onItemClick(View view, InternalFileItem obj);
    }

    void setOnItemClickListener(final OnItemClickListener mItemClickListener)
    {
        this.mOnItemClickListener=mItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        TextView txtFilename;
        LinearLayout grpFilename;

        ViewHolder(View v)
        {
            super(v);

            txtFilename=(TextView) v.findViewById(R.id.txtFilename);
            grpFilename=(LinearLayout) v.findViewById(R.id.grpFilename);
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    InternalFileAdapter(Activity activity, ArrayList<InternalFileItem> items)
    {
        Context context=activity;
        Resources res=context.getResources();
        data=items;
    }

    @Override
    public InternalFileAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // create a new view
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.internalfileitemrow, parent, false);

        // set the view's size, margins, padding and layout parameters
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        final InternalFileItem c=data.get(position);

        holder.txtFilename.setText(c.filename);

        holder.grpFilename.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(mOnItemClickListener != null)
                {
                    mOnItemClickListener.onItemClick(view, c);
                }
            }
        });

    }


    public InternalFileItem getItem(int position)
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
