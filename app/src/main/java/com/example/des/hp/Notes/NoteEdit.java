package com.example.des.hp.Notes;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.ExtraFiles.ExtraFilesDetailsList;
import com.example.des.hp.R;
import com.example.des.hp.myutils.*;
import com.example.des.hp.thirdpartyutils.BadgeView;

public class NoteEdit extends AppCompatActivity
{

    public DatabaseAccess databaseAccess;
    private ImageView imageView;
    public int holidayId;
    public int noteId;
    public NoteItem noteItem;
    private ImageUtils imageUtils;
    public EditText edtNote;
    public ActionBar actionBar;
    public TextView txtBudgetTotal;
    public TextView txtBudgetPaid;
    public TextView txtBudgetUnpaid;
    public TextView txtBudgetNotes;
    public MyMessages myMessages;
    public ImageButton btnShowInfo;
    public BadgeView btnShowInfoBadge;
    public MyColor myColor;
    public MyKeyboard myKeyboard;

    public void showForm()
    {
        try
        {
            databaseAccess=new DatabaseAccess(this);

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

            Bundle extras=getIntent().getExtras();
            if(extras != null)
            {
                String action=extras.getString("ACTION");
                if(action != null && action.equals("modify"))
                {
                    holidayId=extras.getInt("HOLIDAYID");
                    noteId=extras.getInt("NOTEID");
                    noteItem=new NoteItem();
                    noteItem.holidayId = holidayId;
                    noteItem.noteId = noteId;
                    if(!databaseAccess.getNoteItem(holidayId, noteId, noteItem))
                        return;

                    actionBar=getSupportActionBar();
                    if(actionBar != null)
                    {
                        String title=extras.getString("TITLE");
                        String subtitle=extras.getString("SUBTITLE");
                        if(title != null && title.length() > 0)
                        {
                            actionBar.setTitle(title);
                            actionBar.setSubtitle(subtitle);
                        } else
                        {
                            actionBar.setTitle("Notes");
                            actionBar.setSubtitle("");
                        }
                    }

                    edtNote.setText(noteItem.notes);
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
        myMessages.ShowError("Error in NoteEdit::" + argFunction, argMessage);
    }

    public void saveNotes(View view)
    {
        noteItem.notes = edtNote.getText().toString();

        MyBoolean noteExists = new MyBoolean();
        if(!databaseAccess.noteExists(holidayId, noteItem.noteId, noteExists))
            return;
        if(noteExists.Value==false)
        {
            if(!databaseAccess.addNoteItem(noteItem))
                return;
        }
        else
        {
            if(!databaseAccess.updateNoteItem(noteItem))
                return;
        }
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notes_edit);
        edtNote=(EditText) findViewById(R.id.edtNotes);
        edtNote.requestFocus();

        try
        {
            showForm();
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
    }

}
