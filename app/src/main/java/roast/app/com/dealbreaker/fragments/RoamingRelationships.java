package roast.app.com.dealbreaker.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.firebase.client.Query;

import java.util.ArrayList;
import java.util.Hashtable;


import roast.app.com.dealbreaker.RoamingProfile;
import roast.app.com.dealbreaker.R;
import roast.app.com.dealbreaker.models.RelationshipAttribute;
import roast.app.com.dealbreaker.models.RoamingViewHolder;
import roast.app.com.dealbreaker.models.User;
import roast.app.com.dealbreaker.models.UserImages;
import roast.app.com.dealbreaker.util.Constants;
import roast.app.com.dealbreaker.util.DownloadImages;
import roast.app.com.dealbreaker.util.PreRoaming;


//This class will be used to Show the status of the Roaming Relationships
// so it will show other users that have added the current user , allowing
//the user to bypass the queue to someone who has directly expressed interest in them
//It may also be used to track if your requests have been denied or not sure yet

//May also be using a List or Recycler view from the Firebase-UI: https://github.com/firebase/FirebaseUI-Android
//https://www.learn2crack.com/2016/02/custom-swipe-recyclerview.html
// http://stackoverflow.com/questions/24885223/why-doesnt-recyclerview-have-onitemclicklistener-and-how-recyclerview-is-dif/24933117#24933117
public class RoamingRelationships extends AppCompatActivity {

    FirebaseRecyclerAdapter<RelationshipAttribute, RoamingViewHolder> roamingRecyclerAdapter;

