/*
Lets you view a single holiday item and shows a select of icons (days, budget etc)
 */

package com.example.des.hp.Holiday;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.des.hp.Budget.BudgetDetailsList;
import com.example.des.hp.Contact.ContactDetailsList;
import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.Notes.NoteItem;
import com.example.des.hp.Notes.NoteView;
import com.example.des.hp.TipGroup.*;
import com.example.des.hp.Attraction.*;
import com.example.des.hp.Day.DayDetailsList;
import com.example.des.hp.ExtraFiles.*;
import com.example.des.hp.R;
import com.example.des.hp.Tasks.TaskDetailsList;
import com.example.des.hp.myutils.*;
import com.example.des.hp.thirdpartyutils.BadgeView;

public class HolidayDetailsView extends BaseActivity
{

    public DatabaseAccess databaseAccess;
    private ImageView imageView;
    private TextView txtStartDate;
    public int holidayId;
    public HolidayItem holidayItem;
    public LinearLayout grpStartDate;
    public ImageButton btnShowItinerary;
    public ImageButton btnShowMaps;
    public ImageButton btnShowTasks;
    public ImageButton btnShowBudget;
    public ImageButton btnShowTips;
    public ImageButton btnShowAttractions;
    public ImageButton btnShowContacts;
    public ImageButton btnShowInfo;
    public ImageButton btnShowNotes;
    public TextView itineraryBadge;
    public TextView mapBadge;
    public TextView taskBadge;
    public TextView budgetBadge;
    public TextView tipsBadge;
    public TextView attractionsBadge;
    public TextView contactsBadge;
    public TextView btnShowInfoBadge;
    private ImageUtils imageUtils;
    public MyColor myColor;

    //region R U Sure members
    public DialogWithYesNoFragment rusureDialogFragment;
    public String rusureDialogTag;
    public View.OnClickListener rusureOnYesClick;
    public View.OnClickListener rusureOnNoClick;
    //endregion

    // region R U Really Sure members
    public DialogWithYesNoFragment reallysureDialogFragment;
    public String reallysureDialogTag;
    public View.OnClickListener reallysureOnYesClick;
    public View.OnClickListener reallysureOnNoClick;
    //endregion

    //region EditText Dialog
    public DialogWithEditTextFragment dialogWithEditTextFragment;
    public String dwetDialogTag;
    public View.OnClickListener dwetOnOkClick;
    //endregion

    public Context context;
    public MyMessages myMessages;

