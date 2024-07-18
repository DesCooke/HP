package com.example.des.hp.Schedule;

import android.graphics.Bitmap;

import com.example.des.hp.Schedule.GeneralAttraction.GeneralAttractionItem;

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

    public Bitmap scheduleBitmap;
    public boolean pictureChanged;

    public GeneralAttractionItem generalAttractionItem;

    public int GetEndTimeAsMinutes()
    {
        if(generalAttractionItem!=null)
        {
            if (generalAttractionItem.CheckInKnown && generalAttractionItem.DepartsKnown && generalAttractionItem.ArrivalKnown)
                return ((generalAttractionItem.ArrivalHour * 60) + generalAttractionItem.ArrivalMin);

            if (generalAttractionItem.PickUpKnown && generalAttractionItem.DropOffKnown)
                return ((generalAttractionItem.DropOffHour * 60) + generalAttractionItem.DropOffMin);

            if (generalAttractionItem.DepartsKnown && generalAttractionItem.ArrivalKnown)
                return ((generalAttractionItem.ArrivalHour * 60) + generalAttractionItem.ArrivalMin);

            if (generalAttractionItem.ShowKnown)
                return ((generalAttractionItem.ShowHour * 60) + generalAttractionItem.ShowMin);

            if (generalAttractionItem.CheckInKnown)
                return ((generalAttractionItem.CheckInHour * 60) + generalAttractionItem.CheckInMin);

            if (generalAttractionItem.DepartsKnown)
                return ((generalAttractionItem.DepartsHour * 60) + generalAttractionItem.DepartsMin);
        }
        if(endTimeKnown)
            return((endHour*60)+endMin);
        return(0);
    }

    public int GetStartTimeAsMinutes()
    {
        if(generalAttractionItem!=null)
        {
            if (generalAttractionItem.CheckInKnown && generalAttractionItem.DepartsKnown && generalAttractionItem.ArrivalKnown)
                return ((generalAttractionItem.CheckInHour * 60) + generalAttractionItem.CheckInMin);

            if (generalAttractionItem.PickUpKnown && generalAttractionItem.DropOffKnown)
                return ((generalAttractionItem.PickUpHour * 60) + generalAttractionItem.PickUpMin);

            if (generalAttractionItem.DepartsKnown && generalAttractionItem.ArrivalKnown)
                return ((generalAttractionItem.DepartsHour * 60) + generalAttractionItem.DepartsMin);

            if (generalAttractionItem.ShowKnown)
                return ((generalAttractionItem.ShowHour * 60) + generalAttractionItem.ShowMin);

            if (generalAttractionItem.CheckInKnown)
                return ((generalAttractionItem.CheckInHour * 60) + generalAttractionItem.CheckInMin);

            if (generalAttractionItem.DepartsKnown)
                return ((generalAttractionItem.DepartsHour * 60) + generalAttractionItem.DepartsMin);
        }
        if(startTimeKnown)
            return((startHour*60)+startMin);
        return(86400);
    }

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

        generalAttractionItem=null;
    }
}
