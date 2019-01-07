package ml.medyas.wallbay.services;

import android.app.DownloadManager;
import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import ml.medyas.wallbay.BuildConfig;
import ml.medyas.wallbay.R;
import ml.medyas.wallbay.utils.GlideApp;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * helper methods.
 */
public class WallpaperService extends IntentService {
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_SET_WALLPAPER = "ml.medyas.wallbay.services.action.SET_WALLPAPER";
    private static final String ACTION_DOWNLOAD_WALLPAPER = "ml.medyas.wallbay.services.action.DOWNLOAD_WALLPAPER";

    private static final String EXTRA_PARAM1 = "ml.medyas.wallbay.services.extra.IMAGE_ITEM";
    private static final String EXTRA_PARAM2 = "ml.medyas.wallbay.services.extra.IMAGE_PROVIDER";

    public WallpaperService() {
        super("WallpaperService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void setWallpaper(Context context, String url) {
        Intent intent = new Intent(context, WallpaperService.class);
        intent.setAction(ACTION_SET_WALLPAPER);
        intent.putExtra(EXTRA_PARAM1, url);
        context.startService(intent);
    }

    public static void downloadWallpaper(Context context, String url, int provider) {
        Intent intent = new Intent(context, WallpaperService.class);
        intent.setAction(ACTION_DOWNLOAD_WALLPAPER);
        intent.putExtra(EXTRA_PARAM1, url);
        intent.putExtra(EXTRA_PARAM2, provider);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String url = intent.getStringExtra(EXTRA_PARAM1);
            String action = intent.getAction();
            if (ACTION_SET_WALLPAPER.equals(action)) {
                handleSetWallpaperAction(url);
            } else if (ACTION_DOWNLOAD_WALLPAPER.equals(action)) {
                handleDownloadWallpaperAction(url, intent.getIntExtra(EXTRA_PARAM2, 0));
            }
        }
    }

    /**
     * Handle action in the provided background thread with the provided
     * parameters.
     */
    private void handleSetWallpaperAction(String url) {
        final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        String CHANNEL_ID = "WALLPAPER_DOWNLOAD";
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
                .setSmallIcon(R.drawable.ic_set_as_wallpaper)
                .setColor(getResources().getColor(R.color.colorPrimaryDark))
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setAutoCancel(false)
                .setProgress(0, 0, true);
        int notificationId = 124521;
        notificationManager.notify(notificationId, mBuilder.build());

        Bitmap resource = null;
        try {
            resource = GlideApp.with(getApplicationContext())
                    .asBitmap()
                    .load(url)
                    .apply(new RequestOptions()
                            .format(DecodeFormat.PREFER_ARGB_8888))
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
        String text = "Could not get the image";
        if (resource != null) {
            try {
                myWallpaperManager.setBitmap(resource);
                Toast.makeText(getApplicationContext(), "Image set as wallpaper", Toast.LENGTH_SHORT).show();
                Log.d("mainactivity", "finished load of image");
                text = "Download complete";
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("mainactivity", e.getMessage());
            }
        }

        mBuilder.setContentText(text)
                .setProgress(0, 0, false)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(resource));
        notificationManager.notify(notificationId, mBuilder.build());
    }

    private void handleDownloadWallpaperAction(String url, int code) {
        if (code == 2) {
            retrieveUrl(url);
        } else {
            downloadImage(url);
        }

    }

    private void downloadImage(String url) {
        Log.d("mainactivity", "url: " + url);
        Uri uri = Uri.parse(url);
        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle("Downloading Image");
        request.setVisibleInDownloadsUi(true);

        Boolean isSDPresent = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        Boolean isSDSupportedDevice = Environment.isExternalStorageRemovable();
        String dir = "/WallBay images";

        if (isSDSupportedDevice && isSDPresent) {
            // yes SD-card is present
            dir = Environment.getExternalStorageDirectory().toString() + dir;
        }

        request.setDestinationInExternalPublicDir(dir, uri.getLastPathSegment());

        downloadManager.enqueue(request);
    }

    private void retrieveUrl(String u) {
        String url = String.format("%s?client_id=%s", u, BuildConfig.UnsplashApiKey);
        OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        Request request = builder.build();
        try {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                    String CHANNEL_ID = "WALLPAPER_DOWNLOAD";
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        CharSequence name = "Wallpaper Download";
                        String description = "notification for downloading wallpaper in background";
                        int importance = NotificationManager.IMPORTANCE_DEFAULT;
                        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                        channel.setDescription(description);
                        NotificationManager notificationManager1 = getSystemService(NotificationManager.class);
                        notificationManager1.createNotificationChannel(channel);

                    }

                    final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
                    mBuilder.setContentTitle("Image Download")
                            .setContentText("Failed to donwload the image")
                            .setSmallIcon(R.drawable.ic_error_black_24dp)
                            .setPriority(NotificationCompat.PRIORITY_LOW);
                    int notificationId = 124521;
                    notificationManager.notify(notificationId, mBuilder.build());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String temp = response.body().string();
                    JSONObject json = null;
                    try {
                        json = new JSONObject(temp);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("mainactivity", "response: " + temp);
                    downloadImage(json.optString("url"));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
