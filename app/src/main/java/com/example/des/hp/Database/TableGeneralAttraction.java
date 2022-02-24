package com.example.des.hp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.des.hp.R;
import com.example.des.hp.Schedule.GeneralAttraction.GeneralAttractionItem;
import com.example.des.hp.Schedule.ScheduleItem;
import com.example.des.hp.myutils.MyInt;

import java.util.Random;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

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

    private String booleanToString(boolean value)
    {
        if(value)
            return("1");
        return("0");
    }

    private boolean stringToBoolean(String value)
    {
        if(value.compareTo("1")==0)
            return(true);
        return(false);
    }

    public boolean onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        try
        {
            if(oldVersion == 41 && newVersion == 42)
            {
                onCreate(db);
            }
            if(oldVersion == 46 && newVersion == 47)
            {
                db.execSQL("ALTER TABLE generalattraction ADD COLUMN AttractionType VARCHAR DEFAULT ''");
                db.execSQL("ALTER TABLE generalattraction ADD COLUMN BookingReference VARCHAR DEFAULT ''");
                db.execSQL("ALTER TABLE generalattraction ADD COLUMN FlightNo VARCHAR DEFAULT ''");
                db.execSQL("ALTER TABLE generalattraction ADD COLUMN DepartsHour INT(2) DEFAULT 0");
                db.execSQL("ALTER TABLE generalattraction ADD COLUMN DepartsMin INT(2) DEFAULT 0");
                db.execSQL("ALTER TABLE generalattraction ADD COLUMN Terminal VARCHAR DEFAULT ''");
                db.execSQL("ALTER TABLE generalattraction ADD COLUMN RestaurantFullId INT(5) DEFAULT 0");
                db.execSQL("ALTER TABLE generalattraction ADD COLUMN ReservationType INT(5) DEFAULT 0");
                db.execSQL("ALTER TABLE generalattraction ADD COLUMN ShowHour INT(2) DEFAULT 0");
                db.execSQL("ALTER TABLE generalattraction ADD COLUMN ShowMin INT(2) DEFAULT 0");
            }
            if(oldVersion == 47 && newVersion == 48) {
                db.execSQL("ALTER TABLE generalattraction ADD COLUMN DepartsKnown VARCHAR DEFAULT ''");
                db.execSQL("ALTER TABLE generalattraction ADD COLUMN ShowKnown VARCHAR DEFAULT ''");
            }
            if(oldVersion == 48 && newVersion == 49) {
                db.execSQL("ALTER TABLE generalattraction ADD COLUMN PickUpHour INT(2) DEFAULT 0");
                db.execSQL("ALTER TABLE generalattraction ADD COLUMN PickUpMin INT(2) DEFAULT 0");
                db.execSQL("ALTER TABLE generalattraction ADD COLUMN DropOffHour INT(2) DEFAULT 0");
                db.execSQL("ALTER TABLE generalattraction ADD COLUMN DropOffMin INT(2) DEFAULT 0");
                db.execSQL("ALTER TABLE generalattraction ADD COLUMN CheckInHour INT(2) DEFAULT 0");
                db.execSQL("ALTER TABLE generalattraction ADD COLUMN CheckInMin INT(2) DEFAULT 0");
                db.execSQL("ALTER TABLE generalattraction ADD COLUMN ArrivalHour INT(2) DEFAULT 0");
                db.execSQL("ALTER TABLE generalattraction ADD COLUMN ArrivalMin INT(2) DEFAULT 0");
            }
            if(oldVersion == 49 && newVersion == 50) {
                db.execSQL("ALTER TABLE generalattraction ADD COLUMN PickUpKnown VARCHAR DEFAULT '0'");
                db.execSQL("ALTER TABLE generalattraction ADD COLUMN DropOffKnown VARCHAR DEFAULT '0'");
                db.execSQL("ALTER TABLE generalattraction ADD COLUMN CheckInKnown VARCHAR DEFAULT '0'");
                db.execSQL("ALTER TABLE generalattraction ADD COLUMN ArrivalKnown VARCHAR DEFAULT '0'");
            }
            if(oldVersion == 50 && newVersion == 51) {
                String lSQL=
                        "INSERT INTO generalattraction " +
                                " (AttractionType, holidayId, dayId, attractionId, attractionAreaId," +
                                "  scheduleId, bookingReference) " +
                                " SELECT 'Bus', holidayId, dayId, attractionId, attractionAreaId, " +
                                "       scheduleId, bookingReference " +
                                "FROM Bus ";
                db.execSQL(lSQL);
                db.execSQL("UPDATE schedule SET schedType = " + _context.getResources().getInteger(R.integer.schedule_type_generalattraction) + " " +
                        "WHERE schedType = " + _context.getResources().getInteger(R.integer.schedule_type_bus));
                db.execSQL("DELETE FROM Bus");
            }
            if(oldVersion == 51 && newVersion == 52) {
                String lSQL=
                        "INSERT INTO generalattraction " +
                                " (AttractionType, holidayId, dayId, attractionId, attractionAreaId," +
                                "  scheduleId, name, bookingReference) " +
                                " SELECT 'Cinema', holidayId, dayId, attractionId, attractionAreaId, " +
                                "  scheduleId, cinemaName, bookingReference " +
                                "FROM Cinema ";
                db.execSQL(lSQL);
                db.execSQL("UPDATE schedule SET schedType = " + _context.getResources().getInteger(R.integer.schedule_type_generalattraction) + " " +
                        "WHERE schedType = " + _context.getResources().getInteger(R.integer.schedule_type_cinema));
                db.execSQL("DELETE FROM Cinema");
            }
            if(oldVersion == 52 && newVersion == 53)
            {
                String lSQL=
                        "INSERT INTO generalattraction " +
                                " (AttractionType, holidayId, dayId, attractionId, attractionAreaId," +
                                "  scheduleId, FlightNo, DepartsKnown, DepartsHour, DepartsMin, Terminal, bookingReference) " +
                                " SELECT 'Flight', holidayId, dayId, attractionId, attractionAreaId, " +
                                "  scheduleId, flightNo, '1', departsHour, departsMin, terminal, bookingReference " +
                                "FROM Flight ";
                db.execSQL(lSQL);
                db.execSQL("UPDATE schedule SET schedType = " + _context.getResources().getInteger(R.integer.schedule_type_generalattraction) + " " +
                        "WHERE schedType = " + _context.getResources().getInteger(R.integer.schedule_type_flight));
                db.execSQL("DELETE FROM Flight");
            }
            if(oldVersion == 53 && newVersion == 54)
            {
                String lSQL=
                        "INSERT INTO generalattraction " +
                                " (AttractionType, holidayId, dayId, attractionId, attractionAreaId," +
                                "  scheduleId, Name, bookingReference) " +
                                " SELECT 'Hotel', holidayId, dayId, attractionId, attractionAreaId, " +
                                "  scheduleId, hotelName, bookingReference " +
                                "FROM Hotel ";
                db.execSQL(lSQL);
                db.execSQL("UPDATE schedule SET schedType = " + _context.getResources().getInteger(R.integer.schedule_type_generalattraction) + " " +
                        "WHERE schedType = " + _context.getResources().getInteger(R.integer.schedule_type_hotel));
                db.execSQL("DELETE FROM Hotel");
            }
            if(oldVersion == 54 && newVersion == 55)
            {
                String lSQL=
                        "INSERT INTO generalattraction " +
                                " (AttractionType, holidayId, dayId, attractionId, attractionAreaId," +
                                "  scheduleId, Name, bookingReference) " +
                                " SELECT 'Other', holidayId, dayId, attractionId, attractionAreaId, " +
                                "  scheduleId, otherName, bookingReference " +
                                "FROM Other ";
                db.execSQL(lSQL);
                db.execSQL("UPDATE schedule SET schedType = " + _context.getResources().getInteger(R.integer.schedule_type_generalattraction) + " " +
                        "WHERE schedType = " + _context.getResources().getInteger(R.integer.schedule_type_other));
                db.execSQL("DELETE FROM Other");
            }
            if(oldVersion == 55 && newVersion == 56)
            {
                String lSQL=
                        "INSERT INTO generalattraction " +
                                " (AttractionType, holidayId, dayId, attractionId, attractionAreaId," +
                                "  scheduleId, Name, bookingReference) " +
                                " SELECT 'Parade', holidayId, dayId, attractionId, attractionAreaId, " +
                                "  scheduleId, paradeName, bookingReference " +
                                "FROM Parade ";
                db.execSQL(lSQL);
                db.execSQL("UPDATE schedule SET schedType = " + _context.getResources().getInteger(R.integer.schedule_type_generalattraction) + " " +
                        "WHERE schedType = " + _context.getResources().getInteger(R.integer.schedule_type_parade));
                db.execSQL("DELETE FROM Parade");
            }
            if(oldVersion == 56 && newVersion == 57)
            {
                String lSQL=
                        "INSERT INTO generalattraction " +
                                " (AttractionType, holidayId, dayId, attractionId, attractionAreaId," +
                                "  scheduleId, Name, bookingReference) " +
                                " SELECT 'Park', holidayId, dayId, attractionId, attractionAreaId, " +
                                "  scheduleId, parkName, bookingReference " +
                                "FROM Park ";
                db.execSQL(lSQL);
                db.execSQL("UPDATE schedule SET schedType = " + _context.getResources().getInteger(R.integer.schedule_type_generalattraction) + " " +
                        "WHERE schedType = " + _context.getResources().getInteger(R.integer.schedule_type_park));
                db.execSQL("DELETE FROM Park");
            }
            if(oldVersion == 57 && newVersion == 58)
            {
                String lSQL=
                        "INSERT INTO generalattraction " +
                                " (AttractionType, holidayId, dayId, attractionId, attractionAreaId," +
                                "  scheduleId, Name, bookingReference, RestaurantFullId, ReservationType) " +
                                " SELECT 'Restaurant', holidayId, dayId, attractionId, attractionAreaId, " +
                                "  scheduleId, restaurantName, bookingReference, restaurantFullId, reservationType " +
                                "FROM Restaurant ";
                db.execSQL(lSQL);
                db.execSQL("UPDATE schedule SET schedType = " + _context.getResources().getInteger(R.integer.schedule_type_generalattraction) + " " +
                        "WHERE schedType = " + _context.getResources().getInteger(R.integer.schedule_type_restaurant));
                db.execSQL("DELETE FROM Restaurant");
            }
            if(oldVersion == 58 && newVersion == 59)
            {
                String lSQL=
                        "INSERT INTO generalattraction " +
                                " (AttractionType, holidayId, dayId, attractionId, attractionAreaId," +
                                "  scheduleId, Name, HeartRating, ScenicRating, ThrillRating) " +
                                " SELECT 'Ride', holidayId, dayId, attractionId, attractionAreaId, " +
                                "  scheduleId, rideName, heartRating, scenicRating, thrillRating " +
                                "FROM Ride ";
                db.execSQL(lSQL);
                db.execSQL("UPDATE schedule SET schedType = " + _context.getResources().getInteger(R.integer.schedule_type_generalattraction) + " " +
                        "WHERE schedType = " + _context.getResources().getInteger(R.integer.schedule_type_ride));
                db.execSQL("DELETE FROM Ride");
            }
            if(oldVersion == 59 && newVersion == 60)
            {
                String lSQL=
                        "INSERT INTO generalattraction " +
                                " (AttractionType, holidayId, dayId, attractionId, attractionAreaId," +
                                "  scheduleId, Name, ShowKnown, ShowHour, ShowMin, BookingReference, " +
                                "    HeartRating, ScenicRating, ThrillRating) " +
                                " SELECT 'Show', holidayId, dayId, attractionId, attractionAreaId, " +
                                "    scheduleId, showName, '1', showHour, showMin, bookingReference, " +
                                "    heartRating, scenicRating, thrillRating " +
                                "FROM Show ";
                db.execSQL(lSQL);
                db.execSQL("UPDATE schedule SET schedType = " + _context.getResources().getInteger(R.integer.schedule_type_generalattraction) + " " +
                        "WHERE schedType = " + _context.getResources().getInteger(R.integer.schedule_type_show));
                db.execSQL("DELETE FROM Show");
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("onUpgrade", e.getMessage());
        }
        return (false);
    }

    boolean addGeneralAttractionItem(GeneralAttractionItem item)
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

    boolean updateGeneralAttractionItem(GeneralAttractionItem item)
    {
        try
        {
            if(!IsValid())
                return (false);

            if(ItemExists(item) == false)
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

    boolean deleteGeneralAttractionItem(GeneralAttractionItem item)
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

    boolean getGeneralAttractionItem(int holidayId, int dayId, int attractionId, int attractionAreaId, int scheduleId, GeneralAttractionItem litem)
    {
        try
        {
            if(!IsValid())
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
                if(!GetGeneralAttractionItemFromQuery(cursor, litem))
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

    private boolean ItemExists(GeneralAttractionItem litem)
    {
        if(IsValid() == false)
            return (false);

        try
        {
            String lSQL;
            lSQL="SELECT holidayId, dayId, attractionId, attractionAreaId, " +
                    "  scheduleId " + "FROM GeneralAttraction " +
                    "WHERE HolidayId = " + litem.holidayId + " " +
                    "AND DayId = " + litem.dayId + " " +
                    "AND attractionId = " + litem.attractionId + " " +
                    "AND attractionAreaId = " + litem.attractionAreaId + " " +
                    "AND ScheduleId = " + litem.scheduleId;
            Cursor cursor=executeSQLOpenCursor("ItemExists(generalAttraction)", lSQL);
            if(cursor == null)
                return (false);

            if(cursor.getCount() == 0)
                return (false);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("ItemExists(generalAttraction)", e.getMessage());
        }

        return (false);
    }

}
