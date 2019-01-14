package ml.medyas.wallbay.models.unsplash;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import ml.medyas.wallbay.adapters.unsplash.UnsplashFeaturedCollectionsDataSource;
import ml.medyas.wallbay.entities.CollectionEntity;
import ml.medyas.wallbay.utils.Utils;

public class UnsplashFeaturedCollectionsViewModel extends ViewModel {
    private LiveData<Utils.NetworkState> networkStateLiveData;
    private LiveData<PagedList<CollectionEntity>> pagedListLiveData;

    public UnsplashFeaturedCollectionsViewModel(Context context) {
        Executor executor = Executors.newFixedThreadPool(3);
        UnsplashFeaturedCollectionsDataSource.UnsplashFeaturedCollectionsDataSourceFactory unsplashDataSource = new UnsplashFeaturedCollectionsDataSource.UnsplashFeaturedCollectionsDataSourceFactory(context);
        networkStateLiveData = Transformations.switchMap(unsplashDataSource.getMutableLiveData(), new Function<UnsplashFeaturedCollectionsDataSource, LiveData<Utils.NetworkState>>() {
            @Override
            public LiveData<Utils.NetworkState> apply(UnsplashFeaturedCollectionsDataSource input) {
                return input.getNetworkState();
            }
        });

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(30 * 2)
                .setPrefetchDistance(60)
                .setPageSize(30)
                .build();

        pagedListLiveData = new LivePagedListBuilder(unsplashDataSource, config).setFetchExecutor(executor).build();
    }

    public LiveData<Utils.NetworkState> getNetworkStateLiveData() {
        return networkStateLiveData;
    }

    public LiveData<PagedList<CollectionEntity>> getPagedListLiveData() {
        return pagedListLiveData;
    }


    public static class UnsplashFeaturedCollectionsViewModelFactory implements ViewModelProvider.Factory {
        private Context context;

        public UnsplashFeaturedCollectionsViewModelFactory(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new UnsplashFeaturedCollectionsViewModel(context);
        }
    }
}
