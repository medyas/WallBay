package ml.medyas.wallbay.ui.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Observable;

import ml.medyas.wallbay.R;
import ml.medyas.wallbay.entities.ImageEntity;
import ml.medyas.wallbay.models.search.SearchViewModel;
import ml.medyas.wallbay.models.search.SearchViewModelFactory;
import ml.medyas.wallbay.services.NetworkChangeReceiver;
import ml.medyas.wallbay.utils.Utils;

import static ml.medyas.wallbay.utils.Utils.INTEREST_CATEGORIES;
import static ml.medyas.wallbay.utils.Utils.getNetworkStatus;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ForYouFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForYouFragment extends BaseFragment implements java.util.Observer {
    private OnForYouFragmentInteractions mListener;
    private SearchViewModel mViewModel;

    public static final String TAG = "ml.medyas.wallbay.ui.fragments.ForYouFragment";

    public ForYouFragment() {
        // Required empty public constructor
    }

    public static ForYouFragment newInstance() {
        return new ForYouFragment();
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        setUpViewModel();
    }


    @Override
    public void setUpViewModel() {

        if (mViewModel == null) {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
            String interests = pref.getString(INTEREST_CATEGORIES, "");
            if (interests != null && !interests.equals("")) {
                mViewModel = ViewModelProviders.of(this, new SearchViewModelFactory(interests)).get(SearchViewModel.class);
            }
        }


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
                    if (getAdapter().getCurrentList() == null ||getAdapter().getCurrentList().size() == 0) {
                        getNetError().setVisibility(View.VISIBLE);
                        getItemLoad().setVisibility(View.GONE);
                        setSnackbar(Snackbar.make(getNetError(), "Network Error", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Retry", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        getNetError().setVisibility(View.GONE);
                                        mListener.reCreateFragment(ForYouFragment.newInstance());
                                    }
                                }));
                        getSnackbar().show();
                    } else {
                        Snackbar.make(getNetError(), "Failed to load more data", Snackbar.LENGTH_LONG).setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mListener.reCreateFragment(ForYouFragment.newInstance());
                            }
                        }).show();
                    }
                }
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_activity_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_favorite) {
            mListener.onAddFragment(FavoriteFragment.newInstance());
        }
        return super.onOptionsItemSelected(item);
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
    public void onPause() {
        super.onPause();
        NetworkChangeReceiver.getObservable().deleteObserver(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        NetworkChangeReceiver.getObservable().addObserver(this);
    }

    @Override
    public void update(Observable observable, Object o) {
        if (getNetworkStatus(getContext()) && getNetError().getVisibility() == View.VISIBLE) {
            mListener.reCreateFragment(ForYouFragment.newInstance());
            getSnackbar().dismiss();
        }
    }

    public interface OnForYouFragmentInteractions {
        void onAddFragment(Fragment fragment);
        void reCreateFragment(Fragment fragment);
    }

}
