package com.example.des.hp.Tasks;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * * Created by Des on 16/10/2016.
 */

public class TaskItem
{
    // Fields
    public int holidayId;
    public int taskId;
    public int sequenceNo;
    public String taskDescription;
    public boolean taskDateKnown;
    public long taskDateInt;
    public Date taskDateDate;
    public String taskDateString;
    public String taskPicture;
    public boolean taskComplete;
    public String taskNotes;
    public boolean pictureAssigned;
    public int infoId;
    public int noteId;
    public int galleryId;
    public int sygicId;
    
    // Original Fields
    public int origHolidayId;
    public int origTaskId;
    public int origSequenceNo;
    public String origTaskDescription;
    public boolean origTaskDateKnown;
    public long origTaskDateInt;
    public Date origTaskDateDate;
    public String origTaskDateString;
    public String origTaskPicture;
    public boolean origTaskComplete;
    public String origTaskNotes;
    public boolean origPictureAssigned;
    public int origInfoId;
    public int origNoteId;
    public int origGalleryId;
    public int origSygicId;
    
    public boolean pictureChanged;
    
    public Bitmap fileBitmap;
    
    public TaskItem()
    {
        taskDateDate = new Date();
        origTaskDateDate = new Date();
        
        taskDescription = "";
        taskDateString = "";
        taskPicture = "";
        taskNotes = "";
        origTaskDescription = "";
        origTaskDateString = "";
        origTaskPicture = "";
        origTaskNotes = "";
    }
    
}
