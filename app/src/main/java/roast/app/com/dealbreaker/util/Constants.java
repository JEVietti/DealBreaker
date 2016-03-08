package roast.app.com.dealbreaker.util;
import roast.app.com.dealbreaker.BuildConfig;

/**
 * Constants class store most important strings and paths of the app
 */
public final class Constants {

    // Constants related to locations in Firebase, such as the name of the node
     // Constants for Firebase object properties
        public static final String FIREBASE_LOC_USERS = "users";
        public static final String FIREBASE_LOC_SEEKING = "roaming";
        public static final String FIREBASE_LOC_USER_INFO = "user_info";
    // Constants for Firebase URL

    public static final String FIREBASE_URL = BuildConfig.UNIQUE_FIREBASE_ROOT_URL;
    public static final String FIREBASE_PROP_EMAIL = "email";
    public static final String FIREBASE_TIMESTAMP = "timestamp";
    //May need to concat a "/"
    public static final String FIREBASE_URL_USERS = FIREBASE_URL + FIREBASE_LOC_USERS;
    public static final String FIREBASE_URL_ROAMING = FIREBASE_URL + FIREBASE_LOC_SEEKING;
    public static final String FIREBASE_URL_USER_INFO = FIREBASE_URL + FIREBASE_LOC_USER_INFO;
    // Constants for bundles, extras and shared preferences keys

}
