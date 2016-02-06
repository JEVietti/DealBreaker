package roast.app.com.dealbreaker;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListAdapter;
import android.widget.LinearLayout;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParseUser;
import com.parse.Parse;
import com.parse.ParseObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      /* Parse Test Object
        Parse.enableLocalDatastore(this);
        Parse.initialize(this);
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("Test", "DealBreakerv2");
        testObject.saveInBackground();
      */
    }

}
