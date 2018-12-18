package ml.medyas.wallbay.entities.unsplash;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UnsplashCollectionSearchEntity implements Parcelable {

    public final static Parcelable.Creator<UnsplashCollectionSearchEntity> CREATOR = new Creator<UnsplashCollectionSearchEntity>() {


        @SuppressWarnings({
                "unchecked"
        })
        public UnsplashCollectionSearchEntity createFromParcel(Parcel in) {
            return new UnsplashCollectionSearchEntity(in);
        }

        public UnsplashCollectionSearchEntity[] newArray(int size) {
            return (new UnsplashCollectionSearchEntity[size]);
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
    private List<UnsplashCollectionsEntity> results = null;

    protected UnsplashCollectionSearchEntity(Parcel in) {
        this.total = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.totalPages = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.results, (UnsplashCollectionsEntity.class.getClassLoader()));
    }

    /**
     * No args constructor for use in serialization
     */
    public UnsplashCollectionSearchEntity() {
    }

    /**
     * @param total
     * @param results
     * @param totalPages
     */
    public UnsplashCollectionSearchEntity(Integer total, Integer totalPages, List<UnsplashCollectionsEntity> results) {
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

    public List<UnsplashCollectionsEntity> getUnsplashCollectionSearchEntitys() {
        return results;
    }

    public void setUnsplashCollectionSearchEntitys(List<UnsplashCollectionsEntity> results) {
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