package ml.medyas.wallbay.entities;

import android.os.Parcel;
import android.os.Parcelable;


public class CollectionEntity implements Parcelable {
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
    private int id;
    private String title;
    private int totalPhotos;
    private String[] tags;
    private String username;
    private String userImg;
    private String[] imagePreviews;

    public CollectionEntity(int id, String title, int totalPhotos, String[] tags, String username, String userImg, String[] imagePreviews) {
        this.id = id;
        this.title = title;
        this.totalPhotos = totalPhotos;
        this.tags = tags;
        this.username = username;
        this.userImg = userImg;
        this.imagePreviews = imagePreviews;
    }

    public CollectionEntity() {
    }

    protected CollectionEntity(Parcel in) {
        id = in.readInt();
        title = in.readString();
        totalPhotos = in.readInt();
        tags = in.createStringArray();
        username = in.readString();
        userImg = in.readString();
        imagePreviews = in.createStringArray();
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

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
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

    public String[] getImagePreviews() {
        return imagePreviews;
    }

    public void setImagePreviews(String[] imagePreviews) {
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
        parcel.writeStringArray(tags);
        parcel.writeString(username);
        parcel.writeString(userImg);
        parcel.writeStringArray(imagePreviews);
    }
}
