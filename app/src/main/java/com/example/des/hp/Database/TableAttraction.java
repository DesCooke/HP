package com.example.des.hp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.des.hp.Attraction.AttractionItem;
import com.example.des.hp.myutils.MyInt;
import com.example.des.hp.myutils.MyString;

import java.util.ArrayList;

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
            String lSQL="CREATE TABLE IF NOT EXISTS attraction " + "( " + "  holidayId             INT(5),  " + "  attractionId          INT(5),  " + "  sequenceNo            INT(5),  " + "  attractionDescription VARCHAR, " + "  attractionPicture     VARCHAR, " + "  attractionNotes       VARCHAR, " + "  infoId                INT(5),  " + "  noteId                INT(5),  " + "  galleryId             INT(5)  " + ") ";

            db.execSQL(lSQL);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
            return (false);
        }
    }

    boolean getAttractionsCount(int holidayId, MyInt myInt)
    {
        try
        {
            if(!IsValid())
                return (false);

            String lSQL="SELECT IFNULL(COUNT(*),0) " + "FROM Attraction " + "WHERE holidayId = " + holidayId;

            return (executeSQLGetInt("getAttractionsCount", lSQL, myInt));
        }
        catch(Exception e)
        {
            ShowError("getAttractionsCount", e.getMessage());
        }
        return (false);

    }

    boolean addAttractionItem(AttractionItem attractionItem)
    {
        try
        {
            if(!IsValid())
                return (false);


            //myMessages().LogMessage("addAttractionItem:Handling Image");
            if(attractionItem.pictureAssigned)
            {
            /* if picture name has something in it - it means it came from internal folder */
                if(attractionItem.attractionPicture.isEmpty())
                {
                        //myMessages().LogMessage("  - Save new image and get a filename...");
                        MyString myString=new MyString();
                        if(!savePicture(attractionItem.holidayId, attractionItem.fileBitmap, myString))
                            return (false);
                        attractionItem.attractionPicture=myString.Value;
                        //myMessages().LogMessage("  - New filename " + attractionItem.attractionPicture);
                }
            }

            String lSql="INSERT INTO Attraction " + "  (holidayId, attractionId, sequenceNo, attractionDescription, " + "   attractionPicture, attractionNotes, infoId, noteId, galleryId) " + "VALUES " + "(" + attractionItem.holidayId + "," + attractionItem.attractionId + "," + attractionItem.sequenceNo + ", " + MyQuotedString(attractionItem.attractionDescription) + ", " + MyQuotedString(attractionItem.attractionPicture) + ", " + MyQuotedString(attractionItem.attractionNotes) + ", " + attractionItem.infoId + ", " + attractionItem.noteId + ", " + attractionItem.galleryId + ")";

            return (executeSQL("addAttractionItem", lSql));
        }
        catch(Exception e)
        {
            ShowError("addAttractionItem", e.getMessage());
        }
        return (false);

    }

    boolean updateAttractionItems(ArrayList<AttractionItem> items)
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
                    if(!updateAttractionItem(items.get(i)))
                        return (false);
                }
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("updateAttractionItems", e.getMessage());
        }
        return (false);

    }

    boolean updateAttractionItem(AttractionItem attractionItem)
    {
        try
        {
            if(!IsValid())
                return (false);

            //myMessages().LogMessage("updateAttractionItem:Handling Image");
            if(attractionItem.pictureChanged)
            {
                if (!attractionItem.origPictureAssigned || attractionItem.attractionPicture.isEmpty() || attractionItem.attractionPicture.compareTo(attractionItem.origAttractionPicture) != 0) {

                    if(attractionItem.origPictureAssigned)
                    {
                        //myMessages().LogMessage("  - Original Image was assigned - need to get rid of it");
                        if(!removePicture(attractionItem.holidayId, attractionItem.origAttractionPicture))
                            return (false);
                    }

                /* if picture name has something in it - it means it came from internal folder */
                    if(attractionItem.attractionPicture.isEmpty())
                    {
                        //myMessages().LogMessage("  - New Image was not from internal folder...");
                        if(attractionItem.pictureAssigned)
                        {
                            //myMessages().LogMessage("  - Save new image and get a filename...");
                            MyString myString=new MyString();
                            if(!savePicture(attractionItem.holidayId, attractionItem.fileBitmap, myString))
                                return (false);
                            attractionItem.attractionPicture=myString.Value;
                            //myMessages().LogMessage("  - New filename " + attractionItem.attractionPicture);
                        }
                    }
                }
            }


            String lSQL;
            lSQL="UPDATE Attraction " + "SET sequenceNo = " + attractionItem.sequenceNo + ", " + "    attractionDescription = " + MyQuotedString(attractionItem.attractionDescription) + ", " + "    attractionPicture = " + MyQuotedString(attractionItem.attractionPicture) + ", " + "    attractionNotes = " + MyQuotedString(attractionItem.attractionNotes) + ", " + "    infoId = " + attractionItem.infoId + ", " + "    noteId = " + attractionItem.noteId + ", " + "    galleryId = " + attractionItem.galleryId + " WHERE holidayId = " + attractionItem.holidayId + " " + "AND attractionId = " + attractionItem.attractionId;

            return (executeSQL("updateAttractionItem", lSQL));
        }
        catch(Exception e)
        {
            ShowError("updateAttractionItem", e.getMessage());
        }
        return (false);

    }

    boolean deleteAttractionItem(AttractionItem attractionItem)
    {
        try
        {
            if(!IsValid())
                return (false);

            String lSQL="DELETE FROM Attraction " + "WHERE holidayId = " + attractionItem.holidayId + " " + "AND attractionId = " + attractionItem.attractionId;

            if(!attractionItem.attractionPicture.isEmpty())
                if(!removePicture(attractionItem.holidayId, attractionItem.attractionPicture))
                    return (false);

            return executeSQL("deleteAttractionItem", lSQL);
        }
        catch(Exception e)
        {
            ShowError("deleteAttractionItem", e.getMessage());
        }
        return (false);

    }

    boolean getAttractionItem(int holidayId, int attractionId, AttractionItem attractionItem)
    {
        try
        {
            if(!IsValid())
                return (false);

            String lSQL;
            lSQL="SELECT holidayId, attractionId, sequenceNo, attractionDescription, " + "  attractionPicture, attractionNotes, infoId, noteId, galleryId " + "FROM attraction " + "WHERE HolidayId = " + holidayId + " " + "AND AttractionId = " + attractionId;

            Cursor cursor=executeSQLOpenCursor("getAttractionItem", lSQL);
            if(cursor != null)
            {
                cursor.moveToFirst();
                if(!GetAttractionItemFromQuery(cursor, attractionItem))
                    return (false);
            }
            executeSQLCloseCursor("getAttractionItem");
            return (true);
        }
        catch(Exception e)
        {
            ShowError("getAttractionItem", e.getMessage());
        }
        return (false);

    }

    private boolean GetAttractionItemFromQuery(Cursor cursor, AttractionItem attractionItem)
    {
        if(!IsValid())
            return (false);

        try
        {
            if(cursor.getCount() == 0)
                return (false);

            attractionItem.holidayId=Integer.parseInt(cursor.getString(0));
            attractionItem.attractionId=Integer.parseInt(cursor.getString(1));
            attractionItem.sequenceNo=Integer.parseInt(cursor.getString(2));
            attractionItem.attractionDescription=cursor.getString(3);
            attractionItem.attractionPicture=cursor.getString(4);
            attractionItem.attractionNotes=cursor.getString(5);
            attractionItem.infoId=Integer.parseInt(cursor.getString(6));
            attractionItem.noteId=Integer.parseInt(cursor.getString(7));
            attractionItem.galleryId=Integer.parseInt(cursor.getString(8));

            attractionItem.origHolidayId=attractionItem.holidayId;
            attractionItem.origAttractionId=attractionItem.attractionId;
            attractionItem.origSequenceNo=attractionItem.sequenceNo;
            attractionItem.origAttractionDescription=attractionItem.attractionDescription;
            attractionItem.origAttractionPicture=attractionItem.attractionPicture;
            attractionItem.origAttractionNotes=attractionItem.attractionNotes;
            attractionItem.origInfoId=attractionItem.infoId;
            attractionItem.origNoteId=attractionItem.noteId;
            attractionItem.origGalleryId=attractionItem.galleryId;

            attractionItem.pictureChanged=false;

            if(!attractionItem.attractionPicture.isEmpty())
            {
                attractionItem.pictureAssigned=true;
                attractionItem.origPictureAssigned=true;
            } else
            {
                attractionItem.pictureAssigned=false;
                attractionItem.origPictureAssigned=false;
            }

            return (true);

        }
        catch(Exception e)
        {
            ShowError("GetAttractionItemFromQuery", e.getMessage());
        }

        return (false);
    }

    boolean getNextAttractionId(int holidayId, MyInt myInt)
    {
        try
        {
            String lSQL="SELECT IFNULL(MAX(attractionId),0) " + "FROM Attraction " + "WHERE holidayId = " + holidayId;

            if(!executeSQLGetInt("getNextAttractionId", lSQL, myInt))
                return (false);

            myInt.Value=myInt.Value + 1;

            return (true);
        }
        catch(Exception e)
        {
            ShowError("getNextAttractionId", e.getMessage());
        }
        return (false);

    }

    boolean getNextAttractionSequenceNo(int holidayId, MyInt myInt)
    {
        try
        {
            String lSQL="SELECT IFNULL(MAX(SequenceNo),0) " + "FROM Attraction " + "WHERE holidayId = " + holidayId;

            if(!executeSQLGetInt("getNextAttractionSequenceNo", lSQL, myInt))
                return (false);

            myInt.Value=myInt.Value + 1;

            return (true);
        }
        catch(Exception e)
        {
            ShowError("getNextAttractionSequenceNo", e.getMessage());
        }
        return (false);

    }


    boolean getAttractionList(int holidayId, ArrayList<AttractionItem> al)
    {
        try
        {
            String lSql="SELECT holidayId, attractionId, sequenceNo, attractionDescription, " + "  attractionPicture, attractionNotes, infoId, noteId, galleryId " + "FROM Attraction " + "WHERE holidayId = " + holidayId + " " + "ORDER BY SequenceNo ";

            Cursor cursor=executeSQLOpenCursor("getAttractionList", lSql);
            if(cursor == null)
                return (false);

            while(cursor.moveToNext())
            {
                AttractionItem attractionItem=new AttractionItem();
                if(!GetAttractionItemFromQuery(cursor, attractionItem))
                    return (false);

                al.add(attractionItem);
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("getAttractionList", e.getMessage());
        }
        return (false);

    }

}
