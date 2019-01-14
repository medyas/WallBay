package ml.medyas.wallbay.adapters.unsplash;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.arch.paging.PageKeyedDataSource;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import ml.medyas.wallbay.entities.CollectionEntity;
import ml.medyas.wallbay.entities.unsplash.UnsplashCollectionsEntity;
import ml.medyas.wallbay.repositories.UnsplashRepository;
import ml.medyas.wallbay.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnsplashCollectionsDataSource extends PageKeyedDataSource<Integer, CollectionEntity> {
    private UnsplashRepository unsplashRepository;
    private MutableLiveData<Utils.NetworkState> networkState;

    public UnsplashCollectionsDataSource(Context context) {
        this.networkState = new MutableLiveData<>();
        this.unsplashRepository = new UnsplashRepository(context);
    }

    public MutableLiveData<Utils.NetworkState> getNetworkState() {
        return networkState;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, CollectionEntity> callback) {
        networkState.postValue(Utils.NetworkState.LOADING);
        unsplashRepository.getCollections(1).enqueue(new Callback<List<UnsplashCollectionsEntity>>() {
            @Override
            public void onResponse(Call<List<UnsplashCollectionsEntity>> call, Response<List<UnsplashCollectionsEntity>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CollectionEntity> list = new ArrayList<>();
                    for (UnsplashCollectionsEntity item : response.body()) {
                        CollectionEntity collectionEntity = new CollectionEntity(
                                item.getId(),
                                item.getTitle(),
                                item.getTotalPhotos(),
                                item.getTags(),
                                item.getUser().getUsername(),
                                item.getUser().getLinks().getHtml(),
                                item.getPreviewPhotos()
                        );
                        list.add(collectionEntity);
                    }
                    callback.onResult(list, null, 2);
                    networkState.postValue(Utils.NetworkState.LOADED);

                } else {
                    CollectionEntity item = new CollectionEntity();
                    List<CollectionEntity> list = new ArrayList<>();
                    list.add(item);

                    callback.onResult(list, null, null);
                    networkState.postValue(Utils.NetworkState.EMPTY);
                }
            }

            @Override
            public void onFailure(Call<List<UnsplashCollectionsEntity>> call, Throwable t) {
                CollectionEntity item = new CollectionEntity();
                List<CollectionEntity> list = new ArrayList<>();
                list.add(item);

                callback.onResult(list, null, null);
                networkState.postValue(Utils.NetworkState.FAILED);
            }
        });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, CollectionEntity> callback) {

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, CollectionEntity> callback) {
        networkState.postValue(Utils.NetworkState.LOADING);
        unsplashRepository.getCollections(params.key).enqueue(new Callback<List<UnsplashCollectionsEntity>>() {
            @Override
            public void onResponse(Call<List<UnsplashCollectionsEntity>> call, Response<List<UnsplashCollectionsEntity>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CollectionEntity> list = new ArrayList<>();
                    for (UnsplashCollectionsEntity item : response.body()) {
                        CollectionEntity collectionEntity = new CollectionEntity(
                                item.getId(),
                                item.getTitle(),
                                item.getTotalPhotos(),
                                item.getTags(),
                                item.getUser().getUsername(),
                                item.getUser().getLinks().getHtml(),
                                item.getPreviewPhotos()
                        );
                        list.add(collectionEntity);
                    }
                    callback.onResult(list, params.key+1);
                    networkState.postValue(Utils.NetworkState.LOADED);

                } else {
                    CollectionEntity item = new CollectionEntity();
                    List<CollectionEntity> list = new ArrayList<>();
                    list.add(item);

                    callback.onResult(list, null);
                    networkState.postValue(Utils.NetworkState.EMPTY);
                }
            }

            @Override
            public void onFailure(Call<List<UnsplashCollectionsEntity>> call, Throwable t) {
                CollectionEntity item = new CollectionEntity();
                List<CollectionEntity> list = new ArrayList<>();
                list.add(item);

                callback.onResult(list, null);
                networkState.postValue(Utils.NetworkState.FAILED);
            }
        });
    }

    public static class UnsplashCollectionsDataSourceFactory extends DataSource.Factory {
        private Context context;
        private MutableLiveData<UnsplashCollectionsDataSource> mutableLiveData;

        public UnsplashCollectionsDataSourceFactory(Context context) {
            this.context = context;
            mutableLiveData = new MutableLiveData<>();
        }

        public MutableLiveData<UnsplashCollectionsDataSource> getMutableLiveData() {
            return mutableLiveData;
        }

        @Override
        public DataSource create() {
            UnsplashCollectionsDataSource unsplashCollectionsDataSource = new UnsplashCollectionsDataSource(context);
            mutableLiveData.postValue(unsplashCollectionsDataSource);
            return unsplashCollectionsDataSource;
        }
    }
}
