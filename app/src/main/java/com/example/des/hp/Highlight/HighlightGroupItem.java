package com.example.des.hp.Highlight;

import android.graphics.Bitmap;

/**
 ** Created by Des on 16/10/2016.
 */

class HighlightGroupItem
{
    // Fields
    int holidayId;
    int dayId;
    int highlightGroupId;
    int sequenceNo;
    String highlightGroupName;
    String highlightGroupPicture;
    boolean pictureAssigned;

    // Original Fields
    int origHolidayId;
    int origDayId;
    int origHighlightGroupId;
    int origSequenceNo;
    String origHighlightGroupName;
    String origHighlightGroupPicture;
    boolean origPictureAssigned;

    Bitmap highlightGroupBitmap;
    boolean pictureChanged;

    HighlightGroupItem()
    {
    }
}
