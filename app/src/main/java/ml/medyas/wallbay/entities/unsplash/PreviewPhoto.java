package ml.medyas.wallbay.entities.unsplash;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PreviewPhoto implements Parcelable {

    public final static Parcelable.Creator<PreviewPhoto> CREATOR = new Creator<PreviewPhoto>() {


        @SuppressWarnings({
                "unchecked"
        })
        public PreviewPhoto createFromParcel(Parcel in) {
            return new PreviewPhoto(in);
        }

        public PreviewPhoto[] newArray(int size) {
            return (new PreviewPhoto[size]);
        }

    };
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("urls")
    @Expose
    private Urls urls;

    protected PreviewPhoto(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.urls = ((Urls) in.readValue((Urls.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     */
    public PreviewPhoto() {
    }

    /**
     * @param id
     * @param urls
     */
    public PreviewPhoto(String id, Urls urls) {
        super();
        this.id = id;
        this.urls = urls;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Urls getUrls() {
        return urls;
    }

    public void setUrls(Urls urls) {
        this.urls = urls;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(urls);
    }

    public int describeContents() {
        return 0;
    }

}
