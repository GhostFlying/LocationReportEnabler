package com.ghostflying.locationreportenabler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.dialog_content))
                .setPositiveButton(getString(R.string.dialog_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PropUtil.enableLocationReport();
                        hideLauncher();
                        finish();
                    }
                });
        builder.create().show();
    }

    private void hideLauncher(){
        PackageManager p = getApplicationContext().getPackageManager();
        p.setComponentEnabledSetting(getComponentName(),PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }
}
