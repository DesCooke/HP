package com.example.des.hp.Contact;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.ExtraFiles.ExtraFilesDetailsList;
import com.example.des.hp.R;
import com.example.des.hp.myutils.*;
import com.example.des.hp.thirdpartyutils.BadgeView;
import com.example.des.hp.Notes.NoteItem;
import com.example.des.hp.Notes.NoteView;

public class ContactDetailsView extends BaseActivity
{

    public DatabaseAccess databaseAccess;
    private ImageView imageView;
    public int holidayId;
    public int contactId;
    public ContactItem contactItem;
    private ImageUtils imageUtils;
    public TextView txtContactDescription;
    public ActionBar actionBar;
    public TextView txtContactNotes;
    public MyMessages myMessages;
    public ImageButton btnShowInfo;
    public BadgeView btnShowInfoBadge;
    public MyColor myColor;
    public ImageButton btnShowNotes;

    public void clearImage(View view)
    {
        imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.imagemissing));
    }

    public void showNotes(View view)
    {
        Intent intent2 = new Intent(getApplicationContext(), NoteView.class);
        if(contactItem.noteId==0)
        {
            MyInt myInt = new MyInt();
            if(!databaseAccess.getNextNoteId(holidayId, myInt))
                return;
            contactItem.noteId = myInt.Value;
            if(!databaseAccess.updateContactItem(contactItem))
                return;
        }
        intent2.putExtra("ACTION", "view");
        intent2.putExtra("HOLIDAYID", contactItem.holidayId);
        intent2.putExtra("NOTEID", contactItem.noteId);
        intent2.putExtra("TITLE", contactItem.contactDescription);
        intent2.putExtra("SUBTITLE", "Notes");
        startActivity(intent2);
    }

    public void showForm()
    {
        try {
            databaseAccess = new DatabaseAccess(this);

            clearImage(null);

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String action = extras.getString("ACTION");
                if (action != null && action.equals("view")) {
                    holidayId = extras.getInt("HOLIDAYID");
                    contactId = extras.getInt("CONTACTID");
                    contactItem = new ContactItem();
                    if (!databaseAccess.getContactItem(holidayId, contactId, contactItem))
                        return;

                    actionBar = getSupportActionBar();
                    if (actionBar != null) {
                        String title = extras.getString("TITLE");
                        String subtitle = extras.getString("SUBTITLE");
                        if (title != null && title.length() > 0) {
                            actionBar.setTitle(title);
                            actionBar.setSubtitle(subtitle);
                        } else {
                            actionBar.setTitle(contactItem.contactDescription);
                            actionBar.setSubtitle("");
                        }
                    }

                    if (!imageUtils.getPageHeaderImage(this, contactItem.contactPicture, imageView))
                        return;

                    txtContactDescription.setText(contactItem.contactDescription);

                    txtContactNotes.setText(contactItem.contactNotes);
                    MyInt lFileCount = new MyInt();
                    lFileCount.Value = 0;
                    if (contactItem.infoId > 0) {
                        if (!databaseAccess.getExtraFilesCount(contactItem.infoId, lFileCount))
                            return;
                    }
                    btnShowInfoBadge.setText(Integer.toString(lFileCount.Value));

                    if (lFileCount.Value == 0)
                    {
                        btnShowInfoBadge.setVisibility(View.INVISIBLE);
                        if (myColor.SetImageButtonTint(btnShowInfo, R.color.colorDisabled) == false)
                            return;
                    } else
                    {
                        btnShowInfoBadge.setVisibility(View.VISIBLE);
                        if (myColor.SetImageButtonTint(btnShowInfo, R.color.colorEnabled) == false)
                            return;
                    }

                    NoteItem noteItem = new NoteItem();
                    if(!databaseAccess.getNoteItem(contactItem.holidayId, contactItem.noteId, noteItem))
                        return;
                    if (noteItem.notes.length() == 0)
                    {
                        if (myColor.SetImageButtonTint(btnShowNotes, R.color.colorDisabled) == false)
                            return;
                    } else {
                        if (myColor.SetImageButtonTint(btnShowNotes, R.color.colorEnabled) == false)
                            return;
                    }
                }
            }
        }
        catch(Exception e)
        {
            ShowError("showForm", e.getMessage());
        }

    }

    private void ShowError(String argFunction, String argMessage)
    {
        myMessages.ShowError
                (
                        "Error in ContactDetailsView::" + argFunction,
                        argMessage
                );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contact_details_view);
        imageUtils = new ImageUtils(this);
        myMessages = new MyMessages(this);
        myColor = new MyColor(this);

        imageView = (ImageView)findViewById(R.id.imageViewSmall);
        txtContactDescription = (TextView) findViewById(R.id.txtContactDescription);
        txtContactNotes = (TextView) findViewById(R.id.txtContactNotes);
        btnShowInfo=(ImageButton) findViewById(R.id.btnShowInfo);
        btnShowNotes=(ImageButton) findViewById(R.id.btnShowNotes);

        btnShowInfoBadge = new BadgeView(this, btnShowInfo);
        btnShowInfoBadge.setText(Integer.toString(0));
        btnShowInfoBadge.show();

        try
        {
            showForm();
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
    }

    public void editContact()
    {
        Intent intent = new Intent(getApplicationContext(), ContactDetailsEdit.class);
        intent.putExtra("ACTION", "modify");
        intent.putExtra("HOLIDAYID", holidayId);
        intent.putExtra("CONTACTID", contactId);
        intent.putExtra("TITLE", actionBar.getTitle());
        intent.putExtra("SUBTITLE", actionBar.getSubtitle());
        startActivity(intent);
    }

    public void deleteContact()
    {
        if(!databaseAccess.deleteContactItem(contactItem))
            return;
        finish();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contactdetailsformmenu, menu);
        return true;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        try
        {
            showForm();
        }
        catch(Exception e)
        {
            ShowError("onResume", e.getMessage());
        }
    }

    public void showInfo(View view)
    {
        Intent intent2 = new Intent(getApplicationContext(), ExtraFilesDetailsList.class);
        if(contactItem.infoId==0)
        {
            MyInt myInt = new MyInt();
            if(!databaseAccess.getNextFileGroupId(myInt))
                return;
            contactItem.infoId = myInt.Value;
            if(!databaseAccess.updateContactItem(contactItem))
                return;
        }
        intent2.putExtra("FILEGROUPID", contactItem.infoId);
        intent2.putExtra("TITLE", contactItem.contactDescription);
        intent2.putExtra("SUBTITLE", "Info");
        startActivity(intent2);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
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
}
