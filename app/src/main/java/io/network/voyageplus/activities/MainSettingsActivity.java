package io.network.voyageplus.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;

import io.network.voyageplus.LocalDB.AnalyticDBHelper;
import io.network.voyageplus.LocalDB.RecentPlaceDBHelper;
import io.network.voyageplus.MainActivity;
import io.network.voyageplus.R;
import io.network.voyageplus.model.Functions;

public class MainSettingsActivity extends AppCompatActivity {

    Functions functions = new Functions();
    private ImageButton main_settings_back_btn;
    private View main_settings_noti_lay, main_settings_dark_lay, main_settings_appcache_lay,
            main_settings_news_lay,  main_settings_invite_lay;
    private SwitchCompat main_settings_notification_switch;
    private View main_settings_lay;

    private AnalyticDBHelper analyticDBHelper;
    private RecentPlaceDBHelper recentPlaceDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_settings);

        mainSettingsInits();
        mainSettingsOnClickEvents();

    }

    private void mainSettingsInits() {
        functions.lightstatusbardesign(MainSettingsActivity.this);
        main_settings_lay = findViewById(R.id.main_settings_lay);
        main_settings_back_btn = findViewById(R.id.main_settings_back_btn);
        main_settings_noti_lay = findViewById(R.id.main_settings_noti_lay);
        main_settings_notification_switch = findViewById(R.id.main_settings_notification_switch);
        main_settings_dark_lay = findViewById(R.id.main_settings_dark_lay);
        main_settings_appcache_lay = findViewById(R.id.main_settings_appcache_lay);
        main_settings_news_lay = findViewById(R.id.main_settings_news_lay);
        main_settings_invite_lay = findViewById(R.id.main_settings_invite_lay);
        analyticDBHelper = new AnalyticDBHelper(getApplicationContext());
        recentPlaceDBHelper = new RecentPlaceDBHelper(getApplicationContext());

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null && firebaseAuth.getCurrentUser().isAnonymous()) {
            main_settings_noti_lay.setVisibility(View.GONE);
        }

        SharedPreferences prefs = getSharedPreferences("app_data", Activity.MODE_PRIVATE);
        boolean notifi_check = prefs.getBoolean("notification_check", true);
        main_settings_notification_switch.setChecked(notifi_check);
    }

    private void mainSettingsOnClickEvents() {

        main_settings_back_btn.setOnClickListener(v -> {
            finish();
        });

        main_settings_noti_lay.setOnClickListener(v -> {

            if (main_settings_notification_switch.isChecked()) {
                SharedPreferences.Editor editor = getSharedPreferences("app_data", Context.MODE_PRIVATE).edit();
                editor.putBoolean("notification_check", false);
                editor.apply();
                main_settings_notification_switch.setChecked(false);
                Snackbar.make(main_settings_lay, "Notification Disabled", Snackbar.LENGTH_SHORT).show();
            } else {
                SharedPreferences.Editor editor = getSharedPreferences("app_data", Context.MODE_PRIVATE).edit();
                editor.putBoolean("notification_check", true);
                editor.apply();
                main_settings_notification_switch.setChecked(true);
                Snackbar.make(main_settings_lay, "Notification Enabled", Snackbar.LENGTH_SHORT).show();
            }
        });

        main_settings_notification_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {

            SharedPreferences.Editor editor = getSharedPreferences("app_data", Context.MODE_PRIVATE).edit();
            editor.putBoolean("notification_check", isChecked);
            editor.apply();
            if (isChecked) {
                Snackbar.make(main_settings_lay, "Notification Enabled", Snackbar.LENGTH_SHORT).show();

            } else {
                Snackbar.make(main_settings_lay, "Notification Disabled", Snackbar.LENGTH_SHORT).show();
            }
        });

        main_settings_dark_lay.setOnClickListener(v -> {

            final Dialog dialog = new Dialog(MainSettingsActivity.this);
            dialog.setContentView(R.layout.theme_dialog);
            dialog.setCanceledOnTouchOutside(true);
            RadioGroup color_group = dialog.findViewById(R.id.theme_dialog_group);
            TextView cancel = dialog.findViewById(R.id.theme_dialog_cancel);

            SharedPreferences prefs = getSharedPreferences("app_data", Context.MODE_PRIVATE);
            int check_color = prefs.getInt("get_theme", 0);

            {
                if (check_color == 0) {
                    color_group.check(R.id.theme_dialog_light);
                } else if (check_color == 1) {
                    color_group.check(R.id.theme_dialog_dark);
                } else if (check_color == 2) {
                    color_group.check(R.id.theme_dialog_system);
                } else {
                    color_group.check(R.id.theme_dialog_light);
                }
            }

            {
                color_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (checkedId == R.id.theme_dialog_light) {
                            SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("app_data", Context.MODE_PRIVATE).edit();
                            editor.putInt("get_theme", 0);
                            editor.apply();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                    startActivity(new Intent(getApplicationContext(), MainSettingsActivity.class));
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                }
                            }, 500);
                        } else if (checkedId == R.id.theme_dialog_dark) {
                            SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("app_data", Context.MODE_PRIVATE).edit();
                            editor.putInt("get_theme", 1);
                            editor.apply();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                    startActivity(new Intent(getApplicationContext(), MainSettingsActivity.class));
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                }
                            }, 500);
                        } else if (checkedId == R.id.theme_dialog_system) {
                            SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("app_data", Context.MODE_PRIVATE).edit();
                            editor.putInt("get_theme", 2);
                            editor.apply();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                    startActivity(new Intent(getApplicationContext(), MainSettingsActivity.class));
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                }
                            }, 500);
                        }
                    }
                });
            }

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        });

        main_settings_appcache_lay.setOnClickListener(v -> {
            try {
                File dir = getApplication().getCacheDir();
                if (deleteDir(dir)) {
                    Snackbar.make(v, "App cache cleared.......", Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(v, "App cache not cleared !!!", Snackbar.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });



        main_settings_news_lay.setOnClickListener(v -> {
            new Functions().whats_new_dialog(MainSettingsActivity.this, 1);
        });


        main_settings_invite_lay.setOnClickListener(v -> {
            Intent intent1 = new Intent(Intent.ACTION_SEND);
            intent1.setType("text/plain");
            String body = "Let me recommend you this application\n\nVoyage plus ... soon on playStore";
            intent1.putExtra(Intent.EXTRA_TEXT, body);
            startActivity(Intent.createChooser(intent1, "Share using"));
        });

    }

    @NonNull
    private Boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : children) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("from_page", "AccountsettingsActivity");
        startActivity(intent);
    }
}