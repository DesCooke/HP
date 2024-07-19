package com.example.des.hp.myutils;

//
// All functions return true/false
//

import android.content.Context;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.example.des.hp.R;

import static com.example.des.hp.myutils.MyMessages.myMessages;

public class ArchiveRestore
{

    private final Context _context;

    public ArchiveRestore(Context context)
    {
        _context=context;
    }

    private void ShowError(String argMessage)
    {
        myMessages().ShowError("Error in ArchiveRestore::" + "Archive", argMessage);
    }

    //
    // Archive
    //   Description: compresses all files and database into single zip file
    //   Returns: true(worked)/false(failed)
    //
    public boolean Archive()
    {
        try
        {

            myMessages().ShowMessageLong("Archiving...");

            String srcDir= MyFileUtils.MyDocuments() + "/" + _context.getString(R.string.application_file_path);
            String destDir=MyFileUtils.MyDocuments() + "/" +
                    _context.getString(R.string.archive_path);

            SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH);
            String currentDatetime=sdf.format(new Date());

            File f=new File(destDir);
            if(!f.exists())
            {
                if(!f.mkdir())
                {
                    ShowError("Unable to create directory " + f.getName());
                    return (false);
                }
            }

            String zipfilename="HP_" + currentDatetime + ".zip";

            ZipUnzip.zip(srcDir, destDir, zipfilename);

            myMessages().ShowMessageLong("Archiving...complete");

            return (true);
        }
        catch(Exception e)
        {
            ShowError(e.getMessage());
            return (false);
        }

    }

}