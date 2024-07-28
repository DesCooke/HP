package com.example.des.hp.TipGroup;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.R;
import com.example.des.hp.Tip.TipAdapter;
import com.example.des.hp.Tip.TipDetailsEdit;
import com.example.des.hp.Tip.TipDetailsView;
import com.example.des.hp.Tip.TipItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

import java.util.ArrayList;

public class TipGroupDetailsView extends BaseActivity
{

    //region Member variables
    public TipGroupItem tipGroupItem;
    public TextView txtTipGroupDescription;
    public ImageButton btnClear;
    public Button btnSave;
    public LinearLayout grpTipGroupDescription;
    public LinearLayout grpMenuFile;
    public ImageView btnEdit;
    public ImageView btnDelete;
    public FloatingActionButton fab;
    public ArrayList<TipItem> tipItemList;
    public TipAdapter tipAdaptor;
    //endregion

    protected void onResume()
    {
        super.onResume();
        try
        {
            if(action.compareTo("add")!=0) {
                tipGroupItem=new TipGroupItem();
                try(DatabaseAccess da = databaseAccess())
                {
                    if(!da.getTipGroupItem(holidayId, tipGroupId, tipGroupItem))
                        finish();
                    if(tipGroupItem.holidayId==0)
                        finish();
                }
            }
        }
        catch(Exception e)
        {
            ShowError("onResume", e.getMessage());
        }

    }

    //region Constructors/Destructors
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            setContentView(R.layout.activity_tipgroup_details_view);

            imageView= findViewById(R.id.imageViewSmall);
            txtTipGroupDescription= findViewById(R.id.txtTipGroupDescription);
            btnClear= findViewById(R.id.btnClear);
            btnSave= findViewById(R.id.btnSave);
            grpTipGroupDescription= findViewById(R.id.grpTipGroupDescription);
            btnEdit = findViewById(R.id.my_toolbar_edit);
            btnDelete = findViewById(R.id.my_toolbar_delete);
            btnEdit.setOnClickListener(view -> EditTipGroup());
            btnDelete.setOnClickListener(view -> DeleteTipGroup());
            fab = findViewById(R.id.fab);
            fab.setOnClickListener(view -> AddATip());

            afterCreate();

            showForm();
        }
        catch(Exception e)
        {
            ShowError("Error in onCreate", e.getMessage());
        }
    }

    public void EditTipGroup(){
        Intent intent=new Intent(getApplicationContext(), TipGroupDetailsEdit.class);
        intent.putExtra("ACTION", "modify");
        intent.putExtra("HOLIDAYID", tipGroupItem.holidayId);
        intent.putExtra("TIPGROUPID", tipGroupItem.tipGroupId);
        intent.putExtra("TITLE", title);
        intent.putExtra("SUBTITLE", subTitle);
        startActivity(intent);
    }

    public void AddATip(){
        Intent intent=new Intent(getApplicationContext(), TipDetailsEdit.class);
        intent.putExtra("ACTION", "add");
        intent.putExtra("HOLIDAYID", tipGroupItem.holidayId);
        intent.putExtra("TIPGROUPID", tipGroupItem.tipGroupId);
        intent.putExtra("TITLE", "Add a Tip");
        intent.putExtra("SUBTITLE", title);
        startActivity(intent);
    }


    public void DeleteTipGroup(){
        try(DatabaseAccess da = databaseAccess())
        {
            da.deleteTipGroupItem(tipGroupItem);
            finish();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        /* disable the menu entirely */
        return false;
    }
    //endregion

    //region showForm
    public void showForm()
    {
        super.showForm();

        try(DatabaseAccess da = databaseAccess())
        {
            tipGroupItem=new TipGroupItem();

            if(!da.getTipGroupItem(holidayId, tipGroupId, tipGroupItem))
                return;

            if(title == null || (title.isEmpty()))
            {
                SetToolbarTitles(tipGroupItem.tipGroupDescription, "");
            } else
            {
                SetToolbarTitles(title, subTitle);
            }

            SetImage(tipGroupItem.tipGroupPicture);
            txtTipGroupDescription.setText(tipGroupItem.tipGroupDescription);

            if(action.compareTo("view")==0){
                ShowToolbarEdit();
            }
            if(action.compareTo("modify")==0){
                ShowToolbarDelete();
            }



            tipItemList = new ArrayList<>();
            if (!da.getTipList(holidayId, tipGroupId, tipItemList))
                return;

            tipAdaptor = new TipAdapter(this, tipItemList);

            CreateRecyclerView(R.id.tipListView, tipAdaptor);

            tipAdaptor.setOnItemClickListener((view, obj) -> {
                Intent intent = new Intent(getApplicationContext(), TipDetailsView.class);
                intent.putExtra("ACTION", "view");
                intent.putExtra("HOLIDAYID", obj.holidayId);
                intent.putExtra("TIPGROUPID", obj.tipGroupId);
                intent.putExtra("TIPID", obj.tipId);
                intent.putExtra("TITLE", obj.tipDescription);
                intent.putExtra("SUBTITLE", title);
                startActivity(intent);
            });

            afterShow();
        }
        catch(Exception e)
        {
            ShowError("showForm", e.getMessage());
        }
    }
    //endregion

    //region form Functions
    @Override
    public int getInfoId()
    {
        try
        {
            return (tipGroupItem.infoId);
        }
        catch(Exception e)
        {
            ShowError("getInfoId", e.getMessage());
        }
        return (0);

    }

    public void setNoteId(int pNoteId)
    {
        try
        {
            tipGroupItem.noteId=pNoteId;
            try(DatabaseAccess da = databaseAccess())
            {
                da.updateTipGroupItem(tipGroupItem);
            }
        }
        catch(Exception e)
        {
            ShowError("setNoteId", e.getMessage());
        }

    }

    @Override
    public int getNoteId()
    {
        try
        {
            return (tipGroupItem.noteId);
        }
        catch(Exception e)
        {
            ShowError("getNoteId", e.getMessage());
        }
        return (0);
    }

    @Override
    public void setInfoId(int pInfoId)
    {
        try
        {
            tipGroupItem.infoId=pInfoId;
            try(DatabaseAccess da = databaseAccess())
            {
                da.updateTipGroupItem(tipGroupItem);
            }
        }
        catch(Exception e)
        {
            ShowError("setInfoId", e.getMessage());
        }

    }
    //endregion
}
