package roast.app.com.dealbreaker.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import roast.app.com.dealbreaker.R;
import roast.app.com.dealbreaker.RoamingActivity;
import roast.app.com.dealbreaker.models.UpdatingQueueBranch;
import roast.app.com.dealbreaker.models.UpdatingViewingBranch;


public class InitialRoaming extends Fragment {

    private String userName;
    private TextView roamingTitle;
    private Button beginRoaming;
    private UpdatingViewingBranch viewingBranch;

    public static InitialRoaming newInstance(String param1) {
        InitialRoaming fragment = new InitialRoaming();
        Bundle args = new Bundle();
        args.putString("userName", param1);
        fragment.setArguments(args);
        return fragment;
    }

    public InitialRoaming() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userName = getArguments().getString("userName");
        }

        UpdatingQueueBranch queueBranch = new UpdatingQueueBranch(userName);
        queueBranch.updateQueue();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_initial_roaming, container, false);
        initializeView(view);
        return view;
    }

    private void initializeView(View rootView){
        beginRoaming = (Button) rootView.findViewById(R.id.beginRoamingButton);
        beginRoaming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewingBranch = new UpdatingViewingBranch(userName);
                viewingBranch.updateView();

                Intent intent = new Intent(getActivity(),RoamingRelationships.class);
                intent.putExtra("userName", userName);
                startActivity(intent);
            }
        });
    }

}
