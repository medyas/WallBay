package ml.medyas.wallbay.network.pixabay;

import android.content.Context;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import ml.medyas.wallbay.BuildConfig;
import ml.medyas.wallbay.entities.pixabay.PixabayEntity;
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

public class PixabayCalls {
    public static final String URL = "https://pixabay.com/api/";
    private Context ctx;

    public PixabayCalls(Context ctx) {
        this.ctx = ctx;
    }

    private Retrofit builder() {
        int cacheSize = 5 * 1024 * 1024; // 5 MB
        final CacheControl cacheControl = new CacheControl.Builder().maxAge(24, TimeUnit.HOURS).build();
        Cache cache = new Cache(ctx.getCacheDir(), cacheSize);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Cache-Control", cacheControl.toString())
                        .build();
                return chain.proceed(newRequest);
            }
        })
                .cache(cache)
                .build();

        return new Retrofit.Builder()
                .baseUrl(PixabayCalls.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    public Call<PixabayEntity> getSearch(String query, int page, String category, String colors, boolean editorsChoice, String orderBy) {
        return builder().create(PixabayService.class).
                search(BuildConfig.PixabayApiKey, query, REQUEST_SIZE, page, category, colors, editorsChoice, orderBy);
    }
}
