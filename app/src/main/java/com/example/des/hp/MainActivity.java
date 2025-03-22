package com.example.des.hp;

import static android.os.ParcelFileDescriptor.MODE_CREATE;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.myutils.*;
import com.example.des.hp.Holiday.*;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import static com.example.des.hp.Database.DatabaseAccess.database;
import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
import static com.example.des.hp.myutils.MyMessages.myMessages;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends BaseActivity
{
        
    //region Member variables
    public ArrayList<HolidayItem> holidayList;
    public boolean accessGranted = false;
    public FloatingActionButton fab;
    public FloatingActionButton fabExport;

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
            Toolbar myToolbar = findViewById(R.id.my_toolbar);
            setSupportActionBar(myToolbar);
            Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

            MyPermissions.EnsureAccessToExternalDrive(this);
            if(MyPermissions.AccessAllowed())
            {
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
    
    //endregion
    
    //region OnClick Events
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        boolean lv_result = false;
        
        try
        {
            if(!MyPermissions.AccessAllowed()) {
                throw new Exception("Access Not Allowed");
            }
        }
        catch (Exception e)
        {
            ShowError("onOptionsItemSelected", e.getMessage());
        }
        return (lv_result);
    }

    //endregion
    
    //region showForm
    public void showForm()
    {
        super.showForm();
        
        try
        {
            SetToolbarTitles(getResources().getString(R.string.title_planner),
                    getResources().getString(R.string.title_version));

            if(MyPermissions.AccessAllowed()) {
                accessGranted = true;
                invalidateOptionsMenu();

                holidayList = new ArrayList<>();
                try(DatabaseAccess da = databaseAccess())
                {
                    if (!da.getHolidayList(holidayList))
                        return;
                }

                fab=findViewById(R.id.fab);
                if(fab!=null)
                    fab.setOnClickListener(this::showHolidayAdd);
                fabExport = findViewById(R.id.fabExport);
                if(fabExport!=null)
                    fabExport.setOnClickListener(this::exportDB);

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

    public void exportDB(View view)
    {
        try
        {
            String theDateTime = android.text.format.DateFormat.format("yyyy-MM-dd_HH-mm-ss", Calendar.getInstance().getTime()).toString();
            String filename = "/storage/emulated/0/Download/hp_" + theDateTime + ".csv";
            File file = new File(filename);
            Uri uri = Uri.fromFile(file);

            ParcelFileDescriptor pdf =
                    this.getContentResolver().openFileDescriptor(uri, "w");
            FileDescriptor fd = pdf.getFileDescriptor();
            FileOutputStream fileStream = new FileOutputStream(fd);
            OutputStreamWriter chapterWriter = new OutputStreamWriter(fileStream);
            BufferedWriter buffwriter = new BufferedWriter(chapterWriter);
            buffwriter.write("HolidayPlanner\n");
            buffwriter.flush();
            pdf.close();

            ContentResolver cr = this.getContentResolver();
            databaseAccess().export(uri, cr);

        }
        catch (Exception e)
        {
            ShowError("showHolidayAdd", e.getMessage());

        }
    }
    //endregion
    
}
