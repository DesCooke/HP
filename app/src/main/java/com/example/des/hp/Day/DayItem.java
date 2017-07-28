package com.example.des.hp.Day;

import android.graphics.Bitmap;

import com.example.des.hp.Dialog.BaseItem;

/**
 ** Created by Des on 16/10/2016.
 */

public class DayItem extends BaseItem
{
    // Fields
    public int holidayId;
    public int dayId;
    public int sequenceNo;
    public String dayName;
    public String dayPicture;
    public boolean pictureAssigned;
    public int dayCat;
    public int infoId;
    public int noteId;
    public int galleryId;
    public int sygicId;

    // Original Fields
    public int origHolidayId;
    public int origDayId;
    public int origSequenceNo;
    public String origDayName;
    public String origDayPicture;
    public boolean origPictureAssigned;
    public int origDayCat;
    public int origInfoId;
    public int origNoteId;
    public int origGalleryId;
    public int origSygicId;

    public Bitmap dayBitmap;
    public boolean pictureChanged;

    public int startOfDayHours;
    public int startOfDayMins;
    public int endOfDayHours;
    public int endOfDayMins;
    public int totalHours;
    public int totalMins;

    public String start_at;
    public String end_at;

    public DayItem()
    {
        start_at="";
        end_at="";
    }

}
