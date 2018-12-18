package ml.medyas.wallbay.entities.pixabay;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Hit implements Parcelable {

    public final static Parcelable.Creator<Hit> CREATOR = new Creator<Hit>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Hit createFromParcel(Parcel in) {
            return new Hit(in);
        }

        public Hit[] newArray(int size) {
            return (new Hit[size]);
        }

    };
    @SerializedName("largeImageURL")
    @Expose
    private String largeImageURL;
    @SerializedName("webformatHeight")
    @Expose
    private Integer webformatHeight;
    @SerializedName("webformatWidth")
    @Expose
    private Integer webformatWidth;
    @SerializedName("likes")
    @Expose
    private Integer likes;
    @SerializedName("imageWidth")
    @Expose
    private Integer imageWidth;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("views")
    @Expose
    private Integer views;
    @SerializedName("comments")
    @Expose
    private Integer comments;
    @SerializedName("pageURL")
    @Expose
    private String pageURL;
    @SerializedName("imageHeight")
    @Expose
    private Integer imageHeight;
    @SerializedName("webformatURL")
    @Expose
    private String webformatURL;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("previewHeight")
    @Expose
    private Integer previewHeight;
    @SerializedName("tags")
    @Expose
    private String tags;
    @SerializedName("downloads")
    @Expose
    private Integer downloads;
    @SerializedName("user")
    @Expose
    private String user;
    @SerializedName("favorites")
    @Expose
    private Integer favorites;
    @SerializedName("imageSize")
    @Expose
    private Integer imageSize;
    @SerializedName("previewWidth")
    @Expose
    private Integer previewWidth;
    @SerializedName("userImageURL")
    @Expose
    private String userImageURL;
    @SerializedName("previewURL")
    @Expose
    private String previewURL;

    protected Hit(Parcel in) {
        this.largeImageURL = ((String) in.readValue((String.class.getClassLoader())));
        this.webformatHeight = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.webformatWidth = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.likes = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.imageWidth = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.userId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.views = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.comments = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.pageURL = ((String) in.readValue((String.class.getClassLoader())));
        this.imageHeight = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.webformatURL = ((String) in.readValue((String.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
        this.previewHeight = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.tags = ((String) in.readValue((String.class.getClassLoader())));
        this.downloads = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.user = ((String) in.readValue((String.class.getClassLoader())));
        this.favorites = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.imageSize = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.previewWidth = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.userImageURL = ((String) in.readValue((String.class.getClassLoader())));
        this.previewURL = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     */
    public Hit() {
    }

    /**
     * @param tags
     * @param webformatHeight
     * @param imageHeight
     * @param previewHeight
     * @param previewURL
     * @param imageSize
     * @param favorites
     * @param previewWidth
     * @param type
     * @param downloads
     * @param userImageURL
     * @param pageURL
     * @param largeImageURL
     * @param id
     * @param views
     * @param likes
     * @param userId
     * @param webformatWidth
     * @param webformatURL
     * @param user
     * @param imageWidth
     * @param comments
     */
    public Hit(String largeImageURL, Integer webformatHeight, Integer webformatWidth, Integer likes, Integer imageWidth, Integer id, Integer userId, Integer views, Integer comments, String pageURL, Integer imageHeight, String webformatURL, String type, Integer previewHeight, String tags, Integer downloads, String user, Integer favorites, Integer imageSize, Integer previewWidth, String userImageURL, String previewURL) {
        super();
        this.largeImageURL = largeImageURL;
        this.webformatHeight = webformatHeight;
        this.webformatWidth = webformatWidth;
        this.likes = likes;
        this.imageWidth = imageWidth;
        this.id = id;
        this.userId = userId;
        this.views = views;
        this.comments = comments;
        this.pageURL = pageURL;
        this.imageHeight = imageHeight;
        this.webformatURL = webformatURL;
        this.type = type;
        this.previewHeight = previewHeight;
        this.tags = tags;
        this.downloads = downloads;
        this.user = user;
        this.favorites = favorites;
        this.imageSize = imageSize;
        this.previewWidth = previewWidth;
        this.userImageURL = userImageURL;
        this.previewURL = previewURL;
    }

    public String getLargeImageURL() {
        return largeImageURL;
    }

    public void setLargeImageURL(String largeImageURL) {
        this.largeImageURL = largeImageURL;
    }

    public Integer getWebformatHeight() {
        return webformatHeight;
    }

    public void setWebformatHeight(Integer webformatHeight) {
        this.webformatHeight = webformatHeight;
    }

    public Integer getWebformatWidth() {
        return webformatWidth;
    }

    public void setWebformatWidth(Integer webformatWidth) {
        this.webformatWidth = webformatWidth;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(Integer imageWidth) {
        this.imageWidth = imageWidth;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    public String getPageURL() {
        return pageURL;
    }

    public void setPageURL(String pageURL) {
        this.pageURL = pageURL;
    }

    public Integer getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(Integer imageHeight) {
        this.imageHeight = imageHeight;
    }

    public String getWebformatURL() {
        return webformatURL;
    }

    public void setWebformatURL(String webformatURL) {
        this.webformatURL = webformatURL;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getPreviewHeight() {
        return previewHeight;
    }

    public void setPreviewHeight(Integer previewHeight) {
        this.previewHeight = previewHeight;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Integer getDownloads() {
        return downloads;
    }

    public void setDownloads(Integer downloads) {
        this.downloads = downloads;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Integer getFavorites() {
        return favorites;
    }

    public void setFavorites(Integer favorites) {
        this.favorites = favorites;
    }

    public Integer getImageSize() {
        return imageSize;
    }

    public void setImageSize(Integer imageSize) {
        this.imageSize = imageSize;
    }

    public Integer getPreviewWidth() {
        return previewWidth;
    }

    public void setPreviewWidth(Integer previewWidth) {
        this.previewWidth = previewWidth;
    }

    public String getUserImageURL() {
        return userImageURL;
    }

    public void setUserImageURL(String userImageURL) {
        this.userImageURL = userImageURL;
    }

    public String getPreviewURL() {
        return previewURL;
    }

    public void setPreviewURL(String previewURL) {
        this.previewURL = previewURL;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(largeImageURL);
        dest.writeValue(webformatHeight);
        dest.writeValue(webformatWidth);
        dest.writeValue(likes);
        dest.writeValue(imageWidth);
        dest.writeValue(id);
        dest.writeValue(userId);
        dest.writeValue(views);
        dest.writeValue(comments);
        dest.writeValue(pageURL);
        dest.writeValue(imageHeight);
        dest.writeValue(webformatURL);
        dest.writeValue(type);
        dest.writeValue(previewHeight);
        dest.writeValue(tags);
        dest.writeValue(downloads);
        dest.writeValue(user);
        dest.writeValue(favorites);
        dest.writeValue(imageSize);
        dest.writeValue(previewWidth);
        dest.writeValue(userImageURL);
        dest.writeValue(previewURL);
    }

    public int describeContents() {
        return 0;
    }

}
