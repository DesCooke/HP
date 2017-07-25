package com.example.des.hp.ExtraFiles;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBar;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.TextView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.myutils.*;
import com.example.des.hp.R;

import java.io.File;
import java.io.InputStream;

public class ExtraFilesDetailsEdit extends BaseActivity
{
    
    public DatabaseAccess databaseAccess;
    private final int SELECT_PHOTO = 1;
    private final int SELECT_PDF = 2;
    private ImageView imageViewSmall;
    private String action;
    public int fileGroupId;
    public int fileId;
    public TextView fileDescription;
    public TextView fileName;
    public ActionBar actionBar;
    public ExtraFilesItem extraFilesItem;
    public CheckBox cbPicturePicked;
    private ImageUtils imageUtils;
    private MyFileUtils myFileUtils;
    private Uri mySelectedFileUri;
    public boolean FileSelected;
    public MyMessages myMessages;
    
    //region Yes/No dialog
    public DialogWithYesNoFragment dialogWithYesNoFragment;
    public String dwynDialogTag;
    public View.OnClickListener dwynOnYesClick;
    public View.OnClickListener dwynOnNoClick;
    //endregion
    
    //region Rename Edit Text dialog
    public DialogWithEditTextFragment dialogWithEditTextFragment;
    public String dwetDialogTag;
    public View.OnClickListener dwetOnOkClick;
    //endregion
    
