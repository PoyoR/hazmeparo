package com.hazmeparo.Registro.Interactor;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hazmeparo.Login.Interface.LoginInteractor;
import com.hazmeparo.Registro.Interface.RegistroInteractor;
import com.hazmeparo.Registro.Interface.RegistroPresenter;

import java.io.IOException;
import java.util.List;

import Clases.ApiClient;
import Clases.ApiInterface;
import Clases.PrefsManager;
import Models.Lugar;
import Models.User;
import Models.Usuario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroInteractorImpl implements RegistroInteractor {

    private RegistroPresenter presenter;
    private ApiInterface api;
    private PrefsManager manager;

    public RegistroInteractorImpl(RegistroPresenter presenter, PrefsManager manager) {
        this.presenter = presenter;
        this.manager = manager;

        api = ApiClient.getClient().create(ApiInterface.class);
    }

    @Override
    public void registrarse(String estado, String municipio, String correo, String direccion, String numero, String pass) {

        if (!direccion.equals("") && !numero.equals("") && !pass.equals("")){

            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (!task.isSuccessful()) {
                                Log.w("FCM", "Fetching FCM registration token failed", task.getException());
                                return;
                            }

                            // Get new FCM registration token
                            String fcm_token = task.getResult();

                            User usuario = new User("", numero, "", "", "", pass, "",
                                    new Usuario(estado, municipio, "", direccion,"","5", "", "", fcm_token), "", "", "");

                            Call<User> call = api.registrarse(usuario);
                            call.enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {

                                    if (response.isSuccessful()){

                                        Call<User> login = api.login(numero, pass);
                                        login.enqueue(new Callback<User>() {
                                            @Override
                                            public void onResponse(Call<User> call, Response<User> response) {
                                                if (response.isSuccessful()){

                                                    User usuario = response.body();

                                                    manager.guardarValorBoolean("logueado", true);
                                                    manager.guardarValorString("id_usuario", usuario.getId());
                                                    manager.guardarValorString("nom_usuario", usuario.getFirstName());
                                                    manager.guardarValorString("apellidos_usuario", usuario.getLastName());
                                                    manager.guardarValorString("username", usuario.getUsername());
                                                    manager.guardarValorString("token", usuario.getToken());

                                                    presenter.registroSuccess();

                                                }else{

                                                }

                                            }

                                            @Override
                                            public void onFailure(Call<User> call, Throwable t) {

                                            }
                                        });


                                    }else{
                                        presenter.registroError();
                                        /*try {
                                            Log.i("ERROR", response.errorBody().string());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }*/
                                    }

                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {
                                    presenter.registroError();
                                }
                            });

                        }
                    });

        }else{

            if (direccion.equals("")){
                presenter.setDireccionError();
            }

            if (numero.equals("") || numero.length() < 10){
                presenter.setNumeroError();
            }

            if (pass.equals("") || pass.length() < 8){
                presenter.setPassError();
            }
        }
    }

    @Override
    public void getEstados() {
        Call<List<Lugar>> call = api.getEstados();

        call.enqueue(new Callback<List<Lugar>>() {
            @Override
            public void onResponse(Call<List<Lugar>> call, Response<List<Lugar>> response) {
                List<Lugar> estados = response.body();

                if (response.isSuccessful()){
                    presenter.setEstados(estados);
                }else{
                    Log.i("Error", "estados");
                }
            }

            @Override
            public void onFailure(Call<List<Lugar>> call, Throwable t) {
                Log.i("Error", "estados");
            }
        });

    }

    @Override
    public void getMunicipios(String estado) {
        Call<List<Lugar>> call = api.getMunicipios(estado);

        call.enqueue(new Callback<List<Lugar>>() {
            @Override
            public void onResponse(Call<List<Lugar>> call, Response<List<Lugar>> response) {
                List<Lugar> municipios = response.body();

                if (response.isSuccessful()){
                    presenter.setMunicipios(municipios);
                }else{
                    Log.i("Error", "municipios");
                }
            }

            @Override
            public void onFailure(Call<List<Lugar>> call, Throwable t) {

            }
        });
    }

}
