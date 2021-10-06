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

    private Context _context;

    public ArchiveRestore(Context context)
    {
        _context=context;
    }

    private void ShowError(String argFunction, String argMessage)
    {
        myMessages().ShowError("Error in ArchiveRestore::" + argFunction, argMessage);
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
            String currentDateandTime=sdf.format(new Date());

            File f=new File(destDir);
            if(!f.exists())
            {
                if(!f.mkdir())
                {
                    ShowError("Archive", "Unable to create directory " + f.getName());
                    return (false);
                }
            }

            String zipfilename="HP_" + currentDateandTime + ".zip";

            ZipUnzip.zip(srcDir, destDir, zipfilename, true);

            myMessages().ShowMessageLong("Archiving...complete");

            return (true);
        }
        catch(Exception e)
        {
            ShowError("Archive", e.getMessage());
            return (false);
        }

    }

    //
    // Restore
    //   Description: uncompresses all files and database from single zip file
    //   not actually used yet - restoring is still a manual thing
    //   Returns: true(worked)/false(failed)
    //
    public boolean Restore(String filename)
    {
        try
        {
            myMessages().ShowMessageLong("Restoring...");

            String destDir=MyFileUtils.MyDocuments() + "/" +
                    _context.getResources().getString(R.string.tmp_path);

            File f=new File(destDir);
            if(!f.exists())
            {
                if(!f.mkdir())
                {
                    ShowError("Restore", "Unable to create directory " + f.getName());
                    return (false);
                }
            }

            if(ZipUnzip.unzip(filename, destDir) == true)
            {
                myMessages().ShowMessageWithOk("ArchiveRestore::Restore()", "Completed Successfully", null);
                return (true);
            } else
            {
                myMessages().ShowMessageWithOk("ArchiveRestore::Restore()", "Error", null);
                return (false);
            }
        }
        catch(Exception e)
        {
            ShowError("Restore", e.getMessage());
            return (false);
        }
    }
}