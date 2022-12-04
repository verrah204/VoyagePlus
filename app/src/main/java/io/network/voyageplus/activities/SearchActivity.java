package io.network.voyageplus.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
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
import com.google.firebase.firestore.auth.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.network.voyageplus.Data.TripList;
import io.network.voyageplus.LocalDB.AnalyticDBHelper;
import io.network.voyageplus.R;
import io.network.voyageplus.adapter.AllCategoryAdapter;
import io.network.voyageplus.adapter.PhotoListAdapter;
import io.network.voyageplus.adapter.Search_item_adapter;
import io.network.voyageplus.adapter.Top_du_moment_adapter;
import io.network.voyageplus.model.AllCategoryModel;
import io.network.voyageplus.model.Photo;
import io.network.voyageplus.model.SearchItemModel;
import io.network.voyageplus.utils.FireBaseMethods;

public class SearchActivity extends AppCompatActivity {

    private ImageView back ;

    private ListView s_searchresult_rec;
    private Search_item_adapter search_item_adapter;
    private EditText s_search_box_input;
    private ChipGroup chip_group ;
    private FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FireBaseMethods mFirebaseMethods;
    private LinearLayout s_searchresult_lay;

    private ArrayList<Photo> photos;
    private static final String TAG = "SearchActivity";

    private final TextWatcher search_textwatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String search_text = s_search_box_input.getText().toString();

            if (!search_text.isEmpty() && search_text.matches("^[^\\s]+[-a-zA-Z\\s]+([-a-zA-Z^[^ \\n]]+)*$")) {

                Drawable rightSideDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_close);
                if (rightSideDrawable != null) {
                    rightSideDrawable.setColorFilter(getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_IN);
                }
                s_search_box_input.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, rightSideDrawable, null);
                s_search_box_input.setOnTouchListener((v, event) -> {

                    Drawable rightdrawable = s_search_box_input.getCompoundDrawables()[2];
                    if (rightdrawable != null) {
                        if (event.getRawX() >= (s_search_box_input).getRight() - s_search_box_input.getCompoundDrawables()[2].getBounds().width() - 30) {
                            s_search_box_input.setText("");
                            return true;
                        }
                    }
                    return false;
                });

                s_searchresult_lay.setVisibility(View.VISIBLE);
            } else {
                s_searchresult_lay.setVisibility(View.GONE);
                s_search_box_input.setCompoundDrawables(null, null, null, null);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        back = findViewById(R.id.s_search_close);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        s_searchresult_rec = findViewById(R.id.s_searchresult_rec);
        s_search_box_input = findViewById(R.id.s_search_box_input);
        s_searchresult_lay = findViewById(R.id.s_searchresult_lay);
   //     s_search_box_input.addTextChangedListener(search_textwatcher);

        Chip chip1 = findViewById(R.id.chip1);
        Chip chip2 = findViewById(R.id.chip2);
        Chip chip3 = findViewById(R.id.chip3);
        Chip chip4 = findViewById(R.id.chip4);
        Chip chip5 = findViewById(R.id.chip5);
        Chip chip6 = findViewById(R.id.chip6);
        Chip chip7 = findViewById(R.id.chip7);
        Chip chip8 = findViewById(R.id.chip8);
        Chip chip9 = findViewById(R.id.chip9);
        Chip chip10 = findViewById(R.id.chip10);
        Chip chip11 = findViewById(R.id.chip11);
        Chip chip12 = findViewById(R.id.chip12);
        Chip chip13 = findViewById(R.id.chip13);
        Chip chip14 = findViewById(R.id.chip14);
        Chip chip15 = findViewById(R.id.chip15);
        Chip chip16 = findViewById(R.id.chip16);

        clickOnChip(chip1);
        clickOnChip(chip2);
        clickOnChip(chip3);
        clickOnChip(chip4);
        clickOnChip(chip5);
        clickOnChip(chip6);
        clickOnChip(chip7);
        clickOnChip(chip8);
        clickOnChip(chip9);
        clickOnChip(chip10);
        clickOnChip(chip11);
        clickOnChip(chip12);
        clickOnChip(chip13);
        clickOnChip(chip14);
        clickOnChip(chip15);
        clickOnChip(chip16);


        hideSoftKeyboard();
        initTextListener();

    }

    private void clickOnChip(Chip chip){
        chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s_search_box_input.setText(chip.getText());

            }
        });
    }

    private void initTextListener(){
      //  Toast.makeText(this, "initTextListener", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "initTextListener: initializing");



        photos = new ArrayList<>();

        s_search_box_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String text = s_search_box_input.getText().toString();
                searchForMatch(text);
            }
        });
    }

    private void searchForMatch(String keyword){
     //   Toast.makeText(this, "SearchForMatch", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "searchForMatch: searching for a match: " + keyword);
        photos.clear();

        //update the users list view
        if(keyword.length() ==0){

        }else{
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            Query query = reference.child("photos")
                    .orderByChild("town").equalTo(keyword.toLowerCase());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){

//                        Log.d(TAG, "onDataChange: found place:" + singleSnapshot.getValue(Photo.class).toString());

                            photos.add(singleSnapshot.getValue(Photo.class));
                            Log.d(TAG,"come: "+String.valueOf(photos.size()));
                            updatePlacesList();

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    private void updatePlacesList(){
      //  Toast.makeText(this, "updatePlacesList", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "updateUsersList: updating users list");

        search_item_adapter = new Search_item_adapter(SearchActivity.this,R.layout.search_items, photos);

        s_searchresult_rec.setAdapter(search_item_adapter);

        s_searchresult_rec.setOnItemClickListener((parent, view, position, id) -> {
            Log.d(TAG, "onItemClick: selected place: " + photos.get(position).toString());

            //navigate to profile activity
            Intent intent =  new Intent(SearchActivity.this, PlaceDetails.class);
            intent.putExtra("calling_activity", "SearchActivity");
            intent.putExtra("place_details_bundle", photos.get(position));
            startActivity(intent);
        });
    }


    private void hideSoftKeyboard(){
        if(getCurrentFocus() != null){
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}

