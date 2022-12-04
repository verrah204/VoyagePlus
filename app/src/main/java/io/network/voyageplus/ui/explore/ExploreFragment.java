package io.network.voyageplus.ui.explore;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.network.voyageplus.R;
import io.network.voyageplus.activities.AddPlacesActivity;
import io.network.voyageplus.activities.SearchActivity;
import io.network.voyageplus.adapter.Plus_visites_adapter;
import io.network.voyageplus.adapter.Top_du_moment_adapter;
import io.network.voyageplus.databinding.FragmentExploreBinding;
import io.network.voyageplus.model.Functions;
import io.network.voyageplus.model.Photo;
import io.network.voyageplus.utils.FireBaseMethods;

public class ExploreFragment extends Fragment {

    private FragmentExploreBinding binding;
    private SearchView editText;
    private Context context ;
    private static final String TAG = "ExploreFragment";
    private RecyclerView top_du_moment_rec,plus_visites_rec;
    private Top_du_moment_adapter top_du_moment_adapter;
    private Plus_visites_adapter plus_visites_adapter;
    private ImageView add, map ;
    private TextView textView;
    private TextView FtextView;
    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FireBaseMethods mFirebaseMethods;
    CircularProgressIndicator loader;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ExploreViewModel exploreViewModel =
                new ViewModelProvider(this).get(ExploreViewModel.class);
        super.onCreate(savedInstanceState);

        binding = FragmentExploreBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        context = container.getContext();

        ConnectivityManager cm = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork == null || !activeNetwork.isConnected() || !activeNetwork.isAvailable()) {
            new Functions().no_internet_dialog(getActivity());
        }

        plus_visites_rec = binding.plusVisitesRec;
        top_du_moment_rec = binding.topDuMomentRec;
        add = binding.addPlaceImgView;
        textView = binding.FeditText;

        Animation bounce_get = AnimationUtils.loadAnimation(context, R.anim.bounce_anim);
        add.startAnimation(bounce_get);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        editText = binding.editText;
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, SearchActivity.class);
                startActivity(i);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, AddPlacesActivity.class);
                startActivity(i);
            }
        });

        if (!firebaseUser.isAnonymous()) {

            db.collection("users")
                    .document(firebaseUser.getUid())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot != null) {
                                String prof_setting_fullname = documentSnapshot.getString("fullname");

                                if (prof_setting_fullname != null) {
                                   textView.setText(MessageFormat.format("Hey, {0}", prof_setting_fullname ));
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("mprofile_error", "" + e);
                        }
                    });
        }

        // setupFirebaseAuth();
        setupRecView();
        setuplesPlusRecView();
        
        
        return root;
    }


    private void setupRecView(){
        Log.d(TAG, "setupRecView: Setting up image grid.");

        final ArrayList<Photo> photos = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("photos")
                .orderByChild("top_moments").equalTo(true);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i=0;

                for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    Photo photo = new Photo();
                    Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();

                    try {
                        photo.setPhotoId(objectMap.get("photoId").toString());
                        photo.setName(objectMap.get("name").toString());
                        photo.setDescription(objectMap.get("description").toString());
                        photo.setTown(objectMap.get("town").toString());
                        photo.setCategorie(objectMap.get("categorie").toString());
                        photo.setRate(objectMap.get("rate").toString());
                        photo.setImageUrl(objectMap.get("imageUrl").toString());

                        photos.add(photo);
                        setTop_du_moment_rec(photos);
                    }catch(NullPointerException e){
                        Log.e(TAG, "onDataChange: NullPointerException: " + e.getMessage() );
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: query cancelled.");
            }
        });
    }

    private void setuplesPlusRecView(){
        Log.d(TAG, "setuplesPlusRecView: Setting up image grid.");

        final ArrayList<Photo> photos = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("photos")
                .orderByChild("plus_visites").equalTo(true);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i=0;

                for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    Photo photo = new Photo();
                    Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();

                    try {
                        photo.setPhotoId(objectMap.get("photoId").toString());
                        photo.setName(objectMap.get("name").toString());
                        photo.setDescription(objectMap.get("description").toString());
                        photo.setTown(objectMap.get("town").toString());
                        photo.setCategorie(objectMap.get("categorie").toString());
                        photo.setRate(objectMap.get("rate").toString());
                        photo.setImageUrl(objectMap.get("imageUrl").toString());

                        photos.add(photo);
                        i++;

                        setLes_plus_visites_rec(photos);
                    }catch(NullPointerException e){
                        Log.e(TAG, "onDataChange: NullPointerException: " + e.getMessage() );
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: query cancelled.");
            }
        });
    }

    private void setLes_plus_visites_rec(ArrayList<Photo> modelList) {

        Log.d(TAG, "La taille "+String.valueOf(modelList.size()));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        plus_visites_rec.setLayoutManager(layoutManager);
        plus_visites_adapter = new Plus_visites_adapter(context,modelList);
        plus_visites_rec.setAdapter(plus_visites_adapter);
    }

    private void setTop_du_moment_rec(ArrayList<Photo> modelList) {
        Log.d(TAG, "La taille "+String.valueOf(modelList.size()));

        top_du_moment_adapter = new Top_du_moment_adapter(context,modelList);
        top_du_moment_rec.setAdapter(top_du_moment_adapter);
    }



    private void setupFirebaseAuth(){
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

                //retrieve user information from the database

                //retrieve images for the user in question

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}