package ml.medyas.wallbay.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.List;

import ml.medyas.wallbay.R;
import ml.medyas.wallbay.entities.ImageEntity;
import ml.medyas.wallbay.repositories.FavoriteRepository;

/**
 * Implementation of App Widget functionality.
 */
public class WallbayWidget extends AppWidgetProvider {

    public static final String IMAGE_LIST = "IMAGE_LIST";

    static void updateAppWidget(final Context context, final AppWidgetManager appWidgetManager,
                                final int appWidgetId) {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                FavoriteRepository repository = new FavoriteRepository(context);
                RemoteViews views = new RemoteViews(
                        context.getPackageName(),
                        R.layout.wallbay_widget
                );
                Intent intent = new Intent(context, WallbayWidgetRemoteViewsService.class);
                List<ImageEntity> list = repository.getImageEntitiesList();
                ArrayList<String> images = new ArrayList<>();
                for(ImageEntity img: list) {
                    images.add(img.getPreviewImage());
                }
                intent.putStringArrayListExtra(IMAGE_LIST, images);
                views.setRemoteAdapter(R.id.widget_listview, intent);

                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_listview);
                // Instruct the widget manager to update the widget
                appWidgetManager.updateAppWidget(appWidgetId, views);

                return null;
            }
        }.execute();

        // template to handle the click listener for each item
        /*Intent clickIntentTemplate = new Intent(context, MainActivity.class);
        PendingIntent clickPendingIntentTemplate = PendingIntent.getBroadcast(context, 0, clickIntentTemplate,
                PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_listview, clickPendingIntentTemplate);*/


    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }


    public static void sendRefreshBroadcast(Context context) {
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setComponent(new ComponentName(context, WallbayWidget.class));
        context.sendBroadcast(intent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            // refresh all your widgets
            AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            ComponentName cn = new ComponentName(context, WallbayWidget.class);
            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.widget_listview);
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

