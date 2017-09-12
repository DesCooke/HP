package com.example.des.hp.Holiday;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.des.hp.myutils.*;
import com.example.des.hp.R;

import java.util.ArrayList;

import static com.example.des.hp.myutils.MyMessages.myMessages;

public class HolidayAdapter extends ArrayAdapter<HolidayItem>
{
    private Context context;
    private int layoutResourceId;
    private ArrayList<HolidayItem> data=null;
    private ImageUtils imageUtils;

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
            holder.holidayImage=(ImageView) row.findViewById(R.id.imgIcon);
            holder.txtTitle=(TextView) row.findViewById(R.id.txtTitle);
            holder.txtStartDate=(TextView) row.findViewById(R.id.txtStartDate);
            holder.txtToGo=(TextView) row.findViewById(R.id.txtToGo);

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


        if(holidayItem.holidayPicture.length() > 0)
            if(imageUtils.getLargeListIcon(context, holidayItem.holidayPicture, holder.holidayImage) == false)
                return (row);

        return row;
    }
}
