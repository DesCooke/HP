package com.example.des.hp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.des.hp.Schedule.Ride.RideItem;

class TableRide extends TableBase
{
    TableRide(Context context, SQLiteOpenHelper dbHelper)
    {
        super(context, dbHelper);
    }

    public void ShowError(String argFunction, String argMessage)
    {
        super.ShowError("TableRide:" + argFunction, argMessage);
    }

    public boolean onCreate(SQLiteDatabase db)
    {
        try
        {
            String lSQL="CREATE TABLE IF NOT EXISTS ride " + "( " + "  holidayId         INT(5),  " + "  dayId             INT(5),  " + "  attractionId      INT(5),  " + "  attractionAreaId  INT(5),  " + "  scheduleId        INT(5),  " + "  rideName          VARCHAR, " + "  heartRating       FLOAT,   " + "  scenicRating      FLOAT,   " + "  thrillRating      FLOAT   " + ") ";

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
            if(oldVersion == 25 && newVersion == 26)
            {
                db.execSQL("CREATE TABLE IF NOT EXISTS ride " + "( " + "  holidayId         INT(5),  " + "  dayId             INT(5),  " + "  attractionId      INT(5),  " + "  attractionAreaId  INT(5),  " + "  scheduleId        INT(5),  " + "  rideName          VARCHAR, " + "  heartRating       FLOAT,   " + "  scenicRating      FLOAT,   " + "  thrillRating      FLOAT    " + ") ");
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("onUpgrade", e.getMessage());
        }
        return (false);
    }

    boolean addRideItem(RideItem rideItem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            String lSql="INSERT INTO ride " + "  (holidayId, dayId, attractionId, attractionAreaId, " + "   scheduleId, heartRating, scenicRating, thrillRating) " + "VALUES " + "(" + rideItem.holidayId + "," + rideItem.dayId + "," + rideItem.attractionId + "," + rideItem.attractionAreaId + "," + rideItem.scheduleId + "," + rideItem.heartRating + "," + rideItem.scenicRating + "," + rideItem.thrillRating + " " + ")";

            return (executeSQL("addRideItem", lSql));
        }
        catch(Exception e)
        {
            ShowError("addRideItem", e.getMessage());
        }
        return (false);

    }

    boolean updateRideItem(RideItem rideItem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            if(ItemExists(rideItem) == false)
            {
                return (addRideItem(rideItem));
            }
            String lSQL;
            lSQL="UPDATE ride " + "SET heartRating = " + rideItem.heartRating + ", " + "    scenicRating = " + rideItem.scenicRating + ", " + "    thrillRating = " + rideItem.thrillRating + ", " + "    dayId = " + rideItem.dayId + ", " + "    attractionId = " + rideItem.attractionId + ", " + "    attractionAreaId = " + rideItem.attractionAreaId + ", " + "    scheduleId = " + rideItem.scheduleId + " " + "WHERE holidayId = " + rideItem.holidayId + " " + "AND dayId = " + rideItem.origDayId + " " + "AND attractionId = " + rideItem.origAttractionId + " " + "AND attractionAreaId = " + rideItem.origAttractionAreaId + " " + "AND scheduleId = " + rideItem.origScheduleId;

            return (executeSQL("updateRideItem", lSQL));
        }
        catch(Exception e)
        {
            ShowError("updateRideItem", e.getMessage());
        }
        return (false);

    }

    boolean deleteRideItem(RideItem rideItem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            String lSQL="DELETE FROM ride " + "WHERE holidayId = " + rideItem.holidayId + " " + "AND dayId = " + rideItem.dayId + " " + "AND attractionId = " + rideItem.attractionId + " " + "AND attractionAreaId = " + rideItem.attractionAreaId + " " + "AND scheduleId = " + rideItem.scheduleId;

            if(executeSQL("deleteRideItem", lSQL) == false)
                return (false);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("deleteRideItem", e.getMessage());
        }
        return (false);
    }

    boolean getRideItem(int holidayId, int dayId, int attractionId, int attractionAreaId, int scheduleId, RideItem litem)
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
            lSQL="SELECT holidayId, dayId, attractionId, attractionAreaId, " + "  scheduleId, rideName, heartRating, scenicRating, thrillRating " + "FROM ride " + "WHERE HolidayId = " + holidayId + " " + "AND DayId = " + dayId + " " + "AND attractionId = " + attractionId + " " + "AND attractionAreaId = " + attractionAreaId + " " + "AND ScheduleId = " + scheduleId;

            Cursor cursor=executeSQLOpenCursor("getRestaurantItem", lSQL);
            if(cursor != null)
            {
                cursor.moveToFirst();
                if(GetRideItemFromQuery(cursor, litem) == false)
                    return (false);
            }
            executeSQLCloseCursor("getRideItem");
            return (true);
        }
        catch(Exception e)
        {
            ShowError("getRideItem", e.getMessage());
        }
        return (false);
    }

    private boolean GetRideItemFromQuery(Cursor cursor, RideItem litem)
    {
        if(IsValid() == false)
            return (false);

        try
        {
            if(cursor.getCount() == 0)
                return (true);

            litem.holidayId=Integer.parseInt(cursor.getString(0));
            litem.dayId=Integer.parseInt(cursor.getString(1));
            litem.attractionId=Integer.parseInt(cursor.getString(2));
            litem.attractionAreaId=Integer.parseInt(cursor.getString(3));
            litem.scheduleId=Integer.parseInt(cursor.getString(4));
            litem.rideName=cursor.getString(5);
            litem.heartRating=cursor.getFloat(6);
            litem.scenicRating=cursor.getFloat(7);
            litem.thrillRating=cursor.getFloat(8);

            litem.origHolidayId=litem.holidayId;
            litem.origDayId=litem.dayId;
            litem.origAttractionId=litem.attractionId;
            litem.origAttractionAreaId=litem.attractionAreaId;
            litem.origScheduleId=litem.scheduleId;
            litem.origRideName=litem.rideName;
            litem.origHeartRating=litem.heartRating;
            litem.origScenicRating=litem.scenicRating;
            litem.origThrillRating=litem.scenicRating;
            return (true);
        }
        catch(Exception e)
        {
            ShowError("GetRideItemFromQuery", e.getMessage());
        }

        return (false);
    }

    private boolean ItemExists(RideItem litem)
    {
        if(IsValid() == false)
            return (false);

        try
        {
            String lSQL;
            lSQL="SELECT holidayId, dayId, attractionId, attractionAreaId, " + "  scheduleId " + "FROM Ride " + "WHERE HolidayId = " + litem.holidayId + " " + "AND DayId = " + litem.dayId + " " + "AND attractionId = " + litem.attractionId + " " + "AND attractionAreaId = " + litem.attractionAreaId + " " + "AND ScheduleId = " + litem.scheduleId;
            Cursor cursor=executeSQLOpenCursor("ItemExists(ride)", lSQL);
            if(cursor == null)
                return (false);

            if(cursor.getCount() == 0)
                return (false);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("ItemExists(ride)", e.getMessage());
        }

        return (false);
    }

}
