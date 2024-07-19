package com.example.des.hp.myutils;

import android.annotation.SuppressLint;
import android.content.Context;
//noinspection ExifInterface
import android.media.ExifInterface;
import android.net.Uri;
import android.widget.TimePicker;


import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.MainActivity;

import java.io.InputStream;


//
// All functions return true/false
//
public class MyApiSpecific extends BaseActivity
{
    @SuppressLint("StaticFieldLeak")
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

    public int GetImageOrientation(Uri imageUri)
    {
        try
        {
            InputStream input = _context.getContentResolver().openInputStream(imageUri);
            ExifInterface ei;
            assert input != null;
            ei = new ExifInterface(input);

            return ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        }
        catch(Exception e)
        {
            ShowError("GetImageOrientation", e.getMessage());
        }
        return (0);
        
    }
    public int GetHour(TimePicker time)
    {
        try
        {
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
            time.setMinute(minute);
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
            time.setHour(hour);
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

            return (_context.getColor(resColor));
        }
        catch(Exception e)
        {
            ShowError("getListIcon", e.getMessage());
        }
        return (0);

    }


}