    public String origFilename;
    public String newFilename;
    
    
    public void pickImage(View view)
    {
        try
        {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, SELECT_PHOTO);
        }
        catch (Exception e)
        {
            ShowError("pickImage", e.getMessage());
        }
    }
    
    public void pickPDF(View view)
    {
        try
        {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            
            startActivityForResult(
                Intent.createChooser(intent, "Select a File to Upload"),
                SELECT_PDF);
        }
        catch (Exception e)
        {
            // Potentially direct the user to the Market with a Dialog
            ShowError("pickPDF", e.getMessage());
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
                case SELECT_PHOTO:
                    if (resultCode == RESULT_OK)
                    {
                        try
                        {
                            MyBitmap myBitmap = new MyBitmap();
                            Boolean lRetCode =
                                imageUtils.ScaleBitmapFromUrl
                                    (
                                        imageReturnedIntent.getData(),
                                        getContentResolver(),
                                        myBitmap
                                    );
                            if (lRetCode == false)
                                return;
                            
                            // assign new bitmap and set scale type
                            imageViewSmall.setImageBitmap(myBitmap.Value);
                            
                            cbPicturePicked.setChecked(true);
                            
                            extraFilesItem.pictureChanged = true;
                            
                            
                        }
                        catch (Exception e)
                        {
                            ShowError("onActivityResult-selectphoto", e.getMessage());
                        }
                    }
                    break;
                case SELECT_PDF:
                    if (resultCode == RESULT_OK)
                    {
                        final Uri imageUri = imageReturnedIntent.getData();
                        grantUriPermission("com.example.des.hp", imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        mySelectedFileUri = imageUri;
                        extraFilesItem.fileChanged = true;
                        MyString myString = new MyString();
                        if (myFileUtils.BaseFilenameFromUri(imageUri, myString) == false)
                            return;
                        fileName.setText(myString.Value);
                    }
                    break;
                
            }
        }
        catch (Exception e)
        {
            ShowError("onActivityResult", e.getMessage());
        }
    }
    
    public void clearImage(View view)
    {
        try
        {
            cbPicturePicked.setChecked(false);
            imageViewSmall.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.imagemissing));
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
            clearImage(view);
            extraFilesItem.pictureChanged = true;
        }
        catch (Exception e)
        {
            ShowError("btnClearImage", e.getMessage());
        }
    }
    
    
    public void SaveIt()
    {
        try
        {
            extraFilesItem.pictureAssigned = cbPicturePicked.isChecked();
            extraFilesItem.fileDescription = fileDescription.getText().toString();
            if (extraFilesItem.fileChanged)
                extraFilesItem.fileName = newFilename;
            extraFilesItem.fileBitmap = null;
            if (extraFilesItem.pictureAssigned)
                extraFilesItem.fileBitmap = ((BitmapDrawable) imageViewSmall.getDrawable()).getBitmap();
            
            if (extraFilesItem.fileName == null)
            {
                myMessages.ShowMessageShort("Need to select a file first... ");
                return;
            }
            
            myMessages.ShowMessageShort("Saving " + fileDescription.getText().toString());
            
            if (action.equals("add"))
            {
                MyInt myInt = new MyInt();
                
                extraFilesItem.fileGroupId = fileGroupId;
                
                if (!databaseAccess.getNextExtraFilesId(fileGroupId, myInt))
                    return;
                extraFilesItem.fileId = myInt.Value;
                
                if (!databaseAccess.getNextExtraFilesSequenceNo(fileGroupId, myInt))
                    return;
                extraFilesItem.sequenceNo = myInt.Value;
                
                if (!databaseAccess.addExtraFilesItem(extraFilesItem))
                    return;
            }
            
            if (action.equals("modify"))
            {
                extraFilesItem.fileGroupId = fileGroupId;
                extraFilesItem.fileId = fileId;
                if (!databaseAccess.updateExtraFilesItem(extraFilesItem))
                    return;
            }
            
            finish();
        }
        catch (Exception e)
        {
            ShowError("SaveIt", e.getMessage());
        }
    }
    
    public void saveExtraFile(View view)
    {
        try
        {
            if (extraFilesItem.fileChanged == false)
            {
                SaveIt();
                return;
            }
            
            if (mySelectedFileUri != null)
            {
                grantUriPermission("com.example.des.hp", mySelectedFileUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                try
                {
                    InputStream in = getContentResolver().openInputStream(mySelectedFileUri);
                }
                catch (Exception e)
                {
                    ShowError("saveExtraFile", e.getMessage());
                    return;
                }
                extraFilesItem.fileUri = mySelectedFileUri;
                
                MyString myString = new MyString();
                if (myFileUtils.BaseFilenameFromUri(mySelectedFileUri, myString) == false)
                    return;
                newFilename = myString.Value;
                
                File tof = new File(getResources().getString(R.string.files_path) + "/" + newFilename);
                if (tof.exists())
                {
                    origFilename = newFilename;
                    newFilename = "";
                    // offer a rename option
                    handleRename(tof.getName());
                } else
                {
                    SaveIt();
                }
            } else
            {
                newFilename = "";
                SaveIt();
            }
        }
        catch (Exception e)
        {
            ShowError("SaveExtraFile", e.getMessage());
        }
        
    }
    
    public void ExtraFilesNamePicked(View view)
    {
        try
        {
            fileDescription.setText(dialogWithEditTextFragment.getFinalText());
            
            dialogWithEditTextFragment.dismiss();
        }
        catch (Exception e)
        {
            ShowError("ExtraFilesNamePicked", e.getMessage());
        }
    }
    
    public void pickExtraFilesName(View view)
    {
        try
        {
            dwetOnOkClick = new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    ExtraFilesNamePicked(view);
                }
            };
            
            dialogWithEditTextFragment =
                DialogWithEditTextFragment.newInstance
                    (
                        getFragmentManager(),     // for the transaction bit
                        "hihi",            // unique name for this dialog type
                        "File",    // form caption
                        "Description",             // form message
                        R.drawable.attachment,
                        fileDescription.getText().toString(), // initial text
                        dwetOnOkClick,
                        this,
                        false
                    );
            
            dialogWithEditTextFragment.showIt();
        }
        catch (Exception e)
        {
            ShowError("pickExtraFilesName", e.getMessage());
        }
    }
    
    public void dwetOnOkClickProc(View view)
    {
        try
        {
            newFilename = dialogWithEditTextFragment.getFinalText();
            
            myMessages.ShowMessageShort("Renaming to " + newFilename);
            
            dialogWithEditTextFragment.dismiss();
            
            SaveIt();
        }
        catch (Exception e)
        {
            ShowError("dwetOnOkClickProc", e.getMessage());
        }
    }
    
    // Create a YES onclick procedure
    public void dwynOnYesClickProc(View view)
    {
        try
        {
            dialogWithYesNoFragment.dismiss();
            
            dwetOnOkClick = new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    dwetOnOkClickProc(view);
                }
            };
            
            dialogWithEditTextFragment =
                DialogWithEditTextFragment.newInstance
                    (
                        getFragmentManager(),     // for the transaction bit
                        dwetDialogTag,            // unique name for this dialog type
                        "New Filename",    // form caption
                        "Filename",             // form message
                        R.drawable.attachment,
                        origFilename,                // initial text
                        dwetOnOkClick,
                        this,
                        false
                    );
            
            dialogWithEditTextFragment.showIt();
        }
        catch (Exception e)
        {
            ShowError("dwynOnyesClickProc", e.getMessage());
        }
    }
    
    // Create a NO onclick procedure
    public void dwynOnNoClickProc(View view)
    {
        try
        {
            // When button is clicked close the dialog
            dialogWithYesNoFragment.dismiss();
        }
        catch (Exception e)
        {
            ShowError("dwynOnNoClickProc", e.getMessage());
        }
    }
    
    public void handleRename(String origFilename)
    {
        try
        {
            String newFilename;
            
            dwynOnYesClick = new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    dwynOnYesClickProc(view);
                }
            };
            // create a no on-click listener to call your procedure
            dwynOnNoClick = new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    dwynOnNoClickProc(view);
                }
            };
            
            
            dialogWithYesNoFragment =
                DialogWithYesNoFragment.newInstance
                    (
                        getFragmentManager(),     // for the transaction bit
                        dwynDialogTag,            // unique name for this dialog type
                        "File Already Exists",    // form caption
                        "Rename " + origFilename + "?", // form message
                        R.drawable.attachment,
                        dwynOnYesClick,
                        dwynOnNoClick,
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
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        try
        {
            setContentView(R.layout.activity_extra_files_details_edit);
            
            newFilename = "";
            
            databaseAccess = new DatabaseAccess(this);
            actionBar = getSupportActionBar();
            imageUtils = new ImageUtils(this);
            myFileUtils = new MyFileUtils(this);
            myMessages = new MyMessages(this);
            
            dwynDialogTag = getResources().getString(R.string.dwynDialogTag);
            dwetDialogTag = getResources().getString(R.string.dwetDialogTag);
            
            cbPicturePicked = (CheckBox) findViewById(R.id.picturePicked);
            imageViewSmall = (ImageView) findViewById(R.id.imageViewSmall);
            fileDescription = (TextView) findViewById(R.id.txtFileDescription);
            fileName = (TextView) findViewById(R.id.txtFilename);
            
            clearImage(null);
            
            Bundle extras = getIntent().getExtras();
            if (extras != null)
            {
                String title = extras.getString("TITLE");
                String subtitle = extras.getString("SUBTITLE");
                actionBar.setTitle(title);
                action = extras.getString("ACTION");
                if (action != null && action.equals("add"))
                {
                    extraFilesItem = new ExtraFilesItem();
                    fileGroupId = extras.getInt("FILEGROUPID");
                    fileDescription.setText("");
                    fileName.setText("");
                    cbPicturePicked.setChecked(false);
                    actionBar.setSubtitle("Add a File");
                }
                if (action != null && action.equals("modify"))
                {
                    fileGroupId = extras.getInt("FILEGROUPID");
                    fileId = extras.getInt("FILEID");
                    extraFilesItem = new ExtraFilesItem();
                    if (!databaseAccess.getExtraFilesItem(fileGroupId, fileId, extraFilesItem))
                        return;
                    
                    fileDescription.setText(extraFilesItem.fileDescription);
                    fileName.setText(extraFilesItem.fileName);
                    
                    String originalFileName = extraFilesItem.filePicture;
                    
                    if (imageUtils.getPageHeaderImage(this, extraFilesItem.filePicture, imageViewSmall) == false)
                        return;
                    
                    cbPicturePicked.setChecked(extraFilesItem.pictureAssigned);
                    
                    actionBar.setSubtitle(subtitle);
                }
            }
        }
        catch (Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
        
    }
/*
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try
        {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.daydetailsformmenu, menu);
        }
        catch(Exception e)
        {
            ShowError("onCreateOptionsMenu", e.getMessage());
        }
        return true;
    }
*/
}
