package roast.app.com.dealbreaker.fragments;

import android.app.DownloadManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.firebase.client.Query;

import java.util.ArrayList;


import roast.app.com.dealbreaker.R;
import roast.app.com.dealbreaker.models.PendingRelationshipAttribute;
import roast.app.com.dealbreaker.models.PendingRelationshipViewHolder;
import roast.app.com.dealbreaker.models.User;
import roast.app.com.dealbreaker.util.Constants;


//This class will be used to Show the status of the Pending Relationships
// so it will show other users that have added the current user , allowing
//the user to bypass the queue to someone who has directly expressed interest in them
//It may also be used to track if your requests have been denied or not sure yet

//May also be using a List or Recycler view from the Firebase-UI: https://github.com/firebase/FirebaseUI-Android
//https://www.learn2crack.com/2016/02/custom-swipe-recyclerview.html
// http://stackoverflow.com/questions/24885223/why-doesnt-recyclerview-have-onitemclicklistener-and-how-recyclerview-is-dif/24933117#24933117
public class PendingRelationships extends Fragment {

    FirebaseRecyclerAdapter<PendingRelationshipAttribute,PendingRelationshipViewHolder> pendingRecyclerAdapter;

    private RecyclerView recycViewPending;
    private String userName;
    private ArrayList<String> PendingUsername;
    private Query refPendingUser, getPendingUsersInfo;
    private ArrayList<User> pendingUserObjects;
    private View view;
    private Firebase refPendingUsersInfo;

    public PendingRelationships() {
        // Required empty public constructor
    }

    public static PendingRelationships newInstance(String username) {
        PendingRelationships fragment = new PendingRelationships();
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
        View view = inflater.inflate(R.layout.fragment_pending_relationships, container, false);

        LinearLayoutManager RLM = new LinearLayoutManager(getActivity());
        RLM.setOrientation(LinearLayoutManager.VERTICAL);
        Firebase firebaseREF = new Firebase(Constants.FIREBASE_URL_PENDING).child(userName);

        getPendingUsersInfo = firebaseREF.orderByChild("lastName");
        recycViewPending = (RecyclerView) view.findViewById(R.id.pending_relationship_recycler_view);
        recycViewPending.setHasFixedSize(true);
        recycViewPending.setLayoutManager(RLM);

        //recyclerPage(view);
        //listenerFunc();

        return view;
    }

    private void recyclerPage(View rootview){
        pendingRecyclerAdapter = new FirebaseRecyclerAdapter<PendingRelationshipAttribute, PendingRelationshipViewHolder>(
                PendingRelationshipAttribute.class,
                R.layout.item_pending_relationship,
                PendingRelationshipViewHolder.class,
                getPendingUsersInfo
        ) {
            @Override
            protected void populateViewHolder(PendingRelationshipViewHolder pendingRelationshipViewHolder, PendingRelationshipAttribute PRA, int i) {
                pendingRelationshipViewHolder.name.setText(PRA.getFirstName());
                pendingRelationshipViewHolder.attribute.setText(PRA.getSex());
            }
        };
        recycViewPending.setAdapter(pendingRecyclerAdapter);
    }


    private void listenerFunc(){
        Firebase firebaseREF = new Firebase(Constants.FIREBASE_URL_PENDING).child(userName);
        PendingUsername = new ArrayList<>();

        refPendingUser = firebaseREF.orderByKey();
        refPendingUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childUsers : dataSnapshot.getChildren()) {

                    String name = childUsers.getKey();
                    if (name != null) {
                        PendingUsername.add(name);
                    }
                }
                if (PendingUsername.size() > 0) {
                    getUserInfoPending(); //calls func getUserInfoPending to retrieve info
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }

    private void getUserInfoPending() {
        pendingUserObjects = new ArrayList<>();
        for (int i = 0; i < PendingUsername.size(); i++) {
            refPendingUsersInfo = new Firebase(Constants.FIREBASE_URL_USERS).child(PendingUsername.get(i)).child(Constants.FIREBASE_LOC_USER_INFO);
            getPendingUsersInfo = refPendingUsersInfo.orderByKey();
            getPendingUsersInfo.addValueEventListener(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        Log.d("Size", String.valueOf(PendingUsername.size()));
                        Log.d("userObj", user.getFirstName());
                        pendingUserObjects.add(user);

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
    public void onDestroy(){
        super.onDestroy();
        pendingRecyclerAdapter.cleanup();
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

}
