package com.example.des.hp.myutils;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import androidx.core.content.FileProvider;

import com.example.des.hp.R;

import java.io.File;

import static com.example.des.hp.myutils.MyLog.myLog;

public class MyUri
{
    private Context _context;
    public Resources res;

    public MyUri(Context context)
    {
        _context=context;
        res=_context.getResources();
    }

    public void LogMessage(String argMessage)
    {
        myLog().WriteLogMessage(argMessage);
    }

    public Uri getUri(String filename)
    {
        // create a File object from it
        File originalFile=new File(filename);

        // content://com.example.des.hp.provider/my_files/files/Epcot-map.pdf
        // my_files is decoded as
        Uri toUri=FileProvider.getUriForFile(_context, _context.getApplicationContext().getPackageName() + ".provider", originalFile);

        return (toUri);
    }
}
