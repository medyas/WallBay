package ml.medyas.wallbay.network.pexels;

import ml.medyas.wallbay.BuildConfig;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface PexelsService {

    @Headers("Authorization : " + BuildConfig.PexelsApiKey)
    @GET("/search")
    Call<String> search(@Query("query") String query, @Query("per_pager") int perPager, @Query("pager") int page);


    @Headers("Authorization : " + BuildConfig.PexelsApiKey)
    @GET("/curated")
    Call<String> curated(@Query("per_pager") int perPager, @Query("pager") int page);
}
