package com.example.des.hp.myutils;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.provider.Settings;

public class MyPermissions
{
    public static boolean AccessAllowed()
    {
        return (Environment.isExternalStorageManager());
    }

    public static void EnsureAccessToExternalDrive(Activity activity)
    {
        if (!Environment.isExternalStorageManager()) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
            activity.startActivity(intent);
        }
    }

}

