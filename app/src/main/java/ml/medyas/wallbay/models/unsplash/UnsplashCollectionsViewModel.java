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

import ml.medyas.wallbay.adapters.unsplash.UnsplashCollectionsDataSource;
import ml.medyas.wallbay.entities.CollectionEntity;
import ml.medyas.wallbay.utils.Utils;

import static ml.medyas.wallbay.utils.Utils.PREFETCH_DISTANCE;
import static ml.medyas.wallbay.utils.Utils.REQUEST_SIZE;

public class UnsplashCollectionsViewModel extends ViewModel {
    private LiveData<Utils.NetworkState> networkStateLiveData;
    private LiveData<PagedList<CollectionEntity>> pagedListLiveData;

    public UnsplashCollectionsViewModel(Context context) {
        Executor executor = Executors.newFixedThreadPool(3);
        UnsplashCollectionsDataSource.UnsplashCollectionsDataSourceFactory unsplashDataSource = new UnsplashCollectionsDataSource.UnsplashCollectionsDataSourceFactory(context);
        networkStateLiveData = Transformations.switchMap(unsplashDataSource.getMutableLiveData(), new Function<UnsplashCollectionsDataSource, LiveData<Utils.NetworkState>>() {
            @Override
            public LiveData<Utils.NetworkState> apply(UnsplashCollectionsDataSource input) {
                return input.getNetworkState();
            }
        });

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(REQUEST_SIZE * 2)
                .setPrefetchDistance(PREFETCH_DISTANCE)
                .setPageSize(REQUEST_SIZE)
                .build();

        pagedListLiveData = new LivePagedListBuilder(unsplashDataSource, config).setFetchExecutor(executor).build();
    }

    public LiveData<Utils.NetworkState> getNetworkStateLiveData() {
        return networkStateLiveData;
    }

    public LiveData<PagedList<CollectionEntity>> getPagedListLiveData() {
        return pagedListLiveData;
    }


    public static class UnsplashCollectionsViewModelFactory implements ViewModelProvider.Factory {
        private Context context;

        public UnsplashCollectionsViewModelFactory(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new UnsplashCollectionsViewModel(context);
        }
    }
}
