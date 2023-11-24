package com.hazmeparo.Inicio.Views;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hazmeparo.Propuestas.View.PropuestasActivity;
import com.hazmeparo.R;

import java.io.IOException;

import Clases.ApiClient;
import Clases.ApiInterface;
import Clases.PrefsManager;
import Models.Servicio;
import Models.Usuario;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SolicitarFragment extends Fragment {

    PrefsManager manager;

    public SolicitarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_solicitar, container, false);

        ButterKnife.bind(this, view);
        manager = new PrefsManager(getActivity());

        //Si hay un favor activo ir a la vista del favor
        if (manager.obtenerValorBoolean("favorActivo")){
            Fragment fragment = new InicioFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        }

        if (manager.obtenerValorBoolean("servicioActivo")){
            Log.i("SI HAY SERVICIO", "SI");
            Fragment fragment = new ServicioActivoFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        }else{
            Log.i("NO HAY SERVICIO", "NO");
        }

        //Actualizar token fcm
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {

                        if (!task.isSuccessful()) {
                            return;
                        }

                        // Get new FCM registration token
                        String fcm_token = task.getResult();

                        Call<Usuario> call = api.updateFcm("/a/usuario_fcm/" + manager.obtenerValorString("id_usuario"),
                                "Token " + manager.obtenerValorString("token"), fcm_token);

                        call.enqueue(new Callback<Usuario>() {
                            @Override
                            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                            }

                            @Override
                            public void onFailure(Call<Usuario> call, Throwable t) {
                            }
                        });


                    }
                });

        return view;
    }

    @OnClick(R.id.imgSolFavor) void goToInicioSolFavor(){
        Fragment fragment = new InicioFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    @OnClick(R.id.imgSolServicio) void goToInicioSolServicio(){
        Fragment fragment = new ServicioFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }
}