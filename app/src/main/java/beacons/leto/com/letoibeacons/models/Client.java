package beacons.leto.com.letoibeacons.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Client implements Parcelable {

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
    public Client() {
    }

    /**
     *
     * @param id
     * @param wid
     * @param name
     * @param at
     */
    public Client(Integer id, Integer wid, String name, String at) {
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


    protected Client(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readInt();
        wid = in.readByte() == 0x00 ? null : in.readInt();
        name = in.readString();
        at = in.readString();
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
        dest.writeString(name);
        dest.writeString(at);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Client> CREATOR = new Parcelable.Creator<Client>() {
        @Override
        public Client createFromParcel(Parcel in) {
            return new Client(in);
        }

        @Override
        public Client[] newArray(int size) {
            return new Client[size];
        }
    };
}