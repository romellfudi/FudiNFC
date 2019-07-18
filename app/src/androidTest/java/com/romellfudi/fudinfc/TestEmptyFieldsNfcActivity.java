/*
 * TestEmptyFieldsNfcActivity.java
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

import com.google.android.apps.common.testing.ui.espresso.ViewInteraction;
import com.squareup.spoon.Spoon;

import be.appfoundry.nfc.app.MainActivity;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.typeText;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.doesNotExist;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;

/**
 * NfcLibrary by daneo
 * Created on 30/04/14.
 */
public class TestEmptyFieldsNfcActivity extends ActivityInstrumentationTestCase2<MainActivity> {

    public TestEmptyFieldsNfcActivity() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    public void testEmptyUriFieldNotDisplayingAfterClick() {
        makeEmpty(R.id.input_text_uri_target);
        emptyFieldNotDisplayingToast(R.id.btn_write_uri_nfc);
    }

    public void testEmptyGeoFieldNotDisplayingAfterClick() {
        makeEmpty(R.id.input_text_geo_lat_target);
        emptyFieldNotDisplayingToast(R.id.btn_write_geolocation_nfc);
    }

    public void testEmptyEmailFieldNotDisplayingAfterClick() {
        makeEmpty(R.id.input_text_email_target);
        emptyFieldNotDisplayingToast(R.id.btn_write_email_nfc);
    }

    public void testEmptySmsFieldNotDisplayingAfterClick() {
        makeEmpty(R.id.input_text_sms_target);
        emptyFieldNotDisplayingToast(R.id.btn_write_sms_nfc);
    }


    public void testEmptyBluetoothFieldNotDisplayingAfterClick() {
        final EditText editText = (EditText) getActivity().findViewById(R.id.input_text_bluetooth_address);
        editText.post(new Runnable() {
            @Override
            public void run() {
                editText.setText(null);
            }
        });
        emptyFieldNotDisplayingToast(R.id.btn_write_bluetooth_nfc);
    }

    public void testEmptyTelFieldNotDisplayingAfterClick() {
        makeEmpty(R.id.input_text_tel_target);
        emptyFieldNotDisplayingToast(R.id.btn_write_tel_nfc);
    }


    public void emptyFieldNotDisplayingToast(int id) {
        onView(withId(id)).perform(click());

        // Not able to check for Toast being displayed due to their async nature

        // onView(withText(R.string.no_input)).check(matches(isDisplayed()));

        checkProgressDialogNotShowing();
        makeScreenshot("end");
    }

    private ViewInteraction makeEmpty(int id) {
        makeScreenshot("init");
        return onView(withId(id)).perform(typeText(""));
    }

    private void makeScreenshot(String tag) {
        Spoon.screenshot(getActivity(), tag);
    }

    protected void checkProgressDialogNotShowing() {
        getInstrumentation().waitForIdleSync();
        onView(withText(getActivity().getString(R.string.progressdialog_waiting_for_tag))).check(doesNotExist());

    }
}
