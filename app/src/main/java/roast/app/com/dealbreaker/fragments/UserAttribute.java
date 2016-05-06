//Fragment Class that is used to set the User's information such as their name, sex, age, etc.

package roast.app.com.dealbreaker.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.server.converter.StringToIntConverter;

import roast.app.com.dealbreaker.models.Age;
import roast.app.com.dealbreaker.models.UserLocation;
import roast.app.com.dealbreaker.util.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import roast.app.com.dealbreaker.R;
import roast.app.com.dealbreaker.models.User;
import roast.app.com.dealbreaker.util.DatePickerFragment;

public class UserAttribute extends Fragment implements DatePickerFragment.DateListener{
   //Class Variables
    private String firstNameUserValue, lastNameUserValue, ageUserValue, heightUserValue, birthDate, sexUserValue, sexualOrientationUserValue, locationUserValue, username, key;

    private EditText firstNameUserText, lastNameUserText, heightUserText, sexualOrientationUserText;
    private EditText birthDateText;
    private TextView locationText;
    private RadioButton setMale, setFemale;
    RadioGroup maleFemaleGroup;
    Button sendUserValues, retrieveLocationButton;
    private Firebase refUserInfo;
    private ValueEventListener userInfoListener;
    boolean checkSendStatus;
    Location userLocation;
    Context context;

