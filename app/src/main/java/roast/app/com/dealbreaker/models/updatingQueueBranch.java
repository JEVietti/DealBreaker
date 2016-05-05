package roast.app.com.dealbreaker.models;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Enumeration;
import java.util.Hashtable;

import roast.app.com.dealbreaker.R;
import roast.app.com.dealbreaker.RoamingActivity;
import roast.app.com.dealbreaker.util.Constants;

public class UpdatingQueueBranch {

    public String mGenderWanted, mSexualOrientationWanted, mAgeWanted, mLocation;
    public Hashtable mRoamingUsers;
    String mUsername;

    // Constructor takes the user name, assigns it to the activity member, and initializes the hash table.
    public UpdatingQueueBranch(String username){
        mRoamingUsers = new Hashtable();
        mUsername = username;
        updateQueue();
    }

    // Updates the queue
    public void updateQueue(){
        grabInfo();
    }

    // Grabs the user roaming wants and assigns them to the activity members.
    private void grabInfo(){
        Firebase roamingInfo = new Firebase(Constants.FIREBASE_URL_ROAMING).child(mUsername);

        roamingInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mAgeWanted = dataSnapshot.child("age").getValue().toString();
                mGenderWanted = dataSnapshot.child("sex").getValue().toString();
                mSexualOrientationWanted = dataSnapshot.child("sexualOrientation").getValue().toString();
                grabLocation(); // Grab's the location found in the user info branch.
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    // Grabs the location from the user info branch.
    private void grabLocation(){
        Firebase userLoc = new Firebase(Constants.FIREBASE_URL_USERS).child(mUsername).child(Constants.FIREBASE_LOC_USER_INFO).child("location");

        userLoc.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mLocation = dataSnapshot.getValue().toString();
                setListener();      // Set's the child event listener.
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    // Child event listener. This will listen to the specific roaming branch for children being added. When a child is added, this listener will be
    // triggered and the onChildAdded function will be ran. This will update the user's queue with the new added child.
    private void setListener(){
        Firebase roamingURL = new Firebase(Constants.FIREBASE_URL + "roamingList").child(mLocation).child(mGenderWanted).child(mSexualOrientationWanted);

        int userAge = Integer.parseInt(mAgeWanted);

        // If statements to check which branch the user will fall under.
        if(userAge <= 20){
            roamingURL = roamingURL.child("18-20");
        }
        else if(userAge >= 21 && userAge <= 29){
            roamingURL = roamingURL.child("21-29");
        }
        else if(userAge >= 30 && userAge <= 39){
            roamingURL = roamingURL.child("30-39");
        }
        else if(userAge >= 40 && userAge <= 49){
            roamingURL = roamingURL.child("40-49");
        }
        else if(userAge >= 50 && userAge <= 59){
            roamingURL = roamingURL.child("50-59");
        }
        else {
            roamingURL = roamingURL.child("60+");
        }

        roamingURL.addChildEventListener(new ChildEventListener() {
            @Override
            // To be ran when a new user is added to the specific branch.
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                grabUsersFromList();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    // Grab all of the users from the corresponding roamingList branch.
    private void grabUsersFromList(){
        Firebase roamingURL = new Firebase(Constants.FIREBASE_URL + "roamingList").child(mLocation).child(mGenderWanted).child(mSexualOrientationWanted);

        int userAge = Integer.parseInt(mAgeWanted);

        // If statements to check which branch the user will fall under.
        if(userAge <= 20){
            roamingURL = roamingURL.child("18-20");
        }
        else if(userAge >= 21 && userAge <= 29){
            roamingURL = roamingURL.child("21-29");
        }
        else if(userAge >= 30 && userAge <= 39){
            roamingURL = roamingURL.child("30-39");
        }
        else if(userAge >= 40 && userAge <= 49){
            roamingURL = roamingURL.child("40-49");
        }
        else if(userAge >= 50 && userAge <= 59){
            roamingURL = roamingURL.child("50-59");
        }
        else {
            roamingURL = roamingURL.child("60+");
        }

        roamingURL.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // The following turns the hashTable into a vector. The key's of the values are indexes.
                int index = 0;
                for(DataSnapshot currentSnapShot : dataSnapshot.getChildren()){
                    mRoamingUsers.put(currentSnapShot.getKey().toString(), currentSnapShot.getKey().toString());
                    index++;
                }

                checkQueue();

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    // Check to see if the users from the roamingList branch are present in the queue branch. If so, remove them from the hashTable.
    private void checkQueue(){
        Firebase queueURL = new Firebase(Constants.FIREBASE_URL + "queue").child(mUsername);

        queueURL.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot currentSnapShot : dataSnapshot.getChildren()){
                    String tempUser = currentSnapShot.getKey().toString();
                    boolean isPresent = mRoamingUsers.containsKey(tempUser);
                    if(isPresent){
                        mRoamingUsers.remove(tempUser);
                    }
                }

                checkPending();

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    // Check to see if the users from the roamingList branch are present in the pending branch. If so, remove them from the hashTable.
    private void checkPending(){
        Firebase queueURL = new Firebase(Constants.FIREBASE_URL + "pending").child(mUsername);

        queueURL.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot currentSnapShot : dataSnapshot.getChildren()){
                    String tempUser = currentSnapShot.getKey().toString();
                    boolean isPresent = mRoamingUsers.containsKey(tempUser);
                    if(isPresent){
                        mRoamingUsers.remove(tempUser);
                    }
                }

                checkConfirmed();

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    // Check to see if the users from the roamingList branch are present in the confirmed branch. If so, remove them from the hashTable.
    private void checkConfirmed(){
        Firebase queueURL = new Firebase(Constants.FIREBASE_URL + "confirmed").child(mUsername);

        queueURL.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot currentSnapShot : dataSnapshot.getChildren()){
                    String tempUser = currentSnapShot.getKey().toString();
                    boolean isPresent = mRoamingUsers.containsKey(tempUser);
                    if(isPresent){
                        mRoamingUsers.remove(tempUser);
                    }
                }

                checkRejected();

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    // Check to see if the users from the roamingList branch are present in the rejected branch. If so, remove them from the hashTable.
    private void checkRejected(){
        Firebase queueURL = new Firebase(Constants.FIREBASE_URL + "rejected").child(mUsername);

        queueURL.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot currentSnapShot : dataSnapshot.getChildren()){
                    String tempUser = currentSnapShot.getKey().toString();
                    boolean isPresent = mRoamingUsers.containsKey(tempUser);
                    if(isPresent){
                        mRoamingUsers.remove(tempUser);
                    }
                }

                checkViewing();

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    // Check to see if the users from the roamingList branch are present in the viewing branch. If so, remove them from the hashTable.
    private void checkViewing(){
        Firebase queueURL = new Firebase(Constants.FIREBASE_URL + "viewing").child(mUsername);

        queueURL.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot currentSnapShot : dataSnapshot.getChildren()){
                    String tempUser = currentSnapShot.getKey().toString();
                    boolean isPresent = mRoamingUsers.containsKey(tempUser);
                    if(isPresent){
                        mRoamingUsers.remove(tempUser);
                    }
                }

                checkIfSame();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void checkIfSame(){
        boolean isSame = mRoamingUsers.containsKey(mUsername);

        if (isSame){
            mRoamingUsers.remove(mUsername);
        }

        setData();
    }

    // Sets the remaining users, the ones that were not present in all of the other branches, to the user's queue branch.
    private void setData(){
        Firebase queueURL = new Firebase(Constants.FIREBASE_URL + "queue").child(mUsername);

        Enumeration key = mRoamingUsers.keys();

        while(key.hasMoreElements()){
            String tempUser = key.nextElement().toString();
            queueURL.child(tempUser).child("mark").setValue(0);
        }
    }
}
