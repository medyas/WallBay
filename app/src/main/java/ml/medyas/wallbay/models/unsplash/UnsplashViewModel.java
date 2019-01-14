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

import ml.medyas.wallbay.adapters.unsplash.UnsplashDataSource;
import ml.medyas.wallbay.adapters.unsplash.UnsplashDataSourceFactory;
import ml.medyas.wallbay.entities.ImageEntity;
import ml.medyas.wallbay.utils.Utils;

public class UnsplashViewModel extends ViewModel {
    private LiveData<Utils.NetworkState> networkStateLiveData;
    private LiveData<PagedList<ImageEntity>> pagedListLiveData;

    public UnsplashViewModel(Context context, String orderBy) {
        Executor executor = Executors.newFixedThreadPool(3);
        UnsplashDataSourceFactory unsplashDataSource = new UnsplashDataSourceFactory(context, orderBy);
        networkStateLiveData = Transformations.switchMap(unsplashDataSource.getMutableLiveData(), new Function<UnsplashDataSource, LiveData<Utils.NetworkState>>() {
            @Override
            public LiveData<Utils.NetworkState> apply(UnsplashDataSource input) {
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

    public static class UnsplashViewModelFactory implements ViewModelProvider.Factory {
        private Context context;
        private String orderBy;

        public UnsplashViewModelFactory(Context context, String orderBy) {
            this.context = context;
            this.orderBy = orderBy;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new UnsplashViewModel(context, orderBy);
        }
    }
}
