package com.example.des.hp.TipGroup;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.ExtraFiles.ExtraFilesDetailsList;
import com.example.des.hp.R;
import com.example.des.hp.myutils.*;
import com.example.des.hp.thirdpartyutils.BadgeView;
import com.example.des.hp.Notes.NoteItem;
import com.example.des.hp.Notes.NoteView;

public class TipGroupDetailsView extends BaseActivity
{

    public DatabaseAccess databaseAccess;
    private ImageView imageView;
    public int holidayId;
    public int tipGroupId;
    public TipGroupItem tipGroupItem;
    public LinearLayout grpTaskDate;
    private ImageUtils imageUtils;
    public TextView txtTipGroupDescription;
    public ActionBar actionBar;
    public TextView txtTipGroupNotes;
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
        if(tipGroupItem.noteId==0)
        {
            MyInt myInt = new MyInt();
            if(!databaseAccess.getNextNoteId(holidayId, myInt))
                return;
            tipGroupItem.noteId = myInt.Value;
            if(!databaseAccess.updateTipGroupItem(tipGroupItem))
                return;
        }
        intent2.putExtra("ACTION", "view");
        intent2.putExtra("HOLIDAYID", tipGroupItem.holidayId);
        intent2.putExtra("NOTEID", tipGroupItem.noteId);
        intent2.putExtra("TITLE", tipGroupItem.tipGroupDescription);
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
                    tipGroupId = extras.getInt("TIPGROUPID");
                    tipGroupItem = new TipGroupItem();
                    if (!databaseAccess.getTipGroupItem(holidayId, tipGroupId, tipGroupItem))
                        return;

                    actionBar = getSupportActionBar();
                    if (actionBar != null) {
                        String title = extras.getString("TITLE");
                        String subtitle = extras.getString("SUBTITLE");
                        if (title.length() > 0) {
                            actionBar.setTitle(title);
                            actionBar.setSubtitle(subtitle);
                        } else {
                            actionBar.setTitle(tipGroupItem.tipGroupDescription);
                            actionBar.setSubtitle("");
                        }
                    }

                    if (imageUtils.getPageHeaderImage(this, tipGroupItem.tipGroupPicture, imageView) == false)
                        return;
                    txtTipGroupDescription.setText(tipGroupItem.tipGroupDescription);

                    txtTipGroupNotes.setText(tipGroupItem.tipGroupNotes);
                    MyInt lFileCount = new MyInt();
                    lFileCount.Value = 0;
                    if (tipGroupItem.infoId > 0) {
                        if (!databaseAccess.getExtraFilesCount(tipGroupItem.infoId, lFileCount))
                            return;
                    }
                    btnShowInfoBadge.setText(Integer.toString(lFileCount.Value));

                    if (lFileCount.Value == 0) {
                        btnShowInfoBadge.hide();
                        if (myColor.SetImageButtonTint(btnShowInfo, R.color.colorDisabled) == false)
                            return;
                    } else {
                        btnShowInfoBadge.show();
                        if (myColor.SetImageButtonTint(btnShowInfo, R.color.colorEnabled) == false)
                            return;
                    }
                    NoteItem noteItem = new NoteItem();
                    if(!databaseAccess.getNoteItem(tipGroupItem.holidayId, tipGroupItem.noteId, noteItem))
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
                        "Error in TipGroupDetailsView::" + argFunction,
                        argMessage
                );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipgroup_details_view);

        imageUtils = new ImageUtils(this);
        myMessages = new MyMessages(this);
        myColor = new MyColor(this);

        imageView = (ImageView)findViewById(R.id.imageViewSmall);
        txtTipGroupDescription = (TextView) findViewById(R.id.txtTipGroupDescription);
        txtTipGroupNotes = (TextView) findViewById(R.id.txtTipGroupNotes);
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
            ShowError("Error in onCreate", e.getMessage());
        }
    }

    public void showInfo(View view)
    {
        Intent intent2 = new Intent(getApplicationContext(), ExtraFilesDetailsList.class);
        if(tipGroupItem.infoId==0)
        {
            MyInt myInt = new MyInt();
            if(!databaseAccess.getNextFileGroupId(myInt))
                return;
            tipGroupItem.infoId = myInt.Value;
            if(!databaseAccess.updateTipGroupItem(tipGroupItem))
                return;
        }
        intent2.putExtra("FILEGROUPID", tipGroupItem.infoId);
        intent2.putExtra("TITLE", tipGroupItem.tipGroupDescription);
        intent2.putExtra("SUBTITLE", "Info");
        startActivity(intent2);
    }



    public void editTipGroup()
    {
        Intent intent = new Intent(getApplicationContext(), TipGroupDetailsEdit.class);
        intent.putExtra("ACTION", "modify");
        intent.putExtra("HOLIDAYID", holidayId);
        intent.putExtra("TIPGROUPID", tipGroupId);
        intent.putExtra("TITLE", actionBar.getTitle());
        intent.putExtra("SUBTITLE", actionBar.getSubtitle());
        startActivity(intent);
    }

    public void deleteTipGroup()
    {
        if(!databaseAccess.deleteTipGroupItem(tipGroupItem))
            return;
        finish();
    }

    @Override
    protected void onResume(){
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


}
