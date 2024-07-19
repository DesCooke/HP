package com.example.des.hp.ExtraFiles;

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
import com.example.des.hp.R;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

/**
 * * Created by Des on 06/10/2016.
 */

class ExtraFilesAdapter extends RecyclerView.Adapter<ExtraFilesAdapter.ViewHolder>
{
    public ArrayList<ExtraFilesItem> data;
    private OnItemClickListener mOnItemClickListener;

    interface OnItemClickListener
    {
        void onItemClick(View view, ExtraFilesItem obj);
    }

    void setOnItemClickListener(final OnItemClickListener mItemClickListener)
    {
        this.mOnItemClickListener=mItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        ImageView fileImage;
        TextView txtFileDescription;
        LinearLayout fileItemCell;

        ViewHolder(View v)
        {
            super(v);

            fileImage= v.findViewById(R.id.imgIcon);
            txtFileDescription= v.findViewById(R.id.txtFileDescription);
            fileItemCell= v.findViewById(R.id.fileItemCell);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    ExtraFilesAdapter(ArrayList<ExtraFilesItem> items)
    {
        data=items;
    }

    @NonNull
    @Override
    public ExtraFilesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // create a new view
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.extrafileslistitemrow, parent, false);

        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        final ExtraFilesItem c=data.get(position);
        holder.txtFileDescription.setText(c.fileDescription);

        holder.fileItemCell.setOnClickListener(view -> {
            if(mOnItemClickListener != null)
            {
                mOnItemClickListener.onItemClick(view, c);
            }
        });
    }


    public ExtraFilesItem getItem(int position)
    {
        return data.get(position);
    }

    public void DeleteItemAtPos(int position)
    {
        ExtraFilesItem item=getItem(position);
        try(DatabaseAccess da = databaseAccess())
        {
            da.deleteExtraFilesItem(item);
        }
        data.remove(position);
        notifyItemRemoved(position);
    }

    public void add(int position, ExtraFilesItem mail)
    {
        data.add(position, mail);
        notifyItemInserted(position);
    }

    void onItemMove()
    {
        updateGlobalData(data);
    }

    private void updateGlobalData(ArrayList<ExtraFilesItem> items)
    {
        for(int i=0; i < items.size(); i++)
        {
            items.get(i).sequenceNo=i + 1;
        }
        try(DatabaseAccess da = databaseAccess())
        {
            if(!da.updateExtraFilesItems(items))
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
