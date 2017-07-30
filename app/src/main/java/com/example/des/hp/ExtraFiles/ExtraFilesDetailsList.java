package com.example.des.hp.ExtraFiles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.Dialog.BaseFullPageRecycleView;
import com.example.des.hp.R;
import com.example.des.hp.myutils.*;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

/**
 * * Created by Des on 02/11/2016.
 */

public class ExtraFilesDetailsList extends BaseFullPageRecycleView
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
                public void onItemClick(View view, ExtraFilesItem obj, int position)
                {
                    Intent intent=new Intent(getApplicationContext(), ExtraFilesDetailsView.class);
                    intent.putExtra("ACTION", "view");
                    intent.putExtra("FILEGROUPID", extraFilesList.get(position).fileGroupId);
                    intent.putExtra("FILEID", extraFilesList.get(position).fileId);
                    intent.putExtra("TITLE", title + "/" + subTitle);
                    intent.putExtra("SUBTITLE", extraFilesList.get(position).fileDescription);
                    startActivity(intent);
                }
            });
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
        Collections.swap(extraFilesAdapter.data, from, to);
    }

    @Override
    public void OnItemMove(int from, int to)
    {
        extraFilesAdapter.onItemMove(from, to);
    }

    @Override
    public void NotifyItemMoved(int from, int to)
    {
        extraFilesAdapter.notifyItemMoved(from, to);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            setContentView(R.layout.activity_extra_files_list);

            showForm();
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }

    }

}

