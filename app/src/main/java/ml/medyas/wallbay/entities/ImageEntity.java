package ml.medyas.wallbay.entities;

import android.os.Parcel;
import android.os.Parcelable;

import ml.medyas.wallbay.utils.Utils;

public class ImageEntity implements Parcelable {
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
    private String id;
    private String userName;
    private String userImg;
    private Utils.webSite provider;
    private int likes;
    private int views;
    private int downloads;
    private int width;
    private int height;
    private String url;
    private String originalImage;
    private String previewImage;
    private String[] tags;

    public ImageEntity() {
    }

    public ImageEntity(String id, String userName, String userImg, Utils.webSite provider, int likes, int views, int downloads, int width, int height, String url, String originalImage, String previewImage, String[] tags) {
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
        previewImage = in.readString();
        tags = in.createStringArray();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
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
        parcel.writeString(previewImage);
        parcel.writeStringArray(tags);
    }
}
