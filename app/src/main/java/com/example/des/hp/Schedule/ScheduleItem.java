package com.example.des.hp.Schedule;

import android.graphics.Bitmap;

import com.example.des.hp.Schedule.Flight.*;
import com.example.des.hp.Schedule.GeneralAttraction.GeneralAttractionItem;
import com.example.des.hp.Schedule.Hotel.*;
import com.example.des.hp.Schedule.Restaurant.*;
import com.example.des.hp.Schedule.Ride.RideItem;
import com.example.des.hp.Schedule.Show.*;
import com.example.des.hp.Schedule.Bus.*;
import com.example.des.hp.Schedule.Cinema.*;
import com.example.des.hp.Schedule.Park.*;
import com.example.des.hp.Schedule.Parade.*;
import com.example.des.hp.Schedule.Other.*;

/**
 * * Created by Des on 16/10/2016.
 */

public class ScheduleItem
{
    // Fields
    public int holidayId;
    public int dayId;
    public int attractionId;
    public int attractionAreaId;
    public int scheduleId;
    public int sequenceNo;
    public int schedType;
    public String schedName;
    public String schedPicture;
    public boolean startTimeKnown;
    public int startHour;
    public int startMin;
    public boolean endTimeKnown;
    public int endHour;
    public int endMin;
    public boolean pictureAssigned;
    public int infoId;
    public int noteId;
    public int galleryId;
    public int sygicId;

    // Original Fields
    public int origHolidayId;
    public int origDayId;
    public int origAttractionId;
    public int origAttractionAreaId;
    public int origScheduleId;
    public int origSequenceNo;
    public int origSchedType;
    public String origSchedName;
    public String origSchedPicture;
    public boolean origStartTimeKnown;
    public int origStartHour;
    public int origStartMin;
    public boolean origEndTimeKnown;
    public int origEndHour;
    public int origEndMin;
    public boolean origPictureAssigned;
    public int origInfoId;
    public int origNoteId;
    public int origGalleryId;
    public int origSygicId;

    public Bitmap scheduleBitmap;
    public boolean pictureChanged;

    public FlightItem flightItem;
    public HotelItem hotelItem;
    public RestaurantItem restaurantItem;
    public ShowItem showItem;
    public BusItem busItem;
    public CinemaItem cinemaItem;
    public ParkItem parkItem;
    public ParadeItem paradeItem;
    public OtherItem otherItem;
    public RideItem rideItem;
    public GeneralAttractionItem generalAttractionItem;

    public ScheduleItem()
    {
        holidayId=0;
        dayId=0;
        scheduleId=0;
        sequenceNo=0;
        schedType=0;
        schedName="<unknown>";
        schedPicture="";
        startTimeKnown=false;
        startHour=0;
        startMin=0;
        endTimeKnown=false;
        endHour=0;
        endMin=0;
        pictureAssigned=false;

        origHolidayId=0;
        origDayId=0;
        origScheduleId=0;
        origSequenceNo=0;
        origSchedType=0;
        origSchedName="<unknown>";
        origSchedPicture="";
        origStartTimeKnown=false;
        origStartHour=0;
        origStartMin=0;
        origEndTimeKnown=false;
        origEndHour=0;
        origEndMin=0;
        origPictureAssigned=false;

        scheduleBitmap=null;
        pictureChanged=false;

        flightItem=null;
        hotelItem=null;
        restaurantItem=null;
        showItem=null;
        rideItem=null;
        generalAttractionItem=null;
    }
}
