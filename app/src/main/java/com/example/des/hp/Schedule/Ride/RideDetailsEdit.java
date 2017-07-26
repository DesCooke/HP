package com.example.des.hp.Schedule.Ride;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.graphics.BitmapFactory;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.R;
import com.example.des.hp.myutils.*;
import com.example.des.hp.Schedule.*;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
import static com.example.des.hp.myutils.MyMessages.myMessages;

public class RideDetailsEdit extends BaseActivity
{

    private final int SELECT_PHOTO = 1;
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
    public CheckBox cbPicturePicked;
    private ImageUtils imageUtils;
    public CheckBox chkCheckinKnown;
    public TextView checkIn;
    public CheckBox chkDepartureKnown;
    public TextView departs;
    public TextView txtBookingRef;
    public MyColor myColor;
    public DialogWithEditTextFragment dialogWithEditTextFragment;
    public View.OnClickListener dwetOnOkClick;
    public RatingBar heartRating;
    public RatingBar scenicRating;
    public RatingBar thrillRating;

    public void pickImage(View view)
    {
        try
        {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
        }
        catch(Exception e)
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
                            if(lRetCode==false)
                                return;

                            // assign new bitmap and set scale type
                            imageViewSmall.setImageBitmap(myBitmap.Value);

                            cbPicturePicked.setChecked(true);

                            scheduleItem.pictureChanged = true;


                        } catch (Exception e)
                        {
                            ShowError("onActivityResult-selectphoto", e.getMessage());
                        }
                    }
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
        catch(Exception e)
        {
            ShowError("clearImage", e.getMessage());
        }

    }

    public void btnClearImage(View view)
    {
        try
        {
        clearImage(view);
        scheduleItem.pictureChanged=true;
        }
        catch(Exception e)
        {
            ShowError("btnClearImage", e.getMessage());
        }

    }

    public void SchedNamePicked(View view)
    {
        try
        {
        txtSchedName.setText(dialogWithEditTextFragment.getFinalText());

        dialogWithEditTextFragment.dismiss();
        }
        catch(Exception e)
        {
            ShowError("SchedNamePicked", e.getMessage());
        }

    }

    public void pickSchedName(View view)
    {
        try
        {
        dwetOnOkClick = new View.OnClickListener()
        {
            public void onClick(View view)
            {
                SchedNamePicked(view);
            }
        };

        dialogWithEditTextFragment =
                DialogWithEditTextFragment.newInstance
                        (
                                getFragmentManager(),     // for the transaction bit
                                "hihi",            // unique name for this dialog type
                                "Ride" ,    // form caption
                                "Ride",             // form message
                                R.drawable.attachment,
                                txtSchedName.getText().toString(), // initial text
                                dwetOnOkClick,
                                this,
                                false
                        );

        dialogWithEditTextFragment.showIt();
        }
        catch(Exception e)
        {
            ShowError("pickSchedName", e.getMessage());
        }

    }


    public void BookingRefPicked(View view)
    {
        try
        {
        txtBookingRef.setText(dialogWithEditTextFragment.getFinalText());

        dialogWithEditTextFragment.dismiss();
        }
        catch(Exception e)
        {
            ShowError("BookingRefPicked", e.getMessage());
        }

    }

    public void saveSchedule(View view)
    {
        try {
            MyInt myInt = new MyInt();
            myMessages().ShowMessageShort("Saving Schedule");

            scheduleItem.pictureAssigned = cbPicturePicked.isChecked();
            scheduleItem.schedName = txtSchedName.getText().toString();
            scheduleItem.scheduleBitmap = null;
            if (scheduleItem.pictureAssigned)
                scheduleItem.scheduleBitmap = ((BitmapDrawable) imageViewSmall.getDrawable()).getBitmap();

            if (action.equals("add")) {
                scheduleItem.holidayId = holidayId;
                scheduleItem.dayId = dayId;
                scheduleItem.attractionId = attractionId;
                scheduleItem.attractionAreaId = attractionAreaId;

                if (!databaseAccess().getNextScheduleId(holidayId, dayId, attractionId, attractionAreaId, myInt))
                    return;
                scheduleItem.scheduleId = myInt.Value;

                if (!databaseAccess().getNextScheduleSequenceNo(holidayId, dayId, attractionId, attractionAreaId, myInt))
                    return;
                scheduleItem.sequenceNo = myInt.Value;

                scheduleItem.schedType = getResources().getInteger(R.integer.schedule_type_ride);

                scheduleItem.rideItem.holidayId = holidayId;
                scheduleItem.rideItem.dayId = dayId;
                scheduleItem.rideItem.attractionId = attractionId;
                scheduleItem.rideItem.attractionAreaId = attractionAreaId;
                scheduleItem.rideItem.scheduleId = scheduleItem.scheduleId;
                scheduleItem.rideItem.heartRating = heartRating.getRating();
                scheduleItem.rideItem.scenicRating = scenicRating.getRating();
                scheduleItem.rideItem.thrillRating = thrillRating.getRating();

                if (!databaseAccess().addScheduleItem(scheduleItem))
                    return;
            }

            if (action.equals("edit")) {
                scheduleItem.startTimeKnown = false;
                scheduleItem.startHour = 0;
                scheduleItem.startMin = 0;
                scheduleItem.endTimeKnown = false;
                scheduleItem.endHour = 0;
                scheduleItem.endMin = 0;
                if (scheduleItem.rideItem != null) {
                    scheduleItem.rideItem.heartRating = heartRating.getRating();
                    scheduleItem.rideItem.scenicRating = scenicRating.getRating();
                    scheduleItem.rideItem.thrillRating = thrillRating.getRating();
                }

                if (!databaseAccess().updateScheduleItem(scheduleItem))
                    return;
            }

            finish();
        }
        catch (Exception e)
        {
            ShowError("SaveSchedule", e.getMessage());
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
                if (action != null && action.equals("add")) {
                    scheduleItem = new ScheduleItem();
                    scheduleItem.rideItem = new RideItem();

                    txtSchedName.setText("");
                    cbPicturePicked.setChecked(false);
                }
                if (action != null && action.equals("edit")) {
                    scheduleId = extras.getInt("SCHEDULEID");
                    scheduleItem = new ScheduleItem();
                    if (!databaseAccess().getScheduleItem(holidayId, dayId,
                            attractionId, attractionAreaId, scheduleId, scheduleItem))
                        return;

                    if (imageUtils.getPageHeaderImage(this, scheduleItem.schedPicture, imageViewSmall) == false)
                        return;

                    txtSchedName.setText(scheduleItem.schedName);

                    cbPicturePicked.setChecked(scheduleItem.pictureAssigned);

                    if (scheduleItem != null) {
                        if (scheduleItem.rideItem != null) {
                            heartRating.setRating(scheduleItem.rideItem.heartRating);
                            thrillRating.setRating(scheduleItem.rideItem.thrillRating);
                            scenicRating.setRating(scheduleItem.rideItem.scenicRating);
                        }
                    }
                }
            }
        }
        catch(Exception e)
        {
            ShowError("showForm", e.getMessage());
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
        setContentView(R.layout.activity_ride_details_edit);

        actionBar = getSupportActionBar();
        dateUtils = new DateUtils(this);
        imageUtils = new ImageUtils(this);
        myColor = new MyColor(this);

        cbPicturePicked=(CheckBox)findViewById(R.id.picturePicked);
        imageViewSmall = (ImageView)findViewById(R.id.imageViewSmall);
        txtSchedName=(TextView)findViewById(R.id.txtSchedName);
        heartRating=(RatingBar)findViewById(R.id.rbHeartRating);
        scenicRating=(RatingBar)findViewById(R.id.rbScenicRating);
        thrillRating=(RatingBar)findViewById(R.id.rbThrillRating);

        showForm();
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }

    }

}
