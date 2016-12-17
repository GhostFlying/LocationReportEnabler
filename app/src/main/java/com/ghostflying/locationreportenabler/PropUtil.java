package com.ghostflying.locationreportenabler;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by ghostflying on 3/18/15.
 */
public final class PropUtil {
    public static final String PREFERENCE_NAME = "Settings";
    public static final String PREFERENCE_HIDE_ICON = "HideIcon";
    public static final boolean PREFERENCE_HIDE_ICON_DEFAULT = false;

    private static final String COMMAND_PREFIX = "setprop ";
    private static final String[] PROPERTIES = {
            "gsm.sim.operator.numeric 310004,310004",
            "gsm.sim.operator.iso-country us,us"
    };
    private static final String COMMAND_CLEAR_PREFIX = "pm clear ";
    private static final String PKG_GMS = "com.google.android.gms";
    private static final String PKG_MAPS = "com.google.android.apps.maps";
    private static final String COMMAND_REBOOT = "reboot";

    public static void enableLocationReport() {
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

    public static void applyFunctions(boolean[] params){
        try{
            Process p = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(p.getOutputStream());

            if (params[0]){
                for (String property : PROPERTIES) {
                    os.writeBytes(COMMAND_PREFIX + property + "\n");
                }
            }

            if (params[1]){
                os.writeBytes(COMMAND_CLEAR_PREFIX + PKG_GMS + "\n");
            }

            if (params[2]){
                os.writeBytes(COMMAND_CLEAR_PREFIX + PKG_MAPS + "\n");
            }

            if (params[3]){
                os.writeBytes(COMMAND_REBOOT + "\n");
            }

            os.writeBytes("exit\n");
            os.flush();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void hideLauncher(Context context){
        PackageManager p = context.getPackageManager();
        ComponentName componentName = new ComponentName(context, MainActivity.class);
        if (p.getComponentEnabledSetting(componentName) != PackageManager.COMPONENT_ENABLED_STATE_DISABLED){
            p.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
            Log.d("PropUtil", "Hide the icon.");
        }
    }
}
