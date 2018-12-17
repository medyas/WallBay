package ml.medyas.wallbay.network.pixabay;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class PixabayCalls {
    private static final String URL = "https://pixabay.com/api/";

    private Retrofit builder() {
        return new Retrofit.Builder()
                .baseUrl(PixabayCalls.URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }

    public Call<String> getSearch(String query, int perPager, int page, String category, String colors, boolean editorsChoice, String orderBy) {
        return builder().create(PixabayService.class).
                search(query, perPager, page, category, colors, editorsChoice, orderBy);
    }
}
