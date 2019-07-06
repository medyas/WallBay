package ml.medyas.wallbay.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import ml.medyas.wallbay.models.ImageEntity;

@Database(entities = {ImageEntity.class}, version = 1, exportSchema = false)
public abstract class FavoriteDatabase extends RoomDatabase {
    private static volatile FavoriteDatabase mInstance;

    public static FavoriteDatabase getDatabaseInstance(final Context context) {
        if (mInstance == null) {
            synchronized (FavoriteDatabase.class) {
                if (mInstance == null) {
                    mInstance = Room.databaseBuilder(context.getApplicationContext(),
                            FavoriteDatabase.class, "favorite")
                            .build();
                }
            }
        }
        return mInstance;
    }

    public abstract FavoriteDao getFavoriteDao();
}
