package ml.medyas.wallbay.network.unsplash;

import ml.medyas.wallbay.BuildConfig;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UnsplashService {

    @Headers("Authorization : " + BuildConfig.UnsplashApiKey)
    @GET("/photos")
    Call<String> photos(@Query("order_by") String orderBy, @Query("per_pager") int perPager, @Query("pager") int page);

    @Headers("Authorization : " + BuildConfig.UnsplashApiKey)
    @GET("/search/photos")
    Call<String> search(@Query("query") String query, @Query("per_pager") int perPager, @Query("pager") int page);

    @Headers("Authorization : " + BuildConfig.UnsplashApiKey)
    @GET("/collections")
    Call<String> collections(@Query("per_pager") int perPager, @Query("pager") int page);

    @Headers("Authorization : " + BuildConfig.UnsplashApiKey)
    @GET("/collections/featured")
    Call<String> featuredCollections(@Query("per_pager") int perPager, @Query("pager") int page);

    @Headers("Authorization : " + BuildConfig.UnsplashApiKey)
    @GET("/search/collections")
    Call<String> searchCollections(@Query("query") String query, @Query("per_pager") int perPager, @Query("pager") int page);

    @Headers("Authorization : " + BuildConfig.UnsplashApiKey)
    @GET("/collections/{id}/photos")
    Call<String> collectionPhotos(@Path("id") String id);
}
