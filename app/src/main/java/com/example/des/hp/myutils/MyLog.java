package com.example.des.hp.myutils;

import android.content.Context;
import android.content.res.Resources;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.MainActivity;
import com.example.des.hp.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.util.Date;

//
// Simple class containing File Utility functions
//
class MyLog
{
    private Resources res;
    public static MyLog log=null;

    MyLog(Context context)
    {
        Context _context = context;
        res = context.getResources();
    }

    public static MyLog myLog()
    {
        if (log == null)
            log = new MyLog(MainActivity.getInstance());
        
        return (log);
    }

    void WriteLogMessage(String argString)
    {
        try
        {
            String logfilename = res.getString(R.string.log_filename);

            // create a File object from it
            File file = new File(logfilename);
            if (file.exists() == false)
                if(!file.createNewFile())
                    throw new Exception("file.CreateNewFile() returned false");

            String timeStamp = DateFormat.getDateTimeInstance().format(new Date());

            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(timeStamp + ":" + argString + "\n");
            bw.close();
        }
        catch(Exception e)
        {
            //
        }
    }

    void RemoveLog()
    {
        try
        {
            String logfilename = res.getString(R.string.log_filename);

            // create a File object from it
            File file = new File(logfilename);
            if (file.exists())
                if(!file.delete())
                    throw new Exception("file.delete() returned false");
        }
        catch(Exception e)
        {
            //
        }
    }
}
