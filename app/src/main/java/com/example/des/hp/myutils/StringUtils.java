package com.example.des.hp.myutils;

import java.util.Locale;

public class StringUtils
{
    public static String IntToMoneyString(int argInt)
    {
        return(String.format(Locale.ENGLISH, "£%d", argInt));
    }
    public static String StringToMoneyString(String argString)
    {
        String lString;

        lString="0";
        if(argString.length()>0)
            lString = argString;

        int lInt;
        try
        {
            lInt = Integer.parseInt(lString);
        }
        catch(Exception e)
        {
            lInt = 0;
        }

        return(StringUtils.IntToMoneyString(lInt));
    }
}
