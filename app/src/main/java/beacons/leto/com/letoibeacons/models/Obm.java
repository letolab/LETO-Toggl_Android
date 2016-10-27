package beacons.leto.com.letoibeacons.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

public class Obm extends SugarRecord {

    @SerializedName("included")
    @Expose
    private Boolean included;
    @SerializedName("nr")
    @Expose
    private Integer nr;

    /**
     * No args constructor for use in serialization
     *
     */
    public Obm() {
    }

    /**
     *
     * @param nr
     * @param included
     */
    public Obm(Boolean included, Integer nr) {
        this.included = included;
        this.nr = nr;
    }

    /**
     *
     * @return
     * The included
     */
    public Boolean getIncluded() {
        return included;
    }

    /**
     *
     * @param included
     * The included
     */
    public void setIncluded(Boolean included) {
        this.included = included;
    }

    /**
     *
     * @return
     * The nr
     */
    public Integer getNr() {
        return nr;
    }

    /**
     *
     * @param nr
     * The nr
     */
    public void setNr(Integer nr) {
        this.nr = nr;
    }

}