package roast.app.com.dealbreaker.util;

import android.content.Context;

import java.text.SimpleDateFormat;


public class Util {
    /**
     * Format the timestamp with SimpleDateFormat
     */
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private Context context = null;


    /**
     * Public constructor that takes mContext for later use
     */
    public Util(Context con) {
        context = con;
    }

}
