package com.example.des.hp.Budget;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.graphics.BitmapFactory;
import android.widget.TextView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.myutils.*;
import com.example.des.hp.R;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
import static com.example.des.hp.myutils.MyMessages.myMessages;

public class BudgetDetailsEdit extends BaseActivity
{

    private final int SELECT_PHOTO = 1;
    private ImageView imageViewSmall;
    private String action;
    public int holidayId;
    public int budgetId;
    public TextView budgetDescription;
    public TextView budgetTotal;
    public TextView budgetPaid;
    public TextView budgetUnpaid;
    public ActionBar actionBar;
    public BudgetItem budgetItem;
    public CheckBox cbPicturePicked;
    private ImageUtils imageUtils;
    public View.OnClickListener dwetOnOkClick;
    public DialogWithEditTextFragment dialogWithEditTextFragment;
    public DialogWithMultiEditTextFragment dialogWithMultiEditTextFragment;
    public TextView txtBudgetNotes;
    
    public void pickImage(View view)
    {
        try
        {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, SELECT_PHOTO);
        }
        catch (Exception e)
        {
            ShowError("pickImage", e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        try
        {
            switch (requestCode)
            {
                case SELECT_PHOTO:
                    if (resultCode == RESULT_OK)
                    {
                        try
                        {
                            MyBitmap myBitmap = new MyBitmap();
                            Boolean lRetCode =
                                    imageUtils.ScaleBitmapFromUrl
                                            (
                                                    imageReturnedIntent.getData(),
                                                    getContentResolver(),
                                                    myBitmap
                                            );
                            if(!lRetCode)
                                return;

                            // assign new bitmap and set scale type
                            imageViewSmall.setImageBitmap(myBitmap.Value);

                            cbPicturePicked.setChecked(true);

                            budgetItem.pictureChanged = true;


                        } catch (Exception e)
                        {
                            ShowError("onActivityResult-selectphoto", e.getMessage());
                        }
                    }
                    break;

            }
        }
        catch (Exception e)
        {
            ShowError("onActivityResult", e.getMessage());
        }
    }

    public void clearImage(View view)
    {
        try
        {
            cbPicturePicked.setChecked(false);
            imageViewSmall.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.imagemissing));
        }
        catch (Exception e)
        {
            ShowError("clearImage", e.getMessage());
        }
    }

    public void btnClearImage(View view)
    {
        try
        {
            clearImage(view);
            budgetItem.pictureChanged = true;
            budgetItem.pictureAssigned = false;
        }
        catch (Exception e)
        {
            ShowError("btnClearImage", e.getMessage());
        }
    }

    public void saveTask(View view)
    {
        try
        {
            MyInt myInt = new MyInt();

            myMessages().ShowMessageShort("Saving " + budgetDescription.getText().toString());

            budgetItem.pictureAssigned = cbPicturePicked.isChecked();
            budgetItem.budgetDescription = budgetDescription.getText().toString();
            budgetItem.budgetTotal = Integer.parseInt(removePoundSign(budgetTotal.getText().toString()));
            budgetItem.budgetPaid = Integer.parseInt(removePoundSign(budgetPaid.getText().toString()));
            budgetItem.budgetUnpaid = Integer.parseInt(removePoundSign(budgetUnpaid.getText().toString()));
            budgetItem.fileBitmap = null;
            if (budgetItem.pictureAssigned)
                budgetItem.fileBitmap = ((BitmapDrawable) imageViewSmall.getDrawable()).getBitmap();

            budgetItem.budgetNotes = txtBudgetNotes.getText().toString();

            if (action.equals("add"))
            {
                budgetItem.holidayId = holidayId;
                if(!databaseAccess().getNextBudgetId(holidayId, myInt))
                    return;
                budgetItem.budgetId = myInt.Value;

                if(!databaseAccess().getNextBudgetSequenceNo(holidayId, myInt))
                    return;
                budgetItem.sequenceNo = myInt.Value;

                if(!databaseAccess().addBudgetItem(budgetItem))
                    return;
            }

            if (action.equals("modify"))
            {
                budgetItem.holidayId = holidayId;
                budgetItem.budgetId = budgetId;
                if(!databaseAccess().updateBudgetItem(budgetItem))
                    return;
            }

            finish();
        }
        catch (Exception e)
        {
            ShowError("saveTask", e.getMessage());
        }
    }


    public void BudgetDescriptionPicked(View view)
    {
        try
        {
            budgetDescription.setText(dialogWithEditTextFragment.getFinalText());

            dialogWithEditTextFragment.dismiss();
        }
        catch (Exception e)
        {
            ShowError("BudgetDescriptionPicked", e.getMessage());
        }
    }

    // Create a YES onclick procedure
    public void pickBudgetDescription(View view)
    {
        try
        {
            dwetOnOkClick = new View.OnClickListener() {
                public void onClick(View view) {
                    BudgetDescriptionPicked(view);
                }
            };


            dialogWithEditTextFragment =
                    DialogWithEditTextFragment.newInstance
                            (
                                    getFragmentManager(),     // for the transaction bit
                                    "hihi",            // unique name for this dialog type
                                    "Budget Description",    // form caption
                                    "Description",             // form message
                                    R.drawable.attachment,
                                    budgetDescription.getText().toString(),                // initial text
                                    dwetOnOkClick,
                                    this,
                                    false
                            );

            dialogWithEditTextFragment.showIt();
        }
        catch (Exception e)
        {
            ShowError("pickBudgetDescription", e.getMessage());
        }
    }

    public void calculateUnpaid()
    {
        try
        {
        int lTotal = Integer.parseInt(removePoundSign(budgetTotal.getText().toString()));
        int lPaid = Integer.parseInt(removePoundSign(budgetPaid.getText().toString()));
        int lUnpaid = lTotal-lPaid;
        budgetUnpaid.setText(StringUtils.IntToMoneyString(lUnpaid));
        }
        catch (Exception e)
        {
            ShowError("calculateUnpaid", e.getMessage());
        }
    }
    public void BudgetTotalPicked(View view)
    {
        try
        {
            budgetTotal.setText(StringUtils.StringToMoneyString(dialogWithEditTextFragment.getFinalText()));
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
        return(argString.replaceAll("£",""));
    }

    // Create a YES onclick procedure
    public void pickBudgetTotal(View view)
    {
        try {
            dwetOnOkClick = new View.OnClickListener() {
                public void onClick(View view) {
                    BudgetTotalPicked(view);
                }
            };


            dialogWithEditTextFragment =
                    DialogWithEditTextFragment.newInstance
                            (
                                    getFragmentManager(),     // for the transaction bit
                                    "hihi",            // unique name for this dialog type
                                    "Budget Total",    // form caption
                                    "Total",             // form message
                                    R.drawable.attachment,
                                    removePoundSign(budgetTotal.getText().toString()),
                                    dwetOnOkClick,
                                    this,
                                    true
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
            budgetPaid.setText(StringUtils.StringToMoneyString(dialogWithEditTextFragment.getFinalText()));
            calculateUnpaid();
            dialogWithEditTextFragment.dismiss();
        }
        catch (Exception e)
        {
            ShowError("BudgetPaidPicked", e.getMessage());
        }
    }

    // Create a YES onclick procedure
    public void pickBudgetPaid(View view)
    {
        try {
            dwetOnOkClick = new View.OnClickListener() {
                public void onClick(View view) {
                    BudgetPaidPicked(view);
                }
            };


            dialogWithEditTextFragment =
                    DialogWithEditTextFragment.newInstance
                            (
                                    getFragmentManager(),     // for the transaction bit
                                    "hihi",            // unique name for this dialog type
                                    "Budget Paid",    // form caption
                                    "Paid",             // form message
                                    R.drawable.attachment,
                                    removePoundSign(budgetPaid.getText().toString()),
                                    dwetOnOkClick,
                                    this,
                                    true
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
            budgetUnpaid.setText(StringUtils.StringToMoneyString(dialogWithEditTextFragment.getFinalText()));

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
        try {
            dwetOnOkClick = new View.OnClickListener() {
                public void onClick(View view) {
                    BudgetUnpaidPicked(view);
                }
            };


            dialogWithEditTextFragment =
                    DialogWithEditTextFragment.newInstance
                            (
                                    getFragmentManager(),     // for the transaction bit
                                    "hihi",            // unique name for this dialog type
                                    "Budget Unpaid",    // form caption
                                    "Unpaid",             // form message
                                    R.drawable.attachment,
                                    removePoundSign(budgetUnpaid.getText().toString()),
                                    dwetOnOkClick,
                                    this,
                                    true
                            );

            dialogWithEditTextFragment.showIt();
        }
        catch (Exception e)
        {
            ShowError("pickBudgetUnpaid", e.getMessage());
        }
    }

    public void BudgetNotesPicked(View view)
    {
        try {
            txtBudgetNotes.setText(dialogWithMultiEditTextFragment.getFinalText());

            dialogWithMultiEditTextFragment.dismiss();
        }
        catch (Exception e)
        {
            ShowError("BudgetNotesPicked", e.getMessage());
        }
    }

    // Create a YES onclick procedure
    public void pickBudgetNotes(View view)
    {
        try {
            dwetOnOkClick = new View.OnClickListener() {
                public void onClick(View view) {
                    BudgetNotesPicked(view);
                }
            };


            dialogWithMultiEditTextFragment =
                    DialogWithMultiEditTextFragment.newInstance
                            (
                                    getFragmentManager(),     // for the transaction bit
                                    "hjhj",            // unique name for this dialog type
                                    "Budget Notes",    // form caption
                                    "Budget",             // form message
                                    R.drawable.attachment,
                                    txtBudgetNotes.getText().toString(),                // initial text
                                    dwetOnOkClick,
                                    this
                            );


            dialogWithMultiEditTextFragment.showIt();
        }
        catch (Exception e)
        {
            ShowError("pickBudgetNotes", e.getMessage());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_budget_details_edit);

            actionBar = getSupportActionBar();
            imageUtils = new ImageUtils(this);

            cbPicturePicked = (CheckBox) findViewById(R.id.picturePicked);
            imageViewSmall = (ImageView) findViewById(R.id.imageViewSmall);
            budgetDescription = (TextView) findViewById(R.id.txtBudgetDescription);
            budgetTotal = (TextView) findViewById(R.id.txtBudgetTotal);
            budgetPaid = (TextView) findViewById(R.id.txtBudgetPaid);
            budgetUnpaid = (TextView) findViewById(R.id.txtBudgetUnpaid);
            txtBudgetNotes = (TextView) findViewById(R.id.txtBudgetNotes);

            clearImage(null);

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String title = extras.getString("TITLE");
                String subtitle = extras.getString("SUBTITLE");
                actionBar.setTitle(title);
                action = extras.getString("ACTION");
                if (action != null && action.equals("add"))
                {
                    budgetItem = new BudgetItem();
                    holidayId = extras.getInt("HOLIDAYID");
                    budgetDescription.setText("");
                    cbPicturePicked.setChecked(false);
                    actionBar.setSubtitle("Add a Budget");
                    txtBudgetNotes.setText("");
                }
                if (action != null && action.equals("modify"))
                {
                    holidayId = extras.getInt("HOLIDAYID");
                    budgetId = extras.getInt("BUDGETID");
                    budgetItem = new BudgetItem();
                    if(!databaseAccess().getBudgetItem(holidayId, budgetId, budgetItem))
                        return;

                    budgetDescription.setText(budgetItem.budgetDescription);

                    if (!imageUtils.getPageHeaderImage(this, budgetItem.budgetPicture, imageViewSmall))
                        return;

                    cbPicturePicked.setChecked(budgetItem.pictureAssigned);

                    actionBar.setSubtitle(subtitle);

                    budgetTotal.setText(StringUtils.IntToMoneyString(budgetItem.budgetTotal));
                    budgetPaid.setText(StringUtils.IntToMoneyString(budgetItem.budgetPaid));
                    budgetUnpaid.setText(StringUtils.IntToMoneyString(budgetItem.budgetUnpaid));
                    txtBudgetNotes.setText(String.valueOf(budgetItem.budgetNotes));
                }
            }

        } catch (Exception e) {
            ShowError("onCreate", e.getMessage());
        }
    }
}
