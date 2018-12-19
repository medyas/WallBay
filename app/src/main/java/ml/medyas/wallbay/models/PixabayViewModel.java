package ml.medyas.wallbay.models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import ml.medyas.wallbay.entities.ImageEntity;
import ml.medyas.wallbay.repositories.PixabayRepository;

public class PixabayViewModel extends AndroidViewModel {
    private PixabayRepository pixabayRepo;

    public PixabayViewModel(Application application) {
        super(application);
        if (this.pixabayRepo == null) {
            this.pixabayRepo = new PixabayRepository(application.getApplicationContext());
        }
    }

    public LiveData<List<ImageEntity>> getPixabaySearch(String query, int page, String category, String colors, boolean editorsChoice, String orderBy) {
        return pixabayRepo.getSearch(query, page, category, colors, editorsChoice, orderBy);
    }

}
