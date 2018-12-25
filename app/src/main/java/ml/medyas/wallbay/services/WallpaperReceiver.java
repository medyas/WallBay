package ml.medyas.wallbay.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class WallpaperReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (action.equals("android.intent.action.DOWNLOAD_NOTIFICATION_CLICKED")) {
                Log.d("mainactivity", "Clicked on android.intent.action.DOWNLOAD_NOTIFICATION_CLICKED");
            }
        }
    }
}
