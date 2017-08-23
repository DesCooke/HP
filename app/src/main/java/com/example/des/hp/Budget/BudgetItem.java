package com.example.des.hp.Budget;

import android.graphics.Bitmap;

/**
 ** Created by Des on 16/10/2016.
 */

public class BudgetItem
{
    // Fields
    public int holidayId;
    public int budgetId;
    public int sequenceNo;
    public String budgetDescription;
    public int budgetTotal;
    public int budgetPaid;
    public int budgetUnpaid;
    public String budgetPicture;
    public String budgetNotes;
    public boolean pictureAssigned;
    public int infoId;
    public int noteId;
    public int galleryId;
    public int sygicId;

    // Original Fields
    public int origHolidayId;
    public int origBudgetId;
    public int origSequenceNo;
    public String origBudgetDescription;
    public int origBudgetTotal;
    public int origBudgetPaid;
    public int origBudgetUnpaid;
    public String origBudgetPicture;
    public String origBudgetNotes;
    public boolean origPictureAssigned;
    public int origInfoId;
    public int origNoteId;
    public int origGalleryId;
    public int origSygicId;

    public boolean pictureChanged;

    public Bitmap fileBitmap;

    public BudgetItem()
    {
        budgetDescription="";
        budgetPicture="";
        budgetNotes="";
        origBudgetDescription="";
        origBudgetPicture="";
        origBudgetNotes="";
    }

}
