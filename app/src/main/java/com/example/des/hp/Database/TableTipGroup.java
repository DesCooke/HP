package com.example.des.hp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.des.hp.TipGroup.TipGroupItem;
import com.example.des.hp.myutils.MyInt;
import com.example.des.hp.myutils.MyString;

import java.util.ArrayList;

class TableTipGroup extends TableBase
{

    TableTipGroup(Context context, SQLiteOpenHelper dbHelper)
    {
        super(context, dbHelper);
    }

    public void ShowError(String argFunction, String argMessage)
    {
        super.ShowError("TableTipGroup:" + argFunction, argMessage);
    }

    public boolean onCreate(SQLiteDatabase db)
    {
        try
        {
            String lSQL="CREATE TABLE IF NOT EXISTS tipgroup " + "( " + "  holidayId           INT(5),  " + "  tipGroupId          INT(5),  " + "  sequenceNo          INT(5),  " + "  tipGroupDescription VARCHAR, " + "  tipGroupPicture     VARCHAR, " + "  tipGroupNotes       VARCHAR, " + "  infoId              INT(5),  " + "  noteId              INT(5),  " + "  galleryId           INT(5)  " + ")";

            db.execSQL(lSQL);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
        return (false);
    }

    boolean addTipGroupItem(TipGroupItem tipGroupItem)
    {
        try
        {
            if(!IsValid())
                return (false);

            if(tipGroupItem.pictureAssigned) {
                /* if picture name has something in it - it means it came from internal folder */
                if (tipGroupItem.tipGroupPicture.isEmpty()) {
                    //myMessages().LogMessage("  - Save new image and get a filename...");
                    MyString myString = new MyString();
                    if (!savePicture(tipGroupItem.holidayId, tipGroupItem.fileBitmap, myString))
                        return (false);
                    tipGroupItem.tipGroupPicture = myString.Value;
                    //myMessages().LogMessage("  - New filename " + tipGroupItem.tipGroupPicture);
                }
            }

            String lSql="INSERT INTO TipGroup " + "  (holidayId, tipGroupId, sequenceNo, tipGroupDescription, " + "   tipGroupPicture, tipGroupNotes, infoId, noteId, galleryId) " + "VALUES " + "(" + tipGroupItem.holidayId + "," + tipGroupItem.tipGroupId + "," + tipGroupItem.sequenceNo + ", " + MyQuotedString(tipGroupItem.tipGroupDescription) + ", " + MyQuotedString(tipGroupItem.tipGroupPicture) + ", " + MyQuotedString(tipGroupItem.tipGroupNotes) + ", " + tipGroupItem.infoId + ", " + tipGroupItem.noteId + ", " + tipGroupItem.galleryId + ")";

            return (executeSQL("addTipGroupItem", lSql));

        }
        catch(Exception e)
        {
            ShowError("addTipGroupItem", e.getMessage());
        }
        return (false);

    }

    boolean updateTipGroupItems(ArrayList<TipGroupItem> items)
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
                    if(!updateTipGroupItem(items.get(i)))
                        return (false);
                }
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("updateTipGroupItems", e.getMessage());
        }
        return (false);
    }

    boolean updateTipGroupItem(TipGroupItem tipGroupItem)
    {
        try
        {
            if(!IsValid())
                return (false);

            //myMessages().LogMessage("updateTipGroupItem:Handling Image");
            if(tipGroupItem.pictureChanged)
            {
                if (!tipGroupItem.origPictureAssigned || tipGroupItem.tipGroupPicture.isEmpty() || tipGroupItem.tipGroupPicture.compareTo(tipGroupItem.origTipGroupPicture) != 0) {
                    if(tipGroupItem.origPictureAssigned)
                    {
                        //myMessages().LogMessage("  - Original Image was assigned - need to get rid of it");
                        if(!removePicture(tipGroupItem.holidayId, tipGroupItem.origTipGroupPicture))
                            return (false);
                    }

                /* if picture name has something in it - it means it came from internal folder */
                    if(tipGroupItem.tipGroupPicture.isEmpty())
                    {
                        //myMessages().LogMessage("  - New Image was not from internal folder...");
                        if(tipGroupItem.pictureAssigned)
                        {
                            //myMessages().LogMessage("  - Save new image and get a filename...");
                            MyString myString=new MyString();
                            if(!savePicture(tipGroupItem.holidayId, tipGroupItem.fileBitmap, myString))
                                return (false);
                            tipGroupItem.tipGroupPicture=myString.Value;
                            //myMessages().LogMessage("  - New filename " + tipGroupItem.tipGroupPicture);
                        }
                    }
                }
            }

            String lSQL;
            lSQL="UPDATE TipGroup " + "SET sequenceNo = " + tipGroupItem.sequenceNo + ", " + "    tipGroupDescription = " + MyQuotedString(tipGroupItem.tipGroupDescription) + ", " + "    tipGroupPicture = " + MyQuotedString(tipGroupItem.tipGroupPicture) + ", " + "    tipGroupNotes = " + MyQuotedString(tipGroupItem.tipGroupNotes) + ", " + "    infoId = " + tipGroupItem.infoId + ", " + "    noteId = " + tipGroupItem.noteId + ", " + "    galleryId = " + tipGroupItem.galleryId + ", " + "WHERE holidayId = " + tipGroupItem.holidayId + " " + "AND tipGroupId = " + tipGroupItem.tipGroupId;

            return (executeSQL("updateTipGroupItem", lSQL));
        }
        catch(Exception e)
        {
            ShowError("updateTipGroupItem", e.getMessage());
        }
        return (false);

    }

    boolean deleteTipGroupItem(TipGroupItem tipGroupItem)
    {
        try
        {
            if(!IsValid())
                return (false);

            String lSQL="DELETE FROM TipGroup " + "WHERE holidayId = " + tipGroupItem.holidayId + " " + "AND tipGroupId = " + tipGroupItem.tipGroupId;

            if(!tipGroupItem.tipGroupPicture.isEmpty())
                if(!removePicture(tipGroupItem.holidayId, tipGroupItem.tipGroupPicture))
                    return (false);

            if(!executeSQL("deleteTipGroupItem", lSQL))
                return (false);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("deleteTipGroupItem", e.getMessage());
        }
        return (false);
    }

    boolean getTipGroupItem(int holidayId, int tipGroupId, TipGroupItem item)
    {
        try
        {
            if(!IsValid())
                return (false);

            String lSQL;
            lSQL="SELECT holidayId, tipGroupId, sequenceNo, tipGroupDescription, " + "  tipGroupPicture, tipGroupNotes, infoId, noteId, galleryId " + "FROM tipGroup " + "WHERE HolidayId = " + holidayId + " " + "AND TipGroupId = " + tipGroupId;

            Cursor cursor=executeSQLOpenCursor("getTipGroupItem", lSQL);
            if(cursor != null)
            {
                cursor.moveToFirst();
                if(!GetTipGroupItemFromQuery(cursor, item))
                    return (false);
            }
            executeSQLCloseCursor("getTipGroupItem");
            return (true);
        }
        catch(Exception e)
        {
            ShowError("getTipGroupItem", e.getMessage());
        }
        return (false);
    }

    private boolean GetTipGroupItemFromQuery(Cursor cursor, TipGroupItem tipGroupItem)
    {
        if(!IsValid())
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

            tipGroupItem.origHolidayId=tipGroupItem.holidayId;
            tipGroupItem.origTipGroupId=tipGroupItem.tipGroupId;
            tipGroupItem.origSequenceNo=tipGroupItem.sequenceNo;
            tipGroupItem.origTipGroupDescription=tipGroupItem.tipGroupDescription;
            tipGroupItem.origTipGroupPicture=tipGroupItem.tipGroupPicture;
            tipGroupItem.origTipGroupNotes=tipGroupItem.tipGroupNotes;
            tipGroupItem.origInfoId=tipGroupItem.infoId;
            tipGroupItem.origNoteId=tipGroupItem.noteId;
            tipGroupItem.origGalleryId=tipGroupItem.galleryId;

            tipGroupItem.pictureChanged=false;

            if(!tipGroupItem.tipGroupPicture.isEmpty())
            {
                tipGroupItem.pictureAssigned=true;
                tipGroupItem.origPictureAssigned=true;
            } else
            {
                tipGroupItem.pictureAssigned=false;
                tipGroupItem.origPictureAssigned=false;
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("GetTipGroupItemFromQuery", e.getMessage());
        }

        return (false);
    }

    boolean getNextTipGroupId(int holidayId, MyInt retInt)
    {
        try
        {
            String lSQL="SELECT IFNULL(MAX(tipGroupId),0) " + "FROM TipGroup " + "WHERE holidayId = " + holidayId;

            if(!executeSQLGetInt("getNextTipGroupId", lSQL, retInt))
                return (false);
            retInt.Value=retInt.Value + 1;
            return (true);
        }
        catch(Exception e)
        {
            ShowError("getNextTipGroupId", e.getMessage());
        }
        return (false);
    }

    boolean getNextTipGroupSequenceNo(int holidayId, MyInt retInt)
    {
        try
        {
            String lSQL="SELECT IFNULL(MAX(SequenceNo),0) " + "FROM TipGroup " + "WHERE holidayId = " + holidayId;

            if(!executeSQLGetInt("getNextTipGroupSequenceNo", lSQL, retInt))
                return (false);
            retInt.Value=retInt.Value + 1;
            return (true);
        }
        catch(Exception e)
        {
            ShowError("getNextTipGroupSequenceNo", e.getMessage());
        }
        return (false);
    }


    boolean getTipGroupList(int holidayId, ArrayList<TipGroupItem> al)
    {
        try
        {
            String lSql="SELECT holidayId, tipGroupId, sequenceNo, tipGroupDescription, " + "  tipGroupPicture, tipGroupNotes, infoId, noteId, galleryId  " + "FROM TipGroup " + "WHERE holidayId = " + holidayId + " " + "ORDER BY SequenceNo ";

            Cursor cursor=executeSQLOpenCursor("getTipGroupList", lSql);
            if(cursor == null)
                return (false);

            while(cursor.moveToNext())
            {
                TipGroupItem tipGroupItem=new TipGroupItem();
                if(!GetTipGroupItemFromQuery(cursor, tipGroupItem))
                    return (false);

                al.add(tipGroupItem);
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("getTipGroupList", e.getMessage());
        }
        return (false);
    }

}
