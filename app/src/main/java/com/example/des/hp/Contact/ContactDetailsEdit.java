package com.example.des.hp.Contact;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.myutils.*;
import com.example.des.hp.R;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
import static com.example.des.hp.myutils.MyMessages.myMessages;

public class ContactDetailsEdit extends ContactDetailsView implements View.OnClickListener
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
                txtContactDescription.setText(getString(R.string.schedule_unknown));
            }
            grpContactDescription.setOnClickListener(this);
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
                case R.id.grpContactDescription:
                    pickContactDescription(view);
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
    
    public void ContactDescriptionPicked(View view)
    {
        try
        {
            txtContactDescription.setText(dialogWithEditTextFragment.getFinalText());
            
            dialogWithEditTextFragment.dismiss();
        }
        catch (Exception e)
        {
            ShowError("ContactDescriptionPicked", e.getMessage());
        }
    }
    
    // Create a YES onclick procedure
    public void pickContactDescription(View view)
    {
        try
        {
            dwetOnOkClick = new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    ContactDescriptionPicked(view);
                }
            };
            
            
            dialogWithEditTextFragment =
                DialogWithEditTextFragment.newInstance
                    (
                            getSupportFragmentManager(),     // for the transaction bit
                        "hihi",            // unique name for this dialog type
                        "Contact Description",    // form caption
                        "Description",             // form message
                        R.drawable.attachment,
                        txtContactDescription.getText().toString(),                // initial text
                        dwetOnOkClick,
                        this,
                        false
                    );
            
            dialogWithEditTextFragment.showIt();
        }
        catch (Exception e)
        {
            ShowError("pickContactDescription", e.getMessage());
        }
        
    }
    //endregion
    
    //region Saving
    public void saveSchedule(View view)
    {
        try(DatabaseAccess da = databaseAccess();)
        {
            myMessages().ShowMessageShort("Saving " + txtContactDescription.getText().toString());
            
            MyInt myInt = new MyInt();
            
            contactItem.contactDescription = txtContactDescription.getText().toString();
            contactItem.contactNotes = "";
            
            contactItem.contactPicture = "";
            if (internalImageFilename.length() > 0)
                contactItem.contactPicture = internalImageFilename;
            contactItem.pictureAssigned = imageSet;
            contactItem.pictureChanged = imageChanged;
            contactItem.fileBitmap = null;
            if (imageSet)
                contactItem.fileBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            
            if (action.equals("add"))
            {
                contactItem.holidayId = holidayId;

                if (!da.getNextContactId(holidayId, myInt))
                    return;
                contactItem.contactId = myInt.Value;
                
                if (!da.getNextContactSequenceNo(holidayId, myInt))
                    return;
                contactItem.sequenceNo = myInt.Value;
                
                if (!da.addContactItem(contactItem))
                    return;
            }
            
            if (action.equals("modify"))
            {
                contactItem.holidayId = holidayId;
                contactItem.contactId = contactId;
                if (!da.updateContactItem(contactItem))
                    return;
            }
            
            finish();
        }
        catch (Exception e)
        {
            ShowError("saveSchedule", e.getMessage());
        }
    }
    
    
}
