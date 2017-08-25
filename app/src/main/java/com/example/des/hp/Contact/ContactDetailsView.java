package com.example.des.hp.Contact;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.R;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

public class ContactDetailsView extends BaseActivity
{
    
    //region Member variables
    public ContactItem contactItem;
    public TextView txtContactDescription;
    public ImageButton btnClear;
    public Button btnSave;
    public LinearLayout grpMenuFile;
    public LinearLayout grpContactDescription;
    //endregion
    
    //region Constructors/Destructors
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        try
        {
            layoutName = "activity_contact_details_view";
            setContentView(R.layout.activity_contact_details_view);
            
            imageView = (ImageView) findViewById(R.id.imageViewSmall);
            txtContactDescription = (TextView) findViewById(R.id.txtContactDescription);
            btnClear = (ImageButton) findViewById(R.id.btnClear);
            btnSave = (Button) findViewById(R.id.btnSave);
            grpMenuFile = (LinearLayout) findViewById(R.id.grpMenuFile);
            grpContactDescription = (LinearLayout) findViewById(R.id.grpContactDescription);
            
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
            inflater.inflate(R.menu.contactdetailsformmenu, menu);
        }
        catch (Exception e)
        {
            ShowError("onCreateOptionsMenu", e.getMessage());
        }
        return true;
    }
    //endregion
    
    //region OnClick Events
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        try
        {
            switch (item.getItemId())
            {
                case R.id.action_delete_contact:
                    deleteContact();
                    return true;
                case R.id.action_edit_contact:
                    editContact();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
        catch (Exception e)
        {
            ShowError("onOptionsItemSelected", e.getMessage());
        }
        return (true);
    }
    //endregion
    
    //region showForm
    public void showForm()
    {
        super.showForm();
        try
        {
            contactItem = new ContactItem();
            if (!databaseAccess().getContactItem(holidayId, contactId, contactItem))
                return;
            
            if (title == null || (title.length() == 0))
            {
                SetTitles(contactItem.contactDescription, "");
            } else
            {
                SetTitles(title, subTitle);
            }
            
            SetImage(contactItem.contactPicture);
            
            txtContactDescription.setText(contactItem.contactDescription);

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
        return (contactItem.infoId);
    }

    public void setNoteId(int pNoteId)
    {
        contactItem.noteId=pNoteId;
        databaseAccess().updateContactItem(contactItem);
    }

    @Override
    public int getNoteId()
    {
        return (contactItem.noteId);
    }

    @Override
    public void setInfoId(int pInfoId)
    {
        contactItem.infoId=pInfoId;
        databaseAccess().updateContactItem(contactItem);
    }

    public void editContact()
    {
        try
        {
            Intent intent = new Intent(getApplicationContext(), ContactDetailsEdit.class);
            intent.putExtra("ACTION", "modify");
            intent.putExtra("HOLIDAYID", holidayId);
            intent.putExtra("CONTACTID", contactId);
            intent.putExtra("TITLE", title);
            intent.putExtra("SUBTITLE", subTitle);
            startActivity(intent);
        }
        catch (Exception e)
        {
            ShowError("editContact", e.getMessage());
        }
    }
    
    public void deleteContact()
    {
        try
        {
            if (!databaseAccess().deleteContactItem(contactItem))
                return;
            finish();
        }
        catch (Exception e)
        {
            ShowError("deleteContact", e.getMessage());
        }
    }
    //endregion
}
