package ml.medyas.wallbay.ui.fragments;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
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

    private void searchQuery(String text) {
        mListener.onAddFragment(PixabayViewPagerFragment.newInstance(3, text));
        mListener.updateToolbarTitle(text);
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
