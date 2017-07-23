package com.example.des.hp.Schedule.Ride;

public class RideItem
{
    // Fields
    public int holidayId;
    public int dayId;
    public int attractionId;
    public int attractionAreaId;
    public int scheduleId;
    public String rideName;
    public float heartRating;
    public float scenicRating;
    public float thrillRating;

    // Original Fields
    public int origHolidayId;
    public int origDayId;
    public int origAttractionId;
    public int origAttractionAreaId;
    public int origScheduleId;
    public String origRideName;
    public float origHeartRating;
    public float origScenicRating;
    public float origThrillRating;

    public RideItem()
    {
        rideName="";
        origRideName="";
    }
}
