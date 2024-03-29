package com.example.des.hp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.des.hp.Budget.BudgetOptionItem;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.InternalFiles.InternalFileItem;
import com.example.des.hp.InternalImages.InternalImageItem;
import com.example.des.hp.myutils.*;
import com.example.des.hp.Holiday.*;

import java.io.File;
import java.util.ArrayList;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
import static com.example.des.hp.myutils.ImageUtils.imageUtils;
import static com.example.des.hp.myutils.MyMessages.myMessages;

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
    public boolean onOptionsItemSelected(MenuItem item)
    {
        boolean lv_result = false;
        
        try
        {
            if(MyPermissions.AccessAllowed()) {
                switch (item.getItemId()) {
                    case R.id.action_backup:
                        // don't care about return code
                        backup();
                        // consume click here
                        lv_result = true;
                        break;
                    case R.id.action_add_holiday:
                        showHolidayAdd(null);
                        // consume click here
                        lv_result = true;
                        break;
                    case R.id.action_orphaned_images:
                        handleOrphanedImages();
                        // consume click here
                        lv_result = true;
                        break;
                    default:
                        lv_result = super.onOptionsItemSelected(item);
                }
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
                        if (databaseAccess().pictureUsageCount(item.internalImageFilename) == 0) {
                            //myMessages().LogMessage("Picture " + item.internalImageFilename + ", is not linked to anything - removing");
                            databaseAccess().removePicture(holidayId, item.internalImageFilename);
                            lCount++;
                        }
                    }
                    //myMessages().LogMessage("There are a total of " + String.valueOf(internalImageList.size()) + " and " + String.valueOf(lCount) + " were orphaned");
                }

                //myMessages().LogMessage("Identifying orphaned files....");

                int lCount2 = 0;
                ArrayList<InternalFileItem> internalFileList = imageUtils().listInternalFiles(holidayId);
                if (internalFileList != null) {
                    for (InternalFileItem item : internalFileList) {
                        if (databaseAccess().fileUsageCount(holidayId, item.filename) == 0) {
                            //myMessages().LogMessage("File " + item.filename + ", is not linked to anything - removing");
                            databaseAccess().removeExtraFile(holidayId, item.filename);
                            lCount2++;
                        }
                    }
                    //myMessages().LogMessage("There are a total of " + String.valueOf(internalFileList.size()) + " and " + String.valueOf(lCount2) + " were orphaned");

                    if (internalImageList != null)
                        myMessages().ShowMessageLong("Images: Orphaned " + String.valueOf(lCount) + ", " + "Total " + String.valueOf(internalImageList.size()) + ", " + "Files: Orphaned " + String.valueOf(lCount2) + ", " + "Total " + String.valueOf(internalFileList.size()) + " ");
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
                File f = new File(MyFileUtils.MyDocuments() + "/" +
                        getResources().getString(R.string.application_file_path) + "/" +
                        getResources().getString(R.string.database_filename));
                boolean needToCreateSampleDatabase = false;
                if (!f.exists())
                    needToCreateSampleDatabase = true;

                accessGranted = true;
                invalidateOptionsMenu();

                holidayList = new ArrayList<>();
                if (!databaseAccess().getHolidayList(holidayList))
                    return;

                HolidayAdapter adapter = new HolidayAdapter(this, R.layout.holidaylistitemrow, holidayList);
                ListView listView1 = (ListView) findViewById(R.id.holidayListView);
                listView1.setOnItemClickListener
                        (
                                new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Intent intent = new Intent(getApplicationContext(), HolidayDetailsView.class);
                                        intent.putExtra("ACTION", "view");
                                        intent.putExtra("HOLIDAYID", holidayList.get(position).holidayId);
                                        startActivity(intent);
                                    }
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
    
    public boolean backup()
    {
        boolean lv_result = false;
        try
        {
            if(MyPermissions.AccessAllowed()) {
                lv_result = archiveRestore.Archive();
            }
        }
        catch (Exception e)
        {
            ShowError("backup", e.getMessage());
        }
        return (lv_result);
    }
    
    //endregion
    
}
