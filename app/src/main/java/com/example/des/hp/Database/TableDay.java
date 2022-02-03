package com.example.des.hp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.des.hp.Day.DayItem;
import com.example.des.hp.R;
import com.example.des.hp.myutils.MyInt;
import com.example.des.hp.myutils.MyString;

import java.util.ArrayList;
import java.util.Random;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
import static com.example.des.hp.myutils.MyMessages.myMessages;
import static java.lang.Math.abs;

class TableDay extends TableBase
{
    TableDay(Context context, SQLiteOpenHelper dbHelper)
    {
        super(context, dbHelper);
    }

    public void ShowError(String argFunction, String argMessage)
    {
        super.ShowError("TableDay:" + argFunction, argMessage);
    }

    public boolean onCreate(SQLiteDatabase db)
    {
        try
        {
            String lSQL="CREATE TABLE IF NOT EXISTS day " + "( " + "  holidayId  INT(5),  " + "  dayId      INT(5),  " + "  sequenceNo INT(5),  " + "  dayName    VARCHAR, " + "  dayPicture VARCHAR, " + "  dayCat     INT(5),  " + "  infoId     INT(5),  " + "  noteId     INT(5),  " + "  galleryId  INT(5),  " + "  sygicId    INT(5)   " + ") ";

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
                db.execSQL("ALTER TABLE day ADD COLUMN noteId INT(5) DEFAULT 0");
                db.execSQL("ALTER TABLE day ADD COLUMN galleryId INT(5) DEFAULT 0");
                db.execSQL("ALTER TABLE day ADD COLUMN sygicId INT(5) DEFAULT 0");

                db.execSQL("UPDATE day SET noteId = 0");
                db.execSQL("UPDATE day SET galleryId = 0");
                db.execSQL("UPDATE day SET sygicId = 0");
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("onUpgrade", e.getMessage());
        }
        return (false);
    }

    boolean getDayCount(int argHolidayId, MyInt retInt)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            String lSQL;
            lSQL="SELECT COUNT(*) " + "FROM Day " + "WHERE HolidayId = " + argHolidayId + " ";

