package com.example.des.hp.Budget;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.R;
import com.example.des.hp.myutils.*;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

import androidx.annotation.NonNull;

public class BudgetDetailsView extends BaseActivity
{
    
    //region Member variables
    public BudgetItem budgetItem;
    public TextView txtBudgetDescription;
    public TextView txtBudgetTotal;
    public TextView txtBudgetPaid;
    public TextView txtBudgetUnpaid;
    public TextView txtNotes;
    public ImageButton btnClear;
    public Button btnSave;
    public LinearLayout grpMenuFile;
    public LinearLayout grpBudgetDescription;
    public LinearLayout grpBudgetTotal;
    public LinearLayout grpBudgetPaid;
    public LinearLayout grpBudgetUnpaid;
    public ImageView btnEdit;
    public ImageView btnDelete;
    public Switch swActive;
    public TextView lblActive;
    //endregion
    
    //region Constructors/Destructors
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        try
        {
            layoutName = "activity_tip_details_view";
            setContentView(R.layout.activity_budget_details_view);
            
            imageView = findViewById(R.id.imageViewSmall);
            txtBudgetDescription = findViewById(R.id.txtBudgetDescription);
            txtBudgetPaid = findViewById(R.id.txtBudgetPaid);
            txtBudgetUnpaid = findViewById(R.id.txtBudgetUnpaid);
            txtBudgetTotal = findViewById(R.id.txtBudgetTotal);
            txtNotes = findViewById(R.id.txtNotes);
            btnClear = findViewById(R.id.btnClear);
            btnSave = findViewById(R.id.btnSave);
            grpMenuFile = findViewById(R.id.grpMenuFile);
            grpBudgetDescription = findViewById(R.id.grpBudgetDescription);
            grpBudgetTotal = findViewById(R.id.grpBudgetTotal);
            grpBudgetPaid = findViewById(R.id.grpBudgetPaid);
            grpBudgetUnpaid = findViewById(R.id.grpBudgetUnpaid);
            btnEdit = findViewById(R.id.my_toolbar_edit);
            btnDelete = findViewById(R.id.my_toolbar_delete);
            swActive = findViewById(R.id.swActive);
            lblActive = findViewById(R.id.lblActive);

            btnEdit.setOnClickListener(view -> editBudget());

            afterCreate();
            
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
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.budgetdetailsformmenu, menu);
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
        try
        {
            int id=item.getItemId();
            if(id==R.id.action_delete_budget)
                deleteBudget();
            if(id==R.id.action_edit_budget)
                editBudget();
        }
        catch (Exception e)
        {
            ShowError("onOptionsItemSelected", e.getMessage());
        }
        return (true);
    }
    //endregion
    
    //region showForm
    public void showForm()
    {
        super.showForm();
        try
        {
            btnShowInfo.setVisibility(View.GONE);
            btnSave.setVisibility(View.GONE);
            swActive.setEnabled(false);

            budgetItem = new BudgetItem();
            try(DatabaseAccess da = databaseAccess())
            {
                if (!da.getBudgetItem(holidayId, budgetId, budgetItem))
                    return;
            }

            SetToolbarTitles(title, subTitle);

            if(action.compareTo("view")==0){
                ShowToolbarEdit();
            }
            else{
                if(action.compareTo("modify")==0){
                    ShowToolbarDelete();
                }
            }
            SetImage(budgetItem.budgetPicture);
            
            txtBudgetDescription.setText(budgetItem.budgetDescription);
            txtBudgetTotal.setText(StringUtils.IntToMoneyString(budgetItem.budgetTotal));
            txtBudgetUnpaid.setText(StringUtils.IntToMoneyString(budgetItem.budgetUnpaid));
            txtBudgetPaid.setText(StringUtils.IntToMoneyString(budgetItem.budgetPaid));
            swActive.setChecked(budgetItem.active);
            lblActive.setText(R.string.lblInactive);
            if(budgetItem.active)
                lblActive.setText(R.string.lblActive);

            afterShow();
        }
        catch (Exception e)
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
            return (budgetItem.infoId);
        }
        catch (Exception e)
        {
            ShowError("getInfoId", e.getMessage());
        }
        return (0);
    }

    @Override
    public void setNoteId(int pNoteId)
    {
        try
        {
            budgetItem.noteId = pNoteId;
            try(DatabaseAccess da = databaseAccess())
            {
                da.updateBudgetItem(budgetItem);
            }
        }
        catch (Exception e)
        {
            ShowError("setNoteId", e.getMessage());
        }
        
    }
    
    @Override
    public int getNoteId()
    {
        try
        {
            return (budgetItem.noteId);
        }
        catch (Exception e)
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
            budgetItem.infoId = pInfoId;
            try(DatabaseAccess da = databaseAccess())
            {
                da.updateBudgetItem(budgetItem);
            }
        }
        catch (Exception e)
        {
            ShowError("setInfoId", e.getMessage());
        }
        
    }
    
    public void editBudget()
    {
        try
        {
            Intent intent = new Intent(getApplicationContext(), BudgetDetailsEdit.class);
            intent.putExtra("ACTION", "modify");
            intent.putExtra("HOLIDAYID", holidayId);
            intent.putExtra("BUDGETID", budgetId);
            intent.putExtra("TITLE", title);
            intent.putExtra("SUBTITLE", subTitle);
            startActivity(intent);
        }
        catch (Exception e)
        {
            ShowError("editBudget", e.getMessage());
        }
    }
    
    public void deleteBudget()
    {
        try
        {
            try(DatabaseAccess da = databaseAccess())
            {
                if (!da.deleteBudgetItem(budgetItem))
                    return;
            }
            finish();
        }
        catch (Exception e)
        {
            ShowError("deleteBudget", e.getMessage());
        }
    }
    //endregion

    @Override
    protected void onResume()
    {
        super.onResume();
        try
        {
            try(DatabaseAccess da = databaseAccess())
            {
                if(!da.getBudgetItem(holidayId, budgetId, budgetItem)) {
                    finish();
                }
                if(action.compareTo("add")!=0)
                    if(budgetItem!=null)
                        if(budgetItem.budgetId==0)
                            finish();
            }
        }
        catch(Exception e)
        {
            ShowError("onResume", e.getMessage());
        }

    }


}

