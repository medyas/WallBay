package ml.medyas.wallbay.adapters.search;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

public class SearchDataSourceFactory extends DataSource.Factory {
    private SearchDataSource dataSource;
    private MutableLiveData<SearchDataSource> mutableLiveData;
    private String query = "";

    public SearchDataSourceFactory(String query) {
        this.mutableLiveData = new MutableLiveData<SearchDataSource>();
        this.query = query;
    }

    @Override
    public DataSource create() {
        dataSource = new SearchDataSource(query);
        mutableLiveData.postValue(dataSource);
        return dataSource;
    }

    public MutableLiveData<SearchDataSource> getMutableLiveData() {
        return mutableLiveData;
    }
}
