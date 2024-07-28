package com.example.des.hp.InternalFiles;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.R;

import java.util.ArrayList;

import static com.example.des.hp.myutils.ImageUtils.imageUtils;


public class InternalFileList extends BaseActivity
{
    public ArrayList<InternalFileItem> internalFileList;

    public void showForm()
    {
        super.showForm();
        try
        {
            SetToolbarTitles("Internal Files", "please select one");

            internalFileList=imageUtils().listInternalFiles(getHolidayName(holidayId));

            InternalFileAdapter internalFileAdapter = new InternalFileAdapter(internalFileList);

            CreateRecyclerView(R.id.internalFileListView, internalFileAdapter);

            internalFileAdapter.setOnItemClickListener((view, obj) -> {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedfile", obj.filename);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();                });

        }
        catch(Exception e)
        {
            ShowError("showForm", e.getMessage());
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        try
        {
            setContentView(R.layout.activity_internalfile_list);

            showForm();
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }

    }

}

