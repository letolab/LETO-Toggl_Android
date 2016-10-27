package beacons.leto.com.letoibeacons.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.mikepenz.itemanimators.AlphaCrossFadeAnimator;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.altbeacon.beacon.BeaconManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import beacons.leto.com.letoibeacons.core.AppPreferences;
import beacons.leto.com.letoibeacons.core.CoreApplication;
import beacons.leto.com.letoibeacons.R;
import beacons.leto.com.letoibeacons.api.LetoTogglRestClient;
import beacons.leto.com.letoibeacons.custom_views.ProjectLayout;
import beacons.leto.com.letoibeacons.models.Client;
import beacons.leto.com.letoibeacons.models.Data;
import beacons.leto.com.letoibeacons.models.Project;
import beacons.leto.com.letoibeacons.models.StartTimeEntryModel;
import beacons.leto.com.letoibeacons.models.Tag;
import beacons.leto.com.letoibeacons.models.TimeEntry;
import beacons.leto.com.letoibeacons.models.TimeEntryModel;
import beacons.leto.com.letoibeacons.models.TogglUser;
import beacons.leto.com.letoibeacons.models.Workspace;
import beacons.leto.com.letoibeacons.utils.MyUtils;
import beacons.leto.com.letoibeacons.utils.SharedObjects;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends GeofenceActivity {
    private static final String TAG = "MAIN ACTIVITY";
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    Toolbar mToolbar;

    SwipeRefreshLayout mSwipeRefreshLayout;
    TogglUser mCurrentTogglUser;
    Data mUserData;
    List<Project> mProjects = new ArrayList<>();
    List<Tag> mTags = new ArrayList<>();
    List<TimeEntry> mTimeEntries = new ArrayList<>();
    List<Workspace> mWorkspaces = new ArrayList<>();
    List<Client> mClients = new ArrayList<>();
    TimeEntry mCurrentEntry;
    TimeEntry mLastTask;
    Thread timerThread;
    TextView durationCurrentTV;
    FrameLayout mLoadingFL;

    List<TimeEntry> mWeekTimeEntries = new ArrayList<>();
    long totalWeekTime = 0;
    TextView weekTimeTV;
    CardView mWeekCard;
    CardView mWeekProjectCard;
    CardView mLastEntryCard;

    ProgressDialog mProgresDialog;
    ImageView stopIV;
    ImageView playIV;

    ImageView mbeaconStatusIV;
    Drawer drawer;
    AccountHeader headerResult;
    ProfileDrawerItem profileItem;

    public static final String EMAIL_ADDRESS = "team@weareleto.com";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCurrentTogglUser=TogglUser.first(TogglUser.class);
        initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTimer();
        getData();
        ((CoreApplication) getApplication()).mainActivityInstance=this;
        checkLocationPermission();

        //DrawerItems
        PrimaryDrawerItem beaconItem = ((PrimaryDrawerItem)drawer.getDrawerItem(2));
        PrimaryDrawerItem geofenceItem = ((PrimaryDrawerItem)drawer.getDrawerItem(3));
        beaconItem.withBadge(AppPreferences.getBeaconDetectionState()? "On":"Off");
        geofenceItem.withBadge(AppPreferences.getGeofenceDetectionState()? "On":"Off");
        drawer.updateItem(beaconItem);
        drawer.updateItem(geofenceItem);
    }

    private void manageBluetooth(){
        // UNCOMMENT TO CHECK BEACON STATUS
//        if (AppPreferences.getBeaconDetectionState()){
//            ((CustomApplication) getApplication()).setupBeaconManager();
//            mbeaconStatusIV.setVisibility(View.VISIBLE);
//        }else{
//            ((CustomApplication) getApplication()).stopBeaconManager();
//            mbeaconStatusIV.setVisibility(View.INVISIBLE);
//        }

        if (AppPreferences.getGeofenceDetectionState()){
            addGeofences();
        }else{
            removeGeofences();
        }
    }

    private void updateBeaconStatus(){
        // UNCOMMENT TO CHECK BEACON STATUS
//        if (AppPreferences.getBeaconDetected()){
//            mbeaconStatusIV.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.online));
//        }else{
//            mbeaconStatusIV.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.offline));
//        }
    }

    public void getData(){
        LetoTogglRestClient.getPureApi().getUserInfo(new Callback<TogglUser>() {
            @Override
            public void success(TogglUser togglUser, Response response) {
                Log.d(TAG, "SUCCESS: User Info");
                mLoadingFL.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);
                mUserData = togglUser.getData();
                if (mUserData.getWorkspaces().size()>0){
                    AppPreferences.setWorkspaceId(mUserData.getWorkspaces().get(0).getId().intValue());
                }
                mProjects = mUserData.getProjects();
                mTags = mUserData.getTags();
                mWorkspaces = mUserData.getWorkspaces();
                mClients = mUserData.getClients();
                SharedObjects.mClients = mClients;
                SharedObjects.mProjects = mProjects;
                mTimeEntries = MyUtils.createCompleteEntriesModel(mUserData.getTimeEntries(), mProjects, mClients);
                //getCurrentEntry();

                if(mTimeEntries.size()>0) {
                    TimeEntry lastEntry = mTimeEntries.get(mTimeEntries.size() - 1);
                    if (lastEntry.getStop() != null) {
                        mCurrentEntry = null;
                        mLastTask = mTimeEntries.get(mTimeEntries.size() - 1);
                    } else {
                        mCurrentEntry = mTimeEntries.get(mTimeEntries.size() - 1);
                        if (mTimeEntries.size()>1) {
                            mLastTask = mTimeEntries.get(mTimeEntries.size() - 2);
                        }
                    }
                }
                updateUI();
                setupWeek();
                updateBeaconStatus();
            }

            @Override
            public void failure(RetrofitError error) {
                mSwipeRefreshLayout.setRefreshing(false);
                mLoadingFL.setVisibility(View.GONE);
                Log.d(TAG, "ERROR: User Info");
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), getString(R.string.no_internet_swipe), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    private void logoutAction(){
        mCurrentTogglUser.delete();
        AppPreferences.setAuthToken("");
        AppPreferences.setBeaconDetectionState(false);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
        ((CoreApplication) getApplication()).stopBeaconManager();
    }

    private void initUI(){
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("LETO Toggl");

        TextView fullnameTV = (TextView) findViewById(R.id.fullNameTV);
        weekTimeTV = (TextView) findViewById(R.id.timeWeekTV);
        fullnameTV.setText("Welcome back " + mCurrentTogglUser.getData().getFullname());
        // /You will setup the action bar with pull to refresh layout
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "refresh");
                getData();
            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
        mLoadingFL = (FrameLayout) findViewById(R.id.loadingFL);
        mLoadingFL.setVisibility(View.VISIBLE);

        mWeekProjectCard = (CardView) findViewById(R.id.weekProjCard);
        mWeekProjectCard.setVisibility(View.GONE);

        mLastEntryCard = (CardView) findViewById(R.id.lastCard);
        mLastEntryCard.setVisibility(View.GONE);

        mWeekCard = (CardView) findViewById(R.id.weekCard);
        mWeekCard.setVisibility(View.GONE);

        stopIV = (ImageView)findViewById(R.id.stopIV);
//        stopIV.getDrawable().setColorFilter(ContextCompat.getColor(this, R.color.flat_red), PorterDuff.Mode.SRC_IN);
        stopIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopCurrentTask();
            }
        });
        stopIV.setVisibility(View.INVISIBLE);

        playIV = (ImageView)findViewById(R.id.playIV);
