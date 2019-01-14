package ml.medyas.wallbay.ui.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import ml.medyas.wallbay.R;
import ml.medyas.wallbay.databinding.DialogImageDetailsInfoBinding;
import ml.medyas.wallbay.entities.ImageEntity;
import ml.medyas.wallbay.utils.Utils;

import static ml.medyas.wallbay.utils.Utils.PEXELS_PROFILE_URL;
import static ml.medyas.wallbay.utils.Utils.PEXELS_URL;
import static ml.medyas.wallbay.utils.Utils.PIXABAY_PROFILE_URL;
import static ml.medyas.wallbay.utils.Utils.PIXABAY_URL;
import static ml.medyas.wallbay.utils.Utils.UNSPLASH_PROFILE_URL;
import static ml.medyas.wallbay.utils.Utils.UNSPLASH_URL;

public class ImageDetailsInfoDialog extends DialogFragment {
    public static final String IMAGE_ENTITY = "IMAGE_ENTITY";
    private ImageEntity imageEntity;
    private Palette palette;

    public static ImageDetailsInfoDialog newInstance(ImageEntity imageEntity) {
        ImageDetailsInfoDialog dialog = new ImageDetailsInfoDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable(IMAGE_ENTITY, imageEntity);
        dialog.setArguments(bundle);
        return dialog;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @BindingAdapter({"android:tags"})
    public static void setTags(LinearLayout view, String tags) {
        if (!tags.equals("")) {
            String[] temp = tags.split(",");
            for (String txt : temp) {
                final TextView textView = new TextView(view.getContext());
                textView.setText(txt.trim());
                textView.setPadding(16, 16, 16, 16);
                textView.setTextColor(view.getContext().getResources().getColor(R.color.splashBackground));
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                textView.setBackgroundResource(R.drawable.tags_round_background);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(view.getContext(), textView.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(4, 4, 4, 4);
                textView.setLayoutParams(params);
                view.addView(textView);
            }
        }
    }

    @BindingAdapter({"android:setDimensions"})
    public static void setDimensions(TextView view, ImageEntity imageEntity) {
        view.setText(String.format("%d X %d px", imageEntity.getWidth(), imageEntity.getHeight()));
    }

    @BindingAdapter({"android:provider"})
    public static void providedBy(TextView view, Utils.webSite provider) {
        view.setText(Utils.getProviders(provider).toUpperCase());
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

        return binding.getRoot();
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
}
