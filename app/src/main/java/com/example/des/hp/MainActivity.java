package com.example.des.hp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.InternalFiles.InternalFileItem;
import com.example.des.hp.InternalImages.InternalImageItem;
import com.example.des.hp.myutils.*;
import com.example.des.hp.Holiday.*;

import java.util.ArrayList;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
import static com.example.des.hp.myutils.ImageUtils.imageUtils;
import static com.example.des.hp.myutils.MyMessages.myMessages;

import androidx.annotation.NonNull;

public class MainActivity extends BaseActivity
{
        
    //region Member variables
    public ArrayList<HolidayItem> holidayList;
    public ArchiveRestore archiveRestore;
    public boolean accessGranted = false;

    private static MainActivity instance;
    //endregion
    
    //region Single Instance Static
    public static MainActivity getInstance()
    {
        return instance;
    }
    //endregion
    
    //region Constructors/Destructors
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        try
        {
            instance = this;
            layoutName = "activity_main";
            setContentView(R.layout.activity_main);

            MyPermissions.EnsureAccessToExternalDrive(this);
            if(MyPermissions.AccessAllowed())
            {
                archiveRestore = new ArchiveRestore(this);

                myMessages().ClearLog();

                afterCreate();

                showForm();
            }
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
            if (accessGranted)
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
    //endregion
    
    //region OnClick Events
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        boolean lv_result = false;
        
        try
        {
            if(MyPermissions.AccessAllowed()) {
                int id = item.getItemId();
                if (id == R.id.action_backup)
                    backup();
                if (id == R.id.action_add_holiday)
                    showHolidayAdd(null);
                if (id == R.id.action_orphaned_images)
                    handleOrphanedImages();
            }
        }
        catch (Exception e)
        {
            ShowError("onOptionsItemSelected", e.getMessage());
        }
        return (lv_result);
    }
    
    public void handleOrphanedImages()
    {
        try{
            if(MyPermissions.AccessAllowed()) {
                int lCount = 0;
                //myMessages().LogMessage("Identifying orphaned images....");

                ArrayList<InternalImageItem> internalImageList = imageUtils().listInternalImages(holidayId);
                if (internalImageList != null) {
                    for (InternalImageItem item : internalImageList) {

                        try(DatabaseAccess da = databaseAccess())
                        {
                            if(da.pictureUsageCount(item.internalImageFilename) == 0) {
                              da.removePicture(holidayId, item.internalImageFilename);
                              lCount++;
                            }
                        }
                    }
                    //myMessages().LogMessage("There are a total of " + String.valueOf(internalImageList.size()) + " and " + String.valueOf(lCount) + " were orphaned");
                }

                //myMessages().LogMessage("Identifying orphaned files....");

                try(DatabaseAccess da = databaseAccess())
                {
                    int lCount2 = 0;
                    ArrayList<InternalFileItem> internalFileList = imageUtils().listInternalFiles(holidayId);
                    if (internalFileList != null) {
                        for (InternalFileItem item : internalFileList) {
                            if (da.fileUsageCount(holidayId, item.filename) == 0) {
                                da.removeExtraFile(holidayId, item.filename);
                                lCount2++;
                            }
                        }

                        if (internalImageList != null)
                            myMessages().ShowMessageLong("Images: Orphaned " + lCount + ", " + "Total " + internalImageList.size() + ", " + "Files: Orphaned " + lCount2 + ", " + "Total " + internalFileList.size() + " ");
                    }
                }

            }
        }
        catch(Exception e)
        {
            ShowError("handleOrphanedImages", e.getMessage());
        }

    }
    //endregion
    
    //region showForm
    public void showForm()
    {
        super.showForm();
        
        try
        {
            SetTitles(getResources().getString(R.string.title_planner), "");

            if(MyPermissions.AccessAllowed()) {
                accessGranted = true;
                invalidateOptionsMenu();

                holidayList = new ArrayList<>();
                try(DatabaseAccess da = databaseAccess())
                {
                    if (!da.getHolidayList(holidayList))
                        return;
                }

                HolidayAdapter adapter = new HolidayAdapter(this, R.layout.holidaylistitemrow, holidayList);
                ListView listView1 = findViewById(R.id.holidayListView);
                listView1.setOnItemClickListener
                        (
                                (parent, view, position, id) -> {
                                    Intent intent = new Intent(getApplicationContext(), HolidayDetailsView.class);
                                    intent.putExtra("ACTION", "view");
                                    intent.putExtra("HOLIDAYID", holidayList.get(position).holidayId);
                                    startActivity(intent);
                                }
                        );
                listView1.setDivider(null);
                listView1.setAdapter(adapter);

                afterShow();
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
    //endregion
    
    //region form Functions
    public void showHolidayAdd(View view)
    {
        try
        {
            if(MyPermissions.AccessAllowed()) {
                Intent intent = new Intent(getApplicationContext(), HolidayDetailsEdit.class);
                intent.putExtra("ACTION", "add");
                startActivity(intent);
            }
        }
        catch (Exception e)
        {
            ShowError("showHolidayAdd", e.getMessage());
        }
    }
    
    public void backup()
    {
    }
    
    //endregion
    
}
