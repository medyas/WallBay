package ml.medyas.wallbay.adapters.unsplash;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import java.util.ArrayList;
import java.util.List;

import ml.medyas.wallbay.models.CollectionEntity;
import ml.medyas.wallbay.models.unsplash.UnsplashCollectionsEntity;
import ml.medyas.wallbay.repositories.UnsplashRepository;
import ml.medyas.wallbay.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnsplashFeaturedCollectionsDataSource extends PageKeyedDataSource<Integer, CollectionEntity>{
    private UnsplashRepository unsplashRepository;
    private MutableLiveData<Utils.NetworkState> networkState;

    public UnsplashFeaturedCollectionsDataSource(Context context) {
        this.networkState = new MutableLiveData<>();
        this.unsplashRepository = new UnsplashRepository(context);
    }

    public MutableLiveData<Utils.NetworkState> getNetworkState() {
        return networkState;
    }

    @Override
    public void loadInitial(@NonNull PageKeyedDataSource.LoadInitialParams<Integer> params, @NonNull final PageKeyedDataSource.LoadInitialCallback<Integer, CollectionEntity> callback) {
        networkState.postValue(Utils.NetworkState.LOADING);
        unsplashRepository.getFeaturedCollections(1).enqueue(new Callback<List<UnsplashCollectionsEntity>>() {
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
                                item.getUser().getProfileImage().getMedium(),
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
    public void loadBefore(@NonNull PageKeyedDataSource.LoadParams<Integer> params, @NonNull PageKeyedDataSource.LoadCallback<Integer, CollectionEntity> callback) {

    }

    @Override
    public void loadAfter(@NonNull final PageKeyedDataSource.LoadParams<Integer> params, @NonNull final PageKeyedDataSource.LoadCallback<Integer, CollectionEntity> callback) {
        networkState.postValue(Utils.NetworkState.LOADING);
        unsplashRepository.getFeaturedCollections(params.key).enqueue(new Callback<List<UnsplashCollectionsEntity>>() {
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
                                item.getUser().getProfileImage().getMedium(),
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

    public static class UnsplashFeaturedCollectionsDataSourceFactory extends DataSource.Factory {
        private Context context;
        private MutableLiveData<UnsplashFeaturedCollectionsDataSource> mutableLiveData;

        public UnsplashFeaturedCollectionsDataSourceFactory(Context context) {
            this.context = context;
            mutableLiveData = new MutableLiveData<>();
        }

        public MutableLiveData<UnsplashFeaturedCollectionsDataSource> getMutableLiveData() {
            return mutableLiveData;
        }

        @Override
        public DataSource create() {
            UnsplashFeaturedCollectionsDataSource unsplashFeaturedCollectionsDataSource = new UnsplashFeaturedCollectionsDataSource(context);
            mutableLiveData.postValue(unsplashFeaturedCollectionsDataSource);
            return unsplashFeaturedCollectionsDataSource;
        }
    }
}
