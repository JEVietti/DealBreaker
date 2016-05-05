package roast.app.com.dealbreaker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Map;
import java.util.Vector;

import roast.app.com.dealbreaker.models.User;
import roast.app.com.dealbreaker.util.Constants;

public class RegisterActivity extends AppCompatActivity {
    private Button mRegisterButton;
    private EditText mUserName, mEmail, mConfirmEmail, mPassword, mConfirmPassword;
    private TextView mErrorMessage;
    private Vector<String> mUserEmailUsed = new Vector<>();
    private Vector<String> mUserNameUsed = new Vector<>();

    Firebase userDatabase = new Firebase(Constants.FIREBASE_URL_USERS);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        userDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    if(postSnapshot != null){
                        String userEmail = postSnapshot.child("email").getValue().toString();
                        String userName = postSnapshot.child("userName").getValue().toString();
                        mUserEmailUsed.addElement(userEmail);
                        mUserNameUsed.addElement(userName);
                    }
                }

            }

            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        mUserName = (EditText) findViewById(R.id.registerUserName);
        mEmail = (EditText) findViewById(R.id.registerEmail);
        mConfirmEmail = (EditText) findViewById(R.id.confirmEmail);
        mPassword = (EditText) findViewById(R.id.registerPassword);
        mConfirmPassword = (EditText) findViewById(R.id.confirmPassword);
        mErrorMessage = (TextView) findViewById(R.id.registerErrorMessage);

        mRegisterButton = (Button) findViewById(R.id.register_act_button);

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = mUserName.getText().toString();
                String userEmail = mEmail.getText().toString();
                String userConfirmEmail = mConfirmEmail.getText().toString();
                String userPassword = mPassword.getText().toString();
                String userConfirmPassword = mConfirmPassword.getText().toString();

                boolean inUseEmail = false;
                boolean inUseUserName = false;

                mErrorMessage.setVisibility(View.INVISIBLE);

                for (int i = 0; i < mUserEmailUsed.size(); i++) {
                    if (mUserEmailUsed.get(i).equals(userEmail)) {
                        inUseEmail = true;
                    }
                }

                for (int i = 0; i < mUserNameUsed.size(); i++) {
                    if (mUserNameUsed.get(i).equals(username)) {
                        inUseUserName = true;
                    }
                }

                if(!TextUtils.isEmpty(username)) {
                    if (!inUseUserName) {
                        if (isProperUserName(username)) {
                            if (!userEmail.isEmpty()) {
                                if (isProperEmail(userEmail)) {
                                    if (!inUseEmail) {
                                        if (userEmail.equals(userConfirmEmail)) {
                                            if (userPassword.length() >= 8) {
                                                if (userPassword.equals(userConfirmPassword)) {
                                                    registerUser(username, userEmail, userPassword);
                                                } else {
                                                    mErrorMessage.setText("Passwords do not match.");
                                                    mErrorMessage.setVisibility(View.VISIBLE);
                                                }
                                            } else {
                                                mErrorMessage.setText("Passwords is too short.");
                                                mErrorMessage.setVisibility(View.VISIBLE);
                                            }
                                        } else {
                                            mErrorMessage.setText("Emails do not match.");
                                            mErrorMessage.setVisibility(View.VISIBLE);
                                        }
                                    } else {
                                        mErrorMessage.setText("Email is already being used.");
                                        mErrorMessage.setVisibility(View.VISIBLE);
                                        mUserEmailUsed.removeAllElements();
                                    }
                                } else {
                                    mErrorMessage.setText("Email is not proper.");
                                    mErrorMessage.setVisibility(View.VISIBLE);
                                }
                            } else {
                                mErrorMessage.setText("Email cannot be empty.");
                                mErrorMessage.setVisibility(View.VISIBLE);
                            }
                        } else {
                            mErrorMessage.setText("Username can only have letters, numbers, _, and !.");
                            mErrorMessage.setVisibility(View.VISIBLE);
                        }
                    } else {
                        mErrorMessage.setText("Username is already being used.");
                        mErrorMessage.setVisibility(View.VISIBLE);
                    }
                } else {
                    mErrorMessage.setText("Username cannot be empty.");
                    mErrorMessage.setVisibility(View.VISIBLE);
                }
            }

            public  void registerUser(final String userName, final String userEmail, String userPassword){
                Firebase database = new Firebase(Constants.FIREBASE_URL);
                database.createUser(userEmail, userPassword, new Firebase.ValueResultHandler<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> result) {
                        System.out.println("Successfully created user account with uid: " + result.get("uid"));

                        User user = new User(userName, userEmail);
                        userDatabase.child(userName).setValue(user);

                        Intent intent = new Intent(RegisterActivity.this, InitialUserAttributes.class);
                        intent.putExtra(getString(R.string.key_UserName), userName);
                        startActivity(intent);

                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                    }
                });
            }
        });
    }

    public Boolean isProperUserName(String username){
        for(int i = 0; i < username.length(); i++) {
            if( (Character.isLetter(username.charAt(i)))
                    || (Character.isDigit(username.charAt(i)))
                    ||  ((username.charAt(i)) == '_')
                    ||  ((username.charAt(i)) == '!') ){
                continue;
            } else {
                return false;
            }
        }

        return true;
    }

    public final static boolean isProperEmail(String email){
        if (TextUtils.isEmpty(email)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_help:
                Toast.makeText(this,"Information that may help you",Toast.LENGTH_SHORT).show();
                return true;
            case android.R.id.home:
                Intent myIntent = new Intent(getApplicationContext(), InitialScreen.class);
                startActivityForResult(myIntent, 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
