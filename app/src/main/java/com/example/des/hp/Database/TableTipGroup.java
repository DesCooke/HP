package com.example.des.hp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.des.hp.TipGroup.TipGroupItem;
import com.example.des.hp.myutils.DateUtils;
import com.example.des.hp.myutils.MyInt;
import com.example.des.hp.myutils.MyMessages;
import com.example.des.hp.myutils.MyString;

import java.util.ArrayList;

class TableTipGroup extends TableBase
{

    TableTipGroup(Context context, SQLiteOpenHelper dbHelper)
    {
        super(context, dbHelper);
        DateUtils dateUtils=new DateUtils(context);
    }

    public void ShowError(String argFunction, String argMessage)
    {
        super.ShowError("TableTipGroup:" + argFunction, argMessage);
    }

    public boolean onCreate(SQLiteDatabase db)
    {
        try
        {
            String lSQL="CREATE TABLE IF NOT EXISTS tipgroup " +
                "( " +
                "  holidayId           INT(5),  " +
                "  tipGroupId          INT(5),  " +
                "  sequenceNo          INT(5),  " +
                "  tipGroupDescription VARCHAR, " +
                "  tipGroupPicture     VARCHAR, " +
                "  tipGroupNotes       VARCHAR, " +
                "  infoId              INT(5),  " +
                "  noteId              INT(5),  " +
                "  galleryId           INT(5),  " +
                "  sygicId             INT(5)   " +
                ")";

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
            if(oldVersion == 35 && newVersion == 36)
            {
                db.execSQL("ALTER TABLE tipgroup ADD COLUMN noteId INT(5) DEFAULT 0");
                db.execSQL("ALTER TABLE tipgroup ADD COLUMN galleryId INT(5) DEFAULT 0");
                db.execSQL("ALTER TABLE tipgroup ADD COLUMN sygicId INT(5) DEFAULT 0");

                db.execSQL("UPDATE tipgroup SET noteId = 0");
                db.execSQL("UPDATE tipgroup SET galleryId = 0");
                db.execSQL("UPDATE tipgroup SET sygicId = 0");
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("onUpgrade", e.getMessage());
            return (false);
        }
    }

    boolean addTipGroupItem(TipGroupItem tipGroupItem)
    {
        if(IsValid() == false)
            return (false);

        tipGroupItem.tipGroupPicture="";
        if(tipGroupItem.pictureAssigned)
        {
            MyString myString=new MyString();
            if(savePicture(tipGroupItem.fileBitmap, myString) == false)
                return (false);
            tipGroupItem.tipGroupPicture=myString.Value;
        }

        String lSql="INSERT INTO TipGroup " +
            "  (holidayId, tipGroupId, sequenceNo, tipGroupDescription, " +
            "   tipGroupPicture, tipGroupNotes, infoId, noteId, galleryId, sygicId) " +
            "VALUES " +
            "(" +
            tipGroupItem.holidayId + "," +
            tipGroupItem.tipGroupId + "," +
            tipGroupItem.sequenceNo + ", " +
            MyQuotedString(tipGroupItem.tipGroupDescription) + ", " +
            MyQuotedString(tipGroupItem.tipGroupPicture) + ", " +
            MyQuotedString(tipGroupItem.tipGroupNotes) + ", " +
            tipGroupItem.infoId + ", " +
            tipGroupItem.noteId + ", " +
            tipGroupItem.galleryId + ", " +
            tipGroupItem.sygicId + " " +
            ")";

        return (executeSQL("addTipGroupItem", lSql));
    }

    boolean updateTipGroupItems(ArrayList<TipGroupItem> items)
    {
        if(IsValid() == false)
            return (false);

        if(items == null)
            return (false);

        for(int i=0; i < items.size(); i++)
        {
            if(items.get(i).sequenceNo != items.get(i).origSequenceNo)
            {
                if(updateTipGroupItem(items.get(i)) == false)
                    return (false);
            }
        }
        return (true);
    }

    boolean updateTipGroupItem(TipGroupItem tipGroupItem)
    {
        if(IsValid() == false)
            return (false);

        // something has happened to the picture either selected a new one or cleared an
        // existing one
        if(tipGroupItem.pictureChanged)
        {
            if(tipGroupItem.origPictureAssigned)
                if(removePicture(tipGroupItem.origTipGroupPicture) == false)
                    return (false);
            tipGroupItem.tipGroupPicture="";
            if(tipGroupItem.pictureAssigned)
            {
                MyString myString=new MyString();
                if(savePicture(tipGroupItem.fileBitmap, myString) == false)
                    return (false);
                tipGroupItem.tipGroupPicture=myString.Value;
            }
        }

        String lSQL;
        lSQL="UPDATE TipGroup " +
            "SET sequenceNo = " + tipGroupItem.sequenceNo + ", " +
            "    tipGroupDescription = " + MyQuotedString(tipGroupItem.tipGroupDescription) + ", " +
            "    tipGroupPicture = " + MyQuotedString(tipGroupItem.tipGroupPicture) + ", " +
            "    tipGroupNotes = " + MyQuotedString(tipGroupItem.tipGroupNotes) + ", " +
            "    infoId = " + tipGroupItem.infoId + ", " +
            "    noteId = " + tipGroupItem.noteId + ", " +
            "    galleryId = " + tipGroupItem.galleryId + ", " +
            "    sygicId = " + tipGroupItem.sygicId + " " +
            "WHERE holidayId = " + tipGroupItem.holidayId + " " +
            "AND tipGroupId = " + tipGroupItem.tipGroupId;

        return (executeSQL("updateTipGroupItem", lSQL));
    }

    boolean deleteTipGroupItem(TipGroupItem tipGroupItem)

    {
        if(IsValid() == false)
            return (false);

        String lSQL="DELETE FROM TipGroup " +
            "WHERE holidayId = " + tipGroupItem.holidayId + " " +
            "AND tipGroupId = " + tipGroupItem.tipGroupId;

        if(executeSQL("deleteTipGroupItem", lSQL) == false)
            return (false);

        if(tipGroupItem.tipGroupPicture.length() > 0)
            if(removePicture(tipGroupItem.tipGroupPicture) == false)
                return (false);

        return (true);
    }

    boolean getTipGroupItem(int holidayId, int tipGroupId, TipGroupItem litem)
    {
        if(IsValid() == false)
            return (false);

        String lSQL;
        lSQL="SELECT holidayId, tipGroupId, sequenceNo, tipGroupDescription, " +
            "  tipGroupPicture, tipGroupNotes, infoId, noteId, galleryId, sygicId " +
            "FROM tipGroup " +
            "WHERE HolidayId = " + holidayId + " " +
            "AND TipGroupId = " + tipGroupId;

        Cursor cursor=executeSQLOpenCursor("getTipGroupItem", lSQL);
        if(cursor != null)
        {
            cursor.moveToFirst();
            if(GetTipGroupItemFromQuery(cursor, litem) == false)
                return (false);
        }
        executeSQLCloseCursor("getTipGroupItem");
        return (true);
    }

    private boolean GetTipGroupItemFromQuery(Cursor cursor, TipGroupItem tipGroupItem)
    {
        if(IsValid() == false)
            return (false);

        try
        {
            if(cursor.getCount() == 0)
                return (true);

            tipGroupItem.holidayId=Integer.parseInt(cursor.getString(0));
            tipGroupItem.tipGroupId=Integer.parseInt(cursor.getString(1));
            tipGroupItem.sequenceNo=Integer.parseInt(cursor.getString(2));
            tipGroupItem.tipGroupDescription=cursor.getString(3);
            tipGroupItem.tipGroupPicture=cursor.getString(4);
            tipGroupItem.tipGroupNotes=cursor.getString(5);
            tipGroupItem.infoId=Integer.parseInt(cursor.getString(6));
            tipGroupItem.noteId=Integer.parseInt(cursor.getString(7));
            tipGroupItem.galleryId=Integer.parseInt(cursor.getString(8));
            tipGroupItem.sygicId=Integer.parseInt(cursor.getString(9));

            tipGroupItem.origHolidayId=tipGroupItem.holidayId;
            tipGroupItem.origTipGroupId=tipGroupItem.tipGroupId;
            tipGroupItem.origSequenceNo=tipGroupItem.sequenceNo;
            tipGroupItem.origTipGroupDescription=tipGroupItem.tipGroupDescription;
            tipGroupItem.origTipGroupPicture=tipGroupItem.tipGroupPicture;
            tipGroupItem.origTipGroupNotes=tipGroupItem.tipGroupNotes;
            tipGroupItem.origInfoId=tipGroupItem.infoId;
            tipGroupItem.origNoteId=tipGroupItem.noteId;
            tipGroupItem.origGalleryId=tipGroupItem.galleryId;
            tipGroupItem.origSygicId=tipGroupItem.sygicId;

            tipGroupItem.pictureChanged=false;

            if(tipGroupItem.tipGroupPicture.length() > 0)
            {
                tipGroupItem.pictureAssigned=true;
                tipGroupItem.origPictureAssigned=true;
            } else
            {
                tipGroupItem.pictureAssigned=false;
                tipGroupItem.origPictureAssigned=false;
            }
        }
        catch(Exception e)
        {
            ShowError("GetTipGroupItemFromQuery", e.getMessage());
        }

        return (true);
    }

    boolean getNextTipGroupId(int holidayId, MyInt retInt)
    {
        String lSQL="SELECT IFNULL(MAX(tipGroupId),0) " +
            "FROM TipGroup " +
            "WHERE holidayId = " + holidayId;

        if(executeSQLGetInt("getNextTipGroupId", lSQL, retInt) == false)
            return (false);
        retInt.Value=retInt.Value + 1;
        return (true);
    }

    public boolean getTipGroupCount(int holidayId, MyInt retInt)
    {
        String lSQL="SELECT IFNULL(COUNT(*),0) " +
            "FROM TipGroup " +
            "WHERE holidayId = " + holidayId;

        if(executeSQLGetInt("getTipGroupCount", lSQL, retInt) == false)
            return (false);
        return (true);
    }

    boolean getNextTipGroupSequenceNo(int holidayId, MyInt retInt)
    {
        String lSQL="SELECT IFNULL(MAX(SequenceNo),0) " +
            "FROM TipGroup " +
            "WHERE holidayId = " + holidayId;

        if(executeSQLGetInt("getNextTipGroupSequenceNo", lSQL, retInt) == false)
            return (false);
        retInt.Value=retInt.Value + 1;
        return (true);
    }


    boolean getTipGroupList(int holidayId, ArrayList<TipGroupItem> al)
    {
        String lSql="SELECT holidayId, tipGroupId, sequenceNo, tipGroupDescription, " +
            "  tipGroupPicture, tipGroupNotes, infoId, noteId, galleryId, sygicId  " +
            "FROM TipGroup " +
            "WHERE holidayId = " + holidayId + " " +
            "ORDER BY SequenceNo ";

        Cursor cursor=executeSQLOpenCursor("getTipGroupList", lSql);
        if(cursor == null)
            return (false);

        while(cursor.moveToNext())
        {
            TipGroupItem tipGroupItem=new TipGroupItem();
            if(GetTipGroupItemFromQuery(cursor, tipGroupItem) == false)
                return (false);

            al.add(tipGroupItem);
        }
        return (true);
    }

    boolean clearNote(int holidayId, int noteId)
    {
        if(IsValid()==false)
            return(false);

        String l_SQL =
            "UPDATE tipGroup SET noteId = 0 " +
                "WHERE holidayId = " + holidayId + " " +
                "AND noteId = " + noteId;

        if(executeSQL("clearNote", l_SQL)==false)
            return(false);

        return(true);
    }

}
