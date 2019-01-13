package ml.medyas.wallbay.repositories;

import android.content.Context;

import ml.medyas.wallbay.entities.pixabay.PixabayEntity;
import ml.medyas.wallbay.network.pixabay.PixabayCalls;
import retrofit2.Call;

public class PixabayRepository {
    private PixabayCalls pixabayService;

    public PixabayRepository(Context ctx) {
        this.pixabayService = new PixabayCalls(ctx);
    }

    public Call<PixabayEntity> getSearch(String query, int page, String category, String colors, boolean editorsChoice, String orderBy) {
        return pixabayService.getSearch(query, page, category, colors, editorsChoice, orderBy);
    }
}
