package com.romellfudi.fudinfc.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.FormatException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.romellfudi.fudinfc.gear.NfcAct;
import com.romellfudi.fudinfc.util.exceptions.InsufficientCapacityException;
import com.romellfudi.fudinfc.util.exceptions.ReadOnlyTagException;
import com.romellfudi.fudinfc.util.exceptions.TagNotPresentException;
import com.romellfudi.fudinfc.gear.interfaces.OpCallback;
import com.romellfudi.fudinfc.gear.interfaces.TaskCallback;
import com.romellfudi.fudinfc.util.async.WriteCallbackNfc;
import com.romellfudi.fudinfc.util.interfaces.NfcReadUtility;
import com.romellfudi.fudinfc.util.interfaces.NfcWriteUtility;
import com.romellfudi.fudinfc.util.sync.NfcReadUtilityImpl;

public class MainActivity extends NfcAct {

    private static final String TAG = MainActivity.class.getName();

    NfcReadUtility mNfcReadUtility = new NfcReadUtilityImpl();
    ProgressDialog mProgressDialog;

    TaskCallback mTaskCallback = new TaskCallback() {
        @Override
        public void onReturn(Boolean result) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            if (result) {
                Toast.makeText(MainActivity.this, "Write has been done!", Toast.LENGTH_LONG).show();
            }

            Log.d(TAG, "Received our result : " + result);

        }

        @Override
        public void onProgressUpdate(Boolean... values) {
            if (values.length > 0 && values[0] && mProgressDialog != null) {
                mProgressDialog.setMessage("Writing");
                Log.d(TAG, "Writing !");
            }
        }

        @Override
        public void onError(Exception e) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            Log.i(TAG, "Encountered an error !", e);
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    OpCallback mOpCallback;
    private AsyncTask<Object, Void, Boolean> mTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button emailButton = (Button) findViewById(R.id.btn_write_email_nfc);
        Button smsButton = (Button) findViewById(R.id.btn_write_sms_nfc);
        Button geoButton = (Button) findViewById(R.id.btn_write_geolocation_nfc);
        Button uriButton = (Button) findViewById(R.id.btn_write_uri_nfc);
        Button telButton = (Button) findViewById(R.id.btn_write_tel_nfc);
        Button bluetoothButton = (Button) findViewById(R.id.btn_write_bluetooth_nfc);
        if (emailButton != null) {
            emailButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final EditText emailText = ((EditText) retrieveElement(R.id.input_text_email_target));
                    if (emailText != null) {
                        final String text = emailText.getText().toString();
                        mOpCallback = new OpCallback() {
                            @Override
                            public boolean performWrite(NfcWriteUtility writeUtility) throws ReadOnlyTagException, InsufficientCapacityException, TagNotPresentException, FormatException {
                                return writeUtility.writeEmailToTagFromIntent(text, null, null, getIntent());
                            }
                        };
                        showDialog();
                    }
                }
            });
        }

        if (smsButton != null) {
            smsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText smsText = (EditText) retrieveElement(R.id.input_text_sms_target);
                    if (smsText != null) {
                        final String text = smsText.getText().toString();
                        mOpCallback = new OpCallback() {
                            @Override
                            public boolean performWrite(NfcWriteUtility writeUtility) throws ReadOnlyTagException, InsufficientCapacityException, TagNotPresentException, FormatException {
                                return writeUtility.writeSmsToTagFromIntent(text, null, getIntent());
                            }
                        };
                        showDialog();
                    }
                }
            });
        }

        if (geoButton != null) {
            geoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText latitudeText = (EditText) retrieveElement(R.id.input_text_geo_lat_target);
                    EditText longitudeText = (EditText) retrieveElement(R.id.input_text_geo_long_target);
                    if (latitudeText != null && longitudeText != null) {

                        final double longitude = Double.parseDouble(latitudeText.getText().toString());
                        final double latitude = Double.parseDouble(longitudeText.getText().toString());

                        mOpCallback = new OpCallback() {
                            @Override
                            public boolean performWrite(NfcWriteUtility writeUtility) throws ReadOnlyTagException, InsufficientCapacityException, TagNotPresentException, FormatException {
                                return writeUtility.writeGeolocationToTagFromIntent(latitude, longitude, getIntent());
                            }
                        };
                        showDialog();
                    }
                }
            });
        }
        if (uriButton != null) {
            uriButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final TextView uriText = retrieveElement(R.id.input_text_uri_target);
                    if (uriText != null) {
                        final String text = uriText.getText().toString();
                        mOpCallback = new OpCallback() {
                            @Override
                            public boolean performWrite(NfcWriteUtility writeUtility) throws ReadOnlyTagException, InsufficientCapacityException, TagNotPresentException, FormatException {
                                return writeUtility.writeUriToTagFromIntent(text, getIntent());
                            }
                        };
                        showDialog();
                    }

                }
            });
        }
        if (telButton != null) {
            telButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final TextView telText = retrieveElement(R.id.input_text_tel_target);
                    if (telText != null) {
                        final String text = telText.getText().toString();
                        mOpCallback = new OpCallback() {
                            @Override
                            public boolean performWrite(NfcWriteUtility writeUtility) throws ReadOnlyTagException, InsufficientCapacityException, TagNotPresentException, FormatException {
                                return writeUtility.writeTelToTagFromIntent(text, getIntent());
                            }
                        };
                        showDialog();
                    } else {
                        showNoInputToast();
                    }
                }
            });
        }
        if (bluetoothButton != null) {
            bluetoothButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    EditText bluetoothInput = (EditText) findViewById(R.id.input_text_bluetooth_address);
                    if (bluetoothInput != null) {
                        final String text = bluetoothInput.getText().toString();
                        mOpCallback = new OpCallback() {
                            @Override
                            public boolean performWrite(NfcWriteUtility writeUtility) throws ReadOnlyTagException, InsufficientCapacityException, TagNotPresentException, FormatException {
                                return writeUtility.writeBluetoothAddressToTagFromIntent(text, getIntent());
                            }
                        };
                        showDialog();
                    }
                }
            });
        }


        enableBeam();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (getNfcAdapter() != null) {
            getNfcAdapter().disableForegroundDispatch(this);
        }
    }

    /**
     * Launched when in foreground dispatch mode
     *
     * @param paramIntent containing found data
     */
    @Override
    public void onNewIntent(final Intent paramIntent) {
        super.onNewIntent(paramIntent);

        if (mOpCallback != null && mProgressDialog != null && mProgressDialog.isShowing()) {
            new WriteCallbackNfc(mTaskCallback, mOpCallback).executeWriteOperation();
            mOpCallback = null;
        } else {
            for (String data : mNfcReadUtility.readFromTagWithMap(paramIntent).values()) {
                Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showNoInputToast() {
        Toast.makeText(this, getString(R.string.no_input), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
        if (mTask != null) {
            mTask.cancel(true);
            mTask = null;
        }
    }

    public void showDialog() {
        mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setTitle(R.string.progressdialog_waiting_for_tag);
        mProgressDialog.setMessage(getString(R.string.progressdialog_waiting_for_tag_message));
        mProgressDialog.show();
    }

    private TextView retrieveElement(int id) {
        TextView element = (TextView) findViewById(id);
        return (element != null) && ((TextView) findViewById(id)).getText() != null && !"".equals(((TextView) findViewById(id)).getText().toString()) ? element : null;
    }

}
