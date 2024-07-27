package com.example.des.hp.ThemeParks;

import android.graphics.Bitmap;

/**
 ** Created by Des on 16/10/2016.
 */

public class ThemeParkAreaItem
{
    // Fields
    public int holidayId;
    public int attractionId;
    public int attractionAreaId;
    public int sequenceNo;
    public String attractionAreaDescription;
    public String attractionAreaPicture;
    public String attractionAreaNotes;
    public boolean pictureAssigned;
    public int infoId;
    public int noteId;
    public int galleryId;

    // Original Fields
    public int origHolidayId;
    public int origAttractionId;
    public int origAttractionAreaId;
    public int origSequenceNo;
    public String origAttractionAreaDescription;
    public String origAttractionAreaPicture;
    public String origAttractionAreaNotes;
    public boolean origPictureAssigned;
    public int origInfoId;
    public int origNoteId;
    public int origGalleryId;

    public boolean pictureChanged;

    public Bitmap fileBitmap;

    public ThemeParkAreaItem()
    {
        attractionAreaDescription="";
        attractionAreaPicture="";
        attractionAreaNotes="";
        origAttractionAreaDescription="";
        origAttractionAreaPicture="";
        origAttractionAreaNotes="";
    }

}
