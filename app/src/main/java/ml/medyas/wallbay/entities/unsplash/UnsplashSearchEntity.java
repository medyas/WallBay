package ml.medyas.wallbay.entities.unsplash;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UnsplashSearchEntity implements Parcelable {

    public final static Parcelable.Creator<UnsplashPhotoEntity> CREATOR = new Creator<UnsplashPhotoEntity>() {


        @SuppressWarnings({
                "unchecked"
        })
        public UnsplashPhotoEntity createFromParcel(Parcel in) {
            return new UnsplashPhotoEntity(in);
        }

        public UnsplashPhotoEntity[] newArray(int size) {
            return (new UnsplashPhotoEntity[size]);
        }

    };
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("results")
    @Expose
    private List<UnsplashPhotoEntity> results = null;

    protected UnsplashSearchEntity(Parcel in) {
        this.total = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.totalPages = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.results, (ml.medyas.wallbay.entities.unsplash.UnsplashPhotoEntity.class.getClassLoader()));
    }

    /**
     * No args constructor for use in serialization
     */
    public UnsplashSearchEntity() {
    }

    /**
     * @param total
     * @param results
     * @param totalPages
     */
    public UnsplashSearchEntity(Integer total, Integer totalPages, List<UnsplashPhotoEntity> results) {
        super();
        this.total = total;
        this.totalPages = totalPages;
        this.results = results;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public List<UnsplashPhotoEntity> getUnsplashPhotoEntitys() {
        return results;
    }

    public void setUnsplashPhotoEntitys(List<UnsplashPhotoEntity> results) {
        this.results = results;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(total);
        dest.writeValue(totalPages);
        dest.writeList(results);
    }

    public int describeContents() {
        return 0;
    }
}
