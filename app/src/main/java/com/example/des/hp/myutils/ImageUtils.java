package com.example.des.hp.myutils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;

import androidx.annotation.NonNull;
import androidx.exifinterface.media.ExifInterface;
import android.net.Uri;
import android.widget.ImageView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Holiday.HolidayItem;
import com.example.des.hp.InternalFiles.InternalFileItem;
import com.example.des.hp.InternalImages.InternalImageItem;
import com.example.des.hp.MainActivity;
import com.example.des.hp.R;
import com.example.des.hp.thirdpartyutils.*;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
import static com.example.des.hp.myutils.MyApiSpecific.myApiSpecific;
import static com.example.des.hp.myutils.MyMessages.myMessages;

/** @noinspection ResultOfMethodCallIgnored*/
public class ImageUtils
{
    private final Context _context;
    private final Resources res;
    @SuppressLint("StaticFieldLeak")
    private static ImageUtils myImageUtils = null;
    
    public ImageUtils(Context context)
    {
        _context = context;
        res = context.getResources();
    }
    
    public static ImageUtils imageUtils()
    {
        if (myImageUtils == null)
            myImageUtils = new ImageUtils(MainActivity.getInstance());
        
        return (myImageUtils);
    }
    
    private void ShowError(String argFunction, String argMessage)
    {
        myMessages().ShowError("Error in ImageUtils::" + argFunction, argMessage);
    }
    
