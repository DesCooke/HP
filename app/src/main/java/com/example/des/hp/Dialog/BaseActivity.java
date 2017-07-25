package com.example.des.hp.Dialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ErrorDialog.SetContext(this);
        MessageDialog.SetContext(this);
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
    }
}