    private Long mUserAge;
    private String mCurrentCity, mCurrentSex, mCurrentSexOr, mCurrentAge;
    public static UserAttribute newInstance(String userName) {
        UserAttribute fragment = new UserAttribute();
        Bundle args = new Bundle();
        args.putString("userName",userName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * Initialize instance variables with data from bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           key = getString(R.string.key_UserName);
            username = getArguments().getString(key);
            Log.d("Username:",username);
        }
        context = getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         // Initialize UI elements

        View rootView = inflater.inflate(R.layout.fragment_user_attribute, container, false);
        initializeScreen(rootView);

        //retrieveUserInfo();

        /*birthDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment picker = new DatePickerFragment();
                picker.show(getFragmentManager(),"datePicker");
            }
        });
        */

        retrieveLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserLocation();
                locationText.setText(locationUserValue);
            }
        });

        sendUserValues.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                grabEditText();
                grabButtonValues();
                checkSendStatus = checkAndSendData(username, firstNameUserValue, lastNameUserValue, birthDate, locationUserValue, heightUserValue, sexualOrientationUserValue, sexUserValue);
                if (checkSendStatus) {
                    if (checkIfChange()) {      // Checks to see if the user changed any data.
                        deleteUserFromBranch(); // If changes occured, delete the old branch
                    }
                    addUserToRoamingList();     // Add users to the appropriate branch
                    Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "UnSuccessful!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
        retrieveUserInfo();
    }

    @Override
    public void onPause(){
        super.onPause();
        refUserInfo.removeEventListener(userInfoListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        refUserInfo.removeEventListener(userInfoListener);
    }

    //Link the XML to the Activity
    private void initializeScreen(View rootView) {
        firstNameUserText=(EditText)rootView.findViewById(R.id.et_user_first_name);
        lastNameUserText=(EditText)rootView.findViewById(R.id.et_user_last_name);
        maleFemaleGroup = (RadioGroup) rootView.findViewById(R.id.radioGroupSex);
        setMale = (RadioButton) rootView.findViewById(R.id.radioButtonMale);
        setFemale = (RadioButton) rootView.findViewById(R.id.radioButtonFemale);
        heightUserText=(EditText) rootView.findViewById(R.id.et_user_height);
        sexualOrientationUserText=(EditText)rootView.findViewById(R.id.et_user_sexual_or);
        birthDateText = (EditText)rootView.findViewById(R.id.birthDateText);
        locationText = (TextView) rootView.findViewById(R.id.locationTextValue);
        sendUserValues = (Button) rootView.findViewById(R.id.user_attribute_finished_button);
        retrieveLocationButton = (Button) rootView.findViewById(R.id.locationButton);
    }

    // all of the Edit Text Values
    private void grabEditText() {
        firstNameUserValue = firstNameUserText.getText().toString().trim();
        lastNameUserValue = lastNameUserText.getText().toString().trim();
        //ageUserValue = ageUserText.getText().toString().trim();
        birthDate = birthDateText.getText().toString().trim();
        heightUserValue = heightUserText.getText().toString().trim();
        //locationUserValue = locationText.getText().toString();
        sexualOrientationUserValue = sexualOrientationUserText.getText().toString().trim();
    }

    //Grab the Radio Button corresponding values
    private void grabButtonValues(){
        //int checked = maleFemaleGroup.getCheckedRadioButtonId();
        if(setMale.isChecked()) {
            sexUserValue = "male";
        }
        else if(setFemale.isChecked()){
            sexUserValue = "female";
        }
    }

    //Get the Users Location to Store and Display in the UserInfo of the app and Database
    private void getUserLocation(){
        UserLocation mUserLocation = new UserLocation(context, getActivity());
        if(mUserLocation.isLocationPossible()){
            locationUserValue = mUserLocation.getUserLocation();
            if(locationUserValue != null){
            Log.d("Location in USER", locationUserValue);
            }
        }
    }

    private void retrieveUserInfo(){
        /**
         * Create Firebase references
         */
        //Creates a child node under the user so it can be independent of changes to the user's other info
        //because when the user is first created in Register Activity it has to llok in a different location.
        refUserInfo = new Firebase(Constants.FIREBASE_URL_USERS).child(username).child(Constants.FIREBASE_LOC_USER_INFO);

        //Add the value Event Listener so if data has already been inputted by the user then it will
        //pre-populated with existing data
        userInfoListener = refUserInfo.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                // You can use getValue to deserialize the data at dataSnapshot
                User user = dataSnapshot.getValue(User.class);
                // If there was no data at the location we added the listener, then
                if (user != null) {
                    //ageUserText.setText(Long.toString(user.getAge()));
                    firstNameUserText.setText(user.getFirstName());
                    heightUserText.setText(Long.toString(user.getHeight()));
                    String sex = user.getSex();
                    //check the corresponding button of the radio group based on database value
                    if (sex.equals("male")) {
                        maleFemaleGroup.check(R.id.radioButtonMale);
                    } else if (sex.equals("female")) {
                        maleFemaleGroup.check(R.id.radioButtonFemale);
                    }
                    lastNameUserText.setText(user.getLastName());
                    locationUserValue = user.getLocation();
                    locationText.setText(user.getLocation());
                    sexualOrientationUserText.setText(user.getSexualOrientation());
                    birthDateText.setText(user.getBirthDate());

                    mCurrentCity = user.getLocation();
                    mCurrentSex = user.getSex();
                    mCurrentSexOr = user.getSexualOrientation();
                    mCurrentAge = user.getAge().toString();
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(getString(R.string.LogTagUserInfo),
                        getString(R.string.FirebaseOnCancelledError) +
                                firebaseError.getMessage());
            }
        });

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


    //Check to see if the data entered is in the set of inputs needed or that it is not empty
    public boolean checkAndSendData(String username, String firstNameUserValue, String lastNameUserValue, String birthDate, String locationUserValue, String heightUserValue, String sexualOrientationUserValue, String sexUserValue){

        if(TextUtils.isEmpty(firstNameUserValue)){
            //firstNameUserText.setError("This field cannot be empty!");
            return false;
        }
        else if(TextUtils.isEmpty(lastNameUserValue)){
            //lastNameUserText.setError("This field cannot be empty!");
            return false;
        }
        //need to add more handling for checking if the date is entered in a proper format
        else if(!isThisDateValid(birthDate,"MM/dd/yyyy")){
            //birthDateText.setError("Invalid!");
            return false;
        }

        else if( TextUtils.isEmpty(locationUserValue) || locationUserValue == null){
            //locationText.setError("Location Empty");
            return false;
        }
        else if (TextUtils.isEmpty(heightUserValue) | heightUserValue == null || !TextUtils.isDigitsOnly(heightUserValue)) {
            //heightUserText.setError("This field cannot be empty and must be in digits in terms of inches!");
            return false;
        }
        else if(TextUtils.isEmpty(sexualOrientationUserValue)||((!sexualOrientationUserValue.equals("straight")&&(!sexualOrientationUserValue.equals("bisexual"))&&(!sexualOrientationUserValue.equals("gay"))))){
            //sexualOrientationUserText.setError("Invalid!, Inputs can be straight, gay, or bisexual");
            return false;
        }
        else if(sexUserValue == null || /*(!setFemale.isChecked() && !setMale.isChecked())*/  (!sexUserValue.equals("male") && !sexUserValue.equals("female"))){
            //setFemale.setError("Invalid!, Inputs can be either male or female");
            return false;
        } else {
            Age a = new Age();
            Date birth = a.ConvertToDate(birthDate);
            Long age = Long.valueOf(a.calculateAge(birth));

            if(age < 18 ){
               // birthDateText.setText("Must be 18 years or older!");
                return false;
            }
            else if(age >= 130){
                //birthDateText.setText("Must be less than 130 years old!");
                return false;
            }
            Log.d("Age of User", age.toString());
            mUserAge = age;
            String firstName = firstNameUserValue;
            String lastName = lastNameUserValue;
            Long height = Long.valueOf(heightUserValue);
            String sex = sexUserValue;
            String birthday = birthDate;
            String sexual_orientation = sexualOrientationUserValue;
            User user = new User(username, firstName, lastName, sex, birthday ,age, sexual_orientation, height, locationUserValue);
                addUserAttributes(user);

        }
        return true;
    }

    //Add the entered data to the database in the specified location
    private void addUserAttributes(User user){
        //Set Reference to Firebase node
        String username = user.getUserName();
        Firebase ref = new Firebase(Constants.FIREBASE_URL_USERS);
        HashMap<String, Object> updates = new HashMap<String, Object>();
        Map<String,Object> map = new ObjectMapper().convertValue(user, Map.class);
        updates.put(Constants.FIREBASE_LOC_USER_INFO, map);
        ref.child(username).updateChildren(updates);
        /*
        * ref.child(username).child
        * */
    }
    //This is for the Date Dialog Picker not in use as of now...
    @Override
    public void returnDate(String date) {
        birthDate = date;
    }

    // Checks to see if the user changed data. If there is a change, return true;
    public boolean checkIfChange() {
        System.out.println(mCurrentCity);
        System.out.println(locationUserValue);
        System.out.println("City: " + (mCurrentCity.equals(locationUserValue)));
        System.out.println(mCurrentSex);
        System.out.println(sexUserValue);
        System.out.println("Sex: " + (mCurrentSex.equals(sexUserValue)));
        System.out.println(mCurrentSexOr);
        System.out.println(sexualOrientationUserValue);
        System.out.println("SO: " + (mCurrentSexOr.equals(sexualOrientationUserValue)));
        System.out.println(mCurrentAge);
        System.out.println(mUserAge);
        System.out.println("Age: " + mCurrentAge.equals(mUserAge.toString()));;
        if(!mCurrentCity.equals(locationUserValue)){
            return true;
        }

        if(!mCurrentSex.equals(sexUserValue)){
            return true;
        }

        if(!mCurrentSexOr.equals(sexualOrientationUserValue)){
            return true;
        }

        if(!mCurrentAge.equals(mUserAge.toString())){
            return true;
        }

        return false;
    }

    // Delete user from branch
    public void deleteUserFromBranch(){
        Firebase roamingURL = new Firebase(Constants.FIREBASE_URL + "roamingList").child(mCurrentCity).child(mCurrentSex).child(mCurrentSexOr);

        int userAge = Integer.parseInt(mCurrentAge);

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

        roamingURL.child(username).removeValue();
    }

    // Add users to the roming list
    public void addUserToRoamingList(){
        Firebase roamingURL = new Firebase(Constants.FIREBASE_URL + "roamingList").child(locationUserValue).child(sexUserValue).child(sexualOrientationUserValue);

        long userAge = mUserAge;

        // If statements to check which branch the user will fall under.
        if(userAge <= 20){
            roamingURL.child("18-20").child(username).child("mark").setValue(0);
        }
        else if(userAge >= 21 && userAge <= 29){
            roamingURL.child("21-29").child(username).child("mark").setValue(0);
        }
        else if(userAge >= 30 && userAge <= 39){
            roamingURL.child("30-39").child(username).child("mark").setValue(0);
        }
        else if(userAge >= 40 && userAge <= 49){
            roamingURL.child("40-49").child(username).child("mark").setValue(0);
        }
        else if(userAge >= 50 && userAge <= 59){
            roamingURL.child("50-59").child(username).child("mark").setValue(0);
        }
        else {
            roamingURL.child("60+").child(username).child("mark").setValue(0);
        }
    }
}
