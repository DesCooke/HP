package com.example.des.hp.myutils;

import com.example.des.hp.R;
import android.content.Intent;
import android.content.res.Resources;
import android.view.View;
import android.widget.Toast;
import android.content.Context;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.app.Activity;

import java.util.Arrays;

public class MyMessages extends Activity
{
    public static Boolean answerYes;
    public static Boolean answerNo;
    public Context _context;
    public Resources res;
    public MyLog myLog;

    public DialogWithTextViewFragment dialogWithTextViewFragment;
    public String dwtvDialogTag;
    public View.OnClickListener dwtvOnOkClick;
    public View.OnClickListener onError;

    public MyMessages(Context context)
    {
        _context = context;
        res = _context.getResources();

        myLog = new MyLog(_context);

        dwtvDialogTag = res.getString(R.string.dwtvDialogTag);
        onError=null;
    }

    public void ShowMessageShort(String argString)
    {
        Toast.makeText(_context, argString, Toast.LENGTH_SHORT).show();
    }

    public void ShowMessageLong(String argString)
    {
        Toast.makeText(_context, argString, Toast.LENGTH_LONG).show();
    }

    public void LogMessage(String argMessage)
    {
        myLog.WriteLogMessage(argMessage);
    }

    public void ClearLog()
    {
        myLog.RemoveLog();;
    }
    public void ShowError(String argTitle, String argMessage)
    {
        String stackTrace = Arrays.toString(new Throwable().getStackTrace());
        String message = argMessage + "::" + stackTrace;

        myLog.WriteLogMessage(message);

        new AlertDialog.Builder(_context)
                .setTitle(argTitle)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            //
                        }
                    })
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    
    public void ShowMessageWithOk(String argTitle, String argString, View.OnClickListener onClick)
    {

        dialogWithTextViewFragment =
                DialogWithTextViewFragment.newInstance
                        (
                                getFragmentManager(),     // for the transaction bit
                                dwtvDialogTag,            // unique name for this dialog type
                                argTitle,                 // form caption
                                argString,                // form message
                                R.drawable.airplane,      // form icon -1 for default
                                onClick,                  // onclick listener or null for default
                                _context
                        );

        dialogWithTextViewFragment.showIt();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK)
        {
            answerYes=true;
        }
        if (resultCode == RESULT_OK)
        {
            answerYes=false;
        }
    }
}
