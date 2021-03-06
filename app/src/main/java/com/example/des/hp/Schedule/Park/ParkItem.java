package com.example.des.hp.Schedule.Park;

public class ParkItem
{
    // Fields
    public int holidayId;
    public int dayId;
    public int attractionId;
    public int attractionAreaId;
    public int scheduleId;
    public String parkName;
    public String bookingReference;

    // Original Fields
    public int origHolidayId;
    public int origDayId;
    public int origAttractionId;
    public int origAttractionAreaId;
    public int origScheduleId;
    public String origParkName;
    public String origBookingReference;

    public ParkItem()
    {
        parkName="";
        bookingReference="";
        origParkName="";
        origBookingReference="";
    }
}
