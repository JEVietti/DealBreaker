package roast.app.com.dealbreaker.models;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import roast.app.com.dealbreaker.R;
import roast.app.com.dealbreaker.util.PermissionsHelper;

public class UserLocation extends Service implements LocationListener {

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
    private Context mContext;
    private String mProviderName;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private String userLocation; // Location should be city, country
    private Location lastLocation, currentLocation, location;
    private String cityName, adminName,countryName;
    private Geocoder gcd;
    private List<Address> addresses;
    private Activity rootActivity;
    private boolean isGPSEnabled, isNetworkEnabled, locationPossible = false;
    private PermissionsHelper permissionsHelper;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient mClient;


    public UserLocation(Context context, Activity activity){
        mContext = context;
        rootActivity = activity;
        mLocationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
        getLocation();
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        permissionsHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private String getProviderName(){
            return LocationManager.NETWORK_PROVIDER;
    }

    //Get the Location for the User Attributes right now the return statement is not being used instead
    //the helper get statement is being used to retrieve the produced name of the location by city potentially adminarea  and country
    private Location getLocation() {
        mLocationListener = this;
        isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        mProviderName = getProviderName();
        if (isGPSEnabled) {
            //get run time permission
            permissionsHelper = new PermissionsHelper(rootActivity, "GPS", new PermissionsHelper.OnPermissionListener() {
                @Override
                public void OnPermissionChanged(boolean permissionGranted) {
                    Log.d("GPS Permission: ", "permissionGranted: " + permissionGranted);
                    if (permissionGranted) {
                        gcd = new Geocoder(mContext, Locale.getDefault());
                        try{ //updates
                            locationPossible = true;
                            location = mLocationManager.getLastKnownLocation(mProviderName);
                            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME, MINIMUM_DISTANCE, mLocationListener);
                            try{
                                if(location != null) {
                                    addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    if (addresses.size() > 0) {
                                        //set the user Location by city admin name and the country
                                        cityName = addresses.get(0).getLocality();
                                        adminName = addresses.get(0).getAdminArea();
                                        countryName = addresses.get(0).getCountryName();
                                        //some locations do not have an admin area, this is mostly for different states
                                        //with the same city names
                                        if (adminName != null) {
                                            userLocation = cityName + ", " + adminName + ", " + countryName;
                                        } else {
                                            userLocation = cityName + ", " + countryName;
                                        }
                                        Log.e("Location", userLocation);
                                    }
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
        else{
            //show the settings alert dialog to prompt the user
            //to enable their GPS
            showSettingsAlert();
        }

        return location;
    }

    //For the Api <=22 it gets the user to enable their location if they wish to use the grab location button
    //Sends them directly to enable screen in settings
    private void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(rootActivity, R.style.AlertDialogTheme);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button send them to their respective settings menu for location
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                rootActivity.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
            alertDialog.show();
    }


    public boolean isLocationPossible(){
        return this.locationPossible;
    }

    @Override
    public void onLocationChanged(Location location) {

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
    public String getUserLocation(){
        return userLocation;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}