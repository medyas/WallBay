package ml.medyas.wallbay.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

// new since Glide v4
@GlideModule
public final class MyAppGlideModule extends AppGlideModule {
    // leave empty for now


    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {

    }
}
