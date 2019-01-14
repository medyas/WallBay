package ml.medyas.wallbay.adapters.unsplash;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

public class UnsplashDataSourceFactory extends android.arch.paging.DataSource.Factory {
    private MutableLiveData<UnsplashDataSource> mutableLiveData;
    private Context context;
    private String orderBy;

    public UnsplashDataSourceFactory(Context context, String orderBy) {
        this.context = context;
        this.orderBy = orderBy;
        this.mutableLiveData = new MutableLiveData<>();
    }

    @Override
    public android.arch.paging.DataSource create() {
        UnsplashDataSource unsplashDataSource = new UnsplashDataSource(context, orderBy);
        mutableLiveData.postValue(unsplashDataSource);
        return unsplashDataSource;
    }

    public MutableLiveData<UnsplashDataSource> getMutableLiveData() {
        return mutableLiveData;
    }
}
