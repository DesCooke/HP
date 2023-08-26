package com.example.des.hp.Budget;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.R;
import com.example.des.hp.myutils.DialogBudgetOptionFragment;
import com.example.des.hp.myutils.DialogWithEditTextFragment;
import com.example.des.hp.myutils.DialogWithYesNoFragment;
import com.example.des.hp.myutils.MyInt;
import com.example.des.hp.myutils.StringUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

public class BudgetOptionList extends BaseActivity
{

    //region Member Variables
    public ArrayList<BudgetOptionItem> budgetOptionList;
    public BudgetOptionAdapter budgetOptionAdapter;
    public DialogBudgetOptionFragment dialogBudgetOptionFragment;
    public BudgetOptionItem currentBudgetOptionItem;
    public BudgetItem currentBudget;
    public Button btnNone;
    public FloatingActionButton btnAdd;
    //endregion

    //region Constructors/Destructors
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {

            layoutName = "activity_budgetoption_list";
            setContentView(R.layout.activity_budgetoption_list);

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
/*            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.budgetoption_list_add, menu);
 */       }
        catch (Exception e)
        {
            ShowError("onCreateOptionsMenu", e.getMessage());
        }

        return true;
    }
    //endregion

    //region Form Functions
    public void showBudgetAdd(View view)
    {
        try
        {
/*            Intent intent = new Intent(getApplicationContext(), BudgetDetailsEdit.class);
            intent.putExtra("ACTION", "add");
            intent.putExtra("HOLIDAYID", holidayId);
            startActivity(intent);*/
        }
        catch (Exception e)
        {
            ShowError("showBudgetAdd", e.getMessage());
        }
    }

    public void showForm()
    {
        super.showForm();
        try
        {
            allowCellMove = true;

            budgetOptionList = new ArrayList<>();
            if (!databaseAccess().getBudgetOptionList(holidayId, budgetId, budgetOptionList))
                return;

            currentBudget = new BudgetItem();
            if(!databaseAccess().getBudgetItem(holidayId, budgetId, currentBudget))
                return;

            btnNone = (Button)findViewById(R.id.btnNone);
            btnNone.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    currentBudget.optionSequenceNo = 0;
                    currentBudget.RemoveOption();
                    currentBudget.CalculateUnPaid();
                    databaseAccess().updateBudgetItem(currentBudget);
                    finish();
                }
            });

            btnAdd = (FloatingActionButton)findViewById(R.id.btnAdd);
            btnAdd.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    try
                    {
                        View.OnClickListener dwetOnOkClick = new View.OnClickListener()
                        {
                            public void onClick(View view)
                            {
                                BudgetOptionAdd();
                            }
                        };

                        currentBudgetOptionItem = new BudgetOptionItem();

                        dialogBudgetOptionFragment =

                        DialogBudgetOptionFragment.newInstance(getFragmentManager(),
                        "hihi2",            // unique name for this dialog type
                        "Budget Option Add",    // form caption
                        currentBudget.budgetDescription,
                        "",
                        0,
                        dwetOnOkClick,
                        BudgetOptionList.this,
                        false
                        );

                        dialogBudgetOptionFragment.showIt();
                    }
                    catch (Exception e)
                    {
                        ShowError("pickBudgetDescription", e.getMessage());
                    }
                }
            });

            budgetOptionAdapter = new BudgetOptionAdapter(this, budgetOptionList);

            CreateRecyclerView(R.id.budgetOptionListView, budgetOptionAdapter);

            budgetOptionAdapter.setOnEdit(new BudgetOptionAdapter.OnItemClickListener()
            {
                @Override
                public void onItemClick(View view, BudgetOptionItem obj)
                {
                    try
                    {
                        View.OnClickListener dwetOnOkClick = new View.OnClickListener()
                        {
                            public void onClick(View view)
                            {
                                BudgetOptionSet();
                            }
                        };

                        currentBudgetOptionItem = obj;

                        dialogBudgetOptionFragment =

                        DialogBudgetOptionFragment.newInstance(getFragmentManager(),
                        "hihi2",            // unique name for this dialog type
                        "Budget Option",    // form caption
                        currentBudget.budgetDescription,
                        obj.optionDescription,
                        obj.optionTotal,
                        dwetOnOkClick,
                        BudgetOptionList.this,
                        false
                        );

                        dialogBudgetOptionFragment.showIt();
                    }
                    catch (Exception e)
                    {
                        ShowError("pickBudgetDescription", e.getMessage());
                    }
                }
            });

            budgetOptionAdapter.setOnDelete(new BudgetOptionAdapter.OnItemClickListener()
            {
                @Override
                public void onItemClick(View view, BudgetOptionItem obj)
                {
                    try
                    {
                        dialogWithYesNoFragment = DialogWithYesNoFragment.newInstance(getFragmentManager(),     // for the transaction bit
                        "OKTODELETEOPTION",        // unique name for this dialog type
                        "Ok to Delete Budget Option",            // unique name for this dialog type
                        "Are you sure?", // form message
                        R.drawable.budget,
                        new View.OnClickListener()
                        {
                            public void onClick(View view)
                            {
                                databaseAccess().deleteBudgetOptionItem(obj);
                                dialogWithYesNoFragment.dismiss();
                                showForm();
                            }
                        }, new View.OnClickListener()
                        {
                            public void onClick(View view)
                            {
                                dialogWithYesNoFragment.dismiss();
                            }
                        }, BudgetOptionList.this
                        );

                        dialogWithYesNoFragment.showIt();
                    }
                    catch (Exception e)
                    {
                        ShowError("pickBudgetDescription", e.getMessage());
                    }
                }
            });

            budgetOptionAdapter.setOnRecordSelect(new BudgetOptionAdapter.OnItemClickListener()
            {
                @Override
                public void onItemClick(View view, BudgetOptionItem obj)
                {
                    try
                    {
                        currentBudget.optionSequenceNo = obj.rowId;
                        currentBudget.budgetTotal = obj.optionTotal;
                        currentBudget.AddOption(obj.optionDescription);
                        currentBudget.CalculateUnPaid();
                        databaseAccess().updateBudgetItem(currentBudget);
                        finish();
                    }
                    catch (Exception e)
                    {
                        ShowError("onRecordSelect", e.getMessage());
                    }
                }
            });
            afterShow();
        }
        catch (Exception e)

        {
            ShowError("showForm", e.getMessage());
        }

    }

    public void BudgetOptionSet()
    {
        try
        {
            currentBudgetOptionItem.optionDescription = dialogBudgetOptionFragment.tieOptionDescription.getText().toString();
            currentBudgetOptionItem.optionTotal = Integer.valueOf(dialogBudgetOptionFragment.tieOptionTotal.getText().toString());
            databaseAccess().updateBudgetOptionItem(currentBudgetOptionItem);
            dialogBudgetOptionFragment.dismiss();
            showForm();
        }
        catch (Exception e)
        {
            ShowError("BudgetDescriptionPicked", e.getMessage());
        }
    }
    public void BudgetOptionAdd()
    {
        try
        {
            MyInt ret=new MyInt();
            databaseAccess().getNextBudgetOptionSequenceNo(holidayId, budgetId, ret);
            currentBudgetOptionItem.sequenceNo= ret.Value;
            currentBudgetOptionItem.holidayId = holidayId;
            currentBudgetOptionItem.budgetId = budgetId;
            currentBudgetOptionItem.optionDescription = dialogBudgetOptionFragment.tieOptionDescription.getText().toString();
            currentBudgetOptionItem.optionTotal = Integer.valueOf(dialogBudgetOptionFragment.tieOptionTotal.getText().toString());
            databaseAccess().addBudgetOptionItem(currentBudgetOptionItem);
            dialogBudgetOptionFragment.dismiss();
            showForm();
        }
        catch (Exception e)
        {
            ShowError("BudgetDescriptionPicked", e.getMessage());
        }
    }

    @Override
    public void SwapItems(int from, int to)
    {
        try
        {
            Collections.swap(budgetOptionAdapter.data, from, to);
        }
        catch (Exception e)
        {
            ShowError("SwapItems", e.getMessage());
        }
    }

    @Override
    public void OnItemMove(int from, int to)
    {
        try
        {
            budgetOptionAdapter.onItemMove();
        }
        catch (Exception e)
        {
            ShowError("OnItemMove", e.getMessage());
        }
    }

    @Override
    public void NotifyItemMoved(int from, int to)
    {
        try
        {
            budgetOptionAdapter.notifyItemMoved(from, to);
        }
        catch (Exception e)
        {
            ShowError("NotifyItemMoved", e.getMessage());
        }
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
                case R.id.action_add_budget:
                    showBudgetAdd(null);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
        catch (Exception e)
        {
            ShowError("onOptionsItemSelected", e.getMessage());
        }
        return true;
    }
    //endregion

}

