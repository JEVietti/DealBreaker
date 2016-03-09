package roast.app.com.dealbreaker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Toast;

import roast.app.com.dealbreaker.fragments.UserAttribute;

/* The UserNavigation class is meant as the base means of navigation during the app states
* of which include the User's Home page, Images, Preferences and User Information Tasks (Fragments)
*
* */
public class UserNavigation extends AppCompatActivity{
    private String userName;
    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_navigation);
        //Get UserName data passed from the Login
        if(getIntent().getExtras() != null) {
            Bundle arg = getIntent().getExtras();
            userName = arg.getString("username");
            //Initialize the View of the Fragment
            initializeView();
            //Set the first Fragment to be loaded
            if (savedInstanceState == null) {
                setFirstItemNavigationView();
            }
        }
        else {
            finish();
            Toast.makeText(getApplicationContext(), "Failed to retrieve User's Data", Toast.LENGTH_SHORT).show();
        }
    }

    //Changes the default onBackPressed so that the User can get a warning to whether they would like to exit or not
    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder dlg = new AlertDialog.Builder(this,R.style.AlertDialogTheme);
            dlg.setTitle("Really Exit!");
            dlg.setMessage("Are you Sure you want to Exit?");
            dlg.setNegativeButton(android.R.string.no, null);
            dlg.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    UserNavigation.super.onBackPressed();
                }
            }).create().show();
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
                        NavItemSelected(menuItem);
                        return true;
                    }
                });
    }

    //
    @SuppressWarnings("StatementWithEmptyBody")
    private boolean NavItemSelected(MenuItem item) {
        // Create a new fragment and specify the planet to show based on
        // position
        Fragment fragment = null;
        //Switches to different fragments based on the id in the Navigation Drawer Mennu
        //which can be found in the DealBreaker\app\src\main\res\menu\activity_user_navigation_drawer.xml file
        switch (item.getItemId()) {
            case R.id.nav_manage:
                fragment = AttributeAssignment.newInstance(userName);
                break;
            case R.id.nav_send:
                fragment = ProfileActivity.newInstance(userName);
                break;
            default:
                fragment = ProfileActivity.newInstance(userName);
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
        setupDrawerContent(nvDrawer);
    }

    //Sets the Fragment that will be loaded up implicitly
    //by calling the setCheckedItem Helper function and using this function in onCreate
    private void setFirstItemNavigationView() {
        nvDrawer.setCheckedItem(R.id.nav_send);
        nvDrawer.getMenu().performIdentifierAction(R.id.nav_send, 0);
        nvDrawer.setCheckedItem(R.id.nav_send);
    }

}