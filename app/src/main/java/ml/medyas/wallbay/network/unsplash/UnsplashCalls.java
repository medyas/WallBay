package ml.medyas.wallbay.network.unsplash;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class UnsplashCalls {
    private static final String URL = "https://api.unsplash.com/";

    private Retrofit builder() {
        return new Retrofit.Builder()
                .baseUrl(UnsplashCalls.URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }

    public Call<String> getPhotos(String orderBy, int perPager, int page) {
        return builder().create(UnsplashService.class).
                photos(orderBy, perPager, page);
    }

    public Call<String> getSearch(String query, int perPager, int page) {
        return builder().create(UnsplashService.class).
                search(query, perPager, page);
    }

    public Call<String> getCollections(int perPager, int page) {
        return builder().create(UnsplashService.class).
                collections(perPager, page);
    }

    public Call<String> getFeaturedCollections(int perPager, int page) {
        return builder().create(UnsplashService.class).
                featuredCollections(perPager, page);
    }

    public Call<String> getSearchCollections(String query, int perPager, int page) {
        return builder().create(UnsplashService.class).
                searchCollections(query, perPager, page);
    }

    public Call<String> getCollectionPhotos(String id) {
        return builder().create(UnsplashService.class).
                collectionPhotos(id);
    }
}
