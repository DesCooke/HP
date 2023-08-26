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
    public int optionSequenceNo;

    public boolean useOption;
    public boolean useOption1;
    public boolean useOption2;
    public boolean useOption3;
    public boolean useOption4;
    public boolean useOption5;
    public String option1Desc;
    public String option2Desc;
    public String option3Desc;
    public String option4Desc;
    public String option5Desc;
    public int option1Budget;
    public int option2Budget;
    public int option3Budget;
    public int option4Budget;
    public int option5Budget;

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

    public void RemoveOption()
    {
        int i = budgetDescription.indexOf("(");
        if(i<0)
            return;
        budgetDescription = budgetDescription.substring(0,i-1);
        budgetDescription = budgetDescription.trim();
    }

    public void AddOption(String option)
    {
        RemoveOption();
        budgetDescription = budgetDescription + " (" + option + ")";
    }

    public void CalculateUnPaid()
    {
        budgetUnpaid = budgetTotal - budgetPaid;
    }
    public BudgetItem()
    {
        budgetDescription="";
        budgetPicture="";
        budgetNotes="";
        origBudgetDescription="";
        origBudgetPicture="";
        origBudgetNotes="";
        optionSequenceNo = 0;
    }

}
