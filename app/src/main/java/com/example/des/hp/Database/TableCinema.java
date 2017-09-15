package com.example.des.hp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.des.hp.Schedule.Cinema.CinemaItem;

class TableCinema extends TableBase
{
    TableCinema(Context context, SQLiteOpenHelper dbHelper)
    {
        super(context, dbHelper);
    }

    public void ShowError(String argFunction, String argMessage)
    {
        super.ShowError("TableBus:" + argFunction, argMessage);
    }

    public boolean onCreate(SQLiteDatabase db)
    {
        try
        {
            String lSQL="CREATE TABLE IF NOT EXISTS cinema " + "( " + "  holidayId         INT(5),  " + "  dayId             INT(5),  " + "  attractionId      INT(5),  " + "  attractionAreaId  INT(5),  " + "  scheduleId        INT(5),  " + "  cinemaName        VARCHAR, " + "  bookingReference  VARCHAR  " + ") ";

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

    boolean addCinemaItem(CinemaItem cinemaItem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            String lSql="INSERT INTO cinema " + "  (holidayId, dayId, attractionId, attractionAreaId, " + "   scheduleId, cinemaName, bookingReference) " + "VALUES " + "(" + cinemaItem.holidayId + "," + cinemaItem.dayId + "," + cinemaItem.attractionId + "," + cinemaItem.attractionAreaId + "," + cinemaItem.scheduleId + "," + MyQuotedString(cinemaItem.cinemaName) + "," + MyQuotedString(cinemaItem.bookingReference) + " " + ")";

            return (executeSQL("addCinemaItem", lSql));
        }
        catch(Exception e)
        {
            ShowError("addCinemaItem", e.getMessage());
        }
        return (false);

    }

    boolean updateCinemaItem(CinemaItem cinemaItem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            if(ItemExists(cinemaItem) == false)
            {
                return (addCinemaItem(cinemaItem));
            }

            String lSQL="UPDATE cinema " + "SET cinemaName = " + MyQuotedString(cinemaItem.cinemaName) + ", " + "    bookingReference = " + MyQuotedString(cinemaItem.bookingReference) + ", " + "    dayId = " + cinemaItem.dayId + ", " + "    attractionId = " + cinemaItem.attractionId + ", " + "    attractionAreaId = " + cinemaItem.attractionAreaId + ", " + "    scheduleId = " + cinemaItem.scheduleId + " " + "WHERE holidayId = " + cinemaItem.holidayId + " " + "AND dayId = " + cinemaItem.origDayId + " " + "AND attractionId = " + cinemaItem.origAttractionId + " " + "AND attractionAreaId = " + cinemaItem.origAttractionAreaId + " " + "AND scheduleId = " + cinemaItem.origScheduleId;

            return (executeSQL("updateCinemaItem", lSQL));
        }
        catch(Exception e)
        {
            ShowError("updateCinemaItem", e.getMessage());
        }
        return (false);

    }

    boolean deleteCinemaItem(CinemaItem cinemaItem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            String lSQL="DELETE FROM cinema " + "WHERE holidayId = " + cinemaItem.holidayId + " " + "AND dayId = " + cinemaItem.dayId + " " + "AND attractionId = " + cinemaItem.attractionId + " " + "AND attractionAreaId = " + cinemaItem.attractionAreaId + " " + "AND scheduleId = " + cinemaItem.scheduleId;


            if(executeSQL("deleteCinemaItem", lSQL) == false)
                return (false);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("deleteCinemaItem", e.getMessage());
        }
        return (false);

    }

    boolean getCinemaItem(int holidayId, int dayId, int attractionId, int attractionAreaId, int scheduleId, CinemaItem litem)
    {
        try
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
            lSQL="SELECT holidayId, dayId, attractionId, attractionAreaId, " + "  scheduleId, cinemaName, bookingReference " + "FROM Cinema " + "WHERE HolidayId = " + holidayId + " " + "AND DayId = " + dayId + " " + "AND attractionId = " + attractionId + " " + "AND attractionAreaId = " + attractionAreaId + " " + "AND ScheduleId = " + scheduleId;

            Cursor cursor=executeSQLOpenCursor("getCinemaItem", lSQL);
            if(cursor != null)
            {
                cursor.moveToFirst();
                if(GetCinemaItemFromQuery(cursor, litem) == false)
                    return (false);
            }
            executeSQLCloseCursor("getCinemaItem");
            return (true);
        }
        catch(Exception e)
        {
            ShowError("getCinemaItem", e.getMessage());
        }
        return (false);

    }

    private boolean GetCinemaItemFromQuery(Cursor cursor, CinemaItem cinemaItem)
    {
        if(IsValid() == false)
            return (false);

        try
        {
            if(cursor.getCount() == 0)
                return (true);

            cinemaItem.holidayId=Integer.parseInt(cursor.getString(0));
            cinemaItem.dayId=Integer.parseInt(cursor.getString(1));
            cinemaItem.attractionId=Integer.parseInt(cursor.getString(2));
            cinemaItem.attractionAreaId=Integer.parseInt(cursor.getString(3));
            cinemaItem.scheduleId=Integer.parseInt(cursor.getString(4));
            cinemaItem.cinemaName=cursor.getString(5);
            cinemaItem.bookingReference=cursor.getString(6);

            cinemaItem.origHolidayId=cinemaItem.holidayId;
            cinemaItem.origDayId=cinemaItem.dayId;
            cinemaItem.origAttractionId=cinemaItem.attractionId;
            cinemaItem.origAttractionAreaId=cinemaItem.attractionAreaId;
            cinemaItem.origScheduleId=cinemaItem.scheduleId;
            cinemaItem.origCinemaName=cinemaItem.cinemaName;
            cinemaItem.origBookingReference=cinemaItem.bookingReference;
            return (true);
        }
        catch(Exception e)
        {
            ShowError("GetCinemaItemFromQuery", e.getMessage());
        }

        return (false);
    }

    private boolean ItemExists(CinemaItem litem)
    {
        if(IsValid() == false)
            return (false);

        try
        {
            String lSQL;
            lSQL="SELECT holidayId, dayId, attractionId, attractionAreaId, " + "  scheduleId " + "FROM Show " + "WHERE HolidayId = " + litem.holidayId + " " + "AND DayId = " + litem.dayId + " " + "AND attractionId = " + litem.attractionId + " " + "AND attractionAreaId = " + litem.attractionAreaId + " " + "AND ScheduleId = " + litem.scheduleId;
            Cursor cursor=executeSQLOpenCursor("ItemExists(cinema)", lSQL);
            if(cursor == null)
                return (false);

            if(cursor.getCount() == 0)
                return (false);
        }
        catch(Exception e)
        {
            ShowError("ItemExists(cinema)", e.getMessage());
        }

        return (true);
    }

}
