package com.example.des.hp.Notes;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.R;
import com.example.des.hp.myutils.*;
import com.example.des.hp.thirdpartyutils.BadgeView;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

public class NoteEdit extends BaseActivity
{

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
    public ImageButton btnShowInfo;
    public BadgeView btnShowInfoBadge;
    public MyColor myColor;
    public MyKeyboard myKeyboard;

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
            if(!databaseAccess().noteExists(holidayId, noteItem.noteId, noteExists))
                return;
            if(noteExists.Value == false)
            {
                if(!databaseAccess().addNoteItem(noteItem))
                    return;
            } else
            {
                if(!databaseAccess().updateNoteItem(noteItem))
                    return;
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
            edtNote=(EditText) findViewById(R.id.edtNotes);
            edtNote.requestFocus();

            showForm();
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
    }

}
