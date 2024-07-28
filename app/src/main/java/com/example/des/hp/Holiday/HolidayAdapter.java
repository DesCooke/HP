package com.example.des.hp.Holiday;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.des.hp.myutils.*;
import com.example.des.hp.R;

import java.util.ArrayList;

public class HolidayAdapter extends ArrayAdapter<HolidayItem>
{
    private final Context context;
    private final int layoutResourceId;
    private final ArrayList<HolidayItem> data;
    private final ImageUtils imageUtils;

    public HolidayAdapter(Context context, int layoutResourceId, ArrayList<HolidayItem> data)
    {
        super(context, layoutResourceId, data);
        this.layoutResourceId=layoutResourceId;
        this.context=context;
        this.data=data;
        imageUtils=new ImageUtils(context);
    }

    @Override
    public
    @NonNull
    View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View row=convertView;
        HolidayHolder holder;

        if(row == null)
        {
            LayoutInflater inflater=((Activity) context).getLayoutInflater();
            row=inflater.inflate(layoutResourceId, parent, false);

            holder=new HolidayHolder();
            holder.holidayImage= row.findViewById(R.id.imgIcon);
            holder.txtTitle= row.findViewById(R.id.txtTitle);
            holder.txtStartDate= row.findViewById(R.id.txtStartDate);
            holder.txtToGo= row.findViewById(R.id.txtToGo);

            row.setTag(holder);
        } else
        {
            holder=(HolidayHolder) row.getTag();
        }

        HolidayItem holidayItem=data.get(position);
        holder.txtTitle.setText(holidayItem.holidayName);

        if(!holidayItem.dateKnown)
        {
            holder.txtStartDate.setVisibility(View.INVISIBLE);
            holder.txtToGo.setVisibility(View.INVISIBLE);
            holder.txtToGo.setText("");
        } else
        {
            holder.txtStartDate.setVisibility(View.VISIBLE);
            holder.txtStartDate.setText(holidayItem.startDateStr);
            holder.txtToGo.setVisibility(View.VISIBLE);
            holder.txtToGo.setText(holidayItem.ToGo);
        }


        if(!holidayItem.holidayPicture.isEmpty())
            if(!imageUtils.getLargeListIcon(holidayItem.holidayName, context, holidayItem.holidayPicture, holder.holidayImage))
                return (row);

        return row;
    }
}
