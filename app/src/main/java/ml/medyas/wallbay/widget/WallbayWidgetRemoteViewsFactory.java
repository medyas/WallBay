package ml.medyas.wallbay.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.RemoteViews;

import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ml.medyas.wallbay.R;
import ml.medyas.wallbay.entities.ImageEntity;
import ml.medyas.wallbay.repositories.FavoriteRepository;
import ml.medyas.wallbay.utils.GlideApp;

import static ml.medyas.wallbay.widget.WallbayWidget.IMAGE_LIST;

public class WallbayWidgetRemoteViewsFactory implements android.widget.RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private List<String> imageEntities = new ArrayList<>();

    public WallbayWidgetRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
        imageEntities = intent.getStringArrayListExtra(IMAGE_LIST);
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                FavoriteRepository repository = new FavoriteRepository(context);
                List<ImageEntity> list = repository.getImageEntitiesList();
                ArrayList<String> images = new ArrayList<>();
                for(ImageEntity img: list) {
                    images.add(img.getPreviewImage());
                }
                if(imageEntities != null)
                    imageEntities.clear();
                imageEntities = images;

                if(images.size() != imageEntities.size()) {
                    onDataSetChanged();
                }
                return null;
            }
        }.execute();

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

        FutureTarget<Bitmap> futureTarget = GlideApp.with(context)
                .asBitmap()
                .load(imageEntities.get(i))
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
