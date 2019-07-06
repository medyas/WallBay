package ml.medyas.wallbay.ui.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import ml.medyas.wallbay.models.ImageEntity;
import ml.medyas.wallbay.utils.Utils;
import ml.medyas.wallbay.viewmodels.pexels.PexelsCuratedViewModel;
import ml.medyas.wallbay.viewmodels.pexels.PexelsSearchViewModel;
import ml.medyas.wallbay.viewmodels.pexels.PexelsViewModel;

import static ml.medyas.wallbay.utils.Utils.FRAGMENT_POSITION;
import static ml.medyas.wallbay.utils.Utils.SEARCH_QUERY;

public class PexelsViewPagerFragment extends BaseFragment {
    private int position;
    private String query = "";


    public static Fragment newInstance(int position) {
        Fragment fragment = new PexelsViewPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(FRAGMENT_POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static Fragment newInstance(int position, String query) {
        Fragment fragment = new PexelsViewPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(FRAGMENT_POSITION, position);
        bundle.putString(SEARCH_QUERY, query);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            position = getArguments().getInt(FRAGMENT_POSITION);
            if(getArguments().containsKey(SEARCH_QUERY)) {
                query = getArguments().getString(SEARCH_QUERY);
            }
        }

        setUpViewModel();
    }

    @Override
    public void setUpViewModel() {
        ViewModel mViewModel;

        if(position == 0) {
            mViewModel = ViewModelProviders.of(this, new PexelsViewModel.PexelsViewModelFactory(getContext())).get(PexelsViewModel.class);

            ((PexelsViewModel) mViewModel).getPagedListLiveData().observe(this, new Observer<PagedList<ImageEntity>>() {
                @Override
                public void onChanged(@Nullable PagedList<ImageEntity> imageEntities) {
                    getAdapter().submitList(imageEntities);
                }
            });

            ((PexelsViewModel) mViewModel).getNetworkStateLiveData().observe(this, new Observer<Utils.NetworkState>() {
                @Override
                public void onChanged(@Nullable Utils.NetworkState networkState) {
                    checkNetworkStatus(networkState, false);
                }
            });

        } else if(position == 1) {
            mViewModel = ViewModelProviders.of(this, new PexelsCuratedViewModel.PexelsCuratedViewModelFactory(getContext())).get(PexelsCuratedViewModel.class);

            ((PexelsCuratedViewModel) mViewModel).getPagedListLiveData().observe(this, new Observer<PagedList<ImageEntity>>() {
                @Override
                public void onChanged(@Nullable PagedList<ImageEntity> imageEntities) {
                    getAdapter().submitList(imageEntities);
                }
            });

            ((PexelsCuratedViewModel) mViewModel).getNetworkStateLiveData().observe(this, new Observer<Utils.NetworkState>() {
                @Override
                public void onChanged(@Nullable Utils.NetworkState networkState) {
                    checkNetworkStatus(networkState, false);
                }
            });

        } else {
            mViewModel = ViewModelProviders.of(this, new PexelsSearchViewModel.PexelsSearchViewModelFactory(getContext(), query)).get(PexelsSearchViewModel.class);

            ((PexelsSearchViewModel) mViewModel).getPagedListLiveData().observe(this, new Observer<PagedList<ImageEntity>>() {
                @Override
                public void onChanged(@Nullable PagedList<ImageEntity> imageEntities) {
                    getAdapter().submitList(imageEntities);
                }
            });

            ((PexelsSearchViewModel) mViewModel).getNetworkStateLiveData().observe(this, new Observer<Utils.NetworkState>() {
                @Override
                public void onChanged(@Nullable Utils.NetworkState networkState) {
                    checkNetworkStatus(networkState, false);
                }
            });

        }
    }
}
