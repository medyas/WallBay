package ml.medyas.wallbay.entities.unsplash;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileImage implements Parcelable {

    public final static Parcelable.Creator<ProfileImage> CREATOR = new Creator<ProfileImage>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ProfileImage createFromParcel(Parcel in) {
            return new ProfileImage(in);
        }

        public ProfileImage[] newArray(int size) {
            return (new ProfileImage[size]);
        }

    };
    @SerializedName("small")
    @Expose
    private String small;
    @SerializedName("medium")
    @Expose
    private String medium;
    @SerializedName("large")
    @Expose
    private String large;

    protected ProfileImage(Parcel in) {
        this.small = ((String) in.readValue((String.class.getClassLoader())));
        this.medium = ((String) in.readValue((String.class.getClassLoader())));
        this.large = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     */
    public ProfileImage() {
    }

    /**
     * @param small
     * @param large
     * @param medium
     */
    public ProfileImage(String small, String medium, String large) {
        super();
        this.small = small;
        this.medium = medium;
        this.large = large;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(small);
        dest.writeValue(medium);
        dest.writeValue(large);
    }

    public int describeContents() {
        return 0;
    }

}