package com.example.des.hp.ExtraFiles;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 ** Created by Des on 16/10/2016.
 */

public class ExtraFilesItem
{
    // Fields
    public int fileGroupId;
    public int fileId;
    public int sequenceNo;
    public int holidayId;
    public String fileDescription;
    public String fileName;
    public String filePicture;
    public boolean pictureAssigned;
    public Uri fileUri;

    // Original Fields
    public int origFileGroupId;
    public int origFileId;
    public int origSequenceNo;
    public String origFileDescription;
    public String origFileName;
    public String origFilePicture;
    public boolean origPictureAssigned;

    public boolean pictureChanged;
    public boolean fileChanged;

    public Bitmap fileBitmap;

    public String internalFilename;

    public ExtraFilesItem()
    {
    }

}
