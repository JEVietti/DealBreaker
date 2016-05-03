package roast.app.com.dealbreaker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Hashtable;

import roast.app.com.dealbreaker.models.User;
import roast.app.com.dealbreaker.util.Constants;

public class InitialGetRoamingList extends AppCompatActivity {
    private Button continueButton;

    public String mAgeWanted, mGenderWanted, mSexualOrientationWanted, mLocation;
    public Hashtable mUsersRoammingPhoto, mUsersRoamingInfo, mUsersFromRoamingList;
    public String mWantedUsername;
    public String userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_get_roaming_list);

        continueButton = (Button) findViewById(R.id.buttonContinue);
        mUsersRoammingPhoto = new Hashtable();
        mUsersRoamingInfo = new Hashtable();
        mUsersFromRoamingList = new Hashtable();

        // Grab the user info
        Bundle arg = getIntent().getExtras();
        userName = arg.getString("username");
        mAgeWanted = arg.getString("age");
        mGenderWanted = arg.getString("gender");
        mSexualOrientationWanted = arg.getString("SexualOrientation");
        mLocation = arg.getString("location");

        /*Firebase userURL = new Firebase(Constants.FIREBASE_URL_USERS).child(userName).child(Constants.FIREBASE_LOC_USER_INFO).child("location");


        // Grab user list
        Firebase roamingURL = new Firebase(Constants.FIREBASE_URL).child("roamingList").child("Fresno, California, United States").child("female").child("straight");

        int userAge = Integer.parseInt(mAgeWanted);

        // If statements to check which branch the user will fall under.
        if(userAge <= 20){
            roamingURL = roamingURL.child("18-20");
        }
        else if(userAge >= 21 && userAge <= 29){
            roamingURL = roamingURL.child("21-29");
        }
        else if(userAge >= 30 && userAge <= 39){
            roamingURL = roamingURL.child("30-39");
        }
        else if(userAge >= 40 && userAge <= 49){
            roamingURL = roamingURL.child("40-49");
        }
        else if(userAge >= 50 && userAge <= 59){
            roamingURL = roamingURL.child("50-59");
        }
        else {
            roamingURL = roamingURL.child("60+");
        }

        // Grab the list of users in the roamingList branch
        roamingURL.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int index = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    mUsersFromRoamingList.put(index, dataSnapshot.getKey().toString());
                    Log.d("Roaming listener", dataSnapshot.getKey().toString());
                    index++;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });


        //Grab user info
        Firebase roamingInfo = new Firebase(Constants.FIREBASE_URL_ROAMING);

        // Grab the roaming info for the children in the roaming branch
        roamingInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // Loop through all of the users in the mUsersFromRoamingList
                for (int i = 0; i < mUsersFromRoamingList.size(); i++) {
                    // Check to see if the postSnapshot user is in the mUsersFromRoamingList Array
                    mWantedUsername = mUsersFromRoamingList.get(i).toString();
                    if (snapshot.child(mWantedUsername).exists()) {
                        // If the user is found, add the userInfo to the mUserRoamingInfo Hash table.
                        // The key will be the username and the value will be an user object
                        User userInfo = snapshot.child(mWantedUsername).getValue(User.class);
                        //mUsersRoamingInfo.put(mWantedUsername, userInfo);
                        Log.d("Roaming Info listener", snapshot.child(mWantedUsername).getValue().toString());
                    }
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



        // Grab user images
        Firebase userImages = new Firebase(Constants.FIREBASE_URL_IMAGES);

        // Grab the picture for the children in the roaming branch
        userImages.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // Loop through all of the users in the mUsersFromRoamingList
                for (int i = 0; i < mUsersFromRoamingList.size(); i++) {
                    // Check to see if the postSnapshot user is in the mUsersFromRoamingList Array
                    mWantedUsername = mUsersFromRoamingList.get(i).toString();
                    if (snapshot.child(mWantedUsername).exists()) {
                        // If the user is found, add the userInfo to the mUserRoamingInfo Hash table.
                        // The key will be the username and the value will be an user object
                        String userImg = snapshot.child(mWantedUsername).child("profilePic").child("profilePic").getValue().toString();
                        //mUsersRoammingPhoto.put(mWantedUsername, userImg);
                        Log.d("User image listener", userImg);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

            */

        //grabUserList();
        //grabUserInfo();
        //grabUserImg();
        //addToQueue();


        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //addToQueue();
                Intent intent = new Intent(InitialGetRoamingList.this, InitialScreen.class);
                startActivity(intent);
            }
        });
    }

