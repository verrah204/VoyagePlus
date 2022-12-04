package io.network.voyageplus.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import io.network.voyageplus.R;
import io.network.voyageplus.adapter.Comments_adapter;
import io.network.voyageplus.model.Comments;
import io.network.voyageplus.model.Like;
import io.network.voyageplus.model.Photo;

public class PlaceDetails extends AppCompatActivity {
    private static final String TAG = "PlaceDetails";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private StorageReference mStorageReference;
    ImageView pImg, back;
    private ImageView fav_btn;
    TextView pName, pTown, pDesc, pRate, pCat;
    String name, cat, town, desc, rate, photoUrl, photoId;
    String image;
    private Comments_adapter commentAdapter;
    private RecyclerView comment_rec;

    private CoordinatorLayout mplace_main_lay;
    private View mplace_addcomment_lay;
    private EditText mplace_add_comment_box;
    private ImageButton mplace_addcomment_btn;
    private ImageView details_star;


    private FirebaseUser firebaseUser;
    private String user_col_name = "users";
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);
        setupFirebaseAuth();
        Intent i = getIntent();
        Photo photo = i.getParcelableExtra("place_details_bundle");

        details_star = findViewById(R.id.details_star);

        Animation bounce_get = AnimationUtils.loadAnimation(this, R.anim.rotate);
        details_star.startAnimation(bounce_get);



        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        mplace_addcomment_lay = findViewById(R.id.mplace_addcomment_lay);
        mplace_add_comment_box = findViewById(R.id.mplace_add_comment_box);
        mplace_addcomment_btn = findViewById(R.id.mplace_addcomment_btn);
        comment_rec = findViewById(R.id.comment_rec);


        name = photo.getName();
        cat = photo.getCategorie();
        image = photo.getImageUrl();
        rate = photo.getRate();
        town = photo.getTown();
        desc = photo.getDescription();
        photoId = photo.getPhotoId();

        pName = findViewById(R.id.details_name);
        pRate = findViewById(R.id.details_rate);
        pDesc = findViewById(R.id.details_desc);
        pTown = findViewById(R.id.details_town);
        pCat = findViewById(R.id.details_cat);
        pImg = findViewById(R.id.details_image);


        back = findViewById(R.id.back2);
        pName.setText(name);
        pCat.setText(cat);
        pRate.setText(rate);
        pTown.setText(town);
        pDesc.setText(desc);
        // pImg.setImageURI(uri);
        Picasso.get().load(image).placeholder(R.drawable.gradient).into(pImg);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });
        String currUser =FirebaseAuth.getInstance().getCurrentUser().getUid();
        mplace_main_lay = findViewById(R.id.mplace_main_lay);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("photos").child(photoId).child("likes").child(currUser).child("user_id");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.getValue().equals(currUser)) {

                        fav_btn.setImageTintList(ColorStateList.valueOf(Color.RED));
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        setCommentRec();
        mplace_addcomment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addComment();
                recreate();

            }
        });


        fav_btn = findViewById(R.id.fav_btn);
        fav_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            //    Toast.makeText(PlaceDetails.this, photo.getCategorie()+" "+photo.getPhotoId(), Toast.LENGTH_SHORT).show();
                Query query = reference
                        .child("photos").child(photo.getPhotoId())
                        .child(getString(R.string.field_likes))
                        .child(currUser);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //case1: Then user already liked the photo
                            if (dataSnapshot.exists()) {
                                myRef.child(getString(R.string.dbname_photos))
                                        .child(photo.getPhotoId())
                                        .child(getString(R.string.field_likes))
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .removeValue();

                                myRef.child("users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child("likes")
                                        .child(photoId)
                                        .removeValue();

                                fav_btn.setImageTintList(ColorStateList.valueOf(Color.GRAY));
                                Snackbar.make(mplace_main_lay, "Removed from favourites", Snackbar.LENGTH_SHORT).show();

                            }

                            //case2: The user has not liked the photo
                            else  {
                                //add new like
                                addNewLike(photo);
                                Snackbar.make(mplace_main_lay, "Added to favourites", Snackbar.LENGTH_SHORT).show();

                            }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void addNewLike(Photo photo) {
        Log.d(TAG, "addNewLike: adding new like");

        String newLikeID = myRef.push().getKey();
        Like like = new Like();
        like.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());

        myRef.child(getString(R.string.dbname_photos))
                .child(photo.getPhotoId())
                .child(getString(R.string.field_likes))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(like);

        fav_btn.setImageTintList(ColorStateList.valueOf(Color.RED));

        Like userlikes = new Like();
        userlikes.setPhoto_id(photoId);
        myRef.child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("likes")
                .child(photoId)
                .setValue(userlikes);


    }

    private void setCommentRec(){
   //     Log.d(TAG, "bliz:");

        final ArrayList<Comments> comments = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query2 = reference.child("comments")
                .child(photoId).orderByChild("photo_id").equalTo(photoId);
        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.d(TAG, "bliz:"+dataSnapshot);
                    for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                        Comments comment = new Comments();
                        Map<String, Object> objectMap = (Map<String, Object>) singleSnapshot.getValue();

                        try {
                            comment.setDate(objectMap.get("date").toString());
                            comment.setComment_text(objectMap.get("comment_text").toString());
                            comment.setUser_id(objectMap.get("user_id").toString());

                            comments.add(comment);
                            setCommentsrec(comments);
                        }catch(NullPointerException e){
                            Log.e(TAG, "onDataChange: NullPointerException: " + e.getMessage() );
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void addComment() {
        Log.d(TAG, "addNewLike: adding new Coments");

        Comments comments = new Comments();

        comments.setComment_text(mplace_add_comment_box.getText().toString());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM y, hh:mm aa");
        String formattedDate = dateFormat.format(new Date());

        comments.setDate(formattedDate);
        comments.setPhoto_id(photoId);
        comments.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        myRef.child("comments")
                .child(photoId)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(comments);
        Snackbar.make(mplace_main_lay, "Comment Added", Snackbar.LENGTH_SHORT).show();

        mplace_add_comment_box.setText(" ");

    }

    private void setCommentsrec(ArrayList<Comments> modelList) {

        commentAdapter = new Comments_adapter(PlaceDetails.this,modelList);
        comment_rec.setAdapter(commentAdapter);
    }

    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();


                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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
