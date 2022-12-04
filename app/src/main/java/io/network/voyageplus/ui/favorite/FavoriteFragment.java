package io.network.voyageplus.ui.favorite;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.network.voyageplus.R;
import io.network.voyageplus.adapter.Fav_fragment_adapter;
import io.network.voyageplus.adapter.Plus_visites_adapter;
import io.network.voyageplus.databinding.FragmentFavoriteBinding;
import io.network.voyageplus.model.Functions;
import io.network.voyageplus.model.Photo;

public class FavoriteFragment extends Fragment {

    private FragmentFavoriteBinding binding;
    private static final String TAG = "FavoriteFragment";
    private Context context;
    private RecyclerView fav_rec;
    private ImageButton rm_fav_btn;
    private LinearLayout no_fav_yet;
    private SwipeRefreshLayout favfrag_swipe_refresh;

    private Fav_fragment_adapter fav_fragment_adapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        
        binding = FragmentFavoriteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        super.onCreate(savedInstanceState);

        context = container.getContext();


        fav_rec = binding.favRec;
        no_fav_yet = binding.noFavYet;
        rm_fav_btn = root.findViewById(R.id.rm_fav_btn);

        favfrag_swipe_refresh = root.findViewById(R.id.favfrag_swipe_refresh);

        favfrag_swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setupFavRecView();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        favfrag_swipe_refresh.setRefreshing(false);
                    }
                }, 1000);
            }
        });
        setupFavRecView();

       return root;
    }

    private void setupFavRecView(){
        Log.d(TAG, "setup_fav_RecView: Setting up image grid.");
        String currUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final ArrayList<Photo> photos = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("users").child(currUser).child("likes");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i=0;
                if (dataSnapshot.exists()) {
                    no_fav_yet.setVisibility(View.GONE);
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        String photoid = singleSnapshot.getKey().toString();
                        Log.d(TAG,"Snapchat : "+photoid);

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                        Query query2 = ref.child("photos").orderByChild("photoId").equalTo(photoid);
                        query2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {

                                    for (DataSnapshot singlesSnapshot : dataSnapshot.getChildren()) {
                                        Log.d(TAG,"Snapchat..: "+singlesSnapshot);

                                        Photo likedPhoto = new Photo();
                                        Map<String, Object> objectMap = (HashMap<String, Object>) singlesSnapshot.getValue();

                                        try {
                                            likedPhoto.setPhotoId(objectMap.get("photoId").toString());
                                            likedPhoto.setName(objectMap.get("name").toString());
                                            likedPhoto.setDescription(objectMap.get("description").toString());
                                            likedPhoto.setTown(objectMap.get("town").toString());
                                            likedPhoto.setCategorie(objectMap.get("categorie").toString());
                                            likedPhoto.setRate(objectMap.get("rate").toString());
                                            likedPhoto.setImageUrl(objectMap.get("imageUrl").toString());

                                            photos.add(likedPhoto);
                                            setFav_rec(photos);


                                        }
                                        catch (NullPointerException e) {
                                            Log.e(TAG, "onDataChange: NullPointerException: " + e.getMessage());
                                        }


                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: query cancelled.");
            }
        });
    }

    private void setFav_rec(ArrayList<Photo> modelList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        fav_rec.setLayoutManager(layoutManager);
        fav_fragment_adapter = new Fav_fragment_adapter(context,modelList);
        fav_rec.setAdapter(fav_fragment_adapter);
        fav_fragment_adapter.notifyDataSetChanged();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}