package ml.medyas.wallbay.entities.unsplash;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Links_ implements Parcelable {

    public final static Parcelable.Creator<Links_> CREATOR = new Creator<Links_>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Links_ createFromParcel(Parcel in) {
            return new Links_(in);
        }

        public Links_[] newArray(int size) {
            return (new Links_[size]);
        }

    };
    @SerializedName("self")
    @Expose
    private String self;
    @SerializedName("html")
    @Expose
    private String html;
    @SerializedName("photos")
    @Expose
    private String photos;
    @SerializedName("likes")
    @Expose
    private String likes;
    @SerializedName("portfolio")
    @Expose
    private String portfolio;
    @SerializedName("following")
    @Expose
    private String following;
    @SerializedName("followers")
    @Expose
    private String followers;

    protected Links_(Parcel in) {
        this.self = ((String) in.readValue((String.class.getClassLoader())));
        this.html = ((String) in.readValue((String.class.getClassLoader())));
        this.photos = ((String) in.readValue((String.class.getClassLoader())));
        this.likes = ((String) in.readValue((String.class.getClassLoader())));
        this.portfolio = ((String) in.readValue((String.class.getClassLoader())));
        this.following = ((String) in.readValue((String.class.getClassLoader())));
        this.followers = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     */
    public Links_() {
    }

    /**
     * @param photos
     * @param followers
     * @param following
     * @param portfolio
     * @param likes
     * @param html
     * @param self
     */
    public Links_(String self, String html, String photos, String likes, String portfolio, String following, String followers) {
        super();
        this.self = self;
        this.html = html;
        this.photos = photos;
        this.likes = likes;
        this.portfolio = portfolio;
        this.following = following;
        this.followers = followers;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(String portfolio) {
        this.portfolio = portfolio;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(self);
        dest.writeValue(html);
        dest.writeValue(photos);
        dest.writeValue(likes);
        dest.writeValue(portfolio);
        dest.writeValue(following);
        dest.writeValue(followers);
    }

    public int describeContents() {
        return 0;
    }

}