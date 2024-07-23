package com.example.des.hp.Schedule.GeneralAttraction;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.des.hp.Budget.BudgetDetailsList;
import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Holiday.HolidayItem;
import com.example.des.hp.Notes.NoteItem;
import com.example.des.hp.R;
import com.example.des.hp.Schedule.*;
import com.example.des.hp.Tasks.TaskDetailsList;
import com.example.des.hp.TipGroup.TipGroupDetailsList;

public class GeneralAttractionDetailsView extends BaseScheduleView
{

    public String holidayName;
    public RatingBar heartRating;
    public RatingBar scenicRating;
    public RatingBar thrillRating;
    public TextView txtAttractionType;
    public TextView txtBookingReference;
    public TextView txtFlightNo;
    public TextView txtTerminal;
    public RadioButton radUnknown;
    public RadioButton radWalkIn;
    public RadioButton radOnTheDay;
    public RadioButton rad180Days;
    public RadioButton radBooked;
    public TextView txtStart;
    public TextView txtEnd;
    public TextView txtNotes;
    public CheckBox chkStartKnown;
    public CheckBox chkEndKnown;
    public LinearLayout grpAttractionType;
    public LinearLayout grpBookingReference;
    public LinearLayout grpFlightNo;
    public LinearLayout grpTerminal;
    public LinearLayout grpReservationTypeId;
    public LinearLayout grpStartTime;
    public LinearLayout grpEndTime;
    public LinearLayout grpNotes;
    public ImageView btnEdit;

    public ImageButton btnClear;
    public Button btnSave;

    public NoteItem noteItem;
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

            noteItem = new NoteItem();

            grpAttractionType = findViewById(R.id.grpAttractionType);
            grpBookingReference = findViewById(R.id.grpBookingReference);
            grpFlightNo = findViewById(R.id.grpFlightNo);
            grpTerminal = findViewById(R.id.grpTerminal);
            grpStartTime = findViewById(R.id.grpStartTime);
            grpEndTime = findViewById(R.id.grpEndTime);

            grpReservationTypeId = findViewById(R.id.grpReservationTypeId);

            txtSchedName = findViewById(R.id.txtSchedName);
            heartRating = findViewById(R.id.rbHeartRatingView);
            scenicRating = findViewById(R.id.rbScenicRatingView);
            thrillRating = findViewById(R.id.rbThrillRatingView);
            txtAttractionType = findViewById(R.id.txtAttractionType);
            txtBookingReference = findViewById(R.id.txtBookingReference);
            txtFlightNo = findViewById(R.id.txtFlightNo);
            txtStart = findViewById(R.id.txtStart);
            txtEnd = findViewById(R.id.txtEnd);
            txtTerminal = findViewById(R.id.txtTerminal);
            txtNotes = findViewById(R.id.txtNotes);
            radUnknown = findViewById(R.id.radUnknown);
            radWalkIn = findViewById(R.id.radWalkIn);
            radOnTheDay = findViewById(R.id.radOnTheDay);
            rad180Days = findViewById(R.id.rad180Days);
            radBooked = findViewById(R.id.radBooked);
            chkStartKnown = findViewById(R.id.chkStartKnown);
            chkEndKnown = findViewById(R.id.chkEndKnown);
            grpNotes = findViewById(R.id.grpNotes);

