package com.example.des.hp.Database;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.net.Uri;

import com.example.des.hp.R;
import com.example.des.hp.myutils.DateUtils;
import com.example.des.hp.myutils.MyFileUtils;
import com.example.des.hp.myutils.MyInt;
import com.example.des.hp.myutils.MyString;

import java.io.File;
import java.io.FileOutputStream;

import static com.example.des.hp.myutils.MyMessages.myMessages;

public class TableBase
{
    public Context _context;
    private SQLiteOpenHelper _dbHelper;
    Resources _resources;
    DateUtils _dateUtils;
    private MyFileUtils _myFileUtils;


    private SQLiteDatabase currentDB;
    private Cursor currentCursor;

    //region CONSTRUCTORS
    public TableBase(Context context, SQLiteOpenHelper dbHelper)
    {
        _context=context;
        _dbHelper=dbHelper;
        _resources=_context.getResources();
        _dateUtils=new DateUtils(_context);
        _myFileUtils=new MyFileUtils(_context);
    }
    //endregion

    //region HELPER FUNCTIONS
    public boolean IsValid()
    {
        if(_context == null)
            return (false);
        if(_dbHelper == null)
            return (false);
        if(_myFileUtils == null)
            return (false);
        return (true);
    }

    private String CleanString(String argString)
    {
        if(argString.length() == 0)
            return ("");

        return (argString.replace("'", "''"));
    }

    public String MyQuotedString(String argString)
    {
        if(argString.length() == 0)
            return ("''");

        String lCleanString=CleanString(argString);

        return ("'" + lCleanString + "'");
    }

    public void ShowError(String argFunction, String argMessage)
    {
        myMessages().ShowError("Error in DatabaseAccess::" + argFunction, argMessage);
    }

    // returns true/false
    public boolean removePicture(String argFilename)
    {
        try
        {
            myMessages().LogMessage("  - removePicture " + argFilename);
            if(argFilename.length() == 0)
                return (true);

            int lUsageCount=totalUsageCount(argFilename);
            myMessages().LogMessage("  Total Usage Count " + String.valueOf(lUsageCount));

            File file=new File(_resources.getString(R.string.picture_path) + "/" + argFilename);
            if(lUsageCount < 2)
            {
                if(file.exists())
                {
                    myMessages().LogMessage("  Deleting " + file.getAbsolutePath());

                    if(!file.delete())
                        throw new Exception("unable to delete " + file.getAbsolutePath());
                } else
                {
                    myMessages().LogMessage("  Does not seem to exist!... " + file.getAbsolutePath());
                }
            } else
            {
                myMessages().LogMessage("  Not deleting - something else appears to use it" + file.getAbsolutePath());
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("removePicture", e.getMessage());
            return (false);
        }
    }

    private int pictureUsageCount(String argFilename, String argTable, String argField)
    {
        int lCount=0;
        String lSQL;


        lSQL="SELECT COUNT(*) " + "FROM " + argTable + " " + "WHERE " + argField + " = '" + argFilename + "' ";

        Cursor cursor=executeSQLOpenCursor("pictureUsageCount", lSQL);
        if(cursor != null)
        {
            cursor.moveToFirst();
            lCount=Integer.parseInt(cursor.getString(0));
        }

        myMessages().LogMessage("  Count of " + argFilename + " in " + argTable + ": " + String.valueOf(lCount));

        executeSQLCloseCursor("pictureUsageCount");
        return (lCount);
    }

    int totalUsageCount(String argFilename)
    {
        int lCount=0;
        lCount+=pictureUsageCount(argFilename, "Attraction", "attractionPicture");
        lCount+=pictureUsageCount(argFilename, "AttractionArea", "attractionAreaPicture");
        lCount+=pictureUsageCount(argFilename, "Budget", "budgetPicture");
        lCount+=pictureUsageCount(argFilename, "Contact", "contactPicture");
        lCount+=pictureUsageCount(argFilename, "Day", "dayPicture");
        lCount+=pictureUsageCount(argFilename, "ExtraFiles", "filePicture");
        lCount+=pictureUsageCount(argFilename, "Highlight", "highlightPicture");
        lCount+=pictureUsageCount(argFilename, "HighlightGroup", "highlightGroupPicture");
        lCount+=pictureUsageCount(argFilename, "Holiday", "holidayPicture");
        lCount+=pictureUsageCount(argFilename, "Schedule", "schedPicture");
        lCount+=pictureUsageCount(argFilename, "Tasks", "taskPicture");
        lCount+=pictureUsageCount(argFilename, "Tip", "tipPicture");
        lCount+=pictureUsageCount(argFilename, "TipGroup", "tipGroupPicture");
        return (lCount);
    }

    int fileUsageCount(String argFilename)
    {
        int lCount=0;
        String lSQL;


        lSQL="SELECT COUNT(*) " + "FROM extraFiles " + "WHERE fileName = " + MyQuotedString(argFilename) + " ";

        Cursor cursor=executeSQLOpenCursor("fileUsageCount", lSQL);
        if(cursor != null)
        {
            cursor.moveToFirst();
            lCount=Integer.parseInt(cursor.getString(0));
        }

        myMessages().LogMessage("  Count of " + argFilename + " in extraFiles: " + String.valueOf(lCount));

        executeSQLCloseCursor("fileUsageCount");
        return (lCount);
    }


    // returns true/false
    boolean removeExtraFile(String argFilename)
    {
        try
        {
            if(argFilename.length() == 0)
                return (true);

            if(fileUsageCount(argFilename) < 2)
            {
                File file=new File(_resources.getString(R.string.files_path) + "/" + argFilename);
                if(file.exists())
                    if(!file.delete())
                        throw new Exception("unable to delete " + file.getAbsolutePath());
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("removeExtraFile", e.getMessage());
            return (false);
        }
    }

    // returns true/false
    private boolean saveImageToInternalStorage(Bitmap image, String argFilename)
    {
        FileOutputStream out;
        try
        {
            out=new FileOutputStream(_resources.getString(R.string.picture_path) + "/" + argFilename);
            image.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
        }
        catch(Exception e)
        {
            ShowError("saveImageToInternalStorage-1", e.getMessage());
            return (false);
        }
        try
        {
            out.close();

            return (true);
        }
        catch(Exception e)
        {
            ShowError("saveImageToInternalStorage-2", e.getMessage());
            return (false);
        }
    }

    boolean saveExtraFile(Uri uri, String newName)
    {

        return (_myFileUtils.CopyFileToLocalDir(uri, newName));
    }

    // returns true/false
    public boolean savePicture(Bitmap bm, MyString retString)
    {
        try
        {
            String lFilename;
            MyInt myInt=new MyInt();
            if(getNextFileId("picture", myInt) == false)
                return (false);

            if(setNextFileId("picture", myInt.Value + 1) == false)
                return (false);

            lFilename="pic_" + myInt.Value + ".png";

            if(saveImageToInternalStorage(bm, lFilename) == false)
                return (false);

            retString.Value=lFilename;
            return (true);
        }
        catch(Exception e)
        {
            ShowError("savePicture", e.getMessage());
            return (false);
        }
    }

    //endregion

    //region ACTUAL DB SQL FUNCTIONS
    public boolean executeSQLGetInt(String argFunction, String argSql, MyInt retInt)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            SQLiteDatabase db=_dbHelper.getReadableDatabase();

            myMessages().LogMessage("executeSQLGetInt from " + argFunction + ":" + argSql);

            Cursor cursor=db.rawQuery(argSql, new String[]{});
            if(cursor == null)
                return (false);

            cursor.moveToFirst();
            if(cursor.getCount() == 0)
                return (true);

            String str=cursor.getString(0);
            cursor.close();
            db.close();
            retInt.Value=Integer.parseInt(str);

            myMessages().LogMessage("  return value: " + retInt.Value);

            return (true);
        }
        catch(Exception e)
        {
            ShowError(argFunction, e.getMessage());
            return (false);
        }
    }

