package ml.medyas.wallbay.adapters.pixabay;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PageKeyedDataSource;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import ml.medyas.wallbay.entities.ImageEntity;
import ml.medyas.wallbay.entities.pixabay.Hit;
import ml.medyas.wallbay.entities.pixabay.PixabayEntity;
import ml.medyas.wallbay.repositories.PixabayRepository;
import ml.medyas.wallbay.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PixabayDataSource extends PageKeyedDataSource<Integer, ImageEntity> {
    private PixabayRepository pixabayRepository;
    private MutableLiveData<Utils.NetworkState> networkState;
    private String query;
    private String category;
    private String colors;
    private boolean editorsChoice;
    private String orderBy;


    public PixabayDataSource(Context context, String query, String category, String colors, boolean editorsChoice, String orderBy) {
        pixabayRepository = new PixabayRepository(context);
        networkState = new MutableLiveData<>();
        this.query = query;
        this.category = category;
        this.colors = colors;
        this.editorsChoice = editorsChoice;
        this.orderBy = orderBy;
    }

    public MutableLiveData<Utils.NetworkState> getNetworkState() {
        return networkState;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, ImageEntity> callback) {
        networkState.postValue(Utils.NetworkState.LOADING);
        pixabayRepository.getSearch(query, 1, category, colors, editorsChoice, orderBy).enqueue(new Callback<PixabayEntity>() {
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
                                item.getImageURL(),
                                item.getImageURL(), item.getWebformatURL(),
                                item.getTags()
                        );
                        list.add(imageEntity);
                    }
                    callback.onResult(list, null, 2);
                    networkState.postValue(Utils.NetworkState.LOADED);

                } else {
                    ImageEntity item = new ImageEntity();
                    item.setProvider(Utils.webSite.EMPTY);
                    List<ImageEntity> list = new ArrayList<>();
                    list.add(item);
                    callback.onResult(list, null, null);
                    networkState.postValue(Utils.NetworkState.EMPTY);
                }
            }

            @Override
            public void onFailure(@NonNull Call<PixabayEntity> call, @NonNull Throwable t) {
                networkState.postValue(Utils.NetworkState.FAILED);
            }
        });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, ImageEntity> callback) {
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, ImageEntity> callback) {
        networkState.postValue(Utils.NetworkState.LOADING);
        pixabayRepository.getSearch(query, params.key, category, colors, editorsChoice, orderBy).enqueue(new Callback<PixabayEntity>() {
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
                                item.getImageURL(),
                                item.getImageURL(), item.getWebformatURL(),
                                item.getTags()
                        );
                        list.add(imageEntity);
                    }
                    callback.onResult(list, params.key+1);
                    networkState.postValue(Utils.NetworkState.LOADED);

                } else {
                    ImageEntity item = new ImageEntity();
                    item.setProvider(Utils.webSite.EMPTY);
                    List<ImageEntity> list = new ArrayList<>();
                    list.add(item);
                    callback.onResult(list, null);
                    networkState.postValue(Utils.NetworkState.EMPTY);
                }
            }

            @Override
            public void onFailure(@NonNull Call<PixabayEntity> call, @NonNull Throwable t) {
                networkState.postValue(Utils.NetworkState.FAILED);
            }
        });
    }
}
