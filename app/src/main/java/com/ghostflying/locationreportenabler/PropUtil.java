package com.ghostflying.locationreportenabler;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by ghostflying on 3/18/15.
 */
public final class PropUtil {
    private static final String COMMAND_PREFIX = "setprop ";
    private static final String[] PROPERTIES = {
            "gsm.sim.operator.numeric 310030",
            "gsm.sim.operator.iso-country us"
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
}
