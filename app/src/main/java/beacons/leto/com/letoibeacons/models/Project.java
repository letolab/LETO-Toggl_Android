package beacons.leto.com.letoibeacons.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Project implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("wid")
    @Expose
    private Integer wid;
    @SerializedName("cid")
    @Expose
    private Integer cid = -1;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("billable")
    @Expose
    private Boolean billable;
    @SerializedName("is_private")
    @Expose
    private Boolean isPrivate;
    @SerializedName("active")
    @Expose
    private Boolean active;
    @SerializedName("template")
    @Expose
    private Boolean template;
    @SerializedName("at")
    @Expose
    private String at;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("auto_estimates")
    @Expose
    private Boolean autoEstimates;
    @SerializedName("actual_hours")
    @Expose
    private Integer actualHours;

    private Client clientObj;
    /**
     * No args constructor for use in serialization
     *
     */
    public Project() {
    }

    public Project(String name, String color) {
        this.name = name;
        this.color = color;
    }

    /**
     *
     * @param template
     * @param id
     * @param color
     * @param isPrivate
     * @param wid
     * @param createdAt
     * @param name
     * @param autoEstimates
     * @param at
     * @param active
     * @param actualHours
     * @param cid
     * @param billable
     */
    public Project(Integer id, Integer wid, Integer cid, String name, Boolean billable, Boolean isPrivate, Boolean active, Boolean template, String at, String createdAt, String color, Boolean autoEstimates, Integer actualHours) {
        this.id = id;
        this.wid = wid;
        this.cid = cid;
        this.name = name;
        this.billable = billable;
        this.isPrivate = isPrivate;
        this.active = active;
        this.template = template;
        this.at = at;
        this.createdAt = createdAt;
        this.color = color;
        this.autoEstimates = autoEstimates;
        this.actualHours = actualHours;
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
     * The cid
     */
    public Integer getCid() {
        return cid;
    }

    /**
     *
     * @param cid
     * The cid
     */
    public void setCid(Integer cid) {
        this.cid = cid;
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
     * The isPrivate
     */
    public Boolean getIsPrivate() {
        return isPrivate;
    }

    /**
     *
     * @param isPrivate
     * The is_private
     */
    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    /**
     *
     * @return
     * The active
     */
    public Boolean getActive() {
        return active;
    }

    /**
     *
     * @param active
     * The active
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
     *
     * @return
     * The template
     */
    public Boolean getTemplate() {
        return template;
    }

    /**
     *
     * @param template
     * The template
     */
    public void setTemplate(Boolean template) {
        this.template = template;
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
     * The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     *
     * @param createdAt
     * The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     *
     * @return
     * The color
     */
    public String getColor() {
        return color;
    }

    /**
     *
     * @param color
     * The color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     *
     * @return
     * The autoEstimates
     */
    public Boolean getAutoEstimates() {
        return autoEstimates;
    }

    /**
     *
     * @param autoEstimates
     * The auto_estimates
     */
    public void setAutoEstimates(Boolean autoEstimates) {
        this.autoEstimates = autoEstimates;
    }

    /**
     *
     * @return
     * The actualHours
     */
    public Integer getActualHours() {
        return actualHours;
    }

    /**
     *
     * @param actualHours
     * The actual_hours
     */
    public void setActualHours(Integer actualHours) {
        this.actualHours = actualHours;
    }

    public Client getClientObj() {
        return clientObj;
    }

    public void setClientObj(Client clientObj) {
        this.clientObj = clientObj;
    }

    protected Project(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readInt();
        wid = in.readByte() == 0x00 ? null : in.readInt();
        cid = in.readByte() == 0x00 ? null : in.readInt();
        name = in.readString();
        byte billableVal = in.readByte();
        billable = billableVal == 0x02 ? null : billableVal != 0x00;
        byte isPrivateVal = in.readByte();
        isPrivate = isPrivateVal == 0x02 ? null : isPrivateVal != 0x00;
        byte activeVal = in.readByte();
        active = activeVal == 0x02 ? null : activeVal != 0x00;
        byte templateVal = in.readByte();
        template = templateVal == 0x02 ? null : templateVal != 0x00;
        at = in.readString();
        createdAt = in.readString();
        color = in.readString();
        byte autoEstimatesVal = in.readByte();
        autoEstimates = autoEstimatesVal == 0x02 ? null : autoEstimatesVal != 0x00;
        actualHours = in.readByte() == 0x00 ? null : in.readInt();
        clientObj = (Client) in.readValue(Client.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(id);
        }
        if (wid == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(wid);
        }
        if (cid == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(cid);
        }
        dest.writeString(name);
        if (billable == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (billable ? 0x01 : 0x00));
        }
        if (isPrivate == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (isPrivate ? 0x01 : 0x00));
        }
        if (active == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (active ? 0x01 : 0x00));
        }
        if (template == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (template ? 0x01 : 0x00));
        }
        dest.writeString(at);
        dest.writeString(createdAt);
        dest.writeString(color);
        if (autoEstimates == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (autoEstimates ? 0x01 : 0x00));
        }
        if (actualHours == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(actualHours);
        }
        dest.writeValue(clientObj);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Project> CREATOR = new Parcelable.Creator<Project>() {
        @Override
        public Project createFromParcel(Parcel in) {
            return new Project(in);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }
    };
}