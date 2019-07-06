package ml.medyas.wallbay.viewmodels.search;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import ml.medyas.wallbay.adapters.search.SearchDataSource;
import ml.medyas.wallbay.adapters.search.SearchDataSourceFactory;
import ml.medyas.wallbay.models.ImageEntity;
import ml.medyas.wallbay.utils.Utils;

import static ml.medyas.wallbay.utils.Utils.PREFETCH_DISTANCE;
import static ml.medyas.wallbay.utils.Utils.REQUEST_SIZE;

public class SearchViewModel extends ViewModel {
    private LiveData<Utils.NetworkState> networkStateLiveData;
    private LiveData<PagedList<ImageEntity>> pagedListLiveData;

    public SearchViewModel(String query) {
        Executor executor = Executors.newFixedThreadPool(3);

        SearchDataSourceFactory searchDataSourceFactory = new SearchDataSourceFactory(query);

        networkStateLiveData = Transformations.switchMap(searchDataSourceFactory.getMutableLiveData(), new Function<SearchDataSource, LiveData<Utils.NetworkState>>() {
            @Override
            public LiveData<Utils.NetworkState> apply(SearchDataSource input) {
                return input.getNetworkState();
            }
        });

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(REQUEST_SIZE * 2)
                .setPrefetchDistance(PREFETCH_DISTANCE)
                .setPageSize(REQUEST_SIZE)
                .build();

        pagedListLiveData = new LivePagedListBuilder(searchDataSourceFactory, config).setFetchExecutor(executor).build();
    }


    public LiveData<Utils.NetworkState> getNetworkStateLiveData() {
        return networkStateLiveData;
    }

    public LiveData<PagedList<ImageEntity>> getPagedListLiveData() {
        return pagedListLiveData;
    }
}
