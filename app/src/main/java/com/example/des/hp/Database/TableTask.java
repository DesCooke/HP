package com.example.des.hp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.des.hp.Tasks.TaskItem;
import com.example.des.hp.myutils.DateUtils;
import com.example.des.hp.myutils.MyInt;
import com.example.des.hp.myutils.MyString;

import java.util.ArrayList;
import java.util.Random;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
import static com.example.des.hp.myutils.MyMessages.myMessages;

class TableTask extends TableBase
{
    private DateUtils dateUtils;

    TableTask(Context context, SQLiteOpenHelper dbHelper)
    {
        super(context, dbHelper);
        dateUtils=new DateUtils(context);
    }

    public void ShowError(String argFunction, String argMessage)
    {
        super.ShowError("TableTask:" + argFunction, argMessage);
    }

    public boolean onCreate(SQLiteDatabase db)
    {
        try
        {
            String lSQL="CREATE TABLE IF NOT EXISTS tasks " + "( " + "  holidayId       INT(5),  " + "  taskId          INT(5),  " + "  sequenceNo      INT(5),  " + "  taskDescription VARCHAR, " + "  taskDateKnown   INT(1),  " + "  taskDate        INT(16), " + "  taskPicture     VARCHAR, " + "  taskComplete    INT(1),  " + "  taskNotes       VARCHAR, " + "  infoId          INT(5),  " + "  noteId          INT(5),  " + "  galleryId       INT(5)  " + ") ";

            db.execSQL(lSQL);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
        return (false);
    }

    public boolean onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        try
        {
            return (true);
        }
        catch(Exception e)
        {
            ShowError("onUpgrade", e.getMessage());
        }
        return (false);
    }

    boolean getTaskCount(int argHolidayId, MyInt retInt)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            String lSQL="SELECT IFNULL(COUNT(*),0) " + "FROM Tasks " + "WHERE holidayId = " + argHolidayId;

