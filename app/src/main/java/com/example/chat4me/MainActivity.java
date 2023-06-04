package com.example.chat4me;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.example.chat4me.messaging.SmsMessage;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.chat4me.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        /*implements ActivityCompat.OnRequestPermissionsResultCallback */{

    private static final String[] PERMISSIONS_REQUESTED = {
            Manifest.permission.READ_SMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.INTERNET
    };

    private static final int PERMISSION_SMS_READ = 0;
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private View mLayout;
    private FragmentManager fragmentManager;
    private SharedPreferences settings;

    boolean hasRequiredPermissions() {
        for(String permission: PERMISSIONS_REQUESTED) {
            if(ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void showPermissionsRequest() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,
            android.Manifest.permission.READ_SMS)) {
                Snackbar.make(mLayout, R.string.sms_read_permission_ask, Snackbar.LENGTH_LONG)
                    .setAction(R.string.ok,
                        v -> ActivityCompat.requestPermissions(MainActivity.this,
                                PERMISSIONS_REQUESTED, PERMISSION_SMS_READ)).show();
        } else {
            Snackbar.make(mLayout, R.string.sms_loading, Snackbar.LENGTH_SHORT).show();
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this,
                    PERMISSIONS_REQUESTED, PERMISSION_SMS_READ);
        }
    }

    private void goToFragment(int id) {
        NavHostFragment navHostFragment = ((NavHostFragment)fragmentManager
                .getPrimaryNavigationFragment());
        if(navHostFragment == null) {
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage(R.string.no_nav_host_error)
                    .setPositiveButton(R.string.ok, null)
                    .show();
        } else {
            navHostFragment.getNavController().navigate(id);
        }
    }

    private void showDisclaimerPrompt() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.disclaimer_title)
                .setMessage(R.string.disclaimer_message)
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> System.exit(0))
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                    settings.edit().putLong("id", System.currentTimeMillis()).apply();
                    showPermissionsRequest();
                })
                .show();
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentManager = getSupportFragmentManager();

        settings = getSharedPreferences("c4mprefs", 0);
        long id = settings.getLong("id", -1);
        if(id < 0) {
            showDisclaimerPrompt();
        } else {
            System.out.printf("User id %d\n", id);
        }
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        mLayout = findViewById(R.id.nav_host_fragment_content_main);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        navController.addOnDestinationChangedListener( (nc, destination, bundle) -> {
            int destID = destination.getId();
            if(destID == R.id.ConversationsFragment) {
                binding.fab.show();
            } else {
                binding.fab.hide();
            }
        });

        binding.fab.setOnClickListener(view ->
                goToFragment(R.id.ConversationViewFragment));
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
        if(id == R.id.action_settings) {
            goToFragment(R.id.SettingsFragment);
            return true;
        } else if(id == R.id.action_read_contacts) {

            return true;
        } else if(id == R.id.action_exit) {
            System.exit(0);
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