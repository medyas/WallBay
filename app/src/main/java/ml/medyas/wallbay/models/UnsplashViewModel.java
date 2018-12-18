package ml.medyas.wallbay.models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import ml.medyas.wallbay.entities.CollectionEntity;
import ml.medyas.wallbay.entities.ImageEntity;
import ml.medyas.wallbay.repositories.UnsplashRepository;

public class UnsplashViewModel extends ViewModel {
    private UnsplashRepository unsplashRepo;

    public UnsplashViewModel() {
        if (this.unsplashRepo == null) {
            this.unsplashRepo = new UnsplashRepository();
        }
    }

    public LiveData<List<ImageEntity>> getUnsplashPhotos(String orderBy, int per_page, int page) {
        return unsplashRepo.getPhotos(orderBy, per_page, page);
    }

    public LiveData<List<ImageEntity>> getUnsplashSearch(String query, int per_page, int page) {
        return unsplashRepo.getSearch(query, per_page, page);
    }

    public LiveData<List<CollectionEntity>> getUnsplashCollections(int per_page, int page) {
        return unsplashRepo.getCollections(per_page, page);
    }

    public LiveData<List<CollectionEntity>> getUnsplashFeaturedCollections(int per_page, int page) {
        return unsplashRepo.getFeaturedCollections(per_page, page);
    }

    public LiveData<List<CollectionEntity>> getUnsplashSearchCollections(String query, int per_page, int page) {
        return unsplashRepo.getSearchCollections(query, per_page, page);
    }

    public LiveData<List<ImageEntity>> getUnsplashCollectionPhoto(int id, int per_page, int page) {
        return unsplashRepo.getCollectionPhoto(id, per_page, page);
    }
}
