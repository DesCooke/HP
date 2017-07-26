package com.example.des.hp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.des.hp.Schedule.Other.OtherItem;
import com.example.des.hp.myutils.MyMessages;

class TableOther extends TableBase
{
    TableOther(Context context, SQLiteOpenHelper dbHelper)
    {
        super(context, dbHelper);
    }

    public void ShowError(String argFunction, String argMessage)
    {
        super.ShowError("TableOther:" + argFunction, argMessage);
    }

    public boolean onCreate(SQLiteDatabase db)
    {
        try
        {
            String lSQL="CREATE TABLE IF NOT EXISTS other " +
                "( " +
                "  holidayId         INT(5),  " +
                "  dayId             INT(5),  " +
                "  attractionId      INT(5),  " +
                "  attractionAreaId  INT(5),  " +
                "  scheduleId        INT(5),  " +
                "  otherName         VARCHAR, " +
                "  bookingReference  VARCHAR  " +
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
            return (true);
        }
        catch(Exception e)
        {
            ShowError("onUpgrade", e.getMessage());
            return (false);
        }
    }

    boolean addOtherItem(OtherItem otherItem)
    {
        if(IsValid() == false)
            return (false);

        String lSql="INSERT INTO other " +
            "  (holidayId, dayId, attractionId, attractionAreaId, " +
            "   scheduleId, otherName, bookingReference) " +
            "VALUES " +
            "(" +
            otherItem.holidayId + "," +
            otherItem.dayId + "," +
            otherItem.attractionId + "," +
            otherItem.attractionAreaId + "," +
            otherItem.scheduleId + "," +
            MyQuotedString(otherItem.otherName) + "," +
            MyQuotedString(otherItem.bookingReference) + " " +
            ")";

        return (executeSQL("addOtherItem", lSql));
    }

    boolean updateOtherItem(OtherItem otherItem)
    {
        if(IsValid() == false)
            return (false);

        String lSQL;
        lSQL="UPDATE other " +
            "SET otherName = " + MyQuotedString(otherItem.otherName) + ", " +
            "    bookingReference = " + MyQuotedString(otherItem.bookingReference) + ", " +
            "    dayId = " + otherItem.dayId + ", " +
            "    attractionId = " + otherItem.attractionId + ", " +
            "    attractionAreaId = " + otherItem.attractionAreaId + ", " +
            "    scheduleId = " + otherItem.scheduleId + " " +
            "WHERE holidayId = " + otherItem.holidayId + " " +
            "AND dayId = " + otherItem.origDayId + " " +
            "AND attractionId = " + otherItem.origAttractionId + " " +
            "AND attractionAreaId = " + otherItem.origAttractionAreaId + " " +
            "AND scheduleId = " + otherItem.origScheduleId;

        return (executeSQL("updateOtherItem", lSQL));
    }

    boolean deleteOtherItem(OtherItem otherItem)
    {
        if(IsValid() == false)
            return (false);

        String lSQL="DELETE FROM other " +
            "WHERE holidayId = " + otherItem.holidayId + " " +
            "AND dayId = " + otherItem.dayId + " " +
            "AND attractionId = " + otherItem.attractionId + " " +
            "AND attractionAreaId = " + otherItem.attractionAreaId + " " +
            "AND scheduleId = " + otherItem.scheduleId;

        if(executeSQL("deleteOtherItem", lSQL) == false)
            return (false);

        return (true);
    }

    boolean getOtherItem(int holidayId, int dayId, int attractionId, int attractionAreaId, int scheduleId, OtherItem litem)
    {
        if(IsValid() == false)
            return (false);

        String lSQL;
        lSQL="SELECT holidayId, dayId, attractionId, attractionAreaId, " +
            "  scheduleId, otherName, bookingReference " +
            "FROM Other " +
            "WHERE HolidayId = " + holidayId + " " +
            "AND DayId = " + dayId + " " +
            "AND attractionId = " + attractionId + " " +
            "AND attractionAreaId = " + attractionAreaId + " " +
            "AND ScheduleId = " + scheduleId;

        Cursor cursor=executeSQLOpenCursor("getOtherItem", lSQL);
        if(cursor != null)
        {
            cursor.moveToFirst();
            if(GetOtherItemFromQuery(cursor, litem) == false)
                return (false);
        }
        executeSQLCloseCursor("getOtherItem");
        return (true);
    }

    private boolean GetOtherItemFromQuery(Cursor cursor, OtherItem otherItem)
    {
        if(IsValid() == false)
            return (false);

        try
        {
            if(cursor.getCount() == 0)
                return (true);

            otherItem.holidayId=Integer.parseInt(cursor.getString(0));
            otherItem.dayId=Integer.parseInt(cursor.getString(1));
            otherItem.attractionId=Integer.parseInt(cursor.getString(2));
            otherItem.attractionAreaId=Integer.parseInt(cursor.getString(3));
            otherItem.scheduleId=Integer.parseInt(cursor.getString(4));
            otherItem.otherName=cursor.getString(5);
            otherItem.bookingReference=cursor.getString(6);

            otherItem.origHolidayId=otherItem.holidayId;
            otherItem.origDayId=otherItem.dayId;
            otherItem.origAttractionId=otherItem.attractionId;
            otherItem.origAttractionAreaId=otherItem.attractionAreaId;
            otherItem.origScheduleId=otherItem.scheduleId;
            otherItem.origOtherName=otherItem.otherName;
            otherItem.origBookingReference=otherItem.bookingReference;
        }
        catch(Exception e)
        {
            ShowError("GetOtherItemFromQuery", e.getMessage());
        }

        return (true);
    }

}
