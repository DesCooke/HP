package com.example.des.hp.Schedule.Show;

public class ShowItem
{
    // Fields
    public int holidayId;
    public int dayId;
    public int attractionId;
    public int attractionAreaId;
    public int scheduleId;
    public String showName;
    public int showHour;
    public int showMin;
    public String bookingReference;
    public float heartRating;
    public float scenicRating;
    public float thrillRating;

    // Original Fields
    public int origHolidayId;
    public int origDayId;
    public int origAttractionId;
    public int origAttractionAreaId;
    public int origScheduleId;
    public String origShowName;
    public int origShowHour;
    public int origShowMin;
    public String origBookingReference;
    public float origHeartRating;
    public float origScenicRating;
    public float origThrillRating;

    public ShowItem()
    {
        showName="";
        bookingReference="";
        origShowName="";
        origBookingReference="";
    }
}
