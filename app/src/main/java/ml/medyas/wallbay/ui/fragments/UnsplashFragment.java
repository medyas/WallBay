package ml.medyas.wallbay.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import ml.medyas.wallbay.R;
import ml.medyas.wallbay.adapters.unsplash.UnsplashViewPagerAdapter;
import ml.medyas.wallbay.databinding.FragmentUnsplashBinding;
import ml.medyas.wallbay.utils.Utils;


public class UnsplashFragment extends Fragment {
    private Utils.OnFragmentInteractions mListener;
    public static final String TAG = "ml.medyas.wallbay.ui.fragments.UnsplashFragment";

    public UnsplashFragment() {
        // Required empty public constructor
    }

    public static UnsplashFragment newInstance() {
        return new UnsplashFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentUnsplashBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_unsplash, container, false);

        binding.defaultLayout.viewPager.setAdapter(new UnsplashViewPagerAdapter(getChildFragmentManager(), getContext()));
        binding.defaultLayout.tabLayout.setupWithViewPager(binding.defaultLayout.viewPager);
        binding.defaultLayout.viewPager.setCurrentItem(0);
        ViewCompat.setElevation(binding.defaultLayout.viewPager, 4 * getResources().getDisplayMetrics().density);

        return binding.getRoot();
    }

    private void searchQuery(String s) {
        if(!s.equals("")) {
            mListener.onAddFragment(UnsplashDefaultVPFragment.newInstance(3, s));
            mListener.updateToolbarTitle(s);
        } else {
            Toast.makeText(getContext(), getString(R.string.provide_query), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);

        final MenuItem myActionMenuItem = menu.findItem( R.id.menu_search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if( ! searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();

                searchQuery(query);

                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
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
        if (context instanceof Utils.OnFragmentInteractions) {
            mListener = (Utils.OnFragmentInteractions) context;
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


}
