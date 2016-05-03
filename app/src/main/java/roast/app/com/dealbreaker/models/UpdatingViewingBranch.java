package roast.app.com.dealbreaker.models;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import roast.app.com.dealbreaker.util.Constants;

public class UpdatingViewingBranch {

    public String mUsername;
    private int numOfUsers;

    // Constructor takes the user name, assigns it to the activity member. It also sets the child listener.
    public UpdatingViewingBranch(String username){
        mUsername = username;
        setListener();
    }

    // This function set's a listener on the user's viewing branch. When a user is removed, the listener is
    // triggered and a user is moved from the queue list to the view list.
    public void setListener(){
        final Firebase currentlyViewing = new Firebase(Constants.FIREBASE_URL + "viewing").child(mUsername);

        currentlyViewing.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                updateView();
                System.out.println("remove");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    // Function that will update the view branch if it has less than 5 users.
    public void updateView(){
        final Firebase currentlyRoaming = new Firebase(Constants.FIREBASE_URL + "viewing").child(mUsername);

        currentlyRoaming.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                numOfUsers = 0;
                //Keeps track of how many children are present.
                for(DataSnapshot tempSnapShop : dataSnapshot.getChildren()) {
                    numOfUsers++;
                    System.out.println(tempSnapShop.getKey().toString());
                }

                // If less than 5 children are present, then a function is ran to populate the view branch.
                if (numOfUsers < 5){
                    populateCurrentlyBrowsing(currentlyRoaming);
                }
            }



            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    // Populates the view branch until it contains 5 users or there are no more users in the queue.
    public void populateCurrentlyBrowsing(final Firebase currentlyRoaming) {
        final Firebase usersQueue = new Firebase(Constants.FIREBASE_URL + "queue").child(mUsername);

        usersQueue.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MovingUsers move = new MovingUsers();
                for(DataSnapshot tempSnapShot : dataSnapshot.getChildren()){
                    if (numOfUsers >= 5)
                        break;

                    String tempName = tempSnapShot.getKey().toString();
                    move.moveUser(currentlyRoaming, usersQueue, tempName);
                    numOfUsers++;

                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
