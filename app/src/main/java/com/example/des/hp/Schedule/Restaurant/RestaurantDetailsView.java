package com.example.des.hp.Schedule.Restaurant;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.ExtraFiles.ExtraFilesDetailsList;
import com.example.des.hp.R;
import com.example.des.hp.ScheduleArea.ScheduleAreaList;
import com.example.des.hp.myutils.*;
import com.example.des.hp.Schedule.*;
import com.example.des.hp.thirdpartyutils.BadgeView;
import com.example.des.hp.Notes.NoteItem;
import com.example.des.hp.Notes.NoteView;

public class RestaurantDetailsView extends BaseActivity
{

    public DatabaseAccess databaseAccess;
    private final int SELECT_PHOTO = 1;
    private final int MOVEITEM = 2;
    private ImageView imageViewSmall;
    private String originalFileName;
    private String action;
    public int holidayId;
    public int dayId;
    public int attractionId;
    public int attractionAreaId;
    public int scheduleId;
    public DateUtils dateUtils;
    public LinearLayout grpStartDate;
    public TextView txtSchedName;
    public String holidayName;
    public ActionBar actionBar;
    public ScheduleItem scheduleItem;
    public RestaurantItem restaurantItem;
    public CheckBox cbPicturePicked;
    private ImageUtils imageUtils;
    public CheckBox chkCheckinKnown;
    public TextView checkIn;
    public CheckBox chkDepartureKnown;
    public TextView departs;
    public TextView txtBookingRef;
    public MyMessages myMessages;
    public ImageButton btnShowInfo;
    public BadgeView btnShowInfoBadge;
    public MyColor myColor;
    public RadioButton radUnknown;
    public RadioButton radWalkIn;
    public RadioButton radOnTheDay;
    public RadioButton rad180Days;
    public ImageButton btnShowNotes;

