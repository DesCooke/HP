package com.example.des.hp.Event;

import android.graphics.Bitmap;

/**
 * * Created by Des on 16/10/2016.
 */

public class EventScheduleItem
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
    public String url1;
    public String url2;
    public String url3;

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
    public String origUrl1;
    public String origUrl2;
    public String origUrl3;

    public Bitmap scheduleBitmap;
    public boolean pictureChanged;

    public EventScheduleDetailItem eventScheduleDetailItem;

    public int GetEndTimeAsMinutes()
    {
        if(eventScheduleDetailItem !=null)
        {
            if (eventScheduleDetailItem.CheckInKnown && eventScheduleDetailItem.DepartsKnown && eventScheduleDetailItem.ArrivalKnown)
                return ((eventScheduleDetailItem.ArrivalHour * 60) + eventScheduleDetailItem.ArrivalMin);

            if (eventScheduleDetailItem.PickUpKnown && eventScheduleDetailItem.DropOffKnown)
                return ((eventScheduleDetailItem.DropOffHour * 60) + eventScheduleDetailItem.DropOffMin);

            if (eventScheduleDetailItem.DepartsKnown && eventScheduleDetailItem.ArrivalKnown)
                return ((eventScheduleDetailItem.ArrivalHour * 60) + eventScheduleDetailItem.ArrivalMin);

            if (eventScheduleDetailItem.ShowKnown)
                return ((eventScheduleDetailItem.ShowHour * 60) + eventScheduleDetailItem.ShowMin);

            if (eventScheduleDetailItem.CheckInKnown)
                return ((eventScheduleDetailItem.CheckInHour * 60) + eventScheduleDetailItem.CheckInMin);

            if (eventScheduleDetailItem.DepartsKnown)
                return ((eventScheduleDetailItem.DepartsHour * 60) + eventScheduleDetailItem.DepartsMin);
        }
        if(endTimeKnown)
            return((endHour*60)+endMin);
        return(0);
    }

    public int GetStartTimeAsMinutes()
    {
        if(eventScheduleDetailItem !=null)
        {
            if (eventScheduleDetailItem.CheckInKnown && eventScheduleDetailItem.DepartsKnown && eventScheduleDetailItem.ArrivalKnown)
                return ((eventScheduleDetailItem.CheckInHour * 60) + eventScheduleDetailItem.CheckInMin);

            if (eventScheduleDetailItem.PickUpKnown && eventScheduleDetailItem.DropOffKnown)
                return ((eventScheduleDetailItem.PickUpHour * 60) + eventScheduleDetailItem.PickUpMin);

            if (eventScheduleDetailItem.DepartsKnown && eventScheduleDetailItem.ArrivalKnown)
                return ((eventScheduleDetailItem.DepartsHour * 60) + eventScheduleDetailItem.DepartsMin);

            if (eventScheduleDetailItem.ShowKnown)
                return ((eventScheduleDetailItem.ShowHour * 60) + eventScheduleDetailItem.ShowMin);

            if (eventScheduleDetailItem.CheckInKnown)
                return ((eventScheduleDetailItem.CheckInHour * 60) + eventScheduleDetailItem.CheckInMin);

            if (eventScheduleDetailItem.DepartsKnown)
                return ((eventScheduleDetailItem.DepartsHour * 60) + eventScheduleDetailItem.DepartsMin);
        }
        if(startTimeKnown)
            return((startHour*60)+startMin);
        return(86400);
    }

    public EventScheduleItem()
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
        url1="";
        url2="";
        url3="";

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
        origUrl1="";
        origUrl2="";
        origUrl3="";

        scheduleBitmap=null;
        pictureChanged=false;

        eventScheduleDetailItem =new EventScheduleDetailItem();
    }
}
