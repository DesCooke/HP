/*
Shows a list of  attractionarea items (futureword etc) for the current attraction (magic kingdom)
 */
package com.example.des.hp.AttractionArea;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.des.hp.Attraction.AttractionDetailsEdit;
import com.example.des.hp.Attraction.AttractionItem;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.ExtraFiles.ExtraFilesDetailsList;
import com.example.des.hp.Notes.NoteView;
import com.example.des.hp.R;
import com.example.des.hp.myutils.*;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

public class AttractionAreaDetailsList extends BaseActivity
{

    //region Member variables
    public ArrayList<AttractionAreaItem> attractionAreaList;
    public AttractionAreaAdapter attractionAreaAdapter;
    public AttractionItem attractionItem;
    public LinearLayout grpMenuFile;
    public TextView txtAttractionDescription;
    //endregion

    //region Constructors/Destructors
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            layoutName="activity_attractionarea_list";
            setContentView(R.layout.activity_attractionarea_list);

            txtAttractionDescription=(TextView) findViewById(R.id.txtAttractionDescription);
            grpMenuFile=(LinearLayout) findViewById(R.id.grpMenuFile);

            afterCreate();

            showForm();
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }

    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        try
        {
            MenuInflater inflater=getMenuInflater();
            inflater.inflate(R.menu.attractiondetailsformmenu, menu);
        }
        catch(Exception e)
        {
            ShowError("onCreateOptionsMenu", e.getMessage());
        }
        return true;
    }
    //endregion

    //region OnClick Events
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        boolean lv_return=false;
        try
        {
            switch(item.getItemId())
            {
                case R.id.action_add_attractionarea:
                    showAttractionAreaAdd();
                    lv_return=true;
                    break;

                case R.id.action_delete_attraction:
                    deleteAttraction();
                    lv_return=true;
                    break;

                case R.id.action_edit_attraction:
                    editAttraction();
                    lv_return=true;
                    break;

                default:
                    lv_return=super.onOptionsItemSelected(item);
                    break;
            }
        }
        catch(Exception e)
        {
            ShowError("onOptionsItemSelected", e.getMessage());
        }
        return (lv_return);
    }
    //endregion

    //region showForm
    public void showForm()
    {
        super.showForm();
        try
        {
            allowCellMove=true;

            attractionItem=new AttractionItem();
            if(!databaseAccess().getAttractionItem(holidayId, attractionId, attractionItem))
                return;

            attractionAreaList=new ArrayList<>();
            if(!databaseAccess().getAttractionAreaList(holidayId, attractionId, attractionAreaList))
                return;

            SetImage(attractionItem.attractionPicture);

            txtAttractionDescription.setText(attractionItem.attractionDescription);

            subTitle=attractionItem.attractionDescription;
            if(title.length() > 0)
            {
                SetTitles(title, subTitle);
            } else
            {
                SetTitles("ATTRACTIONS", "");
            }

            attractionAreaAdapter=new AttractionAreaAdapter(this, attractionAreaList);

            CreateRecyclerView(R.id.attractionAreaListView, attractionAreaAdapter);

            attractionAreaAdapter.setOnItemClickListener(new AttractionAreaAdapter.OnItemClickListener()
            {
                @Override
                public void onItemClick(View view, AttractionAreaItem obj)
                {
                    Intent intent=new Intent(getApplicationContext(), AttractionAreaDetailsView.class);
                    intent.putExtra("ACTION", "view");
                    intent.putExtra("HOLIDAYID", obj.holidayId);
                    intent.putExtra("ATTRACTIONID", obj.attractionId);
                    intent.putExtra("ATTRACTIONAREAID", obj.attractionAreaId);
                    intent.putExtra("TITLE", title + "/" + subTitle);
                    intent.putExtra("SUBTITLE", obj.attractionAreaDescription);
                    startActivity(intent);
                }
            });
            afterShow();
        }
        catch(Exception e)
        {
            ShowError("showForm", e.getMessage());
        }
    }
    //endregion

    //region form Functions
    @Override
    public int getInfoId()
    {
        try
        {
            return (attractionItem.infoId);
        }
        catch(Exception e)
        {
            ShowError("getInfoId", e.getMessage());
        }
        return (0);
    }

    public void setNoteId(int pNoteId)
    {
        try
        {
            attractionItem.noteId=pNoteId;
            databaseAccess().updateAttractionItem(attractionItem);
        }
        catch(Exception e)
        {
            ShowError("setNoteId", e.getMessage());
        }
    }

    @Override
    public int getNoteId()
    {
        try
        {
            return (attractionItem.noteId);
        }
        catch(Exception e)
        {
            ShowError("getNoteId", e.getMessage());
        }
        return (0);
    }

    @Override
    public void setInfoId(int pInfoId)
    {
        try
        {
            attractionItem.infoId=pInfoId;
            databaseAccess().updateAttractionItem(attractionItem);
        }
        catch(Exception e)
        {
            ShowError("setInfoId", e.getMessage());
        }
    }

    public void showAttractionAreaAdd()
    {
        try
        {
            Intent intent=new Intent(getApplicationContext(), AttractionAreaDetailsEdit.class);
            intent.putExtra("ACTION", "add");
            intent.putExtra("HOLIDAYID", holidayId);
            intent.putExtra("ATTRACTIONID", attractionId);
            startActivity(intent);
        }
        catch(Exception e)
        {
            ShowError("showAttractionAreaAdd", e.getMessage());
        }
    }

    public void showInfo(View view)
    {
        try
        {
            Intent intent2=new Intent(getApplicationContext(), ExtraFilesDetailsList.class);
            if(attractionItem.infoId == 0)
            {
                MyInt myInt=new MyInt();
                if(!databaseAccess().getNextFileGroupId(myInt))
                    return;
                attractionItem.infoId=myInt.Value;
                if(!databaseAccess().updateAttractionItem(attractionItem))
                    return;
            }
            intent2.putExtra("FILEGROUPID", attractionItem.infoId);
            intent2.putExtra("TITLE", attractionItem.attractionDescription);
            intent2.putExtra("SUBTITLE", "Info");
            startActivity(intent2);
        }
        catch(Exception e)
        {
            ShowError("showInfo", e.getMessage());
        }
    }

    public void showNotes(View view)
    {
        try
        {
            Intent intent2=new Intent(getApplicationContext(), NoteView.class);
            if(attractionItem.noteId == 0)
            {
                MyInt myInt=new MyInt();
                if(!databaseAccess().getNextNoteId(holidayId, myInt))
                    return;
                attractionItem.noteId=myInt.Value;
                if(!databaseAccess().updateAttractionItem(attractionItem))
                    return;
            }
            intent2.putExtra("ACTION", "view");
            intent2.putExtra("HOLIDAYID", attractionItem.holidayId);
            intent2.putExtra("NOTEID", attractionItem.noteId);
            intent2.putExtra("TITLE", attractionItem.attractionDescription);
            intent2.putExtra("SUBTITLE", "Notes");
            startActivity(intent2);
        }
        catch(Exception e)
        {
            ShowError("showNotes", e.getMessage());
        }
    }

    public void editAttraction()
    {
        try
        {
            Intent intent=new Intent(getApplicationContext(), AttractionDetailsEdit.class);
            intent.putExtra("ACTION", "modify");
            intent.putExtra("HOLIDAYID", holidayId);
            intent.putExtra("ATTRACTIONID", attractionId);
            intent.putExtra("TITLE", title);
            intent.putExtra("SUBTITLE", subTitle);
            startActivity(intent);
        }
        catch(Exception e)
        {
            ShowError("editAttraction", e.getMessage());
        }
    }

    public void deleteAttraction()
    {
        try
        {
            if(!databaseAccess().deleteAttractionItem(attractionItem))
                return;
            finish();
        }
        catch(Exception e)
        {
            ShowError("deleteAttraction", e.getMessage());
        }
    }


    @Override
    public void SwapItems(int from, int to)
    {
        try
        {
            Collections.swap(attractionAreaAdapter.data, from, to);
        }
        catch(Exception e)
        {
            ShowError("SwapItems", e.getMessage());
        }
    }

    @Override
    public void OnItemMove(int from, int to)
    {
        try
        {
            attractionAreaAdapter.onItemMove();
        }
        catch(Exception e)
        {
            ShowError("OnItemMove", e.getMessage());
        }
    }

    @Override
    public void NotifyItemMoved(int from, int to)
    {
        try
        {
            attractionAreaAdapter.notifyItemMoved(from, to);
        }
        catch(Exception e)
        {
            ShowError("NotifyItemMoved", e.getMessage());
        }
    }

    //endregion

}

