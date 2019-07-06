package ml.medyas.wallbay.ui.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;

import ml.medyas.wallbay.models.ImageEntity;
import ml.medyas.wallbay.utils.Utils;
import ml.medyas.wallbay.viewmodels.search.SearchViewModel;
import ml.medyas.wallbay.viewmodels.search.SearchViewModelFactory;

import static ml.medyas.wallbay.utils.Utils.SEARCH_QUERY;

public class SearchDefaultFragment extends BaseFragment {
    private String query = "";

    public static Fragment newInstance(String q) {
        Fragment fragment = new SearchDefaultFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SEARCH_QUERY, q);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {
            query = getArguments().getString(SEARCH_QUERY);
        }

        setUpViewModel();
    }

    @Override
    public void setUpViewModel() {
        SearchViewModel mViewModel = ViewModelProviders.of(this, new SearchViewModelFactory(query)).get(SearchViewModel.class);

        mViewModel.getNetworkStateLiveData().observe(this, new Observer<Utils.NetworkState>() {
            @Override
            public void onChanged(@Nullable Utils.NetworkState networkState) {
                checkNetworkStatus(networkState, false);
            }
        });

        mViewModel.getPagedListLiveData().observe(this, new Observer<PagedList<ImageEntity>>() {
            @Override
            public void onChanged(@Nullable PagedList<ImageEntity> imageEntities) {
                getAdapter().submitList(imageEntities);
            }
        });
    }
}
