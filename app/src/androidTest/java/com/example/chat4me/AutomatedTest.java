package com.example.chat4me;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import androidx.test.filters.SdkSuppress;
import androidx.test.internal.platform.content.PermissionGranter;
import androidx.test.platform.app.InstrumentationRegistry;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.Until;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class AutomatedTest {
    private static final int LAUNCH_TIMEOUT = 5000;
    private static final String MAIN_PKG = "com.example.chat4me";
    private UiDevice device;

    @Before
    public void startMainActivityFromHome() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        Assert.assertNotNull(device);
        device.pressHome();
        // wait for launcher
        final String launcherPackage = device.getLauncherPackageName();
        device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);

        // launch Chat 4 Me
        Context context = getApplicationContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(MAIN_PKG);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        device.wait(Until.hasObject(By.pkg(MAIN_PKG)
                .depth(0)), LAUNCH_TIMEOUT);
    }

    @Test
    public void promptVisibleTest() {
        UiObject2 alertTitle = device.findObject(By.text("First-use disclaimer"));
        Assert.assertNotNull(alertTitle);

        UiObject2 promptOK = device.findObject(By.clazz("android.widget.Button")
                .text("OK").clickable(true));
        Assert.assertNotNull(promptOK);

        promptOK.click();
        device.wait(Until.gone(By.text("First-use disclaimer")), 500);

        UiObject2 allowBtn = device.wait(
                Until.findObject(By.clickable(true).text("Allow")),
                500);
        Assert.assertNotNull(allowBtn);
        allowBtn.click();

        // wait until permission dialog is gone after clicking Allow
        device.wait(Until.gone(By.clickable(true).text("Allow")),
                500);
    }

    @Test
    public void openSettingsTest() {
        promptVisibleTest();
        UiObject2 toolbar = device.findObject(
                By.res("com.example.chat4me", "toolbar"));
        Assert.assertNotNull(toolbar);

        UiObject2 menuBtn = toolbar.findObject(
                By.desc("More options").clickable(true));
        Assert.assertNotNull(menuBtn);
        menuBtn.click();
        menuBtn.recycle();

        UiObject2 settingsItem = device.wait(Until.findObject(
                By.clickable(true).hasDescendant(By.text("Settings"))),
                    500);
        Assert.assertNotNull(settingsItem);
        settingsItem.click();
        settingsItem.recycle();

        // confirm that the main toolbar text changed from "Conversations" to "Settings"
        UiObject2 title = toolbar.wait(Until.findObject(By.text("Settings")), 500);
        Assert.assertNotNull(title);
    }
}
