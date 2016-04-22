package roast.app.com.dealbreaker.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import roast.app.com.dealbreaker.models.UserLocation;

//Helper Class that Distributes Permissions for the Users due to API 23 (Marshmallow) new way
// in handling permissions for location by requiring users to confirm GPS/Location based Permissions
//directly, not just through the Manifest

public class PermissionsHelper {
    private Activity mActivity;
    private final int PERMISSION_REQUEST = 0; //storing the request state
    private OnPermissionListener mOnPermissionListener;

    public interface OnPermissionListener {
        void OnPermissionChanged(boolean permissionGranted);
    }

    public PermissionsHelper(Activity activity, OnPermissionListener onPermissionListener) {
        mActivity = activity;
        setOnPermissionListener(onPermissionListener);
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            // Permission is already available
            if (mOnPermissionListener != null) {
                mOnPermissionListener.OnPermissionChanged(true);
            }
        } else {
            requestPermission(); //Permission must be requested
        }
    }

    //Requests the permission from the user by sending a form for the Permission in the Activity is is called in
    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity,
                Manifest.permission.ACCESS_FINE_LOCATION)) {

            ActivityCompat.requestPermissions(mActivity,  new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST);

        } else {

            if (mOnPermissionListener != null) {
                mOnPermissionListener.OnPermissionChanged(false);
            }

            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST);
        }
    }

    public void setOnPermissionListener(OnPermissionListener onPermissionListener){
        mOnPermissionListener = onPermissionListener;
    }

    //Receives the result of the Permission Prompt
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST) {
            // Request for permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //permission granted
                if (mOnPermissionListener != null) {
                    mOnPermissionListener.OnPermissionChanged(true);
                }

            } else {
                //permission denied
                if (mOnPermissionListener != null) {
                    mOnPermissionListener.OnPermissionChanged(false);
                }
            }
        }
    }
}




