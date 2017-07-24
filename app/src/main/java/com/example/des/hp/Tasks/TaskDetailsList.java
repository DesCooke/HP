package com.example.des.hp.Tasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.R;
import com.example.des.hp.myutils.*;

import java.util.ArrayList;
import java.util.Collections;

/**
 ** Created by Des on 02/11/2016.
 */

public class TaskDetailsList extends BaseActivity
{

    public DatabaseAccess databaseAccess;
    public ArrayList<TaskItem> taskList;
    public int holidayId;
    public TaskAdapter taskAdapter;
    public String title;
    public String subtitle;
    public ActionBar actionBar;
    public MyMessages myMessages;

    public void showTaskAdd(View view)
    {
        Intent intent = new Intent(getApplicationContext(), TaskDetailsEdit.class);
        intent.putExtra("ACTION", "add");
        intent.putExtra("HOLIDAYID", holidayId);
        startActivity(intent);
    }

    public void showForm()
    {
        try {
            databaseAccess = new DatabaseAccess(this);
            actionBar = getSupportActionBar();
            if (actionBar != null) {
                if (title.length() > 0) {
                    actionBar.setTitle(title);
                    actionBar.setSubtitle(subtitle);
                } else {
                    actionBar.setTitle("Task");
                    actionBar.setSubtitle("Tasks");
                }
            }

            taskList = new ArrayList<TaskItem>();
            if (!databaseAccess.getTaskList(holidayId, taskList))
                return;

            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.taskListView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setHasFixedSize(false);
            taskAdapter = new TaskAdapter(this, taskList);
            recyclerView.setAdapter(taskAdapter);

            itemTouchHelper.attachToRecyclerView(recyclerView);

            taskAdapter.setOnItemClickListener
                    (
                            new TaskAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, TaskItem obj, int position) {
                                    Intent intent = new Intent(getApplicationContext(), TaskDetailsView.class);
                                    intent.putExtra("ACTION", "view");
                                    intent.putExtra("HOLIDAYID", taskList.get(position).holidayId);
                                    intent.putExtra("TASKID", taskList.get(position).taskId);
                                    if (actionBar != null) {
                                        intent.putExtra("TITLE", actionBar.getTitle() + "/" +
                                                actionBar.getSubtitle());
                                        intent.putExtra("SUBTITLE", taskList.get(position).taskDescription);
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

    // handle swipe to delete, and dragable
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
                                            Collections.swap(taskAdapter.data, i, i + 1);
                                        }
                                    } else
                                    {
                                        for (int i = fromPosition; i > toPosition; i--)
                                        {
                                            Collections.swap(taskAdapter.data, i, i - 1);
                                        }
                                    }
                                    taskAdapter.notifyItemMoved(fromPosition, toPosition);

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
                                        taskAdapter.onItemMove(dragFrom, dragTo);
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
                        "Error in TaskDetailsList::" + argFunction,
                        argMessage
                );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

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

