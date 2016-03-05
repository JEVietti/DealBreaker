package roast.app.com.dealbreaker;

import com.firebase.client.Firebase;


public class MainActivity extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);

    }

}
