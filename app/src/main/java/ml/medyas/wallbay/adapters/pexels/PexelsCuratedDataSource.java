package ml.medyas.wallbay.adapters.pexels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.arch.paging.PageKeyedDataSource;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import ml.medyas.wallbay.entities.ImageEntity;
import ml.medyas.wallbay.entities.pexels.PexelsEntity;
import ml.medyas.wallbay.entities.pexels.Photo;
import ml.medyas.wallbay.repositories.PexelsRepository;
import ml.medyas.wallbay.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PexelsCuratedDataSource extends PageKeyedDataSource<Integer, ImageEntity> {
    private PexelsRepository pexelsRepository;
    private MutableLiveData<Utils.NetworkState> networkState;


    public PexelsCuratedDataSource(Context context) {
        pexelsRepository = new PexelsRepository(context);
        networkState = new MutableLiveData<>();
    }

    public MutableLiveData<Utils.NetworkState> getNetworkState() {
        return networkState;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, ImageEntity> callback) {
        pexelsRepository.getCurated(1).enqueue(new Callback<PexelsEntity>() {
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
        pexelsRepository.getCurated(params.key).enqueue(new Callback<PexelsEntity>() {
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


    public static class PexelsCuratedDataSourceFactory extends DataSource.Factory {
        private Context context;
        private PexelsCuratedDataSource dataSource;
        private MutableLiveData<PexelsCuratedDataSource> mutableLiveData;

        public PexelsCuratedDataSourceFactory(Context context) {
            this.context = context;
            mutableLiveData = new MutableLiveData<>();
        }

        @Override
        public DataSource create() {
            dataSource = new PexelsCuratedDataSource(context);
            mutableLiveData.postValue(dataSource);
            return dataSource;
        }

        public MutableLiveData<PexelsCuratedDataSource> getMutableLiveData() {
            return mutableLiveData;
        }
    }
}
