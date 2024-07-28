package com.example.des.hp.ThemeParks;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.myutils.*;
import com.example.des.hp.R;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
import static com.example.des.hp.myutils.MyMessages.myMessages;

public class ThemeParkAreaEdit extends ThemeParkAreaView implements View.OnClickListener
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
                txtThemeParkAreaName.setText("");
            }

            txtThemeParkAreaName.setOnClickListener(this);
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
            int id=view.getId();
            if(id==R.id.txtThemeParkAreaName)
                pickAttractionAreaDescription(view);
            if(id==R.id.imageViewSmall)
                pickImage(view);
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
            txtThemeParkAreaName.setText(dialogWithEditTextFragment.getFinalText());
            
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
            dwetOnOkClick = this::AttractionAreaDescriptionPicked;

            dialogWithEditTextFragment = DialogWithEditTextFragment.newInstance(getSupportFragmentManager(),     // for the transaction bit
                "hihi",            // unique name for this dialog type
                "Attraction Area",    // form caption
                "Description",             // form message
                R.drawable.attachment, txtThemeParkAreaName.getText().toString(),                // initial text
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
        try(DatabaseAccess da = databaseAccess())
        {
            myMessages().ShowMessageShort("Saving " + txtThemeParkAreaName.getText().toString());
            
            themeParkAreaItem.attractionAreaDescription = txtThemeParkAreaName.getText().toString();
            
            
            themeParkAreaItem.attractionAreaPicture = "";
            if (!internalImageFilename.isEmpty())
                themeParkAreaItem.attractionAreaPicture = internalImageFilename;
            themeParkAreaItem.pictureAssigned = imageSet;
            themeParkAreaItem.pictureChanged = imageChanged;
            themeParkAreaItem.fileBitmap = null;
            if (imageSet)
                themeParkAreaItem.fileBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            
            themeParkAreaItem.attractionAreaNotes = "";
            
            if (action.equals("add"))
            {
                themeParkAreaItem.holidayId = holidayId;
                themeParkAreaItem.attractionId = attractionId;
                if (!da.getNextAttractionAreaId(holidayId, attractionId, retInt))
                    return;
                themeParkAreaItem.attractionAreaId = retInt.Value;
                if (!da.getNextAttractionAreaSequenceNo(holidayId, attractionId, retInt))
                    return;
                themeParkAreaItem.sequenceNo = retInt.Value;
                if (!da.addAttractionAreaItem(themeParkAreaItem))
                    return;
            }
            
            if (action.equals("modify"))
            {
                if (!da.updateAttractionAreaItem(themeParkAreaItem))
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
