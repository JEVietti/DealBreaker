package roast.app.com.dealbreaker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

    public String mAgeWanted, mGenderWanted, mSexualOrientationWanted, mLocation, mHeightWanted;
    private EditText ageRoamingText, heightRoamingText, sexualOrientationRoamingText;
    private RadioButton maleButton, femaleButton;
    private Button sendRoamingValues;
    public String userName;
    private Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_roaming);

        ageRoamingText=(EditText) findViewById(R.id.et_roaming_age);
        maleButton = (RadioButton) findViewById(R.id.radioButtonMaleRoaming);
        femaleButton = (RadioButton) findViewById(R.id.radioButtonFemaleRoaming);
        sexualOrientationRoamingText=(EditText)findViewById(R.id.et_roaming_sexual_or);
        heightRoamingText=(EditText) findViewById(R.id.et_roaming_height);
        sendRoamingValues = (Button) findViewById(R.id.finished_roaming_attribute_button);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Desired Match Information");


        // Grab intent
        Bundle arg = getIntent().getExtras();
        userName = arg.getString(getString(R.string.key_UserName));

        // Grab the user's location and assigns it to the member mLocation
        grabUserLocation();

        // Run when the set button is pressed
        sendRoamingValues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grabData();
                Boolean validData = isDataValid();  // Returns true if the data is valid
                if (validData) {
                    setData();

                    Intent intent = new Intent(InitialRoamingAttributes.this, InitialScreen.class);
                    startActivity(intent);
                }
            }
        });

    }

    // Grabs the user's location from the user info branch.
    public void grabUserLocation(){
        Firebase userURL = new Firebase(Constants.FIREBASE_URL_USERS).child(userName).child(Constants.FIREBASE_LOC_USER_INFO).child("location");

        userURL.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mLocation = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    // Grab the data from the activity and assign the data to the activity members
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

    // Check if the data is valid. Returns true when valid.
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

    // Set the user's data to the Firebase roaming branch
    public void setData(){
        Firebase roamingURL = new Firebase(Constants.FIREBASE_URL_ROAMING).child(userName);

        roamingURL.child("age").setValue(mAgeWanted);
        roamingURL.child("sex").setValue(mGenderWanted);
        roamingURL.child("sexualOrientation").setValue(mSexualOrientationWanted);
        roamingURL.child("height").setValue(mHeightWanted);

    }
}