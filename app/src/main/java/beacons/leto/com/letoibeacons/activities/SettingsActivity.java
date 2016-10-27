package beacons.leto.com.letoibeacons.activities;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import beacons.leto.com.letoibeacons.core.AppPreferences;
import beacons.leto.com.letoibeacons.R;

public class SettingsActivity extends NavigationActivity {

    TextView mSettingsMessageTV;
    ImageView mBeaconImage;
    ImageView mMapImage;
    TextView mBeaconStateTV;
    TextView mBeaconUUIDTV;

    boolean isBeaconActive =false;
    boolean isGeofenceActive =false;
    String beaconUUID;

    public static int RESULT_MAP = 1209;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(getString(R.string.settings));
        initUI();
    }

    private void initUI(){
        isBeaconActive=AppPreferences.getBeaconDetectionState();
        isGeofenceActive=AppPreferences.getGeofenceDetectionState();
        beaconUUID=AppPreferences.getBeaconUuid();
        mSettingsMessageTV = (TextView)findViewById(R.id.settingsMessageTV);
        mBeaconImage = (ImageView)findViewById(R.id.beaconIV);
        mMapImage = (ImageView)findViewById(R.id.mapIV);

        mBeaconStateTV = (TextView)findViewById(R.id.detectionStateTV);
        mBeaconUUIDTV = (TextView)findViewById(R.id.beaconUUIDTV);

        mBeaconImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBeaconActive){
                    isBeaconActive=false;
                    setupState();
                }else{
                    new MaterialDialog.Builder(SettingsActivity.this)
                            .title(R.string.beacon_uuid)
                            .content(R.string.beacon_uuid_content)
                            .inputRangeRes(36, 36, R.color.light_grey_text_color)
                            .negativeText(R.string.cancel)
                            .inputType(InputType.TYPE_CLASS_TEXT)
                            .input("", beaconUUID, new MaterialDialog.InputCallback() {
                                @Override
                                public void onInput(MaterialDialog dialog, CharSequence input) {
                                    beaconUUID=input.toString();
                                    isBeaconActive=true;
                                    isGeofenceActive=false;
                                    setupState();
                                }
                            }).show();
                }
            }
        });

        mMapImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isGeofenceActive){
                    isGeofenceActive=false;
                    setupState();
                }else {
                    Intent intent = new Intent(SettingsActivity.this, GeofenceSettingsActivity.class);
                    startActivityForResult(intent, RESULT_MAP);
                }
            }
        });

        findViewById(R.id.saveBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppPreferences.setBeaconUuid(beaconUUID);
                AppPreferences.setBeaconDetectionState(isBeaconActive);
                AppPreferences.setGeofenceDetectionState(isGeofenceActive);
                finish();
            }
        });

        setupState();
    }

    private void setupState(){
        if (isBeaconActive){
            mBeaconImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.beacon_enabled));
            mBeaconStateTV.setText(R.string.beacons_enabled);
            mBeaconStateTV.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
            mBeaconUUIDTV.setText(beaconUUID);
        }else{
            mBeaconImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.beacon_disabled));
            mBeaconUUIDTV.setText("");
        }

        if (isGeofenceActive){
            mMapImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.geofence_enabled));
            mBeaconStateTV.setText(R.string.geofence_enabled);
            mBeaconStateTV.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
            mBeaconUUIDTV.setText("");
        }else{
            mMapImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.geofence_disabled));
            mBeaconUUIDTV.setText("");
        }

        if (!isGeofenceActive && !isBeaconActive){
            mBeaconStateTV.setText(R.string.no_detection);
            mBeaconStateTV.setTextColor(ContextCompat.getColor(this, R.color.medium_grey_text_color));
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==RESULT_MAP) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        }
    }
}
