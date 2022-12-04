package io.network.voyageplus.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import io.network.voyageplus.MainActivity;
import io.network.voyageplus.R;
import io.network.voyageplus.adapter.ConnectivityReceiver;
import io.network.voyageplus.model.Functions;

public class Noconnection extends AppCompatActivity {

    Button no_net_btn;
    RelativeLayout no_net_main_container;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null) {
            ComponentName receiver = new ComponentName(getApplicationContext(), ConnectivityReceiver.class);
            PackageManager pm = getPackageManager();
            pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
            startActivity(new Intent(getApplicationContext(), ConnexionActivity.class));
        } else {
            setContentView(R.layout.activity_noconnection);
            new Functions().lightstatusbardesign(Noconnection.this);

            no_net_btn = findViewById(R.id.no_net_btn);
            no_net_main_container = findViewById(R.id.no_net_main_container);

            no_net_btn.setOnClickListener(v -> {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}