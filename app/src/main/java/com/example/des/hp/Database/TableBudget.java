package com.example.des.hp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.des.hp.Budget.BudgetItem;
import com.example.des.hp.myutils.MyInt;
import com.example.des.hp.myutils.MyMessages;
import com.example.des.hp.myutils.MyString;

import java.util.ArrayList;

class TableBudget extends TableBase
{

    TableBudget(Context context, SQLiteOpenHelper dbHelper)
    {
        super(context, dbHelper);
    }

    public void ShowError(String argFunction, String argMessage)
    {
        super.ShowError("TableBudget:" + argFunction, argMessage);
    }

    public boolean onCreate(SQLiteDatabase db)
    {
        try
        {
            String lSQL="CREATE TABLE IF NOT EXISTS budget " +
                "( " +
                "  holidayId         INT(5),  " +
                "  budgetId          INT(5),  " +
                "  sequenceNo        INT(5),  " +
                "  budgetDescription VARCHAR, " +
                "  budgetTotal       INT(5),  " +
                "  budgetPaid        INT(5),  " +
                "  budgetUnpaid      INT(5),  " +
                "  budgetPicture     VARCHAR, " +
                "  budgetNotes       VARCHAR, " +
                "  infoId            INT(5),  " +
                "  noteId            INT(5),  " +
                "  galleryId         INT(5),  " +
                "  sygicId           INT(5)   " +
                ") ";

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
                db.execSQL("ALTER TABLE budget ADD COLUMN noteId INT(5) DEFAULT 0");
                db.execSQL("ALTER TABLE budget ADD COLUMN galleryId INT(5) DEFAULT 0");
                db.execSQL("ALTER TABLE budget ADD COLUMN sygicId INT(5) DEFAULT 0");

                db.execSQL("UPDATE budget SET noteId = 0");
                db.execSQL("UPDATE budget SET galleryId = 0");
                db.execSQL("UPDATE budget SET sygicId = 0");
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("onUpgrade", e.getMessage());
            return (false);
        }
    }

    boolean getBudgetCount(int argholidayId, MyInt retInt)
    {
        if(IsValid() == false)
            return (false);

        String lSQL="SELECT IFNULL(COUNT(*),0) " +
            "FROM Budget " +
            "WHERE holidayId = " + argholidayId;

        if(executeSQLGetInt("getBudgetCount", lSQL, retInt) == false)
            return (false);

        return (true);
    }

    boolean addBudgetItem(BudgetItem budgetItem)
    {
        if(IsValid() == false)
            return (false);

        budgetItem.budgetPicture="";
        if(budgetItem.pictureAssigned)
        {
            MyString myString=new MyString();
            if(savePicture(budgetItem.fileBitmap, myString) == false)
                return (false);
            budgetItem.budgetPicture=myString.Value;
        }

        String lSql="INSERT INTO Budget " +
            "  (holidayId, budgetId, sequenceNo, budgetDescription, " +
            "   budgetTotal, budgetPaid, budgetUnpaid, budgetPicture, " +
            "   budgetNotes, infoId, noteId, galleryId, sygicId) " +
            "VALUES " +
            "(" +
            budgetItem.holidayId + "," +
            budgetItem.budgetId + "," +
            budgetItem.sequenceNo + ", " +
            MyQuotedString(budgetItem.budgetDescription) + ", " +
            budgetItem.budgetTotal + "," +
            budgetItem.budgetPaid + "," +
            budgetItem.budgetUnpaid + "," +
            MyQuotedString(budgetItem.budgetPicture) + ", " +
            MyQuotedString(budgetItem.budgetNotes) + ", " +
            budgetItem.infoId + ", " +
            budgetItem.noteId + ", " +
            budgetItem.galleryId + ", " +
            budgetItem.sygicId + " " +
            ")";

        return (executeSQL("addBudgetItem", lSql));
    }

    boolean updateBudgetItems(ArrayList<BudgetItem> items)
    {
        if(IsValid() == false)
            return (false);

        if(items == null)
            return (false);

        for(int i=0; i < items.size(); i++)
        {
            if(items.get(i).sequenceNo != items.get(i).origSequenceNo)
            {
                if(updateBudgetItem(items.get(i)) == false)
                    return (false);
            }
        }
        return (true);
    }

