package com.example.des.hp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.des.hp.Schedule.Restaurant.RestaurantItem;

import java.util.Random;

class TableRestaurant extends TableBase
{
    TableRestaurant(Context context, SQLiteOpenHelper dbHelper)
    {
        super(context, dbHelper);
    }

    public void ShowError(String argFunction, String argMessage)
    {
        super.ShowError("TableRestaurant:" + argFunction, argMessage);
    }

    public boolean onCreate(SQLiteDatabase db)
    {
        try
        {
            String lSQL="CREATE TABLE IF NOT EXISTS restaurant " + "( " + "  holidayId         INT(5),  " + "  dayId             INT(5),  " + "  attractionId      INT(5),  " + "  attractionAreaId  INT(5),  " + "  scheduleId        INT(5),  " + "  restaurantName    VARCHAR, " + "  bookingReference  VARCHAR, " + "  restaurantFullId  INT(5),  " + "  reservationType   INT(5)   " + ") ";

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
            if(oldVersion == 33 && newVersion == 34)
            {
                db.execSQL("UPDATE restaurant SET reservationType = 0");
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("onUpgrade", e.getMessage());
        }
        return (false);
    }

    boolean addRestaurantItem(RestaurantItem restaurantItem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            String lSql="INSERT INTO restaurant " + "  (holidayId, dayId, attractionId, attractionAreaId, " + "   scheduleId, restaurantName, bookingReference, " + "   restaurantFullId, reservationType) " + "VALUES " + "(" + restaurantItem.holidayId + "," + restaurantItem.dayId + "," + restaurantItem.attractionId + "," + restaurantItem.attractionAreaId + "," + restaurantItem.scheduleId + "," + MyQuotedString(restaurantItem.restaurantName) + "," + MyQuotedString(restaurantItem.bookingReference) + ", " + restaurantItem.restaurantFullId + ", " + restaurantItem.reservationType + " " + ")";

            return (executeSQL("addRestaurantItem", lSql));
        }
        catch(Exception e)
        {
            ShowError("addRestaurantItem", e.getMessage());
        }
        return (false);

    }

