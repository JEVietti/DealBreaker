package roast.app.com.dealbreaker.util;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import roast.app.com.dealbreaker.R;
//Class in charge of downloading and displaying an image through a passed url
//and passed imageView hopefully this will scale with list and grid views but it may need some tweaking
//I think it might work however with an adapter
public class DownloadImages extends AsyncTask<String, Void, Bitmap> {
    private WeakReference<ImageView> imageViewWeakReference;
    private Bitmap defaultImage, resultImage;
    private Activity source;

    //Empty Constructor
    public DownloadImages(){}
    //Constructor for Initializing a view
    public DownloadImages(ImageView imageView, Activity source){
        this.source = source;
        imageViewWeakReference = new WeakReference<ImageView>(imageView);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    //Do the main tasks in the background to save UI thread
        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap map = null;
            for (String url : urls) {
                if (URLUtil.isValidUrl(url)) {
                    map = downloadImage(url);
                }
                else{
                   map = returnDefault();
                }
            }
                return map;
        }

        // Sets the Bitmap returned by doInBackground
        @Override
        protected void onPostExecute(Bitmap result) {
            if(imageViewWeakReference != null && result != null){
                final ImageView imageView = imageViewWeakReference.get();
                if(imageView != null){
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView.setImageBitmap(result);
                }
            }
        }

        // Creates Bitmap from InputStream and returns it
        public Bitmap downloadImage(String url) {
            Bitmap bitmap = null;
            ImageView imageView = imageViewWeakReference.get();
            InputStream stream = null;
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            if(imageView != null) {
                final int REQUIRED_WIDTH = imageView.getWidth();
                final int REQUIRED_HEIGHT = imageView.getHeight();
                bmOptions.inScaled = true;
                //scales the image appropriately

                bmOptions.inSampleSize = calculateInSampleSize(bmOptions, REQUIRED_WIDTH, REQUIRED_HEIGHT);
                try {
                    URL imageURL = new URL(url);
                    //establish connection
                    stream = getHttpConnection(imageURL);
                    //convert image in url to a bitmap
                    bitmap = BitmapFactory.decodeStream(stream, null, bmOptions);
                    if (bitmap == null || stream == null) {
                        return returnDefault();
                    }
                    stream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                return bitmap;
            }
            return returnDefault();
        }

    private Bitmap returnDefault(){
        Bitmap result;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        ImageView imageView = imageViewWeakReference.get();
        if(imageView != null){
        final int REQUIRED_WIDTH = imageView.getWidth();
        final int REQUIRED_HEIGHT = imageView.getHeight();
        result = decodeSampledBitmapFromResource(source.getResources(), R.drawable.defaultuser, REQUIRED_WIDTH, REQUIRED_HEIGHT, bmOptions);
        return result;}
        else{
            final int REQUIRED_WIDTH = 70;
            final int REQUIRED_HEIGHT = 70;
            result = decodeSampledBitmapFromResource(source.getResources(), R.drawable.defaultuser, REQUIRED_WIDTH, REQUIRED_HEIGHT, bmOptions);
            return result;
        }
    }

        // Makes HttpURLConnection and returns InputStream
        private InputStream getHttpConnection(URL url)
                throws IOException {
            InputStream stream = null;
            URLConnection connection = url.openConnection();
            try {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("GET");
                httpConnection.setConnectTimeout(30000);
                httpConnection.setReadTimeout(40000);
                httpConnection.setInstanceFollowRedirects(true);
                httpConnection.connect();

                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    stream = httpConnection.getInputStream();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return stream;
        }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight, BitmapFactory.Options options) {

        // First decode with inJustDecodeBounds=true to check dimensions
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
}


