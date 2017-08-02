package com.example.des.hp.Dialog;

/*
** BASEACTIVITY
**
** Inter-Intent variables
**   want to pass to and from Intents, then create a variable to hold it (place in the
**   InterIntent variable section.  Call the intent with PutExtra....
**   The OnCreate automatically parses the Bundle and sets up the member variables
**
** Title and SubTitle
**   Make sure your View has a SupportActionBar, then simply call SetTitles (title, subTitle)
**     This function also saves the title/subTitle in member variables so you don't have to
**     go hunting for them
**
**  Errors and Messages
**    This automatically sets the Context of the Error Dialogs and Message dialogs to this
**    activity - it also resets it onResume (when you come back to this activity.
**    They are all single instances also - so you can simply start using them - don't need to
**    create anything.
**      ErrorDialog().Show(<title>, <description>)
**        for errors you can use the member function ShowError (this prefixes the title with
**          the current class name)
**      MessageDialog().Show(<title>, <description>)
**        shows a message box with an OK button
**      myMessages().ShowMessageShort() / myMessages().ShowMessageLong()
**
**  Notes and Info
**    You can have a notes button or info button or both.
**    Notes, button must be called btnShowNotes
**      You must provide a function to getNoteId() which returns the current NoteId
**      and also setNoteId(), to record the NoteId
**      It will automatically handle the showNotes button press
**
**    ShowInfo, button must be called btnShowInfo
**      You must provide a function to getInfoId() which returns the current InfoId
**      and also setInfoId(), to record the InfoId
**      If will automatically handle the showInfo button press
**
**   If you have wither Notes or Info then you have to call
**     afterCreate() at the end of the onCreate function, this will detect the
**       field in the view and initialise some variables
**     afterShow() at the end of the onShow function, this will enable the button and set
**       the badge if needed
*/

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.example.des.hp.ExtraFiles.ExtraFilesDetailsList;
import com.example.des.hp.Notes.NoteItem;
import com.example.des.hp.Notes.NoteView;
import com.example.des.hp.R;
import com.example.des.hp.myutils.MyInt;
import com.example.des.hp.myutils.MyMessages;
import com.example.des.hp.thirdpartyutils.BadgeView;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
import static com.example.des.hp.myutils.MyColor.myColor;
import static com.example.des.hp.myutils.MyLog.myLog;
import static com.example.des.hp.myutils.MyMessages.myMessages;

public class BaseActivity extends AppCompatActivity
{
    // Inter Intent variables
    public int holidayId = 0;
    public int dayId = 0;
    public int scheduleId = 0;
    public int attractionId = 0;
    public int attractionAreaId = 0;
    public String action;
    public int fileGroupId=0;
    public String title;
    public String subTitle;
    public String holidayName="";

    public boolean showInfoEnabled;
    public ImageButton btnShowInfo;
    public BadgeView btnShowInfoBadge;

    public boolean showNotesEnabled;
    public ImageButton btnShowNotes;


    public void showNotes(View view)
    {
        try
        {
            Intent intent2 = new Intent(getApplicationContext(), NoteView.class);
            int lNoteId = getNoteId();
            if (lNoteId == 0)
            {
                MyInt myInt = new MyInt();
                if (!databaseAccess().getNextNoteId(holidayId, myInt))
                    return;
                lNoteId = myInt.Value;
                setNoteId(lNoteId);
            }
            intent2.putExtra("ACTION", "view");
            intent2.putExtra("HOLIDAYID", holidayId);
            intent2.putExtra("NOTEID", lNoteId);
            intent2.putExtra("TITLE", subTitle);
            intent2.putExtra("SUBTITLE", "Notes");
            startActivity(intent2);
        }
        catch (Exception e)
        {
            ShowError("showNotes", e.getMessage());
        }
    }

    public void afterCreate()
    {
        showInfoEnabled = false;
        btnShowInfo = (ImageButton) findViewById(R.id.btnShowInfo);
        if (btnShowInfo != null)
            showInfoEnabled = true;

        showNotesEnabled = false;
        btnShowNotes = (ImageButton) findViewById(R.id.btnShowNotes);
        if (btnShowNotes != null)
            showNotesEnabled = true;

        if (showInfoEnabled)
        {
            btnShowInfoBadge = new BadgeView(this, btnShowInfo);
            btnShowInfoBadge.setText(Integer.toString(0));
            btnShowInfoBadge.show();
        }
    }

