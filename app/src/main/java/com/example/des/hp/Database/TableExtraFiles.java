package com.example.des.hp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.des.hp.ExtraFiles.ExtraFilesItem;
import com.example.des.hp.myutils.MyInt;
import com.example.des.hp.myutils.MyString;

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
                "  filePicture     VARCHAR  " +
                ") ";

            db.execSQL(lSQL);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
            return (false);
        }
    }

    public boolean onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        try
        {
            return (true);
        }
        catch(Exception e)
        {
            ShowError("onUpgrade", e.getMessage());
            return (false);
        }
    }

    boolean addExtraFilesItem(ExtraFilesItem extraFilesItem)
    {
        if(IsValid() == false)
            return (false);

        extraFilesItem.filePicture="";
        if(extraFilesItem.pictureAssigned)
        {
            MyString myString=new MyString();
            if(savePicture(extraFilesItem.fileBitmap, myString) == false)
                return (false);
            extraFilesItem.filePicture=myString.Value;
        }
        if(extraFilesItem.fileName.length() > 0)
        {
            String lNewString=extraFilesItem.fileName.replace("'", "");
            extraFilesItem.fileName=lNewString;
            if(saveExtraFile(extraFilesItem.fileUri, extraFilesItem.fileName) == false)
                return (false);
        }

        String lSql="INSERT INTO ExtraFiles " +
            "  (fileGroupId, fileId, fileDescription, fileName, filePicture, sequenceNo) " +
            "VALUES " +
            "(" +
            extraFilesItem.fileGroupId + "," +
            extraFilesItem.fileId + "," +
            MyQuotedString(extraFilesItem.fileDescription) + ", " +
            MyQuotedString(extraFilesItem.fileName) + "," +
            MyQuotedString(extraFilesItem.filePicture) + "," +
            extraFilesItem.sequenceNo + " " +
            ")";

        return (executeSQL("addExtraFilesItem", lSql));
    }

    boolean updateExtraFilesItems(ArrayList<ExtraFilesItem> items)
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

    boolean updateExtraFilesItem(ExtraFilesItem extraFilesItem)
    {
        if(IsValid() == false)
            return (false);

        // something has happened to the picture either selected a new one or cleared an
        // existing one
        if(extraFilesItem.pictureChanged)
        {
            if(extraFilesItem.origPictureAssigned)
                if(removePicture(extraFilesItem.origFilePicture) == false)
                    return (false);
            extraFilesItem.filePicture="";
            if(extraFilesItem.pictureAssigned)
            {
                MyString myString=new MyString();
                if(savePicture(extraFilesItem.fileBitmap, myString) == false)
                    return (false);
                extraFilesItem.filePicture=myString.Value;
            }
        }
        if(extraFilesItem.fileChanged)
        {
            if(extraFilesItem.origFileName.length() > 0)
                if(removeExtraFile(extraFilesItem.origFileName) == false)
                    return (false);
            if(extraFilesItem.fileName.length() > 0)
            {
                String lNewString=extraFilesItem.fileName.replace("'", "");
                extraFilesItem.fileName=lNewString;
                if(saveExtraFile(extraFilesItem.fileUri, extraFilesItem.fileName) == false)
                    return (false);
            }
        }

        String lSQL;
        lSQL="UPDATE ExtraFiles " +
            "SET fileDescription = " + MyQuotedString(extraFilesItem.fileDescription) + ", " +
            "    fileName = " + MyQuotedString(extraFilesItem.fileName) + ", " +
            "    filePicture = " + MyQuotedString(extraFilesItem.filePicture) + ", " +
            "    sequenceNo = " + extraFilesItem.sequenceNo + " " +
            "WHERE fileGroupId = " + extraFilesItem.fileGroupId + " " +
            "AND fileId = " + extraFilesItem.origFileId;

        return (executeSQL("updateOtherItem", lSQL));
    }

    boolean deleteExtraFilesItem(ExtraFilesItem extraFilesItem)
    {
        if(IsValid() == false)
            return (false);

        String lSQL="DELETE FROM ExtraFiles " +
            "WHERE fileGroupId = " + extraFilesItem.fileGroupId + " " +
            "AND fileId = " + extraFilesItem.fileId;

        if(executeSQL("deleteExtraFilesItem", lSQL) == false)
            return (false);

        if(extraFilesItem.filePicture.length() > 0)
            if(removePicture(extraFilesItem.filePicture) == false)
                return (false);

        if(extraFilesItem.fileName.length() > 0)
            if(removeExtraFile(extraFilesItem.fileName) == false)
                return (false);

        return (true);
    }

    boolean getExtraFilesItem(int fileGroupId, int fileId, ExtraFilesItem litem)
    {
        if(IsValid() == false)
            return (false);

        String lSQL;
        lSQL="SELECT fileGroupId, fileId, fileDescription, fileName, filePicture, sequenceNo " +
            "FROM ExtraFiles " +
            "WHERE FileGroupId = " + fileGroupId + " " +
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
        }
        catch(Exception e)
        {
            ShowError("GetExtraFilesItemFromQuery", e.getMessage());
        }

        return (true);
    }

    boolean getNextExtraFilesId(int fileGroupId, MyInt retInt)
    {
        String lSQL="SELECT IFNULL(MAX(fileId),0) FROM ExtraFiles WHERE fileGroupId = " + fileGroupId;

        if(executeSQLGetInt("getNextSequenceNo", lSQL, retInt) == false)
            return (false);

        retInt.Value=retInt.Value + 1;

        return (true);
    }

    boolean getNextFileGroupId(MyInt retInt)
    {
        if(getNextFileId("fgi", retInt) == false)
            return (false);

        if(setNextFileId("fgi", retInt.Value + 1) == false)
            return (false);

        return (true);
    }

    boolean getExtraFilesCount(int fileGroupId, MyInt retInt)
    {
        String lSQL="SELECT IFNULL(COUNT(*),0) FROM ExtraFiles WHERE fileGroupId = " + fileGroupId;

        if(executeSQLGetInt("getNextSequenceNo", lSQL, retInt) == false)
            return (false);

        return (true);
    }

    boolean getNextExtraFilesSequenceNo(int fileGroupId, MyInt retInt)
    {
        String lSQL="SELECT IFNULL(MAX(SequenceNo),0) FROM ExtraFiles WHERE fileGroupId = " + fileGroupId;

        if(executeSQLGetInt("getNextSequenceNo", lSQL, retInt) == false)
            return (false);

        retInt.Value=retInt.Value + 1;

        return (true);
    }

    public boolean deleteExtraFilesList(int fileGroupId)
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

    boolean getExtraFilesList(int fileGroupId, ArrayList<ExtraFilesItem> al)
    {
        String lSql="SELECT fileGroupId, fileId, fileDescription, fileName, filePicture, sequenceNo " +
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


}
