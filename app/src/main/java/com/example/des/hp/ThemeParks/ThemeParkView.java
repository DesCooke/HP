package com.example.des.hp.ThemeParks;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

import java.util.ArrayList;

public class ThemeParkView extends BaseActivity
{
    //region Member variables
    public ThemeParkItem themeParkItem;
    public TextView txtThemeParkName;
    public ImageButton btnClear;
    public Button btnSave;
    public ImageView btnEdit;
    public ImageView btnDelete;
    public ArrayList<ThemeParkAreaItem> themeParkAreaList;
    public ThemeParkAreaAdapter themeParkAreaAdapter;
    public FloatingActionButton fab;
    //endregion
    
    //region Constructors/Destructors
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        try
        {
            layoutName = "activity_attraction_details_view";
            setContentView(R.layout.activity_themepark_details_view);

            txtThemeParkName = findViewById(R.id.txtThemeParkName);
            btnClear = findViewById(R.id.btnClear);
            btnSave = findViewById(R.id.btnSave);
            btnDelete=findViewById(R.id.my_toolbar_delete);
            btnDelete.setOnClickListener(view -> deleteThemePark());
            btnEdit=findViewById(R.id.my_toolbar_edit);
            btnEdit.setOnClickListener(view -> editThemePark());


            fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showThemeParkAreaAdd();
                }
            });
            btnEdit=findViewById(R.id.my_toolbar_edit);
            btnEdit.setOnClickListener(view -> editThemePark());

            afterCreate();
            
            showForm();
        }
        catch (Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
    }

    public void showThemeParkAreaAdd()
    {
        try
        {
            Intent intent = new Intent(getApplicationContext(), ThemeParkAreaEdit.class);
            intent.putExtra("ACTION", "add");
            intent.putExtra("HOLIDAYID", holidayId);
            intent.putExtra("ATTRACTIONID", attractionId);
            intent.putExtra("TITLE", "Add a Park Area");
            intent.putExtra("SUBTITLE", title);
            startActivity(intent);
        }
        catch (Exception e)
        {
            ShowError("showAttractionAreaAdd", e.getMessage());
        }
    }


    public void editThemePark(){
        Intent intent = new Intent(getApplicationContext(), ThemeParkEdit.class);
        intent.putExtra("ACTION", "modify");
        intent.putExtra("HOLIDAYID", themeParkItem.holidayId);
        intent.putExtra("ATTRACTIONID", themeParkItem.attractionId);
        intent.putExtra("TITLE", themeParkItem.attractionDescription);
        intent.putExtra("SUBTITLE", subTitle);
        startActivity(intent);
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        try
        {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.attractiondetailsformmenu, menu);
        }
        catch (Exception e)
        {
            ShowError("onCreateOptionsMenu", e.getMessage());
        }
        return true;
    }
    //endregion

    //region Regular Form Activities
    public void showForm()
    {
        super.showForm();

        try(DatabaseAccess da = databaseAccess())
        {
            allowCellMove = true;
            
            themeParkItem = new ThemeParkItem();

            if(action.compareTo("view")==0)
                ShowToolbarEdit();
            if(action.compareTo("modify")==0)
                ShowToolbarDelete();

            if (action != null && action.equals("add"))
            {
                txtThemeParkName.setText("");
                SetImage("");
            } else
            {
                if (!da.getAttractionItem(holidayId, attractionId, themeParkItem))
                    return;

                txtThemeParkName.setText(themeParkItem.attractionDescription);
                
                SetImage(themeParkItem.attractionPicture);

                themeParkAreaList = new ArrayList<>();
                if (!da.getThemeParkAreaList(holidayId, attractionId, themeParkAreaList))
                    return;

                themeParkAreaAdapter = new ThemeParkAreaAdapter(this, themeParkAreaList);

                CreateRecyclerView(R.id.themeParkAreaListView, themeParkAreaAdapter);

                themeParkAreaAdapter.setOnItemClickListener((view, obj) -> {
                    Intent intent = new Intent(getApplicationContext(), ThemeParkAreaView.class);
                    intent.putExtra("ACTION", "view");
                    intent.putExtra("HOLIDAYID", obj.holidayId);
                    intent.putExtra("ATTRACTIONID", obj.attractionId);
                    intent.putExtra("ATTRACTIONAREAID", obj.attractionAreaId);
                    intent.putExtra("TITLE", obj.attractionAreaDescription);
                    intent.putExtra("SUBTITLE", title);
                    startActivity(intent);
                });

            }
            if (!title.isEmpty())
            {
                SetToolbarTitles(title, subTitle);
            } else
            {
                SetToolbarTitles(themeParkItem.attractionDescription, "");
            }

            afterShow();

        }
        catch (Exception e)
        {
            ShowError("showForm", e.getMessage());
        }
    }
    //endregion
    
    //region Notes and Info
    @Override
    public int getNoteId()
    {
        try
        {
            return (themeParkItem.noteId);
        }
        catch (Exception e)
        {
            ShowError("getNoteId", e.getMessage());
        }
        return (0);
    }
    
    @Override
    public void setNoteId(int noteId)
    {
        try
        {
            themeParkItem.noteId = noteId;
            try(DatabaseAccess da = databaseAccess())
            {
                da.updateAttractionItem(themeParkItem);
            }
        }
        catch (Exception e)
        {
            ShowError("setNoteId", e.getMessage());
        }
    }
    
    @Override
    public int getInfoId()
    {
        try
        {
            return (themeParkItem.infoId);
        }
        catch (Exception e)
        {
            ShowError("getInfoId", e.getMessage());
        }
        return (0);
    }
    
    @Override
    public void setInfoId(int infoId)
    {
        try
        {
            themeParkItem.infoId = infoId;
            try(DatabaseAccess da = databaseAccess())
            {
                da.updateAttractionItem(themeParkItem);
            }
        }
        catch (Exception e)
        {
            ShowError("setInfoId", e.getMessage());
        }
        
    }

    public void deleteThemePark()
    {
        try
        {
            try(DatabaseAccess da = databaseAccess())
            {
                if(!da.deleteAttractionItem(themeParkItem))
                    return;
            }
            finish();
        }
        catch(Exception e)
        {
            ShowError("deleteHoliday", e.getMessage());
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        try
        {
            if(action.compareTo("add")!=0) {
                try (DatabaseAccess da = databaseAccess()) {
                    if (!da.getAttractionItem(holidayId, attractionId, themeParkItem)) {
                        finish();
                    }
                    if (themeParkItem != null) {
                        if (themeParkItem.holidayId == 0) {
                            finish();
                        }
                    }
                }
            }
        }
        catch(Exception e)
        {
            ShowError("onResume", e.getMessage());
        }

    }

    //endregion
    
}