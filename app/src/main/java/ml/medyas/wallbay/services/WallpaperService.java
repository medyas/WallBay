package ml.medyas.wallbay.services;

import android.app.DownloadManager;
import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ml.medyas.wallbay.BuildConfig;
import ml.medyas.wallbay.R;
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
    private static final String ACTION_BULK_DOWNLOAD = "ml.medyas.wallbay.services.action.BULK_DOWNLOAD";

    private static final String EXTRA_PARAM1 = "ml.medyas.wallbay.services.extra.IMAGE_ITEM";
    private static final String EXTRA_PARAM2 = "ml.medyas.wallbay.services.extra.IMAGE_PROVIDER";
    public static final String UNSPLASH_COM = "https://api.unsplash.com";


    private static final int notificationId = 124521;

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

    public static void bulkWallpaperDownload(Context context, List<String> list) {
        Intent intent = new Intent(context, WallpaperService.class);
        intent.setAction(ACTION_BULK_DOWNLOAD);
        intent.putExtra(EXTRA_PARAM1, (ArrayList<String>) list);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (ACTION_SET_WALLPAPER.equals(action)) {
                String url = intent.getStringExtra(EXTRA_PARAM1);
                handleSetWallpaperAction(url);
            } else if (ACTION_DOWNLOAD_WALLPAPER.equals(action)) {
                String url = intent.getStringExtra(EXTRA_PARAM1);
                handleDownloadWallpaperAction(url, intent.getIntExtra(EXTRA_PARAM2, 0));
            } else if (ACTION_BULK_DOWNLOAD.equals(action)) {
                List<String> urls = intent.getStringArrayListExtra(EXTRA_PARAM1);
                handleBulkDownload(urls);
            }
        }
    }



    /**
     * Handle action in the provided background thread with the provided
     * parameters.
     * Handle setting image as wallpaper
     */
    private void handleSetWallpaperAction(String url) {
        final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        String CHANNEL_ID = "WALLPAPER_DOWNLOAD";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.wallpaper_download);
            String description = getString(R.string.notify_download);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager1 = getSystemService(NotificationManager.class);
            notificationManager1.createNotificationChannel(channel);
        }

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
        mBuilder.setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_text))
                .setSmallIcon(R.drawable.ic_set_as_wallpaper)
                .setColor(getResources().getColor(R.color.colorPrimaryDark))
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setAutoCancel(false)
                .setProgress(0, 0, true);
        notificationManager.notify(notificationId, mBuilder.build());

        if (url.contains(UNSPLASH_COM)) {
            retrieveUrl(url, new OnRetrieveDownloadURL() {
                @Override
                public void onSuccess(String u) {
                    setImageAsWallpaper(u, notificationManager, mBuilder, notificationId);
                }

                @Override
                public void onFailed() {
                    urlDownloadFailed();
                }
            });
        } else {
            setImageAsWallpaper(url, notificationManager, mBuilder, notificationId);
        }
    }

    private void setImageAsWallpaper(String url, NotificationManagerCompat notificationManager, NotificationCompat.Builder mBuilder, int notificationId) {
        Bitmap resource = null;
        try {
            /*resource = GlideApp.with(getApplicationContext())
                    .asBitmap()
                    .load(url)
                    .apply(new RequestOptions()
                            .format(DecodeFormat.PREFER_ARGB_8888))
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();*/
            URL u = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) u
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            resource = BitmapFactory.decodeStream(input);
            input.close();

        } catch (Exception e) {
            e.printStackTrace();
            Intent retry = new Intent(this, WallpaperService.class);
            retry.setAction(ACTION_SET_WALLPAPER);
            retry.putExtra(EXTRA_PARAM1, url);
            PendingIntent retryIntent = PendingIntent.getService(this, 0, retry, 0);
            mBuilder.setContentText(getResources().getString(R.string.could_not_get_image))
                    .addAction(R.drawable.ic_retry, getString(R.string.download_retry), retryIntent)
                    .setProgress(0, 0, false);
            notificationManager.notify(notificationId, mBuilder.build());
            return;
        }

        WallpaperManager myWallpaperManager
                = WallpaperManager.getInstance(getApplicationContext());
        String text = getResources().getString(R.string.could_not_get_image);
        if (resource != null) {
            try {
                myWallpaperManager.setBitmap(resource);
                text = getResources().getString(R.string.dwonload_complete);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        mBuilder.setContentText(text)
                .setProgress(0, 0, false)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(resource));
        notificationManager.notify(notificationId, mBuilder.build());
    }


    /*
            Handle downloading images to storage
     */
    private void handleDownloadWallpaperAction(String url, int code) {
        if (code == 2) {
            retrieveUrl(url, new OnRetrieveDownloadURL() {
                @Override
                public void onSuccess(String url) {
                    downloadImage(url);
                }

                @Override
                public void onFailed() {
                    urlDownloadFailed();
                }
            });
        } else {
            downloadImage(url);
        }
    }

    private void handleBulkDownload(List<String> urls) {
        for (String url : urls) {
            if (url.contains(UNSPLASH_COM)) {
                retrieveUrl(url, new OnRetrieveDownloadURL() {
                    @Override
                    public void onSuccess(String url) {
                        downloadImage(url);
                    }

                    @Override
                    public void onFailed() {
                        urlDownloadFailed();
                    }
                });
            } else {
                downloadImage(url);
            }
        }
    }

    private void downloadImage(String url) {
        Uri uri = Uri.parse(url);
        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle(getString(R.string.downloading_image));
        request.setVisibleInDownloadsUi(true);

        Boolean isSDPresent = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        Boolean isSDSupportedDevice = Environment.isExternalStorageRemovable();
        String dir = "/WallBay images";

        if (isSDSupportedDevice && isSDPresent) {
            // yes SD-card is present
            dir = Environment.getExternalStorageDirectory().toString() + dir;
        }

        String path = uri.getLastPathSegment();
        int index = path.lastIndexOf(".");
        if(index != -1) {
            String temp = path.substring(index).toLowerCase();
            if(!(temp.equals(".jpg") || temp.equals(".jpeg"))) {
                path += ".jpg";
            }
        } else {
            path += ".jpg";
        }
        request.setDestinationInExternalPublicDir(dir,  path);

        downloadManager.enqueue(request);
    }

    private void retrieveUrl(String u, final OnRetrieveDownloadURL listener) {
        String url = String.format("%s?client_id=%s", u, BuildConfig.UnsplashApiKey);
        OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()) {
                String temp = response.body().string();
                JSONObject json;
                try {
                    json = new JSONObject(temp);
                    listener.onSuccess(json.optString("url"));
                } catch (JSONException e) {
                    listener.onFailed();
                    e.printStackTrace();
                }
//                    downloadImage(json.optString("url"));
            } else {
                listener.onFailed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void urlDownloadFailed() {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        String CHANNEL_ID = "WALLPAPER_DOWNLOAD";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.wallpaper_download);
            String description = getString(R.string.notify_download);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager1 = getSystemService(NotificationManager.class);
            notificationManager1.createNotificationChannel(channel);

        }

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        mBuilder.setContentTitle(getString(R.string.wallpaper_download))
                .setColor(getResources().getColor(R.color.colorLightTransparent))
                .setContentText(getString(R.string.download_failed))
                .setSmallIcon(R.drawable.ic_error_black_24dp)
                .setPriority(NotificationCompat.PRIORITY_LOW);
        notificationManager.notify(notificationId, mBuilder.build());
    }

    interface OnRetrieveDownloadURL {
        void onSuccess(String url);
        void onFailed();
    }
}
