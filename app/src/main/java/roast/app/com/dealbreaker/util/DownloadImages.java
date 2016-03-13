package roast.app.com.dealbreaker.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.webkit.URLUtil;
import android.widget.ImageButton;
import android.widget.ImageView;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import roast.app.com.dealbreaker.R;
//Class in charge of downloading and displaying an image through a passed url
//and passed imageview hopefully this will scale with list and grid views but it may need some tweaking
//I think it might work however with an adapter
public class DownloadImages extends AsyncTask<String, Void, Bitmap> {
    public ImageView imageView;
    private Bitmap defaultImage;
    private Activity source;
    public DownloadImages(ImageButton imageView, Activity source){
        super();
        this.source = source;
        this.imageView = imageView;
    }
    //Do the main tasks in the background to save UI thread
        @Override
        protected Bitmap doInBackground(String... urls) {
            defaultImage = BitmapFactory.decodeResource(source.getResources(),R.drawable.defaultuser);
            Bitmap map = null;
            for (String url : urls) {
                map = downloadImage(url);
            }
            return map;
        }

        // Sets the Bitmap returned by doInBackground
        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageBitmap(result);
        }

        // Creates Bitmap from InputStream and returns it
        private Bitmap downloadImage(String url) {
            Bitmap bitmap = null;
            InputStream stream = null;
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            final int REQUIRED_SIZE =70;
            int widthTemp = bmOptions.outWidth;
            int heightTemp = bmOptions.outHeight;
            int scale=1;
            //scales the image appropriately
            while(true){
                if(widthTemp/2<REQUIRED_SIZE || heightTemp/2<REQUIRED_SIZE)
                    break;
                widthTemp/=2;
                heightTemp/=2;
                scale*=2;
            }
            bmOptions.inSampleSize =scale;
            if(URLUtil.isValidUrl(url)){
                try {
                    URL imageURL = new URL(url);
                    //establish connection
                    stream = getHttpConnection(imageURL);
                    //convert image in url to a bitmap
                    bitmap = BitmapFactory.decodeStream(stream, null, bmOptions);
                    if(bitmap==null||stream==null){
                        return defaultImage;}
                    stream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
                return bitmap;
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
}

