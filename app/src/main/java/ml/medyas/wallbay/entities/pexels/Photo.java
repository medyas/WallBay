package ml.medyas.wallbay.entities.pexels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Photo implements Parcelable {

    public final static Parcelable.Creator<Photo> CREATOR = new Creator<Photo>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        public Photo[] newArray(int size) {
            return (new Photo[size]);
        }

    };
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("width")
    @Expose
    private Integer width;
    @SerializedName("height")
    @Expose
    private Integer height;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("photographer")
    @Expose
    private String photographer;
    @SerializedName("photographer_url")
    @Expose
    private String photographerUrl;
    @SerializedName("src")
    @Expose
    private Src src;

    protected Photo(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.width = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.height = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.url = ((String) in.readValue((String.class.getClassLoader())));
        this.photographer = ((String) in.readValue((String.class.getClassLoader())));
        this.photographerUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.src = ((Src) in.readValue((Src.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     */
    public Photo() {
    }

    /**
     * @param id
     * @param photographerUrl
     * @param height
     * @param width
     * @param photographer
     * @param src
     * @param url
     */
    public Photo(Integer id, Integer width, Integer height, String url, String photographer, String photographerUrl, Src src) {
        super();
        this.id = id;
        this.width = width;
        this.height = height;
        this.url = url;
        this.photographer = photographer;
        this.photographerUrl = photographerUrl;
        this.src = src;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPhotographer() {
        return photographer;
    }

    public void setPhotographer(String photographer) {
        this.photographer = photographer;
    }

    public String getPhotographerUrl() {
        return photographerUrl;
    }

    public void setPhotographerUrl(String photographerUrl) {
        this.photographerUrl = photographerUrl;
    }

    public Src getSrc() {
        return src;
    }

    public void setSrc(Src src) {
        this.src = src;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(width);
        dest.writeValue(height);
        dest.writeValue(url);
        dest.writeValue(photographer);
        dest.writeValue(photographerUrl);
        dest.writeValue(src);
    }

    public int describeContents() {
        return 0;
    }

}
