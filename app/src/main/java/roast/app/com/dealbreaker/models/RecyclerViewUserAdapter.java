package roast.app.com.dealbreaker.models;


import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;

//Use this instead of the FireBase Recycler View Adapter if needing to go into a nest place
//in the database from one to another reference for instance getting a username in confirmed and
//getting the info for it in the UserInfo section
public class RecyclerViewUserAdapter extends RecyclerView.Adapter<ConfirmedRelationshipViewHolder> {



    public RecyclerViewUserAdapter(Activity context, ArrayList<RelationshipAttribute> userInfoArrayList){

    }

    @Override
    public ConfirmedRelationshipViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ConfirmedRelationshipViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
