package ml.medyas.wallbay.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.RemoteViews;

import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ml.medyas.wallbay.R;
import ml.medyas.wallbay.entities.ImageEntity;
import ml.medyas.wallbay.utils.GlideApp;

import static ml.medyas.wallbay.ui.activities.MainActivity.IMAGE_ITEM;
import static ml.medyas.wallbay.ui.activities.MainActivity.LAUNCH_IMAGE_DETAILS;

public class WallbayWidgetRemoteViewsFactory implements android.widget.RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private List<ImageEntity> imageEntities = new ArrayList<>();

    public WallbayWidgetRemoteViewsFactory(final Context context, Intent intent) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        this.imageEntities = WallbayWidget.imageEntities;
    }

    @Override
    public void onDataSetChanged() {
        this.imageEntities = WallbayWidget.imageEntities;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if(imageEntities != null)
            return imageEntities.size();
        return 0;
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_item);

        Intent fillInIntent = new Intent();
        fillInIntent.setAction(LAUNCH_IMAGE_DETAILS);
        fillInIntent.putExtra(IMAGE_ITEM, imageEntities.get(i));
        rv.setOnClickFillInIntent(R.id.widget_container, fillInIntent);

        FutureTarget<Bitmap> futureTarget = GlideApp.with(context)
                .asBitmap()
                .load(imageEntities.get(i).getPreviewImage())
                .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
        try {
            rv.setImageViewBitmap(R.id.widget_image, futureTarget.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return new RemoteViews(context.getPackageName(), R.layout.widget_item_loading);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
