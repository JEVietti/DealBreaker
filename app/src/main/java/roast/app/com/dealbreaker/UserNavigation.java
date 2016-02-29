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

import roast.app.com.dealbreaker.fragments.UserAttribute;

public class UserNavigation extends AppCompatActivity{
    private String userName = "password";
    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();
        nvDrawer = (NavigationView) findViewById(R.id.nav_view);
        setupDrawerContent(nvDrawer);

        if (savedInstanceState == null) {
            setFirstItemNavigationView();
        }
    }

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


    @SuppressWarnings("StatementWithEmptyBody")
    private boolean NavItemSelected(MenuItem item) {
        // Create a new fragment and specify the planet to show based on
        // position
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.nav_manage:
                fragment = AttributeAssignment.newInstance("password");
                break;
            case R.id.nav_send:
                fragment = UserAttribute.newInstance("password");
                break;
            default:
                fragment = AttributeAssignment.newInstance("password");
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


    private void setFirstItemNavigationView() {
         nvDrawer.setCheckedItem(R.id.nav_manage);
         nvDrawer.getMenu().performIdentifierAction(R.id.nav_manage, 0);
         nvDrawer.setCheckedItem(R.id.nav_manage);
}
/*
    private void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the planet to show based on
        // position
        Fragment fragment = null;
        switch (menuItem.getItemId()) {
            case R.id.nav_manage:
                fragment = AttributeAssignment.newInstance("password");
                break;
            case R.id.nav_send
                fragment = UserAttribute.newInstance("password");
                break;
            default:
                fragment = AttributeAssignment.newInstance("password");
                break;
        }

        // Insert the fragment by replacing any existing fragment
        if(fragment!=null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.Content, fragment).commit();
        }
        // Highlight the selected item, update the title, and close the drawer
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();

    }
*/


}
