package com.example.des.hp.Attraction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.R;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

public class AttractionDetailsView extends BaseActivity
{
    //region Member variables
    public AttractionItem attractionItem;
    public TextView txtAttractionDescription;
    public ImageButton btnClear;
    public Button btnSave;
    public LinearLayout grpMenuFile;
    public LinearLayout grpAttractionDescription;
    public LinearLayout grpToolBar;
    //endregion
    
    //region Constructors/Destructors
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        try
        {
            layoutName = "activity_attraction_details_view";
            setContentView(R.layout.activity_attraction_details_view);
            
            txtAttractionDescription = findViewById(R.id.txtAttractionDescription);
            btnClear = findViewById(R.id.btnClear);
            btnSave = findViewById(R.id.btnSave);
            grpMenuFile = findViewById(R.id.grpMenuFile);
            grpToolBar = findViewById(R.id.grpToolBar);
            grpAttractionDescription = findViewById(R.id.grpAttractionDescription);
            
            afterCreate();
            
            showForm();
        }
        catch (Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
    }
    
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try
        {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.attractiondetailsformmenu, menu);
        }
        catch (Exception e)
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
            allowCellMove = true;
            
            attractionItem = new AttractionItem();
            
            if (action != null && action.equals("add"))
            {
                txtAttractionDescription.setText("");
                SetImage("");
            } else
            {
                try(DatabaseAccess da = databaseAccess())
                {
                    if (!da.getAttractionItem(holidayId, attractionId, attractionItem))
                        return;
                }

                txtAttractionDescription.setText(attractionItem.attractionDescription);
                
                SetImage(attractionItem.attractionPicture);
            }
            if (!title.isEmpty())
            {
                SetTitles(title, subTitle);
            } else
            {
                SetTitles(attractionItem.attractionDescription, "");
            }

            afterShow();

        }
        catch (Exception e)
        {
            ShowError("showForm", e.getMessage());
        }
    }
    //endregion
    
    //region Notes and Info
    @Override
    public int getNoteId()
    {
        try
        {
            return (attractionItem.noteId);
        }
        catch (Exception e)
        {
            ShowError("getNoteId", e.getMessage());
        }
        return (0);
    }
    
    @Override
    public void setNoteId(int noteId)
    {
        try
        {
            attractionItem.noteId = noteId;
            try(DatabaseAccess da = databaseAccess())
            {
                da.updateAttractionItem(attractionItem);
            }
        }
        catch (Exception e)
        {
            ShowError("setNoteId", e.getMessage());
        }
    }
    
    @Override
    public int getInfoId()
    {
        try
        {
            return (attractionItem.infoId);
        }
        catch (Exception e)
        {
            ShowError("getInfoId", e.getMessage());
        }
        return (0);
    }
    
    @Override
    public void setInfoId(int infoId)
    {
        try
        {
            attractionItem.infoId = infoId;
            try(DatabaseAccess da = databaseAccess())
            {
                da.updateAttractionItem(attractionItem);
            }
        }
        catch (Exception e)
        {
            ShowError("setInfoId", e.getMessage());
        }
        
    }
    
    //endregion
    
}