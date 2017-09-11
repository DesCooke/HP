package com.example.des.hp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.des.hp.Schedule.Show.ShowItem;

class TableShow extends TableBase
{
    TableShow(Context context, SQLiteOpenHelper dbHelper)
    {
        super(context, dbHelper);
    }

    public void ShowError(String argFunction, String argMessage)
    {
        super.ShowError("TableShow:" + argFunction, argMessage);
    }

    public boolean onCreate(SQLiteDatabase db)
    {
        try
        {
            String lSQL="CREATE TABLE IF NOT EXISTS show " + "( " + "  holidayId         INT(5),  " + "  dayId             INT(5),  " + "  attractionId      INT(5),  " + "  attractionAreaId  INT(5),  " + "  scheduleId        INT(5),  " + "  showName          VARCHAR, " + "  showHour          INT(2),  " + "  showMin           INT(2),  " + "  bookingReference  VARCHAR, " + "  heartRating       FLOAT,   " + "  scenicRating      FLOAT,   " + "  thrillRating      FLOAT    " + ") ";

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
            if(oldVersion == 34 && newVersion == 35)
            {
                db.execSQL("ALTER TABLE show ADD COLUMN heartRating FLOAT DEFAULT 0.0");
                db.execSQL("ALTER TABLE show ADD COLUMN scenicRating FLOAT DEFAULT 0.0");
                db.execSQL("ALTER TABLE show ADD COLUMN thrillRating FLOAT DEFAULT 0.0");
                db.execSQL("UPDATE show SET heartRating = 0.00");
                db.execSQL("UPDATE show SET scenicRating = 0.00");
                db.execSQL("UPDATE show SET thrillRating = 0.00");
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("onUpgrade", e.getMessage());
            return (false);
        }
    }

    boolean addShowItem(ShowItem showItem)
    {
        if(IsValid() == false)
            return (false);

        String lSql="INSERT INTO show " + "  (holidayId, dayId, attractionId, attractionAreaId, " + "   scheduleId, showName, showHour, " + "   showMin, bookingReference, heartRating, scenicRating, thrillRating) " + "VALUES " + "(" + showItem.holidayId + "," + showItem.dayId + "," + showItem.attractionId + "," + showItem.attractionAreaId + "," + showItem.scheduleId + "," + MyQuotedString(showItem.showName) + "," + showItem.showHour + "," + showItem.showMin + "," + MyQuotedString(showItem.bookingReference) + ", " + showItem.heartRating + "," + showItem.scenicRating + "," + showItem.thrillRating + " " + ")";

        return (executeSQL("addShowItem", lSql));
    }

    boolean updateShowItem(ShowItem showItem)
    {
        if(IsValid() == false)
            return (false);

        if(ItemExists(showItem) == false)
        {
            return (addShowItem(showItem));
        }

        String lSQL;
        lSQL="UPDATE Show " + "SET showName = " + MyQuotedString(showItem.showName) + ", " + "    showHour = " + showItem.showHour + ", " + "    showMin = " + showItem.showMin + ", " + "    bookingReference = " + MyQuotedString(showItem.bookingReference) + ", " + "    heartRating = " + showItem.heartRating + ", " + "    scenicRating = " + showItem.scenicRating + ", " + "    thrillRating = " + showItem.thrillRating + ", " + "    dayId = " + showItem.dayId + ", " + "    attractionId = " + showItem.attractionId + ", " + "    attractionAreaId = " + showItem.attractionAreaId + ", " + "    scheduleId = " + showItem.scheduleId + " " + "WHERE holidayId = " + showItem.holidayId + " " + "AND dayId = " + showItem.origDayId + " " + "AND attractionId = " + showItem.origAttractionId + " " + "AND attractionAreaId = " + showItem.origAttractionAreaId + " " + "AND scheduleId = " + showItem.origScheduleId;

        return (executeSQL("updateShowItem", lSQL));
    }

    boolean deleteShowItem(ShowItem showItem)
    {
        if(IsValid() == false)
            return (false);

        String lSQL="DELETE FROM show " + "WHERE holidayId = " + showItem.holidayId + " " + "AND dayId = " + showItem.dayId + " " + "AND attractionId = " + showItem.attractionId + " " + "AND attractionAreaId = " + showItem.attractionAreaId + " " + "AND scheduleId = " + showItem.scheduleId;

        if(executeSQL("deleteShowItem", lSQL) == false)
            return (false);

        return (true);
    }

    boolean getShowItem(int holidayId, int dayId, int attractionId, int attractionAreaId, int scheduleId, ShowItem litem)
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
        lSQL="SELECT holidayId, dayId, attractionId, attractionAreaId, " + "  scheduleId, showName, showHour, " + "  showMin, bookingReference, heartRating, scenicRating, thrillRating " + "FROM Show " + "WHERE HolidayId = " + holidayId + " " + "AND DayId = " + dayId + " " + "AND attractionId = " + attractionId + " " + "AND attractionAreaId = " + attractionAreaId + " " + "AND ScheduleId = " + scheduleId;
        Cursor cursor=executeSQLOpenCursor("getShowItem", lSQL);
        if(cursor != null)
        {
            cursor.moveToFirst();
            if(GetShowItemFromQuery(cursor, litem) == false)
                return (false);
        }
        executeSQLCloseCursor("getShowItem");
        return (true);
    }

