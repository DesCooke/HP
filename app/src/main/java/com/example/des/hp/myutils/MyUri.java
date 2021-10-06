package com.example.des.hp.myutils;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import androidx.core.content.FileProvider;

import com.example.des.hp.R;

import java.io.File;

import static com.example.des.hp.myutils.MyLog.myLog;

class MyUri
{
    private Context _context;
    public Resources res;

    MyUri(Context context)
    {
        _context=context;
        res=_context.getResources();
    }

    public void LogMessage(String argMessage)
    {
        myLog().WriteLogMessage(argMessage);
    }

    Uri getUri(String filename)
    {
        // get the full path, "/storage/emulated/0/HP/files" "/" "fred.jpg"
        String originalfullurl=MyFileUtils.MyDocuments() + "/" +
                res.getString(R.string.application_file_path) + "/" +
                res.getString(R.string.files_path) + "/" + filename;

        // create a File object from it
        File originalFile=new File(originalfullurl);

        // content://com.example.des.hp.provider/my_files/files/Epcot-map.pdf
        // my_files is decoded as
        Uri toUri=FileProvider.getUriForFile(_context, _context.getApplicationContext().getPackageName() + ".provider", originalFile);

        return (toUri);
    }
}
