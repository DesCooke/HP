package com.example.des.hp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.des.hp.ThemeParks.ThemeParkItem;
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

    boolean addAttractionItem(ThemeParkItem themeParkItem)
    {
        try
        {
            if(!IsValid())
                return (false);


            //myMessages().LogMessage("addAttractionItem:Handling Image");
            if(themeParkItem.pictureAssigned)
            {
            /* if picture name has something in it - it means it came from internal folder */
                if(themeParkItem.attractionPicture.isEmpty())
                {
                        //myMessages().LogMessage("  - Save new image and get a filename...");
                        MyString myString=new MyString();
                        if(!savePicture(themeParkItem.holidayId, themeParkItem.fileBitmap, myString))
                            return (false);
                        themeParkItem.attractionPicture=myString.Value;
                        //myMessages().LogMessage("  - New filename " + attractionItem.attractionPicture);
                }
            }

            String lSql="INSERT INTO Attraction " + "  (holidayId, attractionId, sequenceNo, attractionDescription, " + "   attractionPicture, attractionNotes, infoId, noteId, galleryId) " + "VALUES " + "(" + themeParkItem.holidayId + "," + themeParkItem.attractionId + "," + themeParkItem.sequenceNo + ", " + MyQuotedString(themeParkItem.attractionDescription) + ", " + MyQuotedString(themeParkItem.attractionPicture) + ", " + MyQuotedString(themeParkItem.attractionNotes) + ", " + themeParkItem.infoId + ", " + themeParkItem.noteId + ", " + themeParkItem.galleryId + ")";

            return (executeSQL("addAttractionItem", lSql));
        }
        catch(Exception e)
        {
            ShowError("addAttractionItem", e.getMessage());
        }
        return (false);

    }

    boolean updateAttractionItems(ArrayList<ThemeParkItem> items)
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

    boolean updateAttractionItem(ThemeParkItem themeParkItem)
    {
        try
        {
            if(!IsValid())
                return (false);

            //myMessages().LogMessage("updateAttractionItem:Handling Image");
            if(themeParkItem.pictureChanged)
            {
                if (!themeParkItem.origPictureAssigned || themeParkItem.attractionPicture.isEmpty() || themeParkItem.attractionPicture.compareTo(themeParkItem.origAttractionPicture) != 0) {

                    if(themeParkItem.origPictureAssigned)
                    {
                        //myMessages().LogMessage("  - Original Image was assigned - need to get rid of it");
                        if(!removePicture(themeParkItem.holidayId, themeParkItem.origAttractionPicture))
                            return (false);
                    }

                /* if picture name has something in it - it means it came from internal folder */
                    if(themeParkItem.attractionPicture.isEmpty())
                    {
                        //myMessages().LogMessage("  - New Image was not from internal folder...");
                        if(themeParkItem.pictureAssigned)
                        {
                            //myMessages().LogMessage("  - Save new image and get a filename...");
                            MyString myString=new MyString();
                            if(!savePicture(themeParkItem.holidayId, themeParkItem.fileBitmap, myString))
                                return (false);
                            themeParkItem.attractionPicture=myString.Value;
                            //myMessages().LogMessage("  - New filename " + attractionItem.attractionPicture);
                        }
                    }
                }
            }


            String lSQL;
            lSQL="UPDATE Attraction " + "SET sequenceNo = " + themeParkItem.sequenceNo + ", " + "    attractionDescription = " + MyQuotedString(themeParkItem.attractionDescription) + ", " + "    attractionPicture = " + MyQuotedString(themeParkItem.attractionPicture) + ", " + "    attractionNotes = " + MyQuotedString(themeParkItem.attractionNotes) + ", " + "    infoId = " + themeParkItem.infoId + ", " + "    noteId = " + themeParkItem.noteId + ", " + "    galleryId = " + themeParkItem.galleryId + " WHERE holidayId = " + themeParkItem.holidayId + " " + "AND attractionId = " + themeParkItem.attractionId;

            return (executeSQL("updateAttractionItem", lSQL));
        }
        catch(Exception e)
        {
            ShowError("updateAttractionItem", e.getMessage());
        }
        return (false);

    }

    boolean deleteAttractionItem(ThemeParkItem themeParkItem)
    {
        try
        {
            if(!IsValid())
                return (false);

            String lSQL="DELETE FROM Attraction " + "WHERE holidayId = " + themeParkItem.holidayId + " " + "AND attractionId = " + themeParkItem.attractionId;

            if(!themeParkItem.attractionPicture.isEmpty())
                if(!removePicture(themeParkItem.holidayId, themeParkItem.attractionPicture))
                    return (false);

            return executeSQL("deleteAttractionItem", lSQL);
        }
        catch(Exception e)
        {
            ShowError("deleteAttractionItem", e.getMessage());
        }
        return (false);

    }

    boolean getAttractionItem(int holidayId, int attractionId, ThemeParkItem themeParkItem)
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
                if(!GetAttractionItemFromQuery(cursor, themeParkItem))
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

    private boolean GetAttractionItemFromQuery(Cursor cursor, ThemeParkItem themeParkItem)
    {
        if(!IsValid())
            return (false);

        try
        {
            if(cursor.getCount() == 0)
                return (false);

            themeParkItem.holidayId=Integer.parseInt(cursor.getString(0));
            themeParkItem.attractionId=Integer.parseInt(cursor.getString(1));
            themeParkItem.sequenceNo=Integer.parseInt(cursor.getString(2));
            themeParkItem.attractionDescription=cursor.getString(3);
            themeParkItem.attractionPicture=cursor.getString(4);
            themeParkItem.attractionNotes=cursor.getString(5);
            themeParkItem.infoId=Integer.parseInt(cursor.getString(6));
            themeParkItem.noteId=Integer.parseInt(cursor.getString(7));
            themeParkItem.galleryId=Integer.parseInt(cursor.getString(8));

            themeParkItem.origHolidayId= themeParkItem.holidayId;
            themeParkItem.origAttractionId= themeParkItem.attractionId;
            themeParkItem.origSequenceNo= themeParkItem.sequenceNo;
            themeParkItem.origAttractionDescription= themeParkItem.attractionDescription;
            themeParkItem.origAttractionPicture= themeParkItem.attractionPicture;
            themeParkItem.origAttractionNotes= themeParkItem.attractionNotes;
            themeParkItem.origInfoId= themeParkItem.infoId;
            themeParkItem.origNoteId= themeParkItem.noteId;
            themeParkItem.origGalleryId= themeParkItem.galleryId;

            themeParkItem.pictureChanged=false;

            if(!themeParkItem.attractionPicture.isEmpty())
            {
                themeParkItem.pictureAssigned=true;
                themeParkItem.origPictureAssigned=true;
            } else
            {
                themeParkItem.pictureAssigned=false;
                themeParkItem.origPictureAssigned=false;
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


    boolean getAttractionList(int holidayId, ArrayList<ThemeParkItem> al)
    {
        try
        {
            String lSql="SELECT holidayId, attractionId, sequenceNo, attractionDescription, " + "  attractionPicture, attractionNotes, infoId, noteId, galleryId " + "FROM Attraction " + "WHERE holidayId = " + holidayId + " " + "ORDER BY SequenceNo ";

            Cursor cursor=executeSQLOpenCursor("getAttractionList", lSql);
            if(cursor == null)
                return (false);

            while(cursor.moveToNext())
            {
                ThemeParkItem themeParkItem =new ThemeParkItem();
                if(!GetAttractionItemFromQuery(cursor, themeParkItem))
                    return (false);

                al.add(themeParkItem);
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
