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

//This class acts as a holder for the pending and confirmed
//relationships for the user : Classes Pending and Confirmed Relationships

public class UserRelationships extends Fragment {
    private static final String LOG_TAG = UserRelationships.class.getSimpleName();
    private SectionPagerAdapter mSectionPagerAdapter;
    private String userName, key;

    public UserRelationships() {/* Required empty public constructor*/ }

    public static UserRelationships newInstance(String username) {
        UserRelationships fragment = new UserRelationships();
        Bundle args = new Bundle();
        args.putString("userName", username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            key = getString(R.string.key_UserName);
            userName = getArguments().getString(key);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_user_relationships, container, false);
        initializeView(root);
        return root;
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
                    fragment = ConfirmedRelationships.newInstance(userName);
                    break;
                case 1:
                    fragment = PendingRelationships.newInstance(userName);
                    break;
                default:
                    fragment = PendingRelationships.newInstance(userName);
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
                    return getString(R.string.ConfirmedRelationshipLabel);
                case 1:
                default:
                    return getString(R.string.PendingRelationshipLabel);
            }
        }
    }

    private void initializeView(View rootView) {
        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.relationships_pager);
        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout_relationships);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        SectionPagerAdapter adapter;
        adapter = new SectionPagerAdapter(getFragmentManager());
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}

