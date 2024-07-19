package com.example.des.hp.Notes;

import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.R;
import com.example.des.hp.myutils.*;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

public class NoteEdit extends BaseActivity
{

    public int holidayId;
    public int noteId;
    public NoteItem noteItem;
    public EditText edtNote;
    public ActionBar actionBar;
    public ImageButton btnShowInfo;

    public void showForm()
    {
        try
        {
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
                    noteItem.holidayId=holidayId;
                    noteItem.noteId=noteId;
                    try(DatabaseAccess da = databaseAccess())
                    {
                        if(!da.getNoteItem(holidayId, noteId, noteItem))
                            return;
                    }

                    actionBar=getSupportActionBar();
                    if(actionBar != null)
                    {
                        String title=extras.getString("TITLE");
                        String subtitle=extras.getString("SUBTITLE");
                        if(title != null && !title.isEmpty())
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

    public void saveNotes(View view)
    {
        try
        {
            noteItem.notes=edtNote.getText().toString();

            MyBoolean noteExists=new MyBoolean();
            try(DatabaseAccess da = databaseAccess())
            {
                if(!da.noteExists(holidayId, noteItem.noteId, noteExists))
                    return;
                if(!noteExists.Value)
                {
                    if(!da.addNoteItem(noteItem))
                        return;
                } else
                {
                    if(!da.updateNoteItem(noteItem))
                        return;
                }
            }

            finish();
        }
        catch(Exception e)
        {
            ShowError("saveNotes", e.getMessage());
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            setContentView(R.layout.activity_notes_edit);
            edtNote= findViewById(R.id.edtNotes);
            edtNote.requestFocus();

            showForm();
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
    }

}
