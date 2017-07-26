package com.example.des.hp.myutils;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.widget.ImageButton;

import static com.example.des.hp.myutils.MyMessages.myMessages;

//
// Simple color related functions
//

public class MyColor
{
    private Context _context;
    
    public MyColor(Context context)
    {
        _context = context;
        Resources res = context.getResources();
    }

    private void ShowError(String argFunction, String argMessage)
    {
        myMessages().ShowError
                (
                        "Error in MyColor::" + argFunction,
                        argMessage
                );
    }

    // Returns: true(worked)/false(failed)
    public boolean SetImageButtonTint(ImageButton object, int resourceId)
    {
        try
        {
            object.setColorFilter(ContextCompat.getColor(_context, resourceId));
            return(true);
        }
        catch (Exception e)
        {
            ShowError("SetImageButtonTint", e.getMessage());
            return(false);
        }
    }

}
