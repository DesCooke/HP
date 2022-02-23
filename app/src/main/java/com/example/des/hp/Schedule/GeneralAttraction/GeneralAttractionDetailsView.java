package com.example.des.hp.Schedule.GeneralAttraction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;
import com.example.des.hp.R;
import com.example.des.hp.Schedule.*;

public class GeneralAttractionDetailsView extends BaseScheduleView
{

    //region Member variables
    public LinearLayout grpStartDate;
    public String holidayName;
    public RatingBar heartRating;
    public RatingBar scenicRating;
    public RatingBar thrillRating;
    public TextView txtAttractionType;
    public TextView txtBookingReference;
    public TextView txtFlightNo;
    public TextView txtDeparts;
    public TextView txtTerminal;
    public RadioButton radUnknown;
    public RadioButton radWalkIn;
    public RadioButton radOnTheDay;
    public RadioButton rad180Days;
    public RadioButton radBooked;
    public TextView txtShow;
    public TextView txtPickUp;
    public TextView txtDropOff;
    public TextView txtCheckIn;
    public TextView txtArrival;
    public LinearLayout grpAttractionType;
    public LinearLayout grpBookingReference;
    public LinearLayout grpFlightNo;
    public LinearLayout grpTerminal;
    public CheckBox chkDepartsKnown;
    public CheckBox chkShowKnown;
    public CheckBox chkPickUpKnown;
    public CheckBox chkDropOffKnown;
    public CheckBox chkCheckInKnown;
    public CheckBox chkArrivalKnown;
    public LinearLayout grpDeparts;
    public LinearLayout grpShow;
    public LinearLayout grpPickUp;
    public LinearLayout grpDropOff;
    public LinearLayout grpCheckIn;
    public LinearLayout grpArrival;
    public LinearLayout grpReservationTypeId;

    public ImageButton btnClear;
    public Button btnSave;
    //endregion

    //region Constructors/Destructors
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        scheduleTypeDescription = getString(R.string.schedule_desc_generalattraction);

        super.onCreate(savedInstanceState);

