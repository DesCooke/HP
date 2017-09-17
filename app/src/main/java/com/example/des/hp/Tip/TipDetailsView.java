package com.example.des.hp.Tip;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.R;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

public class TipDetailsView extends BaseActivity
{

    //region Member variables
    public TipItem tipItem;
    public TextView txtTipDescription;
    public TextView txtTipNotes;
    public ImageButton btnClear;
    public Button btnSave;
    public LinearLayout grpTipDescription;
    public LinearLayout grpMenuFile;
    //endregion

    //region Constructors/Destructors
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            layoutName="activity_tip_details_view";
            setContentView(R.layout.activity_tip_details_view);

            txtTipDescription=(TextView) findViewById(R.id.txtTipDescription);
            txtTipNotes=(TextView) findViewById(R.id.txtTipNotes);
            btnClear=(ImageButton) findViewById(R.id.btnClear);
            btnSave=(Button) findViewById(R.id.btnSave);
            grpTipDescription=(LinearLayout) findViewById(R.id.grpTipDescription);
            grpMenuFile=(LinearLayout) findViewById(R.id.grpMenuFile);

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
            inflater.inflate(R.menu.tipdetailsformmenu, menu);
        }
        catch(Exception e)
        {
            ShowError("onCreateOptionsMenu", e.getMessage());
        }

        return true;
    }
    //endregion

    //region OnClick Events
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        try
        {
            switch(item.getItemId())
            {
                case R.id.action_delete_tip:
                    deleteTip();
                    return true;
                case R.id.action_edit_tip:
                    editTip();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
        catch(Exception e)
        {
            ShowError("onOptionsItemSelected", e.getMessage());
        }
        return true;
    }
    //endregion

    //region showForm
    public void showForm()
    {
        super.showForm();
        try
        {
            tipItem=new TipItem();
            if(!databaseAccess().getTipItem(holidayId, tipGroupId, tipId, tipItem))
                return;

            if(title == null || (title.length() == 0))
            {
                SetTitles(tipItem.tipDescription, "");
            } else
            {
                SetTitles(title, subTitle);
            }

            SetImage(tipItem.tipPicture);
            txtTipDescription.setText(tipItem.tipDescription);

            txtTipNotes.setText(tipItem.tipNotes);

            afterShow();
        }
        catch(Exception e)
        {
            ShowError("showForm", e.getMessage());
        }
    }

    //endregion

    //region form Functions
    @Override
    public int getInfoId()
    {
        try
        {
            return (tipItem.infoId);
        }
        catch(Exception e)
        {
            ShowError("getInfoId", e.getMessage());
        }
        return (0);
    }

    public void setNoteId(int pNoteId)
    {
        try
        {
            tipItem.noteId=pNoteId;
            databaseAccess().updateTipItem(tipItem);
        }
        catch(Exception e)
        {
            ShowError("setNoteId", e.getMessage());
        }

    }

    @Override
    public int getNoteId()
    {
        try
        {
            return (tipItem.noteId);
        }
        catch(Exception e)
        {
            ShowError("getNoteId", e.getMessage());
        }
        return (0);
    }

    @Override
    public void setInfoId(int pInfoId)
    {
        try
        {
            tipItem.infoId=pInfoId;
            databaseAccess().updateTipItem(tipItem);
        }
        catch(Exception e)
        {
            ShowError("setInfoId", e.getMessage());
        }

    }


    public void editTip()
    {
        try
        {
            Intent intent=new Intent(getApplicationContext(), TipDetailsEdit.class);
            intent.putExtra("ACTION", "modify");
            intent.putExtra("HOLIDAYID", holidayId);
            intent.putExtra("TIPGROUPID", tipGroupId);
            intent.putExtra("TIPID", tipId);
            intent.putExtra("TITLE", title);
            intent.putExtra("SUBTITLE", subTitle);
            startActivity(intent);
        }
        catch(Exception e)
        {
            ShowError("editTip", e.getMessage());
        }

    }

    public void deleteTip()
    {
        try
        {
            if(!databaseAccess().deleteTipItem(tipItem))
                return;
            finish();
        }
        catch(Exception e)
        {
            ShowError("deleteTip", e.getMessage());
        }

    }
    //endregion

}
