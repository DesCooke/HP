package com.example.des.hp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.des.hp.Schedule.Ride.RideItem;
import com.example.des.hp.myutils.MyMessages;

class TableRide extends TableBase
{
    TableRide(Context context, SQLiteOpenHelper dbHelper, MyMessages myMessages)
    {
        super(context, dbHelper, myMessages);
    }

    public void ShowError(String argFunction, String argMessage)
    {
        super.ShowError("TableRide:" + argFunction, argMessage);
    }

    public boolean onCreate(SQLiteDatabase db)
    {
        try
        {
            String lSQL="CREATE TABLE IF NOT EXISTS ride " +
                "( " +
                "  holidayId         INT(5),  " +
                "  dayId             INT(5),  " +
                "  attractionId      INT(5),  " +
                "  attractionAreaId  INT(5),  " +
                "  scheduleId        INT(5),  " +
                "  rideName          VARCHAR, " +
                "  heartRating       FLOAT,   " +
                "  scenicRating      FLOAT,   " +
                "  thrillRating      FLOAT   " +
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
            if(oldVersion == 25 && newVersion == 26)
            {
                db.execSQL("CREATE TABLE IF NOT EXISTS ride " +
                    "( " +
                    "  holidayId         INT(5),  " +
                    "  dayId             INT(5),  " +
                    "  attractionId      INT(5),  " +
                    "  attractionAreaId  INT(5),  " +
                    "  scheduleId        INT(5),  " +
                    "  rideName          VARCHAR, " +
                    "  heartRating       FLOAT,   " +
                    "  scenicRating      FLOAT,   " +
                    "  thrillRating      FLOAT    " +
                    ") ");
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("onUpgrade", e.getMessage());
            return (false);
        }
    }

    boolean addRideItem(RideItem rideItem)
    {
        if(IsValid() == false)
            return (false);

        String lSql="INSERT INTO ride " +
            "  (holidayId, dayId, attractionId, attractionAreaId, " +
            "   scheduleId, heartRating, scenicRating, thrillRating) " +
            "VALUES " +
            "(" +
            rideItem.holidayId + "," +
            rideItem.dayId + "," +
            rideItem.attractionId + "," +
            rideItem.attractionAreaId + "," +
            rideItem.scheduleId + "," +
            rideItem.heartRating + "," +
            rideItem.scenicRating + "," +
            rideItem.thrillRating + " " +
            ")";

        return (executeSQL("addRideItem", lSql));
    }

    boolean updateRideItem(RideItem rideItem)
    {
        if(IsValid() == false)
            return (false);

        String lSQL;
        lSQL="UPDATE ride " +
            "SET heartRating = " + rideItem.heartRating + ", " +
            "    scenicRating = " + rideItem.scenicRating + ", " +
            "    thrillRating = " + rideItem.thrillRating + ", " +
            "    dayId = " + rideItem.dayId + ", " +
            "    attractionId = " + rideItem.attractionId + ", " +
            "    attractionAreaId = " + rideItem.attractionAreaId + ", " +
            "    scheduleId = " + rideItem.scheduleId + " " +
            "WHERE holidayId = " + rideItem.holidayId + " " +
            "AND dayId = " + rideItem.origDayId + " " +
            "AND attractionId = " + rideItem.origAttractionId + " " +
            "AND attractionAreaId = " + rideItem.origAttractionAreaId + " " +
            "AND scheduleId = " + rideItem.origScheduleId;

        return (executeSQL("updateRideItem", lSQL));
    }

    boolean deleteRideItem(RideItem rideItem)
    {
        if(IsValid() == false)
            return (false);

        String lSQL="DELETE FROM ride " +
            "WHERE holidayId = " + rideItem.holidayId + " " +
            "AND dayId = " + rideItem.dayId + " " +
            "AND attractionId = " + rideItem.attractionId + " " +
            "AND attractionAreaId = " + rideItem.attractionAreaId + " " +
            "AND scheduleId = " + rideItem.scheduleId;

        if(executeSQL("deleteRideItem", lSQL) == false)
            return (false);

        return (true);
    }

    boolean getRideItem(int holidayId, int dayId, int attractionId, int attractionAreaId, int scheduleId, RideItem litem)
    {
        if(IsValid() == false)
            return (false);

        String lSQL;
        lSQL="SELECT holidayId, dayId, attractionId, attractionAreaId, " +
            "  scheduleId, rideName, heartRating, scenicRating, thrillRating " +
            "FROM ride " +
            "WHERE HolidayId = " + holidayId + " " +
            "AND DayId = " + dayId + " " +
            "AND attractionId = " + attractionId + " " +
            "AND attractionAreaId = " + attractionAreaId + " " +
            "AND ScheduleId = " + scheduleId;

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

    private boolean GetRideItemFromQuery(Cursor cursor, RideItem rideItem)
    {
        if(IsValid() == false)
            return (false);

        try
        {
            if(cursor.getCount() == 0)
                return (true);

            rideItem.holidayId=Integer.parseInt(cursor.getString(0));
            rideItem.dayId=Integer.parseInt(cursor.getString(1));
            rideItem.attractionId=Integer.parseInt(cursor.getString(2));
            rideItem.attractionAreaId=Integer.parseInt(cursor.getString(3));
            rideItem.scheduleId=Integer.parseInt(cursor.getString(4));
            rideItem.rideName=cursor.getString(5);
            rideItem.heartRating=cursor.getFloat(6);
            rideItem.scenicRating=cursor.getFloat(7);
            rideItem.thrillRating=cursor.getFloat(8);

            rideItem.origHolidayId=rideItem.holidayId;
            rideItem.origDayId=rideItem.dayId;
            rideItem.origAttractionId=rideItem.attractionId;
            rideItem.origAttractionAreaId=rideItem.attractionAreaId;
            rideItem.origScheduleId=rideItem.scheduleId;
            rideItem.origRideName=rideItem.rideName;
            rideItem.origHeartRating=rideItem.heartRating;
            rideItem.origScenicRating=rideItem.scenicRating;
            rideItem.origThrillRating=rideItem.scenicRating;
        }
        catch(Exception e)
        {
            ShowError("GetRideItemFromQuery", e.getMessage());
        }

        return (true);
    }

}
