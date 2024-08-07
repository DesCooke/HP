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
    public Date endDateDate;
    public String endDateStr;
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
    public boolean buttonPoi;
    public String url1;
    public String url2;
    public String url3;

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
    public boolean origButtonPoi;
    public String origUrl1;
    public String origUrl2;
    public String origUrl3;


    public String ToGo;

    public Bitmap holidayBitmap;
    public boolean pictureChanged;

    public HolidayItem()
    {
        startDateDate=new Date();
        origStartDateDate=new Date();
        ToGo="";
        url1="";
        url2="";
        url3="";
    }

}
