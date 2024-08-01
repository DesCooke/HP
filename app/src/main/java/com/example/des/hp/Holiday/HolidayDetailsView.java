/*
Lets you view a single holiday item and shows a select of icons (days, budget etc)
 */

package com.example.des.hp.Holiday;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.des.hp.Budget.BudgetDetailsList;
import com.example.des.hp.Contact.ContactDetailsList;
import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.Event.EventScheduleItem;
import com.example.des.hp.Poi.PoiList;
import com.example.des.hp.TipGroup.*;
import com.example.des.hp.ThemeParks.*;
import com.example.des.hp.Day.DayDetailsList;
import com.example.des.hp.ExtraFiles.*;
import com.example.des.hp.R;
import com.example.des.hp.Tasks.TaskDetailsList;
import com.example.des.hp.myutils.*;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

import java.util.ArrayList;

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
    public ImageButton btnShowThemeParks;
    public ImageButton btnShowContacts;
    public ImageButton btnShowPoi;
    public ImageView btnMap;
    public TextView itineraryBadge;
    public TextView mapBadge;
    public TextView taskBadge;
    public TextView budgetBadge;
    public TextView tipsBadge;
    public TextView themeParksBadge;
    public TextView contactsBadge;
    public TextView poiBadge;
    private int buttonCount;
    public RelativeLayout btnGroupDays;
    public RelativeLayout btnGroupMaps;
    public RelativeLayout btnGroupTasks;
    public RelativeLayout btnGroupTips;
    public RelativeLayout btnGroupBudget;
    public RelativeLayout btnGroupAttractions;
    public RelativeLayout btnGroupContacts;
    public RelativeLayout btnGroupPoi;
    public LinearLayout row1;
    public LinearLayout row2;
    public LinearLayout row3;
    public ImageView editHoliday;
    public MyString Url1;
    public MyString Url2;
    public MyString Url3;
    public ImageView btnUrl1;
    public ImageView btnUrl2;
    public ImageView btnUrl3;


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

            Url1 = new MyString();
            Url2 = new MyString();
            Url3 = new MyString();

            imageView= findViewById(R.id.imageViewSmall);
            txtStartDate= findViewById(R.id.txtStartDate);
            grpStartDate= findViewById(R.id.grpStartDate);
            btnShowItinerary= findViewById(R.id.btnShowItinerary);
            btnShowMaps= findViewById(R.id.btnShowMaps);
            btnShowTasks= findViewById(R.id.btnShowTasks);
            btnShowBudget= findViewById(R.id.btnShowBudget);
            btnShowTips= findViewById(R.id.btnShowTips);
            btnShowThemeParks = findViewById(R.id.btnShowThemeParks);
            btnShowContacts= findViewById(R.id.btnShowContacts);
            btnShowPoi= findViewById(R.id.btnShowPoi);

            itineraryBadge= findViewById(R.id.txtItineraryBadge);
            mapBadge= findViewById(R.id.txtMapBadge);
            taskBadge= findViewById(R.id.txtTaskBadge);
            budgetBadge= findViewById(R.id.txtBudgetBadge);
            contactsBadge= findViewById(R.id.txtContactBadge);
            poiBadge= findViewById(R.id.txtPoiBadge);
            tipsBadge= findViewById(R.id.txtTipsBadge);
            themeParksBadge = findViewById(R.id.txtThemeParksBadge);

            editHoliday=findViewById(R.id.my_toolbar_edit);
            editHoliday.setOnClickListener(view -> editHoliday());

            row1= findViewById(R.id.row1);
            row2= findViewById(R.id.row2);
            row3= findViewById(R.id.row3);

            btnGroupDays= findViewById(R.id.btnGroupDays);
            btnGroupMaps= findViewById(R.id.btnGroupMaps);
            btnGroupTasks= findViewById(R.id.btnGroupTasks);
            btnGroupTips= findViewById(R.id.btnGroupTips);
            btnGroupBudget= findViewById(R.id.btnGroupBudget);
            btnGroupAttractions= findViewById(R.id.btnGroupThemeParks);
            btnGroupContacts= findViewById(R.id.btnGroupContacts);
            btnGroupPoi= findViewById(R.id.btnGroupPoi);

            btnUrl1= findViewById(R.id.btnUrl1);
            btnUrl2= findViewById(R.id.btnUrl2);
            btnUrl3= findViewById(R.id.btnUrl3);

            btnUrl1.setOnClickListener(view -> viewUrl1());
            btnUrl2.setOnClickListener(view -> viewUrl2());
            btnUrl3.setOnClickListener(view -> viewUrl3());

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
            intent2.putExtra("TITLE", "Tasks");
            intent2.putExtra("SUBTITLE", holidayItem.holidayName);
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
            intent2.putExtra("TITLE", "Budget");
            intent2.putExtra("SUBTITLE", holidayItem.holidayName);
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

    public void showPoi(View view)
    {
        try
        {
            Intent intent2=new Intent(getApplicationContext(), PoiList.class);
            intent2.putExtra("HOLIDAYID", holidayItem.holidayId);
            intent2.putExtra("TITLE", "Points of Interest");
            intent2.putExtra("SUBTITLE", holidayItem.holidayName);
            startActivity(intent2);
        }
        catch(Exception e)
        {
            ShowError("showTipGroups", e.getMessage());
        }
    }


    public void showThemeParks(View view)
    {
        try
        {
            Intent intent2=new Intent(getApplicationContext(), ThemeParkList.class);
            intent2.putExtra("HOLIDAYID", holidayItem.holidayId);
            intent2.putExtra("TITLE", "Theme Parks");
            intent2.putExtra("SUBTITLE", holidayItem.holidayName);
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
                try(DatabaseAccess da = databaseAccess())
                {
                    if(!da.getNextFileGroupId(myInt))
                        return;
                    holidayItem.mapFileGroupId=myInt.Value;
                    if(!da.updateHolidayItem(holidayItem))
                        return;
                }
            }
            intent2.putExtra("FILEGROUPID", holidayItem.mapFileGroupId);
            intent2.putExtra("HOLIDAYID", holidayItem.holidayId);
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

            showUrlMenu();

            holidayItem=new HolidayItem();
            try(DatabaseAccess da = databaseAccess())
            {

                if(!da.getHolidayItem(holidayId, holidayItem))
                    return;

                SetToolbarTitles(holidayItem.holidayName, "Holiday");
                ShowToolbarEdit();

                if(!da.getDayCount(holidayId, myInt))
                    return;
                int dayCount=myInt.Value;
                itineraryBadge.setText(String.format("Days (%s)", dayCount));

                if(!da.getExtraFilesCount(holidayItem.mapFileGroupId, myInt))
                    return;
                int mapCount=myInt.Value;
                mapBadge.setText(String.format("Maps (%s)", mapCount));

                if(!da.getTaskCount(holidayItem.holidayId, myInt))
                    return;
                int taskCount=myInt.Value;
                if(!da.getOSTaskCount(holidayItem.holidayId, myInt))
                    return;
                int osTaskCount=myInt.Value;
                taskBadge.setText(String.format("Tasks (%s / %s)", osTaskCount, taskCount));

                if(!da.getBudgetCount(holidayItem.holidayId, myInt))
                    return;
                int budgetCount=myInt.Value;
                if(!da.getOSBudgetCount(holidayItem.holidayId, myInt))
                    return;
                int osBudgetCount=myInt.Value;
                budgetBadge.setText(String.format("Budget (%s / %s)", osBudgetCount, budgetCount));

                if(!da.getTipsCount(holidayItem.holidayId, myInt))
                    return;
                int tipsCount=myInt.Value;
                tipsBadge.setText(String.format("Tips (%s)", tipsCount));

                if(!da.getAttractionsCount(holidayItem.holidayId, myInt))
                    return;
                int themeParksCount=myInt.Value;
                themeParksBadge.setText(String.format("Theme Parks (%s)", themeParksCount));

                if(!da.getContactCount(holidayItem.holidayId, myInt))
                    return;
                int contactCount=myInt.Value;
                contactsBadge.setText(String.format("Contacts (%s)", contactCount));

                ArrayList<EventScheduleItem> scheduleList = new ArrayList<>();
                if (!da.getScheduleList(holidayId, 0, 0, 0, scheduleList))
                    return;
                poiBadge.setText(String.format("POIs (%s)", scheduleList.size()));

                if(action.equals("view"))
                {
                    Url1.Value=holidayItem.url1;
                    Url2.Value=holidayItem.url2;
                    Url3.Value=holidayItem.url3;
                    btnUrl1.setVisibility(View.INVISIBLE);
                    btnUrl2.setVisibility(View.INVISIBLE);
                    btnUrl3.setVisibility(View.INVISIBLE);
                    if(!Url1.Value.isEmpty())
                        btnUrl1.setVisibility(View.VISIBLE);
                    if(!Url2.Value.isEmpty())
                        btnUrl2.setVisibility(View.VISIBLE);
                    if(!Url3.Value.isEmpty())
                        btnUrl3.setVisibility(View.VISIBLE);
                }

                if(!holidayItem.dateKnown)
                {
                    grpStartDate.setVisibility(View.INVISIBLE);
                } else
                {
                    grpStartDate.setVisibility(View.VISIBLE);
                    if(dayCount==0) {
                        txtStartDate.setText(holidayItem.startDateStr);
                    }
                    else{
                        txtStartDate.setText(String.format("%s -> %s", holidayItem.startDateStr, holidayItem.endDateStr));
                    }
                }


            }
            SetImage(holidayItem.holidayPicture);

            buttonCount=0;
            showOrHideButton(btnGroupDays, holidayItem.buttonDays);
            showOrHideButton(btnGroupMaps, holidayItem.buttonMaps);
            showOrHideButton(btnGroupTasks, holidayItem.buttonTasks);
            showOrHideButton(btnGroupTips, holidayItem.buttonTips);
            showOrHideButton(btnGroupBudget, holidayItem.buttonBudget);
            showOrHideButton(btnGroupAttractions, holidayItem.buttonAttractions);
            showOrHideButton(btnGroupContacts, holidayItem.buttonContacts);
            showOrHideButton(btnGroupPoi, holidayItem.buttonPoi);

            afterShow();
        }
        catch(Exception e)

        {
            ShowError("showForm", e.getMessage());
        }

    }

    public void viewUrl1(){
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Url1.Value));
            startActivity(browserIntent);
        } catch (Exception e)
        {
            ShowError("showForm", e.getMessage());
        }
    }

    public void viewUrl2(){
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Url2.Value));
            startActivity(browserIntent);
        } catch (Exception e)
        {
            ShowError("showForm", e.getMessage());
        }
    }

    public void viewUrl3(){
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Url3.Value));
            startActivity(browserIntent);
        } catch (Exception e)
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

    @Override
    protected void onResume()
    {
        super.onResume();
        try
        {
            if(action.compareTo("add")!=0) {
                try (DatabaseAccess da = databaseAccess()) {
                    if (!da.getHolidayItem(holidayId, holidayItem)) {
                        finish();
                    }
                    if (holidayItem != null) {
                        if (holidayItem.holidayId == 0) {
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
            try(DatabaseAccess da = databaseAccess())
            {
                da.updateHolidayItem(holidayItem);
            }
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
            try(DatabaseAccess da = databaseAccess())
            {
                da.updateHolidayItem(holidayItem);
            }
        }
        catch(Exception e)
        {
            ShowError("setInfoId", e.getMessage());
        }

    }

    //endregion

}
