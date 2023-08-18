package com.example.des.hp.Budget;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.des.hp.Notes.NoteItem;
import com.example.des.hp.myutils.*;
import com.example.des.hp.R;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
import static com.example.des.hp.myutils.MyMessages.myMessages;

public class BudgetDetailsEdit extends BudgetDetailsView implements View.OnClickListener
{
    
    //region Member variables
    public View.OnClickListener dwetOnOkClick;
    public DialogWithEditTextFragment dialogWithEditTextFragment;
    //endregion
    
    //region Constructors/Destructors
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        try
        {
            btnClear.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.VISIBLE);
            
            if (action != null && action.equals("add"))
            {
                grpMenuFile.setVisibility(View.GONE);
                txtBudgetDescription.setText(getString(R.string.schedule_unknown));
                txtBudgetTotal.setText(getString(R.string.caption_zero));
            }
            grpBudgetDescription.setOnClickListener(this);
            grpBudgetTotal.setOnClickListener(this);
            grpBudgetPaid.setOnClickListener(this);
            grpBudgetUnpaid.setOnClickListener(this);
            imageView.setOnClickListener(this);

            swUseOption.setVisibility(View.VISIBLE);

            grpBudgetDescription1.setOnClickListener(this);
            grpBudgetDescription2.setOnClickListener(this);
            grpBudgetDescription3.setOnClickListener(this);
            grpBudgetDescription4.setOnClickListener(this);
            grpBudgetDescription5.setOnClickListener(this);
            grpBudgetTotal1.setOnClickListener(this);
            grpBudgetTotal2.setOnClickListener(this);
            grpBudgetTotal3.setOnClickListener(this);
            grpBudgetTotal4.setOnClickListener(this);
            grpBudgetTotal5.setOnClickListener(this);
            chkOption1.setOnClickListener(this);
            chkOption2.setOnClickListener(this);
            chkOption3.setOnClickListener(this);
            chkOption4.setOnClickListener(this);
            chkOption5.setOnClickListener(this);

            swUseOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(swUseOption.isChecked())
                    {
                        svOptions.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        svOptions.setVisibility(View.GONE);
                    }
                }
            });


        }
        catch (Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
    }

    //region showForm
    public void showForm()
    {
        super.showForm();
        try
        {
            if(swUseOption.isChecked())
            {
                svOptions.setVisibility(View.VISIBLE);
            }
            else
            {
                svOptions.setVisibility(View.GONE);
            }
        }
        catch (Exception e)
        {
            ShowError("showForm", e.getMessage());
        }
    }
    //endregion

    public void removeOption()
    {
        String lString = txtBudgetDescription.getText().toString();
        int lIndex=lString.indexOf("(");
        if(lIndex==-1)
            return;
        txtBudgetDescription.setText(lString.substring(0,lIndex-1).trim());
    }

    public void addOption(String option)
    {
        removeOption();
        String lString=txtBudgetDescription.getText().toString().trim();
        txtBudgetDescription.setText(lString + " (" + option + ")");
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        /* disable the menu entirely */
        return false;
    }
    //endregion
    
    //region OnClick Events
    public void onClick(View view)
    {
        try
        {
            switch (view.getId())
            {
                case R.id.grpBudgetDescription:
                    pickBudgetDescription(view);
                    break;
                
                case R.id.grpBudgetTotal:
                    pickBudgetTotal(view);
                    break;
                
                case R.id.grpBudgetPaid:
                    pickBudgetPaid(view);
                    break;
                
                case R.id.grpBudgetUnpaid:
                    pickBudgetUnpaid(view);
                    break;
                
                case R.id.imageViewSmall:
                    pickImage(view);
                    break;
                case R.id.grpBudgetDescription1:
                    pickBudgetOptionDesc(txtBudgetDescription1);
                    break;
                case R.id.grpBudgetDescription2:
                    pickBudgetOptionDesc(txtBudgetDescription2);
                    break;
                case R.id.grpBudgetDescription3:
                    pickBudgetOptionDesc(txtBudgetDescription3);
                    break;
                case R.id.grpBudgetDescription4:
                    pickBudgetOptionDesc(txtBudgetDescription4);
                    break;
                case R.id.grpBudgetDescription5:
                    pickBudgetOptionDesc(txtBudgetDescription5);
                    break;
                case R.id.grpBudgetTotal1:
                    pickBudgetOptionTotal(txtBudgetTotal1);
                    break;
                case R.id.grpBudgetTotal2:
                    pickBudgetOptionTotal(txtBudgetTotal2);
                    break;
                case R.id.grpBudgetTotal3:
                    pickBudgetOptionTotal(txtBudgetTotal3);
                    break;
                case R.id.grpBudgetTotal4:
                    pickBudgetOptionTotal(txtBudgetTotal4);
                    break;
                case R.id.grpBudgetTotal5:
                    pickBudgetOptionTotal(txtBudgetTotal5);
                    break;
                case R.id.chkOption1:
                    pickOption(chkOption1, txtBudgetDescription1, txtBudgetTotal1);
                    break;
                case R.id.chkOption2:
                    pickOption(chkOption2, txtBudgetDescription2, txtBudgetTotal2);
                    break;
                case R.id.chkOption3:
                    pickOption(chkOption3, txtBudgetDescription3, txtBudgetTotal3);
                    break;
                case R.id.chkOption4:
                    pickOption(chkOption4, txtBudgetDescription4, txtBudgetTotal4);
                    break;
                case R.id.chkOption5:
                    pickOption(chkOption5, txtBudgetDescription5, txtBudgetTotal5);
                    break;
            }
        }
        catch (Exception e)
        {
            ShowError("onClick", e.getMessage());
        }
        
    }

    public void pickOption(CheckBox checkBox, TextView desc, TextView total)
    {
        if(!checkBox.isChecked())
            return;

        if(checkBox != chkOption1)
            chkOption1.setChecked(false);
        if(checkBox != chkOption2)
            chkOption2.setChecked(false);
        if(checkBox != chkOption3)
            chkOption3.setChecked(false);
        if(checkBox != chkOption4)
            chkOption4.setChecked(false);
        if(checkBox != chkOption5)
            chkOption5.setChecked(false);
        addOption(desc.getText().toString());
        txtBudgetTotal.setText(total.getText().toString());
        calculateUnpaid();
    }

    public void BudgetDescriptionPicked(TextView view)
    {
        try
        {
            view.setText(dialogWithEditTextFragment.getFinalText());
            
            dialogWithEditTextFragment.dismiss();
        }
        catch (Exception e)
        {
            ShowError("BudgetDescriptionPicked", e.getMessage());
        }
    }
    public void BudgetOptionDescriptionPicked(TextView view)
    {
        try
        {
            view.setText(dialogWithEditTextFragment.getFinalText());

            dialogWithEditTextFragment.dismiss();
        }
        catch (Exception e)
        {
            ShowError("BudgetDescriptionPicked", e.getMessage());
        }
    }

    public void BudgetObjectDescPicked(TextView textView)
    {
        try
        {
            textView.setText(dialogWithEditTextFragment.getFinalText());

            dialogWithEditTextFragment.dismiss();
        }
        catch (Exception e)
        {
            ShowError("BudgetObjectDescPicked", e.getMessage());
        }
    }

    // Create a YES onclick procedure
    public void pickBudgetDescription(View view)
    {
        try
        {
            dwetOnOkClick = new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    BudgetDescriptionPicked(txtBudgetDescription);
                }
            };
            
            
            dialogWithEditTextFragment = DialogWithEditTextFragment.newInstance(getFragmentManager(),     // for the transaction bit
                "hihi",            // unique name for this dialog type
                "Budget Description",    // form caption
                "Description",             // form message
                R.drawable.attachment, txtBudgetDescription.getText().toString(),                // initial text
                dwetOnOkClick, this, false
            );
            
            dialogWithEditTextFragment.showIt();
        }
        catch (Exception e)
        {
            ShowError("pickBudgetDescription", e.getMessage());
        }
    }

    public void pickBudgetOptionDesc(TextView textView)
    {
        try
        {
            dwetOnOkClick = new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    BudgetOptionDescriptionPicked(textView);
                }
            };


            dialogWithEditTextFragment = DialogWithEditTextFragment.newInstance(getFragmentManager(),     // for the transaction bit
                    "hihi",            // unique name for this dialog type
                    "Option Description",    // form caption
                    "Description",             // form message
                    R.drawable.attachment, textView.getText().toString(),                // initial text
                    dwetOnOkClick, this, false
            );

            dialogWithEditTextFragment.showIt();
        }
        catch (Exception e)
        {
            ShowError("pickBudgetOptionDesc", e.getMessage());
        }
    }

    public void calculateUnpaid()
    {
        try
        {
            int lTotal = Integer.parseInt(removePoundSign(txtBudgetTotal.getText().toString()));
            int lPaid = Integer.parseInt(removePoundSign(txtBudgetPaid.getText().toString()));
            int lUnpaid = lTotal - lPaid;
            txtBudgetUnpaid.setText(StringUtils.IntToMoneyString(lUnpaid));
        }
        catch (Exception e)
        {
            ShowError("calculateUnpaid", e.getMessage());
        }
    }
    
    public void calculatePaid()
    {
        try
        {
            int lTotal = Integer.parseInt(removePoundSign(txtBudgetTotal.getText().toString()));
            int lUnpaid = Integer.parseInt(removePoundSign(txtBudgetUnpaid.getText().toString()));
            int lPaid = lTotal - lUnpaid;
            txtBudgetPaid.setText(StringUtils.IntToMoneyString(lPaid));
        }
        catch (Exception e)
        {
            ShowError("calculatePaid", e.getMessage());
        }
    }
    
    public void BudgetTotalPicked(TextView view)
    {
        try
        {
            view.setText(StringUtils.StringToMoneyString(dialogWithEditTextFragment.getFinalText()));
            calculateUnpaid();
            dialogWithEditTextFragment.dismiss();
        }
        catch (Exception e)
        {
            ShowError("BudgetTotalPicked", e.getMessage());
        }
    }
    
    public String removePoundSign(String argString)
    {
        try
        {
            return (argString.replaceAll("£", ""));
        }
        catch (Exception e)
        {
            ShowError("removePoundSign", e.getMessage());
        }
        return (argString);
    }
    
    // Create a YES onclick procedure
    public void pickBudgetTotal(View view)
    {
        try
        {
            dwetOnOkClick = new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    BudgetTotalPicked(txtBudgetTotal);
                }
            };
            
            
            dialogWithEditTextFragment = DialogWithEditTextFragment.newInstance(getFragmentManager(),     // for the transaction bit
                "hihi",            // unique name for this dialog type
                "Budget Total",    // form caption
                "Total",             // form message
                R.drawable.attachment, removePoundSign(txtBudgetTotal.getText().toString()), dwetOnOkClick, this, true
            );
            
            dialogWithEditTextFragment.showIt();
        }
        catch (Exception e)
        {
            ShowError("pickBudgetTotal", e.getMessage());
        }
    }
    
    public void BudgetPaidPicked(View view)
    {
        try
        {
            txtBudgetPaid.setText(StringUtils.StringToMoneyString(dialogWithEditTextFragment.getFinalText()));
            calculateUnpaid();
            dialogWithEditTextFragment.dismiss();
        }
        catch (Exception e)
        {
            ShowError("BudgetPaidPicked", e.getMessage());
        }
    }

    public void pickBudgetOptionTotal(TextView textView)
    {
        try
        {
            dwetOnOkClick = new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    BudgetTotalPicked(textView);
                }
            };


            dialogWithEditTextFragment = DialogWithEditTextFragment.newInstance(getFragmentManager(),     // for the transaction bit
                    "hihi",            // unique name for this dialog type
                    "Budget Option",    // form caption
                    "Total",             // form message
                    R.drawable.attachment, removePoundSign(textView.getText().toString()), dwetOnOkClick, this, true
            );

            dialogWithEditTextFragment.showIt();
        }
        catch (Exception e)
        {
            ShowError("pickBudgetTotal", e.getMessage());
        }
    }

    // Create a YES onclick procedure
    public void pickBudgetPaid(View view)
    {
        try
        {
            dwetOnOkClick = new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    BudgetPaidPicked(view);
                }
            };
            
            
            dialogWithEditTextFragment = DialogWithEditTextFragment.newInstance(getFragmentManager(),     // for the transaction bit
                "hihi",            // unique name for this dialog type
                "Budget Paid",    // form caption
                "Paid",             // form message
                R.drawable.attachment, removePoundSign(txtBudgetPaid.getText().toString()), dwetOnOkClick, this, true
            );
            
            dialogWithEditTextFragment.showIt();
        }
        catch (Exception e)
        {
            ShowError("pickBudgetPaid", e.getMessage());
        }
    }
    
    public void BudgetUnpaidPicked(View view)
    {
        try
        {
            txtBudgetUnpaid.setText(StringUtils.StringToMoneyString(dialogWithEditTextFragment.getFinalText()));
            
            calculatePaid();
            
            dialogWithEditTextFragment.dismiss();
        }
        catch (Exception e)
        {
            ShowError("BudgetUnpaidPicked", e.getMessage());
        }
    }
    
    // Create a YES onclick procedure
    public void pickBudgetUnpaid(View view)
    {
        try
        {
            dwetOnOkClick = new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    BudgetUnpaidPicked(view);
                }
            };
            
            
            dialogWithEditTextFragment = DialogWithEditTextFragment.newInstance(getFragmentManager(),     // for the transaction bit
                "hihi",            // unique name for this dialog type
                "Budget Unpaid",    // form caption
                "Unpaid",             // form message
                R.drawable.attachment, removePoundSign(txtBudgetUnpaid.getText().toString()), dwetOnOkClick, this, true
            );
            
            dialogWithEditTextFragment.showIt();
        }
        catch (Exception e)
        {
            ShowError("pickBudgetUnpaid", e.getMessage());
        }
    }
    //endregion
    
    //region Saving
    public void saveSchedule(View view)
    {
        try
        {
            myMessages().ShowMessageShort("Saving " + txtBudgetDescription.getText().toString());
            
            MyInt myInt = new MyInt();
            
            budgetItem.budgetDescription = txtBudgetDescription.getText().toString();
            budgetItem.budgetTotal = Integer.parseInt(removePoundSign(txtBudgetTotal.getText().toString()));
            budgetItem.budgetPaid = Integer.parseInt(removePoundSign(txtBudgetPaid.getText().toString()));
            budgetItem.budgetUnpaid = Integer.parseInt(removePoundSign(txtBudgetUnpaid.getText().toString()));
            budgetItem.budgetNotes = "";
            
            budgetItem.budgetPicture = "";
            if (internalImageFilename.length() > 0)
                budgetItem.budgetPicture = internalImageFilename;
            budgetItem.pictureAssigned = imageSet;
            budgetItem.pictureChanged = imageChanged;
            budgetItem.fileBitmap = null;
            if (imageSet)
                budgetItem.fileBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

            budgetItem.useOption = swUseOption.isChecked();
            budgetItem.useOption1 = chkOption1.isChecked();
            budgetItem.useOption2 = chkOption2.isChecked();
            budgetItem.useOption3 = chkOption3.isChecked();
            budgetItem.useOption4 = chkOption4.isChecked();
            budgetItem.useOption5 = chkOption5.isChecked();

            budgetItem.option1Desc = txtBudgetDescription1.getText().toString();
            budgetItem.option2Desc = txtBudgetDescription2.getText().toString();
            budgetItem.option3Desc = txtBudgetDescription3.getText().toString();
            budgetItem.option4Desc = txtBudgetDescription4.getText().toString();
            budgetItem.option5Desc = txtBudgetDescription5.getText().toString();

            budgetItem.option1Budget = Integer.parseInt(removePoundSign(txtBudgetTotal1.getText().toString()));
            budgetItem.option2Budget = Integer.parseInt(removePoundSign(txtBudgetTotal2.getText().toString()));
            budgetItem.option3Budget = Integer.parseInt(removePoundSign(txtBudgetTotal3.getText().toString()));
            budgetItem.option4Budget = Integer.parseInt(removePoundSign(txtBudgetTotal4.getText().toString()));
            budgetItem.option5Budget = Integer.parseInt(removePoundSign(txtBudgetTotal5.getText().toString()));

            if (action.equals("add"))
            {
                budgetItem.holidayId = holidayId;
                if (!databaseAccess().getNextBudgetId(holidayId, myInt))
                    return;
                budgetItem.budgetId = myInt.Value;
                
                if (!databaseAccess().getNextBudgetSequenceNo(holidayId, myInt))
                    return;
                budgetItem.sequenceNo = myInt.Value;
                
                if (!databaseAccess().addBudgetItem(budgetItem))
                    return;
            }
            
            if (action.equals("modify"))
            {
                if (!databaseAccess().updateBudgetItem(budgetItem))
                    return;
            }
            
            finish();
        }
        catch (Exception e)
        {
            ShowError("saveSchedule", e.getMessage());
        }
    }
    //endregion
    
    
}
