package com.example.des.hp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.Dialog.ErrorDialog;
import com.example.des.hp.myutils.*;
import com.example.des.hp.Holiday.*;

import java.util.ArrayList;

public class MainActivity extends BaseActivity
{
    
    public DatabaseAccess databaseAccess;
    public ArrayList<HolidayItem> holidayList;
    public MyMessages myMessages;
    public ArchiveRestore archiveRestore;
    public boolean accessGranted = false;
    
    public void showHolidayAdd(View view)
    {
        Intent intent = new Intent(getApplicationContext(), HolidayDetailsEdit.class);
        intent.putExtra("ACTION", "add");
        startActivity(intent);
    }
    
    public void showForm()
    {
        try
        {
            ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton);
            if (MyPermissions.checkIfAlreadyhavePermission(this) == false)
            {
                setTitle(getResources().getString(R.string.title_waitingforpermission));
                imageButton.setVisibility(View.INVISIBLE);
                MyPermissions.requestForSpecificPermission(this);
            } else
            {
                setTitle(getResources().getString(R.string.title_planner));
                imageButton.setVisibility(View.VISIBLE);
                accessGranted = true;
                invalidateOptionsMenu();
                
                databaseAccess = new DatabaseAccess(this);
                holidayList = new ArrayList<>();
                if (!databaseAccess.getHolidayList(holidayList))
                    return;
                HolidayAdapter adapter = new HolidayAdapter(this, R.layout.holidaylistitemrow, holidayList);
                ListView listView1 = (ListView) findViewById(R.id.holidayListView);
                listView1.setOnItemClickListener
                    (
                        new AdapterView.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                            {
                                Intent intent = new Intent(getApplicationContext(), HolidayDetailsView.class);
                                intent.putExtra("ACTION", "view");
                                intent.putExtra("HOLIDAYID", holidayList.get(position).holidayId);
                                startActivity(intent);
                            }
                        }
                    );
                listView1.setDivider(null);
                listView1.setAdapter(adapter);
                
            }
        }
        catch (Exception e)
        {
            ErrorDialog.Show("Error in MainActivity", e.getMessage());
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
        catch (Exception e)
        {
            ShowError("onResume", e.getMessage());
        }
        
    }
    
    private void ShowError(String argFunction, String argMessage)
    {
        myMessages.ShowError
            (
                "Error in MainActivity::" + argFunction,
                argMessage
            );
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        myMessages = new MyMessages(this);
        archiveRestore = new ArchiveRestore(this);
        
        myMessages.ClearLog();
        
        try
        {
            showForm();
        }
        catch (Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
        
    }
    
    public boolean onCreateOptionsMenu(Menu menu)
    {
        if (accessGranted == true)
        {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.mainactivitymenu, menu);
        }
        return true;
    }
    
    public boolean backup()
    {
        return (archiveRestore.Archive());
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_backup:
                // don't care about return code
                backup();
                // consume click here
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    
}
