package com.example.des.hp.Day;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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
import static com.example.des.hp.myutils.MyApiSpecific.myApiSpecific;

/**
 * * Created by Des on 06/10/2016.
 */

class DayAdapter extends RecyclerView.Adapter<DayAdapter.ViewHolder>
{
    private final Context context;
    private final Resources res;
    public ArrayList<DayItem> data;
    private final DateUtils dateUtils;
    private OnItemClickListener mOnItemClickListener;
    private final ImageUtils imageUtils;

    interface OnItemClickListener
    {
        void onItemClick(View view, DayItem obj);
    }

    void setOnItemClickListener(final OnItemClickListener mItemClickListener)
    {
        this.mOnItemClickListener=mItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        ImageView dayImage;
        TextView txtTitle;
        public TextView txtStartDate;
        LinearLayout dayItemCell;
        LinearLayout dayFullCell;
        TextView txtRange;
        TextView txtHours;
        LinearLayout timesSection;
        TextView txtStartAt;
        TextView txtEndAt;

        ViewHolder(View v)
        {
            super(v);

            dayImage= v.findViewById(R.id.imgIcon);
            txtStartAt= v.findViewById(R.id.txtStartAt);
            txtHours= v.findViewById(R.id.txtHours);
            txtTitle= v.findViewById(R.id.txtTitle);
            txtEndAt= v.findViewById(R.id.txtEndAt);
            txtStartDate= v.findViewById(R.id.txtStartDate);
            dayItemCell= v.findViewById(R.id.dayItemCell);
            txtRange= v.findViewById(R.id.txtRange);
            txtHours= v.findViewById(R.id.txtHours);
            dayFullCell= v.findViewById(R.id.dayFullCell2);
            timesSection= v.findViewById(R.id.timesSection);
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    DayAdapter(Activity activity, ArrayList<DayItem> items)
    {
        this.context=activity;
        res=this.context.getResources();
        imageUtils=new ImageUtils(activity);
        dateUtils=new DateUtils();
        data=items;
    }

    @NonNull
    @Override
    public DayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // create a new view
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.daylistitemrow, parent, false);

        // set the view's size, margins, padding and layout parameters
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        final DayItem c=data.get(position);

        holder.txtTitle.setText(c.dayName);
        holder.txtStartAt.setText("");
        holder.txtEndAt.setText("");

        MyBoolean isUnknown=new MyBoolean();
        if(!dateUtils.IsUnknown(DatabaseAccess.currentStartDate, isUnknown))
            return;

        String lStartDate;

        if(isUnknown.Value)
        {
            lStartDate=String.format(Locale.ENGLISH, res.getString(R.string.fmt_day_line), c.sequenceNo);
        } else
        {
            Date lcurrdate=new Date();

            // we subtract 1 because sequence starts at 1 - but we want to add 0 days for the
            // first element
            if(!dateUtils.AddDays(DatabaseAccess.currentStartDate, (c.sequenceNo - 1), lcurrdate))
                return;

            MyString myString=new MyString();
            if(!dateUtils.DateToStr(lcurrdate, myString))
                return;
            lStartDate=String.format(Locale.ENGLISH, res.getString(R.string.fmt_date_line), myString.Value);
        }
        holder.txtStartDate.setText(lStartDate);


        if(c.totalHours == 0 && c.totalMins == 0)
        {
            holder.txtRange.setText("");
            holder.txtHours.setText("");
        } else
        {
            String lFrom=DateUtils.FormatTime(c.startOfDayHours, c.startOfDayMins);
            String lTo=DateUtils.FormatTime(c.endOfDayHours, c.endOfDayMins);

            holder.txtRange.setText(String.format("%s-%s", lFrom, lTo));
            String txtHours;
            if(c.totalHours == 1)
            {
                txtHours=String.format(Locale.ENGLISH, res.getString(R.string.fmt_hr_line), c.totalHours);
            } else
            {
                txtHours=String.format(Locale.ENGLISH, res.getString(R.string.fmt_hrs_line), c.totalHours);
            }
            holder.txtHours.setText(txtHours);

            if(c.totalHours > 12)
            {
                holder.txtHours.setTextColor(myApiSpecific().getTheColor(R.color.colorWarning));
            } else
            {
                holder.txtHours.setTextColor(myApiSpecific().getTheColor(R.color.colorOk));
            }

        }

        int lColor=-1;
        if(c.dayCat == res.getInteger(R.integer.day_cat_easy))
            lColor=myApiSpecific().getTheColor(R.color.colorEasy);
        if(c.dayCat == res.getInteger(R.integer.day_cat_moderate))
            lColor=myApiSpecific().getTheColor(R.color.colorModerate);
        if(c.dayCat == res.getInteger(R.integer.day_cat_busy))
            lColor=myApiSpecific().getTheColor(R.color.colorBusy);

        if(lColor != -1)
        {
            holder.dayFullCell.setBackgroundColor(lColor);
            holder.timesSection.setBackgroundColor(lColor);
        }

        if(!c.dayPicture.isEmpty()) {
            holder.dayImage.setVisibility(View.VISIBLE);
            if (!imageUtils.getListIcon(c.holidayId, context, c.dayPicture, holder.dayImage))
                return;
        }
        else{
            holder.dayImage.setVisibility(View.GONE);
        }

        holder.dayItemCell.setOnClickListener(view -> {
            if(mOnItemClickListener != null)
            {
                mOnItemClickListener.onItemClick(view, c);
            }
        });
    }


    public DayItem getItem(int position)
    {
        return data.get(position);
    }

    public void add(int position, DayItem mail)
    {
        data.add(position, mail);
        notifyItemInserted(position);
    }

    void onItemMove(int ignoredFromPosition, int ignoredToPosition)
    {
        updateGlobalData(data);
    }

    private void updateGlobalData(ArrayList<com.example.des.hp.Day.DayItem> items)
    {
        for(int i=0; i < items.size(); i++)
        {
            items.get(i).sequenceNo=i + 1;
        }

        try(DatabaseAccess da = databaseAccess())
        {
            if(!da.updateDayItems(items))
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
