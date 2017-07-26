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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.des.hp.R;

import static com.example.des.hp.myutils.MyMessages.myMessages;

public class DialogWithEditTextFragment extends DialogFragment
{
    public View.OnClickListener okClick;

    private String title;
    private String message;
    private int imageIcon;
    private static DialogWithEditTextFragment dialogWithEditTextFragment;
    private static String dialogTag;
    private static FragmentTransaction fragmentTransaction;
    private EditText editText;
    private String initialText;
    public Context context;
    public MyKeyboard myKeyboard;
    public boolean numericKeypad;

    public DialogWithEditTextFragment()
    {
    }

    public String getFinalText()
    {
      if(editText == null)
        return("");

      return(editText.getText().toString());
    }

    private void ShowError(String argFunction, String argMessage)
    {
        myMessages().ShowError
                (
                        "Error in DialogWithEditTextFragment::" + argFunction,
                        argMessage
                );
    }

    public static DialogWithEditTextFragment newInstance
      (
        FragmentManager fm, 
        String tag,
        String argTitle,
        String argMessage,
        int argImageIcon,
        String argInitialText,
        View.OnClickListener argOnOkClick,
        Context context,
        boolean numericKeypad
      )
    {
        // Look for an existing dialog with same tag
        // and close it

        dialogTag = tag;
        fragmentTransaction = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag(tag);
        if (prev != null)
            fragmentTransaction.remove(prev);
        fragmentTransaction.addToBackStack(null);


        // let's create a new one - giving all the defaults
        dialogWithEditTextFragment = new DialogWithEditTextFragment();
        dialogWithEditTextFragment.context = context;
        dialogWithEditTextFragment.myKeyboard = new MyKeyboard(context);
        dialogWithEditTextFragment.numericKeypad = numericKeypad;

        if(argOnOkClick==null)
        {
          // default the yes click
          dialogWithEditTextFragment.okClick = new View.OnClickListener()
          {
              public void onClick(View view)
              {
                  dialogWithEditTextFragment.dismiss();
              }
          };
        }
        else
        {
          dialogWithEditTextFragment.okClick = argOnOkClick;
        }

        dialogWithEditTextFragment.imageIcon = -1;
        dialogWithEditTextFragment.title = argTitle;
        dialogWithEditTextFragment.message = argMessage;
        dialogWithEditTextFragment.imageIcon = argImageIcon;
        dialogWithEditTextFragment.initialText = argInitialText;

        return dialogWithEditTextFragment;
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
        View v = inflater.inflate(R.layout.dialog_with_edittext, container, false);
        try
        {

            View tv = v.findViewById(R.id.txtTitle);
            ((TextView) tv).setText(title);

            View tm = v.findViewById(R.id.txtMessage);
            ((TextView) tm).setText(message);

            editText = (EditText) v.findViewById(R.id.editText);
            editText.setText(initialText);
            editText.selectAll();

            if(numericKeypad)
            {
                if (myKeyboard.showNumeric(getDialog(), editText) == false)
                    return v;
            }
            else
            {
                if (myKeyboard.show(getDialog()) == false)
                    return v;
            }


            if (imageIcon != -1)
            {
                View iv = v.findViewById(R.id.imageIcon);
                ((ImageView) iv).setImageResource(imageIcon);
            }

            // Watch for button clicks.
            Button btnOk = (Button) v.findViewById(R.id.btnOk);
            btnOk.setOnClickListener(okClick);
        }
        catch (Exception e)
        {
            ShowError("onCreateView", e.getMessage());
        }

        return v;
    }
}