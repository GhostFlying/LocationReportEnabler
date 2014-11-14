package com.ghostflying.locationreportenabler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;

public class BootCompletedReceiver extends BroadcastReceiver {
    private final String COMMAND_PREFIX = "setprop ";
    private final String[] PROPERTIES = {"gsm.sim.operator.numeric 310004",
            "gsm.sim.operator.iso-country us",
            "gsm.sim.operator.alpha Verizon"};

    public BootCompletedReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        enableLocationReport();
        Log.i("Enabler", "Done.");
    }

    public void enableLocationReport() {
        try{
            Process p = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(p.getOutputStream());
            for (String property : PROPERTIES) {
                os.writeBytes(COMMAND_PREFIX + property + "\n");
            }
            os.writeBytes("exit\n");
            os.flush();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
