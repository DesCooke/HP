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

class TableTask extends TableBase
{
    private final DateUtils dateUtils;

    TableTask(Context context, SQLiteOpenHelper dbHelper)
    {
        super(context, dbHelper);
        dateUtils=new DateUtils();
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

    boolean getTaskCount(int argHolidayId, MyInt retInt)
    {
        try
        {
            if(!IsValid())
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
            if(!IsValid())
                return (false);

            if(taskItem.pictureAssigned) {
                /* if picture name has something in it - it means it came from internal folder */
                if (taskItem.taskPicture.isEmpty()) {
                    //myMessages().LogMessage("  - Save new image and get a filename...");
                    MyString myString = new MyString();
                    if (!savePicture(taskItem.holidayId, taskItem.fileBitmap, myString))
                        return (false);
                    taskItem.taskPicture = myString.Value;
                    //myMessages().LogMessage("  - New filename " + taskItem.taskPicture);
                }
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
            if(!IsValid())
                return (false);

            if(items == null)
                return (false);

            for(int i=0; i < items.size(); i++)
            {
                if(items.get(i).sequenceNo != items.get(i).origSequenceNo)
                {
                    if(!updateTaskItem(items.get(i)))
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
            if(!IsValid())
                return (false);

            //myMessages().LogMessage("updateTaskItem:Handling Image");
            if(taskItem.pictureChanged)
            {
                if (!taskItem.origPictureAssigned || taskItem.taskPicture.isEmpty() || taskItem.taskPicture.compareTo(taskItem.origTaskPicture) != 0) {
                    if(taskItem.origPictureAssigned)
                    {
                        //myMessages().LogMessage("  - Original Image was assigned - need to get rid of it");
                        if(!removePicture(taskItem.holidayId, taskItem.origTaskPicture))
                            return (false);
                    }

                /* if picture name has something in it - it means it came from internal folder */
                    if(taskItem.taskPicture.isEmpty())
                    {
                        //myMessages().LogMessage("  - New Image was not from internal folder...");
                        if(taskItem.pictureAssigned)
                        {
                            //myMessages().LogMessage("  - Save new image and get a filename...");
                            MyString myString=new MyString();
                            if(!savePicture(taskItem.holidayId, taskItem.fileBitmap, myString))
                                return (false);
                            taskItem.taskPicture=myString.Value;
                            //myMessages().LogMessage("  - New filename " + taskItem.taskPicture);
                        }
                    }
                }
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

    boolean deleteTaskItem(TaskItem taskItem)
    {
        try
        {
            if(!IsValid())
                return (false);

            String lSQL="DELETE FROM Tasks " + "WHERE holidayId = " + taskItem.holidayId + " " + "AND taskId = " + taskItem.taskId;

            if(!taskItem.taskPicture.isEmpty())
                if(!removePicture(taskItem.holidayId, taskItem.taskPicture))
                    return (false);

            if(!executeSQL("deleteTaskItem", lSQL))
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
            if(!IsValid())
                return (false);

            String lSQL;
            lSQL="SELECT holidayId, taskId, sequenceNo, taskDescription, " + "  taskDateKnown, taskDate, taskPicture, taskComplete, taskNotes, infoId, " + "  noteId, galleryId " + "FROM Tasks " + "WHERE HolidayId = " + holidayId + " " + "AND TaskId = " + taskId;

            Cursor cursor=executeSQLOpenCursor("getTaskItem", lSQL);
            if(cursor != null)
            {
                cursor.moveToFirst();
                if(!GetTaskItemFromQuery(cursor, taskItem))
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
        if(!IsValid())
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
            taskItem.taskDateKnown= lDateKnown == 1;
            taskItem.taskDateInt=Long.parseLong(cursor.getString(5));
            if(!dateUtils.IntToDate(taskItem.taskDateInt, taskItem.taskDateDate))
                return (false);

            MyString myString=new MyString();
            if(!dateUtils.DateToStr(taskItem.taskDateDate, myString))
                return (false);
            taskItem.taskDateString=myString.Value;

            taskItem.taskPicture=cursor.getString(6);
            int lTaskComplete=Integer.parseInt(cursor.getString(7));
            taskItem.taskComplete= lTaskComplete == 1;
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

            if(!taskItem.taskPicture.isEmpty())
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

            if(!executeSQLGetInt("getNextTaskId", lSQL, retInt))
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

            if(!executeSQLGetInt("getNextSequenceNo", lSQL, retInt))
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
            String lSql="SELECT holidayId, taskId, sequenceNo, taskDescription, " +
                    "  taskDateKnown, taskDate, taskPicture, taskComplete, taskNotes, infoId, " +
                    "  noteId, galleryId " + "FROM Tasks " +
                    " WHERE holidayId = " + holidayId + " " + "ORDER BY SequenceNo ";

            Cursor cursor=executeSQLOpenCursor("getTaskList", lSql);
            if(cursor == null)
                return (false);

            while(cursor.moveToNext())
            {
                TaskItem taskItem=new TaskItem();
                if(!GetTaskItemFromQuery(cursor, taskItem))
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

    boolean getOSTaskList(int holidayId, ArrayList<TaskItem> al)
    {
        try
        {
            String lSql="SELECT holidayId, taskId, sequenceNo, taskDescription, " +
                    "  taskDateKnown, taskDate, taskPicture, taskComplete, taskNotes, infoId, " +
                    "  noteId, galleryId " +
                    " FROM Tasks " +
                    " WHERE holidayId = " + holidayId + " " +
                    " AND taskComplete = 0 " +
                    "ORDER BY SequenceNo ";

            Cursor cursor=executeSQLOpenCursor("getTaskList", lSql);
            if(cursor == null)
                return (false);

            while(cursor.moveToNext())
            {
                TaskItem taskItem=new TaskItem();
                if(!GetTaskItemFromQuery(cursor, taskItem))
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


}
