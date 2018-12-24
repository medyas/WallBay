package ml.medyas.wallbay.repositories;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import ml.medyas.wallbay.entities.ImageEntity;
import ml.medyas.wallbay.entities.pexels.PexelsEntity;
import ml.medyas.wallbay.entities.pexels.Photo;
import ml.medyas.wallbay.network.pexels.PexelsCalls;
import ml.medyas.wallbay.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PexelsRepository {
    private PexelsCalls pexelsService;

    public PexelsRepository(Context ctx) {
        if (this.pexelsService == null) {
            this.pexelsService = new PexelsCalls(ctx);
        }
    }


    public LiveData<List<ImageEntity>> getSearch(String query, int page) {
        final MutableLiveData<List<ImageEntity>> data = new MutableLiveData<>();

        pexelsService.getSearch(query, page).enqueue(new Callback<PexelsEntity>() {
            @Override
            public void onResponse(Call<PexelsEntity> call, Response<PexelsEntity> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ImageEntity> list = new ArrayList<>();
                    for (Photo item : response.body().getPhotos()) {
                        ImageEntity imageEntity = new ImageEntity(
                                String.valueOf(item.getId()),
                                item.getPhotographer(),
                                null,
                                Utils.webSite.PEXELS,
                                0,
                                0,
                                0,
                                item.getWidth(),
                                item.getHeight(),
                                item.getUrl(),
                                item.getSrc().getOriginal(),
                                item.getSrc().getOriginal(), item.getSrc().getLarge(),
                                null
                        );
                        list.add(imageEntity);
                    }

                    data.setValue(list);
                } else {
                    ImageEntity item = new ImageEntity();
                    item.setProvider(Utils.webSite.EMPTY);
                    List<ImageEntity> list = new ArrayList<>();
                    list.add(item);
                    data.setValue(list);
                }
            }

            @Override
            public void onFailure(Call<PexelsEntity> call, Throwable t) {
                ImageEntity item = new ImageEntity();
                item.setProvider(Utils.webSite.EMPTY);
                List<ImageEntity> list = new ArrayList<>();
                list.add(item);
                data.setValue(list);
            }
        });

        return data;
    }


    public LiveData<List<ImageEntity>> getCurated(int page) {
        final MutableLiveData<List<ImageEntity>> data = new MutableLiveData<>();

        pexelsService.getCurated(page).enqueue(new Callback<PexelsEntity>() {
            @Override
            public void onResponse(Call<PexelsEntity> call, Response<PexelsEntity> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ImageEntity> list = new ArrayList<>();
                    for (Photo item : response.body().getPhotos()) {
                        ImageEntity imageEntity = new ImageEntity(
                                String.valueOf(item.getId()),
                                item.getPhotographer(),
                                null,
                                Utils.webSite.PEXELS,
                                0,
                                0,
                                0,
                                item.getWidth(),
                                item.getHeight(),
                                item.getUrl(),
                                item.getSrc().getOriginal(),
                                item.getSrc().getOriginal(), item.getSrc().getLarge(),
                                null
                        );
                        list.add(imageEntity);
                    }

                    data.setValue(list);
                } else {
                    ImageEntity item = new ImageEntity();
                    item.setProvider(Utils.webSite.EMPTY);
                    List<ImageEntity> list = new ArrayList<>();
                    list.add(item);
                    data.setValue(list);
                }
            }

            @Override
            public void onFailure(Call<PexelsEntity> call, Throwable t) {
                ImageEntity item = new ImageEntity();
                item.setProvider(Utils.webSite.EMPTY);
                List<ImageEntity> list = new ArrayList<>();
                list.add(item);
                data.setValue(list);
            }
        });

        return data;
    }
}
