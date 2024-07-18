package com.example.des.hp.AttractionArea;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.myutils.*;
import com.example.des.hp.R;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
import static com.example.des.hp.myutils.MyMessages.myMessages;

public class AttractionAreaDetailsEdit extends AttractionAreaView implements View.OnClickListener
{
    
    //region Member variables
    public View.OnClickListener dwetOnOkClick;
    public DialogWithEditTextFragment dialogWithEditTextFragment;
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
            
            if (action != null && action.equals("add"))
            {
                grpMenuFile.setVisibility(View.GONE);
                txtAttractionAreaDescription.setText("");
            }
            
            grpAttractionAreaDescription.setOnClickListener(this);
            imageView.setOnClickListener(this);
            
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
        try
        {
            switch (view.getId())
            {
                
                case R.id.grpAttractionAreaDescription:
                    pickAttractionAreaDescription(view);
                    break;
                
                case R.id.imageViewSmall:
                    pickImage(view);
                    break;
            }
        }
        catch (Exception e)
        {
            ShowError("onClick", e.getMessage());
        }
        
    }
    
    public void AttractionAreaDescriptionPicked(View view)
    {
        try
        {
            txtAttractionAreaDescription.setText(dialogWithEditTextFragment.getFinalText());
            
            dialogWithEditTextFragment.dismiss();
        }
        catch (Exception e)
        {
            ShowError("AttractionAreaDescriptionPicked", e.getMessage());
        }
        
    }
    
    // Create a YES onclick procedure
    public void pickAttractionAreaDescription(View view)
    {
        try
        {
            dwetOnOkClick = new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    AttractionAreaDescriptionPicked(view);
                }
            };
            
            
            dialogWithEditTextFragment = DialogWithEditTextFragment.newInstance(getSupportFragmentManager(),     // for the transaction bit
                "hihi",            // unique name for this dialog type
                "Attraction Area",    // form caption
                "Description",             // form message
                R.drawable.attachment, txtAttractionAreaDescription.getText().toString(),                // initial text
                dwetOnOkClick, this, false
            );
            
            dialogWithEditTextFragment.showIt();
        }
        catch (Exception e)
        {
            ShowError("pickAttractionAreaDescription", e.getMessage());
        }
        
    }
    //endregion
    
    //region Saving
    public void saveSchedule(View view)
    {
        MyInt retInt = new MyInt();
        try(DatabaseAccess da = databaseAccess();)
        {
            myMessages().ShowMessageShort("Saving " + txtAttractionAreaDescription.getText().toString());
            
            attractionAreaItem.attractionAreaDescription = txtAttractionAreaDescription.getText().toString();
            
            
            attractionAreaItem.attractionAreaPicture = "";
            if (internalImageFilename.length() > 0)
                attractionAreaItem.attractionAreaPicture = internalImageFilename;
            attractionAreaItem.pictureAssigned = imageSet;
            attractionAreaItem.pictureChanged = imageChanged;
            attractionAreaItem.fileBitmap = null;
            if (imageSet)
                attractionAreaItem.fileBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            
            attractionAreaItem.attractionAreaNotes = "";
            
            if (action.equals("add"))
            {
                attractionAreaItem.holidayId = holidayId;
                attractionAreaItem.attractionId = attractionId;
                if (!da.getNextAttractionAreaId(holidayId, attractionId, retInt))
                    return;
                attractionAreaItem.attractionAreaId = retInt.Value;
                if (!da.getNextAttractionAreaSequenceNo(holidayId, attractionId, retInt))
                    return;
                attractionAreaItem.sequenceNo = retInt.Value;
                if (!da.addAttractionAreaItem(attractionAreaItem))
                    return;
            }
            
            if (action.equals("modify"))
            {
                if (!da.updateAttractionAreaItem(attractionAreaItem))
                    return;
            }
            
            finish();
        }
        catch (Exception e)
        {
            ShowError("saveSchedule", e.getMessage());
        }
    }
    //endregion
    
    
}
