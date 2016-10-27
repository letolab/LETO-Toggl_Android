package beacons.leto.com.letoibeacons.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.TimeZone;

import beacons.leto.com.letoibeacons.R;
import beacons.leto.com.letoibeacons.api.LetoTogglRestClient;
import beacons.leto.com.letoibeacons.models.Project;
import beacons.leto.com.letoibeacons.models.StartTimeEntryModel;
import beacons.leto.com.letoibeacons.models.TimeEntry;
import beacons.leto.com.letoibeacons.models.TimeEntryModel;
import beacons.leto.com.letoibeacons.utils.MyUtils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class NewTaskActivity extends NavigationActivity {

    public final static int REQ_SELECTED_PROJ = 1;


    EditText mTaskNameET;
    EditText mProjectET;
    SwitchCompat mBillableSwitch;
    Project mSelectedProject = new Project("-","15");

    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        setTitle("New Task");
        initUI();
    }

    private void initUI(){
        mTaskNameET = (EditText)findViewById(R.id.taskNameET);
        mProjectET = (EditText)findViewById(R.id.projectET);
        mBillableSwitch = (SwitchCompat)findViewById(R.id.billableSwitch);

        mProgressDialog = MyUtils.getGeneralProgressDialog(this);

        mTaskNameET.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    // Check if no view has focus:
                    View view = getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    v.clearFocus();
                }
                return false;
            }
        });

        mProjectET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(NewTaskActivity.this, ProjectsActivity.class);
                startActivityForResult(intent,REQ_SELECTED_PROJ);
            }
        });

        findViewById(R.id.startBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewTask();
            }
        });
    }

    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if(requestCode == REQ_SELECTED_PROJ && resultCode==RESULT_OK) {
            mSelectedProject = data.getExtras().getParcelable("project");
            mProjectET.setText(mSelectedProject.getName());
        }
    }


    private void startNewTask(){
        mProgressDialog.show();
        TimeZone tz = TimeZone.getTimeZone("UTC");
        String taskName = (mTaskNameET.getText().toString().equals("")? "-":mTaskNameET.getText().toString());

        Log.d("NEW TASK", "NAME = "+ taskName+" PROJ = "+mSelectedProject.getName()+" BILLABLE = "+ mBillableSwitch.isChecked());

        TimeEntry entryToSend = new TimeEntry();
        entryToSend.setDescription(taskName);
        entryToSend.setPid(mSelectedProject.getId());
        entryToSend.setCreatedWith("LETO Toggl Android");
        entryToSend.setStart(MyUtils.getISO8601StringForDate(new Date()));
        entryToSend.setBillable(mBillableSwitch.isChecked());
        entryToSend.setDuration((int) (-System.currentTimeMillis() / 1000l));
        StartTimeEntryModel timeEntryModel = new StartTimeEntryModel(entryToSend);
        LetoTogglRestClient.getPureApi().startEntry(timeEntryModel, new Callback<TimeEntryModel>() {
            @Override
            public void success(TimeEntryModel timeEntryModel, Response response) {
                mProgressDialog.dismiss();
                finish();
            }

            @Override
            public void failure(RetrofitError error) {
                mProgressDialog.dismiss();
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), getString(R.string.no_internet), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }
}
