package ml.medyas.wallbay.network.unsplash;

import android.content.Context;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ml.medyas.wallbay.BuildConfig;
import ml.medyas.wallbay.entities.unsplash.UnsplashCollectionSearchEntity;
import ml.medyas.wallbay.entities.unsplash.UnsplashCollectionsEntity;
import ml.medyas.wallbay.entities.unsplash.UnsplashPhotoEntity;
import ml.medyas.wallbay.entities.unsplash.UnsplashSearchEntity;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static ml.medyas.wallbay.utils.Utils.REQUEST_SIZE;

public class UnsplashCalls {
    private static final String URL = "https://api.unsplash.com/";
    private Context ctx;

    public UnsplashCalls(Context ctx) {
        this.ctx = ctx;
    }

    private Retrofit builder() {
        int cacheSize = 5 * 1024 * 1024; // 5 MB
        final CacheControl cacheControl = new CacheControl.Builder().maxAge(12, TimeUnit.HOURS).build();
        Cache cache = new Cache(ctx.getCacheDir(), cacheSize);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "Client-ID " + BuildConfig.UnsplashApiKey)
                        .addHeader("Cache-Control", cacheControl.toString())
                        .build();
                return chain.proceed(newRequest);
            }
        })
                .cache(cache)
                .build();

        return new Retrofit.Builder()
                .baseUrl(UnsplashCalls.URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Call<List<UnsplashPhotoEntity>> getPhotos(String orderBy, int page) {
        return builder().create(UnsplashService.class).
                photos(orderBy, REQUEST_SIZE, page);
    }

    public Call<UnsplashSearchEntity> getSearch(String query, int page) {
        return builder().create(UnsplashService.class).
                search(query, REQUEST_SIZE, page);
    }

    public Call<List<UnsplashCollectionsEntity>> getCollections(int page) {
        return builder().create(UnsplashService.class).
                collections(REQUEST_SIZE, page);
    }

    public Call<List<UnsplashCollectionsEntity>> getFeaturedCollections(int page) {
        return builder().create(UnsplashService.class).
                featuredCollections(REQUEST_SIZE, page);
    }

    public Call<UnsplashCollectionSearchEntity> getSearchCollections(String query, int page) {
        return builder().create(UnsplashService.class).
                searchCollections(query, REQUEST_SIZE, page);
    }

    public Call<List<UnsplashPhotoEntity>> getCollectionPhotos(int id, int page) {
        return builder().create(UnsplashService.class).
                collectionPhotos(id, REQUEST_SIZE, page);
    }
}
