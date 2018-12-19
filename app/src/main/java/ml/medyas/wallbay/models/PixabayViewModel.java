package ml.medyas.wallbay.models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import ml.medyas.wallbay.entities.ImageEntity;
import ml.medyas.wallbay.repositories.PixabayRepository;

public class PixabayViewModel extends ViewModel {
    private PixabayRepository pixabayRepo;

    public PixabayViewModel() {
        if (this.pixabayRepo == null) {
            this.pixabayRepo = new PixabayRepository();
        }
    }

    public LiveData<List<ImageEntity>> getPixabaySearch(String query, int page, String category, String colors, boolean editorsChoice, String orderBy) {
        return pixabayRepo.getSearch(query, page, category, colors, editorsChoice, orderBy);
    }

}
