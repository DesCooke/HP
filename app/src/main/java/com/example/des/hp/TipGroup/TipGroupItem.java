package com.example.des.hp.TipGroup;

import android.graphics.Bitmap;

public class TipGroupItem
{
    // Fields
    public int holidayId;
    public int tipGroupId;
    public int sequenceNo;
    public String tipGroupDescription;
    public String tipGroupPicture;
    public String tipGroupNotes;
    public boolean pictureAssigned;
    public int infoId;
    public int noteId;
    public int galleryId;

    // Original Fields
    public int origHolidayId;
    public int origTipGroupId;
    public int origSequenceNo;
    public String origTipGroupDescription;
    public String origTipGroupPicture;
    public String origTipGroupNotes;
    public boolean origPictureAssigned;
    public int origInfoId;
    public int origNoteId;
    public int origGalleryId;

    public boolean pictureChanged;

    public Bitmap fileBitmap;

    public TipGroupItem()
    {
    }

}
