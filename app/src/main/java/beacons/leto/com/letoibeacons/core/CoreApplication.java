package beacons.leto.com.letoibeacons.core;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.orm.SugarApp;

import beacons.leto.com.letoibeacons.BuildConfig;
import beacons.leto.com.letoibeacons.R;
import io.fabric.sdk.android.Fabric;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import beacons.leto.com.letoibeacons.activities.MainActivity;
import beacons.leto.com.letoibeacons.api.LetoTogglRestClient;
import beacons.leto.com.letoibeacons.models.StartTimeEntryModel;
import beacons.leto.com.letoibeacons.models.TimeEntry;
import beacons.leto.com.letoibeacons.models.TimeEntryModel;
import beacons.leto.com.letoibeacons.models.TogglUser;
import beacons.leto.com.letoibeacons.utils.MyUtils;
import beacons.leto.com.letoibeacons.utils.NotificationActionHandler;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Renzo on 23/02/16.
 */
public class CoreApplication extends SugarApp implements BootstrapNotifier {

    private static final String TAG = "iBEACONS APP";
    private RegionBootstrap regionBootstrap;
    private static final String REGION_ID = "2F234454-CF6D-4A0F-ADF2-F4911BA9FFA6";
    private static final String REGION_NAME = "LETO_region";
    Region region;
    BeaconManager beaconManager;

    public MainActivity mainActivityInstance;
    Boolean tempExit = false;

    boolean showNotification = true;

