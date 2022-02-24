package com.example.des.hp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.des.hp.Schedule.Bus.BusItem;

class TableBus extends TableBase
{
    TableBus(Context context, SQLiteOpenHelper dbHelper)
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
            String lSQL="CREATE TABLE IF NOT EXISTS bus " +
                    "( " + "  holidayId         INT(5),  " +
                    "  dayId             INT(5),  " +
                    "  attractionId      INT(5),  " +
                    "  attractionAreaId  INT(5),  " +
                    "  scheduleId        INT(5),  " +
                    "  bookingReference  VARCHAR  " + ") ";

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
            return (false);
        }
    }

    boolean addBusItem(BusItem busItem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            String lSql="INSERT INTO bus " + "  (holidayId, dayId, attractionId, attractionAreaId, " + "   scheduleId, bookingReference) " + "VALUES " + "(" + busItem.holidayId + "," + busItem.dayId + "," + busItem.attractionId + "," + busItem.attractionAreaId + "," + busItem.scheduleId + "," + MyQuotedString(busItem.bookingReference) + " " + ")";

            return (executeSQL("addBusItem", lSql));
        }
        catch(Exception e)
        {
            ShowError("addBusItem", e.getMessage());
        }
        return (false);

    }

    boolean updateBusItem(BusItem busItem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            if(ItemExists(busItem) == false)
            {
                return (addBusItem(busItem));
            }

            String lSQL;
            lSQL="UPDATE bus " + "SET bookingReference = " + MyQuotedString(busItem.bookingReference) + ", " + "    dayId = " + busItem.dayId + ", " + "    attractionId = " + busItem.attractionId + ", " + "    attractionAreaId = " + busItem.attractionAreaId + ", " + "    scheduleId = " + busItem.scheduleId + " " + "WHERE holidayId = " + busItem.holidayId + " " + "AND dayId = " + busItem.origDayId + " " + "AND attractionId = " + busItem.origAttractionId + " " + "AND attractionAreaId = " + busItem.origAttractionAreaId + " " + "AND scheduleId = " + busItem.origScheduleId;

            return (executeSQL("updateBusItem", lSQL));
        }
        catch(Exception e)
        {
            ShowError("updateBusItem", e.getMessage());
        }
        return (false);

    }

    boolean deleteBusItem(BusItem busItem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            String lSQL="DELETE FROM bus " + "WHERE holidayId = " + busItem.holidayId + " " + "AND dayId = " + busItem.dayId + " " + "AND attractionId = " + busItem.attractionId + " " + "AND attractionAreaId = " + busItem.attractionAreaId + " " + "AND scheduleId = " + busItem.scheduleId;

            if(executeSQL("deleteBusItem", lSQL) == false)
                return (false);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("deleteBusItem", e.getMessage());
        }
        return (false);

    }

    boolean getBusItem(int holidayId, int dayId, int attractionId, int attractionAreaId, int scheduleId, BusItem litem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            String lSQL;
            lSQL="SELECT holidayId, dayId, attractionId, attractionAreaId, " + "       scheduleId, bookingReference " + "FROM Bus " + "WHERE HolidayId = " + holidayId + " " + "AND DayId = " + dayId + " " + "AND attractionId = " + attractionId + " " + "AND attractionAreaId = " + attractionAreaId + " " + "AND ScheduleId = " + scheduleId;

            Cursor cursor=executeSQLOpenCursor("getBusItem", lSQL);
            if(cursor != null)
            {
                cursor.moveToFirst();
                if(GetBusItemFromQuery(cursor, litem) == false)
                    return (false);
            }
            executeSQLCloseCursor("getBusItem");
            return (true);
        }
        catch(Exception e)
        {
            ShowError("getBusItem", e.getMessage());
        }
        return (false);

    }

    private boolean GetBusItemFromQuery(Cursor cursor, BusItem busItem)
    {
        if(IsValid() == false)
            return (true);

        try
        {
            if(cursor.getCount() == 0)
                return (true);

            busItem.holidayId=Integer.parseInt(cursor.getString(0));
            busItem.dayId=Integer.parseInt(cursor.getString(1));
            busItem.attractionId=Integer.parseInt(cursor.getString(2));
            busItem.attractionAreaId=Integer.parseInt(cursor.getString(3));
            busItem.scheduleId=Integer.parseInt(cursor.getString(4));
            busItem.bookingReference=cursor.getString(5);

            busItem.origHolidayId=busItem.holidayId;
            busItem.origDayId=busItem.dayId;
            busItem.origAttractionId=busItem.attractionId;
            busItem.origAttractionAreaId=busItem.attractionAreaId;
            busItem.origScheduleId=busItem.scheduleId;
            busItem.origBookingReference=busItem.bookingReference;

            return (true);
        }
        catch(Exception e)
        {
            ShowError("GetBusItemFromQuery", e.getMessage());
        }

        return (false);
    }

    private boolean ItemExists(BusItem litem)
    {
        if(IsValid() == false)
            return (false);

        try
        {
            String lSQL;
            lSQL="SELECT holidayId, dayId, attractionId, attractionAreaId, " + "  scheduleId " + "FROM Show " + "WHERE HolidayId = " + litem.holidayId + " " + "AND DayId = " + litem.dayId + " " + "AND attractionId = " + litem.attractionId + " " + "AND attractionAreaId = " + litem.attractionAreaId + " " + "AND ScheduleId = " + litem.scheduleId;
            Cursor cursor=executeSQLOpenCursor("ItemExists(bus)", lSQL);
            if(cursor == null)
                return (false);

            if(cursor.getCount() == 0)
                return (false);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("ItemExists(bus)", e.getMessage());
        }

        return (false);
    }

    boolean createSample(int lHolidayId, int lDayId, int lAttractionId, int lAttractionAreaId, int lScheduleId)
    {
        try
        {
            BusItem item=new BusItem();

            item.holidayId=lHolidayId;
            item.dayId=lDayId;
            item.attractionId=lAttractionId;
            item.attractionAreaId=lAttractionAreaId;
            item.scheduleId=lScheduleId;
            item.bookingReference="Test Booking Ref";

            if(!addBusItem(item))
                return (false);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("createSample", e.getMessage());
        }
        return (false);
    }


}
