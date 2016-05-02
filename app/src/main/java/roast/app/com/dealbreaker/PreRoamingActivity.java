package roast.app.com.dealbreaker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Enumeration;
import java.util.Hashtable;

import roast.app.com.dealbreaker.util.Constants;

public class PreRoamingActivity extends AppCompatActivity {
    public String mGenderWanted, mSexualOrientationWanted, mAgeWanted;
    public Hashtable mRoamingUsers;





    String username = "bit";
    String mLocation = "Fresno, California, United States";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRoamingUsers = new Hashtable();

        grabInfo();
        //grabUsersFromList();
        //checkPendingQueue();

    }


    public void grabInfo(){
        Firebase roamingInfo = new Firebase(Constants.FIREBASE_URL_ROAMING).child(username);

        roamingInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mAgeWanted = dataSnapshot.child("age").getValue().toString();
                mGenderWanted = dataSnapshot.child("sex").getValue().toString();
                mSexualOrientationWanted = dataSnapshot.child("sexualOrientation").getValue().toString();
                grabUsersFromList();
                checkQueue();
                checkPending();
                checkConfirmed();
                checkRejected();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }

    public void grabUsersFromList(){
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
                int index = 0;
                for(DataSnapshot currentSnapShot : dataSnapshot.getChildren()){
                    mRoamingUsers.put(currentSnapShot.getKey().toString(), currentSnapShot.getKey().toString());
                    index++;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void checkQueue(){
        Firebase queueURL = new Firebase(Constants.FIREBASE_URL + "queue").child(username);

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
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void checkPending(){
        Firebase queueURL = new Firebase(Constants.FIREBASE_URL + "pending").child(username);

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
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void checkConfirmed(){
        Firebase queueURL = new Firebase(Constants.FIREBASE_URL + "confirmed").child(username);

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
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void checkRejected(){
        Firebase queueURL = new Firebase(Constants.FIREBASE_URL + "rejected").child(username);

        queueURL.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("rejected");
                for (DataSnapshot currentSnapShot : dataSnapshot.getChildren()){
                    String tempUser = currentSnapShot.getKey().toString();
                    boolean isPresent = mRoamingUsers.containsKey(tempUser);
                    if(isPresent){
                        mRoamingUsers.remove(tempUser);
                    }
                }

                setData();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void setData(){
        Firebase queueURL = new Firebase(Constants.FIREBASE_URL + "queue").child(username);

        System.out.println("set");

        Enumeration key = mRoamingUsers.keys();

        while(key.hasMoreElements()){
            String tempUser = key.nextElement().toString();
            queueURL.child(tempUser).child("mark").setValue(0);
        }

        Intent intent = new Intent (PreRoamingActivity.this, RoamingActivity.class);
        intent.putExtra(getString(R.string.key_UserName), username);
        startActivity(intent);

    }
}
