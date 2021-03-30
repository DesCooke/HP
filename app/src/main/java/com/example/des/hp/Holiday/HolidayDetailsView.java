/*
Lets you view a single holiday item and shows a select of icons (days, budget etc)
 */

package com.example.des.hp.Holiday;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.des.hp.Budget.BudgetDetailsList;
import com.example.des.hp.Contact.ContactDetailsList;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.TipGroup.*;
import com.example.des.hp.Attraction.*;
import com.example.des.hp.Day.DayDetailsList;
import com.example.des.hp.ExtraFiles.*;
import com.example.des.hp.R;
import com.example.des.hp.Tasks.TaskDetailsList;
import com.example.des.hp.myutils.*;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

public class HolidayDetailsView extends BaseActivity
{

    //region Member variables
    private TextView txtStartDate;
    public HolidayItem holidayItem;
    public LinearLayout grpStartDate;
    public ImageButton btnShowItinerary;
    public ImageButton btnShowMaps;
    public ImageButton btnShowTasks;
    public ImageButton btnShowBudget;
    public ImageButton btnShowTips;
    public ImageButton btnShowAttractions;
    public ImageButton btnShowContacts;
    public TextView itineraryBadge;
    public TextView mapBadge;
    public TextView taskBadge;
    public TextView budgetBadge;
    public TextView tipsBadge;
    public TextView attractionsBadge;
    public TextView contactsBadge;
    private int buttonCount;
    public RelativeLayout btnGroupDays;
    public RelativeLayout btnGroupMaps;
    public RelativeLayout btnGroupTasks;
    public RelativeLayout btnGroupTips;
    public RelativeLayout btnGroupBudget;
    public RelativeLayout btnGroupAttractions;
    public RelativeLayout btnGroupContacts;
    public LinearLayout row1;
    public LinearLayout row2;
    public LinearLayout row3;


    // EditText Dialog
    public DialogWithEditTextFragment dialogWithEditTextFragment;
    public View.OnClickListener dwetOnOkClick;

    public Context context;
    //endregion

    //region Constructors/Destructors
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            layoutName="activity_holiday_details_view";
            setContentView(R.layout.activity_holiday_details_view);

            context=this;

            imageView=(ImageView) findViewById(R.id.imageViewSmall);
            txtStartDate=(TextView) findViewById(R.id.txtStartDate);
            grpStartDate=(LinearLayout) findViewById(R.id.grpStartDate);
            btnShowItinerary=(ImageButton) findViewById(R.id.btnShowItinerary);
            btnShowMaps=(ImageButton) findViewById(R.id.btnShowMaps);
            btnShowTasks=(ImageButton) findViewById(R.id.btnShowTasks);
            btnShowBudget=(ImageButton) findViewById(R.id.btnShowBudget);
            btnShowTips=(ImageButton) findViewById(R.id.btnShowTips);
            btnShowAttractions=(ImageButton) findViewById(R.id.btnShowAttractions);
            btnShowContacts=(ImageButton) findViewById(R.id.btnShowContacts);

            itineraryBadge=(TextView) findViewById(R.id.txtItineraryBadge);
            mapBadge=(TextView) findViewById(R.id.txtMapBadge);
            taskBadge=(TextView) findViewById(R.id.txtTaskBadge);
            budgetBadge=(TextView) findViewById(R.id.txtBudgetBadge);
            contactsBadge=(TextView) findViewById(R.id.txtContactBadge);
            tipsBadge=(TextView) findViewById(R.id.txtTipsBadge);
            attractionsBadge=(TextView) findViewById(R.id.txtAttractionBadge);

            row1=(LinearLayout)findViewById(R.id.row1);
            row2=(LinearLayout)findViewById(R.id.row2);
            row3=(LinearLayout)findViewById(R.id.row3);

