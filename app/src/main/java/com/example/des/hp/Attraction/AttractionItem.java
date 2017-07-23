package com.example.des.hp.Attraction;

import android.graphics.Bitmap;

/**
 ** Created by Des on 16/10/2016.
 */

public class AttractionItem
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
    public int sygicId;

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
    public int origSygicId;

    public boolean pictureChanged;

    public Bitmap fileBitmap;

    public AttractionItem()
    {
    }

}
