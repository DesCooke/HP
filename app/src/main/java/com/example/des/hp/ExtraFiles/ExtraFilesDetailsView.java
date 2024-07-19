package com.example.des.hp.ExtraFiles;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.R;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
import static com.example.des.hp.myutils.MyFileUtils.myFileUtils;

import androidx.annotation.NonNull;

public class ExtraFilesDetailsView extends BaseActivity implements View.OnClickListener
{

    //region Member variables
    public ExtraFilesItem extraFilesItem;
    public TextView txtFileDescription;
    public LinearLayout grpFileDescription;
    public ImageButton btnClear;
    public Button btnSave;
    public ImageButton btnFile;
    //endregion

    //region Constructors/Destructors
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


        try
        {
            layoutName="activity_extra_files_details_view";
            setContentView(R.layout.activity_extra_files_details_view);

            txtFileDescription= findViewById(R.id.txtFileDescription);
            grpFileDescription= findViewById(R.id.grpFileDescription);
            btnClear= findViewById(R.id.btnClear);
            btnSave= findViewById(R.id.btnSave);
            btnFile= findViewById(R.id.btnFile);

            afterCreate();

            btnFile.setOnClickListener(this);

            showForm();
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        try
        {
            MenuInflater inflater=getMenuInflater();
            inflater.inflate(R.menu.extrafilesdetailsformmenu, menu);
        }
        catch(Exception e)
        {
            ShowError("onCreateOptionsMenu", e.getMessage());
        }
        return true;
    }
    //endregion

    //region Regular Form Activities
    public void showForm()
    {
        super.showForm();
        try
        {
            extraFilesItem=new ExtraFilesItem();
            if(action != null && action.equals("add"))
            {
                txtFilename.setText("");
                txtFileDescription.setText("");
                SetImage("");
            } else
            {
                try(DatabaseAccess da = databaseAccess())
                {
                    if(!da.getExtraFilesItem(fileGroupId, fileId, extraFilesItem))
                        return;
                }

                txtFilename.setText(extraFilesItem.fileName);
                txtFileDescription.setText(extraFilesItem.fileDescription);
                SetImage(extraFilesItem.filePicture);

                if(title.isEmpty())
                    if(extraFilesItem.fileDescription != null)
                        SetTitles(extraFilesItem.fileDescription, "");
            }


            afterShow();
        }
        catch(Exception e)
        {
            ShowError("showForm", e.getMessage());
        }
    }

    //endregion

    //region OnClick Events
    @Override
    public void onClick(View view)
    {
        try
        {
            int id=view.getId();
            if(id==R.id.imageViewSmall)
                pickImage(view);
            if(id==R.id.btnFile)
                ViewFile(view);
        }
        catch(Exception e)
        {
            ShowError("onClick", e.getMessage());
        }

    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        try
        {
            int id=item.getItemId();
            if(id==R.id.action_delete_extra_files)
                deleteExtraFiles();
            if(id==R.id.action_edit_extra_files)
                editExtraFiles();
        }
        catch(Exception e)
        {
            ShowError("onOptionsItemSelected", e.getMessage());
        }
        return (true);
    }

    public void editExtraFiles()
    {
        try
        {
            Intent intent=new Intent(getApplicationContext(), ExtraFilesDetailsEdit.class);
            intent.putExtra("ACTION", "modify");
            intent.putExtra("FILEGROUPID", fileGroupId);
            intent.putExtra("FILEID", fileId);
            intent.putExtra("TITLE", title);
            intent.putExtra("SUBTITLE", subTitle);
            startActivity(intent);
        }
        catch(Exception e)
        {
            ShowError("editExtraFiles", e.getMessage());
        }
    }

    public void ViewFile(View view)
    {
        try
        {
            if(!txtFilename.getText().toString().isEmpty())
            {
                myFileUtils().OpenAFile(txtFilename.getText().toString());
            }
        }
        catch(Exception e)
        {
            ShowError("ViewFile", e.getMessage());
        }
    }

    public void deleteExtraFiles()
    {
        try
        {
            try(DatabaseAccess da = databaseAccess())
            {
                if(!da.deleteExtraFilesItem(extraFilesItem))
                    return;
            }
            finish();
        }
        catch(Exception e)
        {
            ShowError("deleteExtraFiles", e.getMessage());
        }
    }

}
