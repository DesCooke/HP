package com.example.des.hp.InternalImages;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.R;

import java.util.ArrayList;

import static com.example.des.hp.myutils.ImageUtils.imageUtils;


public class InternalImageList extends BaseActivity
{
    public ArrayList<InternalImageItem> internalImageList;
    public InternalImageAdapter internalImageAdapter;

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

            internalImageAdapter=new InternalImageAdapter(this, internalImageList);

            gridLayout=true;

            CreateRecyclerView(R.id.internalImageListView, internalImageAdapter);

            internalImageAdapter.setOnItemClickListener(new InternalImageAdapter.OnItemClickListener()
            {
                @Override
                public void onItemClick(View view, InternalImageItem obj)
                {
                    Intent resultIntent=new Intent();
                    resultIntent.putExtra("selectedfile", obj.internalImageFilename);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
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

