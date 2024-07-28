package com.example.des.hp.ThemeParks;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.myutils.*;
import com.example.des.hp.R;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
import static com.example.des.hp.myutils.MyMessages.myMessages;

public class ThemeParkEdit extends ThemeParkView implements View.OnClickListener
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

            if (action != null && action.equals("add")){
                txtThemeParkName.setText("");
                title = "Add a Theme Park";
            }

            txtThemeParkName.setOnClickListener(this);
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
            int id = view.getId();
            if(id==R.id.txtThemeParkName)
                pickAttractionDescription(view);
            if(id==R.id.imageViewSmall)
                pickImage(view);
        }
        catch (Exception e)
        {
            ShowError("onClick", e.getMessage());
        }
    }
    
    public void AttractionDescriptionPicked(View view)
    {
        try
        {
            txtThemeParkName.setText(dialogWithEditTextFragment.getFinalText());

            dialogWithEditTextFragment.dismiss();
        }
        catch (Exception e)
        {
            ShowError("AttractionDescriptionPicked", e.getMessage());
        }
    }
    
    // Create a YES onclick procedure
    public void pickAttractionDescription(View view)
    {
        try
        {
            dwetOnOkClick = this::AttractionDescriptionPicked;
            
            
            dialogWithEditTextFragment =
                DialogWithEditTextFragment.newInstance
                    (
                        getSupportFragmentManager(),     // for the transaction bit
                        "hihi",            // unique name for this dialog type
                        "Attraction",    // form caption
                        "Description",             // form message
                        R.drawable.attachment,
                            txtThemeParkName.getText().toString(),                // initial text
                        dwetOnOkClick,
                        this,
                        false
                    );
            
            dialogWithEditTextFragment.showIt();
        }
        catch (Exception e)
        {
            ShowError("pickAttractionDescription", e.getMessage());
        }
        
    }
    //endregion
    
    //region Saving
    public void saveSchedule(View view)
    {
        try(DatabaseAccess da = databaseAccess())
        {
            MyInt retInt = new MyInt();
            
            myMessages().ShowMessageShort("Saving " + txtThemeParkName.getText().toString());
            
            themeParkItem.pictureAssigned = imageSet;
            themeParkItem.pictureChanged = imageChanged;
            if(imageChanged) {
                themeParkItem.attractionPicture = "";
                if (!internalImageFilename.isEmpty())
                    themeParkItem.attractionPicture = internalImageFilename;
                themeParkItem.fileBitmap = null;
                if (imageSet)
                    themeParkItem.fileBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            }

            themeParkItem.attractionDescription = txtThemeParkName.getText().toString();
            themeParkItem.attractionNotes = "";
            
            if (action.equals("add"))
            {
                themeParkItem.holidayId = holidayId;
                if (!da.getNextAttractionId(holidayId, retInt))
                    return;
                themeParkItem.attractionId = retInt.Value;
                if (!da.getNextAttractionSequenceNo(holidayId, retInt))
                    return;
                themeParkItem.sequenceNo = retInt.Value;
                if (!da.addAttractionItem(themeParkItem))
                    return;
            }
            
            if (action.equals("modify"))
            {
                if (!da.updateAttractionItem(themeParkItem))
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