    //
    // resizes image to 100x100
    // makes it a circle
    // puts image in destImageView
    // if no image - default 'imagemissing' is used
    // Returns: true(worked)/false(failed)
    //
    public boolean getListIcon(int holidayId, Context context, String argFilename, ImageView destImageView)
    {
        MyBoolean lValid = new MyBoolean();
        if (!validFilename(holidayId, argFilename, lValid))
            return (false);
        try
        {
            if (lValid.Value)
            {
                Uri uri = Uri.fromFile(new File(GetHolidayImageDir(holidayId) + "/" + argFilename));
                
                Picasso.with(context).load(uri).resize(100, 100).transform(new CircleTransform()).into(destImageView);
            } else
            {
                Picasso.with(context).load(R.drawable.imagemissing).resize(100, 100).transform(new CircleTransform()).into(destImageView);
            }
            return (true);
        }
        catch (Exception e)
        {
            ShowError("getListIcon", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getGridIcon(int holidayId, Context context, String argFilename, ImageView destImageView)
    {
        MyBoolean lValid = new MyBoolean();
        if (!validFilename(holidayId, argFilename, lValid))
            return (false);
        try
        {
            if (lValid.Value)
            {
                Uri uri = Uri.fromFile(new File(GetHolidayImageDir(holidayId) + "/" + argFilename));
                
                Picasso.with(context).load(uri)
                    //    .resize(150, 150)
                    //.transform(new CircleTransform())
                    .into(destImageView);
            } else
            {
                Picasso.with(context).load(R.drawable.imagemissing)
                    //    .resize(150, 150)
                    //.transform(new CircleTransform())
                    .into(destImageView);
            }
            return (true);
        }
        catch (Exception e)
        {
            ShowError("getListIcon", e.getMessage());
        }
        return (false);
        
    }

    public String GetHolidayDirFromHolidayItem(HolidayItem holidayItem)
    {
        String holidayDirName = getHolidayDirString(holidayItem);

        String lDirName = MyFileUtils.MyDocuments();
        File lFile = new File(lDirName);
        if (!lFile.isDirectory())
            lFile.mkdir();

        lDirName = MyFileUtils.MyDocuments() + "/" +
                res.getString(R.string.application_file_path);
        lFile = new File(lDirName);
        if (!lFile.isDirectory())
            lFile.mkdir();

        lDirName = MyFileUtils.MyDocuments() + "/" +
                res.getString(R.string.application_file_path) + "/" +
                holidayDirName;
        lFile = new File(lDirName);
        if (!lFile.isDirectory())
            lFile.mkdir();

        return(lDirName);
    }


    public String GetHolidayDir(int holidayId)
    {
        HolidayItem holidayItem = new HolidayItem();
        try(DatabaseAccess da = databaseAccess())
        {
            da.getHolidayItem(holidayId, holidayItem);
        }
        String holidayDirName = getHolidayDirString(holidayItem);

        String lDirName = MyFileUtils.MyDocuments();
        File lFile = new File(lDirName);
        if (!lFile.isDirectory())
            lFile.mkdir();

        lDirName = MyFileUtils.MyDocuments() + "/" +
                res.getString(R.string.application_file_path);
        lFile = new File(lDirName);
        if (!lFile.isDirectory())
            lFile.mkdir();

        lDirName = MyFileUtils.MyDocuments() + "/" +
                res.getString(R.string.application_file_path) + "/" +
                holidayDirName;
        lFile = new File(lDirName);
        if (!lFile.isDirectory())
            lFile.mkdir();

        return(lDirName);
    }

    private static @NonNull String getHolidayDirString(HolidayItem holidayItem) {
        String holidayDirName= holidayItem.holidayName.replace(' ', '_');
        holidayDirName=holidayDirName.replace('#', '_');
        holidayDirName=holidayDirName.replace('%', '_');
        holidayDirName=holidayDirName.replace('&', '_');
        holidayDirName=holidayDirName.replace('{', '_');
        holidayDirName=holidayDirName.replace('}', '_');
        holidayDirName=holidayDirName.replace('\\', '_');
        holidayDirName=holidayDirName.replace('*', '_');
        holidayDirName=holidayDirName.replace('?', '_');
        holidayDirName=holidayDirName.replace('/', '_');
        holidayDirName=holidayDirName.replace('$', '_');
        holidayDirName=holidayDirName.replace('!', '_');
        holidayDirName=holidayDirName.replace('"', '_');
        holidayDirName=holidayDirName.replace('@', '_');
        holidayDirName=holidayDirName.replace(':', '_');
        holidayDirName=holidayDirName.replace('!', '_');
        holidayDirName=holidayDirName.replace('=', '_');
        return holidayDirName;
    }

    public String GetHolidayImageDir(int holidayId)
    {
        String lDirName = GetHolidayDir(holidayId) + "/" +
                res.getString(R.string.picture_path);
        File lFile = new File(lDirName);
        if (!lFile.isDirectory())
            lFile.mkdir();

        return(lDirName);
    }

    public String GetHolidayFileDir(int holidayId)
    {
        String lDirName = GetHolidayDir(holidayId) + "/" +
                res.getString(R.string.files_path);
        File lFile = new File(lDirName);
        if (!lFile.isDirectory())
            lFile.mkdir();

        return(lDirName);
    }


    public ArrayList<InternalImageItem> listInternalImages(int holidayId)
    {
        try
        {

            ArrayList<InternalImageItem> l_array = new ArrayList<>();

            if(holidayId==0)
                return l_array;

            String imageDir = GetHolidayImageDir(holidayId);

            File directory = new File(imageDir);
            File[] files = directory.listFiles();
            
            if (files == null)
                return (null);
            
            if (files.length == 0)
                return (null);
            
            Arrays.sort(files,
                    (o1, o2) -> {

                        String s1 = o1.getName();
                        String s1a = s1.replace(".png", "");
                        String[] sa1 = s1a.split("_");
                        String n1 = sa1[1];
                        int num1 = Integer.parseInt(n1);

                        String s2 = o2.getName();
                        String s2a = s2.replace(".png", "");
                        String[] sa2 = s2a.split("_");
                        String n2 = sa2[1];
                        int num2 = Integer.parseInt(n2);

                        return num1 - num2;
                    });
            
            for (File file : files)
            {
                l_array.add(new InternalImageItem(file.getName(), holidayId));
            }
            return (l_array);
        }
        catch (Exception e)
        {
            ShowError("listInternalImages", e.getMessage());
        }
        return (null);
    }
    
    public ArrayList<InternalFileItem> listInternalFiles(int holidayId)
    {
        try
        {
            ArrayList<InternalFileItem> l_array = new ArrayList<>();
            
            File directory = new File(GetHolidayFileDir(holidayId));
            File[] files = directory.listFiles();
            
            if (files == null)
                return (null);
            
            if (files.length == 0)
                return (null);
            
            Arrays.sort(files);
            
            for (File file : files)
            {
                l_array.add(new InternalFileItem(holidayId, file.getName()));
            }
            return (l_array);
        }
        catch (Exception e)
        {
            ShowError("listInternalFiles", e.getMessage());
        }
        return (null);
    }
    

    //
    // Checks to make sure a filename exists
    // Returns: true(worked)/false(failed)
    //
    private boolean validFilename(int holidayId, String filename, MyBoolean retBoolean)
    {
        try
        {
            if (filename.isEmpty())
                return (false);
            File f = new File(GetHolidayImageDir(holidayId) + "/" + filename);
            retBoolean.Value = f.exists();
            return (true);
        }
        catch (Exception e)
        {
            ShowError("validFilename", e.getMessage());
        }
        return (false);
        
    }
    
    /*
    ** resizes image to 256x256
    ** puts image in destImageView
    ** if no image - default 'imagemissing' is used
    ** Returns: true(worked)/false(failed)
    */
    public boolean getLargeListIcon(int holidayId, Context context, String argFilename, ImageView destImageView)
    {
        MyBoolean lValid = new MyBoolean();
        
        if (!validFilename(holidayId, argFilename, lValid))
            return (false);
        
        try
        {
            if (lValid.Value)
            {
                Uri uri = Uri.fromFile(new File(GetHolidayImageDir(holidayId) + "/" + argFilename));
                
                Picasso.with(context).load(uri).resize(256, 256).into(destImageView);
            } else
            {
                Picasso.with(context).load(R.drawable.imagemissing).resize(256, 256).into(destImageView);
            }
            return (true);
        }
        catch (Exception e)
        {
            ShowError("getLargeListIcon", e.getMessage());
        }
        return (false);
    }
    
    /*
    ** resizes image to 256x256
    ** puts image in destImageView
    ** if no image - default 'imagemissing' is used
    ** Returns: true(worked)/false(failed)
    */
    public boolean getPageHeaderImage(int holidayId, Context context, String argFilename, ImageView destImageView)
    {
        MyBoolean lValid = new MyBoolean();
        
        if (argFilename.isEmpty())
            return (true);
        
        if (!validFilename(holidayId, argFilename, lValid))
            return (false);
        
        try
        {
            if (lValid.Value)
            {
                Uri uri = Uri.fromFile(new File(GetHolidayImageDir(holidayId) + "/" + argFilename));
                
                MyBitmap myBitmap = new MyBitmap();
                if (!ScaleBitmapFromUrl(uri, _context.getContentResolver(), myBitmap))
                    return (false);
                // assign new bitmap and set scale type
                destImageView.setImageBitmap(myBitmap.Value);
//                Picasso.with(context).load(uri).resize(512, 512).into(destImageView);
            } else
            {
                Picasso.with(context).load(R.drawable.imagemissing).resize(512, 512).into(destImageView);
            }
            return (true);
        }
        catch (Exception e)
        {
            ShowError("getPageHeaderImage", e.getMessage());
        }
        return (false);
    }
    
    // Returns: true(worked)/false(failed)
    private boolean ScaleKeepingAspectRatio(Point currPoint, Point idealPoint, Point retPoint)
    {
        try
        {
            if (currPoint.x > currPoint.y)
            {
                // x = width, y = height
                // picture is wider than height
                // make the width the ideal width and calculate height
                retPoint.x = idealPoint.x;
                retPoint.y = (((currPoint.y * 10000) / currPoint.x) * idealPoint.x) / 10000;
            } else
            {
                retPoint.y = idealPoint.y;
                retPoint.x = (((currPoint.x * 10000) / currPoint.y) * idealPoint.y) / 10000;
            }
            return (true);
        }
        catch (Exception e)
        {
            ShowError("ScaleKeepingAspectRatio", e.getMessage());
        }
        return (false);
    }
    
    // Returns: true(worked)/false(failed)
    public boolean ScaleBitmapFromUrl(Uri imageUri, ContentResolver cr, MyBitmap retBitmap)
    {
        try
        {
            _context.grantUriPermission("com.example.des.hp", imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            InputStream imageStream = cr.openInputStream(imageUri);
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            
            int orientation = myApiSpecific().GetImageOrientation(imageUri);
            
            Bitmap finalImage;
            
            switch (orientation)
            {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    finalImage = rotateImage(selectedImage, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    finalImage = rotateImage(selectedImage, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    finalImage = rotateImage(selectedImage, 270);
                    break;
                default:
                    finalImage = selectedImage;
            }
            
            Point lCurrPoint = new Point(finalImage.getWidth(), finalImage.getHeight());
            Point lIdealPoint = new Point(512, 512);
            Point lNewPoint = new Point(0, 0);
            if (!ScaleKeepingAspectRatio(lCurrPoint, lIdealPoint, lNewPoint))
                return (false);
            retBitmap.Value = Bitmap.createScaledBitmap(finalImage, lNewPoint.x, lNewPoint.y, false);
            
            return (true);
        }
        catch (Exception e)
        {
            ShowError("ScaleBitmapFromUrl", e.getMessage());
        }
        return (false);
    }
    
    public boolean ScaleBitmapFromFile(int holidayId, String lfile, ContentResolver cr, MyBitmap retBitmap)
    {
        try
        {
            Uri uri = Uri.fromFile(new File(GetHolidayImageDir(holidayId) + "/" + lfile));
            
            return(ScaleBitmapFromUrl(uri, cr, retBitmap));
        }
        catch (Exception e)
        {
            ShowError("ScaleBitmapFromFile", e.getMessage());
        }
        return (false);
    }
    
    private static Bitmap rotateImage(Bitmap img, int degree)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }
    
    
}
