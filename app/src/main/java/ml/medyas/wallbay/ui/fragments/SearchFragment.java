package ml.medyas.wallbay.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import ml.medyas.wallbay.R;
import ml.medyas.wallbay.databinding.FragmentSearchBinding;
import ml.medyas.wallbay.utils.Utils;

import static ml.medyas.wallbay.utils.Utils.hideKeyboard;


public class SearchFragment extends Fragment {

    private Utils.OnFragmentInteractions mListener;
    public static final String TAG = "ml.medyas.wallbay.ui.fragments.SearchFragment";

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
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
        final FragmentSearchBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);

        binding.searchDialog.dialogText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    searchQuery(binding.searchDialog.dialogText.getText().toString());
                    return true;
                }
                return false;
            }
        });

        binding.searchDialog.dialogSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchQuery(binding.searchDialog.dialogText.getText().toString());
            }
        });

        return binding.getRoot();
    }

    private void searchQuery(String text) {
        if(!text.equals("")) {
            hideKeyboard(getActivity());
            mListener.onAddFragment(SearchDefaultFragment.newInstance(text));
            mListener.updateToolbarTitle(text);
        } else {
            Toast.makeText(getContext(), getString(R.string.provide_query), Toast.LENGTH_SHORT).show();
        }
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
