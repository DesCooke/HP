package com.example.des.hp.myutils;

/*
** Usage

    //region create some member variables
    public DialogWithTextViewFragment dialogWithTextViewFragment;
    public String dwtvDialogTag;
    public View.OnClickListener dwtvOnOkClick;
    //endregion


    // Create an onclick procedure
    public void dwtvOnOkClickProc(View view)
    {
        // getFinalText contains the text on the form
        MyMessages.ShowMessageLong(context, "user clicked ok:" +
          dialogWithEditTextFragment.getFinalText();

        // When button is clicked close the dialog
        dialogWithTextViewFragment.dismiss();
    }


    put the tag text on constants.xml
    <string name="dwtvDialogTag">dwtvDialogTag</string>


    // in the onCreate function - retrieve tag contents
    dwtvDialogTag = getResources().getString(R.string.dwtvDialogTag);


    When you want to show the form
    :  :  :
    // create an on-click listener to call your procedure
    dwtvOnOkClick = new View.OnClickListener()
    {
      public void onClick(View view)
      {
        dwtvOnOkClickProc(view);
      }
    };

    dialogWithTextViewFragment =
      DialogWithTextViewFragment.newInstance
        (
          getFragmentManager(),     // for the transaction bit
          dwtvDialogTag,            // unique name for this dialog type
          "Title",                  // form caption
          "Message",                // form message
          R.drawable.airplane,      // form icon -1 for default
          dwtvOnOkClick             // onclick listener or null for default
        );

    dialogWithTextViewFragment.showIt();
*/


import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.des.hp.R;

import static com.example.des.hp.myutils.MyMessages.myMessages;

public class DialogWithTextViewFragment extends DialogFragment
{
    public View.OnClickListener okClick;

    private String title;
    private String message;
    private int imageIcon;
    private static DialogWithTextViewFragment dialogWithTextViewFragment;
    private static String dialogTag;
    private static FragmentTransaction fragmentTransaction;
    public Context context;

    public DialogWithTextViewFragment()
    {

    }

    private void ShowError(String argFunction, String argMessage)
    {
        myMessages().ShowError("Error in DialogWithTextViewFragment::" + argFunction, argMessage);
    }

    public static DialogWithTextViewFragment newInstance(FragmentManager fm, String tag, String argTitle, String argMessage, int argImageIcon, View.OnClickListener argOnOkClick, Context context
    )
    {
        // Look for an existing dialog with same tag
        // and close it
        dialogTag=tag;
        fragmentTransaction=fm.beginTransaction();
        Fragment prev=fm.findFragmentByTag(tag);
        if(prev != null)
            fragmentTransaction.remove(prev);
        fragmentTransaction.addToBackStack(null);


        // let's create a new one - giving all the defaults
        dialogWithTextViewFragment=new DialogWithTextViewFragment();
        dialogWithTextViewFragment.context=context;

        if(argOnOkClick == null)
        {
            // default the yes click
            dialogWithTextViewFragment.okClick=new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    dialogWithTextViewFragment.dismiss();
                }
            };
        } else
        {
            dialogWithTextViewFragment.okClick=argOnOkClick;
        }

        dialogWithTextViewFragment.imageIcon=-1;
        dialogWithTextViewFragment.title=argTitle;
        dialogWithTextViewFragment.message=argMessage;
        dialogWithTextViewFragment.imageIcon=argImageIcon;

        return dialogWithTextViewFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    public void showIt()
    {
        show(fragmentTransaction, dialogTag);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v=inflater.inflate(R.layout.dialog_with_textview, container, false);
        try
        {

            View tv=v.findViewById(R.id.txtTitle);
            ((TextView) tv).setText(title);

            View tm=v.findViewById(R.id.txtMessage);
            ((TextView) tm).setText(message);

            if(imageIcon != -1)
            {
                View iv=v.findViewById(R.id.imageIcon);
                ((ImageView) iv).setImageResource(imageIcon);
            }

            // Watch for button clicks.
            Button btnOk=(Button) v.findViewById(R.id.btnOk);
            btnOk.setOnClickListener(okClick);
        }
        catch(Exception e)
        {
            ShowError("onCreateView", e.getMessage());
        }

        return v;
    }
}