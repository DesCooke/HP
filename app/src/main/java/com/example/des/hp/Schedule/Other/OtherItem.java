package com.example.des.hp.Schedule.Other;

public class OtherItem
{
    // Fields
    public int holidayId;
    public int dayId;
    public int attractionId;
    public int attractionAreaId;
    public int scheduleId;
    public String otherName;
    public String bookingReference;

    // Original Fields
    public int origHolidayId;
    public int origDayId;
    public int origAttractionId;
    public int origAttractionAreaId;
    public int origScheduleId;
    public String origOtherName;
    public String origBookingReference;

    public OtherItem()
    {
        otherName="";
        bookingReference="";
        origOtherName="";
        origBookingReference="";
    }
}