    boolean updateBudgetItem(BudgetItem budgetItem)
    {
        if(IsValid() == false)
            return (false);

        // something has happened to the picture either selected a new one or cleared an
        // existing one
        if(budgetItem.pictureChanged)
        {
            if(budgetItem.origPictureAssigned)
                if(removePicture(budgetItem.origBudgetPicture) == false)
                    return (false);
            budgetItem.budgetPicture="";
            if(budgetItem.pictureAssigned)
            {
                MyString myString=new MyString();
                if(savePicture(budgetItem.fileBitmap, myString) == false)
                    return (false);
                budgetItem.budgetPicture=myString.Value;
            }
        }

        String lSQL;
        lSQL="UPDATE Budget " +
            "SET sequenceNo = " + budgetItem.sequenceNo + ", " +
            "    budgetDescription = " + MyQuotedString(budgetItem.budgetDescription) + ", " +
            "    budgetTotal = " + budgetItem.budgetTotal + ", " +
            "    budgetPaid = " + budgetItem.budgetPaid + ", " +
            "    budgetUnpaid = " + budgetItem.budgetUnpaid + ", " +
            "    budgetPicture = " + MyQuotedString(budgetItem.budgetPicture) + ", " +
            "    budgetNotes = " + MyQuotedString(budgetItem.budgetNotes) + ", " +
            "    infoId = " + budgetItem.infoId + ", " +
            "    noteId = " + budgetItem.noteId + ", " +
            "    galleryId = " + budgetItem.galleryId + ", " +
            "    sygicId = " + budgetItem.sygicId + " " +
            "WHERE holidayId = " + budgetItem.holidayId + " " +
            "AND budgetId = " + budgetItem.budgetId;

        return (executeSQL("updateBudgetItem", lSQL));
    }

    boolean deleteBudgetItem(BudgetItem budgetItem)

    {
        if(IsValid() == false)
            return (false);

        String lSQL="DELETE FROM Budget " +
            "WHERE holidayId = " + budgetItem.holidayId + " " +
            "AND budgetId = " + budgetItem.budgetId;

        if(executeSQL("deleteBudgetItem", lSQL) == false)
            return (false);

        if(budgetItem.budgetPicture.length() > 0)
            if(removePicture(budgetItem.budgetPicture) == false)
                return (false);

        return (true);
    }

    boolean getBudgetItem(int holidayId, int budgetId, BudgetItem litem)
    {
        if(IsValid() == false)
            return (false);

        String lSQL;
        lSQL="SELECT holidayId, budgetId, sequenceNo, budgetDescription, " +
            "  budgetTotal, budgetPaid, budgetUnpaid, budgetPicture, budgetNotes, infoId, " +
            "  noteId, galleryId, sygicId " +
            "FROM budget " +
            "WHERE HolidayId = " + holidayId + " " +
            "AND BudgetId = " + budgetId;

        Cursor cursor=executeSQLOpenCursor("getBudgetItem", lSQL);
        if(cursor != null)
        {
            cursor.moveToFirst();
            if(GetBudgetItemFromQuery(cursor, litem) == false)
                return (false);
        }
        executeSQLCloseCursor("getBudgetItem");
        return (true);
    }

    private boolean GetBudgetItemFromQuery(Cursor cursor, BudgetItem budgetItem)
    {
        if(IsValid() == false)
            return (false);

        try
        {
            if(cursor.getCount() == 0)
                return (true);

            budgetItem.holidayId=Integer.parseInt(cursor.getString(0));
            budgetItem.budgetId=Integer.parseInt(cursor.getString(1));
            budgetItem.sequenceNo=Integer.parseInt(cursor.getString(2));
            budgetItem.budgetDescription=cursor.getString(3);
            budgetItem.budgetTotal=Integer.parseInt(cursor.getString(4));
            budgetItem.budgetPaid=Integer.parseInt(cursor.getString(5));
            budgetItem.budgetUnpaid=Integer.parseInt(cursor.getString(6));
            budgetItem.budgetPicture=cursor.getString(7);
            budgetItem.budgetNotes=cursor.getString(8);
            budgetItem.infoId=Integer.parseInt(cursor.getString(9));
            budgetItem.noteId=Integer.parseInt(cursor.getString(10));
            budgetItem.galleryId=Integer.parseInt(cursor.getString(11));
            budgetItem.sygicId=Integer.parseInt(cursor.getString(12));

            budgetItem.origHolidayId=budgetItem.holidayId;
            budgetItem.origBudgetId=budgetItem.budgetId;
            budgetItem.origSequenceNo=budgetItem.sequenceNo;
            budgetItem.origBudgetDescription=budgetItem.budgetDescription;
            budgetItem.origBudgetTotal=budgetItem.budgetTotal;
            budgetItem.origBudgetPaid=budgetItem.budgetPaid;
            budgetItem.origBudgetUnpaid=budgetItem.budgetUnpaid;
            budgetItem.origBudgetPicture=budgetItem.budgetPicture;
            budgetItem.origBudgetNotes=budgetItem.budgetNotes;
            budgetItem.origInfoId=budgetItem.infoId;
            budgetItem.origNoteId=budgetItem.noteId;
            budgetItem.origGalleryId=budgetItem.galleryId;
            budgetItem.origSygicId=budgetItem.sygicId;

            budgetItem.pictureChanged=false;

            if(budgetItem.budgetPicture.length() > 0)
            {
                budgetItem.pictureAssigned=true;
                budgetItem.origPictureAssigned=true;
            } else
            {
                budgetItem.pictureAssigned=false;
                budgetItem.origPictureAssigned=false;
            }
        }
        catch(Exception e)
        {
            ShowError("GetBudgetItemFromQuery", e.getMessage());
        }

        return (true);
    }

