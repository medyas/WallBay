package ml.medyas.wallbay.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.List;

import ml.medyas.wallbay.R;
import ml.medyas.wallbay.models.ImageEntity;
import ml.medyas.wallbay.repositories.FavoriteRepository;
import ml.medyas.wallbay.ui.activities.MainActivity;

import static ml.medyas.wallbay.ui.activities.MainActivity.LAUNCH_APP;

/**
 * Implementation of App Widget functionality.
 */
public class WallbayWidget extends AppWidgetProvider {

    public static List<ImageEntity> imageEntities = new ArrayList<>();

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
                imageEntities = repository.getImageEntitiesList();

                Intent intent = new Intent(context, WallbayWidgetRemoteViewsService.class);
                views.setRemoteAdapter(R.id.widget_listview, intent);

                // template to handle the click listener for each item
                Intent clickIntentTemplate = new Intent(context, MainActivity.class);
                PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                        .addNextIntentWithParentStack(clickIntentTemplate)
                        .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                views.setPendingIntentTemplate(R.id.widget_listview, clickPendingIntentTemplate);

                Intent appIntent = new Intent(context, MainActivity.class);
                appIntent.setAction(LAUNCH_APP);
                PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, 0);
                views.setOnClickPendingIntent(R.id.widget_app, appPendingIntent);


                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_listview);
                // Instruct the widget manager to update the widget
                appWidgetManager.updateAppWidget(appWidgetId, views);

                return null;
            }
        }.execute();
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
            onUpdate(context, mgr, mgr.getAppWidgetIds(cn));
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

