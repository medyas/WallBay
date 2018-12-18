package ml.medyas.wallbay.network.pexels;

import ml.medyas.wallbay.entities.pexels.PexelsEntity;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class PexelsCalls {
    private static final String URL = "https://api.pexels.com/v1/";

    private Retrofit builder() {
        return new Retrofit.Builder()
                .baseUrl(PexelsCalls.URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }

    public Call<PexelsEntity> getSearch(String query, int perPager, int page) {
        return builder().create(PexelsService.class).
                search(query, perPager, page);
    }

    public Call<PexelsEntity> getCurated(int perPager, int page) {
        return builder().create(PexelsService.class).
                curated(perPager, page);
    }
}
