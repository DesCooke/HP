/*
Shows a list of  attractionarea items (futureword etc) for the current attraction (magic kingdom)
 */
package com.example.des.hp.AttractionArea;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.example.des.hp.Attraction.AttractionDetailsEdit;
import com.example.des.hp.Attraction.AttractionDetailsView;
import com.example.des.hp.Attraction.AttractionItem;
import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.ExtraFiles.ExtraFilesDetailsList;
import com.example.des.hp.Notes.NoteItem;
import com.example.des.hp.Notes.NoteView;
import com.example.des.hp.R;
import com.example.des.hp.myutils.*;
import com.example.des.hp.thirdpartyutils.BadgeView;

import java.util.ArrayList;
import java.util.Collections;

/**
 ** Created by Des on 02/11/2016.
 */

public class AttractionAreaDetailsList extends AppCompatActivity
{

    public DatabaseAccess databaseAccess;
    public ArrayList<AttractionAreaItem> attractionAreaList;
    public int holidayId;
    public int attractionId;
    public AttractionAreaAdapter attractionAreaAdapter;
    public String title;
    public String subtitle;
    public ActionBar actionBar;
    public AttractionItem attractionItem;
    public MyMessages myMessages;
    public ImageButton btnShowInfo;
    public ImageButton btnShowNotes;
    public MyColor myColor;
    public BadgeView btnShowInfoBadge;

    public void showAttractionAreaAdd(View view)
    {
        Intent intent = new Intent(getApplicationContext(), AttractionAreaDetailsEdit.class);
        intent.putExtra("ACTION", "add");
        intent.putExtra("HOLIDAYID", holidayId);
        intent.putExtra("ATTRACTIONID", attractionId);
        startActivity(intent);
    }

    public void showInfo(View view)
    {
        Intent intent2 = new Intent(getApplicationContext(), ExtraFilesDetailsList.class);
        if(attractionItem.infoId==0)
        {
            MyInt myInt = new MyInt();
            if(!databaseAccess.getNextFileGroupId(myInt))
                return;
            attractionItem.infoId = myInt.Value;
            if(!databaseAccess.updateAttractionItem(attractionItem))
                return;
        }
        intent2.putExtra("FILEGROUPID", attractionItem.infoId);
        intent2.putExtra("TITLE", attractionItem.attractionDescription);
        intent2.putExtra("SUBTITLE", "Info");
        startActivity(intent2);
    }

    public void showNotes(View view)
    {
        Intent intent2 = new Intent(getApplicationContext(), NoteView.class);
        if(attractionItem.noteId==0)
        {
            MyInt myInt = new MyInt();
            if(!databaseAccess.getNextNoteId(holidayId, myInt))
                return;
            attractionItem.noteId = myInt.Value;
            if(!databaseAccess.updateAttractionItem(attractionItem))
                return;
        }
        intent2.putExtra("ACTION", "view");
        intent2.putExtra("HOLIDAYID", attractionItem.holidayId);
        intent2.putExtra("NOTEID", attractionItem.noteId);
        intent2.putExtra("TITLE", attractionItem.attractionDescription);
        intent2.putExtra("SUBTITLE", "Notes");
        startActivity(intent2);
    }


    public void showForm()
    {
        try {
            if (actionBar != null) {
                if (title.length() > 0) {
                    actionBar.setTitle(title);
                    actionBar.setSubtitle(subtitle);
                } else {
                    actionBar.setTitle("ATTRACTIONS");
                    actionBar.setSubtitle("");
                }
            }

            attractionItem = new AttractionItem();
            if (!databaseAccess.getAttractionItem(holidayId, attractionId, attractionItem))
                return;

            attractionAreaList = new ArrayList<>();
            if (!databaseAccess.getAttractionAreaList(holidayId, attractionId, attractionAreaList))
                return;

            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.attractionAreaListView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setHasFixedSize(true);
            //listView1.setDivider(null);
            attractionAreaAdapter = new AttractionAreaAdapter(this, attractionAreaList);
            recyclerView.setAdapter(attractionAreaAdapter);

            itemTouchHelper.attachToRecyclerView(recyclerView);

            attractionAreaAdapter.setOnItemClickListener
                    (
                            new AttractionAreaAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, AttractionAreaItem obj, int position) {
                                    Intent intent = new Intent(getApplicationContext(), AttractionAreaDetailsView.class);
                                    intent.putExtra("ACTION", "view");
                                    intent.putExtra("HOLIDAYID", attractionAreaList.get(position).holidayId);
                                    intent.putExtra("ATTRACTIONID", attractionAreaList.get(position).attractionId);
                                    intent.putExtra("ATTRACTIONAREAID", attractionAreaList.get(position).attractionAreaId);
                                    if (actionBar != null) {
                                        intent.putExtra("TITLE", actionBar.getTitle() + "/" +
                                                actionBar.getSubtitle());
                                        intent.putExtra("SUBTITLE", attractionAreaList.get(position).attractionAreaDescription);
                                    }
                                    startActivity(intent);
                                }
                            }
                    );
            MyInt lFileCount = new MyInt();
            lFileCount.Value = 0;
            if (attractionItem.infoId > 0) {
                if (!databaseAccess.getExtraFilesCount(attractionItem.infoId, lFileCount))
                    return;
            }
            btnShowInfoBadge.setText(Integer.toString(lFileCount.Value));

