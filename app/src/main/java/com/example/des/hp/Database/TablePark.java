package com.example.des.hp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.des.hp.Schedule.Park.ParkItem;

class TablePark extends TableBase
{
    TablePark(Context context, SQLiteOpenHelper dbHelper)
    {
        super(context, dbHelper);
    }

    public void ShowError(String argFunction, String argMessage)
    {
        super.ShowError("TablePark:" + argFunction, argMessage);
    }

    public boolean onCreate(SQLiteDatabase db)
    {
        try
        {
            String lSQL="CREATE TABLE IF NOT EXISTS park " + "( " + "  holidayId         INT(5),  " + "  dayId             INT(5),  " + "  attractionId      INT(5),  " + "  attractionAreaId  INT(5),  " + "  scheduleId        INT(5),  " + "  parkName          VARCHAR, " + "  bookingReference  VARCHAR  " + ") ";

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

    boolean addParkItem(ParkItem parkItem)
    {
        if(IsValid() == false)
            return (false);

        String lSql="INSERT INTO park " + "  (holidayId, dayId, attractionId, attractionAreaId, " + "   scheduleId, parkName, " + "   bookingReference) " + "VALUES " + "(" + parkItem.holidayId + "," + parkItem.dayId + "," + parkItem.attractionId + "," + parkItem.attractionAreaId + "," + parkItem.scheduleId + "," + MyQuotedString(parkItem.parkName) + "," + MyQuotedString(parkItem.bookingReference) + " " + ")";

        return (executeSQL("addParkItem", lSql));
    }

    boolean updateParkItem(ParkItem parkItem)
    {
        if(IsValid() == false)
            return (false);

        if(ItemExists(parkItem) == false)
        {
            return (addParkItem(parkItem));
        }
        String lSQL;
        lSQL="UPDATE park " + "SET parkName = " + MyQuotedString(parkItem.parkName) + ", " + "    bookingReference = " + MyQuotedString(parkItem.bookingReference) + ", " + "    dayId = " + parkItem.dayId + ", " + "    attractionId = " + parkItem.attractionId + ", " + "    attractionAreaId = " + parkItem.attractionAreaId + ", " + "    scheduleId = " + parkItem.scheduleId + " " + "WHERE holidayId = " + parkItem.holidayId + " " + "AND dayId = " + parkItem.origDayId + " " + "AND attractionId = " + parkItem.origAttractionId + " " + "AND attractionAreaId = " + parkItem.origAttractionAreaId + " " + "AND scheduleId = " + parkItem.origScheduleId;

        return (executeSQL("updateParkItem", lSQL));
    }

    boolean deleteParkItem(ParkItem parkItem)
    {
        if(IsValid() == false)
            return (false);

        String lSQL="DELETE FROM park " + "WHERE holidayId = " + parkItem.holidayId + " " + "AND dayId = " + parkItem.dayId + " " + "AND attractionId = " + parkItem.attractionId + " " + "AND attractionAreaId = " + parkItem.attractionAreaId + " " + "AND scheduleId = " + parkItem.scheduleId;

        if(executeSQL("deleteParkItem", lSQL) == false)
            return (false);

        return (true);
    }

    boolean getParkItem(int holidayId, int dayId, int attractionId, int attractionAreaId, int scheduleId, ParkItem litem)
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
        lSQL="SELECT holidayId, dayId, attractionId, attractionAreaId, " + "  scheduleId, parkName, bookingReference " + "FROM Park " + "WHERE HolidayId = " + holidayId + " " + "AND DayId = " + dayId + " " + "AND attractionId = " + attractionId + " " + "AND attractionAreaId = " + attractionAreaId + " " + "AND ScheduleId = " + scheduleId;

        Cursor cursor=executeSQLOpenCursor("getParkItem", lSQL);
        if(cursor != null)
        {
            cursor.moveToFirst();
            if(GetParkItemFromQuery(cursor, litem) == false)
                return (false);
        }
        executeSQLCloseCursor("getParkItem");
        return (true);
    }

    private boolean GetParkItemFromQuery(Cursor cursor, ParkItem parkItem)
    {
        if(IsValid() == false)
            return (false);

        try
        {
            if(cursor.getCount() == 0)
                return (true);

            parkItem.holidayId=Integer.parseInt(cursor.getString(0));
            parkItem.dayId=Integer.parseInt(cursor.getString(1));
            parkItem.attractionId=Integer.parseInt(cursor.getString(2));
            parkItem.attractionAreaId=Integer.parseInt(cursor.getString(3));
            parkItem.scheduleId=Integer.parseInt(cursor.getString(4));
            parkItem.parkName=cursor.getString(5);
            parkItem.bookingReference=cursor.getString(6);

            parkItem.origHolidayId=parkItem.holidayId;
            parkItem.origDayId=parkItem.dayId;
            parkItem.origAttractionId=parkItem.attractionId;
            parkItem.origAttractionAreaId=parkItem.attractionAreaId;
            parkItem.origScheduleId=parkItem.scheduleId;
            parkItem.origParkName=parkItem.parkName;
            parkItem.origBookingReference=parkItem.bookingReference;
        }
        catch(Exception e)
        {
            ShowError("GetParkItemFromQuery", e.getMessage());
        }

        return (true);
    }

    private boolean ItemExists(ParkItem litem)
    {
        if(IsValid() == false)
            return (false);

        try
        {
            String lSQL;
            lSQL="SELECT holidayId, dayId, attractionId, attractionAreaId, " + "  scheduleId " + "FROM Park " + "WHERE HolidayId = " + litem.holidayId + " " + "AND DayId = " + litem.dayId + " " + "AND attractionId = " + litem.attractionId + " " + "AND attractionAreaId = " + litem.attractionAreaId + " " + "AND ScheduleId = " + litem.scheduleId;
            Cursor cursor=executeSQLOpenCursor("ItemExists(park)", lSQL);
            if(cursor == null)
                return (false);

            if(cursor.getCount() == 0)
                return (false);
        }
        catch(Exception e)
        {
            ShowError("ItemExists(park)", e.getMessage());
        }

        return (true);
    }

}