    public void showInfo(View view)
    {
        try
        {
            int lInfoId;
            lInfoId = getInfoId();
            Intent intent2 = new Intent(getApplicationContext(), ExtraFilesDetailsList.class);
            if (lInfoId == 0)
            {
                MyInt myInt = new MyInt();
                if (!databaseAccess().getNextFileGroupId(myInt))
                    return;
                lInfoId = myInt.Value;
                setInfoId(lInfoId);
            }
            intent2.putExtra("FILEGROUPID", lInfoId);
            intent2.putExtra("TITLE", subTitle);
            intent2.putExtra("SUBTITLE", "Info");
            startActivity(intent2);
        }
        catch (Exception e)
        {
            ShowError("showInfo", e.getMessage());
        }
    }

    public void afterShow()
    {
        if(showInfoEnabled)
        {
            MyInt lFileCount = new MyInt();
            lFileCount.Value = 0;
            int lInfoId = getInfoId();
            if (lInfoId > 0)
            {
                if (!databaseAccess().getExtraFilesCount(lInfoId, lFileCount))
                    return;
            }
            btnShowInfoBadge.setText(Integer.toString(lFileCount.Value));

            if (lFileCount.Value == 0)
            {
                btnShowInfoBadge.setVisibility(View.INVISIBLE);
                if (myColor().SetImageButtonTint(btnShowInfo, R.color.colorDisabled) == false)
                    return;
            } else
            {
                btnShowInfoBadge.setVisibility(View.VISIBLE);
                if (myColor().SetImageButtonTint(btnShowInfo, R.color.colorEnabled) == false)
                    return;
            }
        }

        if(showNotesEnabled)
        {
            int lNoteId = getNoteId();
            NoteItem noteItem = new NoteItem();
            if (!databaseAccess().getNoteItem(holidayId, lNoteId, noteItem))
                return;
            if (noteItem.notes.length() == 0)
            {
                if (myColor().SetImageButtonTint(btnShowNotes, R.color.colorDisabled) == false)
                    return;
            } else
            {
                if (myColor().SetImageButtonTint(btnShowNotes, R.color.colorEnabled) == false)
                    return;
            }
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ErrorDialog.SetContext(this);
        MessageDialog.SetContext(this);
        MyMessages.SetContext(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            holidayId = extras.getInt("HOLIDAYID", 0);
            dayId = extras.getInt("DAYID", 0);
            scheduleId=extras.getInt("SCHEDULEID", 0);
            attractionAreaId = extras.getInt("ATTRACTIONAREAID", 0);
            attractionId = extras.getInt("ATTRACTIONID", 0);
            action = extras.getString("ACTION", "");
            fileGroupId=extras.getInt("FILEGROUPID", 0);
            title=extras.getString("TITLE", "");
            subTitle=extras.getString("SUBTITLE", "");
            holidayName=extras.getString("HOLIDAYNAME", "");
        }
    }

    public void SetTitles(String pTitle, String pSubTitle)
    {
        title=pTitle;
        subTitle= pSubTitle;
        
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setTitle(title);
            actionBar.setSubtitle(subTitle);
        }
    }

    public void showForm()
    {
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
    }
    
    protected void ShowError(String argFunction, String argMessage)
    {
        String lv_title;
        
        lv_title = this.getLocalClassName() + "::" + argFunction;
        
        myLog().WriteLogMessage("Error in " + lv_title + ". " + argMessage);
        
        ErrorDialog.Show("Error in " + lv_title, argMessage);
    }

    public int getInfoId()
    {
        return(0);
    }

    public int getNoteId()
    {
        return(0);
    }

    public void setInfoId(int pInfoId)
    {

    }

    public void setNoteId(int pNoteId)
    {

    }


    @Override
    protected void onResume()
    {
        super.onResume();
        ErrorDialog.SetContext(this);
        MessageDialog.SetContext(this);
        MyMessages.SetContext(this);
        showForm();
    }
}
