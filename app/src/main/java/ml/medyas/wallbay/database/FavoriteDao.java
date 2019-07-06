package ml.medyas.wallbay.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import ml.medyas.wallbay.models.ImageEntity;

@Dao
public interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addNewFavorite(ImageEntity imageEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addNewFavorite(List<ImageEntity> imageEntities);

    @Query("select * from favorite")
    LiveData<List<ImageEntity>> getFavoriteList();

    @Query("select * from favorite")
    List<ImageEntity> getObservableFavoriteList();

    @Delete
    void deleteFavorite(ImageEntity imageEntity);

    @Delete
    void deleteFavorite(List<ImageEntity> imageEntities);
}
