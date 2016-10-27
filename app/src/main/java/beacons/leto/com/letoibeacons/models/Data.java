package beacons.leto.com.letoibeacons.models;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.dsl.Table;

@Table
public class Data {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("api_token")
    @Expose
    private String apiToken;
    @SerializedName("default_wid")
    @Expose
    private Integer defaultWid;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("fullname")
    @Expose
    private String fullname;
    @SerializedName("jquery_timeofday_format")
    @Expose
    private String jqueryTimeofdayFormat;
    @SerializedName("jquery_date_format")
    @Expose
    private String jqueryDateFormat;
    @SerializedName("timeofday_format")
    @Expose
    private String timeofdayFormat;
    @SerializedName("date_format")
    @Expose
    private String dateFormat;
    @SerializedName("store_start_and_stop_time")
    @Expose
    private Boolean storeStartAndStopTime;
    @SerializedName("beginning_of_week")
    @Expose
    private Integer beginningOfWeek;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("sidebar_piechart")
    @Expose
    private Boolean sidebarPiechart;
    @SerializedName("at")
    @Expose
    private String at;
    @SerializedName("retention")
    @Expose
    private Integer retention;
    @SerializedName("record_timeline")
    @Expose
    private Boolean recordTimeline;
    @SerializedName("render_timeline")
    @Expose
    private Boolean renderTimeline;
    @SerializedName("timeline_enabled")
    @Expose
    private Boolean timelineEnabled;
    @SerializedName("timeline_experiment")
    @Expose
    private Boolean timelineExperiment;
    @SerializedName("new_blog_post")
    @Expose
    private NewBlogPost newBlogPost;
    @SerializedName("time_entries")
    @Expose
    private List<TimeEntry> timeEntries = new ArrayList<TimeEntry>();
    @SerializedName("projects")
    @Expose
    private List<Project> projects = new ArrayList<Project>();
    @SerializedName("tags")
    @Expose
    private List<Tag> tags = new ArrayList<Tag>();
    @SerializedName("workspaces")
    @Expose
    private List<Workspace> workspaces = new ArrayList<Workspace>();
    @SerializedName("clients")
    @Expose
    private List<Client> clients = new ArrayList<Client>();

    /**
     * No args constructor for use in serialization
     *
     */
    public Data() {
    }

