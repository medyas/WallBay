package ml.medyas.wallbay.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import ml.medyas.wallbay.entities.CollectionEntity;
import ml.medyas.wallbay.entities.ImageEntity;
import ml.medyas.wallbay.entities.unsplash.UnsplashCollectionSearchEntity;
import ml.medyas.wallbay.entities.unsplash.UnsplashCollectionsEntity;
import ml.medyas.wallbay.entities.unsplash.UnsplashPhotoEntity;
import ml.medyas.wallbay.entities.unsplash.UnsplashSearchEntity;
import ml.medyas.wallbay.network.unsplash.UnsplashCalls;
import ml.medyas.wallbay.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnsplashRepository {
    private UnsplashCalls unsplashService;

    public UnsplashRepository(Context ctx) {
        this.unsplashService = new UnsplashCalls(ctx);
    }

    public LiveData<List<ImageEntity>> getPhotos(String orderBy, int page) {
        final MutableLiveData<List<ImageEntity>> data = new MutableLiveData<>();

        unsplashService.getPhotos(orderBy, page).enqueue(new Callback<List<UnsplashPhotoEntity>>() {
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
                                item.getLinks().getDownloadLocation(),
                                item.getUrls().getRegular(),
                                null);
                        list.add(imageEntity);
                    }
                    data.setValue(list);
                } else {
                    ImageEntity item = new ImageEntity();
                    item.setProvider(Utils.webSite.EMPTY);
                    List<ImageEntity> list = new ArrayList<>();
                    list.add(item);
                    data.setValue(list);
                }
            }

            @Override
            public void onFailure(Call<List<UnsplashPhotoEntity>> call, Throwable t) {
                ImageEntity item = new ImageEntity();
                item.setProvider(Utils.webSite.ERROR);
                List<ImageEntity> list = new ArrayList<>();
                list.add(item);
                data.setValue(list);
            }
        });

        return data;
    }

    public LiveData<List<ImageEntity>> getSearch(String query, int page) {
        final MutableLiveData<List<ImageEntity>> data = new MutableLiveData<>();

        unsplashService.getSearch(query, page).enqueue(new Callback<UnsplashSearchEntity>() {
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
                                item.getLinks().getDownloadLocation(),
                                item.getUrls().getRegular(),
                                null);
                        list.add(imageEntity);
                    }
                    data.setValue(list);
                } else {
                    ImageEntity item = new ImageEntity();
                    item.setProvider(Utils.webSite.EMPTY);
                    List<ImageEntity> list = new ArrayList<>();
                    list.add(item);
                    data.setValue(list);
                }
            }

            @Override
            public void onFailure(Call<UnsplashSearchEntity> call, Throwable t) {
                ImageEntity item = new ImageEntity();
                item.setProvider(Utils.webSite.ERROR);
                List<ImageEntity> list = new ArrayList<>();
                list.add(item);
                data.setValue(list);
            }
        });

        return data;
    }

    public LiveData<List<CollectionEntity>> getCollections(int page) {
        final MutableLiveData<List<CollectionEntity>> data = new MutableLiveData<>();

        unsplashService.getCollections(page).enqueue(new Callback<List<UnsplashCollectionsEntity>>() {
            @Override
            public void onResponse(Call<List<UnsplashCollectionsEntity>> call, Response<List<UnsplashCollectionsEntity>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CollectionEntity> list = new ArrayList<>();
                    for (UnsplashCollectionsEntity item : response.body()) {
                        CollectionEntity collectionEntity = new CollectionEntity(
                                item.getId(),
                                item.getTitle(),
                                item.getTotalPhotos(),
                                item.getTags(),
                                item.getUser().getUsername(),
                                item.getUser().getLinks().getHtml(),
                                item.getPreviewPhotos()
                        );
                        list.add(collectionEntity);
                    }
                    data.setValue(list);
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<UnsplashCollectionsEntity>> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;

    }

    public LiveData<List<CollectionEntity>> getFeaturedCollections(int page) {
        final MutableLiveData<List<CollectionEntity>> data = new MutableLiveData<>();

        unsplashService.getFeaturedCollections(page).enqueue(new Callback<List<UnsplashCollectionsEntity>>() {
            @Override
            public void onResponse(Call<List<UnsplashCollectionsEntity>> call, Response<List<UnsplashCollectionsEntity>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CollectionEntity> list = new ArrayList<>();
                    for (UnsplashCollectionsEntity item : response.body()) {
                        CollectionEntity collectionEntity = new CollectionEntity(
                                item.getId(),
                                item.getTitle(),
                                item.getTotalPhotos(),
                                item.getTags(),
                                item.getUser().getUsername(),
                                item.getUser().getLinks().getHtml(),
                                item.getPreviewPhotos()
                        );
                        list.add(collectionEntity);
                    }
                    data.setValue(list);
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<UnsplashCollectionsEntity>> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<List<CollectionEntity>> getSearchCollections(String query, int page) {
        final MutableLiveData<List<CollectionEntity>> data = new MutableLiveData<>();

        unsplashService.getSearchCollections(query, page).enqueue(new Callback<UnsplashCollectionSearchEntity>() {
            @Override
            public void onResponse(Call<UnsplashCollectionSearchEntity> call, Response<UnsplashCollectionSearchEntity> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CollectionEntity> list = new ArrayList<>();
                    for (UnsplashCollectionsEntity item : response.body().getUnsplashCollectionSearchEntitys()) {
                        CollectionEntity collectionEntity = new CollectionEntity(
                                item.getId(),
                                item.getTitle(),
                                item.getTotalPhotos(),
                                item.getTags(),
                                item.getUser().getUsername(),
                                item.getUser().getLinks().getHtml(),
                                item.getPreviewPhotos()
                        );
                        list.add(collectionEntity);
                    }
                    data.setValue(list);
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<UnsplashCollectionSearchEntity> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;

    }

    public LiveData<List<ImageEntity>> getCollectionPhoto(int id, int page) {
        final MutableLiveData<List<ImageEntity>> data = new MutableLiveData<>();

        unsplashService.getCollectionPhotos(id, page).enqueue(new Callback<List<UnsplashPhotoEntity>>() {
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
                                item.getLinks().getDownloadLocation(),
                                item.getUrls().getRegular(),
                                null);
                        list.add(imageEntity);
                    }
                    data.setValue(list);
                } else {
                    ImageEntity item = new ImageEntity();
                    item.setProvider(Utils.webSite.EMPTY);
                    List<ImageEntity> list = new ArrayList<>();
                    list.add(item);
                    data.setValue(list);
                }
            }

            @Override
            public void onFailure(Call<List<UnsplashPhotoEntity>> call, Throwable t) {
                ImageEntity item = new ImageEntity();
                item.setProvider(Utils.webSite.ERROR);
                List<ImageEntity> list = new ArrayList<>();
                list.add(item);
                data.setValue(list);
            }
        });

        return data;

    }

}
