package com.example.des.hp.Budget;

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

import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.Notes.NoteItem;
import com.example.des.hp.R;
import com.example.des.hp.myutils.*;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

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
            
            imageView = (ImageView) findViewById(R.id.imageViewSmall);
            txtBudgetDescription = (TextView) findViewById(R.id.txtBudgetDescription);
            txtBudgetPaid = (TextView) findViewById(R.id.txtBudgetPaid);
            txtBudgetUnpaid = (TextView) findViewById(R.id.txtBudgetUnpaid);
            txtBudgetTotal = (TextView) findViewById(R.id.txtBudgetTotal);
            txtNotes = (TextView) findViewById(R.id.txtNotes);
            btnClear = (ImageButton) findViewById(R.id.btnClear);
            btnSave = (Button) findViewById(R.id.btnSave);
            grpMenuFile = (LinearLayout) findViewById(R.id.grpMenuFile);
            grpBudgetDescription = (LinearLayout) findViewById(R.id.grpBudgetDescription);
            grpBudgetTotal = (LinearLayout) findViewById(R.id.grpBudgetTotal);
            grpBudgetPaid = (LinearLayout) findViewById(R.id.grpBudgetPaid);
            grpBudgetUnpaid = (LinearLayout) findViewById(R.id.grpBudgetUnpaid);
            
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
    public boolean onOptionsItemSelected(MenuItem item)
    {
        try
        {
            switch (item.getItemId())
            {
                case R.id.action_delete_budget:
                    deleteBudget();
                    return true;
                case R.id.action_edit_budget:
                    editBudget();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
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
            budgetItem = new BudgetItem();
            if (!databaseAccess().getBudgetItem(holidayId, budgetId, budgetItem))
                return;

            if(budgetItem.noteId>0)
            {
                NoteItem noteItem = new NoteItem();
                if (!databaseAccess().getNoteItem(holidayId, budgetItem.noteId, noteItem))
                    return;
                txtNotes.setText(noteItem.notes);
            }

            if (title == null || (title.length() == 0))
            {
                SetTitles(budgetItem.budgetDescription, "");
            } else
            {
                SetTitles(title, subTitle);
            }
            
            SetImage(budgetItem.budgetPicture);
            
            txtBudgetDescription.setText(budgetItem.budgetDescription);
            txtBudgetTotal.setText(StringUtils.IntToMoneyString(budgetItem.budgetTotal));
            txtBudgetUnpaid.setText(StringUtils.IntToMoneyString(budgetItem.budgetUnpaid));
            txtBudgetPaid.setText(StringUtils.IntToMoneyString(budgetItem.budgetPaid));
            
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
    
    public void setNoteId(int pNoteId)
    {
        try
        {
            budgetItem.noteId = pNoteId;
            databaseAccess().updateBudgetItem(budgetItem);
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
            databaseAccess().updateBudgetItem(budgetItem);
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
            if (!databaseAccess().deleteBudgetItem(budgetItem))
                return;
            finish();
        }
        catch (Exception e)
        {
            ShowError("deleteBudget", e.getMessage());
        }
    }
    //endregion
}
