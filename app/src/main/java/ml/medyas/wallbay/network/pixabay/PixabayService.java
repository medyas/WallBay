package ml.medyas.wallbay.network.pixabay;

import ml.medyas.wallbay.BuildConfig;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PixabayService {

    @GET("/?key=" + BuildConfig.PixabayApiKey)
    Call<String> search(@Query("q") String query,
                        @Query("per_pager") int perPager,
                        @Query("pager") int page,
                        @Query("category") String category,
                        @Query("colors") String colors,
                        @Query("editors_choice") boolean editorsChoice,
                        @Query("order") String orderBy);
}
