package com.example.des.hp.Budget;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.R;
import com.example.des.hp.myutils.*;

import java.util.ArrayList;
import java.util.Collections;

/**
 ** Created by Des on 02/11/2016.
 */

public class BudgetDetailsList extends AppCompatActivity
{

    public DatabaseAccess databaseAccess;
    public ArrayList<BudgetItem> budgetList;
    public int holidayId;
    public BudgetAdapter budgetAdapter;
    public String title;
    public String subtitle;
    public ActionBar actionBar;
    public TextView budgetTotal;
    public TextView budgetPaid;
    public TextView budgetUnpaid;
    public MyMessages myMessages;

    public void showBudgetAdd(View view)
    {
        Intent intent = new Intent(getApplicationContext(), BudgetDetailsEdit.class);
        intent.putExtra("ACTION", "add");
        intent.putExtra("HOLIDAYID", holidayId);
        startActivity(intent);
    }

    public void showForm()
    {
        try {
            if (actionBar != null) {
                if (title.length() > 0) {
                    actionBar.setTitle(title);
                    actionBar.setSubtitle(subtitle);
                } else {
                    actionBar.setTitle("Budget");
                    actionBar.setSubtitle("Budget");
                }
            }

            budgetList = new ArrayList<>();
            if (!databaseAccess.getBudgetList(holidayId, budgetList))
                return;

            budgetTotal = (TextView) findViewById(R.id.totBudgetTotal);
            budgetPaid = (TextView) findViewById(R.id.totBudgetPaid);
            budgetUnpaid = (TextView) findViewById(R.id.totBudgetUnpaid);

            MyInt myInt = new MyInt();

            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.budgetListView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setHasFixedSize(true);
            //listView1.setDivider(null);
            budgetAdapter = new BudgetAdapter(this, budgetList);
            recyclerView.setAdapter(budgetAdapter);

            itemTouchHelper.attachToRecyclerView(recyclerView);

            if (!databaseAccess.getBudgetTotal(holidayId, myInt))
                return;
            budgetTotal.setText(StringUtils.IntToMoneyString(myInt.Value));

            if (!databaseAccess.getBudgetPaid(holidayId, myInt))
                return;
            budgetPaid.setText(StringUtils.IntToMoneyString(myInt.Value));

            if (!databaseAccess.getBudgetUnpaid(holidayId, myInt))
                return;
            budgetUnpaid.setText(StringUtils.IntToMoneyString(myInt.Value));


            budgetAdapter.setOnItemClickListener
                    (
                            new BudgetAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, BudgetItem obj, int position) {
                                    Intent intent = new Intent(getApplicationContext(), BudgetDetailsView.class);
                                    intent.putExtra("ACTION", "view");
                                    intent.putExtra("HOLIDAYID", budgetList.get(position).holidayId);
                                    intent.putExtra("BUDGETID", budgetList.get(position).budgetId);
                                    if (actionBar != null) {
                                        intent.putExtra("TITLE", actionBar.getTitle() + "/" +
                                                actionBar.getSubtitle());
                                        intent.putExtra("SUBTITLE", budgetList.get(position).budgetDescription);
                                    }
                                    startActivity(intent);
                                }
                            }
                    );
        }
        catch(Exception e)
        {
            ShowError("showForm", e.getMessage());
        }

    }

    // handle swipe to delete, and draggable
    ItemTouchHelper itemTouchHelper =
            new ItemTouchHelper
                    (
                            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT)
                            {
                                int dragFrom = -1;
                                int dragTo = -1;

                                @Override
                                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target)
                                {
                                    int fromPosition = viewHolder.getAdapterPosition();
                                    int toPosition = target.getAdapterPosition();


                                    if(dragFrom == -1)
                                    {
                                        dragFrom =  fromPosition;
                                    }
                                    dragTo = toPosition;

                                    if (fromPosition < toPosition)
                                    {
                                        for (int i = fromPosition; i < toPosition; i++)
                                        {
                                            Collections.swap(budgetAdapter.data, i, i + 1);
                                        }
                                    } else
                                    {
                                        for (int i = fromPosition; i > toPosition; i--)
                                        {
                                            Collections.swap(budgetAdapter.data, i, i - 1);
                                        }
                                    }
                                    budgetAdapter.notifyItemMoved(fromPosition, toPosition);

                                    return true;
                                }

                                @Override
                                public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder)
                                {
                                    int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                                    int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                                    return makeMovementFlags(dragFlags, swipeFlags);
                                }

                                @Override
                                public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction)
                                {
                                }

                                @Override
                                public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder)
                                {
                                    super.clearView(recyclerView, viewHolder);

                                    if(dragFrom != -1 && dragTo != -1 && dragFrom != dragTo)
                                    {
                                        budgetAdapter.onItemMove();
                                    }

                                    dragFrom = dragTo = -1;
                                }

                            }
                    );

    @Override
    protected void onResume(){
        super.onResume();

        try
        {
            showForm();
        }
        catch(Exception e)
        {
            ShowError("onResume", e.getMessage());
        }

    }

    private void ShowError(String argFunction, String argMessage)
    {
        myMessages.ShowError
                (
                        "Error in BudgetDetailsList::" + argFunction,
                        argMessage
                );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_list);

        databaseAccess = new DatabaseAccess(this);
        actionBar = getSupportActionBar();
        myMessages = new MyMessages(this);

        title = "";
        subtitle = "";
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            holidayId = extras.getInt("HOLIDAYID");
            title = extras.getString("TITLE");
            subtitle = extras.getString("SUBTITLE");
        }
        try
        {
            showForm();
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }

    }

}

