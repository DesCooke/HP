package com.example.des.hp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

class TableFileIds extends TableBase
{
    TableFileIds(Context context, SQLiteOpenHelper dbHelper)
    {
        super(context, dbHelper);
    }

    public void ShowError(String argFunction, String argMessage)
    {
        super.ShowError("TableFileIds:" + argFunction, argMessage);
    }

    public boolean onCreate(SQLiteDatabase db)
    {
        try
        {
            String lSQL="CREATE TABLE IF NOT EXISTS fileIds " +
                    "( " +
                    "  fileType VARCHAR, " +
                    "  nextId   INT(5)   " +
                    ") ";
            db.execSQL(lSQL);

            lSQL="INSERT INTO fileIds " + " (fileType, nextId) " + "VALUES ('picture',1) ";
            db.execSQL(lSQL);

            lSQL="INSERT INTO fileIds " + " (fileType, nextId) " + "VALUES ('fgi',1) ";
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
            buffwriter.write("<fileIds>\n");

            String lSql =
                    "select fileType, " +
                            "nextId " +
                            "FROM fileIds " +
                            "ORDER BY fileType";

            Cursor cursor=executeSQLOpenCursor("export", lSql);
            if(cursor == null)
                return;

            while(cursor.moveToNext())
            {
                buffwriter.write(cursor.getString(0) + "," +
                        Integer.parseInt(cursor.getString(1)) + "\n"
                );
            }

        } catch (java.io.FileNotFoundException e) {
        } catch (java.io.IOException e) {
        }
    }


}
