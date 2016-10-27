package beacons.leto.com.letoibeacons.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import beacons.leto.com.letoibeacons.core.AppPreferences;
import beacons.leto.com.letoibeacons.R;
import beacons.leto.com.letoibeacons.api.LetoTogglRestClient;
import beacons.leto.com.letoibeacons.models.Client;
import beacons.leto.com.letoibeacons.models.Project;
import beacons.leto.com.letoibeacons.models.TimeEntry;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Renzo on 24/02/16.
 */
public class MyUtils {

    private  static String[] colors = new String[]{"#4dc3ff","#bc85e6","#df7baa","#f68d38","#b27636","#8ab734","#14a88e","#268bb5","#6668b4","#a4506c","#67412c","#3c6526","#094558","#bc2d07","#999999","#AEAEAE"};

    public static List<TimeEntry> createCompleteEntriesModel(List<TimeEntry> timeEntries, List<Project> projects, List<Client> clients){
        for (TimeEntry entry : timeEntries){
            entry = completeTimeEntry(entry, projects, clients);
        }
        return timeEntries;
    }

    public static TimeEntry completeTimeEntry(TimeEntry entry, List<Project> projects, List<Client> clients) {
        // Set Project
            for (Project project : projects) {
                if (entry.getPid()!=null && project.getId().intValue() == entry.getPid().intValue()) {
                    entry.setProjectObj(project);
                    break;
                }
            }
            // Set Client
            for (Client client : clients) {
                if (entry.getProjectObj()!=null && client.getId()!=null && entry.getProjectObj().getCid()!=null && client.getId().intValue() == entry.getProjectObj().getCid().intValue()) {
                    entry.getProjectObj().setClientObj(client);
                    break;
                }
            }
            // Set duration string
            entry.setDurationString(splitToComponentTimes(entry.getDuration().longValue()));
        return entry;
    }

    public static String splitToComponentTimes(long duration) {
        if (duration<0){
            long seconds = System.currentTimeMillis() / 1000l;
            duration = seconds + duration;
        }
        int hours = (int)duration / 3600;
        int remainder = (int) duration - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;

        return String.format("%d:%02d:%02d", hours,mins,secs);
    }

    public static void setupDurationTV (TextView textView){
        int boldCount = 4;
        if (textView.getText().length()>7) {
            boldCount=5;
        }
        final SpannableStringBuilder sb = new SpannableStringBuilder(textView.getText());
        final ForegroundColorSpan fcs = new ForegroundColorSpan(textView.getCurrentTextColor());
        final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
        sb.setSpan(fcs, 0, boldCount, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan(bss, 0, boldCount, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        textView.setText(sb);
    }

    public static String getColorFromIndex(int index){
        return colors[index];
    }

    /**
     * Return an ISO 8601 combined date and time string for specified date/time
     *
     * @param date
     *            Date
     * @return String with format "yyyy-MM-dd'T'HH:mm:ss'Z'"
     */
    public static String getISO8601StringForDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(date);
    }

    public static Date getDateFromISO8601String(String dateString) {
        String dateToConvert = dateString.substring(0, 10);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String dateStringFromISOString(String string) {
        Date date = getDateFromISO8601String(string);
        DateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(date);
    }

    public static void applyRoundedCorners (View view){
        GradientDrawable shape =  new GradientDrawable();
        shape.setCornerRadius( 8 );

        shape.setColor(((ColorDrawable) view.getBackground()).getColor());

        // now find your view and add background to it
        view.setBackground(shape);
    }

    public static void getProjectsWithClientsName(final Callback<List<Project>> callback){
        LetoTogglRestClient.getPureApi().getProjects(AppPreferences.getWorkspaceId(), new Callback<List<Project>>() {
            @Override
            public void success(final List<Project> projects, Response response) {
                LetoTogglRestClient.getPureApi().getClients(AppPreferences.getWorkspaceId(), new Callback<List<Client>>() {
                    @Override
                    public void success(List<Client> clients, Response response) {
                        List<Project> projectsComplete = new ArrayList<Project>();
                        for (Client client : clients){
                            for (Project proj : projects){
                                if (proj.getCid().intValue()==client.getId().intValue()){
                                    proj.setClientObj(client);
                                    projectsComplete.add(proj);
                                }
                            }
                        }
                        callback.success(projectsComplete, response);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        callback.failure(error);
                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });
    }

    public static ProgressDialog getGeneralProgressDialog(Context context){
        ProgressDialog mProgresDialog = new ProgressDialog(context);
        mProgresDialog.setMessage(context.getString(R.string.please_wait));
        mProgresDialog.setCancelable(false);
        return  mProgresDialog;
    }
}
