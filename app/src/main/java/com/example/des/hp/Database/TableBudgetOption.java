package com.example.des.hp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.des.hp.Budget.BudgetItem;
import com.example.des.hp.Budget.BudgetOptionItem;
import com.example.des.hp.R;
import com.example.des.hp.myutils.MyInt;
import com.example.des.hp.myutils.MyString;

import java.util.ArrayList;
import java.util.Random;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
import static com.example.des.hp.myutils.MyMessages.myMessages;

class TableBudgetOption extends TableBase
{

    TableBudgetOption(Context context, SQLiteOpenHelper dbHelper)
    {
        super(context, dbHelper);
    }

    public void ShowError(String argFunction, String argMessage)
    {
        super.ShowError("TableBudgetOption:" + argFunction, argMessage);
    }

    public boolean onCreate(SQLiteDatabase db)
    {
        try
        {
            String lSQL=
            "CREATE TABLE IF NOT EXISTS budgetOption " +
            "( " +
            "  holidayId         INT(5),  " +
            "  budgetId          INT(5),  " +
            "  sequenceNo        INT(5),  " +
            "  optionDescription VARCHAR, " +
            "  optionTotal       INT(5)  " +
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
            if(oldVersion == 63 && newVersion == 64)
            {
                onCreate(db);
            }
            if(oldVersion == 66 && newVersion == 67)
            {
                String lSql =
                "INSERT INTO BudgetOption " +
                "  select " +
                "    holidayId, " +
                "    budgetId, " +
                "    2, " +
                "    option2Desc, " +
                "    option2Budget " +
                "  from Budget " +
                "  where option2Desc <> '' ";
                db.execSQL(lSql);

                lSql =
                "INSERT INTO BudgetOption " +
                "  select " +
                "    holidayId, " +
                "    budgetId, " +
                "    3, " +
                "    option3Desc, " +
                "    option3Budget " +
                "  from Budget " +
                "  where option3Desc <> '' ";
                db.execSQL(lSql);

                lSql =
                "INSERT INTO BudgetOption " +
                "  select " +
                "    holidayId, " +
                "    budgetId, " +
                "    4, " +
                "    option4Desc, " +
                "    option4Budget " +
                "  from Budget " +
                "  where option4Desc <> '' ";
                db.execSQL(lSql);

                lSql =
                "INSERT INTO BudgetOption " +
                "  select " +
                "    holidayId, " +
                "    budgetId, " +
                "    5, " +
                "    option5Desc, " +
                "    option5Budget " +
                "  from Budget " +
                "  where option5Desc <> '' ";
                db.execSQL(lSql);
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("onUpgrade", e.getMessage());
        }
        return (false);
    }

    boolean getBudgetOptionCount(int argholidayId, int argBudgetId, MyInt retInt)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            String lSQL=
            "SELECT IFNULL(COUNT(*),0) " +
            "FROM BudgetOption " +
            "WHERE holidayId = " + argholidayId + " " +
            "AND budgetId " + argBudgetId;

            if(executeSQLGetInt("getBudgetOptionCount", lSQL, retInt) == false)
                return (false);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("getBudgetOptionCount", e.getMessage());
        }
        return (false);

    }

    boolean addBudgetOptionItem(BudgetOptionItem budgetItem)
    {
        try
        {
            if (IsValid() == false)
                return (false);

            String lSql =
            "INSERT INTO budgetOption " +
            "  ( " +
            "    holidayId, " +
            "    budgetId, " +
            "    sequenceNo, " +
            "    optionDescription, " +
            "    optionTotal  " +
            ") " +
            "VALUES " +
            "(" +
            budgetItem.holidayId + "," +
            budgetItem.budgetId + "," +
            budgetItem.sequenceNo + ", " +
            MyQuotedString(budgetItem.optionDescription) + ", " +
            budgetItem.optionTotal + " " +
            ")";

            return (executeSQL("addBudgetOptionItem", lSql));
        }
        catch(Exception e)
        {
            ShowError("addBudgetOptionItem", e.getMessage());
        }
        return (false);

    }

    public int bool2Int(boolean thebool)
    {
        if(thebool)
            return(1);
        return(0);
    }

    boolean updateBudgetOptionItems(ArrayList<BudgetOptionItem> items)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            if(items == null)
                return (false);

            for(int i=0; i < items.size(); i++)
            {
                if(updateBudgetOptionItem(items.get(i)) == false)
                    return (false);
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("updateBudgetOptionItems", e.getMessage());
        }
        return (false);

    }

    boolean updateBudgetOptionItem(BudgetOptionItem budgetOptionItem)
    {
        try
        {
            if (IsValid() == false)
                return (false);

            String lSQL;
            lSQL =
            "UPDATE BudgetOption " +
            "SET sequenceNo = " + budgetOptionItem.sequenceNo + ", " +
            "    optionDescription = " + MyQuotedString(budgetOptionItem.optionDescription) + ", " +
            "    optionTotal = " + budgetOptionItem.optionTotal + " " +
            "WHERE rowid = " + budgetOptionItem.rowId;

            return (executeSQL("updateBudgetOptionItem", lSQL));
        }
        catch(Exception e)
        {
            ShowError("updateBudgetOptionItem", e.getMessage());
        }
        return (false);

    }


    boolean deleteBudgetOptionItem(BudgetOptionItem budgetOptionItem)
    {
        try
        {
            if (IsValid() == false)
                return (false);

            String lSQL =
            "DELETE FROM BudgetOption " +
            "WHERE holidayId = " + budgetOptionItem.holidayId + " " +
            "AND budgetId = " + budgetOptionItem.budgetId + " " +
            "AND sequenceNo = " + budgetOptionItem.sequenceNo;

            if (executeSQL("deleteBudgetOptionItem", lSQL) == false)
                return (false);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("deleteBudgetOptionItem", e.getMessage());
        }
        return (false);

    }

    boolean getBudgetOptionItem(int holidayId, int budgetId, int sequenceNo, BudgetOptionItem litem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            String lSQL;
            lSQL=
            "SELECT " +
            "  rowid, " +
            "  holidayId, " +
            "  budgetId, " +
            "  sequenceNo, " +
            "  optionDescription, " +
            "  optionTotal " +
            " " +
            "FROM budgetOption " +
            "WHERE HolidayId = " + holidayId + " " +
            "AND BudgetId = " + budgetId + " " +
            "AND SequenceNo = " + sequenceNo;

            Cursor cursor=executeSQLOpenCursor("getBudgetOptionItem", lSQL);
            if(cursor != null)
            {
                cursor.moveToFirst();
                if(GetBudgetOptionItemFromQuery(cursor, litem) == false)
                    return (false);
            }
            executeSQLCloseCursor("getBudgetOptionItem");
            return (true);
        }
        catch(Exception e)
        {
            ShowError("getBudgetOptionItem", e.getMessage());
        }
        return (false);

    }

    boolean getBudgetOptionItemFromRowId(int rowId, BudgetOptionItem litem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            String lSQL;
            lSQL=
            "SELECT " +
            "  rowid, " +
            "  holidayId, " +
            "  budgetId, " +
            "  sequenceNo, " +
            "  optionDescription, " +
            "  optionTotal " +
            " " +
            "FROM budgetOption " +
            "WHERE rowid = " + rowId;

            Cursor cursor=executeSQLOpenCursor("getBudgetOptionItem", lSQL);
            if(cursor != null)
            {
                cursor.moveToFirst();
                if(GetBudgetOptionItemFromQuery(cursor, litem) == false)
                    return (false);
            }
            executeSQLCloseCursor("getBudgetOptionItem");
            return (true);
        }
        catch(Exception e)
        {
            ShowError("getBudgetOptionItem", e.getMessage());
        }
        return (false);

    }
    private boolean GetBudgetOptionItemFromQuery(Cursor cursor, BudgetOptionItem budgetOptionItem)
    {
        if(IsValid() == false)
            return (false);

        try
        {
            if(cursor.getCount() == 0)
                return (true);

            budgetOptionItem.rowId=Integer.parseInt(cursor.getString(0));
            budgetOptionItem.holidayId=Integer.parseInt(cursor.getString(1));
            budgetOptionItem.budgetId=Integer.parseInt(cursor.getString(2));
            budgetOptionItem.sequenceNo=Integer.parseInt(cursor.getString(3));
            budgetOptionItem.optionDescription=cursor.getString(4);
            budgetOptionItem.optionTotal=Integer.parseInt(cursor.getString(5));

            return (true);
        }
        catch(Exception e)
        {
            ShowError("GetBudgetOptionItemFromQuery", e.getMessage());
        }

        return (false);
    }

    boolean getNextBudgetSequenceNo(int holidayId, int budgetId, MyInt retInt)
    {
        try
        {
            String lSQL=
            "SELECT IFNULL(MAX(SequenceNo),0) " +
            "FROM BudgetOption " +
            "WHERE holidayId = " + holidayId + " " +
            "AND budgetId = " + budgetId;

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


    boolean getBudgetOptionList(int holidayId, int budgetId, ArrayList<BudgetOptionItem> al)
    {
        try
        {
            String lSql=
            "SELECT " +
            "  rowid, " +
            "  holidayId, " +
            "  budgetId, " +
            "  sequenceNo, " +
            "  optionDescription, " +
            "  optionTotal " +
            "FROM budgetOption " +
            "WHERE HolidayId = " + holidayId + " " +
            "AND budgetId = " + budgetId + " " +
            "ORDER BY SequenceNo ";

            Cursor cursor=executeSQLOpenCursor("getBudgetOptionList", lSql);
            if(cursor == null)
                return (false);

            while(cursor.moveToNext())
            {
                BudgetOptionItem budgetOptionItem=new BudgetOptionItem();
                if(GetBudgetOptionItemFromQuery(cursor, budgetOptionItem) == false)
                    return (false);

                al.add(budgetOptionItem);
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("getBudgetOptionList", e.getMessage());
        }
        return (false);

    }
    boolean getAllOptions(ArrayList<BudgetOptionItem> al)
    {
        try
        {
            String lSql=
            "SELECT " +
            "  rowid, " +
            "  holidayId, " +
            "  budgetId, " +
            "  sequenceNo, " +
            "  optionDescription, " +
            "  optionTotal " +
            "FROM budgetOption " +
            "ORDER BY holidayId, budgetId, SequenceNo ";

            Cursor cursor=executeSQLOpenCursor("getBudgetOptionList", lSql);
            if(cursor == null)
                return (false);

            while(cursor.moveToNext())
            {
                BudgetOptionItem budgetOptionItem=new BudgetOptionItem();
                if(GetBudgetOptionItemFromQuery(cursor, budgetOptionItem) == false)
                    return (false);

                al.add(budgetOptionItem);
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("getBudgetOptionList", e.getMessage());
        }
        return (false);

    }
}
