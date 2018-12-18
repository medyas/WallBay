package ml.medyas.wallbay.entities.pexels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Src implements Parcelable {

    public final static Parcelable.Creator<Src> CREATOR = new Creator<Src>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Src createFromParcel(Parcel in) {
            return new Src(in);
        }

        public Src[] newArray(int size) {
            return (new Src[size]);
        }

    };
    @SerializedName("original")
    @Expose
    private String original;
    @SerializedName("large2x")
    @Expose
    private String large2x;
    @SerializedName("large")
    @Expose
    private String large;
    @SerializedName("medium")
    @Expose
    private String medium;
    @SerializedName("small")
    @Expose
    private String small;
    @SerializedName("portrait")
    @Expose
    private String portrait;
    @SerializedName("square")
    @Expose
    private String square;
    @SerializedName("landscape")
    @Expose
    private String landscape;
    @SerializedName("tiny")
    @Expose
    private String tiny;

    protected Src(Parcel in) {
        this.original = ((String) in.readValue((String.class.getClassLoader())));
        this.large2x = ((String) in.readValue((String.class.getClassLoader())));
        this.large = ((String) in.readValue((String.class.getClassLoader())));
        this.medium = ((String) in.readValue((String.class.getClassLoader())));
        this.small = ((String) in.readValue((String.class.getClassLoader())));
        this.portrait = ((String) in.readValue((String.class.getClassLoader())));
        this.square = ((String) in.readValue((String.class.getClassLoader())));
        this.landscape = ((String) in.readValue((String.class.getClassLoader())));
        this.tiny = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     */
    public Src() {
    }

    /**
     * @param tiny
     * @param original
     * @param square
     * @param landscape
     * @param small
     * @param portrait
     * @param medium
     * @param large
     * @param large2x
     */
    public Src(String original, String large2x, String large, String medium, String small, String portrait, String square, String landscape, String tiny) {
        super();
        this.original = original;
        this.large2x = large2x;
        this.large = large;
        this.medium = medium;
        this.small = small;
        this.portrait = portrait;
        this.square = square;
        this.landscape = landscape;
        this.tiny = tiny;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getLarge2x() {
        return large2x;
    }

    public void setLarge2x(String large2x) {
        this.large2x = large2x;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getSquare() {
        return square;
    }

    public void setSquare(String square) {
        this.square = square;
    }

    public String getLandscape() {
        return landscape;
    }

    public void setLandscape(String landscape) {
        this.landscape = landscape;
    }

    public String getTiny() {
        return tiny;
    }

    public void setTiny(String tiny) {
        this.tiny = tiny;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(original);
        dest.writeValue(large2x);
        dest.writeValue(large);
        dest.writeValue(medium);
        dest.writeValue(small);
        dest.writeValue(portrait);
        dest.writeValue(square);
        dest.writeValue(landscape);
        dest.writeValue(tiny);
    }

    public int describeContents() {
        return 0;
    }

}