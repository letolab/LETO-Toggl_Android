package beacons.leto.com.letoibeacons.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import beacons.leto.com.letoibeacons.api.LetoTogglRestClient;

/**
 * Created by Renzo on 23/02/16.
 */
public class AppPreferences {

    private static final String TAG = "APP PREFERENCES";


    private static SharedPreferences preferences;
    // Preferences keys
    private static final String AUTH_TOKEN = "auth_token";
    private static final String WORKSPACE_ID = "workspace_id";

    private static final String DETECTION_STATE = "detection_state";
    private static final String BEACON_UUID = "beacon_uuid";
    private static final String BEACON_DETECTED = "beacon_detected";

    private static final String GEOFENCE_DETECTION_STATE = "geofence_detection_state";
    private static final String GEOFENCE_LATITUDE = "geofence_latitude";
    private static final String GEOFENCE_LONGITUDE = "geofence_longitude";


    public static void initPreferences(Context context){
        preferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
    }


    // GETTERS
    public static String getAuthToken() {
        return preferences.getString(AUTH_TOKEN, "");
    }

    public static Integer getWorkspaceId() {
        return preferences.getInt(WORKSPACE_ID, -1);
    }

    public static Boolean getBeaconDetectionState() {
        return preferences.getBoolean(DETECTION_STATE, false);
    }

    public static String getBeaconUuid() {
        return preferences.getString(BEACON_UUID, "2F234454-CF6D-4A0F-ADF2-F4911BA9FFA6");
    }

    public static Boolean getBeaconDetected() {
        return preferences.getBoolean(BEACON_DETECTED, false);
    }

    public static Boolean getGeofenceDetectionState() {
        return preferences.getBoolean(GEOFENCE_DETECTION_STATE, false);
    }

    public static double getGeofenceLatitude() {
        return Double.longBitsToDouble(preferences.getLong(GEOFENCE_LATITUDE, Double.doubleToLongBits(0)));
    }

    public static double getGeofenceLongitude() {
        return Double.longBitsToDouble(preferences.getLong(GEOFENCE_LONGITUDE, Double.doubleToLongBits(0)));
    }


    // SETTERS
    public static void setAuthToken(String value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(AUTH_TOKEN, value);
        editor.commit();
        LetoTogglRestClient.setupRestClient(value);
        Log.d(TAG,"AUTH TOKEN = " + value);
    }

    public static void setWorkspaceId(Integer value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(WORKSPACE_ID, value);
        editor.commit();
        Log.d(TAG,"WORKSPACE ID = " + value);
    }

    public static void setBeaconDetectionState(Boolean value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(DETECTION_STATE, value);
        editor.commit();
        Log.d(TAG,"DETECTION STATE = " + value);
    }

    public static void setBeaconUuid(String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(BEACON_UUID, value);
        editor.commit();
        Log.d(TAG, "BEACON UUID = " + value);
    }

    public static void setBeaconDetected(Boolean value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(BEACON_DETECTED, value);
        editor.commit();
        Log.d(TAG,"BEACON DETECTED = " + value);
    }

    public static void setGeofenceDetectionState(Boolean value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(GEOFENCE_DETECTION_STATE, value);
        editor.commit();
        Log.d(TAG,"GEOFENCE DETECTION STATE  = " + value);
    }

    public static void setGeofenceLatitude(double value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(GEOFENCE_LATITUDE, Double.doubleToRawLongBits(value));
        editor.commit();
        Log.d(TAG,"GEOFENCE LATITUDE  = " + value);
    }

    public static void setGeofenceLongitude(double value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(GEOFENCE_LONGITUDE, Double.doubleToRawLongBits(value));
        editor.commit();
        Log.d(TAG,"GEOFENCE LONGITUDE  = " + value);
    }
}
