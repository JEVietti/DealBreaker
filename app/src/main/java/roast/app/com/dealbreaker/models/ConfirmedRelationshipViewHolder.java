package roast.app.com.dealbreaker.models;


import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import roast.app.com.dealbreaker.R;

//Uses the layout item_confirmed_relationship

public class ConfirmedRelationshipViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    public ImageView userImage;
    public TextView userFist_LastName;
    public TextView userAttribute;
    private static ClickListener clickListener;
    public ConfirmedRelationshipViewHolder(View itemView) {
        super(itemView);
        initializeItem(itemView);
    }

    private void initializeItem(View itemView) {
        userImage = (ImageView) itemView.findViewById(R.id.confirmedUserImage);
        userFist_LastName = (TextView) itemView.findViewById(R.id.confirmedUserName);
        userAttribute = (TextView) itemView.findViewById(R.id.confirmedUserAttribute);
    }

    @Override
    public void onClick(View v) {
        clickListener.onItemClick(getAdapterPosition(),v);
    }

    @Override
    public boolean onLongClick(View v) {
        clickListener.onItemLongClick(getAdapterPosition(),v);
        return false;
    }

    public interface ClickListener {
        void onItemClick(int pos, View view);
        void onItemLongClick(int pos, View view);
    }
}