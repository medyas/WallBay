package ml.medyas.wallbay.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import ml.medyas.wallbay.entities.unsplash.PreviewPhoto;
import ml.medyas.wallbay.entities.unsplash.Tag;


public class CollectionEntity implements Parcelable {
    private int id;
    private String title;
    private int totalPhotos;
    public static final Creator<CollectionEntity> CREATOR = new Creator<CollectionEntity>() {
        @Override
        public CollectionEntity createFromParcel(Parcel in) {
            return new CollectionEntity(in);
        }

        @Override
        public CollectionEntity[] newArray(int size) {
            return new CollectionEntity[size];
        }
    };
    private String username;
    private String userImg;
    private List<Tag> tags;
    private List<PreviewPhoto> imagePreviews;

    public CollectionEntity() {
    }

    public CollectionEntity(int id, String title, int totalPhotos, List<Tag> tags, String username, String userImg, List<PreviewPhoto> imagePreviews) {
        this.id = id;
        this.title = title;
        this.totalPhotos = totalPhotos;
        this.tags = tags;
        this.username = username;
        this.userImg = userImg;
        this.imagePreviews = imagePreviews;
    }

    protected CollectionEntity(Parcel in) {
        id = in.readInt();
        title = in.readString();
        totalPhotos = in.readInt();
        tags = in.createTypedArrayList(Tag.CREATOR);
        username = in.readString();
        userImg = in.readString();
        imagePreviews = in.createTypedArrayList(PreviewPhoto.CREATOR);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTotalPhotos() {
        return totalPhotos;
    }

    public void setTotalPhotos(int totalPhotos) {
        this.totalPhotos = totalPhotos;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public List<PreviewPhoto> getImagePreviews() {
        return imagePreviews;
    }

    public void setImagePreviews(List<PreviewPhoto> imagePreviews) {
        this.imagePreviews = imagePreviews;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeInt(totalPhotos);
        parcel.writeTypedList(tags);
        parcel.writeString(username);
        parcel.writeString(userImg);
        parcel.writeTypedList(imagePreviews);
    }
}
