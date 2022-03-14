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
    public String AttractionType;
    public String BookingReference;
    public String FlightNo;
    public boolean DepartsKnown;
    public int DepartsHour;
    public int DepartsMin;
    public String Terminal;
    public int RestaurantFullId;
    public int ReservationType;
    public boolean ShowKnown;
    public int ShowHour;
    public int ShowMin;
    public boolean PickUpKnown;
    public int PickUpHour;
    public int PickUpMin;
    public boolean DropOffKnown;
    public int DropOffHour;
    public int DropOffMin;
    public boolean CheckInKnown;
    public int CheckInHour;
    public int CheckInMin;
    public boolean ArrivalKnown;
    public int ArrivalHour;
    public int ArrivalMin;

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
    public String origAttractionType;
    public String origBookingReference;
    public String origFlightNo;
    public boolean origDepartsKnown;
    public int origDepartsHour;
    public int origDepartsMin;
    public String origTerminal;
    public int origRestaurantFullId;
    public int origReservationType;
    public boolean origShowKnown;
    public int origShowHour;
    public int origShowMin;
    public boolean origPickUpKnown;
    public int origPickUpHour;
    public int origPickUpMin;
    public boolean origDropOffKnown;
    public int origDropOffHour;
    public int origDropOffMin;
    public boolean origCheckInKnown;
    public int origCheckInHour;
    public int origCheckInMin;
    public boolean origArrivalKnown;
    public int origArrivalHour;
    public int origArrivalMin;

    public int GetStartTimeAsMinutes()
    {
        if(CheckInKnown && DepartsKnown && ArrivalKnown)
            return( (CheckInHour*60) + CheckInMin);

        if(PickUpKnown && DropOffKnown)
            return( (PickUpHour*60) + PickUpMin);

        if(DepartsKnown && ArrivalKnown)
            return( (DepartsHour*60) + DepartsMin);

        if (ShowKnown)
            return( (ShowHour*60) + ShowMin);

        if (CheckInKnown)
            return( (CheckInHour*60) + CheckInMin);

        if (DepartsKnown)
            return( (DepartsHour*60) + DepartsMin);

        return(0);
    }

    public GeneralAttractionItem()
    {
        name="";
        origName="";
        AttractionType="";
        origAttractionType="";
        BookingReference="";
        origBookingReference="";
        FlightNo="";
        origFlightNo="";
        Terminal="";
        origTerminal="";
    }
}
