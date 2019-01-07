package ml.medyas.wallbay.ui.fragments;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
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

import ml.medyas.wallbay.R;
import ml.medyas.wallbay.databinding.DialogImageDetailsInfoBinding;
import ml.medyas.wallbay.entities.ImageEntity;
import ml.medyas.wallbay.utils.Utils;

public class ImageDetailsInfoDialog extends DialogFragment {
    public static final String IMAGE_ENTITY = "IMAGE_ENTITY";
    private ImageEntity imageEntity;
    private Palette palette;
    private DialogImageDetailsInfoBinding binding;

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
                TextView textView = new TextView(view.getContext());
                textView.setText(txt.trim());
                textView.setPadding(16, 16, 16, 16);
                textView.setTextColor(view.getContext().getResources().getColor(R.color.splashBackground));
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                textView.setBackgroundResource(R.drawable.tags_round_background);
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
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_image_details_info, container, false);
        binding.setImageDetails(imageEntity);
        binding.setPalette(palette);

        return binding.getRoot();
    }

    public void setPalette(Palette palette) {
        this.palette = palette;
    }
}
