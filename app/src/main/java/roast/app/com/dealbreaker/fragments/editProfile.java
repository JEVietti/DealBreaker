package roast.app.com.dealbreaker.fragments;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import roast.app.com.dealbreaker.R;
import roast.app.com.dealbreaker.models.UserQualities;
import roast.app.com.dealbreaker.util.Constants;


public class editProfile extends Fragment {

    //textviews that user can edit on main profile page
    private TextView bio, badqual, goodqual;
    private ScrollView scrollView;                       //scrollview
    private View.OnTouchListener scrollTouch;
    private Button   bioGo, badGo, goodGo, Submit;
    private Firebase userInfoREF, userInfoREFBAD, userInfoREFGOOD;
    private EditText biographyEdit, badEdit, goodEdit;

    private ValueEventListener InfoListener;

    private String username;
    //Creates a new instance func for editprofile
    public static Fragment newInstance(String userName) {
        editProfile fragment = new editProfile();
        Bundle args = new Bundle();
        args.putString("userName", userName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_profile);
        if (getArguments() != null) {
            String key = getString(R.string.key_UserName);
            username = getArguments().getString(key);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        BIOGRAPHY();
    }

    @Override
    public void onPause(){
        super.onPause();
        userInfoREF.removeEventListener(InfoListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        userInfoREF.removeEventListener(InfoListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final View view = inflater.inflate(R.layout.activity_edit_profile, container, false);
        screen(view);

        //calls funcs
        BIOGRAPHY();          //biography onclick functionality
        //BAD();                //bad onclick functionality
        //GOOD();               //good onclick functionality

        return view;
    }

    private void BIOGRAPHY() {
        userInfoREF = new Firebase(Constants.FIREBASE_URL_USERS).child(username).child(Constants.FIREBASE_LOC_USER_QUALITIES);
        InfoListener = userInfoREF.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserQualities userQualities = dataSnapshot.getValue(UserQualities.class);
                if(userQualities != null){
                    biographyEdit.setText(userQualities.getBiography());
                    badEdit.setText(userQualities.getBadQualities());
                    goodEdit.setText(userQualities.getGoodQualities());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

       // userInfoREF.removeEventListener(initialValues);
        //Biography --
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String BioData = biographyEdit.getText().toString();
                userInfoREF.child("biography").setValue(BioData);

                String badChar = badEdit.getText().toString();
                userInfoREF.child("badQualities").setValue(badChar);

                String goodChar = goodEdit.getText().toString();
                userInfoREF.child("goodQualities").setValue(goodChar);

                Toast.makeText(getContext(), "Submitted", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void BAD() {
        userInfoREFBAD = new Firebase(Constants.FIREBASE_URL_USERS).child(username).child(Constants.FIREBASE_LOC_USER_QUALITIES);

        //Bad Characteristics --
        badGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String badChar = badEdit.getText().toString();
                userInfoREFBAD.child("badQualities").setValue(badChar);
                Toast.makeText(getContext(), "Submitted", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void GOOD(){
        userInfoREFGOOD = new Firebase(Constants.FIREBASE_URL_USERS).child(username).child(Constants.FIREBASE_LOC_USER_QUALITIES);

        //Good Characteristics --
        goodGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String goodChar = goodEdit.getText().toString();
                userInfoREFGOOD.child("goodQualities").setValue(goodChar);
                Toast.makeText(getContext(), "Submitted", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //initialize widgets
    private void screen(View mainview){
        bio = (TextView) mainview.findViewById(R.id.bio);
        badqual = (TextView) mainview.findViewById(R.id.badqual);
        goodqual = (TextView) mainview.findViewById(R.id.goodqual);
        scrollView = (ScrollView) mainview.findViewById(R.id.ScrollView01);

        //bioGo = (Button) mainview.findViewById(R.id.buttonBio);
        //badGo = (Button) mainview.findViewById(R.id.buttonBad);
        //goodGo = (Button) mainview.findViewById(R.id.buttonGood);
        Submit = (Button) mainview.findViewById(R.id.Submit);

        biographyEdit = (EditText) mainview.findViewById(R.id.bioEditText);
        badEdit = (EditText) mainview.findViewById(R.id.badQualEditText2);
        goodEdit = (EditText) mainview.findViewById(R.id.goodQualEditText3);

        bio.setText("Bio:");
        badqual.setText("Set Your Bad Qualities:");
        goodqual.setText(" Set Your Good Qualities");
    }

}
