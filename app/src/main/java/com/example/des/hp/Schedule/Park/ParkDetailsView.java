package com.example.des.hp.Schedule.Park;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBar;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.TextView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.ExtraFiles.ExtraFilesDetailsList;
import com.example.des.hp.R;
import com.example.des.hp.Schedule.Ride.RideDetailsEdit;
import com.example.des.hp.ScheduleArea.ScheduleAreaList;
import com.example.des.hp.myutils.*;
import com.example.des.hp.Schedule.*;
import com.example.des.hp.thirdpartyutils.BadgeView;
import com.example.des.hp.Notes.NoteItem;
import com.example.des.hp.Notes.NoteView;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ParkDetailsView extends BaseActivity
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
    public ParkItem parkItem;
    public CheckBox cbPicturePicked;
    private ImageUtils imageUtils;
    public CheckBox chkCheckinKnown;
    public TextView checkIn;
    public CheckBox chkDepartureKnown;
    public TextView departs;
    public TextView txtBookingRef;
    public MyMessages myMessages;
    public DialogWithEditTextFragment dialogWithEditTextFragment;
    public View.OnClickListener dwetOnOkClick;
    public ImageButton btnShowInfo;
    public BadgeView btnShowInfoBadge;
    public MyColor myColor;
    public ImageButton btnShowNotes;

    public void clearImage(View view)
    {
        try {
            imageViewSmall.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.imagemissing));
        }
        catch (Exception e)
        {
            ShowError("clearImage", e.getMessage());
        }
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


    private void ShowError(String argFunction, String argMessage)
    {
        myMessages.ShowError
                (
                        "Error in ParkDetailsView::" + argFunction,
                        argMessage
                );
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

        try {
            setContentView(R.layout.activity_park_details_view);

            actionBar = getSupportActionBar();
            databaseAccess = new DatabaseAccess(this);
            dateUtils = new DateUtils(this);
            imageUtils = new ImageUtils(this);
            myMessages = new MyMessages(this);
            myColor = new MyColor(this);

            cbPicturePicked = (CheckBox) findViewById(R.id.picturePicked);
            imageViewSmall = (ImageView) findViewById(R.id.imageViewSmall);
            txtSchedName = (TextView) findViewById(R.id.txtSchedName);
            checkIn = (TextView) findViewById(R.id.txtCheckin);
            departs = (TextView) findViewById(R.id.txtDeparture);
            txtBookingRef = (TextView) findViewById(R.id.txtBookingRef);
            chkCheckinKnown = (CheckBox) findViewById(R.id.chkCheckinKnown);
            chkDepartureKnown = (CheckBox) findViewById(R.id.chkDepartureKnown);
            btnShowInfo = (ImageButton) findViewById(R.id.btnShowInfo);
            btnShowNotes=(ImageButton) findViewById(R.id.btnShowNotes);

            btnShowInfoBadge = new BadgeView(this, btnShowInfo);
            btnShowInfoBadge.setText(Integer.toString(0));
            btnShowInfoBadge.show();

            showForm();
        }
        catch (Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }

    }

    public void showForm()
    {
        try {
            clearImage(null);
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

                    setTimeText(checkIn, scheduleItem.startHour, scheduleItem.startMin);

                    setTimeText(departs, scheduleItem.endHour, scheduleItem.endMin);

                    txtSchedName.setText(scheduleItem.schedName);
                    txtBookingRef.setText(scheduleItem.parkItem.bookingReference);

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

                    originalFileName = scheduleItem.schedPicture;

                    if (imageUtils.getPageHeaderImage(this, scheduleItem.schedPicture, imageViewSmall) == false)
                        return;
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
        catch (Exception e)
        {
            ShowError("showForm", e.getMessage());
        }
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
        try {
            switch (item.getItemId()) {
                case R.id.action_edit_park:
                    editPark();
                    return true;
                case R.id.action_delete_park:
                    deletePark();
                    return true;
                case R.id.action_move:
                    move();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
        catch (Exception e)
        {
            ShowError("onOptionsItemSelected", e.getMessage());
        }
        return(true);
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
        try {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.parkdetailsformmenu, menu);
        }
        catch (Exception e)
        {
            ShowError("onCreateOptionsMenu", e.getMessage());
        }
        return true;
    }

    public void editPark()
    {
        try {
            Intent intent = new Intent(getApplicationContext(), ParkDetailsEdit.class);
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
        catch (Exception e)
        {
            ShowError("editPark", e.getMessage());
        }
    }

    public void showInfo(View view)
    {
        try {
            Intent intent2 = new Intent(getApplicationContext(), ExtraFilesDetailsList.class);
            if (scheduleItem.infoId == 0) {
                MyInt myInt = new MyInt();
                if (!databaseAccess.getNextFileGroupId(myInt))
                    return;
                scheduleItem.infoId = myInt.Value;
                if (!databaseAccess.updateScheduleItem(scheduleItem))
                    return;
            }
            intent2.putExtra("FILEGROUPID", scheduleItem.infoId);
            intent2.putExtra("TITLE", scheduleItem.schedName);
            intent2.putExtra("SUBTITLE", "Info");
            startActivity(intent2);
        }
        catch (Exception e)
        {
            ShowError("showInfo", e.getMessage());
        }
    }



    public void deletePark()
    {
        try {
            if (!databaseAccess.deleteScheduleItem(scheduleItem))
                return;

            finish();
        }
        catch (Exception e)
        {
            ShowError("deletePark", e.getMessage());
        }
    }


    private int getHour(TextView textview)
    {
        try {
            String[] sarray = textview.getText().toString().split(":");
            int lHour = Integer.parseInt(sarray[0]);
            if (lHour < 0)
                lHour = 0;
            if (lHour > 23)
                lHour = 23;
            return (lHour);
        }
        catch (Exception e)
        {
            ShowError("getHour", e.getMessage());
        }
        return(0);
    }

    private int getMinute(TextView textview)
    {
        try {
            String[] sarray = textview.getText().toString().split(":");
            int lMinute = Integer.parseInt(sarray[1]);
            if (lMinute < 0)
                lMinute = 0;
            if (lMinute > 59)
                lMinute = 59;
            return (lMinute);
        }
        catch (Exception e)
        {
            ShowError("getMinute", e.getMessage());
        }
        return(0);
    }

    private void handleTime(TextView txtTime, CheckBox chkTime, String title)
    {
        try {
            DialogTimePicker mTimePicker;
            int hour;
            int minute;

            hour = getHour(txtTime);
            minute = getMinute(txtTime);

            mTimePicker = new DialogTimePicker(this);
            mTimePicker.title = title;
            mTimePicker.chkTimeKnown = chkTime;
            mTimePicker.txtStartTime = txtTime;
            mTimePicker.hour = hour;
            mTimePicker.minute = minute;
            mTimePicker.timeKnown = chkTime.isChecked();
            mTimePicker.show();
        }
        catch (Exception e)
        {
            ShowError("handleTime", e.getMessage());
        }
    }

    private void setTimeText(TextView textView, int hour, int minute)
    {
        try {
            String lTime;
            lTime = "";
            if (hour < 10)
                lTime = "0";
            lTime = lTime + hour;
            lTime = lTime + ":";
            if (minute < 10)
                lTime = lTime + "0";
            lTime = lTime + minute;
            textView.setText(lTime);
        }
        catch (Exception e)
        {
            ShowError("setTimeText", e.getMessage());
        }
    }

}
