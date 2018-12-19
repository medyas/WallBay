package ml.medyas.wallbay.network.pixabay;

import io.reactivex.Observable;
import ml.medyas.wallbay.entities.pixabay.PixabayEntity;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PixabayService {

    @GET(".")
    Call<PixabayEntity> search(@Query("key") String key,
                               @Query("q") String query,
                               @Query("per_page") int perPager,
                               @Query("page") int page,
                               @Query("category") String category,
                               @Query("colors") String colors,
                               @Query("editors_choice") boolean editorsChoice,
                               @Query("order") String orderBy);

    @GET(".")
    Observable<PixabayEntity> searchAll(@Query("key") String key,
                                        @Query("q") String query,
                                        @Query("per_page") int perPager,
                                        @Query("page") int page,
                                        @Query("category") String category,
                                        @Query("colors") String colors,
                                        @Query("editors_choice") boolean editorsChoice,
                                        @Query("order") String orderBy);
}
