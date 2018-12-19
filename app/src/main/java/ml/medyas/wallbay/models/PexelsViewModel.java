package ml.medyas.wallbay.models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import ml.medyas.wallbay.entities.ImageEntity;
import ml.medyas.wallbay.repositories.PexelsRepository;

public class PexelsViewModel extends AndroidViewModel {
    private PexelsRepository pexelsRepo;

    public PexelsViewModel(Application application) {
        super(application);
        if (this.pexelsRepo == null) {
            this.pexelsRepo = new PexelsRepository(application.getApplicationContext());
        }
    }

    public LiveData<List<ImageEntity>> getPexelsSearch(String query, int page) {
        return pexelsRepo.getSearch(query, page);
    }

    public LiveData<List<ImageEntity>> getPexelsCurated(int page) {
        return pexelsRepo.getCurated(page);
    }
}
