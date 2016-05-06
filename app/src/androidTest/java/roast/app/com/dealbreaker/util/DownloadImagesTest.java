package roast.app.com.dealbreaker.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runners.model.TestClass;

import static org.junit.Assert.*;


public class DownloadImagesTest extends TestCase {


    DownloadImages downloadImages = new DownloadImages();



    @Test
    public void testCalculateInSampleSize() throws Exception {
        BitmapFactory.Options options =   new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        assertSame(1, DownloadImages.calculateInSampleSize(options,0,0));
        assertSame(1, DownloadImages.calculateInSampleSize(options,100,200));
        options.inJustDecodeBounds = false;
        assertSame(1, DownloadImages.calculateInSampleSize(options,0,0));
        assertSame(1, DownloadImages.calculateInSampleSize(options,100,200));
        int result;
        options.inJustDecodeBounds = false;
        options.inScaled= false;
        result = DownloadImages.calculateInSampleSize(options, 1000, 1000);
        Log.d("Scaling",String.valueOf(result));
        //assertNotSame(1,result);
    }


    //Could not test due to needing a reference to a weak image view
    @Test
    public void testDownload() throws Exception {
        Bitmap result;
        //test a url that doesn't exist
        result = downloadImages.downloadImage("dummy");
        assertNotNull(result);
        //a url that is empty
        result = downloadImages.downloadImage("");
        assertNotNull(result);
        //a search url of many images
        result = downloadImages.downloadImage("https://www.bing.com/images/search?q=google+images&FORM=HDRSC2");
        assertNotNull(result);
        //a website url
        result = downloadImages.downloadImage("https://en.wikipedia.org/wiki/Text_file");
        assertNotNull(result);
        //an image from a random server
        result = downloadImages.downloadImage("http://4.bp.blogspot.com/-hJ2r9MozNUM/TWRKU4dHpAI/AAAAAAAAAEc/Ih-lH2AbcDc/s1600/angry-person-istock.jpg");
        assertNotNull(result);
        //an image from our server
        result = downloadImages.downloadImage("https://s3-us-west-1.amazonaws.com/dealbreaker/password/69285af774db20b6b5f9580825947ec3.png");
        assertNotNull(result);
    }
}