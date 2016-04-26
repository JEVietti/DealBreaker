package roast.app.com.dealbreaker.models;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import roast.app.com.dealbreaker.R;

//Uses the layout item_confirmed_relationship

public class ConfirmedRelationshipViewHolder extends RecyclerView.ViewHolder {
    public ImageView userImage;
    public TextView userFist_LastName;
    public TextView userAttribute;

    public ConfirmedRelationshipViewHolder(View itemView) {
        super(itemView);
        initializeItem(itemView);
    }

    private void initializeItem(View itemView) {
        userImage = (ImageView) itemView.findViewById(R.id.confirmedUserImage);
        userFist_LastName = (TextView) itemView.findViewById(R.id.confirmedUserName);
        userAttribute = (TextView) itemView.findViewById(R.id.confirmedUserAttribute);
    }
}