package com.example.des.hp.Schedule.Cinema;

import android.graphics.Bitmap;

import java.util.Date;

/**
 ** Created by Des on 16/10/2016.
 */

public class CinemaItem
{
    // Fields
    public int holidayId;
    public int dayId;
    public int attractionId;
    public int attractionAreaId;
    public int scheduleId;
    public String cinemaName;
    public String bookingReference;

    // Original Fields
    public int origHolidayId;
    public int origDayId;
    public int origAttractionId;
    public int origAttractionAreaId;
    public int origScheduleId;
    public String origCinemaName;
    public String origBookingReference;

    public CinemaItem()
    {
        cinemaName="";
        bookingReference="";
        origCinemaName="";
        origBookingReference="";
    }
}
