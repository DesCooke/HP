package com.example.des.hp.Schedule.Bus;


public class BusItem
{
    // Key Access Fields
    public int holidayId;
    public int dayId;
    public int attractionId;
    public int attractionAreaId;
    public int scheduleId;

    public String bookingReference;

    // Original Fields
    public int origHolidayId;
    public int origDayId;
    public int origAttractionId;
    public int origAttractionAreaId;
    public int origScheduleId;

    public String origBookingReference;

    public BusItem()
    {
        holidayId=0;
        dayId=0;
        scheduleId=0;
        bookingReference="<unknown>";

        origHolidayId=0;
        origDayId=0;
        origScheduleId=0;
        origBookingReference="<unknown>";
    }
}
