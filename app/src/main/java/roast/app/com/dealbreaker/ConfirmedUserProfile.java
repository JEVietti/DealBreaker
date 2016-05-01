package roast.app.com.dealbreaker;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import roast.app.com.dealbreaker.models.ContactInfo;
import roast.app.com.dealbreaker.models.RelationshipAttribute;
import roast.app.com.dealbreaker.models.User;
import roast.app.com.dealbreaker.models.UserQualities;
import roast.app.com.dealbreaker.util.Constants;
import roast.app.com.dealbreaker.util.DownloadImages;

public class ConfirmedUserProfile extends AppCompatActivity {

    private TextView confirmedBiography,confirmedGoodQualitiesInfo, confirmedBadQualitiesInfo, confirmedPersonalName, confirmedUserAge, confirmedUserLocation, confirmedSexText;
    private TextView confirmedHeightText, confirmedSexORText;
    private ImageView confirmedUserView;
    private String userName, rootUserName, userActualName, confirmedContactInfo;
    private String key, profilePicURL;
    private DownloadImages downloadImages;
    private Firebase userInfoREF, userQualitiesREF, profilePicREF, confirmedRootUserRef, confirmedUserRef, rejectedRootREF, rejectedUserREF;
    private ValueEventListener userInfoListener, userQualitiesListener, profilePicListener;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmed_user_profile);
        if(getIntent().getExtras() != null) {
            Bundle arg = getIntent().getExtras();
            userName = arg.getString(getString(R.string.key_UserName));
            rootUserName = arg.getString("rootUser");
            //Initialize the View of the Fragment
            initializeView();
            returnContactInfo();
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
        confirmedSexORText = (TextView) findViewById(R.id.confirmedSexORContent);
        confirmedHeightText = (TextView) findViewById(R.id.confirmedHeightTextValue);
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

    //REMEMBER DON'T JUST REMOVE THEM FROM THE QUEUE ADD THEM TO THE REJECTED LIST
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_contact_info_rel:
                String info = returnContactInfo();
                AlertDialog.Builder dlg = new AlertDialog.Builder(this,R.style.AlertDialogTheme);
                dlg.setTitle("Contact Info");
                dlg.setMessage(info);
                dlg.setPositiveButton(R.string.GotItLabel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                }).create();
                dlg.show();
                return true;
            //Remove the User from confirmed list, Use Dialog to make sure
            case R.id.remove_user_icon:
                AlertDialog.Builder dlgRM = new AlertDialog.Builder(this,R.style.AlertDialogTheme);
                dlgRM.setTitle("End Relationship");
                dlgRM.setMessage("... One more Chance?");
                dlgRM.setNegativeButton(R.string.removeUserNo, null);
                dlgRM.setPositiveButton(R.string.removeUserYes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(getApplicationContext(),userActualName + " has been removed from your confirmed relationship list." , Toast.LENGTH_LONG).show();
                        removeFromConfirmed();
                    }
                }).create();
                dlgRM.show();
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

        Log.d("Event Listeners Back: ", "In Confirmed ");
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
            Log.d("Event Listeners Gone: ", "In Confirmed Profile!");
        }
    }

    private String returnContactInfo(){
        Query contactInfo;
        contactInfo = new Firebase(Constants.FIREBASE_URL_USERS).child(userName).child("contact_info");
        if(contactInfo != null){
              contactInfo.addValueEventListener(new ValueEventListener() {
                  @Override
                  public void onDataChange(DataSnapshot dataSnapshot) {
                      ContactInfo userCI = dataSnapshot.child("contactInfo").getValue(ContactInfo.class);
                      if(userCI != null) {
                          confirmedContactInfo = userCI.getContactInfo();
                      }
                      else{
                          confirmedContactInfo = "User has not made their contact information available.";
                      }
                  }

                  @Override
                  public void onCancelled(FirebaseError firebaseError) {

                  }
              });

        }

        return confirmedContactInfo;
    }

    //Remove a selected user from the confirmed list and yourself from their confirmed list
    private void removeFromConfirmed(){
        confirmedRootUserRef = new Firebase(Constants.FIREBASE_URL_CONFIRMED_RELATIONSHIPS).child(rootUserName).child(userName);

        confirmedUserRef = new Firebase(Constants.FIREBASE_URL_CONFIRMED_RELATIONSHIPS).child(userName).child(rootUserName);
        //Add the Users to their respective rejected lists
        rejectedRootREF = new Firebase(Constants.FIREBASE_URL_REJECTED).child(rootUserName).child(userName);
        rejectedUserREF = new Firebase(Constants.FIREBASE_URL_REJECTED).child(userName).child(rootUserName);
        //get their Relationship Attribute Objects
        confirmedRootUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RelationshipAttribute rootRelationshipAttribute = dataSnapshot.getValue(RelationshipAttribute.class);
                if(rootRelationshipAttribute != null){
                    rootRelationshipAttribute.setMark(0);
                    rejectedUserREF.setValue(rootRelationshipAttribute);
                    confirmedRootUserRef.removeValue();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        confirmedUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RelationshipAttribute userRelationshipAttribute = dataSnapshot.getValue(RelationshipAttribute.class);
                if (userRelationshipAttribute != null) {
                    userRelationshipAttribute.setMark(0);
                    rejectedRootREF.setValue(userRelationshipAttribute);
                    confirmedUserRef.removeValue();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

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
                    userActualName = firstAndLastName;
                    confirmedPersonalName.setText(firstAndLastName);
                    //displays the age of the user
                    confirmedUserAge.setText(user.getAge().toString());
                    confirmedSexText.setText(user.getSex());
                    confirmedSexORText.setText(user.getSexualOrientation());
                    confirmedHeightText.setText(user.getHeight().toString() + "'");
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
