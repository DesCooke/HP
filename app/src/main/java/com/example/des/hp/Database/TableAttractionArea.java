package com.example.des.hp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.des.hp.AttractionArea.AttractionAreaItem;
import com.example.des.hp.myutils.MyInt;
import com.example.des.hp.myutils.MyMessages;
import com.example.des.hp.myutils.MyString;

import java.util.ArrayList;

import static com.example.des.hp.myutils.MyMessages.myMessages;

class TableAttractionArea extends TableBase
{
    TableAttractionArea(Context context, SQLiteOpenHelper dbHelper)
    {
        super(context, dbHelper);
    }
    
    public void ShowError(String argFunction, String argMessage)
    {
        super.ShowError("TableAttractionArea:" + argFunction, argMessage);
    }
    
    public boolean onCreate(SQLiteDatabase db)
    {
        try
        {
            String lSQL = "CREATE TABLE IF NOT EXISTS attractionarea " +
                "( " +
                "  holidayId                 INT(5),  " +
                "  attractionId              INT(5),  " +
                "  attractionAreaId          INT(5),  " +
                "  sequenceNo                INT(5),  " +
                "  attractionAreaDescription VARCHAR, " +
                "  attractionAreaPicture     VARCHAR, " +
                "  attractionAreaNotes       VARCHAR, " +
                "  infoId                    INT(5),  " +
                "  noteId                    INT(5),  " +
                "  galleryId                 INT(5),  " +
                "  sygicId                   INT(5)   " +
                ") ";
            
            db.execSQL(lSQL);
            
            return (true);
        }
        catch (Exception e)
        {
            ShowError("onCreate", e.getMessage());
            return (false);
        }
    }
    
