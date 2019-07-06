package ml.medyas.wallbay.ui.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import ml.medyas.wallbay.R;
import ml.medyas.wallbay.models.ImageEntity;
import ml.medyas.wallbay.utils.Utils;
import ml.medyas.wallbay.viewmodels.pixabay.PixabayViewModel;
import ml.medyas.wallbay.viewmodels.pixabay.PixabayViewModelFactory;

import static ml.medyas.wallbay.utils.Utils.FRAGMENT_POSITION;
import static ml.medyas.wallbay.utils.Utils.SEARCH_QUERY;

public class PixabayViewPagerFragment extends BaseFragment {
    private PixabayViewModel mViewModel;
    private int position;
    private String query;

    public static final String TAG = "ml.medyas.wallbay.ui.fragments.PixabayViewPagerFragment";

    public PixabayViewPagerFragment() {
    }

    public static Fragment newInstance(int position) {
        Fragment fragment = new PixabayViewPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(FRAGMENT_POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static Fragment newInstance(int position, String query) {
        Fragment fragment = new PixabayViewPagerFragment();
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
        if(mViewModel == null) {
            switch (position) {
                case 0:
                    mViewModel = ViewModelProviders.of(this, new PixabayViewModelFactory(getContext(),
                            "", "", "", false, getResources().getString(R.string.popular))).get(PixabayViewModel.class);
                    break;

                case 1:
                    mViewModel = ViewModelProviders.of(this, new PixabayViewModelFactory(getContext(),
                            "", "", "", false, getResources().getString(R.string.latest))).get(PixabayViewModel.class);
                    break;

                case 2:
                    mViewModel = ViewModelProviders.of(this, new PixabayViewModelFactory(getContext(),
                        "", "", "", true, "")).get(PixabayViewModel.class);
                    break;
                case 3:
                    mViewModel = ViewModelProviders.of(this, new PixabayViewModelFactory(getContext(),
                            query, "", "", false, "")).get(PixabayViewModel.class);
                    break;
            }
        }

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
