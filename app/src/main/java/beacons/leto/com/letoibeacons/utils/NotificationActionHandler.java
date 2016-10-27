package beacons.leto.com.letoibeacons.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import beacons.leto.com.letoibeacons.api.LetoTogglRestClient;
import beacons.leto.com.letoibeacons.core.CoreApplication;
import beacons.leto.com.letoibeacons.R;
import beacons.leto.com.letoibeacons.activities.MainActivity;
import beacons.leto.com.letoibeacons.models.TimeEntryModel;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Renzo on 20/04/16.
 */
public class NotificationActionHandler extends BroadcastReceiver {
    NotificationManager notificationManager;

    //NOTIFICATION IDENTIFIERS
    public static final int START_ID = 0;
    public static final int STOP_ID = 1;

    //NOTIFICATION ACTIONS
    public static final String START_ACTION = "START";
    public static final String REMIND_ACTION = "REMIND";
    public static final String STOP_ACTION = "STOP";

    private static final long REMINDER_DELAY_MILL = 1800000;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(STOP_ID);
        notificationManager.cancel(START_ID);

        if(action.equals(START_ACTION)) {
            Log.v("NOTIF","Pressed START");
            performStartAction(context);
        }else if (action.equals(REMIND_ACTION)){
            Log.v("NOTIF","Pressed REMIND");
            performRemindAction(context);
        }else if (action.equals(STOP_ACTION)){
            Log.v("NOTIF","Pressed STOP");
            performStopAction(context);
        }

    }

    // START ACTION
    private void performStartAction(Context context){
        ((CoreApplication) context.getApplicationContext()).didEnterRegion(null);
    }


    // REMIND ACTION
    private void performRemindAction(final Context context){
        notificationManager.cancel(START_ID);
        ((CoreApplication) context.getApplicationContext()).didExitRegion(null);
        showRemindInfoNotification(context);
        new Timer().schedule(new TimerTask(){
            @Override
            public void run (){
                showRemindNotification(context);
            }
        }, REMINDER_DELAY_MILL);
    }

    private void showRemindInfoNotification(Context context){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle("LETO Toggl")
                .setContentText("Toggl stopped. Will remind you to start tracking in 30 minutes!")
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(null)
                .setColor(ContextCompat.getColor(context, R.color.flat_red))
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setPriority(Notification.PRIORITY_HIGH)
                .setShowWhen(true)
                .setAutoCancel(true)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Toggl stopped. Will remind you to start tracking in 30 minutes!"))
                .setSmallIcon(R.drawable.ic_stop);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(new Intent(context, MainActivity.class));
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NotificationActionHandler.STOP_ID, builder.build());
    }

    private void showRemindNotification(final Context context){
        LetoTogglRestClient.getPureApi().getCurrentEntry(new Callback<TimeEntryModel>() {
            @Override
            public void success(TimeEntryModel timeEntryModel, Response response) {
                if (timeEntryModel==null || timeEntryModel.getData()==null || timeEntryModel.getData().getDuration()>0){
                    //NO RUNNING TASK
                    showStartNotification(context);
                }else{
                    // RUNNING TASK
                    showDetectionEnabledNotification(context);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                showStartNotification(context);
            }
        });
    }

    private void showStartNotification(Context context){
        long[] vibrate = {0, 100, 200, 300};

        Intent startReceive = new Intent(context.getApplicationContext(), NotificationActionHandler.class);
        startReceive.setAction(NotificationActionHandler.START_ACTION);
        PendingIntent pendingIntentStart = PendingIntent.getBroadcast(context.getApplicationContext(), 12345, startReceive, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext())
                .setContentTitle("LETO Toggl")
                .setContentText("Reminder: Start Toggl task")
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(vibrate)
                .setColor(ContextCompat.getColor(context, R.color.flat_green))
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setPriority(Notification.PRIORITY_HIGH)
                .setShowWhen(true)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_play_arrow)
                .addAction(R.drawable.ic_play_arrow, context.getString(R.string.notif_start_action), pendingIntentStart);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context.getApplicationContext());
        stackBuilder.addNextIntent(new Intent(context.getApplicationContext(), MainActivity.class));
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NotificationActionHandler.START_ID, builder.build());
    }

    private void showDetectionEnabledNotification(Context context){
        long[] vibrate = {0, 100, 200, 300};

        Intent startReceive = new Intent(context.getApplicationContext(), NotificationActionHandler.class);
        startReceive.setAction(NotificationActionHandler.START_ACTION);
        PendingIntent pendingIntentStart = PendingIntent.getBroadcast(context.getApplicationContext(), 12345, startReceive, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext())
                .setContentTitle("LETO Toggl")
                .setContentText("Detection enabled")
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(vibrate)
                .setColor(ContextCompat.getColor(context, R.color.flat_green))
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setPriority(Notification.PRIORITY_HIGH)
                .setShowWhen(true)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_play_arrow)
                .setVisibility(Notification.VISIBILITY_PUBLIC);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context.getApplicationContext());
        stackBuilder.addNextIntent(new Intent(context.getApplicationContext(), MainActivity.class));
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NotificationActionHandler.START_ID, builder.build());
    }


    // STOP ACTION
    private void performStopAction(Context context){
        notificationManager.cancel(START_ID);
        ((CoreApplication) context.getApplicationContext()).didExitRegion(null);
    }
}