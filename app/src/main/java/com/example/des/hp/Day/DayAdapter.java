package com.example.des.hp.Day;

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
 * * Created by Des on 06/10/2016.
 */

class DayAdapter extends RecyclerView.Adapter<DayAdapter.ViewHolder>
{
    private Context context;
    private Resources res;
    private int layoutResourceId;
    public ArrayList<DayItem> data=null;
    private DateUtils dateUtils;
    private OnItemClickListener mOnItemClickListener;
    private ImageUtils imageUtils;
    private TextView txtRange;
    private TextView txtHours;
    private LinearLayout dayFullCell;

    interface OnItemClickListener
    {
        void onItemClick(View view, DayItem obj);
    }

    void setOnItemClickListener(final OnItemClickListener mItemClickListener)
    {
        this.mOnItemClickListener=mItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder
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

            dayImage=(ImageView) v.findViewById(R.id.imgIcon);
            txtStartAt=(TextView) v.findViewById(R.id.txtStartAt);
            txtHours=(TextView) v.findViewById(R.id.txtHours);
            txtTitle=(TextView) v.findViewById(R.id.txtTitle);
            txtEndAt=(TextView) v.findViewById(R.id.txtEndAt);
            txtStartDate=(TextView) v.findViewById(R.id.txtStartDate);
            dayItemCell=(LinearLayout) v.findViewById(R.id.dayItemCell);
            txtRange=(TextView) v.findViewById(R.id.txtRange);
            txtHours=(TextView) v.findViewById(R.id.txtHours);
            dayFullCell=(LinearLayout) v.findViewById(R.id.dayFullCell2);
            timesSection=(LinearLayout) v.findViewById(R.id.timesSection);
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    DayAdapter(Activity activity, ArrayList<DayItem> items)
    {
        this.context=activity;
        res=this.context.getResources();
        imageUtils=new ImageUtils(activity);
        dateUtils=new DateUtils(activity);
        data=items;
    }

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
        holder.txtStartAt.setText(c.start_at);
        holder.txtEndAt.setText(c.end_at);

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

            // we subtract 1 because sequenceno starts at 1 - but we want to add 0 days for the
            // first element
            if(dateUtils.AddDays(DatabaseAccess.currentStartDate, (c.sequenceNo - 1), lcurrdate) == false)
                return;

            MyString myString=new MyString();
            if(dateUtils.DateToStr(lcurrdate, myString) == false)
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
            String lTotal=DateUtils.FormatTime(c.totalHours, c.totalMins);

            holder.txtRange.setText(lFrom + "-" + lTo);
            String ltxtHours;
            if(c.totalHours == 1)
            {
                ltxtHours=String.format(Locale.ENGLISH, res.getString(R.string.fmt_hr_line), c.totalHours);
            } else
            {
                ltxtHours=String.format(Locale.ENGLISH, res.getString(R.string.fmt_hrs_line), c.totalHours);
            }
            holder.txtHours.setText(ltxtHours);

            if(c.totalHours > 12)
            {
                holder.txtHours.setTextColor(context.getColor(R.color.colorWarning));
            } else
            {
                holder.txtHours.setTextColor(context.getColor(R.color.colorOk));
            }

        }

        int lColor=-1;
        if(c.dayCat == res.getInteger(R.integer.day_cat_easy))
            lColor=context.getColor(R.color.colorEasy);
        if(c.dayCat == res.getInteger(R.integer.day_cat_moderate))
            lColor=context.getColor(R.color.colorModerate);
        if(c.dayCat == res.getInteger(R.integer.day_cat_busy))
            lColor=context.getColor(R.color.colorBusy);

        if(lColor != -1)
        {
            holder.dayFullCell.setBackgroundColor(lColor);
            holder.timesSection.setBackgroundColor(lColor);
        }

        if(c.dayPicture.length() > 0)
            if(imageUtils.getListIcon(context, c.dayPicture, holder.dayImage) == false)
                return;

        holder.dayItemCell.setOnClickListener(new View.OnClickListener()
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


    public DayItem getItem(int position)
    {
        return data.get(position);
    }

    public void add(int position, DayItem mail)
    {
        data.add(position, mail);
        notifyDataSetChanged();
    }

    boolean onItemMove(int fromPosition, int toPosition)
    {
        updateGlobalData(data);
        return true;
    }

    private void updateGlobalData(ArrayList<com.example.des.hp.Day.DayItem> items)
    {
        for(int i=0; i < items.size(); i++)
        {
            items.get(i).sequenceNo=i + 1;
        }
        if(!databaseAccess().updateDayItems(items))
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
