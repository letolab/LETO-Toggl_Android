package beacons.leto.com.letoibeacons.activities;

import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import beacons.leto.com.letoibeacons.R;
import beacons.leto.com.letoibeacons.adapters.EntryAdapter;
import beacons.leto.com.letoibeacons.api.LetoTogglRestClient;
import beacons.leto.com.letoibeacons.models.TimeEntry;
import beacons.leto.com.letoibeacons.utils.MyUtils;
import beacons.leto.com.letoibeacons.utils.SharedObjects;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LastMonthTimeEntriesActivity extends NavigationActivity {

    private static final String TAG = "LAST MONTH ACTIVITY";

    RecyclerView mRecyclerView;
    List<TimeEntry> mTimeEntries = new ArrayList<>();
    EntryAdapter mAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    FrameLayout mLoadingFL;
    List<List<TimeEntry>> mSectionedTimeEntries = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_month_entries);
        setTitle(R.string.last_month);
        initUI();
        getData();
    }

    private void initUI(){
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
    }

    private void getData(){
        Date today = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(today);
        cal.add(Calendar.DAY_OF_MONTH, -30);
        Date today30 = cal.getTime();
        LetoTogglRestClient.getPureApi().getTimeEntries(MyUtils.getISO8601StringForDate(today30), MyUtils.getISO8601StringForDate(today), new Callback<List<TimeEntry>>() {
            @Override
            public void success(List<TimeEntry> timeEntries, Response response) {
                Log.d(TAG, "SUCCESS: Last month entries");
                mLoadingFL.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);
                mTimeEntries = Lists.reverse(timeEntries);
                mTimeEntries = MyUtils.createCompleteEntriesModel(mTimeEntries, SharedObjects.mProjects, SharedObjects.mClients);
                List<String> sections = createSections();
                mAdapter = new EntryAdapter(mSectionedTimeEntries,sections);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "ERROR: Last month entries");
                mLoadingFL.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), getString(R.string.no_internet), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    private List<String> createSections(){
        mSectionedTimeEntries.clear();
        List<String> sectionsName = new ArrayList<>();

        for (TimeEntry entry : mTimeEntries){
            if (entry.getDuration()>0){
                int itemIndex = -1;
                String entryStartDate = MyUtils.dateStringFromISOString(entry.getStart());
                for (String date : sectionsName){
                    if (entryStartDate.equals(date)){
                        itemIndex = sectionsName.indexOf(date);
                        break;
                    }
                }

                if (itemIndex==-1){
                    sectionsName.add(entryStartDate);
                    List<TimeEntry> newSection = new ArrayList<>();
                    newSection.add(entry);
                    mSectionedTimeEntries.add(newSection);
                }else{
                    mSectionedTimeEntries.get(itemIndex).add(entry);
                }
            }
        }
        return sectionsName;
    }
}
