package ml.medyas.wallbay.ui.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ml.medyas.wallbay.R;
import ml.medyas.wallbay.databinding.FragmentImageDetailsBinding;
import ml.medyas.wallbay.entities.ImageEntity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnImageDetailsFragmentInteractions} interface
 * to handle interaction events.
 * Use the {@link ImageDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageDetailsFragment extends Fragment {
    public static final String IMAGE_ENTITY = "IMAGE_ENTITY";
    private OnImageDetailsFragmentInteractions mListener;
    private ImageEntity imageEntity;

    public ImageDetailsFragment() {
        // Required empty public constructor
    }

    public static ImageDetailsFragment newInstance(ImageEntity imageEntity) {
        ImageDetailsFragment frag = new ImageDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(IMAGE_ENTITY, imageEntity);
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imageEntity = getArguments().getParcelable(IMAGE_ENTITY);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentImageDetailsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_image_details, container, false);

        binding.setImage(imageEntity);

        binding.onbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onImageBackPressed();
            }
        });

        return binding.getRoot();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnImageDetailsFragmentInteractions) {
            mListener = (OnImageDetailsFragmentInteractions) context;
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
    public interface OnImageDetailsFragmentInteractions {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

        void onImageBackPressed();
    }
}
