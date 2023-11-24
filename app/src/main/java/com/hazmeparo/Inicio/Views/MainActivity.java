package com.hazmeparo.Inicio.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hazmeparo.Cuenta.View.CuentaFragment;
import com.hazmeparo.Favores.View.FavoresFragment;
import com.hazmeparo.R;

import java.io.IOException;

import Clases.ApiClient;
import Clases.ApiInterface;
import Clases.PrefsManager;
import Models.Usuario;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.bottomNavigation) BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        createNotificationChannel();
        showSelectedFragment(new SolicitarFragment());

        navigation.setOnNavigationItemSelectedListener(item -> {

            if (item.getItemId() == R.id.navigation_home){
                showSelectedFragment(new SolicitarFragment());
            }

            if (item.getItemId() == R.id.navigation_favores){
                showSelectedFragment(new FavoresFragment());
            }

            if (item.getItemId() == R.id.navigation_cuenta){
                showSelectedFragment(new CuentaFragment());
            }

            return true;
        });

        PreferenceManager.setDefaultValues(this, R.xml.preferencias, false);
    }

    private void showSelectedFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Hazme Paro";
            String description = "Notificaciones Hazme Paro";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1907", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
