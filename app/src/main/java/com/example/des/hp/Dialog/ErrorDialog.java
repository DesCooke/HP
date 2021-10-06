package com.example.des.hp.Dialog;

import android.content.Context;
import androidx.appcompat.app.AlertDialog;

class ErrorDialog
{
    private static Context myContext;

    static void SetContext(Context context)
    {
        myContext=context;
    }

    public static void Show(String title, String description)
    {
        if(myContext == null)
            return;

        AlertDialog.Builder messageBox=new AlertDialog.Builder(myContext);
        messageBox.setTitle(title);
        messageBox.setMessage(description);
        messageBox.setCancelable(false);
        messageBox.setNeutralButton("OK", null);
        messageBox.show();
    }

}
