package com.example.des.hp.myutils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.widget.ImageView;

import com.example.des.hp.R;
import com.example.des.hp.thirdpartyutils.*;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.InputStream;

public class ImageUtils
{
    private Context _context;
    private Resources res;
    private MyMessages myMessages;

    public ImageUtils(Context context)
    {
        _context = context;
        res = context.getResources();
        myMessages = new MyMessages(_context);
    }

    private void ShowError(String argFunction, String argMessage)
    {
        myMessages.ShowError
                (
                        "Error in ImageUtils::" + argFunction,
                        argMessage
                );
    }

    //
    // resizes image to 100x100
    // makes it a circle
    // puts image in destImageView
    // if no image - default 'imagemissing' is used
    // Returns: true(worked)/false(failed)
    //
    public boolean getListIcon(Context context, String argFilename, ImageView destImageView)
    {
        MyBoolean lValid = new MyBoolean();
        if(validFilename(argFilename, lValid)==false)
            return(false);
        try
        {
            if (lValid.Value == true)
            {
                Uri uri = Uri.fromFile(new File(res.getString(R.string.picture_path) + "/" + argFilename));

                Picasso.with(context)
                        .load(uri)
                        .resize(100, 100)
                        .transform(new CircleTransform())
                        .into(destImageView);
            } else
            {
                Picasso.with(context)
                        .load(R.drawable.imagemissing)
                        .resize(100, 100)
                        .transform(new CircleTransform())
                        .into(destImageView);
            }
            return(true);
        }
        catch (Exception e)
        {
            ShowError("getListIcon", e.getMessage());
            return(false);
        }

    }

    //
    // Checks to make sure a filename exists
    // Returns: true(worked)/false(failed)
    //
    private boolean validFilename(String filename, MyBoolean retBoolean)
    {
        try
        {
            if (filename.length() == 0)
                return (false);
            File f = new File(res.getString(R.string.picture_path) + "/" + filename);
            retBoolean.Value = f.exists();
            return(true);
        }
        catch (Exception e)
        {
            ShowError("validFilename", e.getMessage());
            return(false);
        }

    }
    /*
    ** resizes image to 256x256
    ** puts image in destImageView
    ** if no image - default 'imagemissing' is used
    ** Returns: true(worked)/false(failed)
    */
    public boolean getLargeListIcon(Context context, String argFilename, ImageView destImageView)
    {
        MyBoolean lValid = new MyBoolean();

        if(validFilename(argFilename, lValid)==false)
            return(false);

        try
        {
            if (lValid.Value == true)
            {
                Uri uri = Uri.fromFile(new File(res.getString(R.string.picture_path) + "/" + argFilename));

                Picasso.with(context)
                        .load(uri)
                        .resize(256, 256)
                        .into(destImageView);
            } else
            {
                Picasso.with(context)
                        .load(R.drawable.imagemissing)
                        .resize(256, 256)
                        .into(destImageView);
            }
            return(true);
        }
        catch (Exception e)
        {
            ShowError("getLargeListIcon", e.getMessage());
            return(false);
        }

    }

    /*
    ** resizes image to 256x256
    ** puts image in destImageView
    ** if no image - default 'imagemissing' is used
    ** Returns: true(worked)/false(failed)
    */
    public boolean getPageHeaderImage(Context context, String argFilename, ImageView destImageView)
    {
        MyBoolean lValid = new MyBoolean();

        if(argFilename.length()==0)
            return(true);

        if(validFilename(argFilename, lValid)==false)
            return(false);

        try
        {
            if (lValid.Value == true)
            {
                Uri uri = Uri.fromFile(new File(res.getString(R.string.picture_path) + "/" + argFilename));

                Picasso.with(context)
                        .load(uri)
                        .resize(512, 512)
                        .into(destImageView);
            } else
            {
                Picasso.with(context)
                        .load(R.drawable.imagemissing)
                        .resize(512, 512)
                        .into(destImageView);
            }
            return(true);
        }
        catch (Exception e)
        {
            ShowError("getPageHeaderImage", e.getMessage());
            return(false);
        }
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
            }
            else
            {
                retPoint.y = idealPoint.y;
                retPoint.x = (((currPoint.x * 10000) / currPoint.y) * idealPoint.y) / 10000;
            }
            return(true);
        }
        catch (Exception e)
        {
            ShowError("ScaleKeepingAspectRatio", e.getMessage());
            return(false);
        }
    }

    // Returns: true(worked)/false(failed)
    public boolean ScaleBitmapFromUrl(Uri imageUri, ContentResolver cr, MyBitmap retBitmap)
    {
        try
        {
            _context.grantUriPermission("com.example.des.hp", imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            InputStream imageStream = cr.openInputStream(imageUri);
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

            Point lCurrPoint = new Point(selectedImage.getWidth(), selectedImage.getHeight());
            Point lIdealPoint = new Point(512, 512);
            Point lNewPoint = new Point(0, 0);
            if (ScaleKeepingAspectRatio(lCurrPoint, lIdealPoint, lNewPoint) == false)
                return (false);
            retBitmap.Value = Bitmap.createScaledBitmap(selectedImage, lNewPoint.x, lNewPoint.y, false);

            return (true);
        }
        catch (Exception e)
        {
            ShowError("ScaleBitmapFromUrl", e.getMessage());
            return(false);
        }
    }
}
