package roast.app.com.dealbreaker.fragments;

import android.app.DownloadManager;
import android.content.Context;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseListAdapter;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.firebase.client.Query;

import java.util.List;

import roast.app.com.dealbreaker.R;
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

    FirebaseRecyclerAdapter<User,PendingRelationshipViewHolder> pendingRecyclerAdapter;

    Firebase firebaseREF = new Firebase(Constants.FIREBASE_URL_PENDING).child("password");
    private RecyclerView recycViewPending;

    private String username;

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
            String key = getString(R.string.key_UserName);
            username = getArguments().getString(key);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pending_relationships, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState ){
        super.onViewCreated(view, savedInstanceState);
        recycViewPending = (RecyclerView) view.findViewById(R.id.pending_relationship_recycler_view);
        final LinearLayoutManager laym = new LinearLayoutManager(getActivity());
        laym.setOrientation(LinearLayoutManager.VERTICAL);
        recycViewPending.setLayoutManager(laym);
        recyclerPage();

        //recycViewPending.setAdapter(pendingRecyclerAdapter);
    }

    private void recyclerPage(){

        pendingRecyclerAdapter = new FirebaseRecyclerAdapter<User, PendingRelationshipViewHolder>(
                User.class,
                R.layout.item_pending_relationship,
                PendingRelationshipViewHolder.class,
                firebaseREF
        ) {
            @Override
            protected void populateViewHolder(PendingRelationshipViewHolder pendingRelationshipViewHolder, User user, int i) {

                pendingRelationshipViewHolder.name.setText(user.getFirstName());
                pendingRelationshipViewHolder.attribute.setText(user.getSex());

            }
        };
        recycViewPending.setAdapter(pendingRecyclerAdapter);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        pendingRecyclerAdapter.cleanup();
    }

    //class list users  -- list of users
    public class listUsers{
        public List<User> users;
    }

    /*public void populateList()
    {
        firebaseREF.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot user: dataSnapshot.getChildren()){
                        for(DataSnapshot programSnapshot :user.getChildren()){
                            final listUsers users = programSnapshot.getValue(listUsers.class);

                        }
                    }
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }*/

}
