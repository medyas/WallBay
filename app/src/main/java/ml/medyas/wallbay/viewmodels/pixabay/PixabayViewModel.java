package ml.medyas.wallbay.viewmodels.pixabay;

import android.content.Context;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import ml.medyas.wallbay.adapters.pixabay.PixabayDataSource;
import ml.medyas.wallbay.adapters.pixabay.PixabayDataSourceFactory;
import ml.medyas.wallbay.models.ImageEntity;
import ml.medyas.wallbay.utils.Utils;

import static ml.medyas.wallbay.utils.Utils.PREFETCH_DISTANCE;
import static ml.medyas.wallbay.utils.Utils.REQUEST_SIZE;

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
                .setInitialLoadSizeHint(REQUEST_SIZE * 2)
                .setPrefetchDistance(PREFETCH_DISTANCE)
                .setPageSize(REQUEST_SIZE)
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
