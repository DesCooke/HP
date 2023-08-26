package com.example.des.hp.Budget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import com.example.des.hp.myutils.*;
import com.example.des.hp.R;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

class BudgetOptionAdapter extends RecyclerView.Adapter<BudgetOptionAdapter.ViewHolder>
{
    private Context context;
    public ArrayList<BudgetOptionItem> data = null;
    private OnItemClickListener mOnRecordSelect;
    private OnItemClickListener mOnEdit;
    private OnItemClickListener mOnDelete;
    private ImageUtils imageUtils;
    private static Bitmap imageTotal = null;

    interface OnItemClickListener
    {
        void onItemClick(View view, BudgetOptionItem obj);
    }

    void setOnRecordSelect(final OnItemClickListener mItemClickListener)
    {
        this.mOnRecordSelect = mItemClickListener;
    }

    void setOnEdit(final OnItemClickListener mItemClickListener)
    {
        this.mOnEdit = mItemClickListener;
    }

    void setOnDelete(final OnItemClickListener mItemClickListener)
    {
        this.mOnDelete = mItemClickListener;
    }
    class ViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        TextView txtOptionDescription;
        TextView txtOptionTotal;
        LinearLayout budgetOptionItemCell;
        ImageButton btnEdit;
        ImageButton btnDelete;


        ViewHolder(View v)
        {
            super(v);

            txtOptionDescription = v.findViewById(R.id.optionDescription);
            txtOptionTotal = v.findViewById(R.id.optionTotal);
            budgetOptionItemCell = v.findViewById(R.id.budgetOptionItemCell2);
            btnEdit = v.findViewById(R.id.btnEdit);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    BudgetOptionAdapter(Activity activity, ArrayList<BudgetOptionItem> items)
    {
        this.context = activity;
        data = items;
    }

    @Override
    public BudgetOptionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.budgetoptionlistitemrow, parent, false);

        // set the view's size, margins, padding and layout parameters
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        final BudgetOptionItem c = data.get(position);
        holder.txtOptionDescription.setText("(" + StringUtils.IntToString(c.sequenceNo) + ") " + c.optionDescription);
        holder.txtOptionTotal.setText(StringUtils.IntToMoneyString(c.optionTotal));

        holder.budgetOptionItemCell.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (mOnRecordSelect != null)
                {
                    mOnRecordSelect.onItemClick(view, c);
                }
            }
        });
        holder.btnEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (mOnEdit != null)
                {
                    mOnEdit.onItemClick(view, c);
                }
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (mOnDelete != null)
                {
                    mOnDelete.onItemClick(view, c);
                }
            }
        });
    }


    public BudgetOptionItem getItem(int position)
    {
        return data.get(position);
    }

    public void add(int position, BudgetOptionItem mail)
    {
        data.add(position, mail);
        notifyDataSetChanged();
    }

    boolean onItemMove()
    {
        updateGlobalData(data);
        return true;
    }

    private void updateGlobalData(ArrayList<BudgetOptionItem> items)
    {
        for (int i = 0; i < items.size(); i++)
        {
            items.get(i).sequenceNo = i + 1;
        }
        if (!databaseAccess().updateBudgetOptionItems(items))
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
