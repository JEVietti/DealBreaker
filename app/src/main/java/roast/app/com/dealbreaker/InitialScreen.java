package roast.app.com.dealbreaker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import roast.app.com.dealbreaker.util.Constants;

public class InitialScreen extends AppCompatActivity {
    private Button registerButton,loginButton;
    private EditText mEmailEditText, mPasswordEditText;
    private TextView mLoginErrorMessage;
    private String mUserEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //FacebookSdk.sdkInitialize();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        registerButton = (Button) findViewById(R.id.register_button);
        loginButton = (Button) findViewById(R.id.login_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InitialScreen.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmailEditText = (EditText) findViewById(R.id.LoginEmail);
                mPasswordEditText = (EditText) findViewById(R.id.LoginPassword);

                String userEmail = mEmailEditText.getText().toString();
                String userPassword = mPasswordEditText.getText().toString();

                Firebase ref = new Firebase(Constants.FIREBASE_URL);
                ref.authWithPassword(userEmail, userPassword, new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        System.out.println("User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
                        mUserEmail = (String) authData.getProviderData().get("email");
                        //Intent intent = new Intent(InitialScreen.this, LoginActivity.class);
                        //intent.putExtra(getString(R.string.key_UserEmail), mUserEmail);
                        //startActivity(intent);
                        Intent intent = new Intent(InitialScreen.this, UserNavigation.class);
                        intent.putExtra("username","password");
                        startActivity(intent);
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        // there was an error
                        mLoginErrorMessage = (TextView) findViewById(R.id.loginErrorMessage);
                        mLoginErrorMessage.setVisibility(View.VISIBLE);
                    }

                });
            }
        });

    }

         public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_initial_screen, menu);
            return true;
    }

        public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_help:
                Toast.makeText(this,"Information that may help you",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
