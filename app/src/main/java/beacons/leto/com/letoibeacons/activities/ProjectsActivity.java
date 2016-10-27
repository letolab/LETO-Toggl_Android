package beacons.leto.com.letoibeacons.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import beacons.leto.com.letoibeacons.R;
import beacons.leto.com.letoibeacons.adapters.EntryAdapter;
import beacons.leto.com.letoibeacons.adapters.ProjectAdapter;
import beacons.leto.com.letoibeacons.api.LetoTogglRestClient;
import beacons.leto.com.letoibeacons.models.Project;
import beacons.leto.com.letoibeacons.models.TimeEntry;
import beacons.leto.com.letoibeacons.utils.MyUtils;
import beacons.leto.com.letoibeacons.utils.SharedObjects;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ProjectsActivity extends NavigationActivity {

    RecyclerView mProjectsRecyclerView;
    private ProjectAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    List<List<Project>> mSectionedProjects = new ArrayList<>();
    List<Project> mProjects = new ArrayList<>();
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Projects");
        setContentView(R.layout.activity_projects);
        initUI();
        getData();
    }

    private void initUI(){
        mProjectsRecyclerView = (RecyclerView)findViewById(R.id.projectsList);
        mProjectsRecyclerView.setHasFixedSize(true);

        mProgressDialog = MyUtils.getGeneralProgressDialog(this);

        mLayoutManager = new LinearLayoutManager(this);
        mProjectsRecyclerView.setLayoutManager(mLayoutManager);
    }

    private void getData(){
        mProgressDialog.show();
        MyUtils.getProjectsWithClientsName(new Callback<List<Project>>() {
            @Override
            public void success(List<Project> projects, Response response) {
                mProgressDialog.dismiss();
                mProjects=projects;
                List <String> sectionTitles = createSections();
                mAdapter = new ProjectAdapter(mSectionedProjects, sectionTitles, ProjectsActivity.this);
                mProjectsRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void failure(RetrofitError error) {
                mProgressDialog.dismiss();
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), getString(R.string.no_internet), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    private List<String> createSections(){
        Collections.sort(mProjects, new Comparator<Project>(){
            public int compare(Project proj1, Project proj2) {
                return proj1.getClientObj().getName().compareToIgnoreCase(proj2.getClientObj().getName());
            }
        });
        List<Integer> clientsID = new ArrayList<>();
        List<String> sectionsName = new ArrayList<>();
        mSectionedProjects.clear();
        for (Project proj : mProjects){
                int itemIndex = -1;
                for (Integer clientID : clientsID){
                    if (proj.getCid().intValue()==clientID.intValue()){
                        itemIndex = clientsID.indexOf(clientID);
                        break;
                    }
                }

                if (itemIndex==-1){
                    sectionsName.add(proj.getClientObj().getName());
                    clientsID.add(proj.getCid());
                    List<Project> newSection = new ArrayList<>();
                    newSection.add(proj);
                    mSectionedProjects.add(newSection);
                }else{
                    mSectionedProjects.get(itemIndex).add(proj);
                }
        }
        return sectionsName;
    }
}
