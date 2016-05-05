package roast.app.com.dealbreaker.util;

import android.os.AsyncTask;
import android.util.Log;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.File;
import java.security.Security;
import java.util.List;


public class UploadFile extends AsyncTask<File, Void, String> {
    private static final String ACCESS_KEY = "AKIAI7GGLZSDDTTKVOXQ",
                    SECRET_KEY = "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",
                    MY_BUCKET = "dealbreaker";

    private String username = "password";
    private File fileToUpload;
    private String uploadedFileURL;
    public UploadFile(String username){
        this.username = username;
    }

    public UploadFile(){}

    @Override
    protected String doInBackground(File... params) {
        for (File f : params) {
            uploadFile(f);
        }
        return uploadedFileURL;
    }

    @Override
    protected void onPostExecute(String fileURL) {
        super.onPostExecute(fileURL);
    }

    private boolean uploadFile(File f){
        AWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        AmazonS3 s3 = new AmazonS3Client(credentials);
        Security.setProperty("networkaddress.cache.ttl", "60");
        s3.setRegion(Region.getRegion(Regions.US_WEST_1));
        s3.setEndpoint("https://s3-us-west-1.amazonaws.com/");
        List<Bucket> buckets=s3.listBuckets();
        for(Bucket bucket:buckets){
            Log.e("Bucket ","Name "+bucket.getName()+" Owner "+bucket.getOwner()+ " Date " + bucket.getCreationDate());
        }
        Log.e("Size ", "" + s3.listBuckets().size());
        fileToUpload = f;
        PutObjectRequest por = new PutObjectRequest(MY_BUCKET,username+"/"+f.getName(),fileToUpload);
        por.setCannedAcl(CannedAccessControlList.PublicRead);
        por.setGeneralProgressListener(new ProgressListener() {
            @Override
            public void progressChanged(ProgressEvent progressEvent) {

            }
        });
        try {
            s3.putObject(por);
        }
        catch (Exception e){
            Log.e("Upload ERROR:", e.getMessage());
        }
        uploadedFileURL = "https://s3-us-west-1.amazonaws.com/" +  MY_BUCKET +username+ "/" + f.getName();
        return true;
    }

}
