package com.example.des.hp.Database;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import com.example.des.hp.Day.DayItem;
import com.example.des.hp.Event.EventScheduleDetailItem;
import com.example.des.hp.Event.EventScheduleItem;
import com.example.des.hp.Holiday.HolidayItem;
import com.example.des.hp.myutils.MyInt;
import com.example.des.hp.myutils.MyString;

import java.io.BufferedWriter;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import static com.example.des.hp.Database.DatabaseAccess.database;
import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
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
            String lSQL="CREATE TABLE IF NOT EXISTS day " +
                    "( " +
                    "  holidayId  INT(5),  " +
                    "  dayId      INT(5),  " +
                    "  sequenceNo INT(5),  " +
                    "  dayName    VARCHAR, " +
                    "  dayPicture VARCHAR, " +
                    "  dayCat     INT(5),  " +
                    "  infoId     INT(5),  " +
                    "  noteId     INT(5),  " +
                    "  galleryId  INT(5) ) ";

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
            buffwriter.write("<day>\n");

            String lSql =
                    "select holidayId, " +
                            "dayId, " +
                            "sequenceNo, " +
                            "dayName, " +
                            "dayPicture, " +
                            "dayCat, " +
                            "infoId, " +
                            "noteId, " +
                            "galleryId " +
                            "FROM day " +
                            "ORDER BY holidayId, dayId";

            Cursor cursor = executeSQLOpenCursor("export", lSql);
            if (cursor == null)
                return;

            while (cursor.moveToNext()) {
                int holidayId = Integer.parseInt(cursor.getString(0));
                String pic = cursor.getString(4);
                String picAsBase64 = "";
                if (!pic.isEmpty()) {
                    picAsBase64 = pictureToBase64(holidayId, pic);
                }

                buffwriter.write(cursor.getString(0) + "," +
                        cursor.getString(1) + "," +
                        cursor.getString(2) + "," +
                        encodeString(cursor.getString(3)) + "," +
                        picAsBase64 + "," +
                        cursor.getString(5) + "," +
                        cursor.getString(6) + "," +
                        cursor.getString(7) + "," +
                        cursor.getString(8) + "\n"
                );

            }

        } catch (java.io.FileNotFoundException e) {
        } catch (java.io.IOException e) {
        }
    }
    boolean getDayCount(int argHolidayId, MyInt retInt)
    {
        try
        {
            if(!IsValid())
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
            if(!IsValid())
                return (false);

            if(dayItem.pictureAssigned) {
                /* if picture name has something in it - it means it came from internal folder */
                if (dayItem.dayPicture.isEmpty()) {
                    //myMessages().LogMessage("  - Save new image and get a filename...");
                    MyString myString = new MyString();
                    try(DatabaseAccess da = databaseAccess()){
                        HolidayItem holidayItem = new HolidayItem();
                        da.getHolidayItem(dayItem.holidayId, holidayItem);
                        if (!savePicture(holidayItem.holidayId, dayItem.dayBitmap, myString))
                            return (false);
                    }
                    dayItem.dayPicture = myString.Value;
                    //myMessages().LogMessage("  - New filename " + dayItem.dayPicture);
                }
            }

            String lSql="INSERT INTO day " + "  (holidayId, dayId, dayName, dayPicture, sequenceNo, dayCat, infoId, " + "   noteId, galleryId) " + "VALUES " + "(" + dayItem.holidayId + "," + dayItem.dayId + "," + MyQuotedString(dayItem.dayName) + "," + MyQuotedString(dayItem.dayPicture) + "," + dayItem.sequenceNo + ", " + dayItem.dayCat + ", " + dayItem.infoId + ", " + dayItem.noteId + ", " + dayItem.galleryId + ")";

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
            if(!IsValid())
                return (false);

            if(items == null)
                return (false);

            for(int i=0; i < items.size(); i++)
            {
                if(items.get(i).sequenceNo != items.get(i).origSequenceNo)
                {
                    if(!updateDayItem(items.get(i)))
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
            if(!IsValid())
                return (false);

            if(dayItem.pictureChanged)
            {
                if (!dayItem.origPictureAssigned ||
                        dayItem.dayPicture.isEmpty() ||
                        dayItem.dayPicture.compareTo(dayItem.origDayPicture) != 0) {

                    if(dayItem.origPictureAssigned)
                    {
                        if (!removePictureByHolidayId(dayItem.holidayId, dayItem.origDayPicture))
                            return (false);
                    }

                /* if picture name has something in it - it means it came from internal folder */
                    if(dayItem.dayPicture.isEmpty())
                    {
                        //myMessages().LogMessage("  - New Image was not from internal folder...");
                        if(dayItem.pictureAssigned)
                        {
                            //myMessages().LogMessage("  - Save new image and get a filename...");
                            MyString myString=new MyString();
                            if(!savePicture(dayItem.holidayId, dayItem.dayBitmap, myString))
                                return (false);
                            dayItem.dayPicture=myString.Value;
                            //myMessages().LogMessage("  - New filename " + dayItem.dayPicture);
                        }
                    }
                }
            }

            String lSQL;
            lSQL="UPDATE day " + "SET dayName = " + MyQuotedString(dayItem.dayName) + ", " + "    dayPicture = " + MyQuotedString(dayItem.dayPicture) + ", " + "    dayId = " + dayItem.dayId + ", " + "    sequenceNo = " + dayItem.sequenceNo + ", " + "    dayCat = " + dayItem.dayCat + ", " + "    infoId = " + dayItem.infoId + ", " + "    noteId = " + dayItem.noteId + ", " + "    galleryId = " + dayItem.galleryId + "  " + "WHERE holidayId = " + dayItem.holidayId + " " + "AND dayId = " + dayItem.origDayId;

            return (executeSQL("updateDayItem", lSQL));
        }
        catch(Exception e)
        {
            ShowError("updateDayItem", e.getMessage());
        }
        return (false);

    }

    boolean deleteDayItem(DayItem dayItem)
    {
        try
        {
            if(!IsValid())
                return (false);

            String lSQL="DELETE FROM day " + "WHERE holidayId = " + dayItem.holidayId + " " + "AND dayId = " + dayItem.dayId;

            if(!dayItem.dayPicture.isEmpty())
                if(!removePictureByHolidayId(dayItem.holidayId, dayItem.dayPicture))
                    return (false);

            if(!executeSQL("deleteDayItem", lSQL))
                return (false);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("deleteDayItem", e.getMessage());
        }
        return (false);

    }

    boolean getDayItem(int holidayId, int dayId, DayItem item)
    {
        try
        {
            if(!IsValid())
                return (false);

            String lSQL;
            lSQL="SELECT holidayId, dayId, dayName, dayPicture, sequenceNo, dayCat, infoId, " + " noteId, galleryId " + "FROM Day " + "WHERE HolidayId = " + holidayId + " " + "AND DayId = " + dayId;

            Cursor cursor=executeSQLOpenCursor("getDayItem", lSQL);
            if(cursor != null)
            {
                cursor.moveToFirst();
                if(!GetDayItemFromQuery(cursor, item))
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
        if(!IsValid())
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

            dayItem.origHolidayId=dayItem.holidayId;
            dayItem.origDayId=dayItem.dayId;
            dayItem.origDayName=dayItem.dayName;
            dayItem.origDayPicture=dayItem.dayPicture;
            dayItem.origSequenceNo=dayItem.sequenceNo;
            dayItem.origDayCat=dayItem.dayCat;
            dayItem.origInfoId=dayItem.infoId;
            dayItem.origNoteId=dayItem.noteId;
            dayItem.origGalleryId=dayItem.galleryId;

            dayItem.pictureChanged=false;

            if(!dayItem.dayPicture.isEmpty())
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

            if(!executeSQLGetInt("getNextDayId", lSQL, retInt))
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

            if(!executeSQLGetInt("getNextSequenceNo", lSQL, retInt))
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
            String lSql="SELECT holidayId, dayId, dayName, dayPicture, sequenceNo, dayCat, infoId, " + " noteId, galleryId " + "FROM day " + "WHERE holidayId = " + holidayId + " " + "ORDER BY SequenceNo ";

            Cursor cursor=executeSQLOpenCursor("getDayList", lSql);
            if(cursor == null)
                return (false);

            while(cursor.moveToNext())
            {
                DayItem dayItem=new DayItem();
                if(!GetDayItemFromQuery(cursor, dayItem))
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

    private void getStartEndAt(DayItem dayItem)
    {
        try
        {
            dayItem.start_at="";
            String lSQL=
                    "SELECT schedName " +
                            "FROM schedule " +
                            "WHERE holidayId = " + dayItem.holidayId + " " +
                            "AND dayId = " + dayItem.dayId + " " +
                            "ORDER BY SequenceNo";

            Cursor cursor=executeSQLOpenCursor("getStartEndAt", lSQL);
            if(cursor == null)
                return;

            cursor.moveToNext();
            if(cursor.getCount() > 0)
                dayItem.start_at=cursor.getString(0);

            cursor.close();


            dayItem.end_at="";
            lSQL=
                    "SELECT schedName " +
                            "FROM schedule " +
                            "WHERE holidayId = " + dayItem.holidayId + " " +
                            "AND dayId = " + dayItem.dayId + " " +
                            "ORDER BY SequenceNo DESC";


            cursor=executeSQLOpenCursor("getStartEndAt", lSQL);
            if(cursor == null)
                return;

            cursor.moveToNext();
            if(cursor.getCount() > 0)
                dayItem.end_at=cursor.getString(0);

            cursor.close();


        }
        catch(Exception e)
        {
            ShowError("getStartEndAt", e.getMessage());
        }
    }

    private void getScheduledTimes(DayItem dayItem)
    {
        try
        {
            ArrayList<EventScheduleItem> al = new ArrayList<>();
            database.getScheduleList(dayItem.holidayId, dayItem.dayId, 0,0,al);

            int lMinMinutes=86400;
            int lMaxMinutes=0;
            for (int i=0; i<al.size(); i++)
            {
                int startTimeAsMinutes = al.get(i).GetStartTimeAsMinutes();
                int endTimeAsMinutes = al.get(i).GetEndTimeAsMinutes();
                if(startTimeAsMinutes<lMinMinutes)
                    lMinMinutes=startTimeAsMinutes;
                if(endTimeAsMinutes > lMaxMinutes)
                    lMaxMinutes=endTimeAsMinutes;
            }
            if(al.size()>1)
            {
                // if we have an overnight flight then the night before will show...
                //   check-in and departs, but not arrival - set end date to midnight
                // the day after will show
                //   arrival but not check-in or departs- set start date to 00:00
                EventScheduleDetailItem gai = al.get(al.size()-1).eventScheduleDetailItem;
                if(gai.CheckInKnown && gai.DepartsKnown && !gai.ArrivalKnown)
                    lMaxMinutes = 24 * 60;
                gai = al.get(0).eventScheduleDetailItem;
                if(!gai.CheckInKnown && !gai.DepartsKnown && gai.ArrivalKnown)
                    lMinMinutes = 0;
            }

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
        }
        catch(Exception e)
        {
            ShowError("GetDayItemFromQuery", e.getMessage());
        }
    }

}
