package com.example.des.hp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.des.hp.Day.DayItem;
import com.example.des.hp.Schedule.ScheduleItem;
import com.example.des.hp.myutils.MyInt;
import com.example.des.hp.myutils.MyMessages;
import com.example.des.hp.myutils.MyString;

import java.util.ArrayList;

import static java.lang.Math.abs;

class TableSchedule extends TableBase
{
    TableSchedule(Context context, SQLiteOpenHelper dbHelper, MyMessages myMessages)
    {
        super(context, dbHelper, myMessages);
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
                "( " +
                "  holidayId        INT(5),  " +
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
                "  galleryId        INT(5),  " +
                "  sygicId          INT(5)   " +
                ") ";

            db.execSQL(lSQL);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
            return (false);
        }
    }

    public boolean onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        try
        {
            if(oldVersion == 35 && newVersion == 36)
            {
                db.execSQL("ALTER TABLE schedule ADD COLUMN noteId INT(5) DEFAULT 0");
                db.execSQL("ALTER TABLE schedule ADD COLUMN galleryId INT(5) DEFAULT 0");
                db.execSQL("ALTER TABLE schedule ADD COLUMN sygicId INT(5) DEFAULT 0");

                db.execSQL("UPDATE schedule SET noteId = 0");
                db.execSQL("UPDATE schedule SET galleryId = 0");
                db.execSQL("UPDATE schedule SET sygicId = 0");
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("onUpgrade", e.getMessage());
            return (false);
        }
    }

    public boolean addScheduleItem(ScheduleItem scheduleItem)
    {
        if(IsValid() == false)
            return (false);

        scheduleItem.schedPicture="";
        if(scheduleItem.pictureAssigned)
        {
            MyString myString=new MyString();
            if(savePicture(scheduleItem.scheduleBitmap, myString) == false)
                return (false);
            scheduleItem.schedPicture=myString.Value;
        }

        int lStartTimeKnown=scheduleItem.startTimeKnown ? 1 : 0;

        int lEndTimeKnown=scheduleItem.endTimeKnown ? 1 : 0;

        String lSql="INSERT INTO schedule " +
            "  (holidayId, dayId, attractionId, attractionAreaId, " +
            "   scheduleId, sequenceNo, schedType, " +
            "   schedName, schedPicture, startTimeKnown, startHour, " +
            "   startMin, endTimeKnown, endHour, endMin, infoId, " +
            "   noteId, galleryId, sygicId) " +
            " VALUES " +
            "(" +
            scheduleItem.holidayId + "," +
            scheduleItem.dayId + "," +
            scheduleItem.attractionId + "," +
            scheduleItem.attractionAreaId + "," +
            scheduleItem.scheduleId + "," +
            scheduleItem.sequenceNo + "," +
            scheduleItem.schedType + "," +
            MyQuotedString(scheduleItem.schedName) + "," +
            MyQuotedString(scheduleItem.schedPicture) + "," +
            lStartTimeKnown + ", " +
            scheduleItem.startHour + ", " +
            scheduleItem.startMin + ", " +
            lEndTimeKnown + ", " +
            scheduleItem.endHour + ", " +
            scheduleItem.endMin + ", " +
            scheduleItem.infoId + ", " +
            scheduleItem.noteId + ", " +
            scheduleItem.galleryId + ", " +
            scheduleItem.sygicId + " " +
            ")";

        return (executeSQL("addScheduleItem", lSql));
    }

    public boolean getScheduledTimes(DayItem dayItem)
    {

        String lSQL="SELECT StartTimeKnown, StartHour, StartMin, EndTimeKnown, EndHour, EndMin " +
            "FROM schedule " +
            "WHERE holidayId = " + dayItem.holidayId + " " +
            "AND dayId = " + dayItem.dayId;
        Cursor cursor=executeSQLOpenCursor("getScheduledTimes", lSQL);
        if(cursor == null)
            return (false);

        if(cursor.getCount() == 0)
            return (true);

        int lMinMinutes=86400;
        int lMaxMinutes=0;

        while(cursor.moveToNext())
        {
            int lStartTimeKnown=Integer.parseInt(cursor.getString(0));
            int lStartHour=Integer.parseInt(cursor.getString(1));
            int lStartMin=Integer.parseInt(cursor.getString(2));
            int lEndTimeKnown=Integer.parseInt(cursor.getString(3));
            int lEndHour=Integer.parseInt(cursor.getString(4));
            int lEndMin=Integer.parseInt(cursor.getString(5));

            if(lStartTimeKnown == 1)
            {
                int lMinutes=(lStartHour * 60) + lStartMin;
                if(lMinutes < lMinMinutes)
                    lMinMinutes=lMinutes;
                if(lMinutes > lMaxMinutes)
                    lMaxMinutes=lMinutes;
            }
            if(lEndTimeKnown == 1)
            {
                int lMinutes=(lEndHour * 60) + lEndMin;
                if(lMinutes < lMinMinutes)
                    lMinMinutes=lMinutes;
                if(lMinutes > lMaxMinutes)
                    lMaxMinutes=lMinutes;
            }
        }
        cursor.close();

        dayItem.startOfDayHours=-1;
        dayItem.startOfDayMins=-1;
        if(lMinMinutes != 86400)
        {
            dayItem.startOfDayHours=lMinMinutes / 60;
            dayItem.startOfDayMins=lMinMinutes - (dayItem.startOfDayHours * 60);
        }
        dayItem.endOfDayHours=-1;
        dayItem.endOfDayMins=-1;
        if(lMaxMinutes != 0)
        {
            dayItem.endOfDayHours=lMaxMinutes / 60;
            dayItem.endOfDayMins=lMaxMinutes - (dayItem.endOfDayHours * 60);
        }
        if(lMinMinutes != 86400 && lMaxMinutes != 0)
        {
            int lTotalMins=abs(lMaxMinutes - lMinMinutes);
            dayItem.totalHours=lTotalMins / 60;
            dayItem.totalMins=lTotalMins - (dayItem.totalHours * 60);
        }
        return (true);
    }

    boolean updateScheduleItems(ArrayList<ScheduleItem> items)
    {
        if(IsValid() == false)
            return (false);

        if(items == null)
            return (false);

        for(int i=0; i < items.size(); i++)
        {
            if(items.get(i).sequenceNo != items.get(i).origSequenceNo)
            {
                if(updateScheduleItem(items.get(i)) == false)
                    return (false);
            }
        }
        return (true);
    }

    public boolean updateScheduleItem(ScheduleItem scheduleItem)
    {
        if(IsValid() == false)
            return (false);

        // something has happened to the picture either selected a new one or cleared an
        // existing one
        if(scheduleItem.pictureChanged)
        {
            if(scheduleItem.origPictureAssigned)
                if(removePicture(scheduleItem.origSchedPicture) == false)
                    return (false);
            scheduleItem.schedPicture="";
            if(scheduleItem.pictureAssigned)
            {
                MyString myString=new MyString();
                if(savePicture(scheduleItem.scheduleBitmap, myString) == false)
                    return (false);
                scheduleItem.schedPicture=myString.Value;
            }
        }

        int lStartTimeKnown=scheduleItem.startTimeKnown ? 1 : 0;
        int lEndTimeKnown=scheduleItem.endTimeKnown ? 1 : 0;

        if(scheduleItem.dayId != scheduleItem.origDayId ||
            scheduleItem.attractionAreaId != scheduleItem.origAttractionAreaId ||
            scheduleItem.attractionId != scheduleItem.origAttractionId)
        {
            MyInt myInt=new MyInt();
            if(getNextScheduleSequenceNo(scheduleItem.holidayId, scheduleItem.dayId, scheduleItem.attractionId, scheduleItem.attractionAreaId, myInt) == false)
                return (false);
            scheduleItem.sequenceNo=myInt.Value;
            if(getNextScheduleId(scheduleItem.holidayId, scheduleItem.dayId, scheduleItem.attractionId, scheduleItem.attractionAreaId, myInt) == false)
                return (false);
            scheduleItem.scheduleId=myInt.Value;

            if(scheduleItem.flightItem != null)
                scheduleItem.flightItem.scheduleId=scheduleItem.scheduleId;
            if(scheduleItem.hotelItem != null)
                scheduleItem.hotelItem.scheduleId=scheduleItem.scheduleId;
            if(scheduleItem.restaurantItem != null)
                scheduleItem.restaurantItem.scheduleId=scheduleItem.scheduleId;
            if(scheduleItem.rideItem != null)
                scheduleItem.rideItem.scheduleId=scheduleItem.scheduleId;
            if(scheduleItem.showItem != null)
                scheduleItem.showItem.scheduleId=scheduleItem.scheduleId;
            if(scheduleItem.hotelItem != null)
                scheduleItem.hotelItem.scheduleId=scheduleItem.scheduleId;
            if(scheduleItem.busItem != null)
                scheduleItem.busItem.scheduleId=scheduleItem.scheduleId;
            if(scheduleItem.cinemaItem != null)
                scheduleItem.cinemaItem.scheduleId=scheduleItem.scheduleId;
            if(scheduleItem.paradeItem != null)
                scheduleItem.paradeItem.scheduleId=scheduleItem.scheduleId;
            if(scheduleItem.parkItem != null)
                scheduleItem.parkItem.scheduleId=scheduleItem.scheduleId;
            if(scheduleItem.otherItem != null)
                scheduleItem.otherItem.scheduleId=scheduleItem.scheduleId;

        }
        String lSQL;
        lSQL="UPDATE schedule " +
            "SET dayId = " + scheduleItem.dayId + ", " +
            "    attractionId = " + scheduleItem.attractionId + ", " +
            "    attractionAreaId = " + scheduleItem.attractionAreaId + ", " +
            "    schedName = " + MyQuotedString(scheduleItem.schedName) + ", " +
            "    schedPicture = " + MyQuotedString(scheduleItem.schedPicture) + ", " +
            "    startTimeKnown = " + lStartTimeKnown + ", " +
            "    startHour = " + scheduleItem.startHour + ", " +
            "    startMin = " + scheduleItem.startMin + ", " +
            "    endTimeKnown = " + lEndTimeKnown + ", " +
            "    endHour = " + scheduleItem.endHour + ", " +
            "    endMin = " + scheduleItem.endMin + ", " +
            "    sequenceNo = " + scheduleItem.sequenceNo + ", " +
            "    scheduleId = " + scheduleItem.scheduleId + ", " +
            "    infoId = " + scheduleItem.infoId + ", " +
            "    noteId = " + scheduleItem.noteId + ", " +
            "    galleryId = " + scheduleItem.galleryId + ", " +
            "    sygicId = " + scheduleItem.sygicId + " " +
            "WHERE holidayId = " + scheduleItem.holidayId + " " +
            "AND dayId = " + scheduleItem.origDayId + " " +
            "AND attractionId = " + scheduleItem.origAttractionId + " " +
            "AND attractionAreaId = " + scheduleItem.origAttractionAreaId + " " +
            "AND scheduleId = " + scheduleItem.origScheduleId;

        return (executeSQL("updateScheduleItem", lSQL));
    }

    public boolean deleteSchedule(int holidayId, int dayId, int attractionId, int attractionAreaId)
    {
        ArrayList<ScheduleItem> scheduleList=new ArrayList<ScheduleItem>();
        if(getScheduleList(holidayId, dayId, attractionId, attractionAreaId, scheduleList) == false)
            return (false);

        for(ScheduleItem scheduleItem : scheduleList)
            if(deleteScheduleItem(scheduleItem) == false)
                return (false);

        return (true);
    }

    public boolean deleteScheduleItem(ScheduleItem scheduleItem)

    {
        if(IsValid() == false)
            return (false);

        String lSQL="DELETE FROM schedule " +
            "WHERE holidayId = " + scheduleItem.holidayId + " " +
            "AND dayId = " + scheduleItem.dayId + " " +
            "AND attractionId = " + scheduleItem.attractionId + " " +
            "AND attractionAreaId = " + scheduleItem.attractionAreaId + " " +
            "AND scheduleId = " + scheduleItem.scheduleId;

        if(executeSQL("deleteScheduleItem", lSQL) == false)
            return (false);

        if(scheduleItem.schedPicture.length() > 0)
            if(removePicture(scheduleItem.schedPicture) == false)
                return (false);

        return (true);
    }

    public boolean getScheduleItem(int holidayId, int dayId, int attractionId, int attractionAreaId, int scheduleId, ScheduleItem litem)
    {
        if(IsValid() == false)
            return (false);

        String lSQL;
        lSQL="SELECT holidayId, dayId, attractionId, attractionAreaId, " +
            "       scheduleId, sequenceNo, schedType, " +
            "       schedName, schedPicture, startTimeKnown, startHour,  " +
            "       startMin, endTimeKnown, endHour, endMin, infoId, noteId, galleryId, " +
            "       sygicId " +
            "FROM Schedule " +
            "WHERE HolidayId = " + holidayId + " " +
            "AND DayId = " + dayId + " " +
            "AND attractionId = " + attractionId + " " +
            "AND attractionAreaId = " + attractionAreaId + " " +
            "AND ScheduleId = " + scheduleId;

        Cursor cursor=executeSQLOpenCursor("getScheduleItem", lSQL);
        if(cursor != null)
        {
            cursor.moveToFirst();
            if(GetScheduleItemFromQuery(cursor, litem) == false)
                return (false);
        }
        executeSQLCloseCursor("getScheduleItem");

        return (true);
    }

    private boolean GetScheduleItemFromQuery(Cursor cursor, ScheduleItem scheduleItem)
    {
        if(IsValid() == false)
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
            scheduleItem.sygicId=Integer.parseInt(cursor.getString(18));

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
            scheduleItem.origSygicId=scheduleItem.sygicId;

            scheduleItem.pictureChanged=false;

            if(scheduleItem.schedPicture.length() > 0)
            {
                scheduleItem.pictureAssigned=true;
                scheduleItem.origPictureAssigned=true;
            } else
            {
                scheduleItem.pictureAssigned=false;
                scheduleItem.origPictureAssigned=false;
            }
        }
        catch(Exception e)
        {
            ShowError("GetScheduleItemFromQuery", e.getMessage());
        }

        return (true);
    }

    public boolean getNextScheduleId(int holidayId, int dayId, int attractionId, int attractionAreaId, MyInt retInt)
    {
        String lSQL="SELECT IFNULL(MAX(scheduleId),0) " +
            "FROM schedule " +
            "WHERE holidayId = " + holidayId + " " +
            "AND dayId = " + dayId + " " +
            "AND attractionId = " + attractionId + " " +
            "AND attractionAreaId = " + attractionAreaId;

        if(executeSQLGetInt("getNextScheduleId", lSQL, retInt) == false)
            return (false);
        retInt.Value=retInt.Value + 1;
        return (true);
    }

    public boolean getNextScheduleSequenceNo(int holidayId, int dayId, int attractionId, int attractionAreaId, MyInt retInt)
    {
        String lSQL="SELECT IFNULL(MAX(sequenceNo),0) " +
            "FROM schedule " +
            "WHERE holidayId = " + holidayId + " " +
            "AND dayId = " + dayId + " " +
            "AND attractionId = " + attractionId + " " +
            "AND attractionAreaId = " + attractionAreaId;

        if(executeSQLGetInt("getNextScheduleSequenceNo", lSQL, retInt) == false)
            return (false);
        retInt.Value=retInt.Value + 1;
        return (true);
    }


    boolean getScheduleList(int holidayId, int dayId, int attractionId, int attractionAreaId, ArrayList<ScheduleItem> al)
    {
        String lSql="SELECT holidayId, dayId, attractionId, attractionAreaId, " +
            "       scheduleId, sequenceNo, schedType, " +
            "       schedName, schedPicture, startTimeKnown, startHour,  " +
            "       startMin, endTimeKnown, endHour, endMin, infoId, noteId, galleryId, " +
            "       sygicId " +
            "FROM Schedule " +
            "WHERE HolidayId = " + holidayId + " " +
            "AND DayId = " + dayId + " " +
            "AND attractionId = " + attractionId + " " +
            "AND attractionAreaId = " + attractionAreaId + " " +
            "ORDER BY SequenceNo ";

        Cursor cursor=executeSQLOpenCursor("getScheduleList", lSql);
        if(cursor == null)
            return (false);

        while(cursor.moveToNext())
        {
            ScheduleItem scheduleItem=new ScheduleItem();
            if(GetScheduleItemFromQuery(cursor, scheduleItem) == false)
                return (false);

            al.add(scheduleItem);
        }
        return (true);
    }

    boolean clearNote(int holidayId, int noteId)
    {
        if(IsValid()==false)
            return(false);

        String l_SQL =
            "UPDATE schedule SET noteId = 0 " +
                "WHERE holidayId = " + holidayId + " " +
                "AND noteId = " + noteId;

        if(executeSQL("clearNote", l_SQL)==false)
            return(false);

        return(true);
    }

}
