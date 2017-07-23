package com.example.des.hp.Tasks;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.myutils.*;
import com.example.des.hp.R;

import java.io.InputStream;
import java.util.Date;

public class TaskDetailsEdit extends AppCompatActivity {

    public DatabaseAccess databaseAccess;
    private final int SELECT_PHOTO = 1;
    private ImageView imageViewSmall;
    private String action;
    public int holidayId;
    public int taskId;
    public TextView taskDescription;
    public TextView taskDate;
    public ActionBar actionBar;
    public Switch sw;
    public TaskItem taskItem;
    public CheckBox cbPicturePicked;
    private ImageUtils imageUtils;
    public DateUtils dateUtils;
    public View.OnClickListener dwetOnOkClick;
    public DialogWithEditTextFragment dialogWithEditTextFragment;
    public DialogWithMultiEditTextFragment dialogWithMultiEditTextFragment;
    public LinearLayout grpTaskDate;
    public TextView lblKnownDate;
    public CheckBox chkTaskComplete;
    public TextView txtTaskNotes;
    public MyMessages myMessages;

    public void pickImage(View view)
    {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        try
        {
            switch (requestCode)
            {
                case SELECT_PHOTO:
                    if (resultCode == RESULT_OK)
                    {
                        try
                        {
                            MyBitmap myBitmap = new MyBitmap();
                            Boolean lRetCode =
                                    imageUtils.ScaleBitmapFromUrl
                                            (
                                                    imageReturnedIntent.getData(),
                                                    getContentResolver(),
                                                    myBitmap
                                            );
                            if(lRetCode==false)
                                return;

                            // assign new bitmap and set scale type
                            imageViewSmall.setImageBitmap(myBitmap.Value);

                            cbPicturePicked.setChecked(true);

                            taskItem.pictureChanged = true;


                        } catch (Exception e)
                        {
                            ShowError("onActivityResult-selectphoto", e.getMessage());
                        }
                    }
                    break;

            }
        }
        catch (Exception e)
        {
            ShowError("onActivityResult", e.getMessage());
        }
    }

