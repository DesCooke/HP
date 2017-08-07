package com.example.des.hp.Schedule;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.myutils.*;
import com.example.des.hp.R;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

/**
 ** Created by Des on 06/10/2016.
 */

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder>
{
    private Context context;
    private int layoutResourceId;
    public ArrayList<ScheduleItem> data = null;
    private OnItemClickListener mOnItemClickListener;
    private ImageUtils imageUtils;
    private Drawable drawableAirplane;
    private Drawable drawableHotel;
    private Drawable drawableShow;
    private Drawable drawableBus;
    private Drawable drawableRestaurant;
    private Drawable drawableCinema;
    private Drawable drawablePark;
    private Drawable drawableParade;
    private Drawable drawableOther;
    private Drawable drawableRide;
    private Drawable drawableGeneralAttraction;


    public interface OnItemClickListener {
        void onItemClick(View view, ScheduleItem obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener)
    {
        this.mOnItemClickListener = mItemClickListener;
    }
    

    class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        ImageView scheduleTypeImage;
        ImageView scheduleImage;
        TextView txtSchedName;
        TextView txtTimeRange;
        LinearLayout scheduleItemCell;
        TextView txtReservationType;
        RatingBar heartRatingView;
        RatingBar scenicRatingView;
        RatingBar thrillRatingView;

        ViewHolder(View v) {
            super(v);

            scheduleTypeImage = (ImageView)v.findViewById(R.id.imgScheduleType);
            scheduleImage = (ImageView)v.findViewById(R.id.imgSchedule);
            txtSchedName = (TextView) v.findViewById(R.id.txtSchedName);
            txtTimeRange = (TextView) v.findViewById(R.id.txtTimeRange);
            scheduleItemCell = (LinearLayout) v.findViewById(R.id.scheduleItemCell);
            scenicRatingView = (RatingBar) v.findViewById(R.id.rbScenicRatingView);
            heartRatingView = (RatingBar) v.findViewById(R.id.rbHeartRatingView);
            thrillRatingView = (RatingBar) v.findViewById(R.id.rbThrillRatingView);
            txtReservationType = (TextView) v.findViewById(R.id.txtReservationType);
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ScheduleAdapter(Activity activity, ArrayList<ScheduleItem> items) {
        this.context = activity;
        imageUtils = new ImageUtils(activity);
        DateUtils dateUtils = new DateUtils(activity);
        data = items;
        drawableAirplane = context.getDrawable(R.drawable.airplane);
        drawableHotel = context.getDrawable(R.drawable.hotel);
        drawableBus = context.getDrawable(R.drawable.bus);
        drawableRestaurant = context.getDrawable(R.drawable.restaurant);
        drawableCinema = context.getDrawable(R.drawable.cinema);
        drawablePark= context.getDrawable(R.drawable.park);
        drawableParade = context.getDrawable(R.drawable.parade);
        drawableOther = context.getDrawable(R.drawable.other);
        drawableShow = context.getDrawable(R.drawable.show);
        drawableRide = context.getDrawable(R.drawable.ride);
        drawableGeneralAttraction = context.getDrawable(R.drawable.attraction);
    }

    @Override
    public ScheduleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedulelistitemrow, parent, false);

        // set the view's size, margins, padding and layout parameters
        return new ViewHolder(v);
    }

    private void setScheduleTypeIcon(int schedType, ImageView imageView)
    {
        if(schedType==context.getResources().getInteger(R.integer.schedule_type_flight))
            imageView.setImageDrawable(drawableAirplane);
        if(schedType==context.getResources().getInteger(R.integer.schedule_type_hotel))
            imageView.setImageDrawable(drawableHotel);
        if(schedType==context.getResources().getInteger(R.integer.schedule_type_bus))
            imageView.setImageDrawable(drawableBus);
        if(schedType==context.getResources().getInteger(R.integer.schedule_type_restaurant))
            imageView.setImageDrawable(drawableRestaurant);
        if(schedType==context.getResources().getInteger(R.integer.schedule_type_cinema))
            imageView.setImageDrawable(drawableCinema);
        if(schedType==context.getResources().getInteger(R.integer.schedule_type_parade))
            imageView.setImageDrawable(drawableParade);
        if(schedType==context.getResources().getInteger(R.integer.schedule_type_park))
            imageView.setImageDrawable(drawablePark);
        if(schedType==context.getResources().getInteger(R.integer.schedule_type_other))
            imageView.setImageDrawable(drawableOther);
        if(schedType==context.getResources().getInteger(R.integer.schedule_type_show))
            imageView.setImageDrawable(drawableShow);
        if(schedType==context.getResources().getInteger(R.integer.schedule_type_ride))
            imageView.setImageDrawable(drawableRide);
        if(schedType==context.getResources().getInteger(R.integer.schedule_type_generalattraction))
            imageView.setImageDrawable(drawableGeneralAttraction);
    }

    private String formatTime(int hour, int minute)
    {
        String lTime;
        lTime="";
        if(hour<10)
            lTime="0";
        lTime=lTime+hour;
        lTime=lTime+":";
        if(minute<10)
            lTime=lTime+"0";
        lTime=lTime+minute;
        return(lTime);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final ScheduleItem c = data.get(position);

        holder.txtSchedName.setText(c.schedName);

        holder.scenicRatingView.setVisibility(View.GONE);
        holder.heartRatingView.setVisibility(View.GONE);
        holder.thrillRatingView.setVisibility(View.GONE);

        String lString;
        lString="";
        if (c.startTimeKnown==true)
            lString = lString + formatTime(c.startHour, c.startMin);
        if (c.startTimeKnown==true || c.endTimeKnown==true)
            lString = lString + " - ";
        if (c.endTimeKnown==true)
            lString = lString + formatTime(c.endHour, c.endMin);
        holder.txtTimeRange.setText(lString);

        setScheduleTypeIcon(c.schedType, holder.scheduleTypeImage);

        boolean lShowRating=false;

        holder.txtReservationType.setVisibility(View.GONE);
        if(c.schedType==context.getResources().getInteger(R.integer.schedule_type_restaurant))
        {
            if(c.restaurantItem!=null)
            {
                if(c.restaurantItem.reservationType==1)
                {
                    holder.txtReservationType.setVisibility(View.VISIBLE);
                    holder.txtReservationType.setText("No Reservations needed - just walk in");
                }
                if(c.restaurantItem.reservationType==2)
                {
                    holder.txtReservationType.setVisibility(View.VISIBLE);
                    holder.txtReservationType.setText("Reserve a table as early as possible on the day");
                }
                if(c.restaurantItem.reservationType==3)
                {
                    holder.txtReservationType.setVisibility(View.VISIBLE);
                    holder.txtReservationType.setText("Reserve a table 180 days in advance");
                }
            }
        }

        if(c.schedType==context.getResources().getInteger(R.integer.schedule_type_ride))
            lShowRating = true;
        if(c.schedType==context.getResources().getInteger(R.integer.schedule_type_show))
            lShowRating = true;
        if(c.schedType==context.getResources().getInteger(R.integer.schedule_type_generalattraction))
            lShowRating = true;
        if(lShowRating)
        {
            holder.scenicRatingView.setVisibility(View.VISIBLE);
            holder.heartRatingView.setVisibility(View.VISIBLE);
            holder.thrillRatingView.setVisibility(View.VISIBLE);
            if(c.rideItem!=null) {
                holder.scenicRatingView.setRating(c.rideItem.scenicRating);
                holder.heartRatingView.setRating(c.rideItem.heartRating);
                holder.thrillRatingView.setRating(c.rideItem.thrillRating);
            }
            if(c.showItem!=null) {
                holder.scenicRatingView.setRating(c.showItem.scenicRating);
                holder.heartRatingView.setRating(c.showItem.heartRating);
                holder.thrillRatingView.setRating(c.showItem.thrillRating);
            }
            if(c.generalAttractionItem!=null) {
                holder.scenicRatingView.setRating(c.generalAttractionItem.scenicRating);
                holder.heartRatingView.setRating(c.generalAttractionItem.heartRating);
                holder.thrillRatingView.setRating(c.generalAttractionItem.thrillRating);
            }
        }

        if(c.schedPicture.length()>0)
            if(imageUtils.getListIcon(context, c.schedPicture, holder.scheduleImage)==false)
                return;

        holder.scheduleItemCell.setOnClickListener(new View.OnClickListener()
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


    public ScheduleItem getItem(int position){
        return data.get(position);
    }

    public void add(int position, ScheduleItem mail){
        data.add(position, mail);
        notifyDataSetChanged();
    }

    public boolean onItemMove(int fromPosition, int toPosition) {
        updateGlobalData(data);
        return true;
    }

    private void updateGlobalData(ArrayList<com.example.des.hp.Schedule.ScheduleItem> items)
    {
        for (int i=0;i<items.size();i++)
        {
            items.get(i).sequenceNo=i+1;
        }
        if(!databaseAccess().updateScheduleItems(items))
            return;
        notifyDataSetChanged();
    }

    private int lastPosition = -1;

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return data.size();
    }

}
