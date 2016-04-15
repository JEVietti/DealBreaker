package roast.app.com.dealbreaker.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.firebase.ui.FirebaseListAdapter;

import roast.app.com.dealbreaker.R;

//This class will show all of the users that have been accepted by both parties
//From their they can do other things hopefully in the future like chat and
//view each others photos

//This should be displayed in a list or recycler view so we are going to need to use an adapter
//luckily one is supplied by the Firebase UI that has been added to the build.gradle
//see an example: https://github.com/firebase/FirebaseUI-Android
//                http://stackoverflow.com/questions/36143837/firebase-recyclerview-not-able-to-query


public class ConfirmedRelationships extends Fragment {
    private static final String USER_NAME = "userName";

    private String userName;
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
        return inflater.inflate(R.layout.fragment_confirmed_relationship, container, false);
    }

    private void initializeView(){

    }

    private void listenRelationships(){

    }

}
