package com.example.des.hp.Highlight;

import android.graphics.Bitmap;

/**
 ** Created by Des on 16/10/2016.
 */

class HighlightItem
{
    // Fields
    int holidayId;
    int dayId;
    int highlightGroupId;
    int highlightId;
    int sequenceNo;
    int highlightType;
    String highlightName;
    String highlightPicture;
    boolean pictureAssigned;

    // Original Fields
    int origHolidayId;
    int origDayId;
    int origHighlightGroupId;
    int origHighlightId;
    int origSequenceNo;
    int origHighlightType;
    String origHighlightName;
    String origHighlightPicture;
    boolean origPictureAssigned;

    Bitmap highlightBitmap;
    boolean pictureChanged;

    HighlightItem()
    {
    }
}
