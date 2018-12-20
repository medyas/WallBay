package ml.medyas.wallbay.entities;

import android.databinding.BindingAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class GetStartedEntity {
    private String categoryName;
    private int categoryImage;

    public GetStartedEntity() {
    }

    public GetStartedEntity(String categoryName, int categoryImage) {
        this.categoryName = categoryName;
        this.categoryImage = categoryImage;
    }

    @BindingAdapter({"android:loadImage"})
    public static void loadImage(ImageView view, int imageUrl) {
        Glide.with(view.getContext())
                .load(imageUrl)
                .into(view);
    }

    @BindingAdapter({"android:toCap"})
    public static void toCap(TextView view, String text) {
        view.setText(text.substring(0, 1).toUpperCase() + text.substring(1));
    }

    public int getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(int categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
