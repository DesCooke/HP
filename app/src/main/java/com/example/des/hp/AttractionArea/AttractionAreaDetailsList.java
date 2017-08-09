/*
Shows a list of  attractionarea items (futureword etc) for the current attraction (magic kingdom)
 */
package com.example.des.hp.AttractionArea;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.des.hp.Attraction.AttractionDetailsEdit;
import com.example.des.hp.Attraction.AttractionDetailsView;
import com.example.des.hp.Attraction.AttractionItem;
import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.ExtraFiles.ExtraFilesDetailsList;
import com.example.des.hp.Notes.NoteItem;
import com.example.des.hp.Notes.NoteView;
import com.example.des.hp.R;
import com.example.des.hp.myutils.*;
import com.example.des.hp.thirdpartyutils.BadgeView;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
import static com.example.des.hp.myutils.ImageUtils.imageUtils;

/**
 * * Created by Des on 02/11/2016.
 */

public class AttractionAreaDetailsList extends BaseActivity
{
    
    public ArrayList<AttractionAreaItem> attractionAreaList;
    public int holidayId;
    public int attractionId;
    public AttractionAreaAdapter attractionAreaAdapter;
    public String title;
    public String subtitle;
    public ActionBar actionBar;
    public AttractionItem attractionItem;
    public ImageButton btnShowInfo;
    public ImageButton btnShowNotes;
    public MyColor myColor;
    public BadgeView btnShowInfoBadge;
    public ImageView imageViewSmall;
    public Bitmap imageDefault;

    public void clearImage()
    {
        try
        {
            imageViewSmall.setImageBitmap(imageDefault);
        }
        catch (Exception e)
        {
            ShowError("clearImage", e.getMessage());
        }
    }
    
    public void showAttractionAreaAdd(View view)
    {
        try
        {
            Intent intent = new Intent(getApplicationContext(), AttractionAreaDetailsEdit.class);
            intent.putExtra("ACTION", "add");
            intent.putExtra("HOLIDAYID", holidayId);
            intent.putExtra("ATTRACTIONID", attractionId);
            startActivity(intent);
        }
        catch (Exception e)
        {
            ShowError("showAttractionAreaAdd", e.getMessage());
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
                if (!databaseAccess().getNextFileGroupId(myInt))
                    return;
                attractionItem.infoId = myInt.Value;
                if (!databaseAccess().updateAttractionItem(attractionItem))
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
    
    public void showNotes(View view)
    {
        try
        {
            Intent intent2 = new Intent(getApplicationContext(), NoteView.class);
            if (attractionItem.noteId == 0)
            {
                MyInt myInt = new MyInt();
                if (!databaseAccess().getNextNoteId(holidayId, myInt))
                    return;
                attractionItem.noteId = myInt.Value;
                if (!databaseAccess().updateAttractionItem(attractionItem))
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
            attractionItem = new AttractionItem();
            if (!databaseAccess().getAttractionItem(holidayId, attractionId, attractionItem))
                return;
            
            attractionAreaList = new ArrayList<>();
            if (!databaseAccess().getAttractionAreaList(holidayId, attractionId, attractionAreaList))
                return;


            clearImage();
            if(attractionItem.attractionPicture.length()>0)
            {
                if (!imageUtils().getPageHeaderImage(this, attractionItem.attractionPicture, imageViewSmall))
                    return;
            }

            subtitle=attractionItem.attractionDescription;
            subTitle=attractionItem.attractionDescription;
            if (actionBar != null)
            {
                if (title.length() > 0)
                {
                    actionBar.setTitle(title);
                    actionBar.setSubtitle(subtitle);
                } else
                {
                    actionBar.setTitle("ATTRACTIONS");
                    actionBar.setSubtitle("");
                }
            }
            
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.attractionAreaListView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setHasFixedSize(true);
            //listView1.setDivider(null);
            attractionAreaAdapter = new AttractionAreaAdapter(this, attractionAreaList);
            recyclerView.setAdapter(attractionAreaAdapter);
            
            itemTouchHelper.attachToRecyclerView(recyclerView);
            
            attractionAreaAdapter.setOnItemClickListener
                (
                    new AttractionAreaAdapter.OnItemClickListener()
                    {
                        @Override
                        public void onItemClick(View view, AttractionAreaItem obj, int position)
                        {
                            Intent intent = new Intent(getApplicationContext(), AttractionAreaDetailsView.class);
                            intent.putExtra("ACTION", "view");
                            intent.putExtra("HOLIDAYID", attractionAreaList.get(position).holidayId);
                            intent.putExtra("ATTRACTIONID", attractionAreaList.get(position).attractionId);
                            intent.putExtra("ATTRACTIONAREAID", attractionAreaList.get(position).attractionAreaId);
                            if (actionBar != null)
                            {
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
            if (attractionItem.infoId > 0)
            {
                if (!databaseAccess().getExtraFilesCount(attractionItem.infoId, lFileCount))
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
            if (!databaseAccess().getNoteItem(attractionItem.holidayId, attractionItem.noteId, noteItem))
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
        catch (Exception e)
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
                        
                        
                        if (dragFrom == -1)
                        {
                            dragFrom = fromPosition;
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
                        
                        if (dragFrom != -1 && dragTo != -1 && dragFrom != dragTo)
                        {
                            attractionAreaAdapter.onItemMove();
                        }
                        
                        dragFrom = dragTo = -1;
                    }
                    
                }
            );
    
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
        setContentView(R.layout.activity_attractionarea_list);
        
        try
        {
            actionBar = getSupportActionBar();
            myColor = new MyColor(this);
            btnShowInfo = (ImageButton) findViewById(R.id.btnShowInfo);
            btnShowNotes = (ImageButton) findViewById(R.id.btnShowNotes);
            imageViewSmall = (ImageView) findViewById(R.id.imageViewSmall);
            imageDefault = BitmapFactory.decodeResource(getResources(), R.drawable.imagemissing);
            
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
            showForm();
        }
        catch (Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
        
    }
    
    public void editAttraction()
    {
        try
        {
            Intent intent = new Intent(getApplicationContext(), AttractionDetailsEdit.class);
            intent.putExtra("ACTION", "modify");
            intent.putExtra("HOLIDAYID", holidayId);
            intent.putExtra("ATTRACTIONID", attractionId);
            intent.putExtra("TITLE", actionBar.getTitle());
            intent.putExtra("SUBTITLE", actionBar.getSubtitle());
            startActivity(intent);
        }
        catch (Exception e)
        {
            ShowError("editAttraction", e.getMessage());
        }
    }
    
    public void deleteAttraction()
    {
        try
        {
            if (!databaseAccess().deleteAttractionItem(attractionItem))
                return;
            finish();
        }
        catch (Exception e)
        {
            ShowError("deleteAttraction", e.getMessage());
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
    public boolean onOptionsItemSelected(MenuItem item)
    {
        boolean lv_return = false;
        try
        {
            switch (item.getItemId())
            {
                case R.id.action_delete_attraction:
                    deleteAttraction();
                    lv_return = true;
                    break;
                
                case R.id.action_edit_attraction:
                    editAttraction();
                    lv_return = true;
                    break;
                
                default:
                    lv_return = super.onOptionsItemSelected(item);
                    break;
            }
        }
        catch (Exception e)
        {
            ShowError("onOptionsItemSelected", e.getMessage());
        }
        return (lv_return);
    }
    
    
}

