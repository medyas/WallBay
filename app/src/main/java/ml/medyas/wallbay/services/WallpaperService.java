package ml.medyas.wallbay.services;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.request.target.Target;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import ml.medyas.wallbay.R;
import ml.medyas.wallbay.entities.ImageEntity;
import ml.medyas.wallbay.utils.GlideApp;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * helper methods.
 */
public class WallpaperService extends IntentService {
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_SET_WALLPAPER = "ml.medyas.wallbay.services.action.SET_WALLPAPER";

    private static final String EXTRA_PARAM = "ml.medyas.wallbay.services.extra.IMAGE_ITEM";

    private static String CHANNEL_ID = "WALLPAPER_DOWNLOAD";
    private static int notificationId = 124521;

    public WallpaperService() {
        super("WallpaperService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void setWallpaper(Context context, ImageEntity imageEntity) {
        Intent intent = new Intent(context, WallpaperService.class);
        intent.setAction(ACTION_SET_WALLPAPER);
        intent.putExtra(EXTRA_PARAM, imageEntity);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (ACTION_SET_WALLPAPER.equals(action)) {
                ImageEntity param = intent.getParcelableExtra(EXTRA_PARAM);
                handleWallpaperAction(param);
            }
        }
    }

    /**
     * Handle action in the provided background thread with the provided
     * parameters.
     */
    private void handleWallpaperAction(ImageEntity imageEntity) {
        final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Wallpaper Download";
            String description = "notification for downloading wallpaper in background";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager1 = getSystemService(NotificationManager.class);
            notificationManager1.createNotificationChannel(channel);

        }

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
        mBuilder.setContentTitle("Wallpaper Download")
                .setContentText("Download in progress...")
                .setSmallIcon(R.drawable.ic_image_black_24dp)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setProgress(0, 0, true);
        notificationManager.notify(notificationId, mBuilder.build());

        Bitmap resource = null;
        try {
            resource = GlideApp.with(getApplicationContext())
                    .asBitmap()
                    .load(imageEntity.getOriginalImage())
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();

        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.d("mainactivity", e.getMessage());
            Toast.makeText(getApplicationContext(), "Could not set image", Toast.LENGTH_SHORT).show();
            mBuilder.setContentText("Could not get the image")
                    .setProgress(0, 0, false);
            notificationManager.notify(notificationId, mBuilder.build());

        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.d("mainactivity", e.getMessage());
            Toast.makeText(getApplicationContext(), "Could not set image", Toast.LENGTH_SHORT).show();
            mBuilder.setContentText("Could not get the image")
                    .setProgress(0, 0, false);
            notificationManager.notify(notificationId, mBuilder.build());

        }

        Toast.makeText(getApplicationContext(), "Image loaded", Toast.LENGTH_SHORT).show();
        Log.d("mainactivity", "onResourceReady !!");
        WallpaperManager myWallpaperManager
                = WallpaperManager.getInstance(getApplicationContext());
        try {
            myWallpaperManager.setBitmap(resource);
            Toast.makeText(getApplicationContext(), "Image set as wallpaper", Toast.LENGTH_SHORT).show();
            Log.d("mainactivity", "finished load of image");
            mBuilder.setContentText("Download complete")
                    .setProgress(0, 0, false)
                    .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(resource));
            notificationManager.notify(notificationId, mBuilder.build());
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("mainactivity", e.getMessage());
            Toast.makeText(getApplicationContext(), "Could not set image", Toast.LENGTH_SHORT).show();
            mBuilder.setContentText("Could not get the image")
                    .setProgress(0, 0, false);
            notificationManager.notify(notificationId, mBuilder.build());
        }

        /*GlideApp.with(this)
                .asBitmap()
                .load(imageEntity.getOriginalImage())
                .into(new Target<Bitmap>() {
                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {

                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        Toast.makeText(getApplicationContext(), "Could not set image", Toast.LENGTH_SHORT).show();
                        mBuilder.setContentText("Could not get the image")
                                .setProgress(0,0,false);
                        notificationManager.notify(notificationId, mBuilder.build());
                    }

                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Toast.makeText(getApplicationContext(), "Image loaded", Toast.LENGTH_SHORT).show();
                        Log.d("mainactivity", "onResourceReady !!");
                        WallpaperManager myWallpaperManager
                                = WallpaperManager.getInstance(getApplicationContext());
                        try {
                            myWallpaperManager.setBitmap(resource);
                            Toast.makeText(getApplicationContext(), "Image set as wallpaper", Toast.LENGTH_SHORT).show();
                            Log.d("mainactivity", "finished load of image");
                            mBuilder.setContentText("Download complete")
                                    .setProgress(0,0,false);
                            notificationManager.notify(notificationId, mBuilder.build());
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.d("mainactivity", e.getMessage());
                            Toast.makeText(getApplicationContext(), "Could not set image", Toast.LENGTH_SHORT).show();
                            mBuilder.setContentText("Could not get the image")
                                    .setProgress(0,0,false);
                            notificationManager.notify(notificationId, mBuilder.build());
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }

                    @Override
                    public void getSize(@NonNull SizeReadyCallback cb) {

                    }

                    @Override
                    public void removeCallback(@NonNull SizeReadyCallback cb) {

                    }

                    @Override
                    public void setRequest(@Nullable Request request) {

                    }

                    @Nullable
                    @Override
                    public Request getRequest() {
                        return null;
                    }

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onStop() {

                    }

                    @Override
                    public void onDestroy() {

                    }
                });*/
    }


}
