package ml.medyas.wallbay.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import ml.medyas.wallbay.entities.ImageEntity;
import ml.medyas.wallbay.entities.pixabay.Hit;
import ml.medyas.wallbay.entities.pixabay.PixabayEntity;
import ml.medyas.wallbay.network.pixabay.PixabayCalls;
import ml.medyas.wallbay.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PixabayRepository {
    private PixabayCalls pixabayService;

    public PixabayRepository() {
        this.pixabayService = new PixabayCalls();
    }

    public LiveData<List<ImageEntity>> getSearch(String query, int page, String category, String colors, boolean editorsChoice, String orderBy) {
        final MutableLiveData<List<ImageEntity>> data = new MutableLiveData<>();

        pixabayService.getSearch(query, page, category, colors, editorsChoice, orderBy).enqueue(new Callback<PixabayEntity>() {
            @Override
            public void onResponse(@NonNull Call<PixabayEntity> call, @NonNull Response<PixabayEntity> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ImageEntity> list = new ArrayList<>();
                    for (Hit item : response.body().getHits()) {
                        ImageEntity imageEntity = new ImageEntity(
                                String.valueOf(item.getId()),
                                item.getUser(),
                                item.getUserImageURL(),
                                Utils.webSite.PIXABAY,
                                item.getLikes(),
                                item.getViews(),
                                item.getDownloads(),
                                item.getImageWidth(),
                                item.getImageHeight(),
                                item.getPageURL(),
                                item.getLargeImageURL(),
                                item.getWebformatURL(),
                                item.getTags()
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
            public void onFailure(@NonNull Call<PixabayEntity> call, @NonNull Throwable t) {
                ImageEntity item = new ImageEntity();
                item.setProvider(Utils.webSite.ERROR);
                List<ImageEntity> list = new ArrayList<>();
                list.add(item);
                data.setValue(list);
            }
        });

        return data;
    }
}
