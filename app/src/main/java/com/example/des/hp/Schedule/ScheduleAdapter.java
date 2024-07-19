package com.example.des.hp.Schedule;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Schedule.GeneralAttraction.GeneralAttractionItem;
import com.example.des.hp.myutils.*;
import com.example.des.hp.R;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

/**
 * * Created by Des on 06/10/2016.
 * @noinspection ClassEscapesDefinedScope
 */

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder>
{
    private final Context context;
    public ArrayList<ScheduleItem> data;
    private OnItemClickListener mOnItemClickListener;
    private final ImageUtils imageUtils;


    public interface OnItemClickListener
    {
        void onItemClick(View view, ScheduleItem obj);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener)
    {
        this.mOnItemClickListener=mItemClickListener;
    }


    static class ViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        ImageView scheduleImage;
        TextView txtSchedName;
        TextView txtTimeRange;
        LinearLayout scheduleItemCell;
        TextView txtReservationType;
        RatingBar heartRatingView;
        RatingBar scenicRatingView;
        RatingBar thrillRatingView;

        ViewHolder(View v)
        {
            super(v);

            scheduleImage= v.findViewById(R.id.imgSchedule);
            txtSchedName= v.findViewById(R.id.txtSchedName);
            txtTimeRange= v.findViewById(R.id.txtTimeRange);
            scheduleItemCell= v.findViewById(R.id.scheduleItemCell);
            scenicRatingView= v.findViewById(R.id.rbScenicRatingView);
            heartRatingView= v.findViewById(R.id.rbHeartRatingView);
            thrillRatingView= v.findViewById(R.id.rbThrillRatingView);
            txtReservationType= v.findViewById(R.id.txtReservationType);
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ScheduleAdapter(Activity activity, ArrayList<ScheduleItem> items)
    {
        this.context=activity;
        imageUtils=new ImageUtils(activity);
        data=items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // create a new view
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.schedulelistitemrow, parent, false);

        // set the view's size, margins, padding and layout parameters
        return new ViewHolder(v);
    }

    private String formatTime(int hour, int minute)
    {
        String lTime;
        lTime="";
        if(hour < 10)
            lTime="0";
        lTime=lTime + hour;
        lTime=lTime + ":";
        if(minute < 10)
            lTime=lTime + "0";
        lTime=lTime + minute;
        return (lTime);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        final ScheduleItem c=data.get(position);

        holder.txtSchedName.setText("");
        holder.txtReservationType.setText("");
        holder.txtTimeRange.setText("");

        holder.scenicRatingView.setVisibility(View.VISIBLE);
        holder.heartRatingView.setVisibility(View.VISIBLE);
        holder.thrillRatingView.setVisibility(View.VISIBLE);
        holder.txtReservationType.setVisibility(View.VISIBLE);
        holder.scheduleImage.setVisibility(View.VISIBLE);

        if(c.generalAttractionItem != null)
        {
            GeneralAttractionItem g = c.generalAttractionItem;

            if(!g.AttractionType.isEmpty())
            {
                holder.txtSchedName.setText(String.format("%s (%s)", c.schedName, g.AttractionType));
            }
            else
            {
                holder.txtSchedName.setText(c.schedName);
            }

            holder.scenicRatingView.setRating(g.scenicRating);
            holder.heartRatingView.setRating(g.heartRating);
            holder.thrillRatingView.setRating(g.thrillRating);

            String lString;
            if(g.CheckInKnown && g.DepartsKnown && g.ArrivalKnown)
            {
                lString=formatTime(g.CheckInHour, g.CheckInMin) + " -> " +
                        formatTime(g.DepartsHour, g.DepartsMin) + " -> " +
                        formatTime(g.ArrivalHour, g.ArrivalMin) ;
                holder.txtTimeRange.setText(lString);
            }
            else
            {
                if(g.CheckInKnown && g.DepartsKnown)
                {
                    lString=formatTime(g.CheckInHour, g.CheckInMin) + " -> " +
                            formatTime(g.DepartsHour, g.DepartsMin) + " -> ";
                    holder.txtTimeRange.setText(lString);
                }
                else {
                    if(!g.CheckInKnown && !g.DepartsKnown && g.ArrivalKnown)
                    {
                        lString= " -> " + formatTime(g.ArrivalHour, g.ArrivalMin);
                        holder.txtTimeRange.setText(lString);
                    }
                    else {
                        if (g.PickUpKnown && g.DropOffKnown) {
                            lString = formatTime(g.PickUpHour, g.PickUpMin) + " -> " +
                                    formatTime(g.DropOffHour, g.DropOffMin);
                            holder.txtTimeRange.setText(lString);
                        } else {
                            if (g.DepartsKnown && g.ArrivalKnown) {
                                lString = formatTime(g.DepartsHour, g.DepartsMin) + " -> " +
                                        formatTime(g.ArrivalHour, g.ArrivalMin);
                                holder.txtTimeRange.setText(lString);
                            } else {
                                if (g.ShowKnown) {
                                    lString = formatTime(g.ShowHour, g.ShowMin);
                                    holder.txtTimeRange.setText(lString);
                                } else {
                                    if (g.CheckInKnown) {
                                        lString = formatTime(g.CheckInHour, g.CheckInMin) + " -> ";
                                        holder.txtTimeRange.setText(lString);
                                    } else {
                                        if (g.DepartsKnown) {
                                            lString = " -> " + formatTime(g.DepartsHour, g.DepartsMin);
                                            holder.txtTimeRange.setText(lString);
                                        } else {
                                            if (c.startTimeKnown && c.endTimeKnown) {
                                                lString = formatTime(c.startHour, c.startMin) + " -> " +
                                                        formatTime(c.endHour, c.endMin);
                                                holder.txtTimeRange.setText(lString);
                                            } else {
                                                if (c.startTimeKnown) {
                                                    lString = formatTime(c.startHour, c.startMin);
                                                    holder.txtTimeRange.setText(lString);
                                                } else {
                                                    if (c.endTimeKnown) {
                                                        lString = formatTime(c.endHour, c.endMin);
                                                        holder.txtTimeRange.setText(lString);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if(c.generalAttractionItem.scenicRating<0.25)
                holder.scenicRatingView.setVisibility(View.GONE);
            if(c.generalAttractionItem.heartRating<0.25)
                holder.heartRatingView.setVisibility(View.GONE);
            if(c.generalAttractionItem.thrillRating<0.25)
                holder.thrillRatingView.setVisibility(View.GONE);

            holder.txtReservationType.setText("");
            if(c.generalAttractionItem.ReservationType == 1)
            {
                holder.txtReservationType.setVisibility(View.VISIBLE);
                holder.txtReservationType.setText(R.string.ReservationTypeJustWalkIn);
            }
            if(c.generalAttractionItem.ReservationType == 2)
            {
                holder.txtReservationType.setVisibility(View.VISIBLE);
                holder.txtReservationType.setText(R.string.ReservationTypeReserveOnTheDay);
            }
            if(c.generalAttractionItem.ReservationType == 3)
            {
                holder.txtReservationType.setVisibility(View.VISIBLE);
                holder.txtReservationType.setText(R.string.ReservationTypeReserve180DaysInAdvance);
            }
            if(c.generalAttractionItem.ReservationType == 4)
            {
                holder.txtReservationType.setVisibility(View.VISIBLE);
                holder.txtReservationType.setText(R.string.ReservationTypeBooked);
            }

        }
        else
        {
            holder.txtSchedName.setText(c.schedName);

            String lString;
            lString="";
            if(c.startTimeKnown)
                lString=lString + formatTime(c.startHour, c.startMin);
            if(c.startTimeKnown || c.endTimeKnown)
                lString=lString + " - ";
            if(c.endTimeKnown)
                lString=lString + formatTime(c.endHour, c.endMin);
            holder.txtTimeRange.setText(lString);

            holder.txtReservationType.setVisibility(View.GONE);

                    holder.scenicRatingView.setVisibility(View.GONE);
                    holder.heartRatingView.setVisibility(View.GONE);
                    holder.thrillRatingView.setVisibility(View.GONE);
        }

        if(!c.schedPicture.isEmpty()) {
            if (!imageUtils.getListIcon(c.holidayId, context, c.schedPicture, holder.scheduleImage))
                return;
            holder.scheduleImage.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.scheduleImage.setVisibility(View.GONE);
        }

        holder.scheduleItemCell.setOnClickListener(view -> {
            if(mOnItemClickListener != null)
            {
                mOnItemClickListener.onItemClick(view, c);
            }
        });

    }


    public ScheduleItem getItem(int position)
    {
        return data.get(position);
    }

    public void add(int position, ScheduleItem mail)
    {
        data.add(position, mail);
        notifyItemInserted(position);
    }

    public boolean onItemMove()
    {
        updateGlobalData(data);
        return true;
    }

    private void updateGlobalData(ArrayList<com.example.des.hp.Schedule.ScheduleItem> items)
    {
        for(int i=0; i < items.size(); i++)
        {
            items.get(i).sequenceNo=i + 1;
        }
        try(DatabaseAccess da = databaseAccess())
        {
            if(!da.updateScheduleItems(items))
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
