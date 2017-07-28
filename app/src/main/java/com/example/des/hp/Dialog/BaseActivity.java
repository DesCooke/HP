package com.example.des.hp.Dialog;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.example.des.hp.myutils.MyMessages;

import static com.example.des.hp.myutils.MyMessages.myMessages;

public class BaseActivity extends AppCompatActivity
{
    public String title;
    public String subTitle;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ErrorDialog.SetContext(this);
        MessageDialog.SetContext(this);
        MyMessages.SetContext(this);
    }

    public void SetTitles(String pTitle, String pSubTitle)
    {
        title=pTitle;
        subTitle= pSubTitle;
        
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setTitle(title);
            actionBar.setSubtitle(subTitle);
        }
    }

    public void showForm()
    {
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
    }
    
    protected void ShowError(String argFunction, String argMessage)
    {
        String lv_title;
        
        lv_title = this.getLocalClassName() + "::" + argFunction;
        
        ErrorDialog.Show("Error in " + lv_title, argMessage);
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        ErrorDialog.SetContext(this);
        MessageDialog.SetContext(this);
        MyMessages.SetContext(this);
        showForm();
    }
}
