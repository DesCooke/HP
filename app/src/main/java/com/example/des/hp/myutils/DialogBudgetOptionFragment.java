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
import android.widget.TextView;

import com.example.des.hp.R;
import com.google.android.material.textfield.TextInputEditText;

import static com.example.des.hp.myutils.MyMessages.myMessages;

public class DialogBudgetOptionFragment extends DialogFragment
{
    public View.OnClickListener okClick;

    private String title;
    private String message;
    private String optionDescription;
    private int optionTotal;
    @SuppressLint("StaticFieldLeak")
    private static DialogBudgetOptionFragment dialogBudgetOptionFragment;
    private static String dialogTag;
    private static FragmentTransaction fragmentTransaction;
    public TextInputEditText tieOptionDescription;
    public TextInputEditText tieOptionTotal;
    public Context context;
    public MyKeyboard myKeyboard;
    public boolean numericKeypad;

    public DialogBudgetOptionFragment()
    {
    }

    private void ShowError(String argMessage)
    {
        myMessages().ShowError
                    (
                    "Error in DialogBudgetOptionFragment::" + "onCreateView",
                    argMessage
                    );
    }

    public static DialogBudgetOptionFragment newInstance
    (
    FragmentManager fm,
    String tag,
    String argTitle,
    String argMessage,
    String argOptionDescription,
    int argOptionTotal,
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
        dialogBudgetOptionFragment = new DialogBudgetOptionFragment();
        dialogBudgetOptionFragment.context = context;
        dialogBudgetOptionFragment.myKeyboard = new MyKeyboard();
        dialogBudgetOptionFragment.numericKeypad = numericKeypad;

        if(argOnOkClick==null)
        {
            // default the yes click
            dialogBudgetOptionFragment.okClick = view -> dialogBudgetOptionFragment.dismiss();
        }
        else
        {
            dialogBudgetOptionFragment.okClick = argOnOkClick;
        }

        dialogBudgetOptionFragment.title = argTitle;
        dialogBudgetOptionFragment.message = argMessage;
        dialogBudgetOptionFragment.optionDescription = argOptionDescription;
        dialogBudgetOptionFragment.optionTotal=argOptionTotal;

        return dialogBudgetOptionFragment;
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
        View v = inflater.inflate(R.layout.dialog_budget_option, container, false);
        try
        {

            View tv = v.findViewById(R.id.txtTitle);
            ((TextView) tv).setText(title);

            View tm = v.findViewById(R.id.txtMessage);
            ((TextView) tm).setText(message);

            tieOptionDescription = v.findViewById(R.id.tieOptionDescription);
            tieOptionDescription.setText(optionDescription);

            tieOptionTotal = v.findViewById(R.id.tieOptionTotal);
            tieOptionTotal.setText(StringUtils.IntToString(optionTotal));
            if(numericKeypad)
            {
                if (!myKeyboard.showNumeric(getDialog(), tieOptionDescription))
                    return v;
            }
            else
            {
                if (!myKeyboard.show(getDialog()))
                    return v;
            }

            // Watch for button clicks.
            Button btnOk = v.findViewById(R.id.btnOk);
            btnOk.setOnClickListener(okClick);
        }
        catch (Exception e)
        {
            ShowError(e.getMessage());
        }

        return v;
    }
}