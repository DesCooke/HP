package com.example.des.hp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import com.example.des.hp.ExtraFiles.ExtraFilesItem;
import com.example.des.hp.myutils.ImageUtils;
import com.example.des.hp.myutils.MyInt;
import com.example.des.hp.myutils.MyString;
import com.example.des.hp.myutils.MyUri;

import java.io.File;
import java.util.ArrayList;

import static com.example.des.hp.myutils.MyMessages.myMessages;

class TableExtraFiles extends TableBase
{
    TableExtraFiles(Context context, SQLiteOpenHelper dbHelper)
    {
        super(context, dbHelper);
    }

    public void ShowError(String argFunction, String argMessage)
    {
        super.ShowError("TableExtraFiles:" + argFunction, argMessage);
    }

    public boolean onCreate(SQLiteDatabase db)
    {
        try
        {
            String lSQL="CREATE TABLE IF NOT EXISTS extraFiles " +
                    "( " +
                    "  fileGroupId     INT(5),  " +
                    "  fileId          INT(5),  " +
                    "  sequenceNo      INT(5),  " +
                    "  fileDescription VARCHAR, " +
                    "  fileName        VARCHAR, " +
                    "  filePicture     VARCHAR, " +
                    "  holidayId       INT(5)   " +
                    ") ";

            db.execSQL(lSQL);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
        return (false);
    }

    public boolean onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        try
        {
            if(oldVersion==44 && newVersion==45)
            {
                db.execSQL("ALTER TABLE extraFiles ADD COLUMN holidayId INT(5) DEFAULT 0");
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("onUpgrade", e.getMessage());
        }
        return (false);
    }

    boolean addExtraFilesItem(ExtraFilesItem extraFilesItem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            if(extraFilesItem.pictureAssigned)
            {
            /* if picture name has something in it - it means it came from internal folder */
                if(extraFilesItem.filePicture.length() == 0)
                {
                    //myMessages().LogMessage("  - New Image was not from internal folder...");
                    if(extraFilesItem.pictureAssigned)
                    {
                        //myMessages().LogMessage("  - Save new image and get a filename...");
                        MyString myString=new MyString();
                        if(savePicture(/*holidayid*/ 0, extraFilesItem.fileBitmap, myString) == false)
                            return (false);
                        extraFilesItem.filePicture=myString.Value;
                        //myMessages().LogMessage("  - New filename " + extraFilesItem.filePicture);
                    } else
                    {
                        //myMessages().LogMessage("  - New Image not setup - so - keep it blank");
                    }
                } else
                {
                    //myMessages().LogMessage("  - New Image was from internal folder - so just use it (" + extraFilesItem.filePicture + ")");
                }
            } else
            {
                //myMessages().LogMessage("  - New Image not assigned - do nothing");
            }


            if(extraFilesItem.fileName.length() > 0)
            {
                String lFilename;
                MyInt myInt=new MyInt();
                if(getNextFileId("file", myInt) == false)
                    return (false);

                if(setNextFileId("file", myInt.Value + 1) == false)
                    return (false);

                MyUri myUri = new MyUri(_context);
                Uri toUri=myUri.getUri(ImageUtils.imageUtils().GetHolidayFileDir(extraFilesItem.holidayId) + "/" + extraFilesItem.fileName);

                // put back into a string
                String theString=toUri.toString();

                //get the extension - pdf etc
                String extension=MimeTypeMap.getFileExtensionFromUrl(theString);
                if(extension == null)
                    return (false);
                lFilename="file_" + myInt.Value + "." + extension;


                extraFilesItem.fileName=lFilename;
                if(extraFilesItem.internalFilename.length() == 0)
                    if(saveExtraFile(extraFilesItem.holidayId, extraFilesItem.fileUri, extraFilesItem.fileName) == false)
                        return (false);
            }

            String lSql="INSERT INTO ExtraFiles " +
                    "  (fileGroupId, fileId, fileDescription, fileName, filePicture, sequenceNo, holidayId) " +
                    "VALUES " + "(" + extraFilesItem.fileGroupId + "," + extraFilesItem.fileId +
                    "," + MyQuotedString(extraFilesItem.fileDescription) + ", " +
                    MyQuotedString(extraFilesItem.fileName) + "," +
                    MyQuotedString(extraFilesItem.filePicture) + "," +
                    extraFilesItem.sequenceNo + ", " +
                    extraFilesItem.holidayId + ")";

            return (executeSQL("addExtraFilesItem", lSql));
        }
        catch(Exception e)
        {
            ShowError("addExtraFilesItem", e.getMessage());
        }
        return (false);
    }

