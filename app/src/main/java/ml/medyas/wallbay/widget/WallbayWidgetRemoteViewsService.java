package ml.medyas.wallbay.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class WallbayWidgetRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        WallbayWidget.sendRefreshBroadcast(getApplicationContext());
        return new WallbayWidgetRemoteViewsFactory(getApplicationContext(), intent);
    }
}
