package ml.medyas.wallbay.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

// new since Glide v4
@GlideModule
public final class MyAppGlideModule extends AppGlideModule {
    // leave empty for now


    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        int diskCacheSizeBytes = 1024 * 1024 * 500; // 500 MB
        builder
                .setDefaultRequestOptions(new RequestOptions().dontAnimate());
    }
}
