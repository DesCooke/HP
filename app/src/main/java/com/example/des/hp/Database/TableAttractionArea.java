package com.example.des.hp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.des.hp.ThemeParks.ThemeParkAreaItem;
import com.example.des.hp.myutils.MyInt;
import com.example.des.hp.myutils.MyString;

import java.util.ArrayList;

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
            String lSQL="CREATE TABLE IF NOT EXISTS attractionarea " + "( " + "  holidayId                 INT(5),  " + "  attractionId              INT(5),  " + "  attractionAreaId          INT(5),  " + "  sequenceNo                INT(5),  " + "  attractionAreaDescription VARCHAR, " + "  attractionAreaPicture     VARCHAR, " + "  attractionAreaNotes       VARCHAR, " + "  infoId                    INT(5),  " + "  noteId                    INT(5),  " + "  galleryId                 INT(5)  " + ") ";

            db.execSQL(lSQL);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
        return (false);
    }

    boolean addAttractionAreaItem(ThemeParkAreaItem themeParkAreaItem)
    {
        try
        {
            if(!IsValid())
                return (false);

            //myMessages().LogMessage("addAttractionAreaItem:Handling Image");
            if(themeParkAreaItem.pictureAssigned)
            {
            /* if picture name has something in it - it means it came from internal folder */
                if(themeParkAreaItem.attractionAreaPicture.isEmpty())
                {
                        //myMessages().LogMessage("  - Save new image and get a filename...");
                        MyString myString=new MyString();
                        if(!savePicture(themeParkAreaItem.holidayId, themeParkAreaItem.fileBitmap, myString))
                            return (false);
                        themeParkAreaItem.attractionAreaPicture=myString.Value;
                        //myMessages().LogMessage("  - New filename " + attractionAreaItem.attractionAreaPicture);
                }
            }

            String lSql="INSERT INTO AttractionArea " +
                    "  (holidayId, attractionId, attractionAreaId, sequenceNo, attractionAreaDescription," +
                    "   attractionAreaPicture, attractionAreaNotes, infoId, noteId, galleryId) " +
                    "VALUES " + "(" + themeParkAreaItem.holidayId + "," + themeParkAreaItem.attractionId + "," + themeParkAreaItem.attractionAreaId + "," + themeParkAreaItem.sequenceNo + ", " + MyQuotedString(themeParkAreaItem.attractionAreaDescription) + ", " + MyQuotedString(themeParkAreaItem.attractionAreaPicture) + ", " + MyQuotedString(themeParkAreaItem.attractionAreaNotes) + ", " + themeParkAreaItem.infoId + ", " + themeParkAreaItem.noteId + ", " + themeParkAreaItem.galleryId + ")";

            return (executeSQL("addAttractionAreaItem", lSql));
        }
        catch(Exception e)
        {
            ShowError("addAttractionAreaItem", e.getMessage());
        }
        return (false);

    }

    boolean updateAttractionAreaItems(ArrayList<ThemeParkAreaItem> items)
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
                    if(!updateAttractionAreaItem(items.get(i)))
                        return (false);
                }
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("updateAttractionAreaItems", e.getMessage());
        }
        return (false);

    }

    boolean updateAttractionAreaItem(ThemeParkAreaItem item)
    {
        try
        {
            if(!IsValid())
                return (false);

            //myMessages().LogMessage("updateAttractionAreaItem:Handling Image");
            if(item.pictureChanged)
            {
                if (!item.origPictureAssigned || item.attractionAreaPicture.isEmpty() || item.attractionAreaPicture.compareTo(item.origAttractionAreaPicture) != 0) {

                    if(item.origPictureAssigned)
                    {
                        //myMessages().LogMessage("  - Original Image was assigned - need to get rid of it");
                        if(!removePicture(item.holidayId, item.origAttractionAreaPicture))
                            return (false);
                    }

                /* if picture name has something in it - it means it came from internal folder */
                    if(item.attractionAreaPicture.isEmpty())
                    {
                        //myMessages().LogMessage("  - New Image was not from internal folder...");
                        if(item.pictureAssigned)
                        {
                            //myMessages().LogMessage("  - Save new image and get a filename...");
                            MyString myString=new MyString();
                            if(!savePicture(item.holidayId, item.fileBitmap, myString))
                                return (false);
                            item.attractionAreaPicture=myString.Value;
                            //myMessages().LogMessage("  - New filename " + item.attractionAreaPicture);
                        }
                    }
                }
            }


            String lSQL;
            lSQL="UPDATE AttractionArea " + "SET sequenceNo = " + item.sequenceNo + ", " + "    attractionAreaDescription = " + MyQuotedString(item.attractionAreaDescription) + ", " + "    attractionAreaPicture = " + MyQuotedString(item.attractionAreaPicture) + ", " + "    attractionAreaNotes = " + MyQuotedString(item.attractionAreaNotes) + ", " + "    infoId = " + item.infoId + ", " + "    noteId = " + item.noteId + ", " + "    galleryId = " + item.galleryId + " WHERE holidayId = " + item.holidayId + " " + "AND attractionId = " + item.attractionId + " " + "AND attractionAreaId = " + item.attractionAreaId;

            return (executeSQL("updateAttractionAreaItem", lSQL));
        }
        catch(Exception e)
        {
            ShowError("updateAttractionAreaItem", e.getMessage());
        }
        return (false);

    }

    boolean deleteAttractionAreaItem(ThemeParkAreaItem themeParkAreaItem)
    {
        try
        {
            if(!IsValid())
                return (false);

            String lSQL="DELETE FROM AttractionArea " + "WHERE holidayId = " + themeParkAreaItem.holidayId + " " + "AND attractionId = " + themeParkAreaItem.attractionId + " " + "AND attractionAreaId = " + themeParkAreaItem.attractionAreaId;

            if(!themeParkAreaItem.attractionAreaPicture.isEmpty())
                if(!removePicture(themeParkAreaItem.holidayId, themeParkAreaItem.attractionAreaPicture))
                    return (false);

            return executeSQL("deleteAttractionAreaItem", lSQL);
        }
        catch(Exception e)
        {
            ShowError("deleteAttractionAreaItem", e.getMessage());
        }
        return (false);

    }

    boolean getAttractionAreaItem(int holidayId, int attractionId, int attractionAreaId, ThemeParkAreaItem item)
    {
        try
        {
            if(!IsValid())
                return (false);

            String lSQL;
            lSQL="SELECT holidayId, attractionId, attractionAreaId, sequenceNo, " + "attractionAreaDescription, attractionAreaPicture, attractionAreaNotes, infoId, " + "noteId, galleryId " + "FROM AttractionArea " + "WHERE HolidayId = " + holidayId + " " + "AND AttractionId = " + attractionId + " " + "and AttractionAreaId = " + attractionAreaId;

            Cursor cursor=executeSQLOpenCursor("getAttractionAreaItem", lSQL);
            if(cursor != null)
            {
                cursor.moveToFirst();
                if(!GetAttractionAreaItemFromQuery(cursor, item))
                    return (false);
            }
            executeSQLCloseCursor("getAttractionAreaItem");
            return (true);
        }
        catch(Exception e)
        {
            ShowError("getAttractionAreaItem", e.getMessage());
        }
        return (false);

    }

    private boolean GetAttractionAreaItemFromQuery(Cursor cursor, ThemeParkAreaItem themeParkAreaItem)
    {
        if(!IsValid())
            return (false);

        try
        {
            if(cursor.getCount() == 0)
                return (true);

            themeParkAreaItem.holidayId=Integer.parseInt(cursor.getString(0));
            themeParkAreaItem.attractionId=Integer.parseInt(cursor.getString(1));
            themeParkAreaItem.attractionAreaId=Integer.parseInt(cursor.getString(2));
            themeParkAreaItem.sequenceNo=Integer.parseInt(cursor.getString(3));
            themeParkAreaItem.attractionAreaDescription=cursor.getString(4);
            themeParkAreaItem.attractionAreaPicture=cursor.getString(5);
            themeParkAreaItem.attractionAreaNotes=cursor.getString(6);
            themeParkAreaItem.infoId=Integer.parseInt(cursor.getString(7));
            themeParkAreaItem.noteId=Integer.parseInt(cursor.getString(8));
            themeParkAreaItem.galleryId=Integer.parseInt(cursor.getString(9));

            themeParkAreaItem.origHolidayId= themeParkAreaItem.holidayId;
            themeParkAreaItem.origAttractionId= themeParkAreaItem.attractionId;
            themeParkAreaItem.origAttractionAreaId= themeParkAreaItem.attractionAreaId;
            themeParkAreaItem.origSequenceNo= themeParkAreaItem.sequenceNo;
            themeParkAreaItem.origAttractionAreaDescription= themeParkAreaItem.attractionAreaDescription;
            themeParkAreaItem.origAttractionAreaPicture= themeParkAreaItem.attractionAreaPicture;
            themeParkAreaItem.origAttractionAreaNotes= themeParkAreaItem.attractionAreaNotes;
            themeParkAreaItem.origInfoId= themeParkAreaItem.infoId;
            themeParkAreaItem.origNoteId= themeParkAreaItem.noteId;
            themeParkAreaItem.origGalleryId= themeParkAreaItem.galleryId;

            themeParkAreaItem.pictureChanged=false;

            if(!themeParkAreaItem.attractionAreaPicture.isEmpty())
            {
                themeParkAreaItem.pictureAssigned=true;
                themeParkAreaItem.origPictureAssigned=true;
            } else
            {
                themeParkAreaItem.pictureAssigned=false;
                themeParkAreaItem.origPictureAssigned=false;
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("GetAttractionAreaItemFromQuery", e.getMessage());
        }

        return (false);
    }

    boolean getNextAttractionAreaId(int holidayId, int attractionId, MyInt myInt)
    {
        try
        {
            String lSQL="SELECT IFNULL(MAX(attractionAreaId),0) " + "FROM AttractionArea " + "WHERE holidayId = " + holidayId + " " + "AND attractionId = " + attractionId;

            if(!executeSQLGetInt("getNextAttractionAreaId", lSQL, myInt))
                return (false);

            myInt.Value=myInt.Value + 1;

            return (true);
        }
        catch(Exception e)
        {
            ShowError("getNextAttractionAreaId", e.getMessage());
        }
        return (false);

    }

    boolean getNextAttractionAreaSequenceNo(int holidayId, int attractionId, MyInt myInt)
    {
        try
        {
            String lSQL="SELECT IFNULL(MAX(SequenceNo),0) " + "FROM AttractionArea " + "WHERE holidayId = " + holidayId + " " + "AND attractionId = " + attractionId;

            if(!executeSQLGetInt("getNextAttractionAreaSequenceNo", lSQL, myInt))
                return (false);

            myInt.Value=myInt.Value + 1;

            return (true);
        }
        catch(Exception e)
        {
            ShowError("getNextAttractionAreaSequenceNo", e.getMessage());
        }
        return (false);

    }


    boolean getAttractionAreaList(int holidayId, int attractionId, ArrayList<ThemeParkAreaItem> al)
    {
        try
        {
            String lSql="SELECT holidayId, attractionId, attractionAreaId, sequenceNo, " + "attractionAreaDescription, attractionAreaPicture, attractionAreaNotes, infoId,  " + "noteId, galleryId " + "FROM AttractionArea " + "WHERE holidayId = " + holidayId + " " + "AND attractionId = " + attractionId + " " + "ORDER BY SequenceNo ";

            Cursor cursor=executeSQLOpenCursor("getAttractionAreaList", lSql);
            if(cursor == null)
                return (false);

            while(cursor.moveToNext())
            {
                ThemeParkAreaItem themeParkAreaItem =new ThemeParkAreaItem();
                if(!GetAttractionAreaItemFromQuery(cursor, themeParkAreaItem))
                    return (false);

                al.add(themeParkAreaItem);
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("getAttractionAreaList", e.getMessage());
        }
        return (false);

    }

}
