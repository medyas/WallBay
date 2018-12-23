package ml.medyas.wallbay.entities;

import android.databinding.BindingAdapter;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.TextView;

import ml.medyas.wallbay.utils.GlideApp;

public class GetStartedEntity implements Parcelable {
    private String categoryName;
    private int categoryImage;
    public static final Creator<GetStartedEntity> CREATOR = new Creator<GetStartedEntity>() {
        @Override
        public GetStartedEntity createFromParcel(Parcel in) {
            return new GetStartedEntity(in);
        }

        @Override
        public GetStartedEntity[] newArray(int size) {
            return new GetStartedEntity[size];
        }
    };

    public GetStartedEntity() {
    }

    private Boolean selected;

    public GetStartedEntity(String categoryName, int categoryImage, boolean selected) {
        this.categoryName = categoryName;
        this.categoryImage = categoryImage;
        this.selected = selected;
    }

    protected GetStartedEntity(Parcel in) {
        categoryName = in.readString();
        categoryImage = in.readInt();
        selected = in.readByte() != 0;
    }

    @BindingAdapter({"android:loadImage"})
    public static void loadImage(ImageView view, int imageUrl) {
        GlideApp.with(view.getContext())
                .load(imageUrl)
                .into(view);
    }

    @BindingAdapter({"android:toCap"})
    public static void toCap(TextView view, String text) {
        view.setText(String.format("%s%s", text.substring(0, 1).toUpperCase(), text.substring(1)));
    }


    public Boolean isSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(categoryName);
        parcel.writeInt(categoryImage);
        parcel.writeByte((byte) (selected ? 1 : 0));
    }
}
