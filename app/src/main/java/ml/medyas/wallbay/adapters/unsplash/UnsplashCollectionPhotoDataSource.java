package ml.medyas.wallbay.adapters.unsplash;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import java.util.ArrayList;
import java.util.List;

import ml.medyas.wallbay.models.ImageEntity;
import ml.medyas.wallbay.models.unsplash.UnsplashPhotoEntity;
import ml.medyas.wallbay.repositories.UnsplashRepository;
import ml.medyas.wallbay.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnsplashCollectionPhotoDataSource extends PageKeyedDataSource<Integer, ImageEntity> {
    private UnsplashRepository unsplashRepository;
    private MutableLiveData<Utils.NetworkState> networkState;
    private int id;

    public UnsplashCollectionPhotoDataSource(Context context, int id) {
        this.id = id;
        this.networkState = new MutableLiveData<>();
        this.unsplashRepository = new UnsplashRepository(context);
    }

    public MutableLiveData<Utils.NetworkState> getNetworkState() {
        return networkState;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, ImageEntity> callback) {
        networkState.postValue(Utils.NetworkState.LOADING);
        unsplashRepository.getCollectionPhoto(id, 1).enqueue(new Callback<List<UnsplashPhotoEntity>>() {
            @Override
            public void onResponse(Call<List<UnsplashPhotoEntity>> call, Response<List<UnsplashPhotoEntity>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ImageEntity> list = new ArrayList<>();
                    for (UnsplashPhotoEntity item : response.body()) {
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
                    callback.onResult(list, null, 2);
                    networkState.postValue(Utils.NetworkState.LOADED);

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
            public void onFailure(Call<List<UnsplashPhotoEntity>> call, Throwable t) {
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
        unsplashRepository.getCollectionPhoto(id, params.key).enqueue(new Callback<List<UnsplashPhotoEntity>>() {
            @Override
            public void onResponse(Call<List<UnsplashPhotoEntity>> call, Response<List<UnsplashPhotoEntity>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ImageEntity> list = new ArrayList<>();
                    for (UnsplashPhotoEntity item : response.body()) {
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
                    callback.onResult(list, params.key+1);
                    networkState.postValue(Utils.NetworkState.LOADED);

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
            public void onFailure(Call<List<UnsplashPhotoEntity>> call, Throwable t) {
                ImageEntity item = new ImageEntity();
                item.setProvider(Utils.webSite.ERROR);
                List<ImageEntity> list = new ArrayList<>();
                list.add(item);

                networkState.postValue(Utils.NetworkState.FAILED);
                callback.onResult(list, null);
            }
        });

    }


    public static class UnsplashCollectionPhotoDataSourceFactory extends androidx.paging.DataSource.Factory {
        private MutableLiveData<UnsplashCollectionPhotoDataSource> mutableLiveData;
        private Context context;
        private int id;

        public UnsplashCollectionPhotoDataSourceFactory(Context context, int id) {
            this.context = context;
            this.id = id;
            this.mutableLiveData = new MutableLiveData<>();
        }

        @Override
        public androidx.paging.DataSource create() {
            UnsplashCollectionPhotoDataSource unsplashDataSource = new UnsplashCollectionPhotoDataSource(context, id);
            mutableLiveData.postValue(unsplashDataSource);
            return unsplashDataSource;
        }

        public MutableLiveData<UnsplashCollectionPhotoDataSource> getMutableLiveData() {
            return mutableLiveData;
        }
    }
}
