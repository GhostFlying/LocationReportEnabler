package com.ghostflying.locationreportenabler;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.ghostflying.locationreportenabler.views.FunctionChooseAlertView;


public class MainActivity extends Activity {
    private static final int MY_PERMISSIONS_REQUEST = 100;
    private FunctionChooseAlertView mAlertView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
    }

    private AlertDialog.Builder getFunctionDialogBuilder() {

        if (mAlertView == null) {
            mAlertView = new FunctionChooseAlertView(this);
        }

        return new AlertDialog.Builder(this)
                .setTitle(R.string.function_choice_dialog_title)
                .setView(mAlertView)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        MainActivity.this.finish();
                    }
                })
                .setPositiveButton(R.string.function_choice_dialog_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences preferences = getSharedPreferences(PropUtil.PREFERENCE_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        String numeric = mAlertView.getOperatorNumber();
                        String country = mAlertView.getOperatorCountry();

                        editor.putString(PropUtil.PREFERENCE_FAKE_NUMERIC, numeric);
                        editor.putString(PropUtil.PREFERENCE_FAKE_COUNTRY, country);
                        editor.apply();

                        PropUtil.applyFunctions(mAlertView.getSelectedFunctions(), numeric, country);
                        finish();
                    }
                });
    }

    private AlertDialog.Builder getNoticeDialogBuilder() {
        return new AlertDialog.Builder(this)
                .setTitle(R.string.notice_dialog_title)
                .setMessage(R.string.notice_dialog_message)
                .setPositiveButton(R.string.notice_dialog_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.READ_PHONE_STATE},
                                MY_PERMISSIONS_REQUEST);
                    }
                });
    }

    private void setHideIcon() {
        SharedPreferences sharedPreferences = getSharedPreferences(PropUtil.PREFERENCE_NAME, MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(PropUtil.PREFERENCE_HIDE_ICON, true).apply();
        PropUtil.hideOrShowLauncher(this, true);
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_PHONE_STATE)) {
                showNoticeDialog();
            } else {

                // to show notice before the first permission request.
                if (!getSharedPreferences(PropUtil.PREFERENCE_NAME, MODE_PRIVATE)
                        .getBoolean(PropUtil.PREFERENCE_NOTICE_SHOWED, PropUtil.PREFERENCE_NOTICE_SHOWED_DEFAULT)) {
                    showNoticeDialog();
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_PHONE_STATE},
                            MY_PERMISSIONS_REQUEST);
                }
            }
        } else {
            Dialog dialog = getFunctionDialogBuilder().create();
            Window window = dialog.getWindow();
            if (window != null)
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            dialog.show();
        }
    }

    private void showNoticeDialog() {
        Dialog dialog = getNoticeDialogBuilder().create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        getSharedPreferences(PropUtil.PREFERENCE_NAME, MODE_PRIVATE).edit()
                .putBoolean(PropUtil.PREFERENCE_NOTICE_SHOWED, true).apply();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, R.string.permission_deny_toast, Toast.LENGTH_SHORT).show();
                }
                getFunctionDialogBuilder().create().show();
                return;
            }
        }
    }
}
