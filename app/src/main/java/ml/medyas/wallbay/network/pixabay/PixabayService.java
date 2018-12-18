package ml.medyas.wallbay.network.pixabay;

import ml.medyas.wallbay.BuildConfig;
import ml.medyas.wallbay.entities.pixabay.PixabayEntity;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PixabayService {

    @GET("/?key=" + BuildConfig.PixabayApiKey)
    Call<PixabayEntity> search(@Query("q") String query,
                               @Query("per_page") int perPager,
                               @Query("page") int page,
                               @Query("category") String category,
                               @Query("colors") String colors,
                               @Query("editors_choice") boolean editorsChoice,
                               @Query("order") String orderBy);
}
