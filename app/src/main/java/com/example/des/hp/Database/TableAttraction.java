package com.example.des.hp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.des.hp.Attraction.AttractionItem;
import com.example.des.hp.myutils.MyInt;
import com.example.des.hp.myutils.MyString;

import java.util.ArrayList;

import static com.example.des.hp.myutils.MyMessages.myMessages;


class TableAttraction extends TableBase
{
    TableAttraction(Context context, SQLiteOpenHelper dbHelper)
    {
        super(context, dbHelper);
    }
    
    public void ShowError(String argFunction, String argMessage)
    {
        super.ShowError("TableAttraction:" + argFunction, argMessage);
    }
    
    public boolean onCreate(SQLiteDatabase db)
    {
        try
        {
            String lSQL = "CREATE TABLE IF NOT EXISTS attraction " +
                "( " +
                "  holidayId             INT(5),  " +
                "  attractionId          INT(5),  " +
                "  sequenceNo            INT(5),  " +
                "  attractionDescription VARCHAR, " +
                "  attractionPicture     VARCHAR, " +
                "  attractionNotes       VARCHAR, " +
                "  infoId                INT(5),  " +
                "  noteId                INT(5),  " +
                "  galleryId             INT(5),  " +
                "  sygicId               INT(5)   " +
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
                db.execSQL("ALTER TABLE attraction ADD COLUMN noteId INT(5) DEFAULT 0");
                db.execSQL("ALTER TABLE attraction ADD COLUMN galleryId INT(5) DEFAULT 0");
                db.execSQL("ALTER TABLE attraction ADD COLUMN sygicId INT(5) DEFAULT 0");
                
                db.execSQL("UPDATE attraction SET noteId = 0");
                db.execSQL("UPDATE attraction SET galleryId = 0");
                db.execSQL("UPDATE attraction SET sygicId = 0");
            }
            
            return (true);
        }
        catch (Exception e)
        {
            ShowError("onUpgrade", e.getMessage());
            return (false);
        }
    }
    
    boolean getAttractionsCount(int holidayId, MyInt myInt)
    {
        if (IsValid() == false)
            return (false);
        
        String lSQL = "SELECT IFNULL(COUNT(*),0) " +
            "FROM Attraction " +
            "WHERE holidayId = " + holidayId;
        
        return (executeSQLGetInt("getAttractionsCount", lSQL, myInt));
    }
    
    boolean addAttractionItem(AttractionItem attractionItem)
    {
        if (IsValid() == false)
            return (false);
        
        
        myMessages().LogMessage("addAttractionItem:Handling Image");
        if (attractionItem.pictureAssigned)
        {
            /* if picture name has something in it - it means it came from internal folder */
            if (attractionItem.attractionPicture.length() == 0)
            {
                myMessages().LogMessage("  - New Image was not from internal folder...");
                if (attractionItem.pictureAssigned)
                {
                    myMessages().LogMessage("  - Save new image and get a filename...");
                    MyString myString = new MyString();
                    if (savePicture(attractionItem.fileBitmap, myString) == false)
                        return (false);
                    attractionItem.attractionPicture = myString.Value;
                    myMessages().LogMessage("  - New filename " + attractionItem.attractionPicture);
                } else
                {
                    myMessages().LogMessage("  - New Image not setup - so - keep it blank");
                }
            } else
            {
                myMessages().LogMessage("  - New Image was from internal folder - so just use it ("
                    + attractionItem.attractionPicture + ")");
            }
        } else
        {
            myMessages().LogMessage("  - New Image not assigned - do nothing");
        }
        
        String lSql = "INSERT INTO Attraction " +
            "  (holidayId, attractionId, sequenceNo, attractionDescription, " +
            "   attractionPicture, attractionNotes, infoId, noteId, galleryId, sygicId) " +
            "VALUES " +
            "(" +
            attractionItem.holidayId + "," +
            attractionItem.attractionId + "," +
            attractionItem.sequenceNo + ", " +
            MyQuotedString(attractionItem.attractionDescription) + ", " +
            MyQuotedString(attractionItem.attractionPicture) + ", " +
            MyQuotedString(attractionItem.attractionNotes) + ", " +
            attractionItem.infoId + ", " +
            attractionItem.noteId + ", " +
            attractionItem.galleryId + ", " +
            attractionItem.sygicId + " " +
            ")";
        
        return (executeSQL("addAttractionItem", lSql));
    }
    
    boolean updateAttractionItems(ArrayList<AttractionItem> items)
    {
        if (IsValid() == false)
            return (false);
        
        if (items == null)
            return (false);
        
        for (int i = 0; i < items.size(); i++)
        {
            if (items.get(i).sequenceNo != items.get(i).origSequenceNo)
            {
                if (updateAttractionItem(items.get(i)) == false)
                    return (false);
            }
        }
        return (true);
    }
    
    boolean updateAttractionItem(AttractionItem attractionItem)
    {
        if (IsValid() == false)
            return (false);
        
        myMessages().LogMessage("updateAttractionItem:Handling Image");
        if (attractionItem.pictureChanged)
        {
            if (attractionItem.origPictureAssigned && attractionItem.attractionPicture.length() > 0 &&
                attractionItem.attractionPicture.compareTo(attractionItem.origAttractionPicture) == 0)
            {
                myMessages().LogMessage("  - Original Image changed back to the original - do nothing");
            } else
            {
                
                if (attractionItem.origPictureAssigned)
                {
                    myMessages().LogMessage("  - Original Image was assigned - need to get rid of it");
                    if (removePicture(attractionItem.origAttractionPicture) == false)
                        return (false);
                }
            
                /* if picture name has something in it - it means it came from internal folder */
                if (attractionItem.attractionPicture.length() == 0)
                {
                    myMessages().LogMessage("  - New Image was not from internal folder...");
                    if (attractionItem.pictureAssigned)
                    {
                        myMessages().LogMessage("  - Save new image and get a filename...");
                        MyString myString = new MyString();
                        if (savePicture(attractionItem.fileBitmap, myString) == false)
                            return (false);
                        attractionItem.attractionPicture = myString.Value;
                        myMessages().LogMessage("  - New filename " + attractionItem.attractionPicture);
                    } else
                    {
                        myMessages().LogMessage("  - New Image not setup - so - keep it blank");
                    }
                } else
                {
                    myMessages().LogMessage("  - New Image was from internal folder - so just use it ("
                        + attractionItem.attractionPicture + ")");
                }
            }
        } else
        {
            myMessages().LogMessage("  - Image not changed - do nothing");
        }
        
        
        String lSQL;
        lSQL = "UPDATE Attraction " +
            "SET sequenceNo = " + attractionItem.sequenceNo + ", " +
            "    attractionDescription = " + MyQuotedString(attractionItem.attractionDescription)
            + ", " +
            "    attractionPicture = " + MyQuotedString(attractionItem.attractionPicture) + ", " +
            "    attractionNotes = " + MyQuotedString(attractionItem.attractionNotes) + ", " +
            "    infoId = " + attractionItem.infoId + ", " +
            "    noteId = " + attractionItem.noteId + ", " +
            "    galleryId = " + attractionItem.galleryId + ", " +
            "    sygicId = " + attractionItem.sygicId + " " +
            "WHERE holidayId = " + attractionItem.holidayId + " " +
            "AND attractionId = " + attractionItem.attractionId;
        
        return (executeSQL("updateAttractionItem", lSQL));
    }
    
    boolean deleteAttractionItem(AttractionItem attractionItem)
    
    {
        if (IsValid() == false)
            return (false);
        
        String lSQL = "DELETE FROM Attraction " +
            "WHERE holidayId = " + attractionItem.holidayId + " " +
            "AND attractionId = " + attractionItem.attractionId;
        
        if (attractionItem.attractionPicture.length() > 0)
            if (removePicture(attractionItem.attractionPicture) == false)
                return (false);
        
        if (executeSQL("deleteAttractionItem", lSQL) == false)
            return (false);
        
        return (true);
    }
    
    boolean getAttractionItem(int holidayId, int attractionId, AttractionItem attractionItem)
    {
        if (IsValid() == false)
            return (false);
        
        String lSQL;
        lSQL = "SELECT holidayId, attractionId, sequenceNo, attractionDescription, " +
            "  attractionPicture, attractionNotes, infoId, noteId, galleryId, sygicId " +
            "FROM attraction " +
            "WHERE HolidayId = " + holidayId + " " +
            "AND AttractionId = " + attractionId;
        
        Cursor cursor = executeSQLOpenCursor("getAttractionItem", lSQL);
        if (cursor != null)
        {
            cursor.moveToFirst();
            if (GetAttractionItemFromQuery(cursor, attractionItem) == false)
                return (false);
        }
        executeSQLCloseCursor("getAttractionItem");
        return (true);
    }
    
    private boolean GetAttractionItemFromQuery(Cursor cursor, AttractionItem attractionItem)
    {
        if (IsValid() == false)
            return (false);
        
        try
        {
            if (cursor.getCount() == 0)
                return (false);
            
            attractionItem.holidayId = Integer.parseInt(cursor.getString(0));
            attractionItem.attractionId = Integer.parseInt(cursor.getString(1));
            attractionItem.sequenceNo = Integer.parseInt(cursor.getString(2));
            attractionItem.attractionDescription = cursor.getString(3);
            attractionItem.attractionPicture = cursor.getString(4);
            attractionItem.attractionNotes = cursor.getString(5);
            attractionItem.infoId = Integer.parseInt(cursor.getString(6));
            attractionItem.noteId = Integer.parseInt(cursor.getString(7));
            attractionItem.galleryId = Integer.parseInt(cursor.getString(8));
            attractionItem.sygicId = Integer.parseInt(cursor.getString(9));
            
            attractionItem.origHolidayId = attractionItem.holidayId;
            attractionItem.origAttractionId = attractionItem.attractionId;
            attractionItem.origSequenceNo = attractionItem.sequenceNo;
            attractionItem.origAttractionDescription = attractionItem.attractionDescription;
            attractionItem.origAttractionPicture = attractionItem.attractionPicture;
            attractionItem.origAttractionNotes = attractionItem.attractionNotes;
            attractionItem.origInfoId = attractionItem.infoId;
            attractionItem.origNoteId = attractionItem.noteId;
            attractionItem.origGalleryId = attractionItem.galleryId;
            attractionItem.origSygicId = attractionItem.sygicId;
            
            attractionItem.pictureChanged = false;
            
            if (attractionItem.attractionPicture.length() > 0)
            {
                attractionItem.pictureAssigned = true;
                attractionItem.origPictureAssigned = true;
            } else
            {
                attractionItem.pictureAssigned = false;
                attractionItem.origPictureAssigned = false;
            }
            
        }
        catch (Exception e)
        {
            ShowError("GetAttractionItemFromQuery", e.getMessage());
        }
        
        return true;
    }
    
    boolean getNextAttractionId(int holidayId, MyInt myInt)
    {
        String lSQL = "SELECT IFNULL(MAX(attractionId),0) " +
            "FROM Attraction " +
            "WHERE holidayId = " + holidayId;
        
        if (executeSQLGetInt("getNextAttractionId", lSQL, myInt) == false)
            return (false);
        
        myInt.Value = myInt.Value + 1;
        
        return (true);
    }
    
    boolean getNextAttractionSequenceNo(int holidayId, MyInt myInt)
    {
        String lSQL = "SELECT IFNULL(MAX(SequenceNo),0) " +
            "FROM Attraction " +
            "WHERE holidayId = " + holidayId;
        
        if (executeSQLGetInt("getNextAttractionSequenceNo", lSQL, myInt) == false)
            return (false);
        
        myInt.Value = myInt.Value + 1;
        
        return (true);
    }
    
    
    boolean getAttractionList(int holidayId, ArrayList<AttractionItem> al)
    {
        String lSql = "SELECT holidayId, attractionId, sequenceNo, attractionDescription, " +
            "  attractionPicture, attractionNotes, infoId, noteId, galleryId, sygicId " +
            "FROM Attraction " +
            "WHERE holidayId = " + holidayId + " " +
            "ORDER BY SequenceNo ";
        
        Cursor cursor = executeSQLOpenCursor("getAttractionList", lSql);
        if (cursor == null)
            return (false);
        
        while (cursor.moveToNext())
        {
            AttractionItem attractionItem = new AttractionItem();
            if (GetAttractionItemFromQuery(cursor, attractionItem) == false)
                return (false);
            
            al.add(attractionItem);
        }
        return (true);
    }
    
}
