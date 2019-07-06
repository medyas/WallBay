package ml.medyas.wallbay.ui.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import ml.medyas.wallbay.R;
import ml.medyas.wallbay.adapters.unsplash.TagsRecyclerViewAdapter;
import ml.medyas.wallbay.databinding.DialogImageDetailsInfoBinding;
import ml.medyas.wallbay.models.ImageEntity;
import ml.medyas.wallbay.utils.Utils;

import static ml.medyas.wallbay.utils.Utils.PEXELS_PROFILE_URL;
import static ml.medyas.wallbay.utils.Utils.PEXELS_URL;
import static ml.medyas.wallbay.utils.Utils.PIXABAY_PROFILE_URL;
import static ml.medyas.wallbay.utils.Utils.PIXABAY_URL;
import static ml.medyas.wallbay.utils.Utils.UNSPLASH_PROFILE_URL;
import static ml.medyas.wallbay.utils.Utils.UNSPLASH_URL;

public class ImageDetailsInfoDialog extends DialogFragment implements TagsRecyclerViewAdapter.OnTagItemClicked {
    public static final String IMAGE_ENTITY = "IMAGE_ENTITY";
    private ImageEntity imageEntity;
    private Palette palette;
    private OnImageDialogInteractions mListener;

    public static ImageDetailsInfoDialog newInstance(ImageEntity imageEntity) {
        ImageDetailsInfoDialog dialog = new ImageDetailsInfoDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable(IMAGE_ENTITY, imageEntity);
        dialog.setArguments(bundle);
        return dialog;
    }

    @BindingAdapter({"android:setDimensions"})
    public static void setDimensions(TextView view, ImageEntity imageEntity) {
        view.setText(String.format(view.getContext().getResources().getConfiguration().locale, "%d X %d px", imageEntity.getWidth(), imageEntity.getHeight()));
    }

    @BindingAdapter({"android:provider"})
    public static void providedBy(TextView view, Utils.webSite provider) {
        view.setText(Utils.getProviders(provider).toUpperCase(view.getContext().getResources().getConfiguration().locale));
    }

    @BindingAdapter({"android:setBackground"})
    public static void setBackground(ImageView view, Palette.Swatch color) {
        if (color != null) {
            view.setBackgroundColor(color.getRgb());
        } else {
            view.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            imageEntity = getArguments().getParcelable(IMAGE_ENTITY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DialogImageDetailsInfoBinding binding = DataBindingUtil.inflate(inflater, R.layout.dialog_image_details_info, container, false);
        binding.setImageDetails(imageEntity);
        binding.setPalette(palette);
        binding.setFragment(this);

        if(!imageEntity.getTags().equals("")) {
            binding.dialogRecyclerView.setHasFixedSize(true);
            binding.dialogRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

            String[] tags = imageEntity.getTags().split(",");
            if (tags.length != 0) {
                List<String> list = new ArrayList<>();
                for(String tag: tags) {
                    list.add(tag.trim());
                }
                binding.dialogRecyclerView.setAdapter(new TagsRecyclerViewAdapter(list, this));
            }
        }

        return binding.getRoot();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnImageDialogInteractions) {
            mListener = (OnImageDialogInteractions) context;
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

    public void setPalette(Palette palette) {
        this.palette = palette;
    }


    public void onUserClicked(View view) {
        String url = "";
        switch (imageEntity.getProvider()) {
            case PEXELS:
                url = PEXELS_PROFILE_URL + imageEntity.getUserName();
                break;

            case UNSPLASH:
                url = UNSPLASH_PROFILE_URL + imageEntity.getUserName();
                break;

            case PIXABAY:
                url = PIXABAY_PROFILE_URL + imageEntity.getUserName();
                break;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        Intent chooser = Intent.createChooser(intent, getString(R.string.choose_browser));
        startActivity(chooser);
    }

    public void onPaletteClicked(View view) {

        // Gets a handle to the clipboard service.
        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);

        ColorDrawable color = (ColorDrawable) view.getBackground();
        // Creates a new text clip to put on the clipboard
        ClipData clip = ClipData.newPlainText("Palette Color", String.format("#%s", Integer.toHexString(color.getColor()).substring(2)));
        clipboard.setPrimaryClip(clip);

        Toast.makeText(getContext(), String.format("Color copied: #%s", Integer.toHexString(color.getColor()).substring(2)), Toast.LENGTH_SHORT).show();
    }

    public void onLinkClicked(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(imageEntity.getUrl()));
        Intent chooser = Intent.createChooser(intent, getString(R.string.choose_browser));
        startActivity(chooser);
    }

    public void onProviderClicked(View view) {
        String url = "";
        switch (imageEntity.getProvider()) {
            case PEXELS:
                url = PEXELS_URL;
                break;

            case UNSPLASH:
                url = UNSPLASH_URL;
                break;

            case PIXABAY:
                url = PIXABAY_URL;
                break;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        Intent chooser = Intent.createChooser(intent, getString(R.string.choose_browser));
        startActivity(chooser);
    }

    @Override
    public void onTagItemClicked(String query) {

        switch (imageEntity.getProvider()) {
            case PEXELS:
                mListener.onTagItemPressed(PexelsViewPagerFragment.newInstance(2, query), query);
                break;

            case PIXABAY:
                mListener.onTagItemPressed(PixabayViewPagerFragment.newInstance(3, query), query);
                break;

            case UNSPLASH:
                mListener.onTagItemPressed(UnsplashDefaultVPFragment.newInstance(4, query), query);
                break;
        }

        dismiss();
    }


    public interface OnImageDialogInteractions {
        void onTagItemPressed(Fragment fragment, String query);
    }
}