    private void ShowError(String argFunction, String argMessage)
    {
        myMessages.ShowError
                (
                        "Error in RestaurantDetailsView::" + argFunction,
                        argMessage
                );
    }
    public void showNotes(View view)
    {
        Intent intent2 = new Intent(getApplicationContext(), NoteView.class);
        if(scheduleItem.noteId==0)
        {
            MyInt myInt = new MyInt();
            if(!databaseAccess.getNextNoteId(holidayId, myInt))
                return;
            scheduleItem.noteId = myInt.Value;
            if(!databaseAccess.updateScheduleItem(scheduleItem))
                return;
        }
        intent2.putExtra("ACTION", "view");
        intent2.putExtra("HOLIDAYID", scheduleItem.holidayId);
        intent2.putExtra("NOTEID", scheduleItem.noteId);
        intent2.putExtra("TITLE", scheduleItem.schedName);
        intent2.putExtra("SUBTITLE", "Notes");
        startActivity(intent2);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        try
        {
            switch (requestCode)
            {
                case MOVEITEM:
                    if (resultCode == RESULT_OK)
                    {
                        try
                        {
                            scheduleItem.dayId = data.getIntExtra("DAYID", 0);
                            scheduleItem.attractionId = data.getIntExtra("ATTRACTIONID", 0);
                            scheduleItem.attractionAreaId = data.getIntExtra("ATTRACTIONAREAID", 0);
                            databaseAccess.updateScheduleItem(scheduleItem);
                            finish();
                        } catch (Exception e)
                        {
                            ShowError("onActivityResult-MOVEITEM", e.getMessage());
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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_restaurant_details_view);

        actionBar = getSupportActionBar();
        databaseAccess = new DatabaseAccess(this);
        dateUtils = new DateUtils(this);
        imageUtils = new ImageUtils(this);
        myMessages = new MyMessages(this);
        myColor = new MyColor(this);

        cbPicturePicked=(CheckBox)findViewById(R.id.picturePicked);
        imageViewSmall = (ImageView)findViewById(R.id.imageViewSmall);
        txtSchedName=(TextView)findViewById(R.id.txtSchedName);
        checkIn=(TextView)findViewById(R.id.txtCheckin);
        departs=(TextView)findViewById(R.id.txtDeparture);
        txtBookingRef=(TextView)findViewById(R.id.txtBookingRef);
        chkCheckinKnown=(CheckBox)findViewById(R.id.chkCheckinKnown);
        chkDepartureKnown=(CheckBox)findViewById(R.id.chkDepartureKnown);
        btnShowInfo=(ImageButton) findViewById(R.id.btnShowInfo);
        radUnknown=(RadioButton)findViewById(R.id.radUnknown);
        radWalkIn=(RadioButton)findViewById(R.id.radWalkIn);
        radOnTheDay=(RadioButton)findViewById(R.id.radOnTheDay);
        rad180Days=(RadioButton)findViewById(R.id.rad180Days);
        btnShowNotes=(ImageButton) findViewById(R.id.btnShowNotes);

        btnShowInfoBadge = new BadgeView(this, btnShowInfo);
        btnShowInfoBadge.setText(Integer.toString(0));
        btnShowInfoBadge.show();

        showForm();
    }

    public void showForm()
    {
        try {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String title = extras.getString("TITLE");
                String subtitle = extras.getString("SUBTITLE");
                actionBar.setTitle(title);
                actionBar.setSubtitle(subtitle);

                holidayId = extras.getInt("HOLIDAYID");
                dayId = extras.getInt("DAYID");
                attractionId = extras.getInt("ATTRACTIONID");
                attractionAreaId = extras.getInt("ATTRACTIONAREAID");
                holidayName = extras.getString("HOLIDAYNAME");

                action = extras.getString("ACTION");
                if (action != null && action.equals("view")) {
                    scheduleId = extras.getInt("SCHEDULEID");
                    scheduleItem = new ScheduleItem();
                    if (!databaseAccess.getScheduleItem(holidayId, dayId,
                            attractionId, attractionAreaId, scheduleId, scheduleItem))
                        return;

                    if (scheduleItem.schedType == getResources().getInteger(R.integer.schedule_type_unknown)) {
                        finish();
                        return;
                    }

                    radUnknown.setChecked(false);
                    radWalkIn.setChecked(false);
                    radOnTheDay.setChecked(false);
                    rad180Days.setChecked(false);
                    if (scheduleItem.restaurantItem.reservationType == 0)
                        radUnknown.setChecked(true);
                    if (scheduleItem.restaurantItem.reservationType == 1)
                        radWalkIn.setChecked(true);
                    if (scheduleItem.restaurantItem.reservationType == 2)
                        radOnTheDay.setChecked(true);
                    if (scheduleItem.restaurantItem.reservationType == 3)
                        rad180Days.setChecked(true);

                    chkCheckinKnown.setChecked(scheduleItem.startTimeKnown);
                    setTimeText(checkIn, scheduleItem.startHour, scheduleItem.startMin);

                    chkDepartureKnown.setChecked(scheduleItem.endTimeKnown);
                    setTimeText(departs, scheduleItem.endHour, scheduleItem.endMin);

                    txtSchedName.setText(scheduleItem.schedName);
                    txtBookingRef.setText(scheduleItem.restaurantItem.bookingReference);

                    originalFileName = scheduleItem.schedPicture;

                    if (imageUtils.getPageHeaderImage(this, scheduleItem.schedPicture, imageViewSmall) == false)
                        return;

                    cbPicturePicked.setChecked(scheduleItem.pictureAssigned);

                    MyInt lFileCount = new MyInt();
                    lFileCount.Value = 0;
                    if (scheduleItem.infoId > 0) {
                        if (!databaseAccess.getExtraFilesCount(scheduleItem.infoId, lFileCount))
                            return;
                    }
                    btnShowInfoBadge.setText(Integer.toString(lFileCount.Value));

                    if (lFileCount.Value == 0) {
                        btnShowInfoBadge.hide();
                        if (myColor.SetImageButtonTint(btnShowInfo, R.color.colorDisabled) == false)
                            return;
                    } else {
                        btnShowInfoBadge.show();
                        if (myColor.SetImageButtonTint(btnShowInfo, R.color.colorEnabled) == false)
                            return;
                    }
                    NoteItem noteItem = new NoteItem();
                    if(!databaseAccess.getNoteItem(scheduleItem.holidayId, scheduleItem.noteId, noteItem))
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

    public void showInfo(View view)
    {
        Intent intent2 = new Intent(getApplicationContext(), ExtraFilesDetailsList.class);
        if(scheduleItem.infoId==0)
        {
            MyInt myInt = new MyInt();
            if(!databaseAccess.getNextFileGroupId(myInt))
                return;
            scheduleItem.infoId = myInt.Value;
            if(!databaseAccess.updateScheduleItem(scheduleItem))
                return;
        }
        intent2.putExtra("FILEGROUPID", scheduleItem.infoId);
        intent2.putExtra("TITLE", scheduleItem.schedName);
        intent2.putExtra("SUBTITLE", "Info");
        startActivity(intent2);
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

    public void editRestaurant()
    {
        Intent intent = new Intent(getApplicationContext(), RestaurantDetailsEdit.class);
        intent.putExtra("ACTION", "edit");
        intent.putExtra("HOLIDAYID", holidayId);
        intent.putExtra("DAYID", dayId);
        intent.putExtra("ATTRACTIONID", attractionId);
        intent.putExtra("ATTRACTIONAREAID", attractionAreaId);
        intent.putExtra("SCHEDULEID", scheduleId);
        intent.putExtra("HOLIDAYNAME", holidayName);
        intent.putExtra("TITLE", actionBar.getTitle());
        intent.putExtra("SUBTITLE", actionBar.getSubtitle());

        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_delete_restaurant:
                deleteRestaurant();
                return true;
            case R.id.action_edit_restaurant:
                editRestaurant();
                return true;
            case R.id.action_move:
                move();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void move()
    {
        try
        {
            Intent intent = new Intent(getApplicationContext(), ScheduleAreaList.class);
            intent.putExtra("ACTION", "move");
            intent.putExtra("HOLIDAYID", holidayId);
            intent.putExtra("DAYID", dayId);
            intent.putExtra("ATTRACTIONID", attractionId);
            intent.putExtra("ATTRACTIONAREAID", attractionAreaId);
            intent.putExtra("SCHEDULEID", scheduleId);
            startActivityForResult(intent, MOVEITEM);
        }
        catch (Exception e)
        {
            ShowError("move", e.getMessage());
        }
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.restaurantdetailsformmenu, menu);
        return true;
    }

    public void deleteRestaurant()
    {
        if(!databaseAccess.deleteScheduleItem(scheduleItem))
            return;

        finish();
    }


    private int getHour(TextView textview)
    {
        String[] sarray=textview.getText().toString().split(":");
        int lHour = Integer.parseInt(sarray[0]);
        if(lHour<0)
            lHour=0;
        if(lHour>23)
            lHour=23;
        return(lHour);
    }

    private int getMinute(TextView textview)
    {
        String[] sarray=textview.getText().toString().split(":");
        int lMinute = Integer.parseInt(sarray[1]);
        if(lMinute<0)
            lMinute=0;
        if(lMinute>59)
            lMinute=59;
        return(lMinute);
    }

    public void checkInClick(View view)
    {
        handleTime(checkIn, chkCheckinKnown, "Select Reservation Time");
    }

    public void departureClick(View view)
    {
        handleTime(departs, chkDepartureKnown, "Select Finish Time");
    }

    private void handleTime(TextView txtTime, CheckBox chkTime, String title)
    {
        DialogTimePicker mTimePicker;
        int hour;
        int minute;

        hour=getHour(txtTime);
        minute=getMinute(txtTime);

        mTimePicker = new DialogTimePicker(this);
        mTimePicker.title = title;
        mTimePicker.chkTimeKnown = chkTime;
        mTimePicker.txtStartTime = txtTime;
        mTimePicker.hour=hour;
        mTimePicker.minute = minute;
        mTimePicker.timeKnown = chkTime.isChecked();
        mTimePicker.show();
    }

    private void setTimeText(TextView textView, int hour, int minute)
    {
        String lTime;
        lTime="";
        if(hour<10)
            lTime="0";
        lTime=lTime+hour;
        lTime=lTime+":";
        if(minute<10)
            lTime=lTime+"0";
        lTime=lTime+minute;
        textView.setText(lTime);
    }

}
