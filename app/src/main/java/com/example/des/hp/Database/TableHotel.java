package com.example.des.hp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.des.hp.Schedule.Hotel.HotelItem;

class TableHotel extends TableBase
{
    TableHotel(Context context, SQLiteOpenHelper dbHelper)
    {
        super(context, dbHelper);
    }

    public void ShowError(String argFunction, String argMessage)
    {
        super.ShowError("TableHotel:" + argFunction, argMessage);
    }

    public boolean onCreate(SQLiteDatabase db)
    {
        try
        {
            String lSQL="CREATE TABLE IF NOT EXISTS hotel " + "( " + "  holidayId         INT(5),  " + "  dayId             INT(5),  " + "  attractionId      INT(5),  " + "  attractionAreaId  INT(5),  " + "  scheduleId        INT(5),  " + "  hotelName         VARCHAR, " + "  bookingReference  VARCHAR  " + ") ";

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

    boolean addHotelItem(HotelItem hotelItem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            String lSql="INSERT INTO hotel " + "  (holidayId, dayId, attractionId, attractionAreaId, " + "   scheduleId, hotelName, bookingReference) " + "VALUES " + "(" + hotelItem.holidayId + "," + hotelItem.dayId + "," + hotelItem.attractionId + "," + hotelItem.attractionAreaId + "," + hotelItem.scheduleId + "," + MyQuotedString(hotelItem.hotelName) + "," + MyQuotedString(hotelItem.bookingReference) + " " + ")";

            return (executeSQL("addHotelItem", lSql));
        }
        catch(Exception e)
        {
            ShowError("addHotelItem", e.getMessage());
        }
        return (false);

    }

    boolean updateHotelItem(HotelItem hotelItem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            if(ItemExists(hotelItem) == false)
            {
                return (addHotelItem(hotelItem));
            }
            String lSQL;
            lSQL="UPDATE hotel " + "SET hotelName = " + MyQuotedString(hotelItem.hotelName) + ", " + "    bookingReference = " + MyQuotedString(hotelItem.bookingReference) + ", " + "    dayId = " + hotelItem.dayId + ", " + "    attractionId = " + hotelItem.attractionId + ", " + "    attractionAreaId = " + hotelItem.attractionAreaId + ", " + "    scheduleId = " + hotelItem.scheduleId + " " + "WHERE holidayId = " + hotelItem.holidayId + " " + "AND dayId = " + hotelItem.origDayId + " " + "AND attractionId = " + hotelItem.origAttractionId + " " + "AND attractionAreaId = " + hotelItem.origAttractionAreaId + " " + "AND scheduleId = " + hotelItem.origScheduleId;

            return (executeSQL("updateHotelItem", lSQL));
        }
        catch(Exception e)
        {
            ShowError("updateHotelItem", e.getMessage());
        }
        return (false);

    }

    boolean deleteHotelItem(HotelItem hotelItem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            String lSQL="DELETE FROM hotel " + "WHERE holidayId = " + hotelItem.holidayId + " " + "AND dayId = " + hotelItem.dayId + " " + "AND attractionId = " + hotelItem.attractionId + " " + "AND attractionAreaId = " + hotelItem.attractionAreaId + " " + "AND scheduleId = " + hotelItem.scheduleId;

            if(executeSQL("deleteHotelItem", lSQL) == false)
                return (false);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("deleteHotelItem", e.getMessage());
        }
        return (false);

    }

    boolean getHotelItem(int holidayId, int dayId, int attractionId, int attractionAreaId, int scheduleId, HotelItem litem)
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
            lSQL="SELECT holidayId, dayId, attractionId, attractionAreaId, " + "  scheduleId, hotelName, bookingReference " + "FROM Hotel " + "WHERE HolidayId = " + holidayId + " " + "AND DayId = " + dayId + " " + "AND attractionId = " + attractionId + " " + "AND attractionAreaId = " + attractionAreaId + " " + "AND ScheduleId = " + scheduleId;

            Cursor cursor=executeSQLOpenCursor("getHotelItem", lSQL);
            if(cursor != null)
            {
                cursor.moveToFirst();
                if(GetHotelItemFromQuery(cursor, litem) == false)
                    return (false);
            }
            executeSQLCloseCursor("getHotelItem");
            return (true);
        }
        catch(Exception e)
        {
            ShowError("getHotelItem", e.getMessage());
        }
        return (false);
    }

    private boolean GetHotelItemFromQuery(Cursor cursor, HotelItem hotelItem)
    {
        if(IsValid() == false)
            return (false);

        try
        {
            if(cursor.getCount() == 0)
                return (true);

            hotelItem.holidayId=Integer.parseInt(cursor.getString(0));
            hotelItem.dayId=Integer.parseInt(cursor.getString(1));
            hotelItem.attractionId=Integer.parseInt(cursor.getString(2));
            hotelItem.attractionAreaId=Integer.parseInt(cursor.getString(3));
            hotelItem.scheduleId=Integer.parseInt(cursor.getString(4));
            hotelItem.hotelName=cursor.getString(5);
            hotelItem.bookingReference=cursor.getString(6);

            hotelItem.origHolidayId=hotelItem.holidayId;
            hotelItem.origDayId=hotelItem.dayId;
            hotelItem.origAttractionId=hotelItem.attractionId;
            hotelItem.origAttractionAreaId=hotelItem.attractionAreaId;
            hotelItem.origScheduleId=hotelItem.scheduleId;
            hotelItem.origHotelName=hotelItem.hotelName;
            hotelItem.origBookingReference=hotelItem.bookingReference;
            return (true);
        }
        catch(Exception e)
        {
            ShowError("GetHotelItemFromQuery", e.getMessage());
        }

        return (false);
    }

    private boolean ItemExists(HotelItem litem)
    {
        if(IsValid() == false)
            return (false);

        try
        {
            String lSQL;
            lSQL="SELECT holidayId, dayId, attractionId, attractionAreaId, " + "  scheduleId " + "FROM Hotel " + "WHERE HolidayId = " + litem.holidayId + " " + "AND DayId = " + litem.dayId + " " + "AND attractionId = " + litem.attractionId + " " + "AND attractionAreaId = " + litem.attractionAreaId + " " + "AND ScheduleId = " + litem.scheduleId;
            Cursor cursor=executeSQLOpenCursor("ItemExists(hotel)", lSQL);
            if(cursor == null)
                return (false);

            if(cursor.getCount() == 0)
                return (false);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("ItemExists(hotel)", e.getMessage());
        }

        return (false);
    }

    boolean createSample(int lHolidayId, int lDayId, int lAttractionId, int lAttractionAreaId, int lScheduleId)
    {
        try
        {
            HotelItem item=new HotelItem();

            item.holidayId=lHolidayId;
            item.dayId=lDayId;
            item.attractionId=lAttractionId;
            item.attractionAreaId=lAttractionAreaId;
            item.scheduleId=lScheduleId;

            if(!addHotelItem(item))
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
