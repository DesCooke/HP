package com.example.des.hp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.des.hp.myutils.MyMessages;

class TableHighlight  extends TableBase
{
    TableHighlight(Context context, SQLiteOpenHelper dbHelper)
    {
        super(context, dbHelper);
    }

    public void ShowError(String argFunction, String argMessage)
    {
        super.ShowError("TableHighlight:"+argFunction, argMessage);
    }

    public boolean onCreate(SQLiteDatabase db)
    {
        try
        {
            String lSQL="CREATE TABLE IF NOT EXISTS highlight " +
                "( " +
                "  holidayId        INT(5),  " +
                "  dayId            INT(5),  " +
                "  highlightGroupId INT(5),  " +
                "  highlightId      INT(5),  " +
                "  sequenceNo       INT(5),  " +
                "  highlightType    INT(5),  " +
                "  highlightName    VARCHAR, " +
                "  highlightPicture VARCHAR, " +
                "  infoId           INT(5),  " +
                "  noteId           INT(5),  " +
                "  galleryId        INT(5),  " +
                "  sygicId          INT(5)   " +
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
                db.execSQL("ALTER TABLE highlight ADD COLUMN noteId INT(5) DEFAULT 0");
                db.execSQL("ALTER TABLE highlight ADD COLUMN galleryId INT(5) DEFAULT 0");
                db.execSQL("ALTER TABLE highlight ADD COLUMN sygicId INT(5) DEFAULT 0");

                db.execSQL("UPDATE highlight SET noteId = 0");
                db.execSQL("UPDATE highlight SET galleryId = 0");
                db.execSQL("UPDATE highlight SET sygicId = 0");
            }
            return(true);
        }
        catch(Exception e)
        {
            ShowError("onUpgrade", e.getMessage());
            return(false);
        }
    }


}