    public void clearImage(View view)
    {
        imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.imagemissing));
    }

    public void showDay(View view)
    {
        Intent intent = new Intent(getApplicationContext(), DayDetailsList.class);
        intent.putExtra("HOLIDAYID", holidayId);
        startActivity(intent);
    }

    // region R U Sure procedures
    public void rusureOnYesClickProc(View view)
    {
        rusureDialogFragment.dismiss();

        reallysureOnYesClick = new View.OnClickListener()
        {
            public void onClick(View view)
            {
                reallysureOnYesClickProc(view);
            }
        };

        reallysureOnNoClick = new View.OnClickListener()
        {
            public void onClick(View view)
            {
                reallysureOnNoClickProc(view);
            }
        };

    }

    public void rusureOnNoClickProc(View view)
    {
        myMessages.ShowMessageLong("user clicked no");
        // When button is clicked, call up to owning activity.
        rusureDialogFragment.dismiss();
    }


    public void dwetOnOkClickProc(View view)
    {
        //MyMessages.ShowMessageLong(context, "user clicked ok" + dialogWithEditTextFragment.editText.getText().toString());
        // When button is clicked, call up to owning activity.
        dialogWithEditTextFragment.dismiss();
    }

    public void showDialog(View view)
    {
        rusureOnYesClick = new View.OnClickListener()
        {
            public void onClick(View view)
            {
                rusureOnYesClickProc(view);
            }
        };

        rusureOnNoClick = new View.OnClickListener()
        {
            public void onClick(View view)
            {
                rusureOnNoClickProc(view);
            }
        };

        dwetOnOkClick = new View.OnClickListener()
        {
            public void onClick(View view)
            {
                dwetOnOkClickProc(view);
            }
        };

        // Create and show the dialog.
        dialogWithEditTextFragment =
            DialogWithEditTextFragment.newInstance
            (
                getFragmentManager(),
                rusureDialogTag,
                "File Already Exists",
                "Rename it?",
                R.drawable.airplane,
                "hi there",
                dwetOnOkClick,
                this,
                    false
            );

        dialogWithEditTextFragment.showIt();

    }
    //endregion




    // region R U Really Sure procedures
    public void reallysureOnNoClickProc(View view)
    {
        myMessages.ShowMessageLong("ah ok");
        // When button is clicked, call up to owning activity.
        reallysureDialogFragment.dismiss();
    }

    public void reallysureOnYesClickProc(View view)
    {
        myMessages.ShowMessageLong("ah righty ho");
        // When button is clicked, call up to owning activity.
        reallysureDialogFragment.dismiss();
    }
    // endregion

    public void showTasks(View view)
    {
        Intent intent2 = new Intent(getApplicationContext(), TaskDetailsList.class);
        intent2.putExtra("HOLIDAYID", holidayItem.holidayId);
        intent2.putExtra("TITLE", holidayItem.holidayName);
        intent2.putExtra("SUBTITLE", "Tasks");
        startActivity(intent2);
    }

    public void showBudget(View view)
    {
        Intent intent2 = new Intent(getApplicationContext(), BudgetDetailsList.class);
        intent2.putExtra("HOLIDAYID", holidayItem.holidayId);
        intent2.putExtra("TITLE", holidayItem.holidayName);
        intent2.putExtra("SUBTITLE", "Budget");
        startActivity(intent2);
    }

    public void showContacts(View view)
    {
        Intent intent2 = new Intent(getApplicationContext(), ContactDetailsList.class);
        intent2.putExtra("HOLIDAYID", holidayItem.holidayId);
        intent2.putExtra("TITLE", holidayItem.holidayName);
        intent2.putExtra("SUBTITLE", "Contacts");
        startActivity(intent2);
    }

    public void showTipGroups(View view)
    {
        Intent intent2 = new Intent(getApplicationContext(), TipGroupDetailsList.class);
        intent2.putExtra("HOLIDAYID", holidayItem.holidayId);
        intent2.putExtra("TITLE", holidayItem.holidayName);
        intent2.putExtra("SUBTITLE", "Tips");
        startActivity(intent2);
    }

    public void showAttractions(View view)
    {
        Intent intent2 = new Intent(getApplicationContext(), AttractionDetailsList.class);
        intent2.putExtra("HOLIDAYID", holidayItem.holidayId);
        intent2.putExtra("TITLE", holidayItem.holidayName);
        intent2.putExtra("SUBTITLE", "Attractions");
        startActivity(intent2);
    }

    public void showMaps(View view)
    {
        Intent intent2 = new Intent(getApplicationContext(), ExtraFilesDetailsList.class);
        if(holidayItem.mapFileGroupId==0)
        {
            MyInt myInt = new MyInt();
            if(!databaseAccess.getNextFileGroupId(myInt))
                return;
            holidayItem.mapFileGroupId = myInt.Value;
            if(!databaseAccess.updateHolidayItem(holidayItem))
                return;
        }
        intent2.putExtra("FILEGROUPID", holidayItem.mapFileGroupId);
        intent2.putExtra("TITLE", holidayItem.holidayName);
        intent2.putExtra("SUBTITLE", "Maps");
        startActivity(intent2);
    }

    public void showNotes(View view)
    {
        Intent intent2 = new Intent(getApplicationContext(), NoteView.class);
        if(holidayItem.noteId==0)
        {
            MyInt myInt = new MyInt();
            if(!databaseAccess.getNextNoteId(holidayId, myInt))
                return;
            holidayItem.noteId = myInt.Value;
            if(!databaseAccess.updateHolidayItem(holidayItem))
                return;
        }
        intent2.putExtra("ACTION", "view");
        intent2.putExtra("HOLIDAYID", holidayItem.holidayId);
        intent2.putExtra("NOTEID", holidayItem.noteId);
        intent2.putExtra("TITLE", holidayItem.holidayName);
        intent2.putExtra("SUBTITLE", "Notes");
        startActivity(intent2);
    }

    public void showForm()
    {
        try {
            clearImage(null);

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String action = extras.getString("ACTION");
                if (action != null && action.equals("view")) {
                    MyInt myInt = new MyInt();

                    holidayId = extras.getInt("HOLIDAYID");
                    holidayItem = new HolidayItem();
                    if (!databaseAccess.getHolidayItem(holidayId, holidayItem))
                        return;

                    if (!databaseAccess.getDayCount(holidayId, myInt))
                        return;
                    int dayCount = myInt.Value;
                    itineraryBadge.setText("Days (" + Integer.toString(dayCount) + ")");

                    if (!databaseAccess.getExtraFilesCount(holidayItem.mapFileGroupId, myInt))
                        return;
                    int mapCount = myInt.Value;
                    mapBadge.setText("Maps (" + Integer.toString(mapCount) +")");

                    if (!databaseAccess.getTaskCount(holidayItem.holidayId, myInt))
                        return;
                    int taskCount = myInt.Value;
                    taskBadge.setText("Tasks (" + Integer.toString(taskCount) + ")");

                    if (!databaseAccess.getBudgetCount(holidayItem.holidayId, myInt))
                        return;
                    int budgetCount = myInt.Value;
                    budgetBadge.setText("Budget (" + Integer.toString(budgetCount) + ")");

                    if (!databaseAccess.getTipsCount(holidayItem.holidayId, myInt))
                        return;
                    int tipsCount = myInt.Value;
                    tipsBadge.setText("Tips (" + Integer.toString(tipsCount) + ")");

                    if (!databaseAccess.getAttractionsCount(holidayItem.holidayId, myInt))
                        return;
                    int attractionsCount = myInt.Value;
                    attractionsBadge.setText("Attractions (" + Integer.toString(attractionsCount) + ")");

                    if (!databaseAccess.getContactCount(holidayItem.holidayId, myInt))
                        return;
                    int contactCount = myInt.Value;
                    contactsBadge.setText("Contacts (" + Integer.toString(contactCount) + ")");

                    if (holidayItem.holidayPicture.length() > 0)
                        if (imageUtils.getPageHeaderImage(this, holidayItem.holidayPicture, imageView) == false)
                            return;

                    if (!holidayItem.dateKnown) {
                        grpStartDate.setVisibility(View.INVISIBLE);
                    } else {
                        grpStartDate.setVisibility(View.VISIBLE);
                        txtStartDate.setText(holidayItem.startDateStr);
                    }

                    setTitle(holidayItem.holidayName);

                    MyInt lFileCount = new MyInt();
                    lFileCount.Value = 0;
                    if (holidayItem.infoId > 0) {
                        if (!databaseAccess.getExtraFilesCount(holidayItem.infoId, lFileCount))
                            return;
                    }
                    btnShowInfoBadge.setText("Info (" + Integer.toString(lFileCount.Value) +")");

                    if (lFileCount.Value == 0) {
                        if (myColor.SetImageButtonTint(btnShowInfo, R.color.colorDisabled) == false)
                            return;
                    } else {
                        if (myColor.SetImageButtonTint(btnShowInfo, R.color.colorEnabled) == false)
                            return;
                    }
                    NoteItem noteItem = new NoteItem();
                    if(!databaseAccess.getNoteItem(holidayItem.holidayId, holidayItem.noteId, noteItem))
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
                        "Error in HolidayDetailsView::" + argFunction,
                        argMessage
                );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holiday_details_view);
        imageUtils = new ImageUtils(this);
        myColor = new MyColor(this);
        context = this;
        myMessages = new MyMessages(this);
        databaseAccess = new DatabaseAccess(this);

        rusureDialogTag = getResources().getString(R.string.rusureDialogTag);
        dwetDialogTag = getResources().getString(R.string.dwetDialogTag);
         
        imageView = (ImageView)findViewById(R.id.imageView);
        txtStartDate = (TextView)findViewById(R.id.txtStartDate);
        grpStartDate = (LinearLayout)findViewById(R.id.grpStartDate);
        btnShowItinerary = (ImageButton)findViewById(R.id.btnShowItinerary);
        btnShowMaps = (ImageButton)findViewById(R.id.btnShowMaps);
        btnShowTasks = (ImageButton)findViewById(R.id.btnShowTasks);
        btnShowBudget = (ImageButton)findViewById(R.id.btnShowBudget);
        btnShowTips = (ImageButton)findViewById(R.id.btnShowTips);
        btnShowAttractions = (ImageButton)findViewById(R.id.btnShowAttractions);
        btnShowContacts = (ImageButton)findViewById(R.id.btnShowContacts);
        btnShowInfo=(ImageButton) findViewById(R.id.btnShowInfo);
        btnShowNotes=(ImageButton) findViewById(R.id.btnShowNotes);

        itineraryBadge = (TextView) findViewById(R.id.txtItineraryBadge);
        mapBadge = (TextView) findViewById(R.id.txtMapBadge);
        taskBadge = (TextView) findViewById(R.id.txtTaskBadge);
        budgetBadge = (TextView) findViewById(R.id.txtBudgetBadge);
        contactsBadge = (TextView) findViewById(R.id.txtContactBadge);
        tipsBadge = (TextView) findViewById(R.id.txtTipsBadge);
        attractionsBadge = (TextView) findViewById(R.id.txtAttractionBadge);
        btnShowInfoBadge = (TextView) findViewById(R.id.txtInfoBadge);

        try
        {
            showForm();
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
    }

    public void showInfo(View view)
    {
        Intent intent2 = new Intent(getApplicationContext(), ExtraFilesDetailsList.class);
        if(holidayItem.infoId==0)
        {
            MyInt myInt = new MyInt();
            if(!databaseAccess.getNextFileGroupId(myInt))
                return;
            holidayItem.infoId = myInt.Value;
            if(!databaseAccess.updateHolidayItem(holidayItem))
                return;
        }
        intent2.putExtra("FILEGROUPID", holidayItem.infoId);
        intent2.putExtra("TITLE", holidayItem.holidayName);
        intent2.putExtra("SUBTITLE", "Info");
        startActivity(intent2);
    }


    public void editHoliday()
    {
        Intent intent = new Intent(getApplicationContext(), HolidayDetailsEdit.class);
        intent.putExtra("ACTION", "modify");
        intent.putExtra("HOLIDAYID", holidayId);
        startActivity(intent);
    }

    public void deleteHoliday()
    {
        if(!databaseAccess.deleteHolidayItem(holidayItem))
            return;
        finish();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.holidaydetailsformmenu, menu);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_edit_holiday:
                editHoliday();
                return true;
            case R.id.action_delete_holiday:
                deleteHoliday();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
