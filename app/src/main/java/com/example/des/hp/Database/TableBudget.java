package com.example.des.hp.Database;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import androidx.annotation.NonNull;

import com.example.des.hp.Budget.BudgetItem;
import com.example.des.hp.R;
import com.example.des.hp.myutils.MyInt;
import com.example.des.hp.myutils.MyString;

import java.io.BufferedWriter;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
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
            String lSQL =
            "CREATE TABLE IF NOT EXISTS budget " + "( " +
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
            "  active            INT(5)   " +
            ") ";

            db.execSQL(lSQL);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
        return (false);
    }

    public void export(OutputStreamWriter buffwriter)
    {

        try
        {
            buffwriter.write("<budget>\n");

            String lSql =
                    "select holidayId, " +
                            "budgetId, " +
                            "sequenceNo, " +
                            "budgetDescription, " +
                            "budgetTotal, " +
                            "budgetPaid, " +
                            "budgetUnpaid, " +
                            "budgetPicture, " +
                            "budgetNotes, " +
                            "infoId, " +
                            "noteId, " +
                            "galleryId, " +
                            "active " +
                            "FROM budget " +
                            "ORDER BY holidayId, budgetId";

            Cursor cursor=executeSQLOpenCursor("export", lSql);
            if(cursor == null)
                return;

            while(cursor.moveToNext())
            {
                int holidayId = Integer.parseInt(cursor.getString(0));
                String pic = cursor.getString(7);
                String picAsBase64 = "";
                if(!pic.isEmpty()) {
                    picAsBase64 = pictureToBase64(holidayId, pic);
                }

                buffwriter.write(cursor.getString(0) + "," +
                        cursor.getString(1) + "," +
                        cursor.getString(2) + "," +
                        encodeString(cursor.getString(3)) + "," +
                        cursor.getString(4) + "," +
                        cursor.getString(5) + "," +
                        cursor.getString(6) + "," +
                        picAsBase64 + "," +
                        cursor.getString(8) + "," +
                        cursor.getString(9) + "," +
                        cursor.getString(10) + "," +
                        cursor.getString(11) + "," +
                        cursor.getString(12) + "\n"
                );

            }

        } catch (java.io.FileNotFoundException e)
        {
        }
        catch (java.io.IOException e)
        {
        }
    }


    private String booleanToString(boolean value)
    {
        if(value)
            return("1");
        return("0");
    }

    boolean booleanFromString(String value)
    {
        return value.compareTo("0") != 0;
    }


    boolean getBudgetCount(int holidayId, MyInt retInt)
    {
        try
        {
            if(!IsValid())
                return (false);

            String lSQL="SELECT IFNULL(COUNT(*),0) " +
                    "FROM Budget " +
                    "WHERE holidayId = " + holidayId + " " +
                    "AND active = " + booleanToString(true );

            return executeSQLGetInt("getBudgetCount", lSQL, retInt);
        }
        catch(Exception e)
        {
            ShowError("getBudgetCount", e.getMessage());
        }
        return (false);

    }

    boolean getOSBudgetCount(int holidayId, MyInt retInt)
    {
        try
        {
            if(!IsValid())
                return (false);

            String lSQL="SELECT IFNULL(COUNT(*),0) " +
                    "FROM Budget " +
                    "WHERE holidayId = " + holidayId + " " +
                    "AND (budgetUnpaid < -0.001 OR budgetUnpaid > 0.001) " +
                    "AND active = " + booleanToString(true );

            return executeSQLGetInt("getBudgetCount", lSQL, retInt);
        }
        catch(Exception e)
        {
            ShowError("getBudgetCount", e.getMessage());
        }
        return (false);

    }

    boolean addBudgetItem(BudgetItem budgetItem)
    {
        try
        {
            if (!IsValid())
                return (false);

            if (budgetItem.pictureAssigned)
            {
                /* if picture name has something in it - it means it came from internal folder */
                if (budgetItem.budgetPicture.isEmpty())
                {
                        //myMessages().LogMessage("  - Save new image and get a filename...");
                        MyString myString = new MyString();
                        if (!savePicture(budgetItem.holidayId, budgetItem.fileBitmap, myString))
                            return (false);
                        budgetItem.budgetPicture = myString.Value;
                        //myMessages().LogMessage("  - New filename " + budgetItem.budgetPicture);
                }
            }

            String lSql = "INSERT INTO Budget " +
                          "  ( " +
                          "    holidayId, " +
                          "    budgetId, " +
                          "    sequenceNo, " +
                          "    budgetDescription, " +
                          "    budgetTotal, " +
                          "    budgetPaid, " +
                          "    budgetUnpaid, " +
                          "    budgetPicture, " +
                          "    budgetNotes, " +
                          "    infoId, " +
                          "    noteId, " +
                          "    galleryId, " +
                          "    active " +
                          ") " +
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
                          budgetItem.galleryId + "," +
                          booleanToString(budgetItem.active) + " " +
                          ")";

            return (executeSQL("addBudgetItem", lSql));
        }
        catch(Exception e)
        {
            ShowError("addBudgetItem", e.getMessage());
        }
        return (false);

    }

    boolean updateBudgetItems(ArrayList<BudgetItem> items)
    {
        try
        {
            if(!IsValid())
                return (false);

            if(items == null)
                return (false);

            for(int i=0; i < items.size(); i++)
            {
                if(items.get(i).sequenceNo != 9999 && items.get(i).sequenceNo != items.get(i).origSequenceNo)
                {
                    if(!updateBudgetItem(items.get(i)))
                        return (false);
                }
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("updateBudgetItems", e.getMessage());
        }
        return (false);

    }

    boolean updateBudgetItem(BudgetItem budgetItem)
    {
        try
        {
            if (!IsValid())
                return (false);

            //myMessages().LogMessage("updateBudgetItem:Handling Image");
            if (budgetItem.pictureChanged)
            {
                if (!budgetItem.origPictureAssigned || budgetItem.budgetPicture.isEmpty() || budgetItem.budgetPicture.compareTo(budgetItem.origBudgetPicture) != 0) {
                    if (budgetItem.origPictureAssigned)
                    {
                        //myMessages().LogMessage("  - Original Image was assigned - need to get rid of it");
                        if (!removePictureByHolidayId(budgetItem.holidayId, budgetItem.origBudgetPicture))
                            return (false);
                    }

                    /* if picture name has something in it - it means it came from internal folder */
                    if (budgetItem.budgetPicture.isEmpty())
                    {
                        //myMessages().LogMessage("  - New Image was not from internal folder...");
                        if (budgetItem.pictureAssigned)
                        {
                            //myMessages().LogMessage("  - Save new image and get a filename...");
                            MyString myString = new MyString();
                            if (!savePicture(budgetItem.holidayId, budgetItem.fileBitmap, myString))
                                return (false);
                            budgetItem.budgetPicture = myString.Value;
                            //myMessages().LogMessage("  - New filename " + budgetItem.budgetPicture);
                        }
                    }
                }
            }

            String lSQL;
            lSQL = "UPDATE Budget " +
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
                   "    active = " + booleanToString(budgetItem.active) + " " +
                   "WHERE holidayId = " + budgetItem.holidayId + " " +
                   "AND budgetId = " + budgetItem.budgetId;

            return (executeSQL("updateBudgetItem", lSQL));
        }
        catch(Exception e)
        {
            ShowError("updateBudgetItem", e.getMessage());
        }
        return (false);

    }


    boolean deleteBudgetItem(BudgetItem budgetItem)
    {
        try
        {
            if(!IsValid())
                return (false);

            String lSQL="DELETE FROM Budget " + "WHERE holidayId = " + budgetItem.holidayId + " " + "AND budgetId = " + budgetItem.budgetId;

            if(!budgetItem.budgetPicture.isEmpty())
                if(!removePictureByHolidayId(budgetItem.holidayId, budgetItem.budgetPicture))
                    return (false);

            if(!executeSQL("deleteBudgetItem", lSQL))
                return (false);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("deleteBudgetItem", e.getMessage());
        }
        return (false);

    }

    boolean getBudgetItem(int holidayId, int budgetId, BudgetItem item)
    {
        try
        {
            if(!IsValid())
                return (false);

            String lSQL = getBudgetItemString(holidayId, budgetId);

            Cursor cursor=executeSQLOpenCursor("getBudgetItem", lSQL);
            if(cursor != null)
            {
                cursor.moveToFirst();
                if(!GetBudgetItemFromQuery(cursor, item))
                    return (false);
            }
            executeSQLCloseCursor("getBudgetItem");
            return (true);
        }
        catch(Exception e)
        {
            ShowError("getBudgetItem", e.getMessage());
        }
        return (false);

    }

    private static @NonNull String getBudgetItemString(int holidayId, int budgetId) {
        String lSQL;
        lSQL=
                "SELECT " +
                "  holidayId, " +
                "  budgetId, " +
                "  sequenceNo, " +
                "  budgetDescription, " +
                "  budgetTotal, " +
                "  budgetPaid, " +
                "  budgetUnpaid, " +
                "  budgetPicture, " +
                "  budgetNotes, " +
                "  infoId, " +
                "  noteId, " +
                "  galleryId, " +
                "  active " +
                "FROM budget " +
                "WHERE HolidayId = " + holidayId + " " +
                "AND BudgetId = " + budgetId;
        return lSQL;
    }

    private boolean GetBudgetItemFromQuery(Cursor cursor, BudgetItem budgetItem)
    {
        if(!IsValid())
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
            budgetItem.active=booleanFromString(cursor.getString(12));

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
            budgetItem.origActive=budgetItem.active;

            budgetItem.pictureChanged=false;

            if(!budgetItem.budgetPicture.isEmpty())
            {
                budgetItem.pictureAssigned=true;
                budgetItem.origPictureAssigned=true;
            } else
            {
                budgetItem.pictureAssigned=false;
                budgetItem.origPictureAssigned=false;
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("GetBudgetItemFromQuery", e.getMessage());
        }

        return (false);
    }

    boolean getNextBudgetId(int holidayId, MyInt retInt)
    {
        try
        {
            String lSQL="SELECT IFNULL(MAX(budgetId),0) " + "FROM Budget " + "WHERE holidayId = " + holidayId;

            if(!executeSQLGetInt("getNextBudgetId", lSQL, retInt))
                return (false);

            retInt.Value=retInt.Value + 1;

            return (true);
        }
        catch(Exception e)
        {
            ShowError("getNextBudgetId", e.getMessage());
        }
        return (false);

    }

    private void getBudgetTotal(int holidayId, MyInt retInt)
    {
        try
        {
            String lSQL=
                    "SELECT IFNULL(SUM(budgetTotal),0) " +
                    "FROM Budget " +
                    "WHERE holidayId = " + holidayId + " " +
                    "AND active = " + booleanToString(true);

            executeSQLGetInt("getBudgetTotal", lSQL, retInt);   
        }
        catch(Exception e)
        {
            ShowError("getBudgetTotal", e.getMessage());
        }
    }

    private void getBudgetUnpaid(int holidayId, MyInt retInt)
    {
        try
        {
            String lSQL="SELECT IFNULL(SUM(budgetUnpaid),0) " +
                    "FROM Budget " +
                    "WHERE holidayId = " + holidayId + " " +
                    "AND active = " + booleanToString(true);

            executeSQLGetInt("getBudgetUnpaid", lSQL, retInt);
        }
        catch(Exception e)
        {
            ShowError("getBudgetUnpaid", e.getMessage());
        }
    }

    private void getBudgetPaid(int holidayId, MyInt retInt)
    {
        try
        {
            String lSQL=
                    "SELECT IFNULL(SUM(budgetPaid),0) " +
                    "FROM Budget " +
                    "WHERE holidayId = " + holidayId + " " +
                    "AND active = " + booleanToString(true);

            executeSQLGetInt("getBudgetPaid", lSQL, retInt);
        }
        catch(Exception e)
        {
            ShowError("getBudgetPaid", e.getMessage());
        }
    }


    private void getOSBudgetTotal(int holidayId, MyInt retInt)
    {
        try
        {
            String lSQL="SELECT IFNULL(SUM(budgetTotal),0) " +
                    "FROM Budget " +
                    "WHERE holidayId = " + holidayId + " " +
                    "AND (budgetUnpaid < -0.001 OR budgetUnpaid > 0.001) " +
                    "AND active = " + booleanToString(true);

            executeSQLGetInt("getBudgetTotal", lSQL, retInt);
        }
        catch(Exception e)
        {
            ShowError("getBudgetTotal", e.getMessage());
        }
    }

    private void getOSBudgetUnpaid(int holidayId, MyInt retInt)
    {
        try
        {
            String lSQL="SELECT IFNULL(SUM(budgetUnpaid),0) " +
                    "FROM Budget " +
                    "WHERE holidayId = " + holidayId + " " +
                    "AND (budgetUnpaid < -0.001 OR budgetUnpaid > 0.001) " +
                    "AND active = " + booleanToString(true);

            executeSQLGetInt("getBudgetUnpaid", lSQL, retInt);
        }
        catch(Exception e)
        {
            ShowError("getBudgetUnpaid", e.getMessage());
        }
    }

    private void getOSBudgetPaid(int holidayId, MyInt retInt)
    {
        try
        {
            String lSQL="SELECT IFNULL(SUM(budgetPaid),0) " +
                    "FROM Budget " +
                    "WHERE holidayId = " + holidayId + " " +
                    "AND (budgetUnpaid < -0.001 OR budgetUnpaid > 0.001) " +
                    "AND active = " + booleanToString(true);

            executeSQLGetInt("getBudgetPaid", lSQL, retInt);
        }
        catch(Exception e)
        {
            ShowError("getBudgetPaid", e.getMessage());
        }
    }

    boolean getNextBudgetSequenceNo(int holidayId, MyInt retInt)
    {
        try
        {
            String lSQL="SELECT IFNULL(MAX(SequenceNo),0) " + "FROM Budget " + "WHERE holidayId = " + holidayId;

            if(!executeSQLGetInt("getNextBudgetSequenceNo", lSQL, retInt))
                return (false);

            retInt.Value=retInt.Value + 1;

            return (true);
        }
        catch(Exception e)
        {
            ShowError("getNextBudgetSequenceNo", e.getMessage());
        }
        return (false);

    }


    boolean getBudgetList(int holidayId, ArrayList<BudgetItem> al)
    {
        try
        {
            String lSql=
                    "SELECT " +
                            "  holidayId, " +
                            "  budgetId, " +
                            "  sequenceNo, " +
                            "  budgetDescription, " +
                            "  budgetTotal, " +
                            "  budgetPaid, " +
                            "  budgetUnpaid, " +
                            "  budgetPicture, " +
                            "  budgetNotes, " +
                            "  infoId, " +
                            "  noteId, " +
                            "  galleryId, " +
                            "  active " +
                            "FROM budget " +
                            "WHERE HolidayId = " + holidayId + " " +
                            "ORDER BY SequenceNo ";

            Cursor cursor=executeSQLOpenCursor("getBudgetList", lSql);
            if(cursor == null)
                return (false);

            while(cursor.moveToNext())
            {
                BudgetItem budgetItem=new BudgetItem();
                if(!GetBudgetItemFromQuery(cursor, budgetItem))
                    return (false);

                al.add(budgetItem);
            }
            BudgetItem totalBudgetItem=new BudgetItem();
            totalBudgetItem.sequenceNo=9999;

            totalBudgetItem.budgetDescription=_context.getString(R.string.caption_budget_total_marker);
            MyInt myInt=new MyInt();

            getBudgetPaid(holidayId, myInt);
            totalBudgetItem.budgetPaid=myInt.Value;

            getBudgetUnpaid(holidayId, myInt);
            totalBudgetItem.budgetUnpaid=myInt.Value;

            getBudgetTotal(holidayId, myInt);
            totalBudgetItem.budgetTotal=myInt.Value;

            al.add(totalBudgetItem);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("getBudgetList", e.getMessage());
        }
        return (false);

    }

    boolean getActiveBudgetList(int holidayId, ArrayList<BudgetItem> al)
    {
        try
        {
            String lSql=
                    "SELECT " +
                            "  holidayId, " +
                            "  budgetId, " +
                            "  sequenceNo, " +
                            "  budgetDescription, " +
                            "  budgetTotal, " +
                            "  budgetPaid, " +
                            "  budgetUnpaid, " +
                            "  budgetPicture, " +
                            "  budgetNotes, " +
                            "  infoId, " +
                            "  noteId, " +
                            "  galleryId, " +
                            "  active " +
                            "FROM budget " +
                            "WHERE HolidayId = " + holidayId + " " +
                            "AND active = " + booleanToString(true) + " " +
                            "ORDER BY SequenceNo ";

            Cursor cursor=executeSQLOpenCursor("getBudgetList", lSql);
            if(cursor == null)
                return (false);

            while(cursor.moveToNext())
            {
                BudgetItem budgetItem=new BudgetItem();
                if(!GetBudgetItemFromQuery(cursor, budgetItem))
                    return (false);

                al.add(budgetItem);
            }
            BudgetItem totalBudgetItem=new BudgetItem();
            totalBudgetItem.sequenceNo=9999;

            totalBudgetItem.budgetDescription=_context.getString(R.string.caption_budget_total_marker);
            MyInt myInt=new MyInt();

            getBudgetPaid(holidayId, myInt);
            totalBudgetItem.budgetPaid=myInt.Value;

            getBudgetUnpaid(holidayId, myInt);
            totalBudgetItem.budgetUnpaid=myInt.Value;

            getBudgetTotal(holidayId, myInt);
            totalBudgetItem.budgetTotal=myInt.Value;

            al.add(totalBudgetItem);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("getBudgetList", e.getMessage());
        }
        return (false);

    }


    boolean getOSActiveBudgetList(int holidayId, ArrayList<BudgetItem> al)
    {
        try
        {
            String lSql=
                    "SELECT " +
                            "  holidayId, " +
                            "  budgetId, " +
                            "  sequenceNo, " +
                            "  budgetDescription, " +
                            "  budgetTotal, " +
                            "  budgetPaid, " +
                            "  budgetUnpaid, " +
                            "  budgetPicture, " +
                            "  budgetNotes, " +
                            "  infoId, " +
                            "  noteId, " +
                            "  galleryId, " +
                            "  active  " +
                            "FROM budget " +
                            "WHERE HolidayId = " + holidayId + " " +
                            "AND (budgetUnpaid < -0.001 OR budgetUnpaid > 0.001) " +
                            "AND active = " + booleanToString(true) + " " +
                            "ORDER BY SequenceNo ";

            Cursor cursor=executeSQLOpenCursor("getBudgetList", lSql);
            if(cursor == null)
                return (false);

            while(cursor.moveToNext())
            {
                BudgetItem budgetItem=new BudgetItem();
                if(!GetBudgetItemFromQuery(cursor, budgetItem))
                    return (false);

                al.add(budgetItem);
            }
            BudgetItem totalBudgetItem=new BudgetItem();
            totalBudgetItem.sequenceNo=9999;

            totalBudgetItem.budgetDescription=_context.getString(R.string.caption_budget_total_marker);
            MyInt myInt=new MyInt();

            getOSBudgetPaid(holidayId, myInt);
            totalBudgetItem.budgetPaid=myInt.Value;

            getOSBudgetUnpaid(holidayId, myInt);
            totalBudgetItem.budgetUnpaid=myInt.Value;

            getOSBudgetTotal(holidayId, myInt);
            totalBudgetItem.budgetTotal=myInt.Value;

            al.add(totalBudgetItem);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("getBudgetList", e.getMessage());
        }
        return (false);

    }

    boolean getOSBudgetList(int holidayId, ArrayList<BudgetItem> al)
    {
        try
        {
            String lSql=
                    "SELECT " +
                            "  holidayId, " +
                            "  budgetId, " +
                            "  sequenceNo, " +
                            "  budgetDescription, " +
                            "  budgetTotal, " +
                            "  budgetPaid, " +
                            "  budgetUnpaid, " +
                            "  budgetPicture, " +
                            "  budgetNotes, " +
                            "  infoId, " +
                            "  noteId, " +
                            "  galleryId, " +
                            "  active  " +
                            "FROM budget " +
                            "WHERE HolidayId = " + holidayId + " " +
                            "AND (budgetUnpaid < -0.001 OR budgetUnpaid > 0.001) " +
                            "ORDER BY SequenceNo ";

            Cursor cursor=executeSQLOpenCursor("getBudgetList", lSql);
            if(cursor == null)
                return (false);

            while(cursor.moveToNext())
            {
                BudgetItem budgetItem=new BudgetItem();
                if(!GetBudgetItemFromQuery(cursor, budgetItem))
                    return (false);

                al.add(budgetItem);
            }
            BudgetItem totalBudgetItem=new BudgetItem();
            totalBudgetItem.sequenceNo=9999;

            totalBudgetItem.budgetDescription=_context.getString(R.string.caption_budget_total_marker);
            MyInt myInt=new MyInt();

            getOSBudgetPaid(holidayId, myInt);
            totalBudgetItem.budgetPaid=myInt.Value;

            getOSBudgetUnpaid(holidayId, myInt);
            totalBudgetItem.budgetUnpaid=myInt.Value;

            getOSBudgetTotal(holidayId, myInt);
            totalBudgetItem.budgetTotal=myInt.Value;

            al.add(totalBudgetItem);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("getBudgetList", e.getMessage());
        }
        return (false);

    }


}
