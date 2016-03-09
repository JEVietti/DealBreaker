package roast.app.com.dealbreaker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;

import roast.app.com.dealbreaker.util.Constants;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = getIntent();
        String userEmail = intent.getStringExtra(getString(R.string.key_UserName));

        Toast.makeText(this, "Login successful for " + userEmail, Toast.LENGTH_LONG).show();
    }
}