//        playIV.getDrawable().setColorFilter(ContextCompat.getColor(this, R.color.flat_green), PorterDuff.Mode.SRC_IN);
        playIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLastTask();
            }
        });

        mProgresDialog = MyUtils.getGeneralProgressDialog(this);

        mbeaconStatusIV = (ImageView)findViewById(R.id.beaconStatusIV);

        Button startNewBtn = (Button)findViewById(R.id.startNewBtn);
        startNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewTaskActivity.class);
                startActivity(intent);
            }
        });
        MyUtils.applyRoundedCorners(startNewBtn);
        initDrawer();
    }

    private void initDrawer(){
        new DrawerBuilder().withActivity(this).build();

        // Create the AccountHeader
        profileItem = new ProfileDrawerItem().withName(" ").withEmail(" ").withIcon(getResources().getDrawable(R.drawable.empty_profile));
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.primary)
                .withSelectionListEnabledForSingleProfile(false)
                .addProfiles(
                        profileItem
                )
                .build();


        //create the drawer and remember the `Drawer` result object
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withAccountHeader(headerResult)
                .withItemAnimator(new AlphaCrossFadeAnimator())
                .addDrawerItems(
                        new SectionDrawerItem().withName(R.string.time_tracker).withDivider(false),
                        new PrimaryDrawerItem().withIdentifier(1).withName(R.string.last_30_days).withIcon(R.drawable.icon_histogram),
                        new SectionDrawerItem().withName(R.string.settings).withDivider(false),
                        new PrimaryDrawerItem().withIdentifier(2).withName(R.string.i_beacon).withIcon(R.drawable.icon_ibeacon).withBadge(AppPreferences.getBeaconDetectionState()? "On":"Off").withBadgeStyle(new BadgeStyle().withTextColor(ContextCompat.getColor(this, R.color.medium_grey_text_color))),
                        new PrimaryDrawerItem().withIdentifier(3).withName(R.string.geofence).withIcon(R.drawable.icon_geofence).withBadge(AppPreferences.getGeofenceDetectionState()? "On":"Off").withBadgeStyle(new BadgeStyle().withTextColor(ContextCompat.getColor(this, R.color.medium_grey_text_color))),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withIdentifier(4).withName(R.string.feedback),
//                        new PrimaryDrawerItem().withIdentifier(5).withName(R.string.terms_and_cond),
                        new PrimaryDrawerItem().withIdentifier(6).withName(R.string.log_out)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Intent intent;
                        switch ((int)drawerItem.getIdentifier()){
                            case 1:
                                intent = new Intent(MainActivity.this, LastMonthTimeEntriesActivity.class);
                                startActivity(intent);
                                break;
                            case 2:
                                intent = new Intent(MainActivity.this, BeaconSettingsActivity.class);
                                startActivity(intent);
                                break;
                            case 3:
                                intent = new Intent(MainActivity.this, GeofenceSettingsActivity.class);
                                startActivity(intent);
                                break;
                            case 4:
                                PackageInfo pInfo = null;
                                try {
                                    pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                                    Intent emailIntent = new Intent(Intent.ACTION_VIEW);
                                    Uri data = Uri.parse("mailto:?subject=" + getResources().getString(R.string.app_name) + " - Version: " + pInfo.versionName + "&body=" + "" + "&to=" + EMAIL_ADDRESS);
                                    emailIntent.setData(data);
                                    startActivity(emailIntent);
                                } catch (PackageManager.NameNotFoundException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case 5:
//                                Intent intent = new Intent(MainActivity.this, LastMonthTimeEntriesActivity.class);
//                                startActivity(intent);
                                break;
                            case 6:
                                logoutAction();
                                break;
                        }
                        return false;
                    }
                })
                .withCloseOnClick(true)
                .build();

        drawer.setSelection(-1);
    }

    private void stopCurrentTask(){
        mProgresDialog.show();
        LetoTogglRestClient.getPureApi().stopTask(mCurrentEntry.getId(), new Callback<TimeEntryModel>() {
            @Override
            public void success(TimeEntryModel timeEntryModel, Response response) {
                mProgresDialog.dismiss();
                getData();
            }

            @Override
            public void failure(RetrofitError error) {
                mProgresDialog.dismiss();
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), getString(R.string.no_internet), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    private void startLastTask(){
        TimeZone tz = TimeZone.getTimeZone("UTC");
        TimeEntry entryToSend = new TimeEntry();
        entryToSend.setDescription(mLastTask.getDescription());
        entryToSend.setTags(mLastTask.getTags());
        entryToSend.setPid(mLastTask.getPid());
        entryToSend.setCreatedWith("LETO Toggl Android");
        entryToSend.setStart(MyUtils.getISO8601StringForDate(new Date()));
        entryToSend.setBillable(mLastTask.getBillable());
        entryToSend.setDuration((int) (-System.currentTimeMillis() / 1000l));
        StartTimeEntryModel timeEntryModel = new StartTimeEntryModel(entryToSend);
        mProgresDialog.show();
        LetoTogglRestClient.getPureApi().startEntry(timeEntryModel, new Callback<TimeEntryModel>() {
            @Override
            public void success(TimeEntryModel timeEntryModel, Response response) {
                mProgresDialog.dismiss();
                getData();
            }

            @Override
            public void failure(RetrofitError error) {
                mProgresDialog.dismiss();
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), getString(R.string.no_internet), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    private void updateUI(){
        TextView currentEntryNameTV = (TextView) findViewById(R.id.currentTV);
        TextView currentDetailTV = (TextView) findViewById(R.id.currentDetailTV);
        durationCurrentTV = (TextView) findViewById(R.id.timeCurrentTV);

        if (mCurrentEntry!=null) {
            currentEntryNameTV.setMaxLines(1);
            currentEntryNameTV.setText(mCurrentEntry.getDescription());
            String projectName;
            if (mCurrentEntry.getProjectObj()!=null){
                projectName=mCurrentEntry.getProjectObj().getName();
            }else{
                projectName="-";
            }
            currentDetailTV.setText(projectName);
            currentDetailTV.setBackgroundColor(Color.parseColor(MyUtils.getColorFromIndex(Integer.valueOf(mCurrentEntry.getProjectObj().getColor()))));
            currentDetailTV.setVisibility(View.VISIBLE);
            MyUtils.applyRoundedCorners(currentDetailTV);
            durationCurrentTV.setText(mCurrentEntry.getDurationString());
            MyUtils.setupDurationTV(durationCurrentTV);
            stopIV.setVisibility(View.VISIBLE);
            // Start Timer
            startTimer();
        }else{
            currentDetailTV.setVisibility(View.GONE);
            stopIV.setVisibility(View.GONE);
            currentEntryNameTV.setMaxLines(5);
            currentEntryNameTV.setText(getString(R.string.tracking_nothing));
            durationCurrentTV.setText("");
        }

        TextView lastEntryNameTV = (TextView) findViewById(R.id.lastTaskTV);
        TextView lastDetailTV = (TextView) findViewById(R.id.lastTaskDescTV);
        TextView durationLastTV = (TextView) findViewById(R.id.timeLastTV);

        if (mLastTask!=null) {
            durationLastTV.setText(mLastTask.getDurationString());
            MyUtils.setupDurationTV(durationLastTV);
            lastEntryNameTV.setText(mLastTask.getDescription());
            String projectName;
            if (mLastTask.getProjectObj() != null) {
                projectName = mLastTask.getProjectObj().getName();
            } else {
                projectName = "-";
            }
            String clientName;
            lastDetailTV.setText(projectName);
            lastDetailTV.setBackgroundColor(Color.parseColor(MyUtils.getColorFromIndex(Integer.valueOf(mLastTask.getProjectObj().getColor()))));
            lastDetailTV.setVisibility(View.VISIBLE);
            MyUtils.applyRoundedCorners(lastDetailTV);
            playIV.setVisibility(View.VISIBLE);
        }else{
            playIV.setVisibility(View.INVISIBLE);
        }

        if (mUserData.getImageUrl().equals("https://assets.toggl.com/images/profile.png")){
            profileItem.withName(mUserData.getFullname()).withEmail(mUserData.getEmail());
            headerResult.updateProfile(profileItem);
        }else{
            Glide.with( this )
                    .load( mUserData.getImageUrl())
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            profileItem.withName(mUserData.getFullname()).withEmail(mUserData.getEmail()).withIcon(resource);
                            headerResult.updateProfile(profileItem);
                        }
                    });
        }
    }


    private void checkLocationPermission(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (AppPreferences.getBeaconDetectionState()) {
                if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(R.string.location_permission_title);
                    builder.setMessage(R.string.location_permission_message);
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                            }
                        }
                    });
                    builder.show();
                } else {
                    if (AppPreferences.getBeaconDetectionState()) {
                        verifyBluetooth();
                    } else {
                        manageBluetooth();
                    }
                }
            }else{
                manageBluetooth();
            }
        }else{
            if (AppPreferences.getBeaconDetectionState()) {
                verifyBluetooth();
            }else{
                manageBluetooth();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("LOCATION PERMISSION", "coarse location permission granted");
                    if (AppPreferences.getBeaconDetectionState()) {
                        verifyBluetooth();
                    }
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(R.string.no_location_permission_title);
                    builder.setMessage(R.string.no_location_permission_message);
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            System.exit(0);
                        }
                    });
                    builder.setNegativeButton(R.string.grant_permission, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final Intent i = new Intent();
                            i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            i.addCategory(Intent.CATEGORY_DEFAULT);
                            i.setData(Uri.parse("package:" + MainActivity.this.getPackageName()));
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                            startActivity(i);
                        }
                    });
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }
                    });
                    builder.show();
                }
                return;
            }
        }
    }

    private void verifyBluetooth() {
        try {
            if (!BeaconManager.getInstanceForApplication(this).checkAvailability()) {
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setTitle(R.string.bt_not_enabled_title);
                builder.setMessage(R.string.bt_not_enabled_message);
                builder.setNegativeButton(R.string.settings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setPositiveButton(R.string.turn_on_bt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intentBluetooth = new Intent();
                        intentBluetooth.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
                        startActivity(intentBluetooth);
                    }
                });
                builder.show();
            }else{
                // Bluetooth is enabled
                manageBluetooth();
            }
        }
        catch (RuntimeException e) {
//            final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
//            builder.setTitle(R.string.bt_LE_not_enabled_title);
//            builder.setMessage(R.string.bt_LE_not_enabled_message);
//            builder.setPositiveButton(android.R.string.ok, null);
//            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//
//                @Override
//                public void onDismiss(DialogInterface dialog) {
//                    finish();
//                    System.exit(0);
//                }
//
//            });
//            builder.show();
        }

    }


    private void setupWeek(){
        Date today = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(today);
        cal.add(Calendar.DAY_OF_WEEK, -cal.get(Calendar.DAY_OF_WEEK));
        Date today7 = cal.getTime();
        LetoTogglRestClient.getPureApi().getTimeEntries(MyUtils.getISO8601StringForDate(today7), MyUtils.getISO8601StringForDate(today), new Callback<List<TimeEntry>>() {
            @Override
            public void success(List<TimeEntry> timeEntries, Response response) {
                Log.d(TAG, "SUCCESS: Last week entries");
                mWeekTimeEntries = MyUtils.createCompleteEntriesModel(timeEntries, SharedObjects.mProjects, SharedObjects.mClients);
                setuWeekProjects(mWeekTimeEntries);
                setupWeekPlot(mWeekTimeEntries);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "ERROR: Last week entries");
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), getString(R.string.no_internet), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    private void setuWeekProjects(List<TimeEntry> weekTimeEntries) {
        mWeekProjectCard.setVisibility(View.VISIBLE);

        List<Integer> projectDurations = new ArrayList<>();
        List<Integer> projectColors = new ArrayList<>();
        List<String> projectNames = new ArrayList<>();

        for (TimeEntry entry : weekTimeEntries) {
            if (entry.getDuration()>0){
                int itemIndex = -1;
                if (entry.getProjectObj()==null){
                    continue;
                }else{
                    for (String proj : projectNames){
                        if (proj.equals(entry.getProjectObj().getName())){
                            itemIndex = projectNames.indexOf(proj);
                            break;
                        }
                    }
                }

                Integer color = Color.BLACK;
                if (entry.getProjectObj().getColor()!=null){
                    color = Color.parseColor(MyUtils.getColorFromIndex(Integer.valueOf(entry.getProjectObj().getColor())));
                }

                if (itemIndex==-1){
                    projectNames.add(entry.getProjectObj().getName());
                    projectColors.add(color);
                    projectDurations.add(entry.getDuration());
                }else{
                    projectNames.set(itemIndex, entry.getProjectObj().getName());
                    projectColors.set(itemIndex, color);
                    projectDurations.set(itemIndex, projectDurations.get(itemIndex)+entry.getDuration());
                }
            }
        }

        addProjectToWeekList(projectDurations,projectColors,projectNames);
    }

    private void addProjectToWeekList(List<Integer> projectDurations,  List<Integer> projectColors, List<String> projectNames){
        LinearLayout projectLL = (LinearLayout) findViewById(R.id.projectsLL);
        projectLL.removeAllViews();
        for (int i=0; i<projectDurations.size(); i++){
            ProjectLayout valueTV = new ProjectLayout(this, projectNames.get(i), projectDurations.get(i), projectColors.get(i));
            valueTV.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            projectLL.addView(valueTV);
        }

    }

    private void setupWeekPlot(List<TimeEntry> weekTimeEntries){

        BarChart chart = (BarChart) findViewById(R.id.chart2);
        ArrayList <BarEntry > yVals = new ArrayList<BarEntry>();

        totalWeekTime =0;
        List<List<Float>> dayByDayDuration = new ArrayList<>(7);
        List<List<Integer>> dayByDayColors = new ArrayList<>(7);
        for (int i=0; i<7; i++){
            dayByDayDuration.add(new ArrayList<Float>());
            dayByDayColors.add(new ArrayList<Integer>());
        }

        for (TimeEntry entry : weekTimeEntries){
            if (entry.getDuration()>0) {
                totalWeekTime += entry.getDuration();
                Date startDate = MyUtils.getDateFromISO8601String(entry.getStart());
                Calendar c = Calendar.getInstance();
                c.setTime(startDate);
                int dayOfWeek = c.get(Calendar.DAY_OF_WEEK)-1;
                Log.d("Weekday", String.valueOf(dayOfWeek));
                dayByDayDuration.get(dayOfWeek).add((float)entry.getDuration());

                Integer color = ContextCompat.getColor(this,R.color.colorAccent);
                if (entry.getProjectObj()!=null && entry.getProjectObj().getColor()!=null){
                    color = Color.parseColor(MyUtils.getColorFromIndex(Integer.valueOf(entry.getProjectObj().getColor())));
                }
                dayByDayColors.get(dayOfWeek).add(color);
            }
        }
        if (totalWeekTime>0){
            weekTimeTV.setText(MyUtils.splitToComponentTimes(totalWeekTime));
            MyUtils.setupDurationTV(weekTimeTV);
        }
        mWeekCard.setVisibility(View.VISIBLE);
        mLastEntryCard.setVisibility(View.VISIBLE);

        for (int i=0; i<dayByDayDuration.size(); i++){
            float[] floatArray = new float[dayByDayDuration.get(i).size()];
            int j = 0;
            for (Float f : dayByDayDuration.get(i)) {
                floatArray[j++] = (f != null ? f : Float.NaN); // Or whatever default you want.
            }
            BarEntry tempEntry = new BarEntry(i, floatArray);
            yVals.add(tempEntry);
        }

        BarDataSet setComp1 = new BarDataSet(yVals, "Week Plot");

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(setComp1);


        BarData data = new BarData(dataSets);
        //-----------------------------------

        // PLOT UI
        setComp1.setHighLightColor(getResources().getColor(R.color.flat_red));
        setComp1.setBarShadowColor(getResources().getColor(android.R.color.transparent));
        setComp1.setHighLightAlpha(255);
        setComp1.stackedColors=dayByDayColors;
        setComp1.useStackedColors=true;

        chart.getLegend().setEnabled(false);
        chart.setDescription("");
        chart.setTouchEnabled(false);
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);
        chart.setFitBars(true);


        chart.setNoDataTextDescription("No data");
        chart.animateY(0);

        XAxis xl = chart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawGridLines(false);
        xl.setAxisLineColor(Color.BLACK);
        xl.setValueFormatter(new MyDayValueFormatter());


        YAxis yLeftAxis = chart.getAxisLeft();
        yLeftAxis.setStartAtZero(true);
        yLeftAxis.setAxisLineColor(Color.TRANSPARENT);
        yLeftAxis.setValueFormatter(new MyYAxisValueFormatter());
        yLeftAxis.setGridColor(ContextCompat.getColor(this, R.color.light_grey_text_color));


        YAxis yRightAxis = chart.getAxisRight();
        yRightAxis.setEnabled(false);

        chart.setData(data);
        chart.getBarData().setDrawValues(false);
        chart.getBarData().setBarWidth(0.55f);
        chart.getBarData().setHighlightEnabled(true);
    }

    public class MyYAxisValueFormatter implements AxisValueFormatter {

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            int hours = (int)value / 3600;
            int remainder = (int) value - hours * 3600;
            int mins = remainder / 60;
            return String.format("%d h %d m", hours, mins);
        }

        @Override
        public int getDecimalDigits() {
            return 0;
        }
    }

    public class MyDayValueFormatter implements AxisValueFormatter {

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            List<String> xVals =  Arrays.asList("Sun","Mon","Tue","Wed","Thu","Fri","Sat");
            return xVals.get((int) value);
        }

        @Override
        public int getDecimalDigits() {
            return 0;
        }
    }

    private void startTimer(){
        if (timerThread==null && mCurrentEntry!=null){
            timerThread = new Thread() {
                @Override
                public void run() {
                    try {
                        while (!isInterrupted()) {
                            Thread.sleep(1000);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Log.d(TAG, "Updating time");
                                    if (mCurrentEntry!=null) {
                                        mCurrentEntry.setDurationString(MyUtils.splitToComponentTimes(mCurrentEntry.getDuration()));
                                        durationCurrentTV.setText(mCurrentEntry.getDurationString());
                                        MyUtils.setupDurationTV(durationCurrentTV);
                                    }
                                }
                            });
                        }
                    } catch (InterruptedException e) {
                    }
                }
            };
            timerThread.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (timerThread!=null) {
            timerThread.interrupt();
            timerThread = null;
        }
        ((CoreApplication) getApplication()).mainActivityInstance=null;
    }
}