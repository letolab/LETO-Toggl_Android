package beacons.leto.com.letoibeacons.api;

import java.util.List;

import beacons.leto.com.letoibeacons.models.Client;
import beacons.leto.com.letoibeacons.models.Project;
import beacons.leto.com.letoibeacons.models.StartTimeEntryModel;
import beacons.leto.com.letoibeacons.models.TimeEntry;
import beacons.leto.com.letoibeacons.models.TimeEntryModel;
import beacons.leto.com.letoibeacons.models.TogglUser;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

public interface LetoTogglApi {
    @GET("/me")
    void signin(Callback<TogglUser> callback);

    @GET("/me?with_related_data=true")
    void getUserInfo(Callback<TogglUser> callback);

    @GET("/time_entries/current")
    void getCurrentEntry(Callback<TimeEntryModel> callback);

    @PUT("/time_entries/{task_id}/stop")
    void stopTask(@Path("task_id") int taskId, Callback<TimeEntryModel> callback);

    @POST("/time_entries")
    void startEntry(@Body StartTimeEntryModel entryModel, Callback<TimeEntryModel> callback);

    @GET("/time_entries")
    void getTimeEntries(@Query("start_date") String startDate, @Query("end_date") String endDate, Callback<List<TimeEntry>> callback);

    @GET("/workspaces/{wid}/projects")
    void getProjects(@Path("wid") int wid, Callback<List<Project>> callback);

    @GET("/workspaces/{wid}/clients")
    void getClients(@Path("wid") int wid, Callback<List<Client>> callback);
}