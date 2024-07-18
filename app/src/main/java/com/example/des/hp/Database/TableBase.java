package com.example.des.hp.Database;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.example.des.hp.R;
import com.example.des.hp.myutils.DateUtils;
import com.example.des.hp.myutils.ImageUtils;
import com.example.des.hp.myutils.MyFileUtils;
import com.example.des.hp.myutils.MyInt;
import com.example.des.hp.myutils.MyLong;
import com.example.des.hp.myutils.MyString;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Random;

import static com.example.des.hp.myutils.DateUtils.dateUtils;
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
        try
        {
            if(_context == null)
                return (false);
            if(_dbHelper == null)
                return (false);
            if(_myFileUtils == null)
                return (false);
            return (true);
        }
        catch(Exception e)
        {
            ShowError("IsValid", e.getMessage());
        }
        return (false);

    }

    String randomPictureName()
    {
        try
        {
            Random random=new Random();
            int fileid=random.nextInt(5) + 1;
            return ("pic_" + String.valueOf(fileid) + ".png");
        }
        catch(Exception e)
        {
            ShowError("randomPictureName", e.getMessage());
        }
        return ("");

    }

    String randomFileName()
    {
        try
        {
            Random random=new Random();
            int fileid=random.nextInt(5) + 1;
            return ("testfile_" + String.valueOf(fileid) + ".txt");
        }
        catch(Exception e)
        {
            ShowError("randomFileName", e.getMessage());
        }
        return ("");

    }

    private String CleanString(String argString)
    {
        if(argString.length() == 0)
            return ("");

        return (argString.replace("'", "''"));
    }

    public String MyQuotedString(String argString)
    {
        if(argString == null)
            return ("''");

        if(argString.length() == 0)
            return ("''");

        String lCleanString=CleanString(argString);

        return ("'" + lCleanString + "'");
    }

    public void ShowError(String argFunction, String argMessage)
    {
        myMessages().ShowError("Error in DatabaseAccess::" + argFunction, argMessage);
    }

    long randomDateInt()
    {
        try
        {
            Date today=new Date();
            MyLong myLong=new MyLong();
            Random random=new Random();
            int i=random.nextInt(100);
            int addDays=random.nextInt(100) + 5;
            if(!dateUtils().GetToday(today))
                return (DateUtils.unknownDate);

            if(!dateUtils().DateToInt(today, myLong))
                return (DateUtils.unknownDate);
            return (myLong.Value + (addDays * DateUtils.milliSecondsInADay));
        }
        catch(Exception e)
        {
            ShowError("randomDateInt", e.getMessage());
        }
        return (0);

    }

    Bitmap randomPicture()
    {
        try
        {
            int pictureId;
            Random random=new Random();
            int sample=random.nextInt(5);
            pictureId=R.drawable.sample1;
            switch(sample)
            {
                case 0:
                    pictureId=R.drawable.sample2;
                    break;
                case 1:
                    pictureId=R.drawable.sample3;
                    break;
                case 2:
                    pictureId=R.drawable.sample4;
                    break;
                case 3:
                    pictureId=R.drawable.sample5;
                    break;
            }

            return (BitmapFactory.decodeResource(_resources, pictureId));
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
        return (null);

    }


    // returns true/false
    public boolean removePicture(int holidayId, String argFilename)
    {
        try
        {
            //myMessages().LogMessage("  - removePicture " + argFilename);
            if(argFilename.length() == 0)
                return (true);

            int lUsageCount=totalUsageCount(argFilename);
            //myMessages().LogMessage("  Total Usage Count " + String.valueOf(lUsageCount));

            File file=new File(ImageUtils.imageUtils().GetHolidayImageDir(holidayId) + "/" + argFilename);
            if(lUsageCount < 2)
            {
                if(file.exists())
                {
                    //myMessages().LogMessage("  Deleting " + file.getAbsolutePath());

                    if(!file.delete())
                        throw new Exception("unable to delete " + file.getAbsolutePath());
                } else
                {
                    //myMessages().LogMessage("  Does not seem to exist!... " + file.getAbsolutePath());
                }
            } else
            {
                //myMessages().LogMessage("  Not deleting - something else appears to use it" + file.getAbsolutePath());
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("removePicture", e.getMessage());
        }
        return (false);
    }

    private int pictureUsageCount(String argFilename, String argTable, String argField)
    {
        try
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

            //myMessages().LogMessage("  Count of " + argFilename + " in " + argTable + ": " + String.valueOf(lCount));

            executeSQLCloseCursor("pictureUsageCount");
            return (lCount);
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
        return (0);

    }

    int totalUsageCount(String argFilename)
    {
        try
        {
            int lCount=0;
            lCount+=pictureUsageCount(argFilename, "Attraction", "attractionPicture");
            lCount+=pictureUsageCount(argFilename, "AttractionArea", "attractionAreaPicture");
            lCount+=pictureUsageCount(argFilename, "Budget", "budgetPicture");
            lCount+=pictureUsageCount(argFilename, "Contact", "contactPicture");
            lCount+=pictureUsageCount(argFilename, "Day", "dayPicture");
            lCount+=pictureUsageCount(argFilename, "ExtraFiles", "filePicture");
            lCount+=pictureUsageCount(argFilename, "Holiday", "holidayPicture");
            lCount+=pictureUsageCount(argFilename, "Schedule", "schedPicture");
            lCount+=pictureUsageCount(argFilename, "Tasks", "taskPicture");
            lCount+=pictureUsageCount(argFilename, "Tip", "tipPicture");
            lCount+=pictureUsageCount(argFilename, "TipGroup", "tipGroupPicture");
            return (lCount);
        }
        catch(Exception e)
        {
            ShowError("totalUsageCount", e.getMessage());
        }
        return (0);

    }

    int fileUsageCount(int holidayId, String argFilename)
    {
        try
        {
            int lCount=0;
            String lSQL;


            lSQL="SELECT COUNT(*) " + "FROM extraFiles " +
                    "WHERE holidayId = " + holidayId + " " +
                    "AND fileName = " + MyQuotedString(argFilename) + " ";

            Cursor cursor=executeSQLOpenCursor("fileUsageCount", lSQL);
            if(cursor != null)
            {
                cursor.moveToFirst();
                lCount=Integer.parseInt(cursor.getString(0));
            }

//            myMessages().LogMessage("  Count of " + argFilename + " in extraFiles: " + String.valueOf(lCount));

            executeSQLCloseCursor("fileUsageCount");
            return (lCount);
        }
        catch(Exception e)
        {
            ShowError("fileUsageCount", e.getMessage());
        }
        return (0);

    }


    // returns true/false
    boolean removeExtraFile(int holidayId, String argFilename)
    {
        try
        {
            if(argFilename.length() == 0)
                return (true);

            if(fileUsageCount(holidayId, argFilename) < 2)
            {
                File file=new File(ImageUtils.imageUtils().GetHolidayFileDir(holidayId) + "/" + argFilename);
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
    private boolean saveImageToInternalStorage(int holidayId, Bitmap image, String argFilename)
    {
        FileOutputStream out;
        try
        {
            out=new FileOutputStream(ImageUtils.imageUtils().GetHolidayImageDir(holidayId) + "/" + argFilename);
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
        }
        return (false);
    }

    boolean saveExtraFile(int holidayId, Uri uri, String newName)
    {
        try
        {
            return (_myFileUtils.CopyFileToLocalDir(holidayId, uri, newName));
        }
        catch(Exception e)
        {
            ShowError("saveExtraFile", e.getMessage());
        }
        return (false);
    }

    boolean createExtraFileSample(String newName)
    {
        try
        {
            return (_myFileUtils.createSample(newName));
        }
        catch(Exception e)
        {
            ShowError("createExtraFileSample", e.getMessage());
        }
        return (false);

    }

    // returns true/false
    public boolean savePicture(int holidayId, Bitmap bm, MyString retString)
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

            if(saveImageToInternalStorage(holidayId, bm, lFilename) == false)
                return (false);

            retString.Value=lFilename;
            return (true);
        }
        catch(Exception e)
        {
            ShowError("savePicture", e.getMessage());
        }
        return (false);
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

            //myMessages().LogMessage("executeSQLGetInt from " + argFunction + ":" + argSql);

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

            //myMessages().LogMessage("  return value: " + retInt.Value);

            return (true);
        }
        catch(Exception e)
        {
            ShowError(argFunction, e.getMessage());
        }
        return (false);
    }

    public boolean executeSQL(String argFunction, String argSql)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            SQLiteDatabase db=_dbHelper.getWritableDatabase();

            //myMessages().LogMessage("executeSQL from " + argFunction + ":" + argSql);

            db.execSQL(argSql);

            db.close();
            return (true);
        }
        catch(Exception e)
        {
            ShowError(argFunction, e.getMessage());
        }
        return (false);
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

            //myMessages().LogMessage("executeSQLOpenCursor from " + argFunction + ":" + argSql);

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
        }
        return (null);
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
        }
        return (false);
    }
    //endregion

    // gets next file id for particular file type - record must always exist
    // returns true/false
    boolean getNextFileId(String argType, MyInt retInt)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            String lSQL;
            lSQL="SELECT nextId " + "FROM fileIds " + "WHERE fileType = '" + argType + "' ";

            if(executeSQLGetInt("getNextFileId", lSQL, retInt) == false)
                return (false);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("getNextFileId", e.getMessage());
        }
        return (false);

    }

    boolean setNextFileId(String argType, int argNextFileId)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            String lSQL;
            lSQL="UPDATE fileIds " + "SET nextId = " + argNextFileId + " " + "WHERE fileType='" + argType + "'";

            return (executeSQL("setNextFileId", lSQL));
        }
        catch(Exception e)
        {
            ShowError("setNextFileId", e.getMessage());
        }
        return (false);

    }

}
