package roast.app.com.dealbreaker.models;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


public class MovingUsers {
    private Firebase mNewLocation, mOldLocation;
    private String mUsername;

    public MovingUsers(){
    }

    // This function takes two Firebase locations: the current location (oldLocation) that the value
    // will be removed from, and the new location (newLocation) to which the value will go to. It
    // will also take the username of the user that will be moving from one branch to the other.
    public void moveUser(Firebase newLocation, Firebase oldLocation, String username){
        // Assigns the passed values to the activities' members.
        mNewLocation = newLocation;
        mOldLocation = oldLocation;
        mUsername = username;

        // Places the user to it new branch location.
        mNewLocation.child(mUsername).child("mark").setValue(0);

        // Removes the user from it's previous location.
        mOldLocation.child(mUsername).removeValue();
    }
}