    public boolean executeSQL(String argFunction, String argSql)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            SQLiteDatabase db=_dbHelper.getWritableDatabase();

            myMessages().LogMessage("executeSQL from " + argFunction + ":" + argSql);

            db.execSQL(argSql);

            db.close();
            return (true);
        }
        catch(Exception e)
        {
            ShowError(argFunction, e.getMessage());
            return (false);
        }
    }

    public Cursor executeSQLOpenCursor(String argFunction, String argSql)
    {
        if(IsValid() == false)
            return (null);

        currentDB=null;
        currentCursor=null;
        try
        {
            SQLiteDatabase db=_dbHelper.getReadableDatabase();

            myMessages().LogMessage("executeSQLOpenCursor from " + argFunction + ":" + argSql);

            Cursor cursor=db.rawQuery(argSql, new String[]{});
            if(cursor == null)
                return (null);

            currentDB=db;
            currentCursor=cursor;

            return (cursor);
        }
        catch(Exception e)
        {
            ShowError(argFunction, e.getMessage());
            return (null);
        }
    }

    public boolean executeSQLCloseCursor(String argFunction)
    {
        if(IsValid() == false)
            return (false);

        try
        {
            if(currentCursor != null)
                currentCursor.close();
            if(currentDB != null)
                currentDB.close();
            return (true);
        }
        catch(Exception e)
        {
            ShowError(argFunction, e.getMessage());
            return (false);
        }
    }
    //endregion

    // gets next file id for particular file type - record must always exist
    // returns true/false
    boolean getNextFileId(String argType, MyInt retInt)
    {
        if(IsValid() == false)
            return (false);

        String lSQL;
        lSQL="SELECT nextId " + "FROM fileIds " + "WHERE fileType = '" + argType + "' ";

        if(executeSQLGetInt("getNextFileId", lSQL, retInt) == false)
            return (false);

        return (true);
    }

    boolean setNextFileId(String argType, int argNextFileId)
    {
        if(IsValid() == false)
            return (false);

        String lSQL;
        lSQL="UPDATE fileIds " + "SET nextId = " + argNextFileId + " " + "WHERE fileType='" + argType + "'";

        return (executeSQL("setNextFileId", lSQL));
    }

}
