package com.example.des.hp.ExtraFiles;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.R;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

/**
 * * Created by Des on 02/11/2016.
 */

public class ExtraFilesDetailsList extends BaseActivity
{

    public ArrayList<ExtraFilesItem> extraFilesList;
    public ExtraFilesAdapter extraFilesAdapter;

    public void showMapAdd(View view)
    {
        try
        {
            Intent intent=new Intent(getApplicationContext(), ExtraFilesDetailsEdit.class);
            intent.putExtra("ACTION", "add");
            intent.putExtra("FILEGROUPID", fileGroupId);
            startActivity(intent);
        }
        catch(Exception e)
        {
            ShowError("showMapAdd", e.getMessage());
        }
    }

    public void showForm()
    {
        super.showForm();
        try
        {
            allowCellMove=true;

            if(title.length() > 0)
            {
                SetTitles(title, subTitle);
            } else
            {
                SetTitles("File Group", "Extra Files");
            }

            extraFilesList=new ArrayList<>();
            if(!databaseAccess().getExtraFilesList(fileGroupId, extraFilesList))
                return;
            extraFilesAdapter=new ExtraFilesAdapter(this, extraFilesList);

            CreateRecyclerView(R.id.extraFilesListView, extraFilesAdapter);

            extraFilesAdapter.setOnItemClickListener(new ExtraFilesAdapter.OnItemClickListener()
            {
                @Override
                public void onItemClick(View view, ExtraFilesItem obj)
                {
                    Intent intent=new Intent(getApplicationContext(), ExtraFilesDetailsView.class);
                    intent.putExtra("ACTION", "view");
                    intent.putExtra("FILEGROUPID", obj.fileGroupId);
                    intent.putExtra("FILEID", obj.fileId);
                    intent.putExtra("TITLE", title + "/" + subTitle);
                    intent.putExtra("SUBTITLE", obj.fileDescription);
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

    @Override
    public void SwapItems(int from, int to)
    {
        try
        {
            Collections.swap(extraFilesAdapter.data, from, to);
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
            extraFilesAdapter.onItemMove(from, to);
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
            extraFilesAdapter.notifyItemMoved(from, to);
        }
        catch(Exception e)
        {
            ShowError("NotifyItemMoved", e.getMessage());
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            layoutName="activity_extra_files_list";
            setContentView(R.layout.activity_extra_files_list);

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
            inflater.inflate(R.menu.extrafilesdetailslistmenu, menu);
        }
        catch(Exception e)
        {
            ShowError("onCreateOptionsMenu", e.getMessage());
        }
        return true;
    }

    //region OnClick Events
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        boolean lv_result=false;

        try
        {
            switch(item.getItemId())
            {
                case R.id.action_add_extra_files:
                    showMapAdd(null);
                    // consume click here
                    lv_result=true;
                    break;
                default:
                    lv_result=super.onOptionsItemSelected(item);
            }
        }
        catch(Exception e)
        {
            ShowError("onOptionsItemSelected", e.getMessage());
        }
        return (lv_result);
    }
    //endregion


}

