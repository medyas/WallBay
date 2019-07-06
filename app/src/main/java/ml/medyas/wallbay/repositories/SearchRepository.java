package ml.medyas.wallbay.repositories;

import io.reactivex.Observable;
import ml.medyas.wallbay.models.SearchEntity;
import ml.medyas.wallbay.network.SearchCalls;

public class SearchRepository {
    private SearchCalls searchCalls;

    public SearchRepository() {
        this.searchCalls = new SearchCalls();
    }

    public Observable<SearchEntity> searchAllEndpoints(String query, int page) {
        return searchCalls.searchAll(query, page);
    }
}
