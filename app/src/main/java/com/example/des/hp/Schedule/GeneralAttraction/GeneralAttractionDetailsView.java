package com.example.des.hp.Schedule.GeneralAttraction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

            txtSchedName = (TextView) findViewById(R.id.txtSchedName);
            heartRating = (RatingBar) findViewById(R.id.rbHeartRatingView);
            scenicRating = (RatingBar) findViewById(R.id.rbScenicRatingView);
            thrillRating = (RatingBar) findViewById(R.id.rbThrillRatingView);
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
        try
        {
            if(action != null)
            {
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

            txtSchedName.setText(scheduleItem.schedName);

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
