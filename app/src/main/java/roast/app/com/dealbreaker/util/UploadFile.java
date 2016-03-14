package roast.app.com.dealbreaker.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Path;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;

import java.io.File;
import java.security.Security;
import java.util.List;


public class UploadFile extends AsyncTask<File, Void, String> {
    private String  ACCESS_KEY = "",
                    SECRET_KEY = "",
                    MY_BUCKET = "",
                    username = "password";
    private File fileToUpload;
    private Activity activity;
    private String path, uploadedFileURL;
    private ProgressDialog progress;
    UploadFile(String username, Activity activity){
        this.username = username;
        this.activity = activity;
    }
    @Override
    protected String doInBackground(File... params) {
        return uploadedFileURL;
    }

    @Override
    protected void onPostExecute(String fileURL) {
        super.onPostExecute(fileURL);
    }

    private boolean uploadFile(File f){
        AWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        AmazonS3 s3 = new AmazonS3Client(credentials);
        Security.setProperty("networkaddress.cache.ttl" , "60");
        s3.setRegion(Region.getRegion(Regions.US_WEST_1));
        s3.setEndpoint("https://s3-us-west-1.amazonaws.com/");
        List<Bucket> buckets=s3.listBuckets();
        for(Bucket bucket:buckets){
            Log.e("Bucket ","Name "+bucket.getName()+" Owner "+bucket.getOwner()+ " Date " + bucket.getCreationDate());
        }
        Log.e("Size ", "" + s3.listBuckets().size());
        TransferUtility transferUtility = new TransferUtility(s3, activity);
        //This will be replaced eventually in the activity that calls this class by passing in a selected
        //File path from the user's selection of an image file from Gallery
        //using Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        fileToUpload=new File(Environment.getExternalStorageDirectory().getPath()+"/Screenshot.png");
        TransferObserver observer = transferUtility.upload(MY_BUCKET,username,fileToUpload);
        observer.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                // do something
                progress.hide();
                Log.e("Transferring File","ID "+id+"\nState "+state.name()+"\nImage ID "+username);

            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                int percentage = (int) (bytesCurrent / bytesTotal * 100);
                progress.setProgress(percentage);
                //Display percentage transferred to user
            }

            @Override
            public void onError(int id, Exception ex) {
                // do something
                Log.e("Error  ", "" + ex);
            }

        });
        return true;
    }

}
