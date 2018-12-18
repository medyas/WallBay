package ml.medyas.wallbay.network.unsplash;

import java.util.List;

import ml.medyas.wallbay.entities.unsplash.UnsplashCollectionSearchEntity;
import ml.medyas.wallbay.entities.unsplash.UnsplashCollectionsEntity;
import ml.medyas.wallbay.entities.unsplash.UnsplashPhotoEntity;
import ml.medyas.wallbay.entities.unsplash.UnsplashSearchEntity;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UnsplashCalls {
    private static final String URL = "https://api.unsplash.com/";

    private Retrofit builder() {
        return new Retrofit.Builder()
                .baseUrl(UnsplashCalls.URL)
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

    public Call<UnsplashCollectionsEntity> getCollections(int perPager, int page) {
        return builder().create(UnsplashService.class).
                collections(perPager, page);
    }

    public Call<UnsplashCollectionsEntity> getFeaturedCollections(int perPager, int page) {
        return builder().create(UnsplashService.class).
                featuredCollections(perPager, page);
    }

    public Call<UnsplashCollectionSearchEntity> getSearchCollections(String query, int perPager, int page) {
        return builder().create(UnsplashService.class).
                searchCollections(query, perPager, page);
    }

    public Call<List<UnsplashPhotoEntity>> getCollectionPhotos(int id, int page, int perPage) {
        return builder().create(UnsplashService.class).
                collectionPhotos(id, page, perPage);
    }
}
