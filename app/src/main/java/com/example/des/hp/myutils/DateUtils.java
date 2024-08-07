package com.example.des.hp.myutils;

import android.widget.DatePicker;


import com.example.des.hp.Dialog.BaseActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static java.text.DateFormat.getDateInstance;

//
// All functions return true/false
//
public class DateUtils extends BaseActivity
{
    public static long unknownDate=2051222400000L;
    public static DateUtils myDateUtils=null;

    public static DateUtils dateUtils()
    {
        if(myDateUtils == null)
            myDateUtils=new DateUtils();

        return (myDateUtils);
    }


    public DateUtils()
    {
    }


    //
    // getDateFromDatePicker
    //   Description: accepts a DatePicker and extracts the date element
    //                and passes back through retDate
    //   Returns: true(worked)/false(failed)
    //
    public boolean getDateFromDatePicker(DatePicker datePicker, java.util.Date retDate)
    {
        try
        {
            int day=datePicker.getDayOfMonth();
            int month=datePicker.getMonth();
            int year=datePicker.getYear();

            Calendar calendar=Calendar.getInstance();
            calendar.set(year, month, day);

            retDate.setTime(calendar.getTimeInMillis());
            return (true);
        }
        catch(Exception e)
        {
            ShowError("getDateFromDatePicker", e.getMessage());
            return (false);
        }
    }

    //
    // DateToInt
    //   Description: accepts a Date and returns the milliseconds part
    //   Returns: true(worked)/false(failed)
    //
    public boolean DateToInt(Date date, MyLong retLong)
    {
        try
        {
            retLong.Value=date.getTime();
            return (true);
        }
        catch(Exception e)
        {
            ShowError("DateToInt", e.getMessage());
            return (false);
        }
    }

    //
    // IntToDate
    //   Description: accepts an integer and returns the date equivalent
    //   Returns: true(worked)/false(failed)
    //
    public boolean IntToDate(long date, Date retDate)
    {
        try
        {
            retDate.setTime(date);
            return (true);
        }
        catch(Exception e)
        {
            ShowError("IntToDate", e.getMessage());
            return (false);
        }
    }

    //
    // IsUnknown
    //   Description: accepts date - if this is equal to the rogue value 'unknown'
    //                then the retBoolean is returned true, else false
    //   Returns: true(worked)/false(failed)
    //
    public boolean IsUnknown(Date argDate, MyBoolean retBoolean)
    {
        try
        {
            retBoolean.Value= argDate.getTime() == unknownDate;
            return (true);
        }
        catch(Exception e)
        {
            ShowError("IsUnknown", e.getMessage());
            return (false);
        }
    }

