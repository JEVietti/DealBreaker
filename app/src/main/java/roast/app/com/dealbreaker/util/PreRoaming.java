package roast.app.com.dealbreaker.util;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Enumeration;
import java.util.Hashtable;

import roast.app.com.dealbreaker.models.User;
import roast.app.com.dealbreaker.util.Constants;

public class PreRoaming {
    public String mGenderWanted, userName, mSexualOrientationWanted, mAgeWanted, mLocation;
    public Hashtable mRoamingUsers;

    public PreRoaming(String username){
        this.userName = username;
    }

    public void grabInfo(){
        grabRoamingInfo();
    }

    public void grabRoamingInfo(){
        final Firebase roamingInfo = new Firebase(Constants.FIREBASE_URL_ROAMING).child(userName);

        roamingInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User roamingUser = dataSnapshot.getValue(User.class);
                if (roamingUser != null) {
                    mSexualOrientationWanted = roamingUser.getSexualOrientation();
                    mGenderWanted = roamingUser.getSex();
                    mAgeWanted = roamingUser.getAge().toString();
                }
                if(mAgeWanted != null && mSexualOrientationWanted != null && mGenderWanted != null ){
                    getUserLocation();
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    public void getUserLocation (){
        Firebase userInfo = new Firebase(Constants.FIREBASE_URL_USER_INFO).child(userName);
        userInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    mLocation = user.getLocation();
                }
                if(mAgeWanted != null && mSexualOrientationWanted != null && mLocation != null && mGenderWanted != null ) {
                    grabUsersFromList();
                    checkQueue();
                    checkPending();
                    checkConfirmed();
                    checkRejected();
                }
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
        Firebase queueURL = new Firebase(Constants.FIREBASE_URL + "queue").child(userName);

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
        Firebase queueURL = new Firebase(Constants.FIREBASE_URL_PENDING).child(userName);

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
        Firebase queueURL = new Firebase(Constants.FIREBASE_URL_CONFIRMED_RELATIONSHIPS).child(userName);

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
        Firebase queueURL = new Firebase(Constants.FIREBASE_URL_REJECTED).child(userName);

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
        Firebase queueURL = new Firebase(Constants.FIREBASE_URL + "queue").child(userName);
        System.out.println("set");
        Enumeration key = mRoamingUsers.keys();
        while(key.hasMoreElements()){
            String tempUser = key.nextElement().toString();
            queueURL.child(tempUser).child("mark").setValue(0);
        }

    }
}
