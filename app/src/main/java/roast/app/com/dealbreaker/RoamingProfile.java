package roast.app.com.dealbreaker;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.api.GoogleApiClient;

import roast.app.com.dealbreaker.fragments.UpdateImage;
import roast.app.com.dealbreaker.models.RelationshipAttribute;
import roast.app.com.dealbreaker.models.User;
import roast.app.com.dealbreaker.models.UserQualities;
import roast.app.com.dealbreaker.util.Constants;
import roast.app.com.dealbreaker.util.DownloadImages;

public class RoamingProfile extends AppCompatActivity {

    private TextView roamingBio_info,roamingGoodQualitiesInfo,roamingBadQualitiesInfo, roamingSexORText, roamingHeightText, roamingPersonalName,roamingAge, roamingUserLocation, roamingSexText;
    private ImageView roamingImageView;
    private View view;
    private String userName, rootUserName;
    private String key, profilePicURL;
    private DownloadImages downloadImages;
    private Firebase userInfoREF, userQualitiesREF, profilePicREF, roamingRootUserRef, roamingUserRef, rejectedRootREF, rejectedUserREF;
    private Firebase pendingRootUserREF, pendingUserREF, queueRootREF, queueUserREF;
    private ValueEventListener userInfoListener, userQualitiesListener, profilePicListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roaming_profile);
        initializeScreen();
        if (getIntent().getExtras() != null) {
            Bundle arg = getIntent().getExtras();
            userName = arg.getString(getString(R.string.key_UserName));
            rootUserName = arg.getString("rootUser");
            //Initialize the View of the Fragment
            initializeScreen();
        } else {
            finish();
            Toast.makeText(getApplicationContext(), "Failed to load selected User's page", Toast.LENGTH_SHORT).show();
        }
    }

        // Initialize UI elements

    private void initializeScreen() {
        //profile_info = (TextView) rootView.findViewById(R.id.textView);
        roamingPersonalName = (TextView) findViewById(R.id.roamingNameTextView);
        roamingBio_info = (TextView) findViewById(R.id.roamingBioText);
        roamingBadQualitiesInfo = (TextView) findViewById(R.id.roamingBadQualitiesText);
        roamingGoodQualitiesInfo = (TextView) findViewById(R.id.roamingGoodQualitiesText);
        roamingImageView = (ImageView) findViewById(R.id.roamingUserImage);
        roamingAge = (TextView) findViewById(R.id.roamingAgeContent);
        roamingSexText = (TextView) findViewById(R.id.roamingSexContent);
        roamingUserLocation = (TextView) findViewById(R.id.roamingLocationTextValue);
        roamingSexORText = (TextView) findViewById(R.id.roamingSexORContent);
        roamingHeightText = (TextView) findViewById(R.id.roamingHeightTextValue);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_roaming_profile, menu);
        return true;

    }

    //REMEMBER DON'T JUST REMOVE THEM FROM THE QUEUE ADD THEM TO THE REJECTED LIST
    //
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.action_roaming_dismiss:
                //Add a AlertDialog
                rejectRoaming();
                return true;

            case R.id.action_roaming_send_request:
                //Add a AlertDialog
                roamingSendRequest();
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


        Log.d("Event Listeners Back: ", "In Roaming Profile!");
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
                // If there was no data at the location we added the listener, then
                if (user != null) {
                    //displays first and last name of user to profile page
                    String firstAndLastName = user.getFirstName() + " " + user.getLastName();
                    //
                    roamingPersonalName.setText(firstAndLastName);
                    //displays the age of the user
                    roamingAge.setText(user.getAge().toString());
                    roamingSexText.setText(user.getSex());
                    roamingUserLocation.setText(user.getLocation());
                    roamingHeightText.setText(user.getHeight().toString());
                    roamingSexORText.setText(user.getSexualOrientation());
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
                    roamingBio_info.setText(userQualities.getBiography());
                    roamingBadQualitiesInfo.setText(userQualities.getBadQualities());
                    // Until accepted, Good Qualities are not shown.
                    roamingGoodQualitiesInfo.setText("Unavailable, Until both Users are in a confirmed relationship with each other!");
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
                    downloadImages = new DownloadImages(roamingImageView, RoamingProfile.this);
                    profilePicURL = profilePicSource.getProfilePic();
                    downloadImages.execute(profilePicURL);
                    downloadImages = null;
                } else {
                    downloadImages = new DownloadImages(roamingImageView, RoamingProfile.this);
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

    //Add the users into their corresponding rejected list pile and remove them from the queue/viewing
    private void rejectRoaming(){
        roamingRootUserRef = new Firebase(Constants.FIREBASE_URL_CONFIRMED_RELATIONSHIPS).child(rootUserName).child(userName);

        roamingUserRef = new Firebase(Constants.FIREBASE_URL_CONFIRMED_RELATIONSHIPS).child(userName).child(rootUserName);
        //Add the Users to their respective rejected lists
        rejectedRootREF = new Firebase(Constants.FIREBASE_URL_REJECTED).child(rootUserName).child(userName);
        rejectedUserREF = new Firebase(Constants.FIREBASE_URL_REJECTED).child(userName).child(rootUserName);
        //get their Relationship Attribute Objects
        roamingRootUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RelationshipAttribute rootRelationshipAttribute = dataSnapshot.getValue(RelationshipAttribute.class);
                if(rootRelationshipAttribute != null){
                    rootRelationshipAttribute.setMark(0);
                    rejectedUserREF.setValue(rootRelationshipAttribute);
                    roamingRootUserRef.removeValue();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        roamingUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RelationshipAttribute userRelationshipAttribute = dataSnapshot.getValue(RelationshipAttribute.class);
                if (userRelationshipAttribute != null) {
                    userRelationshipAttribute.setMark(0);
                    rejectedRootREF.setValue(userRelationshipAttribute);
                    roamingUserRef.removeValue();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    //Send a request to the selected user and add them both to their pending list delete both correspondingly from their queues
    private void roamingSendRequest(){
        pendingRootUserREF = new Firebase(Constants.FIREBASE_URL_PENDING).child(rootUserName).child(userName);
        pendingUserREF = new Firebase(Constants.FIREBASE_URL_PENDING).child(userName).child(rootUserName);
        queueRootREF = new Firebase(Constants.FIREBASE_URL_QUEUE).child(rootUserName).child(userName);
        queueUserREF = new Firebase(Constants.FIREBASE_URL_QUEUE).child(userName).child(rootUserName);

        //This one is for the user who selected the profile and send them a request
        queueRootREF.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RelationshipAttribute rootRA = dataSnapshot.getValue(RelationshipAttribute.class);
                rootRA.setMark(1);
                pendingRootUserREF.setValue(rootRA);
                queueRootREF.removeValue();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        //This should add the rootUser RA to the selected users pending list to be confirmed
        queueUserREF.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RelationshipAttribute userRA = dataSnapshot.getValue(RelationshipAttribute.class);
                userRA.setMark(0);
                pendingUserREF.setValue(userRA);
                queueUserREF.removeValue();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        //Remove the two Relationship Attributes from their Queues

    }


}