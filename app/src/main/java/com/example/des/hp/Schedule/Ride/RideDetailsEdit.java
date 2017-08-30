package com.example.des.hp.Schedule.Ride;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.example.des.hp.R;
import com.example.des.hp.myutils.*;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
import static com.example.des.hp.myutils.MyMessages.myMessages;

public class RideDetailsEdit extends RideDetailsView implements View.OnClickListener
{
    //region Member variables
    public DialogWithEditTextFragment dialogWithEditTextFragment;
    public View.OnClickListener dwetOnOkClick;
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

            heartRating.setIsIndicator(false);
            scenicRating.setIsIndicator(false);
            thrillRating.setIsIndicator(false);

            if(action != null && action.equals("add"))
            {
                grpMenuFile.setVisibility(View.GONE);
                txtSchedName.setText("");
                heartRating.setRating(Float.valueOf(getString(R.string.default_rating)));
                scenicRating.setRating(Float.valueOf(getString(R.string.default_rating)));
                thrillRating.setRating(Float.valueOf(getString(R.string.default_rating)));
            }

            grpSchedName.setOnClickListener(this);
            imageView.setOnClickListener(this);
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }

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
        switch(view.getId())
        {

            case R.id.grpSchedName:
                pickSchedName(view);
                break;

            case R.id.imageViewSmall:
                pickImage(view);
                break;
        }
    }
    //endregion

    //region Saving
    public void saveSchedule(View view)
    {
        try
        {
            myMessages().ShowMessageShort("Saving Schedule");

            MyInt myInt=new MyInt();

            scheduleItem.schedName=txtSchedName.getText().toString();

            scheduleItem.schedPicture="";
            if(internalImageFilename.length()>0)
                scheduleItem.schedPicture=internalImageFilename;
            scheduleItem.pictureAssigned=imageSet;
            scheduleItem.pictureChanged=imageChanged;
            scheduleItem.scheduleBitmap=null;
            if(imageSet)
                scheduleItem.scheduleBitmap=((BitmapDrawable) imageView.getDrawable()).getBitmap();

            scheduleItem.startTimeKnown=false;
            scheduleItem.startHour=0;
            scheduleItem.startMin=0;
            scheduleItem.endTimeKnown=false;
            scheduleItem.endHour=0;
            scheduleItem.endMin=0;
            scheduleItem.rideItem.heartRating=heartRating.getRating();
            scheduleItem.rideItem.scenicRating=scenicRating.getRating();
            scheduleItem.rideItem.thrillRating=thrillRating.getRating();

            if(action.equals("add"))
            {
                scheduleItem.holidayId=holidayId;
                scheduleItem.dayId=dayId;
                scheduleItem.attractionId=attractionId;
                scheduleItem.attractionAreaId=attractionAreaId;

                if(!databaseAccess().getNextScheduleId(holidayId, dayId, attractionId, attractionAreaId, myInt))
                    return;
                scheduleItem.scheduleId=myInt.Value;

                if(!databaseAccess().getNextScheduleSequenceNo(holidayId, dayId, attractionId, attractionAreaId, myInt))
                    return;
                scheduleItem.sequenceNo=myInt.Value;

                scheduleItem.schedType=getResources().getInteger(R.integer.schedule_type_ride);

                scheduleItem.rideItem.holidayId=holidayId;
                scheduleItem.rideItem.dayId=dayId;
                scheduleItem.rideItem.attractionId=attractionId;
                scheduleItem.rideItem.attractionAreaId=attractionAreaId;
                scheduleItem.rideItem.scheduleId=scheduleItem.scheduleId;

                if(!databaseAccess().addScheduleItem(scheduleItem))
                    return;
            }

            if(action.equals("edit"))
            {

                if(!databaseAccess().updateScheduleItem(scheduleItem))
                    return;
            }

            finish();
        }
        catch(Exception e)
        {
            ShowError("SaveSchedule", e.getMessage());
        }
    }
    //endregion

}
