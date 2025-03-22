package com.example.des.hp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.des.hp.myutils.*;
import com.example.des.hp.Notes.*;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

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
            String lSQL="CREATE TABLE IF NOT EXISTS notes " +
                    "( " +
                    "  holidayId       INT(5),  " +
                    "  noteId          INT(5),  " +
                    "  notes           VARCHAR  " + ") ";

            db.execSQL(lSQL);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
        return (false);
    }

    public void export(OutputStreamWriter buffwriter) {

        try {
            buffwriter.write("<notes>\n");

            String lSql =
                    "select HolidayId, noteId, notes " +
                            "FROM notes " +
                            "ORDER BY holidayId, noteId";

            Cursor cursor = executeSQLOpenCursor("export", lSql);
            if (cursor == null)
                return;

            while (cursor.moveToNext()) {
                buffwriter.write(
                        cursor.getString(0) + "," +
                                cursor.getString(1) + "," +
                                stringToBase64(cursor.getString(2)) + "\n"
                );

            }

        } catch (java.io.FileNotFoundException e) {
        } catch (java.io.IOException e) {
        }
    }


    boolean addNoteItem(NoteItem noteItem)
    {
        try
        {
            if(!IsValid())
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
            if(!IsValid())
                return (false);

            String lSQL="UPDATE notes SET notes=" + MyQuotedString(noteItem.notes) + " " + "WHERE holidayId = " + noteItem.holidayId + " " + "AND noteId = " + noteItem.noteId;

            return executeSQL("updateNoteItem", lSQL);
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
            if(!IsValid())
                return (false);

            noteItem.notes="";
            String lSQL="DELETE FROM notes WHERE holidayId = " + noteItem.holidayId + " " + "AND noteId = " + noteItem.noteId;

            return executeSQL("deleteNoteItem", lSQL);
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
            if(!IsValid())
                return (false);

            String lSQL;
            lSQL="SELECT holidayId, noteId, notes " + "FROM notes " + "WHERE holidayId = " + holidayId + " " + "AND NoteId = " + noteId;

            Cursor cursor=executeSQLOpenCursor("getNoteItem", lSQL);
            if(cursor != null)
            {
                cursor.moveToFirst();
                if(!GetNoteItemFromQuery(cursor, noteItem))
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
            if(!IsValid())
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
        if(!IsValid())
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

    boolean getNextNoteId(int holidayId, MyInt retInt)
    {
        try
        {
            String lSQL="SELECT IFNULL(MAX(noteId),0) " + "FROM notes " + "WHERE holidayId = " + holidayId;

            if(!executeSQLGetInt("getNextNoteId", lSQL, retInt))
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
