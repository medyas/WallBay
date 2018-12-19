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

    public LiveData<List<ImageEntity>> getUnsplashPhotos(String orderBy, int page) {
        return unsplashRepo.getPhotos(orderBy, page);
    }

    public LiveData<List<ImageEntity>> getUnsplashSearch(String query, int page) {
        return unsplashRepo.getSearch(query, page);
    }

    public LiveData<List<CollectionEntity>> getUnsplashCollections(int page) {
        return unsplashRepo.getCollections(page);
    }

    public LiveData<List<CollectionEntity>> getUnsplashFeaturedCollections(int page) {
        return unsplashRepo.getFeaturedCollections(page);
    }

    public LiveData<List<CollectionEntity>> getUnsplashSearchCollections(String query, int page) {
        return unsplashRepo.getSearchCollections(query, page);
    }

    public LiveData<List<ImageEntity>> getUnsplashCollectionPhoto(int id, int page) {
        return unsplashRepo.getCollectionPhoto(id, page);
    }
}
