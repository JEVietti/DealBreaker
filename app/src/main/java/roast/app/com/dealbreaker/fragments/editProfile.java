package roast.app.com.dealbreaker.fragments;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import roast.app.com.dealbreaker.R;
import roast.app.com.dealbreaker.util.Constants;


public class editProfile extends Fragment {

    //textviews that user can edit on main profile page
    private TextView bio, badqual, goodqual;
    private ScrollView scrollView;                       //scrollview
    private View.OnTouchListener scrollTouch;
    private Button   bioGo, badGo, goodGo;
    private Firebase userInfoREF;
    private EditText biographyEdit, badEdit, goodEdit;

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

        /*userInfoREF = new Firebase(Constants.FIREBASE_URL_USERS).child(username).child(Constants.FIREBASE_LOC_USER_QUALITIES);
        bioGo.setOnClickListener(new View.OnClickListener() {
        String BioData = biographyEdit.getText().toString();
            @Override
            public void onClick(View v) {
                userInfoREF.child("biography").setValue(BioData);
            }
        });*/

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final View view = inflater.inflate(R.layout.activity_edit_profile, container, false);
        screen(view);

        return view;
    }


    //initalizing widgets
    private void screen(View mainview){
        bio = (TextView) mainview.findViewById(R.id.bio);
        badqual = (TextView) mainview.findViewById(R.id.badqual);
        goodqual = (TextView) mainview.findViewById(R.id.goodqual);
        scrollView = (ScrollView) mainview.findViewById(R.id.ScrollView01);

        bioGo = (Button) mainview.findViewById(R.id.buttonBio);
        badGo = (Button) mainview.findViewById(R.id.buttonBad);
        goodGo = (Button) mainview.findViewById(R.id.buttonGood);

        biographyEdit = (EditText) mainview.findViewById(R.id.bioEditText);
        badEdit = (EditText) mainview.findViewById(R.id.badQualEditText2);
        goodEdit = (EditText) mainview.findViewById(R.id.goodQualEditText3);

        bio.setText("Bio:");
        badqual.setText("Set Your Bad Qualities:");
        goodqual.setText(" Set Your Good Qualities");
    }

    private void addDataFirebase(){


        /*if(){

        }
        else{

        }*/

    }

}
