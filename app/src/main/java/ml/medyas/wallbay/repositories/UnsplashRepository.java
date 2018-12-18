package ml.medyas.wallbay.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import ml.medyas.wallbay.entities.ImageEntity;
import ml.medyas.wallbay.entities.unsplash.UnsplashPhotoEntity;
import ml.medyas.wallbay.network.unsplash.UnsplashCalls;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnsplashRepository {
    private UnsplashCalls unsplashService;

    public UnsplashRepository() {
        this.unsplashService = new UnsplashCalls();
    }

    public LiveData<List<ImageEntity>> getPhotos(String orderBy, int per_page, int page) {
        final MutableLiveData<List<ImageEntity>> data = new MutableLiveData<>();

        unsplashService.getPhotos(orderBy, per_page, page).enqueue(new Callback<List<UnsplashPhotoEntity>>() {
            @Override
            public void onResponse(Call<List<UnsplashPhotoEntity>> call, Response<List<UnsplashPhotoEntity>> response) {

            }

            @Override
            public void onFailure(Call<List<UnsplashPhotoEntity>> call, Throwable t) {

            }
        });

        return data;
    }

}
