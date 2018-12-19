package ml.medyas.wallbay.models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import ml.medyas.wallbay.entities.ImageEntity;
import ml.medyas.wallbay.repositories.PexelsRepository;

public class PexelsViewModel extends ViewModel {
    private PexelsRepository pexelsRepo;

    public PexelsViewModel() {
        if (this.pexelsRepo == null) {
            this.pexelsRepo = new PexelsRepository();
        }
    }

    public LiveData<List<ImageEntity>> getPexelsSearch(String query, int page) {
        return pexelsRepo.getSearch(query, page);
    }

    public LiveData<List<ImageEntity>> getPexelsCurated(int page) {
        return pexelsRepo.getCurated(page);
    }
}
