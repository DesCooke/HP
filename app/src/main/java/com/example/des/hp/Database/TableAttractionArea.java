package com.example.des.hp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.des.hp.AttractionArea.AttractionAreaItem;
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

    boolean addAttractionAreaItem(AttractionAreaItem attractionAreaItem)
    {
        try
        {
            if(!IsValid())
                return (false);

            //myMessages().LogMessage("addAttractionAreaItem:Handling Image");
            if(attractionAreaItem.pictureAssigned)
            {
            /* if picture name has something in it - it means it came from internal folder */
                if(attractionAreaItem.attractionAreaPicture.isEmpty())
                {
                        //myMessages().LogMessage("  - Save new image and get a filename...");
                        MyString myString=new MyString();
                        if(!savePicture(attractionAreaItem.holidayId, attractionAreaItem.fileBitmap, myString))
                            return (false);
                        attractionAreaItem.attractionAreaPicture=myString.Value;
                        //myMessages().LogMessage("  - New filename " + attractionAreaItem.attractionAreaPicture);
                }
            }

            String lSql="INSERT INTO AttractionArea " +
                    "  (holidayId, attractionId, attractionAreaId, sequenceNo, attractionAreaDescription," +
                    "   attractionAreaPicture, attractionAreaNotes, infoId, noteId, galleryId) " +
                    "VALUES " + "(" + attractionAreaItem.holidayId + "," + attractionAreaItem.attractionId + "," + attractionAreaItem.attractionAreaId + "," + attractionAreaItem.sequenceNo + ", " + MyQuotedString(attractionAreaItem.attractionAreaDescription) + ", " + MyQuotedString(attractionAreaItem.attractionAreaPicture) + ", " + MyQuotedString(attractionAreaItem.attractionAreaNotes) + ", " + attractionAreaItem.infoId + ", " + attractionAreaItem.noteId + ", " + attractionAreaItem.galleryId + ")";

            return (executeSQL("addAttractionAreaItem", lSql));
        }
        catch(Exception e)
        {
            ShowError("addAttractionAreaItem", e.getMessage());
        }
        return (false);

    }

    boolean updateAttractionAreaItems(ArrayList<AttractionAreaItem> items)
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

    boolean updateAttractionAreaItem(AttractionAreaItem item)
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

    boolean deleteAttractionAreaItem(AttractionAreaItem attractionAreaItem)
    {
        try
        {
            if(!IsValid())
                return (false);

            String lSQL="DELETE FROM AttractionArea " + "WHERE holidayId = " + attractionAreaItem.holidayId + " " + "AND attractionId = " + attractionAreaItem.attractionId + " " + "AND attractionAreaId = " + attractionAreaItem.attractionAreaId;

            if(!attractionAreaItem.attractionAreaPicture.isEmpty())
                if(!removePicture(attractionAreaItem.holidayId, attractionAreaItem.attractionAreaPicture))
                    return (false);

            return executeSQL("deleteAttractionAreaItem", lSQL);
        }
        catch(Exception e)
        {
            ShowError("deleteAttractionAreaItem", e.getMessage());
        }
        return (false);

    }

    boolean getAttractionAreaItem(int holidayId, int attractionId, int attractionAreaId, AttractionAreaItem item)
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

    private boolean GetAttractionAreaItemFromQuery(Cursor cursor, AttractionAreaItem attractionAreaItem)
    {
        if(!IsValid())
            return (false);

        try
        {
            if(cursor.getCount() == 0)
                return (true);

            attractionAreaItem.holidayId=Integer.parseInt(cursor.getString(0));
            attractionAreaItem.attractionId=Integer.parseInt(cursor.getString(1));
            attractionAreaItem.attractionAreaId=Integer.parseInt(cursor.getString(2));
            attractionAreaItem.sequenceNo=Integer.parseInt(cursor.getString(3));
            attractionAreaItem.attractionAreaDescription=cursor.getString(4);
            attractionAreaItem.attractionAreaPicture=cursor.getString(5);
            attractionAreaItem.attractionAreaNotes=cursor.getString(6);
            attractionAreaItem.infoId=Integer.parseInt(cursor.getString(7));
            attractionAreaItem.noteId=Integer.parseInt(cursor.getString(8));
            attractionAreaItem.galleryId=Integer.parseInt(cursor.getString(9));

            attractionAreaItem.origHolidayId=attractionAreaItem.holidayId;
            attractionAreaItem.origAttractionId=attractionAreaItem.attractionId;
            attractionAreaItem.origAttractionAreaId=attractionAreaItem.attractionAreaId;
            attractionAreaItem.origSequenceNo=attractionAreaItem.sequenceNo;
            attractionAreaItem.origAttractionAreaDescription=attractionAreaItem.attractionAreaDescription;
            attractionAreaItem.origAttractionAreaPicture=attractionAreaItem.attractionAreaPicture;
            attractionAreaItem.origAttractionAreaNotes=attractionAreaItem.attractionAreaNotes;
            attractionAreaItem.origInfoId=attractionAreaItem.infoId;
            attractionAreaItem.origNoteId=attractionAreaItem.noteId;
            attractionAreaItem.origGalleryId=attractionAreaItem.galleryId;

            attractionAreaItem.pictureChanged=false;

            if(!attractionAreaItem.attractionAreaPicture.isEmpty())
            {
                attractionAreaItem.pictureAssigned=true;
                attractionAreaItem.origPictureAssigned=true;
            } else
            {
                attractionAreaItem.pictureAssigned=false;
                attractionAreaItem.origPictureAssigned=false;
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


    boolean getAttractionAreaList(int holidayId, int attractionId, ArrayList<AttractionAreaItem> al)
    {
        try
        {
            String lSql="SELECT holidayId, attractionId, attractionAreaId, sequenceNo, " + "attractionAreaDescription, attractionAreaPicture, attractionAreaNotes, infoId,  " + "noteId, galleryId " + "FROM AttractionArea " + "WHERE holidayId = " + holidayId + " " + "AND attractionId = " + attractionId + " " + "ORDER BY SequenceNo ";

            Cursor cursor=executeSQLOpenCursor("getAttractionAreaList", lSql);
            if(cursor == null)
                return (false);

            while(cursor.moveToNext())
            {
                AttractionAreaItem attractionAreaItem=new AttractionAreaItem();
                if(!GetAttractionAreaItemFromQuery(cursor, attractionAreaItem))
                    return (false);

                al.add(attractionAreaItem);
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
