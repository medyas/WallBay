package ml.medyas.wallbay.ui.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import ml.medyas.wallbay.entities.ImageEntity;
import ml.medyas.wallbay.models.pixabay.PixabayViewModel;
import ml.medyas.wallbay.models.pixabay.PixabayViewModelFactory;
import ml.medyas.wallbay.utils.Utils;

public class PixabayViewPagerFragment extends BaseFragment {
    public static final String FRAGMENT_POSITION = "FRAGMENT_POSITION";
    public static final String LATEST = "latest";
    public static final String POPULAR = "popular";
    public static final String SEARCH_QUERY = "SEARCH_QUERY";
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
                            "", "", "", false, POPULAR)).get(PixabayViewModel.class);
                    break;

                case 1:
                    mViewModel = ViewModelProviders.of(this, new PixabayViewModelFactory(getContext(),
                            "", "", "", false, LATEST)).get(PixabayViewModel.class);
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
                if (networkState == Utils.NetworkState.LOADED) {
                    getItemLoad().setVisibility(View.GONE);
                    getRecyclerView().setVisibility(View.VISIBLE);

                } else if (networkState == Utils.NetworkState.EMPTY) {
                    Toast.makeText(getContext(), "Error retrieving more data!", Toast.LENGTH_SHORT).show();

                } else if (networkState == Utils.NetworkState.FAILED) {
                    if (getAdapter().getCurrentList().size() == 0) {
                        getNetError().setVisibility(View.VISIBLE);
                        getItemLoad().setVisibility(View.GONE);
                        Snackbar.make(getNetError(), "Network Error", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Retry", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        getNetError().setVisibility(View.GONE);
                                        //mListener.reCreateFragment(ForYouFragment.newInstance());
                                    }
                                });
                        getSnackbar().show();
                    } else {
                        Snackbar.make(getNetError(), "Failed to load more data", Snackbar.LENGTH_LONG).setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //mListener.reCreateFragment(ForYouFragment.newInstance());
                            }
                        }).show();
                    }
                }
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
