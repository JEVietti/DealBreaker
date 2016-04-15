package roast.app.com.dealbreaker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

/* Roaming Activity is for searching for other users that match with your requirements
* This app should be launched from a FAB from the UserNavigation Activity so it can be launched
 * across all of the fragments and not have to be initialized at individual fragments
 * this should help with maintenance and be a better user experience:
 * http://developer.android.com/intl/es/reference/android/support/design/widget/FloatingActionButton.html
* */

public class RoamingActivity extends AppCompatActivity {
    //Class variables
    private ArrayList<String> matchedUsers;
    //If we don't want to implement swiping yet we could instead add buttons to the page

    private Button addButton, rejectButton, nextButton;
    //add will immediately add them to the pending list if the other user has not made a decision on
    // the user yet, the rejected one will move them to the rejected list and will no longer show
    //up in their queue anymore. The next button keeps them in the queue but moves them into the back
    //of their queue

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roaming);
    }
    //Query the data in a pipeline
    //Grab a subset of data at once then display and regrab the data
    //when the user begins to run out of users to go through
    private ArrayList getUsers(){
        matchedUsers = new ArrayList<>();

        return matchedUsers;
    }

    //Initialize the view for the fragment
    private void initializeView(){

    }
}
