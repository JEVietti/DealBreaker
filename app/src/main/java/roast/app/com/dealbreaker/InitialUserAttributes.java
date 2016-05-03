package roast.app.com.dealbreaker;

import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import roast.app.com.dealbreaker.models.Age;
import roast.app.com.dealbreaker.models.UserLocation;
import roast.app.com.dealbreaker.util.Constants;

public class InitialUserAttributes extends AppCompatActivity {
    public String mFirstName, mLastName, mBirthDay, mGender, mSexualOrientation, mHeight, mLocation;
    public Long mAge;
    private EditText firstNameUserText, lastNameUserText, heightUserText, sexualOrientationUserText;
    private EditText birthDateText;
    private TextView locationText;
    private RadioButton setMale, setFemale;
    RadioGroup maleFemaleGroup;
    Button sendUserValues, retrieveLocationButton;
    Context context;
    public String userName;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intial_user_info);
        firstNameUserText=(EditText) findViewById(R.id.et_user_first_name);
        lastNameUserText=(EditText) findViewById(R.id.et_user_last_name);
        maleFemaleGroup = (RadioGroup) findViewById(R.id.radioGroupSex);
        setMale = (RadioButton) findViewById(R.id.radioButtonMale);
        setFemale = (RadioButton) findViewById(R.id.radioButtonFemale);
        heightUserText=(EditText) findViewById(R.id.et_user_height);
        sexualOrientationUserText=(EditText)findViewById(R.id.et_user_sexual_or);
        birthDateText = (EditText)findViewById(R.id.birthDateText);
        locationText = (TextView) findViewById(R.id.locationTextValue);
        sendUserValues = (Button) findViewById(R.id.user_attribute_finished_button);
        retrieveLocationButton = (Button) findViewById(R.id.locationButton);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("User Info");


        Bundle arg = getIntent().getExtras();
        userName = arg.getString(getString(R.string.key_UserName));

        Firebase test = new Firebase(Constants.FIREBASE_URL);
        test.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("success user");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("Failure");
            }
        });



        retrieveLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserLocation();
                locationText.setText(mLocation);
            }
        });

        sendUserValues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grabData();
                Boolean validData = isDataValid();  // Returns true if the data is valid
                if(validData){
                    setData();
                    addUserToRoamingList();

                    Intent intent = new Intent(InitialUserAttributes.this, InitialRoamingAttributes.class);
                    intent.putExtra(getString(R.string.key_UserName), userName);
                    startActivity(intent);
                }

            }
        });

    }

    private void getUserLocation(){
        mLocation = "Fresno, California, United States";
        /*UserLocation mUserLocation = new UserLocation(context, InitialUserAttributes.this);
        if(mUserLocation.isLocationPossible()){
            // mLocation = mUserLocation.getUserLocation();
            mLocation = "Fresno, California, United States";
            if(mLocation != null){
                Log.d("Location in USER", mLocation);
            }
        }*/
    }

    private void grabData(){
        mFirstName = firstNameUserText.getText().toString();
        mLastName = lastNameUserText.getText().toString();

        mBirthDay = birthDateText.getText().toString();

        Age a = new Age();
        Date birth = a.ConvertToDate(mBirthDay);
        mAge = Long.valueOf(a.calculateAge(birth));

        if(setMale.isChecked()) {
            mGender = "male";
        }
        else if(setFemale.isChecked()){
            mGender = "female";
        }

        mSexualOrientation = sexualOrientationUserText.getText().toString();
        mHeight = heightUserText.getText().toString();
    }

    public Boolean isDataValid(){
        if(mFirstName.isEmpty()){
            Toast.makeText(this,"First name cannot be empty!", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(mLastName.isEmpty()){
            Toast.makeText(this,"Last name cannot be empty!", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(!isThisDateValid(mBirthDay,"MM/dd/yyyy")){
            Toast.makeText(this,"Incorrect birthday format!", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(mGender.isEmpty()){
            Toast.makeText(this,"Gender must be chosen!", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(mSexualOrientation.isEmpty()){
            Toast.makeText(this,"Sexual orientation cannot be empty!", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(mHeight.isEmpty()){
            Toast.makeText(this,"Height cannot be empty!", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(mLocation.isEmpty()){
            Toast.makeText(this,"Please grab your current location!", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    public boolean isThisDateValid(String dateToValidate, String dateFormat){
        Date date;
        if(dateToValidate == null){
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false);

        try {

            //if not valid, it will throw ParseException
            date = sdf.parse(dateToValidate);
            Log.d("UserAttributes Date",date.toString());

        } catch (ParseException e) {
            Log.d("UserAttributes Date","Not working");
            e.printStackTrace();
            return false;
        }
        Date current = new Date();
        try {
            current = sdf.parse(sdf.format(current));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return current.getTime() >= date.getTime();  //current date < entered date
    }

    public void setData(){
        Firebase usersURL = new Firebase (Constants.FIREBASE_URL_USERS).child(userName).child(Constants.FIREBASE_LOC_USER_INFO);

        usersURL.child("firstName").setValue(mFirstName);
        usersURL.child("lastName").setValue(mLastName);
        usersURL.child("age").setValue("23");
        usersURL.child("birthDate").setValue(mBirthDay);
        usersURL.child("sex").setValue(mGender);
        usersURL.child("sexualOrientation").setValue(mSexualOrientation);
        usersURL.child("height").setValue(mHeight);
        usersURL.child("location").setValue(mLocation);
        usersURL.child("userName").setValue(userName);
    }

    // Add users to the roming list
    public void addUserToRoamingList(){
        Firebase roamingURL = new Firebase(Constants.FIREBASE_URL + "roamingList").child(mLocation).child(mGender).child(mSexualOrientation);

        long userAge = mAge;

        // If statements to check which branch the user will fall under.
        if(userAge <= 20){
            roamingURL.child("18-20").child(userName).setValue(0);
        }
        else if(userAge >= 21 && userAge <= 29){
            roamingURL.child("21-29").child(userName).setValue(0);
        }
        else if(userAge >= 30 && userAge <= 39){
            roamingURL.child("30-39").child(userName).setValue(0);
        }
        else if(userAge >= 40 && userAge <= 49){
            roamingURL.child("40-49").child(userName).setValue(0);
        }
        else if(userAge >= 50 && userAge <= 59){
            roamingURL.child("50-59").child(userName).setValue(0);
        }
        else {
            roamingURL.child("60+").child(userName).setValue(0);
        }
    }
}


