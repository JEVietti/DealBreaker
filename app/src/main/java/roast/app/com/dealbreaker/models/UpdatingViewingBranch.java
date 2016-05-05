package roast.app.com.dealbreaker.models;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import roast.app.com.dealbreaker.util.Constants;

public class UpdatingViewingBranch {

    private String mUsername;
    private Firebase currentlyViewing, currentlyRoaming, usersQueue, listedUserInfo, listedUserImagesREF;
    private int numOfUsers;
    private ChildEventListener currentlyViewingListener;
    private ValueEventListener currentlyRoamingListener, usersQueueListener, listedUserInfoListener, listedUserImagesREFListener;

    // Constructor takes the user name, assigns it to the activity member. It also sets the child listener.
    public UpdatingViewingBranch(String username){
        mUsername = username;
        setListener();
    }

    public void removeListener(){
        if(currentlyViewingListener != null){
            currentlyViewing.removeEventListener(currentlyViewingListener);
        }

        if (currentlyRoamingListener != null){
            currentlyRoaming.removeEventListener(currentlyRoamingListener);
        }

        if (usersQueueListener != null){
            usersQueue.removeEventListener(usersQueueListener);
        }

        if (listedUserInfoListener != null){
            listedUserInfo.removeEventListener(listedUserInfoListener);
        }

        if (listedUserImagesREFListener != null){
            listedUserImagesREF.removeEventListener(listedUserImagesREFListener);
        }
    }

    // This function set's a listener on the user's viewing branch. When a user is removed, the listener is
    // triggered and a user is moved from the queue list to the view list.
    public void setListener(){
        currentlyViewing = new Firebase(Constants.FIREBASE_URL + "viewing").child(mUsername);

        currentlyViewingListener= currentlyViewing.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                updateView();
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
        currentlyRoaming = new Firebase(Constants.FIREBASE_URL + "viewing").child(mUsername);

        currentlyRoamingListener = currentlyRoaming.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                numOfUsers = 0;
                //Keeps track of how many children are present.
                for (DataSnapshot tempSnapShop : dataSnapshot.getChildren()) {
                    numOfUsers++;
                    System.out.println(tempSnapShop.getKey().toString());
                }

                // If less than 5 children are present, then a function is ran to populate the view branch.
                if (numOfUsers < 5) {
                    populateCurrentlyBrowsing(currentlyRoaming);
                }
            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    // Populates the view branch until it contains 5 users or there are no more users in the queue.
    private void populateCurrentlyBrowsing(final Firebase currentlyRoaming) {
        usersQueue = new Firebase(Constants.FIREBASE_URL + "queue").child(mUsername);

        usersQueueListener = usersQueue.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MovingUsers move = new MovingUsers();
                for(DataSnapshot tempSnapShot : dataSnapshot.getChildren()){
                    if (numOfUsers >= 5)
                        break;

                    String tempName = tempSnapShot.getKey().toString();
                    move.moveUser(currentlyRoaming, usersQueue, tempName);
                    //updateUserInfo(tempName, currentlyRoaming);
                    //currentProfilePic(tempName, currentlyRoaming);
                    numOfUsers++;

                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void updateUserInfo(final String listedUser, final Firebase userQueue){
        listedUserInfo = new Firebase(Constants.FIREBASE_URL_USERS).child(listedUser).child(Constants.FIREBASE_LOC_USER_INFO);
        listedUserInfoListener = listedUserInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                System.out.println("inside");
                if (user != null) {
                    System.out.println("inside inside");
                    String firstName = user.getFirstName();
                    String lastName = user.getLastName();
                    Long age = user.getAge();
                    String sex = user.getSex();
                    String location = user.getLocation();
                    String sexualOrientation = user.getSexualOrientation();

                    userQueue.child(listedUser).child("firstName").setValue(firstName);
                    userQueue.child(listedUser).child("lastName").setValue(lastName);
                    userQueue.child(listedUser).child("age").setValue(age);
                    userQueue.child(listedUser).child("sex").setValue(sex);
                    userQueue.child(listedUser).child("location").setValue(location);
                    userQueue.child(listedUser).child("sexualOrientation").setValue(sexualOrientation);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void currentProfilePic(final String listedUser, final Firebase userQueue){
        listedUserImagesREF = new Firebase(Constants.FIREBASE_URL_IMAGES).child(listedUser).child(Constants.FIREBASE_LOC_PROFILE_PIC);
        listedUserImagesREFListener = listedUserImagesREF.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserImages userImages = dataSnapshot.getValue(UserImages.class);
                if (userImages != null) {
                    String profilePic = userImages.getProfilePic();
                    userQueue.child(listedUser).child("profilePic").setValue(profilePic);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }
}
