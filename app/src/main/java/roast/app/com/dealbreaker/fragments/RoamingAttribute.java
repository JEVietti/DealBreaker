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
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import roast.app.com.dealbreaker.util.Constants;
import java.util.Date;

import roast.app.com.dealbreaker.R;
import roast.app.com.dealbreaker.User;


public class RoamingAttribute extends Fragment {
    //Class Variables
   // private ListView mListView;
    private String ageRoamingValue,heightRoamingValue,sexRoamingValue,sexualOrientationRoamingValue, username="cindy123";
    private EditText ageRoamingText, heightRoamingText, sexRoamingText, sexualOrientationRoamingText;
    private Button sendRoamingValues;

    public static RoamingAttribute newInstance() {
        RoamingAttribute fragment = new RoamingAttribute();
        Bundle args = new Bundle();
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Take in passed values


        /**
         * Initialize UI elements
         */
        final View rootView = inflater.inflate(R.layout.fragment_roaming_attribute, container, false);
        /**EditTexts
         *   Getting and storing their value by id
         */
        initializeScreen(rootView);
        sendRoamingValues.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                grabEditText();
                checkAndSendData();
            }
        });
            /**
         * Create Firebase references
         */

        Firebase refName_Roaming = new Firebase(Constants.FIREBASE_URL_SEEKING).child(username);

        /**
         * Add ValueEventListeners to Firebase references
         * to control get data and control behavior and visibility of elements
         */
        refName_Roaming.addValueEventListener(new ValueEventListener() {
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
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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

    private void checkAndSendData(){
        if (TextUtils.isEmpty(ageRoamingValue)) {
            ageRoamingText.setError("Don't leave field empty!");
        }
        else if(TextUtils.isEmpty(sexualOrientationRoamingValue)||((!sexualOrientationRoamingValue.equals("straight")&&(!sexualOrientationRoamingValue.equals("bisexual"))&&(!sexualOrientationRoamingValue.equals("gay"))))){
            sexualOrientationRoamingText.setError("Invalid!, Inputs can be straight, gay, or bisexual");
        }
        else if(TextUtils.isEmpty(sexRoamingValue)||((!sexRoamingValue.equals("male")&&(!sexRoamingValue.equals("female"))))) {
            sexRoamingText.setError("Invalid!, Inputs can be wither male or female");
        }
        else if(TextUtils.isEmpty(heightRoamingValue)){
            heightRoamingText.setError("Invalid!,Don't leave field Empty!");
        }
        else{
            Long age = Long.valueOf(ageRoamingValue);
            Long height = Long.valueOf(heightRoamingValue);
            String sex = sexRoamingValue;
            String sexual_orientation = sexualOrientationRoamingValue;
            User seekingUser = new User(sex,age,height,sexual_orientation);
            addRoamingAttributes(seekingUser);
        }
    }
        //add the Attributes of the User its seeking to the database
    public void addRoamingAttributes(User user){
        //Set Reference to Firebase node
        Firebase ref = new Firebase(Constants.FIREBASE_URL_SEEKING);
        ref.child(username).setValue(user);
    }


}
