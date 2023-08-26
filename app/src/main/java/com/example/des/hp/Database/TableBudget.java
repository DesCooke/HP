package com.example.des.hp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.des.hp.Budget.BudgetItem;
import com.example.des.hp.R;
import com.example.des.hp.myutils.MyInt;
import com.example.des.hp.myutils.MyString;

import java.util.ArrayList;
import java.util.Random;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
import static com.example.des.hp.myutils.MyMessages.myMessages;

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
            "  sygicId           INT(5),  " +
            "  useOption         INT(5),  " +
            "  useOption1        INT(5),  " +
            "  useOption2        INT(5),  " +
            "  useOption3        INT(5),  " +
            "  useOption4        INT(5),  " +
            "  useOption5        INT(5),  " +
            "  option1Desc       VARCHAR," +
            "  option2Desc       VARCHAR," +
            "  option3Desc       VARCHAR," +
            "  option4Desc       VARCHAR," +
            "  option5Desc       VARCHAR," +
            "  option1Budget     INT(5), " +
            "  option2Budget     INT(5), " +
            "  option3Budget     INT(5), " +
            "  option4Budget     INT(5), " +
            "  option5Budget     INT(5), " +
            "  optionSequenceNo  INT(5) " +
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
            if(oldVersion == 60 && newVersion == 61)
            {
                db.execSQL("ALTER TABLE budget ADD COLUMN useOption INT(5) DEFAULT 0");
                db.execSQL("ALTER TABLE budget ADD COLUMN useOption1 INT(5) DEFAULT 0");
                db.execSQL("ALTER TABLE budget ADD COLUMN option1Desc VARCHAR DEFAULT ''");
                db.execSQL("ALTER TABLE budget ADD COLUMN option1Budget INT(5) DEFAULT 0");
                db.execSQL("ALTER TABLE budget ADD COLUMN useOption2 INT(5) DEFAULT 0");
                db.execSQL("ALTER TABLE budget ADD COLUMN option2Desc VARCHAR DEFAULT ''");
                db.execSQL("ALTER TABLE budget ADD COLUMN option2Budget INT(5) DEFAULT 0");
                db.execSQL("ALTER TABLE budget ADD COLUMN useOption3 INT(5) DEFAULT 0");
                db.execSQL("ALTER TABLE budget ADD COLUMN option3Desc VARCHAR DEFAULT ''");
                db.execSQL("ALTER TABLE budget ADD COLUMN option3Budget INT(5) DEFAULT 0");

                db.execSQL("UPDATE budget SET useOption = 0");
                db.execSQL("UPDATE budget SET useOption1 = 0");
                db.execSQL("UPDATE budget SET option1Desc = ''");
                db.execSQL("UPDATE budget SET option1Budget = 0");
                db.execSQL("UPDATE budget SET useOption2 = 0");
                db.execSQL("UPDATE budget SET option2Desc = ''");
                db.execSQL("UPDATE budget SET option2Budget = 0");
                db.execSQL("UPDATE budget SET useOption3 = 0");
                db.execSQL("UPDATE budget SET option3Desc = ''");
                db.execSQL("UPDATE budget SET option3Budget = 0");
            }
            if(oldVersion == 61 && newVersion == 62)
            {
                db.execSQL("ALTER TABLE budget ADD COLUMN useOption4 INT(5) DEFAULT 0");
                db.execSQL("ALTER TABLE budget ADD COLUMN option4Desc VARCHAR DEFAULT ''");
                db.execSQL("ALTER TABLE budget ADD COLUMN option4Budget INT(5) DEFAULT 0");
                db.execSQL("ALTER TABLE budget ADD COLUMN useOption5 INT(5) DEFAULT 0");
                db.execSQL("ALTER TABLE budget ADD COLUMN option5Desc VARCHAR DEFAULT ''");
                db.execSQL("ALTER TABLE budget ADD COLUMN option5Budget INT(5) DEFAULT 0");

                db.execSQL("UPDATE budget SET useOption4 = 0");
                db.execSQL("UPDATE budget SET option4Desc = ''");
                db.execSQL("UPDATE budget SET option4Budget = 0");
                db.execSQL("UPDATE budget SET useOption5 = 0");
                db.execSQL("UPDATE budget SET option5Desc = ''");
                db.execSQL("UPDATE budget SET option5Budget = 0");
            }
            if(oldVersion == 67 && newVersion == 68)
            {
                db.execSQL("ALTER TABLE budget ADD COLUMN optionSequenceNo INT(5) DEFAULT 0");

                db.execSQL("UPDATE budget SET optionSequenceNo = 0");
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("onUpgrade", e.getMessage());
        }
        return (false);
    }

    boolean getBudgetCount(int argholidayId, MyInt retInt)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            String lSQL="SELECT IFNULL(COUNT(*),0) " + "FROM Budget " + "WHERE holidayId = " + argholidayId;

            if(executeSQLGetInt("getBudgetCount", lSQL, retInt) == false)
                return (false);

            return (true);
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
            if (IsValid() == false)
                return (false);

            if (budgetItem.pictureAssigned)
            {
                /* if picture name has something in it - it means it came from internal folder */
                if (budgetItem.budgetPicture.length() == 0)
                {
                    //myMessages().LogMessage("  - New Image was not from internal folder...");
                    if (budgetItem.pictureAssigned)
                    {
                        //myMessages().LogMessage("  - Save new image and get a filename...");
                        MyString myString = new MyString();
                        if (savePicture(budgetItem.holidayId, budgetItem.fileBitmap, myString) == false)
                            return (false);
                        budgetItem.budgetPicture = myString.Value;
                        //myMessages().LogMessage("  - New filename " + budgetItem.budgetPicture);
                    } else
                    {
                        //myMessages().LogMessage("  - New Image not setup - so - keep it blank");
                    }
                } else
                {
                    //myMessages().LogMessage("  - New Image was from internal folder - so just use it (" + budgetItem.budgetPicture + ")");
                }
            } else
            {
                //myMessages().LogMessage("  - New Image not assigned - do nothing");
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
                          "    sygicId, " +
                          "    useOption, " +
                          "    useOption1, " +
                          "    useOption2, " +
                          "    useOption3, " +
                          "    useOption4, " +
                          "    useOption5, " +
                          "    option1Desc, " +
                          "    option2Desc, " +
                          "    option3Desc, " +
                          "    option4Desc, " +
                          "    option5Desc, " +
                          "    option1Budget, " +
                          "    option2Budget, " +
                          "    option3Budget, " +
                          "    option4Budget, " +
                          "    option5Budget, " +
                          "    optionSequenceNo" +
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
                          budgetItem.galleryId + ", " +
                          budgetItem.sygicId + ", " +
                          bool2Int(budgetItem.useOption) + ", " +
                          bool2Int(budgetItem.useOption1) + ", " +
                          bool2Int(budgetItem.useOption2) + ", " +
                          bool2Int(budgetItem.useOption3) + ", " +
                          bool2Int(budgetItem.useOption4) + ", " +
                          bool2Int(budgetItem.useOption5) + ", " +
                          MyQuotedString(budgetItem.option1Desc) + ", " +
                          MyQuotedString(budgetItem.option2Desc) + ", " +
                          MyQuotedString(budgetItem.option3Desc) + ", " +
                          MyQuotedString(budgetItem.option4Desc) + ", " +
                          MyQuotedString(budgetItem.option5Desc) + ", " +
                          budgetItem.option1Budget + ", " +
                          budgetItem.option2Budget + ", " +
                          budgetItem.option3Budget + ", " +
                          budgetItem.option4Budget + ", " +
                          budgetItem.option5Budget + ", " +
                          budgetItem.optionSequenceNo + " " +
                          ")";

            return (executeSQL("addBudgetItem", lSql));
        }
        catch(Exception e)
        {
            ShowError("addBudgetItem", e.getMessage());
        }
        return (false);

    }

    public int bool2Int(boolean thebool)
    {
        if(thebool)
            return(1);
        return(0);
    }

    boolean updateBudgetItems(ArrayList<BudgetItem> items)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            if(items == null)
                return (false);

            for(int i=0; i < items.size(); i++)
            {
                if(items.get(i).sequenceNo != 9999 && items.get(i).sequenceNo != items.get(i).origSequenceNo)
                {
                    if(updateBudgetItem(items.get(i)) == false)
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
            if (IsValid() == false)
                return (false);

            //myMessages().LogMessage("updateBudgetItem:Handling Image");
            if (budgetItem.pictureChanged)
            {
                if (budgetItem.origPictureAssigned && budgetItem.budgetPicture.length() > 0 && budgetItem.budgetPicture.compareTo(budgetItem.origBudgetPicture) == 0)
                {
                    //myMessages().LogMessage("  - Original Image changed back to the original - do nothing");
                } else
                {
                    if (budgetItem.origPictureAssigned)
                    {
                        //myMessages().LogMessage("  - Original Image was assigned - need to get rid of it");
                        if (removePicture(budgetItem.holidayId, budgetItem.origBudgetPicture) == false)
                            return (false);
                    }

                    /* if picture name has something in it - it means it came from internal folder */
                    if (budgetItem.budgetPicture.length() == 0)
                    {
                        //myMessages().LogMessage("  - New Image was not from internal folder...");
                        if (budgetItem.pictureAssigned)
                        {
                            //myMessages().LogMessage("  - Save new image and get a filename...");
                            MyString myString = new MyString();
                            if (savePicture(budgetItem.holidayId, budgetItem.fileBitmap, myString) == false)
                                return (false);
                            budgetItem.budgetPicture = myString.Value;
                            //myMessages().LogMessage("  - New filename " + budgetItem.budgetPicture);
                        } else
                        {
                            //myMessages().LogMessage("  - New Image not setup - so - keep it blank");
                        }
                    } else
                    {
                        //myMessages().LogMessage("  - New Image was from internal folder - so just use it (" + budgetItem.budgetPicture + ")");
                    }
                }
            } else
            {
                //myMessages().LogMessage("  - Image not changed - do nothing");
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
                   "    sygicId = " + budgetItem.sygicId + ", " +
                   "    useOption = " + bool2Int(budgetItem.useOption) + ", " +
                   "    useOption1 = " + bool2Int(budgetItem.useOption1) + ", " +
                   "    useOption2 = " + bool2Int(budgetItem.useOption2) + ", " +
                   "    useOption3 = " + bool2Int(budgetItem.useOption3) + ", " +
                   "    useOption4 = " + bool2Int(budgetItem.useOption4) + ", " +
                   "    useOption5 = " + bool2Int(budgetItem.useOption5) + ", " +
                   "    option1Desc = " + MyQuotedString(budgetItem.option1Desc) + ", " +
                   "    option2Desc = " + MyQuotedString(budgetItem.option2Desc) + ", " +
                   "    option3Desc = " + MyQuotedString(budgetItem.option3Desc) + ", " +
                   "    option4Desc = " + MyQuotedString(budgetItem.option4Desc) + ", " +
                   "    option5Desc = " + MyQuotedString(budgetItem.option5Desc) + ", " +
                   "    option1Budget = " + budgetItem.option1Budget + ", " +
                   "    option2Budget = " + budgetItem.option2Budget + ", " +
                   "    option3Budget = " + budgetItem.option3Budget + ", " +
                   "    option4Budget = " + budgetItem.option4Budget + ", " +
                   "    option5Budget = " + budgetItem.option5Budget + ", " +
                   "    optionSequenceNo = " + budgetItem.optionSequenceNo + " " +
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
            if(IsValid() == false)
                return (false);

            String lSQL="DELETE FROM Budget " + "WHERE holidayId = " + budgetItem.holidayId + " " + "AND budgetId = " + budgetItem.budgetId;

            if(budgetItem.budgetPicture.length() > 0)
                if(removePicture(budgetItem.holidayId, budgetItem.budgetPicture) == false)
                    return (false);

            if(executeSQL("deleteBudgetItem", lSQL) == false)
                return (false);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("deleteBudgetItem", e.getMessage());
        }
        return (false);

    }

    boolean getBudgetItem(int holidayId, int budgetId, BudgetItem litem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

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
                    "  sygicId, " +
                    "  useOption, " +
                    "  useOption1, " +
                    "  useOption2, " +
                    "  useOption3, " +
                    "  useOption4, " +
                    "  useOption5, " +
                    "  option1Desc, " +
                    "  option2Desc, " +
                    "  option3Desc, " +
                    "  option4Desc, " +
                    "  option5Desc, " +
                    "  option1Budget, " +
                    "  option2Budget, " +
                    "  option3Budget, " +
                    "  option4Budget, " +
                    "  option5Budget, " +
                    "  optionSequenceNo " +
                    " " +
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
        catch(Exception e)
        {
            ShowError("getBudgetItem", e.getMessage());
        }
        return (false);

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
            budgetItem.useOption=String2Bool(cursor.getString(13));
            budgetItem.useOption1=String2Bool(cursor.getString(14));
            budgetItem.useOption2=String2Bool(cursor.getString(15));
            budgetItem.useOption3=String2Bool(cursor.getString(16));
            budgetItem.useOption4=String2Bool(cursor.getString(17));
            budgetItem.useOption5=String2Bool(cursor.getString(18));
            budgetItem.option1Desc=cursor.getString(19);
            budgetItem.option2Desc=cursor.getString(20);
            budgetItem.option3Desc=cursor.getString(21);
            budgetItem.option4Desc=cursor.getString(22);
            budgetItem.option5Desc=cursor.getString(23);
            budgetItem.option1Budget=Integer.parseInt(cursor.getString(24));
            budgetItem.option2Budget=Integer.parseInt(cursor.getString(25));
            budgetItem.option3Budget=Integer.parseInt(cursor.getString(26));
            budgetItem.option4Budget=Integer.parseInt(cursor.getString(27));
            budgetItem.option5Budget=Integer.parseInt(cursor.getString(28));
            budgetItem.optionSequenceNo=Integer.parseInt(cursor.getString(29));

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
            return (true);
        }
        catch(Exception e)
        {
            ShowError("GetBudgetItemFromQuery", e.getMessage());
        }

        return (false);
    }

    boolean String2Bool(String theString)
    {
        if(theString.compareTo("1")==0)
            return(true);
        return(false);
    }
    boolean getNextBudgetId(int holidayId, MyInt retInt)
    {
        try
        {
            String lSQL="SELECT IFNULL(MAX(budgetId),0) " + "FROM Budget " + "WHERE holidayId = " + holidayId;

            if(executeSQLGetInt("getNextBudgetId", lSQL, retInt) == false)
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

    private boolean getBudgetTotal(int holidayId, MyInt retInt)
    {
        try
        {
            String lSQL="SELECT IFNULL(SUM(budgetTotal),0) " + "FROM Budget " + "WHERE holidayId = " + holidayId;

            if(executeSQLGetInt("getBudgetTotal", lSQL, retInt) == false)
                return (false);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("getBudgetTotal", e.getMessage());
        }
        return (false);

    }

    private boolean getBudgetUnpaid(int holidayId, MyInt retInt)
    {
        try
        {
            String lSQL="SELECT IFNULL(SUM(budgetUnpaid),0) " + "FROM Budget " + "WHERE holidayId = " + holidayId;

            if(executeSQLGetInt("getBudgetUnpaid", lSQL, retInt) == false)
                return (false);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("getBudgetUnpaid", e.getMessage());
        }
        return (false);

    }

    private boolean getBudgetPaid(int holidayId, MyInt retInt)
    {
        try
        {
            String lSQL="SELECT IFNULL(SUM(budgetPaid),0) " + "FROM Budget " + "WHERE holidayId = " + holidayId;

            if(executeSQLGetInt("getBudgetPaid", lSQL, retInt) == false)
                return (false);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("getBudgetPaid", e.getMessage());
        }
        return (false);

    }

    boolean getNextBudgetSequenceNo(int holidayId, MyInt retInt)
    {
        try
        {
            String lSQL="SELECT IFNULL(MAX(SequenceNo),0) " + "FROM Budget " + "WHERE holidayId = " + holidayId;

            if(executeSQLGetInt("getNextBudgetSequenceNo", lSQL, retInt) == false)
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
            "  sygicId, " +
            "  useOption, " +
            "  useOption1, " +
            "  useOption2, " +
            "  useOption3, " +
            "  useOption4, " +
            "  useOption5, " +
            "  option1Desc, " +
            "  option2Desc, " +
            "  option3Desc, " +
            "  option4Desc, " +
            "  option5Desc, " +
            "  option1Budget, " +
            "  option2Budget, " +
            "  option3Budget, " +
            "  option4Budget, " +
            "  option5Budget, " +
            "  optionSequenceNo " +
            " " +
            "FROM budget " +
            "WHERE HolidayId = " + holidayId + " " +
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

    private String randomBudgetDescription()
    {
        try
        {
            Random random=new Random();
            int i=random.nextInt(7);
            switch(i)
            {
                case 0:
                    return ("Villa");
                case 1:
                    return ("Park Tickets");
                case 2:
                    return ("Flight");
                case 3:
                    return ("VIP Room");
                case 4:
                    return ("Special Event");
                case 5:
                    return ("Cruise");
                case 6:
                    return ("Photo Pass");
            }
            return ("Photo Pass");
        }
        catch(Exception e)
        {
            ShowError("randomBudgetDescription", e.getMessage());
        }
        return ("Photo Pass");

    }


    
}
