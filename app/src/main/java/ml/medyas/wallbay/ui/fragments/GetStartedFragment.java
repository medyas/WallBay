package ml.medyas.wallbay.ui.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ml.medyas.wallbay.R;
import ml.medyas.wallbay.adapter.GetStartedAdapter;
import ml.medyas.wallbay.databinding.FragmentGetStartedBinding;

import static ml.medyas.wallbay.utils.Utils.calculateNoOfColumns;
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
    private OnGetStartedFragmentInteractions mListener;
    private FragmentGetStartedBinding binding;

    public GetStartedFragment() {
        // Required empty public constructor
    }

    public static GetStartedFragment newInstance() {
        GetStartedFragment fragment = new GetStartedFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_get_started, container, false);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_get_started, container, false);

        binding.getStartedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onGetStartedDone();
            }
        });

        binding.getStartedRecyclerView.setHasFixedSize(true);
        binding.getStartedRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), calculateNoOfColumns(getContext())));
        binding.getStartedRecyclerView.setAdapter(new GetStartedAdapter(getCategoriesList(), getContext()));


        return binding.getRoot();

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
        void onGetStartedDone();
    }
}
