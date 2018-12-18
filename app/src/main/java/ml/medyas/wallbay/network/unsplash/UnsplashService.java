package ml.medyas.wallbay.network.unsplash;

import java.util.List;

import ml.medyas.wallbay.entities.unsplash.UnsplashCollectionSearchEntity;
import ml.medyas.wallbay.entities.unsplash.UnsplashCollectionsEntity;
import ml.medyas.wallbay.entities.unsplash.UnsplashPhotoEntity;
import ml.medyas.wallbay.entities.unsplash.UnsplashSearchEntity;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UnsplashService {

    @GET("photos")
    Call<List<UnsplashPhotoEntity>> photos(@Query("order_by") String orderBy, @Query("per_page") int perPager, @Query("page") int page);

    @GET("search/photos")
    Call<UnsplashSearchEntity> search(@Query("query") String query, @Query("per_page") int perPager, @Query("page") int page);

    @GET("collections")
    Call<List<UnsplashCollectionsEntity>> collections(@Query("per_page") int perPager, @Query("page") int page);

    @GET("collections/featured")
    Call<List<UnsplashCollectionsEntity>> featuredCollections(@Query("per_page") int perPager, @Query("page") int page);

    @GET("search/collections")
    Call<UnsplashCollectionSearchEntity> searchCollections(@Query("query") String query, @Query("per_page") int perPager, @Query("page") int page);

    @GET("collections/{id}/photos")
    Call<List<UnsplashPhotoEntity>> collectionPhotos(@Path("id") int id, @Query("per_page") int perPage, @Query("page") int page);
}