            btnClear= findViewById(R.id.btnClear);
            btnSave= findViewById(R.id.btnSave);
            btnEdit=findViewById(R.id.my_toolbar_edit);
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editSchedule(GeneralAttractionDetailsEdit.class);
                }
            });

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
        try(DatabaseAccess da = databaseAccess())
        {
            if(action != null)
            {
                if(action.equals("view")) {
                    ShowToolbarEdit();
                    viewOnlyForm = true;
                }
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


            int lNoteId = scheduleItem.noteId;
            if(lNoteId != 0)
            {
                if(da.getNoteItem(holidayId, lNoteId, noteItem))
                {
                    txtNotes.setText(noteItem.notes);
                    grpNotes.setVisibility(View.VISIBLE);
                }
                else
                {
                    grpNotes.setVisibility(View.GONE);
                }
            }
            else
            {
                grpNotes.setVisibility(View.GONE);
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

            HolidayItem holidayItem = new HolidayItem();
            da.getHolidayItem(holidayId, holidayItem);

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
            if(viewOnlyForm && scheduleItem.schedName.isEmpty())
                grpSchedName.setVisibility(View.GONE);

            txtAttractionType.setText(scheduleItem.generalAttractionItem.AttractionType);
            grpAttractionType.setVisibility(View.VISIBLE);
            if(viewOnlyForm && scheduleItem.generalAttractionItem.AttractionType.isEmpty())
                grpAttractionType.setVisibility(View.GONE);

            txtBookingReference.setText(scheduleItem.generalAttractionItem.BookingReference);
            grpBookingReference.setVisibility(View.VISIBLE);
            if(viewOnlyForm && scheduleItem.generalAttractionItem.BookingReference.isEmpty())
                grpBookingReference.setVisibility(View.GONE);

            txtFlightNo.setText(scheduleItem.generalAttractionItem.FlightNo);
            grpFlightNo.setVisibility(View.VISIBLE);
            if(viewOnlyForm && scheduleItem.generalAttractionItem.FlightNo.isEmpty())
                grpFlightNo.setVisibility(View.GONE);

            txtTerminal.setText(scheduleItem.generalAttractionItem.Terminal);
            grpTerminal.setVisibility(View.VISIBLE);
            if(viewOnlyForm && scheduleItem.generalAttractionItem.Terminal.isEmpty())
                grpTerminal.setVisibility(View.GONE);

            chkStartKnown.setChecked(scheduleItem.startTimeKnown);
            setTimeText(txtStart, scheduleItem.startHour, scheduleItem.startMin);
            grpStartTime.setVisibility(View.VISIBLE);
            if(viewOnlyForm && !scheduleItem.startTimeKnown)
                grpStartTime.setVisibility(View.GONE);

            chkEndKnown.setChecked(scheduleItem.endTimeKnown);
            setTimeText(txtEnd, scheduleItem.endHour, scheduleItem.endMin);
            grpEndTime.setVisibility(View.VISIBLE);
            if(viewOnlyForm && !scheduleItem.endTimeKnown)
                grpEndTime.setVisibility(View.GONE);

            afterShow();

        }
        catch (Exception e)
        {
            ShowError("showForm", e.getMessage());
        }
    }

    public void showBudget()
    {
        try
        {
            Intent intent2=new Intent(getApplicationContext(), BudgetDetailsList.class);
            intent2.putExtra("HOLIDAYID", scheduleItem.holidayId);
            intent2.putExtra("TITLE", holidayName);
            intent2.putExtra("SUBTITLE", "Budget");
            startActivity(intent2);
        }
        catch(Exception e)
        {
            ShowError("showBudget", e.getMessage());
        }
    }

    public void showTipGroups()
    {
        try
        {
            Intent intent2=new Intent(getApplicationContext(), TipGroupDetailsList.class);
            intent2.putExtra("HOLIDAYID", scheduleItem.holidayId);
            intent2.putExtra("TITLE", holidayName);
            intent2.putExtra("SUBTITLE", "Tips");
            startActivity(intent2);
        }
        catch(Exception e)
        {
            ShowError("showTipGroups", e.getMessage());
        }
    }

    public void showTasks()
    {
        try
        {
            Intent intent2=new Intent(getApplicationContext(), TaskDetailsList.class);
            intent2.putExtra("HOLIDAYID", scheduleItem.holidayId);
            intent2.putExtra("TITLE", holidayName);
            intent2.putExtra("SUBTITLE", "Tasks");
            startActivity(intent2);
        }
        catch(Exception e)
        {
            ShowError("showTasks", e.getMessage());
        }
    }


    //endregion

    //region OnClick Events
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        try {
            int id = item.getItemId();
            if (id == R.id.action_delete_generalattraction)
                deleteSchedule();
            if (id == R.id.action_edit_generalattraction)
                editSchedule(GeneralAttractionDetailsEdit.class);
            if (id == R.id.action_move)
                move();
            if (id == R.id.action_task)
                showTasks();
            if (id == R.id.action_budget)
                showBudget();
            if (id == R.id.action_tips)
                showTipGroups();
        }
        catch (Exception e)
        {
            ShowError("onOptionsItemSelected", e.getMessage());
        }
        return true;
    }
    //endregion

    @Override
    protected void onResume()
    {
        super.onResume();
        try
        {
            try(DatabaseAccess da = databaseAccess();)
            {
                if(!da.getScheduleItem(holidayId, dayId, attractionId, attractionAreaId, scheduleId, scheduleItem))
                    return;
                if(scheduleItem.scheduleId==0)
                    finish();
            }
        }
        catch(Exception e)
        {
            ShowError("onResume", e.getMessage());
        }

    }

}
