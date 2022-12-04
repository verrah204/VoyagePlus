package io.network.voyageplus.adapter;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


import org.jetbrains.annotations.NotNull;

import io.network.voyageplus.activities.MyApp;

public class ConnectivityReceiver extends BroadcastReceiver {

    public static ConnectivityReceiverListner connectivityReceiverListner;

    public ConnectivityReceiver() {
        super();
    }

    public static boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) MyApp
                .getInstance()
                .getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(@NotNull Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (connectivityReceiverListner != null) {
            connectivityReceiverListner.onNetworkConnectionChanged(isConnected);
        }
    }

    public interface ConnectivityReceiverListner {
        void onNetworkConnectionChanged(boolean isConnected);
    }
}
