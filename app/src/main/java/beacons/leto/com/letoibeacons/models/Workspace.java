package beacons.leto.com.letoibeacons.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.dsl.Table;

@Table
public class Workspace {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("profile")
    @Expose
    private Integer profile;
    @SerializedName("premium")
    @Expose
    private Boolean premium;
    @SerializedName("admin")
    @Expose
    private Boolean admin;
    @SerializedName("default_hourly_rate")
    @Expose
    private Integer defaultHourlyRate;
    @SerializedName("default_currency")
    @Expose
    private String defaultCurrency;
    @SerializedName("only_admins_may_create_projects")
    @Expose
    private Boolean onlyAdminsMayCreateProjects;
    @SerializedName("only_admins_see_billable_rates")
    @Expose
    private Boolean onlyAdminsSeeBillableRates;
    @SerializedName("only_admins_see_team_dashboard")
    @Expose
    private Boolean onlyAdminsSeeTeamDashboard;
    @SerializedName("projects_billable_by_default")
    @Expose
    private Boolean projectsBillableByDefault;
    @SerializedName("rounding")
    @Expose
    private Integer rounding;
    @SerializedName("rounding_minutes")
    @Expose
    private Integer roundingMinutes;
    @SerializedName("at")
    @Expose
    private String at;
    @SerializedName("logo_url")
    @Expose
    private String logoUrl;
    @SerializedName("ical_url")
    @Expose
    private String icalUrl;
    @SerializedName("ical_enabled")
    @Expose
    private Boolean icalEnabled;
    @SerializedName("subscription")
    @Expose
    private Subscription subscription;
    @SerializedName("campaign")
    @Expose
    private Boolean campaign;
    @SerializedName("business_tester")
    @Expose
    private Boolean businessTester;

    /**
     * No args constructor for use in serialization
     *
     */
    public Workspace() {
    }

    /**
     *
     * @param projectsBillableByDefault
     * @param icalUrl
     * @param roundingMinutes
     * @param premium
     * @param defaultCurrency
     * @param at
     * @param logoUrl
     * @param rounding
     * @param defaultHourlyRate
     * @param id
     * @param onlyAdminsSeeBillableRates
     * @param subscription
     * @param onlyAdminsMayCreateProjects
     * @param admin
     * @param onlyAdminsSeeTeamDashboard
     * @param name
     * @param icalEnabled
     * @param businessTester
     * @param campaign
     * @param profile
     */
    public Workspace(Long id, String name, Integer profile, Boolean premium, Boolean admin, Integer defaultHourlyRate, String defaultCurrency, Boolean onlyAdminsMayCreateProjects, Boolean onlyAdminsSeeBillableRates, Boolean onlyAdminsSeeTeamDashboard, Boolean projectsBillableByDefault, Integer rounding, Integer roundingMinutes, String at, String logoUrl, String icalUrl, Boolean icalEnabled, Subscription subscription, Boolean campaign, Boolean businessTester) {
        this.id = id;
        this.name = name;
        this.profile = profile;
        this.premium = premium;
        this.admin = admin;
        this.defaultHourlyRate = defaultHourlyRate;
        this.defaultCurrency = defaultCurrency;
        this.onlyAdminsMayCreateProjects = onlyAdminsMayCreateProjects;
        this.onlyAdminsSeeBillableRates = onlyAdminsSeeBillableRates;
        this.onlyAdminsSeeTeamDashboard = onlyAdminsSeeTeamDashboard;
        this.projectsBillableByDefault = projectsBillableByDefault;
        this.rounding = rounding;
        this.roundingMinutes = roundingMinutes;
        this.at = at;
        this.logoUrl = logoUrl;
        this.icalUrl = icalUrl;
        this.icalEnabled = icalEnabled;
        this.subscription = subscription;
        this.campaign = campaign;
        this.businessTester = businessTester;
    }

    /**
     *
     * @return
     * The id
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Long id) {
        this.id = id;
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
     * The profile
     */
    public Integer getProfile() {
        return profile;
    }

    /**
     *
     * @param profile
     * The profile
     */
    public void setProfile(Integer profile) {
        this.profile = profile;
    }

    /**
     *
     * @return
     * The premium
     */
    public Boolean getPremium() {
        return premium;
    }

    /**
     *
     * @param premium
     * The premium
     */
    public void setPremium(Boolean premium) {
        this.premium = premium;
    }

    /**
     *
     * @return
     * The admin
     */
    public Boolean getAdmin() {
        return admin;
    }

    /**
     *
     * @param admin
     * The admin
     */
    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    /**
     *
     * @return
     * The defaultHourlyRate
     */
    public Integer getDefaultHourlyRate() {
        return defaultHourlyRate;
    }

