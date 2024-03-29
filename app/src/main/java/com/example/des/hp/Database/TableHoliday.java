package com.example.des.hp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import com.example.des.hp.Holiday.HolidayItem;
import com.example.des.hp.myutils.DateUtils;
import com.example.des.hp.myutils.MyDateDiff;
import com.example.des.hp.myutils.MyInt;
import com.example.des.hp.myutils.MyString;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
import static com.example.des.hp.myutils.MyMessages.myMessages;

class TableHoliday extends TableBase
{
    TableHoliday(Context context, SQLiteOpenHelper dbHelper)
    {
        super(context, dbHelper);
    }

    public void ShowError(String argFunction, String argMessage)
    {
        super.ShowError("TableHoliday:" + argFunction, argMessage);
    }

    public boolean onCreate(SQLiteDatabase db)
    {
        try
        {
            String lSQL=
                    "CREATE TABLE IF NOT EXISTS holiday " +
                            "( " +
                            "  holidayId      INT(5),  " +
                            "  holidayName    VARCHAR, " +
                            "  holidayPicture VARCHAR, " +
                            "  startDate      INT(16), " +
                            "  mapFileGroupId INT(5),  " +
                            "  budgetTotal    INT(5),  " +
                            "  infoId         INT(5),  " +
                            "  noteId         INT(5),  " +
                            "  galleryId      INT(5),  " +
                            "  sygicId        INT(5),  " +
                            "  buttonDays     INT(5),  " +
                            "  buttonDay      INT(5),  " +
                            "  buttonMaps     INT(5),  " +
                            "  buttonTasks    INT(5),  " +
                            "  buttonTips     INT(5),  " +
                            "  buttonBudget   INT(5),  " +
                            "  buttonAttractions INT(5),  " +
                            "  buttonContacts INT(5)   " +
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
                db.execSQL("ALTER TABLE holiday ADD COLUMN noteId INT(5) DEFAULT 0");
                db.execSQL("ALTER TABLE holiday ADD COLUMN galleryId INT(5) DEFAULT 0");
                db.execSQL("ALTER TABLE holiday ADD COLUMN sygicId INT(5) DEFAULT 0");

                db.execSQL("UPDATE holiday SET noteId = 0");
                db.execSQL("UPDATE holiday SET galleryId = 0");
                db.execSQL("UPDATE holiday SET sygicId = 0");
            }

            if(oldVersion == 42 && newVersion == 43)
            {
                db.execSQL("ALTER TABLE holiday ADD COLUMN buttonDays INT(5) DEFAULT 0");
                db.execSQL("ALTER TABLE holiday ADD COLUMN buttonDay INT(5) DEFAULT 0");
                db.execSQL("ALTER TABLE holiday ADD COLUMN buttonMaps INT(5) DEFAULT 0");
                db.execSQL("ALTER TABLE holiday ADD COLUMN buttonTasks INT(5) DEFAULT 0");
                db.execSQL("ALTER TABLE holiday ADD COLUMN buttonTips INT(5) DEFAULT 0");
                db.execSQL("ALTER TABLE holiday ADD COLUMN buttonBudget INT(5) DEFAULT 0");
                db.execSQL("ALTER TABLE holiday ADD COLUMN buttonAttractions INT(5) DEFAULT 0");
                db.execSQL("ALTER TABLE holiday ADD COLUMN buttonContacts INT(5) DEFAULT 0");

                db.execSQL("UPDATE holiday SET buttonDays = 1");
                db.execSQL("UPDATE holiday SET buttonDay = 1");
                db.execSQL("UPDATE holiday SET buttonMaps = 1");
                db.execSQL("UPDATE holiday SET buttonTasks = 1");
                db.execSQL("UPDATE holiday SET buttonTips = 1");
                db.execSQL("UPDATE holiday SET buttonBudget = 1");
                db.execSQL("UPDATE holiday SET buttonAttractions = 1");
                db.execSQL("UPDATE holiday SET buttonContacts = 1");
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("onUpgrade", e.getMessage());
        }
        return (false);
    }

