package io.network.voyageplus.ui.person;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.Date;

import io.network.voyageplus.LocalDB.AnalyticDBHelper;
import io.network.voyageplus.LocalDB.RecentPlaceDBHelper;
import io.network.voyageplus.R;
import io.network.voyageplus.activities.AboutusActivity;
import io.network.voyageplus.activities.AccountsettingsActivity;
import io.network.voyageplus.activities.ConnexionActivity;
import io.network.voyageplus.activities.MainSettingsActivity;
import io.network.voyageplus.activities.ProfilesettingsActivity;
import io.network.voyageplus.activities.TermsCondnActivity;
import io.network.voyageplus.databinding.FragmentPersonBinding;
import io.network.voyageplus.model.Functions;

public class PersonFragment extends Fragment {


    private FragmentPersonBinding binding;
    private TextView  prof_joined;
    private ImageButton logout;
    private LinearLayout prof_get_registered_lay, prof_person_settings, prof_acc_settings, prof_app_settings, prof_aboutus, prof_privacy;
    private LinearLayout info_settings;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private GoogleSignInClient googleSignInClient;
    private FirebaseUserMetadata metadata;
    private ImageButton logoutBtn;

    public void Personfragment() {
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        new Functions().checkTheme(requireContext());
        View view = inflater.inflate(R.layout.fragment_person, container, false);

        profileFragInits(view);
        uname_jdateMethod();  //Username & joined Date
        profileFragEventListnerMethods();
        logoutBtn = view.findViewById(R.id.logout);


        return view;
    }

    private void profileFragInits(@NotNull View view) {

        prof_get_registered_lay = view.findViewById(R.id.prof_get_registered_lay);
        prof_joined = view.findViewById(R.id.prof_joined);
        info_settings = view.findViewById(R.id.info_settings);
        prof_person_settings = view.findViewById(R.id.prof_person_settings);
        prof_acc_settings = view.findViewById(R.id.prof_acc_settings);
        prof_app_settings = view.findViewById(R.id.prof_app_settings);
        prof_aboutus = view.findViewById(R.id.prof_aboutus);
        prof_privacy = view.findViewById(R.id.prof_privacy);
        logout = view.findViewById(R.id.logout);
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        metadata = firebaseUser.getMetadata();
    }

    private void uname_jdateMethod() {

        if (!firebaseUser.isAnonymous()) {

            db.collection("users")
                    .document(firebaseUser.getUid())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot != null) {
                                String prof_setting_emailid = documentSnapshot.getString("emailid");

                                if (prof_setting_emailid != null) {
                                    prof_joined.setText( prof_setting_emailid);
                                } else {
                                    prof_joined.setText("aaa@gmail.com");
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

    }

    private void profileFragEventListnerMethods() {

        prof_app_settings.setOnClickListener(v -> startActivity(new Intent(getContext(), MainSettingsActivity.class)));

        logout.setOnClickListener(v -> {

            if (firebaseUser != null) {
                if (firebaseUser.isAnonymous()) {

                    SharedPreferences.Editor editor = requireContext().getSharedPreferences("app_data", Context.MODE_PRIVATE).edit();
                    editor.putBoolean("log_status", false);
                    editor.putBoolean("isUser", false);
                    editor.putBoolean("isGoogle", false);
                    editor.apply();

                    db.collection("users")
                            .document(firebaseUser.getUid())
                            .collection("favourites")
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.getResult() != null) {
                                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                        documentSnapshot.getReference().delete();
                                    }
                                }
                            });

                    new Handler().postDelayed(() -> db.collection("users")
                            .document(firebaseUser.getUid())
                            .delete()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {

                                    SharedPreferences.Editor user_prefs1 = requireContext().getSharedPreferences("user_data", Activity.MODE_PRIVATE).edit();
                                    user_prefs1.clear();
                                    user_prefs1.apply();

                                    verify();
                                    firebaseAuth.signOut();
                                    firebaseUser.delete();
                                    new RecentPlaceDBHelper(getContext()).deleteRecentPlace();
                                    new AnalyticDBHelper(getContext()).deleteAllRecentPlaces();
                                    startActivity(new Intent(getContext(), ConnexionActivity.class));
                                } else {
                                    Toast.makeText(getContext(), "Failed to Sign-out...!!", Toast.LENGTH_SHORT).show();
                                }
                            }), 500);
                } else {

                    SharedPreferences.Editor editor = requireContext().getSharedPreferences("app_data", Context.MODE_PRIVATE).edit();
                    editor.putBoolean("log_status", false);
                    editor.putBoolean("isGoogle", false);
                    editor.apply();

                    verify();
                    firebaseAuth.signOut();
                    creategooglerequest();
                    googleSignInClient.signOut();
                    new RecentPlaceDBHelper(getContext()).deleteRecentPlace();
                    new AnalyticDBHelper(getContext()).deleteAllRecentPlaces();
                    startActivity(new Intent(getContext(), ConnexionActivity.class));
                }
            }
        });

        prof_person_settings.setOnClickListener(v -> startActivity(new Intent(getContext(), ProfilesettingsActivity.class)));

        prof_acc_settings.setOnClickListener(v -> startActivity(new Intent(getContext(), AccountsettingsActivity.class)));

        prof_privacy.setOnClickListener(v -> startActivity(new Intent(getContext(), TermsCondnActivity.class)));

        prof_aboutus.setOnClickListener(v -> startActivity(new Intent(getContext(), AboutusActivity.class)));


    }

    private void verify() {
        FirebaseAuth.AuthStateListener mAuthStateListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseuser = firebaseAuth.getCurrentUser();
                if (mFirebaseuser == null) {
                    Toast.makeText(getContext(), "You have already logged out", Toast.LENGTH_SHORT).show();

                    Intent in7 = new Intent(getContext(), ConnexionActivity.class);
                    startActivity(in7);
                }
            }
        };
    }

    private void creategooglerequest() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}