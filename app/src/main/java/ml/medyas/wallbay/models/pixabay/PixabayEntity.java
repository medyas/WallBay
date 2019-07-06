package ml.medyas.wallbay.models.pixabay;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class PixabayEntity implements Parcelable {

    public final static Parcelable.Creator<PixabayEntity> CREATOR = new Creator<PixabayEntity>() {


        @SuppressWarnings({
                "unchecked"
        })
        public PixabayEntity createFromParcel(Parcel in) {
            return new PixabayEntity(in);
        }

        public PixabayEntity[] newArray(int size) {
            return (new PixabayEntity[size]);
        }

    };
    @SerializedName("totalHits")
    @Expose
    private Integer totalHits;
    @SerializedName("hits")
    @Expose
    private List<Hit> hits = null;
    @SerializedName("total")
    @Expose
    private Integer total;

    protected PixabayEntity(Parcel in) {
        this.totalHits = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.hits, (Hit.class.getClassLoader()));
        this.total = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     */
    public PixabayEntity() {
    }

    /**
     * @param total
     * @param hits
     * @param totalHits
     */
    public PixabayEntity(Integer totalHits, List<Hit> hits, Integer total) {
        super();
        this.totalHits = totalHits;
        this.hits = hits;
        this.total = total;
    }

    public Integer getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(Integer totalHits) {
        this.totalHits = totalHits;
    }

    public List<Hit> getHits() {
        return hits;
    }

    public void setHits(List<Hit> hits) {
        this.hits = hits;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(totalHits);
        dest.writeList(hits);
        dest.writeValue(total);
    }

    public int describeContents() {
        return 0;
    }

}

