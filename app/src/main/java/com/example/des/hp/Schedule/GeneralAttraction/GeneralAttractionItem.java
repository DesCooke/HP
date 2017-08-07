package com.example.des.hp.Schedule.GeneralAttraction;

public class GeneralAttractionItem
{
    // Fields
    public int holidayId;
    public int dayId;
    public int attractionId;
    public int attractionAreaId;
    public int scheduleId;
    public String name;
    public float heartRating;
    public float scenicRating;
    public float thrillRating;

    // Original Fields
    public int origHolidayId;
    public int origDayId;
    public int origAttractionId;
    public int origAttractionAreaId;
    public int origScheduleId;
    public String origName;
    public float origHeartRating;
    public float origScenicRating;
    public float origThrillRating;

    public GeneralAttractionItem()
    {
        name="";
        origName="";
    }
}
