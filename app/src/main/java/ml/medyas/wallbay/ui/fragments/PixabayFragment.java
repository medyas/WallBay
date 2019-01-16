package ml.medyas.wallbay.ui.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ml.medyas.wallbay.R;
import ml.medyas.wallbay.adapters.pixabay.PixabayViewPagerAdapter;
import ml.medyas.wallbay.databinding.FragmentPixabayBinding;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnPixabayFragmentInteractions} interface
 * to handle interaction events.
 * Use the {@link PixabayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PixabayFragment extends Fragment {

    private OnPixabayFragmentInteractions mListener;
    public static final String TAG = "ml.medyas.wallbay.ui.fragments.PixabayFragment";

    public PixabayFragment() {
        // Required empty public constructor
    }

    public static PixabayFragment newInstance() {
        return new PixabayFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentPixabayBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pixabay, container, false);

        binding.defaultLayout.viewPager.setAdapter(new PixabayViewPagerAdapter(getChildFragmentManager(), getContext()));
        binding.defaultLayout.tabLayout.setupWithViewPager(binding.defaultLayout.viewPager);
        binding.defaultLayout.viewPager.setCurrentItem(0);
        ViewCompat.setElevation(binding.defaultLayout.viewPager, 4 * getResources().getDisplayMetrics().density);

        return binding.getRoot();
    }

    private void searchQuery(String text) {
        if(!text.equals("")) {
            mListener.onAddFragment(PixabayViewPagerFragment.newInstance(3, text));
            mListener.updateToolbarTitle(text);
        } else {
            Toast.makeText(getContext(), "Please provide a search query!", Toast.LENGTH_SHORT).show();
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
        int id = item.getItemId();
        if (id == R.id.menu_favorite) {
            mListener.onAddFragment(FavoriteFragment.newInstance());
        } else if(id == R.id.menu_search) {

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPixabayFragmentInteractions) {
            mListener = (OnPixabayFragmentInteractions) context;
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
    public void onResume() {
        super.onResume();
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
    public interface OnPixabayFragmentInteractions {
        void onAddFragment(Fragment fragment);
        void updateToolbarTitle(String title);
    }
}
