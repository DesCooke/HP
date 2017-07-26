package com.example.des.hp.myutils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import com.example.des.hp.R;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import static com.example.des.hp.myutils.MyMessages.myMessages;

//
// Simple class containing File Utility functions
//
public class MyFileUtils
{
    private Context _context;
    private Resources res;
    private MyUri myUri;

    public MyFileUtils(Context context)
    {
        _context = context;
        res = context.getResources();
        myUri = new MyUri(_context);
    }

    private void ShowError(String argFunction, String argMessage)
    {
        myMessages().ShowError
                (
                        "Error in MyFileUtils::" + argFunction,
                        argMessage
                );
    }

    public boolean BaseFilenameFromUri(Uri uri, MyString retString)
    {
        try
        {
            String fPath = uri.getPath();
            int lPos = fPath.lastIndexOf(':') + 1;
            int lLength = fPath.length();
            String fPathRight = fPath.substring(lPos, lLength);
            retString.Value = fPathRight;
            return (true);
        }
        catch(Exception e)
        {
            ShowError("BaseFilenameFromUri", e.getMessage());
            return(false);
        }
    }

    // Returns: true(worked)/false(failed)
    public boolean OpenAFile(String aFile)
    {
        try
        {
            String mimeType;

            Uri toUri = myUri.getUri(aFile);

            // put back into a string
            String theString = toUri.toString();

            //get the extension - pdf etc
            String extension = MimeTypeMap.getFileExtensionFromUrl(theString);
            if (extension == null)
                return(false);

            // create or use a mime object
            MimeTypeMap mime = MimeTypeMap.getSingleton();

            // get the mimetype from extension
            mimeType = mime.getMimeTypeFromExtension(extension);

            if (mimeType == null)
                return(false);

            Intent viewIntent = new Intent();
            viewIntent.setAction(Intent.ACTION_VIEW);
            viewIntent.setDataAndType(toUri, mimeType);
            viewIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            List<ResolveInfo> resolved = _context.getPackageManager().queryIntentActivities(viewIntent, 0);
            if (resolved != null && resolved.size() > 0)
            {
                _context.startActivity(viewIntent);
            } else
            {
                myMessages().ShowMessageWithOk("Unable to open file", aFile, null);
                return(false);
            }
            return(true);
        }
        catch (Exception e)
        {
            ShowError("OpenAFile", e.getMessage());
            return(false);
        }
    }

    // Returns: true(worked)/false(failed)
    public boolean CopyFileToLocalDir(Uri argFromUri, String newFilename)
    {
        try
        {
            _context.grantUriPermission("com.example.des.hp",argFromUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            InputStream in = _context.getContentResolver().openInputStream(argFromUri);
            if(in==null)
                throw new Exception("Unable to open for read" + argFromUri.getPath());

            String ffromPath=argFromUri.getPath();
            File f = new File(ffromPath);

            File f99 = new File(res.getString(R.string.files_path));
            if(!f99.exists())
            {
                if(!f99.mkdir())
                {
                    myMessages().ShowMessageWithOk("DatabaseAccess()", "Unable to create directory " +
                        "" + f99.getName(), null);
                }
            }

            File tof=new File(res.getString(R.string.files_path) + "/" + newFilename);
            if (tof.exists())
                return(false);

            Uri toUri = Uri.fromFile(tof);
            _context.grantUriPermission("com.example.des.hp",toUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            OutputStream out = _context.getContentResolver().openOutputStream(toUri);
            if(out==null)
                throw new Exception("Unable to open for write" + tof);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1)
            {
                out.write(buffer, 0, read);
            }
            in.close();

            // write the output file (You have now copied the file)
            out.flush();
            out.close();

            return(true);
        }
        catch (Exception e)
        {
            ShowError("CopyFileToLocalDir", e.getMessage());
            return(false);
        }
    }

}
