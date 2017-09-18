package com.example.des.hp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.des.hp.Schedule.Flight.FlightItem;

import java.util.Random;

class TableFlight extends TableBase
{
    TableFlight(Context context, SQLiteOpenHelper dbHelper)
    {
        super(context, dbHelper);
    }

    public void ShowError(String argFunction, String argMessage)
    {
        super.ShowError("TableFlight:" + argFunction, argMessage);
    }

    public boolean onCreate(SQLiteDatabase db)
    {
        try
        {
            String lSQL="CREATE TABLE IF NOT EXISTS flight " + "( " + "  holidayId         INT(5),  " + "  dayId             INT(5),  " + "  attractionId      INT(5),  " + "  attractionAreaId  INT(5),  " + "  scheduleId        INT(5),  " + "  flightNo          VARCHAR, " + "  departsHour       INT(2),  " + "  departsMin        INT(2),  " + "  terminal          VARCHAR, " + "  bookingReference  VARCHAR  " + ") ";

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

    boolean addFlightItem(FlightItem flightItem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            String lSql="INSERT INTO flight " + "  (holidayId, dayId, attractionId, attractionAreaId, " + "   scheduleId, flightNo, departsHour, departsMin, terminal, " + "   bookingReference) " + "VALUES " + "(" + flightItem.holidayId + "," + flightItem.dayId + "," + flightItem.attractionId + "," + flightItem.attractionAreaId + "," + flightItem.scheduleId + "," + MyQuotedString(flightItem.flightNo) + "," + flightItem.departsHour + "," + flightItem.departsMin + "," + MyQuotedString(flightItem.terminal) + "," + MyQuotedString(flightItem.bookingReference) + " " + ")";

            return (executeSQL("addFlightItem", lSql));
        }
        catch(Exception e)
        {
            ShowError("addFlightItem", e.getMessage());
        }
        return (false);

    }

    boolean updateFlightItem(FlightItem flightItem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            if(ItemExists(flightItem) == false)
            {
                return (addFlightItem(flightItem));
            }

            String lSQL;
            lSQL="UPDATE flight " + "SET flightNo = " + MyQuotedString(flightItem.flightNo) + ", " + "    departsHour = " + flightItem.departsHour + ", " + "    departsMin = " + flightItem.departsMin + ", " + "    terminal = " + MyQuotedString(flightItem.terminal) + ", " + "    bookingReference = " + MyQuotedString(flightItem.bookingReference) + ", " + "    dayId = " + flightItem.dayId + ", " + "    attractionId = " + flightItem.attractionId + ", " + "    attractionAreaId = " + flightItem.attractionAreaId + ", " + "    scheduleId = " + flightItem.scheduleId + " " + "WHERE holidayId = " + flightItem.holidayId + " " + "AND dayId = " + flightItem.origDayId + " " + "AND attractionId = " + flightItem.origAttractionId + " " + "AND attractionAreaId = " + flightItem.origAttractionAreaId + " " + "AND scheduleId = " + flightItem.origScheduleId;

            return (executeSQL("updateFlightItem", lSQL));
        }
        catch(Exception e)
        {
            ShowError("updateFlightItem", e.getMessage());
        }
        return (false);

    }

