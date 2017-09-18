package com.example.des.hp.AttractionArea;

import android.graphics.Bitmap;

/**
 ** Created by Des on 16/10/2016.
 */

public class AttractionAreaItem
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
    public int sygicId;

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
    public int origSygicId;

    public boolean pictureChanged;

    public Bitmap fileBitmap;

    public AttractionAreaItem()
    {
        attractionAreaDescription="";
        attractionAreaPicture="";
        attractionAreaNotes="";
        origAttractionAreaDescription="";
        origAttractionAreaPicture="";
        origAttractionAreaNotes="";
    }

}