    boolean updateExtraFilesItems(ArrayList<ExtraFilesItem> items)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            if(items == null)
                return (false);

            for(int i=0; i < items.size(); i++)
            {
                if(items.get(i).sequenceNo != items.get(i).origSequenceNo)
                {
                    if(updateExtraFilesItem(items.get(i)) == false)
                        return (false);
                }
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("updateExtraFilesItems", e.getMessage());
        }
        return (false);

    }

    boolean updateExtraFilesItem(ExtraFilesItem extraFilesItem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            if(extraFilesItem.fileChanged)
            {
                if(extraFilesItem.origFileName.length() > 0 && extraFilesItem.origFileName.compareTo(extraFilesItem.internalFilename) != 0)
                    if(removeExtraFile(extraFilesItem.holidayId, extraFilesItem.origFileName) == false)
                        return (false);
                if(extraFilesItem.internalFilename.length() == 0 && extraFilesItem.fileName.length() > 0)
                {
                    extraFilesItem.fileName=extraFilesItem.fileName.replace("'", "");
                    if(saveExtraFile(extraFilesItem.holidayId, extraFilesItem.fileUri, extraFilesItem.fileName) == false)
                        return (false);
                }
            }

            String lSQL;
            lSQL="UPDATE ExtraFiles " + "SET fileDescription = " + MyQuotedString(extraFilesItem.fileDescription) + ", " + "    fileName = " + MyQuotedString(extraFilesItem.fileName) + ", " + "    filePicture = " + MyQuotedString(extraFilesItem.filePicture) + ", " + "    sequenceNo = " + extraFilesItem.sequenceNo + " " + "WHERE fileGroupId = " + extraFilesItem.fileGroupId + " " + "AND fileId = " + extraFilesItem.origFileId;

            return (executeSQL("updateOtherItem", lSQL));
        }
        catch(Exception e)
        {
            ShowError("updateExtraFilesItem", e.getMessage());
        }
        return (false);

    }

    boolean deleteExtraFilesItem(ExtraFilesItem extraFilesItem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            String lSQL="DELETE FROM ExtraFiles " + "WHERE fileGroupId = " + extraFilesItem.fileGroupId + " " + "AND fileId = " + extraFilesItem.fileId;

            if(extraFilesItem.fileName.length() > 0)
                if(removeExtraFile(extraFilesItem.holidayId, extraFilesItem.fileName) == false)
                    return (false);

            if(executeSQL("deleteExtraFilesItem", lSQL) == false)
                return (false);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("deleteExtraFilesItem", e.getMessage());
        }
        return (false);

    }

    boolean getExtraFilesItem(int fileGroupId, int fileId, ExtraFilesItem litem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            String lSQL;
            lSQL="SELECT fileGroupId, fileId, fileDescription, fileName, filePicture, sequenceNo, holidayId " +
                    "FROM ExtraFiles " + "WHERE FileGroupId = " + fileGroupId + " " +
                    "AND FileId = " + fileId;

            Cursor cursor=executeSQLOpenCursor("getExtraFilesItem", lSQL);
            if(cursor != null)
            {
                cursor.moveToFirst();
                if(GetExtraFilesItemFromQuery(cursor, litem) == false)
                    return (false);
            }
            executeSQLCloseCursor("getExtraFilesItem");
            return (true);
        }
        catch(Exception e)
        {
            ShowError("getExtraFilesItem", e.getMessage());
        }
        return (false);

    }

    private boolean GetExtraFilesItemFromQuery(Cursor cursor, ExtraFilesItem extraFilesItem)
    {
        if(IsValid() == false)
            return (false);

        try
        {
            if(cursor.getCount() == 0)
                return (true);

            extraFilesItem.fileGroupId=Integer.parseInt(cursor.getString(0));
            extraFilesItem.fileId=Integer.parseInt(cursor.getString(1));
            extraFilesItem.fileDescription=cursor.getString(2);
            extraFilesItem.fileName=cursor.getString(3);
            extraFilesItem.filePicture=cursor.getString(4);
            extraFilesItem.sequenceNo=Integer.parseInt(cursor.getString(5));
            extraFilesItem.holidayId=Integer.parseInt(cursor.getString(6));

            extraFilesItem.origFileGroupId=extraFilesItem.fileGroupId;
            extraFilesItem.origFileId=extraFilesItem.fileId;
            extraFilesItem.origFileDescription=extraFilesItem.fileDescription;
            extraFilesItem.origFileName=extraFilesItem.fileName;
            extraFilesItem.origFilePicture=extraFilesItem.filePicture;
            extraFilesItem.origSequenceNo=extraFilesItem.sequenceNo;

            extraFilesItem.pictureChanged=false;

            if(extraFilesItem.filePicture.length() > 0)
            {
                extraFilesItem.pictureAssigned=true;
                extraFilesItem.origPictureAssigned=true;
            } else
            {
                extraFilesItem.pictureAssigned=false;
                extraFilesItem.origPictureAssigned=false;
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("GetExtraFilesItemFromQuery", e.getMessage());
        }

        return (false);
    }

    boolean getNextExtraFilesId(int fileGroupId, MyInt retInt)
    {
        try
        {
            String lSQL="SELECT IFNULL(MAX(fileId),0) FROM ExtraFiles WHERE fileGroupId = " + fileGroupId;

            if(executeSQLGetInt("getNextSequenceNo", lSQL, retInt) == false)
                return (false);

            retInt.Value=retInt.Value + 1;

            return (true);
        }
        catch(Exception e)
        {
            ShowError("getNextExtraFilesId", e.getMessage());
        }
        return (false);

    }

    boolean getNextFileGroupId(MyInt retInt)
    {
        try
        {
            if(getNextFileId("fgi", retInt) == false)
                return (false);

            if(setNextFileId("fgi", retInt.Value + 1) == false)
                return (false);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("getNextFileGroupId", e.getMessage());
        }
        return (false);

    }

    boolean getExtraFilesCount(int fileGroupId, MyInt retInt)
    {
        try
        {
            String lSQL="SELECT IFNULL(COUNT(*),0) FROM ExtraFiles WHERE fileGroupId = " + fileGroupId;

            if(executeSQLGetInt("getNextSequenceNo", lSQL, retInt) == false)
                return (false);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("getExtraFilesCount", e.getMessage());
        }
        return (false);

    }

    boolean getNextExtraFilesSequenceNo(int fileGroupId, MyInt retInt)
    {
        try
        {
            String lSQL="SELECT IFNULL(MAX(SequenceNo),0) FROM ExtraFiles WHERE fileGroupId = " + fileGroupId;

            if(executeSQLGetInt("getNextSequenceNo", lSQL, retInt) == false)
                return (false);

            retInt.Value=retInt.Value + 1;

            return (true);
        }
        catch(Exception e)
        {
            ShowError("getNextExtraFilesSequenceNo", e.getMessage());
        }
        return (false);

    }

    public boolean deleteExtraFilesList(int fileGroupId)
    {
        try
        {
            ArrayList<ExtraFilesItem> al=new ArrayList<>();
            if(getExtraFilesList(fileGroupId, al) == false)
                return (false);

            for(int i=0; i < al.size(); i++)
            {
                if(deleteExtraFilesItem(al.get(i)) == false)
                    return (false);
            }

            return (true);
        }
        catch(Exception e)
        {
            ShowError("deleteExtraFilesList", e.getMessage());
        }
        return (false);

    }

    boolean getExtraFilesList(int fileGroupId, ArrayList<ExtraFilesItem> al)
    {
        try
        {
            String lSql="SELECT fileGroupId, fileId, fileDescription, fileName, filePicture, sequenceNo, holidayId " +
                    "FROM ExtraFiles " +
                    "WHERE fileGroupId = " + fileGroupId + " " +
                    "ORDER BY SequenceNo ";

            Cursor cursor=executeSQLOpenCursor("getExtraFilesList", lSql);
            if(cursor == null)
                return (false);

            while(cursor.moveToNext())
            {
                ExtraFilesItem extraFilesItem=new ExtraFilesItem();
                if(GetExtraFilesItemFromQuery(cursor, extraFilesItem) == false)
                    return (false);

                al.add(extraFilesItem);
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("getExtraFilesList", e.getMessage());
        }
        return (false);

    }


}
