package com.example.des.hp.myutils;

import android.app.Dialog;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.text.method.KeyListener;
import android.view.WindowManager;
import android.widget.EditText;

import static com.example.des.hp.myutils.MyMessages.myMessages;

public class MyKeyboard
{

    MyKeyboard()
    {
    }

    private void ShowError(String argFunction, String argMessage)
    {
        myMessages().ShowError("Error in MyKeyboard::" + argFunction, argMessage);
    }

    public boolean show(Dialog dialog)
    {
        try
        {
            if(dialog != null)
            {
                if(dialog.getWindow() != null)
                {
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                } else
                {
                    throw new Exception("dialog.getWindow() is null");
                }
            } else
            {
                throw new Exception("dialog is null");
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("show", e.getMessage());
        }
        return (false);
    }

    boolean showNumeric(Dialog dialog, EditText editText)
    {
        try
        {
            editText.setInputType(InputType.TYPE_CLASS_PHONE);

            KeyListener keyListener=DigitsKeyListener.getInstance("1234567890-");
            editText.setKeyListener(keyListener);

            return (show(dialog));
        }
        catch(Exception e)
        {
            ShowError("showNumeric", e.getMessage());
        }
        return (false);
    }

}
