package com.example.des.hp.InternalImages;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.des.hp.Dialog.BaseFullPageRecycleView;
import com.example.des.hp.Dialog.BaseItem;
import com.example.des.hp.R;
import com.example.des.hp.Holiday.*;
import com.example.des.hp.myutils.MyMessages;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
import static com.example.des.hp.myutils.ImageUtils.imageUtils;
import static com.example.des.hp.myutils.MyMessages.myMessages;


public class InternalImageList extends BaseFullPageRecycleView
{
    public ArrayList<InternalImageItem> internalImageList;
    public InternalImageAdapter internalImageAdapter;

    public void showForm()
    {
        super.showForm();
        try
        {
            SetTitles("Internal Images", "please select one");

            internalImageList=imageUtils().listInternalImages();
            
            internalImageAdapter=new InternalImageAdapter(this, internalImageList);

            gridLayout=true;

            CreateRecyclerView(R.id.internalImageListView, internalImageAdapter);

            internalImageAdapter.setOnItemClickListener(new InternalImageAdapter.OnItemClickListener()
            {
                @Override
                public void onItemClick(View view, InternalImageItem obj, int position)
                {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("selectedfile", internalImageList.get(position).internalImageFilename);
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
            setContentView(R.layout.activity_internalimage_list);

            showForm();
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }

    }

}

