package ml.medyas.wallbay.adapters.search;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;
import android.util.Log;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import ml.medyas.wallbay.models.ImageEntity;
import ml.medyas.wallbay.models.SearchEntity;
import ml.medyas.wallbay.repositories.SearchRepository;
import ml.medyas.wallbay.utils.Utils;

public class SearchDataSource extends PageKeyedDataSource<Integer, ImageEntity> {
    private MutableLiveData<Utils.NetworkState> networkState;
    private SearchRepository searchRepository;
    private String query;

    public SearchDataSource(String query) {
        this.networkState = new MutableLiveData<Utils.NetworkState>();
        this.searchRepository = new SearchRepository();
        this.query = query;
    }

    public MutableLiveData<Utils.NetworkState> getNetworkState() {
        return networkState;
    }

    @Override
    public void loadInitial(@NonNull final LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, ImageEntity> callback) {
        networkState.postValue(Utils.NetworkState.LOADING);
        searchRepository.searchAllEndpoints(query, 1).subscribe(new Observer<SearchEntity>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(SearchEntity searchEntity) {
                callback.onResult(searchEntity.getAll(), null, 2);
                networkState.postValue(Utils.NetworkState.LOADED);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Log.d("mainactivity", "NetError: " + e.getMessage());
                networkState.postValue(Utils.NetworkState.FAILED);
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, ImageEntity> callback) {

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, ImageEntity> callback) {
        networkState.postValue(Utils.NetworkState.LOADING);
        searchRepository.searchAllEndpoints(query, params.key).subscribe(new Observer<SearchEntity>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(SearchEntity searchEntity) {
                callback.onResult(searchEntity.getAll(), params.key + 1);
                networkState.postValue(Utils.NetworkState.LOADED);
            }

            @Override
            public void onError(Throwable e) {
                Log.d("mainactivity", "NetError: " + e.getMessage());
                networkState.postValue(Utils.NetworkState.FAILED);
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
