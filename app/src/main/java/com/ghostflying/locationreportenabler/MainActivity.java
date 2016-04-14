package com.ghostflying.locationreportenabler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;


public class MainActivity extends Activity {
    private boolean[] function_enable = new boolean[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, R.string.prompt_toast, Toast.LENGTH_LONG).show();
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
                            PropUtil.applyFunctions(function_enable);
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
}
