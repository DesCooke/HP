package com.example.des.hp.Schedule.Flight;

import android.graphics.Bitmap;

import java.util.Date;

/**
 ** Created by Des on 16/10/2016.
 */

public class FlightItem
{
    // Key Access Fields
    public int holidayId;
    public int dayId;
    public int attractionId;
    public int attractionAreaId;
    public int scheduleId;
    public String flightNo;
    public boolean departsKnown;
    public int departsHour;
    public int departsMin;
    public String terminal;
    public String bookingReference;

    // Original Fields
    public int origHolidayId;
    public int origDayId;
    public int origAttractionId;
    public int origAttractionAreaId;
    public int origScheduleId;
    public String origFlightNo;
    public boolean origDepartsKnown;
    public int origDepartsHour;
    public int origDepartsMin;
    public String origTerminal;
    public String origBookingReference;

    public FlightItem()
    {
        holidayId=0;
        dayId=0;
        scheduleId=0;
        flightNo="<unknown>";
        departsHour=0;
        departsMin=0;
        terminal="<unknown>";
        bookingReference="<unknown>";

        origHolidayId=0;
        origDayId=0;
        origScheduleId=0;
        origFlightNo="<unknown>";
        origDepartsHour=0;
        origDepartsMin=0;
        origTerminal="<unknown>";
        origBookingReference="<unknown>";
    }
}
