package com.example.des.hp.Tasks;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

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
            layoutName = "activity_task_list";
            setContentView(R.layout.activity_task_list);
            
            afterCreate();
            
            showForm();
        }
        catch (Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
        
    }
    
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try
        {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.task_list_add, menu);
        }
        catch (Exception e)
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
            Intent intent = new Intent(getApplicationContext(), TaskDetailsEdit.class);
            intent.putExtra("ACTION", "add");
            intent.putExtra("HOLIDAYID", holidayId);
            startActivity(intent);
        }
        catch (Exception e)
        {
            ShowError("showTaskAdd", e.getMessage());
        }
        
    }
    
    public void showForm()
    {
        try
        {
            allowCellMove=true;

            if (title.length() == 0)
                SetTitles("Task", "Tasks");
            
            taskList = new ArrayList<>();
            if (!databaseAccess().getTaskList(holidayId, taskList))
                return;
            taskAdapter = new TaskAdapter(this, taskList);

            CreateRecyclerView(R.id.taskListView, taskAdapter);
            
            taskAdapter.setOnItemClickListener(new TaskAdapter.OnItemClickListener()
            {
                @Override
                public void onItemClick(View view, TaskItem obj, int position)
                {
                    Intent intent = new Intent(getApplicationContext(), TaskDetailsView.class);
                    intent.putExtra("ACTION", "view");
                    intent.putExtra("HOLIDAYID", taskList.get(position).holidayId);
                    intent.putExtra("TASKID", taskList.get(position).taskId);
                    intent.putExtra("TITLE", title + "/" + subTitle);
                    intent.putExtra("SUBTITLE", taskList.get(position).taskDescription);
                    startActivity(intent);
                }
            });


            afterShow();
        }
        catch (Exception e)
        {
            ShowError("showForm", e.getMessage());
        }
        
    }
    
    @Override
    public void SwapItems(int from, int to)
    {
        Collections.swap(taskAdapter.data, from, to);
    }

    @Override
    public void OnItemMove(int from, int to)
    {
        taskAdapter.onItemMove(from, to);
    }

    @Override
    public void NotifyItemMoved(int from, int to)
    {
        taskAdapter.notifyItemMoved(from, to);
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
                case R.id.action_add_day:
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

