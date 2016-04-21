package roast.app.com.dealbreaker.models;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import roast.app.com.dealbreaker.util.PermissionsHelper;

public class UserLocation extends AppCompatActivity implements LocationListener {

    /* GPS Constant Permission */
    //This permission actually accounts for both Coarse and Fine Permissions
    //as per google doc:
    // Note: If you are using both NETWORK_PROVIDER and GPS_PROVIDER, then you need to request only the ACCESS_FINE_LOCATION permission,
    // because it includes permission for both providers. (Permission for ACCESS_COARSE_LOCATION includes permission only for NETWORK_PROVIDER.)
    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 12;

    /* Position */
    private static final int MINIMUM_TIME = 10000;  // 10s
    private static final int MINIMUM_DISTANCE = 50; // 50m

    /* GPS */
    private String mProviderName;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private String userLocation; // Location should be city, country
    private Location lastLocation, currentLocation;
    private String cityName, countryName;
    private Geocoder gcd;
    private List<Address> addresses;

    private PermissionsHelper permissionsHelper;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //private GoogleApiClient mClient;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationListener = new UserLocation();
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Get the best provider between gps, network and passive
        Criteria criteria = new Criteria();
        mProviderName = mLocationManager.getBestProvider(criteria, true);
        //mClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        getProviderName();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        permissionsHelper.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    private void getProviderName(){
            mProviderName = LocationManager.NETWORK_PROVIDER;
    }

    //may not be needed
    private void askPermissions(){
        //With API>=23, you have to ask the user for permission to view their location.
        if(Build.VERSION.SDK_INT >= 23){
            int accessFinePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

            if(accessFinePermission !=PackageManager.PERMISSION_GRANTED){

                String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

                ActivityCompat.requestPermissions(this, permissions, MY_PERMISSION_ACCESS_FINE_LOCATION);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //check GPS availability
        /*
        boolean isGPSAvailable = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isGPSAvailable) {
            //get run time permission
            permissionsHelper = new PermissionsHelper(this, new PermissionsHelper.OnPermissionListener() {
                @Override
                public void OnPermissionChanged(boolean permissionGranted) {
                    Log.d("GPS Permission: ", "permissionGranted: " + permissionGranted);
                    if (permissionGranted) {
                        gcd = new Geocoder(getBaseContext(), Locale.getDefault());
                        try{ //updates
                            lastLocation = mLocationManager.getLastKnownLocation(mProviderName);
                            addresses = gcd.getFromLocation(loc)
                            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME, MINIMUM_DISTANCE, mLocationListener);
                        } catch (SecurityException e) {   }
                    }

                }
            });

        }
        */

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onLocationChanged(final Location location) {
        boolean isGPSAvailable = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isGPSAvailable) {
            //get run time permission
            permissionsHelper = new PermissionsHelper(this, new PermissionsHelper.OnPermissionListener() {
                @Override
                public void OnPermissionChanged(boolean permissionGranted) {
                    Log.d("GPS Permission: ", "permissionGranted: " + permissionGranted);
                    if (permissionGranted) {
                        gcd = new Geocoder(getBaseContext(), Locale.getDefault());
                        try{ //updates
                            lastLocation = mLocationManager.getLastKnownLocation(mProviderName);
                            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME, MINIMUM_DISTANCE, mLocationListener);
                            try{
                                addresses = gcd.getFromLocation(location.getLatitude(),location.getLongitude(), 1);
                                if(addresses.size() > 0){
                                    cityName = addresses.get(0).getLocality();
                                    countryName = addresses.get(0).getCountryName();
                                    userLocation = cityName +","+ countryName;
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        } catch (SecurityException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });

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

    //Get the Location String of the City and Country
    public String getLocation(){
        return userLocation;
    }
}