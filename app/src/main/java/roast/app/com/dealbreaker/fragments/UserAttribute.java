//Fragment Class that is used to set the User's information such as their name, sex, age, etc.

package roast.app.com.dealbreaker.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import roast.app.com.dealbreaker.util.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import roast.app.com.dealbreaker.R;
import roast.app.com.dealbreaker.models.User;
import roast.app.com.dealbreaker.util.DatePickerFragment;

public class UserAttribute extends Fragment implements DatePickerFragment.DateListener{
   //Class Variables
    private String firstNameUserValue, lastNameUserValue, ageUserValue, heightUserValue, birthDate, sexUserValue, sexualOrientationUserValue, username, key;
    private EditText firstNameUserText, lastNameUserText, ageUserText, heightUserText, sexualOrientationUserText;
    private EditText birthDateText;
    private RadioButton setMale, setFemale;
    RadioGroup maleFemaleGroup;
    Button sendUserValues;
    private Firebase refUserInfo;
    private ValueEventListener userInfoListener;
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

        sendUserValues.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                grabEditText();
                grabButtonValues();
                checkAndSendData();
                Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();

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
        ageUserText=(EditText) rootView.findViewById(R.id.et_user_age);
        maleFemaleGroup = (RadioGroup) rootView.findViewById(R.id.radioGroupSex);
        setMale = (RadioButton) rootView.findViewById(R.id.radioButtonMale);
        setFemale = (RadioButton) rootView.findViewById(R.id.radioButtonFemale);
        heightUserText=(EditText) rootView.findViewById(R.id.et_user_height);
        sexualOrientationUserText=(EditText)rootView.findViewById(R.id.et_user_sexual_or);
        birthDateText = (EditText)rootView.findViewById(R.id.birthDateText);
        sendUserValues = (Button) rootView.findViewById(R.id.user_attribute_finished_button);
    }
    //Grab all of the Edit Text Values
    private void grabEditText() {
        firstNameUserValue = firstNameUserText.getText().toString().trim();
        lastNameUserValue = lastNameUserText.getText().toString().trim();
        ageUserValue = ageUserText.getText().toString().trim();
        birthDate = birthDateText.getText().toString().trim();
        heightUserValue = heightUserText.getText().toString().trim();
        sexualOrientationUserValue = sexualOrientationUserText.getText().toString().trim();
    }

    private void grabButtonValues(){
        int checked = maleFemaleGroup.getCheckedRadioButtonId();
        switch(checked){
            case R.id.radioButtonMale:
                sexUserValue = "male";
                break;
            case R.id.radioButtonFemale:
                sexUserValue = "female";
                break;
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
                    ageUserText.setText(Long.toString(user.getAge()));
                    firstNameUserText.setText(user.getFirstName());
                    heightUserText.setText(Long.toString(user.getHeight()));
                    String sex = user.getSex();
                    if(sex.equals("male")) {
                        maleFemaleGroup.check(R.id.radioButtonMale);
                    }
                    else if(sex.equals("female")){
                        maleFemaleGroup.check(R.id.radioButtonFemale);
                    }
                    lastNameUserText.setText(user.getLastName());
                    sexualOrientationUserText.setText(user.getSexualOrientation());
                    birthDateText.setText(user.getBirthDate());
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

    //Check to see if the data entered is in the set of inputs needed or that it is not empty
    private void checkAndSendData(){

        if(TextUtils.isEmpty(firstNameUserValue)){
            firstNameUserText.setError("This field cannot be empty!");
        }
        else if(TextUtils.isEmpty(lastNameUserValue)){
            lastNameUserText.setError("This field cannot be empty!");
        }
        //need to add more handling for checking if the date is entered in a proper format
        else if(birthDate == null ){
            birthDateText.setError("Invalid!");
        }
        else if (TextUtils.isEmpty(ageUserValue)) {
            ageUserText.setError("This field cannot be empty!");
        }
        else if(TextUtils.isEmpty(sexualOrientationUserValue)||((!sexualOrientationUserValue.equals("straight")&&(!sexualOrientationUserValue.equals("bisexual"))&&(!sexualOrientationUserValue.equals("gay"))))){
            sexualOrientationUserText.setError("Invalid!, Inputs can be straight, gay, or bisexual");
        }
        else if(sexUserValue == null || (!sexUserValue.equals("male") && !sexUserValue.equals("female"))){
            setFemale.setError("Invalid!, Inputs can be either male or female");
        } else {
            String firstName = firstNameUserValue;
            String lastName = lastNameUserValue;
            Long age = Long.valueOf(ageUserValue);
            Long height = Long.valueOf(heightUserValue);
            String sex = sexUserValue;
            String birthday = birthDate;
            String sexual_orientation = sexualOrientationUserValue;
            User user = new User(username, firstName, lastName, sex, birthday ,age, sexual_orientation, height);
            addUserAttributes(user);
        }

    }

    //Add the entered data to the database in the specified location
    private void addUserAttributes(User user){
        //Set Reference to Firebase node
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
}
