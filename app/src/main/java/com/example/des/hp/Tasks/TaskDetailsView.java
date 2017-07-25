package com.example.des.hp.Tasks;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.ExtraFiles.ExtraFilesDetailsList;
import com.example.des.hp.R;
import com.example.des.hp.myutils.*;
import com.example.des.hp.thirdpartyutils.BadgeView;
import com.example.des.hp.Notes.NoteItem;
import com.example.des.hp.Notes.NoteView;

public class TaskDetailsView extends BaseActivity
{

    public DatabaseAccess databaseAccess;
    private ImageView imageView;
    private TextView txtTaskDate;
    public int holidayId;
    public int taskId;
    public TaskItem taskItem;
    public LinearLayout grpTaskDate;
    private ImageUtils imageUtils;
    public TextView txtTaskDescription;
    public ActionBar actionBar;
    public CheckBox chkTaskComplete;
    public TextView txtTaskNotes;
    public MyMessages myMessages;
    public ImageButton btnShowInfo;
    public BadgeView btnShowInfoBadge;
    public MyColor myColor;
    public ImageButton btnShowNotes;


    public void clearImage(View view)
    {
        try
        {
            imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.imagemissing));
        }
        catch(Exception e)
        {
            ShowError("clearImage", e.getMessage());
        }

    }

    public void showNotes(View view)
    {
        try
        {
            Intent intent2=new Intent(getApplicationContext(), NoteView.class);
            if(taskItem.noteId == 0)
            {
                MyInt myInt=new MyInt();
                if(!databaseAccess.getNextNoteId(holidayId, myInt))
                    return;
                taskItem.noteId=myInt.Value;
                if(!databaseAccess.updateTaskItem(taskItem))
                    return;
            }
            intent2.putExtra("ACTION", "view");
            intent2.putExtra("HOLIDAYID", taskItem.holidayId);
            intent2.putExtra("NOTEID", taskItem.noteId);
            intent2.putExtra("TITLE", taskItem.taskDescription);
            intent2.putExtra("SUBTITLE", "Notes");
            startActivity(intent2);
        }
        catch(Exception e)
        {
            ShowError("showNotes", e.getMessage());
        }

    }

    public void showForm()
    {
        try
        {
            databaseAccess=new DatabaseAccess(this);

            clearImage(null);

            Bundle extras=getIntent().getExtras();
            if(extras != null)
            {
                String action=extras.getString("ACTION");
                if(action != null && action.equals("view"))
                {
                    holidayId=extras.getInt("HOLIDAYID");
                    taskId=extras.getInt("TASKID");
                    taskItem=new TaskItem();
                    if(!databaseAccess.getTaskItem(holidayId, taskId, taskItem))
                        return;

                    actionBar=getSupportActionBar();
                    if(actionBar != null)
                    {
                        String title=extras.getString("TITLE");
                        String subtitle=extras.getString("SUBTITLE");
                        if(title.length() > 0)
                        {
                            actionBar.setTitle(title);
                            actionBar.setSubtitle(subtitle);
                        } else
                        {
                            actionBar.setTitle(taskItem.taskDescription);
                            actionBar.setSubtitle("");
                        }
                    }

                    if(imageUtils.getPageHeaderImage(this, taskItem.taskPicture, imageView) == false)
                        return;
                    txtTaskDescription.setText(taskItem.taskDescription);

                    if(taskItem.taskDateKnown)
                    {
                        grpTaskDate.setVisibility(View.VISIBLE);
                        txtTaskDate.setText(taskItem.taskDateString);
                    } else
                    {
                        grpTaskDate.setVisibility(View.INVISIBLE);
                    }

                    chkTaskComplete.setChecked(taskItem.taskComplete);
                    txtTaskNotes.setText(taskItem.taskNotes);

                    MyInt lFileCount=new MyInt();
                    lFileCount.Value=0;
                    if(taskItem.infoId > 0)
                    {
                        if(!databaseAccess.getExtraFilesCount(taskItem.infoId, lFileCount))
                            return;
                    }
                    btnShowInfoBadge.setText(Integer.toString(lFileCount.Value));

                    if(lFileCount.Value == 0)
                    {
                        btnShowInfoBadge.hide();
                        if(myColor.SetImageButtonTint(btnShowInfo, R.color.colorDisabled) == false)
                            return;
                    } else
                    {
                        btnShowInfoBadge.show();
                        if(myColor.SetImageButtonTint(btnShowInfo, R.color.colorEnabled) == false)
                            return;
                    }

                    NoteItem noteItem=new NoteItem();
                    if(!databaseAccess.getNoteItem(taskItem.holidayId, taskItem.noteId, noteItem))
                        return;
                    if(noteItem.notes.length() == 0)
                    {
                        if(myColor.SetImageButtonTint(btnShowNotes, R.color.colorDisabled) == false)
                            return;
                    } else
                    {
                        if(myColor.SetImageButtonTint(btnShowNotes, R.color.colorEnabled) == false)
                            return;
                    }
                }
            }
        }
        catch(Exception e)
        {
            ShowError("showForm", e.getMessage());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            setContentView(R.layout.activity_task_details_view);

            imageUtils=new ImageUtils(this);
            myMessages=new MyMessages(this);
            myColor=new MyColor(this);

            imageView=(ImageView) findViewById(R.id.imageViewSmall);
            txtTaskDescription=(TextView) findViewById(R.id.txtTaskName);
            grpTaskDate=(LinearLayout) findViewById(R.id.grpTaskDate);
            txtTaskDate=(TextView) findViewById(R.id.txtTaskDate);
            chkTaskComplete=(CheckBox) findViewById(R.id.chkTaskComplete);
            txtTaskNotes=(TextView) findViewById(R.id.txtTaskNotes);
            btnShowInfo=(ImageButton) findViewById(R.id.btnShowInfo);
            btnShowNotes=(ImageButton) findViewById(R.id.btnShowNotes);

            btnShowInfoBadge=new BadgeView(this, btnShowInfo);
            btnShowInfoBadge.setText(Integer.toString(0));
            btnShowInfoBadge.show();

            showForm();
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
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
            intent.putExtra("TITLE", actionBar.getTitle());
            intent.putExtra("SUBTITLE", actionBar.getSubtitle());
            startActivity(intent);
        }
        catch(Exception e)
        {
            ShowError("editTask", e.getMessage());
        }

    }

    public void showInfo(View view)
    {
        try
        {
            Intent intent2=new Intent(getApplicationContext(), ExtraFilesDetailsList.class);
            if(taskItem.infoId == 0)
            {
                MyInt myInt=new MyInt();
                if(!databaseAccess.getNextFileGroupId(myInt))
                    return;
                taskItem.infoId=myInt.Value;
                if(!databaseAccess.updateTaskItem(taskItem))
                    return;
            }
            intent2.putExtra("FILEGROUPID", taskItem.infoId);
            intent2.putExtra("TITLE", taskItem.taskDescription);
            intent2.putExtra("SUBTITLE", "Info");
            startActivity(intent2);
        }
        catch(Exception e)
        {
            ShowError("showInfo", e.getMessage());
        }

    }


    public void deleteTask()
    {
        try
        {
            if(!databaseAccess.deleteTaskItem(taskItem))
                return;
            finish();
        }
        catch(Exception e)
        {
            ShowError("deleteTask", e.getMessage());
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

    @Override
    protected void onResume()
    {
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        try
        {
            switch(item.getItemId())
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
        catch(Exception e)
        {
            ShowError("onOptionsItemSelected", e.getMessage());
        }
        return true;

    }
}
