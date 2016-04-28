package roast.app.com.dealbreaker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import roast.app.com.dealbreaker.models.User;
import roast.app.com.dealbreaker.models.UserQualities;
import roast.app.com.dealbreaker.util.Constants;
import roast.app.com.dealbreaker.util.DownloadImages;

public class ConfirmedUserProfile extends AppCompatActivity {

    private TextView confirmedBiography,confirmedGoodQualitiesInfo, confirmedBadQualitiesInfo, confirmedPersonalName, confirmedUserAge, confirmedUserLocation, confirmedSexText;
    private ImageView confirmedUserView;
    private String userName;
    private String key, profilePicURL;
    private DownloadImages downloadImages;
    private Firebase userInfoREF, userQualitiesREF, profilePicREF;
    private ValueEventListener userInfoListener, userQualitiesListener, profilePicListener;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmed_user_profile);
        if(getIntent().getExtras() != null) {
            Bundle arg = getIntent().getExtras();
            userName = arg.getString(getString(R.string.key_UserName));
            //Initialize the View of the Fragment
            initializeView();
        }
        else{
            finish();
            Toast.makeText(getApplicationContext(), "Failed to load selected User's page", Toast.LENGTH_SHORT).show();
        }
    }


    private void initializeView() {
        //profile_info = (TextView) rootView.findViewById(R.id.textView);
        confirmedPersonalName = (TextView) findViewById(R.id.confirmedNameTextView);
        confirmedBiography = (TextView) findViewById(R.id.confirmedBioText);
        confirmedBadQualitiesInfo = (TextView) findViewById(R.id.confirmedBadQualitiesText);
        confirmedGoodQualitiesInfo = (TextView) findViewById(R.id.confirmedGoodQualitiesText);
        confirmedUserView = (ImageView) findViewById(R.id.confirmedUserImage);
        confirmedUserAge = (TextView) findViewById(R.id.confirmedAgeContent);
        confirmedSexText = (TextView) findViewById(R.id.confirmedSexContent);
        confirmedUserLocation = (TextView) findViewById(R.id.confirmedLocationTextValue);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_confirmed_relationship, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_remove_rel:
                return true;
            case R.id.action_help:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    //Recreate the Listener if it had been removed due to Pause
    @Override
    public void onResume() {
        super.onResume();

        //FireBase Reference for User Profile Images will go here
        //for now static URL
        listenUSER_PROFILE_PIC();
        //profilePicURL = "https://s3-us-west-1.amazonaws.com/dealbreaker/classpic.jpg";
        //downloadImages.execute(profilePicURL);

        //Firebase Listener to Update UI Function calls
        //Function call for listening for the user_info section: used right now for Name of User
        listenUSER_INFO();

        //Function call for listening to update the user qualities of the Firebase Database
        listenUSER_QUALITIES();


        Log.d("Event Listeners Back: ", "In User Profile Fragment!");
    }

    //Destroy the Listener if the App is paused
    @Override
    public void onPause() {
        super.onPause();
        //if statement due to onDestroy being called in a the UpdateImage after replacing due to Orientation Change
        if (userInfoListener != null && userQualitiesListener != null && profilePicListener != null) {
            userInfoREF.removeEventListener(userInfoListener);
            profilePicREF.removeEventListener(profilePicListener);
            userQualitiesREF.removeEventListener(userQualitiesListener);
            Log.d("Event Listeners Gone: ", "In User Profile Fragment!");
        }
    }
    //Destroy the Listener if the App is destroyed/exited
    @Override
    public void onDestroy(){
        super.onDestroy();
        //if statement due to onDestroy being called in a the UpdateImage after replacing due to Orientation Change
        if(userInfoListener != null && userQualitiesListener != null && profilePicListener != null) {
            userInfoREF.removeEventListener(userInfoListener);
            profilePicREF.removeEventListener(profilePicListener);
            userQualitiesREF.removeEventListener(userQualitiesListener);
            Log.d("Event Listeners Gone: ", "In User Profile Fragment!");
        }
    }

    private void listenUSER_INFO(){
        userInfoREF = new Firebase(Constants.FIREBASE_URL_USERS).child(userName).child(Constants.FIREBASE_LOC_USER_INFO);
        //Add the value Event Listener so if data has already been inputted by the user then it will
        //pre-populated with existing data
        userInfoListener =  userInfoREF.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                // You can use getValue to deserialize the data at dataSnapshot
                User user = dataSnapshot.getValue(User.class);
                // If there was no data at the confirmedUserLocation we added the listener, then
                if (user != null) {
                    //displays first and last name of user to profile page
                    String firstAndLastName = user.getFirstName() + " " + user.getLastName();
                    confirmedPersonalName.setText(firstAndLastName);
                    //displays the age of the user
                    confirmedUserAge.setText(user.getAge().toString());
                    confirmedSexText.setText(user.getSex());
                    confirmedUserLocation.setText(user.getLocation());
                    toolbar.setTitle(firstAndLastName);
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(getString(R.string.LogTagUserProfile),
                        getString(R.string.FirebaseOnCancelledError) +
                                firebaseError.getMessage());
            }
        });
    }

    private void listenUSER_QUALITIES(){
        //Firebase section for Qualities
        userQualitiesREF = new Firebase(Constants.FIREBASE_URL_USERS).child(userName).child(Constants.FIREBASE_LOC_USER_QUALITIES);
        userQualitiesListener = userQualitiesREF.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserQualities userQualities = dataSnapshot.getValue(UserQualities.class);
                if (userQualities != null) {
                    confirmedBiography.setText(userQualities.getBiography());
                    confirmedBadQualitiesInfo.setText(userQualities.getBadQualities());
                    confirmedGoodQualitiesInfo.setText(userQualities.getGoodQualities());
                } else {
                    //Will send a toast message that will pop up notifying user that there was an error
                    Toast.makeText(getApplicationContext(), "Failed to Retrieve Info!!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(getString(R.string.LogTagUserProfile),
                        getString(R.string.FirebaseOnCancelledError) +
                                firebaseError.getMessage());
            }
        });

    }

    public void listenUSER_PROFILE_PIC(){
        profilePicREF = new Firebase(Constants.FIREBASE_URL_IMAGES).child(userName).child(Constants.FIREBASE_LOC_PROFILE_PIC);
        profilePicListener = profilePicREF.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserQualities profilePicSource = dataSnapshot.getValue(UserQualities.class);
                if (profilePicSource != null) {
                    downloadImages = new DownloadImages(confirmedUserView, ConfirmedUserProfile.this);
                    profilePicURL = profilePicSource.getProfilePic();
                    downloadImages.execute(profilePicURL);
                    downloadImages = null;
                } else {
                    downloadImages = new DownloadImages(confirmedUserView, ConfirmedUserProfile.this);
                    downloadImages.execute("dummyURL");
                    downloadImages = null;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(getString(R.string.LogTagUserProfile),
                        getString(R.string.FirebaseOnCancelledError) +
                                firebaseError.getMessage());
            }
        });
    }



}
