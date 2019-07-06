package ml.medyas.wallbay.models.unsplash;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UnsplashCollectionsEntity implements Parcelable {

    public final static Parcelable.Creator<UnsplashCollectionsEntity> CREATOR = new Creator<UnsplashCollectionsEntity>() {


        @SuppressWarnings({
                "unchecked"
        })
        public UnsplashCollectionsEntity createFromParcel(Parcel in) {
            return new UnsplashCollectionsEntity(in);
        }

        public UnsplashCollectionsEntity[] newArray(int size) {
            return (new UnsplashCollectionsEntity[size]);
        }

    };
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private Object description;
    @SerializedName("published_at")
    @Expose
    private String publishedAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("curated")
    @Expose
    private Boolean curated;
    @SerializedName("featured")
    @Expose
    private Boolean featured;
    @SerializedName("total_photos")
    @Expose
    private Integer totalPhotos;
    @SerializedName("private")
    @Expose
    private Boolean _private;
    @SerializedName("share_key")
    @Expose
    private String shareKey;
    @SerializedName("tags")
    @Expose
    private List<Tag> tags = null;
    @SerializedName("cover_photo")
    @Expose
    private CoverPhoto coverPhoto;
    @SerializedName("preview_photos")
    @Expose
    private List<PreviewPhoto> previewPhotos = null;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("links")
    @Expose
    private Links3 links;

    protected UnsplashCollectionsEntity(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.description = in.readValue((Object.class.getClassLoader()));
        this.publishedAt = ((String) in.readValue((String.class.getClassLoader())));
        this.updatedAt = ((String) in.readValue((String.class.getClassLoader())));
        this.curated = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.featured = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.totalPhotos = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this._private = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.shareKey = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.tags, (Tag.class.getClassLoader()));
        this.coverPhoto = ((CoverPhoto) in.readValue((CoverPhoto.class.getClassLoader())));
        in.readList(this.previewPhotos, (PreviewPhoto.class.getClassLoader()));
        this.user = ((User) in.readValue((User.class.getClassLoader())));
        this.links = ((Links3) in.readValue((Links3.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     */
    public UnsplashCollectionsEntity() {
    }

    /**
     * @param tags
     * @param previewPhotos
     * @param totalPhotos
     * @param links
     * @param featured
     * @param shareKey
     * @param id
     * @param publishedAt
     * @param updatedAt
     * @param title
     * @param _private
     * @param description
     * @param coverPhoto
     * @param curated
     * @param user
     */
    public UnsplashCollectionsEntity(Integer id, String title, Object description, String publishedAt, String updatedAt, Boolean curated, Boolean featured, Integer totalPhotos, Boolean _private, String shareKey, List<Tag> tags, CoverPhoto coverPhoto, List<PreviewPhoto> previewPhotos, User user, Links3 links) {
        super();
        this.id = id;
        this.title = title;
        this.description = description;
        this.publishedAt = publishedAt;
        this.updatedAt = updatedAt;
        this.curated = curated;
        this.featured = featured;
        this.totalPhotos = totalPhotos;
        this._private = _private;
        this.shareKey = shareKey;
        this.tags = tags;
        this.coverPhoto = coverPhoto;
        this.previewPhotos = previewPhotos;
        this.user = user;
        this.links = links;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getCurated() {
        return curated;
    }

    public void setCurated(Boolean curated) {
        this.curated = curated;
    }

    public Boolean getFeatured() {
        return featured;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    public Integer getTotalPhotos() {
        return totalPhotos;
    }

    public void setTotalPhotos(Integer totalPhotos) {
        this.totalPhotos = totalPhotos;
    }

    public Boolean getPrivate() {
        return _private;
    }

    public void setPrivate(Boolean _private) {
        this._private = _private;
    }

    public String getShareKey() {
        return shareKey;
    }

    public void setShareKey(String shareKey) {
        this.shareKey = shareKey;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public CoverPhoto getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(CoverPhoto coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public List<PreviewPhoto> getPreviewPhotos() {
        return previewPhotos;
    }

    public void setPreviewPhotos(List<PreviewPhoto> previewPhotos) {
        this.previewPhotos = previewPhotos;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Links3 getLinks() {
        return links;
    }

    public void setLinks(Links3 links) {
        this.links = links;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(title);
        dest.writeValue(description);
        dest.writeValue(publishedAt);
        dest.writeValue(updatedAt);
        dest.writeValue(curated);
        dest.writeValue(featured);
        dest.writeValue(totalPhotos);
        dest.writeValue(_private);
        dest.writeValue(shareKey);
        dest.writeList(tags);
        dest.writeValue(coverPhoto);
        dest.writeList(previewPhotos);
        dest.writeValue(user);
        dest.writeValue(links);
    }

    public int describeContents() {
        return 0;
    }

}
