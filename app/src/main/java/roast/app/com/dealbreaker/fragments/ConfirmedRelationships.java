package roast.app.com.dealbreaker.fragments;

import android.content.Intent;
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
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;

import java.util.ArrayList;

import roast.app.com.dealbreaker.ConfirmedUserProfile;
import roast.app.com.dealbreaker.R;
import roast.app.com.dealbreaker.models.ConfirmedRelationshipViewHolder;
import roast.app.com.dealbreaker.models.RelationshipAttribute;
import roast.app.com.dealbreaker.models.User;
import roast.app.com.dealbreaker.util.Constants;
import roast.app.com.dealbreaker.util.DownloadImages;

//This class will show all of the users that have been accepted by both parties
//From their they can do other things hopefully in the future like chat and
//view each others photos

//This should be displayed in a list or recycler view so we are going to need to use an adapter
//luckily one is supplied by the Firebase UI that has been added to the build.gradle
//see an example: https://github.com/firebase/FirebaseUI-Android
//                http://stackoverflow.com/questions/36143837/firebase-recyclerview-not-able-to-query
//https://www.learn2crack.com/2016/02/custom-swipe-recyclerview.html
// http://stackoverflow.com/questions/24885223/why-doesnt-recyclerview-have-onitemclicklistener-and-how-recyclerview-is-dif/24933117#24933117

public class ConfirmedRelationships extends Fragment {
    private static final String USER_NAME = "userName";
    private FirebaseRecyclerAdapter<RelationshipAttribute, ConfirmedRelationshipViewHolder> recyclerAdapter;
    private String userName;
    private ArrayList<User> confirmedUserInfoObjects;
    private ArrayList<String> confirmedUserName;
    private Firebase refConfirmedUsers;
    private Query getRefConfirmedUsersInfo, getRefConfirmedUser;
    private Firebase refConfirmedUsersInfo;
    private RecyclerView confirmedRelationshipsRecycler;
    private View view;
    public ConfirmedRelationships() {
        // Required empty public constructor
    }

    public static ConfirmedRelationships newInstance(String username) {
        ConfirmedRelationships fragment = new ConfirmedRelationships();
        Bundle args = new Bundle();
        args.putString(USER_NAME, username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userName = getArguments().getString(USER_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_confirmed_relationship, container, false);
        initializeView(view);
        listenRelationships();
        return view;
    }

    //Initialize the Fragments View
    private void initializeView(View rootView) {
        //Add the value Event Listener so if data has already been inputted by the user then it will
        //pre-populated with existing data
        refConfirmedUsers = new Firebase(Constants.FIREBASE_URL_CONFIRMED_RELATIONSHIPS).child(userName);
        //refConfirmedUsers.orderByChild("lastName");
        getRefConfirmedUsersInfo = refConfirmedUsers.orderByChild("lastName");
        confirmedRelationshipsRecycler = (RecyclerView) rootView.findViewById(R.id.confirmed_relationship_recycler_view);
        confirmedRelationshipsRecycler.setHasFixedSize(true);
        confirmedRelationshipsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));


        //Use the Query
        if (getRefConfirmedUsersInfo != null) {
            Log.d("Building Adpater", "true");
            recyclerAdapter = new FirebaseRecyclerAdapter<RelationshipAttribute, ConfirmedRelationshipViewHolder>(RelationshipAttribute.class, R.layout.item_confirmed_relationship, ConfirmedRelationshipViewHolder.class, getRefConfirmedUsersInfo) {
                @Override
                protected void populateViewHolder(ConfirmedRelationshipViewHolder confirmedRelationshipViewHolder, RelationshipAttribute user, final int pos) {
                    DownloadImages downloadImages = new DownloadImages(confirmedRelationshipViewHolder.userImage, getActivity());
                    downloadImages.execute(user.getProfilePic());
                    confirmedRelationshipViewHolder.userFist_LastName.setText(user.getFirstName() + " " + user.getLastName() + "    Age: " + user.getAge());
                    confirmedRelationshipViewHolder.userAttribute.setText(user.getLocation() + "    SO: " + user.getSexualOrientation());
                    confirmedRelationshipViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent selectedProfile = new Intent(getActivity(), ConfirmedUserProfile.class);
                            selectedProfile.putExtra("userName",recyclerAdapter.getRef(pos).getKey());
                            startActivity(selectedProfile);
                        }
                    });
                }
            };
            confirmedRelationshipsRecycler.setAdapter(recyclerAdapter);
        }
    }

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

    //Potential: Swipe Method for the Recycler View
    private void initSwipe(View rootView){

    }

}
