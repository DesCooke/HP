package com.example.des.hp.Budget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Holiday.HolidayItem;
import com.example.des.hp.myutils.*;
import com.example.des.hp.R;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.ViewHolder>
{
    private final Context context;
    public ArrayList<BudgetItem> data;
    private OnItemClickListener mOnItemClickListener;
    private final ImageUtils imageUtils;
    private static Bitmap imageTotal = null;
    
    interface OnItemClickListener
    {
        void onItemClick(View view, BudgetItem obj);
    }
    
    void setOnItemClickListener(final OnItemClickListener mItemClickListener)
    {
        this.mOnItemClickListener = mItemClickListener;
    }
    
    static class ViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        ImageView budgetImage;
        TextView txtBudgetDescription;
        LinearLayout budgetItemCell;
        TextView txtBudgetTotal;
        TextView txtBudgetPaid;
        TextView txtBudgetUnpaid;
        
        ViewHolder(View v)
        {
            super(v);
            
            budgetImage = v.findViewById(R.id.imgIcon);
            txtBudgetDescription = v.findViewById(R.id.budgetDescription);
            budgetItemCell = v.findViewById(R.id.budgetItemCell);
            txtBudgetTotal = v.findViewById(R.id.budgetTotal);
            txtBudgetPaid = v.findViewById(R.id.budgetPaid);
            txtBudgetUnpaid = v.findViewById(R.id.budgetUnpaid);
        }
    }
    
    // Provide a suitable constructor (depends on the kind of dataset)
    BudgetAdapter(Activity activity, ArrayList<BudgetItem> items)
    {
        this.context = activity;
        imageUtils = new ImageUtils(activity);
        data = items;
    }
    
    @NonNull
    @Override
    public BudgetAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.budgetlistitemrow, parent, false);
        
        // set the view's size, margins, padding and layout parameters
        return new ViewHolder(v);
    }
    
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        final BudgetItem c = data.get(position);

        int lColor=R.color.colorFullyPaid;
        if(!c.active){
            lColor=R.color.colorInactive;
        }
        else{
            if(c.budgetUnpaid<-0.0001 || c.budgetUnpaid>0.0001)
                lColor=R.color.colorPrimaryDark;
        }

        holder.txtBudgetDescription.setText(c.budgetDescription);
        holder.txtBudgetTotal.setText(StringUtils.IntToMoneyString(c.budgetTotal));
        holder.txtBudgetPaid.setText(StringUtils.IntToMoneyString(c.budgetPaid));
        holder.txtBudgetUnpaid.setText(StringUtils.IntToMoneyString(c.budgetUnpaid));

        holder.txtBudgetDescription.setTextColor(context.getColor(lColor));
        holder.txtBudgetTotal.setTextColor(context.getColor(lColor));
        holder.txtBudgetPaid.setTextColor(context.getColor(lColor));
        holder.txtBudgetUnpaid.setTextColor(context.getColor(lColor));

        holder.budgetItemCell.setBackgroundColor(ContextCompat.getColor(context, R.color.colorTransparent));
        holder.budgetImage.setVisibility(View.INVISIBLE);

        if (c.pictureAssigned)
        {
            holder.budgetImage.setVisibility(View.VISIBLE);
            try(DatabaseAccess da = databaseAccess()) {
                HolidayItem holidayItem = new HolidayItem();
                da.getHolidayItem(c.holidayId, holidayItem);

                if (!imageUtils.getListIcon(holidayItem.holidayId, context, c.budgetPicture, holder.budgetImage))
                    return;
            }
        } else
        {
            if (c.budgetDescription.equals("Total"))
            {
                if (imageTotal == null)
                    imageTotal = BitmapFactory.decodeResource(context.getResources(), R.drawable.sum);
                holder.budgetImage.setImageBitmap(imageTotal);
                holder.budgetItemCell.setBackgroundColor(ContextCompat.getColor(context, R.color.colorEasy));
                holder.budgetImage.setVisibility(View.VISIBLE);
            }
        }
        
        
        holder.budgetItemCell.setOnClickListener(view -> {
            if (mOnItemClickListener != null)
            {
                mOnItemClickListener.onItemClick(view, c);
            }
        });
    }
    
    
    public BudgetItem getItem(int position)
    {
        return data.get(position);
    }
    
    public void add(int position, BudgetItem mail)
    {
        data.add(position, mail);
        notifyItemInserted(position);
    }
    
    void onItemMove()
    {
        updateGlobalData(data);
    }
    
    private void updateGlobalData(ArrayList<BudgetItem> items)
    {
        for (int i = 0; i < items.size(); i++)
        {
            items.get(i).sequenceNo = i + 1;
        }
        try(DatabaseAccess da = databaseAccess())
        {
            if (!da.updateBudgetItems(items))
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
