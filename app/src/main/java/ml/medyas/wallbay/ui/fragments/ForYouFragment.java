package ml.medyas.wallbay.ui.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import ml.medyas.wallbay.R;
import ml.medyas.wallbay.adapters.foryou.ForYouAdapter;
import ml.medyas.wallbay.databinding.FragmentForYouBinding;
import ml.medyas.wallbay.entities.ImageEntity;
import ml.medyas.wallbay.models.foryou.ForYouViewModel;
import ml.medyas.wallbay.models.foryou.ForYouViewModelFactory;
import ml.medyas.wallbay.utils.Utils;

import static ml.medyas.wallbay.utils.Utils.INTEREST_CATEGORIES;
import static ml.medyas.wallbay.utils.Utils.calculateNoOfColumns;
import static ml.medyas.wallbay.utils.Utils.convertPixelsToDp;

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
    private FragmentForYouBinding binding;

    public ForYouFragment() {
        // Required empty public constructor
    }

    public static ForYouFragment newInstance() {
        return new ForYouFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        retryRequest();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_for_you, container, false);

        mAdapter = new ForYouAdapter(this);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(calculateNoOfColumns(getContext(), convertPixelsToDp(getResources().getDimension(R.dimen.item_width), getContext())), StaggeredGridLayoutManager.VERTICAL);
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
        binding.forYouRecyclerView.setAdapter(mAdapter);

        return binding.getRoot();
    }

    private void retryRequest() {

        if (mViewModel != null) {
            if (mViewModel.getPagedListLiveData().hasActiveObservers()) {
                mViewModel.getPagedListLiveData().removeObservers(this);
                mViewModel.getNetworkStateLiveData().removeObservers(this);
            }
        } else {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
            String interests = pref.getString(INTEREST_CATEGORIES, "");
            if (interests != null && !interests.equals("")) {
                mViewModel = ViewModelProviders.of(this, new ForYouViewModelFactory(interests)).get(ForYouViewModel.class);
            }
        }


        mViewModel.getPagedListLiveData().observe(this, new Observer<PagedList<ImageEntity>>() {
            @Override
            public void onChanged(@Nullable PagedList<ImageEntity> imageEntities) {
                mAdapter.submitList(imageEntities);
            }
        });

        mViewModel.getNetworkStateLiveData().observe(this, new Observer<Utils.NetworkState>() {
            @Override
            public void onChanged(@Nullable Utils.NetworkState networkState) {
                if (networkState == Utils.NetworkState.LOADED) {
                    binding.loadErrorLayout.itemLoad.setVisibility(View.GONE);
                    binding.forYouRecyclerView.setVisibility(View.VISIBLE);

                } else if (networkState == Utils.NetworkState.LOADING) {

                } else if (networkState == Utils.NetworkState.FAILED) {
                    if (mAdapter.getCurrentList().size() == 0) {
                        binding.loadErrorLayout.netError.setVisibility(View.VISIBLE);
                        binding.loadErrorLayout.itemLoad.setVisibility(View.GONE);
                        Snackbar.make(binding.loadErrorLayout.netError, "Network Error", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Retray", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        binding.loadErrorLayout.netError.setVisibility(View.GONE);
                                        binding.loadErrorLayout.itemLoad.setVisibility(View.VISIBLE);

                                        retryRequest();
                                    }
                                }).show();
                    } else {
                        Snackbar.make(binding.loadErrorLayout.netError, "Failed to load more data", Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });
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
    public void onItemClicked(ImageEntity imageEntity, ImageView itemImage) {
        ImageDetailsFragment frag = ImageDetailsFragment.newInstance(imageEntity);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            frag.setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move).setDuration(300));
            //frag.setEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.slide_right));
            frag.setExitTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.slide_left));
        }

        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.main_container, frag, frag.getClass().getName())
                .addToBackStack(frag.getClass().getName())
                .addSharedElement(itemImage, String.format("transition %s", imageEntity.getId()))
                .commit();

        mListener.onSetOnBackToolbar(true);
    }

    @Override
    public void onAddToFavorite(ImageEntity position) {

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

        void onSetOnBackToolbar(boolean setup);
    }
}