            if (lFileCount.Value == 0) {
                btnShowInfoBadge.setVisibility(View.INVISIBLE);
                if (myColor.SetImageButtonTint(btnShowInfo, R.color.colorDisabled) == false)
                    return;
            } else {
                btnShowInfoBadge.setVisibility(View.VISIBLE);
                if (myColor.SetImageButtonTint(btnShowInfo, R.color.colorEnabled) == false)
                    return;
            }
            NoteItem noteItem = new NoteItem();
            if(!databaseAccess.getNoteItem(attractionItem.holidayId, attractionItem.noteId, noteItem))
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
        catch(Exception e)
        {
            ShowError("showForm", e.getMessage());
        }
    }

    // handle swipe to delete, and draggable
    ItemTouchHelper itemTouchHelper =
            new ItemTouchHelper
                    (
                            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT)
                            {
                                int dragFrom = -1;
                                int dragTo = -1;

                                @Override
                                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target)
                                {
                                    int fromPosition = viewHolder.getAdapterPosition();
                                    int toPosition = target.getAdapterPosition();


                                    if(dragFrom == -1)
                                    {
                                        dragFrom =  fromPosition;
                                    }
                                    dragTo = toPosition;

                                    if (fromPosition < toPosition)
                                    {
                                        for (int i = fromPosition; i < toPosition; i++)
                                        {
                                            Collections.swap(attractionAreaAdapter.data, i, i + 1);
                                        }
                                    } else
                                    {
                                        for (int i = fromPosition; i > toPosition; i--)
                                        {
                                            Collections.swap(attractionAreaAdapter.data, i, i - 1);
                                        }
                                    }
                                    attractionAreaAdapter.notifyItemMoved(fromPosition, toPosition);

                                    return true;
                                }

                                @Override
                                public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder)
                                {
                                    int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                                    int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                                    return makeMovementFlags(dragFlags, swipeFlags);
                                }

                                @Override
                                public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction)
                                {
                                }

                                @Override
                                public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder)
                                {
                                    super.clearView(recyclerView, viewHolder);

                                    if(dragFrom != -1 && dragTo != -1 && dragFrom != dragTo)
                                    {
                                        attractionAreaAdapter.onItemMove();
                                    }

                                    dragFrom = dragTo = -1;
                                }

                            }
                    );

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

    private void ShowError(String argFunction, String argMessage)
    {
        myMessages.ShowError
                (
                        "Error in AttractionAreaDetailsList::" + argFunction,
                        argMessage
                );
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attractionarea_list);

        databaseAccess = new DatabaseAccess(this);
        actionBar = getSupportActionBar();
        myMessages = new MyMessages(this);
        myColor = new MyColor(this);
        btnShowInfo=(ImageButton) findViewById(R.id.btnShowInfo);
        btnShowNotes=(ImageButton) findViewById(R.id.btnShowNotes);

        btnShowInfoBadge = new BadgeView(this, btnShowInfo);
        btnShowInfoBadge.setText(Integer.toString(0));
        btnShowInfoBadge.show();

        title = "";
        subtitle = "";
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            holidayId = extras.getInt("HOLIDAYID");
            attractionId = extras.getInt("ATTRACTIONID");
            title = extras.getString("TITLE");
            subtitle = extras.getString("SUBTITLE");
        }
        try
        {
            showForm();
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }

    }

    public void viewAttraction()
    {
        Intent intent = new Intent(getApplicationContext(), AttractionDetailsView.class);
        intent.putExtra("ACTION", "view");
        intent.putExtra("HOLIDAYID", holidayId);
        intent.putExtra("ATTRACTIONID", attractionId);
        intent.putExtra("TITLE", actionBar.getTitle());
        intent.putExtra("SUBTITLE", actionBar.getSubtitle());
        startActivity(intent);
    }

    public void editAttraction()
    {
        Intent intent = new Intent(getApplicationContext(), AttractionDetailsEdit.class);
        intent.putExtra("ACTION", "modify");
        intent.putExtra("HOLIDAYID", holidayId);
        intent.putExtra("ATTRACTIONID", attractionId);
        intent.putExtra("TITLE", actionBar.getTitle());
        intent.putExtra("SUBTITLE", actionBar.getSubtitle());
        startActivity(intent);
    }

    public void deleteAttraction()
    {
        if(!databaseAccess.deleteAttractionItem(attractionItem))
            return;
        finish();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.attractiondetailsformmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_delete_attraction:
                deleteAttraction();
                return true;
            case R.id.action_view_attraction:
                viewAttraction();
                return true;
            case R.id.action_edit_attraction:
                editAttraction();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}