    private boolean GetShowItemFromQuery(Cursor cursor, ShowItem showItem)
    {
        if(IsValid() == false)
            return (false);

        try
        {
            if(cursor.getCount() == 0)
                return (true);

            showItem.holidayId=Integer.parseInt(cursor.getString(0));
            showItem.dayId=Integer.parseInt(cursor.getString(1));
            showItem.attractionId=Integer.parseInt(cursor.getString(2));
            showItem.attractionAreaId=Integer.parseInt(cursor.getString(3));
            showItem.scheduleId=Integer.parseInt(cursor.getString(4));
            showItem.showName=cursor.getString(5);
            showItem.showHour=Integer.parseInt(cursor.getString(6));
            showItem.showMin=Integer.parseInt(cursor.getString(7));
            showItem.bookingReference=cursor.getString(8);
            showItem.heartRating=cursor.getFloat(9);
            showItem.scenicRating=cursor.getFloat(10);
            showItem.thrillRating=cursor.getFloat(11);

            showItem.origHolidayId=showItem.holidayId;
            showItem.origDayId=showItem.dayId;
            showItem.origAttractionId=showItem.attractionId;
            showItem.origAttractionAreaId=showItem.attractionAreaId;
            showItem.origScheduleId=showItem.scheduleId;
            showItem.origShowName=showItem.showName;
            showItem.origShowHour=showItem.showHour;
            showItem.origShowMin=showItem.showMin;
            showItem.origBookingReference=showItem.bookingReference;
            showItem.origHeartRating=showItem.heartRating;
            showItem.origScenicRating=showItem.scenicRating;
            showItem.origThrillRating=showItem.scenicRating;
        }
        catch(Exception e)
        {
            ShowError("GetShowItemFromQuery", e.getMessage());
        }

        return (true);
    }

    private boolean ItemExists(ShowItem litem)
    {
        if(IsValid() == false)
            return (false);

        try
        {
            String lSQL;
            lSQL="SELECT holidayId, dayId, attractionId, attractionAreaId, " + "  scheduleId " + "FROM Show " + "WHERE HolidayId = " + litem.holidayId + " " + "AND DayId = " + litem.dayId + " " + "AND attractionId = " + litem.attractionId + " " + "AND attractionAreaId = " + litem.attractionAreaId + " " + "AND ScheduleId = " + litem.scheduleId;
            Cursor cursor=executeSQLOpenCursor("ItemExists(show)", lSQL);
            if(cursor == null)
                return (false);

            if(cursor.getCount() == 0)
                return (false);
        }
        catch(Exception e)
        {
            ShowError("ItemExists(show)", e.getMessage());
        }

        return (true);
    }

}
