package com.example.chat4me;

import android.Manifest;
import android.app.Activity;
import android.view.View;

public class PermissionsHandler {
    public static final String[] PERMISSIONS_REQUESTED = {
        Manifest.permission.READ_SMS,
        Manifest.permission.RECEIVE_SMS,
        Manifest.permission.WAKE_LOCK,
        Manifest.permission.SEND_SMS,
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.INTERNET,
        Manifest.permission.ACTIVITY_RECOGNITION
    };
    public static final int PERMISSION_SMS_READ = 0;
    private View pView;
    private Activity pActivity;
    public PermissionsHandler(Activity activity, View view) {
        pActivity = activity;
        pView = view;
    }

    public void setView(View view) {
        pView = view;
    }
    public void setActivity(Activity activity) {
        pActivity = activity;
    }
}
