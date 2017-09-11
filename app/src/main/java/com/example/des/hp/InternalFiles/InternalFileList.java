package com.example.des.hp.InternalFiles;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.R;

import java.util.ArrayList;

import static com.example.des.hp.myutils.ImageUtils.imageUtils;


public class InternalFileList extends BaseActivity
{
    public ArrayList<InternalFileItem> internalFileList;
    public InternalFileAdapter internalFileAdapter;

    public void showForm()
    {
        super.showForm();
        try
        {
            SetTitles("Internal Files", "please select one");

            internalFileList=imageUtils().listInternalFiles();

            internalFileAdapter=new InternalFileAdapter(this, internalFileList);

            CreateRecyclerView(R.id.internalFileListView, internalFileAdapter);

            internalFileAdapter.setOnItemClickListener(new InternalFileAdapter.OnItemClickListener()
            {
                @Override
                public void onItemClick(View view, InternalFileItem obj)
                {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("selectedfile", obj.filename);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();                }
            });

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

