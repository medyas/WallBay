package ml.medyas.wallbay.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

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
