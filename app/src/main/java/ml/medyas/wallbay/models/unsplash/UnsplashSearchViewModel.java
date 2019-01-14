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

import ml.medyas.wallbay.adapters.unsplash.UnsplashSearchDataSource;
import ml.medyas.wallbay.entities.ImageEntity;
import ml.medyas.wallbay.utils.Utils;

public class UnsplashSearchViewModel extends ViewModel {
    private LiveData<Utils.NetworkState> networkStateLiveData;
    private LiveData<PagedList<ImageEntity>> pagedListLiveData;

    public UnsplashSearchViewModel(Context context, String query) {
        Executor executor = Executors.newFixedThreadPool(3);
        UnsplashSearchDataSource.UnsplashSearchDataSourceFactory unsplashDataSource = new UnsplashSearchDataSource.UnsplashSearchDataSourceFactory(context, query);
        networkStateLiveData = Transformations.switchMap(unsplashDataSource.getMutableLiveData(), new Function<UnsplashSearchDataSource, LiveData<Utils.NetworkState>>() {
            @Override
            public LiveData<Utils.NetworkState> apply(UnsplashSearchDataSource input) {
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

    public LiveData<PagedList<ImageEntity>> getPagedListLiveData() {
        return pagedListLiveData;
    }

    public class UnsplashSearchViewModelFactory implements ViewModelProvider.Factory {
        private Context context;
        private String query;

        public UnsplashSearchViewModelFactory(Context context, String query) {
            this.context = context;
            this.query = query;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new UnsplashViewModel(context, query);
        }
    }
}
