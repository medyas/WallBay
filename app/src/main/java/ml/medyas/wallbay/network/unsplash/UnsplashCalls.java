package ml.medyas.wallbay.network.unsplash;

import java.io.IOException;
import java.util.List;

import ml.medyas.wallbay.BuildConfig;
import ml.medyas.wallbay.entities.unsplash.UnsplashCollectionSearchEntity;
import ml.medyas.wallbay.entities.unsplash.UnsplashCollectionsEntity;
import ml.medyas.wallbay.entities.unsplash.UnsplashPhotoEntity;
import ml.medyas.wallbay.entities.unsplash.UnsplashSearchEntity;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UnsplashCalls {
    private static final String URL = "https://api.unsplash.com/";

    private Retrofit builder() {
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "Client-ID " + BuildConfig.UnsplashApiKey)
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();
        return new Retrofit.Builder()
                .baseUrl(UnsplashCalls.URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Call<List<UnsplashPhotoEntity>> getPhotos(String orderBy, int perPager, int page) {
        return builder().create(UnsplashService.class).
                photos(orderBy, perPager, page);
    }

    public Call<UnsplashSearchEntity> getSearch(String query, int perPager, int page) {
        return builder().create(UnsplashService.class).
                search(query, perPager, page);
    }

    public Call<List<UnsplashCollectionsEntity>> getCollections(int perPager, int page) {
        return builder().create(UnsplashService.class).
                collections(perPager, page);
    }

    public Call<List<UnsplashCollectionsEntity>> getFeaturedCollections(int perPager, int page) {
        return builder().create(UnsplashService.class).
                featuredCollections(perPager, page);
    }

    public Call<UnsplashCollectionSearchEntity> getSearchCollections(String query, int perPager, int page) {
        return builder().create(UnsplashService.class).
                searchCollections(query, perPager, page);
    }

    public Call<List<UnsplashPhotoEntity>> getCollectionPhotos(int id, int perPage, int page) {
        return builder().create(UnsplashService.class).
                collectionPhotos(id, perPage, page);
    }
}
