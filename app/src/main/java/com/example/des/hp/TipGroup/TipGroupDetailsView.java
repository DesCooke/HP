package com.example.des.hp.TipGroup;

import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.R;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

public class TipGroupDetailsView extends BaseActivity
{

    //region Member variables
    public TipGroupItem tipGroupItem;
    public TextView txtTipGroupDescription;
    public ImageButton btnClear;
    public Button btnSave;
    public LinearLayout grpTipGroupDescription;
    public LinearLayout grpMenuFile;
    //endregion

    //region Constructors/Destructors
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            layoutName="activity_tipgroup_details_view";
            setContentView(R.layout.activity_tipgroup_details_view);

            imageView= findViewById(R.id.imageViewSmall);
            txtTipGroupDescription= findViewById(R.id.txtTipGroupDescription);
            btnClear= findViewById(R.id.btnClear);
            btnSave= findViewById(R.id.btnSave);
            grpTipGroupDescription= findViewById(R.id.grpTipGroupDescription);
            grpMenuFile= findViewById(R.id.grpMenuFile);

            afterCreate();

            showForm();
        }
        catch(Exception e)
        {
            ShowError("Error in onCreate", e.getMessage());
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        /* disable the menu entirely */
        return false;
    }
    //endregion

    //region showForm
    public void showForm()
    {
        super.showForm();

        try
        {
            tipGroupItem=new TipGroupItem();
            try(DatabaseAccess da = databaseAccess())
            {
                if(!da.getTipGroupItem(holidayId, tipGroupId, tipGroupItem))
                    return;
            }

            if(title == null || (title.isEmpty()))
            {
                SetToolbarTitles(tipGroupItem.tipGroupDescription, "");
            } else
            {
                SetToolbarTitles(title, subTitle);
            }

            SetImage(tipGroupItem.tipGroupPicture);
            txtTipGroupDescription.setText(tipGroupItem.tipGroupDescription);

            afterShow();
        }
        catch(Exception e)
        {
            ShowError("showForm", e.getMessage());
        }
    }
    //endregion

    //region form Functions
    @Override
    public int getInfoId()
    {
        try
        {
            return (tipGroupItem.infoId);
        }
        catch(Exception e)
        {
            ShowError("getInfoId", e.getMessage());
        }
        return (0);

    }

    public void setNoteId(int pNoteId)
    {
        try
        {
            tipGroupItem.noteId=pNoteId;
            try(DatabaseAccess da = databaseAccess())
            {
                da.updateTipGroupItem(tipGroupItem);
            }
        }
        catch(Exception e)
        {
            ShowError("setNoteId", e.getMessage());
        }

    }

    @Override
    public int getNoteId()
    {
        try
        {
            return (tipGroupItem.noteId);
        }
        catch(Exception e)
        {
            ShowError("getNoteId", e.getMessage());
        }
        return (0);
    }

    @Override
    public void setInfoId(int pInfoId)
    {
        try
        {
            tipGroupItem.infoId=pInfoId;
            try(DatabaseAccess da = databaseAccess())
            {
                da.updateTipGroupItem(tipGroupItem);
            }
        }
        catch(Exception e)
        {
            ShowError("setInfoId", e.getMessage());
        }

    }
    //endregion
}
