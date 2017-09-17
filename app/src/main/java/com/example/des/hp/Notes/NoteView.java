package com.example.des.hp.Notes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.R;
import com.example.des.hp.myutils.*;
import com.example.des.hp.thirdpartyutils.BadgeView;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

public class NoteView extends BaseActivity
{

    private ImageView imageView;
    public int holidayId;
    public int noteId;
    public NoteItem noteItem;
    private ImageUtils imageUtils;
    public TextView txtNoteView;
    public ActionBar actionBar;
    public TextView txtBudgetTotal;
    public TextView txtBudgetPaid;
    public TextView txtBudgetUnpaid;
    public TextView txtBudgetNotes;
    public ImageButton btnShowInfo;
    public BadgeView btnShowInfoBadge;
    public MyColor myColor;

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
                    if(!databaseAccess().getNoteItem(holidayId, noteId, noteItem))
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
            txtNoteView=(TextView) findViewById(R.id.txtNotes);
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
            if(!databaseAccess().updateNoteItem(noteItem))
                return;
            finish();
        }
        catch(Exception e)
        {
            ShowError("deleteNotes", e.getMessage());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        try
        {
            switch(item.getItemId())
            {
                case R.id.action_edit_notes:
                    editNotes();
                    return true;
                case R.id.action_delete_notes:
                    deleteNotes();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
        catch(Exception e)
        {
            ShowError("onOptionsItemSelected", e.getMessage());
        }
        return (true);
    }
}
