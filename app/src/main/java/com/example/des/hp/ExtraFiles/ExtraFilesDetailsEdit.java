package com.example.des.hp.ExtraFiles;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.example.des.hp.InternalFiles.InternalFileList;
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

            dwynDialogTag=getResources().getString(R.string.dwynDialogTag);
            dwetDialogTag=getResources().getString(R.string.dwetDialogTag);

            imageView.setOnClickListener(this);
            btnFile.setOnClickListener(this);

        }
        catch(Exception e)
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
        try
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
        catch(Exception e)
        {
            ShowError("onClick", e.getMessage());
        }

    }

    public void pickDevicePDF(View view)
    {
        try
        {
            Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), SELECT_DEVICE_FILE);
        }
        catch(Exception e)
        {
            // Potentially direct the user to the Market with a Dialog
            ShowError("pickDevicePDF", e.getMessage());
        }
    }

    public void pickInternalPDF(View view)
    {
        try
        {
            Intent intent=new Intent(getApplicationContext(), InternalFileList.class);
            startActivityForResult(intent, SELECT_INTERNAL_FILE);
        }
        catch(Exception e)
        {
            // Potentially direct the user to the Market with a Dialog
            ShowError("pickInternalPDF", e.getMessage());
        }
    }

    public void pickPDF(View view)
    {
        try
        {
            dialogWithYesNoFragment=DialogWithYesNoFragment.newInstance(getFragmentManager(),     // for the transaction bit
                "SELECTFILELOCATION2",        // unique name for this dialog type
                "Select File",            // unique name for this dialog type
                "Yes=Device, No=Files already stored", // form message
                R.drawable.attachment, new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        dialogWithYesNoFragment.dismiss();
                        pickDevicePDF(view);
                    }
                }, new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        dialogWithYesNoFragment.dismiss();
                        pickInternalPDF(view);
                    }
                }, this
            );

            dialogWithYesNoFragment.showIt();
        }
        catch(Exception e)
        {
            ShowError("handleRename", e.getMessage());
        }
    }
    //endregion

    //region Saving
    public void SaveIt()
    {
        try
        {
            if(mySelectedFileChanged)
            {
                if(mySelectedFileNameOnly.length() == 0)
                {
                    myMessages().ShowMessageShort("Need to select a file first... ");
                    return;
                }
            } else
            {
                if(extraFilesItem != null && extraFilesItem.fileName != null && extraFilesItem.fileName.length() == 0)
                {

                    myMessages().ShowMessageShort("Need to select a file first... ");
                    return;
                }
            }

            myMessages().ShowMessageShort("Saving " + mySelectedFileNameOnly);

            extraFilesItem.fileDescription=txtFileDescription.getText().toString();
            extraFilesItem.fileChanged=mySelectedFileChanged;
            if(mySelectedFileChanged)
                extraFilesItem.fileName=mySelectedFileNameOnly;

            extraFilesItem.fileBitmap=null;
            extraFilesItem.filePicture="";
            extraFilesItem.internalFilename="";
            if(internalFilename.length() > 0)
                extraFilesItem.internalFilename=internalFilename;
            if(internalImageFilename.length() > 0)
                extraFilesItem.filePicture=internalImageFilename;
            extraFilesItem.pictureAssigned=imageSet;
            extraFilesItem.pictureChanged=imageChanged;
            extraFilesItem.fileBitmap=null;
            if(imageSet)
                extraFilesItem.fileBitmap=((BitmapDrawable) imageView.getDrawable()).getBitmap();

            extraFilesItem.fileGroupId=fileGroupId;

            if(action.equals("add"))
            {
                MyInt myInt=new MyInt();

                if(!databaseAccess().getNextExtraFilesId(fileGroupId, myInt))
                    return;
                extraFilesItem.fileId=myInt.Value;

                if(!databaseAccess().getNextExtraFilesSequenceNo(fileGroupId, myInt))
                    return;
                extraFilesItem.sequenceNo=myInt.Value;

                if(!databaseAccess().addExtraFilesItem(extraFilesItem))
                    return;
            }

            if(action.equals("modify"))
            {
                extraFilesItem.fileId=fileId;
                if(!databaseAccess().updateExtraFilesItem(extraFilesItem))
                    return;
            }

            finish();
        }
        catch(Exception e)
        {
            ShowError("SaveIt", e.getMessage());
        }
    }

    public void saveSchedule(View view)
    {
        try
        {
            if(mySelectedFileUri != null)
            {
                if(internalFilename.length() == 0)
                {
                    grantUriPermission("com.example.des.hp", mySelectedFileUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    try
                    {
                        getContentResolver().openInputStream(mySelectedFileUri);
                    }
                    catch(Exception e)
                    {
                        ShowError("saveExtraFile ", e.getMessage() + ", " + mySelectedFileUri.getPath());
                        return;
                    }
                    extraFilesItem.fileUri=mySelectedFileUri;

                    myMessages().LogMessage("URI is " + mySelectedFileUri.getPath());

                    MyString myString=new MyString();
                    if(!myFileUtils().BaseFilenameFromUri(mySelectedFileUri, myString))
                        return;
                    newFilename=myString.Value;

                    myMessages().LogMessage("New Filename is " + newFilename);

                    File tof=new File(getResources().getString(R.string.files_path) + "/" + newFilename);
                    if(tof.exists())
                    {
                        origFilename=newFilename;
                        newFilename="";
                        // offer a rename option
                        handleRename(tof.getName());
                    } else
                    {
                        SaveIt();
                    }
                } else
                {
                    newFilename=internalFilename;
                    extraFilesItem.fileUri=null;
                    SaveIt();
                }
            } else
            {
                newFilename="";
                SaveIt();
            }
        }
        catch(Exception e)
        {
            ShowError("SaveSchedule", e.getMessage());
        }

    }

    public void ExtraFilesNamePicked(View view)
    {
        try
        {
            txtFileDescription.setText(dialogWithEditTextFragment.getFinalText());

            dialogWithEditTextFragment.dismiss();
        }
        catch(Exception e)
        {
            ShowError("ExtraFilesNamePicked", e.getMessage());
        }
    }

    public void pickExtraFilesName(View view)
    {
        try
        {
            dwetOnOkClick=new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    ExtraFilesNamePicked(view);
                }
            };

            dialogWithEditTextFragment=DialogWithEditTextFragment.newInstance(getFragmentManager(),     // for the transaction bit
                "hihi",            // unique name for this dialog type
                "File",    // form caption
                "Description",             // form message
                R.drawable.attachment, txtFileDescription.getText().toString(), // initial text
                dwetOnOkClick, this, false
            );

            dialogWithEditTextFragment.showIt();
        }
        catch(Exception e)
        {
            ShowError("pickExtraFilesName", e.getMessage());
        }
    }

    public void dwetOnOkClickProc(View view)
    {
        try
        {
            newFilename=dialogWithEditTextFragment.getFinalText();
            txtFilename.setText(newFilename);
            mySelectedFileNameOnly=newFilename;
            myMessages().ShowMessageShort("Renaming to " + newFilename);

            dialogWithEditTextFragment.dismiss();

            SaveIt();
        }
        catch(Exception e)
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

            dwetOnOkClick=new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    dwetOnOkClickProc(view);
                }
            };

            dialogWithEditTextFragment=DialogWithEditTextFragment.newInstance(getFragmentManager(),     // for the transaction bit
                dwetDialogTag,            // unique name for this dialog type
                "New Filename",    // form caption
                "Filename",             // form message
                R.drawable.attachment, origFilename,                // initial text
                dwetOnOkClick, this, false
            );

            dialogWithEditTextFragment.showIt();
        }
        catch(Exception e)
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
        catch(Exception e)
        {
            ShowError("dwynOnNoClickProc", e.getMessage());
        }
    }

    public void handleRename(String origFilename)
    {
        try
        {

            dwynOnYesClick=new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    dwynOnYesClickProc(view);
                }
            };
            // create a no on-click listener to call your procedure
            dwynOnNoClick=new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    dwynOnNoClickProc(view);
                }
            };


            dialogWithYesNoFragment=DialogWithYesNoFragment.newInstance(getFragmentManager(),     // for the transaction bit
                dwynDialogTag,            // unique name for this dialog type
                "File Already Exists",    // form caption
                "Rename " + origFilename + "?", // form message
                R.drawable.attachment, dwynOnYesClick, dwynOnNoClick, this
            );

            dialogWithYesNoFragment.showIt();
        }
        catch(Exception e)
        {
            ShowError("handleRename", e.getMessage());
        }
    }

}
