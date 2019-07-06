package ml.medyas.wallbay.adapters.pixabay;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

public class PixabayDataSourceFactory extends DataSource.Factory  {
    private PixabayDataSource pixabayDataSource;
    private MutableLiveData<PixabayDataSource> mutableLiveData;
    private Context context;
    private String query;
    private String category;
    private String colors;
    private boolean editorsChoice;
    private String orderBy;

    public PixabayDataSourceFactory(Context context, String query, String category, String colors, boolean editorsChoice, String orderBy) {
        this.mutableLiveData = new MutableLiveData<>();
        this.context = context;
        this.query = query;
        this.category = category;
        this.colors = colors;
        this.editorsChoice = editorsChoice;
        this.orderBy = orderBy;
    }

    @Override
    public DataSource create() {
        pixabayDataSource = new PixabayDataSource(context, query, category, colors, editorsChoice, orderBy);
        mutableLiveData.postValue(pixabayDataSource);
        return pixabayDataSource;
    }

    public MutableLiveData<PixabayDataSource> getMutableLiveData() {
        return mutableLiveData;
    }
}
