package com.example.des.hp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.des.hp.Schedule.Parade.ParadeItem;
import com.example.des.hp.Schedule.Show.ShowItem;
import com.example.des.hp.myutils.MyMessages;

class TableParade extends TableBase
{
    TableParade(Context context, SQLiteOpenHelper dbHelper)
    {
        super(context, dbHelper);
    }

    public void ShowError(String argFunction, String argMessage)
    {
        super.ShowError("TableParade:" + argFunction, argMessage);
    }

    public boolean onCreate(SQLiteDatabase db)
    {
        try
        {
            String lSQL="CREATE TABLE IF NOT EXISTS parade " +
                "( " +
                "  holidayId         INT(5),  " +
                "  dayId             INT(5),  " +
                "  attractionId      INT(5),  " +
                "  attractionAreaId  INT(5),  " +
                "  scheduleId        INT(5),  " +
                "  paradeName        VARCHAR, " +
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

    boolean addParadeItem(ParadeItem paradeItem)
    {
        if(IsValid() == false)
            return (false);

        String lSql="INSERT INTO parade " +
            "  (holidayId, dayId, attractionId, attractionAreaId, " +
            "   scheduleId, paradeName, bookingReference) " +
            "VALUES " +
            "(" +
            paradeItem.holidayId + "," +
            paradeItem.dayId + "," +
            paradeItem.attractionId + "," +
            paradeItem.attractionAreaId + "," +
            paradeItem.scheduleId + "," +
            MyQuotedString(paradeItem.paradeName) + "," +
            MyQuotedString(paradeItem.bookingReference) + " " +
            ")";

        return (executeSQL("addParadeItem", lSql));
    }

    boolean updateParadeItem(ParadeItem paradeItem)
    {
        if(IsValid() == false)
            return (false);

        if(ItemExists(paradeItem)==false)
        {
            return(addParadeItem(paradeItem));
        }
        String lSQL;
        lSQL="UPDATE parade " +
            "SET paradeName = " + MyQuotedString(paradeItem.paradeName) + ", " +
            "    bookingReference = " + MyQuotedString(paradeItem.bookingReference) + ", " +
            "    dayId = " + paradeItem.dayId + ", " +
            "    attractionId = " + paradeItem.attractionId + ", " +
            "    attractionAreaId = " + paradeItem.attractionAreaId + ", " +
            "    scheduleId = " + paradeItem.scheduleId + " " +
            "WHERE holidayId = " + paradeItem.holidayId + " " +
            "AND dayId = " + paradeItem.origDayId + " " +
            "AND attractionId = " + paradeItem.origAttractionId + " " +
            "AND attractionAreaId = " + paradeItem.origAttractionAreaId + " " +
            "AND scheduleId = " + paradeItem.origScheduleId;

        return (executeSQL("updateParadeItem", lSQL));
    }

    boolean deleteParadeItem(ParadeItem paradeItem)
    {
        if(IsValid() == false)
            return (false);

        String lSQL="DELETE FROM parade " +
            "WHERE holidayId = " + paradeItem.holidayId + " " +
            "AND dayId = " + paradeItem.dayId + " " +
            "AND attractionId = " + paradeItem.attractionId + " " +
            "AND attractionAreaId = " + paradeItem.attractionAreaId + " " +
            "AND scheduleId = " + paradeItem.scheduleId;

        if(executeSQL("deleteParadeItem", lSQL) == false)
            return (false);

        return (true);
    }

    boolean getParadeItem(int holidayId, int dayId, int attractionId, int attractionAreaId, int scheduleId, ParadeItem litem)
    {
        if(IsValid() == false)
            return (false);

        litem.holidayId=holidayId;
        litem.dayId=dayId;
        litem.attractionId=attractionId;
        litem.attractionAreaId=attractionAreaId;
        litem.scheduleId=scheduleId;
        litem.origHolidayId=holidayId;
        litem.origDayId=dayId;
        litem.origAttractionId=attractionId;
        litem.origAttractionAreaId=attractionAreaId;
        litem.origScheduleId=scheduleId;

        String lSQL;
        lSQL="SELECT holidayId, dayId, attractionId, attractionAreaId, " +
            "  scheduleId, paradeName, bookingReference " +
            "FROM Parade " +
            "WHERE HolidayId = " + holidayId + " " +
            "AND DayId = " + dayId + " " +
            "AND attractionId = " + attractionId + " " +
            "AND attractionAreaId = " + attractionAreaId + " " +
            "AND ScheduleId = " + scheduleId;

        Cursor cursor=executeSQLOpenCursor("getParadeItem", lSQL);
        if(cursor != null)
        {
            cursor.moveToFirst();
            if(GetParadeItemFromQuery(cursor, litem) == false)
                return (false);
        }
        executeSQLCloseCursor("getParadeItem");
        return (true);
    }

    private boolean GetParadeItemFromQuery(Cursor cursor, ParadeItem paradeItem)
    {
        if(IsValid() == false)
            return (false);

        try
        {
            if(cursor.getCount() == 0)
                return (true);

            paradeItem.holidayId=Integer.parseInt(cursor.getString(0));
            paradeItem.dayId=Integer.parseInt(cursor.getString(1));
            paradeItem.attractionId=Integer.parseInt(cursor.getString(2));
            paradeItem.attractionAreaId=Integer.parseInt(cursor.getString(3));
            paradeItem.scheduleId=Integer.parseInt(cursor.getString(4));
            paradeItem.paradeName=cursor.getString(5);
            paradeItem.bookingReference=cursor.getString(6);

            paradeItem.origHolidayId=paradeItem.holidayId;
            paradeItem.origDayId=paradeItem.dayId;
            paradeItem.origAttractionId=paradeItem.attractionId;
            paradeItem.origAttractionAreaId=paradeItem.attractionAreaId;
            paradeItem.origScheduleId=paradeItem.scheduleId;
            paradeItem.origParadeName=paradeItem.paradeName;
            paradeItem.origBookingReference=paradeItem.bookingReference;
        }
        catch(Exception e)
        {
            ShowError("GetParadeItemFromQuery", e.getMessage());
        }

        return (true);
    }

    private boolean ItemExists(ParadeItem litem)
    {
        if(IsValid() == false)
            return (false);

        try
        {
            String lSQL;
            lSQL="SELECT holidayId, dayId, attractionId, attractionAreaId, " +
                "  scheduleId " +
                "FROM Parade " +
                "WHERE HolidayId = " + litem.holidayId + " " +
                "AND DayId = " + litem.dayId + " " +
                "AND attractionId = " + litem.attractionId + " " +
                "AND attractionAreaId = " + litem.attractionAreaId + " " +
                "AND ScheduleId = " + litem.scheduleId;
            Cursor cursor=executeSQLOpenCursor("ItemExists(parade)", lSQL);
            if(cursor == null)
                return(false);

            if(cursor.getCount() == 0)
                return (false);
        }
        catch(Exception e)
        {
            ShowError("ItemExists(parade)", e.getMessage());
        }

        return (true);
    }

}
