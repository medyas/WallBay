package ml.medyas.wallbay.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Observable;

public class NetworkChangeReceiver extends BroadcastReceiver {

    public static NetworkObservable getObservable() {
        return NetworkObservable.getInstance();
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (intent.getAction() != null) {
            if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE") || intent.getAction().equals("android.net.wifi.WIFI_STATE_CHANGED")) {
                getObservable().connectionChanged();
            }
        }
    }

    public static class NetworkObservable extends Observable {
        private static NetworkObservable instance = null;

        private NetworkObservable() {
            // Exist to defeat instantiation.
        }

        public static NetworkObservable getInstance() {
            if (instance == null) {
                instance = new NetworkObservable();
            }
            return instance;
        }

        private void connectionChanged() {
            setChanged();
            notifyObservers();
        }
    }
}
