package com.example.des.hp.Tip;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class TipDetailsView extends BaseActivity
{

    public DatabaseAccess databaseAccess;
    private ImageView imageView;
    public int holidayId;
    public int tipGroupId;
    public int tipId;
    public TipItem tipItem;
    public LinearLayout grpTaskDate;
    private ImageUtils imageUtils;
    public TextView txtTipDescription;
    public ActionBar actionBar;
    public TextView txtTipNotes;
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
        if(tipItem.noteId==0)
        {
            MyInt myInt = new MyInt();
            if(!databaseAccess.getNextNoteId(holidayId, myInt))
                return;
            tipItem.noteId = myInt.Value;
            if(!databaseAccess.updateTipItem(tipItem))
                return;
        }
        intent2.putExtra("ACTION", "view");
        intent2.putExtra("HOLIDAYID", tipItem.holidayId);
        intent2.putExtra("NOTEID", tipItem.noteId);
        intent2.putExtra("TITLE", tipItem.tipDescription);
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
                    tipId = extras.getInt("TIPID");
                    tipItem = new TipItem();
                    if (!databaseAccess.getTipItem(holidayId, tipGroupId, tipId, tipItem))
                        return;

                    actionBar = getSupportActionBar();
                    if (actionBar != null) {
                        String title = extras.getString("TITLE");
                        String subtitle = extras.getString("SUBTITLE");
                        if (title.length() > 0) {
                            actionBar.setTitle(title);
                            actionBar.setSubtitle(subtitle);
                        } else {
                            actionBar.setTitle(tipItem.tipDescription);
                            actionBar.setSubtitle("");
                        }
                    }

                    if (imageUtils.getPageHeaderImage(this, tipItem.tipPicture, imageView) == false)
                        return;
                    txtTipDescription.setText(tipItem.tipDescription);

                    txtTipNotes.setText(tipItem.tipNotes);

                    MyInt lFileCount = new MyInt();
                    lFileCount.Value = 0;
                    if (tipItem.infoId > 0) {
                        if (!databaseAccess.getExtraFilesCount(tipItem.infoId, lFileCount))
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
                    if(!databaseAccess.getNoteItem(tipItem.holidayId, tipItem.noteId, noteItem))
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
                        "Error in TipDetailsView::" + argFunction,
                        argMessage
                );
    }

    public void showInfo(View view)
    {
        Intent intent2 = new Intent(getApplicationContext(), ExtraFilesDetailsList.class);
        if(tipItem.infoId==0)
        {
            MyInt myInt = new MyInt();
            if(!databaseAccess.getNextFileGroupId(myInt))
                return;
            tipItem.infoId = myInt.Value;
            if(!databaseAccess.updateTipItem(tipItem))
                return;
        }
        intent2.putExtra("FILEGROUPID", tipItem.infoId);
        intent2.putExtra("TITLE", tipItem.tipDescription);
        intent2.putExtra("SUBTITLE", "Info");
        startActivity(intent2);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_details_view);

        imageUtils = new ImageUtils(this);
        myMessages = new MyMessages(this);
        myColor = new MyColor(this);

        imageView = (ImageView)findViewById(R.id.imageViewSmall);
        txtTipDescription = (TextView) findViewById(R.id.txtTipDescription);
        txtTipNotes = (TextView) findViewById(R.id.txtTipNotes);
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
            ShowError("onCreate", e.getMessage());
        }
    }

    public void editTip()
    {
        Intent intent = new Intent(getApplicationContext(), TipDetailsEdit.class);
        intent.putExtra("ACTION", "modify");
        intent.putExtra("HOLIDAYID", holidayId);
        intent.putExtra("TIPGROUPID", tipGroupId);
        intent.putExtra("TIPID", tipId);
        intent.putExtra("TITLE", actionBar.getTitle());
        intent.putExtra("SUBTITLE", actionBar.getSubtitle());
        startActivity(intent);
    }

    public void deleteTip()
    {
        if(!databaseAccess.deleteTipItem(tipItem))
            return;
        finish();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tipdetailsformmenu, menu);
        return true;
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_delete_tip:
                deleteTip();
                return true;
            case R.id.action_edit_tip:
                editTip();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
