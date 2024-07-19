package com.example.des.hp.InternalImages;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.R;

import java.util.ArrayList;

import static com.example.des.hp.myutils.ImageUtils.imageUtils;


public class InternalImageList extends BaseActivity
{
    public ArrayList<InternalImageItem> internalImageList;

    public void showForm()
    {
        super.showForm();
        try
        {
            SetTitles("Internal Images", "please select one");

            Bundle extras=getIntent().getExtras();
            if(extras != null) {
                holidayId = extras.getInt("HOLIDAYID", 0);
            }

            internalImageList=imageUtils().listInternalImages(holidayId);

            InternalImageAdapter internalImageAdapter = new InternalImageAdapter(this, internalImageList);

            gridLayout=true;

            CreateRecyclerView(R.id.internalImageListView, internalImageAdapter);

            internalImageAdapter.setOnItemClickListener((view, obj) -> {
                Intent resultIntent=new Intent();
                resultIntent.putExtra("selectedfile", obj.internalImageFilename);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
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
            setContentView(R.layout.activity_internalimage_list);

            showForm();
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }

    }

}

