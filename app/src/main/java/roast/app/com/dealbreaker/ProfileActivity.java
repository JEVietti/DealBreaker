package roast.app.com.dealbreaker;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.support.v7.widget.Toolbar;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.HashMap;
import java.util.Map;
import java.lang.String;

import roast.app.com.dealbreaker.util.Constants;

public class ProfileActivity extends Fragment {
    private Button profile_button;
    private TextView profile_info, firstName1, lastName2;
    private ImageButton imageButton;
    private String userName;

    private GoogleApiClient client;
    public static ProfileActivity newInstance(String userName) {
        ProfileActivity fragment = new ProfileActivity();
        Bundle args = new Bundle();
        //Adding the userName to the Bundle in order to use it later on
        args.putString("username",userName);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_profile);
        if (getArguments() != null) {
            userName = getArguments().getString("username");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize UI elements

        final View view = inflater.inflate(R.layout.activity_profile, container, false);
        initializeScreen(view);
        //setSupportActionBar(toolbar);
        //setTitle("username");

        Firebase refName_User_Profile = new Firebase(Constants.FIREBASE_URL_USERS).child(userName).child(Constants.FIREBASE_LOC_USER_INFO);

        //Add the value Event Listener so if data has already been inputted by the user then it will
        //pre-populated with existing data
        refName_User_Profile.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                // You can use getValue to deserialize the data at dataSnapshot
                User user = dataSnapshot.getValue(User.class);
                // If there was no data at the location we added the listener, then
                if (user != null) {
                    //displays first name of user to profile page
                    firstName1.setText(user.getFirstName()); //user.getFirstName()
                    //displays last name of user to profile page
                    lastName2.setText(user.getLastName());
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

/*
        //Image view

        //Firebase section
        final Firebase user_ref = new Firebase(Constants.FIREBASE_URL_USERS).child(userName).child(Constants.FIREBASE_LOC_USER_QUALITIES);
        //******************setTitle(user_ref.getKey());       //sets title(name of the user) on the action bar

        //sets up textview for name
        //sets the value to key, name

        user_ref.addValueEventListener(new ValueEventListener() {                    //event listener for firebase data
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {           //on data change, will do these tasks
                Log.e("ProfileActivity.java", "Data has been Modified");    //log that data has been modified
                User user = dataSnapshot.getValue(User.class);

                if (user != null)//checks if user is null
                {

                }
                else
                {   //will send a toast message that will pop up notifying user that there was an error
                    Toast.makeText(getContext(),"Failed to Retrieve Info!!",Toast.LENGTH_LONG).show();
                }

                //user_ref.child(userName).getKey();
                //String last_name;

                //last_name = hashmap.get(userName).toString();

                //lastname.setText(last_name);



//fix here      String user = dataSnapshot.child("user_info").child("lastName").getValue().toString();  //last name retrieved from firebase
                //profile_info.setText(user_ref.getKey());
                //lastname = (TextView) view.findViewById(R.id.textView2);                  //creates textview widget
                //lastname.setText(user_ref.child(userName).getKey());                                              //sets text to be displayed via widget
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
        */
        //Image Button section
       /* imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on click fetch image and set as the profile picture

            }
        });
        */
        return view;
    }

   private void initializeScreen(View rootView) {
       //profile_info = (TextView) rootView.findViewById(R.id.textView);
       firstName1 = (TextView) rootView.findViewById(R.id.textView);
       lastName2 = (TextView) rootView.findViewById(R.id.textView2);
       //imageButton = (ImageButton) rootView.findViewById(R.id.imageButton);
       Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
   }

   private void addElements(User user){
       //Set Reference to Firebase node
       Firebase ref = new Firebase(Constants.FIREBASE_URL_USERS);
       HashMap<String, Object> updates = new HashMap<String, Object>();
       Map<String,Object> map = new ObjectMapper().convertValue(user, Map.class);
       updates.put(Constants.FIREBASE_LOC_USER_QUALITIES, map);
       ref.child(userName).updateChildren(updates);
   }

}
