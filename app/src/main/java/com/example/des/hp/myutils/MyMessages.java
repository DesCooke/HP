package com.example.des.hp.myutils;

import com.example.des.hp.MainActivity;
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

import static com.example.des.hp.myutils.MyLog.myLog;

public class MyMessages extends Activity
{
    public static Boolean answerYes;
    public static Boolean answerNo;
    public static Context lcontext;
    public static Resources lres;
    public static MyMessages messages=null;

    public DialogWithTextViewFragment dialogWithTextViewFragment;
    public String dwtvDialogTag;
    public View.OnClickListener dwtvOnOkClick;
    public View.OnClickListener onError;

    public static MyMessages myMessages()
    {
        if(messages == null)
            messages=new MyMessages(MainActivity.getInstance());

        return (messages);
    }

    public static void SetContext(Context context)
    {
        lcontext=context;
        lres=lcontext.getResources();
    }

    public MyMessages(Context context)
    {
        lcontext=context;
        lres=lcontext.getResources();

        dwtvDialogTag=lres.getString(R.string.dwtvDialogTag);
        onError=null;
    }

    public void ShowMessageShort(String argString)
    {
        Toast.makeText(lcontext, argString, Toast.LENGTH_SHORT).show();
    }

    public void ShowMessageLong(String argString)
    {
        Toast.makeText(lcontext, argString, Toast.LENGTH_LONG).show();
    }

    public void LogMessage(String argMessage)
    {
        myLog().WriteLogMessage(argMessage);
    }

    public void ClearLog()
    {
        myLog().RemoveLog();
    }

    public void ShowError(String argTitle, String argMessage)
    {
        String stackTrace=Arrays.toString(new Throwable().getStackTrace());
        String message=argMessage + "::" + stackTrace;

        myLog().WriteLogMessage(message);

        new AlertDialog.Builder(lcontext).setTitle(argTitle).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                //
            }
        }).setMessage(message).setIcon(android.R.drawable.ic_dialog_alert).show();
    }

    public void ShowMessageWithOk(String argTitle, String argString, View.OnClickListener onClick)
    {

        dialogWithTextViewFragment=DialogWithTextViewFragment.newInstance(
            getFragmentManager(),     // for the transaction bit
            dwtvDialogTag,            // unique name for this dialog type
            argTitle,                 // form caption
            argString,                // form message
            R.drawable.airplane,      // form icon -1 for default
            onClick,                  // onclick listener or null for default
            lcontext
        );

        dialogWithTextViewFragment.showIt();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode == RESULT_OK)
        {
            answerYes=true;
        }
        if(resultCode == RESULT_OK)
        {
            answerYes=false;
        }
    }
}
