package ml.medyas.wallbay.utils;

import android.arch.persistence.room.TypeConverter;

public class ProviderTypeConverter {

    @TypeConverter
    public static Utils.webSite toProvider(int code) {
        if (code == Utils.webSite.PIXABAY.getCode()) {
            return Utils.webSite.PIXABAY;

        } else if (code == Utils.webSite.PEXELS.getCode()) {
            return Utils.webSite.PEXELS;

        } else if (code == Utils.webSite.UNSPLASH.getCode()) {
            return Utils.webSite.UNSPLASH;

        } else if (code == Utils.webSite.EMPTY.getCode()) {
            return Utils.webSite.EMPTY;

        } else if (code == Utils.webSite.ERROR.getCode()) {
            return Utils.webSite.ERROR;

        } else {
            throw new IllegalArgumentException("Could not recognize status");
        }
    }

    @TypeConverter
    public static int toCode(Utils.webSite provider) {
        return provider.getCode();
    }
}
