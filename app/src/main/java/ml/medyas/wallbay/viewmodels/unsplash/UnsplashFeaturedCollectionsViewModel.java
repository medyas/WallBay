package ml.medyas.wallbay.viewmodels.unsplash;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import ml.medyas.wallbay.adapters.unsplash.UnsplashFeaturedCollectionsDataSource;
import ml.medyas.wallbay.models.CollectionEntity;
import ml.medyas.wallbay.utils.Utils;

import static ml.medyas.wallbay.utils.Utils.PREFETCH_DISTANCE;
import static ml.medyas.wallbay.utils.Utils.REQUEST_SIZE;

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
