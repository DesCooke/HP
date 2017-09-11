package com.example.des.hp.Schedule.Restaurant;

public class RestaurantItem
{
    // Fields
    public int holidayId;
    public int dayId;
    public int attractionId;
    public int attractionAreaId;
    public int scheduleId;
    public String restaurantName;
    public String bookingReference;
    public int restaurantFullId;
    public int reservationType; /* 0=unknown, 1=walk in, 2 = on the day, 3 = 180 days */

    // Original Fields
    public int origHolidayId;
    public int origDayId;
    public int origAttractionId;
    public int origAttractionAreaId;
    public int origScheduleId;
    public String origRestaurantName;
    public String origBookingReference;
    public int origRestaurantFullId;
    public int origReservationType;

    public RestaurantItem()
    {
        restaurantName="";
        bookingReference="";
        origRestaurantName="";
        origBookingReference="";
        reservationType = 0;
    }
}
