package com.example.des.hp.Contact;

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

class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder>
{
    private Context context;
    public ArrayList<ContactItem> data=null;
    private OnItemClickListener mOnItemClickListener;
    private ImageUtils imageUtils;


    interface OnItemClickListener
    {
        void onItemClick(View view, ContactItem obj);
    }

    void setOnItemClickListener(final OnItemClickListener mItemClickListener)
    {
        this.mOnItemClickListener=mItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        ImageView contactImage;
        TextView txtContactDescription;
        LinearLayout contactItemCell;


        ViewHolder(View v)
        {
            super(v);

            contactImage=(ImageView) v.findViewById(R.id.imgIcon);
            txtContactDescription=(TextView) v.findViewById(R.id.contactDescription);
            contactItemCell=(LinearLayout) v.findViewById(R.id.contactItemCell);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    ContactAdapter(Activity activity, ArrayList<ContactItem> items)
    {
        this.context=activity;
        imageUtils=new ImageUtils(activity);
        data=items;
    }

    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // create a new view
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.contactlistitemrow, parent, false);

        // set the view's size, margins, padding and layout parameters
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        final ContactItem c=data.get(position);
        holder.txtContactDescription.setText(c.contactDescription);

        if(c.pictureAssigned)
        {
            holder.contactImage.setVisibility(View.VISIBLE);
            if(!imageUtils.getListIcon(context, c.contactPicture, holder.contactImage))
                return;
        } else
        {
            holder.contactImage.setVisibility(View.INVISIBLE);
        }


        holder.contactItemCell.setOnClickListener(new View.OnClickListener()
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


    public ContactItem getItem(int position)
    {
        return data.get(position);
    }

    public void add(int position, ContactItem mail)
    {
        data.add(position, mail);
        notifyDataSetChanged();
    }

    boolean onItemMove()
    {
        updateGlobalData(data);
        return true;
    }

    private void updateGlobalData(ArrayList<ContactItem> items)
    {
        for(int i=0; i < items.size(); i++)
        {
            items.get(i).sequenceNo=i + 1;
        }
        if(!databaseAccess().updateContactItems(items))
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
