package com.example.des.hp.myutils;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
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

public class DialogWithYesNoFragment extends DialogFragment
{
    private String title;
    private String message;
    private int imageIcon;
    public View.OnClickListener yesClick;
    public View.OnClickListener noClick;
    @SuppressLint("StaticFieldLeak")
    public static DialogWithYesNoFragment dialogWithYesNoFragment;
    private static FragmentTransaction fragmentTransaction;
    private String dialogTag;
    public Context context;

    public DialogWithYesNoFragment()
    {

    }

    private void ShowError(String argMessage)
    {
        myMessages().ShowError
                (
                        "Error in DialogWithYesNoFragment::" + "onCreateView",
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
        dialogWithYesNoFragment.context = context;

        if(argOnYesClick==null)
        {
          // default the yes click
          dialogWithYesNoFragment.yesClick = view -> dialogWithYesNoFragment.dismiss();
        }
        else
        {
          dialogWithYesNoFragment.yesClick = argOnYesClick;
        }

        if(argOnNoClick==null)
        {        
          // default the no click
          dialogWithYesNoFragment.noClick = view -> dialogWithYesNoFragment.dismiss();
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
            Button btnYes = v.findViewById(R.id.btnYes);
            btnYes.setOnClickListener(yesClick);

            Button btnNo = v.findViewById(R.id.btnNo);
            btnNo.setOnClickListener(noClick);
        }
        catch (Exception e)
        {
            ShowError(e.getMessage());
        }

        return v;
    }
}