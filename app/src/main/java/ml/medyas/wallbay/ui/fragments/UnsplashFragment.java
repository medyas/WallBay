package ml.medyas.wallbay.ui.fragments;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import ml.medyas.wallbay.R;
import ml.medyas.wallbay.adapters.unsplash.UnsplashViewPagerAdapter;
import ml.medyas.wallbay.databinding.FragmentUnsplashBinding;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnUnsplashFragmentInteractions} interface
 * to handle interaction events.
 * Use the {@link UnsplashFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UnsplashFragment extends Fragment {
    private OnUnsplashFragmentInteractions mListener;
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

        binding.defaultLayout.lottieSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext(), R.style.SearchDialogStyle);

                dialog.getWindow().setGravity(Gravity.TOP);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.search_dialog);
                dialog.setCancelable(true);

                final EditText text = dialog.findViewById(R.id.dialog_text);
                ImageView search = dialog.findViewById(R.id.dialog_search);

                text.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View view, int i, KeyEvent keyEvent) {
                        if(i == KeyEvent.KEYCODE_ENTER) {
                            searchQuery(text.getText().toString());
                            dialog.dismiss();
                            return true;
                        }
                        return false;
                    }
                });

                search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        searchQuery(text.getText().toString());
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        return binding.getRoot();
    }

    private void searchQuery(String s) {
        if(!s.equals("")) {
            mListener.onAddFragment(UnsplashDefaultVPFragment.newInstance(3, s));
            mListener.updateToolbarTitle(s);
        } else {
            Toast.makeText(getContext(), "Please provide a search query!", Toast.LENGTH_SHORT).show();
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
        if (context instanceof OnUnsplashFragmentInteractions) {
            mListener = (OnUnsplashFragmentInteractions) context;
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
    public interface OnUnsplashFragmentInteractions {
        void onAddFragment(Fragment fragment);
        void updateToolbarTitle(String title);
    }
}
