package com.example.des.hp.Contact;

import android.graphics.Bitmap;

/**
 ** Created by Des on 16/10/2016.
 */

public class ContactItem
{
    // Fields
    public int holidayId;
    public int contactId;
    public int sequenceNo;
    public String contactDescription;
    public String contactPicture;
    public String contactNotes;
    public boolean pictureAssigned;
    public int infoId;
    public int noteId;
    public int galleryId;
    public int sygicId;

    // Original Fields
    public int origHolidayId;
    public int origContactId;
    public int origSequenceNo;
    public String origContactDescription;
    public String origContactPicture;
    public String origContactNotes;
    public boolean origPictureAssigned;
    public int origInfoId;
    public int origNoteId;
    public int origGalleryId;
    public int origSygicId;

    public boolean pictureChanged;

    public Bitmap fileBitmap;

    public ContactItem()
    {
    }

}
