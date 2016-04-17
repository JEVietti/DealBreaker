package roast.app.com.dealbreaker.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.FirebaseListAdapter;
import com.firebase.ui.FirebaseRecyclerAdapter;

import roast.app.com.dealbreaker.R;
import roast.app.com.dealbreaker.models.PendingRelationshipViewHolder;
import roast.app.com.dealbreaker.models.User;

//This class will be used to Show the status of the Pending Relationships
// so it will show other users that have added the current user , allowing
//the user to bypass the queue to someone who has directly expressed interest in them
//It may also be used to track if your requests have been denied or not sure yet

//May also be using a List or Recycler view from the Firebase-UI: https://github.com/firebase/FirebaseUI-Android
//https://www.learn2crack.com/2016/02/custom-swipe-recyclerview.html
// http://stackoverflow.com/questions/24885223/why-doesnt-recyclerview-have-onitemclicklistener-and-how-recyclerview-is-dif/24933117#24933117
public class PendingRelationships extends Fragment {
    FirebaseRecyclerAdapter<User,PendingRelationshipViewHolder> pendingRecyclerAdapter;

    public PendingRelationships() {
        // Required empty public constructor
    }


    public static PendingRelationships newInstance(String username) {
        PendingRelationships fragment = new PendingRelationships();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pending_relationships, container, false);
    }
}
