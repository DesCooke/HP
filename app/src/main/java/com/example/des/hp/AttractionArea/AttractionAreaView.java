package com.example.des.hp.AttractionArea;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.R;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

public class AttractionAreaView extends BaseActivity
{
    
    //region Member variables
    public TextView txtAttractionAreaDescription;
    public LinearLayout grpAttractionAreaDescription;
    public AttractionAreaItem attractionAreaItem;
    public ImageButton btnClear;
    public Button btnSave;
    public LinearLayout grpMenuFile;
    //endregion
    
    //region Constructors/Destructors
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        try
        {
            layoutName = "activity_attractionarea_view";
            setContentView(R.layout.activity_attractionarea_view);
            
            txtAttractionAreaDescription = (TextView) findViewById(R.id.txtAttractionAreaDescription);
            grpAttractionAreaDescription = (LinearLayout) findViewById(R.id.grpAttractionAreaDescription);
            btnClear = (ImageButton) findViewById(R.id.btnClear);
            btnSave = (Button) findViewById(R.id.btnSave);
            grpMenuFile = (LinearLayout) findViewById(R.id.grpMenuFile);
            
            
            afterCreate();
            
            showForm();
        }
        catch (Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
    }
    //endregion
    
    
    //region showForm
    public void showForm()
    {
        super.showForm();
        try
        {
            attractionAreaItem = new AttractionAreaItem();
            if (!databaseAccess().getAttractionAreaItem(holidayId, attractionId, attractionAreaId, attractionAreaItem))
                return;
            
            txtAttractionAreaDescription.setText(attractionAreaItem.attractionAreaDescription);
            
            SetImage(attractionAreaItem.attractionAreaPicture);
            
            afterShow();
        }
        catch (Exception e)
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
            return (attractionAreaItem.infoId);
        }
        catch (Exception e)
        {
            ShowError("getInfoId", e.getMessage());
        }
        return (0);
    }
    
    public void setNoteId(int pNoteId)
    {
        try
        {
            attractionAreaItem.noteId = pNoteId;
            databaseAccess().updateAttractionAreaItem(attractionAreaItem);
        }
        catch (Exception e)
        {
            ShowError("setNoteId", e.getMessage());
        }
    }
    
    @Override
    public int getNoteId()
    {
        try
        {
            return (attractionAreaItem.noteId);
        }
        catch (Exception e)
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
            attractionAreaItem.infoId = pInfoId;
            databaseAccess().updateAttractionAreaItem(attractionAreaItem);
        }
        catch (Exception e)
        {
            ShowError("setInfoId", e.getMessage());
        }
        
    }
    //endregion
    
    
}
