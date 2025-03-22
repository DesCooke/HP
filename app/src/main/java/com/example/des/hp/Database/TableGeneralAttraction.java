package com.example.des.hp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.des.hp.Event.EventScheduleDetailItem;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

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
                    "  thrillRating      FLOAT,   " +
                    "  AttractionType    VARCHAR, " +
                    "  BookingReference  VARCHAR, " +
                    "  FlightNo          VARCHAR, " +
                    "  DepartsKnown      VARCHAR, " +
                    "  DepartsHour       INT(2),  " +
                    "  DepartsMin        INT(2),  " +
                    "  Terminal          VARCHAR, " +
                    "  RestaurantFullId  INT(5),  " +
                    "  ReservationType   INT(5),  " +
                    "  ShowKnown         VARCHAR, " +
                    "  ShowHour          INT(2),  " +
                    "  ShowMin           INT(2),  " +
                    "  PickUpKnown         VARCHAR, " +
                    "  PickUpHour          INT(2),  " +
                    "  PickUpMin           INT(2),  " +
                    "  DropOffKnown         VARCHAR, " +
                    "  DropOffHour          INT(2),  " +
                    "  DropOffMin           INT(2),  " +
                    "  CheckInKnown         VARCHAR, " +
                    "  CheckInHour          INT(2),  " +
                    "  CheckInMin           INT(2),  " +
                    "  ArrivalKnown         VARCHAR, " +
                    "  ArrivalHour          INT(2),  " +
                    "  ArrivalMin           INT(2)  " +
                    ") ";

            db.execSQL(lSQL);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
        return (false);
    }

    public void export(OutputStreamWriter buffwriter) {

        try {
            buffwriter.write("<generalattraction>\n");

            String lSql =
                    "SELECT holidayId, dayId, attractionId, attractionAreaId, " +
                            "  scheduleId, name, heartRating, scenicRating, thrillRating," +
                            "  AttractionType, BookingReference, FlightNo, DepartsKnown, DepartsHour, " +
                            "  DepartsMin, Terminal, RestaurantFullId, ReservationType, " +
                            "  ShowKnown, ShowHour, ShowMin, PickUpKnown, PickUpHour, PickUpMin, " +
                            "  DropOffKnown, DropOffHour, DropOffMin, CheckInKnown, CheckInHour, CheckInMin, " +
                            "  ArrivalKnown, ArrivalHour, ArrivalMin " +
                            "FROM generalattraction " +
                            "ORDER BY holidayId, dayId, attractionId, attractionAreaId";

            Cursor cursor = executeSQLOpenCursor("exportRecord", lSql);
            if (cursor == null)
                return;

            while (cursor.moveToNext()) {
                buffwriter.write(
                        cursor.getString(0) + "," +
                        cursor.getString(1) + "," +
                        cursor.getString(2) + "," +
                        cursor.getString(3) + "," +
                        cursor.getString(4) + "," +
                                encodeString(cursor.getString(5)) + "," +
                        cursor.getString(6) + "," +
                        cursor.getString(7) + "," +
                        cursor.getString(8) + "," +
                                cursor.getString(9) + "," +
                                encodeString(cursor.getString(10)) + "," +
                                cursor.getString(11) + "," +
                                cursor.getString(12) + "," +
                                cursor.getString(13) + "," +
                                cursor.getString(14) + "," +
                                cursor.getString(15) + "," +
                                cursor.getString(16) + "," +
                                cursor.getString(17) + "," +
                                cursor.getString(18) + "," +
                                cursor.getString(19) + "," +
                                cursor.getString(20) + "," +
                                cursor.getString(21) + "," +
                                cursor.getString(22) + "," +
                                cursor.getString(23) + "," +
                                cursor.getString(24) + "," +
                                cursor.getString(25) + "," +
                                cursor.getString(26) + "," +
                                cursor.getString(27) + "," +
                                cursor.getString(28) + "," +
                                cursor.getString(29) + "," +
                                cursor.getString(30) + "," +
                                cursor.getString(31) + "," +
                                cursor.getString(32) + "\n"
                );

            }

        } catch (java.io.FileNotFoundException e) {
        } catch (java.io.IOException e) {
        }
    }

    private String booleanToString(boolean value)
    {
        if(value)
            return("1");
        return("0");
    }

    private boolean stringToBoolean(String value)
    {
        return value.compareTo("1") == 0;
    }

    boolean addGeneralAttractionItem(EventScheduleDetailItem item)
    {
        try
        {
            if(!IsValid())
                return (false);

            String lSql="INSERT INTO generalattraction " +
                    "  (holidayId, dayId, attractionId, attractionAreaId, " +
                    "   scheduleId, name, heartRating, scenicRating, thrillRating, " +
                    "   AttractionType, BookingReference, FlightNo, DepartsKnown, DepartsHour, " +
                    "   DepartsMin, Terminal, RestaurantFullId, ReservationType, " +
                    "   ShowKnown, ShowHour, ShowMin, PickUpKnown, PickUpHour, PickUpMin," +
                    "   DropOffKnown, DropOffHour, DropOffMin, CheckInKnown, CheckInHour, CheckInMin, " +
                    "   ArrivalKnown, ArrivalHour, ArrivalMin) " +
                    "VALUES " +
                    "(" +
                    item.holidayId + "," +
                    item.dayId + "," +
                    item.attractionId + "," +
                    item.attractionAreaId + "," +
                    item.scheduleId + "," +
                    "'" + item.name + "', " +
                    item.heartRating + "," +
                    item.scenicRating + "," +
                    item.thrillRating + ", " +
                    "'" + item.AttractionType + "', " +
                    "'" + item.BookingReference + "', " +
                    "'" + item.FlightNo + "', " +
                    "'" + booleanToString(item.DepartsKnown) + "', " +
                    item.DepartsHour + "," +
                    item.DepartsMin + "," +
                    "'" + item.Terminal + "', " +
                    item.RestaurantFullId + ", " +
                    item.ReservationType + ", " +
                    "'" + booleanToString(item.ShowKnown) + "', " +
                    item.ShowHour + ", " +
                    item.ShowMin + ", " +
                    "'" + booleanToString(item.PickUpKnown) + "', " +
                    item.PickUpHour + ", " +
                    item.PickUpMin + ", " +
                    "'" + booleanToString(item.DropOffKnown) + "', " +
                    item.DropOffHour + ", " +
                    item.DropOffMin + ", " +
                    "'" + booleanToString(item.CheckInKnown) + "', " +
                    item.CheckInHour + ", " +
                    item.CheckInMin + ", " +
                    "'" + booleanToString(item.ArrivalKnown) + "', " +
                    item.ArrivalHour + ", " +
                    item.ArrivalMin + " " +
                    ")";

            return (executeSQL("addGeneralAttractionItem", lSql));
        }
        catch(Exception e)
        {
            ShowError("addGeneralAttractionItem", e.getMessage());
        }
        return (false);

    }

    boolean updateGeneralAttractionItem(EventScheduleDetailItem item)
    {
        try
        {
            if(!IsValid())
                return (false);

            if(!ItemExists(item))
            {
                return (addGeneralAttractionItem(item));
            }

            String lSQL;
            lSQL="UPDATE generalattraction " +
                    "SET " +
                    "    heartRating = " + item.heartRating + ", " +
                    "    scenicRating = " + item.scenicRating + ", " +
                    "    thrillRating = " + item.thrillRating + ", " +
                    "    dayId = " + item.dayId + ", " +
                    "    attractionId = " + item.attractionId + ", " +
                    "    attractionAreaId = " + item.attractionAreaId + ", " +
                    "    scheduleId = " + item.scheduleId + ", " +
                    "    name = '" + item.name + "', " +
                    "    AttractionType = '" + item.AttractionType + "', " +
                    "    BookingReference = '" + item.BookingReference + "', " +
                    "    FlightNo = '" + item.FlightNo + "', " +
                    "    DepartsKnown = '" + booleanToString(item.DepartsKnown) + "', " +
                    "    DepartsHour = " + item.DepartsHour + ", " +
                    "    DepartsMin = " + item.DepartsMin + ", " +
                    "    Terminal = '" + item.Terminal + "', " +
                    "    RestaurantFullId = " + item.RestaurantFullId + ", " +
                    "    ReservationType = " + item.ReservationType + ", " +
                    "    ShowKnown = '" + booleanToString(item.ShowKnown) + "', " +
                    "    ShowHour = " + item.ShowHour + ", " +
                    "    ShowMin = " + item.ShowMin + ", " +
                    "    PickUpKnown = '" + booleanToString(item.PickUpKnown) + "', " +
                    "    PickUpHour = " + item.PickUpHour + ", " +
                    "    PickUpMin = " + item.PickUpMin + ", " +
                    "    DropOffKnown = '" + booleanToString(item.DropOffKnown) + "', " +
                    "    DropOffHour = " + item.DropOffHour + ", " +
                    "    DropOffMin = " + item.DropOffMin + ", " +
                    "    CheckInKnown = '" + booleanToString(item.CheckInKnown) + "', " +
                    "    CheckInHour = " + item.CheckInHour + ", " +
                    "    CheckInMin = " + item.CheckInMin + ", " +
                    "    ArrivalKnown = '" + booleanToString(item.ArrivalKnown) + "', " +
                    "    ArrivalHour = " + item.ArrivalHour + ", " +
                    "    ArrivalMin = " + item.ArrivalMin + " " +
                    "WHERE holidayId = " + item.holidayId + " " +
                    "AND dayId = " + item.origDayId + " " +
                    "AND attractionId = " + item.origAttractionId + " " +
                    "AND attractionAreaId = " + item.origAttractionAreaId + " " +
                    "AND scheduleId = " + item.origScheduleId;

            return (executeSQL("updateGeneralAttractionItem", lSQL));
        }
        catch(Exception e)
        {
            ShowError("updateGeneralAttractionItem", e.getMessage());
        }
        return (false);

    }

    boolean deleteGeneralAttractionItem(EventScheduleDetailItem item)
    {
        try
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
        catch(Exception e)
        {
            ShowError("deleteGeneralAttractionItem", e.getMessage());
        }
        return (false);


    }

    boolean getGeneralAttractionItem(int holidayId, int dayId, int attractionId, int attractionAreaId, int scheduleId, EventScheduleDetailItem item)
    {
        try
        {
            if(!IsValid())
                return (false);

            item.holidayId=holidayId;
            item.dayId=dayId;
            item.attractionId=attractionId;
            item.attractionAreaId=attractionAreaId;
            item.scheduleId=scheduleId;
            item.origHolidayId=holidayId;
            item.origDayId=dayId;
            item.origAttractionId=attractionId;
            item.origAttractionAreaId=attractionAreaId;
            item.origScheduleId=scheduleId;


            String lSQL;
            lSQL="SELECT holidayId, dayId, attractionId, attractionAreaId, " +
                    "  scheduleId, name, heartRating, scenicRating, thrillRating," +
                    "  AttractionType, BookingReference, FlightNo, DepartsKnown, DepartsHour, " +
                    "  DepartsMin, Terminal, RestaurantFullId, ReservationType, " +
                    "  ShowKnown, ShowHour, ShowMin, PickUpKnown, PickUpHour, PickUpMin, " +
                    "  DropOffKnown, DropOffHour, DropOffMin, CheckInKnown, CheckInHour, CheckInMin, " +
                    "  ArrivalKnown, ArrivalHour, ArrivalMin " +
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
                if(!GetGeneralAttractionItemFromQuery(cursor, item))
                    return (false);
            }
            executeSQLCloseCursor("getGeneralAttractionItem");
            return (true);
        }
        catch(Exception e)
        {
            ShowError("getGeneralAttractionItem", e.getMessage());
        }
        return (false);

    }

    private boolean GetGeneralAttractionItemFromQuery(Cursor cursor, EventScheduleDetailItem item)
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
            item.AttractionType=cursor.getString(9);
            item.BookingReference=cursor.getString(10);
            item.FlightNo=cursor.getString(11);
            item.DepartsKnown = stringToBoolean(cursor.getString(12));
            item.DepartsHour=Integer.parseInt(cursor.getString(13));
            item.DepartsMin=Integer.parseInt(cursor.getString(14));
            item.Terminal=cursor.getString(15);
            item.RestaurantFullId=Integer.parseInt(cursor.getString(16));
            item.ReservationType=Integer.parseInt(cursor.getString(17));
            item.ShowKnown = stringToBoolean(cursor.getString(18));
            item.ShowHour=Integer.parseInt(cursor.getString(19));
            item.ShowMin=Integer.parseInt(cursor.getString(20));
            item.PickUpKnown = stringToBoolean(cursor.getString(21));
            item.PickUpHour=Integer.parseInt(cursor.getString(22));
            item.PickUpMin=Integer.parseInt(cursor.getString(23));
            item.DropOffKnown = stringToBoolean(cursor.getString(24));
            item.DropOffHour=Integer.parseInt(cursor.getString(25));
            item.DropOffMin=Integer.parseInt(cursor.getString(26));
            item.CheckInKnown = stringToBoolean(cursor.getString(27));
            item.CheckInHour=Integer.parseInt(cursor.getString(28));
            item.CheckInMin=Integer.parseInt(cursor.getString(29));
            item.ArrivalKnown = stringToBoolean(cursor.getString(30));
            item.ArrivalHour=Integer.parseInt(cursor.getString(31));
            item.ArrivalMin=Integer.parseInt(cursor.getString(32));

            item.origHolidayId=item.holidayId;
            item.origDayId=item.dayId;
            item.origAttractionId=item.attractionId;
            item.origAttractionAreaId=item.attractionAreaId;
            item.origScheduleId=item.scheduleId;
            item.origName=item.name;
            item.origHeartRating=item.heartRating;
            item.origScenicRating=item.scenicRating;
            item.origThrillRating=item.scenicRating;
            item.origAttractionType=item.AttractionType;
            item.origBookingReference=item.BookingReference;
            item.origFlightNo=item.FlightNo;
            item.origDepartsKnown=item.DepartsKnown;
            item.origDepartsHour=item.DepartsHour;
            item.origDepartsMin=item.DepartsMin;
            item.origTerminal=item.Terminal;
            item.origReservationType=item.ReservationType;
            item.origRestaurantFullId=item.RestaurantFullId;
            item.origShowKnown=item.ShowKnown;
            item.origShowHour=item.ShowHour;
            item.origShowMin=item.ShowMin;
            item.origPickUpKnown=item.PickUpKnown;
            item.origPickUpHour=item.PickUpHour;
            item.origPickUpMin=item.PickUpMin;
            item.origDropOffKnown=item.DropOffKnown;
            item.origDropOffHour=item.DropOffHour;
            item.origDropOffMin=item.DropOffMin;
            item.origCheckInKnown=item.CheckInKnown;
            item.origCheckInHour=item.CheckInHour;
            item.origCheckInMin=item.CheckInMin;
            item.origArrivalKnown=item.ArrivalKnown;
            item.origArrivalHour=item.ArrivalHour;
            item.origArrivalMin=item.ArrivalMin;

            return (true);
        }
        catch(Exception e)
        {
            ShowError("GetGeneralAttractionItemFromQuery", e.getMessage());
        }

        return (false);
    }

    private boolean ItemExists(EventScheduleDetailItem item)
    {
        if(!IsValid())
            return (false);

        try
        {
            String lSQL;
            lSQL="SELECT holidayId, dayId, attractionId, attractionAreaId, " +
                    "  scheduleId " + "FROM GeneralAttraction " +
                    "WHERE HolidayId = " + item.holidayId + " " +
                    "AND DayId = " + item.dayId + " " +
                    "AND attractionId = " + item.attractionId + " " +
                    "AND attractionAreaId = " + item.attractionAreaId + " " +
                    "AND ScheduleId = " + item.scheduleId;
            try(Cursor cursor=executeSQLOpenCursor("ItemExists(generalAttraction)", lSQL)){
                if(cursor == null)
                    return (false);

                if(cursor.getCount() == 0)
                    return (false);
            }

            return (true);
        }
        catch(Exception e)
        {
            ShowError("ItemExists(generalAttraction)", e.getMessage());
        }

        return (false);
    }

}
