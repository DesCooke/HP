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

import java.util.ArrayList;

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

    boolean addExtraFilesItem(ExtraFilesItem extraFilesItem)
    {
        try
        {
            if(!IsValid())
                return (false);

            if(extraFilesItem.pictureAssigned) {
                /* if picture name has something in it - it means it came from internal folder */
                if (extraFilesItem.filePicture.isEmpty()) {
                    //myMessages().LogMessage("  - Save new image and get a filename...");
                    MyString myString = new MyString();
                    if (!savePicture(/*holidayid*/ 0, extraFilesItem.fileBitmap, myString))
                        return (false);
                    extraFilesItem.filePicture = myString.Value;
                    //myMessages().LogMessage("  - New filename " + extraFilesItem.filePicture);
                }
            }


            if(!extraFilesItem.fileName.isEmpty())
            {
                String lFilename;
                MyInt myInt=new MyInt();
                if(!getNextFileId("file", myInt))
                    return (false);

                if(!setNextFileId("file", myInt.Value + 1))
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
                if(extraFilesItem.internalFilename.isEmpty())
                    if(!saveExtraFile(extraFilesItem.holidayId, extraFilesItem.fileUri, extraFilesItem.fileName))
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
            if(!IsValid())
                return (false);

            if(items == null)
                return (false);

            for(int i=0; i < items.size(); i++)
            {
                if(items.get(i).sequenceNo != items.get(i).origSequenceNo)
                {
                    if(!updateExtraFilesItem(items.get(i)))
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
            if(!IsValid())
                return (false);

            if(extraFilesItem.fileChanged)
            {
                if(!extraFilesItem.origFileName.isEmpty() && extraFilesItem.origFileName.compareTo(extraFilesItem.internalFilename) != 0)
                    if(!removeExtraFile(extraFilesItem.holidayId, extraFilesItem.origFileName))
                        return (false);
                if(extraFilesItem.internalFilename.isEmpty() && !extraFilesItem.fileName.isEmpty())
                {
                    extraFilesItem.fileName=extraFilesItem.fileName.replace("'", "");
                    if(!saveExtraFile(extraFilesItem.holidayId, extraFilesItem.fileUri, extraFilesItem.fileName))
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
            if(!IsValid())
                return (false);

            String lSQL="DELETE FROM ExtraFiles " + "WHERE fileGroupId = " + extraFilesItem.fileGroupId + " " + "AND fileId = " + extraFilesItem.fileId;

            if(!extraFilesItem.fileName.isEmpty())
                if(!removeExtraFile(extraFilesItem.holidayId, extraFilesItem.fileName))
                    return (false);

            if(!executeSQL("deleteExtraFilesItem", lSQL))
                return (false);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("deleteExtraFilesItem", e.getMessage());
        }
        return (false);

    }

    boolean getExtraFilesItem(int fileGroupId, int fileId, ExtraFilesItem item)
    {
        try
        {
            if(!IsValid())
                return (false);

            String lSQL;
            lSQL="SELECT fileGroupId, fileId, fileDescription, fileName, filePicture, sequenceNo, holidayId " +
                    "FROM ExtraFiles " + "WHERE FileGroupId = " + fileGroupId + " " +
                    "AND FileId = " + fileId;

            Cursor cursor=executeSQLOpenCursor("getExtraFilesItem", lSQL);
            if(cursor != null)
            {
                cursor.moveToFirst();
                if(!GetExtraFilesItemFromQuery(cursor, item))
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
        if(!IsValid())
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

            if(!extraFilesItem.filePicture.isEmpty())
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

            if(!executeSQLGetInt("getNextSequenceNo", lSQL, retInt))
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
            if(!getNextFileId("fgi", retInt))
                return (false);

            if(!setNextFileId("fgi", retInt.Value + 1))
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

            return executeSQLGetInt("getNextSequenceNo", lSQL, retInt);
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

            if(!executeSQLGetInt("getNextSequenceNo", lSQL, retInt))
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
                if(!GetExtraFilesItemFromQuery(cursor, extraFilesItem))
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
