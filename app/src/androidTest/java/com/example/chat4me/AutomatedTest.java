package com.example.chat4me;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import androidx.test.filters.SdkSuppress;
import androidx.test.platform.app.InstrumentationRegistry;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.Until;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class AutomatedTest {
    private static final int LAUNCH_TIMEOUT = 5000;
    private static final String MAIN_PKG = "com.example.chat4me";
    private UiDevice device;

    @Before
    public void startMainActivityFromHome() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        device.pressHome();

        // wait for launcher
        final String launcherPackage = device.getLauncherPackageName();
        device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);

        // launch Chat 4 Me
        Context context = getApplicationContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(MAIN_PKG);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        context.startActivity(intent);

        device.wait(Until.hasObject(By.pkg(MAIN_PKG)
                .depth(0)), LAUNCH_TIMEOUT);
    }

    @Test
    public void doTest() {
        Assert.assertNotNull(device);
        //Assert.assertNotNull(getLauncherPackageName());
    }

}
