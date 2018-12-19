package ml.medyas.wallbay.models;

import android.arch.lifecycle.ViewModel;

import io.reactivex.Observable;
import ml.medyas.wallbay.entities.SearchEntity;
import ml.medyas.wallbay.repositories.SearchRepository;

public class SearchViewModel extends ViewModel {
    private SearchRepository searchRepository;

    public SearchViewModel() {
        this.searchRepository = new SearchRepository();
    }

    public Observable<SearchEntity> getSearchAllEndpoints(String query, int page) {
        return searchRepository.searchAllEndpoints(query, page);
    }


}
