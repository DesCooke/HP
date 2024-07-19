package com.example.des.hp.ExtraFiles;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.R;
import com.example.des.hp.myutils.ImageUtils;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
import static com.example.des.hp.myutils.MyFileUtils.myFileUtils;

import androidx.annotation.NonNull;

/**
 * * Created by Des on 02/11/2016.
 */

public class ExtraFilesDetailsList extends BaseActivity
{

    public ArrayList<ExtraFilesItem> extraFilesList;
    private ExtraFilesAdapter extraFilesAdapter;

    public void showMapAdd(View view)
    {
        try
        {
            action="add";
            pickFile(view);
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
            allowCellSwipe=true;
            allowDelete=true;

            if(!title.isEmpty())
            {
                SetTitles(title, subTitle);
            } else
            {
                SetTitles("File Group", "Extra Files");
            }

            extraFilesList=new ArrayList<>();
            try(DatabaseAccess da = databaseAccess())
            {
                if(!da.getExtraFilesList(fileGroupId, extraFilesList))
                    return;
            }
            extraFilesAdapter=new ExtraFilesAdapter(extraFilesList);

            CreateRecyclerView(R.id.extraFilesListView, extraFilesAdapter);

            extraFilesAdapter.setOnItemClickListener((view, obj) -> {
                if(!obj.fileName.isEmpty())
                {
                    String lDir = ImageUtils.imageUtils().GetHolidayFileDir(holidayId);
                    myFileUtils().OpenAFile(lDir + "/" + obj.fileName);
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
    public void DeleteItemAtPos(int pos)
    {
        try
        {
            extraFilesAdapter.DeleteItemAtPos(pos);
        }
        catch(Exception e)
        {
            ShowError("DeleteItemAtPos", e.getMessage());
        }
    }


    @Override
    public void OnItemMove(int from, int to)
    {
        try
        {
            extraFilesAdapter.onItemMove();
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        boolean lv_result=false;

        try
        {
            int id=item.getItemId();
            if(id==R.id.action_add_extra_files) {
                showMapAdd(null);
                lv_result=true;
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

