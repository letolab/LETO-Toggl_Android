package beacons.leto.com.letoibeacons.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

public class Subscription extends SugarRecord {

    @SerializedName("workspace_id")
    @Expose
    private Integer workspaceId;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private Object updatedAt;
    @SerializedName("vat_valid")
    @Expose
    private Boolean vatValid;
    @SerializedName("vat_validated_at")
    @Expose
    private Object vatValidatedAt;
    @SerializedName("vat_invalid_accepted_at")
    @Expose
    private Object vatInvalidAcceptedAt;
    @SerializedName("vat_invalid_accepted_by")
    @Expose
    private Object vatInvalidAcceptedBy;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("vat_applicable")
    @Expose
    private Boolean vatApplicable;

    /**
     * No args constructor for use in serialization
     *
     */
    public Subscription() {
    }

    /**
     *
     * @param updatedAt
     * @param workspaceId
     * @param vatInvalidAcceptedBy
     * @param vatValidatedAt
     * @param description
     * @param createdAt
     * @param vatInvalidAcceptedAt
     * @param deletedAt
     * @param vatApplicable
     * @param vatValid
     */
    public Subscription(Integer workspaceId, Object deletedAt, String createdAt, Object updatedAt, Boolean vatValid, Object vatValidatedAt, Object vatInvalidAcceptedAt, Object vatInvalidAcceptedBy, String description, Boolean vatApplicable) {
        this.workspaceId = workspaceId;
        this.deletedAt = deletedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.vatValid = vatValid;
        this.vatValidatedAt = vatValidatedAt;
        this.vatInvalidAcceptedAt = vatInvalidAcceptedAt;
        this.vatInvalidAcceptedBy = vatInvalidAcceptedBy;
        this.description = description;
        this.vatApplicable = vatApplicable;
    }

    /**
     *
     * @return
     * The workspaceId
     */
    public Integer getWorkspaceId() {
        return workspaceId;
    }

    /**
     *
     * @param workspaceId
     * The workspace_id
     */
    public void setWorkspaceId(Integer workspaceId) {
        this.workspaceId = workspaceId;
    }

    /**
     *
     * @return
     * The deletedAt
     */
    public Object getDeletedAt() {
        return deletedAt;
    }

    /**
     *
     * @param deletedAt
     * The deleted_at
     */
    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
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
     * The updatedAt
     */
    public Object getUpdatedAt() {
        return updatedAt;
    }

    /**
     *
     * @param updatedAt
     * The updated_at
     */
    public void setUpdatedAt(Object updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     *
     * @return
     * The vatValid
     */
    public Boolean getVatValid() {
        return vatValid;
    }

    /**
     *
     * @param vatValid
     * The vat_valid
     */
    public void setVatValid(Boolean vatValid) {
        this.vatValid = vatValid;
    }

    /**
     *
     * @return
     * The vatValidatedAt
     */
    public Object getVatValidatedAt() {
        return vatValidatedAt;
    }

    /**
     *
     * @param vatValidatedAt
     * The vat_validated_at
     */
    public void setVatValidatedAt(Object vatValidatedAt) {
        this.vatValidatedAt = vatValidatedAt;
    }

    /**
     *
     * @return
     * The vatInvalidAcceptedAt
     */
    public Object getVatInvalidAcceptedAt() {
        return vatInvalidAcceptedAt;
    }

    /**
     *
     * @param vatInvalidAcceptedAt
     * The vat_invalid_accepted_at
     */
    public void setVatInvalidAcceptedAt(Object vatInvalidAcceptedAt) {
        this.vatInvalidAcceptedAt = vatInvalidAcceptedAt;
    }

    /**
     *
     * @return
     * The vatInvalidAcceptedBy
     */
    public Object getVatInvalidAcceptedBy() {
        return vatInvalidAcceptedBy;
    }

    /**
     *
     * @param vatInvalidAcceptedBy
     * The vat_invalid_accepted_by
     */
    public void setVatInvalidAcceptedBy(Object vatInvalidAcceptedBy) {
        this.vatInvalidAcceptedBy = vatInvalidAcceptedBy;
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
     * The vatApplicable
     */
    public Boolean getVatApplicable() {
        return vatApplicable;
    }

    /**
     *
     * @param vatApplicable
     * The vat_applicable
     */
    public void setVatApplicable(Boolean vatApplicable) {
        this.vatApplicable = vatApplicable;
    }

}