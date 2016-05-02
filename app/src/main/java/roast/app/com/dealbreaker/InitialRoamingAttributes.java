package roast.app.com.dealbreaker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseApp;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Hashtable;

import roast.app.com.dealbreaker.models.User;
import roast.app.com.dealbreaker.util.Constants;

public class InitialRoamingAttributes extends AppCompatActivity {
    public String  mAge, mGender, mSexualOrientation, mHeight;
    public String mAgeWanted, mGenderWanted, mSexualOrientationWanted, mLocation, mHeightWanted;
    private EditText ageRoamingText, heightRoamingText, sexualOrientationRoamingText;
    private RadioButton maleButton, femaleButton;
    private Button sendRoamingValues;
    public String userName;

    //public ArrayList<String> mUsersFromRoamingList;
    public Hashtable mUsersRoammingPhoto, mUsersRoamingInfo, mUsersFromRoamingList;
    public String mWantedUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_roaming_attribute);

        ageRoamingText=(EditText) findViewById(R.id.et_roaming_age);
        maleButton = (RadioButton) findViewById(R.id.radioButtonMaleRoaming);
        femaleButton = (RadioButton) findViewById(R.id.radioButtonFemaleRoaming);
        sexualOrientationRoamingText=(EditText)findViewById(R.id.et_roaming_sexual_or);
        heightRoamingText=(EditText) findViewById(R.id.et_roaming_height);
        sendRoamingValues = (Button) findViewById(R.id.finished_roaming_attribute_button);

        Bundle arg = getIntent().getExtras();
        userName = arg.getString(getString(R.string.key_UserName));

        Firebase test = new Firebase(Constants.FIREBASE_URL);

        grabUserLocation();


        sendRoamingValues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grabData();
                Boolean validData = isDataValid();  // Returns true if the data is valid
                if (validData) {
                    setData();

                    Intent intent = new Intent(InitialRoamingAttributes.this, InitialGetRoamingList.class);
                    intent.putExtra("username", userName);
                    intent.putExtra("age", mAgeWanted);
                    intent.putExtra("gender", mGenderWanted);
                    intent.putExtra("SexualOrientation", mSexualOrientationWanted);
                    System.out.println("before bundle");
                    intent.putExtra("location", mLocation);
                    System.out.println("after bundle: " + mLocation);
                    startActivity(intent);
                }
            }
        });

    }

    public void grabUserLocation(){
        Firebase userURL = new Firebase(Constants.FIREBASE_URL_USERS).child(userName).child(Constants.FIREBASE_LOC_USER_INFO).child("location");

        userURL.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mLocation = dataSnapshot.getValue().toString();
                System.out.println(mLocation);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void grabData(){
        mAgeWanted = ageRoamingText.getText().toString();

        if(maleButton.isChecked()) {
            mGenderWanted = "male";
        }
        else if(femaleButton.isChecked()){
            mGenderWanted = "female";
        }

        mSexualOrientationWanted = sexualOrientationRoamingText.getText().toString();
        mHeightWanted = heightRoamingText.getText().toString();
    }

    public Boolean isDataValid(){
        if(mAgeWanted.isEmpty()){
            Toast.makeText(this, "First name cannot be empty!", Toast.LENGTH_LONG).show();
            return false;
        }

        else if(mGenderWanted.isEmpty()){
            Toast.makeText(this,"Gender must be chosen!", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(mSexualOrientationWanted.isEmpty()){
            Toast.makeText(this,"Sexual orientation cannot be empty!", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(mHeightWanted.isEmpty()){
            Toast.makeText(this,"Height cannot be empty!", Toast.LENGTH_LONG).show();
            return false;
        }


        return true;
    }

    public void setData(){
        Firebase roamingURL = new Firebase(Constants.FIREBASE_URL_ROAMING).child(userName);

        roamingURL.child("age").setValue(mAgeWanted);
        roamingURL.child("sex").setValue(mGenderWanted);
        roamingURL.child("sexualOrientation").setValue(mSexualOrientationWanted);
        roamingURL.child("height").setValue(mHeightWanted);

    }


    public void grabUserList(){
        Firebase roamingURL = new Firebase(Constants.FIREBASE_URL + "roamingList").child(mLocation).child(mGenderWanted).child(mSexualOrientationWanted).child("21-29");

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



    }

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
                for (int i = 0; i < mUsersFromRoamingList.size(); i++) {
                    // Check to see if the postSnapshot user is in the mUsersFromRoamingList Array
                    mWantedUsername = mUsersFromRoamingList.get(i).toString();
                    if (snapshot.child(mWantedUsername).exists()) {
                        // If the user is found, add the userInfo to the mUserRoamingInfo Hash table.
                        // The key will be the username and the value will be an user object
                        String userImg = snapshot.child(mWantedUsername).child("profilePic").child("profilePic").getValue().toString();
                        mUsersRoammingPhoto.put(mWantedUsername, userImg);
                        Log.d("User image listener", userImg);
                    }
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

        for (int i = 0; i < mUsersFromRoamingList.size(); i++){
            String tempUser = mUsersFromRoamingList.get(i).toString();
            userQueue.child(tempUser).child("profilePic").setValue(mUsersRoammingPhoto.get(tempUser));
            userQueue.child(tempUser).setValue(mUsersRoamingInfo.get(tempUser));
        }

    }
}
