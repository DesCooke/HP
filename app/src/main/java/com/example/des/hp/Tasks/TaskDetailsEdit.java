package com.example.des.hp.Tasks;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.myutils.*;
import com.example.des.hp.R;

import java.util.Date;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
import static com.example.des.hp.myutils.DateUtils.dateUtils;
import static com.example.des.hp.myutils.MyMessages.myMessages;

public class TaskDetailsEdit extends TaskDetailsView implements View.OnClickListener
{

    //region Member variables
    public View.OnClickListener dwetOnOkClick;
    public DialogWithEditTextFragment dialogWithEditTextFragment;
    public ImageView btnDelete;
    //endregion

    //region Constructors/Destructors
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            btnClear.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.VISIBLE);
            grpTaskDate.setVisibility(View.VISIBLE);
            chkTaskComplete.setClickable(true);

            btnDelete=findViewById(R.id.my_toolbar_delete);
            btnDelete.setOnClickListener(view -> deleteTask());

            if(action != null && action.equals("add"))
            {
                grpMenuFile.setVisibility(View.GONE);
                txtTaskDescription.setText("");
                datesAreUnknown();
                swKnownDate.setChecked(false);
            } else
            {
                if(taskItem.taskDateKnown)
                {
                    swKnownDate.setChecked(true);
                    datesAreKnown();
                } else
                {
                    swKnownDate.setChecked(false);
                    datesAreUnknown();
                }
            }

            txtTaskDate.setOnClickListener(this);
            txtTaskDescription.setOnClickListener(this);
            imageView.setOnClickListener(this);
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        /* disable the menu entirely */
        return false;
    }
    //endregion

    //region OnClick Events
    public void onClick(View view)
    {
        try {
            int id = view.getId();
            if (id == R.id.txtTaskDate)
                pickDateTime(view);
            if (id == R.id.txtTaskDescription)
                pickTaskName(view);
            if (id == R.id.imageViewSmall)
                pickImage(view);
        }
        catch(Exception e)
        {
            ShowError("onClick", e.getMessage());
        }

    }

    //region showForm
    public void showForm()
    {
        super.showForm();
        try
        {
            ShowToolbarDelete();
        }
        catch(Exception e)
        {
            ShowError("showForm", e.getMessage());
        }
    }

    public void TaskDescriptionPicked(View view)
    {
        try
        {
            txtTaskDescription.setText(dialogWithEditTextFragment.getFinalText());

            dialogWithEditTextFragment.dismiss();
        }
        catch(Exception e)
        {
            ShowError("TaskDescriptionPicked", e.getMessage());
        }

    }

    // Create a YES onclick procedure
    public void pickTaskName(View view)
    {
        try
        {
            dwetOnOkClick= this::TaskDescriptionPicked;


            dialogWithEditTextFragment=DialogWithEditTextFragment.newInstance(getSupportFragmentManager(),     // for the transaction bit
                "hihi",            // unique name for this dialog type
                "Task Description",    // form caption
                "Description",             // form message
                R.drawable.attachment, txtTaskDescription.getText().toString(),                // initial text
                dwetOnOkClick, this,

                false
            );

            dialogWithEditTextFragment.showIt();
        }
        catch(Exception e)
        {
            ShowError("pickTaskName", e.getMessage());
        }

    }

    public void pickDateTime(View view)
    {
        try
        {
            DialogDatePicker ddp=new DialogDatePicker(this);
            ddp.txtStartDate= findViewById(R.id.txtTaskDate);
            Date date=new Date();
            if(!dateUtils().StrToDate(ddp.txtStartDate.getText().toString(), date))
                return;
            ddp.setInitialDate(date);
            ddp.show();
        }
        catch(Exception e)
        {
            ShowError("pickDateTime", e.getMessage());
        }

    }


    public void datesAreUnknown()
    {
        try
        {
            grpTaskDate.setVisibility(View.INVISIBLE);
            txtKnownDate.setText(getString(R.string.date_not_known));
        }
        catch(Exception e)
        {
            ShowError("datesAreUnknown", e.getMessage());
        }

    }

    public void datesAreKnown()
    {
        try
        {
            grpTaskDate.setVisibility(View.VISIBLE);
            txtKnownDate.setText(getString(R.string.date_known));
        }
        catch(Exception e)
        {
            ShowError("datesAreKnown", e.getMessage());
        }

    }

    public void toggleVisibility(View view)
    {
        try
        {
            if(swKnownDate.isChecked())
            {
                MyString myString=new MyString();
                if(!dateUtils().DateToStr(new Date(), myString))
                    return;
                txtTaskDate.setText(myString.Value);
                datesAreKnown();
            } else
            {
                datesAreUnknown();
            }
        }
        catch(Exception e)
        {
            ShowError("toggleVisibility", e.getMessage());
        }

    }
    //endregion

    //region Saving
    public void saveSchedule(View view)
    {
        try(DatabaseAccess da = databaseAccess())
        {
            myMessages().ShowMessageShort("Saving " + txtTaskDescription.getText().toString());

            taskItem.taskDescription=txtTaskDescription.getText().toString();

            if(imageChanged) {
                taskItem.taskPicture = "";
                if (!internalImageFilename.isEmpty())
                    taskItem.taskPicture = internalImageFilename;
                taskItem.pictureAssigned = imageSet;
                taskItem.pictureChanged = true;
                taskItem.fileBitmap = null;
                if (imageSet)
                    taskItem.fileBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            }


            if(swKnownDate.isChecked())
            {
                taskItem.taskDateDate=new Date();
                if(!dateUtils().StrToDate(txtTaskDate.getText().toString(), taskItem.taskDateDate))
                    return;
            } else
            {
                taskItem.taskDateDate=new Date(DateUtils.unknownDate);
            }

            MyLong myLong=new MyLong();
            if(!dateUtils().DateToInt(taskItem.taskDateDate, myLong))
                return;
            taskItem.taskDateInt=myLong.Value;

            taskItem.taskDateString=txtTaskDate.getText().toString();
            taskItem.taskDateKnown=swKnownDate.isChecked();
            taskItem.taskComplete=chkTaskComplete.isChecked();
            taskItem.taskNotes="";

            if(action.equals("add"))
            {
                MyInt myInt=new MyInt();
                taskItem.holidayId=holidayId;

                if(!da.getNextTaskId(holidayId, myInt))
                    return;
                taskItem.taskId=myInt.Value;

                if(!da.getNextTaskSequenceNo(holidayId, myInt))
                    return;
                taskItem.sequenceNo=myInt.Value;

                if(!da.addTaskItem(taskItem))
                    return;
            }

            if(action.equals("modify"))
            {
                if(!da.updateTaskItem(taskItem))
                    return;
            }

            finish();
        }
        catch(Exception e)
        {
            ShowError("saveTask", e.getMessage());
        }

    }

    @Override
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

    //endregion

}