    private RecyclerView recyclerViewRoaming;
    private String userName;
    private ArrayList<String> RoamingUsername;
    private Query refRoamingUser, getRoamingUsersInfo;
    private ArrayList<User> roamingUserObjects;
    private View view;
    private Firebase refRoamingUsersInfo, roamingFirebaseREF;
    private Firebase refPending, refSelectedPending, removeREFRoaming, removeREFSelectedRoaming;
    private Firebase refRejected, refSelectedRejected;
    private ValueEventListener removeREFRoamingListener, removeREFSelectedRoamingListener;
    private Firebase refQueue, refViewing;
    public Hashtable mRoamingUsers;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_roaming_relationships);
       // initializeView();
        if(getIntent().getExtras() != null) {
            Bundle arg = getIntent().getExtras();
            userName = arg.getString(getString(R.string.key_UserName));
            PreRoaming preRoaming = new PreRoaming(userName);
            preRoaming.grabInfo();
            recyclerPage();
            listenerFunc();

        }
    }


    private void recyclerPage() {
        roamingFirebaseREF = new Firebase(Constants.FIREBASE_URL_VIEWING_QUEUE).child(userName);
        //LinearLayoutManager RLM = new LinearLayoutManager(getActivity());
        //RLM.setOrientation(LinearLayoutManager.VERTICAL);

        getRoamingUsersInfo = roamingFirebaseREF.orderByChild("lastName");
        recyclerViewRoaming = (RecyclerView) findViewById(R.id.roaming_relationship_recycler);

        recyclerViewRoaming.setHasFixedSize(true);
        recyclerViewRoaming.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        if (getRoamingUsersInfo != null) {
            roamingRecyclerAdapter = new FirebaseRecyclerAdapter<RelationshipAttribute, RoamingViewHolder>(
                    RelationshipAttribute.class,
                    R.layout.item_roaming,
                    RoamingViewHolder.class,
                    getRoamingUsersInfo
            ) {
                @Override
                protected void populateViewHolder(RoamingViewHolder roamingRelationshipViewHolder, final RelationshipAttribute PRA, final int i) {
                    String listedUserName = roamingRecyclerAdapter.getRef(i).getKey();
                    currentProfilePic(listedUserName);
                    updateUserInfo(listedUserName);
                    DownloadImages imageDownload = new DownloadImages(roamingRelationshipViewHolder.imageView, RoamingRelationships.this);
                    imageDownload.execute(PRA.getProfilePic());
                    roamingRelationshipViewHolder.name.setText(PRA.getFirstName() + " " + PRA.getLastName() + "      Age: " + PRA.getAge());
                    roamingRelationshipViewHolder.attribute.setText(PRA.getLocation() + "   SO:" + PRA.getSexualOrientation());
                    //Send a user a request and add them to your pending list, and yourself to their pending list
                    if (PRA.getMark() == 0) {
                        roamingRelationshipViewHolder.request.setText("Request");
                        roamingRelationshipViewHolder.request.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String key = roamingRecyclerAdapter.getRef(i).getKey();
                                //Add to pending list
                                addToPending(key);
                            }
                        });
                    }

                    //On Click Listener for bringing up User's Profile in RoamingUserProfileActivity
                    roamingRelationshipViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent selectedProfile = new Intent(RoamingRelationships.this, RoamingProfile.class);
                            selectedProfile.putExtra("userName", roamingRecyclerAdapter.getRef(i).getKey());
                            selectedProfile.putExtra("rootUser", userName);
                            startActivity(selectedProfile);
                        }
                    });
                }
            };
            recyclerViewRoaming.setAdapter(roamingRecyclerAdapter);
        }
    }

    private void initializeView(){
        //toolbar = (Toolbar) findViewById(R.id.roaming_relationship_toolbar);
        //toolbar.setTitle("Roaming");
    }

    private void addToPending(String key){

        refPending = new Firebase(Constants.FIREBASE_URL_PENDING).child(userName).child(key);
        refSelectedPending = new Firebase(Constants.FIREBASE_URL_PENDING).child(key).child(userName);
        removeREFRoaming = new Firebase(Constants.FIREBASE_URL_VIEWING_QUEUE).child(userName).child(key);


        removeREFRoamingListener = removeREFRoaming.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RelationshipAttribute selectedRA = new RelationshipAttribute();
                RelationshipAttribute rootRA = new RelationshipAttribute();
                selectedRA.setMark(0);
                rootRA.setMark(1);
                refPending.setValue(rootRA);
                refSelectedPending.setValue(selectedRA);
                removeREFRoaming.removeValue();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }

    private void addToRejected(String key){

        refRejected = new Firebase(Constants.FIREBASE_URL_REJECTED).child(userName).child(key);
        refSelectedRejected = new Firebase(Constants.FIREBASE_URL_REJECTED).child(key).child(userName);
        removeREFRoaming = new Firebase(Constants.FIREBASE_URL_VIEWING_QUEUE).child(userName).child(key);


        removeREFRoamingListener = removeREFRoaming.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RelationshipAttribute selectedRA = new RelationshipAttribute();
                RelationshipAttribute rootRA = new RelationshipAttribute();
                selectedRA.setMark(0);
                rootRA.setMark(1);
                refRejected.setValue(rootRA);
                refSelectedRejected.setValue(selectedRA);
                removeREFRoaming.removeValue();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }


    private void listenerFunc() {
        Firebase firebaseREF = new Firebase(Constants.FIREBASE_URL_VIEWING_QUEUE).child(userName);
        RoamingUsername = new ArrayList<>();

        refRoamingUser = firebaseREF.orderByKey();
        refRoamingUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //recyclerPage();
                for (DataSnapshot childUsers : dataSnapshot.getChildren()) {

                    String name = childUsers.getKey();
                    if (name != null) {
                        RoamingUsername.add(name);
                    }
                }
                if (RoamingUsername.size() > 0) {
                    getUserInfoRoaming(); //calls func getUserInfoRoaming to retrieve info
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }



    private void getUserInfoRoaming() {
        roamingUserObjects = new ArrayList<>();
        for (int i = 0; i < RoamingUsername.size(); i++) {
            refRoamingUsersInfo = new Firebase(Constants.FIREBASE_URL_USERS).child(RoamingUsername.get(i)).child(Constants.FIREBASE_LOC_USER_INFO);
            getRoamingUsersInfo = refRoamingUsersInfo.orderByKey();
            getRoamingUsersInfo.addValueEventListener(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        Log.d("Size", String.valueOf(RoamingUsername.size()));
                        Log.d("userObj", user.getFirstName());
                        roamingUserObjects.add(user);

                    }

                    recyclerPage();
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

    @Override
    public void onResume() {
        super.onResume();
        recyclerPage();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (removeREFRoamingListener != null && removeREFSelectedRoamingListener != null) {
            removeREFSelectedRoaming.removeEventListener(removeREFRoamingListener);
            removeREFSelectedRoaming.removeEventListener(removeREFSelectedRoamingListener);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (removeREFRoamingListener != null && removeREFSelectedRoamingListener != null) {
            removeREFSelectedRoaming.removeEventListener(removeREFRoamingListener);
            removeREFSelectedRoaming.removeEventListener(removeREFSelectedRoamingListener);
        }
    }

    /* This allows for the Images updated by the listed user at any time to be updated directly to the pending list
  * */
    private void currentProfilePic(final String listedUser){
        Firebase listedUserImagesREF = new Firebase(Constants.FIREBASE_URL_IMAGES).child(listedUser).child(Constants.FIREBASE_LOC_PROFILE_PIC);
        listedUserImagesREF.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserImages userImages = dataSnapshot.getValue(UserImages.class);
                if (userImages != null) {
                    String profilePic = userImages.getProfilePic();
                    roamingFirebaseREF.child(listedUser).child("profilePic").setValue(profilePic);
                    //initializeView(view);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    /* This allows for any pertinent User information updated by the listed user at any time to be updated directly to the pending list
    */
    private void updateUserInfo(final String listedUser){
        Firebase listedUserInfo = new Firebase(Constants.FIREBASE_URL_USERS).child(listedUser).child(Constants.FIREBASE_LOC_USER_INFO);
        listedUserInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    String firstName = user.getFirstName();
                    String lastName = user.getLastName();
                    Long age = user.getAge();
                    String sex = user.getSex();
                    String location = user.getLocation();
                    String sexualOrientation = user.getSexualOrientation();

                    roamingFirebaseREF.child(listedUser).child("firstName").setValue(firstName);
                    roamingFirebaseREF.child(listedUser).child("lastName").setValue(lastName);
                    roamingFirebaseREF.child(listedUser).child("age").setValue(age);
                    roamingFirebaseREF.child(listedUser).child("sex").setValue(sex);
                    roamingFirebaseREF.child(listedUser).child("location").setValue(location);
                    roamingFirebaseREF.child(listedUser).child("sexualOrientation").setValue(sexualOrientation);
                    //initializeView(view);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
