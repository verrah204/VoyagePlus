package io.network.voyageplus.activities;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;

import io.network.voyageplus.R;
import io.network.voyageplus.model.Functions;

public class AboutusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_aboutus);

        ImageButton abt_back_btn = findViewById(R.id.abt_back_btn);
        LottieAnimationView abt_anim1 = findViewById(R.id.abt_anim1);
        LottieAnimationView abt_anim2 = findViewById(R.id.abt_anim2);

        abt_anim1.setRepeatCount(LottieDrawable.INFINITE);
        abt_anim2.setRepeatCount(LottieDrawable.INFINITE);

        abt_back_btn.setOnClickListener(v -> finish());
    }
}