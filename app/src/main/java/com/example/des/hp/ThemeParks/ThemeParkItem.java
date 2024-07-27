package com.example.des.hp.ThemeParks;

import android.graphics.Bitmap;

/**
 * * Created by Des on 16/10/2016.
 */

public class ThemeParkItem
{
    // Fields
    public int holidayId;
    public int attractionId;
    public int sequenceNo;
    public String attractionDescription;
    public String attractionPicture;
    public String attractionNotes;
    public boolean pictureAssigned;
    public int infoId;
    public int noteId;
    public int galleryId;

    // Original Fields
    public int origHolidayId;
    public int origAttractionId;
    public int origSequenceNo;
    public String origAttractionDescription;
    public String origAttractionPicture;
    public String origAttractionNotes;
    public boolean origPictureAssigned;
    public int origInfoId;
    public int origNoteId;
    public int origGalleryId;

    public boolean pictureChanged;
    
    public Bitmap fileBitmap;
    
    public ThemeParkItem()
    {
        attractionDescription="";
        attractionNotes="";
        attractionPicture="";
        origAttractionDescription="";
        origAttractionNotes="";
        origAttractionPicture="";
    }
    
}
