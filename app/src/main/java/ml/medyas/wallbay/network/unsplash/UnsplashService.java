package ml.medyas.wallbay.network.unsplash;

import java.util.List;

import ml.medyas.wallbay.BuildConfig;
import ml.medyas.wallbay.entities.unsplash.UnsplashCollectionSearchEntity;
import ml.medyas.wallbay.entities.unsplash.UnsplashCollectionsEntity;
import ml.medyas.wallbay.entities.unsplash.UnsplashPhotoEntity;
import ml.medyas.wallbay.entities.unsplash.UnsplashSearchEntity;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UnsplashService {

    @Headers("Authorization : " + BuildConfig.UnsplashApiKey)
    @GET("/photos")
    Call<List<UnsplashPhotoEntity>> photos(@Query("order_by") String orderBy, @Query("per_pager") int perPager, @Query("pager") int page);

    @Headers("Authorization : " + BuildConfig.UnsplashApiKey)
    @GET("/search/photos")
    Call<UnsplashSearchEntity> search(@Query("query") String query, @Query("per_pager") int perPager, @Query("pager") int page);

    @Headers("Authorization : " + BuildConfig.UnsplashApiKey)
    @GET("/collections")
    Call<UnsplashCollectionsEntity> collections(@Query("per_pager") int perPager, @Query("pager") int page);

    @Headers("Authorization : " + BuildConfig.UnsplashApiKey)
    @GET("/collections/featured")
    Call<UnsplashCollectionsEntity> featuredCollections(@Query("per_pager") int perPager, @Query("pager") int page);

    @Headers("Authorization : " + BuildConfig.UnsplashApiKey)
    @GET("/search/collections")
    Call<UnsplashCollectionSearchEntity> searchCollections(@Query("query") String query, @Query("per_pager") int perPager, @Query("pager") int page);

    @Headers("Authorization : " + BuildConfig.UnsplashApiKey)
    @GET("/collections/{id}/photos")
    Call<List<UnsplashPhotoEntity>> collectionPhotos(@Path("id") int id, @Query("pager") int page, @Query("per_pager") int perPage);
}
