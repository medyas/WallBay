package ml.medyas.wallbay.entities;

import android.annotation.TargetApi;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.databinding.BindingAdapter;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;

import ml.medyas.wallbay.R;
import ml.medyas.wallbay.utils.GlideApp;
import ml.medyas.wallbay.utils.ProviderTypeConverter;
import ml.medyas.wallbay.utils.Utils;

@Entity(tableName = "favorite")
public class ImageEntity implements Parcelable {

    @Ignore
    public static DiffUtil.ItemCallback<ImageEntity> DIFF_CALLBACK = new DiffUtil.ItemCallback<ImageEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull ImageEntity imageEntity, @NonNull ImageEntity t1) {
            return imageEntity.getId().equals(t1.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull ImageEntity imageEntity, @NonNull ImageEntity t1) {
            return imageEntity.equals(t1);
        }
    };


    @NonNull
    @PrimaryKey
    private String id = "";
    private String userName;
    private String userImg;
    @TypeConverters(ProviderTypeConverter.class)
    private Utils.webSite provider;
    private int likes;
    private int views;
    private int downloads;
    private int width;
    private int height;
    private String url;
    private String originalImage;
    private String originalUrl;
    private String previewImage;
    private String tags;

    @Ignore
    public ImageEntity() {
    }

    public ImageEntity(@NonNull String id, String userName, String userImg, Utils.webSite provider, int likes, int views, int downloads, int width, int height, String url, String originalImage, String originalUrl, String previewImage, String tags) {
        this.id = id;
        this.userName = userName;
        this.userImg = userImg;
        this.provider = provider;
        this.likes = likes;
        this.views = views;
        this.downloads = downloads;
        this.width = width;
        this.height = height;
        this.url = url;
        this.originalImage = originalImage;
        this.originalUrl = originalUrl;
        this.previewImage = previewImage;
        this.tags = tags;
    }

    protected ImageEntity(Parcel in) {
        id = in.readString();
        userName = in.readString();
        userImg = in.readString();
        likes = in.readInt();
        views = in.readInt();
        downloads = in.readInt();
        width = in.readInt();
        height = in.readInt();
        url = in.readString();
        originalImage = in.readString();
        originalUrl = in.readString();
        previewImage = in.readString();
        tags = in.readString();
        provider = (Utils.webSite) in.readSerializable();
    }

    public static final Creator<ImageEntity> CREATOR = new Creator<ImageEntity>() {
        @Override
        public ImageEntity createFromParcel(Parcel in) {
            return new ImageEntity(in);
        }

        @Override
        public ImageEntity[] newArray(int size) {
            return new ImageEntity[size];
        }
    };

    @Ignore
    @BindingAdapter({"android:loadImage"})
    public static void loadImage(ImageView view, String imageUrl) {
        GlideApp.with(view.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_image_black_24dp)
                .error(R.drawable.ic_image_black_24dp)
                .into(view);
    }

    @Ignore
    @BindingAdapter({"android:loadRoundImage"})
    public static void loadRoundImage(ImageView view, String imageUrl) {
        GlideApp.with(view.getContext())
                .load(imageUrl)
                .apply(new RequestOptions().circleCrop())
                .placeholder(R.drawable.ic_image_black_24dp)
                .error(R.drawable.ic_person_black_24dp)
                .into(view);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Ignore
    @BindingAdapter({"android:addTransitionName"})
    public static void addTransitionName(ImageView view, String id) {
        view.setTransitionName(String.format("transition %s", id));
    }

    @Ignore
    @BindingAdapter({"android:setText"})
    public static void setText(TextView view, int num) {
        if (num >= 100000) {
            String temp = String.valueOf(num);
            view.setText(String.format("%sM", temp.substring(0, temp.length() - 5)));
            return;
        }
        if (num >= 1000) {
            String temp = String.valueOf(num);
            view.setText(String.format("%sK", temp.substring(0, temp.length() - 3)));
            return;
        }
        view.setText(String.format(view.getContext().getResources().getConfiguration().locale, "%d", num));
    }


    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public Utils.webSite getProvider() {
        return provider;
    }

    public void setProvider(Utils.webSite provider) {
        this.provider = provider;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getDownloads() {
        return downloads;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOriginalImage() {
        return originalImage;
    }

    public void setOriginalImage(String originalImage) {
        this.originalImage = originalImage;
    }

    public String getPreviewImage() {
        return previewImage;
    }

    public void setPreviewImage(String previewImage) {
        this.previewImage = previewImage;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(userName);
        parcel.writeString(userImg);
        parcel.writeInt(likes);
        parcel.writeInt(views);
        parcel.writeInt(downloads);
        parcel.writeInt(width);
        parcel.writeInt(height);
        parcel.writeString(url);
        parcel.writeString(originalImage);
        parcel.writeString(originalUrl);
        parcel.writeString(previewImage);
        parcel.writeString(tags);
        parcel.writeSerializable(provider);
    }
}
