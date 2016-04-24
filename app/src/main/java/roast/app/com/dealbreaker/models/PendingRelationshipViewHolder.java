package roast.app.com.dealbreaker.models;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import roast.app.com.dealbreaker.R;

//Uses the layout item_pending_relationship

public class PendingRelationshipViewHolder extends RecyclerView.ViewHolder{

    public TextView name, attribute;
    public Button add;

    public PendingRelationshipViewHolder(View itemView){
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.nameLinePending);
        attribute = (TextView) itemView.findViewById(R.id.attributeLinePending);
        add = (Button) itemView.findViewById(R.id.addUserButton);
    }

}
