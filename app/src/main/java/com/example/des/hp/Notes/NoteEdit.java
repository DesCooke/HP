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

                    String title = extras.getString("TITLE");
                    String subtitle = extras.getString("SUBTITLE");
                    SetToolbarTitles(title, subtitle);

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
            noteItem.notes=edtNote.getText().toString().trim();

            MyBoolean noteExists=new MyBoolean();
            try(DatabaseAccess da = databaseAccess())
            {
                // does the note exist?
                if(!da.noteExists(holidayId, noteItem.noteId, noteExists))
                    return;

                if (noteExists.Value) {
                    // if the note exists, but the new value is empty - then delete it
                    if(noteItem.notes.isEmpty()){
                        if(!da.deleteNoteItem(noteItem))
                            return;
                    }
                    else {
                        // If the note exists and the new value is not empty - update it
                        if (!da.updateNoteItem(noteItem))
                            return;
                    }
                } else {
                    // if the note does not exist, and the new value is not empty - add it
                    if(!noteItem.notes.isEmpty())
                        if(!da.addNoteItem(noteItem))
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
