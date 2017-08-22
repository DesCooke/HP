package com.example.des.hp.Tasks;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.R;
import com.example.des.hp.thirdpartyutils.BadgeView;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

public class TaskDetailsView extends BaseActivity
{
    
    //region Member variables
    public TextView txtTaskDate;
    public TaskItem taskItem;
    public LinearLayout grpTaskDate;
    public TextView txtTaskDescription;
    public CheckBox chkTaskComplete;
    public ImageButton btnShowInfo;
    public BadgeView btnShowInfoBadge;
    public ImageButton btnShowNotes;
    public ImageButton btnClear;
    public Button btnSave;
    public LinearLayout grpMenuFile;
    public LinearLayout grpTaskName;
    public TextView lblTaskDate;
    public TextView lblKnownDate;
    public LinearLayout grpKnownDate;
    public Switch swKnownDate;
    //endregion
    
    //region Constructors/Destructors
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            layoutName = "activity_task_details_view";
            setContentView(R.layout.activity_task_details_view);
            
            txtTaskDescription = (TextView) findViewById(R.id.txtTaskName);
            grpTaskDate = (LinearLayout) findViewById(R.id.grpTaskDate);
            txtTaskDate = (TextView) findViewById(R.id.txtTaskDate);
            chkTaskComplete = (CheckBox) findViewById(R.id.chkTaskComplete);
            btnClear=(ImageButton) findViewById(R.id.btnClear);
            btnSave=(Button) findViewById(R.id.btnSave);
            grpMenuFile=(LinearLayout) findViewById(R.id.grpMenuFile);
            grpTaskName=(LinearLayout) findViewById(R.id.grpTaskName);
            lblTaskDate=(TextView)findViewById(R.id.lblTaskDate);
            lblKnownDate=(TextView)findViewById(R.id.lblKnownDate);
            grpKnownDate=(LinearLayout) findViewById(R.id.grpKnownDate);
            swKnownDate=(Switch) findViewById(R.id.swKnownDate);
            
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
            inflater.inflate(R.menu.taskdetailsformmenu, menu);
        }
        catch (Exception e)
        {
            ShowError("onCreateOptionsMenu", e.getMessage());
        }
        
        return true;
    }
    
    //endregion
    
    //region OnClick Events
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        try
        {
            switch (item.getItemId())
            {
                case R.id.action_delete_task:
                    deleteTask();
                    return true;
                case R.id.action_edit_task:
                    editTask();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
        catch (Exception e)
        {
            ShowError("onOptionsItemSelected", e.getMessage());
        }
        return true;
        
    }
    //endregion
    
    //region showForm
    public void showForm()
    {
        super.showForm();
        try
        {
            taskItem = new TaskItem();
            if (!databaseAccess().getTaskItem(holidayId, taskId, taskItem))
                return;
            
            if (title == null || (title != null && title.length() == 0) )
            {
                SetTitles(taskItem.taskDescription, "");
            } else
            {
                SetTitles(title, subTitle);
            }
            
            SetImage(taskItem.taskPicture);
            
            txtTaskDescription.setText(taskItem.taskDescription);
            
            if (taskItem.taskDateKnown)
            {
                grpTaskDate.setVisibility(View.VISIBLE);
                txtTaskDate.setText(taskItem.taskDateString);
            } else
            {
                grpTaskDate.setVisibility(View.GONE);
            }
            
            chkTaskComplete.setChecked(taskItem.taskComplete);
            
            afterShow();
        }
        catch (Exception e)
        {
            ShowError("showForm", e.getMessage());
        }
    }
    
    //region form Functions
    @Override
    public int getInfoId()
    {
        return (taskItem.infoId);
    }

    public void setNoteId(int pNoteId)
    {
        taskItem.noteId=pNoteId;
        databaseAccess().updateTaskItem(taskItem);
    }

    @Override
    public int getNoteId()
    {
        return (taskItem.noteId);
    }

    @Override
    public void setInfoId(int pInfoId)
    {
        taskItem.infoId=pInfoId;
        databaseAccess().updateTaskItem(taskItem);
    }

    public void editTask()
    {
        try
        {
            Intent intent = new Intent(getApplicationContext(), TaskDetailsEdit.class);
            intent.putExtra("ACTION", "modify");
            intent.putExtra("HOLIDAYID", holidayId);
            intent.putExtra("TASKID", taskId);
            intent.putExtra("TITLE", title);
            intent.putExtra("SUBTITLE", subTitle);
            startActivity(intent);
        }
        catch (Exception e)
        {
            ShowError("editTask", e.getMessage());
        }
        
    }
    
    public void deleteTask()
    {
        try
        {
            if (!databaseAccess().deleteTaskItem(taskItem))
                return;
            finish();
        }
        catch (Exception e)
        {
            ShowError("deleteTask", e.getMessage());
        }
        
    }
    
}
