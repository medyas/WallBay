package ml.medyas.wallbay.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.content.Context;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import ml.medyas.wallbay.database.FavoriteDao;
import ml.medyas.wallbay.database.FavoriteDatabase;
import ml.medyas.wallbay.models.ImageEntity;

public class FavoriteRepository {
    private FavoriteDao favoriteDao;
    private LiveData<List<ImageEntity>> imageEntities;

    public FavoriteRepository(Application application) {
        FavoriteDatabase db = FavoriteDatabase.getDatabaseInstance(application);
        favoriteDao = db.getFavoriteDao();
        imageEntities = favoriteDao.getFavoriteList();
    }

    public FavoriteRepository(Context context) {
        FavoriteDatabase db = FavoriteDatabase.getDatabaseInstance(context);
        favoriteDao = db.getFavoriteDao();
        imageEntities = favoriteDao.getFavoriteList();
    }


    public LiveData<List<ImageEntity>> getImageEntities() {
        return imageEntities;
    }

    public List<ImageEntity> getImageEntitiesList() {
        return favoriteDao.getObservableFavoriteList();
    }

    public Completable insertFavorite(final ImageEntity imageEntity) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() {
                favoriteDao.addNewFavorite(imageEntity);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable insertFavorite(final List<ImageEntity> imageEntity) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() {
                favoriteDao.addNewFavorite(imageEntity);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Completable deleteFavorite(final ImageEntity imageEntity) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() {
                favoriteDao.deleteFavorite(imageEntity);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public Completable deleteFavorite(final List<ImageEntity> imageEntity) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() {
                favoriteDao.deleteFavorite(imageEntity);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