    public void clearImage(View view)
    {
        cbPicturePicked.setChecked(false);
        imageViewSmall.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.imagemissing));
    }

    public void btnClearImage(View view)
    {
        clearImage(view);
        taskItem.pictureChanged = true;
        taskItem.pictureAssigned = false;
    }

    public void saveTask(View view)
    {
        myMessages.ShowMessageShort("Saving " + taskDescription.getText().toString());

        taskItem.pictureAssigned = cbPicturePicked.isChecked();
        taskItem.taskDescription = taskDescription.getText().toString();
        taskItem.fileBitmap = null;
        if (taskItem.pictureAssigned)
            taskItem.fileBitmap=((BitmapDrawable)imageViewSmall.getDrawable()).getBitmap() ;

        if (sw.isChecked())
        {
            taskItem.taskDateDate = new Date();
            if(dateUtils.StrToDate(taskDate.getText().toString(), taskItem.taskDateDate)==false)
                return;
        }
        else
        {
            taskItem.taskDateDate = new Date(DateUtils.unknownDate);
        }

        MyLong myLong = new MyLong();
        if(dateUtils.DateToInt(taskItem.taskDateDate, myLong)==false)
            return;
        taskItem.taskDateInt = myLong.Value;

        taskItem.taskDateString = taskDate.getText().toString();
        taskItem.taskDateKnown = sw.isChecked();
        taskItem.taskComplete = chkTaskComplete.isChecked();
        taskItem.taskNotes = txtTaskNotes.getText().toString();

        if(action.equals("add"))
        {
            MyInt myInt=new MyInt();
            taskItem.holidayId = holidayId;

            if(!databaseAccess.getNextTaskId(holidayId, myInt))
                return;
            taskItem.taskId = myInt.Value;

            if(!databaseAccess.getNextTaskSequenceNo(holidayId, myInt))
                return;
            taskItem.sequenceNo = myInt.Value;

            if(!databaseAccess.addTaskItem(taskItem))
                return;
        }

        if(action.equals("modify"))
        {
            taskItem.holidayId = holidayId;
            taskItem.taskId = taskId;
            if(!databaseAccess.updateTaskItem(taskItem))
                return;
        }

        finish();
    }


    public void TaskDescriptionPicked(View view)
    {
        taskDescription.setText(dialogWithEditTextFragment.getFinalText());

        dialogWithEditTextFragment.dismiss();
    }

    // Create a YES onclick procedure
    public void pickTaskName(View view)
    {
        dwetOnOkClick = new View.OnClickListener()
        {
            public void onClick(View view)
            {
                TaskDescriptionPicked(view);
            }
        };


        dialogWithEditTextFragment =
                DialogWithEditTextFragment.newInstance
                        (
                                getFragmentManager(),     // for the transaction bit
                                "hihi",            // unique name for this dialog type
                                "Task Description" ,    // form caption
                                "Description",             // form message
                                R.drawable.attachment,
                                taskDescription.getText().toString(),                // initial text
                                dwetOnOkClick,
                                this,
                                false
                        );

        dialogWithEditTextFragment.showIt();
    }

    public void TaskNotesPicked(View view)
    {
        txtTaskNotes.setText(dialogWithMultiEditTextFragment.getFinalText());

        dialogWithMultiEditTextFragment.dismiss();
    }

    // Create a YES onclick procedure
    public void pickTaskNotes(View view)
    {
        dwetOnOkClick = new View.OnClickListener()
        {
            public void onClick(View view)
            {
                TaskNotesPicked(view);
            }
        };


        dialogWithMultiEditTextFragment =
                DialogWithMultiEditTextFragment.newInstance
                        (
                                getFragmentManager(),     // for the transaction bit
                                "hjhj",            // unique name for this dialog type
                                "Task Notes" ,    // form caption
                                "Notes",             // form message
                                R.drawable.attachment,
                                txtTaskNotes.getText().toString(),                // initial text
                                dwetOnOkClick,
                                this
                        );


        dialogWithMultiEditTextFragment.showIt();
    }


    private void ShowError(String argFunction, String argMessage)
    {
        myMessages.ShowError
                (
                        "Error in TaskDetailsEdit::" + argFunction,
                        argMessage
                );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_task_details_edit);

        databaseAccess = new DatabaseAccess(this);
        actionBar = getSupportActionBar();
        imageUtils = new ImageUtils(this);
        dateUtils = new DateUtils(this);
        myMessages = new MyMessages(this);

        cbPicturePicked=(CheckBox)findViewById(R.id.picturePicked);
        imageViewSmall = (ImageView)findViewById(R.id.imageViewSmall);
        taskDescription =(TextView)findViewById(R.id.txtTaskName);
        taskDate =(TextView)findViewById(R.id.txtTaskDate);
        grpTaskDate = (LinearLayout)findViewById(R.id.grpTaskDate);
        lblKnownDate = (TextView)findViewById(R.id.lblKnownDate);
        sw = (Switch)findViewById(R.id.swKnownDate);
        chkTaskComplete = (CheckBox) findViewById(R.id.chkTaskComplete);
        txtTaskNotes = (TextView) findViewById(R.id.txtTaskNotes);

        clearImage(null);

        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            String title = extras.getString("TITLE");
            String subtitle = extras.getString("SUBTITLE");
            actionBar.setTitle(title);
            action = extras.getString("ACTION");
            if(action!=null && action.equals("add"))
            {
                taskItem = new TaskItem();
                holidayId = extras.getInt("HOLIDAYID");
                taskDescription.setText("");
                cbPicturePicked.setChecked(false);
                actionBar.setSubtitle("Add a Task");
                txtTaskNotes.setText("");
            }
            if(action!=null && action.equals("modify"))
            {
                holidayId = extras.getInt("HOLIDAYID");
                taskId = extras.getInt("TASKID");
                taskItem = new TaskItem();
                if(!databaseAccess.getTaskItem(holidayId, taskId, taskItem))
                    return;

                taskDescription.setText(taskItem.taskDescription);

                if(imageUtils.getPageHeaderImage(this, taskItem.taskPicture, imageViewSmall)==false)
                    return;

                cbPicturePicked.setChecked(taskItem.pictureAssigned);

                actionBar.setSubtitle(subtitle);

                if(taskItem.taskDateKnown)
                {
                    grpTaskDate.setVisibility(View.VISIBLE);
                    taskDate.setText(taskItem.taskDateString);
                    sw.setChecked(true);
                }
                else
                {
                    grpTaskDate.setVisibility(View.INVISIBLE);
                    sw.setChecked(false);
                }
                chkTaskComplete.setChecked(taskItem.taskComplete);
                txtTaskNotes.setText(taskItem.taskNotes);
            }
        }

    }

    public void pickDateTime(View view)
    {
        DialogDatePicker ddp = new DialogDatePicker(this);
        ddp.txtStartDate = (TextView)findViewById(R.id.txtTaskDate);
        Date date = new Date();
        if(dateUtils.StrToDate(ddp.txtStartDate.getText().toString(), date)==false)
            return;
        ddp.setInitialDate(date);
        ddp.show();
    }


    public void datesAreUnknown()
    {
        grpTaskDate.setVisibility(View.INVISIBLE);
        lblKnownDate.setText("Date not known");
    }

    public void datesAreKnown()
    {
        grpTaskDate.setVisibility(View.VISIBLE);
        lblKnownDate.setText("Date known");
    }

    public void toggleVisibility(View view)
    {
        if(sw.isChecked())
        {
            MyString myString = new MyString();
            if(dateUtils.DateToStr(new Date(), myString)==false)
                return;
            taskDate.setText(myString.Value);
            datesAreKnown();
        }
        else
        {
            datesAreUnknown();
        }
    }
/*
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.daydetailsformmenu, menu);
        return true;
    }
*/
}
