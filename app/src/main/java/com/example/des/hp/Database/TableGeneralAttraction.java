package com.example.des.hp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.des.hp.Schedule.GeneralAttraction.GeneralAttractionItem;

class TableGeneralAttraction extends TableBase
{
    TableGeneralAttraction(Context context, SQLiteOpenHelper dbHelper)
    {
        super(context, dbHelper);
    }

    public void ShowError(String argFunction, String argMessage)
    {
        super.ShowError("TableGeneralAttraction:" + argFunction, argMessage);
    }

    public boolean onCreate(SQLiteDatabase db)
    {
        try
        {
            String lSQL="CREATE TABLE IF NOT EXISTS generalattraction " +
                "( " +
                "  holidayId         INT(5),  " +
                "  dayId             INT(5),  " +
                "  attractionId      INT(5),  " +
                "  attractionAreaId  INT(5),  " +
                "  scheduleId        INT(5),  " +
                "  name              VARCHAR, " +
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
            if(oldVersion==41 && newVersion==42)
            {
                onCreate(db);
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("onUpgrade", e.getMessage());
            return (false);
        }
    }

    boolean addGeneralAttractionItem(GeneralAttractionItem item)
    {
        if(!IsValid())
            return (false);

        String lSql="INSERT INTO generalattraction " +
            "  (holidayId, dayId, attractionId, attractionAreaId, " +
            "   scheduleId, heartRating, scenicRating, thrillRating) " +
            "VALUES " +
            "(" +
            item.holidayId + "," +
            item.dayId + "," +
            item.attractionId + "," +
            item.attractionAreaId + "," +
            item.scheduleId + "," +
            item.heartRating + "," +
            item.scenicRating + "," +
            item.thrillRating + " " +
            ")";

        return (executeSQL("addGeneralAttractionItem", lSql));
    }

    boolean updateGeneralAttractionItem(GeneralAttractionItem item)
    {
        if(!IsValid())
            return (false);

        String lSQL;
        lSQL="UPDATE generalattraction " +
            "SET heartRating = " + item.heartRating + ", " +
            "    scenicRating = " + item.scenicRating + ", " +
            "    thrillRating = " + item.thrillRating + ", " +
            "    dayId = " + item.dayId + ", " +
            "    attractionId = " + item.attractionId + ", " +
            "    attractionAreaId = " + item.attractionAreaId + ", " +
            "    scheduleId = " + item.scheduleId + " " +
            "WHERE holidayId = " + item.holidayId + " " +
            "AND dayId = " + item.origDayId + " " +
            "AND attractionId = " + item.origAttractionId + " " +
            "AND attractionAreaId = " + item.origAttractionAreaId + " " +
            "AND scheduleId = " + item.origScheduleId;

        return (executeSQL("updateGeneralAttractionItem", lSQL));
    }

    boolean deleteGeneralAttractionItem(GeneralAttractionItem item)
    {
        if(!IsValid())
            return (false);

        String lSQL="DELETE FROM generalattraction " +
            "WHERE holidayId = " + item.holidayId + " " +
            "AND dayId = " + item.dayId + " " +
            "AND attractionId = " + item.attractionId + " " +
            "AND attractionAreaId = " + item.attractionAreaId + " " +
            "AND scheduleId = " + item.scheduleId;

        return executeSQL("deleteGeneralAttractionItem", lSQL);

    }

    boolean getGeneralAttractionItem(int holidayId, int dayId, int attractionId, int attractionAreaId,
        int scheduleId, GeneralAttractionItem litem)
    {
        if(!IsValid())
            return (false);

        String lSQL;
        lSQL="SELECT holidayId, dayId, attractionId, attractionAreaId, " +
            "  scheduleId, name, heartRating, scenicRating, thrillRating " +
            "FROM generalAttraction " +
            "WHERE HolidayId = " + holidayId + " " +
            "AND DayId = " + dayId + " " +
            "AND attractionId = " + attractionId + " " +
            "AND attractionAreaId = " + attractionAreaId + " " +
            "AND ScheduleId = " + scheduleId;

        Cursor cursor=executeSQLOpenCursor("getGeneralAttractionItem", lSQL);
        if(cursor != null)
        {
            cursor.moveToFirst();
            if(!GetGeneralAttractionItemFromQuery(cursor, litem))
                return (false);
        }
        executeSQLCloseCursor("getGeneralAttractionItem");
        return (true);
    }

    private boolean GetGeneralAttractionItemFromQuery(Cursor cursor, GeneralAttractionItem item)
    {
        if(!IsValid())
            return (false);

        try
        {
            if(cursor.getCount() == 0)
                return (true);

            item.holidayId=Integer.parseInt(cursor.getString(0));
            item.dayId=Integer.parseInt(cursor.getString(1));
            item.attractionId=Integer.parseInt(cursor.getString(2));
            item.attractionAreaId=Integer.parseInt(cursor.getString(3));
            item.scheduleId=Integer.parseInt(cursor.getString(4));
            item.name=cursor.getString(5);
            item.heartRating=cursor.getFloat(6);
            item.scenicRating=cursor.getFloat(7);
            item.thrillRating=cursor.getFloat(8);

            item.origHolidayId=item.holidayId;
            item.origDayId=item.dayId;
            item.origAttractionId=item.attractionId;
            item.origAttractionAreaId=item.attractionAreaId;
            item.origScheduleId=item.scheduleId;
            item.origName=item.name;
            item.origHeartRating=item.heartRating;
            item.origScenicRating=item.scenicRating;
            item.origThrillRating=item.scenicRating;
        }
        catch(Exception e)
        {
            ShowError("GetGeneralAttractionItemFromQuery", e.getMessage());
        }

        return (true);
    }

}