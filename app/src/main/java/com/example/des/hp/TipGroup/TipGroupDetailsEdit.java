package com.example.des.hp.TipGroup;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;

import com.example.des.hp.Database.DatabaseAccess;
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
                SetToolbarTitles("Add a Tip", "");
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

    //endregion

    //region OnClick Events
    public void onClick(View view)
    {
        try {
            int id = view.getId();
            if (id == R.id.grpTipGroupDescription)
                pickTipGroupDescription(view);

            if (id == R.id.imageViewSmall)
                pickImage(view);
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
            dwetOnOkClick= this::TipGroupDescriptionPicked;


            dialogWithEditTextFragment=DialogWithEditTextFragment.newInstance(getSupportFragmentManager(),     // for the transaction bit
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
        try(DatabaseAccess da = databaseAccess())
        {
            myMessages().ShowMessageShort("Saving " + txtTipGroupDescription.getText().toString());

            tipGroupItem.tipGroupDescription=txtTipGroupDescription.getText().toString();

            tipGroupItem.tipGroupPicture="";
            if(!internalImageFilename.isEmpty())
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

                if(!da.getNextTipGroupId(holidayId, myInt))
                    return;
                tipGroupItem.tipGroupId=myInt.Value;

                if(!da.getNextTipGroupSequenceNo(holidayId, myInt))
                    return;

                tipGroupItem.sequenceNo=myInt.Value;

                if(!da.addTipGroupItem(tipGroupItem))
                    return;
            }

            if(action.equals("modify"))
            {
                if(!da.updateTipGroupItem(tipGroupItem))
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
