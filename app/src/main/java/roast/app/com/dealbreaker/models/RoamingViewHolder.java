package roast.app.com.dealbreaker.models;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import roast.app.com.dealbreaker.R;

public class RoamingViewHolder extends RecyclerView.ViewHolder {

    public TextView name, attribute;
    public Button request;
    public ImageView imageView;

    public RoamingViewHolder(View itemView){
        super(itemView);
        initializeHolder(itemView);

    }

    private void initializeHolder(View item){
        name = (TextView) item.findViewById(R.id.nameLineRoaming);
        attribute = (TextView) item.findViewById(R.id.attributeLineRoaming);
        request = (Button) item.findViewById(R.id.roamingAddUserButton);
        imageView = (ImageView) item.findViewById(R.id.roamingUserImage);
    }
}
