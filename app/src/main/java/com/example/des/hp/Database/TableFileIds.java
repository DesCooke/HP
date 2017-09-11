package com.example.des.hp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
            String lSQL="CREATE TABLE IF NOT EXISTS fileIds " + "( " + "  fileType VARCHAR, " + "  nextId   INT(5)   " + ") ";
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
            return (false);
        }
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
            return (false);
        }
    }


}
