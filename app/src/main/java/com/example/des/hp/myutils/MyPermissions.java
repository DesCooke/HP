package com.example.des.hp.myutils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MyPermissions
{
    public static boolean AccessAllowed()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return (Environment.isExternalStorageManager());
        }
        return false;
    }

    public static boolean EnsureAccessToExternalDrive(Activity activity)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if(!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                activity.startActivity(intent);
            }
            else
            {
                return(true);
            }
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