    @Override
    public void onCreate() {
        super.onCreate();
        if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }
        Log.d(TAG, "App started up");
        AppPreferences.initPreferences(getApplicationContext());
        LetoTogglRestClient.prepare(this);
        setupBeaconManager();
    }



    public void setupBeaconManager(){
        if (beaconManager==null && AppPreferences.getBeaconDetectionState()) {
            AppPreferences.setBeaconDetected(false);
            Log.d(TAG, "Init beacon");
            beaconManager = BeaconManager.getInstanceForApplication(this);
            // To detect proprietary beacons, you must add a line like below corresponding to your beacon
            // type.  Do a web search for "setBeaconLayout" to get the proper expression.
            beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
            beaconManager.setBackgroundScanPeriod(2000l);
            beaconManager.setBackgroundBetweenScanPeriod(5000l); // (300000l) every 5 minutes
            // wake up the app when any beacon is seen (you can specify specific id filers in the parameters below)
            region = new Region(REGION_NAME, Identifier.parse(AppPreferences.getBeaconUuid()), null, null);
            regionBootstrap = new RegionBootstrap(this, region);
        }
    }

    public void stopBeaconManager(){
        if (beaconManager!=null) {
            try {
                beaconManager.stopMonitoringBeaconsInRegion(region);
                beaconManager = null;
                regionBootstrap.disable();
                regionBootstrap = null;
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void didDetermineStateForRegion(int state, Region region) {
        Log.d(TAG, "Got a didDetermineStateForRegion call: STATE" + String.valueOf(state));
    }

    @Override
    public void didEnterRegion (Region regionBeac){
        if (regionBeac==null){
            showNotification=false;
        }else{
            showNotification=true;
        }

        Log.d(TAG, "Got a didEnterRegion call");
        tempExit=false;
        if (!AppPreferences.getBeaconDetected() || regionBeac==null) {
            if (regionBeac!=null){
                AppPreferences.setBeaconDetected(true);
            }
            getAndStartNewTask();
        }

        //UNCOMMENT IF I WANT TO RUN THE APP
//        Intent intent = new Intent(this, MainActivity.class);
//        // IMPORTANT: in the AndroidManifest.xml definition of this activity, you must set android:launchMode="singleInstance" or you will get two instances
//        // created when a user launches the activity manually and it gets launched from here.
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        this.startActivity(intent);
    }

    @Override
    public void didExitRegion(final Region regionBeac) {
        Log.d(TAG, "Got a didExitRegion call");
        tempExit=true;
        int delay;
        if (regionBeac==null){
            delay=0;
        }else{
            delay=35000;
        }
        new Timer().schedule(new TimerTask(){
            @Override
            public void run (){
                //wait 10 seconds before stop the task (this is to assure that the beacon is definitely out of the range)
                if (tempExit) {
                    if (regionBeac==null){
                        showNotification=false;
                    }else{
                        showNotification=true;
                        AppPreferences.setBeaconDetected(false);
                    }
                    Log.d(TAG, "Stopping task");
                    getAndStopRunningTask();
                }
            }
        }, delay);
    }

    public void getAndStopRunningTask(){
        LetoTogglRestClient.getPureApi().getCurrentEntry(new Callback<TimeEntryModel>() {
            @Override
            public void success(TimeEntryModel timeEntryModel, Response response) {
                if (timeEntryModel.getData() != null) {
                    stopRunningTask(timeEntryModel.getData().getId());
                }else{
                    if (mainActivityInstance!=null){
                        mainActivityInstance.getData();
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
//                sendNotificationWithMessage(error.toString());
                Log.d("STOP TASK", "Something went wrong getting the task to stop it. Trying Again in 20 ---------------> " + error.getKind());
                if (error.getKind() == RetrofitError.Kind.NETWORK) {
                    Log.d("STOP TASK", "IT was a network error ---------------> " + error.getKind());
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Log.d("STOP TASK", " Trying Again to get the task to stop ....");
                            getAndStopRunningTask();
                        }
                    }, 20000);
                }
            }
        });
    }

    private void stopRunningTask(final int taskId){
        LetoTogglRestClient.getPureApi().stopTask(taskId, new Callback<TimeEntryModel>() {
            @Override
            public void success(TimeEntryModel timeEntryModel, Response response) {
                sendNotification(false);
            }

            @Override
            public void failure(RetrofitError error) {
//                sendNotificationWithMessage(error.toString());
                Log.d("STOP TASK", "Something went wrong stopping the task. will try again in 10 seconds");
                if (error.getKind() == RetrofitError.Kind.NETWORK) {
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Log.d("STOP TASK", " Trying Again to stop task....");
                            stopRunningTask(taskId);
                            ;
                        }
                    }, 10000);
                }
            }
        });
    }

    public void getAndStartNewTask(){

        LetoTogglRestClient.getPureApi().getUserInfo(new Callback<TogglUser>() {
            @Override
            public void success(TogglUser togglUser, Response response) {
                TimeEntry lastEntry = togglUser.getData().getTimeEntries().get(togglUser.getData().getTimeEntries().size() - 1);
                if (lastEntry.getStop() != null) {
                    startNewTask(lastEntry);
                }else{
                    if (mainActivityInstance!=null){
                        mainActivityInstance.getData();
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "ERROR: User Info");
            }
        });

    }

    private void startNewTask(TimeEntry entry){
        TimeEntry entryToSend = new TimeEntry();
        if (entry!=null) {
            entryToSend.setDescription(entry.getDescription());
            entryToSend.setTags(entry.getTags());
            entryToSend.setPid(entry.getPid());
            entryToSend.setBillable(entry.getBillable());
        }else{
            entryToSend.setDescription("-");
            entryToSend.setBillable(false);
        }
        entryToSend.setCreatedWith("LETO Toggl Android");
        entryToSend.setStart(MyUtils.getISO8601StringForDate(new Date()));
        entryToSend.setDuration((int) (-System.currentTimeMillis() / 1000l));
        StartTimeEntryModel timeEntryModel = new StartTimeEntryModel(entryToSend);
        LetoTogglRestClient.getPureApi().startEntry(timeEntryModel, new Callback<TimeEntryModel>() {
            @Override
            public void success(TimeEntryModel timeEntryModel, Response response) {
                sendNotification(true);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }



    private void sendNotification(boolean isStarted) {
        if (showNotification) {
            long[] vibrate = {0, 100, 200, 300};
            NotificationCompat.Builder builder;
            int notifID = 0;

            if (isStarted) {
                // TASK STARTED NOTIF

                //REMIND ACTION
                Intent remindReceive = new Intent(this, NotificationActionHandler.class);
                remindReceive.setAction(NotificationActionHandler.REMIND_ACTION);
                PendingIntent pendingIntentRemind = PendingIntent.getBroadcast(this, 12346, remindReceive, PendingIntent.FLAG_UPDATE_CURRENT);

                //STOP ACTION
                Intent stopReceive = new Intent(this, NotificationActionHandler.class);
                stopReceive.setAction(NotificationActionHandler.STOP_ACTION);
                PendingIntent pendingIntentStop = PendingIntent.getBroadcast(this, 12347, stopReceive, PendingIntent.FLAG_UPDATE_CURRENT);

                builder = new NotificationCompat.Builder(this)
                        .setContentTitle("LETO Toggl")
                        .setContentText("Toggl tracker started")
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setVibrate(vibrate)
                        .setColor(ContextCompat.getColor(getApplicationContext(), R.color.flat_green))
                        .setCategory(Notification.CATEGORY_MESSAGE)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setShowWhen(true)
                        .setAutoCancel(false)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setVisibility(Notification.VISIBILITY_PUBLIC)
                        .setSmallIcon(R.drawable.ic_play_arrow)
                        .addAction(R.drawable.ic_action_alarm, getString(R.string.notif_stop_remind), pendingIntentRemind)
                        .addAction(R.drawable.ic_stop, getString(R.string.notif_stop_action), pendingIntentStop);

                notifID = NotificationActionHandler.START_ID;
            } else {
                // TASK STOPPED NOTIF

                //START ACTION
                Intent startReceive = new Intent(this, NotificationActionHandler.class);
                startReceive.setAction(NotificationActionHandler.START_ACTION);
                PendingIntent pendingIntentStart = PendingIntent.getBroadcast(this, 12345, startReceive, PendingIntent.FLAG_UPDATE_CURRENT);

                builder = new NotificationCompat.Builder(this)
                        .setContentTitle("LETO Toggl")
                        .setContentText("Toggl Tracker stopped")
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setVibrate(vibrate)
                        .setColor(ContextCompat.getColor(getApplicationContext(), R.color.flat_red))
                        .setCategory(Notification.CATEGORY_MESSAGE)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setShowWhen(true)
                        .setAutoCancel(false)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setVisibility(Notification.VISIBILITY_PUBLIC)
                        .setSmallIcon(R.drawable.ic_stop)
                        .addAction(R.drawable.ic_play_arrow, getString(R.string.notif_start_action), pendingIntentStart);

                notifID = NotificationActionHandler.STOP_ID;
            }

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addNextIntent(new Intent(this, MainActivity.class));
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            builder.setContentIntent(resultPendingIntent);
            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(notifID, builder.build());
            if (notifID==NotificationActionHandler.STOP_ID) {
                notificationManager.cancel(NotificationActionHandler.START_ID);
            }else{
                notificationManager.cancel(NotificationActionHandler.STOP_ID);
            }
        }

        if (mainActivityInstance!=null){
            mainActivityInstance.getData();
        }
    }

private void sendNotificationWithMessage(String message){

    long[] vibrate = {0};
    NotificationCompat.Builder builder;
    int notifID = 0;

    // TASK STARTED NOTIF

    //REMIND ACTION
    Intent remindReceive = new Intent(this, NotificationActionHandler.class);
    remindReceive.setAction(NotificationActionHandler.REMIND_ACTION);
    PendingIntent pendingIntentRemind = PendingIntent.getBroadcast(this, 12346, remindReceive, PendingIntent.FLAG_UPDATE_CURRENT);

    //STOP ACTION
    Intent stopReceive = new Intent(this, NotificationActionHandler.class);
    stopReceive.setAction(NotificationActionHandler.STOP_ACTION);
    PendingIntent pendingIntentStop = PendingIntent.getBroadcast(this, 12347, stopReceive, PendingIntent.FLAG_UPDATE_CURRENT);

    builder = new NotificationCompat.Builder(this)
            .setDefaults(Notification.DEFAULT_SOUND)
            .setVibrate(null)
            .setContentTitle("LETO Toggl")
            .setContentText(message)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setColor(ContextCompat.getColor(getApplicationContext(), R.color.flat_red))
            .setCategory(Notification.CATEGORY_MESSAGE)
            .setPriority(Notification.PRIORITY_HIGH)
            .setShowWhen(true)
            .setAutoCancel(false)
            .setDefaults(Notification.DEFAULT_ALL)
            .setVisibility(Notification.VISIBILITY_PUBLIC)
            .setSmallIcon(R.drawable.ic_play_arrow)
            .addAction(R.drawable.ic_action_alarm, getString(R.string.notif_stop_remind), pendingIntentRemind)
            .addAction(R.drawable.ic_stop, getString(R.string.notif_stop_action), pendingIntentStop);

    notifID = NotificationActionHandler.START_ID;

    TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
    stackBuilder.addNextIntent(new Intent(this, MainActivity.class));
    PendingIntent resultPendingIntent =
            stackBuilder.getPendingIntent(
                    0,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
    builder.setContentIntent(resultPendingIntent);
    NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
    notificationManager.notify(notifID, builder.build());
    if (notifID==NotificationActionHandler.STOP_ID) {
        notificationManager.cancel(NotificationActionHandler.START_ID);
    }else{
        notificationManager.cancel(NotificationActionHandler.STOP_ID);
    }

    if (mainActivityInstance!=null){
        mainActivityInstance.getData();
    }

}

}
