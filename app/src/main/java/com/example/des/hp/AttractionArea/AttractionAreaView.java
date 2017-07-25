package com.example.des.hp.AttractionArea;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.graphics.BitmapFactory;
import android.widget.TextView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.ExtraFiles.ExtraFilesDetailsList;
import com.example.des.hp.myutils.*;
import com.example.des.hp.R;
import com.example.des.hp.thirdpartyutils.BadgeView;
import com.example.des.hp.Notes.NoteItem;
import com.example.des.hp.Notes.NoteView;

public class AttractionAreaView extends BaseActivity
{
    
    public DatabaseAccess databaseAccess;
    private final int SELECT_PHOTO = 1;
    private ImageView imageViewSmall;
    private String action;
    public int holidayId;
    public int attractionId;
    public int attractionAreaId;
    public TextView attractionAreaDescription;
    public ActionBar actionBar;
    public AttractionAreaItem attractionAreaItem;
    private ImageUtils imageUtils;
    public TextView txtAttractionAreaNotes;
    public MyMessages myMessages;
    public ImageButton btnShowInfo;
    public BadgeView btnShowInfoBadge;
    public MyColor myColor;
    public ImageButton btnShowNotes;
    
    public void clearImage(View view)
    {
        try
        {
            imageViewSmall.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.imagemissing));
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
            if (attractionAreaItem.noteId == 0)
            {
                MyInt myInt = new MyInt();
                if (!databaseAccess.getNextNoteId(holidayId, myInt))
                    return;
                attractionAreaItem.noteId = myInt.Value;
                if (!databaseAccess.updateAttractionAreaItem(attractionAreaItem))
                    return;
            }
            intent2.putExtra("ACTION", "view");
            intent2.putExtra("HOLIDAYID", attractionAreaItem.holidayId);
            intent2.putExtra("NOTEID", attractionAreaItem.noteId);
            intent2.putExtra("TITLE", attractionAreaItem.attractionAreaDescription);
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
            clearImage(null);
            
            Bundle extras = getIntent().getExtras();
            if (extras != null)
            {
                String title = extras.getString("TITLE");
                String subtitle = extras.getString("SUBTITLE");
                actionBar.setTitle(title);
                action = extras.getString("ACTION");
                
                holidayId = extras.getInt("HOLIDAYID");
                attractionId = extras.getInt("ATTRACTIONID");
                attractionAreaId = extras.getInt("ATTRACTIONAREAID");
                attractionAreaItem = new AttractionAreaItem();
                if (!databaseAccess.getAttractionAreaItem(holidayId, attractionId, attractionAreaId, attractionAreaItem))
                    return;
                
                attractionAreaDescription.setText(attractionAreaItem.attractionAreaDescription);
                
                if (!imageUtils.getPageHeaderImage(this, attractionAreaItem.attractionAreaPicture, imageViewSmall))
                    return;
                
                actionBar.setSubtitle(subtitle);
                
                txtAttractionAreaNotes.setText(String.valueOf(attractionAreaItem.attractionAreaNotes));
                
                MyInt lFileCount = new MyInt();
                lFileCount.Value = 0;
                if (attractionAreaItem.infoId > 0)
                {
                    if (!databaseAccess.getExtraFilesCount(attractionAreaItem.infoId, lFileCount))
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
                if (!databaseAccess.getNoteItem(attractionAreaItem.holidayId, attractionAreaItem.noteId, noteItem))
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
            if (attractionAreaItem.infoId == 0)
            {
                MyInt myInt = new MyInt();
                if (!databaseAccess.getNextFileGroupId(myInt))
                    return;
                attractionAreaItem.infoId = myInt.Value;
                if (!databaseAccess.updateAttractionAreaItem(attractionAreaItem))
                    return;
            }
            intent2.putExtra("FILEGROUPID", attractionAreaItem.infoId);
            intent2.putExtra("TITLE", attractionAreaItem.attractionAreaDescription);
            intent2.putExtra("SUBTITLE", "Info");
            startActivity(intent2);
        }
        catch (Exception e)
        {
            ShowError("showInfo", e.getMessage());
        }
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
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        try
        {
            setContentView(R.layout.activity_attractionarea_view);
            
            databaseAccess = new DatabaseAccess(this);
            actionBar = getSupportActionBar();
            imageUtils = new ImageUtils(this);
            myMessages = new MyMessages(this);
            myColor = new MyColor(this);
            
            imageViewSmall = (ImageView) findViewById(R.id.imageViewSmall);
            attractionAreaDescription = (TextView) findViewById(R.id.txtAttractionAreaDescription);
            txtAttractionAreaNotes = (TextView) findViewById(R.id.txtAttractionAreaNotes);
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
}
