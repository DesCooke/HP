package com.example.des.hp.Dialog;

/*
** BASEACTIVITY->BASEVIEW
**
** Basically - just manages an ImageView - does not let you click it - just view it
**   The image has to be called imageViewSmall
**   There has to be a Checkbox (visible or invisible) called picturePicked
**     determines if there is a picture or not
**
** Picture picked defaults to false and the image defaults to the default one
**
** Call SetImage with the filename of the image to set it
** If the filename is blank or there is a problem loading it - the image goes back to the
** default one and the picture Picked is set to false
**
*/

import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.example.des.hp.R;
import static com.example.des.hp.myutils.ImageUtils.imageUtils;

public class BaseView extends BaseActivity
{
    public ImageView imageView;
    public boolean imagePicked;
    public Bitmap imageDefault;
    
    public void clearImage()
    {
        try
        {
            imagePicked = false;
            imageView.setImageBitmap(imageDefault);
        }
        catch (Exception e)
        {
            ShowError("clearImage", e.getMessage());
        }
    }
    
    @Override
    public void afterCreate()
    {
        super.afterCreate();
        
        try
        {
            imageDefault = BitmapFactory.decodeResource(getResources(), R.drawable.imagemissing);
            imageView = (ImageView) findViewById(R.id.imageViewSmall);
        }
        catch (Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
    }
    
    public void SetImage(String picture)
    {
        imagePicked = false;
        try
        {
            clearImage();
            
            if(picture != null && picture.length()>0)
            {
                if (!imageUtils().getPageHeaderImage(this, picture, imageView))
                    return;
                imagePicked = true;
            }
            
        }
        catch (Exception e)
        {
            ShowError("SetImage", e.getMessage());
        }
    }
    
    public void showForm()
    {
        try
        {
            clearImage();
            SetTitles(title, subTitle);
        }
        
        catch (Exception e)
        {
            ShowError("showForm", e.getMessage());
        }
    }
    
    
}
