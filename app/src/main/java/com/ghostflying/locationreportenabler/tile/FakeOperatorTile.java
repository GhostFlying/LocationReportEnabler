package com.ghostflying.locationreportenabler.tile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.support.annotation.RequiresApi;
import android.telephony.TelephonyManager;

import com.ghostflying.locationreportenabler.PropUtil;
import com.ghostflying.locationreportenabler.R;

/**
 * Created by ghostflying on 2017/1/11.
 */

@RequiresApi(api = Build.VERSION_CODES.N)
public class FakeOperatorTile extends TileService {


    @Override
    public void onStartListening() {
        super.onStartListening();

        updateTile();
    }

    private void updateTile() {
        TelephonyManager manager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String code = manager.getSimOperator();
        String country = manager.getSimCountryIso();

        SharedPreferences preferences = PropUtil.getProtecredSharedPreferences(this);

        Tile tile = getQsTile();
        tile.setLabel(String.format("%s %s", code, country));
        if (preferences.getBoolean(PropUtil.PREFERENCE_ENABLED, PropUtil.PREFERENCE_ENABLED_DEFAULT)) {
            tile.setState(Tile.STATE_ACTIVE);
        }
        else {
            tile.setState(Tile.STATE_INACTIVE);
        }
        tile.updateTile();
    }

    @Override
    public void onClick() {
        super.onClick();

        SharedPreferences preferences = PropUtil.getProtecredSharedPreferences(this);
        final boolean enabledCurrent = preferences.getBoolean(PropUtil.PREFERENCE_ENABLED, PropUtil.PREFERENCE_ENABLED_DEFAULT);

        final SharedPreferences.Editor editor = preferences.edit();

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage(
                        String.format(
                                getString(R.string.tile_dialog_message),
                                enabledCurrent ?
                                        getString(R.string.tile_dialog_message_disabled) :
                                        getString(R.string.tile_dialog_message_enabled)
                        )
                )
                .setNegativeButton(R.string.tile_dialog_reboot, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        PropUtil.reboot();
                    }
                })
                .setPositiveButton(R.string.tile_dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // just dismiss
                    }
                });
        showDialog(builder.create());

        editor.putBoolean(PropUtil.PREFERENCE_ENABLED, !enabledCurrent).apply();
        updateTile();
    }
}
