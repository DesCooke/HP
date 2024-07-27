package com.example.des.hp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.des.hp.Event.EventScheduleItem;
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

    public boolean addScheduleItem(EventScheduleItem eventScheduleItem)
    {
        try
        {
            if(!IsValid())
                return (false);

            if(eventScheduleItem.pictureAssigned) {
                /* if picture name has something in it - it means it came from internal folder */
                if (eventScheduleItem.schedPicture.isEmpty()) {
                    //myMessages().LogMessage("  - Save new image and get a filename...");
                    MyString myString = new MyString();
                    if (!savePicture(eventScheduleItem.holidayId, eventScheduleItem.scheduleBitmap, myString))
                        return (false);
                    eventScheduleItem.schedPicture = myString.Value;
                    //myMessages().LogMessage("  - New filename " + scheduleItem.schedPicture);
                }
            }

            int lStartTimeKnown= eventScheduleItem.startTimeKnown ? 1 : 0;

            int lEndTimeKnown= eventScheduleItem.endTimeKnown ? 1 : 0;

            String lSql="INSERT INTO schedule " + "  (holidayId, dayId, attractionId, attractionAreaId, " + "   scheduleId, sequenceNo, schedType, " + "   schedName, schedPicture, startTimeKnown, startHour, " + "   startMin, endTimeKnown, endHour, endMin, infoId, " + "   noteId, galleryId) " + " VALUES " + "(" + eventScheduleItem.holidayId + "," + eventScheduleItem.dayId + "," + eventScheduleItem.attractionId + "," + eventScheduleItem.attractionAreaId + "," + eventScheduleItem.scheduleId + "," + eventScheduleItem.sequenceNo + "," + eventScheduleItem.schedType + "," + MyQuotedString(eventScheduleItem.schedName) + "," + MyQuotedString(eventScheduleItem.schedPicture) + "," + lStartTimeKnown + ", " + eventScheduleItem.startHour + ", " + eventScheduleItem.startMin + ", " + lEndTimeKnown + ", " + eventScheduleItem.endHour + ", " + eventScheduleItem.endMin + ", " + eventScheduleItem.infoId + ", " + eventScheduleItem.noteId + ", " + eventScheduleItem.galleryId + ")";

            return (executeSQL("addScheduleItem", lSql));

        }
        catch(Exception e)
        {
            ShowError("addScheduleItem", e.getMessage());
        }
        return (false);

    }

    boolean updateScheduleItems(ArrayList<EventScheduleItem> items)
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

    public boolean updateScheduleItem(EventScheduleItem eventScheduleItem)
    {
        try
        {
            if(!IsValid())
                return (false);

            //myMessages().LogMessage("updateScheduleItem:Handling Image");
            if(eventScheduleItem.pictureChanged)
            {
                if (!eventScheduleItem.origPictureAssigned || eventScheduleItem.schedPicture.isEmpty() || eventScheduleItem.schedPicture.compareTo(eventScheduleItem.origSchedPicture) != 0) {

                    if(eventScheduleItem.origPictureAssigned)
                    {
                        //myMessages().LogMessage("  - Original Image was assigned - need to get rid of it");
                        if(!removePicture(eventScheduleItem.holidayId, eventScheduleItem.origSchedPicture))
                            return (false);
                    }

                /* if picture name has something in it - it means it came from internal folder */
                    if(eventScheduleItem.schedPicture.isEmpty())
                    {
                        //myMessages().LogMessage("  - New Image was not from internal folder...");
                        if(eventScheduleItem.pictureAssigned)
                        {
                            //myMessages().LogMessage("  - Save new image and get a filename...");
                            MyString myString=new MyString();
                            if(!savePicture(eventScheduleItem.holidayId, eventScheduleItem.scheduleBitmap, myString))
                                return (false);
                            eventScheduleItem.schedPicture=myString.Value;
                            //myMessages().LogMessage("  - New filename " + scheduleItem.schedPicture);
                        }
                    }
                }
            }


            int lStartTimeKnown= eventScheduleItem.startTimeKnown ? 1 : 0;
            int lEndTimeKnown= eventScheduleItem.endTimeKnown ? 1 : 0;

            if(eventScheduleItem.dayId != eventScheduleItem.origDayId || eventScheduleItem.attractionAreaId != eventScheduleItem.origAttractionAreaId || eventScheduleItem.attractionId != eventScheduleItem.origAttractionId)
            {
                MyInt myInt=new MyInt();
                if(!getNextScheduleSequenceNo(eventScheduleItem.holidayId, eventScheduleItem.dayId, eventScheduleItem.attractionId, eventScheduleItem.attractionAreaId, myInt))
                    return (false);
                eventScheduleItem.sequenceNo=myInt.Value;
                if(!getNextScheduleId(eventScheduleItem.holidayId, eventScheduleItem.dayId, eventScheduleItem.attractionId, eventScheduleItem.attractionAreaId, myInt))
                    return (false);
                eventScheduleItem.scheduleId=myInt.Value;

            }
            String lSQL;
            lSQL="UPDATE schedule " + "SET dayId = " + eventScheduleItem.dayId + ", " + "    attractionId = " + eventScheduleItem.attractionId + ", " + "    attractionAreaId = " + eventScheduleItem.attractionAreaId + ", " + "    schedName = " + MyQuotedString(eventScheduleItem.schedName) + ", " + "    schedPicture = " + MyQuotedString(eventScheduleItem.schedPicture) + ", " + "    startTimeKnown = " + lStartTimeKnown + ", " + "    startHour = " + eventScheduleItem.startHour + ", " + "    startMin = " + eventScheduleItem.startMin + ", " + "    endTimeKnown = " + lEndTimeKnown + ", " + "    endHour = " + eventScheduleItem.endHour + ", " + "    endMin = " + eventScheduleItem.endMin + ", " + "    sequenceNo = " + eventScheduleItem.sequenceNo + ", " + "    scheduleId = " + eventScheduleItem.scheduleId + ", " + "    infoId = " + eventScheduleItem.infoId + ", " + "    noteId = " + eventScheduleItem.noteId + ", " + "    galleryId = " + eventScheduleItem.galleryId + " WHERE holidayId = " + eventScheduleItem.holidayId + " " + "AND dayId = " + eventScheduleItem.origDayId + " " + "AND attractionId = " + eventScheduleItem.origAttractionId + " " + "AND attractionAreaId = " + eventScheduleItem.origAttractionAreaId + " " + "AND scheduleId = " + eventScheduleItem.origScheduleId;

            return (executeSQL("updateScheduleItem", lSQL));

        }
        catch(Exception e)
        {
            ShowError("updateScheduleItem", e.getMessage());
        }
        return (false);

    }

    boolean deleteScheduleItem(EventScheduleItem eventScheduleItem)
    {
        try
        {
            if(!IsValid())
                return (false);

            String lSQL="DELETE FROM schedule " + "WHERE holidayId = " + eventScheduleItem.holidayId + " " + "AND dayId = " + eventScheduleItem.dayId + " " + "AND attractionId = " + eventScheduleItem.attractionId + " " + "AND attractionAreaId = " + eventScheduleItem.attractionAreaId + " " + "AND scheduleId = " + eventScheduleItem.scheduleId;

            if(!eventScheduleItem.schedPicture.isEmpty())
                if(!removePicture(eventScheduleItem.holidayId, eventScheduleItem.schedPicture))
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

    boolean getScheduleItem(int holidayId, int dayId, int attractionId, int attractionAreaId, int scheduleId, EventScheduleItem item)
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

    private boolean GetScheduleItemFromQuery(Cursor cursor, EventScheduleItem eventScheduleItem)
    {
        if(!IsValid())
            return (false);

        try
        {
            if(cursor.getCount() == 0)
                return (true);

            eventScheduleItem.holidayId=Integer.parseInt(cursor.getString(0));
            eventScheduleItem.dayId=Integer.parseInt(cursor.getString(1));
            eventScheduleItem.attractionId=Integer.parseInt(cursor.getString(2));
            eventScheduleItem.attractionAreaId=Integer.parseInt(cursor.getString(3));
            eventScheduleItem.scheduleId=Integer.parseInt(cursor.getString(4));
            eventScheduleItem.sequenceNo=Integer.parseInt(cursor.getString(5));
            eventScheduleItem.schedType=Integer.parseInt(cursor.getString(6));
            eventScheduleItem.schedName=cursor.getString(7);
            eventScheduleItem.schedPicture=cursor.getString(8);
            eventScheduleItem.startTimeKnown=Integer.parseInt(cursor.getString(9)) == 1;
            eventScheduleItem.startHour=Integer.parseInt(cursor.getString(10));
            eventScheduleItem.startMin=Integer.parseInt(cursor.getString(11));
            eventScheduleItem.endTimeKnown=Integer.parseInt(cursor.getString(12)) == 1;
            eventScheduleItem.endHour=Integer.parseInt(cursor.getString(13));
            eventScheduleItem.endMin=Integer.parseInt(cursor.getString(14));
            eventScheduleItem.infoId=Integer.parseInt(cursor.getString(15));
            eventScheduleItem.noteId=Integer.parseInt(cursor.getString(16));
            eventScheduleItem.galleryId=Integer.parseInt(cursor.getString(17));

            eventScheduleItem.origHolidayId= eventScheduleItem.holidayId;
            eventScheduleItem.origDayId= eventScheduleItem.dayId;
            eventScheduleItem.origAttractionId= eventScheduleItem.attractionId;
            eventScheduleItem.origAttractionAreaId= eventScheduleItem.attractionAreaId;
            eventScheduleItem.origScheduleId= eventScheduleItem.scheduleId;
            eventScheduleItem.origSequenceNo= eventScheduleItem.sequenceNo;
            eventScheduleItem.origSchedType= eventScheduleItem.schedType;
            eventScheduleItem.origSchedName= eventScheduleItem.schedName;
            eventScheduleItem.origSchedPicture= eventScheduleItem.schedPicture;
            eventScheduleItem.origStartTimeKnown= eventScheduleItem.startTimeKnown;
            eventScheduleItem.origStartHour= eventScheduleItem.startHour;
            eventScheduleItem.origStartMin= eventScheduleItem.startMin;
            eventScheduleItem.origEndTimeKnown= eventScheduleItem.endTimeKnown;
            eventScheduleItem.origEndHour= eventScheduleItem.endHour;
            eventScheduleItem.origEndMin= eventScheduleItem.endMin;
            eventScheduleItem.origInfoId= eventScheduleItem.infoId;
            eventScheduleItem.origNoteId= eventScheduleItem.noteId;
            eventScheduleItem.origGalleryId= eventScheduleItem.galleryId;

            eventScheduleItem.pictureChanged=false;

            if(!eventScheduleItem.schedPicture.isEmpty())
            {
                eventScheduleItem.pictureAssigned=true;
                eventScheduleItem.origPictureAssigned=true;
            } else
            {
                eventScheduleItem.pictureAssigned=false;
                eventScheduleItem.origPictureAssigned=false;
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

    boolean getScheduleList(int holidayId, int dayId, int attractionId, int attractionAreaId, ArrayList<EventScheduleItem> al)
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
                EventScheduleItem eventScheduleItem =new EventScheduleItem();
                if(!GetScheduleItemFromQuery(cursor, eventScheduleItem))
                    return (false);

                al.add(eventScheduleItem);
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
