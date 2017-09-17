package com.example.des.hp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.des.hp.myutils.*;
import com.example.des.hp.Notes.*;

import java.util.Random;

import static com.example.des.hp.myutils.MyLoremIpsum.myLoremIpsum;

class TableNotes extends TableBase
{
    TableNotes(Context context, SQLiteOpenHelper dbHelper)
    {
        super(context, dbHelper);
    }

    public void ShowError(String argFunction, String argMessage)
    {
        super.ShowError("TableNotes:" + argFunction, argMessage);
    }

    public boolean onCreate(SQLiteDatabase db)
    {
        try
        {
            String lSQL="CREATE TABLE IF NOT EXISTS notes " + "( " + "  holidayId       INT(5),  " + "  noteId          INT(5),  " + "  notes           VARCHAR  " + ") ";

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
            if(oldVersion == 38 && newVersion == 39)
            {
                String lSQL="CREATE TABLE IF NOT EXISTS notes " + "( " + "  holidayId       INT(5),  " + "  noteId          INT(5),  " + "  notes           VARCHAR  " + ") ";

                db.execSQL(lSQL);
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("onUpgrade", e.getMessage());
        }
        return (false);
    }

    boolean addNoteItem(NoteItem noteItem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            String lSql="INSERT INTO notes " + "  (holidayId, noteId, notes) " + "VALUES " + "(" + noteItem.holidayId + ", " + noteItem.noteId + "," + MyQuotedString(noteItem.notes) + " " + ")";

            return (executeSQL("addNoteItem", lSql));
        }
        catch(Exception e)
        {
            ShowError("addNoteItem", e.getMessage());
        }
        return (false);

    }

    boolean updateNoteItem(NoteItem noteItem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            String lSQL="UPDATE notes SET notes=" + MyQuotedString(noteItem.notes) + " " + "WHERE holidayId = " + noteItem.holidayId + " " + "AND noteId = " + noteItem.noteId;

            if(executeSQL("updateNoteItem", lSQL) == false)
                return (false);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("updateNoteItem", e.getMessage());
        }
        return (false);
    }

    boolean deleteNoteItem(NoteItem noteItem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            noteItem.notes="";
            String lSQL="UPDATE notes SET notes=" + MyQuotedString(noteItem.notes) + " " + "WHERE holidayId = " + noteItem.holidayId + " " + "AND noteId = " + noteItem.noteId;

            if(executeSQL("deleteNoteItem", lSQL) == false)
                return (false);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("deleteNoteItem", e.getMessage());
        }
        return (false);
    }

    boolean getNoteItem(int holidayId, int noteId, NoteItem noteItem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            String lSQL;
            lSQL="SELECT holidayId, noteId, notes " + "FROM notes " + "WHERE holidayId = " + holidayId + " " + "AND NoteId = " + noteId;

            Cursor cursor=executeSQLOpenCursor("getNoteItem", lSQL);
            if(cursor != null)
            {
                cursor.moveToFirst();
                if(GetNoteItemFromQuery(cursor, noteItem) == false)
                    return (false);
            }
            executeSQLCloseCursor("getNoteItem");
            return (true);
        }
        catch(Exception e)
        {
            ShowError("getNoteItem", e.getMessage());
        }
        return (false);
    }

    boolean noteExists(int holidayId, int noteId, MyBoolean myBoolean)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            myBoolean.Value=false;

            String lSQL;
            lSQL="SELECT holidayId, noteId, notes " + "FROM notes " + "WHERE holidayId = " + holidayId + " " + "AND NoteId = " + noteId;

            Cursor cursor=executeSQLOpenCursor("noteExists", lSQL);
            if(cursor != null)
            {
                cursor.moveToFirst();
                if(cursor.getCount() > 0)
                    myBoolean.Value=true;
            }
            executeSQLCloseCursor("noteExists");
            return (true);
        }
        catch(Exception e)
        {
            ShowError("noteExists", e.getMessage());
        }
        return (false);
    }

    private boolean GetNoteItemFromQuery(Cursor cursor, NoteItem noteItem)
    {
        if(IsValid() == false)
            return (false);

        try
        {
            if(cursor.getCount() == 0)
                return (true);

            noteItem.holidayId=Integer.parseInt(cursor.getString(0));
            noteItem.noteId=Integer.parseInt(cursor.getString(1));
            noteItem.notes=cursor.getString(2);

            noteItem.origHolidayId=noteItem.holidayId;
            noteItem.origNoteId=noteItem.noteId;
            noteItem.origNotes=noteItem.notes;

            return (true);
        }
        catch(Exception e)
        {
            ShowError("GetNoteItemFromQuery", e.getMessage());
        }
        return (false);
    }

    boolean createSample(int holidayId, MyInt myInt)
    {
        try
        {
            NoteItem item=new NoteItem();
            Random random=new Random();
            int lStart=random.nextInt(5) + 1;
            int lCount=random.nextInt(30) + 4;

            if(!getNextNoteId(holidayId, myInt))
                return (false);
            item.holidayId=holidayId;
            item.noteId=myInt.Value;
            item.notes=myLoremIpsum().getWords(lCount, lStart);
            if(!addNoteItem(item))
                return (false);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("createSample", e.getMessage());
        }
        return (false);
    }

    boolean getNextNoteId(int holidayId, MyInt retInt)
    {
        try
        {
            String lSQL="SELECT IFNULL(MAX(noteId),0) " + "FROM notes " + "WHERE holidayId = " + holidayId;

            if(executeSQLGetInt("getNextNoteId", lSQL, retInt) == false)
                return (false);
            retInt.Value=retInt.Value + 1;
            return (true);
        }
        catch(Exception e)
        {
            ShowError("getNextNoteId", e.getMessage());
        }
        return (false);
    }

}
