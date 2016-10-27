package beacons.leto.com.letoibeacons.models;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimeEntry {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("guid")
    @Expose
    private String guid;
    @SerializedName("wid")
    @Expose
    private Integer wid;
    @SerializedName("pid")
    @Expose
    private Integer pid;
    @SerializedName("billable")
    @Expose
    private Boolean billable;
    @SerializedName("start")
    @Expose
    private String start;
    @SerializedName("stop")
    @Expose
    private String stop;
    @SerializedName("duration")
    @Expose
    private Integer duration;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("tags")
    @Expose
    private List<String> tags = new ArrayList<String>();
    @SerializedName("duronly")
    @Expose
    private Boolean duronly;
    @SerializedName("at")
    @Expose
    private String at;
    @SerializedName("uid")
    @Expose
    private Integer uid;
    @SerializedName("created_with")
    @Expose
    private String createdWith;
    private Project projectObj = new Project("-", "15");
    private String durationString;

    /**
     * No args constructor for use in serialization
     *
     */
    public TimeEntry() {
    }

    /**
     *
     * @param uid
     * @param tags
     * @param stop
     * @param wid
     * @param at
     * @param pid
     * @param duronly
     * @param billable
     * @param id
     * @param guid
     * @param duration
     * @param start
     * @param description
     */
    public TimeEntry(Integer id, String guid, Integer wid, Integer pid, Boolean billable, String start, String stop, Integer duration, String description, List<String> tags, Boolean duronly, String at, Integer uid) {
        this.id = id;
        this.guid = guid;
        this.wid = wid;
        this.pid = pid;
        this.billable = billable;
        this.start = start;
        this.stop = stop;
        this.duration = duration;
        this.description = description;
        this.tags = tags;
        this.duronly = duronly;
        this.at = at;
        this.uid = uid;
    }

    /**
     *
     * @return
     * The id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The guid
     */
    public String getGuid() {
        return guid;
    }

    /**
     *
     * @param guid
     * The guid
     */
    public void setGuid(String guid) {
        this.guid = guid;
    }

    /**
     *
     * @return
     * The wid
     */
    public Integer getWid() {
        return wid;
    }

    /**
     *
     * @param wid
     * The wid
     */
    public void setWid(Integer wid) {
        this.wid = wid;
    }

    /**
     *
     * @return
     * The pid
     */
    public Integer getPid() {
        return pid;
    }

    /**
     *
     * @param pid
     * The pid
     */
    public void setPid(Integer pid) {
        this.pid = pid;
    }

    /**
     *
     * @return
     * The billable
     */
    public Boolean getBillable() {
        return billable;
    }

    /**
     *
     * @param billable
     * The billable
     */
    public void setBillable(Boolean billable) {
        this.billable = billable;
    }

    /**
     *
     * @return
     * The start
     */
    public String getStart() {
        return start;
    }

    /**
     *
     * @param start
     * The start
     */
    public void setStart(String start) {
        this.start = start;
    }

    /**
     *
     * @return
     * The stop
     */
    public String getStop() {
        return stop;
    }

    /**
     *
     * @param stop
     * The stop
     */
    public void setStop(String stop) {
        this.stop = stop;
    }

    /**
     *
     * @return
     * The duration
     */
    public Integer getDuration() {
        return duration;
    }

    /**
     *
     * @param duration
     * The duration
     */
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    /**
     *
     * @return
     * The description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     * The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     * The tags
     */
    public List<String> getTags() {
        return tags;
    }

    /**
     *
     * @param tags
     * The tags
     */
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    /**
     *
     * @return
     * The duronly
     */
    public Boolean getDuronly() {
        return duronly;
    }

    /**
     *
     * @param duronly
     * The duronly
     */
    public void setDuronly(Boolean duronly) {
        this.duronly = duronly;
    }

    /**
     *
     * @return
     * The at
     */
    public String getAt() {
        return at;
    }

    /**
     *
     * @param at
     * The at
     */
    public void setAt(String at) {
        this.at = at;
    }

    /**
     *
     * @return
     * The uid
     */
    public Integer getUid() {
        return uid;
    }

    /**
     *
     * @param uid
     * The uid
     */
    public void setUid(Integer uid) {
        this.uid = uid;
    }


    public Project getProjectObj() {
        return projectObj;
    }

    public void setProjectObj(Project projectObj) {
        this.projectObj = projectObj;
    }

    public String getDurationString() {
        return durationString;
    }

    public void setDurationString(String durationString) {
        this.durationString = durationString;
    }

    public String getCreatedWith() {
        return createdWith;
    }

    public void setCreatedWith(String createdWith) {
        this.createdWith = createdWith;
    }
}