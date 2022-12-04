package io.network.voyageplus.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import io.network.voyageplus.activities.Loginfragment;
import io.network.voyageplus.activities.Signupfragment;

@SuppressWarnings("all")
public class LogsignPageAdapter extends FragmentPagerAdapter {

    Context context;
    int totalTabs;

    public LogsignPageAdapter(@NonNull FragmentManager fm, Context context, int totalTabs) {
        super(fm);
        this.context = context;
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Loginfragment();
            case 1:
                return new Signupfragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
