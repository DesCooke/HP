package com.example.des.hp.Attraction;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
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

public class AttractionDetailsView extends BaseActivity
{
    
    public DatabaseAccess databaseAccess;
    private ImageView imageView;
    public int holidayId;
    public int attractionId;
    public AttractionItem attractionItem;
    private ImageUtils imageUtils;
    public TextView txtAttractionDescription;
    public ActionBar actionBar;
    public TextView txtAttractionNotes;
    public MyMessages myMessages;
    public ImageButton btnShowInfo;
    public BadgeView btnShowInfoBadge;
    public MyColor myColor;
    public ImageButton btnShowNotes;
    
    public void clearImage(View view)
    {
        try
        {
            imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.imagemissing));
        }
        catch (Exception e)
        {
            ShowError("clearImage", e.getMessage());
        }
        
    }
    
    public void showNotes(View view)
    {
        try
        {
            Intent intent2 = new Intent(getApplicationContext(), NoteView.class);
            if (attractionItem.noteId == 0)
            {
                MyInt myInt = new MyInt();
                if (!databaseAccess.getNextNoteId(holidayId, myInt))
                    return;
                attractionItem.noteId = myInt.Value;
                if (!databaseAccess.updateAttractionItem(attractionItem))
                    return;
            }
            intent2.putExtra("ACTION", "view");
            intent2.putExtra("HOLIDAYID", attractionItem.holidayId);
            intent2.putExtra("NOTEID", attractionItem.noteId);
            intent2.putExtra("TITLE", attractionItem.attractionDescription);
            intent2.putExtra("SUBTITLE", "Notes");
            startActivity(intent2);
        }
        catch (Exception e)
        {
            ShowError("showNotes", e.getMessage());
        }
        
    }
    
    public void showForm()
    {
        try
        {
            databaseAccess = new DatabaseAccess(this);
            
            clearImage(null);
            
            Bundle extras = getIntent().getExtras();
            if (extras != null)
            {
                String action = extras.getString("ACTION");
                if (action != null && action.equals("view"))
                {
                    holidayId = extras.getInt("HOLIDAYID");
                    attractionId = extras.getInt("ATTRACTIONID");
                    attractionItem = new AttractionItem();
                    if (!databaseAccess.getAttractionItem(holidayId, attractionId, attractionItem))
                        return;
                    
                    actionBar = getSupportActionBar();
                    if (actionBar != null)
                    {
                        String title = extras.getString("TITLE");
                        String subtitle = extras.getString("SUBTITLE");
                        if (title != null && title.length() > 0)
                        {
                            actionBar.setTitle(title);
                            actionBar.setSubtitle(subtitle);
                        } else
                        {
                            actionBar.setTitle(attractionItem.attractionDescription);
                            actionBar.setSubtitle("");
                        }
                    }
                    
                    if (attractionItem.attractionPicture.length() > 0)
                        if (!imageUtils.getPageHeaderImage(this, attractionItem.attractionPicture, imageView))
                            return;
                    
                    txtAttractionDescription.setText(attractionItem.attractionDescription);
                    
                    txtAttractionNotes.setText(attractionItem.attractionNotes);
                    MyInt lFileCount = new MyInt();
                    lFileCount.Value = 0;
                    if (attractionItem.infoId > 0)
                    {
                        if (!databaseAccess.getExtraFilesCount(attractionItem.infoId, lFileCount))
                            return;
                    }
                    btnShowInfoBadge.setText(Integer.toString(lFileCount.Value));
                    
                    if (lFileCount.Value == 0)
                    {
                        btnShowInfoBadge.hide();
                        if (myColor.SetImageButtonTint(btnShowInfo, R.color.colorDisabled) == false)
                            return;
                    } else
                    {
                        btnShowInfoBadge.show();
                        if (myColor.SetImageButtonTint(btnShowInfo, R.color.colorEnabled) == false)
                            return;
                    }
                    NoteItem noteItem = new NoteItem();
                    if (!databaseAccess.getNoteItem(attractionItem.holidayId, attractionItem.noteId, noteItem))
                        return;
                    if (noteItem.notes.length() == 0)
                    {
                        if (myColor.SetImageButtonTint(btnShowNotes, R.color.colorDisabled) == false)
                            return;
                    } else
                    {
                        if (myColor.SetImageButtonTint(btnShowNotes, R.color.colorEnabled) == false)
                            return;
                    }
                }
            }
        }
        catch (Exception e)
        {
            ShowError("showForm", e.getMessage());
        }
    }
    
    public void showInfo(View view)
    {
        try
        {
            Intent intent2 = new Intent(getApplicationContext(), ExtraFilesDetailsList.class);
            if (attractionItem.infoId == 0)
            {
                MyInt myInt = new MyInt();
                if (!databaseAccess.getNextFileGroupId(myInt))
                    return;
                attractionItem.infoId = myInt.Value;
                if (!databaseAccess.updateAttractionItem(attractionItem))
                    return;
            }
            intent2.putExtra("FILEGROUPID", attractionItem.infoId);
            intent2.putExtra("TITLE", attractionItem.attractionDescription);
            intent2.putExtra("SUBTITLE", "Info");
            startActivity(intent2);
        }
        catch (Exception e)
        {
            ShowError("showInfo", e.getMessage());
        }
    }
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attraction_details_view);
        
        try
        {
            imageUtils = new ImageUtils(this);
            myMessages = new MyMessages(this);
            myColor = new MyColor(this);
            
            imageView = (ImageView) findViewById(R.id.imageViewSmall);
            txtAttractionDescription = (TextView) findViewById(R.id.txtAttractionDescription);
            txtAttractionNotes = (TextView) findViewById(R.id.txtAttractionNotes);
            btnShowInfo = (ImageButton) findViewById(R.id.btnShowInfo);
            btnShowNotes = (ImageButton) findViewById(R.id.btnShowNotes);
            
            btnShowInfoBadge = new BadgeView(this, btnShowInfo);
            btnShowInfoBadge.setText(Integer.toString(0));
            btnShowInfoBadge.show();
            
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
            inflater.inflate(R.menu.attractiondetailsformmenu, menu);
        }
        catch (Exception e)
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
        catch (Exception e)
        {
            ShowError("onResume", e.getMessage());
        }
    }
    
    
}
