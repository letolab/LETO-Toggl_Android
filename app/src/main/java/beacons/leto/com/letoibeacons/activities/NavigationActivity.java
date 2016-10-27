package beacons.leto.com.letoibeacons.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;


public class NavigationActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                actionOnBack();
                finish();
                return true;
        }
        return false;
    }

    public void actionOnBack(){
        Log.d("Nav Activity", "Back pressed");
    }

    @Override
    public void onBackPressed() {
        actionOnBack();
        super.onBackPressed();
    }
}
