package ml.medyas.wallbay.ui.fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import ml.medyas.wallbay.R;
import ml.medyas.wallbay.databinding.FragmentImageDetailsBinding;
import ml.medyas.wallbay.entities.ImageEntity;
import ml.medyas.wallbay.utils.GlideApp;

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

    //TODO: save visibility status of the fabs

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

        postponeEnterTransition();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final FragmentImageDetailsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_image_details, container, false);

        binding.setImage(imageEntity);
        GlideApp.with(this)
                .asDrawable()
                .load(imageEntity.getPreviewImage())
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        startPostponedEnterTransition();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        startPostponedEnterTransition();
                        return false;
                    }
                })
                .into(binding.photoView);

        binding.itemToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onImageBackPressed();
            }
        });

        binding.itemPlus.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {

                Display mdisp = getActivity().getWindowManager().getDefaultDisplay();
                Point mdispSize = new Point();
                mdisp.getSize(mdispSize);
                int maxX = mdispSize.x;
                int maxY = mdispSize.y;

                AnimatorSet decSet2 = new AnimatorSet();
                decSet2.playTogether(
                        ObjectAnimator.ofFloat(binding.floatingActionButton, View.ALPHA, 0, 1),
                        ObjectAnimator.ofFloat(binding.floatingActionButton2, View.ALPHA, 0, 1),
                        ObjectAnimator.ofFloat(binding.floatingActionButton3, View.ALPHA, 0, 1),
                        ObjectAnimator.ofFloat(binding.floatingActionButton4, View.ALPHA, 0, 1),
                        ObjectAnimator.ofFloat(binding.floatingActionButton5, View.ALPHA, 0, 1),
                        ObjectAnimator.ofFloat(binding.floatingActionButton, "y", maxY - 350),
                        ObjectAnimator.ofFloat(binding.floatingActionButton2, "y", maxY - 350),
                        ObjectAnimator.ofFloat(binding.floatingActionButton3, "y", maxY - 250),
                        ObjectAnimator.ofFloat(binding.floatingActionButton4, "y", maxY - 350),
                        ObjectAnimator.ofFloat(binding.floatingActionButton5, "y", maxY - 350),
                        ObjectAnimator.ofFloat(binding.itemPlus, View.ALPHA, 1, 0),
                        ObjectAnimator.ofFloat(binding.itemPlus, "y", maxY)
                );
                decSet2.setDuration(500);
                decSet2.start();
            }
        });

        binding.floatingActionButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Display mdisp = getActivity().getWindowManager().getDefaultDisplay();
                Point mdispSize = new Point();
                mdisp.getSize(mdispSize);
                int maxX = mdispSize.x;
                int maxY = mdispSize.y;

                AnimatorSet decSet2 = new AnimatorSet();
                decSet2.playTogether(
                        ObjectAnimator.ofFloat(binding.floatingActionButton, View.ALPHA, 1, 0),
                        ObjectAnimator.ofFloat(binding.floatingActionButton2, View.ALPHA, 1, 0),
                        ObjectAnimator.ofFloat(binding.floatingActionButton3, View.ALPHA, 1, 0),
                        ObjectAnimator.ofFloat(binding.floatingActionButton4, View.ALPHA, 1, 0),
                        ObjectAnimator.ofFloat(binding.floatingActionButton5, View.ALPHA, 1, 0),
                        ObjectAnimator.ofFloat(binding.floatingActionButton, "y", maxY),
                        ObjectAnimator.ofFloat(binding.floatingActionButton2, "y", maxY),
                        ObjectAnimator.ofFloat(binding.floatingActionButton3, "y", maxY),
                        ObjectAnimator.ofFloat(binding.floatingActionButton4, "y", maxY),
                        ObjectAnimator.ofFloat(binding.floatingActionButton5, "y", maxY),
                        ObjectAnimator.ofFloat(binding.itemPlus, View.ALPHA, 0, 1),
                        ObjectAnimator.ofFloat(binding.itemPlus, "y", maxY - 200)
                );
                decSet2.setDuration(500);
                decSet2.start();
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
