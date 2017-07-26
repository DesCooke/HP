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

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
import static com.example.des.hp.myutils.MyMessages.myMessages;

public class MainActivity extends BaseActivity
{
    
    public ArrayList<HolidayItem> holidayList;
    public ArchiveRestore archiveRestore;
    public boolean accessGranted = false;
    
    private static MainActivity instance;
    
    public static MainActivity getInstance()
    {
        return instance;
    }

    public void showHolidayAdd(View view)
    {
        try
        {
            Intent intent = new Intent(getApplicationContext(), HolidayDetailsEdit.class);
            intent.putExtra("ACTION", "add");
            startActivity(intent);
        }
        catch (Exception e)
        {
            ShowError("showHolidayAdd", e.getMessage());
        }
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
                
                holidayList = new ArrayList<>();
                if (!databaseAccess().getHolidayList(holidayList))
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
            ShowError("ShowForm", e.getMessage());
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
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_main);
        
        try
        {
            archiveRestore = new ArchiveRestore(this);
            
            myMessages().ClearLog();
            
            showForm();
        }
        catch (Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
        
    }
    
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try
        {
            if (accessGranted == true)
            {
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.mainactivitymenu, menu);
            }
        }
        catch (Exception e)
        {
            ShowError("onCreateOptionsMenu", e.getMessage());
        }
        return true;
    }
    
    public boolean backup()
    {
        boolean lv_result = false;
        try
        {
            lv_result = archiveRestore.Archive();
        }
        catch (Exception e)
        {
            ShowError("backup", e.getMessage());
        }
        return (lv_result);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        boolean lv_result = false;
        
        try
        {
            switch (item.getItemId())
            {
                case R.id.action_backup:
                    // don't care about return code
                    backup();
                    // consume click here
                    lv_result = true;
                    break;
                default:
                    lv_result = super.onOptionsItemSelected(item);
            }
        }
        catch (Exception e)
        {
            ShowError("onOptionsItemSelected", e.getMessage());
        }
        return (lv_result);
    }
    
    
}
