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
        PropUtil.enableLocationReport();
        SharedPreferences sharedPreferences = context.getSharedPreferences(PropUtil.PREFERENCE_NAME, Context.MODE_PRIVATE);
        PropUtil.hideOrShowLauncher(context, sharedPreferences.getBoolean(PropUtil.PREFERENCE_HIDE_ICON, PropUtil.PREFERENCE_HIDE_ICON_DEFAULT));
    }
}
