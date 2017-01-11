package com.ghostflying.locationreportenabler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class EnablerReceiver extends BroadcastReceiver {
    public EnablerReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("EnablerReceiver", "Set prop by " + intent.getAction());

        SharedPreferences preferences = context.getSharedPreferences(PropUtil.PREFERENCE_NAME, Context.MODE_PRIVATE);

        if (preferences.getBoolean(PropUtil.PREFERENCE_ENABLED, PropUtil.PREFERENCE_ENABLED_DEFAULT)) {
            String numeric = preferences.getString(PropUtil.PREFERENCE_FAKE_NUMERIC, PropUtil.PREFERENCE_FAKE_NUMERIC_DEFAULT);
            String country = preferences.getString(PropUtil.PREFERENCE_FAKE_COUNTRY, PropUtil.PREFERENCE_FAKE_COUNTRY_DEFAULT);

            PropUtil.enableLocationReport(numeric, country);
            PropUtil.hideOrShowLauncher(context, preferences.getBoolean(PropUtil.PREFERENCE_HIDE_ICON, PropUtil.PREFERENCE_HIDE_ICON_DEFAULT));
        }
    }
}
