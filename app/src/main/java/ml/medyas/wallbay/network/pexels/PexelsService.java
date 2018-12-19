package ml.medyas.wallbay.network.pexels;

import ml.medyas.wallbay.entities.pexels.PexelsEntity;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PexelsService {

    @GET("/search")
    Call<PexelsEntity> search(@Query("query") String query, @Query("per_page") int perPager, @Query("page") int page);

    @GET("/curated")
    Call<PexelsEntity> curated(@Query("per_page") int perPager, @Query("page") int page);
}
