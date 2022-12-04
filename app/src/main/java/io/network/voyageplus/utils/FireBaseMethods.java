package io.network.voyageplus.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import io.network.voyageplus.R;
import io.network.voyageplus.activities.AddPlacesActivity;
import io.network.voyageplus.model.Comments;
import io.network.voyageplus.model.Like;
import io.network.voyageplus.model.Photo;

public class FireBaseMethods {
    private static final String TAG = "FireBaseMethods";
    private Context mContext ;
    private double mPhotoUploadProgress = 0;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private StorageReference mStorageReference;

    public FireBaseMethods(Context context) {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mContext = context;
        }

    public void uploadPhoto(final String name, final String description,final  String town, final String categorie, final String rate,final int count, final String imgUrl,
                               Bitmap bm) {
        Log.d(TAG, "uploadNewPhoto: attempting to uplaod photo.");
        FilePaths filePaths = new FilePaths();
            Log.d(TAG, "uploadNewPhoto: uploading NEW photo.");
        StorageReference storageReference = mStorageReference.child(filePaths.FIREBASE_IMAGE_STORAGE + "/photo" + (count + 1));

/*
        bm=getImageBitmap(AddPlacesActivity.getImageUriFromHere().toString());
        byte[] bytes=ImageManager.getBytesFromBitmap(bm,100);
        uploadTask=storageReference.putBytes(bytes);
*/
        UploadTask uploadTask = null;

        uploadTask = storageReference.putFile(AddPlacesActivity.getImageUriFromHere());
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Uri firebaseUrl = taskSnapshot.getDownloadUrl();
                if (taskSnapshot.getMetadata() != null) {
                    if (taskSnapshot.getMetadata().getReference() != null) {
                        Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Toast.makeText(mContext, "photo upload success", Toast.LENGTH_SHORT).show();
                                addPhotoToDatabase(name,description,town,categorie,rate,uri.toString());

                                //createNewPost(imageUrl);
                            }
                        });
                    }
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: Photo upload failed.");
                Toast.makeText(mContext, "Photo upload failed ", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = 100 + taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount();
                if (progress - 15 > mPhotoUploadProgress) {
                    Toast.makeText(mContext, "Photo upload progress", Toast.LENGTH_SHORT).show();
                    mPhotoUploadProgress = progress;
                }
            }
        });


    }

            // Code for showing progressDialog while uploading
    private void addPhotoToDatabase(String name, String description, String town, String categorie, String rate, String url){
        Log.d(TAG, "addPhotoToDatabase: adding photo to database.");

        String newPhotoKey = myRef.child("photos").push().getKey();

        Photo photo = new Photo();
        photo.setName(name);
        photo.setTown(town);
        photo.setDescription(description);
        photo.setCategorie(categorie);
        photo.setImageUrl(url);
        photo.setRate(rate);
        photo.setPhotoId(newPhotoKey);
        //insert into database
         myRef.child(mContext.getString(R.string.dbname_photos)).child(newPhotoKey).setValue(photo);

    }

    public int getImageCount(DataSnapshot dataSnapshot){
        int count = 0;
        for(DataSnapshot ds: dataSnapshot
                .child(mContext.getString(R.string.dbname_photos))
                .getChildren()){
            count++;
        }
        return count;
    }

    public static Bitmap getImageBitmap(String url) {
        File imageFile = new File(url);
        FileInputStream fis = null;
        Bitmap bitmap = null;
        try{
            fis = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(fis);
        }catch (FileNotFoundException e){
            Log.e(TAG, "getBitmap: FileNotFoundException: " + e.getMessage() );
        }finally {
            try{
                fis.close();
            }catch (IOException e){
                Log.e(TAG, "getBitmap: FileNotFoundException: " + e.getMessage() );
            }
        }
        return bitmap;
    }



}
