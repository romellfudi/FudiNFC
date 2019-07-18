/*
 * TestFilledFieldsNfcActivity.java
 * NfcLibrary project.
 *
 * Created by : Daneo van Overloop - 17/6/2014.
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 AppFoundry. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 */

package com.romellfudi.fudinfc;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers;
import com.squareup.spoon.Spoon;


import be.appfoundry.nfc.app.MainActivity;
import be.appfoundry.nfc.app.R;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.pressImeActionButton;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.typeText;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.RootMatchers.withDecorView;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDisplayed;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Is.is;

/**
 * NfcLibrary by daneo
 * Created on 14/04/14.
 */
public class TestFilledFieldsNfcActivity extends ActivityInstrumentationTestCase2<MainActivity> {

    private static final String TAG = TestFilledFieldsNfcActivity.class.getSimpleName();

    public TestFilledFieldsNfcActivity() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    public void testFilledUriFieldButtonClickShowsProgressDialog() throws Exception {
        //checkAndDismissBluetoothDialog();
        makeScreenshot("init");
        onView(ViewMatchers.withId(be.appfoundry.nfc.app.R.id.input_text_uri_target)).perform(typeText("google.be"));
        onView(ViewMatchers.withId(be.appfoundry.nfc.app.R.id.btn_write_uri_nfc)).perform(click());
        makeScreenshot("Showing_progressdialog");
        checkProgressDialogShowing();
        makeScreenshot("end");
    }

    public void testFilledGeoFieldButtonClickShowsProgressDialog() throws Exception {
        //checkAndDismissBluetoothDialog();
        makeScreenshot("init");
        onView(ViewMatchers.withId(be.appfoundry.nfc.app.R.id.input_text_geo_lat_target)).perform(typeText("50.2345323"));
        onView(ViewMatchers.withId(be.appfoundry.nfc.app.R.id.input_text_geo_long_target)).perform(typeText("50.2345323"));
        onView(ViewMatchers.withId(be.appfoundry.nfc.app.R.id.btn_write_geolocation_nfc)).perform(click());
        makeScreenshot("Showing_progressdialog");
        checkProgressDialogShowing();
        makeScreenshot("end");
    }

    public void testFilledEmailFieldButtonClickShowsProgressDialog() throws Exception {
        //checkAndDismissBluetoothDialog();
        makeScreenshot("init");
        onView(ViewMatchers.withId(be.appfoundry.nfc.app.R.id.input_text_email_target)).perform(typeText("daneo@derp.com"));
        onView(ViewMatchers.withId(be.appfoundry.nfc.app.R.id.btn_write_email_nfc)).perform(click());
        makeScreenshot("Showing_progressdialog");
        checkProgressDialogShowing();
        makeScreenshot("end");
    }

    public void testFilledSmsFieldButtonClickShowsProgressDialog() throws Exception {
        //checkAndDismissBluetoothDialog();
        makeScreenshot("init");
        onView(ViewMatchers.withId(be.appfoundry.nfc.app.R.id.input_text_sms_target)).perform(typeText("0123456789"));
        onView(ViewMatchers.withId(be.appfoundry.nfc.app.R.id.btn_write_sms_nfc)).perform(click());
        makeScreenshot("Showing_progressdialog");
        checkProgressDialogShowing();
        makeScreenshot("end");
    }

    //2131296269
    public void testFilledBluetoothMacAddressFieldButtonClickShowsProgressDialog() throws Exception {
        //checkAndDismissBluetoothDialog();
        makeScreenshot("init");
        final EditText text = (EditText) getActivity().findViewById(be.appfoundry.nfc.app.R.id.input_text_bluetooth_address);

        final String macAddress = "00:11:22:33:44";

        text.post(new Runnable() {
            @Override
            public void run() {
                text.setText(macAddress);
            }
        });

        makeScreenshot("Item_filled");
        onView(ViewMatchers.withId(be.appfoundry.nfc.app.R.id.input_text_bluetooth_address)).perform(click());


//        onView(withId(R.id.spinner_bluetooth_addresses)).perform(click(pressImeActionButton()));
        onView(ViewMatchers.withId(be.appfoundry.nfc.app.R.id.btn_write_bluetooth_nfc)).perform(click());
        makeScreenshot("Showing_progressdialog");
        checkProgressDialogShowing();
        makeScreenshot("end");
    }

    public void testFilledTelephoneFieldButtonClickShowsProgressDialog() throws Exception {
        //checkAndDismissBluetoothDialog();
        makeScreenshot("init");
        onView(ViewMatchers.withId(be.appfoundry.nfc.app.R.id.input_text_tel_target)).perform(typeText("0123456789"));
        onView(ViewMatchers.withId(be.appfoundry.nfc.app.R.id.btn_write_tel_nfc)).perform(click());
        checkProgressDialogShowing();
    }

    private void checkAndDismissBluetoothDialog() {
        onView(withText("Bluetooth"))
                .inRoot(withDecorView(not(is(getActivity().getWindow().getDecorView()))))
                .perform(click(pressImeActionButton()));
    }

    protected void checkProgressDialogShowing() {
        onView(withText(getActivity().getString(R.string.progressdialog_waiting_for_tag))).check(matches(isDisplayed()));
    }

    private void makeScreenshot(final String tag) {
        Spoon.screenshot(getActivity(),tag);
    }

}
