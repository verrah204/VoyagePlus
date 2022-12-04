package io.network.voyageplus.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import io.network.voyageplus.MainActivity;
import io.network.voyageplus.R;
import io.network.voyageplus.model.Functions;

public class ProfilesettingsActivity extends AppCompatActivity {

    MainActivity mainActivity = new MainActivity();
    private NestedScrollView mprofile_nest_lay;
    private AppBarLayout mprofile_appbarlay;
    private Toolbar mprofile_toolbar;
    private TextView mprofile_toolbar_title;
    private ImageButton mprofile_back_btn;
    private LinearLayout mprofile_add_each_prefs;
    private FloatingActionButton mprofile_edit_btn;
    private TextView mprofile_fullname, mprofile_gender, mprofile_dob, mprofile_address, mprofile_contactno, mprofile_emailid;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Functions().checkTheme(getApplicationContext());
        setContentView(R.layout.activity_profilesettings);

        {
            mprofile_nest_lay = findViewById(R.id.mprofile_nest_lay);
            mprofile_back_btn = findViewById(R.id.mprofile_back_btn);
            mprofile_edit_btn = findViewById(R.id.mprofile_edit_btn);

            mprofile_fullname = findViewById(R.id.mprofile_fullname);
            mprofile_gender = findViewById(R.id.mprofile_gender);
            mprofile_dob = findViewById(R.id.mprofile_dob);
            mprofile_address = findViewById(R.id.mprofile_address);
            mprofile_contactno = findViewById(R.id.mprofile_contactno);
            mprofile_emailid = findViewById(R.id.mprofile_emailid);
            firebaseAuth = FirebaseAuth.getInstance();
            db = FirebaseFirestore.getInstance();
            firebaseUser = firebaseAuth.getCurrentUser();

        }  //Initializations

        {
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
                                    String prof_setting_gender = documentSnapshot.getString("gender");
                                    String prof_setting_dob = documentSnapshot.getString("dob");
                                    String prof_setting_address = documentSnapshot.getString("fulladdress");
                                    String prof_setting_contactno = documentSnapshot.getString("contact_number");
                                    String prof_setting_emailid = documentSnapshot.getString("emailid");

                                    if (prof_setting_fullname != null) {
                                        mprofile_fullname.setText(prof_setting_fullname);
                                    } else {
                                        mprofile_fullname.setText("(Not Added)");
                                    }

                                    if (prof_setting_gender != null) {
                                        mprofile_gender.setText(prof_setting_gender);
                                    } else {
                                        mprofile_gender.setText("(Not Added)");
                                    }

                                    if (prof_setting_dob != null) {
                                        mprofile_dob.setText(prof_setting_dob);
                                    } else {
                                        mprofile_dob.setText("(Not Added)");
                                    }

                                    if (prof_setting_address != null) {
                                        mprofile_address.setText(prof_setting_address);
                                    } else {
                                        mprofile_address.setText("(Not Added)");
                                    }

                                    if (prof_setting_contactno != null) {
                                        mprofile_contactno.setText(prof_setting_contactno);
                                    } else {
                                        mprofile_contactno.setText("(Not Added)");
                                    }

                                    if (prof_setting_emailid != null) {
                                        mprofile_emailid.setText(prof_setting_emailid);
                                    } else {
                                        mprofile_emailid.setText("(Not Added)");
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

        }  //Data displaying

        mprofile_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mprofile_edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), EditProfileActivity.class));
            }
        });

    }




}