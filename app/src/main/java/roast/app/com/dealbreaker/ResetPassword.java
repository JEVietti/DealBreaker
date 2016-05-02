package roast.app.com.dealbreaker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import roast.app.com.dealbreaker.util.Constants;

public class ResetPassword extends AppCompatActivity {

    private EditText mUserEmail;
    private TextView mErrorMessage;
    private Button mResetButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mUserEmail = (EditText) findViewById(R.id.forgotPasswordEmail);
        mErrorMessage = (TextView) findViewById(R.id.resetPasswordErrorMessage);
        mResetButton = (Button) findViewById(R.id.forgotPasswordButton);

        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String userEmail = mUserEmail.getText().toString();

                Firebase database = new Firebase(Constants.FIREBASE_URL_USER_INFO);
                database.resetPassword(userEmail, new Firebase.ResultHandler() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(ResetPassword.this, "Email sent.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(ResetPassword.this, InitialScreen.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(FirebaseError error) {
                        if(isProperEmail(userEmail)){
                            mErrorMessage.setText("Email not found.");
                        }
                        else {
                            mErrorMessage.setText("Please enter an email.");
                        }

                        mErrorMessage.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed(){
        finish();
    }

    public final static boolean isProperEmail(String email){
        if (TextUtils.isEmpty(email)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

}