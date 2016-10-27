package beacons.leto.com.letoibeacons.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

public class TogglUser extends SugarRecord {

    @SerializedName("since")
    @Expose
    private Integer since;
    @SerializedName("data")
    @Expose
    private Data data;

    /**
     * No args constructor for use in serialization
     *
     */
    public TogglUser() {
    }

    /**
     *
     * @param data
     * @param since
     */
    public TogglUser(Integer since, Data data) {
        this.since = since;
        this.data = data;
    }

    /**
     *
     * @return
     * The since
     */
    public Integer getSince() {
        return since;
    }

    /**
     *
     * @param since
     * The since
     */
    public void setSince(Integer since) {
        this.since = since;
    }

    /**
     *
     * @return
     * The data
     */
    public Data getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(Data data) {
        this.data = data;
    }

}