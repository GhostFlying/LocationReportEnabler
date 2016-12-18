package com.ghostflying.locationreportenabler;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;


public class MainActivity extends Activity {
    private static final int MY_PERMISSIONS_REQUEST = 100;

    private boolean[] function_enable = new boolean[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, R.string.prompt_toast, Toast.LENGTH_SHORT).show();
        requestPermission();
        getFunctionDialogBuilder().create().show();
    }

    private AlertDialog.Builder getFunctionDialogBuilder(){
        boolean[] selected = new boolean[]{
            true, false, false, false, false
        };
        function_enable[0] = true;

        return new AlertDialog.Builder(this)
                .setTitle(R.string.function_choice_dialog_title)
                .setMultiChoiceItems(R.array.dialog_choices, selected, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        function_enable[which] = isChecked;
                    }
                })
                .setPositiveButton(R.string.function_choice_dialog_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (function_enable[0]){
                            if (function_enable[4]){
                                setHideIcon();
                            }
                            PropUtil.applyFunctions(function_enable,MainActivity.this);
                        }
                        finish();
                    }
                });
    }

    private void setHideIcon(){
        SharedPreferences sharedPreferences = getSharedPreferences(PropUtil.PREFERENCE_NAME, MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(PropUtil.PREFERENCE_HIDE_ICON, true).apply();
        PropUtil.hideLauncher(this);
    }

    private void requestPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_PHONE_STATE)) {
                Toast.makeText(this, R.string.permission_toast, Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST);
            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, R.string.permission_deny_toast, Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}
