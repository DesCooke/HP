package com.example.des.hp.Holiday;

import android.graphics.Bitmap;

import java.util.Date;

/**
 ** Created by Des on 16/10/2016.
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
        startDateDate = new Date();
        origStartDateDate = new Date();
        ToGo = "";
    }
}
