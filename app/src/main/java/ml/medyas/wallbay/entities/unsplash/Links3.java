package ml.medyas.wallbay.entities.unsplash;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Links3 implements Parcelable {

    public final static Parcelable.Creator<Links3> CREATOR = new Creator<Links3>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Links3 createFromParcel(Parcel in) {
            return new Links3(in);
        }

        public Links3[] newArray(int size) {
            return (new Links3[size]);
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
    @SerializedName("related")
    @Expose
    private String related;

    protected Links3(Parcel in) {
        this.self = ((String) in.readValue((String.class.getClassLoader())));
        this.html = ((String) in.readValue((String.class.getClassLoader())));
        this.photos = ((String) in.readValue((String.class.getClassLoader())));
        this.related = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     */
    public Links3() {
    }

    /**
     * @param photos
     * @param html
     * @param self
     * @param related
     */
    public Links3(String self, String html, String photos, String related) {
        super();
        this.self = self;
        this.html = html;
        this.photos = photos;
        this.related = related;
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

    public String getRelated() {
        return related;
    }

    public void setRelated(String related) {
        this.related = related;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(self);
        dest.writeValue(html);
        dest.writeValue(photos);
        dest.writeValue(related);
    }

    public int describeContents() {
        return 0;
    }

}
