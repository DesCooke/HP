package com.example.des.hp.Schedule.Hotel;

public class HotelItem
{
    // Fields
    public int holidayId;
    public int dayId;
    public int attractionId;
    public int attractionAreaId;
    public int scheduleId;
    public String hotelName;
    public String bookingReference;

    // Original Fields
    public int origHolidayId;
    public int origDayId;
    public int origAttractionId;
    public int origAttractionAreaId;
    public int origScheduleId;
    public String origHotelName;
    public String origBookingReference;

    public HotelItem()
    {
        hotelName="";
        bookingReference="";
        origHotelName="";
        origBookingReference="";
    }
}