/*
    public void grabUserList(){
        Firebase roamingURL = new Firebase(Constants.FIREBASE_URL + "roamingList").child(mLocation).child(mGenderWanted).child(mSexualOrientationWanted);

        int userAge = Integer.parseInt(mAgeWanted);

        // If statements to check which branch the user will fall under.
        if(userAge <= 20){
            roamingURL = roamingURL.child("18-20");
        }
        else if(userAge >= 21 && userAge <= 29){
            roamingURL = roamingURL.child("21-29");
        }
        else if(userAge >= 30 && userAge <= 39){
            roamingURL = roamingURL.child("30-39");
        }
        else if(userAge >= 40 && userAge <= 49){
            roamingURL = roamingURL.child("40-49");
        }
        else if(userAge >= 50 && userAge <= 59){
            roamingURL = roamingURL.child("50-59");
        }
        else {
            roamingURL = roamingURL.child("60+");
        }

        // Grab the list of users in the roamingList branch
        roamingURL.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int index = 0;
                System.out.println("inside");
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    System.out.println("inside inside");
                    mUsersFromRoamingList.put(index, dataSnapshot.getKey().toString());
                    Log.d("Roaming listener", dataSnapshot.getKey().toString());
                    index++;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });



    }*/

    public void grabUserInfo(){
        Firebase roamingInfo = new Firebase(Constants.FIREBASE_URL_ROAMING);


        // Grab the roaming info for the children in the roaming branch
        roamingInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // Loop through all of the users in the mUsersFromRoamingList
                for (int i = 0; i < mUsersFromRoamingList.size(); i++) {
                    // Check to see if the postSnapshot user is in the mUsersFromRoamingList Array
                    mWantedUsername = mUsersFromRoamingList.get(i).toString();
                    if (snapshot.child(mWantedUsername).exists()) {
                        // If the user is found, add the userInfo to the mUserRoamingInfo Hash table.
                        // The key will be the username and the value will be an user object
                        User userInfo = snapshot.child(mWantedUsername).getValue(User.class);
                        mUsersRoamingInfo.put(mWantedUsername, userInfo);
                        Log.d("Roaming Info listener", snapshot.child(mWantedUsername).getValue().toString());
                    }
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void grabUserImg(){
        Firebase userImages = new Firebase(Constants.FIREBASE_URL_IMAGES);


        // Grab the picture for the children in the roaming branch
        userImages.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // Loop through all of the users in the mUsersFromRoamingList
                String userImg;
                for (int i = 0; i < mUsersFromRoamingList.size(); i++) {
                    // Check to see if the postSnapshot user is in the mUsersFromRoamingList Array
                    mWantedUsername = mUsersFromRoamingList.get(i).toString();
                    if (snapshot.child(mWantedUsername).exists()) {
                        // If the user is found, add the userInfo to the mUserRoamingInfo Hash table.
                        // The key will be the username and the value will be an user object
                        userImg = snapshot.child(mWantedUsername).child("profilePic").child("profilePic").getValue().toString();
                    }
                    else {
                        userImg = null;
                    }
                    mUsersRoammingPhoto.put(mWantedUsername, userImg);
                    Log.d("User image listener", userImg);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    // Get people who meet the user's roaming requirements to the queue
    public void addToQueue(){
        Firebase userQueue = new Firebase(Constants.FIREBASE_URL).child("queue").child(userName);

        for (int i =0; i < mUsersFromRoamingList.size(); i++){
            String tempUser = mUsersFromRoamingList.get(i).toString();
            //String userImg = mUsersRoammingPhoto.get(tempUser).toString();
            User user = (User) mUsersRoamingInfo.get(tempUser);
           // userQueue.child(tempUser).child("profilePic").setValue(userImg);
            userQueue.child(tempUser).child("mark").setValue(0);//.setValue(mUsersRoamingInfo.get(tempUser));
        }

    }




}

