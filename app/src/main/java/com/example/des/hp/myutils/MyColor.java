package com.example.des.hp.myutils;

import android.widget.ImageButton;

import static com.example.des.hp.myutils.MyApiSpecific.myApiSpecific;
import static com.example.des.hp.myutils.MyMessages.myMessages;

//
// Simple color related functions
//

public class MyColor
{
    private static MyColor myInternalColor=null;

    public static MyColor myColor()
    {
        if(myInternalColor == null)
            myInternalColor=new MyColor();

        return (myInternalColor);
    }

    private MyColor()
    {
    }

    private void ShowError(String argMessage)
    {
        myMessages().ShowError("Error in MyColor::" + "SetImageButtonTint", argMessage);
    }

    // Returns: true(worked)/false(failed)
    public void SetImageButtonTint(ImageButton object, int resourceId)
    {
        try
        {
            object.setColorFilter(myApiSpecific().getTheColor(resourceId));
        }
        catch(Exception e)
        {
            ShowError(e.getMessage());
        }
    }

}
