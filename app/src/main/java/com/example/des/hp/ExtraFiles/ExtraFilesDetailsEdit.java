package com.example.des.hp.ExtraFiles;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.example.des.hp.myutils.*;
import com.example.des.hp.R;

import java.io.File;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
import static com.example.des.hp.myutils.MyFileUtils.myFileUtils;
import static com.example.des.hp.myutils.MyMessages.myMessages;

public class ExtraFilesDetailsEdit extends ExtraFilesDetailsView implements View.OnClickListener
{

    //region Member variables

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
    //endregion

    //region Constructors/Destructors
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            btnClear.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.VISIBLE);

            dwynDialogTag = getResources().getString(R.string.dwynDialogTag);
            dwetDialogTag = getResources().getString(R.string.dwetDialogTag);

            imageView.setOnClickListener(this);
            btnFile.setOnClickListener(this);

        }
        catch (Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        /* disable the menu entirely */
        return false;
    }
    //endregion

    //region OnClick Events
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.imageViewSmall:
                pickImage(view);
                break;
            case R.id.btnFile:
                pickPDF(view);
                break;
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
    //endregion

    //region Saving
    public void SaveIt()
    {
        try
        {
            if( mySelectedFileChanged)
            {
                if(mySelectedFileName.length() == 0)
                {
                    myMessages().ShowMessageShort("Need to select a file first... ");
                    return;
                }
            }
            else
            {
                if(extraFilesItem!=null && extraFilesItem.fileName!=null && extraFilesItem.fileName.length()==0)
                {

                    myMessages().ShowMessageShort("Need to select a file first... ");
                    return;
                }
            }

            myMessages().ShowMessageShort("Saving " + mySelectedFileName);

            extraFilesItem.fileDescription = txtFileDescription.getText().toString();
            extraFilesItem.fileChanged=mySelectedFileChanged;
            if (mySelectedFileChanged)
            {
                extraFilesItem.fileName=mySelectedFileName;
            }

            extraFilesItem.fileBitmap = null;
            extraFilesItem.pictureAssigned=imageSet;
            extraFilesItem.pictureChanged=imageChanged;
            extraFilesItem.fileBitmap=null;
            if (imageSet)
                extraFilesItem.fileBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

            extraFilesItem.fileGroupId = fileGroupId;

            if (action.equals("add"))
            {
                MyInt myInt = new MyInt();
                
                if (!databaseAccess().getNextExtraFilesId(fileGroupId, myInt))
                    return;
                extraFilesItem.fileId = myInt.Value;
                
                if (!databaseAccess().getNextExtraFilesSequenceNo(fileGroupId, myInt))
                    return;
                extraFilesItem.sequenceNo = myInt.Value;
                
                if (!databaseAccess().addExtraFilesItem(extraFilesItem))
                    return;
            }
            
            if (action.equals("modify"))
            {
                extraFilesItem.fileId = fileId;
                if (!databaseAccess().updateExtraFilesItem(extraFilesItem))
                    return;
            }
            
            finish();
        }
        catch (Exception e)
        {
            ShowError("SaveIt", e.getMessage());
        }
    }
    
    public void saveSchedule(View view)
    {
        try
        {
            if (!mySelectedFileChanged)
            {
                SaveIt();
                return;
            }
            
            if (mySelectedFileUri != null)
            {
                grantUriPermission("com.example.des.hp", mySelectedFileUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                try
                {
                    getContentResolver().openInputStream(mySelectedFileUri);
                }
                catch (Exception e)
                {
                    ShowError("saveExtraFile", e.getMessage());
                    return;
                }
                extraFilesItem.fileUri = mySelectedFileUri;
                
                MyString myString = new MyString();
                if (!myFileUtils().BaseFilenameFromUri(mySelectedFileUri, myString))
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
            txtFileDescription.setText(dialogWithEditTextFragment.getFinalText());
            
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
                        txtFileDescription.getText().toString(), // initial text
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
            txtFilename.setText(newFilename);
            mySelectedFileName=newFilename;
            myMessages().ShowMessageShort("Renaming to " + newFilename);
            
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
    
}
