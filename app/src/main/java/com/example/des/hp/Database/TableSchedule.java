package com.example.des.hp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.des.hp.Schedule.ScheduleItem;
import com.example.des.hp.myutils.MyInt;
import com.example.des.hp.myutils.MyString;

import java.util.ArrayList;

class TableSchedule extends TableBase
{
    TableSchedule(Context context, SQLiteOpenHelper dbHelper)
    {
        super(context, dbHelper);
    }

    public void ShowError(String argFunction, String argMessage)
    {
        super.ShowError("TableSchedule:" + argFunction, argMessage);
    }

    public boolean onCreate(SQLiteDatabase db)
    {
        try
        {
            String lSQL="CREATE TABLE IF NOT EXISTS schedule " +
                    "( " + "  holidayId        INT(5),  " +
                    "  dayId            INT(5),  " +
                    "  attractionId     INT(5),  " +
                    "  attractionAreaId INT(5),  " +
                    "  scheduleId       INT(5),  " +
                    "  sequenceNo       INT(5),  " +
                    "  schedType        INT(5),  " +
                    "  schedName        VARCHAR, " +
                    "  schedPicture     VARCHAR, " +
                    "  startTimeKnown   INT(1),  " +
                    "  startHour        INT(2),  " +
                    "  startMin         INT(2),  " +
                    "  endTimeKnown     INT(1),  " +
                    "  endHour          INT(2),  " +
                    "  endMin           INT(2),  " +
                    "  infoId           INT(5),  " +
                    "  noteId           INT(5),  " +
                    "  galleryId        INT(5)   " +
                    ") ";

            db.execSQL(lSQL);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
        return (false);
    }

    public boolean addScheduleItem(ScheduleItem scheduleItem)
    {
        try
        {
            if(!IsValid())
                return (false);

            if(scheduleItem.pictureAssigned) {
                /* if picture name has something in it - it means it came from internal folder */
                if (scheduleItem.schedPicture.isEmpty()) {
                    //myMessages().LogMessage("  - Save new image and get a filename...");
                    MyString myString = new MyString();
                    if (!savePicture(scheduleItem.holidayId, scheduleItem.scheduleBitmap, myString))
                        return (false);
                    scheduleItem.schedPicture = myString.Value;
                    //myMessages().LogMessage("  - New filename " + scheduleItem.schedPicture);
                }
            }

            int lStartTimeKnown=scheduleItem.startTimeKnown ? 1 : 0;

            int lEndTimeKnown=scheduleItem.endTimeKnown ? 1 : 0;

            String lSql="INSERT INTO schedule " + "  (holidayId, dayId, attractionId, attractionAreaId, " + "   scheduleId, sequenceNo, schedType, " + "   schedName, schedPicture, startTimeKnown, startHour, " + "   startMin, endTimeKnown, endHour, endMin, infoId, " + "   noteId, galleryId) " + " VALUES " + "(" + scheduleItem.holidayId + "," + scheduleItem.dayId + "," + scheduleItem.attractionId + "," + scheduleItem.attractionAreaId + "," + scheduleItem.scheduleId + "," + scheduleItem.sequenceNo + "," + scheduleItem.schedType + "," + MyQuotedString(scheduleItem.schedName) + "," + MyQuotedString(scheduleItem.schedPicture) + "," + lStartTimeKnown + ", " + scheduleItem.startHour + ", " + scheduleItem.startMin + ", " + lEndTimeKnown + ", " + scheduleItem.endHour + ", " + scheduleItem.endMin + ", " + scheduleItem.infoId + ", " + scheduleItem.noteId + ", " + scheduleItem.galleryId + ")";

            return (executeSQL("addScheduleItem", lSql));

        }
        catch(Exception e)
        {
            ShowError("addScheduleItem", e.getMessage());
        }
        return (false);

    }

    boolean updateScheduleItems(ArrayList<ScheduleItem> items)
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
                    if(!updateScheduleItem(items.get(i)))
                        return (false);
                }
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("updateScheduleItems", e.getMessage());
        }
        return (false);
    }

    public boolean updateScheduleItem(ScheduleItem scheduleItem)
    {
        try
        {
            if(!IsValid())
                return (false);

            //myMessages().LogMessage("updateScheduleItem:Handling Image");
            if(scheduleItem.pictureChanged)
            {
                if (!scheduleItem.origPictureAssigned || scheduleItem.schedPicture.isEmpty() || scheduleItem.schedPicture.compareTo(scheduleItem.origSchedPicture) != 0) {

                    if(scheduleItem.origPictureAssigned)
                    {
                        //myMessages().LogMessage("  - Original Image was assigned - need to get rid of it");
                        if(!removePicture(scheduleItem.holidayId, scheduleItem.origSchedPicture))
                            return (false);
                    }

                /* if picture name has something in it - it means it came from internal folder */
                    if(scheduleItem.schedPicture.isEmpty())
                    {
                        //myMessages().LogMessage("  - New Image was not from internal folder...");
                        if(scheduleItem.pictureAssigned)
                        {
                            //myMessages().LogMessage("  - Save new image and get a filename...");
                            MyString myString=new MyString();
                            if(!savePicture(scheduleItem.holidayId, scheduleItem.scheduleBitmap, myString))
                                return (false);
                            scheduleItem.schedPicture=myString.Value;
                            //myMessages().LogMessage("  - New filename " + scheduleItem.schedPicture);
                        }
                    }
                }
            }


            int lStartTimeKnown=scheduleItem.startTimeKnown ? 1 : 0;
            int lEndTimeKnown=scheduleItem.endTimeKnown ? 1 : 0;

            if(scheduleItem.dayId != scheduleItem.origDayId || scheduleItem.attractionAreaId != scheduleItem.origAttractionAreaId || scheduleItem.attractionId != scheduleItem.origAttractionId)
            {
                MyInt myInt=new MyInt();
                if(!getNextScheduleSequenceNo(scheduleItem.holidayId, scheduleItem.dayId, scheduleItem.attractionId, scheduleItem.attractionAreaId, myInt))
                    return (false);
                scheduleItem.sequenceNo=myInt.Value;
                if(!getNextScheduleId(scheduleItem.holidayId, scheduleItem.dayId, scheduleItem.attractionId, scheduleItem.attractionAreaId, myInt))
                    return (false);
                scheduleItem.scheduleId=myInt.Value;

            }
            String lSQL;
            lSQL="UPDATE schedule " + "SET dayId = " + scheduleItem.dayId + ", " + "    attractionId = " + scheduleItem.attractionId + ", " + "    attractionAreaId = " + scheduleItem.attractionAreaId + ", " + "    schedName = " + MyQuotedString(scheduleItem.schedName) + ", " + "    schedPicture = " + MyQuotedString(scheduleItem.schedPicture) + ", " + "    startTimeKnown = " + lStartTimeKnown + ", " + "    startHour = " + scheduleItem.startHour + ", " + "    startMin = " + scheduleItem.startMin + ", " + "    endTimeKnown = " + lEndTimeKnown + ", " + "    endHour = " + scheduleItem.endHour + ", " + "    endMin = " + scheduleItem.endMin + ", " + "    sequenceNo = " + scheduleItem.sequenceNo + ", " + "    scheduleId = " + scheduleItem.scheduleId + ", " + "    infoId = " + scheduleItem.infoId + ", " + "    noteId = " + scheduleItem.noteId + ", " + "    galleryId = " + scheduleItem.galleryId + " WHERE holidayId = " + scheduleItem.holidayId + " " + "AND dayId = " + scheduleItem.origDayId + " " + "AND attractionId = " + scheduleItem.origAttractionId + " " + "AND attractionAreaId = " + scheduleItem.origAttractionAreaId + " " + "AND scheduleId = " + scheduleItem.origScheduleId;

            return (executeSQL("updateScheduleItem", lSQL));

        }
        catch(Exception e)
        {
            ShowError("updateScheduleItem", e.getMessage());
        }
        return (false);

    }

    boolean deleteScheduleItem(ScheduleItem scheduleItem)
    {
        try
        {
            if(!IsValid())
                return (false);

            String lSQL="DELETE FROM schedule " + "WHERE holidayId = " + scheduleItem.holidayId + " " + "AND dayId = " + scheduleItem.dayId + " " + "AND attractionId = " + scheduleItem.attractionId + " " + "AND attractionAreaId = " + scheduleItem.attractionAreaId + " " + "AND scheduleId = " + scheduleItem.scheduleId;

            if(!scheduleItem.schedPicture.isEmpty())
                if(!removePicture(scheduleItem.holidayId, scheduleItem.schedPicture))
                    return (false);

            if(!executeSQL("deleteScheduleItem", lSQL))
                return (false);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("deleteScheduleItem", e.getMessage());
        }
        return (false);
    }

    boolean getScheduleItem(int holidayId, int dayId, int attractionId, int attractionAreaId, int scheduleId, ScheduleItem item)
    {
        try
        {
            if(!IsValid())
                return (false);

            String lSQL;
            lSQL="SELECT holidayId, dayId, attractionId, attractionAreaId, " + "       scheduleId, sequenceNo, schedType, " + "       schedName, schedPicture, startTimeKnown, startHour,  " + "       startMin, endTimeKnown, endHour, endMin, infoId, noteId, galleryId " + " FROM Schedule " + "WHERE HolidayId = " + holidayId + " " + "AND DayId = " + dayId + " " + "AND attractionId = " + attractionId + " " + "AND attractionAreaId = " + attractionAreaId + " " + "AND ScheduleId = " + scheduleId;

            Cursor cursor=executeSQLOpenCursor("getScheduleItem", lSQL);
            if(cursor != null)
            {
                cursor.moveToFirst();
                if(!GetScheduleItemFromQuery(cursor, item))
                    return (false);
            }
            executeSQLCloseCursor("getScheduleItem");

            return (true);
        }
        catch(Exception e)
        {
            ShowError("getScheduleItem", e.getMessage());
        }
        return (false);
    }

    private boolean GetScheduleItemFromQuery(Cursor cursor, ScheduleItem scheduleItem)
    {
        if(!IsValid())
            return (false);

        try
        {
            if(cursor.getCount() == 0)
                return (true);

            scheduleItem.holidayId=Integer.parseInt(cursor.getString(0));
            scheduleItem.dayId=Integer.parseInt(cursor.getString(1));
            scheduleItem.attractionId=Integer.parseInt(cursor.getString(2));
            scheduleItem.attractionAreaId=Integer.parseInt(cursor.getString(3));
            scheduleItem.scheduleId=Integer.parseInt(cursor.getString(4));
            scheduleItem.sequenceNo=Integer.parseInt(cursor.getString(5));
            scheduleItem.schedType=Integer.parseInt(cursor.getString(6));
            scheduleItem.schedName=cursor.getString(7);
            scheduleItem.schedPicture=cursor.getString(8);
            scheduleItem.startTimeKnown=Integer.parseInt(cursor.getString(9)) == 1;
            scheduleItem.startHour=Integer.parseInt(cursor.getString(10));
            scheduleItem.startMin=Integer.parseInt(cursor.getString(11));
            scheduleItem.endTimeKnown=Integer.parseInt(cursor.getString(12)) == 1;
            scheduleItem.endHour=Integer.parseInt(cursor.getString(13));
            scheduleItem.endMin=Integer.parseInt(cursor.getString(14));
            scheduleItem.infoId=Integer.parseInt(cursor.getString(15));
            scheduleItem.noteId=Integer.parseInt(cursor.getString(16));
            scheduleItem.galleryId=Integer.parseInt(cursor.getString(17));

            scheduleItem.origHolidayId=scheduleItem.holidayId;
            scheduleItem.origDayId=scheduleItem.dayId;
            scheduleItem.origAttractionId=scheduleItem.attractionId;
            scheduleItem.origAttractionAreaId=scheduleItem.attractionAreaId;
            scheduleItem.origScheduleId=scheduleItem.scheduleId;
            scheduleItem.origSequenceNo=scheduleItem.sequenceNo;
            scheduleItem.origSchedType=scheduleItem.schedType;
            scheduleItem.origSchedName=scheduleItem.schedName;
            scheduleItem.origSchedPicture=scheduleItem.schedPicture;
            scheduleItem.origStartTimeKnown=scheduleItem.startTimeKnown;
            scheduleItem.origStartHour=scheduleItem.startHour;
            scheduleItem.origStartMin=scheduleItem.startMin;
            scheduleItem.origEndTimeKnown=scheduleItem.endTimeKnown;
            scheduleItem.origEndHour=scheduleItem.endHour;
            scheduleItem.origEndMin=scheduleItem.endMin;
            scheduleItem.origInfoId=scheduleItem.infoId;
            scheduleItem.origNoteId=scheduleItem.noteId;
            scheduleItem.origGalleryId=scheduleItem.galleryId;

            scheduleItem.pictureChanged=false;

            if(!scheduleItem.schedPicture.isEmpty())
            {
                scheduleItem.pictureAssigned=true;
                scheduleItem.origPictureAssigned=true;
            } else
            {
                scheduleItem.pictureAssigned=false;
                scheduleItem.origPictureAssigned=false;
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("GetScheduleItemFromQuery", e.getMessage());
        }

        return (false);
    }

    public boolean getNextScheduleId(int holidayId, int dayId, int attractionId, int attractionAreaId, MyInt retInt)
    {
        try
        {
            String lSQL="SELECT IFNULL(MAX(scheduleId),0) " + "FROM schedule " + "WHERE holidayId = " + holidayId + " " + "AND dayId = " + dayId + " " + "AND attractionId = " + attractionId + " " + "AND attractionAreaId = " + attractionAreaId;

            if(!executeSQLGetInt("getNextScheduleId", lSQL, retInt))
                return (false);
            retInt.Value=retInt.Value + 1;
            return (true);
        }
        catch(Exception e)
        {
            ShowError("getNextScheduleId", e.getMessage());
        }
        return (false);
    }

    public boolean getNextScheduleSequenceNo(int holidayId, int dayId, int attractionId, int attractionAreaId, MyInt retInt)
    {
        try
        {
            String lSQL="SELECT IFNULL(MAX(sequenceNo),0) " + "FROM schedule " + "WHERE holidayId = " + holidayId + " " + "AND dayId = " + dayId + " " + "AND attractionId = " + attractionId + " " + "AND attractionAreaId = " + attractionAreaId;

            if(!executeSQLGetInt("getNextScheduleSequenceNo", lSQL, retInt))
                return (false);
            retInt.Value=retInt.Value + 1;
            return (true);
        }
        catch(Exception e)
        {
            ShowError("getNextScheduleSequenceNo", e.getMessage());
        }
        return (false);
    }

    boolean getScheduleList(int holidayId, int dayId, int attractionId, int attractionAreaId, ArrayList<ScheduleItem> al)
    {
        try
        {
            String lSql="SELECT holidayId, dayId, attractionId, attractionAreaId, " +
                    "       scheduleId, sequenceNo, schedType, " +
                    "       schedName, schedPicture, startTimeKnown, startHour,  " +
                    "       startMin, endTimeKnown, endHour, endMin, infoId, noteId, galleryId " +
                    "       FROM Schedule " + "WHERE HolidayId = " + holidayId +
                    " " + "AND DayId = " + dayId + " " + "AND attractionId = " + attractionId +
                    " " + "AND attractionAreaId = " + attractionAreaId + " " + "ORDER BY SequenceNo ";

            Cursor cursor=executeSQLOpenCursor("getScheduleList", lSql);
            if(cursor == null)
                return (false);

            while(cursor.moveToNext())
            {
                ScheduleItem scheduleItem=new ScheduleItem();
                if(!GetScheduleItemFromQuery(cursor, scheduleItem))
                    return (false);

                al.add(scheduleItem);
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("getScheduleList", e.getMessage());
        }
        return (false);
    }

}
