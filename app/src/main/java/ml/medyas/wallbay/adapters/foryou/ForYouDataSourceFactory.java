package ml.medyas.wallbay.adapters.foryou;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

public class ForYouDataSourceFactory extends DataSource.Factory {
    private ForYouDataSource dataSource;
    private MutableLiveData<ForYouDataSource> mutableLiveData;
    private String query = "";

    public ForYouDataSourceFactory(String query) {
        this.mutableLiveData = new MutableLiveData<ForYouDataSource>();
        this.query = query;
    }

    @Override
    public DataSource create() {
        dataSource = new ForYouDataSource(query);
        mutableLiveData.postValue(dataSource);
        return dataSource;
    }

    public MutableLiveData<ForYouDataSource> getMutableLiveData() {
        return mutableLiveData;
    }
}
