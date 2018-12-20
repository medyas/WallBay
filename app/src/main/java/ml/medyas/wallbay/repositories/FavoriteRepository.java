package ml.medyas.wallbay.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.functions.Action;
import ml.medyas.wallbay.database.FavoriteDao;
import ml.medyas.wallbay.database.FavoriteDatabase;
import ml.medyas.wallbay.entities.ImageEntity;

public class FavoriteRepository {
    private FavoriteDao favoriteDao;
    private LiveData<List<ImageEntity>> imageEntities;

    public FavoriteRepository(Application application) {
        FavoriteDatabase db = FavoriteDatabase.getDatabaseInstance(application);
        favoriteDao = db.getFavoriteDao();
        imageEntities = favoriteDao.getFavoriteList();
    }

    public LiveData<List<ImageEntity>> getImageEntities() {
        return imageEntities;
    }

    public Completable insertFavorite(final ImageEntity imageEntity) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() {
                favoriteDao.addNewFavorite(imageEntity);
            }
        });
    }

    public Completable insertFavorite(final List<ImageEntity> imageEntity) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() {
                favoriteDao.addNewFavorite(imageEntity);
            }
        });
    }


    public Completable deleteFavorite(final ImageEntity imageEntity) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() {
                favoriteDao.deleteFavorite(imageEntity);
            }
        });

    }

    public Completable deleteFavorite(final List<ImageEntity> imageEntity) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() {
                favoriteDao.deleteFavorite(imageEntity);
            }
        });
    }

}
