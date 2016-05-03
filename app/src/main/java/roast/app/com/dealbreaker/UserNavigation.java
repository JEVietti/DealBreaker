package roast.app.com.dealbreaker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import roast.app.com.dealbreaker.fragments.AttributeAssignment;
import roast.app.com.dealbreaker.fragments.InitialRoaming;
import roast.app.com.dealbreaker.fragments.ProfileActivity;
import roast.app.com.dealbreaker.fragments.UpdateImage;
import roast.app.com.dealbreaker.fragments.UserRelationships;
import roast.app.com.dealbreaker.fragments.editProfile;
import roast.app.com.dealbreaker.models.UserImages;
import roast.app.com.dealbreaker.util.Constants;
import roast.app.com.dealbreaker.util.DownloadImages;

/* The UserNavigation class is meant as the base means of navigation during the app states
* of which include the User's Home page, Images, Preferences and User Information Tasks (Fragments)
*
* */
public class UserNavigation extends AppCompatActivity {
    private String userName;
    private DrawerLayout mDrawer;
    Firebase userImageREF;
    private ValueEventListener profilePicListener;
    private NavigationView nvDrawer;
    private int state;
    private ImageView headerImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_navigation);
        //Get UserName data passed from the Login
        if(getIntent().getExtras() != null) {
            Bundle arg = getIntent().getExtras();
            userName = arg.getString(getString(R.string.key_UserName));
            //Initialize the View of the Fragment
            initializeView();
           // profilePicListener();
            state = 0;
            //Set the first Fragment to be loaded
            if (savedInstanceState == null) {
                setFirstItemNavigationView();
                state = 1;
            }
        }
        else {
            finish();
            Toast.makeText(getApplicationContext(), "Failed to retrieve User's Data", Toast.LENGTH_SHORT).show();
        }
    }

    //Need to handle Orientation changes in the app runtime
    //as of now if orientation changes in certain fragments it can cause
    //a restart of the app or worse a crash
    @Override
    public void onConfigurationChanged(Configuration newConfig){


    }

    //Changes the default onBackPressed so that the User can get a warning to whether they would like to exit or not
    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder dlg = new AlertDialog.Builder(this,R.style.AlertDialogTheme);
            AlertDialog dialog = dlg.create();
            dlg.setTitle("Logout!");
            dlg.setMessage("Are you Sure you want to log out?");
            dlg.setNegativeButton(android.R.string.no, null);
            dlg.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {

                    /**We want to end the activity, not move on to a new one.**/
                    //Intent intent = new Intent(UserNavigation.this,InitialScreen.class);
                    //startActivity(intent);
                    finish();
                }
            }).create();
            dlg.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_navigation_logout:
                //onBackPressed();
                finish();
                return true;
            case R.id.action_navigation_delete_account:
               showOnDeleteDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //Sets the DrawerContent to be able to be changed based on Different Navigation Drawer Items
    //using the helper functions NavItemSelected which actually calls the other fragments based on
    //the menu item selected.
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        if(menuItem.isChecked() && state != 0){
                            mDrawer.closeDrawer(nvDrawer);
                            return true;
                        }
                        else{
                             NavItemSelected(menuItem);
                             return true;}

                    }
                });
    }

    private void deleteAccount(final String userEmail, final String userPassword){
        final Firebase ref = new Firebase(Constants.FIREBASE_URL);
        //Delete the account from the email registry, User Profile
       // Query getUserEmail = new Firebase(Constants.FIREBASE_URL_USERS).child(userName).child("email");

        ref.authWithPassword(userEmail, userPassword, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                ref.removeUser(userEmail, userPassword, new Firebase.ResultHandler() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(),"Your Account has been deleted",Toast.LENGTH_LONG);
                        finish();
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        Toast.makeText(getApplicationContext(),"Your Account has not been deleted, try again later",Toast.LENGTH_LONG);
                    }
                });
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                // there was an error
                Toast.makeText(getApplicationContext(),"Your Account has not been deleted, due to an incorrect password",Toast.LENGTH_LONG);
            }
        });
        finish();
    }

    private boolean NavItemSelected(MenuItem item) {
        // Create a new fragment and specify the planet to show based on
        // position
        Fragment fragment = null;
        //Switches to different fragments based on the id in the Navigation Drawer Mennu
        //which can be found in the DealBreaker\app\src\main\res\menu\activity_user_navigation_drawer.xml file
        switch (item.getItemId()) {
            case R.id.nav_preferences:
                fragment = AttributeAssignment.newInstance(userName);
                break;
            case R.id.nav_roam:
                fragment = InitialRoaming.newInstance(userName);
                break;
            case R.id.nav_gallery:
                fragment = UpdateImage.newInstance(userName);
                break;
            case R.id.nav_home:
                fragment = ProfileActivity.newInstance(userName);
                break;
            case R.id.profile_edit:
                fragment = editProfile.newInstance(userName);
                break;
            case R.id.nav_relationship:
                fragment = UserRelationships.newInstance(userName);
                break;
            case R.id.nav_share:
                fragment = editProfile.newInstance(userName);
                break;
            default:
                fragment = AttributeAssignment.newInstance(userName);
                break;
        }

        // Insert the fragment by replacing any existing fragment
        if(fragment!=null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.Content, fragment).commit();
        }
        // Highlight the selected item, update the title, and close the drawer
        item.setChecked(true);
        setTitle(item.getTitle());
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Initializes the view for this Activity
    private void initializeView(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Sets up the Drawer
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();
        nvDrawer = (NavigationView) findViewById(R.id.nav_view);
        headerImageView = (ImageView) findViewById(R.id.nav_header_imageView);
        setupDrawerContent(nvDrawer);
    }

    //Sets the Fragment that will be loaded up implicitly
    //by calling the setCheckedItem Helper function and using this function in onCreate
    private void setFirstItemNavigationView() {
        nvDrawer.setCheckedItem(R.id.nav_home);
        nvDrawer.getMenu().performIdentifierAction(R.id.nav_home, 0);
        nvDrawer.setCheckedItem(R.id.nav_home);
    }
  /*
    private void profilePicListener(){
        userImageREF = new Firebase(Constants.FIREBASE_URL_IMAGES).child(userName).child(Constants.FIREBASE_LOC_PROFILE_PIC);
        profilePicListener = userImageREF.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserImages userImages = dataSnapshot.getValue(UserImages.class);
                String url = userImages.getProfilePic();
                DownloadImages downloadImages = new DownloadImages(headerImageView, UserNavigation.this);
                downloadImages.execute(url);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
*/

    public void showOnDeleteDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.delete_account_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edtPassword = (EditText) dialogView.findViewById(R.id.editDialogPassword);
        final EditText edtEmail = (EditText) dialogView.findViewById(R.id.editDialogEmail);
        dialogBuilder.setTitle("Delete Account");
        dialogBuilder.setMessage("Enter your password below");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String email, password;
                email = edtEmail.getText().toString();
                password = edtPassword.getText().toString();

                if(email != null && password != null) {
                    deleteAccount(email, password);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Make Sure to Enter your email and password",Toast.LENGTH_LONG);
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //profilePicListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //if(profilePicListener != null){
         //   userImageREF.removeEventListener(profilePicListener);
       // }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