    boolean deleteFlightItem(FlightItem flightItem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            String lSQL="DELETE FROM flight " + "WHERE holidayId = " + flightItem.holidayId + " " + "AND dayId = " + flightItem.dayId + " " + "AND attractionId = " + flightItem.attractionId + " " + "AND attractionAreaId = " + flightItem.attractionAreaId + " " + "AND scheduleId = " + flightItem.scheduleId;


            if(executeSQL("deleteFlightItem", lSQL) == false)
                return (false);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("deleteFlightItem", e.getMessage());
        }
        return (false);

    }

    boolean getFlightItem(int holidayId, int dayId, int attractionId, int attractionAreaId, int scheduleId, FlightItem litem)
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
            lSQL="SELECT holidayId, dayId, attractionId, attractionAreaId, " + "  scheduleId, flightNo, departsHour, departsMin, terminal, " + "  bookingReference " + "FROM Flight " + "WHERE HolidayId = " + holidayId + " " + "AND DayId = " + dayId + " " + "AND attractionId = " + attractionId + " " + "AND attractionAreaId = " + attractionAreaId + " " + "AND ScheduleId = " + scheduleId;

            Cursor cursor=executeSQLOpenCursor("getFlightItem", lSQL);
            if(cursor != null)
            {
                cursor.moveToFirst();
                if(GetFlightItemFromQuery(cursor, litem) == false)
                    return (false);
            }
            executeSQLCloseCursor("getFlightItem");
            return (true);
        }
        catch(Exception e)
        {
            ShowError("getFlightItem", e.getMessage());
        }
        return (false);

    }

    private boolean GetFlightItemFromQuery(Cursor cursor, FlightItem flightItem)
    {
        if(IsValid() == false)
            return (false);

        try
        {
            if(cursor.getCount() == 0)
                return (true);

            flightItem.holidayId=Integer.parseInt(cursor.getString(0));
            flightItem.dayId=Integer.parseInt(cursor.getString(1));
            flightItem.attractionId=Integer.parseInt(cursor.getString(2));
            flightItem.attractionAreaId=Integer.parseInt(cursor.getString(3));
            flightItem.scheduleId=Integer.parseInt(cursor.getString(4));
            flightItem.flightNo=cursor.getString(5);
            flightItem.departsHour=Integer.parseInt(cursor.getString(6));
            flightItem.departsMin=Integer.parseInt(cursor.getString(7));
            flightItem.terminal=cursor.getString(8);
            flightItem.bookingReference=cursor.getString(9);

            flightItem.origHolidayId=flightItem.holidayId;
            flightItem.origDayId=flightItem.dayId;
            flightItem.origAttractionId=flightItem.attractionId;
            flightItem.origAttractionAreaId=flightItem.attractionAreaId;
            flightItem.origScheduleId=flightItem.scheduleId;
            flightItem.origFlightNo=flightItem.flightNo;
            flightItem.origDepartsHour=flightItem.departsHour;
            flightItem.origDepartsMin=flightItem.departsMin;
            flightItem.origTerminal=flightItem.terminal;
            flightItem.origBookingReference=flightItem.bookingReference;
            return (true);
        }
        catch(Exception e)
        {
            ShowError("GetFlightItemFromQuery", e.getMessage());
        }

        return (false);
    }

    private boolean ItemExists(FlightItem litem)
    {
        if(IsValid() == false)
            return (false);

        try
        {
            String lSQL;
            lSQL="SELECT holidayId, dayId, attractionId, attractionAreaId, " + "  scheduleId " + "FROM Flight " + "WHERE HolidayId = " + litem.holidayId + " " + "AND DayId = " + litem.dayId + " " + "AND attractionId = " + litem.attractionId + " " + "AND attractionAreaId = " + litem.attractionAreaId + " " + "AND ScheduleId = " + litem.scheduleId;
            Cursor cursor=executeSQLOpenCursor("ItemExists(flight)", lSQL);
            if(cursor == null)
                return (false);

            if(cursor.getCount() == 0)
                return (false);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("ItemExists(flight)", e.getMessage());
        }

        return (false);
    }

    String randomTerminal()
    {
        Random random = new Random();
        return(String.valueOf(random.nextInt(5)+1));
    }
    
    String randomFlightNo()
    {
        Random random = new Random();
        char l1 = (char)(random.nextInt(26) + 'A');
        char l2 = (char)(random.nextInt(26) + 'A');
        char l3 = (char)(random.nextInt(10) + '0');
        char l4 = (char)(random.nextInt(10) + '0');
        char l5 = (char)(random.nextInt(10) + '0');
        
        StringBuilder sb=new StringBuilder();
        sb.append(l1);
        sb.append(l2);
        sb.append(l3);
        sb.append(l4);
        sb.append(l5);
        return(sb.toString());
    }
    
    boolean createSample(int lHolidayId, int lDayId, int lAttractionId, int lAttractionAreaId, int lScheduleId)
    {
        try
        {
            FlightItem item=new FlightItem();

            item.holidayId=lHolidayId;
            item.dayId=lDayId;
            item.attractionId=lAttractionId;
            item.attractionAreaId=lAttractionAreaId;
            item.scheduleId=lScheduleId;
            item.bookingReference="Test Booking Ref";
            item.flightNo=randomFlightNo();
            item.terminal=randomTerminal();
            Random random = new Random();
            if(random.nextInt(10)<5)
            {
                item.departsKnown=false;
                item.departsHour=0;
                item.departsMin=0;
            }
            else
            {
                item.departsKnown = true;
                item.departsHour = random.nextInt(23);
                item.departsMin = random.nextInt(60);
            }
            
            if(!addFlightItem(item))
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
