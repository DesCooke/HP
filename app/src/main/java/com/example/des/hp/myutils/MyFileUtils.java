package com.example.des.hp.myutils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

import com.example.des.hp.MainActivity;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import static com.example.des.hp.myutils.MyMessages.myMessages;

import androidx.annotation.Nullable;

//
// Simple class containing File Utility functions
//
public class MyFileUtils
{
    private final Context _context;
    @SuppressLint("StaticFieldLeak")
    private static MyFileUtils fileUtils=null;

    public static String MyDocuments()
    {
        return("/storage/emulated/0");
        //return(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath());
    }

    public static MyFileUtils myFileUtils()
    {
        if(fileUtils == null)
            fileUtils=new MyFileUtils(MainActivity.getInstance());

        return (fileUtils);
    }

    public MyFileUtils(Context context)
    {
        _context=context;
    }

    private void ShowError(String argFunction, String argMessage)
    {
        myMessages().ShowError("Error in MyFileUtils::" + argFunction, argMessage);
    }

    public boolean BaseFilenameFromUri(Uri uri, MyString retString)
    {
        try
        {
            String fPath=uri.getPath();
            assert fPath != null;
            int lColonPos=fPath.lastIndexOf(':') + 1;
            int lSlashPos=fPath.lastIndexOf('/') + 1;
            int lPos;
            lPos=lColonPos;
            if(lSlashPos > lPos)
                lPos=lSlashPos;
            int lLength=fPath.length();
            retString.Value= fPath.substring(lPos, lLength);
            return (true);
        }
        catch(Exception e)
        {
            ShowError("BaseFilenameFromUri", e.getMessage());
        }
        return (false);
    }

    // Returns: true(worked)/false(failed)
    public void OpenAFile(String aFile)
    {
        try
        {
            MyUri myUri = new MyUri(_context);
            Uri uri = myUri.getUri(aFile);

            //get the extension - pdf etc
            String extension=MimeTypeMap.getFileExtensionFromUrl(aFile);
            if(extension == null)
                return;

            // create or use a mime object
            MimeTypeMap mime=MimeTypeMap.getSingleton();

            // get the mimetype from extension
            String mimeType=mime.getMimeTypeFromExtension(extension);

            if(mimeType == null)
                return;

            Intent viewIntent=new Intent();
            viewIntent.setAction(Intent.ACTION_VIEW);
            viewIntent.setDataAndType(uri, mimeType);
            viewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            viewIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            _context.startActivity(viewIntent);
        }
        catch(Exception e)
        {
            ShowError("OpenAFile", e.getMessage());
        }
    }

    public String getMyFilePath(Uri uri)
    {

        // DocumentProvider
        if(DocumentsContract.isDocumentUri(_context, uri))
        {
            // ExternalStorageProvider
            if(isExternalStorageDocument(uri))
            {
                final String docId=DocumentsContract.getDocumentId(uri);
                final String[] split=docId.split(":");
                final String type=split[0];

                if("primary".equalsIgnoreCase(type))
                {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if(isDownloadsDocument(uri))
            {
                final String id=DocumentsContract.getDocumentId(uri);
                final Uri contentUri=ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.parseLong(id));
                return getDataColumn(_context, contentUri, null, null);
            }
            // MediaProvider
            else if(isMediaDocument(uri))
            {
                final String docId=DocumentsContract.getDocumentId(uri);
                final String[] split=docId.split(":");
                Uri contentUri = getUri(split);
                final String selection="_id=?";
                final String[] selectionArgs=new String[]{split[1]};
                return getDataColumn(_context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if("content".equalsIgnoreCase(uri.getScheme()))
        {
            // Return the remote address
            if(isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(_context, uri, null, null);
        }
        // File
        else if("file".equalsIgnoreCase(uri.getScheme()))
        {
            return uri.getPath();
        }
        return null;
    }

    private static @Nullable Uri getUri(String[] split) {
        final String type= split[0];
        Uri contentUri=null;
        if("image".equals(type))
        {
            contentUri=MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else if("video".equals(type))
        {
            contentUri=MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        } else if("audio".equals(type))
        {
            contentUri=MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }
        return contentUri;
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs)
    {
        Cursor cursor=null;
        final String column="_data";
        final String[] projection={column};
        try
        {
            cursor=context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if(cursor != null && cursor.moveToFirst())
            {
                final int index=cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        }
        finally
        {
            if(cursor != null)
                cursor.close();
        }
        return null;
    }

    private static boolean isExternalStorageDocument(Uri uri)
    {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri)
    {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri)
    {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private static boolean isGooglePhotosUri(Uri uri)
    {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    // Returns: true(worked)/false(failed)
    public boolean CopyFileToLocalDir(String holidayName, Uri argFromUri, String newFilename)
    {
        try
        {
            _context.grantUriPermission("com.example.des.hp", argFromUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            InputStream in=_context.getContentResolver().openInputStream(argFromUri);
            if(in == null)
                throw new Exception("Unable to open for read" + argFromUri.getPath());

            String holidayFileDir = ImageUtils.imageUtils().GetHolidayFileDir(holidayName);
            File f99=new File(holidayFileDir);
            if(!f99.exists())
            {
                if(!f99.mkdir())
                {
                    myMessages().ShowMessageWithOk("DatabaseAccess()", "Unable to create directory " + f99.getName(), null);
                }
            }

            File tof=new File(holidayFileDir + "/" + newFilename);
            if(tof.exists())
                return (false);

            Uri toUri=Uri.fromFile(tof);
            _context.grantUriPermission("com.example.des.hp", toUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            OutputStream out=_context.getContentResolver().openOutputStream(toUri);
            if(out == null)
                throw new Exception("Unable to open for write" + tof);

            byte[] buffer=new byte[1024];
            int read;
            while((read=in.read(buffer)) != -1)
            {
                out.write(buffer, 0, read);
            }
            in.close();

            // write the output file (You have now copied the file)
            out.flush();
            out.close();

            return (true);
        }
        catch(Exception e)
        {
            ShowError("CopyFileToLocalDir", e.getMessage());
        }
        return (false);
    }

}
