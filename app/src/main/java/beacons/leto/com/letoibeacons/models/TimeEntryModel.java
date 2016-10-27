package beacons.leto.com.letoibeacons.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimeEntryModel {

    @SerializedName("data")
    @Expose
    private TimeEntry data;

    /**
     * No args constructor for use in serialization
     */
    public TimeEntryModel() {
    }

    /**
     * @param data
     */
    public TimeEntryModel(TimeEntry data) {
        this.data = data;
    }

    /**
     * @return The data
     */
    public TimeEntry getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(TimeEntry data) {
        this.data = data;
    }
}