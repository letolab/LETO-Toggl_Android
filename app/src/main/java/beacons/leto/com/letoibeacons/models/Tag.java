package beacons.leto.com.letoibeacons.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tag {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("wid")
    @Expose
    private Integer wid;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("at")
    @Expose
    private String at;

    /**
     * No args constructor for use in serialization
     *
     */
    public Tag() {
    }

    /**
     *
     * @param id
     * @param wid
     * @param name
     * @param at
     */
    public Tag(Integer id, Integer wid, String name, String at) {
        this.id = id;
        this.wid = wid;
        this.name = name;
        this.at = at;
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
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
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

}