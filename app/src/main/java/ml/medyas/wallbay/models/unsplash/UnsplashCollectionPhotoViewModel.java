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

import ml.medyas.wallbay.adapters.unsplash.UnsplashCollectionPhotoDataSource;
import ml.medyas.wallbay.entities.ImageEntity;
import ml.medyas.wallbay.utils.Utils;

public class UnsplashCollectionPhotoViewModel extends ViewModel{
    private LiveData<Utils.NetworkState> networkStateLiveData;
    private LiveData<PagedList<ImageEntity>> pagedListLiveData;

    public UnsplashCollectionPhotoViewModel(Context context, int id) {
        Executor executor = Executors.newFixedThreadPool(3);
        UnsplashCollectionPhotoDataSource.UnsplashCollectionPhotoDataSourceFactory unsplashDataSource =
                new UnsplashCollectionPhotoDataSource.UnsplashCollectionPhotoDataSourceFactory(context, id);
        networkStateLiveData = Transformations.switchMap(unsplashDataSource.getMutableLiveData(), new Function<UnsplashCollectionPhotoDataSource, LiveData<Utils.NetworkState>>() {
            @Override
            public LiveData<Utils.NetworkState> apply(UnsplashCollectionPhotoDataSource input) {
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



    public class UnsplashCollectionPhotoViewModelFactory implements ViewModelProvider.Factory {
        private Context context;
        private int id;

        public UnsplashCollectionPhotoViewModelFactory(Context context, int id) {
            this.context = context;
            this.id = id;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new UnsplashCollectionPhotoViewModel(context, id);
        }
    }
}