    boolean updateRestaurantItem(RestaurantItem restaurantItem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            if(ItemExists(restaurantItem) == false)
            {
                return (addRestaurantItem(restaurantItem));
            }
            String lSQL;
            lSQL="UPDATE restaurant " + "SET restaurantName = " + MyQuotedString(restaurantItem.restaurantName) + ", " + "    bookingReference = " + MyQuotedString(restaurantItem.bookingReference) + ", " + "    restaurantFullId = " + restaurantItem.restaurantFullId + ", " + "    reservationType = " + restaurantItem.reservationType + ", " + "    dayId = " + restaurantItem.dayId + ", " + "    attractionId = " + restaurantItem.attractionId + ", " + "    attractionAreaId = " + restaurantItem.attractionAreaId + ", " + "    scheduleId = " + restaurantItem.scheduleId + " " + "WHERE holidayId = " + restaurantItem.holidayId + " " + "AND dayId = " + restaurantItem.origDayId + " " + "AND attractionId = " + restaurantItem.origAttractionId + " " + "AND attractionAreaId = " + restaurantItem.origAttractionAreaId + " " + "AND scheduleId = " + restaurantItem.origScheduleId;

            return (executeSQL("updateRestaurantItem", lSQL));
        }
        catch(Exception e)
        {
            ShowError("updateRestaurantItem", e.getMessage());
        }
        return (false);

    }

    boolean deleteRestaurantItem(RestaurantItem restaurantItem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            String lSQL="DELETE FROM restaurant " + "WHERE holidayId = " + restaurantItem.holidayId + " " + "AND dayId = " + restaurantItem.dayId + " " + "AND attractionId = " + restaurantItem.attractionId + " " + "AND attractionAreaId = " + restaurantItem.attractionAreaId + " " + "AND scheduleId = " + restaurantItem.scheduleId;

            if(executeSQL("deleteRestaurantItem", lSQL) == false)
                return (false);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("deleteRestaurantItem", e.getMessage());
        }
        return (false);
    }

    boolean getRestaurantItem(int holidayId, int dayId, int attractionId, int attractionAreaId, int scheduleId, RestaurantItem litem)
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
            lSQL="SELECT holidayId, dayId, attractionId, attractionAreaId, " + "  scheduleId, restaurantName, bookingReference, " + "  restaurantFullId, reservationType " + "FROM Restaurant " + "WHERE HolidayId = " + holidayId + " " + "AND DayId = " + dayId + " " + "AND attractionId = " + attractionId + " " + "AND attractionAreaId = " + attractionAreaId + " " + "AND ScheduleId = " + scheduleId;

            Cursor cursor=executeSQLOpenCursor("getRestaurantItem", lSQL);
            if(cursor != null)
            {
                cursor.moveToFirst();
                if(GetRestaurantItemFromQuery(cursor, litem) == false)
                    return (false);
            }
            executeSQLCloseCursor("getRestaurantItem");
            return (true);
        }
        catch(Exception e)
        {
            ShowError("getRestaurantItem", e.getMessage());
        }
        return (false);
    }

    private boolean GetRestaurantItemFromQuery(Cursor cursor, RestaurantItem restaurantItem)
    {
        if(IsValid() == false)
            return (false);

        try
        {
            if(cursor.getCount() == 0)
                return (true);

            restaurantItem.holidayId=Integer.parseInt(cursor.getString(0));
            restaurantItem.dayId=Integer.parseInt(cursor.getString(1));
            restaurantItem.attractionId=Integer.parseInt(cursor.getString(2));
            restaurantItem.attractionAreaId=Integer.parseInt(cursor.getString(3));
            restaurantItem.scheduleId=Integer.parseInt(cursor.getString(4));
            restaurantItem.restaurantName=cursor.getString(5);
            restaurantItem.bookingReference=cursor.getString(6);
            restaurantItem.restaurantFullId=Integer.parseInt(cursor.getString(7));
            restaurantItem.reservationType=Integer.parseInt(cursor.getString(8));

            restaurantItem.origHolidayId=restaurantItem.holidayId;
            restaurantItem.origDayId=restaurantItem.dayId;
            restaurantItem.origAttractionId=restaurantItem.attractionId;
            restaurantItem.origAttractionAreaId=restaurantItem.attractionAreaId;
            restaurantItem.origScheduleId=restaurantItem.scheduleId;
            restaurantItem.origRestaurantName=restaurantItem.restaurantName;
            restaurantItem.origBookingReference=restaurantItem.bookingReference;
            restaurantItem.origRestaurantFullId=restaurantItem.restaurantFullId;
            restaurantItem.origReservationType=restaurantItem.reservationType;

            return (true);
        }
        catch(Exception e)
        {
            ShowError("GetRestaurantItemFromQuery", e.getMessage());
        }

        return (false);
    }

    private boolean ItemExists(RestaurantItem litem)
    {
        if(IsValid() == false)
            return (false);

        try
        {
            String lSQL;
            lSQL="SELECT holidayId, dayId, attractionId, attractionAreaId, " + "  scheduleId " + "FROM Restaurant " + "WHERE HolidayId = " + litem.holidayId + " " + "AND DayId = " + litem.dayId + " " + "AND attractionId = " + litem.attractionId + " " + "AND attractionAreaId = " + litem.attractionAreaId + " " + "AND ScheduleId = " + litem.scheduleId;
            Cursor cursor=executeSQLOpenCursor("ItemExists(restaurant)", lSQL);
            if(cursor == null)
                return (false);

            if(cursor.getCount() == 0)
                return (false);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("ItemExists(restaurant)", e.getMessage());
        }

        return (false);
    }

    boolean createSample(int lHolidayId, int lDayId, int lAttractionId, int lAttractionAreaId, int lScheduleId)
    {
        try
        {
            RestaurantItem item=new RestaurantItem();

            item.holidayId=lHolidayId;
            item.dayId=lDayId;
            item.attractionId=lAttractionId;
            item.attractionAreaId=lAttractionAreaId;
            item.scheduleId=lScheduleId;

            Random random=new Random();
            item.reservationType = random.nextInt(4);
            if(!addRestaurantItem(item))
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
