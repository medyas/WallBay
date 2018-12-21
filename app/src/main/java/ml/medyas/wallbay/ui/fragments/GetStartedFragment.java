package ml.medyas.wallbay.ui.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ml.medyas.wallbay.R;
import ml.medyas.wallbay.adapter.GetStartedAdapter;
import ml.medyas.wallbay.databinding.FragmentGetStartedBinding;
import ml.medyas.wallbay.entities.GetStartedEntity;

import static ml.medyas.wallbay.utils.Utils.calculateNoOfColumns;
import static ml.medyas.wallbay.utils.Utils.getCategoriesFromList;
import static ml.medyas.wallbay.utils.Utils.getCategoriesList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnGetStartedFragmentInteractions} interface
 * to handle interaction events.
 * Use the {@link GetStartedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GetStartedFragment extends Fragment {
    public static final String CATEGORIES_LIST = "categories_list";
    private OnGetStartedFragmentInteractions mListener;
    private List<GetStartedEntity> mList;

    public GetStartedFragment() {
        // Required empty public constructor
    }

    public static GetStartedFragment newInstance() {
        return new GetStartedFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            mList = new ArrayList<>(getCategoriesList());
        } else {
            mList = savedInstanceState.getParcelableArrayList(CATEGORIES_LIST);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(CATEGORIES_LIST, (ArrayList<? extends Parcelable>) mList);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentGetStartedBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_get_started, container, false);

        binding.getStartedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<GetStartedEntity> list = new ArrayList<>(getSelectedList());
                if (!list.isEmpty()) {
                    mListener.onGetStartedDone(getCategoriesFromList(list));
                } else {
                    Toast.makeText(getContext(), "Please select a category !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.getStartedRecyclerView.setHasFixedSize(true);
        binding.getStartedRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), calculateNoOfColumns(getContext())));
        binding.getStartedRecyclerView.setAdapter(new GetStartedAdapter(mList));


        return binding.getRoot();

    }

    private List<GetStartedEntity> getSelectedList() {
        List<GetStartedEntity> list = new ArrayList<>();
        for (GetStartedEntity item : mList) {
            if (item.isSelected()) {
                list.add(item);
            }
        }

        return list;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnGetStartedFragmentInteractions) {
            mListener = (OnGetStartedFragmentInteractions) context;
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
    public interface OnGetStartedFragmentInteractions {
        void onGetStartedDone(String categories);
    }
}
