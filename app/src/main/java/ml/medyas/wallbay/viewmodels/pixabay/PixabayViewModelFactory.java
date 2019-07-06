package ml.medyas.wallbay.viewmodels.pixabay;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class PixabayViewModelFactory implements ViewModelProvider.Factory {
    private String query;
    private String category;
    private String colors;
    private boolean editorsChoice;
    private String orderBy;
    private Context context;

    public PixabayViewModelFactory(Context context, String query, String category, String colors, boolean editorsChoice, String orderBy) {
        this.query = query;
        this.category = category;
        this.colors = colors;
        this.editorsChoice = editorsChoice;
        this.orderBy = orderBy;
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new PixabayViewModel(context, query, category, colors, editorsChoice, orderBy);
    }
}
