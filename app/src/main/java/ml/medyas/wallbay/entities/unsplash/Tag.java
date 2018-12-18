package ml.medyas.wallbay.entities.unsplash;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tag implements Parcelable {

    public final static Parcelable.Creator<Tag> CREATOR = new Creator<Tag>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Tag createFromParcel(Parcel in) {
            return new Tag(in);
        }

        public Tag[] newArray(int size) {
            return (new Tag[size]);
        }

    };
    @SerializedName("title")
    @Expose
    private String title;

    protected Tag(Parcel in) {
        this.title = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     */
    public Tag() {
    }

    /**
     * @param title
     */
    public Tag(String title) {
        super();
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(title);
    }

    public int describeContents() {
        return 0;
    }

}