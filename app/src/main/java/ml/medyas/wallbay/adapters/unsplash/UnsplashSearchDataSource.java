package ml.medyas.wallbay.adapters.unsplash;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.arch.paging.PageKeyedDataSource;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import ml.medyas.wallbay.models.ImageEntity;
import ml.medyas.wallbay.models.unsplash.UnsplashPhotoEntity;
import ml.medyas.wallbay.models.unsplash.UnsplashSearchEntity;
import ml.medyas.wallbay.repositories.UnsplashRepository;
import ml.medyas.wallbay.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnsplashSearchDataSource extends PageKeyedDataSource<Integer, ImageEntity> {
    private UnsplashRepository unsplashRepository;
    private MutableLiveData<Utils.NetworkState> networkState;
    private String query;

    public UnsplashSearchDataSource(Context context, String query) {
        unsplashRepository = new UnsplashRepository(context);
        this.query = query;
        this.networkState = new MutableLiveData<>();
    }

    public MutableLiveData<Utils.NetworkState> getNetworkState() {
        return networkState;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, ImageEntity> callback) {
        networkState.postValue(Utils.NetworkState.LOADING);
        unsplashRepository.getSearch(query, 1).enqueue(new Callback<UnsplashSearchEntity>() {
            @Override
            public void onResponse(Call<UnsplashSearchEntity> call, Response<UnsplashSearchEntity> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ImageEntity> list = new ArrayList<>();
                    for (UnsplashPhotoEntity item : response.body().getUnsplashPhotoEntitys()) {
                        ImageEntity imageEntity = new ImageEntity(item.getId(),
                                item.getUser().getUsername(),
                                item.getUser().getProfileImage().getMedium(),
                                Utils.webSite.UNSPLASH,
                                item.getLikes(),
                                0,
                                0,
                                item.getWidth(),
                                item.getHeight(),
                                item.getLinks().getHtml(),
                                item.getUrls().getRaw(),
                                item.getLinks().getDownloadLocation(),
                                item.getUrls().getRegular(),
                                "");
                        list.add(imageEntity);
                    }

                    networkState.postValue(Utils.NetworkState.LOADED);
                    callback.onResult(list, null, 2);

                } else {
                    ImageEntity item = new ImageEntity();
                    item.setProvider(Utils.webSite.EMPTY);
                    List<ImageEntity> list = new ArrayList<>();
                    list.add(item);

                    networkState.postValue(Utils.NetworkState.EMPTY);
                    callback.onResult(list, null, null);
                }
            }

            @Override
            public void onFailure(Call<UnsplashSearchEntity> call, Throwable t) {
                ImageEntity item = new ImageEntity();
                item.setProvider(Utils.webSite.ERROR);
                List<ImageEntity> list = new ArrayList<>();
                list.add(item);

                networkState.postValue(Utils.NetworkState.FAILED);
                callback.onResult(list, null, null);
            }
        });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, ImageEntity> callback) {

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, ImageEntity> callback) {
        networkState.postValue(Utils.NetworkState.LOADING);
        unsplashRepository.getSearch(query, params.key).enqueue(new Callback<UnsplashSearchEntity>() {
            @Override
            public void onResponse(Call<UnsplashSearchEntity> call, Response<UnsplashSearchEntity> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ImageEntity> list = new ArrayList<>();
                    for (UnsplashPhotoEntity item : response.body().getUnsplashPhotoEntitys()) {
                        ImageEntity imageEntity = new ImageEntity(item.getId(),
                                item.getUser().getUsername(),
                                item.getUser().getProfileImage().getMedium(),
                                Utils.webSite.UNSPLASH,
                                item.getLikes(),
                                0,
                                0,
                                item.getWidth(),
                                item.getHeight(),
                                item.getLinks().getHtml(),
                                item.getUrls().getRaw(),
                                item.getLinks().getDownloadLocation(),
                                item.getUrls().getRegular(),
                                "");
                        list.add(imageEntity);
                    }

                    networkState.postValue(Utils.NetworkState.LOADED);
                    callback.onResult(list, params.key+1);

                } else {
                    ImageEntity item = new ImageEntity();
                    item.setProvider(Utils.webSite.EMPTY);
                    List<ImageEntity> list = new ArrayList<>();
                    list.add(item);

                    networkState.postValue(Utils.NetworkState.EMPTY);
                    callback.onResult(list, null);
                }
            }

            @Override
            public void onFailure(Call<UnsplashSearchEntity> call, Throwable t) {
                ImageEntity item = new ImageEntity();
                item.setProvider(Utils.webSite.ERROR);
                List<ImageEntity> list = new ArrayList<>();
                list.add(item);

                networkState.postValue(Utils.NetworkState.FAILED);
                callback.onResult(list, null);
            }
        });
    }

    public static class UnsplashSearchDataSourceFactory extends Factory {
        private Context context;
        private String query;
        private MutableLiveData<UnsplashSearchDataSource> mutableLiveData;

        public UnsplashSearchDataSourceFactory(Context context, String query) {
            this.context = context;
            this.query = query;
            this.mutableLiveData = new MutableLiveData<>();
        }

        public MutableLiveData<UnsplashSearchDataSource> getMutableLiveData() {
            return mutableLiveData;
        }

        @Override
        public DataSource create() {
            UnsplashSearchDataSource unsplashSearchDataSource = new UnsplashSearchDataSource(context, query);
            mutableLiveData.postValue(unsplashSearchDataSource);
            return unsplashSearchDataSource;
        }
    }
}
