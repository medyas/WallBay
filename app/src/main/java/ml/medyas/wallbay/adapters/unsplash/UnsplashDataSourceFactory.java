package ml.medyas.wallbay.adapters.unsplash;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

public class UnsplashDataSourceFactory extends androidx.paging.DataSource.Factory {
    private MutableLiveData<UnsplashDataSource> mutableLiveData;
    private Context context;
    private String orderBy;

    public UnsplashDataSourceFactory(Context context, String orderBy) {
        this.context = context;
        this.orderBy = orderBy;
        this.mutableLiveData = new MutableLiveData<>();
    }

    @Override
    public androidx.paging.DataSource create() {
        UnsplashDataSource unsplashDataSource = new UnsplashDataSource(context, orderBy);
        mutableLiveData.postValue(unsplashDataSource);
        return unsplashDataSource;
    }

    public MutableLiveData<UnsplashDataSource> getMutableLiveData() {
        return mutableLiveData;
    }
}