            return (executeSQLGetInt("getTaskCount", lSQL, retInt));
        }
        catch(Exception e)
        {
            ShowError("getTaskCount", e.getMessage());
        }
        return (false);

    }

    boolean addTaskItem(TaskItem taskItem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            if(taskItem.pictureAssigned)
            {
            /* if picture name has something in it - it means it came from internal folder */
                if(taskItem.taskPicture.length() == 0)
                {
                    //myMessages().LogMessage("  - New Image was not from internal folder...");
                    if(taskItem.pictureAssigned)
                    {
                        //myMessages().LogMessage("  - Save new image and get a filename...");
                        MyString myString=new MyString();
                        if(savePicture(taskItem.holidayId, taskItem.fileBitmap, myString) == false)
                            return (false);
                        taskItem.taskPicture=myString.Value;
                        //myMessages().LogMessage("  - New filename " + taskItem.taskPicture);
                    } else
                    {
                        //myMessages().LogMessage("  - New Image not setup - so - keep it blank");
                    }
                } else
                {
                    //myMessages().LogMessage("  - New Image was from internal folder - so just use it (" + taskItem.taskPicture + ")");
                }
            } else
            {
                //myMessages().LogMessage("  - New Image not assigned - do nothing");
            }

            int lTaskDateKnown=0;
            if(taskItem.taskDateKnown)
                lTaskDateKnown=1;

            int lTaskComplete=0;
            if(taskItem.taskComplete)
                lTaskComplete=1;

            String lSql="INSERT INTO tasks " + "  (holidayId, taskId, sequenceNo, taskDescription, " + "   taskDateKnown, taskDate, taskPicture, taskComplete, taskNotes, infoId, " + "   noteId, galleryId) " + "VALUES " + "(" + taskItem.holidayId + "," + taskItem.taskId + "," + taskItem.sequenceNo + ", " + MyQuotedString(taskItem.taskDescription) + ", " + lTaskDateKnown + "," + taskItem.taskDateInt + "," + MyQuotedString(taskItem.taskPicture) + ", " + lTaskComplete + ", " + MyQuotedString(taskItem.taskNotes) + ", " + taskItem.infoId + ", " + taskItem.noteId + ", " + taskItem.galleryId + ")";

            return (executeSQL("addTaskItem", lSql));

        }
        catch(Exception e)
        {
            ShowError("addTaskItem", e.getMessage());
        }
        return (false);

    }

    boolean updateTaskItems(ArrayList<TaskItem> items)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            if(items == null)
                return (false);

            for(int i=0; i < items.size(); i++)
            {
                if(items.get(i).sequenceNo != items.get(i).origSequenceNo)
                {
                    if(updateTaskItem(items.get(i)) == false)
                        return (false);
                }
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("updateTaskItems", e.getMessage());
        }
        return (false);
    }

    boolean updateTaskItem(TaskItem taskItem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            //myMessages().LogMessage("updateTaskItem:Handling Image");
            if(taskItem.pictureChanged)
            {
                if(taskItem.origPictureAssigned && taskItem.taskPicture.length() > 0 && taskItem.taskPicture.compareTo(taskItem.origTaskPicture) == 0)
                {
                    //myMessages().LogMessage("  - Original Image changed back to the original - do nothing");
                } else
                {
                    if(taskItem.origPictureAssigned)
                    {
                        //myMessages().LogMessage("  - Original Image was assigned - need to get rid of it");
                        if(removePicture(taskItem.holidayId, taskItem.origTaskPicture) == false)
                            return (false);
                    }
            
                /* if picture name has something in it - it means it came from internal folder */
                    if(taskItem.taskPicture.length() == 0)
                    {
                        //myMessages().LogMessage("  - New Image was not from internal folder...");
                        if(taskItem.pictureAssigned)
                        {
                            //myMessages().LogMessage("  - Save new image and get a filename...");
                            MyString myString=new MyString();
                            if(savePicture(taskItem.holidayId, taskItem.fileBitmap, myString) == false)
                                return (false);
                            taskItem.taskPicture=myString.Value;
                            //myMessages().LogMessage("  - New filename " + taskItem.taskPicture);
                        } else
                        {
                            //myMessages().LogMessage("  - New Image not setup - so - keep it blank");
                        }
                    } else
                    {
                        //myMessages().LogMessage("  - New Image was from internal folder - so just use it (" + taskItem.taskPicture + ")");
                    }
                }
            } else
            {
                //myMessages().LogMessage("  - Image not changed - do nothing");
            }


            int lDateKnown=0;
            if(taskItem.taskDateKnown)
                lDateKnown=1;

            int lTaskComplete=0;
            if(taskItem.taskComplete)
                lTaskComplete=1;

            String lSQL;
            lSQL="UPDATE Tasks " + "SET sequenceNo = " + taskItem.sequenceNo + ", " + "    taskDescription = " + MyQuotedString(taskItem.taskDescription) + ", " + "    taskDateKnown = " + lDateKnown + ", " + "    taskDate = " + taskItem.taskDateInt + ", " + "    taskPicture = " + MyQuotedString(taskItem.taskPicture) + ", " + "    taskComplete = " + lTaskComplete + ", " + "    taskNotes = " + MyQuotedString(taskItem.taskNotes) + ", " + "    infoId = " + taskItem.infoId + ", " + "    noteId = " + taskItem.noteId + ", " + "    galleryId = " + taskItem.galleryId + " " + "WHERE holidayId = " + taskItem.holidayId + " " + "AND taskId = " + taskItem.taskId;
            return (executeSQL("updateTaskItem", lSQL));
        }
        catch(Exception e)
        {
            ShowError("updateTaskItem", e.getMessage());
        }
        return (false);

    }

    public boolean deleteTasks(int holidayId)
    {
        try
        {
            ArrayList<TaskItem> taskList=new ArrayList<>();

            if(getTaskList(holidayId, taskList) == false)
                return (false);

            for(TaskItem taskItem : taskList)
                if(deleteTaskItem(taskItem) == false)
                    return (false);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("deleteTasks", e.getMessage());
        }
        return (false);
    }

    boolean deleteTaskItem(TaskItem taskItem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            String lSQL="DELETE FROM Tasks " + "WHERE holidayId = " + taskItem.holidayId + " " + "AND taskId = " + taskItem.taskId;

            if(taskItem.taskPicture.length() > 0)
                if(removePicture(taskItem.holidayId, taskItem.taskPicture) == false)
                    return (false);

            if(executeSQL("deleteTaskItem", lSQL) == false)
                return (false);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("deleteTaskItem", e.getMessage());
        }
        return (false);
    }

    boolean getTaskItem(int holidayId, int taskId, TaskItem taskItem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            String lSQL;
            lSQL="SELECT holidayId, taskId, sequenceNo, taskDescription, " + "  taskDateKnown, taskDate, taskPicture, taskComplete, taskNotes, infoId, " + "  noteId, galleryId " + "FROM Tasks " + "WHERE HolidayId = " + holidayId + " " + "AND TaskId = " + taskId;

            Cursor cursor=executeSQLOpenCursor("getTaskItem", lSQL);
            if(cursor != null)
            {
                cursor.moveToFirst();
                if(GetTaskItemFromQuery(cursor, taskItem) == false)
                    return (false);
            }
            executeSQLCloseCursor("getTaskItem");
            return (true);
        }
        catch(Exception e)
        {
            ShowError("getTaskItem", e.getMessage());
        }
        return (false);
    }

    private boolean GetTaskItemFromQuery(Cursor cursor, TaskItem taskItem)
    {
        if(IsValid() == false)
            return (false);

        try
        {
            if(cursor.getCount() == 0)
                return (true);

            taskItem.holidayId=Integer.parseInt(cursor.getString(0));
            taskItem.taskId=Integer.parseInt(cursor.getString(1));
            taskItem.sequenceNo=Integer.parseInt(cursor.getString(2));
            taskItem.taskDescription=cursor.getString(3);
            int lDateKnown=Integer.parseInt(cursor.getString(4));
            taskItem.taskDateKnown=false;
            if(lDateKnown == 1)
                taskItem.taskDateKnown=true;
            taskItem.taskDateInt=Long.parseLong(cursor.getString(5));
            if(dateUtils.IntToDate(taskItem.taskDateInt, taskItem.taskDateDate) == false)
                return (false);

            MyString myString=new MyString();
            if(dateUtils.DateToStr(taskItem.taskDateDate, myString) == false)
                return (false);
            taskItem.taskDateString=myString.Value;

            taskItem.taskPicture=cursor.getString(6);
            int lTaskComplete=Integer.parseInt(cursor.getString(7));
            taskItem.taskComplete=false;
            if(lTaskComplete == 1)
                taskItem.taskComplete=true;
            taskItem.taskNotes=cursor.getString(8);
            taskItem.infoId=Integer.parseInt(cursor.getString(9));
            taskItem.noteId=Integer.parseInt(cursor.getString(10));
            taskItem.galleryId=Integer.parseInt(cursor.getString(11));

            taskItem.origHolidayId=taskItem.holidayId;
            taskItem.origTaskId=taskItem.taskId;
            taskItem.origSequenceNo=taskItem.sequenceNo;
            taskItem.origTaskDescription=taskItem.taskDescription;
            taskItem.origTaskDateKnown=taskItem.taskDateKnown;
            taskItem.origTaskDateInt=taskItem.taskDateInt;
            taskItem.origTaskDateDate=taskItem.taskDateDate;
            taskItem.origTaskDateString=taskItem.taskDateString;
            taskItem.origTaskPicture=taskItem.taskPicture;
            taskItem.origTaskComplete=taskItem.taskComplete;
            taskItem.origTaskNotes=taskItem.taskNotes;
            taskItem.origInfoId=taskItem.infoId;
            taskItem.origNoteId=taskItem.noteId;
            taskItem.origGalleryId=taskItem.galleryId;
            taskItem.pictureChanged=false;

            if(taskItem.taskPicture.length() > 0)
            {
                taskItem.pictureAssigned=true;
                taskItem.origPictureAssigned=true;
            } else
            {
                taskItem.pictureAssigned=false;
                taskItem.origPictureAssigned=false;
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("GetTaskItemFromQuery", e.getMessage());
        }
        return (false);
    }

    boolean getNextTaskId(int holidayId, MyInt retInt)
    {
        try
        {
            String lSQL="SELECT IFNULL(MAX(taskId),0) " + "FROM Tasks " + "WHERE holidayId = " + holidayId;

            if(executeSQLGetInt("getNextTaskId", lSQL, retInt) == false)
                return (false);
            retInt.Value=retInt.Value + 1;
            return (true);
        }
        catch(Exception e)
        {
            ShowError("getNextTaskId", e.getMessage());
        }
        return (false);
    }

    boolean getNextSequenceNo(int holidayId, MyInt retInt)
    {
        try
        {
            String lSQL="SELECT IFNULL(MAX(SequenceNo),0) " + "FROM Tasks " + "WHERE holidayId = " + holidayId;

            if(executeSQLGetInt("getNextSequenceNo", lSQL, retInt) == false)
                return (false);
            retInt.Value=retInt.Value + 1;
            return (true);
        }
        catch(Exception e)
        {
            ShowError("getNextSequenceNo", e.getMessage());
        }
        return (false);
    }


    boolean getTaskList(int holidayId, ArrayList<TaskItem> al)
    {
        try
        {
            String lSql="SELECT holidayId, taskId, sequenceNo, taskDescription, " + "  taskDateKnown, taskDate, taskPicture, taskComplete, taskNotes, infoId, " + "  noteId, galleryId " + "FROM Tasks " + "WHERE holidayId = " + holidayId + " " + "ORDER BY SequenceNo ";

            Cursor cursor=executeSQLOpenCursor("getTaskList", lSql);
            if(cursor == null)
                return (false);

            while(cursor.moveToNext())
            {
                TaskItem taskItem=new TaskItem();
                if(GetTaskItemFromQuery(cursor, taskItem) == false)
                    return (false);

                al.add(taskItem);
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("getTaskList", e.getMessage());
        }
        return (false);
    }

    boolean clearNote(int holidayId, int noteId)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            String l_SQL="UPDATE tasks SET noteId = 0 " + "WHERE holidayId = " + holidayId + " " + "AND noteId = " + noteId;

            if(executeSQL("clearNote", l_SQL) == false)
                return (false);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("clearNote", e.getMessage());
        }
        return (false);
    }

    private String randomTaskDescription()
    {
        try
        {
            Random random=new Random();
            int i=random.nextInt(10);
            switch(i)
            {
                case 0:
                    return ("Change Currency");
                case 1:
                    return ("Get Park Tickets");
                case 2:
                    return ("Book Cinema");
                case 3:
                    return ("Checkin");
                case 4:
                    return ("Check Park Times");
                case 5:
                    return ("Book Holiday");
                case 6:
                    return ("Check Passport");
                case 7:
                    return ("Check Parade Times");
                case 8:
                    return ("Book Time Off");
            }
            return ("Book VIP Room");
        }
        catch(Exception e)
        {
            ShowError("randomTaskDescription", e.getMessage());
        }
        return ("Spare Day");

    }


    
}
