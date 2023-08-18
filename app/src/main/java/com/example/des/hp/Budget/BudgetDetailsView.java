package com.example.des.hp.Budget;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
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
    public Switch swUseOption;
    public ScrollView svOptions;
    public LinearLayout grpBudgetDescription1;
    public LinearLayout grpBudgetDescription2;
    public LinearLayout grpBudgetDescription3;
    public LinearLayout grpBudgetDescription4;
    public LinearLayout grpBudgetDescription5;
    public LinearLayout grpBudgetTotal1;
    public LinearLayout grpBudgetTotal2;
    public LinearLayout grpBudgetTotal3;
    public LinearLayout grpBudgetTotal4;
    public LinearLayout grpBudgetTotal5;
    public CheckBox chkOption1;
    public CheckBox chkOption2;
    public CheckBox chkOption3;
    public CheckBox chkOption4;
    public CheckBox chkOption5;
    public TextView txtBudgetDescription1;
    public TextView txtBudgetDescription2;
    public TextView txtBudgetDescription3;
    public TextView txtBudgetDescription4;
    public TextView txtBudgetDescription5;
    public TextView txtBudgetTotal1;
    public TextView txtBudgetTotal2;
    public TextView txtBudgetTotal3;
    public TextView txtBudgetTotal4;
    public TextView txtBudgetTotal5;
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
            swUseOption = (Switch)findViewById(R.id.swUseOption);
            svOptions = (ScrollView)findViewById(R.id.svOptions);
            grpBudgetDescription1 = (LinearLayout)findViewById(R.id.grpBudgetDescription1);
            grpBudgetDescription2 = (LinearLayout)findViewById(R.id.grpBudgetDescription2);
            grpBudgetDescription3 = (LinearLayout)findViewById(R.id.grpBudgetDescription3);
            grpBudgetDescription4 = (LinearLayout)findViewById(R.id.grpBudgetDescription4);
            grpBudgetDescription5 = (LinearLayout)findViewById(R.id.grpBudgetDescription5);
            grpBudgetTotal1 = (LinearLayout)findViewById(R.id.grpBudgetTotal1);
            grpBudgetTotal2 = (LinearLayout)findViewById(R.id.grpBudgetTotal2);
            grpBudgetTotal3 = (LinearLayout)findViewById(R.id.grpBudgetTotal3);
            grpBudgetTotal4 = (LinearLayout)findViewById(R.id.grpBudgetTotal4);
            grpBudgetTotal5 = (LinearLayout)findViewById(R.id.grpBudgetTotal5);
            chkOption1 = (CheckBox)findViewById(R.id.chkOption1);
            chkOption2 = (CheckBox)findViewById(R.id.chkOption2);
            chkOption3 = (CheckBox)findViewById(R.id.chkOption3);
            chkOption4 = (CheckBox)findViewById(R.id.chkOption4);
            chkOption5 = (CheckBox)findViewById(R.id.chkOption5);
            txtBudgetDescription1 = (TextView)findViewById(R.id.txtBudgetDescription1);
            txtBudgetDescription2 = (TextView)findViewById(R.id.txtBudgetDescription2);
            txtBudgetDescription3 = (TextView)findViewById(R.id.txtBudgetDescription3);
            txtBudgetDescription4 = (TextView)findViewById(R.id.txtBudgetDescription4);
            txtBudgetDescription5 = (TextView)findViewById(R.id.txtBudgetDescription5);
            txtBudgetTotal1 = (TextView)findViewById(R.id.txtBudgetTotal1);
            txtBudgetTotal2 = (TextView)findViewById(R.id.txtBudgetTotal2);
            txtBudgetTotal3 = (TextView)findViewById(R.id.txtBudgetTotal3);
            txtBudgetTotal4 = (TextView)findViewById(R.id.txtBudgetTotal4);
            txtBudgetTotal5 = (TextView)findViewById(R.id.txtBudgetTotal5);

            swUseOption.setVisibility(View.GONE);

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

            swUseOption.setChecked(budgetItem.useOption);

            chkOption1.setChecked(budgetItem.useOption1);
            chkOption2.setChecked(budgetItem.useOption2);
            chkOption3.setChecked(budgetItem.useOption3);
            chkOption4.setChecked(budgetItem.useOption4);
            chkOption5.setChecked(budgetItem.useOption5);

            txtBudgetDescription1.setText(budgetItem.option1Desc);
            txtBudgetDescription2.setText(budgetItem.option2Desc);
            txtBudgetDescription3.setText(budgetItem.option3Desc);
            txtBudgetDescription4.setText(budgetItem.option4Desc);
            txtBudgetDescription5.setText(budgetItem.option5Desc);

            txtBudgetTotal1.setText(StringUtils.IntToMoneyString(budgetItem.option1Budget));
            txtBudgetTotal2.setText(StringUtils.IntToMoneyString(budgetItem.option2Budget));
            txtBudgetTotal3.setText(StringUtils.IntToMoneyString(budgetItem.option3Budget));
            txtBudgetTotal4.setText(StringUtils.IntToMoneyString(budgetItem.option4Budget));
            txtBudgetTotal5.setText(StringUtils.IntToMoneyString(budgetItem.option5Budget));

            svOptions.setVisibility(View.GONE);

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
