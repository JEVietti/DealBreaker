package roast.app.com.dealbreaker;

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

import roast.app.com.dealbreaker.models.User;
import roast.app.com.dealbreaker.util.Constants;

/* Roaming Activity is for searching for other users that match with your requirements
* This app should be launched from a FAB from the UserNavigation Activity so it can be launched
 * across all of the fragments and not have to be initialized at individual fragments
 * this should help with maintenance and be a better user experience:
 * http://developer.android.com/intl/es/reference/android/support/design/widget/FloatingActionButton.html
* */

public class RoamingActivity extends AppCompatActivity {
    //Class variables
    private ArrayList<String> matchedUsers;
    //If we don't want to implement swiping yet we could instead add buttons to the page

    private Button addButton, rejectButton, nextButton;
    //add will immediately add them to the pending list if the other user has not made a decision on
    // the user yet, the rejected one will move them to the rejected list and will no longer show
    //up in their queue anymore. The next button keeps them in the queue but moves them into the back
    //of their queue

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roaming);
    }
    //Query the data in a pipeline
    //Grab a subset of data at once then display and regrab the data
    //when the user begins to run out of users to go through
    private ArrayList getUsers(){
        matchedUsers = new ArrayList<>();

        return matchedUsers;
    }

    //Initialize the view for the fragment
    private void initializeView(){

    }
/*
    private void listenRelationships(){
        confirmedUserName = new ArrayList<>();
        refConfirmedUsers = new Firebase(Constants.FIREBASE_URL_CONFIRMED_RELATIONSHIPS).child(userName);
        //Query for the username keys of the people listed in the confirmed section of the database
        //Get that list and use it in the Recycler View by grabbing from that List a List of
        //User info object of each user in the confirmed to display the pertinent information in the item holder
        //Store the Keys into the Array List ()
        //Maybe we link the needed information with the user in each section of pending and confirmed and queue
        getRefConfirmedUser = refConfirmedUsers.orderByKey();
        getRefConfirmedUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childUsers : dataSnapshot.getChildren()) {
                    Log.d("Users confirmed", childUsers.toString());
                    String name = childUsers.getKey();
                    if (name != null) {
                        confirmedUserName.add(name);
                    }
                }
                if(confirmedUserName.size() > 0) {
                    getConfirmedUserInfo();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }

    private void getConfirmedUserInfo() {
        confirmedUserInfoObjects = new ArrayList<>();
        for (int i = 0; i < confirmedUserName.size(); i++) {
            refConfirmedUsersInfo = new Firebase(Constants.FIREBASE_URL_USERS).child(confirmedUserName.get(i)).child(Constants.FIREBASE_LOC_USER_INFO);
            //Add the value Event Listener so if data has already been inputted by the user then it will
            //pre-populated with existing data
            getRefConfirmedUsersInfo = refConfirmedUsersInfo.orderByKey();
            getRefConfirmedUsersInfo.addValueEventListener(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // You can use getValue to deserialize the data at dataSnapshot
                    User user = dataSnapshot.getValue(User.class);
                    // If there was no data at the location we added the listener, then
                    if (user != null) {
                        Log.d("Size", String.valueOf(confirmedUserName.size()));
                        Log.d("userObj", user.getFirstName());
                        confirmedUserInfoObjects.add(user);

                    }

                    //RecyclerViewRelationshipAdapter recyclerViewUserAdapter = new RecyclerViewRelationshipAdapter(getActivity(), confirmedUserInfoObjects);
                    initializeView(view);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Log.e(getString(R.string.LogTagUserProfile),
                            getString(R.string.FirebaseOnCancelledError) +
                                    firebaseError.getMessage());
                }
            });
        }
    }

    */
}
