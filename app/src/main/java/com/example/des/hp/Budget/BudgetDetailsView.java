package com.example.des.hp.Budget;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.ExtraFiles.ExtraFilesDetailsList;
import com.example.des.hp.R;
import com.example.des.hp.myutils.*;
import com.example.des.hp.thirdpartyutils.BadgeView;
import com.example.des.hp.Notes.NoteItem;
import com.example.des.hp.Notes.NoteView;

public class BudgetDetailsView extends AppCompatActivity {

    public DatabaseAccess databaseAccess;
    private ImageView imageView;
    public int holidayId;
    public int budgetId;
    public BudgetItem budgetItem;
    private ImageUtils imageUtils;
    public TextView txtBudgetDescription;
    public ActionBar actionBar;
    public TextView txtBudgetTotal;
    public TextView txtBudgetPaid;
    public TextView txtBudgetUnpaid;
    public TextView txtBudgetNotes;
    public MyMessages myMessages;
    public ImageButton btnShowInfo;
    public BadgeView btnShowInfoBadge;
    public MyColor myColor;
    public ImageButton btnShowNotes;

    public void clearImage(View view)
    {
        imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.imagemissing));
    }

    public void showNotes(View view)
    {
        Intent intent2 = new Intent(getApplicationContext(), NoteView.class);
        if(budgetItem.noteId==0)
        {
            MyInt myInt = new MyInt();
            if(!databaseAccess.getNextNoteId(holidayId, myInt))
                return;
            budgetItem.noteId = myInt.Value;
            if(!databaseAccess.updateBudgetItem(budgetItem))
                return;
        }
        intent2.putExtra("ACTION", "view");
        intent2.putExtra("HOLIDAYID", budgetItem.holidayId);
        intent2.putExtra("NOTEID", budgetItem.noteId);
        intent2.putExtra("TITLE", budgetItem.budgetDescription);
        intent2.putExtra("SUBTITLE", "Notes");
        startActivity(intent2);
    }

    public void showForm()
    {
        try {
            databaseAccess = new DatabaseAccess(this);

            clearImage(null);

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String action = extras.getString("ACTION");
                if (action != null && action.equals("view")) {
                    holidayId = extras.getInt("HOLIDAYID");
                    budgetId = extras.getInt("BUDGETID");
                    budgetItem = new BudgetItem();
                    if (!databaseAccess.getBudgetItem(holidayId, budgetId, budgetItem))
                        return;

                    actionBar = getSupportActionBar();
                    if (actionBar != null) {
                        String title = extras.getString("TITLE");
                        String subtitle = extras.getString("SUBTITLE");
                        if (title != null && title.length() > 0) {
                            actionBar.setTitle(title);
                            actionBar.setSubtitle(subtitle);
                        } else {
                            actionBar.setTitle(budgetItem.budgetDescription);
                            actionBar.setSubtitle("");
                        }
                    }

                    if (!imageUtils.getPageHeaderImage(this, budgetItem.budgetPicture, imageView))
                        return;

                    txtBudgetDescription.setText(budgetItem.budgetDescription);
                    txtBudgetTotal.setText(StringUtils.IntToMoneyString(budgetItem.budgetTotal));
                    txtBudgetUnpaid.setText(StringUtils.IntToMoneyString(budgetItem.budgetUnpaid));
                    txtBudgetPaid.setText(StringUtils.IntToMoneyString(budgetItem.budgetPaid));

                    txtBudgetNotes.setText(budgetItem.budgetNotes);

                    MyInt lFileCount = new MyInt();
                    lFileCount.Value = 0;
                    if (budgetItem.infoId > 0) {
                        if (!databaseAccess.getExtraFilesCount(budgetItem.infoId, lFileCount))
                            return;
                    }
                    btnShowInfoBadge.setText(Integer.toString(lFileCount.Value));

                    if (lFileCount.Value == 0)
                    {
                        btnShowInfoBadge.setVisibility(View.INVISIBLE);
                        if (myColor.SetImageButtonTint(btnShowInfo, R.color.colorDisabled) == false)
                            return;
                    } else
                    {
                        btnShowInfoBadge.setVisibility(View.VISIBLE);
                        if (myColor.SetImageButtonTint(btnShowInfo, R.color.colorEnabled) == false)
                            return;
                    }
                    NoteItem noteItem = new NoteItem();
                    if(!databaseAccess.getNoteItem(budgetItem.holidayId, budgetItem.noteId, noteItem))
                        return;
                    if (noteItem.notes.length() == 0)
                    {
                        if (myColor.SetImageButtonTint(btnShowNotes, R.color.colorDisabled) == false)
                            return;
                    } else {
                        if (myColor.SetImageButtonTint(btnShowNotes, R.color.colorEnabled) == false)
                            return;
                    }
                }
            }
        }
        catch(Exception e)
        {
            ShowError("showForm", e.getMessage());
        }
    }

    private void ShowError(String argFunction, String argMessage)
    {
        myMessages.ShowError
                (
                        "Error in BudgetDetailsView::" + argFunction,
                        argMessage
                );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_budget_details_view);
        imageUtils = new ImageUtils(this);
        myMessages = new MyMessages(this);
        myColor = new MyColor(this);

        imageView = (ImageView)findViewById(R.id.imageViewSmall);
        txtBudgetDescription = (TextView) findViewById(R.id.txtBudgetDescription);
        txtBudgetPaid = (TextView) findViewById(R.id.txtBudgetPaid);
        txtBudgetUnpaid = (TextView) findViewById(R.id.txtBudgetUnpaid);
        txtBudgetTotal = (TextView) findViewById(R.id.txtBudgetTotal);
        txtBudgetNotes = (TextView) findViewById(R.id.txtBudgetNotes);
        btnShowInfo=(ImageButton) findViewById(R.id.btnShowInfo);
        btnShowNotes=(ImageButton) findViewById(R.id.btnShowNotes);

        btnShowInfoBadge = new BadgeView(this, btnShowInfo);
        btnShowInfoBadge.setText(Integer.toString(0));
        btnShowInfoBadge.show();

        try
        {
            showForm();
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
    }

    public void editBudget()
    {
        Intent intent = new Intent(getApplicationContext(), BudgetDetailsEdit.class);
        intent.putExtra("ACTION", "modify");
        intent.putExtra("HOLIDAYID", holidayId);
        intent.putExtra("BUDGETID", budgetId);
        intent.putExtra("TITLE", actionBar.getTitle());
        intent.putExtra("SUBTITLE", actionBar.getSubtitle());
        startActivity(intent);
    }

    public void deleteBudget()
    {
        if(!databaseAccess.deleteBudgetItem(budgetItem))
            return;
        finish();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.budgetdetailsformmenu, menu);
        return true;
    }

    @Override
    protected void onResume(){
        super.onResume();
        try
        {
            showForm();
        }
        catch(Exception e)
        {
            ShowError("onResume", e.getMessage());
        }

    }

    public void showInfo(View view)
    {
        Intent intent2 = new Intent(getApplicationContext(), ExtraFilesDetailsList.class);
        if(budgetItem.infoId==0)
        {
            MyInt myInt = new MyInt();
            if(!databaseAccess.getNextFileGroupId(myInt))
                return;
            budgetItem.infoId = myInt.Value;
            if(!databaseAccess.updateBudgetItem(budgetItem))
                return;
        }
        intent2.putExtra("FILEGROUPID", budgetItem.infoId);
        intent2.putExtra("TITLE", budgetItem.budgetDescription);
        intent2.putExtra("SUBTITLE", "Info");
        startActivity(intent2);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
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
}