    /**
     *
     * @param jqueryDateFormat
     * @param imageUrl
     * @param storeStartAndStopTime
     * @param id
     * @param apiToken
     * @param retention
     * @param timelineExperiment
     * @param dateFormat
     * @param sidebarPiechart
     * @param recordTimeline
     * @param fullname
     * @param tags
     * @param jqueryTimeofdayFormat
     * @param timeEntries
     * @param newBlogPost
     * @param beginningOfWeek
     * @param at
     * @param timelineEnabled
     * @param timeofdayFormat
     * @param projects
     * @param email
     * @param workspaces
     * @param renderTimeline
     * @param language
     * @param defaultWid
     * @param clients
     */
    public Data(Long id, String apiToken, Integer defaultWid, String email, String fullname, String jqueryTimeofdayFormat, String jqueryDateFormat, String timeofdayFormat, String dateFormat, Boolean storeStartAndStopTime, Integer beginningOfWeek, String language, String imageUrl, Boolean sidebarPiechart, String at, Integer retention, Boolean recordTimeline, Boolean renderTimeline, Boolean timelineEnabled, Boolean timelineExperiment, NewBlogPost newBlogPost, List<TimeEntry> timeEntries, List<Project> projects, List<Tag> tags, List<Workspace> workspaces, List<Client> clients) {
        this.id = id;
        this.apiToken = apiToken;
        this.defaultWid = defaultWid;
        this.email = email;
        this.fullname = fullname;
        this.jqueryTimeofdayFormat = jqueryTimeofdayFormat;
        this.jqueryDateFormat = jqueryDateFormat;
        this.timeofdayFormat = timeofdayFormat;
        this.dateFormat = dateFormat;
        this.storeStartAndStopTime = storeStartAndStopTime;
        this.beginningOfWeek = beginningOfWeek;
        this.language = language;
        this.imageUrl = imageUrl;
        this.sidebarPiechart = sidebarPiechart;
        this.at = at;
        this.retention = retention;
        this.recordTimeline = recordTimeline;
        this.renderTimeline = renderTimeline;
        this.timelineEnabled = timelineEnabled;
        this.timelineExperiment = timelineExperiment;
        this.newBlogPost = newBlogPost;
        this.timeEntries = timeEntries;
        this.projects = projects;
        this.tags = tags;
        this.workspaces = workspaces;
        this.clients = clients;
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
     * The apiToken
     */
    public String getApiToken() {
        return apiToken;
    }

    /**
     *
     * @param apiToken
     * The api_token
     */
    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    /**
     *
     * @return
     * The defaultWid
     */
    public Integer getDefaultWid() {
        return defaultWid;
    }

    /**
     *
     * @param defaultWid
     * The default_wid
     */
    public void setDefaultWid(Integer defaultWid) {
        this.defaultWid = defaultWid;
    }

    /**
     *
     * @return
     * The email
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     * The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return
     * The fullname
     */
    public String getFullname() {
        return fullname;
    }

    /**
     *
     * @param fullname
     * The fullname
     */
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    /**
     *
     * @return
     * The jqueryTimeofdayFormat
     */
    public String getJqueryTimeofdayFormat() {
        return jqueryTimeofdayFormat;
    }

    /**
     *
     * @param jqueryTimeofdayFormat
     * The jquery_timeofday_format
     */
    public void setJqueryTimeofdayFormat(String jqueryTimeofdayFormat) {
        this.jqueryTimeofdayFormat = jqueryTimeofdayFormat;
    }

    /**
     *
     * @return
     * The jqueryDateFormat
     */
    public String getJqueryDateFormat() {
        return jqueryDateFormat;
    }

    /**
     *
     * @param jqueryDateFormat
     * The jquery_date_format
     */
    public void setJqueryDateFormat(String jqueryDateFormat) {
        this.jqueryDateFormat = jqueryDateFormat;
    }

    /**
     *
     * @return
     * The timeofdayFormat
     */
    public String getTimeofdayFormat() {
        return timeofdayFormat;
    }

    /**
     *
     * @param timeofdayFormat
     * The timeofday_format
     */
    public void setTimeofdayFormat(String timeofdayFormat) {
        this.timeofdayFormat = timeofdayFormat;
    }

    /**
     *
     * @return
     * The dateFormat
     */
    public String getDateFormat() {
        return dateFormat;
    }

    /**
     *
     * @param dateFormat
     * The date_format
     */
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    /**
     *
     * @return
     * The storeStartAndStopTime
     */
    public Boolean getStoreStartAndStopTime() {
        return storeStartAndStopTime;
    }

    /**
     *
     * @param storeStartAndStopTime
     * The store_start_and_stop_time
     */
    public void setStoreStartAndStopTime(Boolean storeStartAndStopTime) {
        this.storeStartAndStopTime = storeStartAndStopTime;
    }

    /**
     *
     * @return
     * The beginningOfWeek
     */
    public Integer getBeginningOfWeek() {
        return beginningOfWeek;
    }

    /**
     *
     * @param beginningOfWeek
     * The beginning_of_week
     */
    public void setBeginningOfWeek(Integer beginningOfWeek) {
        this.beginningOfWeek = beginningOfWeek;
    }

    /**
     *
     * @return
     * The language
     */
    public String getLanguage() {
        return language;
    }

    /**
     *
     * @param language
     * The language
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     *
     * @return
     * The imageUrl
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     *
     * @param imageUrl
     * The image_url
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     *
     * @return
     * The sidebarPiechart
     */
    public Boolean getSidebarPiechart() {
        return sidebarPiechart;
    }

    /**
     *
     * @param sidebarPiechart
     * The sidebar_piechart
     */
    public void setSidebarPiechart(Boolean sidebarPiechart) {
        this.sidebarPiechart = sidebarPiechart;
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
     * The retention
     */
    public Integer getRetention() {
        return retention;
    }

    /**
     *
     * @param retention
     * The retention
     */
    public void setRetention(Integer retention) {
        this.retention = retention;
    }

    /**
     *
     * @return
     * The recordTimeline
     */
    public Boolean getRecordTimeline() {
        return recordTimeline;
    }

    /**
     *
     * @param recordTimeline
     * The record_timeline
     */
    public void setRecordTimeline(Boolean recordTimeline) {
        this.recordTimeline = recordTimeline;
    }

    /**
     *
     * @return
     * The renderTimeline
     */
    public Boolean getRenderTimeline() {
        return renderTimeline;
    }

    /**
     *
     * @param renderTimeline
     * The render_timeline
     */
    public void setRenderTimeline(Boolean renderTimeline) {
        this.renderTimeline = renderTimeline;
    }

    /**
     *
     * @return
     * The timelineEnabled
     */
    public Boolean getTimelineEnabled() {
        return timelineEnabled;
    }

    /**
     *
     * @param timelineEnabled
     * The timeline_enabled
     */
    public void setTimelineEnabled(Boolean timelineEnabled) {
        this.timelineEnabled = timelineEnabled;
    }

    /**
     *
     * @return
     * The timelineExperiment
     */
    public Boolean getTimelineExperiment() {
        return timelineExperiment;
    }

    /**
     *
     * @param timelineExperiment
     * The timeline_experiment
     */
    public void setTimelineExperiment(Boolean timelineExperiment) {
        this.timelineExperiment = timelineExperiment;
    }

    /**
     *
     * @return
     * The newBlogPost
     */
    public NewBlogPost getNewBlogPost() {
        return newBlogPost;
    }

    /**
     *
     * @param newBlogPost
     * The new_blog_post
     */
    public void setNewBlogPost(NewBlogPost newBlogPost) {
        this.newBlogPost = newBlogPost;
    }

    /**
     *
     * @return
     * The timeEntries
     */
    public List<TimeEntry> getTimeEntries() {
        return timeEntries;
    }

    /**
     *
     * @param timeEntries
     * The time_entries
     */
    public void setTimeEntries(List<TimeEntry> timeEntries) {
        this.timeEntries = timeEntries;
    }

    /**
     *
     * @return
     * The projects
     */
    public List<Project> getProjects() {
        return projects;
    }

    /**
     *
     * @param projects
     * The projects
     */
    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    /**
     *
     * @return
     * The tags
     */
    public List<Tag> getTags() {
        return tags;
    }

    /**
     *
     * @param tags
     * The tags
     */
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    /**
     *
     * @return
     * The workspaces
     */
    public List<Workspace> getWorkspaces() {
        return workspaces;
    }

    /**
     *
     * @param workspaces
     * The workspaces
     */
    public void setWorkspaces(List<Workspace> workspaces) {
        this.workspaces = workspaces;
    }

    /**
     *
     * @return
     * The clients
     */
    public List<Client> getClients() {
        return clients;
    }

    /**
     *
     * @param clients
     * The clients
     */
    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

}
