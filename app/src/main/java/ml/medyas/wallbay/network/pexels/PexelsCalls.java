package ml.medyas.wallbay.network.pexels;

import java.io.IOException;

import ml.medyas.wallbay.BuildConfig;
import ml.medyas.wallbay.entities.pexels.PexelsEntity;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static ml.medyas.wallbay.utils.Utils.REQUEST_SIZE;

public class PexelsCalls {
    private static final String URL = "https://api.pexels.com/v1/";

    private Retrofit builder() {
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", BuildConfig.PexelsApiKey)
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();
        return new Retrofit.Builder()
                .baseUrl(PexelsCalls.URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Call<PexelsEntity> getSearch(String query, int page) {
        return builder().create(PexelsService.class).
                search(query, REQUEST_SIZE, page);
    }

    public Call<PexelsEntity> getCurated(int page) {
        return builder().create(PexelsService.class).
                curated(REQUEST_SIZE, page);
    }
}
