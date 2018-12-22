package ml.medyas.wallbay.models.foryou;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class ForYouViewModelFactory implements ViewModelProvider.Factory {
    private String query;

    public ForYouViewModelFactory(String query) {
        this.query = query;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ForYouViewModel(query);
    }
}
