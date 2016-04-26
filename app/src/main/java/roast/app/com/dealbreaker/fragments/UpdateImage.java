package roast.app.com.dealbreaker.fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


import java.io.File;
import java.util.HashMap;
import java.util.Map;

import roast.app.com.dealbreaker.R;
import roast.app.com.dealbreaker.models.UserImages;
import roast.app.com.dealbreaker.models.UserQualities;
import roast.app.com.dealbreaker.util.Constants;
import roast.app.com.dealbreaker.util.DownloadImages;
import roast.app.com.dealbreaker.util.UploadFile;

public class UpdateImage extends Fragment {
    private Firebase profilePicREF;
    private File newUserProfilePic;
    private ValueEventListener profilePicListener;
    private String userName;
    private ImageView currentProfilePic;
    private DownloadImages downloadImages;
    private String profilePicURL, imagePath, newProfilePicURL;
    private Button uploadImageButton, chooseFileButton;
    private static final int checkStatus = 1;
    private Intent intent;

    public static UpdateImage newInstance(String param) {
        UpdateImage updateImage = new UpdateImage();
        Bundle bundle = new Bundle();
        bundle.putString("userName", param);
        updateImage.setArguments(bundle);
        return updateImage;
    }

    // Required empty public constructor
    public UpdateImage() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userName = getArguments().getString("userName");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root;
        root = inflater.inflate(R.layout.fragment_update_image, container, false);
        initializeView(root);
        listenUSER_PROFILE_PIC();
        pickImage();
        uploadImage();
        return root;
    }

    /*
    @Override
    public void onResume(){
        super.onResume();
        listenUSER_PROFILE_PIC();
    }
    */

    @Override
    public void onPause() {
        super.onPause();
        if (profilePicListener != null) {
            profilePicREF.removeEventListener(profilePicListener);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (profilePicListener != null) {
            profilePicREF.removeEventListener(profilePicListener);
        }
    }
    
    private void pickImage() {
        chooseFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profilePicREF.removeEventListener(profilePicListener);
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), checkStatus);
            }
        });
    }

    private void uploadImage(){
       final UploadFile uploadFile = new UploadFile(userName);
        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imagePath != null) {
                    newUserProfilePic = new File(imagePath);
                    uploadFile.execute(newUserProfilePic);
                    //Work around until I can figure out how to retrieve resulting URL from the ASYNC TASK
                    //Partially Hardcoded URL
                    newProfilePicURL = "https://s3-us-west-1.amazonaws.com/" + "dealbreaker" + "/" + userName + "/" + newUserProfilePic.getName();
                    setNewUserProfilePic();
                    Toast.makeText(getContext(), "Successfully Uploaded your File", Toast.LENGTH_LONG).show();
                    listenUSER_PROFILE_PIC();
                }
                else{
                    Toast.makeText(getContext(), "Please select an Image First", Toast.LENGTH_LONG).show();
                    listenUSER_PROFILE_PIC();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == checkStatus && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri selectedImage  = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
               if(cursor != null){
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imagePath = cursor.getString(columnIndex);
                    cursor.close();
                   currentProfilePic.setImageBitmap(BitmapFactory.decodeFile(imagePath));
                }
            }
        }
    }

    private void initializeView(View rootView) {
        currentProfilePic = (ImageView) rootView.findViewById(R.id.imageCurrentProfilePic);
        uploadImageButton = (Button) rootView.findViewById(R.id.uploadImageButton);
        chooseFileButton = (Button) rootView.findViewById(R.id.chooseImageButton);
    }

    private void listenUSER_PROFILE_PIC() {
        profilePicREF = new Firebase(Constants.FIREBASE_URL_IMAGES).child(userName).child(Constants.FIREBASE_LOC_PROFILE_PIC);
        profilePicListener = profilePicREF.addValueEventListener(new ValueEventListener() {
            @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                UserQualities profilePicSource = dataSnapshot.getValue(UserQualities.class);
                if (profilePicSource != null) {
                    downloadImages = new DownloadImages(currentProfilePic, getActivity());
                    profilePicURL = profilePicSource.getProfilePic();
                    downloadImages.execute(profilePicURL);
                    downloadImages = null;
                } else {
                    downloadImages = new DownloadImages(currentProfilePic, getActivity());
                    downloadImages.execute("dummyURL");
                    downloadImages = null;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void setNewUserProfilePic(){
        Firebase ref = new Firebase(Constants.FIREBASE_URL_IMAGES);
        UserImages userImages = new UserImages(newProfilePicURL);
        HashMap<String, Object> updates = new HashMap<String, Object>();
        Map<String,Object> map = new ObjectMapper().convertValue(userImages, Map.class);
        updates.put(Constants.FIREBASE_LOC_PROFILE_PIC, map);
        ref.child(userName).updateChildren(updates);
    }
}
