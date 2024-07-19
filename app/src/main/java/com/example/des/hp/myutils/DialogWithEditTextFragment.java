package com.example.des.hp.myutils;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.Fragment;
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
        DialogWithEditTextFragment new1 = new DialogWithEditTextFragment();
        new1.context = context;
        new1.myKeyboard = new MyKeyboard();
        new1.numericKeypad = numericKeypad;

        if(argOnOkClick==null)
        {
          // default the yes click
            new1.okClick = view -> new1.dismiss();
        }
        else
        {
            new1.okClick = argOnOkClick;
        }

        new1.title = argTitle;
        new1.message = argMessage;
        new1.imageIcon = argImageIcon;
        new1.initialText = argInitialText;

        return new1;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    public void showIt()
    {
        try
        {
            show(fragmentTransaction, dialogTag);
        }
        catch (Exception e)
        {
            ShowError("showIt", e.getMessage());
        }

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

            editText = v.findViewById(R.id.editText);
            editText.setText(initialText);
            editText.selectAll();

            if(numericKeypad)
            {
                if (!myKeyboard.showNumeric(getDialog(), editText))
                    return v;
            }
            else
            {
                if (!myKeyboard.show(getDialog()))
                    return v;
            }


            if (imageIcon != -1)
            {
                View iv = v.findViewById(R.id.imageIcon);
                ((ImageView) iv).setImageResource(imageIcon);
            }

            // Watch for button clicks.
            Button btnOk = v.findViewById(R.id.btnOk);
            btnOk.setOnClickListener(okClick);
        }
        catch (Exception e)
        {
            ShowError("onCreateView", e.getMessage());
        }

        return v;
    }

}