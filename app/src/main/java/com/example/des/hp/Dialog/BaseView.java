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

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.des.hp.InternalImages.InternalImageList;
import com.example.des.hp.R;
import com.example.des.hp.myutils.DialogWithYesNoFragment;
import com.example.des.hp.myutils.MyBitmap;

import static com.example.des.hp.myutils.ImageUtils.imageUtils;
import static com.example.des.hp.myutils.MyMessages.myMessages;

public class BaseView extends BaseActivity
{
    private final int SELECT_DEVICE_PHOTO = 1;
    private final int SELECT_INTERNAL_PHOTO = 3;
    public ImageView imageView;
    public boolean imageSet = false;
    public boolean imageChanged = false;
    public Bitmap imageDefault;
    public DialogWithYesNoFragment dialogWithYesNoFragment;
          
    
    public void clearImage()
    {
        try
        {
            imageView.setImageBitmap(imageDefault);
            imageSet = false;
            imageChanged = true;
        }
        catch (Exception e)
        {
            ShowError("clearImage", e.getMessage());
        }
    }
    
    public void btnClearImage(View view)
    {
        try
        {
            clearImage();
        }
        catch (Exception e)
        {
            ShowError("btnClearImage", e.getMessage());
        }
    }
    
    
    public void selectFromDevice(View view)
    {
        try
        {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, SELECT_DEVICE_PHOTO);
        }
        catch (Exception e)
        {
            ShowError("pickImage", e.getMessage());
        }
    }

    public void selectFromApplication(View view)
    {
        try
        {
            Intent intent=new Intent(getApplicationContext(), InternalImageList.class);
            startActivityForResult(intent, SELECT_INTERNAL_PHOTO);
        }
        catch(Exception e)
        {
            ShowError("showDayAdd", e.getMessage());
        }
    }

    public void pickImage(View view)
    {
        try
        {
            dialogWithYesNoFragment =
                DialogWithYesNoFragment.newInstance
                    (
                        getFragmentManager(),     // for the transaction bit
                        "SELECTPICTURELOCATION2",        // unique name for this dialog type
                        "Select Picture Location",            // unique name for this dialog type
                        "Yes=Device, No=Images already stored", // form message
                        R.drawable.attachment,
                        new View.OnClickListener()
                        {
                            public void onClick(View view)
                            {
                                dialogWithYesNoFragment.dismiss();
                                selectFromDevice(view);
                            }
                        },
                        new View.OnClickListener()
                        {
                            public void onClick(View view)
                            {
                                dialogWithYesNoFragment.dismiss();
                                selectFromApplication(view);
                            }
                        },
                        this
                    );
            
            dialogWithYesNoFragment.showIt();
        }
        catch (Exception e)
        {
            ShowError("handleRename", e.getMessage());
        }
    }
    
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent)
    {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        try
        {
            switch (requestCode)
            {
                case SELECT_DEVICE_PHOTO:
                    if (resultCode == RESULT_OK)
                    {
                        try
                        {
                            MyBitmap myBitmap = new MyBitmap();
                            Uri luri = imageReturnedIntent.getData();
                            Boolean lRetCode = imageUtils().ScaleBitmapFromUrl(luri, getContentResolver(), myBitmap);
                            if (!lRetCode)
                                return;
                            
                            // assign new bitmap and set scale type
                            imageView.setImageBitmap(myBitmap.Value);
                            
                            imageSet = true;
                            reloadOnShow = false;
                            imageChanged = true;
                            
                        }
                        catch (Exception e)
                        {
                            ShowError("onActivityResult-selectphoto", e.getMessage());
                        }
                    }
                    break;
                case SELECT_INTERNAL_PHOTO:
                    if (resultCode == RESULT_OK)
                    {
                        try
                        {
                            MyBitmap myBitmap = new MyBitmap();
                            String lfile = imageReturnedIntent.getStringExtra("selectedfile");
                            Boolean lRetCode = imageUtils().ScaleBitmapFromFile(lfile, getContentResolver(), myBitmap);
                            if (!lRetCode)
                                return;

                            // assign new bitmap and set scale type
                            imageView.setImageBitmap(myBitmap.Value);

                            imageSet = true;
                            reloadOnShow = false;
                            imageChanged = true;

                        }
                        catch (Exception e)
                        {
                            ShowError("onActivityResult-selectphoto", e.getMessage());
                        }
                    }

            }
        }
        catch (Exception e)
        {
            ShowError("onActivityResult", e.getMessage());
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
        try
        {
            clearImage();
            
            if (picture != null && picture.length() > 0)
            {
                if (!imageUtils().getPageHeaderImage(this, picture, imageView))
                    return;
                imageSet = true;
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