        try
        {
            layoutName="activity_generalattraction_details_view";
            setContentView(R.layout.activity_generalattraction_details_view);

            grpAttractionType = (LinearLayout) findViewById(R.id.grpAttractionType);
            grpBookingReference = (LinearLayout) findViewById(R.id.grpBookingReference);
            grpFlightNo = (LinearLayout) findViewById(R.id.grpFlightNo);
            grpTerminal = (LinearLayout) findViewById(R.id.grpTerminal);
            grpDeparts = (LinearLayout) findViewById(R.id.grpDeparts);
            grpShow = (LinearLayout) findViewById(R.id.grpShow);
            grpPickUp = (LinearLayout) findViewById(R.id.grpPickUp);
            grpDropOff = (LinearLayout) findViewById(R.id.grpDropOff);
            grpCheckIn = (LinearLayout) findViewById(R.id.grpCheckIn);
            grpArrival = (LinearLayout) findViewById(R.id.grpArrival);
            grpReservationTypeId = (LinearLayout) findViewById(R.id.grpReservationTypeId);

            txtSchedName = (TextView) findViewById(R.id.txtSchedName);
            heartRating = (RatingBar) findViewById(R.id.rbHeartRatingView);
            scenicRating = (RatingBar) findViewById(R.id.rbScenicRatingView);
            thrillRating = (RatingBar) findViewById(R.id.rbThrillRatingView);
            txtAttractionType = (TextView) findViewById(R.id.txtAttractionType);
            txtBookingReference = (TextView) findViewById(R.id.txtBookingReference);
            txtFlightNo = (TextView) findViewById(R.id.txtFlightNo);
            txtDeparts = (TextView) findViewById(R.id.txtDeparts);
            txtTerminal = (TextView) findViewById(R.id.txtTerminal);
            radUnknown = (RadioButton) findViewById(R.id.radUnknown);
            radWalkIn = (RadioButton) findViewById(R.id.radWalkIn);
            radOnTheDay = (RadioButton) findViewById(R.id.radOnTheDay);
            rad180Days = (RadioButton) findViewById(R.id.rad180Days);
            radBooked = (RadioButton) findViewById(R.id.radBooked);
            txtShow = (TextView) findViewById(R.id.txtShow);
            txtPickUp = (TextView) findViewById(R.id.txtPickUp);
            txtDropOff = (TextView) findViewById(R.id.txtDropOff);
            txtCheckIn = (TextView) findViewById(R.id.txtCheckIn);
            txtArrival = (TextView) findViewById(R.id.txtArrival);
            chkDepartsKnown = (CheckBox) findViewById(R.id.chkDepartsKnown);
            chkShowKnown = (CheckBox) findViewById(R.id.chkShowKnown);
            chkPickUpKnown = (CheckBox) findViewById(R.id.chkPickUpKnown);
            chkDropOffKnown = (CheckBox) findViewById(R.id.chkDropOffKnown);
            chkCheckInKnown = (CheckBox) findViewById(R.id.chkCheckInKnown);
            chkArrivalKnown = (CheckBox) findViewById(R.id.chkArrivalKnown);

            btnClear=(ImageButton) findViewById(R.id.btnClear);
            btnSave=(Button) findViewById(R.id.btnSave);

            afterCreate();

            showForm();
        }
        catch (Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }

    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        try
        {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.generalattractiondetailsformmenu, menu);
        }
        catch (Exception e)
        {
            ShowError("onCreateOptionsMenu", e.getMessage());
        }

        return true;
    }
    //endregion

    //region Regular Form Activities
    public void showForm()
    {
        super.showForm();
        boolean viewOnlyForm=false;
        try
        {
            if(action != null)
            {
                if(action.equals("view"))
                    viewOnlyForm=true;
                if(action.equals("add"))
                    if(scheduleItem.generalAttractionItem == null)
                        scheduleItem.generalAttractionItem=new GeneralAttractionItem();

                if(!action.equals("add"))
                {
                    heartRating.setRating(scheduleItem.generalAttractionItem.heartRating);
                    thrillRating.setRating(scheduleItem.generalAttractionItem.thrillRating);
                    scenicRating.setRating(scheduleItem.generalAttractionItem.scenicRating);
                }
            }

            heartRating.setVisibility(View.VISIBLE);
            if(viewOnlyForm && scheduleItem.generalAttractionItem.heartRating < 0.25)
                heartRating.setVisibility(View.GONE);
            thrillRating.setVisibility(View.VISIBLE);
            if(viewOnlyForm && scheduleItem.generalAttractionItem.thrillRating < 0.25)
                thrillRating.setVisibility(View.GONE);
            scenicRating.setVisibility(View.VISIBLE);
            if(viewOnlyForm && scheduleItem.generalAttractionItem.scenicRating < 0.25)
                scenicRating.setVisibility(View.GONE);

            radUnknown.setChecked(false);
            radWalkIn.setChecked(false);
            radOnTheDay.setChecked(false);
            rad180Days.setChecked(false);
            radBooked.setChecked(false);
            grpReservationTypeId.setVisibility(View.VISIBLE);
            if(scheduleItem.generalAttractionItem.ReservationType == 0)
            {
                radUnknown.setChecked(true);
                if(viewOnlyForm)
                    grpReservationTypeId.setVisibility(View.GONE);
            }
            if(scheduleItem.generalAttractionItem.ReservationType == 1)
                radWalkIn.setChecked(true);
            if(scheduleItem.generalAttractionItem.ReservationType == 2)
                radOnTheDay.setChecked(true);
            if(scheduleItem.generalAttractionItem.ReservationType == 3)
                rad180Days.setChecked(true);
            if(scheduleItem.generalAttractionItem.ReservationType == 4)
                radBooked.setChecked(true);

            txtSchedName.setText(scheduleItem.schedName);
            grpSchedName.setVisibility(View.VISIBLE);
            if(viewOnlyForm && scheduleItem.schedName.length()==0)
                grpSchedName.setVisibility(View.GONE);

            txtAttractionType.setText(scheduleItem.generalAttractionItem.AttractionType);
            grpAttractionType.setVisibility(View.VISIBLE);
            if(viewOnlyForm && scheduleItem.generalAttractionItem.AttractionType.length()==0)
                grpAttractionType.setVisibility(View.GONE);

            txtBookingReference.setText(scheduleItem.generalAttractionItem.BookingReference);
            grpBookingReference.setVisibility(View.VISIBLE);
            if(viewOnlyForm && scheduleItem.generalAttractionItem.BookingReference.length()==0)
                grpBookingReference.setVisibility(View.GONE);

            txtFlightNo.setText(scheduleItem.generalAttractionItem.FlightNo);
            grpFlightNo.setVisibility(View.VISIBLE);
            if(viewOnlyForm && scheduleItem.generalAttractionItem.FlightNo.length()==0)
                grpFlightNo.setVisibility(View.GONE);

            txtTerminal.setText(scheduleItem.generalAttractionItem.Terminal);
            grpTerminal.setVisibility(View.VISIBLE);
            if(viewOnlyForm && scheduleItem.generalAttractionItem.Terminal.length()==0)
                grpTerminal.setVisibility(View.GONE);

            chkDepartsKnown.setChecked(scheduleItem.generalAttractionItem.DepartsKnown);
            setTimeText(txtDeparts, scheduleItem.generalAttractionItem.DepartsHour, scheduleItem.generalAttractionItem.DepartsMin);
            grpDeparts.setVisibility(View.VISIBLE);
            if(viewOnlyForm && scheduleItem.generalAttractionItem.DepartsKnown==false)
                grpDeparts.setVisibility(View.GONE);

            chkShowKnown.setChecked(scheduleItem.generalAttractionItem.ShowKnown);
            setTimeText(txtShow, scheduleItem.generalAttractionItem.ShowHour, scheduleItem.generalAttractionItem.ShowMin);
            grpShow.setVisibility(View.VISIBLE);
            if(viewOnlyForm && scheduleItem.generalAttractionItem.ShowKnown==false)
                grpShow.setVisibility(View.GONE);

            chkPickUpKnown.setChecked(scheduleItem.generalAttractionItem.PickUpKnown);
            setTimeText(txtPickUp, scheduleItem.generalAttractionItem.PickUpHour, scheduleItem.generalAttractionItem.PickUpMin);
            grpPickUp.setVisibility(View.VISIBLE);
            if(viewOnlyForm && scheduleItem.generalAttractionItem.PickUpKnown==false)
                grpPickUp.setVisibility(View.GONE);

            chkDropOffKnown.setChecked(scheduleItem.generalAttractionItem.DropOffKnown);
            setTimeText(txtDropOff, scheduleItem.generalAttractionItem.DropOffHour, scheduleItem.generalAttractionItem.DropOffMin);
            grpDropOff.setVisibility(View.VISIBLE);
            if(viewOnlyForm && scheduleItem.generalAttractionItem.DropOffKnown==false)
                grpDropOff.setVisibility(View.GONE);

            chkCheckInKnown.setChecked(scheduleItem.generalAttractionItem.CheckInKnown);
            setTimeText(txtCheckIn, scheduleItem.generalAttractionItem.CheckInHour, scheduleItem.generalAttractionItem.CheckInMin);
            grpCheckIn.setVisibility(View.VISIBLE);
            if(viewOnlyForm && scheduleItem.generalAttractionItem.CheckInKnown==false)
                grpCheckIn.setVisibility(View.GONE);

            chkArrivalKnown.setChecked(scheduleItem.generalAttractionItem.ArrivalKnown);
            setTimeText(txtArrival, scheduleItem.generalAttractionItem.ArrivalHour, scheduleItem.generalAttractionItem.ArrivalMin);
            grpArrival.setVisibility(View.VISIBLE);
            if(viewOnlyForm && scheduleItem.generalAttractionItem.ArrivalKnown==false)
                grpArrival.setVisibility(View.GONE);

            afterShow();

        }
        catch (Exception e)
        {
            ShowError("showForm", e.getMessage());
        }
    }
    //endregion

    //region OnClick Events
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        try
        {
            switch (item.getItemId())
            {
                case R.id.action_delete_generalattraction:
                    deleteSchedule();
                    return true;
                case R.id.action_edit_generalattraction:
                    editSchedule(GeneralAttractionDetailsEdit.class);
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
        return true;
    }
    //endregion

}
