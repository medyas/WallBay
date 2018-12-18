package ml.medyas.wallbay.entities.pexels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PexelsEntity implements Parcelable {

    public final static Parcelable.Creator<PexelsEntity> CREATOR = new Creator<PexelsEntity>() {


        @SuppressWarnings({
                "unchecked"
        })
        public PexelsEntity createFromParcel(Parcel in) {
            return new PexelsEntity(in);
        }

        public PexelsEntity[] newArray(int size) {
            return (new PexelsEntity[size]);
        }

    };
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("per_page")
    @Expose
    private Integer perPage;
    @SerializedName("photos")
    @Expose
    private List<Photo> photos = null;
    @SerializedName("next_page")
    @Expose
    private String nextPage;
    @SerializedName("prev_page")
    @Expose
    private String prevPage;

    protected PexelsEntity(Parcel in) {
        this.totalResults = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.page = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.perPage = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.photos, (Photo.class.getClassLoader()));
        this.nextPage = ((String) in.readValue((String.class.getClassLoader())));
        this.prevPage = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     */
    public PexelsEntity() {
    }

    /**
     * @param photos
     * @param prevPage
     * @param nextPage
     * @param page
     * @param totalResults
     * @param perPage
     */
    public PexelsEntity(Integer totalResults, Integer page, Integer perPage, List<Photo> photos, String nextPage, String prevPage) {
        super();
        this.totalResults = totalResults;
        this.page = page;
        this.perPage = perPage;
        this.photos = photos;
        this.nextPage = nextPage;
        this.prevPage = prevPage;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPerPage() {
        return perPage;
    }

    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public String getPrevPage() {
        return prevPage;
    }

    public void setPrevPage(String prevPage) {
        this.prevPage = prevPage;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(totalResults);
        dest.writeValue(page);
        dest.writeValue(perPage);
        dest.writeList(photos);
        dest.writeValue(nextPage);
        dest.writeValue(prevPage);
    }

    public int describeContents() {
        return 0;
    }

}
