package ml.medyas.wallbay.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

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
