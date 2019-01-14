package ml.medyas.wallbay.repositories;

import android.content.Context;

import java.util.List;

import ml.medyas.wallbay.entities.unsplash.UnsplashCollectionSearchEntity;
import ml.medyas.wallbay.entities.unsplash.UnsplashCollectionsEntity;
import ml.medyas.wallbay.entities.unsplash.UnsplashPhotoEntity;
import ml.medyas.wallbay.entities.unsplash.UnsplashSearchEntity;
import ml.medyas.wallbay.network.unsplash.UnsplashCalls;
import retrofit2.Call;

public class UnsplashRepository {
    private UnsplashCalls unsplashService;

    public UnsplashRepository(Context ctx) {
        this.unsplashService = new UnsplashCalls(ctx);
    }

    public Call<List<UnsplashPhotoEntity>> getPhotos(String orderBy, int page) {
        return unsplashService.getPhotos(orderBy, page);
    }

    public Call<UnsplashSearchEntity> getSearch(String query, int page) {
        return unsplashService.getSearch(query, page);

    }

    public Call<List<UnsplashCollectionsEntity>> getCollections(int page) {
        return unsplashService.getCollections(page);
    }

    public Call<List<UnsplashCollectionsEntity>> getFeaturedCollections(int page) {
        return unsplashService.getFeaturedCollections(page);
    }

    public Call<UnsplashCollectionSearchEntity> getSearchCollections(String query, int page) {
        return unsplashService.getSearchCollections(query, page);
    }

    public Call<List<UnsplashPhotoEntity>> getCollectionPhoto(int id, int page) {
        return unsplashService.getCollectionPhotos(id, page);
    }

}