    boolean addHolidayItem(HolidayItem holidayItem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            if(holidayItem.pictureAssigned)
            {
            /* if picture name has something in it - it means it came from internal folder */
                if(holidayItem.holidayPicture.length() == 0)
                {
                    //myMessages().LogMessage("  - New Image was not from internal folder...");
                    if(holidayItem.pictureAssigned)
                    {
                        //myMessages().LogMessage("  - Save new image and get a filename...");
                        MyString myString=new MyString();
                        if(savePicture(holidayItem.holidayId, holidayItem.holidayBitmap, myString) == false)
                            return (false);
                        holidayItem.holidayPicture=myString.Value;
                        //myMessages().LogMessage("  - New filename " + holidayItem.holidayPicture);
                    } else
                    {
                        //myMessages().LogMessage("  - New Image not setup - so - keep it blank");
                    }
                } else
                {
                    //myMessages().LogMessage("  - New Image was from internal folder - so just use it (" + holidayItem.holidayPicture + ")");
                }
            } else
            {
                //myMessages().LogMessage("  - New Image not assigned - do nothing");
            }

            String lSql=
                    "INSERT INTO holiday " +
                            "  (holidayName, holidayId, startDate, holidayPicture, infoId, " +
                            "   noteId, galleryId, sygicId, buttonDays, buttonDay, " +
                            "   buttonMaps, buttonTasks, buttonTips, buttonBudget, " +
                            "   buttonAttractions, buttonContacts) " +
                            "VALUES " +
                            "(" +
                            MyQuotedString(holidayItem.holidayName) + "," +
                            holidayItem.holidayId + "," +
                            holidayItem.startDateInt + "," +
                            MyQuotedString(holidayItem.holidayPicture) + ", " +
                            holidayItem.infoId + ", " +
                            holidayItem.noteId + ", " +
                            holidayItem.galleryId + ", " +
                            holidayItem.sygicId + ", " +
                            booleanToString(holidayItem.buttonDays)+ ", " +
                            booleanToString(holidayItem.buttonDay)+ ", " +
                            booleanToString(holidayItem.buttonMaps)+ ", " +
                            booleanToString(holidayItem.buttonTasks)+ ", " +
                            booleanToString(holidayItem.buttonTips)+ ", " +
                            booleanToString(holidayItem.buttonBudget)+ ", " +
                            booleanToString(holidayItem.buttonAttractions)+ ", " +
                            booleanToString(holidayItem.buttonContacts)+ " " +
                            ")";

            return (executeSQL("addHolidayItem", lSql));
        }
        catch(Exception e)
        {
            ShowError("addHolidayItem", e.getMessage());
        }
        return (false);

    }

    private String booleanToString(boolean value)
    {
        if(value)
            return("1");
        return("0");
    }
    private String randomHolidayName()
    {
        try
        {
            Random random=new Random();
            int i=random.nextInt(10);
            switch(i)
            {
                case 0:
                    return ("New York");
                case 1:
                    return ("Disney World");
                case 2:
                    return ("Italy");
                case 3:
                    return ("Africa");
                case 4:
                    return ("Australia");
                case 5:
                    return ("Blackpool");
                case 6:
                    return ("Greece");
                case 7:
                    return ("Spain");
                case 8:
                    return ("Corfu");
            }
            return ("Hawaii");
        }
        catch(Exception e)
        {
            ShowError("randomHolidayName", e.getMessage());
        }
        return ("Canada");

    }

    boolean updateHolidayItem(HolidayItem holidayItem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            //myMessages().LogMessage("updateHolidayItem:Handling Image");
            if(holidayItem.pictureChanged)
            {
                if(holidayItem.origPictureAssigned && holidayItem.holidayPicture.length() > 0 && holidayItem.holidayPicture.compareTo(holidayItem.origHolidayPicture) == 0)
                {
                    //myMessages().LogMessage("  - Original Image changed back to the original - do nothing");
                } else
                {
                    if(holidayItem.origPictureAssigned)
                    {
                        //myMessages().LogMessage("  - Original Image was assigned - need to get rid of it");
                        if(removePicture(holidayItem.holidayId, holidayItem.origHolidayPicture) == false)
                            return (false);
                    }
            
                /* if picture name has something in it - it means it came from internal folder */
                    if(holidayItem.holidayPicture.length() == 0)
                    {
                        //myMessages().LogMessage("  - New Image was not from internal folder...");
                        if(holidayItem.pictureAssigned)
                        {
                            //myMessages().LogMessage("  - Save new image and get a filename...");
                            MyString myString=new MyString();
                            if(savePicture(holidayItem.holidayId, holidayItem.holidayBitmap, myString) == false)
                                return (false);
                            holidayItem.holidayPicture=myString.Value;
                            //myMessages().LogMessage("  - New filename " + holidayItem.holidayPicture);
                        } else
                        {
                            //myMessages().LogMessage("  - New Image not setup - so - keep it blank");
                        }
                    } else
                    {
                        //myMessages().LogMessage("  - New Image was from internal folder - so just use it (" + holidayItem.holidayPicture + ")");
                    }
                }
            } else
            {
                //myMessages().LogMessage("  - Image not changed - do nothing");
            }


            String l_SQL=
                    "UPDATE holiday " +
                    "  SET holidayName = " + MyQuotedString(holidayItem.holidayName) + ", " +
                            "      holidayPicture = " + MyQuotedString(holidayItem.holidayPicture) + ", " +
                            "      startDate = " + holidayItem.startDateInt + ", " +
                            "      mapFileGroupId = " + holidayItem.mapFileGroupId + ", " +
                            "      infoId = " + holidayItem.infoId + ", " +
                            "      noteId = " + holidayItem.noteId + ", " +
                            "      galleryId = " + holidayItem.galleryId + ", " +
                            "      sygicId = " + holidayItem.sygicId + ", " +
                            "      buttonDays = " + booleanToString(holidayItem.buttonDays) + ", " +
                            "      buttonDay = " + booleanToString(holidayItem.buttonDay) + ", " +
                            "      buttonMaps = " + booleanToString(holidayItem.buttonMaps) + ", " +
                            "      buttonTasks = " + booleanToString(holidayItem.buttonTasks) + ", " +
                            "      buttonTips = " + booleanToString(holidayItem.buttonTips) + ", " +
                            "      buttonBudget = " + booleanToString(holidayItem.buttonBudget) + ", " +
                            "      buttonAttractions = " + booleanToString(holidayItem.buttonAttractions) + ", " +
                            "      buttonContacts = " + booleanToString(holidayItem.buttonContacts) + " " +
                            "WHERE holidayId = " + holidayItem.holidayId;
            return (executeSQL("updateHolidayItem", l_SQL));
        }
        catch(Exception e)
        {
            ShowError("updateHolidayItem", e.getMessage());
        }
        return (false);

    }

    boolean deleteHolidayItem(HolidayItem holidayItem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            String l_SQL="DELETE FROM holiday WHERE holidayId = " + holidayItem.holidayId;

            if(holidayItem.holidayPicture.length() > 0)
                if(removePicture(holidayItem.holidayId, holidayItem.holidayPicture) == false)
                    return (false);

            if(executeSQL("deleteHolidayItem", l_SQL) == false)
                return (false);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("deleteHolidayItem", e.getMessage());
        }
        return (false);

    }

    boolean clearNote(int holidayId, int noteId)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            String l_SQL="UPDATE holiday SET noteId = 0 " + "WHERE holidayId = " + holidayId + " " + "AND noteId = " + noteId;

            if(executeSQL("clearNote", l_SQL) == false)
                return (false);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("clearNote", e.getMessage());
        }
        return (false);

    }

    boolean getHolidayItem(int id, HolidayItem holidayItem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            String lSQL;
            lSQL=
                    "SELECT HolidayId, HolidayName, HolidayPicture, StartDate, mapFileGroupId, infoId, " +
                    " noteId, galleryId, sygicId, buttonDays, buttonDay, buttonMaps, " +
                    " buttonTasks, buttonTips, buttonBudget, buttonAttractions, buttonContacts " +
                    "FROM Holiday " +
                    "WHERE HolidayId = " + id + " ";

            Cursor cursor=executeSQLOpenCursor("getHolidayItem", lSQL);
            if(cursor != null)
            {
                cursor.moveToFirst();
                if(GetHolidayItemFromQuery(cursor, holidayItem) == false)
                    return (false);
            }
            executeSQLCloseCursor("getHolidayItem");
            return (true);
        }
        catch(Exception e)
        {
            ShowError("getHolidayItem", e.getMessage());
        }
        return (false);

    }

    private boolean GetHolidayItemFromQuery(Cursor cursor, HolidayItem retHolidayItem)
    {
        if(IsValid() == false)
            return (false);

        try
        {
            if(cursor.getCount() == 0)
                return (true);

            retHolidayItem.holidayId=Integer.parseInt(cursor.getString(0));
            retHolidayItem.holidayName=cursor.getString(1);
            retHolidayItem.holidayPicture=cursor.getString(2);
            retHolidayItem.startDateInt=Long.parseLong(cursor.getString(3));
            retHolidayItem.mapFileGroupId=0;
            if(cursor.getString(4) != null)
                retHolidayItem.mapFileGroupId=Integer.parseInt(cursor.getString(4));
            if(_dateUtils.IntToDate(retHolidayItem.startDateInt, retHolidayItem.startDateDate) == false)
                return (false);

            MyString myString=new MyString();
            if(_dateUtils.DateToStr(retHolidayItem.startDateDate, myString) == false)
                return (false);
            retHolidayItem.startDateStr=myString.Value;

            retHolidayItem.dateKnown=retHolidayItem.startDateInt != DateUtils.unknownDate;
            retHolidayItem.infoId=Integer.parseInt(cursor.getString(5));
            retHolidayItem.noteId=Integer.parseInt(cursor.getString(6));
            retHolidayItem.galleryId=Integer.parseInt(cursor.getString(7));
            retHolidayItem.sygicId=Integer.parseInt(cursor.getString(8));
            retHolidayItem.buttonDays=booleanFromString(cursor.getString(9));
            retHolidayItem.buttonDay=booleanFromString(cursor.getString(10));
            retHolidayItem.buttonMaps=booleanFromString(cursor.getString(11));
            retHolidayItem.buttonTasks=booleanFromString(cursor.getString(12));
            retHolidayItem.buttonTips=booleanFromString(cursor.getString(13));
            retHolidayItem.buttonBudget=booleanFromString(cursor.getString(14));
            retHolidayItem.buttonAttractions=booleanFromString(cursor.getString(15));
            retHolidayItem.buttonContacts=booleanFromString(cursor.getString(16));
            retHolidayItem.pictureChanged=false;


            retHolidayItem.origHolidayId=retHolidayItem.holidayId;
            retHolidayItem.origHolidayName=retHolidayItem.holidayName;
            retHolidayItem.origHolidayPicture=retHolidayItem.holidayPicture;
            retHolidayItem.origStartDateInt=retHolidayItem.startDateInt;
            retHolidayItem.origMapFileGroupId=retHolidayItem.mapFileGroupId;
            retHolidayItem.origStartDateDate=retHolidayItem.startDateDate;
            retHolidayItem.origStartDateStr=retHolidayItem.startDateStr;
            retHolidayItem.origDateKnown=retHolidayItem.dateKnown;
            retHolidayItem.origInfoId=retHolidayItem.infoId;
            retHolidayItem.origNoteId=retHolidayItem.noteId;
            retHolidayItem.origGalleryId=retHolidayItem.galleryId;
            retHolidayItem.origSygicId=retHolidayItem.sygicId;
            retHolidayItem.origButtonDays=retHolidayItem.buttonDays;
            retHolidayItem.origButtonDay=retHolidayItem.buttonDay;
            retHolidayItem.origButtonMaps=retHolidayItem.buttonMaps;
            retHolidayItem.origButtonTasks=retHolidayItem.buttonTasks;
            retHolidayItem.origButtonTips=retHolidayItem.buttonTips;
            retHolidayItem.origButtonBudget=retHolidayItem.buttonBudget;
            retHolidayItem.origButtonAttractions=retHolidayItem.buttonAttractions;
            retHolidayItem.origButtonContacts=retHolidayItem.buttonContacts;

            Bitmap bitmap=null;

            int currentHolidayId=retHolidayItem.holidayId;

            if(retHolidayItem.holidayPicture.length() > 0)
            {
                retHolidayItem.pictureAssigned=true;
                retHolidayItem.origPictureAssigned=true;
            } else
            {
                retHolidayItem.pictureAssigned=false;
                retHolidayItem.origPictureAssigned=false;
            }


            Date lToday=new Date();
            if(_dateUtils.GetToday(lToday) == false)
                return (false);

            MyInt myInt=new MyInt();

            String lSuffix=" ago";
            if(retHolidayItem.startDateDate.getTime() > lToday.getTime())
                lSuffix=" to go";

            MyDateDiff myDateDiff=new MyDateDiff();
            if(_dateUtils.GetDiff(lToday, new Date(retHolidayItem.startDateDate.getTime()), myDateDiff) == false)
                return (false);
            int lYear=myDateDiff.year;
            int lMonth=myDateDiff.month;
            int lDay=myDateDiff.day;

            String lString="";
            if(lYear != 0 || lMonth != 0 || lDay != 0)
            {
                if(lYear != 0)
                {
                    if(lYear == 1)
                    {
                        lString=lYear + " year";
                    } else
                    {
                        lString=lYear + " years";
                    }
                }
                if(lString.length() > 0 || lMonth != 0)
                {
                    if(lString.length() > 0)
                        lString=lString + ", ";

                    if(lMonth == 1)
                    {
                        lString=lString + lMonth + " month";
                    } else
                    {
                        lString=lString + lMonth + " months";
                    }
                }
                if(lString.length() > 0 || lDay != 0)
                {
                    if(lString.length() > 0)
                        lString=lString + ", ";

                    if(lDay == 1)
                    {
                        lString=lString + lDay + " day";
                    } else
                    {
                        lString=lString + lDay + " days";
                    }
                }
                lString=lString + lSuffix;
            } else
            {
                lString="*** OFF ON OUR HOLS TODAY ***";
            }

            retHolidayItem.ToGo=lString;

            retHolidayItem.holidayBitmap=null;
            return (true);
        }
        catch(Exception e)
        {
            ShowError("GetHolidayItemFromQuery", e.getMessage());
            return (false);
        }
    }

    boolean booleanFromString(String value)
    {
        if(value.compareTo("0")==0)
            return(false);
        return(true);
    }
    boolean getNextHolidayId(MyInt retInt)
    {
        try
        {
            String lSQL;

            lSQL="SELECT IFNULL(MAX(holidayId),0) FROM holiday";

            if(executeSQLGetInt("getNextHolidayId", lSQL, retInt) == false)
                return (false);
            retInt.Value=retInt.Value + 1;
            return (true);
        }
        catch(Exception e)
        {
            ShowError("getNextHolidayId", e.getMessage());
        }
        return (false);

    }

    boolean getHolidayList(ArrayList<HolidayItem> retAl)
    {
        try
        {
            String lSql=
                    "SELECT holidayId, holidayName, holidayPicture, startDate, mapFileGroupId, infoId, " +
                    " noteId, galleryId, sygicId, buttonDays, buttonDay, buttonMaps, buttonTasks, " +
                    " buttonTips, buttonBudget, buttonAttractions, buttonContacts " +
                    "FROM holiday " +
                    "ORDER BY startDate DESC";

            Cursor cursor=executeSQLOpenCursor("getHolidayList", lSql);
            if(cursor == null)
                return (false);

            while(cursor.moveToNext())
            {
                HolidayItem holidayItem=new HolidayItem();
                if(GetHolidayItemFromQuery(cursor, holidayItem) == false)
                    return (false);

                retAl.add(holidayItem);
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("getHolidayList", e.getMessage());
        }
        return (false);
    }


}