    /**
     *
     * @param defaultHourlyRate
     * The default_hourly_rate
     */
    public void setDefaultHourlyRate(Integer defaultHourlyRate) {
        this.defaultHourlyRate = defaultHourlyRate;
    }

    /**
     *
     * @return
     * The defaultCurrency
     */
    public String getDefaultCurrency() {
        return defaultCurrency;
    }

    /**
     *
     * @param defaultCurrency
     * The default_currency
     */
    public void setDefaultCurrency(String defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }

    /**
     *
     * @return
     * The onlyAdminsMayCreateProjects
     */
    public Boolean getOnlyAdminsMayCreateProjects() {
        return onlyAdminsMayCreateProjects;
    }

    /**
     *
     * @param onlyAdminsMayCreateProjects
     * The only_admins_may_create_projects
     */
    public void setOnlyAdminsMayCreateProjects(Boolean onlyAdminsMayCreateProjects) {
        this.onlyAdminsMayCreateProjects = onlyAdminsMayCreateProjects;
    }

    /**
     *
     * @return
     * The onlyAdminsSeeBillableRates
     */
    public Boolean getOnlyAdminsSeeBillableRates() {
        return onlyAdminsSeeBillableRates;
    }

    /**
     *
     * @param onlyAdminsSeeBillableRates
     * The only_admins_see_billable_rates
     */
    public void setOnlyAdminsSeeBillableRates(Boolean onlyAdminsSeeBillableRates) {
        this.onlyAdminsSeeBillableRates = onlyAdminsSeeBillableRates;
    }

    /**
     *
     * @return
     * The onlyAdminsSeeTeamDashboard
     */
    public Boolean getOnlyAdminsSeeTeamDashboard() {
        return onlyAdminsSeeTeamDashboard;
    }

    /**
     *
     * @param onlyAdminsSeeTeamDashboard
     * The only_admins_see_team_dashboard
     */
    public void setOnlyAdminsSeeTeamDashboard(Boolean onlyAdminsSeeTeamDashboard) {
        this.onlyAdminsSeeTeamDashboard = onlyAdminsSeeTeamDashboard;
    }

    /**
     *
     * @return
     * The projectsBillableByDefault
     */
    public Boolean getProjectsBillableByDefault() {
        return projectsBillableByDefault;
    }

    /**
     *
     * @param projectsBillableByDefault
     * The projects_billable_by_default
     */
    public void setProjectsBillableByDefault(Boolean projectsBillableByDefault) {
        this.projectsBillableByDefault = projectsBillableByDefault;
    }

    /**
     *
     * @return
     * The rounding
     */
    public Integer getRounding() {
        return rounding;
    }

    /**
     *
     * @param rounding
     * The rounding
     */
    public void setRounding(Integer rounding) {
        this.rounding = rounding;
    }

    /**
     *
     * @return
     * The roundingMinutes
     */
    public Integer getRoundingMinutes() {
        return roundingMinutes;
    }

    /**
     *
     * @param roundingMinutes
     * The rounding_minutes
     */
    public void setRoundingMinutes(Integer roundingMinutes) {
        this.roundingMinutes = roundingMinutes;
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
     * The logoUrl
     */
    public String getLogoUrl() {
        return logoUrl;
    }

    /**
     *
     * @param logoUrl
     * The logo_url
     */
    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    /**
     *
     * @return
     * The icalUrl
     */
    public String getIcalUrl() {
        return icalUrl;
    }

    /**
     *
     * @param icalUrl
     * The ical_url
     */
    public void setIcalUrl(String icalUrl) {
        this.icalUrl = icalUrl;
    }

    /**
     *
     * @return
     * The icalEnabled
     */
    public Boolean getIcalEnabled() {
        return icalEnabled;
    }

    /**
     *
     * @param icalEnabled
     * The ical_enabled
     */
    public void setIcalEnabled(Boolean icalEnabled) {
        this.icalEnabled = icalEnabled;
    }

    /**
     *
     * @return
     * The subscription
     */
    public Subscription getSubscription() {
        return subscription;
    }

    /**
     *
     * @param subscription
     * The subscription
     */
    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    /**
     *
     * @return
     * The campaign
     */
    public Boolean getCampaign() {
        return campaign;
    }

    /**
     *
     * @param campaign
     * The campaign
     */
    public void setCampaign(Boolean campaign) {
        this.campaign = campaign;
    }

    /**
     *
     * @return
     * The businessTester
     */
    public Boolean getBusinessTester() {
        return businessTester;
    }

    /**
     *
     * @param businessTester
     * The business_tester
     */
    public void setBusinessTester(Boolean businessTester) {
        this.businessTester = businessTester;
    }

}