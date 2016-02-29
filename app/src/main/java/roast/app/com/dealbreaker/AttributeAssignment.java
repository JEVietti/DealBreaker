package roast.app.com.dealbreaker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import roast.app.com.dealbreaker.fragments.RoamingAttribute;
import roast.app.com.dealbreaker.fragments.UserAttribute;


public class AttributeAssignment extends Fragment{
    private static final String LOG_TAG = AttributeAssignment.class.getSimpleName();
    private SectionPagerAdapter mSectionPagerAdapter;
    private String username;
    ViewPager mViewPager;

    //Empty Constructor
    public void AttributeAssignment(){}

    public static AttributeAssignment newInstance(String userName) {
      AttributeAssignment fragment = new AttributeAssignment();
        Bundle args = new Bundle();
        //Adding the userName to the Bundle in order to use it later on
        args.putString("username",userName);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString("username");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_attribute_assignment, container, false);
        ViewPager viewPager = (ViewPager) v.findViewById(R.id.pager);
        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.tab_layout);
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);

        SectionPagerAdapter adapter;
        adapter = new SectionPagerAdapter(getFragmentManager());
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        return v;
    }

    public class SectionPagerAdapter extends FragmentStatePagerAdapter {

        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {

            Fragment fragment = null;

            switch (position) {
                case 0:
                    fragment = UserAttribute.newInstance(username);
                    break;
                case 1:
                    fragment = RoamingAttribute.newInstance(username);
                    break;
                default:
                    fragment = UserAttribute.newInstance(username);
                    break;
            }

            return fragment;
        }


        @Override
        public int getCount() {
            return 2;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.user_fragment);
                case 1:
                default:
                    return getString(R.string.roaming_fragment);
            }
        }
    }
    /*
    public void initializeScreen() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        SectionPagerAdapter adapter = new SectionPagerAdapter(getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }
    */
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_attribute_assignment, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.action_help:
                Toast.makeText(this, "Information that may help you", Toast.LENGTH_SHORT).show();
                return true;
            case android.R.id.home:
                Intent myIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivityForResult(myIntent, 0);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
    */

}