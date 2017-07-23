package com.example.des.hp.Schedule.Parade;

import android.graphics.Bitmap;

import java.util.Date;

/**
 ** Created by Des on 16/10/2016.
 */

public class ParadeItem
{
    // Fields
    public int holidayId;
    public int dayId;
    public int attractionId;
    public int attractionAreaId;
    public int scheduleId;
    public String paradeName;
    public String bookingReference;

    // Original Fields
    public int origHolidayId;
    public int origDayId;
    public int origAttractionId;
    public int origAttractionAreaId;
    public int origScheduleId;
    public String origParadeName;
    public String origBookingReference;

    public ParadeItem()
    {
        paradeName="";
        bookingReference="";
        origParadeName="";
        origBookingReference="";
    }
}
