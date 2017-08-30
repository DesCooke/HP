package com.example.des.hp.Tip;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

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
            
            if (action != null && action.equals("add"))
            {
                grpMenuFile.setVisibility(View.GONE);
                txtTipDescription.setText("");
                txtTipNotes.setText("");
            }
            grpTipDescription.setOnClickListener(this);
            txtTipNotes.setOnClickListener(this);
            imageView.setOnClickListener(this);
        }
        catch (Exception e)
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
        catch (Exception e)
        {
            ShowError("TipDescriptionPicked", e.getMessage());
        }
        
    }
    
    // Create a YES onclick procedure
    public void pickTipDescription(View view)
    {
        try
        {
            dwetOnOkClick = new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    TipDescriptionPicked(view);
                }
            };
            
            
            dialogWithEditTextFragment = DialogWithEditTextFragment.newInstance(getFragmentManager(),     // for the transaction bit
                "hihi",            // unique name for this dialog type
                "A New Tip",    // form caption
                "Description",             // form message
                R.drawable.attachment, txtTipDescription.getText().toString(),                // initial text
                dwetOnOkClick, this, false
            );
            
            dialogWithEditTextFragment.showIt();
        }
        catch (Exception e)
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
        catch (Exception e)
        {
            ShowError("TipNotesPicked", e.getMessage());
        }
        
    }
    
    // Create a YES onclick procedure
    public void onClick(View view)
    {
        switch (view.getId())
        {
            
            case R.id.grpTipDescription:
                pickTipDescription(view);
                break;
            
            case R.id.txtTipNotes:
                pickTipNotes(view);
                break;
            
            case R.id.imageViewSmall:
                pickImage(view);
                break;
        }
    }
    public void pickTipNotes(View view)
    {
        try
        {
            dwetOnOkClick = new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    TipNotesPicked(view);
                }
            };
            
            
            dialogWithMultiEditTextFragment = DialogWithMultiEditTextFragment.newInstance(getFragmentManager(),     // for the transaction bit
                "hjhj",            // unique name for this dialog type
                "TIP Group Notes",    // form caption
                "Notes",             // form message
                R.drawable.attachment, txtTipNotes.getText().toString(),                // initial text
                dwetOnOkClick, this
            );
            
            
            dialogWithMultiEditTextFragment.showIt();
        }
        catch (Exception e)
        {
            ShowError("pickTipNotes", e.getMessage());
        }
        
    }
    //endregion
    
    //region Saving
    public void saveSchedule(View view)
    {
        try
        {
            myMessages().ShowMessageShort("Saving " + txtTipDescription.getText().toString());
            
            tipItem.tipDescription = txtTipDescription.getText().toString();
            tipItem.tipNotes = txtTipNotes.getText().toString();

            tipItem.tipPicture="";
            if(internalImageFilename.length()>0)
                tipItem.tipPicture=internalImageFilename;
            tipItem.pictureAssigned = imageSet;
            tipItem.pictureChanged = imageChanged;
            tipItem.fileBitmap = null;
            if (imageSet)
                tipItem.fileBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            

            if (action.equals("add"))
            {
                MyInt myInt = new MyInt();
                
                tipItem.holidayId = holidayId;
                tipItem.tipGroupId = tipGroupId;
                
                if (!databaseAccess().getNextTipId(holidayId, tipGroupId, myInt))
                    return;
                tipItem.tipId = myInt.Value;
                
                if (!databaseAccess().getNextTipSequenceNo(holidayId, tipGroupId, myInt))
                    return;
                tipItem.sequenceNo = myInt.Value;
                
                if (!databaseAccess().addTipItem(tipItem))
                    return;
            }
            
            if (action.equals("modify"))
            {
                if (!databaseAccess().updateTipItem(tipItem))
                    return;
            }
            
            finish();
        }
        catch (Exception e)
        {
            ShowError("saveTip", e.getMessage());
        }
        
    }
    
}
