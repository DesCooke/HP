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
    public int sygicId;

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
    public int origSygicId;

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
        if(item1.sygicId!=item2.sygicId)
            return(false);

        return(true);
    }

}
