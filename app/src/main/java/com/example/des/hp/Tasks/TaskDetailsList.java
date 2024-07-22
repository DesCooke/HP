package com.example.des.hp.Tasks;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.R;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

public class TaskDetailsList extends BaseActivity
{

    //region Member Variables
    public ArrayList<TaskItem> taskList;
    public TaskAdapter taskAdapter;
    //endregion

    //region Constructors/Destructors
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            layoutName="activity_task_list";
            setContentView(R.layout.activity_task_list);

            afterCreate();

            showForm();
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }

    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        try
        {
            MenuInflater inflater=getMenuInflater();
            inflater.inflate(R.menu.task_list_add, menu);
        }
        catch(Exception e)
        {
            ShowError("onCreateOptionsMenu", e.getMessage());
        }

        return true;
    }
    //endregion

    //region Form Functions
    public void showTaskAdd(View view)
    {
        try
        {
            Intent intent=new Intent(getApplicationContext(), TaskDetailsEdit.class);
            intent.putExtra("ACTION", "add");
            intent.putExtra("HOLIDAYID", holidayId);
            intent.putExtra("TITLE", title);
            intent.putExtra("SUBTITLE", "Add a Task");
            startActivity(intent);
        }
        catch(Exception e)
        {
            ShowError("showTaskAdd", e.getMessage());
        }

    }

    public void showForm()
    {
        try
        {
            allowCellMove=true;

            if(title.isEmpty())
                SetToolbarTitles("Task", "Tasks");

            taskList=new ArrayList<>();
            try(DatabaseAccess da = databaseAccess();)
            {
                if(!da.getTaskList(holidayId, taskList))
                    return;
            }

            taskAdapter=new TaskAdapter(this, taskList);

            CreateRecyclerView(R.id.taskListView, taskAdapter);

            taskAdapter.setOnItemClickListener(new TaskAdapter.OnItemClickListener()
            {
                @Override
                public void onItemClick(View view, TaskItem obj)
                {
                    Intent intent=new Intent(getApplicationContext(), TaskDetailsView.class);
                    intent.putExtra("ACTION", "view");
                    intent.putExtra("HOLIDAYID", obj.holidayId);
                    intent.putExtra("TASKID", obj.taskId);
                    intent.putExtra("TITLE", title + "/" + subTitle);
                    intent.putExtra("SUBTITLE", obj.taskDescription);
                    startActivity(intent);
                }
            });


            afterShow();
        }
        catch(Exception e)
        {
            ShowError("showForm", e.getMessage());
        }

    }

    @Override
    public void SwapItems(int from, int to)
    {
        try
        {
            Collections.swap(taskAdapter.data, from, to);
        }
        catch(Exception e)
        {
            ShowError("SwapItems", e.getMessage());
        }

    }

    @Override
    public void OnItemMove(int from, int to)
    {
        try
        {
            taskAdapter.onItemMove();
        }
        catch(Exception e)
        {
            ShowError("OnItemMove", e.getMessage());
        }

    }

    @Override
    public void NotifyItemMoved(int from, int to)
    {
        try
        {
            taskAdapter.notifyItemMoved(from, to);
        }
        catch(Exception e)
        {
            ShowError("NotifyItemMoved", e.getMessage());
        }

    }

    //endregion

    //region OnClick Events
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        try
        {
            switch(item.getItemId())
            {
                case R.id.action_add_task:
                    showTaskAdd(null);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
        catch(Exception e)
        {
            ShowError("onOptionsItemSelected", e.getMessage());
        }
        return true;
    }
    //endregion


}

