package ml.medyas.wallbay.models.search;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class SearchViewModelFactory implements ViewModelProvider.Factory {
    private String query;

    public SearchViewModelFactory(String query) {
        this.query = query;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SearchViewModel(query);
    }
}
