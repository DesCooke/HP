package com.example.des.hp.Budget;

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

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.myutils.*;
import com.example.des.hp.R;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

/**
 ** Created by Des on 06/10/2016.
 */

class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.ViewHolder>
{
    private Context context;
    public ArrayList<BudgetItem> data = null;
    private OnItemClickListener mOnItemClickListener;
    private ImageUtils imageUtils;


    interface OnItemClickListener {
        void onItemClick(View view, BudgetItem obj, int position);
    }

    void setOnItemClickListener(final OnItemClickListener mItemClickListener)
    {
        this.mOnItemClickListener = mItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder
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

            budgetImage = (ImageView) v.findViewById(R.id.imgIcon);
            txtBudgetDescription = (TextView) v.findViewById(R.id.budgetDescription);
            budgetItemCell = (LinearLayout) v.findViewById(R.id.budgetItemCell);
            txtBudgetTotal = (TextView) v.findViewById(R.id.budgetTotal);
            txtBudgetPaid = (TextView) v.findViewById(R.id.budgetPaid);
            txtBudgetUnpaid = (TextView) v.findViewById(R.id.budgetUnpaid);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    BudgetAdapter(Activity activity, ArrayList<BudgetItem> items) {
        this.context = activity;
        imageUtils = new ImageUtils(activity);
        data = items;
    }

    @Override
    public BudgetAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.budgetlistitemrow, parent, false);

        // set the view's size, margins, padding and layout parameters
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final BudgetItem c = data.get(position);
        holder.txtBudgetDescription.setText(c.budgetDescription);
        holder.txtBudgetTotal.setText(StringUtils.IntToMoneyString(c.budgetTotal));
        holder.txtBudgetPaid.setText(StringUtils.IntToMoneyString(c.budgetPaid));
        holder.txtBudgetUnpaid.setText(StringUtils.IntToMoneyString(c.budgetUnpaid));

        if(c.pictureAssigned)
        {
            holder.budgetImage.setVisibility(View.VISIBLE);
            if(!imageUtils.getListIcon(context, c.budgetPicture, holder.budgetImage))
                return;
        }
        else
        {
            holder.budgetImage.setVisibility(View.INVISIBLE);
        }


        holder.budgetItemCell.setOnClickListener(new View.OnClickListener()
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


    public BudgetItem getItem(int position){
        return data.get(position);
    }

    public void add(int position, BudgetItem mail){
        data.add(position, mail);
        notifyDataSetChanged();
    }

    boolean onItemMove() {
        updateGlobalData(data);
        return true;
    }

    private void updateGlobalData(ArrayList<BudgetItem> items)
    {
        for (int i=0;i<items.size();i++)
        {
            items.get(i).sequenceNo=i+1;
        }
        if(!databaseAccess().updateBudgetItems(items))
            return;
        notifyDataSetChanged();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return data.size();
    }

}