            return (executeSQLGetInt("getDayCount", lSQL, retInt));
        }
        catch(Exception e)
        {
            ShowError("getDayCount", e.getMessage());
        }
        return (false);

    }

    boolean addDayItem(DayItem dayItem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            if(dayItem.pictureAssigned)
            {
            /* if picture name has something in it - it means it came from internal folder */
                if(dayItem.dayPicture.length() == 0)
                {
                    //myMessages().LogMessage("  - New Image was not from internal folder...");
                    if(dayItem.pictureAssigned)
                    {
                        //myMessages().LogMessage("  - Save new image and get a filename...");
                        MyString myString=new MyString();
                        if(savePicture(dayItem.holidayId, dayItem.dayBitmap, myString) == false)
                            return (false);
                        dayItem.dayPicture=myString.Value;
                        //myMessages().LogMessage("  - New filename " + dayItem.dayPicture);
                    } else
                    {
                        //myMessages().LogMessage("  - New Image not setup - so - keep it blank");
                    }
                } else
                {
                    //myMessages().LogMessage("  - New Image was from internal folder - so just use it (" + dayItem.dayPicture + ")");
                }
            } else
            {
                //myMessages().LogMessage("  - New Image not assigned - do nothing");
            }

            String lSql="INSERT INTO day " + "  (holidayId, dayId, dayName, dayPicture, sequenceNo, dayCat, infoId, " + "   noteId, galleryId, sygicId) " + "VALUES " + "(" + dayItem.holidayId + "," + dayItem.dayId + "," + MyQuotedString(dayItem.dayName) + "," + MyQuotedString(dayItem.dayPicture) + "," + dayItem.sequenceNo + ", " + dayItem.dayCat + ", " + dayItem.infoId + ", " + dayItem.noteId + ", " + dayItem.galleryId + ", " + dayItem.sygicId + " " + ")";

            return (executeSQL("addDayItem", lSql));
        }
        catch(Exception e)
        {
            ShowError("addDayItem", e.getMessage());
        }
        return (false);

    }

    boolean updateDayItems(ArrayList<DayItem> items)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            if(items == null)
                return (false);

            for(int i=0; i < items.size(); i++)
            {
                if(items.get(i).sequenceNo != items.get(i).origSequenceNo)
                {
                    if(updateDayItem(items.get(i)) == false)
                        return (false);
                }
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("updateDayItems", e.getMessage());
        }
        return (false);

    }

    boolean updateDayItem(DayItem dayItem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            //myMessages().LogMessage("updateDayItem:Handling Image");
            if(dayItem.pictureChanged)
            {
                if(dayItem.origPictureAssigned && dayItem.dayPicture.length() > 0 && dayItem.dayPicture.compareTo(dayItem.origDayPicture) == 0)
                {
                    //myMessages().LogMessage("  - Original Image changed back to the original - do nothing");
                } else
                {
                    if(dayItem.origPictureAssigned)
                    {
                        //myMessages().LogMessage("  - Original Image was assigned - need to get rid of it");
                        if(removePicture(dayItem.holidayId, dayItem.origDayPicture) == false)
                            return (false);
                    }
            
                /* if picture name has something in it - it means it came from internal folder */
                    if(dayItem.dayPicture.length() == 0)
                    {
                        //myMessages().LogMessage("  - New Image was not from internal folder...");
                        if(dayItem.pictureAssigned)
                        {
                            //myMessages().LogMessage("  - Save new image and get a filename...");
                            MyString myString=new MyString();
                            if(savePicture(dayItem.holidayId, dayItem.dayBitmap, myString) == false)
                                return (false);
                            dayItem.dayPicture=myString.Value;
                            //myMessages().LogMessage("  - New filename " + dayItem.dayPicture);
                        } else
                        {
                            //myMessages().LogMessage("  - New Image not setup - so - keep it blank");
                        }
                    } else
                    {
                        //myMessages().LogMessage("  - New Image was from internal folder - so just use it (" + dayItem.dayPicture + ")");
                    }
                }
            } else
            {
                //myMessages().LogMessage("  - Image not changed - do nothing");
            }

            String lSQL;
            lSQL="UPDATE day " + "SET dayName = " + MyQuotedString(dayItem.dayName) + ", " + "    dayPicture = " + MyQuotedString(dayItem.dayPicture) + ", " + "    dayId = " + dayItem.dayId + ", " + "    sequenceNo = " + dayItem.sequenceNo + ", " + "    dayCat = " + dayItem.dayCat + ", " + "    infoId = " + dayItem.infoId + ", " + "    noteId = " + dayItem.noteId + ", " + "    galleryId = " + dayItem.galleryId + ", " + "    sygicId = " + dayItem.sygicId + " " + "WHERE holidayId = " + dayItem.holidayId + " " + "AND dayId = " + dayItem.origDayId;

            return (executeSQL("updateDayItem", lSQL));
        }
        catch(Exception e)
        {
            ShowError("updateDayItem", e.getMessage());
        }
        return (false);

    }

    public boolean deleteDays(int holidayId)
    {
        try
        {
            ArrayList<DayItem> dayList=new ArrayList<>();
            if(getDayList(holidayId, dayList) == false)
                return (false);

            for(DayItem dayItem : dayList)
                if(deleteDayItem(dayItem) == false)
                    return (false);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("deleteDays", e.getMessage());
        }
        return (false);

    }

    boolean deleteDayItem(DayItem dayItem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            String lSQL="DELETE FROM day " + "WHERE holidayId = " + dayItem.holidayId + " " + "AND dayId = " + dayItem.dayId;

            if(dayItem.dayPicture.length() > 0)
                if(removePicture(dayItem.holidayId, dayItem.dayPicture) == false)
                    return (false);

            if(executeSQL("deleteDayItem", lSQL) == false)
                return (false);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("deleteDayItem", e.getMessage());
        }
        return (false);

    }

    boolean getDayItem(int holidayId, int dayId, DayItem litem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            String lSQL;
            lSQL="SELECT holidayId, dayId, dayName, dayPicture, sequenceNo, dayCat, infoId, " + " noteId, galleryId, sygicId " + "FROM Day " + "WHERE HolidayId = " + holidayId + " " + "AND DayId = " + dayId;

            Cursor cursor=executeSQLOpenCursor("getDayItem", lSQL);
            if(cursor != null)
            {
                cursor.moveToFirst();
                if(GetDayItemFromQuery(cursor, litem) == false)
                    return (false);
            }
            executeSQLCloseCursor("getDayItem");
            return (true);
        }
        catch(Exception e)
        {
            ShowError("getDayItem", e.getMessage());
        }
        return (false);

    }

    private boolean GetDayItemFromQuery(Cursor cursor, DayItem dayItem)
    {
        if(IsValid() == false)
            return (false);

        try
        {
            if(cursor.getCount() == 0)
                return (true);

            dayItem.holidayId=Integer.parseInt(cursor.getString(0));
            dayItem.dayId=Integer.parseInt(cursor.getString(1));
            dayItem.dayName=cursor.getString(2);
            dayItem.dayPicture=cursor.getString(3);
            dayItem.sequenceNo=Integer.parseInt(cursor.getString(4));
            dayItem.dayCat=Integer.parseInt(cursor.getString(5));
            dayItem.infoId=Integer.parseInt(cursor.getString(6));
            dayItem.noteId=Integer.parseInt(cursor.getString(7));
            dayItem.galleryId=Integer.parseInt(cursor.getString(8));
            dayItem.sygicId=Integer.parseInt(cursor.getString(9));

            dayItem.origHolidayId=dayItem.holidayId;
            dayItem.origDayId=dayItem.dayId;
            dayItem.origDayName=dayItem.dayName;
            dayItem.origDayPicture=dayItem.dayPicture;
            dayItem.origSequenceNo=dayItem.sequenceNo;
            dayItem.origDayCat=dayItem.dayCat;
            dayItem.origInfoId=dayItem.infoId;
            dayItem.origNoteId=dayItem.noteId;
            dayItem.origGalleryId=dayItem.galleryId;
            dayItem.origSygicId=dayItem.sygicId;

            dayItem.pictureChanged=false;

            if(dayItem.dayPicture.length() > 0)
            {
                dayItem.pictureAssigned=true;
                dayItem.origPictureAssigned=true;
            } else
            {
                dayItem.pictureAssigned=false;
                dayItem.origPictureAssigned=false;
            }

            return (true);
        }
        catch(Exception e)
        {
            ShowError("GetDayItemFromQuery", e.getMessage());
        }

        return (false);
    }

    boolean getNextDayId(int holidayId, MyInt retInt)
    {
        try
        {
            String lSQL="SELECT IFNULL(MAX(dayId),0) FROM day WHERE holidayId = " + holidayId;

            if(executeSQLGetInt("getNextDayId", lSQL, retInt) == false)
                return (false);

            retInt.Value=retInt.Value + 1;
            return (true);
        }
        catch(Exception e)
        {
            ShowError("getNextDayId", e.getMessage());
        }
        return (false);

    }

    boolean getNextSequenceNo(int holidayId, MyInt retInt)
    {
        try
        {
            String lSQL="SELECT IFNULL(MAX(SequenceNo),0) FROM day WHERE holidayId = " + holidayId;

            if(executeSQLGetInt("getNextSequenceNo", lSQL, retInt) == false)
                return (false);

            retInt.Value=retInt.Value + 1;
            return (true);
        }
        catch(Exception e)
        {
            ShowError("getNextSequenceNo", e.getMessage());
        }
        return (false);

    }


    boolean getDayList(int holidayId, ArrayList<DayItem> al)
    {
        try
        {
            String lSql="SELECT holidayId, dayId, dayName, dayPicture, sequenceNo, dayCat, infoId, " + " noteId, galleryId, sygicId " + "FROM day " + "WHERE holidayId = " + holidayId + " " + "ORDER BY SequenceNo ";

            Cursor cursor=executeSQLOpenCursor("getDayList", lSql);
            if(cursor == null)
                return (false);

            while(cursor.moveToNext())
            {
                DayItem dayItem=new DayItem();
                if(GetDayItemFromQuery(cursor, dayItem) == false)
                    return (false);

                al.add(dayItem);
            }

            for(int i=0; i < al.size(); i++)
            {
                getScheduledTimes(al.get(i));
                getStartEndAt(al.get(i));
            }

            return (true);
        }
        catch(Exception e)
        {
            ShowError("getDayList", e.getMessage());
        }
        return (false);

    }

    private boolean getStartEndAt(DayItem dayItem)
    {
        try
        {
            dayItem.start_at="";
            String lSQL="SELECT schedName " + "FROM schedule " + "WHERE holidayId = " + dayItem.holidayId + " " + "AND dayId = " + dayItem.dayId + " " + "AND schedType = " + _resources.getInteger(R.integer.schedule_type_hotel) + " " + "AND startHour < 12";

            Cursor cursor=executeSQLOpenCursor("getStartEndAt", lSQL);
            if(cursor == null)
                return (false);

            cursor.moveToNext();
            if(cursor.getCount() > 0)
                dayItem.start_at=cursor.getString(0);

            cursor.close();


            dayItem.end_at="";
            lSQL="SELECT schedName " + "FROM schedule " + "WHERE holidayId = " + dayItem.holidayId + " " + "AND dayId = " + dayItem.dayId + " " + "AND schedType = " + _resources.getInteger(R.integer.schedule_type_hotel) + " " + "AND startHour > 12";

            cursor=executeSQLOpenCursor("getStartEndAt", lSQL);
            if(cursor == null)
                return (false);

            cursor.moveToNext();
            if(cursor.getCount() > 0)
                dayItem.end_at=cursor.getString(0);

            cursor.close();


            return (true);
        }
        catch(Exception e)
        {
            ShowError("getStartEndAt", e.getMessage());
        }
        return (false);
    }

    private boolean getScheduledTimes(DayItem dayItem)
    {
        try
        {
            String lSQL="SELECT StartTimeKnown, StartHour, StartMin, EndTimeKnown, EndHour, EndMin " + "FROM schedule " + "WHERE holidayId = " + dayItem.holidayId + " " + "AND dayId = " + dayItem.dayId;

            Cursor cursor=executeSQLOpenCursor("getScheduledTimes", lSQL);
            if(cursor == null)
                return (false);

            int lMinMinutes=86400;
            int lMaxMinutes=0;
            while(cursor.moveToNext())
            {
                int lStartTimeKnown=Integer.parseInt(cursor.getString(0));
                int lStartHour=Integer.parseInt(cursor.getString(1));
                int lStartMin=Integer.parseInt(cursor.getString(2));
                int lEndTimeKnown=Integer.parseInt(cursor.getString(3));
                int lEndHour=Integer.parseInt(cursor.getString(4));
                int lEndMin=Integer.parseInt(cursor.getString(5));

                if(lStartTimeKnown == 1)
                {
                    int lMinutes=(lStartHour * 60) + lStartMin;
                    if(lMinutes < lMinMinutes)
                        lMinMinutes=lMinutes;
                    if(lMinutes > lMaxMinutes)
                        lMaxMinutes=lMinutes;
                }
                if(lEndTimeKnown == 1)
                {
                    int lMinutes=(lEndHour * 60) + lEndMin;
                    if(lMinutes < lMinMinutes)
                        lMinMinutes=lMinutes;
                    if(lMinutes > lMaxMinutes)
                        lMaxMinutes=lMinutes;
                }
            }
            cursor.close();
            dayItem.startOfDayHours=-1;
            dayItem.startOfDayMins=-1;
            if(lMinMinutes != 86400)
            {
                dayItem.startOfDayHours=lMinMinutes / 60;
                dayItem.startOfDayMins=lMinMinutes - (dayItem.startOfDayHours * 60);
            }
            dayItem.endOfDayHours=-1;
            dayItem.endOfDayMins=-1;
            if(lMaxMinutes != 0)
            {
                dayItem.endOfDayHours=lMaxMinutes / 60;
                dayItem.endOfDayMins=lMaxMinutes - (dayItem.endOfDayHours * 60);
            }
            if(lMinMinutes != 86400 && lMaxMinutes != 0)
            {
                int lTotalMins=abs(lMaxMinutes - lMinMinutes);
                dayItem.totalHours=lTotalMins / 60;
                dayItem.totalMins=lTotalMins - (dayItem.totalHours * 60);
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("GetDayItemFromQuery", e.getMessage());
        }
        return (false);
    }

    boolean clearNote(int holidayId, int noteId)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            String l_SQL="UPDATE day SET noteId = 0 " + "WHERE holidayId = " + holidayId + " " + "AND noteId = " + noteId;

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

}
