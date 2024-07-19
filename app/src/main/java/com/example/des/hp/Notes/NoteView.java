package com.example.des.hp.Notes;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.R;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

public class NoteView extends BaseActivity
{

    public int holidayId;
    public int noteId;
    public NoteItem noteItem;
    public TextView txtNoteView;
    public ActionBar actionBar;
    public ImageButton btnShowInfo;

    public void showForm()
    {
        try
        {
            Bundle extras=getIntent().getExtras();
            if(extras != null)
            {
                String action=extras.getString("ACTION");
                if(action != null && action.equals("view"))
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

                    txtNoteView.setText(noteItem.notes);
                }
            }
        }
        catch(Exception e)
        {
            ShowError("showForm", e.getMessage());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            setContentView(R.layout.activity_notes_view);
            txtNoteView= findViewById(R.id.txtNotes);
            showForm();
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
    }

    public void editNotes()
    {
        try
        {
            Intent intent=new Intent(getApplicationContext(), NoteEdit.class);
            intent.putExtra("ACTION", "modify");
            intent.putExtra("HOLIDAYID", holidayId);
            intent.putExtra("NOTEID", noteId);
            intent.putExtra("TITLE", actionBar.getTitle());
            intent.putExtra("SUBTITLE", actionBar.getSubtitle());
            startActivity(intent);
        }
        catch(Exception e)
        {
            ShowError("editNotes", e.getMessage());
        }
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        try
        {
            MenuInflater inflater=getMenuInflater();
            inflater.inflate(R.menu.notesformmenu, menu);
        }
        catch(Exception e)
        {
            ShowError("onCreateOptionsMenu", e.getMessage());
        }
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

    public void deleteNotes()
    {
        try
        {
            noteItem.notes="";
            try(DatabaseAccess da = databaseAccess())
            {
                if(!da.updateNoteItem(noteItem))
                    return;
            }
            finish();
        }
        catch(Exception e)
        {
            ShowError("deleteNotes", e.getMessage());
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        try
        {
            int id = item.getItemId();
            if(id==R.id.action_edit_notes)
                editNotes();
            if(id==R.id.action_delete_notes)
                deleteNotes();
        }
        catch(Exception e)
        {
            ShowError("onOptionsItemSelected", e.getMessage());
        }
        return (true);
    }
}
