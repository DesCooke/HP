package com.example.des.hp.Tip;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.R;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

import androidx.annotation.NonNull;

public class TipDetailsView extends BaseActivity
{

    //region Member variables
    public TipItem tipItem;
    public TextView txtTipDescription;
    public TextView txtTipNotes;
    public ImageButton btnClear;
    public Button btnSave;
    public LinearLayout grpTipDescription;
    public LinearLayout grpMenuFile;
    public ImageView btnEdit;
    public ImageView btnDelete;
    //endregion

    //region Constructors/Destructors
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            layoutName="activity_tip_details_view";
            setContentView(R.layout.activity_tip_details_view);

            txtTipDescription= findViewById(R.id.txtTipDescription);
            txtTipNotes= findViewById(R.id.txtTipNotes);
            btnClear= findViewById(R.id.btnClear);
            btnSave= findViewById(R.id.btnSave);
            grpTipDescription= findViewById(R.id.grpTipDescription);
            grpMenuFile= findViewById(R.id.grpMenuFile);
            btnEdit=findViewById(R.id.my_toolbar_edit);
            btnDelete=findViewById(R.id.my_toolbar_delete);
            btnEdit.setOnClickListener(view -> editTip());
            btnDelete.setOnClickListener(view -> deleteTip());

            afterCreate();

            showForm();
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        try
        {
            MenuInflater inflater=getMenuInflater();
            inflater.inflate(R.menu.tipdetailsformmenu, menu);
        }
        catch(Exception e)
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
        try {
            int id = item.getItemId();
            if (id == R.id.action_delete_tip)
                deleteTip();
            if (id == R.id.action_edit_tip)
                editTip();
        }
        catch(Exception e)
        {
            ShowError("onOptionsItemSelected", e.getMessage());
        }
        return true;
    }
    //endregion

    //region showForm
    public void showForm()
    {
        super.showForm();
        try
        {
            tipItem=new TipItem();
            try(DatabaseAccess da = databaseAccess())
            {
                if(!da.getTipItem(holidayId, tipGroupId, tipId, tipItem))
                    return;
            }

            if(title == null || (title.isEmpty()))
            {
                SetToolbarTitles(tipItem.tipDescription, "");
            } else
            {
                SetToolbarTitles(title, subTitle);
            }

            SetImage(tipItem.tipPicture);
            txtTipDescription.setText(tipItem.tipDescription);

            txtTipNotes.setText(tipItem.tipNotes);

            if(action.compareTo("view")==0){

                ShowToolbarEdit();
            }
            if(action.compareTo("modify")==0){
                ShowToolbarDelete();
            }

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
            return (tipItem.infoId);
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
                tipItem.noteId = pNoteId;
                try (DatabaseAccess da = databaseAccess()) {
                    da.updateTipItem(tipItem);
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
            return (tipItem.noteId);
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
            tipItem.infoId=pInfoId;
            try(DatabaseAccess da = databaseAccess())
            {
                da.updateTipItem(tipItem);
            }
        }
        catch(Exception e)
        {
            ShowError("setInfoId", e.getMessage());
        }

    }


    public void editTip()
    {
        try
        {
            Intent intent=new Intent(getApplicationContext(), TipDetailsEdit.class);
            intent.putExtra("ACTION", "modify");
            intent.putExtra("HOLIDAYID", holidayId);
            intent.putExtra("TIPGROUPID", tipGroupId);
            intent.putExtra("TIPID", tipId);
            intent.putExtra("TITLE", title);
            intent.putExtra("SUBTITLE", subTitle);
            startActivity(intent);
        }
        catch(Exception e)
        {
            ShowError("editTip", e.getMessage());
        }

    }

    public void deleteTip()
    {
        try
        {
            try(DatabaseAccess da = databaseAccess())
            {
                if(!da.deleteTipItem(tipItem))
                    return;
            }
            finish();
        }
        catch(Exception e)
        {
            ShowError("deleteTip", e.getMessage());
        }

    }
    //endregion

    @Override
    protected void onResume()
    {
        super.onResume();
        try
        {
            if(action.compareTo("add")!=0) {
                try (DatabaseAccess da = databaseAccess()) {
                    if (!da.getTipItem(holidayId, tipGroupId, tipId, tipItem)) {
                        finish();
                    }
                    if (tipItem != null) {
                        if (tipItem.holidayId == 0) {
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


}
