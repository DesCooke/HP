package com.example.des.hp.ExtraFiles;

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

import com.example.des.hp.myutils.*;
import com.example.des.hp.R;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

/**
 * * Created by Des on 06/10/2016.
 */

class ExtraFilesAdapter extends RecyclerView.Adapter<ExtraFilesAdapter.ViewHolder>
{
    private Context context;
    private int layoutResourceId;
    public ArrayList<ExtraFilesItem> data=null;
    private DateUtils dateUtils;
    private OnItemClickListener mOnItemClickListener;
    private ImageUtils imageUtils;

    interface OnItemClickListener
    {
        void onItemClick(View view, ExtraFilesItem obj);
    }

    void setOnItemClickListener(final OnItemClickListener mItemClickListener)
    {
        this.mOnItemClickListener=mItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        ImageView fileImage;
        TextView txtFileDescription;
        LinearLayout fileItemCell;

        ViewHolder(View v)
        {
            super(v);

            fileImage=(ImageView) v.findViewById(R.id.imgIcon);
            txtFileDescription=(TextView) v.findViewById(R.id.txtFileDescription);
            fileItemCell=(LinearLayout) v.findViewById(R.id.fileItemCell);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    ExtraFilesAdapter(Activity activity, ArrayList<ExtraFilesItem> items)
    {
        this.context=activity;
        imageUtils=new ImageUtils(activity);
        data=items;
    }

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

        if(c.filePicture.length() > 0)
            if(imageUtils.getListIcon(context, c.filePicture, holder.fileImage) == false)
                return;

        holder.fileItemCell.setOnClickListener(new View.OnClickListener()
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


    public ExtraFilesItem getItem(int position)
    {
        return data.get(position);
    }

    public void add(int position, ExtraFilesItem mail)
    {
        data.add(position, mail);
        notifyDataSetChanged();
    }

    boolean onItemMove(int fromPosition, int toPosition)
    {
        updateGlobalData(data);
        return true;
    }

    private void updateGlobalData(ArrayList<ExtraFilesItem> items)
    {
        for(int i=0; i < items.size(); i++)
        {
            items.get(i).sequenceNo=i + 1;
        }
        if(!databaseAccess().updateExtraFilesItems(items))
            return;
        notifyDataSetChanged();
    }

    private int lastPosition=-1;

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return data.size();
    }

}
