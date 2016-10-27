package beacons.leto.com.letoibeacons.activities;

import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import beacons.leto.com.letoibeacons.core.AppPreferences;
import beacons.leto.com.letoibeacons.R;

public class BeaconSettingsActivity extends NavigationActivity {

    SwitchCompat mSwitch;
    EditText mUUIDET;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_settings);
        initUI();
    }

    private void initUI(){
        mSwitch = (SwitchCompat)findViewById(R.id.beaconSwitch);
        mUUIDET = (EditText)findViewById(R.id.beaconUUIDET);

        mUUIDET.setText(AppPreferences.getBeaconUuid());
        mSwitch.setChecked(AppPreferences.getBeaconDetectionState());

        mUUIDET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(BeaconSettingsActivity.this)
                        .title(R.string.beacon_uuid)
                        .content(R.string.beacon_uuid_content)
                        .inputRangeRes(36, 36, R.color.light_grey_text_color)
                        .negativeText(R.string.cancel)
                        .neutralText(R.string.clear)
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                mSwitch.setChecked(false);
                            }
                        })
                        .onNeutral(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                mUUIDET.setText("");
                                mSwitch.setChecked(false);
                                AppPreferences.setBeaconUuid("");
                            }
                        })
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input("", mUUIDET.getText().toString(), new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                mUUIDET.setText(input.toString());
                                mSwitch.setChecked(true);
                            }
                        }).show();
            }
        });

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && mUUIDET.getText().length()==0){
                    mUUIDET.performClick();
                }
            }
        });
    }

    @Override
    public void actionOnBack(){
        Log.d("Beac set Activity", "Back pressed");
        if (mSwitch.isChecked() && mUUIDET.getText().length()>0){
            AppPreferences.setBeaconUuid(mUUIDET.getText().toString());
            AppPreferences.setBeaconDetectionState(true);
            AppPreferences.setGeofenceDetectionState(false);
        } else if (!mSwitch.isChecked()){
            AppPreferences.setBeaconDetectionState(false);
        }
    }
}
