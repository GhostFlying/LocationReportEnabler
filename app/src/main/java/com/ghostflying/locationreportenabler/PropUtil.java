package com.ghostflying.locationreportenabler;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by ghostflying on 3/18/15.
 */
public final class PropUtil {
    private static final String COMMAND_PREFIX = "setprop ";
    private static final String[] PROPERTIES = {"gsm.sim.operator.numeric 310030"};

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
}
