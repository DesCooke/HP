package com.example.des.hp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.des.hp.Tip.TipItem;
import com.example.des.hp.myutils.DateUtils;
import com.example.des.hp.myutils.MyInt;
import com.example.des.hp.myutils.MyString;

import java.util.ArrayList;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
import static com.example.des.hp.myutils.MyMessages.myMessages;

class TableTip extends TableBase
{

    TableTip(Context context, SQLiteOpenHelper dbHelper)
    {
        super(context, dbHelper);
        DateUtils dateUtils=new DateUtils(context);
    }

    public void ShowError(String argFunction, String argMessage)
    {
        super.ShowError("TableTip:" + argFunction, argMessage);
    }

    public boolean onCreate(SQLiteDatabase db)
    {
        try
        {
            String lSQL="CREATE TABLE IF NOT EXISTS tip " + "( " + "  holidayId           INT(5),  " + "  tipGroupId          INT(5),  " + "  tipId               INT(5),  " + "  sequenceNo          INT(5),  " + "  tipDescription      VARCHAR, " + "  tipPicture          VARCHAR, " + "  tipNotes            VARCHAR, " + "  infoId              INT(5),  " + "  noteId              INT(5),  " + "  galleryId           INT(5)  " + ") ";

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

    boolean addTipItem(TipItem tipItem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            if(tipItem.pictureAssigned)
            {
            /* if picture name has something in it - it means it came from internal folder */
                if(tipItem.tipPicture.length() == 0)
                {
                    //myMessages().LogMessage("  - New Image was not from internal folder...");
                    if(tipItem.pictureAssigned)
                    {
                        //myMessages().LogMessage("  - Save new image and get a filename...");
                        MyString myString=new MyString();
                        if(savePicture(tipItem.holidayId, tipItem.fileBitmap, myString) == false)
                            return (false);
                        tipItem.tipPicture=myString.Value;
                        //myMessages().LogMessage("  - New filename " + tipItem.tipPicture);
                    } else
                    {
                        //myMessages().LogMessage("  - New Image not setup - so - keep it blank");
                    }
                } else
                {
                    //myMessages().LogMessage("  - New Image was from internal folder - so just use it (" + tipItem.tipPicture + ")");
                }
            } else
            {
                //myMessages().LogMessage("  - New Image not assigned - do nothing");
            }

            String lSql="INSERT INTO Tip " + "  (holidayId, tipGroupId, tipId, sequenceNo, tipDescription, " + "   tipPicture, tipNotes, infoId, noteId, galleryId) " + "VALUES " + "(" + tipItem.holidayId + "," + tipItem.tipGroupId + "," + tipItem.tipId + "," + tipItem.sequenceNo + ", " + MyQuotedString(tipItem.tipDescription) + ", " + MyQuotedString(tipItem.tipPicture) + ", " + MyQuotedString(tipItem.tipNotes) + ", " + tipItem.infoId + ", " + tipItem.noteId + ", " + tipItem.galleryId + ")";

            return (executeSQL("addTipItem", lSql));

        }
        catch(Exception e)
        {
            ShowError("addTipItem", e.getMessage());
        }
        return (false);

    }

    boolean updateTipItems(ArrayList<TipItem> items)
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
                    if(updateTipItem(items.get(i)) == false)
                        return (false);
                }
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("updateTipItems", e.getMessage());
        }
        return (false);
    }

    boolean updateTipItem(TipItem tipItem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            //myMessages().LogMessage("updateTipItem:Handling Image");
            if(tipItem.pictureChanged)
            {
                if(tipItem.origPictureAssigned && tipItem.tipPicture.length() > 0 && tipItem.tipPicture.compareTo(tipItem.origTipPicture) == 0)
                {
                    //myMessages().LogMessage("  - Original Image changed back to the original - do nothing");
                } else
                {
                    if(tipItem.origPictureAssigned)
                    {
                        //myMessages().LogMessage("  - Original Image was assigned - need to get rid of it");
                        if(removePicture(tipItem.holidayId, tipItem.origTipPicture) == false)
                            return (false);
                    }
            
                /* if picture name has something in it - it means it came from internal folder */
                    if(tipItem.tipPicture.length() == 0)
                    {
                        //myMessages().LogMessage("  - New Image was not from internal folder...");
                        if(tipItem.pictureAssigned)
                        {
                            //myMessages().LogMessage("  - Save new image and get a filename...");
                            MyString myString=new MyString();
                            if(savePicture(tipItem.holidayId, tipItem.fileBitmap, myString) == false)
                                return (false);
                            tipItem.tipPicture=myString.Value;
                            //myMessages().LogMessage("  - New filename " + tipItem.tipPicture);
                        } else
                        {
                            //myMessages().LogMessage("  - New Image not setup - so - keep it blank");
                        }
                    } else
                    {
                        //myMessages().LogMessage("  - New Image was from internal folder - so just use it (" + tipItem.tipPicture + ")");
                    }
                }
            } else
            {
                //myMessages().LogMessage("  - Image not changed - do nothing");
            }


            String lSQL;
            lSQL="UPDATE Tip " + "SET sequenceNo = " + tipItem.sequenceNo + ", " + "    tipDescription = " + MyQuotedString(tipItem.tipDescription) + ", " + "    tipPicture = " + MyQuotedString(tipItem.tipPicture) + ", " + "    tipNotes = " + MyQuotedString(tipItem.tipNotes) + ", " + "    infoId = " + tipItem.infoId + ", " + "    noteId = " + tipItem.noteId + ", " + "    galleryId = " + tipItem.galleryId + ", " + "WHERE holidayId = " + tipItem.holidayId + " " + "AND tipGroupId = " + tipItem.tipGroupId + " " + "AND tipId = " + tipItem.tipId;

            return (executeSQL("updateTipItem", lSQL));
        }
        catch(Exception e)
        {
            ShowError("updateTipItem", e.getMessage());
        }
        return (false);

    }

    boolean deleteTipItem(TipItem tipItem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            String lSQL="DELETE FROM Tip " + "WHERE holidayId = " + tipItem.holidayId + " " + "AND tipGroupId = " + tipItem.tipGroupId + " " + "AND tipId = " + tipItem.tipId;

            if(tipItem.tipPicture.length() > 0)
                if(removePicture(tipItem.holidayId, tipItem.tipPicture) == false)
                    return (false);

            if(executeSQL("deleteTipItem", lSQL) == false)
                return (false);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("deleteTipItem", e.getMessage());
        }
        return (false);
    }

    boolean getTipItem(int holidayId, int tipGroupId, int tipId, TipItem litem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            String lSQL;
            lSQL="SELECT holidayId, tipGroupId, tipId, sequenceNo, tipDescription, " + "  tipPicture, tipNotes, infoId, noteId, galleryId " + "FROM tip " + "WHERE HolidayId = " + holidayId + " " + "AND TipGroupId = " + tipGroupId + " " + "and TipId = " + tipId;

            Cursor cursor=executeSQLOpenCursor("getTipItem", lSQL);
            if(cursor != null)
            {
                cursor.moveToFirst();
                if(GetTipItemFromQuery(cursor, litem) == false)
                    return (false);
            }
            executeSQLCloseCursor("getTipItem");
            return (true);
        }
        catch(Exception e)
        {
            ShowError("getTipItem", e.getMessage());
        }
        return (false);
    }

    private boolean GetTipItemFromQuery(Cursor cursor, TipItem tipItem)
    {
        if(IsValid() == false)
            return (false);

        try
        {
            if(cursor.getCount() == 0)
                return (true);

            tipItem.holidayId=Integer.parseInt(cursor.getString(0));
            tipItem.tipGroupId=Integer.parseInt(cursor.getString(1));
            tipItem.tipId=Integer.parseInt(cursor.getString(2));
            tipItem.sequenceNo=Integer.parseInt(cursor.getString(3));
            tipItem.tipDescription=cursor.getString(4);
            tipItem.tipPicture=cursor.getString(5);
            tipItem.tipNotes=cursor.getString(6);
            tipItem.infoId=Integer.parseInt(cursor.getString(7));
            tipItem.noteId=Integer.parseInt(cursor.getString(8));
            tipItem.galleryId=Integer.parseInt(cursor.getString(9));

            tipItem.origHolidayId=tipItem.holidayId;
            tipItem.origTipGroupId=tipItem.tipGroupId;
            tipItem.origTipId=tipItem.tipId;
            tipItem.origSequenceNo=tipItem.sequenceNo;
            tipItem.origTipDescription=tipItem.tipDescription;
            tipItem.origTipPicture=tipItem.tipPicture;
            tipItem.origTipNotes=tipItem.tipNotes;
            tipItem.origInfoId=tipItem.infoId;
            tipItem.origNoteId=tipItem.noteId;
            tipItem.origGalleryId=tipItem.noteId;

            tipItem.pictureChanged=false;

            if(tipItem.tipPicture.length() > 0)
            {
                tipItem.pictureAssigned=true;
                tipItem.origPictureAssigned=true;
            } else
            {
                tipItem.pictureAssigned=false;
                tipItem.origPictureAssigned=false;
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("GetTipItemFromQuery", e.getMessage());
        }

        return (false);
    }

    boolean getNextTipId(int holidayId, int tidGroupId, MyInt retInt)
    {
        try
        {
            String lSQL="SELECT IFNULL(MAX(tipId),0) " + "FROM Tip " + "WHERE holidayId = " + holidayId + " " + "AND tipGroupId = " + tidGroupId;

            if(executeSQLGetInt("getNextTipId", lSQL, retInt) == false)
                return (false);
            retInt.Value=retInt.Value + 1;
            return (true);
        }
        catch(Exception e)
        {
            ShowError("getNextTipId", e.getMessage());
        }
        return (false);
    }

    boolean getTipsCount(int holidayId, MyInt retInt)
    {
        try
        {
            String lSQL="SELECT IFNULL(COUNT(*),0) " + "FROM TipGroup " + "WHERE holidayId = " + holidayId;

            if(executeSQLGetInt("getTipsCount", lSQL, retInt) == false)
                return (false);
            return (true);
        }
        catch(Exception e)
        {
            ShowError("getTipsCount", e.getMessage());
        }
        return (false);
    }

    boolean getNextTipSequenceNo(int holidayId, int tipGroupId, MyInt retInt)
    {
        try
        {
            String lSQL="SELECT IFNULL(MAX(SequenceNo),0) " + "FROM Tip " + "WHERE holidayId = " + holidayId + " " + "AND tipGroupId = " + tipGroupId;

            if(executeSQLGetInt("getNextTipSequenceNo", lSQL, retInt) == false)
                return (false);
            retInt.Value=retInt.Value + 1;
            return (true);
        }
        catch(Exception e)
        {
            ShowError("getNextTipSequenceNo", e.getMessage());
        }
        return (false);
    }


    boolean getTipList(int holidayId, int tipGroupId, ArrayList<TipItem> al)
    {
        try
        {
            String lSql="SELECT holidayId, tipGroupId, tipId, sequenceNo, tipDescription, " + "  tipPicture, tipNotes, infoId, noteId, galleryId  " + "FROM Tip " + "WHERE holidayId = " + holidayId + " " + "AND tipGroupId = " + tipGroupId + " " + "ORDER BY SequenceNo ";

            Cursor cursor=executeSQLOpenCursor("getTipList", lSql);
            if(cursor == null)
                return (false);

            while(cursor.moveToNext())
            {
                TipItem tipItem=new TipItem();
                if(GetTipItemFromQuery(cursor, tipItem) == false)
                    return (false);

                al.add(tipItem);
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("getTipList", e.getMessage());
        }
        return (false);
    }

    public boolean deleteTips(int holidayId, int tipGroupId)
    {
        try
        {
            ArrayList<TipItem> tipList=new ArrayList<>();
            if(getTipList(holidayId, tipGroupId, tipList) == false)
                return (false);
            for(TipItem tipItem : tipList)
            {
                if(deleteTipItem(tipItem) == false)
                    return (false);
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("deleteTips", e.getMessage());
        }
        return (false);
    }

    boolean clearNote(int holidayId, int noteId)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            String l_SQL="UPDATE tip SET noteId = 0 " + "WHERE holidayId = " + holidayId + " " + "AND noteId = " + noteId;

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
