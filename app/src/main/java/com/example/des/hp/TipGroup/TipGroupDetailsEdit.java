package com.example.des.hp.TipGroup;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.example.des.hp.myutils.*;
import com.example.des.hp.R;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
import static com.example.des.hp.myutils.MyMessages.myMessages;

public class TipGroupDetailsEdit extends TipGroupDetailsView implements View.OnClickListener
{

    //region Member variables
    public View.OnClickListener dwetOnOkClick;
    public DialogWithEditTextFragment dialogWithEditTextFragment;
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


            if(action != null && action.equals("add"))
            {
                txtTipGroupDescription.setText("");
                SetTitles("Add a Tip", "");
                grpMenuFile.setVisibility(View.GONE);
            }
            grpTipGroupDescription.setOnClickListener(this);
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
        try
        {
            switch(view.getId())
            {

                case R.id.grpTipGroupDescription:
                    pickTipGroupDescription(view);
                    break;

                case R.id.imageViewSmall:
                    pickImage(view);
                    break;
            }
        }
        catch(Exception e)
        {
            ShowError("onClick", e.getMessage());
        }

    }

    public void TipGroupDescriptionPicked(View view)
    {
        try
        {
            txtTipGroupDescription.setText(dialogWithEditTextFragment.getFinalText());

            dialogWithEditTextFragment.dismiss();
        }
        catch(Exception e)
        {
            ShowError("TipGroupDescriptionPicked", e.getMessage());
        }

    }

    // Create a YES onclick procedure
    public void pickTipGroupDescription(View view)
    {
        try
        {
            dwetOnOkClick=new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    TipGroupDescriptionPicked(view);
                }
            };


            dialogWithEditTextFragment=DialogWithEditTextFragment.newInstance(getFragmentManager(),     // for the transaction bit
                "hihi",            // unique name for this dialog type
                "Tip Group",    // form caption
                "Description",             // form message
                R.drawable.attachment, txtTipGroupDescription.getText().toString(),                // initial text
                dwetOnOkClick, this, false
            );

            dialogWithEditTextFragment.showIt();
        }
        catch(Exception e)
        {
            ShowError("pickTipGroupDescription", e.getMessage());
        }

    }
    //endregion

    //region Saving
    public void saveSchedule(View view)
    {
        try
        {
            myMessages().ShowMessageShort("Saving " + txtTipGroupDescription.getText().toString());

            tipGroupItem.tipGroupDescription=txtTipGroupDescription.getText().toString();

            tipGroupItem.tipGroupPicture="";
            if(internalImageFilename.length() > 0)
                tipGroupItem.tipGroupPicture=internalImageFilename;
            tipGroupItem.pictureAssigned=imageSet;
            tipGroupItem.pictureChanged=imageChanged;
            tipGroupItem.fileBitmap=null;
            if(imageSet)
                tipGroupItem.fileBitmap=((BitmapDrawable) imageView.getDrawable()).getBitmap();

            tipGroupItem.tipGroupNotes="";

            if(action.equals("add"))
            {
                MyInt myInt=new MyInt();

                tipGroupItem.holidayId=holidayId;

                if(!databaseAccess().getNextTipGroupId(holidayId, myInt))
                    return;
                tipGroupItem.tipGroupId=myInt.Value;

                if(!databaseAccess().getNextTipGroupSequenceNo(holidayId, myInt))
                    return;

                tipGroupItem.sequenceNo=myInt.Value;

                if(!databaseAccess().addTipGroupItem(tipGroupItem))
                    return;
            }

            if(action.equals("modify"))
            {
                if(!databaseAccess().updateTipGroupItem(tipGroupItem))
                    return;
            }

            finish();
        }
        catch(Exception e)
        {
            ShowError("saveTipGroup", e.getMessage());
        }

    }
    //endregion

}
