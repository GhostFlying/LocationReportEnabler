package com.ghostflying.locationreportenabler.tile;

import android.content.Context;
import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.support.annotation.RequiresApi;
import android.telephony.TelephonyManager;

/**
 * Created by ghostflying on 2017/1/11.
 */

@RequiresApi(api = Build.VERSION_CODES.N)
public class FakeOperatorTile extends TileService {
    @Override
    public void onStartListening() {
        super.onStartListening();

        TelephonyManager manager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String code = manager.getSimOperator();
        String country = manager.getSimCountryIso();

        Tile tile = getQsTile();
        tile.setLabel(String.format("%s %s", code, country));
        tile.updateTile();
    }

    @Override
    public void onClick() {
        super.onClick();
    }
}
