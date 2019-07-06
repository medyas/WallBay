package ml.medyas.wallbay.repositories;


import android.content.Context;

import ml.medyas.wallbay.models.pexels.PexelsEntity;
import ml.medyas.wallbay.network.pexels.PexelsCalls;
import retrofit2.Call;

public class PexelsRepository {
    private PexelsCalls pexelsService;

    public PexelsRepository(Context ctx) {
        if (this.pexelsService == null) {
            this.pexelsService = new PexelsCalls(ctx);
        }
    }


    public  Call<PexelsEntity> getPopular(int page) {
        return pexelsService.getPopular(page);
    }

    public Call<PexelsEntity> getSearch(String query, int page) {
        return pexelsService.getSearch(query, page);
    }


    public Call<PexelsEntity> getCurated(int page) {
        return pexelsService.getCurated(page);
    }
}
