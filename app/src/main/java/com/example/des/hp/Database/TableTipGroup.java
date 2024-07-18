package com.example.des.hp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.des.hp.TipGroup.TipGroupItem;
import com.example.des.hp.myutils.DateUtils;
import com.example.des.hp.myutils.MyInt;
import com.example.des.hp.myutils.MyString;

import java.util.ArrayList;
import java.util.Random;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
import static com.example.des.hp.myutils.MyMessages.myMessages;

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

    public boolean onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        try
        {
            return (true);
        }
        catch(Exception e)
        {
            ShowError("onUpgrade", e.getMessage());
        }
        return (false);
    }

    boolean addTipGroupItem(TipGroupItem tipGroupItem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            if(tipGroupItem.pictureAssigned)
            {
            /* if picture name has something in it - it means it came from internal folder */
                if(tipGroupItem.tipGroupPicture.length() == 0)
                {
                    //myMessages().LogMessage("  - New Image was not from internal folder...");
                    if(tipGroupItem.pictureAssigned)
                    {
                        //myMessages().LogMessage("  - Save new image and get a filename...");
                        MyString myString=new MyString();
                        if(savePicture(tipGroupItem.holidayId, tipGroupItem.fileBitmap, myString) == false)
                            return (false);
                        tipGroupItem.tipGroupPicture=myString.Value;
                        //myMessages().LogMessage("  - New filename " + tipGroupItem.tipGroupPicture);
                    } else
                    {
                        //myMessages().LogMessage("  - New Image not setup - so - keep it blank");
                    }
                } else
                {
                    //myMessages().LogMessage("  - New Image was from internal folder - so just use it (" + tipGroupItem.tipGroupPicture + ")");
                }
            } else
            {
                //myMessages().LogMessage("  - New Image not assigned - do nothing");
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
            if(IsValid() == false)
                return (false);

            //myMessages().LogMessage("updateTipGroupItem:Handling Image");
            if(tipGroupItem.pictureChanged)
            {
                if(tipGroupItem.origPictureAssigned && tipGroupItem.tipGroupPicture.length() > 0 && tipGroupItem.tipGroupPicture.compareTo(tipGroupItem.origTipGroupPicture) == 0)
                {
                    //myMessages().LogMessage("  - Original Image changed back to the original - do nothing");
                } else
                {
                    if(tipGroupItem.origPictureAssigned)
                    {
                        //myMessages().LogMessage("  - Original Image was assigned - need to get rid of it");
                        if(removePicture(tipGroupItem.holidayId, tipGroupItem.origTipGroupPicture) == false)
                            return (false);
                    }
            
                /* if picture name has something in it - it means it came from internal folder */
                    if(tipGroupItem.tipGroupPicture.length() == 0)
                    {
                        //myMessages().LogMessage("  - New Image was not from internal folder...");
                        if(tipGroupItem.pictureAssigned)
                        {
                            //myMessages().LogMessage("  - Save new image and get a filename...");
                            MyString myString=new MyString();
                            if(savePicture(tipGroupItem.holidayId, tipGroupItem.fileBitmap, myString) == false)
                                return (false);
                            tipGroupItem.tipGroupPicture=myString.Value;
                            //myMessages().LogMessage("  - New filename " + tipGroupItem.tipGroupPicture);
                        } else
                        {
                            //myMessages().LogMessage("  - New Image not setup - so - keep it blank");
                        }
                    } else
                    {
                        //myMessages().LogMessage("  - New Image was from internal folder - so just use it (" + tipGroupItem.tipGroupPicture + ")");
                    }
                }
            } else
            {
                //myMessages().LogMessage("  - Image not changed - do nothing");
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
            if(IsValid() == false)
                return (false);

            String lSQL="DELETE FROM TipGroup " + "WHERE holidayId = " + tipGroupItem.holidayId + " " + "AND tipGroupId = " + tipGroupItem.tipGroupId;

            if(tipGroupItem.tipGroupPicture.length() > 0)
                if(removePicture(tipGroupItem.holidayId, tipGroupItem.tipGroupPicture) == false)
                    return (false);

            if(executeSQL("deleteTipGroupItem", lSQL) == false)
                return (false);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("deleteTipGroupItem", e.getMessage());
        }
        return (false);
    }

    boolean getTipGroupItem(int holidayId, int tipGroupId, TipGroupItem litem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            String lSQL;
            lSQL="SELECT holidayId, tipGroupId, sequenceNo, tipGroupDescription, " + "  tipGroupPicture, tipGroupNotes, infoId, noteId, galleryId " + "FROM tipGroup " + "WHERE HolidayId = " + holidayId + " " + "AND TipGroupId = " + tipGroupId;

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
        catch(Exception e)
        {
            ShowError("getTipGroupItem", e.getMessage());
        }
        return (false);
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

            if(tipGroupItem.tipGroupPicture.length() > 0)
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

            if(executeSQLGetInt("getNextTipGroupId", lSQL, retInt) == false)
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

    public boolean getTipGroupCount(int holidayId, MyInt retInt)
    {
        try
        {
            String lSQL="SELECT IFNULL(COUNT(*),0) " + "FROM TipGroup " + "WHERE holidayId = " + holidayId;

            if(executeSQLGetInt("getTipGroupCount", lSQL, retInt) == false)
                return (false);
            return (true);
        }
        catch(Exception e)
        {
            ShowError("getTipGroupCount", e.getMessage());
        }
        return (false);
    }

    boolean getNextTipGroupSequenceNo(int holidayId, MyInt retInt)
    {
        try
        {
            String lSQL="SELECT IFNULL(MAX(SequenceNo),0) " + "FROM TipGroup " + "WHERE holidayId = " + holidayId;

            if(executeSQLGetInt("getNextTipGroupSequenceNo", lSQL, retInt) == false)
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
                if(GetTipGroupItemFromQuery(cursor, tipGroupItem) == false)
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

    boolean clearNote(int holidayId, int noteId)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            String l_SQL="UPDATE tipGroup SET noteId = 0 " + "WHERE holidayId = " + holidayId + " " + "AND noteId = " + noteId;

            if(executeSQL("clearNote", l_SQL) == false)
                return (false);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("clearNote", e.getMessage());
        }
        return (false);
    }

}
