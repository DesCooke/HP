package com.example.des.hp.myutils;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.widget.TimePicker;


import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.MainActivity;


//
// All functions return true/false
//
public class MyApiSpecific extends BaseActivity
{
    public static MyApiSpecific apiSpecific=null;
    public Context _context;

    public static MyApiSpecific myApiSpecific()
    {
        if(apiSpecific == null)
            apiSpecific=new MyApiSpecific(MainActivity.getInstance());

        return (apiSpecific);
    }


    public MyApiSpecific(Context context)
    {
        try
        {
            _context=context;
        }
        catch(Exception e)
        {
            ShowError("MyApiSpecific", e.getMessage());
        }
    }

    public int GetHour(TimePicker time)
    {
        try
        {
            if(Build.VERSION.SDK_INT < 23)
            {
                //noinspection deprecation
                return (time.getCurrentHour());

            }
            return (time.getHour());
        }
        catch(Exception e)
        {
            ShowError("GetHour", e.getMessage());
        }
        return (0);

    }

    public int GetMinute(TimePicker time)
    {
        try
        {
            if(Build.VERSION.SDK_INT < 23)
            {
                //noinspection deprecation
                return (time.getCurrentMinute());
            }
            return (time.getMinute());
        }
        catch(Exception e)
        {
            ShowError("GetMinute", e.getMessage());
        }
        return (0);
    }

    public void SetMinute(TimePicker time, int minute)
    {
        try
        {
            if(Build.VERSION.SDK_INT < 23)
            {
                //noinspection deprecation
                time.setCurrentMinute(minute);
                return;
            } else
            {
                time.setMinute(minute);
            }
        }
        catch(Exception e)
        {
            ShowError("SetMinute", e.getMessage());
        }

    }

    public void SetHour(TimePicker time, int hour)
    {
        try
        {
            if(Build.VERSION.SDK_INT < 23)
            {
                //noinspection deprecation
                time.setCurrentHour(hour);
                return;
            } else
            {
                time.setHour(hour);
            }
        }
        catch(Exception e)
        {
            ShowError("SetHour", e.getMessage());
        }

    }

    public int getTheColor(int resColor)
    {
        try
        {
            if(Build.VERSION.SDK_INT < 23)
                return (ContextCompat.getColor(_context, resColor));

            return (_context.getColor(resColor));
        }
        catch(Exception e)
        {
            ShowError("getListIcon", e.getMessage());
        }
        return (0);

    }


}
