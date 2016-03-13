//Fragment Class used to set the user's preferences for a possible match when roaming

package roast.app.com.dealbreaker.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import roast.app.com.dealbreaker.util.Constants;
import roast.app.com.dealbreaker.R;
import roast.app.com.dealbreaker.User;


public class RoamingAttribute extends Fragment {
    //Class Variables
   // private ListView mListView;
    private String ageRoamingValue,heightRoamingValue,sexRoamingValue,sexualOrientationRoamingValue, username, key;
    private EditText ageRoamingText, heightRoamingText, sexRoamingText, sexualOrientationRoamingText;
    private Button sendRoamingValues;
    private ValueEventListener connectedListener;
    private Firebase refName_Roaming;
    private boolean checkStateRoamingData;

    //Class is responsible for adding and retrieving the data the users are seeking
    //with potential matches
    public static RoamingAttribute newInstance(String userName) {
        RoamingAttribute fragment = new RoamingAttribute();
        Bundle args = new Bundle();
        //Adding the userName to the Bundle in order to use it later on
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
        //Getting Arguments from the Bundle in newInstance(String userName) above
        if (getArguments() != null) {
            key = getString(R.string.key_UserName);
            username = getArguments().getString(key);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       //Initialize where the view is
        final View rootView = inflater.inflate(R.layout.fragment_roaming_attribute, container, false);
       //Call the initialization of XML file
        initializeScreen(rootView);
        sendRoamingValues.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                grabEditText();
                checkStateRoamingData = checkAndSendData();
                if (checkStateRoamingData) {
                    Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView;
    }
    //Recreate the Listener if it had been removed due to Pause
    @Override
    public void onResume(){
            super.onResume();
            retrieveRoamingData();
    }
    //Destroy the Listener if the App is paused
    @Override
    public void onPause(){
        super.onPause();
        refName_Roaming.removeEventListener(connectedListener);
        Log.d("Event Listener Gone: ", "In Roaming Fragment!");
    }

    //Destroy the Listener if the App is destroyed/exited
    @Override
    public void onDestroy(){
        super.onDestroy();
        refName_Roaming.removeEventListener(connectedListener);
        Log.d("Event Listener Gone: ", "In Roaming Fragment!");
    }

    //Retrieve the Roaming Data of the User
    private void retrieveRoamingData(){
        /**
         * Create Firebase references
         */
        refName_Roaming = new Firebase(Constants.FIREBASE_URL_ROAMING).child(username);

        /**
         * Add ValueEventListeners to Firebase references
         * to control get data and control behavior and visibility of elements
         */
        connectedListener = refName_Roaming.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // You can use getValue to deserialize the data at dataSnapshot
                User user = dataSnapshot.getValue(User.class);
                // If there was no data at the location we added the listener, then
                if (user != null) {
                    ageRoamingText.setText(user.getAge().toString());
                    heightRoamingText.setText(user.getHeight().toString());
                    sexRoamingText.setText(user.getSex());
                    sexualOrientationRoamingText.setText(user.getSexualOrientation());
                }
                else {
                    Toast.makeText(getContext(), "Failed to retrieve User's Desired Match Data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    /**
     * Link layout elements from XML
     */
    private void initializeScreen(View rootView) {
        ageRoamingText=(EditText) rootView.findViewById(R.id.et_roaming_age);
        heightRoamingText=(EditText) rootView.findViewById(R.id.et_roaming_height);
        sexRoamingText=(EditText)rootView.findViewById(R.id.et_roaming_sex);
        sexualOrientationRoamingText=(EditText)rootView.findViewById(R.id.et_roaming_sexual_or);
        sendRoamingValues = (Button) rootView.findViewById(R.id.finished_roaming_attribute_button);
    }

    //Grab all of the Edit Text Values
    private void grabEditText() {
        ageRoamingValue = ageRoamingText.getText().toString().trim();
        heightRoamingValue = heightRoamingText.getText().toString().trim();
        sexRoamingValue = sexRoamingText.getText().toString().trim();
        sexualOrientationRoamingValue = sexualOrientationRoamingText.getText().toString().trim();
    }

    //Check that the Data is infact valid for the database schema
    private boolean checkAndSendData(){
        if (TextUtils.isEmpty(ageRoamingValue)) {
            ageRoamingText.setError("Don't leave field empty!");
            return false;
        }
        else if(TextUtils.isEmpty(sexualOrientationRoamingValue)||((!sexualOrientationRoamingValue.equals("straight")&&(!sexualOrientationRoamingValue.equals("bisexual"))&&(!sexualOrientationRoamingValue.equals("gay"))))){
            sexualOrientationRoamingText.setError("Invalid!, Inputs can be straight, gay, or bisexual");
            return false;
        }
        else if(TextUtils.isEmpty(sexRoamingValue)||((!sexRoamingValue.equals("male")&&(!sexRoamingValue.equals("female"))))) {
            sexRoamingText.setError("Invalid!, Inputs can be wither male or female");
            return false;
        }
        else if(TextUtils.isEmpty(heightRoamingValue)){
            heightRoamingText.setError("Invalid!,Don't leave field Empty!");
            return false;
        }
        else{
            Long age = Long.valueOf(ageRoamingValue);
            Long height = Long.valueOf(heightRoamingValue);
            String sex = sexRoamingValue;
            String sexual_orientation = sexualOrientationRoamingValue;
            User seekingUser = new User(sex,age,height,sexual_orientation);
            addRoamingAttributes(seekingUser);
            return true;
        }
    }
    //Add the Attributes of the User its seeking to the database and store it
    private void addRoamingAttributes(User user){
        //Set Reference to Firebase node
        Firebase ref = new Firebase(Constants.FIREBASE_URL_ROAMING);
        ref.child(username).setValue(user);
    }


}
