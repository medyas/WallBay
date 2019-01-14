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

import ml.medyas.wallbay.R;
import ml.medyas.wallbay.entities.ImageEntity;
import ml.medyas.wallbay.models.unsplash.UnsplashViewModel;
import ml.medyas.wallbay.utils.Utils;

public class UnsplashDefaultVPFragment extends BaseFragment {
    private int position;
    private String orderBy;

    public static final String FRAGMENT_POSITION = "FRAGMENT_POSITION";

    public static Fragment newInstance(int position) {
        Fragment fragment = new UnsplashDefaultVPFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(FRAGMENT_POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {
            position = getArguments().getInt(FRAGMENT_POSITION);
        }

        switch (position) {
            case 0:
                orderBy = getResources().getString(R.string.popular);
                break;

            case 1:
                orderBy = getResources().getString(R.string.latest);
                break;

            case 2:
                orderBy = getResources().getString(R.string.oldest);
                break;
        }

        setUpViewModel();
    }

    @Override
    public void setUpViewModel() {
        UnsplashViewModel mViewModel = ViewModelProviders.of(this, new UnsplashViewModel.UnsplashViewModelFactory(getContext(), orderBy))
                .get(UnsplashViewModel.class);

        mViewModel.getPagedListLiveData().observe(this, new Observer<PagedList<ImageEntity>>() {
            @Override
            public void onChanged(@Nullable PagedList<ImageEntity> imageEntities) {
                getAdapter().submitList(imageEntities);
            }
        });

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
                        setSnackbar( Snackbar.make(getNetError(), "Network Error", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Retry", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        getNetError().setVisibility(View.GONE);
                                        //mListener.reCreateFragment(ForYouFragment.newInstance());
                                    }
                                }));
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
    }
}
