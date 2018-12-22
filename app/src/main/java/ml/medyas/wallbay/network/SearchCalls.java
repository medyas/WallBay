package ml.medyas.wallbay.network;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function3;
import io.reactivex.schedulers.Schedulers;
import ml.medyas.wallbay.BuildConfig;
import ml.medyas.wallbay.entities.SearchEntity;
import ml.medyas.wallbay.entities.pexels.PexelsEntity;
import ml.medyas.wallbay.entities.pixabay.PixabayEntity;
import ml.medyas.wallbay.entities.unsplash.UnsplashSearchEntity;
import ml.medyas.wallbay.network.pexels.PexelsCalls;
import ml.medyas.wallbay.network.pexels.PexelsService;
import ml.medyas.wallbay.network.pixabay.PixabayCalls;
import ml.medyas.wallbay.network.pixabay.PixabayService;
import ml.medyas.wallbay.network.unsplash.UnsplashCalls;
import ml.medyas.wallbay.network.unsplash.UnsplashService;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchCalls {

    private Retrofit pexelsBuilder() {
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
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private Retrofit pixabayBuilder() {
        return new Retrofit.Builder()
                .baseUrl(PixabayCalls.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private Retrofit unsplashBuilder() {
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
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public Observable<SearchEntity> searchAll(String query, int page) {

        Observable<UnsplashSearchEntity> unsplashSearch = unsplashBuilder()
                .create(UnsplashService.class)
                .searchAll(query, 10, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        Observable<PixabayEntity> pixabaySearch = pixabayBuilder()
                .create(PixabayService.class)
                .searchAll(BuildConfig.PixabayApiKey, query, 10, page, "", "", false, "")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        Observable<PexelsEntity> pexelsSearch = pexelsBuilder()
                .create(PexelsService.class)
                .searchAll(query, 10, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        Observable<SearchEntity> combinedCalls = Observable.zip(pexelsSearch, pixabaySearch, unsplashSearch, new Function3<PexelsEntity, PixabayEntity, UnsplashSearchEntity, SearchEntity>() {
            @Override
            public SearchEntity apply(PexelsEntity pexelsEntity, PixabayEntity pixabayEntity, UnsplashSearchEntity unsplashSearchEntity) {
                return new SearchEntity(pixabayEntity, unsplashSearchEntity, pexelsEntity);
            }
        });

        return combinedCalls;
    }
}