    boolean getNextBudgetId(int holidayId, MyInt retInt)
    {
        String lSQL="SELECT IFNULL(MAX(budgetId),0) " +
            "FROM Budget " +
            "WHERE holidayId = " + holidayId;

        if(executeSQLGetInt("getNextBudgetId", lSQL, retInt) == false)
            return (false);

        retInt.Value=retInt.Value + 1;

        return (true);
    }

    boolean getBudgetTotal(int holidayId, MyInt retInt)
    {
        String lSQL="SELECT IFNULL(SUM(budgetTotal),0) " +
            "FROM Budget " +
            "WHERE holidayId = " + holidayId;

        if(executeSQLGetInt("getBudgetTotal", lSQL, retInt) == false)
            return (false);

        return (true);
    }

    boolean getBudgetUnpaid(int holidayId, MyInt retInt)
    {
        String lSQL="SELECT IFNULL(SUM(budgetUnpaid),0) " +
            "FROM Budget " +
            "WHERE holidayId = " + holidayId;

        if(executeSQLGetInt("getBudgetUnpaid", lSQL, retInt) == false)
            return (false);

        return (true);
    }

    boolean getBudgetPaid(int holidayId, MyInt retInt)
    {
        String lSQL="SELECT IFNULL(SUM(budgetPaid),0) " +
            "FROM Budget " +
            "WHERE holidayId = " + holidayId;

        if(executeSQLGetInt("getBudgetPaid", lSQL, retInt) == false)
            return (false);

        return (true);
    }

    boolean getNextBudgetSequenceNo(int holidayId, MyInt retInt)
    {
        String lSQL="SELECT IFNULL(MAX(SequenceNo),0) " +
            "FROM Budget " +
            "WHERE holidayId = " + holidayId;

        if(executeSQLGetInt("getNextBudgetSequenceNo", lSQL, retInt) == false)
            return (false);

        retInt.Value=retInt.Value + 1;

        return (true);
    }


    boolean getBudgetList(int holidayId, ArrayList<BudgetItem> al)
    {
        String lSql="SELECT holidayId, budgetId, sequenceNo, budgetDescription, " +
            "  budgetTotal, budgetPaid, budgetUnpaid, budgetPicture, budgetNotes, infoId,  " +
            "  noteId, galleryId, sygicId " +
            "FROM Budget " +
            "WHERE holidayId = " + holidayId + " " +
            "ORDER BY SequenceNo ";

        Cursor cursor=executeSQLOpenCursor("getBudgetList", lSql);
        if(cursor == null)
            return (false);

        while(cursor.moveToNext())
        {
            BudgetItem budgetItem=new BudgetItem();
            if(GetBudgetItemFromQuery(cursor, budgetItem) == false)
                return (false);

            al.add(budgetItem);
        }
        return (true);
    }

    boolean clearNote(int holidayId, int noteId)
    {
        if(IsValid()==false)
            return(false);

        String l_SQL =
            "UPDATE budget SET noteId = 0 " +
                "WHERE holidayId = " + holidayId + " " +
                "AND noteId = " + noteId;

        if(executeSQL("clearNote", l_SQL)==false)
            return(false);

        return(true);
    }

}
