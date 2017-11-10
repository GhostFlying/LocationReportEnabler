package com.ghostflying.locationreportenabler;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ghostflying on 3/18/15.
 */
public final class PropUtil {
    public static final String PREFERENCE_NAME = "Settings";
    public static final String PREFERENCE_ENABLED = "Enabled";
    public static final boolean PREFERENCE_ENABLED_DEFAULT = true;
    public static final String PREFERENCE_HIDE_ICON = "HideIcon";
    public static final String PREFERENCE_NOTICE_SHOWED = "NoticeShowed";
    public static final boolean PREFERENCE_HIDE_ICON_DEFAULT = false;
    public static final boolean PREFERENCE_NOTICE_SHOWED_DEFAULT = false;
    public static final String PREFERENCE_FAKE_NUMERIC = "FakeNumeric";
    public static final String PREFERENCE_FAKE_COUNTRY = "FakeCountry";
    public static final String PREFERENCE_FAKE_NUMERIC_DEFAULT = "310030";
    public static final String PREFERENCE_FAKE_COUNTRY_DEFAULT = "us";

    private static final String COMMAND_SET_PREFIX = "setprop ";
    private static final String COMMAND_GET_PREFIX = "getprop ";
    private static final String PROPERTY_NUMERIC = "gsm.sim.operator.numeric";
    private static final String PROPERTY_COUNTRY = "gsm.sim.operator.iso-country";
    private static final String COMMAND_CLEAR_PREFIX = "pm clear ";
    private static final String PKG_GMS = "com.google.android.gms";
    private static final String PKG_MAPS = "com.google.android.apps.maps";
    private static final String COMMAND_REBOOT = "reboot";

    public static void enableLocationReport(String numeric, String country) {
        try {
            Process p = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(p.getOutputStream());
            setFakeCarrier(p, os, numeric, country);
            os.writeBytes("exit\n");
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void applyFunctions(boolean[] params, String numeric, String country) {
        try {
            Process p = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(p.getOutputStream());

            if (params[0]) {
                setFakeCarrier(p, os, numeric, country);
            }

            if (params[1]) {
                os.writeBytes(COMMAND_CLEAR_PREFIX + PKG_GMS + "\n");
            }

            if (params[2]) {
                os.writeBytes(COMMAND_CLEAR_PREFIX + PKG_MAPS + "\n");
            }

            if (params[3]) {
                os.writeBytes(COMMAND_REBOOT + "\n");
            }

            os.writeBytes("exit\n");
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void reboot() {
        try {
            Process p = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(p.getOutputStream());

            os.writeBytes(COMMAND_REBOOT + "\n");

            os.writeBytes("exit\n");
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void setFakeCarrier(
            Process processWithSu, DataOutputStream os, String numeric, String country) {

        setFakerCarrierForDualCard(processWithSu, os, numeric, country);
    }

    private static final int BUF_LEN = 100;

    private static void setFakerCarrierForDualCard(
            Process processWithSu, DataOutputStream os, String numberic, String country) {

        try {
            // numeric
            os.writeBytes(COMMAND_GET_PREFIX + PROPERTY_NUMERIC + "\n");
            os.flush();

            InputStream stdout = processWithSu.getInputStream();
            String out = getShellOutput(stdout);
            Log.d("PropUtil", String.format("current prop %s is %s", PROPERTY_NUMERIC, out));

            // replace all as some devices only have one even it have dual card.
            // as we can not find out the implement of the devices with dual card before 6.0,
            // any action should be careful.
            String[] operatorCodes = out.split(",");
            for (int i = 0; i < operatorCodes.length; i++) {
                operatorCodes[i] = numberic;
            }
            os.writeBytes(COMMAND_SET_PREFIX + PROPERTY_NUMERIC + " " + TextUtils.join(",", operatorCodes) + "\n");

            // country
            os.writeBytes(COMMAND_GET_PREFIX + PROPERTY_COUNTRY + "\n");
            os.flush();
            out = getShellOutput(stdout);
            Log.d("PropUtil", String.format("current prop %s is %s", PROPERTY_COUNTRY, out));

            String[] isoCountries = out.split(",");
            for (int i = 0; i < isoCountries.length; i++) {
                isoCountries[i] = country;
            }
            os.writeBytes(COMMAND_SET_PREFIX + PROPERTY_COUNTRY + " " + TextUtils.join(",", isoCountries) + "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    private static String getShellOutput(InputStream stdout) throws IOException {
        byte[] buffer = new byte[BUF_LEN];
        int read;
        StringBuilder out = new StringBuilder();
        //read method will wait forever if there is nothing in the stream
        //so we need to read it in another way than while((read=stdout.read(buffer))>0)
        while (true) {
            read = stdout.read(buffer);
            if (read > 0) {
                out.append(new String(buffer, 0, read));
            }
            if (read < BUF_LEN) {
                //we have read everything
                break;
            }
        }
        return out.toString();
    }

    public static void hideOrShowLauncher(Context context, boolean isHide) {
        PackageManager p = context.getPackageManager();
        ComponentName componentName = new ComponentName(context, SettingActivity.class);
        if (isHide) {
            if (p.getComponentEnabledSetting(componentName) != PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
                p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                Log.d("PropUtil", "Hide the icon.");
            }
        } else {
            if (p.getComponentEnabledSetting(componentName) != PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
                p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
                Log.d("PropUtil", "Show the icon.");
            }
        }
    }

    public static SharedPreferences getProtecredSharedPreferences(Context context) {
        SharedPreferences preferences;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Context protectedContext = context.createDeviceProtectedStorageContext();
            preferences = protectedContext.getSharedPreferences(PropUtil.PREFERENCE_NAME, Context.MODE_PRIVATE);
        }
        else {
            preferences = context.getSharedPreferences(PropUtil.PREFERENCE_NAME, Context.MODE_PRIVATE);
        }
        return preferences;
    }
}
