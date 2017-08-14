package com.example.des.hp.Attraction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    //endregion
    
    //region Constructors/Destructors
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        try
        {
            layoutName="activity_attraction_details_view";
            setContentView(R.layout.activity_attraction_details_view);
            
            txtAttractionDescription = (TextView) findViewById(R.id.txtAttractionDescription);
            btnClear=(ImageButton) findViewById(R.id.btnClear);
            btnSave=(Button) findViewById(R.id.btnSave);
            grpMenuFile=(LinearLayout) findViewById(R.id.grpMenuFile);
            grpAttractionDescription=(LinearLayout) findViewById(R.id.grpAttractionDescription);
            
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
            attractionItem = new AttractionItem();
    
            if (action != null && action.equals("add"))
            {
                txtAttractionDescription.setText("");
                SetImage("");
            } else
            {
                if (!databaseAccess().getAttractionItem(holidayId, attractionId, attractionItem))
                    return;
    
                txtAttractionDescription.setText(attractionItem.attractionDescription);
    
                SetImage(attractionItem.attractionPicture);
            }
            if (title.length() > 0)
            {
                SetTitles(title, subTitle);
            } else
            {
                SetTitles(attractionItem.attractionDescription, "");
            }
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
        catch(Exception e)
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
            attractionItem.noteId=noteId;
            databaseAccess().updateAttractionItem(attractionItem);
        }
        catch(Exception e)
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
        catch(Exception e)
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
            attractionItem.infoId=infoId;
            databaseAccess().updateAttractionItem(attractionItem);
        }
        catch(Exception e)
        {
            ShowError("setInfoId", e.getMessage());
        }

    }
    //endregion
}