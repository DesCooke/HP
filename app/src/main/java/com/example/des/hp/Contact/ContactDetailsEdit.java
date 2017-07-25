package com.example.des.hp.Contact;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.graphics.BitmapFactory;
import android.widget.TextView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.myutils.*;
import com.example.des.hp.R;

public class ContactDetailsEdit extends BaseActivity
{
    
    public DatabaseAccess databaseAccess;
    private final int SELECT_PHOTO = 1;
    private ImageView imageViewSmall;
    private String action;
    public int holidayId;
    public int contactId;
    public TextView contactDescription;
    public ActionBar actionBar;
    public ContactItem contactItem;
    public CheckBox cbPicturePicked;
    private ImageUtils imageUtils;
    public View.OnClickListener dwetOnOkClick;
    public DialogWithEditTextFragment dialogWithEditTextFragment;
    public DialogWithMultiEditTextFragment dialogWithMultiEditTextFragment;
    public TextView txtContactNotes;
    public MyMessages myMessages;
    
    public void pickImage(View view)
    {
        try
        {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, SELECT_PHOTO);
        }
        catch (Exception e)
        {
            ShowError("pickImage", e.getMessage());
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent)
    {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        try
        {
            switch (requestCode)
            {
                case SELECT_PHOTO:
                    if (resultCode == RESULT_OK)
                    {
                        try
                        {
                            MyBitmap myBitmap = new MyBitmap();
                            Boolean lRetCode =
                                imageUtils.ScaleBitmapFromUrl
                                    (
                                        imageReturnedIntent.getData(),
                                        getContentResolver(),
                                        myBitmap
                                    );
                            if (!lRetCode)
                                return;
                            
                            // assign new bitmap and set scale type
                            imageViewSmall.setImageBitmap(myBitmap.Value);
                            
                            cbPicturePicked.setChecked(true);
                            
                            contactItem.pictureChanged = true;
                            
                            
                        }
                        catch (Exception e)
                        {
                            ShowError("onActivityResult-selectphoto", e.getMessage());
                        }
                    }
                    break;
                
            }
        }
        catch (Exception e)
        {
            ShowError("onActivityResult", e.getMessage());
        }
    }
    
    public void clearImage(View view)
    {
        try
        {
            cbPicturePicked.setChecked(false);
            imageViewSmall.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.imagemissing));
        }
        catch (Exception e)
        {
            ShowError("clearImage", e.getMessage());
        }
    }
    
    public void btnClearImage(View view)
    {
        try
        {
            clearImage(view);
            contactItem.pictureChanged = true;
            contactItem.pictureAssigned = false;
        }
        catch (Exception e)
        {
            ShowError("btnClearImage", e.getMessage());
        }
    }
    
    public void saveContact(View view)
    {
        try
        {
            myMessages.ShowMessageShort("Saving " + contactDescription.getText().toString());
            
            contactItem.pictureAssigned = cbPicturePicked.isChecked();
            contactItem.contactDescription = contactDescription.getText().toString();
            contactItem.fileBitmap = null;
            if (contactItem.pictureAssigned)
                contactItem.fileBitmap = ((BitmapDrawable) imageViewSmall.getDrawable()).getBitmap();
            
            contactItem.contactNotes = txtContactNotes.getText().toString();
            
            if (action.equals("add"))
            {
                MyInt myInt = new MyInt();
                contactItem.holidayId = holidayId;
                
                if (!databaseAccess.getNextContactId(holidayId, myInt))
                    return;
                contactItem.contactId = myInt.Value;
                
                if (!databaseAccess.getNextContactSequenceNo(holidayId, myInt))
                    return;
                contactItem.sequenceNo = myInt.Value;
                
                if (!databaseAccess.addContactItem(contactItem))
                    return;
            }
            
            if (action.equals("modify"))
            {
                contactItem.holidayId = holidayId;
                contactItem.contactId = contactId;
                if (!databaseAccess.updateContactItem(contactItem))
                    return;
            }
            
            finish();
        }
        catch (Exception e)
        {
            ShowError("saveContact", e.getMessage());
        }
    }
    
    
    public void ContactDescriptionPicked(View view)
    {
        try
        {
            contactDescription.setText(dialogWithEditTextFragment.getFinalText());
            
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
                        getFragmentManager(),     // for the transaction bit
                        "hihi",            // unique name for this dialog type
                        "Contact Description",    // form caption
                        "Description",             // form message
                        R.drawable.attachment,
                        contactDescription.getText().toString(),                // initial text
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
    
    public void ContactNotesPicked(View view)
    {
        try
        {
            txtContactNotes.setText(dialogWithMultiEditTextFragment.getFinalText());
            
            dialogWithMultiEditTextFragment.dismiss();
        }
        catch (Exception e)
        {
            ShowError("ContactNotesPicked", e.getMessage());
        }
    }
    
    // Create a YES onclick procedure
    public void pickContactNotes(View view)
    {
        try
        {
            dwetOnOkClick = new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    ContactNotesPicked(view);
                }
            };
            
            
            dialogWithMultiEditTextFragment =
                DialogWithMultiEditTextFragment.newInstance
                    (
                        getFragmentManager(),     // for the transaction bit
                        "hjhj",            // unique name for this dialog type
                        "Contact Notes",    // form caption
                        "Contact",             // form message
                        R.drawable.attachment,
                        txtContactNotes.getText().toString(),                // initial text
                        dwetOnOkClick,
                        this
                    );
            
            
            dialogWithMultiEditTextFragment.showIt();
        }
        catch (Exception e)
        {
            ShowError("pickContactNotes", e.getMessage());
        }
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        try
        {
            setContentView(R.layout.activity_contact_details_edit);
            
            databaseAccess = new DatabaseAccess(this);
            actionBar = getSupportActionBar();
            imageUtils = new ImageUtils(this);
            myMessages = new MyMessages(this);
            
            cbPicturePicked = (CheckBox) findViewById(R.id.picturePicked);
            imageViewSmall = (ImageView) findViewById(R.id.imageViewSmall);
            contactDescription = (TextView) findViewById(R.id.txtContactDescription);
            txtContactNotes = (TextView) findViewById(R.id.txtContactNotes);
            
            clearImage(null);
            
            Bundle extras = getIntent().getExtras();
            if (extras != null)
            {
                String title = extras.getString("TITLE");
                String subtitle = extras.getString("SUBTITLE");
                actionBar.setTitle(title);
                action = extras.getString("ACTION");
                if (action != null && action.equals("add"))
                {
                    contactItem = new ContactItem();
                    holidayId = extras.getInt("HOLIDAYID");
                    contactDescription.setText("");
                    cbPicturePicked.setChecked(false);
                    actionBar.setSubtitle("Add a Contact");
                    txtContactNotes.setText("");
                }
                if (action != null && action.equals("modify"))
                {
                    holidayId = extras.getInt("HOLIDAYID");
                    contactId = extras.getInt("CONTACTID");
                    contactItem = new ContactItem();
                    if (!databaseAccess.getContactItem(holidayId, contactId, contactItem))
                        return;
                    
                    contactDescription.setText(contactItem.contactDescription);
                    
                    if (!imageUtils.getPageHeaderImage(this, contactItem.contactPicture, imageViewSmall))
                        return;
                    
                    cbPicturePicked.setChecked(contactItem.pictureAssigned);
                    
                    actionBar.setSubtitle(subtitle);
                    
                    txtContactNotes.setText(String.valueOf(contactItem.contactNotes));
                }
            }
        }
        catch (Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
        
    }

/*
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.daydetailsformmenu, menu);
        return true;
    }
*/
}
