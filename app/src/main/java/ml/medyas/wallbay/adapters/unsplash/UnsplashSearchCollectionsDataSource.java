package ml.medyas.wallbay.adapters.unsplash;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.arch.paging.PageKeyedDataSource;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import ml.medyas.wallbay.entities.CollectionEntity;
import ml.medyas.wallbay.entities.unsplash.UnsplashCollectionSearchEntity;
import ml.medyas.wallbay.entities.unsplash.UnsplashCollectionsEntity;
import ml.medyas.wallbay.repositories.UnsplashRepository;
import ml.medyas.wallbay.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnsplashSearchCollectionsDataSource extends PageKeyedDataSource<Integer, CollectionEntity>{
    private UnsplashRepository unsplashRepository;
    private MutableLiveData<Utils.NetworkState> networkState;
    private String query;

    public UnsplashSearchCollectionsDataSource(Context context, String query) {
        this.networkState = new MutableLiveData<>();
        this.query = query;
        this.unsplashRepository = new UnsplashRepository(context);
    }

    public MutableLiveData<Utils.NetworkState> getNetworkState() {
        return networkState;
    }

    @Override
    public void loadInitial(@NonNull PageKeyedDataSource.LoadInitialParams<Integer> params, @NonNull final PageKeyedDataSource.LoadInitialCallback<Integer, CollectionEntity> callback) {
        networkState.postValue(Utils.NetworkState.LOADING);
        unsplashRepository.getSearchCollections(query, 1).enqueue(new Callback<UnsplashCollectionSearchEntity>() {
            @Override
            public void onResponse(Call<UnsplashCollectionSearchEntity> call, Response<UnsplashCollectionSearchEntity> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CollectionEntity> list = new ArrayList<>();
                    for (UnsplashCollectionsEntity item : response.body().getUnsplashCollectionSearchEntitys()) {
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
            public void onFailure(Call<UnsplashCollectionSearchEntity> call, Throwable t) {
                CollectionEntity item = new CollectionEntity();
                List<CollectionEntity> list = new ArrayList<>();
                list.add(item);

                callback.onResult(list, null, null);
                networkState.postValue(Utils.NetworkState.FAILED);
            }
        });
    }

    @Override
    public void loadBefore(@NonNull PageKeyedDataSource.LoadParams<Integer> params, @NonNull PageKeyedDataSource.LoadCallback<Integer, CollectionEntity> callback) {

    }

    @Override
    public void loadAfter(@NonNull final PageKeyedDataSource.LoadParams<Integer> params, @NonNull final PageKeyedDataSource.LoadCallback<Integer, CollectionEntity> callback) {
        networkState.postValue(Utils.NetworkState.LOADING);
        unsplashRepository.getSearchCollections(query, params.key).enqueue(new Callback<UnsplashCollectionSearchEntity>() {
            @Override
            public void onResponse(Call<UnsplashCollectionSearchEntity> call, Response<UnsplashCollectionSearchEntity> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CollectionEntity> list = new ArrayList<>();
                    for (UnsplashCollectionsEntity item : response.body().getUnsplashCollectionSearchEntitys()) {
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
            public void onFailure(Call<UnsplashCollectionSearchEntity> call, Throwable t) {
                CollectionEntity item = new CollectionEntity();
                List<CollectionEntity> list = new ArrayList<>();
                list.add(item);

                callback.onResult(list, null);
                networkState.postValue(Utils.NetworkState.FAILED);
            }
        });
    }

    public static class UnsplashSearchCollectionsDataSourceFactory extends DataSource.Factory {
        private Context context;
        private String query;
        private MutableLiveData<UnsplashSearchCollectionsDataSource> mutableLiveData;

        public UnsplashSearchCollectionsDataSourceFactory(Context context, String query) {
            this.context = context;
            this.query = query;
            mutableLiveData = new MutableLiveData<>();
        }

        public MutableLiveData<UnsplashSearchCollectionsDataSource> getMutableLiveData() {
            return mutableLiveData;
        }

        @Override
        public DataSource create() {
            UnsplashSearchCollectionsDataSource unsplashCollectionsDataSource = new UnsplashSearchCollectionsDataSource(context, query);
            mutableLiveData.postValue(unsplashCollectionsDataSource);
            return unsplashCollectionsDataSource;
        }
    }
}
