package ml.medyas.wallbay.ui.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import ml.medyas.wallbay.R;
import ml.medyas.wallbay.entities.ImageEntity;
import ml.medyas.wallbay.models.unsplash.UnsplashCollectionPhotoViewModel;
import ml.medyas.wallbay.models.unsplash.UnsplashSearchViewModel;
import ml.medyas.wallbay.models.unsplash.UnsplashViewModel;
import ml.medyas.wallbay.utils.Utils;

import static ml.medyas.wallbay.utils.Utils.FRAGMENT_POSITION;
import static ml.medyas.wallbay.utils.Utils.SEARCH_QUERY;

public class UnsplashDefaultVPFragment extends BaseFragment {
    public static final String COLLECTION_ID = "COLLECTION_ID";
    private int position;
    private String orderBy;
    private String query;
    private int collectionId;

    public static final String TAG = "ml.medyas.wallbay.ui.fragments.UnsplashDefaultVPFragment";

    public static Fragment newInstance(int position) {
        Fragment fragment = new UnsplashDefaultVPFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(FRAGMENT_POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static Fragment newInstance(int position, String query) {
        Fragment fragment = new UnsplashDefaultVPFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(FRAGMENT_POSITION, position);
        bundle.putString(SEARCH_QUERY, query);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static Fragment newInstance(int position, int id) {
        Fragment fragment = new UnsplashDefaultVPFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(FRAGMENT_POSITION, position);
        bundle.putInt(COLLECTION_ID, id);
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

            if(getArguments().containsKey(COLLECTION_ID)) {
                collectionId = getArguments().getInt(COLLECTION_ID);
            }
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
        ViewModel mViewModel;
        if(position == 4) {
            mViewModel = ViewModelProviders.of(this, new UnsplashCollectionPhotoViewModel.UnsplashCollectionPhotoViewModelFactory(getContext(), collectionId))
                    .get(UnsplashCollectionPhotoViewModel.class);

            ((UnsplashCollectionPhotoViewModel) mViewModel).getPagedListLiveData().observe(this, new Observer<PagedList<ImageEntity>>() {
                @Override
                public void onChanged(@Nullable PagedList<ImageEntity> imageEntities) {
                    getAdapter().submitList(imageEntities);
                }
            });

            ((UnsplashCollectionPhotoViewModel) mViewModel).getNetworkStateLiveData().observe(this, new Observer<Utils.NetworkState>() {
                @Override
                public void onChanged(@Nullable Utils.NetworkState networkState) {
                    checkNetworkStatus(networkState, false);
                }
            });
        } else if(position == 3) {
             mViewModel = ViewModelProviders.of(this, new UnsplashSearchViewModel.UnsplashSearchViewModelFactory(getContext(), query))
                    .get(UnsplashSearchViewModel.class);

             ((UnsplashSearchViewModel) mViewModel).getNetworkStateLiveData().observe(this, new Observer<Utils.NetworkState>() {
                 @Override
                 public void onChanged(@Nullable Utils.NetworkState networkState) {
                     checkNetworkStatus(networkState, false);
                 }
             });

             ((UnsplashSearchViewModel) mViewModel).getPagedListLiveData().observe(this, new Observer<PagedList<ImageEntity>>() {
                 @Override
                 public void onChanged(@Nullable PagedList<ImageEntity> imageEntities) {
                     getAdapter().submitList(imageEntities);
                 }
             });
        } else {
            mViewModel = ViewModelProviders.of(this, new UnsplashViewModel.UnsplashViewModelFactory(getContext(), orderBy))
                    .get(UnsplashViewModel.class);

            ((UnsplashViewModel) mViewModel).getPagedListLiveData().observe(this, new Observer<PagedList<ImageEntity>>() {
                @Override
                public void onChanged(@Nullable PagedList<ImageEntity> imageEntities) {
                    getAdapter().submitList(imageEntities);
                }
            });

            ((UnsplashViewModel) mViewModel).getNetworkStateLiveData().observe(this, new Observer<Utils.NetworkState>() {
                @Override
                public void onChanged(@Nullable Utils.NetworkState networkState) {
                    checkNetworkStatus(networkState, false);
                }
            });
        }
    }
}
