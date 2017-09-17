package com.example.des.hp.myutils;

import android.content.Context;
import android.content.res.Resources;
import android.widget.ImageButton;

import com.example.des.hp.MainActivity;

import static com.example.des.hp.myutils.MyApiSpecific.myApiSpecific;
import static com.example.des.hp.myutils.MyMessages.myMessages;

//
// Simple color related functions
//

public class MyColor
{
    private Context _context;
    private static MyColor myInternalColor=null;

    public static MyColor myColor()
    {
        if(myInternalColor == null)
            myInternalColor=new MyColor(MainActivity.getInstance());

        return (myInternalColor);
    }

    private MyColor(Context context)
    {
        _context=context;
        Resources res=context.getResources();
    }

    private void ShowError(String argFunction, String argMessage)
    {
        myMessages().ShowError("Error in MyColor::" + argFunction, argMessage);
    }

    // Returns: true(worked)/false(failed)
    public boolean SetImageButtonTint(ImageButton object, int resourceId)
    {
        try
        {
            object.setColorFilter(myApiSpecific().getTheColor(resourceId));
            return (true);
        }
        catch(Exception e)
        {
            ShowError("SetImageButtonTint", e.getMessage());
            return (false);
        }
    }

}
