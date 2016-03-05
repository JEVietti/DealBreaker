package roast.app.com.dealbreaker;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.HashMap;
import java.util.Map;

import roast.app.com.dealbreaker.util.Constants;

public class ProfileActivity extends Fragment {
    private Button profile_button;
    private TextView profile_info, lastname;
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

        //Image view

        //Firebase section
        final Firebase user_ref = new Firebase(Constants.FIREBASE_URL_USERS).child(userName);
        //******************setTitle(user_ref.getKey());                                 //sets title(name of the user) on the action bar

        //sets up textview for name
        //sets the value to key, name

        user_ref.addValueEventListener(new ValueEventListener() {                    //event listener for firebase data
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {                    //on data change, will do these tasks
                Log.e("ProfileActivity.java", "Data has been Modified");             //log that data has been modified

                User user = dataSnapshot.getValue(User.class);
                //creates a new hashmap
                HashMap<String, Object> hashmap = new HashMap<String,Object>();
                Map<String, Object> map = new ObjectMapper().convertValue(user,Map.class);

                hashmap.put(Constants.FIREBASE_LOC_USER_INFO, map);
                //user_ref.child(userName).getKey();
                //String last_name;

                //last_name = hashmap.get(userName).toString();

                //lastname.setText(last_name);


                /*...........................................*/
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


        //Image Button section
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on click fetch image and set as the profile picture

            }
        });
        return view;
    }
   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_help:
                Toast.makeText(this,"Information that may help you",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/
   private void initializeScreen(View rootView) {
       profile_info = (TextView) rootView.findViewById(R.id.textView);
       imageButton = (ImageButton) rootView.findViewById(R.id.imageButton);
       Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
   }

}
