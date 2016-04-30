package roast.app.com.dealbreaker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import roast.app.com.dealbreaker.models.RelationshipAttribute;
import roast.app.com.dealbreaker.models.User;
import roast.app.com.dealbreaker.models.UserQualities;
import roast.app.com.dealbreaker.util.Constants;
import roast.app.com.dealbreaker.util.DownloadImages;

public class PendingUserProfile extends AppCompatActivity {

    private TextView pendingBiography, pendingGoodQualitiesInfo, pendingBadQualitiesInfo, pendingPersonalName, pendingUserAge, pendingUserLocation, pendingSexText;
    private TextView pendingHeightText, pendingSexORText;
    private int pendingMark;
    private ImageView pendingUserView;
    private String userName, rootUserName, userActualName, pendingContactInfo;
    private String key, profilePicURL;
    private DownloadImages downloadImages;
    private Firebase userInfoREF, userQualitiesREF, profilePicREF, pendingRootUserRef, pendingUserRef;
    private ValueEventListener userInfoListener, userQualitiesListener, profilePicListener;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_user_profile);
        if (getIntent().getExtras() != null) {
            Bundle arg = getIntent().getExtras();
            userName = arg.getString(getString(R.string.key_UserName));
            rootUserName = arg.getString("rootUser");
            //Initialize the View of the Fragment
            initializeView();
            returnPendingMark();
        } else {
            finish();
            Toast.makeText(getApplicationContext(), "Failed to load selected User's page", Toast.LENGTH_SHORT).show();
        }
    }


    private void initializeView() {
        //profile_info = (TextView) rootView.findViewById(R.id.textView);
        pendingPersonalName = (TextView) findViewById(R.id.pendingNameTextView);
        pendingBiography = (TextView) findViewById(R.id.pendingBioText);
        pendingBadQualitiesInfo = (TextView) findViewById(R.id.pendingBadQualitiesText);
        pendingGoodQualitiesInfo = (TextView) findViewById(R.id.pendingGoodQualitiesText);
        pendingUserView = (ImageView) findViewById(R.id.pendingUserImage);
        pendingUserAge = (TextView) findViewById(R.id.pendingAgeContent);
        pendingSexText = (TextView) findViewById(R.id.pendingSexContent);
        pendingUserLocation = (TextView) findViewById(R.id.pendingLocationTextValue);
        pendingSexORText = (TextView) findViewById(R.id.pendingSexORContent);
        pendingHeightText = (TextView) findViewById(R.id.pendingHeightTextValue);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        int mark = returnPendingMark();
        if(mark == 1) {
            getMenuInflater().inflate(R.menu.menu_pending_relationship_pending, menu);
            return true;
        }
        else{
            getMenuInflater().inflate(R.menu.menu_pending_relationship_request, menu);
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

            switch (item.getItemId()) {
                case android.R.id.home:
                    onBackPressed();
                    return true;

                case R.id.action_confirm_pending_request:
                    confirmFromPending();
                    return true;

                case R.id.action_dismiss_pending:
                    dismissFromPending();
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
    public void onDestroy() {
        super.onDestroy();
        //if statement due to onDestroy being called in a the UpdateImage after replacing due to Orientation Change
        if (userInfoListener != null && userQualitiesListener != null && profilePicListener != null) {
            userInfoREF.removeEventListener(userInfoListener);
            profilePicREF.removeEventListener(profilePicListener);
            userQualitiesREF.removeEventListener(userQualitiesListener);
            Log.d("Event Listeners Gone: ", "In User Profile Fragment!");
        }
    }

    private int returnPendingMark(){
        final Query markValue = new Firebase(Constants.FIREBASE_URL_PENDING).child(rootUserName).child(userName);
        markValue.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RelationshipAttribute markUser = dataSnapshot.getValue(RelationshipAttribute.class);
                if (markUser != null) {
                    pendingMark = markUser.getMark();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        return pendingMark;
    }

    //Remove a selected user from the pending list and yourself from their pending list
    private void dismissFromPending() {
        pendingRootUserRef = new Firebase(Constants.FIREBASE_URL_PENDING).child(rootUserName).child(userName);
        pendingRootUserRef.removeValue();

        pendingUserRef = new Firebase(Constants.FIREBASE_URL_PENDING).child(userName).child(rootUserName);
        pendingUserRef.removeValue();
    }

    private void confirmFromPending(){
        final Firebase addREFConfirmed = new Firebase(Constants.FIREBASE_URL_CONFIRMED_RELATIONSHIPS).child(rootUserName).child(userName);
        final Firebase addREFPendingUserConfirmed = new Firebase(Constants.FIREBASE_URL_CONFIRMED_RELATIONSHIPS).child(userName).child(rootUserName);
        final Firebase REFPendingUserPending = new Firebase(Constants.FIREBASE_URL_PENDING).child(userName).child(rootUserName);
        final Firebase REFRootUserPending = new Firebase(Constants.FIREBASE_URL_PENDING).child(rootUserName).child(userName);

        REFPendingUserPending.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RelationshipAttribute pendingRA = dataSnapshot.getValue(RelationshipAttribute.class);
                if (pendingRA != null) {
                    pendingRA.setMark(1);
                    addREFPendingUserConfirmed.setValue(pendingRA);
                    REFPendingUserPending.removeValue();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        REFRootUserPending.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RelationshipAttribute rootRA = dataSnapshot.getValue(RelationshipAttribute.class);
                if (rootRA != null) {
                    rootRA.setMark(1);
                    addREFConfirmed.setValue(rootRA);
                    REFRootUserPending.removeValue();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



    }

    private void listenUSER_INFO() {
        userInfoREF = new Firebase(Constants.FIREBASE_URL_USERS).child(userName).child(Constants.FIREBASE_LOC_USER_INFO);
        //Add the value Event Listener so if data has already been inputted by the user then it will
        //pre-populated with existing data
        userInfoListener = userInfoREF.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                // You can use getValue to deserialize the data at dataSnapshot
                User user = dataSnapshot.getValue(User.class);
                // If there was no data at the pendingUserLocation we added the listener, then
                if (user != null) {
                    //displays first and last name of user to profile page
                    String firstAndLastName = user.getFirstName() + " " + user.getLastName();
                    userActualName = firstAndLastName;
                    pendingPersonalName.setText(firstAndLastName);
                    //displays the age of the user
                    pendingUserAge.setText(user.getAge().toString());
                    pendingSexText.setText(user.getSex());
                    pendingSexORText.setText(user.getSexualOrientation());
                    pendingHeightText.setText(user.getHeight().toString() + "'");
                    pendingUserLocation.setText(user.getLocation());
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

    private void listenUSER_QUALITIES() {
        //Firebase section for Qualities
        userQualitiesREF = new Firebase(Constants.FIREBASE_URL_USERS).child(userName).child(Constants.FIREBASE_LOC_USER_QUALITIES);
        userQualitiesListener = userQualitiesREF.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserQualities userQualities = dataSnapshot.getValue(UserQualities.class);
                if (userQualities != null) {
                    pendingBiography.setText(userQualities.getBiography());
                    pendingBadQualitiesInfo.setText(userQualities.getBadQualities());
                    pendingGoodQualitiesInfo.setText("Unavailable, Until both Users are in a confirmed relationship with each other!");
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

    public void listenUSER_PROFILE_PIC() {
        profilePicREF = new Firebase(Constants.FIREBASE_URL_IMAGES).child(userName).child(Constants.FIREBASE_LOC_PROFILE_PIC);
        profilePicListener = profilePicREF.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserQualities profilePicSource = dataSnapshot.getValue(UserQualities.class);
                if (profilePicSource != null) {
                    downloadImages = new DownloadImages(pendingUserView, PendingUserProfile.this);
                    profilePicURL = profilePicSource.getProfilePic();
                    downloadImages.execute(profilePicURL);
                    downloadImages = null;
                } else {
                    downloadImages = new DownloadImages(pendingUserView, PendingUserProfile.this);
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