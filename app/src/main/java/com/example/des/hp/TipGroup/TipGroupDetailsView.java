package com.example.des.hp.TipGroup;

import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

            imageView=(ImageView) findViewById(R.id.imageViewSmall);
            txtTipGroupDescription=(TextView) findViewById(R.id.txtTipGroupDescription);
            btnClear=(ImageButton) findViewById(R.id.btnClear);
            btnSave=(Button) findViewById(R.id.btnSave);
            grpTipGroupDescription=(LinearLayout) findViewById(R.id.grpTipGroupDescription);
            grpMenuFile=(LinearLayout) findViewById(R.id.grpMenuFile);

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
            if(!databaseAccess().getTipGroupItem(holidayId, tipGroupId, tipGroupItem))
                return;

            if(title == null || (title.length() == 0))
            {
                SetTitles(tipGroupItem.tipGroupDescription, "");
            } else
            {
                SetTitles(title, subTitle);
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
            databaseAccess().updateTipGroupItem(tipGroupItem);
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
            databaseAccess().updateTipGroupItem(tipGroupItem);
        }
        catch(Exception e)
        {
            ShowError("setInfoId", e.getMessage());
        }

    }
    //endregion
}
