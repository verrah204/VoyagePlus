package io.network.voyageplus.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import io.network.voyageplus.R;
import io.network.voyageplus.utils.FireBaseMethods;

public class AddPlacesActivity extends AppCompatActivity {
    private EditText placeName,placeDescription,placeRate;
    AutoCompleteTextView placeTown, placeCategorie ;
    private String name;
    private String town;
    private String desc;
    private String cat;
    private String rate;
    private String Url;
    private Button savePlace,loadImg;
    private ImageView placeImg ;

    private static final String TAG = "NextActivity";
    int SELECT_PICTURE = 200;
    private Context mContext=AddPlacesActivity.this;
    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FireBaseMethods mFirebaseMethods;

    private static Uri ImageUri;
    private static final String TAG2 = "GetImgUriActivity";

    //widgets
    private EditText mCaption;
    private int mFollowersCount = 3;


    //vars
    private String mAppend = "file:/";
    private int imageCount = 1;
    private String imgUrl;
    private Bitmap bitmap;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_places);
        // get the Firebase  storage reference
        mFirebaseMethods = new FireBaseMethods(AddPlacesActivity.this);
        setupFirebaseAuth();
        placeName = findViewById(R.id.place_name);
        placeTown = findViewById(R.id.place_town);
        placeCategorie = findViewById(R.id.place_categorie);
        placeDescription = findViewById(R.id.place_desc);
        placeRate = findViewById(R.id.place_rate);
        savePlace=findViewById(R.id.save_place);
        loadImg=findViewById(R.id.load_img);
        placeImg=findViewById(R.id.place_img);


        loadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });

        savePlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, String.valueOf(getImageUriFromHere()));

                name = placeName.getText().toString();
                town = String.valueOf(placeTown.getText());
                desc = String.valueOf(placeDescription.getText());
                cat = String.valueOf(placeCategorie.getText());
                rate = String.valueOf(placeRate.getText());
                imgUrl = String.valueOf(getImageUriFromHere());
               // Toast.makeText(this,String.valueOf(getImageUriFromHere()),Toast.LENGTH_LONG).show();
                mFirebaseMethods.uploadPhoto(name, desc, town, cat, rate,imageCount,imgUrl,null);

            }
        });

    }

    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                ImageUri = data.getData();
                if (null != ImageUri) {
                    // update the preview image in the layout
                    placeImg.setImageURI(ImageUri);
                }

            }
        }
    }

    public static Uri getImageUriFromHere() {
        return ImageUri ;
    }


    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        Log.d(TAG, "onDataChange: image count: " + imageCount);

        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();


            if (user != null) {
                // User is signed in
                Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
            } else {
                // User is signed out
                Log.d(TAG, "onAuthStateChanged:signed_out");
            }
            // ...
        };


        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //retrieve user information from the database
                imageCount = mFirebaseMethods.getImageCount(dataSnapshot);
                Log.d(TAG, "onDataChange: image count: " + imageCount);
                Log.d(TAG, "Everthing is okvbn !!!");


                //retrieve images for the user in question

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


}