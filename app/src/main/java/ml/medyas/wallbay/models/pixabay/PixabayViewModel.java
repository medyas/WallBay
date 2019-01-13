package ml.medyas.wallbay.models.pixabay;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.content.Context;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import ml.medyas.wallbay.adapters.pixabay.PixabayDataSource;
import ml.medyas.wallbay.adapters.pixabay.PixabayDataSourceFactory;
import ml.medyas.wallbay.entities.ImageEntity;
import ml.medyas.wallbay.utils.Utils;

public class PixabayViewModel extends ViewModel {
    private LiveData<Utils.NetworkState> networkStateLiveData;
    private LiveData<PagedList<ImageEntity>> pagedListLiveData;

    public PixabayViewModel(Context context, String query, String category, String colors, boolean editorsChoice, String orderBy) {

        Executor executor = Executors.newFixedThreadPool(3);

        PixabayDataSourceFactory pixabayDataSourceFactory = new PixabayDataSourceFactory(context, query, category, colors, editorsChoice, orderBy);

        networkStateLiveData = Transformations.switchMap(pixabayDataSourceFactory.getMutableLiveData(), new Function<PixabayDataSource, LiveData<Utils.NetworkState>>() {
            @Override
            public LiveData<Utils.NetworkState> apply(PixabayDataSource input) {
                return input.getNetworkState();
            }
        });

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(30 * 2)
                .setPrefetchDistance(60)
                .setPageSize(30)
                .build();

        pagedListLiveData = new LivePagedListBuilder(pixabayDataSourceFactory, config).setFetchExecutor(executor).build();
    }

    public LiveData<Utils.NetworkState> getNetworkStateLiveData() {
        return networkStateLiveData;
    }

    public LiveData<PagedList<ImageEntity>> getPagedListLiveData() {
        return pagedListLiveData;
    }
}
