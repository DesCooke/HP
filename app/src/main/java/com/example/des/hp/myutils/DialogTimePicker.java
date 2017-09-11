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
        Activity activity=a;
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

            DateUtils dateUtils=new DateUtils(this.getContext());

            Button ok=(Button) findViewById(R.id.btnOk);
            frmTimePicker=(TimePicker) findViewById(R.id.timePicker);
            frmCheckBox=(CheckBox) findViewById(R.id.chkTimeKnown);
            TextView txtTitle=(TextView) findViewById(R.id.txtTitle);

            setTime(hour, minute);
            frmCheckBox.setChecked(timeKnown);
            txtTitle.setText(title);
            ok.setOnClickListener(this);
            frmCheckBox.setOnClickListener(this);
            frmTimePicker.setIs24HourView(true);

            //Set a TimeChangedListener for TimePicker widget
            frmTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener()
            {
                @Override
                public void onTimeChanged(TimePicker view, int hourOfDay, int minute)
                {
                    //Display the new time to app interface
                    if(frmTimePicker.getHour() > 0 || frmTimePicker.getMinute() > 0)
                    {
                        frmCheckBox.setChecked(true);
                    }
                }
            });
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
    }

    private void timeKnownOnClick(View view)
    {
        try
        {
            if(frmCheckBox.isChecked() == false)
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
            frmTimePicker.setHour(hour);
            frmTimePicker.setMinute(minute);
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
            switch(v.getId())
            {
                case R.id.btnOk:
                    if(frmCheckBox.isChecked() == false)
                    {
                        setTimeText(0, 0);
                    } else
                    {
                        setTimeText(frmTimePicker.getHour(), frmTimePicker.getMinute());
                    }
                    chkTimeKnown.setChecked(frmCheckBox.isChecked());
                    dismiss();
                    break;
                case R.id.chkTimeKnown:
                    timeKnownOnClick(v);
                    break;
                default:
                    break;
            }
        }
        catch(Exception e)
        {
            ShowError("onClick", e.getMessage());
        }
    }

}
