package com.example.des.hp.myutils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MyPermissions
{
    public static boolean AllowedToUseManageStored(Activity activity)
    {
        int permission = ContextCompat.checkSelfPermission(activity, Manifest.permission.MANAGE_EXTERNAL_STORAGE);
        if (permission == PackageManager.PERMISSION_GRANTED)
        {
            return(true);
        }
        return(false);
    }

    public static void requestManageStored(Activity activity) {
        ActivityCompat.requestPermissions
                (
                        activity,
                        new String[]
                                {
                                        Manifest.permission.MANAGE_EXTERNAL_STORAGE
                                },
                        102);
    }

    public static boolean checkIfAlreadyhavePermission(Activity activity)
    {
        int read_permission = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int write_permission = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (read_permission == PackageManager.PERMISSION_GRANTED &&
            write_permission == PackageManager.PERMISSION_GRANTED)
        {
            return(true);
        }
        return(false);
    }

    public static void requestForSpecificPermission(Activity activity) {
        ActivityCompat.requestPermissions
                  (
                          activity,
                          new String[]
                                  {
                                          Manifest.permission.READ_EXTERNAL_STORAGE,
                                          Manifest.permission.WRITE_EXTERNAL_STORAGE
                                  },
                          101);
    }
}

