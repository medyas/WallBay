package ml.medyas.wallbay.adapters.pexels;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import java.util.ArrayList;
import java.util.List;

import ml.medyas.wallbay.models.ImageEntity;
import ml.medyas.wallbay.models.pexels.PexelsEntity;
import ml.medyas.wallbay.models.pexels.Photo;
import ml.medyas.wallbay.repositories.PexelsRepository;
import ml.medyas.wallbay.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PexelsDataSource extends PageKeyedDataSource<Integer, ImageEntity> {
    private PexelsRepository pexelsRepository;
    private String query;
    private MutableLiveData<Utils.NetworkState> networkState;

    public PexelsDataSource(Context context) {
        pexelsRepository = new PexelsRepository(context);
        networkState = new MutableLiveData<>();
    }

    public MutableLiveData<Utils.NetworkState> getNetworkState() {
        return networkState;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, ImageEntity> callback) {
        networkState.postValue(Utils.NetworkState.LOADING);
        pexelsRepository.getPopular(1).enqueue(new Callback<PexelsEntity>() {
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
                                item.getSrc().getOriginal(),
                                item.getSrc().getLarge(),
                                ""
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
            public void onFailure(Call<PexelsEntity> call, Throwable t) {
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
        pexelsRepository.getPopular(params.key).enqueue(new Callback<PexelsEntity>() {
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
                                item.getSrc().getOriginal(),
                                item.getSrc().getLarge(),
                                ""
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
            public void onFailure(Call<PexelsEntity> call, Throwable t) {
                networkState.postValue(Utils.NetworkState.FAILED);
            }
        });
    }


    public static class PexelsDataSourceFactory extends DataSource.Factory {
        private Context context;
        private PexelsDataSource dataSource;
        private MutableLiveData<PexelsDataSource> mutableLiveData;

        public PexelsDataSourceFactory(Context context) {
            this.context = context;
            mutableLiveData = new MutableLiveData<>();
        }

        @Override
        public DataSource create() {
            dataSource = new PexelsDataSource(context);
            mutableLiveData.postValue(dataSource);
            return dataSource;
        }

        public MutableLiveData<PexelsDataSource> getMutableLiveData() {
            return mutableLiveData;
        }
    }
}
