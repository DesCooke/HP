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
import android.widget.EditText;
import android.widget.TextView;

import com.example.des.hp.R;

import static com.example.des.hp.myutils.MyMessages.myMessages;

public class DialogWithMultiEditTextFragment extends DialogFragment
{
    public View.OnClickListener okClick;

    private String title;
    @SuppressLint("StaticFieldLeak")
    private static DialogWithMultiEditTextFragment dialogWithMultiEditTextFragment;
    private static String dialogTag;
    private static FragmentTransaction fragmentTransaction;
    private EditText editText;
    private String initialText;
    public Context context;
    public MyKeyboard myKeyboard;

    public DialogWithMultiEditTextFragment()
    {

    }

    public String getFinalText()
    {
        if(editText == null)
            return ("");

        return (editText.getText().toString());
    }

    private void ShowError(String argMessage)
    {
        myMessages().ShowError("Error in DialogWithMultiEditTextFragment::" + "onCreateView", argMessage);
    }

    public static DialogWithMultiEditTextFragment newInstance(FragmentManager fm, String tag, String argTitle, String argInitialText, View.OnClickListener argOnOkClick, Context context
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
        dialogWithMultiEditTextFragment=new DialogWithMultiEditTextFragment();
        dialogWithMultiEditTextFragment.context=context;
        dialogWithMultiEditTextFragment.myKeyboard=new MyKeyboard();


        if(argOnOkClick == null)
        {
            // default the yes click
            dialogWithMultiEditTextFragment.okClick= view -> dialogWithMultiEditTextFragment.dismiss();
        } else
        {
            dialogWithMultiEditTextFragment.okClick=argOnOkClick;
        }

        dialogWithMultiEditTextFragment.title=argTitle;
        dialogWithMultiEditTextFragment.initialText=argInitialText;

        return dialogWithMultiEditTextFragment;
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
        View v=inflater.inflate(R.layout.dialog_with_multiedittext, container, false);

        try
        {
            View tv=v.findViewById(R.id.txtTitle);
            ((TextView) tv).setText(title);

            editText= v.findViewById(R.id.editText);
            editText.setText(initialText);
            editText.requestFocus();

            if(!myKeyboard.show(getDialog()))
                return v;


            // Watch for button clicks.
            Button btnOk= v.findViewById(R.id.btnOk);
            btnOk.setOnClickListener(okClick);
        }
        catch(Exception e)
        {
            ShowError(e.getMessage());
        }

        return v;
    }
}