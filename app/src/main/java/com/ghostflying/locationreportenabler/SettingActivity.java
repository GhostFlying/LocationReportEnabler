package com.ghostflying.locationreportenabler;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.sax.RootElement;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ghostflying.locationreportenabler.view.FunctionChooseAlertView;

import java.lang.ref.WeakReference;

import eu.chainfire.libsuperuser.Shell;

public class SettingActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST = 100;
    private ViewGroup mContainer;
    private View mRootView;
    private FloatingActionButton mFab;
    private AsyncTask<Void, Void, Void> mRootCheckTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mContainer = findViewById(R.id.container);
        mRootView = findViewById(R.id.root_view);
        mFab = findViewById(R.id.fab);

        final FunctionChooseAlertView functionChooseAlertView = new FunctionChooseAlertView(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        mContainer.addView(functionChooseAlertView, 1, lp);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = PropUtil.getProtecredSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                final String numeric = functionChooseAlertView.getOperatorNumber();
                final String country = functionChooseAlertView.getOperatorCountry();

                editor.putString(PropUtil.PREFERENCE_FAKE_NUMERIC, numeric);
                editor.putString(PropUtil.PREFERENCE_FAKE_COUNTRY, country);
                editor.apply();

                final boolean[] result = functionChooseAlertView.getSelectedFunctions();
                if (result[4]) {
                    setHideIcon();
                }

                if (mRootCheckTask != null && !mRootCheckTask.isCancelled()) {
                    mRootCheckTask.cancel(true);
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        PropUtil.applyFunctions(result, numeric, country);
                    }
                }).start();
                finish();
            }
        });

        requestPermission();
    }

    private void setHideIcon() {
        SharedPreferences sharedPreferences = PropUtil.getProtecredSharedPreferences(this);
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
        }

        mRootCheckTask = new RootCheckTask(this);
        mRootCheckTask.execute();
    }

    private void showNoticeDialog() {
        Dialog dialog = getNoticeDialogBuilder().create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        getSharedPreferences(PropUtil.PREFERENCE_NAME, MODE_PRIVATE).edit()
                .putBoolean(PropUtil.PREFERENCE_NOTICE_SHOWED, true).apply();
    }

    private AlertDialog.Builder getNoticeDialogBuilder() {
        return new AlertDialog.Builder(this)
                .setTitle(R.string.notice_dialog_title)
                .setMessage(R.string.notice_dialog_message)
                .setPositiveButton(R.string.notice_dialog_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(SettingActivity.this,
                                new String[]{Manifest.permission.READ_PHONE_STATE},
                                MY_PERMISSIONS_REQUEST);
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted
                } else {
                    Snackbar.make(mRootView, R.string.permission_deny_toast, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private static class RootCheckTask extends AsyncTask<Void, Void, Void> {
        private static final int MAX_RETRY_TIME = 3;

        private WeakReference<SettingActivity> mAct;

        private volatile boolean mIsRootGranted;

        RootCheckTask(SettingActivity act) {
            mAct = new WeakReference<SettingActivity>(act);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 0; i < MAX_RETRY_TIME; i++) {
                if (isCancelled()) {
                    return null;
                }

                mIsRootGranted = Shell.SU.available();

                Log.d("RootCheckTask", "check root permission with res: " + mIsRootGranted);

                if (mIsRootGranted) {
                    break;
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            SettingActivity settingActivity = mAct.get();
            if (settingActivity != null && !mIsRootGranted) {
                settingActivity.showNoticeDialog();
            }
        }
    }
}
