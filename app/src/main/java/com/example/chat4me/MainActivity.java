package com.example.chat4me;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.example.chat4me.messaging.SmsMessage;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.chat4me.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int PERMISSION_SMS_READ = 0;
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private View mLayout;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_SMS_READ) {
            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readSms();
            } else {
                // request was denied
                Snackbar.make(mLayout, "Permission denied :(",
                        Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private void readSms() {
        Cursor cur = getContentResolver().query(
            Uri.parse("content://sms/inbox"),
                null, null, null, null
        );
        String[] permissions = {"android.permission.READ_SMS"};

        if(cur.moveToFirst()) {
            System.out.println("Starting to read messages...");
            SmsMessage msg;
            System.out.printf("Found %d threads\n", cur.getCount());
            do {
                msg = SmsMessage.readFromCursor(cur);
                if(msg.getBody().contains("Kaed")) {
                    System.out.printf("Address: %s\n", msg.getAddress());
                }
            } while(cur.moveToNext());
            System.out.println("Done reading messages");
        } else {
            // no messages
            System.out.println("No messages");
            cur.close();
            return;
        }
        cur.close();
    }

    private void showSmsPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,
            android.Manifest.permission.READ_SMS)) {
                Snackbar.make(mLayout, R.string.sms_permission_ask, Snackbar.LENGTH_LONG)
                    .setAction("OK",
                        v -> ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_SMS},
                            PERMISSION_SMS_READ)).show();
        } else {
            Snackbar.make(mLayout, R.string.sms_loading, Snackbar.LENGTH_SHORT).show();
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_SMS}, PERMISSION_SMS_READ);
        }
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        mLayout = findViewById(R.id.nav_host_fragment_content_main);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            showSmsPermission();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}