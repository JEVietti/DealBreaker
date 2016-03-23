package roast.app.com.dealbreaker.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import roast.app.com.dealbreaker.R;

//Acts as a Fragment and Tab Manager, and is a fragment due to Navigation Drawer constraints
public class AttributeAssignment extends Fragment{
    private static final String LOG_TAG = AttributeAssignment.class.getSimpleName();
    private SectionPagerAdapter mSectionPagerAdapter;
    private String username;
    private String key;
    //Creates a new Instance of the Fragment class: Attribute Assignment
    //Its purpose is to facilitate passing arguments into the Fragment
    public static AttributeAssignment newInstance(String userName) {
      AttributeAssignment fragment = new AttributeAssignment();
        Bundle args = new Bundle();
        //Adding the userName to the Bundle in order to use it later on

        args.putString("userName",userName);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            key = getString(R.string.key_UserName);
            username = getArguments().getString(key);
        }
        else {
            getActivity().finish();
            Toast.makeText(getContext(), "Failed to retrieve User's Data", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_attribute_assignment, container, false);
        initializeView(v);
        return v;
    }

    //This class is to control and manage the tabs which house the User and Roaming attribute Class
    private class SectionPagerAdapter extends FragmentStatePagerAdapter {

        private SectionPagerAdapter(FragmentManager fm) {
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

    private void initializeView(View rootView) {
        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.pager);
        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);

        SectionPagerAdapter adapter;
        adapter = new SectionPagerAdapter(getFragmentManager());
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }


}