    //
    // AddDays
    //   Description: accepts date - if this is equal to the rogue value 'unknown'
    //                then the retBoolean is returned true, else false
    //   Returns: true(worked)/false(failed)
    //
    public boolean AddDays(Date date, int days, Date retDate)
    {
        try
        {
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.HOUR, 0);
            calendar.add(Calendar.DATE, days);

            retDate.setTime(calendar.getTimeInMillis());

            return (true);
        }
        catch(Exception e)
        {
            ShowError("AddDays", e.getMessage());
            return (false);
        }
    }

    //
    // StrToDate
    //   Description: accepts string and sets retDate to the date equivalent
    //   Returns: true(worked)/false(failed)
    //
    public boolean StrToDate(String string, Date date)
    {
        try
        {
            DateFormat df=getDateInstance();
            if(string==null || string.isEmpty()) {
                GetToday(date);
                return true;
            }
            date.setTime(Objects.requireNonNull(df.parse(string)).getTime());
            return (true);
        }
        catch(ParseException e)
        {
            ShowError("StrToDate", e.getMessage());
            return (false);
        }
    }

    public boolean GetToday(Date retDate)
    {
        try
        {
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.HOUR, 0);

            retDate.setTime(calendar.getTimeInMillis());

            return (true);
        }
        catch(Exception e)
        {
            ShowError("GetToday", e.getMessage());
            return (false);
        }
    }

    public boolean GetYear(Date date, MyInt myInt)
    {
        try
        {
            String lString=new SimpleDateFormat("yyyy", Locale.ENGLISH).format(date);
            myInt.Value=Integer.parseInt(lString);
            return (true);
        }
        catch(Exception e)
        {
            ShowError("GetYear", e.getMessage());
            return (false);
        }
    }

    public boolean GetMonth(Date date, MyInt myInt)
    {
        try
        {
            String lString=new SimpleDateFormat("MM", Locale.ENGLISH).format(date);
            myInt.Value=Integer.parseInt(lString);
            return (true);
        }
        catch(Exception e)
        {
            ShowError("GetYear", e.getMessage());
            return (false);
        }
    }

    public boolean GetDay(Date date, MyInt myInt)
    {
        try
        {
            String lString=new SimpleDateFormat("dd", Locale.ENGLISH).format(date);
            myInt.Value=Integer.parseInt(lString);
            return (true);
        }
        catch(Exception e)
        {
            ShowError("GetYear", e.getMessage());
            return (false);
        }
    }

    public boolean GetDiff(Date date1, Date date2, MyDateDiff myDateDiff)
    {
        try
        {
            Date ldate1=new Date();
            Date ldate2=new Date();
            MyInt lyear1=new MyInt();
            MyInt lyear2=new MyInt();
            MyInt lmonth1=new MyInt();
            MyInt lmonth2=new MyInt();
            MyInt lday1=new MyInt();
            MyInt lday2=new MyInt();

            // Ensure date1 is always the earlier date, date2 is always the later date
            // remove time element
            if(date1.getTime() < date2.getTime())
            {
                ldate1.setTime(date1.getTime());
                ldate2.setTime(date2.getTime());
            } else
            {
                ldate1.setTime(date2.getTime());
                ldate2.setTime(date1.getTime());
            }

            if(!GetYear(ldate1, lyear1))
                return (false);
            if(!GetYear(ldate2, lyear2))
                return (false);
            if(!GetMonth(ldate1, lmonth1))
                return (false);
            if(!GetMonth(ldate2, lmonth2))
                return (false);
            if(!GetDay(ldate1, lday1))
                return (false);
            if(!GetDay(ldate2, lday2))
                return (false);

            myDateDiff.year=lyear2.Value - lyear1.Value;
            myDateDiff.month=lmonth2.Value - lmonth1.Value;
            myDateDiff.day=lday2.Value - lday1.Value;

            if(myDateDiff.day < 0)
            {
                Calendar c=Calendar.getInstance();
                c.set(lyear1.Value, lmonth1.Value - 1, 1);
                int lDaysInMonth=c.getActualMaximum(Calendar.DAY_OF_MONTH);
                //myMessages().LogMessage("days in earlier month " + lyear1.Value + "/" + lmonth1.Value + " = " + lDaysInMonth);

                int lDaysStartOfNextMonth=lday2.Value;
                //myMessages().LogMessage("Days in next month " + lDaysStartOfNextMonth);

                int lDaysEndOfThisMonth=lDaysInMonth - lday1.Value;
                //myMessages().LogMessage("Days to end of this month " + lDaysEndOfThisMonth);

                myDateDiff.day=lDaysStartOfNextMonth + lDaysEndOfThisMonth;
                //myMessages().LogMessage("final number of days " + myDateDiff.day);

                myDateDiff.month--;
            }

            if(myDateDiff.month < 0)
            {
                myDateDiff.month=12 + myDateDiff.month;
                myDateDiff.year--;
            }
        }
        catch(Exception e)
        {
            ShowError("GetDiff", e.getMessage());
        }
        return (true);
    }

    //
    // DatePickerToStr
    //   Description: accepts a DatePicker and extracts the date element
    //                and passes back the text version vases on the current
    //                date format through a MyString object
    //   Returns: true(worked)/false(failed)
    //
    public boolean DatePickerToStr(DatePicker datePicker, MyString retString)
    {
        try
        {
            retString.Value="";

            Date date=new Date();
            if(!getDateFromDatePicker(datePicker, date))
                return (false);

            DateFormat df=getDateInstance();
            retString.Value=df.format(date);
            return (true);
        }
        catch(Exception e)
        {
            ShowError("DatePickerToStr", e.getMessage());
            return (false);
        }
    }


    //
    // DateToStr
    //   Description: accepts a Date and returns the string equivalent through
    //                MyString
    //   Returns: true(worked)/false(failed)
    //
    public boolean DateToStr(Date date, MyString retString)
    {
        try
        {
            DateFormat df=getDateInstance();
            retString.Value=df.format(date);
            return (true);
        }
        catch(Exception e)
        {
            ShowError("DateToStr", e.getMessage());
            return (false);
        }
    }

    //
    // FormatTime
    //   Description: ensures correct formatting of a time string - 04:09 etc
    //   Returns: String - no need for error checking and it can be static
    //
    public static String FormatTime(int hour, int minute)
    {
        String timeString="";

        if(hour < 10)
            timeString=timeString + "0";

        timeString=timeString + hour + ":";

        if(minute < 10)
            timeString=timeString + "0";

        timeString=timeString + minute;

        return (timeString);
    }

}
