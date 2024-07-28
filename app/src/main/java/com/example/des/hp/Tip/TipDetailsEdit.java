package com.example.des.hp.Tip;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.myutils.*;
import com.example.des.hp.R;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
import static com.example.des.hp.myutils.MyMessages.myMessages;

public class TipDetailsEdit extends TipDetailsView implements View.OnClickListener
{

    //region Member variables
    public View.OnClickListener dwetOnOkClick;
    public DialogWithEditTextFragment dialogWithEditTextFragment;
    public DialogWithMultiEditTextFragment dialogWithMultiEditTextFragment;
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
                txtTipDescription.setText("");
                txtTipNotes.setText("");
                title = "Add a Tip";
            }

            grpTipDescription.setOnClickListener(this);
            txtTipNotes.setOnClickListener(this);
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
    public void TipDescriptionPicked(View view)
    {
        try
        {
            txtTipDescription.setText(dialogWithEditTextFragment.getFinalText());

            dialogWithEditTextFragment.dismiss();
        }
        catch(Exception e)
        {
            ShowError("TipDescriptionPicked", e.getMessage());
        }

    }

    // Create a YES onclick procedure
    public void pickTipDescription(View view)
    {
        try
        {
            dwetOnOkClick= this::TipDescriptionPicked;


            dialogWithEditTextFragment=DialogWithEditTextFragment.newInstance(getSupportFragmentManager(),     // for the transaction bit
                "hihi",            // unique name for this dialog type
                "A New Tip",    // form caption
                "Description",             // form message
                R.drawable.attachment, txtTipDescription.getText().toString(),                // initial text
                dwetOnOkClick, this, false
            );

            dialogWithEditTextFragment.showIt();
        }
        catch(Exception e)
        {
            ShowError("pickTipDescription", e.getMessage());
        }

    }

    public void TipNotesPicked(View view)
    {
        try
        {
            txtTipNotes.setText(dialogWithMultiEditTextFragment.getFinalText());

            dialogWithMultiEditTextFragment.dismiss();
        }
        catch(Exception e)
        {
            ShowError("TipNotesPicked", e.getMessage());
        }

    }

    // Create a YES onclick procedure
    public void onClick(View view)
    {
        try {
            int id = view.getId();
            if (id == R.id.grpTipDescription)
                pickTipDescription(view);

            if (id == R.id.txtTipNotes)
                pickTipNotes(view);

            if (id == R.id.imageViewSmall)
                pickImage(view);
        }
        catch(Exception e)
        {
            ShowError("onClick", e.getMessage());
        }

    }

    public void pickTipNotes(View view)
    {
        try
        {
            dwetOnOkClick= this::TipNotesPicked;


            dialogWithMultiEditTextFragment=DialogWithMultiEditTextFragment.newInstance(getSupportFragmentManager(),     // for the transaction bit
                "hjhj",            // unique name for this dialog type
                "TIP Group Notes",    // form caption
                    // form message
                    txtTipNotes.getText().toString(),                // initial text
                dwetOnOkClick, this
            );


            dialogWithMultiEditTextFragment.showIt();
        }
        catch(Exception e)
        {
            ShowError("pickTipNotes", e.getMessage());
        }

    }
    //endregion

    //region Saving
    public void saveSchedule(View view)
    {
        try(DatabaseAccess da = databaseAccess())
        {
            myMessages().ShowMessageShort("Saving " + txtTipDescription.getText().toString());

            tipItem.tipDescription=txtTipDescription.getText().toString();
            tipItem.tipNotes=txtTipNotes.getText().toString();

            tipItem.tipPicture="";
            if(!internalImageFilename.isEmpty())
                tipItem.tipPicture=internalImageFilename;
            tipItem.pictureAssigned=imageSet;
            tipItem.pictureChanged=imageChanged;
            tipItem.fileBitmap=null;
            if(imageSet)
                tipItem.fileBitmap=((BitmapDrawable) imageView.getDrawable()).getBitmap();


            if(action.equals("add")) {
                MyInt myInt = new MyInt();

                tipItem.holidayId = holidayId;
                tipItem.tipGroupId = tipGroupId;

                if (!da.getNextTipId(holidayId, tipGroupId, myInt))
                    return;
                tipItem.tipId = myInt.Value;

                if (!da.getNextTipSequenceNo(holidayId, tipGroupId, myInt))
                    return;
                tipItem.sequenceNo = myInt.Value;

                if (!da.addTipItem(tipItem))
                    return;
            }

            if(action.equals("modify"))
            {
                if(!da.updateTipItem(tipItem))
                    return;
            }

            finish();
        }
        catch(Exception e)
        {
            ShowError("saveTip", e.getMessage());
        }

    }

    public void setNoteId(int pNoteId)
    {
        try
        {
            tipItem.noteId = pNoteId;
            try (DatabaseAccess da = databaseAccess()) {
                da.updateTipItem(tipItem);
            }
        }
        catch(Exception e)
        {
            ShowError("setNoteId", e.getMessage());
        }

    }

    @Override
    public void setInfoId(int pInfoId)
    {
        try
        {
            tipItem.infoId=pInfoId;
            try(DatabaseAccess da = databaseAccess())
            {
                da.updateTipItem(tipItem);
            }
        }
        catch(Exception e)
        {
            ShowError("setInfoId", e.getMessage());
        }

    }



}
