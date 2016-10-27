package beacons.leto.com.letoibeacons.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import beacons.leto.com.letoibeacons.core.AppPreferences;
import beacons.leto.com.letoibeacons.R;
import beacons.leto.com.letoibeacons.utils.MyUtils;

public class GeofenceSettingsActivity extends NavigationActivity implements OnMapReadyCallback, LocationListener{

    private GoogleMap mMap;

    /* GPS Constant Permission */
    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 12;
    private LocationManager locationManager;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;

    MarkerOptions mWorkspaceMarker;

    SwitchCompat mSwitch;
    EditText mAddressET;

    TextView mDistanceTV;
    TextView mHelpTextView;

    Float distance;
    LatLng myLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geofence_settings);

        initUI();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void initUI(){
        mDistanceTV = (TextView)findViewById(R.id.distanceTV);
        mHelpTextView = (TextView)findViewById(R.id.helpTV);
        mSwitch = (SwitchCompat)findViewById(R.id.locationSwitch);
        final TextInputLayout addressTIL = (TextInputLayout)findViewById(R.id.locationTIL);
        mAddressET = (EditText)findViewById(R.id.locationET);

        mDistanceTV.setVisibility(View.GONE);

        MyUtils.applyRoundedCorners(mDistanceTV);
        MyUtils.applyRoundedCorners(mHelpTextView);

        mSwitch.setChecked(AppPreferences.getGeofenceDetectionState());


        mAddressET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    // Check if no view has focus:
                    View view = getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                    //do something
                    v.clearFocus();
                    addressTIL.clearFocus();
                    LatLng markerCoordinates = getLocationFromAddress(v.getText().toString());
                    if (markerCoordinates!=null){
                        addMarker(markerCoordinates);
                    }
                }
                return false;
            }
        });
    }

    private void checkPermissions() {
        // API 23: we have to check if ACCESS_FINE_LOCATION and/or ACCESS_COARSE_LOCATION permission are granted
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            setupMap();
        } else {
            // The ACCESS_FINE_LOCATION is denied, then I request it and manage the result in
            // onRequestPermissionsResult() using the constant MY_PERMISSION_ACCESS_FINE_LOCATION
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSION_ACCESS_FINE_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    checkPermissions();
                } else {
                    // permission denied
                }
                break;
            }

        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        checkPermissions();
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                addMarker(latLng);
            }
        });

        if (AppPreferences.getGeofenceLatitude()!=0 && AppPreferences.getGeofenceLongitude()!=0){
            addMarker(new LatLng(AppPreferences.getGeofenceLatitude(), AppPreferences.getGeofenceLongitude()));
            mHelpTextView.setVisibility(View.GONE);
        }
    }

    private void addMarker(LatLng position){
        mMap.clear();
        mWorkspaceMarker = new MarkerOptions()
                .position(position)
                .title("Your Workplace")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location));
        mMap.addMarker(mWorkspaceMarker);
        // Instantiates a new CircleOptions object and defines the center and radius
        double radiusInMeters = 100.0;
        int strokeColor = 0xffff0000; //red outline
        int shadeColor = 0x44ff0000; //opaque red fill

        CircleOptions circleOptions = new CircleOptions().center(position).radius(radiusInMeters).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(2);
        Circle mCircle = mMap.addCircle(circleOptions);

        if (myLocation!=null){
            calculationByDistance(myLocation,position);
        }

        mAddressET.setText(getAddressFromCoordinates(position));
    }

    private void setupMap() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this); //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER
//        new MaterialDialog.Builder(GeofenceSettingsActivity.this)
//                .title(R.string.geofence_title)
//                .content(R.string.geofence_message)
//                .positiveText("OK")
//                .show();
        if (AppPreferences.getGeofenceLatitude()!=0){
            addMarker(new LatLng(AppPreferences.getGeofenceLatitude(),AppPreferences.getGeofenceLongitude()));
        }
    }

    public void calculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        float km = (float)valueResult / 1;
        float metres = km * 1000;

        distance = metres;
        String distString = String.format("%.2f m", distance);
        if (metres>=1000) {
            distance = km;
            distString = String.format("%.2f Km", distance);
        }

        mDistanceTV.setText("Distance: " + distString);
        mDistanceTV.setVisibility(View.VISIBLE);
    }

    private String getAddressFromCoordinates(LatLng coordinates) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(coordinates.latitude, coordinates.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

        return address + ", " + city + ", " + postalCode + ", " + country;
    }

    public LatLng getLocationFromAddress(String strAddress) {
        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 1);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (Exception ex) {

            ex.printStackTrace();
            return null;
        }

        return p1;
    }

    // LOCATION CALLBACKS

    @Override
    public void onLocationChanged(Location location) {
        myLocation = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(myLocation, 15);
        mMap.animateCamera(cameraUpdate);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (distance==null && mWorkspaceMarker!=null){
            calculationByDistance(myLocation,mWorkspaceMarker.getPosition());
        }else if (distance!=null) {
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    //SAVE

    @Override
    public void actionOnBack(){
        Log.d("Beac set Activity", "Back pressed");
        if (mSwitch.isChecked() && mWorkspaceMarker != null){
            LatLng position = mWorkspaceMarker.getPosition();
            Log.d("POSITION", "Lat = " + position.latitude + " || " + "Long = " + position.longitude);
            AppPreferences.setGeofenceDetectionState(true);
            AppPreferences.setBeaconDetectionState(false);
            AppPreferences.setGeofenceLatitude(position.latitude);
            AppPreferences.setGeofenceLongitude(position.longitude);
        } else if (mWorkspaceMarker == null || !mSwitch.isChecked()){
            AppPreferences.setGeofenceDetectionState(false);
        }
    }
}