    public boolean onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        try
        {
            if (oldVersion == 35 && newVersion == 36)
            {
                db.execSQL("ALTER TABLE attractionarea ADD COLUMN noteId INT(5) DEFAULT 0");
                db.execSQL("ALTER TABLE attractionarea ADD COLUMN galleryId INT(5) DEFAULT 0");
                db.execSQL("ALTER TABLE attractionarea ADD COLUMN sygicId INT(5) DEFAULT 0");
                
                db.execSQL("UPDATE attractionarea SET noteId = 0");
                db.execSQL("UPDATE attractionarea SET galleryId = 0");
                db.execSQL("UPDATE attractionarea SET sygicId = 0");
            }
            
            return (true);
        }
        catch (Exception e)
        {
            ShowError("onUpgrade", e.getMessage());
            return (false);
        }
    }
    
    boolean addAttractionAreaItem(AttractionAreaItem attractionAreaItem)
    {
        if (IsValid() == false)
            return (false);
        
        myMessages().LogMessage("addAttractionAreaItem:Handling Image");
        if (attractionAreaItem.pictureAssigned)
        {
            /* if picture name has something in it - it means it came from internal folder */
            if (attractionAreaItem.attractionAreaPicture.length() == 0)
            {
                myMessages().LogMessage("  - New Image was not from internal folder...");
                if (attractionAreaItem.pictureAssigned)
                {
                    myMessages().LogMessage("  - Save new image and get a filename...");
                    MyString myString = new MyString();
                    if (savePicture(attractionAreaItem.fileBitmap, myString) == false)
                        return (false);
                    attractionAreaItem.attractionAreaPicture = myString.Value;
                    myMessages().LogMessage("  - New filename " + attractionAreaItem.attractionAreaPicture);
                } else
                {
                    myMessages().LogMessage("  - New Image not setup - so - keep it blank");
                }
            } else
            {
                myMessages().LogMessage("  - New Image was from internal folder - so just use it ("
                    + attractionAreaItem.attractionAreaPicture + ")");
            }
        } else
        {
            myMessages().LogMessage("  - New Image not assigned - do nothing");
        }
        
        String lSql = "INSERT INTO AttractionArea " +
            "  (holidayId, attractionId, attractionAreaId, sequenceNo, attractionAreaDescription," +
            " " +
            "   attractionAreaPicture, attractionAreaNotes, infoId, noteId, galleryId, sygicId) " +
            "VALUES " +
            "(" +
            attractionAreaItem.holidayId + "," +
            attractionAreaItem.attractionId + "," +
            attractionAreaItem.attractionAreaId + "," +
            attractionAreaItem.sequenceNo + ", " +
            MyQuotedString(attractionAreaItem.attractionAreaDescription) + ", " +
            MyQuotedString(attractionAreaItem.attractionAreaPicture) + ", " +
            MyQuotedString(attractionAreaItem.attractionAreaNotes) + ", " +
            attractionAreaItem.infoId + ", " +
            attractionAreaItem.noteId + ", " +
            attractionAreaItem.galleryId + ", " +
            attractionAreaItem.sygicId + " " +
            ")";
        
        return (executeSQL("addAttractionAreaItem", lSql));
    }
    
    boolean updateAttractionAreaItems(ArrayList<AttractionAreaItem> items)
    {
        if (IsValid() == false)
            return (false);
        
        if (items == null)
            return (false);
        
        for (int i = 0; i < items.size(); i++)
        {
            if (items.get(i).sequenceNo != items.get(i).origSequenceNo)
            {
                if (updateAttractionAreaItem(items.get(i)) == false)
                    return (false);
            }
        }
        return (true);
    }
    
    boolean updateAttractionAreaItem(AttractionAreaItem item)
    {
        if (IsValid() == false)
            return (false);
        
        myMessages().LogMessage("updateAttractionAreaItem:Handling Image");
        if (item.pictureChanged)
        {
            if (item.origPictureAssigned && item.attractionAreaPicture.length() > 0 &&
                item.attractionAreaPicture.compareTo(item.origAttractionAreaPicture) == 0)
            {
                myMessages().LogMessage("  - Original Image changed back to the original - do nothing");
            } else
            {
                
                if (item.origPictureAssigned)
                {
                    myMessages().LogMessage("  - Original Image was assigned - need to get rid of it");
                    if (removePicture(item.origAttractionAreaPicture) == false)
                        return (false);
                }
            
                /* if picture name has something in it - it means it came from internal folder */
                if (item.attractionAreaPicture.length() == 0)
                {
                    myMessages().LogMessage("  - New Image was not from internal folder...");
                    if (item.pictureAssigned)
                    {
                        myMessages().LogMessage("  - Save new image and get a filename...");
                        MyString myString = new MyString();
                        if (savePicture(item.fileBitmap, myString) == false)
                            return (false);
                        item.attractionAreaPicture = myString.Value;
                        myMessages().LogMessage("  - New filename " + item.attractionAreaPicture);
                    } else
                    {
                        myMessages().LogMessage("  - New Image not setup - so - keep it blank");
                    }
                } else
                {
                    myMessages().LogMessage("  - New Image was from internal folder - so just use it ("
                        + item.attractionAreaPicture + ")");
                }
            }
            
        } else
        {
            myMessages().LogMessage("  - Image not changed - do nothing");
        }
        
        
        String lSQL;
        lSQL = "UPDATE AttractionArea " +
            "SET sequenceNo = " + item.sequenceNo + ", " +
            "    attractionAreaDescription = " + MyQuotedString(item.attractionAreaDescription) + ", " +
            "    attractionAreaPicture = " + MyQuotedString(item.attractionAreaPicture) + ", " +
            "    attractionAreaNotes = " + MyQuotedString(item.attractionAreaNotes) + ", " +
            "    infoId = " + item.infoId + ", " +
            "    noteId = " + item.noteId + ", " +
            "    galleryId = " + item.galleryId + ", " +
            "    sygicId = " + item.sygicId + " " +
            "WHERE holidayId = " + item.holidayId + " " +
            "AND attractionId = " + item.attractionId + " " +
            "AND attractionAreaId = " + item.attractionAreaId;
        
        return (executeSQL("updateAttractionAreaItem", lSQL));
    }
    
    boolean deleteAttractionAreaItem(AttractionAreaItem attractionAreaItem)
    {
        if (IsValid() == false)
            return (false);
        
        String lSQL = "DELETE FROM AttractionArea " +
            "WHERE holidayId = " + attractionAreaItem.holidayId + " " +
            "AND attractionId = " + attractionAreaItem.attractionId + " " +
            "AND attractionAreaId = " + attractionAreaItem.attractionAreaId;
        
        if (attractionAreaItem.attractionAreaPicture.length() > 0)
            if (removePicture(attractionAreaItem.attractionAreaPicture) == false)
                return (false);
        
        if (executeSQL("deleteAttractionAreaItem", lSQL) == false)
            return (false);
        
        return (true);
    }
    
    boolean getAttractionAreaItem(int holidayId, int attractionId, int attractionAreaId,
                                  AttractionAreaItem litem)
    {
        if (IsValid() == false)
            return (false);
        
        String lSQL;
        lSQL = "SELECT holidayId, attractionId, attractionAreaId, sequenceNo, " +
            "attractionAreaDescription, attractionAreaPicture, attractionAreaNotes, infoId, " +
            "noteId, galleryId, sygicId " +
            "FROM AttractionArea " +
            "WHERE HolidayId = " + holidayId + " " +
            "AND AttractionId = " + attractionId + " " +
            "and AttractionAreaId = " + attractionAreaId;
        
        Cursor cursor = executeSQLOpenCursor("getAttractionAreaItem", lSQL);
        if (cursor != null)
        {
            cursor.moveToFirst();
            if (GetAttractionAreaItemFromQuery(cursor, litem) == false)
                return (false);
        }
        executeSQLCloseCursor("getAttractionAreaItem");
        return (true);
    }
    
    private boolean GetAttractionAreaItemFromQuery(Cursor cursor, AttractionAreaItem
        attractionAreaItem)
    {
        if (IsValid() == false)
            return (false);
        
        try
        {
            if (cursor.getCount() == 0)
                return (true);
            
            attractionAreaItem.holidayId = Integer.parseInt(cursor.getString(0));
            attractionAreaItem.attractionId = Integer.parseInt(cursor.getString(1));
            attractionAreaItem.attractionAreaId = Integer.parseInt(cursor.getString(2));
            attractionAreaItem.sequenceNo = Integer.parseInt(cursor.getString(3));
            attractionAreaItem.attractionAreaDescription = cursor.getString(4);
            attractionAreaItem.attractionAreaPicture = cursor.getString(5);
            attractionAreaItem.attractionAreaNotes = cursor.getString(6);
            attractionAreaItem.infoId = Integer.parseInt(cursor.getString(7));
            attractionAreaItem.noteId = Integer.parseInt(cursor.getString(8));
            attractionAreaItem.galleryId = Integer.parseInt(cursor.getString(9));
            attractionAreaItem.sygicId = Integer.parseInt(cursor.getString(10));
            
            attractionAreaItem.origHolidayId = attractionAreaItem.holidayId;
            attractionAreaItem.origAttractionId = attractionAreaItem.attractionId;
            attractionAreaItem.origAttractionAreaId = attractionAreaItem.attractionAreaId;
            attractionAreaItem.origSequenceNo = attractionAreaItem.sequenceNo;
            attractionAreaItem.origAttractionAreaDescription = attractionAreaItem
                .attractionAreaDescription;
            attractionAreaItem.origAttractionAreaPicture = attractionAreaItem.attractionAreaPicture;
            attractionAreaItem.origAttractionAreaNotes = attractionAreaItem.attractionAreaNotes;
            attractionAreaItem.origInfoId = attractionAreaItem.infoId;
            attractionAreaItem.origNoteId = attractionAreaItem.noteId;
            attractionAreaItem.origGalleryId = attractionAreaItem.galleryId;
            attractionAreaItem.origSygicId = attractionAreaItem.sygicId;
            
            attractionAreaItem.pictureChanged = false;
            
            if (attractionAreaItem.attractionAreaPicture.length() > 0)
            {
                attractionAreaItem.pictureAssigned = true;
                attractionAreaItem.origPictureAssigned = true;
            } else
            {
                attractionAreaItem.pictureAssigned = false;
                attractionAreaItem.origPictureAssigned = false;
            }
        }
        catch (Exception e)
        {
            ShowError("GetAttractionAreaItemFromQuery", e.getMessage());
        }
        
        return (true);
    }
    
    boolean getNextAttractionAreaId(int holidayId, int attractionId, MyInt myInt)
    {
        String lSQL = "SELECT IFNULL(MAX(attractionAreaId),0) " +
            "FROM AttractionArea " +
            "WHERE holidayId = " + holidayId + " " +
            "AND attractionId = " + attractionId;
        
        if (executeSQLGetInt("getNextAttractionAreaId", lSQL, myInt) == false)
            return (false);
        
        myInt.Value = myInt.Value + 1;
        
        return (true);
    }
    
    boolean getNextAttractionAreaSequenceNo(int holidayId, int attractionId, MyInt myInt)
    {
        String lSQL = "SELECT IFNULL(MAX(SequenceNo),0) " +
            "FROM AttractionArea " +
            "WHERE holidayId = " + holidayId + " " +
            "AND attractionId = " + attractionId;
        
        if (executeSQLGetInt("getNextAttractionAreaSequenceNo", lSQL, myInt) == false)
            return (false);
        
        myInt.Value = myInt.Value + 1;
        
        return (true);
    }
    
    
    boolean getAttractionAreaList(int holidayId, int attractionId, ArrayList<AttractionAreaItem> al)
    {
        String lSql = "SELECT holidayId, attractionId, attractionAreaId, sequenceNo, " +
            "attractionAreaDescription, attractionAreaPicture, attractionAreaNotes, infoId,  " +
            "noteId, galleryId, sygicId " +
            "FROM AttractionArea " +
            "WHERE holidayId = " + holidayId + " " +
            "AND attractionId = " + attractionId + " " +
            "ORDER BY SequenceNo ";
        
        Cursor cursor = executeSQLOpenCursor("getAttractionAreaList", lSql);
        if (cursor == null)
            return (false);
        
        while (cursor.moveToNext())
        {
            AttractionAreaItem attractionAreaItem = new AttractionAreaItem();
            if (GetAttractionAreaItemFromQuery(cursor, attractionAreaItem) == false)
                return (false);
            
            al.add(attractionAreaItem);
        }
        return (true);
    }
    
    boolean clearNote(int holidayId, int noteId)
    {
        if (IsValid() == false)
            return (false);
        
        String l_SQL =
            "UPDATE attractionarea SET noteId = 0 " +
                "WHERE holidayId = " + holidayId + " " +
                "AND noteId = " + noteId;
        
        if (executeSQL("clearNote", l_SQL) == false)
            return (false);
        
        return (true);
    }
}
