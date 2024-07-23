package com.example.des.hp.Tasks;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.R;
import com.example.des.hp.Schedule.GeneralAttraction.GeneralAttractionDetailsEdit;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

import androidx.annotation.NonNull;

public class TaskDetailsView extends BaseActivity
{

    //region Member variables
    public TextView txtTaskDate;
    public TaskItem taskItem;
    public LinearLayout grpTaskDate;
    public TextView txtTaskDescription;
    public CheckBox chkTaskComplete;
    public ImageButton btnClear;
    public Button btnSave;
    public LinearLayout grpMenuFile;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    public Switch swKnownDate;
    public TextView txtKnownDate;
    public ImageView btnEdit;
    //endregion

    //region Constructors/Destructors
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            layoutName="activity_task_details_view";
            setContentView(R.layout.activity_task_details_view);

            txtKnownDate=findViewById(R.id.txtKnownDate);
            txtTaskDescription= findViewById(R.id.txtTaskDescription);
            txtTaskDate= findViewById(R.id.txtTaskDate);
            chkTaskComplete= findViewById(R.id.chkTaskComplete);
            btnClear= findViewById(R.id.btnClear);
            btnSave= findViewById(R.id.btnSave);
            grpMenuFile= findViewById(R.id.grpMenuFile);
            swKnownDate= findViewById(R.id.swKnownDate);
            grpTaskDate=findViewById(R.id.grpTaskDate);

            btnEdit=findViewById(R.id.my_toolbar_edit);
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editTask();
                }
            });

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
            inflater.inflate(R.menu.taskdetailsformmenu, menu);
        }
        catch(Exception e)
        {
            ShowError("onCreateOptionsMenu", e.getMessage());
        }

        return true;
    }
    //endregion

    //region OnClick Events
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        try {
            int id = item.getItemId();
            if (id == R.id.action_delete_task)
                deleteTask();
            if (id == R.id.action_edit_task)
                editTask();
        }
        catch(Exception e)
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
            taskItem=new TaskItem();
            try(DatabaseAccess da = databaseAccess())
            {
                if(!da.getTaskItem(holidayId, taskId, taskItem))
                    return;
            }

            if(title == null || (title.isEmpty()))
            {
                SetToolbarTitles(taskItem.taskDescription, "");
            } else
            {
                SetToolbarTitles(title, subTitle);
            }

            SetImage(taskItem.taskPicture);

            txtTaskDescription.setText(taskItem.taskDescription);

            if(action.compareTo("view")==0)
                ShowToolbarEdit();

            if(taskItem.taskDateKnown)
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
        catch(Exception e)
        {
            ShowError("showForm", e.getMessage());
        }
    }
    //endregion

    //region form Functions
    @Override
    public int getInfoId()
    {
        try
        {
            return (taskItem.infoId);
        }
        catch(Exception e)
        {
            ShowError("getInfoId", e.getMessage());
        }
        return (0);

    }

    public void setNoteId(int pNoteId)
    {
        try
        {
            taskItem.noteId=pNoteId;
            try(DatabaseAccess da = databaseAccess())
            {
                da.updateTaskItem(taskItem);
            }
        }
        catch(Exception e)
        {
            ShowError("setNoteId", e.getMessage());
        }

    }

    @Override
    public int getNoteId()
    {
        try
        {
            return (taskItem.noteId);
        }
        catch(Exception e)
        {
            ShowError("getNoteId", e.getMessage());
        }
        return (0);
    }

    @Override
    public void setInfoId(int pInfoId)
    {
        try
        {
            taskItem.infoId=pInfoId;
            try(DatabaseAccess da = databaseAccess())
            {
                da.updateTaskItem(taskItem);
            }
        }
        catch(Exception e)
        {
            ShowError("setInfoId", e.getMessage());
        }

    }

    public void editTask()
    {
        try
        {
            Intent intent=new Intent(getApplicationContext(), TaskDetailsEdit.class);
            intent.putExtra("ACTION", "modify");
            intent.putExtra("HOLIDAYID", holidayId);
            intent.putExtra("TASKID", taskId);
            intent.putExtra("TITLE", title);
            intent.putExtra("SUBTITLE", subTitle);
            startActivity(intent);
        }
        catch(Exception e)
        {
            ShowError("editTask", e.getMessage());
        }

    }

    public void deleteTask()
    {
        try
        {
            try(DatabaseAccess da = databaseAccess())
            {
                if(!da.deleteTaskItem(taskItem))
                    return;
            }
            finish();
        }
        catch(Exception e)
        {
            ShowError("deleteTask", e.getMessage());
        }

    }

    protected void onResume()
    {
        super.onResume();
        try
        {
            try(DatabaseAccess da = databaseAccess();)
            {
                if(!da.getTaskItem(holidayId, taskId, taskItem))
                    return;
                if(action.compareTo("add")!=0)
                    if(taskItem.taskId==0)
                        finish();
            }
        }
        catch(Exception e)
        {
            ShowError("onResume", e.getMessage());
        }

    }

    //endregion

}
