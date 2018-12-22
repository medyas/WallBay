package ml.medyas.wallbay.ui.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ml.medyas.wallbay.R;
import ml.medyas.wallbay.adapters.foryou.ForYouAdapter;
import ml.medyas.wallbay.databinding.FragmentForYouBinding;
import ml.medyas.wallbay.entities.ImageEntity;
import ml.medyas.wallbay.models.foryou.ForYouViewModel;
import ml.medyas.wallbay.models.foryou.ForYouViewModelFactory;
import ml.medyas.wallbay.utils.Utils;

import static ml.medyas.wallbay.utils.Utils.INTEREST_CATEGORIES;
import static ml.medyas.wallbay.utils.Utils.calculateNoOfColumns;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnForYouFragmentInteractions} interface
 * to handle interaction events.
 * Use the {@link ForYouFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForYouFragment extends Fragment implements ForYouAdapter.onImageItemClicked {
    private OnForYouFragmentInteractions mListener;
    private ForYouViewModel mViewModel;
    private ForYouAdapter mAdapter;

    public ForYouFragment() {
        // Required empty public constructor
    }

    public static ForYouFragment newInstance() {
        return new ForYouFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String interests = pref.getString(INTEREST_CATEGORIES, "");
        if (interests != null && !interests.equals("")) {
            mViewModel = ViewModelProviders.of(this, new ForYouViewModelFactory(interests)).get(ForYouViewModel.class);
        }
        mAdapter = new ForYouAdapter(this);
        mViewModel.getPagedListLiveData().observe(this, new Observer<PagedList<ImageEntity>>() {
            @Override
            public void onChanged(@Nullable PagedList<ImageEntity> imageEntities) {
                Log.d("mainactivity", String.format("image list size: %d", imageEntities.size()));
                mAdapter.submitList(imageEntities);
            }
        });

        mViewModel.getNetworkStateLiveData().observe(this, new Observer<Utils.NetworkState>() {
            @Override
            public void onChanged(@Nullable Utils.NetworkState networkState) {
                if (networkState == Utils.NetworkState.LOADED) {
                    Log.d("mainactivity", "loaded data");
                } else if (networkState == Utils.NetworkState.LOADING) {
                    Log.d("mainactivity", "loading data");
                } else if (networkState == Utils.NetworkState.FAILED) {
                    Log.d("mainactivity", "failed to load");
                }
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentForYouBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_for_you, container, false);


        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(calculateNoOfColumns(getContext(), 200), StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);

        /*layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                if(i==0) {
                    return calculateNoOfColumns(getContext(), 200);
                }
                return 1;
            }
        });*/
        binding.forYouRecyclerView.setLayoutManager(layoutManager);
        binding.forYouRecyclerView.setHasFixedSize(false);
        binding.forYouRecyclerView.setNestedScrollingEnabled(false);
        binding.forYouRecyclerView.setAdapter(mAdapter);

        return binding.getRoot();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnForYouFragmentInteractions) {
            mListener = (OnForYouFragmentInteractions) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClicked(int position) {
        Log.d("mainactivity", String.format("clicked item: %d", position));
    }

    @Override
    public void onAddToFavorite(int position) {
        Log.d("mainactivity", String.format("add to fav item: %d", position));
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnForYouFragmentInteractions {
        void onImageClicked(ImageEntity imageEntity);
    }

    public abstract class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
        // The minimum amount of items to have below your current scroll position
        // before loading more.
        private int visibleThreshold = 0;

        // The current offset index of data you have loaded
        private int currentPage = 1;

        // The total number of items in the dataset after the last load
        private int previousTotalItemCount = 0;

        // True if we are still waiting for the last set of data to load.
        private boolean loading = true;

        // Sets the starting page index
        private int startingPageIndex = 0;

        private RecyclerView.LayoutManager mLayoutManager;

        public EndlessRecyclerViewScrollListener(LinearLayoutManager layoutManager) {
            this.mLayoutManager = layoutManager;
        }

        public EndlessRecyclerViewScrollListener(GridLayoutManager layoutManager) {
            this.mLayoutManager = layoutManager;
            visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
        }

        public EndlessRecyclerViewScrollListener(StaggeredGridLayoutManager layoutManager) {
            this.mLayoutManager = layoutManager;
            visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
        }

        public int getLastVisibleItem(int[] lastVisibleItemPositions) {
            int maxSize = 0;
            for (int i = 0; i < lastVisibleItemPositions.length; i++) {
                if (i == 0) {
                    maxSize = lastVisibleItemPositions[i];
                } else if (lastVisibleItemPositions[i] > maxSize) {
                    maxSize = lastVisibleItemPositions[i];
                }
            }
            return maxSize;
        }

        // This happens many times a second during a scroll, so be wary of the code you place here.
        // We are given a few useful parameters to help us work out if we need to load some more data,
        // but first we check if we are waiting for the previous load to finish.
        @Override
        public void onScrolled(RecyclerView view, int dx, int dy) {
            int lastVisibleItemPosition = 0;
            int totalItemCount = mLayoutManager.getItemCount();

            if (mLayoutManager instanceof StaggeredGridLayoutManager) {
                int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(null);
                // get maximum element within the list
                lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
            } else if (mLayoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
            } else if (mLayoutManager instanceof LinearLayoutManager) {
                lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
            }

            // If it’s still loading, we check to see if the dataset count has
            // changed, if so we conclude it has finished loading and update the current page
            // number and total item count.
            if (loading && (totalItemCount > previousTotalItemCount)) {
                loading = false;
                previousTotalItemCount = totalItemCount;
            }

            // If it isn’t currently loading, we check to see if we have breached
            // the visibleThreshold and need to reload more data.
            // If we do need to reload some more data, we execute onLoadMore to fetch the data.
            // threshold should reflect how many total columns there are too
            if (!loading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount
                    && view.getAdapter().getItemCount() > visibleThreshold) {// This condition will useful when recyclerview has less than visibleThreshold items
                currentPage++;
                onLoadMore(currentPage, totalItemCount, view);
                loading = true;
            }
        }

        // Call whenever performing new searches
        public void resetState() {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = 0;
            this.loading = true;
        }

        // Defines the process for actually loading more data based on page
        public abstract void onLoadMore(int page, int totalItemsCount, RecyclerView view);

    }
}
