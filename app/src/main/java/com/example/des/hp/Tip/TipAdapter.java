package com.example.des.hp.Tip;

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

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.myutils.*;
import com.example.des.hp.R;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

class TipAdapter extends RecyclerView.Adapter<TipAdapter.ViewHolder>
{
    private Context context;
    private int layoutResourceId;
    public ArrayList<TipItem> data=null;
    private OnItemClickListener mOnItemClickListener;
    private ImageUtils imageUtils;


    interface OnItemClickListener
    {
        void onItemClick(View view, TipItem obj);
    }

    void setOnItemClickListener(final OnItemClickListener mItemClickListener)
    {
        this.mOnItemClickListener=mItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        ImageView tipImage;
        TextView txtTipDescription;
        LinearLayout tipItemCell;
        TextView txtNotes;


        ViewHolder(View v)
        {
            super(v);

            tipImage=(ImageView) v.findViewById(R.id.imgIcon);
            txtTipDescription=(TextView) v.findViewById(R.id.tipDescription);
            tipItemCell=(LinearLayout) v.findViewById(R.id.tipItemCell);
            txtNotes=(TextView) v.findViewById(R.id.tipNotes);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    TipAdapter(Activity activity, ArrayList<TipItem> items)
    {
        this.context=activity;
        imageUtils=new ImageUtils(activity);
        data=items;
    }

    @Override
    public TipAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // create a new view
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.tiplistitemrow, parent, false);

        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        final TipItem c=data.get(position);
        holder.txtTipDescription.setText(c.tipDescription);
        holder.txtNotes.setText(c.tipNotes);

        if(c.pictureAssigned)
        {
            holder.tipImage.setVisibility(View.VISIBLE);
            if(imageUtils.getListIcon(c.holidayId, context, c.tipPicture, holder.tipImage) == false)
                return;
        } else
        {
            holder.tipImage.setVisibility(View.INVISIBLE);
        }


        holder.tipItemCell.setOnClickListener(new View.OnClickListener()
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


    public TipItem getItem(int position)
    {
        return data.get(position);
    }

    public void add(int position, TipItem mail)
    {
        data.add(position, mail);
        notifyDataSetChanged();
    }

    boolean onItemMove(int fromPosition, int toPosition)
    {
        updateGlobalData(data);
        return true;
    }

    private void updateGlobalData(ArrayList<TipItem> items)
    {
        for(int i=0; i < items.size(); i++)
        {
            items.get(i).sequenceNo=i + 1;
        }
        try(DatabaseAccess da = databaseAccess();)
        {
            if(!da.updateTipItems(items))
                return;
        }
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
