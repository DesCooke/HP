package com.example.des.hp.Tip;

import android.graphics.Bitmap;

/**
 ** Created by Des on 16/10/2016.
 */

public class TipItem
{
    // Fields
    public int holidayId;
    public int tipGroupId;
    public int tipId;
    public int sequenceNo;
    public String tipDescription;
    public String tipPicture;
    public String tipNotes;
    public boolean pictureAssigned;
    public int infoId;
    public int noteId;
    public int galleryId;

    // Original Fields
    public int origHolidayId;
    public int origTipGroupId;
    public int origTipId;
    public int origSequenceNo;
    public String origTipDescription;
    public String origTipPicture;
    public String origTipNotes;
    public boolean origPictureAssigned;
    public int origInfoId;
    public int origNoteId;
    public int origGalleryId;

    public boolean pictureChanged;

    public Bitmap fileBitmap;

    public TipItem()
    {
        tipDescription="";
        tipPicture="";
        tipNotes="";
    }

}
