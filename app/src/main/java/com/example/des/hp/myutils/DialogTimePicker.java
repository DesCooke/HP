package com.example.des.hp.myutils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TimePicker;
import android.widget.TextView;

import com.example.des.hp.R;

import static com.example.des.hp.myutils.MyApiSpecific.myApiSpecific;
import static com.example.des.hp.myutils.MyMessages.myMessages;

/**
 * * Created by Des on 27/10/2016.
 */

public class DialogTimePicker extends Dialog implements android.view.View.OnClickListener
{
    public TextView txtStartTime;
    public CheckBox chkTimeKnown;
    private TimePicker frmTimePicker;
    private CheckBox frmCheckBox;
    public int hour;
    public int minute;
    public boolean timeKnown;
    public String title;

    public DialogTimePicker(Activity a)
    {
        super(a);
    }

    private void ShowError(String argFunction, String argMessage)
    {
        myMessages().ShowError("Error in DialogTimePicker::" + argFunction, argMessage);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_time_picker);

            Button ok= findViewById(R.id.btnOk);
            frmTimePicker= findViewById(R.id.timePicker);
            frmCheckBox= findViewById(R.id.chkTimeKnown);
            TextView txtTitle= findViewById(R.id.txtTitle);

            setTime(hour, minute);
            frmCheckBox.setChecked(timeKnown);
            txtTitle.setText(title);
            ok.setOnClickListener(this);
            frmCheckBox.setOnClickListener(this);
            frmTimePicker.setIs24HourView(true);

            //Set a TimeChangedListener for TimePicker widget
            frmTimePicker.setOnTimeChangedListener((view, hourOfDay, minute) -> {
                //Display the new time to app interface
                if(myApiSpecific().GetHour(frmTimePicker) > 0 || myApiSpecific().GetMinute(frmTimePicker) > 0)
                {
                    frmCheckBox.setChecked(true);
                }
            });
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
    }

    private void timeKnownOnClick()
    {
        try
        {
            if(!frmCheckBox.isChecked())
            {
                setTime(0, 0);
            }
        }
        catch(Exception e)
        {
            ShowError("timeKnownOnClick", e.getMessage());
        }
    }

    private void setTimeText(int hour, int minute)
    {
        try
        {
            txtStartTime.setText(DateUtils.FormatTime(hour, minute));
        }
        catch(Exception e)
        {
            ShowError("setTimeText", e.getMessage());
        }
    }

    private void setTime(int hour, int minute)
    {
        try
        {
            setTimeText(hour, minute);

            myApiSpecific().SetHour(frmTimePicker, hour);
            myApiSpecific().SetMinute(frmTimePicker, minute);

            frmTimePicker.refreshDrawableState();
        }
        catch(Exception e)
        {
            ShowError("setTime", e.getMessage());
        }
    }

    @Override
    public void onClick(View v)
    {
        try
        {
            int id=v.getId();
            if(id==R.id.btnOk){
                if(!frmCheckBox.isChecked())
                {
                    setTimeText(0, 0);
                } else
                {
                    setTimeText(myApiSpecific().GetHour(frmTimePicker), myApiSpecific().GetMinute(frmTimePicker));
                }
                chkTimeKnown.setChecked(frmCheckBox.isChecked());
                dismiss();
            }
            if(id==R.id.chkTimeKnown){
                timeKnownOnClick();
            }
        }
        catch(Exception e)
        {
            ShowError("onClick", e.getMessage());
        }
    }

}
