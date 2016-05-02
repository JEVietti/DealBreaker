package roast.app.com.dealbreaker.fragments;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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



import roast.app.com.dealbreaker.RoamingProfile;
import roast.app.com.dealbreaker.R;
import roast.app.com.dealbreaker.models.RelationshipAttribute;
import roast.app.com.dealbreaker.models.RoamingViewHolder;
import roast.app.com.dealbreaker.models.RelationshipAttribute;
import roast.app.com.dealbreaker.models.User;
import roast.app.com.dealbreaker.models.UserImages;
import roast.app.com.dealbreaker.util.Constants;
import roast.app.com.dealbreaker.util.DownloadImages;


//This class will be used to Show the status of the Roaming Relationships
// so it will show other users that have added the current user , allowing
//the user to bypass the queue to someone who has directly expressed interest in them
//It may also be used to track if your requests have been denied or not sure yet

//May also be using a List or Recycler view from the Firebase-UI: https://github.com/firebase/FirebaseUI-Android
//https://www.learn2crack.com/2016/02/custom-swipe-recyclerview.html
// http://stackoverflow.com/questions/24885223/why-doesnt-recyclerview-have-onitemclicklistener-and-how-recyclerview-is-dif/24933117#24933117
public class RoamingRelationships extends Fragment {

    FirebaseRecyclerAdapter<RelationshipAttribute, RoamingViewHolder> roamingRecyclerAdapter;

    private RecyclerView recycViewRoaming;
    private String userName;
    private ArrayList<String> RoamingUsername;
    private Query refRoamingUser, getRoamingUsersInfo;
    private ArrayList<User> roamingUserObjects;
    private View view;
    private Firebase refRoamingUsersInfo, roamingFirebaseREF;
    private Firebase refPending, refSelectedPending, removeREFRoaming, removeREFSelectedRoaming;
    private ValueEventListener removeRefRoamingListener;

    public RoamingRelationships() {
        // Required empty public constructor
    }

    public static RoamingRelationships newInstance(String username) {
        RoamingRelationships fragment = new RoamingRelationships();
        Bundle args = new Bundle();
        args.putString("userName", username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userName = getArguments().getString("userName");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_roaming_relationships, container, false);

        recyclerPage(view);
        listenerFunc();

        return view;
    }

    private void recyclerPage(View rootView) {
        roamingFirebaseREF = new Firebase(Constants.FIREBASE_URL_QUEUE).child(userName);
        //LinearLayoutManager RLM = new LinearLayoutManager(getActivity());
        //RLM.setOrientation(LinearLayoutManager.VERTICAL);

        getRoamingUsersInfo = roamingFirebaseREF.orderByChild("lastName");
        recycViewRoaming = (RecyclerView) rootView.findViewById(R.id.roaming_relationship_recycler);

        recycViewRoaming.setHasFixedSize(true);
        recycViewRoaming.setLayoutManager(new LinearLayoutManager(getContext()));

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
                    DownloadImages imageDownload = new DownloadImages(roamingRelationshipViewHolder.imageView, getActivity());
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
                                refPending = new Firebase(Constants.FIREBASE_URL_PENDING).child(userName).child(key);
                                refSelectedPending = new Firebase(Constants.FIREBASE_URL_PENDING).child(key).child(userName);
                                removeREFRoaming = new Firebase(Constants.FIREBASE_URL_QUEUE).child(userName).child(key);
                                removeREFSelectedRoaming = new Firebase(Constants.FIREBASE_URL_QUEUE).child(key).child(userName);
                                removeRefRoamingListener = removeREFSelectedRoaming.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        RelationshipAttribute userRA = dataSnapshot.getValue(RelationshipAttribute.class);
                                        if (userRA != null) {
                                            userRA.setMark(1);
                                            refSelectedPending.setValue(userRA);
                                            PRA.setMark(1);
                                            refPending.setValue(PRA);
                                            removeREFRoaming.removeValue();
                                            removeREFSelectedRoaming.removeValue();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                });

                                //Delete from roaming


                                removeREFRoaming.removeValue();

                            }
                        });
                    }

                    //On Click Listener for bringing up User's Profile in RoamingUserProfileActivity
                    roamingRelationshipViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent selectedProfile = new Intent(getActivity(), RoamingProfile.class);
                            selectedProfile.putExtra("userName", roamingRecyclerAdapter.getRef(i).getKey());
                            selectedProfile.putExtra("rootUser", userName);
                            startActivity(selectedProfile);
                        }
                    });
                }
            };
            recycViewRoaming.setAdapter(roamingRecyclerAdapter);
        }
    }

    private void listenerFunc() {
        Firebase firebaseREF = new Firebase(Constants.FIREBASE_URL_QUEUE).child(userName);
        RoamingUsername = new ArrayList<>();

        refRoamingUser = firebaseREF.orderByKey();
        refRoamingUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                recyclerPage(view);
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

    private void getRoamingUserRelationshipInfo() {

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

                    recyclerPage(view);
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
        recyclerPage(view);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (removeRefRoamingListener != null){
            removeREFSelectedRoaming.removeEventListener(removeRefRoamingListener);
       }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (removeRefRoamingListener != null) {
            removeREFSelectedRoaming.removeEventListener(removeRefRoamingListener);
        }
    }

    public void listener(Firebase firebaseREF)
    {
        //class list users  -- list of users
        final ArrayList<User> users = new ArrayList<>();

        firebaseREF.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot user: dataSnapshot.getChildren()){
                        for(DataSnapshot programSnapshot :user.getChildren()) {
                            final User us = programSnapshot.getValue(User.class);
                            users.add(us);
                        }
                    }
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    /* This allows for the Images updated by the listed user at any time to be updated directly to the roaming list
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

    /* This allows for any pertinent User information updated by the listed user at any time to be updated directly to the roaming list
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
