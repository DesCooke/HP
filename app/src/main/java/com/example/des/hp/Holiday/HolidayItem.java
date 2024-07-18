package com.example.des.hp.Holiday;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * * Created by Des on 16/10/2016.
 */

public class HolidayItem
{
    // Fields
    public int holidayId;
    public String holidayName;
    public String holidayPicture;
    public boolean pictureAssigned;
    public long startDateInt;
    public Date startDateDate;
    public String startDateStr;
    public boolean dateKnown;
    public int mapFileGroupId;
    public int infoId;
    public int noteId;
    public int galleryId;
    public boolean buttonDays;
    public boolean buttonDay;
    public boolean buttonMaps;
    public boolean buttonTasks;
    public boolean buttonTips;
    public boolean buttonBudget;
    public boolean buttonAttractions;
    public boolean buttonContacts;

    // Original Fields
    public int origHolidayId;
    public String origHolidayName;
    public String origHolidayPicture;
    public boolean origPictureAssigned;
    public long origStartDateInt;
    public Date origStartDateDate;
    public String origStartDateStr;
    public boolean origDateKnown;
    public int origMapFileGroupId;
    public int origInfoId;
    public int origNoteId;
    public int origGalleryId;
    public boolean origButtonDays;
    public boolean origButtonDay;
    public boolean origButtonMaps;
    public boolean origButtonTasks;
    public boolean origButtonTips;
    public boolean origButtonBudget;
    public boolean origButtonAttractions;
    public boolean origButtonContacts;

    public String ToGo;

    public Bitmap holidayBitmap;
    public boolean pictureChanged;

    public HolidayItem()
    {
        startDateDate=new Date();
        origStartDateDate=new Date();
        ToGo="";
    }

    public static boolean CompareItems(HolidayItem item1, HolidayItem item2)
    {
        if(item1.holidayId!=item2.holidayId)
            return(false);
        if(item1.holidayName.compareTo(item2.holidayName)!=0)
            return(false);
        if(item1.holidayPicture.compareTo(item2.holidayPicture)!=0)
            return(false);
        if(item1.pictureAssigned!=item2.pictureAssigned)
            return(false);
        if(item1.startDateInt!=item2.startDateInt)
            return(false);
        if(item1.startDateDate.compareTo(item2.startDateDate)!=0)
            return(false);
        if(item1.dateKnown!=item2.dateKnown)
            return(false);
        if(item1.mapFileGroupId!=item2.mapFileGroupId)
            return(false);
        if(item1.infoId!=item2.infoId)
            return(false);
        if(item1.noteId!=item2.noteId)
            return(false);
        if(item1.galleryId!=item2.galleryId)
            return(false);
        if(item1.buttonDays!=item2.buttonDays)
            return(false);
        if(item1.buttonDay!=item2.buttonDay)
            return(false);
        if(item1.buttonMaps!=item2.buttonMaps)
            return(false);
        if(item1.buttonTasks!=item2.buttonTasks)
            return(false);
        if(item1.buttonTips!=item2.buttonTips)
            return(false);
        if(item1.buttonBudget!=item2.buttonBudget)
            return(false);
        if(item1.buttonAttractions!=item2.buttonAttractions)
            return(false);
        if(item1.buttonContacts!=item2.buttonContacts)
            return(false);

        return(true);
    }

}
