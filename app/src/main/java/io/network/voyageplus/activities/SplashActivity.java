package io.network.voyageplus.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import io.network.voyageplus.MainActivity;
import io.network.voyageplus.R;
import io.network.voyageplus.databinding.ActivitySplashBinding;
import io.network.voyageplus.onboarding.OnBoardingMainActivity;

public class SplashActivity extends AppCompatActivity{

    int SPLASH_TIME = 3500; //This is 3.5 seconds

    private Boolean intro_check, log_check, sign_status;
    private FirebaseAuth firebaseAuth;
    private TextView text1,text2;

    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        text1 = (TextView) findViewById(R.id.text1);

        Animation bounce_get = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce_anim);
        text1.startAnimation(bounce_get);


        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            SharedPreferences.Editor editor = getSharedPreferences("app_data", Context.MODE_PRIVATE).edit();
            editor.putBoolean("isUser", false);
            editor.apply();

        }

        SharedPreferences prefs_1 = getSharedPreferences("app_data", Activity.MODE_PRIVATE);
        intro_check = prefs_1.getBoolean("intro", false);
        log_check = prefs_1.getBoolean("log_status", false);
        sign_status = prefs_1.getBoolean("sign_status", false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (firebaseAuth.getCurrentUser() != null) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();

                } else  {
                    Log.e("splash_to_page", "To Enterdetails Page");
                    startActivity(new Intent(getApplicationContext(), OnBoardingMainActivity.class));
                    finish();
                }
            }

            }, SPLASH_TIME);

    }


}