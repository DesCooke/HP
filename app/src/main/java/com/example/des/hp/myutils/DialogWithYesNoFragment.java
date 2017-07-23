package com.example.des.hp.myutils;

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

public class DialogWithYesNoFragment extends DialogFragment
{
    private String title;
    private String message;
    private int imageIcon;
    public View.OnClickListener yesClick;
    public View.OnClickListener noClick;
    public static DialogWithYesNoFragment dialogWithYesNoFragment;
    private static FragmentTransaction fragmentTransaction;
    private String dialogTag;
    public Context context;
    public MyMessages myMessages;

    public DialogWithYesNoFragment()
    {

    }

    private void ShowError(String argFunction, String argMessage)
    {
        myMessages.ShowError
                (
                        "Error in DialogWithYesNoFragment::" + argFunction,
                        argMessage
                );
    }
    public static DialogWithYesNoFragment newInstance
      (
        FragmentManager fm, 
        String tag,
        String argTitle,
        String argMessage,
        int argImageIcon,
        View.OnClickListener argOnYesClick,
        View.OnClickListener argOnNoClick,
        Context context
      )
    {
        // Look for an existing dialog with same tag
        // and close it
        fragmentTransaction = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag(tag);
        if (prev != null)
            fragmentTransaction.remove(prev);
        fragmentTransaction.addToBackStack(null);


        // let's create a new one - giving all the defaults
        dialogWithYesNoFragment = new DialogWithYesNoFragment();
        dialogWithYesNoFragment.dialogTag=tag;
        dialogWithYesNoFragment.context = context;
        dialogWithYesNoFragment.myMessages = new MyMessages(context);

        if(argOnYesClick==null)
        {
          // default the yes click
          dialogWithYesNoFragment.yesClick = new View.OnClickListener()
          {
              public void onClick(View view)
              {
                  dialogWithYesNoFragment.dismiss();
              }
          };
        }
        else
        {
          dialogWithYesNoFragment.yesClick = argOnYesClick;
        }

        if(argOnNoClick==null)
        {        
          // default the no click
          dialogWithYesNoFragment.noClick = new View.OnClickListener()
          {
              public void onClick(View view)
              {
                  dialogWithYesNoFragment.dismiss();
              }
          };
        }
        else
        {
          dialogWithYesNoFragment.noClick = argOnNoClick;
        }

        dialogWithYesNoFragment.imageIcon = argImageIcon;
        dialogWithYesNoFragment.title = argTitle;
        dialogWithYesNoFragment.message = argMessage;
        dialogWithYesNoFragment.dialogTag = tag;
        

        return dialogWithYesNoFragment;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.dialog_with_yes_no, container, false);
        try
        {
            View tv = v.findViewById(R.id.txtTitle);
            ((TextView) tv).setText(title);
            View tm = v.findViewById(R.id.txtMessage);
            ((TextView) tm).setText(message);
            if (imageIcon != -1) {
                View iv = v.findViewById(R.id.imageIcon);
                ((ImageView) iv).setImageResource(imageIcon);
            }

            // Watch for button clicks.
            Button btnYes = (Button) v.findViewById(R.id.btnYes);
            btnYes.setOnClickListener(yesClick);

            Button btnNo = (Button) v.findViewById(R.id.btnNo);
            btnNo.setOnClickListener(noClick);
        }
        catch (Exception e)
        {
            ShowError("onCreateView", e.getMessage());
        }

        return v;
    }
}