            btnGroupDays=(RelativeLayout)findViewById(R.id.btnGroupDays);
            btnGroupMaps=(RelativeLayout)findViewById(R.id.btnGroupMaps);
            btnGroupTasks=(RelativeLayout)findViewById(R.id.btnGroupTasks);
            btnGroupTips=(RelativeLayout)findViewById(R.id.btnGroupTips);
            btnGroupBudget=(RelativeLayout)findViewById(R.id.btnGroupBudget);
            btnGroupAttractions=(RelativeLayout)findViewById(R.id.btnGroupAttractions);
            btnGroupContacts=(RelativeLayout)findViewById(R.id.btnGroupContacts);

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
            inflater.inflate(R.menu.holidaydetailsformmenu, menu);
        }
        catch(Exception e)
        {
            ShowError("onCreateOptionsMenu", e.getMessage());
        }
        return true;
    }

    //endregion

    //region form Functions
    public void showDay(View view)
    {
        try
        {
            Intent intent=new Intent(getApplicationContext(), DayDetailsList.class);
            intent.putExtra("HOLIDAYID", holidayId);
            startActivity(intent);
        }
        catch(Exception e)
        {
            ShowError("showDay", e.getMessage());
        }
    }

    public void showTasks(View view)
    {
        try
        {
            Intent intent2=new Intent(getApplicationContext(), TaskDetailsList.class);
            intent2.putExtra("HOLIDAYID", holidayItem.holidayId);
            intent2.putExtra("TITLE", holidayItem.holidayName);
            intent2.putExtra("SUBTITLE", "Tasks");
            startActivity(intent2);
        }
        catch(Exception e)
        {
            ShowError("showTasks", e.getMessage());
        }
    }

    public void showBudget(View view)
    {
        try
        {
            Intent intent2=new Intent(getApplicationContext(), BudgetDetailsList.class);
            intent2.putExtra("HOLIDAYID", holidayItem.holidayId);
            intent2.putExtra("TITLE", holidayItem.holidayName);
            intent2.putExtra("SUBTITLE", "Budget");
            startActivity(intent2);
        }
        catch(Exception e)
        {
            ShowError("showBudget", e.getMessage());
        }
    }

    public void showContacts(View view)
    {
        try
        {
            Intent intent2=new Intent(getApplicationContext(), ContactDetailsList.class);
            intent2.putExtra("HOLIDAYID", holidayItem.holidayId);
            intent2.putExtra("TITLE", holidayItem.holidayName);
            intent2.putExtra("SUBTITLE", "Contacts");
            startActivity(intent2);
        }
        catch(Exception e)
        {
            ShowError("showContacts", e.getMessage());
        }
    }

    public void showTipGroups(View view)
    {
        try
        {
            Intent intent2=new Intent(getApplicationContext(), TipGroupDetailsList.class);
            intent2.putExtra("HOLIDAYID", holidayItem.holidayId);
            intent2.putExtra("TITLE", holidayItem.holidayName);
            intent2.putExtra("SUBTITLE", "Tips");
            startActivity(intent2);
        }
        catch(Exception e)
        {
            ShowError("showTipGroups", e.getMessage());
        }
    }

    public void showAttractions(View view)
    {
        try
        {
            Intent intent2=new Intent(getApplicationContext(), AttractionDetailsList.class);
            intent2.putExtra("HOLIDAYID", holidayItem.holidayId);
            intent2.putExtra("TITLE", holidayItem.holidayName);
            intent2.putExtra("SUBTITLE", "Attractions");
            startActivity(intent2);
        }
        catch(Exception e)
        {
            ShowError("showAttractions", e.getMessage());
        }
    }

    public void showMaps(View view)
    {
        try
        {
            Intent intent2=new Intent(getApplicationContext(), ExtraFilesDetailsList.class);
            if(holidayItem.mapFileGroupId == 0)
            {
                MyInt myInt=new MyInt();
                if(!databaseAccess().getNextFileGroupId(myInt))
                    return;
                holidayItem.mapFileGroupId=myInt.Value;
                if(!databaseAccess().updateHolidayItem(holidayItem))
                    return;
            }
            intent2.putExtra("FILEGROUPID", holidayItem.mapFileGroupId);
            intent2.putExtra("TITLE", holidayItem.holidayName);
            intent2.putExtra("SUBTITLE", "Maps");
            startActivity(intent2);
        }
        catch(Exception e)
        {
            ShowError("showMaps", e.getMessage());
        }
    }

    @Override
    public void showForm()
    {
        super.showForm();

        try
        {
            MyInt myInt=new MyInt();

            holidayItem=new HolidayItem();
            if(!databaseAccess().getHolidayItem(holidayId, holidayItem))
                return;

            SetTitles(holidayItem.holidayName, "Holiday");

            if(!databaseAccess().getDayCount(holidayId, myInt))
                return;
            int dayCount=myInt.Value;
            itineraryBadge.setText("Days (" + Integer.toString(dayCount) + ")");

            if(!databaseAccess().getExtraFilesCount(holidayItem.mapFileGroupId, myInt))
                return;
            int mapCount=myInt.Value;
            mapBadge.setText("Maps (" + Integer.toString(mapCount) + ")");

            if(!databaseAccess().getTaskCount(holidayItem.holidayId, myInt))
                return;
            int taskCount=myInt.Value;
            taskBadge.setText("Tasks (" + Integer.toString(taskCount) + ")");

            if(!databaseAccess().getBudgetCount(holidayItem.holidayId, myInt))
                return;
            int budgetCount=myInt.Value;
            budgetBadge.setText("Budget (" + Integer.toString(budgetCount) + ")");

            if(!databaseAccess().getTipsCount(holidayItem.holidayId, myInt))
                return;
            int tipsCount=myInt.Value;
            tipsBadge.setText("Tips (" + Integer.toString(tipsCount) + ")");

            if(!databaseAccess().getAttractionsCount(holidayItem.holidayId, myInt))
                return;
            int attractionsCount=myInt.Value;
            attractionsBadge.setText("Attractions (" + Integer.toString(attractionsCount) + ")");

            if(!databaseAccess().getContactCount(holidayItem.holidayId, myInt))
                return;
            int contactCount=myInt.Value;
            contactsBadge.setText("Contacts (" + Integer.toString(contactCount) + ")");

            SetImage(holidayItem.holidayPicture);

            if(!holidayItem.dateKnown)
            {
                grpStartDate.setVisibility(View.INVISIBLE);
            } else
            {
                grpStartDate.setVisibility(View.VISIBLE);
                txtStartDate.setText(holidayItem.startDateStr);
            }

            buttonCount=0;
            showOrHideButton(btnGroupDays, holidayItem.buttonDays);
            showOrHideButton(btnGroupMaps, holidayItem.buttonMaps);
            showOrHideButton(btnGroupTasks, holidayItem.buttonTasks);
            showOrHideButton(btnGroupTips, holidayItem.buttonTips);
            showOrHideButton(btnGroupBudget, holidayItem.buttonBudget);
            showOrHideButton(btnGroupAttractions, holidayItem.buttonAttractions);
            showOrHideButton(btnGroupContacts, holidayItem.buttonContacts);

            afterShow();
        }
        catch(Exception e)

        {
            ShowError("showForm", e.getMessage());
        }

    }

    public void showOrHideButton(RelativeLayout layout, boolean show)
    {
        if(show)
        {
            buttonCount++;
            layout.setVisibility(View.VISIBLE);

            ViewGroup parent = (ViewGroup) layout.getParent();
            if (parent != null)
                parent.removeView(layout);

//            LinearLayout.LayoutParams params = new LinearLayout.LayoutP...
//            yourChildView.setLayoutParams(params);
            if(buttonCount==1 || buttonCount==2 || buttonCount==3)
                row1.addView(layout);
            if(buttonCount==4 || buttonCount==5 || buttonCount==6)
                row2.addView(layout);
            if(buttonCount==7 || buttonCount==8 || buttonCount==9)
                row3.addView(layout);
        }
        else
        {
            layout.setVisibility(View.GONE);
        }
    }
    public void editHoliday()
    {
        try
        {
            Intent intent=new Intent(getApplicationContext(), HolidayDetailsEdit.class);
            intent.putExtra("ACTION", "modify");
            intent.putExtra("HOLIDAYID", holidayId);
            startActivity(intent);
        }
        catch(Exception e)
        {
            ShowError("editHoliday", e.getMessage());
        }
    }

    public void deleteHoliday()
    {
        try
        {
            if(!databaseAccess().deleteHolidayItem(holidayItem))
                return;
            finish();
        }
        catch(Exception e)
        {
            ShowError("deleteHoliday", e.getMessage());
        }
    }

    @Override
    protected void onResume()
    {
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
        try
        {
            switch(item.getItemId())
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
        catch(Exception e)
        {
            ShowError("onOptionsItemSelected", e.getMessage());
        }
        return (true);
    }

    @Override
    public int getNoteId()
    {
        try
        {
            return (holidayItem.noteId);
        }
        catch(Exception e)
        {
            ShowError("getNoteId", e.getMessage());
        }
        return (0);
    }

    @Override
    public void setNoteId(int noteId)
    {
        try
        {
            holidayItem.noteId=noteId;
            databaseAccess().updateHolidayItem(holidayItem);
        }
        catch(Exception e)
        {
            ShowError("setNoteId", e.getMessage());
        }
    }

    @Override
    public int getInfoId()
    {
        try
        {
            return (holidayItem.infoId);
        }
        catch(Exception e)
        {
            ShowError("getInfoId", e.getMessage());
        }
        return (0);
    }

    @Override
    public void setInfoId(int infoId)
    {
        try
        {
            holidayItem.infoId=infoId;
            databaseAccess().updateHolidayItem(holidayItem);
        }
        catch(Exception e)
        {
            ShowError("setInfoId", e.getMessage());
        }

    }

    //endregion

}
